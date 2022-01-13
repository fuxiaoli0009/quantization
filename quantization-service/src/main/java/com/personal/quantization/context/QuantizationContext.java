package com.personal.quantization.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuantizationContext {

	@Value("${remote.service:tencentService}")
	public String REMOVE_SERVICE;
}
