package com.personal.quantization.strategy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.personal.quantization.enums.RemoteDataPrefixEnum;
import com.personal.quantization.enums.StockTypeEnum;
import com.personal.quantization.enums.TencentEnum;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.model.RemoteQuantization;
import com.personal.quantization.model.TbStock;
import com.personal.quantization.strategy.RemoteService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TencentService extends RemoteService {

	private static String REMOTE_URL_TENCENT = "http://qt.gtimg.cn/r=0q=";
	
	@Override
	public List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources) {
		
		StringBuilder quantizationDetails = new StringBuilder();
		quantizationSources.stream().forEach(quantizationSource->{
			quantizationSource.setQuantizationCode(TencentEnum.getQuantizationPreBySource(quantizationSource.getSource())+quantizationSource.getQuantizationCode());
		});
		List<String> quantizationCodes = quantizationSources.stream().map(QuantizationSource::getQuantizationCode).collect(Collectors.toList());
		List<List<String>> quantizationCodesList = Lists.partition(quantizationCodes, 100);
		quantizationCodesList.forEach(quantizationCode -> {
			quantizationDetails.append(getRealTimeDatas(Joiner.on(",").join(quantizationCode), REMOTE_URL_TENCENT));
		});
		return parseQuantizationDetails(quantizationDetails.toString());
	}

	@Override
	public String getRealTimeDatas(String quantizationCodes) {
		long start = System.currentTimeMillis();
		String result = getRealTimeDatas(quantizationCodes, REMOTE_URL_TENCENT);
		log.info("quantizationCodes: {}, quantizationCodes耗时：{}", quantizationCodes, System.currentTimeMillis() - start);
		return result;
	}
	
	@Override
	public List<QuantizationDetailInfo> parseQuantizationDetails(String result) {
		
		List<QuantizationDetailInfo> quantizations = new ArrayList<>();
		if(StringUtils.isNotBlank(result)) {
			String[] resultArray = result.split(";");
			if(resultArray!=null && resultArray.length>0) {
				for(int i=0; i<resultArray.length; i++) {
					String row = resultArray[i];
					try {
						if(StringUtils.isNotBlank(row) && row.contains("\"")) {
							String info = row.split("\"")[1];
							if(StringUtils.isNotEmpty(info)) {
								String[] resultInfo = info.split("~");
								if(resultInfo!=null && resultInfo.length>52&&Double.parseDouble(resultInfo[3])>0) {
									QuantizationDetailInfo detail = new QuantizationDetailInfo();
									detail.setQuantizationCode(resultInfo[2]);
									detail.setPb(Double.valueOf(resultInfo[46]));
									detail.setPe(Double.valueOf(resultInfo[52]));
									detail.setRealTime(Double.parseDouble(resultInfo[3]));
									quantizations.add(detail);
								}
							}
						}
					} catch (NumberFormatException e) {
						log.info("报错行信息：{}", row);
						e.printStackTrace();
					}
				}
			}
		}
		return quantizations;
	}
	
	@Override
	public Map<String, RemoteQuantization> transferToMap(String response) {
		
		if(response!=null && response.contains(";")) {
			String[] responseArray = response.split(";");
			Map<String, RemoteQuantization> remoteDataInfoMap = new HashMap<>();
			for(int i=0; i< responseArray.length-1; i++) {
				try {
					RemoteQuantization remote = new RemoteQuantization();
					String[] strs = responseArray[i].split("~");
					if(strs!=null&&strs.length>0) {
						if((strs[0].contains(RemoteDataPrefixEnum.TENCENT_HK.getCode())||strs[0].contains(RemoteDataPrefixEnum.TENCENT_SH.getCode())||strs[0].contains(RemoteDataPrefixEnum.TENCENT_SZ.getCode()))&&StringUtils.isEmpty(strs[7])) {
						log.error("数据:{},成交量为0,剔除统计.", responseArray[i]);
						}else {
							remote.setQuantizationCode(strs[2]);
							remote.setQuantizationName(strs[1]);
							remote.setRealTimePrice(Double.parseDouble(strs[3]));
							/*
							 * if(strs[0].contains(RemoteDataPrefixEnum.TENCENT_HK.getCode())) {
							 * remote.setRatePercent(strs[32]+"%"); }else {
							 * remote.setRatePercent(strs[5]+"%"); }
							 */
							remote.setRatePercent(strs[5]+"%");
							/*
							 * if(strs[0].contains(RemoteDataPrefixEnum.TENCENT_SH.getCode())||strs[0].
							 * contains(RemoteDataPrefixEnum.TENCENT_SZ.getCode())){
							 * remote.setTurnOver(Long.valueOf(strs[7])*10000); }
							 */
							//remote.setTurnOver(Long.valueOf(strs[7])*10000);
							remoteDataInfoMap.put(remote.getQuantizationCode(), remote);
						}
					}
				} catch (Exception e) {
					log.error("数据:{},封装Tencent数据为通用模板异常", responseArray[i], e);
				}
			}
			return remoteDataInfoMap;
		}
		return null;
	}
	
	

	/**
	 * 封装Tencent接口代码前缀
	 * @param tbStocks
	 * @param typeCode
	 * @return
	 */
	public String getCodesFromStocks(List<TbStock> tbStocks, String typeCode) {
		if(tbStocks!=null && tbStocks.size()>0) {
    		StringBuffer sb = new StringBuffer();
            for(TbStock tbStock : tbStocks){
            	String code = tbStock.getCode();
                try {
                	if(StockTypeEnum.STOCK_STATUS_HS.getCode().equals(typeCode)||StockTypeEnum.STOCK_STATUS_CHOSEN.getCode().equals(typeCode)) {
                		sb.append(code.length() == 6 && code.startsWith("6") ? RemoteDataPrefixEnum.TENCENT_SH.getCode() : RemoteDataPrefixEnum.TENCENT_SZ.getCode());
                	}
                	if(StockTypeEnum.STOCK_STATUS_HK.getCode().equals(typeCode)) {
                		sb.append(RemoteDataPrefixEnum.TENCENT_HK.getCode());
                	}
                	if(StockTypeEnum.STOCK_STAR.getCode().equals(typeCode)) {
                		sb.append(RemoteDataPrefixEnum.TENCENT_SH.getCode());
                	}
    				sb.append(code);
    				sb.append(",");
    			} catch (Exception e) {
    				log.error("code:{}, Tencent拼接字符串异常, 请修改数据库相关字段, 异常:{}", code, ExceptionUtils.getStackTrace(e));
    			}
            }
            return sb.toString().substring(0, sb.length()-1);
    	}
        return null;
	}

	/**
	 * 组装上海指数前缀
	 * @param codes
	 * @param type
	 * @return
	 */
	public String getCodesFromSHZSCodes(List<String> codes, String type) {
		StringBuffer sb = new StringBuffer();
		for(String code : codes) {
			sb.append(code.startsWith("0")?"s_sh":"s_sz");
			sb.append(code);
			sb.append(",");
		}
		return sb.toString().substring(0, sb.length()-1);
	}

}
