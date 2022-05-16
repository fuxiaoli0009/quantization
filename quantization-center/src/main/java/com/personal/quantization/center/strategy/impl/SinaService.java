package com.personal.quantization.center.strategy.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.personal.quantization.center.strategy.CenterService;
import com.personal.quantization.enums.RemoteDataPrefixEnum;
import com.personal.quantization.model.CenterQuantization;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationSource;

/**
 * Sina接口服务
 */
@Service
public class SinaService extends CenterService {

	private final Logger logger = LoggerFactory.getLogger(SinaService.class);
	
	@Value("${REMOTE.URL.SINA:http://hq.sinajs.cn/list=}")
	private static String REMOTE_URL_SINA;
	
	public Map<String, CenterQuantization> transferToMap(String response){
		if(response!=null && response.contains(";")) {
			String[] responseArray = response.split("\n");
			Map<String, CenterQuantization> remoteDataInfoMap = new HashMap<>();
			for(int i=0; i< responseArray.length; i++) {
				try {
					CenterQuantization remote = new CenterQuantization();
					String[] datas = responseArray[i].split(",");  //每条数据
					String[] market = datas[0].split("=\"");
					String codeStr = market[0];
					String code = "";
					String name = "";
					Double realTimePrice = 0D;
					Double yesterdayPrice = 0D;//今日涨跌及百分比
					if(datas!=null&&datas.length>3) {
						if(codeStr.contains(RemoteDataPrefixEnum.SINA_S_SH.getCode()) || codeStr.contains(RemoteDataPrefixEnum.SINA_S_SZ.getCode())) {
							//沪深指数解析
							code = codeStr.substring(codeStr.length()-6, codeStr.length());
							name = market[1];
							realTimePrice = Double.parseDouble(datas[1]);
							remote.setRatePercent(datas[3]+"%");
						}else if(codeStr.contains(RemoteDataPrefixEnum.SINA_HK.getCode())) {
							//港股解析
							code = codeStr.substring(codeStr.length()-5, codeStr.length());
							name = datas[1];
							realTimePrice = Double.parseDouble(datas[6]);
							remote.setRatePercent(datas[8]+"%");
						}else{
							//沪深股票解析
							code = codeStr.substring(codeStr.length()-6, codeStr.length());
							name = market[1];
							realTimePrice = Double.parseDouble(datas[3]);
							yesterdayPrice = datas[2].startsWith("0.0")?realTimePrice:Double.parseDouble(datas[2]);//昨天收盘价
							if(realTimePrice!=0 && yesterdayPrice!=0) {
								BigDecimal b1 = new BigDecimal(realTimePrice).subtract(new BigDecimal(Double.toString(yesterdayPrice)));  
								BigDecimal b2 = new BigDecimal(Double.toString(yesterdayPrice));
								Double rate = b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
								NumberFormat nf = NumberFormat.getPercentInstance();
								nf.setMaximumIntegerDigits(4); //小数点前保留几位
								nf.setMinimumFractionDigits(2);//小数点后保留几位
								remote.setRatePercent(nf.format(rate));
							} else {
								remote.setRatePercent("0.00%");
							}
							remote.setTurnOver(Long.parseLong(datas[9].substring(0, datas[9].indexOf("."))));
						}
						remote.setQuantizationCode(code);
						remote.setQuantizationName(name);
						remote.setRealTimePrice(realTimePrice);
						remoteDataInfoMap.put(remote.getQuantizationCode(), remote);
					}else {
						logger.error("封装Sina数据为通用模板RemoteDataInfo数据异常, 长度不够", responseArray[i]);
						//warningInfoService.saveWarningInfo("SinaApiService", "封装Sina数据为通用模板RemoteDataInfo数据异常, 长度不够, responseArray[i]="+responseArray[i]);
					}
					
				} catch (Exception e) {
					logger.error("封装Sina数据为通用模板RemoteDataInfo数据异常,{}", responseArray[i], e);
					//warningInfoService.saveWarningInfo("SinaApiService", "封装Sina数据为通用模板RemoteDataInfo数据异常, responseArray[i]="+responseArray[i]+"."+e);
				}
			}
			return remoteDataInfoMap;
		}
		return null;
	}
	
	@Override
	public List<QuantizationDetailInfo> getQuantizationDetails(List<QuantizationSource> quantizationSources) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuantizationDetailInfo> parseQuantizationDetails(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceURL() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
