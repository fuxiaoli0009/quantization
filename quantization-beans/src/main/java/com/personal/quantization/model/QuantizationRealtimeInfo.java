package com.personal.quantization.model;

import lombok.Data;

@Data
public class QuantizationRealtimeInfo implements Comparable<QuantizationRealtimeInfo>{

    public int quantizationId;

    public String quantizationCode;

    public String quantizationName;
    
    public Double realTime;

    public Double buyPrice;

    public Double sellPrice;
    
    public String turnoverRate;
	
	public Double circulationMarketValue;
	
	public Double totalMarketValue;
	
	public String debtRatio;
	
	public String goodWill;
    
    public Double pb;
    
    public Double pe;
    
    public String classify;

    public Double maxValue;

    public Double minValue;
    
    public Double buyRateDouble;

    public String buyRate;

    public String maxRate;
    
    public String minRate;

    public String ratePercent;

    public String description;

    public String analysisURL;
    
    public String retainedProfits2016;
    
    public String retainedProfits2017;
    
    public String retainedProfits2018;
    
    public String retainedProfits2019;
    
    public String retainedProfits2020;
    
    public String retainedProfits2021;
    
	@Override
	public int compareTo(QuantizationRealtimeInfo info) {
		if(this.buyRateDouble > info.getBuyRateDouble()) {
			return 1;
		}else {
			return -1;
		}
	}
    
}
