package com.personal.quantization.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.personal.quantization.center.api.QuantizationCenterClient;
import com.personal.quantization.constant.Constants;
import com.personal.quantization.enums.QuantizationSelectedStatusEnum;
import com.personal.quantization.enums.QuantizationSourceEnum;
import com.personal.quantization.mapper.QuantizationMapper;
import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.model.QuantizationSource;
import com.personal.quantization.service.QuantizationChooseService;
import com.personal.quantization.utils.QuantizationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuantizationChooseServiceImpl implements QuantizationChooseService {
    
	@Autowired
	QuantizationCenterClient centerClient;
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
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
		details.stream().forEach(detail -> {
			detail.setNegativeCount(0);
			detail.setAnalysisURL(Constants.PRE_ANALYSISURL + detail.getQuantizationCode());
			detail.setDebtRatio(QuantizationUtil.getTwoPointDouble(detail.getDebtRatio()));
			detail.setGoodWill(QuantizationUtil.getTwoPointDouble(detail.getGoodWill()));
			detail.setRetainedProfits2016(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2016()));
			detail.setRetainedProfits2017(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2017()));
			detail.setRetainedProfits2018(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2018()));
			detail.setRetainedProfits2019(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2019()));
			detail.setRetainedProfits2020(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2020()));
			detail.setRetainedProfits2021(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2021()));
			if(detail.getRetainedProfits2016().startsWith("-")) {
				detail.setNegativeCount(detail.getNegativeCount()+1);
			}
		});
		return details.stream().filter(tail -> filterByCondition(tail)).collect(Collectors.toList());
	}
	
	public boolean filterByCondition(QuantizationDetailInfo tail) {
		if("3".equals(tail.getSelectedStatus())) {
			return true;
		} else {
			if("半导体".equals(tail.getClassify())) {
				return tail.getPb() > 0 && tail.getPb() < 5 && (tail.getPe() > 0 || tail.getPe() <-40);
			}
			if(!"半导体".equals(tail.getClassify())) {
				return tail.getNegativeCount()<1 && tail.getPb() > 0 && tail.getPb() < 3 && (tail.getPe() > 0 && tail.getPe() < 50);
			}
		}
		return false;
	}

	@Override
	public List<QuantizationDetailInfo> selectQuantizationsByClassify() {
		List<QuantizationDetailInfo> details = quantizationMapper.selectQuantizationsByClassify();
		details.stream().forEach(detail -> { 
			detail.setNegativeCount(0);
		    detail.setAnalysisURL(Constants.PRE_ANALYSISURL + detail.getQuantizationCode());
		    detail.setDebtRatio(QuantizationUtil.getTwoPointDouble(detail.getDebtRatio()));
			detail.setGoodWill(QuantizationUtil.getTwoPointDouble(detail.getGoodWill()));
			detail.setRetainedProfits2016(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2016()));
			detail.setRetainedProfits2017(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2017()));
			detail.setRetainedProfits2018(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2018()));
			detail.setRetainedProfits2019(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2019()));
			detail.setRetainedProfits2020(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2020()));
			detail.setRetainedProfits2021(QuantizationUtil.getTwoPointDouble(detail.getRetainedProfits2021()));
			if(detail.getRetainedProfits2016().startsWith("-")) {
				detail.setNegativeCount(detail.getNegativeCount()+1);
			}
		});
		return details.stream().filter(tail -> filterByCondition(tail)).collect(Collectors.toList());
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

	@Override
	public Boolean clearRedis() {
		for(QuantizationSourceEnum quantizationSourceEnum : QuantizationSourceEnum.values()){
			String cache = quantizationSourceEnum.getCache();
			if(redisTemplate.hasKey(cache)) {
				if(!redisTemplate.expire(quantizationSourceEnum.getCache(), 0,TimeUnit.MILLISECONDS)) {
					log.info("clearRedis 删除key:{}异常！", quantizationSourceEnum.getCache());
					return false;
				}
			}
        }
		log.info("clearRedis清除缓存成功！");
		return true;
	}
}
