package com.personal.quantization.knowledge.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personal.quantization.knowledge.dto.GoodsEntity;
import com.personal.quantization.knowledge.dto.OrderEntity;
import com.personal.quantization.knowledge.mapper.GoodsMapper;
import com.personal.quantization.knowledge.service.GoodsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {
    
	@Autowired
    GoodsMapper goodsMapper;

    @Autowired
    GoodsService goodsService;

    /**
     * 乐观锁实现下单
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int submit(Integer id) {
        GoodsEntity goods = goodsMapper.findById(id);
        int result=0;
        //查询库存
        if (goods==null){
            return result;
        }
        if (goods.getStock()<1){
            return result;
        }
        log.info("当前线程：{}, 当前库存：{}", Thread.currentThread().getName(), goods.getStock());
        goods.setStock(goods.getStock()-1);
        //  goods.setUpdateTime(new Date());
        result=goodsMapper.updateStick(goods);
        if (result==0){
            throw  new RuntimeException("库存减少失败了");
        }
        log.info("当前线程：{}, 减库存1, 剩余库存：{}", Thread.currentThread().getName(), (goods.getStock()-1));
        //加入订单表
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setGoodsName(goods.getGoodsName());
        orderEntity.setAddTime(new Date());
        orderEntity.setUpdateTime(new Date());
        if (goodsService.insert(orderEntity)==0){
            throw  new RuntimeException("加入订单失败了");
        }
        log.info("当前线程：{}, 添加订单", Thread.currentThread().getName());
        return result;
    }
    
    @Override
    public int insert(OrderEntity orderEntity) {
        return goodsMapper.insert(orderEntity);
    }
}
