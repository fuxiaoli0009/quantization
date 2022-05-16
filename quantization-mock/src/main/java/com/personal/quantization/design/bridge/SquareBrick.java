package com.personal.quantization.design.bridge;

public class SquareBrick extends AbstractSquareOrRectangleBrick {

	public SquareBrick(AbstractRedOrBlackBrick redOrBlackBrick, AbstractBigOrSmallBrick bigOrSmallBrick) {
		super(redOrBlackBrick, bigOrSmallBrick);
	}

	@Override
	public void produce() {
		System.out.print("生产正方形砖头-");
		redOrBlackBrick.selectMode();
		bigOrSmallBrick.bigOrSmall();
	}
	
}
