package com.personal.quantization.knowledge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.quantization.knowledge.service.GoodsService;

import io.swagger.annotations.ApiOperation;

/**
 * 【秒杀商品：springboot+mybatis实现乐观锁】
 *  参考：https://blog.csdn.net/weixin_45031612/article/details/108364789
 * @author fuxiaoli
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
	
    @Autowired
    private GoodsService goodsService;

    /**
     * 下单操作
     */
    @ApiOperation(value = "submitOrder", httpMethod = "GET")
    @RequestMapping(value = "/submitOrder", method = RequestMethod.GET)
    public Object submitOrder(Integer id){
        return goodsService.submit(id);
    }
    
}
