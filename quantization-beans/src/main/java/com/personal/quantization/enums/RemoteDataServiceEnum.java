package com.personal.quantization.enums;

public enum RemoteDataServiceEnum {
	
	SINA_SERVICE("sinaService", "1"),
	TENCENT_SERVICE("tencentService", "2");


	RemoteDataServiceEnum(String service, String serviceDes) {
		this.service = service;
		this.serviceDes = serviceDes;
	}

	private String service;
	
	private String serviceDes;

}
