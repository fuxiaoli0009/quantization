package com.personal.quantization.inventory.service;

public interface InventoryService {

	public void reduceInventory(String productId, String threadName);
}