package com.personal.quantization.design.flyweight;

import java.util.HashMap;
import java.util.Map;

public class PhoneFactory {

	public static final Map<String, Phone> pool = new HashMap<>();
	
	public static Phone getPhone(String branch) {
		Phone phone = pool.get(branch);
		if(phone == null) {
			System.out.println("创建手机"+branch);
			phone = new Phone(branch);
			pool.put(branch, phone);
		}
		return phone;
	}
}
