package com.personal.quantization.chain;

import org.springframework.stereotype.Component;

import com.personal.quantization.enums.QuantizationSourceEnum;

@Component
public class AddPreKCHandler implements AddPreHandler {

	@Override
	public String addPre(String source) {
		
		return QuantizationSourceEnum.QUANTIZATION_SOURCE_KC.getSource().equals(source) ? "s_sh" : null;
	}

}
