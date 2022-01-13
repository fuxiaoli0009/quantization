package com.personal.quantization.enums;

public enum QuantizationSourceEnum {
	
	QUANTIZATION_SOURCE_SH("A", "上海", "quantizations_source_a"),
	QUANTIZATION_SOURCE_SZ("B", "深圳", "quantizations_source_b"),
	QUANTIZATION_SOURCE_KC("C", "科创", "quantizations_source_c"),
	QUANTIZATION_SOURCE_BJ("D", "北交", "quantizations_source_d"),
	QUANTIZATION_SOURCE_XG("E", "香港", "quantizations_source_e"),
	QUANTIZATION_SOURCE_HS("AB", "沪深", "quantizations_source_ab");
	
	private String source;
	
	private String msg;
	
	private String cache;
	
	private QuantizationSourceEnum(String source, String msg, String cache) {
		this.source = source;
		this.msg = msg;
		this.cache = cache;
	}
	
	public static String getCacheBySource(String source){
        for(QuantizationSourceEnum quantizationSourceEnum : QuantizationSourceEnum.values()){
            if(source.equals(quantizationSourceEnum.getSource())){
                return quantizationSourceEnum.getCache();
            }
        }
        return  null;
    }

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}
	
}