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
    
}
