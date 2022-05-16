package com.personal.quantization.chain;

import org.springframework.stereotype.Component;

import com.personal.quantization.enums.QuantizationSourceEnum;

@Component
public class AddPreXGHandler implements AddPreHandler {

	@Override
	public String addPre(String source) {
		
		return QuantizationSourceEnum.QUANTIZATION_SOURCE_XG.getSource().equals(source) ? "s_hk" : null;
	}

}
