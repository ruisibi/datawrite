package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.Dimension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DimensionMapper {

	void insertDim(Dimension dim);

	void updatedim(Dimension dim);

	void deleteDim(Dimension dim);

	void insertGroup(Dimension dim);

	void deleteGroupById(@Param("groupId") String groupId);

	void deleteGroupByCubeId(@Param("cubeId") String cubeId);

	List<String> listGroup(@Param("cubeId") String cubeId);

	Dimension getDimInfo(@Param("id") String id, @Param("cubeId") String cubeId);

	void updateColType(Map<String, Object> dim);
}
