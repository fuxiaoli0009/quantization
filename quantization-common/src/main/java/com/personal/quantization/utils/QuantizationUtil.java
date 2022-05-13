package com.personal.quantization.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.personal.quantization.enums.QuantizationEnum;
import com.personal.quantization.enums.QuantizationSourceEnum;
import com.personal.quantization.model.QuantizationDetailInfo;

import net.sf.cglib.beans.BeanMap;

/**
 * @Author: fuxiaoli
 * @Date: 2018/4/6
 **/
public class QuantizationUtil {
	
	private static DecimalFormat df = new DecimalFormat("######0.00");

	public static List<String> transferToACodes(List<QuantizationDetailInfo> quantizations) {
		List<String> quantizationCodes = new ArrayList<>();
		for(QuantizationDetailInfo quantization : quantizations) {
			StringBuffer sb = new StringBuffer();
			if(quantization.getSource().equals(QuantizationSourceEnum.QUANTIZATION_SOURCE_SH.getSource())) {
				sb.append("s_sh");
			}
			if(quantization.getSource().equals(QuantizationSourceEnum.QUANTIZATION_SOURCE_SZ.getSource())) {
				sb.append("s_sz");
			}
			if(quantization.getSource().equals(QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource())) {
				sb.append("s_sh");
			}
			if(quantization.getSource().equals(QuantizationSourceEnum.QUANTIZATION_SOURCE_XG.getSource())) {
				sb.append("s_hk");
			}
			sb.append(quantization.getQuantizationCode());
			quantizationCodes.add(sb.toString());
		}
		return quantizationCodes;
	}
	
	public static String getTwoPointDouble(String data) {
		if(StringUtils.isNotEmpty(data)) {
			return df.format(Double.valueOf(data));
		}
		return "0.00";
	}
	
	public static String transferToACodes(List<String> codes, String type) {
		StringBuffer sb = new StringBuffer();
		for(String code : codes) {
			if(type.equals(QuantizationEnum.QUANTIZATION_TYPE_HS.getCode()) 
					|| type.equals(QuantizationEnum.QUANTIZATION_TYPE_CHOSEN.getCode())) {
				sb.append(code.startsWith("6")?"s_sh":"s_sz");
			}
			if(type.equals(QuantizationEnum.QUANTIZATION_TYPE_STAR.getCode())) {
				sb.append("s_sh");
			}
			if(type.equals(QuantizationEnum.QUANTIZATION_TYPE_HK.getCode())) {
				sb.append("hk");
			}
			sb.append(code);
			sb.append(",");
		}
		return sb.toString().substring(0, sb.length()-1);
	}
	
	
	
	
	
	/**
     * 数据抽取自：https://www.legulegu.com/stockdata/market-activity
     */
    private static String REGEX = "id=\"\\w+\"\n\\s+data-chart='\\d+'";

    /**
     * 获取股票涨跌数据
     * @param response
     * @return
     */
    public static Map<String, Integer> findRiseAndFallData(String response){
        Map<String, Integer> riseAndFallData = new HashMap<String, Integer>();
        Pattern pattern = Pattern.compile(QuantizationUtil.REGEX);
        Matcher matcher = pattern.matcher(response);
        while(matcher.find())
            riseAndFallData.put(matcher.group().split("\"")[1], Integer.parseInt(matcher.group().split("\'")[1]));
        return riseAndFallData;
    }

    /**
     * BeanMap实现map转对象
     * @param map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Integer> map,T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 计算两个数的百分比,保留小数后两位
     * @param obj1
     * @param obj2
     * @return
     */
    public static String calPercentage(Float obj1, Float obj2){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format((obj1) /  obj2 * 100) + "%";
    }

}
