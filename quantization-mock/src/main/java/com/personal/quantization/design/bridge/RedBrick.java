package com.personal.quantization.design.bridge;

public class RedBrick implements AbstractRedOrBlackBrick {

	@Override
	public int selectMode() {
		System.out.println("红砖头");
		return 1;
	}

}
