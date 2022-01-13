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
    
    public Double pb;
    
    public Double pe;
    
    public String classify;

    /**
     * 5178前复权最高价
     */
    public Double maxValue;

    /**
     * 5178前复权最低价
     */
    public Double minValue;
    
    public Double buyRateDouble;

    /**
     * 买入还差
     */
    public String buyRate;

    /**
     * 最高点已跌百分比
     */
    public String maxRate;
    
    public String minRate;

    /**
     * 今日涨跌幅
     */
    public String ratePercent;

    public String description;

    public String analysisURL;

	@Override
	public int compareTo(QuantizationRealtimeInfo info) {
		if(this.buyRateDouble > info.getBuyRateDouble()) {
			return 1;
		}else {
			return -1;
		}
	}
    
}
