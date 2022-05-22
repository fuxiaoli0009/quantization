package com.personal.quantization.model;

import lombok.Data;

@Data
public class QuantizationDetailInfo {

    public long quantizationId;

    public String quantizationCode;

    public String quantizationName;
    
    public Double realTime;
    
    public Double maxValue;
    
    public Double minValue;
    
    public String goodWill;
    
    public String debtRatio;
    
    public Double pb;
    
    public Double pe;
    
    public String classify;
    
    public Double buyPrice;
    
    public String source;
    
    public String selectedStatus;
    
    public Double diffValue;
    
    public Double incri;
    
    public Double decri;
    
    public String analysisURL;
    
    public String retainedProfits2016;
    
    public String retainedProfits2017;
    
    public String retainedProfits2018;
    
    public String retainedProfits2019;
    
    public String retainedProfits2020;
    
    public String retainedProfits2021;
    
    public Integer sortOrder;
    
    public Integer negativeCount;
}
