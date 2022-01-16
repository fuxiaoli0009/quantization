package com.personal.quantization.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.quantization.center.api.QuantizationCenterClient;
import com.personal.quantization.constant.Constants;
import com.personal.quantization.enums.QuantizationSelectedStatusEnum;
import com.personal.quantization.mapper.QuantizationMapper;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.service.QuantizationChooseService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuantizationChooseServiceImpl implements QuantizationChooseService {
    
	@Autowired
	QuantizationCenterClient centerClient;
	
	@Autowired
	private QuantizationMapper quantizationMapper;
	
	@Override
	public Integer updatePBPE() {
		
		List<String> selectedStatus = new ArrayList<>();
		selectedStatus.add("0");
		selectedStatus.add("1");
		selectedStatus.add("2");
		selectedStatus.add("3");
		List<QuantizationSource> quantizationSources = quantizationMapper.getQuantizationDetailInfos(selectedStatus);
		List<QuantizationDetailInfo> quantizationDetailInfos = centerClient.getQuantizationDetails(quantizationSources);
		Integer updatedSize = quantizationMapper.batchUpdateQuantizations(quantizationDetailInfos);
		log.info("更新pb/pe条数：{}", updatedSize);
		return updatedSize;
	}

	@Override
	public List<QuantizationDetailInfo> selectQuantizations() {
		quantizationMapper.updateSelected();
		List<QuantizationDetailInfo> details = quantizationMapper.selectQuantizations();
		Integer size = 0;
		if(!details.isEmpty()) {
			size = quantizationMapper.udpateQuantizations(details);
		}
		log.info("量化待选中数量：{}", size);
		details.stream().forEach(detail -> detail.setAnalysisURL(Constants.PRE_ANALYSISURL + detail.getQuantizationCode()));
		return details;
	}

	@Override
	public List<QuantizationDetailInfo> selectQuantizationsByClassify() {
		List<QuantizationDetailInfo> details = quantizationMapper.selectQuantizationsByClassify();
		details.stream().forEach(detail -> detail.setAnalysisURL(Constants.PRE_ANALYSISURL + detail.getQuantizationCode()));
		return details;
	}

	@Override
	public List<QuantizationDetailInfo> selectQuantizationsBySelected() {
		return quantizationMapper.selectQuantizationsBySelected(QuantizationSelectedStatusEnum.QUANTIZATION_SELECTED_STATUS_1.getCode());
	}

	@Override
	public void updateSelectedStatus(String quantizationCode, String selectedStatus) {
		quantizationMapper.updateSelectedStatus(quantizationCode, selectedStatus);
	}

	@Override
	public void add(QuantizationRealtimeInfo quantizationRealtimeInfo) {
		quantizationMapper.updateSelectedStatus(quantizationRealtimeInfo.getQuantizationCode(), QuantizationSelectedStatusEnum.QUANTIZATION_SELECTED_STATUS_1.getCode());
	}
}
