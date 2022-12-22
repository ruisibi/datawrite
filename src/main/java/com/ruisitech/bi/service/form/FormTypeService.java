/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.form;

import com.ruisitech.bi.entity.form.FormType;
import com.ruisitech.bi.mapper.form.FormTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class FormTypeService {

	@Resource
	private FormTypeMapper mapper;

	public List<FormType> listcataTree(){
		return mapper.listcataTree();
	}

	public void insertType(FormType type){
		mapper.insertType(type);
	}

	public void updateType(FormType type){
		mapper.updateType(type);
	}

	public void deleleType(String id){
		mapper.deleleType(id);
	}

	public FormType getType(String id){
		return mapper.getType(id);
	}

	public Integer cntReport(String id){
		return mapper.cntReport(id);
	}

}
