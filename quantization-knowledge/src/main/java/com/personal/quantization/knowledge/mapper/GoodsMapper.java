package com.personal.quantization.knowledge.mapper;

import org.springframework.stereotype.Repository;
import com.personal.quantization.knowledge.dto.GoodsEntity;
import com.personal.quantization.knowledge.dto.OrderEntity;

@Repository
public interface GoodsMapper {
	
     GoodsEntity findById(Integer id);

     int updateStick(GoodsEntity goodsEntity);
     
     int insert(OrderEntity orderEntity);

}