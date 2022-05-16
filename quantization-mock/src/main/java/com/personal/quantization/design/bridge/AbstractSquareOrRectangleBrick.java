package com.personal.quantization.design.bridge;

public abstract class AbstractSquareOrRectangleBrick {

	public AbstractRedOrBlackBrick redOrBlackBrick;
	
	public AbstractBigOrSmallBrick bigOrSmallBrick; 
	
	public AbstractSquareOrRectangleBrick(AbstractRedOrBlackBrick redOrBlackBrick, AbstractBigOrSmallBrick bigOrSmallBrick) {
		this.redOrBlackBrick = redOrBlackBrick;
		this.bigOrSmallBrick = bigOrSmallBrick;
	}
	
	public abstract void produce();
}
