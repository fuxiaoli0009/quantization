package com.personal.quantization.service;

public interface QuantizationTestService {
	
	public void mongoTransactionTest();
	
	public void mysqlTransactionTest();
	
	public void mongoMysqlTransactionTest();
	
	public void twoMysqlTransactionTest();
	
	public void twoMysqlTransactionBySeataTest();
	
	public void twoMicroServiceTransactionBySeataTest();
	
	public void twoMysqlSelectTest();
	
	public void shardingTransactionTest();

}
