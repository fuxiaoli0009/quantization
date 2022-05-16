package com.personal.quantization.design.bridge;

public class BlueBrick implements AbstractRedOrBlackBrick {

	@Override
	public int selectMode() {
		System.out.println("蓝砖头");
		return 1;
	}

}
