package com.personal.quantization.model;

import lombok.Data;

/**
 * 远程数据封装模板类
 */
@Data
public class RemoteQuantization {

	public String quantizationCode;
	
	public String quantizationName;
	
	public Double realTimePrice;
	
	public String ratePercent;
	
	public Long turnOver; //成交额-元

}
