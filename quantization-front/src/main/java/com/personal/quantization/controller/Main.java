package com.personal.quantization.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.personal.quantization.model.QuantizationDetailInfo;

public class Main {

	public static void main(String[] args) {
		QuantizationDetailInfo info = new QuantizationDetailInfo();
		List<QuantizationDetailInfo> list = new ArrayList<>();
		info.setSortOrder(100);
		list.add(info);
		QuantizationDetailInfo info2 = new QuantizationDetailInfo();
		info2.setSortOrder(200);
		list.add(info2);
		QuantizationDetailInfo info3 = new QuantizationDetailInfo();
		info3.setSortOrder(150);
		list.add(info3);
		System.out.println(JSON.toJSON(list));
		ord(list);
		System.out.println(JSON.toJSON(list));
	}
	
	public static void ord(List<QuantizationDetailInfo> list) {
		list = list.stream().sorted(Comparator.comparing(QuantizationDetailInfo::getSortOrder)).collect(Collectors.toList());
	}

}
