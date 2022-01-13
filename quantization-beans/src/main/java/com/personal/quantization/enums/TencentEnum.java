package com.personal.quantization.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TencentEnum {
	
	QUANTIZATION_SH("sh", "s_sh", "A"),
	QUANTIZATION_SZ("sz", "s_sz", "B"),
	QUANTIZATION_KC("sh", "s_sh", "C"),
	QUANTIZATION_BJ("bj", "", "D"),
	QUANTIZATION_XG("xg", "",  "E");
	
	private String quantizationPre;
	private String quantizationShortPre;
	private String source;
	
	public static String getQuantizationPreBySource(String source) {
		for(TencentEnum tencent : TencentEnum.values()) {
			if(tencent.getSource().equals(source)) {
				return tencent.getQuantizationPre();
			}
		}
		return null;
	}
}
