package com.personal.quantization.knowledge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.knowledge.service.GoodsService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/goods")
public class GoodsController {
	
    @Autowired
    private GoodsService goodsService;

    /**
     * 【TCC实现分布式事务：下订单、减库存】
     *  参考：https://blog.csdn.net/weixin_45031612/article/details/108364789
     * @author fuxiaoli
     *
     */
    @ApiOperation(value = "pay", httpMethod = "GET")
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public void pay(){
        goodsService.pay();
    }
    
    /**
     * 【秒杀商品：springboot+mybatis实现乐观锁】
     * 下单操作
     *  参考：https://blog.csdn.net/weixin_45031612/article/details/108364789
     * @author fuxiaoli
     *
     */
    @ApiOperation(value = "submitOrder", httpMethod = "GET")
    @RequestMapping(value = "/submitOrder", method = RequestMethod.GET)
    public Object submitOrder(Integer id){
        return goodsService.submit(id);
    }

}
