package com.personal.quantization.service;

public interface QuantizationService {
	
	public String queryQuantizationsBySelectedStatus();

	public void updateQuantization(String tdIndex, String quantizationCode, String value);
	
	
	public String selectQuantizationsBySource(String sources);

}
