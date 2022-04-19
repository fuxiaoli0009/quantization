package com.personal.quantization.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class QuantizationValue {
	
	@Field("quantization_code")
    public String quantizationCode;
	
	@Field("quantization_value")
	public BigDecimal quantizationValue;
	
}