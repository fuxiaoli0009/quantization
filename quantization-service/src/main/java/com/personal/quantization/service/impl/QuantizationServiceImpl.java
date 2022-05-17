package com.personal.quantization.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.personal.quantization.center.api.QuantizationCenterClient;
import com.personal.quantization.chain.AddPreChain;
import com.personal.quantization.constant.Constants;
import com.personal.quantization.enums.ColumnEnum;
import com.personal.quantization.enums.QuantizationSourceEnum;
import com.personal.quantization.mapper.QuantizationMapper;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationHistoryDetail;
import com.personal.quantization.model.QuantizationIndexValues;
import com.personal.quantization.model.QuantizationInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.model.QuantizationValue;
import com.personal.quantization.model.QuantizationValueDetail;
import com.personal.quantization.service.QuantizationService;
import com.personal.quantization.utils.DateUtils;
import com.personal.quantization.utils.QuantizationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuantizationServiceImpl implements QuantizationService, InitializingBean {
	
	private static final String QUANTIZATION_SOURCE = "quantization_source";
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	QuantizationCenterClient centerClient;
	
	@Autowired
	private QuantizationMapper quantizationMapper;
	
	@Autowired
	private AddPreChain addPreChain;
	
	@Resource(name = "myDataThreadPool")
	private ExecutorService executorService;
	
	@Value("${spring.profiles.active}")
	private String PROFILE_ACTIVE;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    	executor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
			}
			
    	}, 0, 3, TimeUnit.MINUTES);
	}
	
	@Override
	public String selectQuantizationsBySource(String sources) {
		Map<String, Object> maps = new HashMap<String, Object>();
		if(!StringUtils.isEmpty(sources)) {
			List<QuantizationDetailInfo> quantizations = quantizationMapper.queryQuantizationsBySelectedStatus(Arrays.asList(sources.split(",")));
			List<QuantizationRealtimeInfo> quantizationRealTimes = getQuantizationRealtimeInfo(quantizations, sources);
			long start2 = System.currentTimeMillis();
			Collections.sort(quantizationRealTimes);
			maps.put("result", quantizationRealTimes);
			log.info("查询数据耗时排序：{}", System.currentTimeMillis() - start2);
		}
		return JSON.toJSONString(maps);
	}

	@Override
	public String queryQuantizationsBySelectedStatus() {
		
		Map<String, Object> maps = new HashMap<String, Object>();
		List<String> source = new ArrayList<>();
		source.add(QuantizationSourceEnum.QUANTIZATION_SOURCE_SH.getSource());
		source.add(QuantizationSourceEnum.QUANTIZATION_SOURCE_SZ.getSource());
		List<QuantizationDetailInfo> quantizations = quantizationMapper.queryQuantizationsBySelectedStatus(source);
		List<QuantizationRealtimeInfo> hsQuantizations = getQuantizationRealtimeInfo(quantizations, QuantizationSourceEnum.QUANTIZATION_SOURCE_SH.getSource()+QuantizationSourceEnum.QUANTIZATION_SOURCE_SZ.getSource());
		long start2 = System.currentTimeMillis();
		if("dev".equals(PROFILE_ACTIVE)) {
			List<QuantizationInfo> infos = new ArrayList<>();
			for(QuantizationRealtimeInfo realtimeInfo : hsQuantizations) {
				QuantizationInfo info = new QuantizationInfo();
				info.setQuantizationCode(Integer.valueOf(realtimeInfo.getQuantizationCode()));
				info.setQuantizationName(realtimeInfo.getQuantizationName());
				infos.add(info);
			}
			executorService.execute(() -> quantizationMapper.insertQuantizationInfo(infos));
			executorService.execute(() -> saveQuantizationInfosByPipeline(infos));
		}
		Collections.sort(hsQuantizations);
		maps.put("result", hsQuantizations);
		log.info("查询数据耗时：{}", System.currentTimeMillis() - start2);
		return JSON.toJSONString(maps);
	}
	
	public void saveQuantizationInfosByPipeline(List<QuantizationInfo> infos) { 
		
		long start = System.currentTimeMillis() ; 
		redisTemplate.executePipelined(new RedisCallback<Object>(){
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				for(QuantizationInfo info : infos) {
					connection.setEx(("q_"+info.getQuantizationCode()).getBytes(), 60, JSON.toJSONString(info).getBytes());
				}
				return null;
			}
		});
		log.info("QuantizationInfo数据通过管道存入redis，耗时:{}", (System.currentTimeMillis() - start) + " ms");
	} 

	
	public List<QuantizationRealtimeInfo> getQuantizationRealtimeInfo(List<QuantizationDetailInfo> quantizations, String source){
    	
		List<QuantizationRealtimeInfo> realtimeInfos = new ArrayList<>();
		String cache = QuantizationSourceEnum.getCacheBySource(source);
		List<Object> resp = redisTemplate.opsForList().range(cache, 0, -1);
		if(!CollectionUtils.isEmpty(resp)) {
			for(Object obj : resp) {
				QuantizationRealtimeInfo qr = JSON.parseObject(JSON.toJSONString(obj), new TypeReference<QuantizationRealtimeInfo>() { });
				realtimeInfos.add(qr);
			}
			log.info("从redis缓存读取数据。");
			return realtimeInfos;
		} else {
			log.info("redis缓存数据为空，开始调用远程接口查询。");
			List<String> quantizationCodes = transferToACodes(quantizations);
	    	Map<String, CenterQuantization> quantizationCodesResultMap = centerClient.obtainRealTimeDatas(String.join(",", quantizationCodes));
	    	realtimeInfos = this.assembleDatas(quantizationCodesResultMap, quantizations);
	    	realtimeInfos.stream().forEach(realtimeInfo -> redisTemplate.opsForList().leftPushAll(cache, realtimeInfo));
	    	//Long result = redisTemplate.opsForList().leftPushAll(cache, realtimeInfos);
	    	redisTemplate.expire(cache, 10,TimeUnit.MINUTES);
	    	log.info("存redis成功");
		}
    	return realtimeInfos;
	}
	
	public List<String> transferToACodes(List<QuantizationDetailInfo> quantizations) {
		List<String> quantizationCodes = new ArrayList<>();
		for(QuantizationDetailInfo quantization : quantizations) {
			StringBuffer sb = new StringBuffer();
			String result = addPreChain.addPre(quantization.getSource());
			sb.append(result);
			sb.append(quantization.getQuantizationCode());
			quantizationCodes.add(sb.toString());
		}
		return quantizationCodes;
	}
	
	@Override
	public void getQuantizationHistoryDetails() {
		
		List<String> source = new ArrayList<>();
		source.add(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource());
		List<QuantizationDetailInfo> quantizations = quantizationMapper.queryQuantizationsBySelectedStatus(source);
		List<String> quantizationCodes = new ArrayList<>();
		quantizations.stream().forEach(quantization -> quantizationCodes.add(Constants.HIS_DATA_PRE + quantization.getQuantizationCode()));
		List<QuantizationHistoryDetail> historys = centerClient.getQuantizationHistoryDetails(quantizationCodes);
		historys.stream().forEach(detail -> {
			String code = detail.getCode().substring(3);
			detail.setCode(code);
			detail.setId(code);
			detail.setQuantizationSource(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource());
		});
		mongoTemplate.insert(historys, QuantizationHistoryDetail.class);
		log.info("批量插入Mongodb完成。");
	}
	
	@Override
	public String calculateIndex() {
		Query query = new Query();
		query.addCriteria(Criteria.where(QUANTIZATION_SOURCE).is(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource()));
		List<QuantizationHistoryDetail> details = mongoTemplate.find(query, QuantizationHistoryDetail.class);
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal totalValue = new BigDecimal(0);
		QuantizationIndexValues quantizationValues = new QuantizationIndexValues();
		QuantizationValueDetail valueDetail = new QuantizationValueDetail();
		List<QuantizationValueDetail> valueDetailList = new ArrayList<>();
		List<QuantizationValue> quantizationValueList = new ArrayList<>();
		for(QuantizationHistoryDetail detail : details) {
			List<List<String>> datas = detail.getHq();
			BigDecimal cal = new BigDecimal(1);
			QuantizationValue quantizationValue = new QuantizationValue();
			for(List<String> data : datas) {
				BigDecimal riseRate = new BigDecimal(data.get(4).replace("%",""));
				BigDecimal result = hundred.add(riseRate).divide(new BigDecimal(100));
				cal = cal.multiply(result);
			}
			totalValue = totalValue.add(cal);
			quantizationValue.setQuantizationCode(detail.getCode());
			quantizationValue.setQuantizationValue(cal);
			quantizationValueList.add(quantizationValue);
		}
		valueDetail.setIndexValue(totalValue.divide(new BigDecimal(details.size()), 10, RoundingMode.HALF_UP).multiply(new BigDecimal(1000)).setScale(8, BigDecimal.ROUND_HALF_UP));
		valueDetail.setQuantizationSource(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource());
		valueDetail.setQuantizationValues(quantizationValueList);
		Date date = new Date();
		valueDetailList.add(valueDetail);
		quantizationValues.setDetail(valueDetailList);
		quantizationValues.setId(DateUtils.getDate(date));
		quantizationValues.setDate(date);
		mongoTemplate.save(quantizationValues);
		log.info("保存indexvalue到mongo。");
		return valueDetail.getIndexValue().toString();
	}
	
	@Override
	public String getIndex() {
		List<QuantizationIndexValues> list = mongoTemplate.find(new Query(), QuantizationIndexValues.class);
		List<QuantizationValueDetail> values = list.get(0).getDetail();
		for(QuantizationValueDetail detail : values) {
			if(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource().equals(detail.getQuantizationSource())) {
				return detail.getIndexValue().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			}
		}
		return null;
	}
	
	public List<QuantizationRealtimeInfo> assembleDatas(Map<String, CenterQuantization> remoteQuantizationMap, List<QuantizationDetailInfo> quantizations) {
		long start = System.currentTimeMillis();
		List<QuantizationRealtimeInfo> viewList = new ArrayList<>();
		for(int i=0; i<quantizations.size(); i++) {
			QuantizationDetailInfo quantization = quantizations.get(i);//表对象
			String quantizationCode = quantization.getQuantizationCode();
			try {
				CenterQuantization remoteQuantization = remoteQuantizationMap.get(quantizationCode);//远程对象
				QuantizationRealtimeInfo realtimeInfo = new QuantizationRealtimeInfo();//组装对象
				realtimeInfo.setQuantizationCode(quantizationCode);
				realtimeInfo.setQuantizationName(quantization.getQuantizationName());
				realtimeInfo.setRealTime(-1D);
				realtimeInfo.setGoodWill(QuantizationUtil.getTwoPointDouble(String.valueOf(quantization.getGoodWill()!=null?quantization.getGoodWill():0D)));
				String debtRatisDouble = quantization.getDebtRatio();
				if(debtRatisDouble!=null){
					String debtRatisStr = QuantizationUtil.getTwoPointDouble(debtRatisDouble);
					debtRatisStr = debtRatisStr.substring(0, debtRatisStr.indexOf('.')+3) + "%";
					realtimeInfo.setDebtRatio(debtRatisStr);
				}else {
					realtimeInfo.setDebtRatio("0.00%");
				}
				realtimeInfo.setPb(quantization.getPb());
				realtimeInfo.setPe(quantization.getPe());
				realtimeInfo.setClassify(quantization.getClassify());
				realtimeInfo.setAnalysisURL(Constants.PRE_ANALYSISURL + quantizationCode);
				realtimeInfo.setRetainedProfits2016(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2016()));
				realtimeInfo.setRetainedProfits2017(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2017()));
				realtimeInfo.setRetainedProfits2018(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2018()));
				realtimeInfo.setRetainedProfits2019(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2019()));
				realtimeInfo.setRetainedProfits2020(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2020()));
				realtimeInfo.setRetainedProfits2021(QuantizationUtil.getTwoPointDouble(quantization.getRetainedProfits2021()));
				Double realTimePrice = 100D;
				if(remoteQuantization!=null) {
					realtimeInfo.setQuantizationName(remoteQuantization.getQuantizationName());
					realTimePrice = remoteQuantization.getRealTimePrice()==0D?100D:remoteQuantization.getRealTimePrice();
					realtimeInfo.setRealTime(realTimePrice);
					realtimeInfo.setRatePercent(remoteQuantization.getRatePercent());
					realtimeInfo.setTotalMarketValue(remoteQuantization.getTotalMarketValue());
					Double buyPrice = quantization.getBuyPrice()==null?0.5D:quantization.getBuyPrice().doubleValue();
					realtimeInfo.setBuyPrice(buyPrice);
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(2);
					BigDecimal b1 = new BigDecimal(Double.toString(realTimePrice - buyPrice));
					BigDecimal b2 = new BigDecimal(Double.toString(realTimePrice));
					Double buyRate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
					realtimeInfo.setBuyRateDouble(buyRate);
					realtimeInfo.setBuyRate(nf.format(buyRate));
					Double maxValue = quantization.getMaxValue()==null?200D:quantization.getMaxValue().doubleValue();
					b1 = new BigDecimal(Double.toString(maxValue - realTimePrice));
					b2 = new BigDecimal(Double.toString(maxValue));
					Double maxRate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
					realtimeInfo.setMaxRate(nf.format(maxRate));
					//最低点已涨跌百分比
					Double minValue = quantization.getMinValue()==null?1D:quantization.getMinValue().doubleValue();
					b1 = new BigDecimal(Double.toString(realTimePrice - minValue));
					b2 = new BigDecimal(Double.toString(minValue));
					Double minRate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
					realtimeInfo.setMinRate(nf.format(minRate));
					viewList.add(realtimeInfo);
				}else {
					//logger.info("code:{}, 远程数据对象为空", code);
					//warningInfoService.saveWarningInfo("RemoteDataService", "quantizationCode:"+quantizationCode+", 远程数据对象为空");
				}
			} catch (Exception e) {
				log.error("编码{}, 根据远程数据组装展示数据方法异常: {}", quantizationCode, e);
				//warningInfoService.saveWarningInfo("RemoteDataService", "根据远程数据组装展示数据方法异常,code:"+quantizations.get(i).getQuantizationCode()+"."+e);
			}
		}
		log.info("实时查询数据耗时4：{}", System.currentTimeMillis() - start);
		return viewList;
	}

	public void updateQuantization(String column, String quantizationCode, String value){ 
		
		StringBuffer sb = new StringBuffer();
		sb.append("quantizationCode:"+quantizationCode+", ");
		QuantizationRealtimeInfo quantization = new QuantizationRealtimeInfo();
		quantization.setQuantizationCode(quantizationCode);
		if(ColumnEnum.DESCRIPTION.getCode().equals(column)) { 
			quantization.setDescription(value);
			sb.append("更新"+ColumnEnum.DESCRIPTION.getMsg()+"为: "+value);
		}
		if(ColumnEnum.CLASSIFY.getCode().equals(column)) { 
			quantization.setClassify(value);
			sb.append("更新"+ColumnEnum.CLASSIFY.getMsg()+"为: "+value);
		}
		if(ColumnEnum.BUYPRICE.getCode().equals(column)) {
			quantization.setBuyPrice(Double.valueOf(value));
			sb.append("更新"+ColumnEnum.BUYPRICE.getMsg()+"为: "+value);
		}
		quantizationMapper.updateQuantization(quantization);
		log.info("quantizationCode:{}, {}.", quantizationCode, sb.toString());
	}

}
