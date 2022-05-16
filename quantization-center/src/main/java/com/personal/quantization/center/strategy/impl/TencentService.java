package com.personal.quantization.center.strategy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.personal.quantization.center.strategy.CenterService;
import com.personal.quantization.enums.RemoteDataPrefixEnum;
import com.personal.quantization.enums.TencentEnum;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TencentService extends CenterService {

	private static String REMOTE_URL_TENCENT = "http://qt.gtimg.cn/r=0q=";
	
	@Override
	public String getServiceURL() {
		return REMOTE_URL_TENCENT;
	}
	
	@Override
	public List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources) {
		
		StringBuilder quantizationDetails = new StringBuilder();
		quantizationSources.stream().forEach(quantizationSource->{
			quantizationSource.setQuantizationCode(TencentEnum.getQuantizationPreBySource(quantizationSource.getSource())+quantizationSource.getQuantizationCode());
		});
		List<String> quantizationCodes = quantizationSources.stream().map(QuantizationSource::getQuantizationCode).collect(Collectors.toList());
		List<List<String>> quantizationCodesList = Lists.partition(quantizationCodes, 100);
		quantizationCodesList.forEach(quantizationCode -> {
			quantizationDetails.append(getRealTimeDatasFromRemote(Joiner.on(",").join(quantizationCode), REMOTE_URL_TENCENT));
		});
		return parseQuantizationDetails(quantizationDetails.toString());
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
	public Map<String, CenterQuantization> transferToMap(String response) {
		
		if(response!=null && response.contains(";")) {
			String[] responseArray = response.split(";");
			Map<String, CenterQuantization> remoteDataInfoMap = new HashMap<>();
			for(int i=0; i< responseArray.length-1; i++) {
				try {
					CenterQuantization remote = new CenterQuantization();
					String[] strs = responseArray[i].split("~");
					if(strs!=null&&strs.length>0) {
						if((strs[0].contains(RemoteDataPrefixEnum.TENCENT_HK.getCode())||strs[0].contains(RemoteDataPrefixEnum.TENCENT_SH.getCode())||strs[0].contains(RemoteDataPrefixEnum.TENCENT_SZ.getCode()))&&StringUtils.isEmpty(strs[7])) {
						log.error("数据:{},成交量为0,剔除统计.", responseArray[i]);
						}else {
							remote.setQuantizationCode(strs[2]);
							remote.setQuantizationName(strs[1]);
							remote.setRealTimePrice(Double.parseDouble(strs[3]));
							String marketValueStr = strs[9];
							if(marketValueStr!=null && marketValueStr.length()>0 && marketValueStr.indexOf('.')>0) {
								marketValueStr = marketValueStr.substring(0, marketValueStr.indexOf(".")+3);
								remote.setTotalMarketValue(Double.parseDouble(marketValueStr));
							}
							remote.setRatePercent(strs[5]+"%");
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
	
}
