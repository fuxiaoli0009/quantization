package com.personal.quantization.enums;

public enum QuantizationEnum {
	
	QUANTIZATION_STATUS_INIT("0", "初始"),
	QUANTIZATION_STATUS_DELETE("1", "删除"),
	
	QUANTIZATION_TYPE_HS("0", "沪深"),
	QUANTIZATION_TYPE_HK("1", "香港"),
	QUANTIZATION_TYPE_STAR("2", "科创"),
	QUANTIZATION_TYPE_CHOSEN("1", "选中");

	private String code;
	
	private String msg;
	
	private QuantizationEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public static String getValueByCode(String code){
        for(QuantizationEnum quantizationEnum:QuantizationEnum.values()){
            if(code.equals(quantizationEnum.getCode())){
                return quantizationEnum.getMsg();
            }
        }
        return  null;
    }
	
	public static String getCodeByValue(String msg) {
		for(QuantizationEnum quantizationEnum:QuantizationEnum.values()) {
			if(msg.equals(quantizationEnum.getMsg())) {
				return quantizationEnum.getCode();
			}
		}
		return null;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}