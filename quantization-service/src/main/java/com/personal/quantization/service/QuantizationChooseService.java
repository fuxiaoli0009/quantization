package com.personal.quantization.service;

import java.util.List;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;

public interface QuantizationChooseService {

	public Integer updatePBPE();

	public List<QuantizationDetailInfo> selectQuantizations();

	public List<QuantizationDetailInfo> selectQuantizationsByClassify();

	public List<QuantizationDetailInfo> selectQuantizationsBySelected();

	public void updateSelectedStatus(String quantizationCode, String selectedStatus);

	public void add(QuantizationRealtimeInfo quantizationRealtimeInfo);
	
}
