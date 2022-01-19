package com.personal.quantization.knowledge.dto;
import java.util.Date;
import java.time.LocalDateTime;

public class GoodsEntity {
    private Integer id;
    private String goodsName;
    private Integer stock;
    private Integer version;
    private Date addTime;
    private Date updateTime;

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    @Override
    public String toString() {
        return "GoodsEntity{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", stock=" + stock +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
