package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.Cube;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CubeMapper {

	List<Cube> listCube(@Param("keyword") String keyword);

	void insertCube(Cube cube);

	void updateCube(Cube cube);

	void deleteCube(@Param("cubeId") String cubeId);

	Cube getCubeById(@Param("cubeId") String cubeId);

	List<Map<String, Object>> getCubeDims(@Param("cubeId") String cubeId);

	List<Map<String, Object>> getCubeKpis(@Param("cubeId") String cubeId);

	List<Map<String, Object>> listCubeMeta(@Param("cubeId") String cubeId);

	List<Map<String, Object>> listDs(@Param("selectDsIds") String selectDsIds);
}
