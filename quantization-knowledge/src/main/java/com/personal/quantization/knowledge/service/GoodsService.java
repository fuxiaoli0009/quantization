package com.personal.quantization.knowledge.service;

import com.personal.quantization.knowledge.dto.OrderEntity;

public interface GoodsService {

    /**
     * 下单
     */
    public int submit(Integer id);
    
    /**
     * 插入订单
     */
    public int insert(OrderEntity orderEntity);

	public void pay();
}