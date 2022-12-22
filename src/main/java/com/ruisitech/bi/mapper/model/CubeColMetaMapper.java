package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.CubeColMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

public interface CubeColMetaMapper {

	void insertMeta(CubeColMeta meta);

	void deleteKpiMeta(@Param("cubeId") String cubeId);

	void deleteDimMeta(@Param("cubeId") String cubeId);

	void deleteByCubeId(@Param("cubeId") String cubeId);
}
