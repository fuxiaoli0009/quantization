package com.personal.quantization.knowledge.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="quantization-inventory", path="/inventory")
public interface InventoryClient {
	
	@RequestMapping(method=RequestMethod.POST, value="/reduceInventory")
	void reduceInventory(@RequestBody String productId, @RequestParam String threadName);
	
}
