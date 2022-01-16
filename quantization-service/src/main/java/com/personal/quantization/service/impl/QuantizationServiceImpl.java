package com.personal.quantization.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.personal.quantization.center.api.QuantizationCenterClient;
import com.personal.quantization.constant.Constants;
import com.personal.quantization.enums.ColumnEnum;
import com.personal.quantization.enums.QuantizationSourceEnum;
import com.personal.quantization.mapper.QuantizationMapper;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.service.QuantizationService;
import com.personal.quantization.utils.QuantizationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuantizationServiceImpl implements QuantizationService, InitializingBean {
	
	@Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	QuantizationCenterClient centerClient;
	
	@Autowired
	private QuantizationMapper quantizationMapper;
	
	@Resource(name = "myDataThreadPool")
	private ExecutorService executorService;
	
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
		Collections.sort(hsQuantizations);
		maps.put("result", hsQuantizations);
		log.info("查询数据耗时：{}", System.currentTimeMillis() - start2);
		return JSON.toJSONString(maps);
	}
	
	public List<QuantizationRealtimeInfo> getQuantizationRealtimeInfo(List<QuantizationDetailInfo> quantizations, String source){
		String quantizationCodesResult = null;
		try {
			String cache = QuantizationSourceEnum.getCacheBySource(source);
			quantizationCodesResult = redisTemplate.opsForValue().get(cache);
			if(StringUtils.isEmpty(quantizationCodesResult)) {
				log.info("redis缓存数据为空，调用远程接口查询.");
				quantizationCodesResult = this.getRealTimeDatas(QuantizationUtil.transferToACodes(quantizations));
				redisTemplate.opsForValue().set(cache, quantizationCodesResult, 5, TimeUnit.MINUTES);
			} else {
				log.info("从redis缓存读取数据.");
			}
		} catch (Exception e) {
			log.error("redis连接超时" + e);
			quantizationCodesResult = this.getRealTimeDatas(QuantizationUtil.transferToACodes(quantizations));
		}
    	Map<String, CenterQuantization> quantizationCodesResultMap = centerClient.transferToMap(quantizationCodesResult);
    	return this.assembleDatas(quantizationCodesResultMap, quantizations);
	}
	
	public String getRealTimeDatas(List<String> quantizationCodes) {
		return centerClient.getRealTimeDatas(String.join(",", quantizationCodes));
	}
	
	
	public List<QuantizationRealtimeInfo> getQuantizationRealtimeInfo(String type){
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
				realtimeInfo.setPb(quantization.getPb());
				realtimeInfo.setPe(quantization.getPe());
				realtimeInfo.setClassify(quantization.getClassify());
				realtimeInfo.setAnalysisURL(Constants.PRE_ANALYSISURL + quantizationCode);
				Double realTimePrice = 100D;
				if(remoteQuantization!=null) {
					realtimeInfo.setQuantizationName(remoteQuantization.getQuantizationName());
					//实时价格
					realTimePrice = remoteQuantization.getRealTimePrice()==0D?100D:remoteQuantization.getRealTimePrice();
					realtimeInfo.setRealTime(realTimePrice);
					realtimeInfo.setRatePercent(remoteQuantization.getRatePercent());
					Double buyPrice = quantization.getBuyPrice()==null?0.5D:quantization.getBuyPrice().doubleValue();
					realtimeInfo.setBuyPrice(buyPrice);
					//买入还差百分之几
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(2);
					BigDecimal b1 = new BigDecimal(Double.toString(realTimePrice - buyPrice));
					BigDecimal b2 = new BigDecimal(Double.toString(realTimePrice));
					Double buyRate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
					realtimeInfo.setBuyRateDouble(buyRate);
					realtimeInfo.setBuyRate(nf.format(buyRate));
					//最高点已跌百分比
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
