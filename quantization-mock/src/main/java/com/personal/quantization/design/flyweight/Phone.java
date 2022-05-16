package com.personal.quantization.design.flyweight;

import lombok.Data;

@Data
public class Phone {
	
	public Phone(String branch) {
		this.branch = branch;
	}

	public String branch;
	
	public Double price;
	
}
