package com.personal.quantization.inventory.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryMapper {

	void reduceInventory(@Param("productId") String productId, @Param("threadName") String threadName);

}