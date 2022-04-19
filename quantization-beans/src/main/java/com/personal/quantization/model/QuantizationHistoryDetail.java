package com.personal.quantization.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document("quantization_history_detail")
public class QuantizationHistoryDetail {
	
	@Id
	public String id;

	@Field("status")
	public String status;

	@Field("detail")
    public List<List<String>> hq;
	
	@Field("quantization_code")
    public String code;
	
	@Field("quantization_source")
	public String quantizationSource;
}

