package com.personal.quantization.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("quantization_error")
public class QuantizationError {

    public Long quantizationId;

    public String quantizationCode;

    public String quantizationName;
    
    public String message;
    
    
    
}
