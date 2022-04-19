package com.personal.quantization.inventory.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.quantization.inventory.mapper.InventoryMapper;
import com.personal.quantization.inventory.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    
	@Autowired
    InventoryMapper inventoryMapper;

	@Override
	public void reduceInventory(String productId, String threadName) {
		
		inventoryMapper.reduceInventory(productId, Thread.currentThread().getName()+"|"+threadName);
	}

}
