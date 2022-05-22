package com.personal.quantization.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("quantization_info")
public class QuantizationInfo {

    public Long quantizationId;

    public Integer quantizationCode;

    public String quantizationName;
    
}
