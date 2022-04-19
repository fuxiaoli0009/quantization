package com.personal.quantization.knowledge.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.personal.quantization.knowledge.client.InventoryClient;
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
    
    // 积分服务
    @Autowired
    private InventoryClient inventoryClient;
    
    /******************************** TCC分布式事务 ********************************/
    // 对这个订单完成支付
    @Override
    public void pay(){
        
    	String productId = "product001";
    	//对本地的保存订单的订单数据库修改订单状态为"已支付"
    	String threadName = Thread.currentThread().getName();
    	goodsMapper.insertOrder(productId, threadName);
        //调用库存服务扣减库存
    	inventoryClient.reduceInventory(productId, threadName);
        //调用积分服务增加积分
        //creditService.addCredit();
        //调用仓储服务通知发货
        //wmsService.saleDelivery();
    }

    /******************************** 乐观锁 ********************************/
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
        	log.info("当前线程：{}, 已没有库存", Thread.currentThread().getName());
            return result;
        }
        if (goods.getStock()<1){
        	log.info("当前线程：{}, 已没有库存", Thread.currentThread().getName());
            return result;
        }
        log.info("当前线程：{}, 当前库存：{}", Thread.currentThread().getName(), goods.getStock());
        goods.setStock(goods.getStock()-1);
        //  goods.setUpdateTime(new Date());
        result=goodsMapper.updateStick(goods);
        if (result==0){
        	log.info("当前线程：{}, 库存减少失败", Thread.currentThread().getName());
            return result;//throw new RuntimeException("库存减少失败了");
        }
        log.info("当前线程：{}, 减库存1, 剩余库存：{}", Thread.currentThread().getName(), goods.getStock());
        //加入订单表
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setGoodsName(goods.getGoodsName());
        orderEntity.setAddTime(new Date());
        orderEntity.setUpdateTime(new Date());
        orderEntity.setThreadName(Thread.currentThread().getName());
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
