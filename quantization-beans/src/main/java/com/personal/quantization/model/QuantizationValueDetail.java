package com.personal.quantization.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class QuantizationValueDetail {

	@Field("quantization_source")
	public String quantizationSource;

	@Field("index_value")
	public BigDecimal indexValue;
	
	@Field("quantization_values")
	public List<QuantizationValue> quantizationValues;
}