package com.personal.quantization.mock.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * 自定义响应结构
 */
@Data
public class QuantizationResult implements Serializable {

	private static final long serialVersionUID = 1L;

	// 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private Object data;

    public QuantizationResult() {
    }
    public QuantizationResult(Object data) {
        this.code = 200;
        this.message = "OK";
        this.data = data;
    }
    public QuantizationResult(String message, Object data) {
        this.code = 200;
        this.message = message;
        this.data = data;
    }

    public QuantizationResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static QuantizationResult ok() {
        return new QuantizationResult(null);
    }
    public static QuantizationResult ok(String message) {
        return new QuantizationResult(message, null);
    }
    public static QuantizationResult ok(Object data) {
        return new QuantizationResult(data);
    }
    public static QuantizationResult ok(String message, Object data) {
        return new QuantizationResult(message, data);
    }

    public static QuantizationResult build(Integer code, String message) {
        return new QuantizationResult(code, message, null);
    }

    public static QuantizationResult build(Integer code, String message, Object data) {
        return new QuantizationResult(code, message, data);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    /**
     * JSON字符串转成 MengxueguResult 对象
     * @param json
     * @return
     */
    public static QuantizationResult format(String json) {
        try {
            return JSON.parseObject(json, QuantizationResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
