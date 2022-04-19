package com.personal.quantization.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.inventory.service.InventoryService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	private InventoryService inventoryService;
	
	@ApiOperation(value = "reduceInventory", httpMethod = "POST")
	@RequestMapping(value = "/reduceInventory", method = RequestMethod.POST)
	public void reduceInventory(@RequestBody String productId, @RequestParam String threadName) {
		log.info("order:{}, threadName:{}, currentThradName:{}", productId, threadName, Thread.currentThread().getName());
		inventoryService.reduceInventory(productId, threadName);
	}
}
