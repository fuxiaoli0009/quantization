package com.personal.quantization.model;

import lombok.Data;

/**
 * 远程数据封装模板类
 */
@Data
public class CenterQuantization {

	public String quantizationCode;
	
	public String quantizationName;
	
	public Double realTimePrice;
	
	public String ratePercent;
	
	public String turnoverRate;
	
	public Double circulationMarketValue;
	
	public Double totalMarketValue;
	
	public Long turnOver;

}
