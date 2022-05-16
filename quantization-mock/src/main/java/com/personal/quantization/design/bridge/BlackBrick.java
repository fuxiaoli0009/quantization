package com.personal.quantization.design.bridge;

public class BlackBrick implements AbstractRedOrBlackBrick {

	@Override
	public int selectMode() {
		System.out.println("黑砖头");
		return 2;
	}

}
