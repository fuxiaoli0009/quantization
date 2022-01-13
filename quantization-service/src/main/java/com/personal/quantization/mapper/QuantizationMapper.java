package com.personal.quantization.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.personal.quantization.model.QuantizationDetailInfo;
import com.personal.quantization.model.QuantizationRealtimeInfo;
import com.personal.quantization.model.QuantizationSource;

public interface QuantizationMapper {

	public List<QuantizationRealtimeInfo> getAllQuantizations(String status);
	
	public List<Integer> getCloseIndexsByCode(String code);

	public void updateQuantization(QuantizationRealtimeInfo quantization);

	public List<QuantizationSource> getQuantizationDetailInfos(@Param("selectedStatus") List<String> selectedStatus);

	public Integer batchUpdateQuantizations(@Param("quantizationDetailInfos") List<QuantizationDetailInfo> quantizationDetailInfos);

	public List<QuantizationDetailInfo> selectQuantizations();

	public List<QuantizationDetailInfo> selectQuantizationsByClassify();

	public Integer udpateQuantizations(@Param("details") List<QuantizationDetailInfo> details);

	public void updateSelected();

	public List<QuantizationDetailInfo> selectQuantizationsBySelected(String code);

	public List<QuantizationDetailInfo> queryQuantizationsBySelectedStatus(@Param("sources") List<String> source);

	public void updateSelectedStatus(@Param("quantizationCode") String quantizationCode, @Param("selectedStatus") String selectedStatus);

	public void add(String string);
}
