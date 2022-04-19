package com.personal.quantization.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document("quantization_index_values")
public class QuantizationIndexValues {
	
	@Id
	public String id;

	@Field("date")
	public Date date;
	
	@Field("quantization_value_Detail")
	public List<QuantizationValueDetail> detail;
	
}
