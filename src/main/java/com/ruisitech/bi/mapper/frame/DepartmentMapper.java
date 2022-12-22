/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.frame;

import com.ruisitech.bi.entity.frame.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    int deleteByPrimaryKey( @Param("id") String id);

    int insertSelective(Department record);

    Department selectByPrimaryKey( @Param("id") String id);

    int updateByPrimaryKeySelective(Department record);

    List<Department> list();


    List<Department> tree( @Param("pid") String pid);

    int cntDepartmentUsers( @Param("deptId") String deptId);

}
