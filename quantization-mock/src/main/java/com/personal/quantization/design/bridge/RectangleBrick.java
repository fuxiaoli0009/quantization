package com.personal.quantization.design.bridge;

public class RectangleBrick extends AbstractSquareOrRectangleBrick {

	public RectangleBrick(AbstractRedOrBlackBrick redOrBlackBrick, AbstractBigOrSmallBrick bigOrSmallBrick) {
		super(redOrBlackBrick, bigOrSmallBrick);
	}

	@Override
	public void produce() {
		System.out.print("生产长方形砖头-");
		redOrBlackBrick.selectMode();
		bigOrSmallBrick.bigOrSmall();
	}
	
}
