package com.personal.quantization.knowledge.dto;
import java.util.Date;

import lombok.Data;

@Data
public class OrderEntity {
    private Integer id;
    private String goodsName;
    private Date addTime;
    private Date updateTime;
    private String threadName;

}
