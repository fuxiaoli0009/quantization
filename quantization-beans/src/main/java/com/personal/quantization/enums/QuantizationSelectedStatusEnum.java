package com.personal.quantization.enums;

public enum QuantizationSelectedStatusEnum {
	
	QUANTIZATION_SELECTED_STATUS_("-1", "不考虑"),
	QUANTIZATION_SELECTED_STATUS_0("0", "未选中"),
	QUANTIZATION_SELECTED_STATUS_1("1", "已选中"),
	QUANTIZATION_SELECTED_STATUS_2("2", "待选中"),
	QUANTIZATION_SELECTED_STATUS_3("3", "已买入");
	
	private String code;
	
	private String msg;
	
	private QuantizationSelectedStatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
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