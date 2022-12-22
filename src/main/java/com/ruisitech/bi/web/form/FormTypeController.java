/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.form;

import com.ruisitech.bi.entity.form.FormType;
import com.ruisitech.bi.service.form.FormTypeService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * 填报表分类
 * @author hq
 *
 */
@Controller
@RequestMapping(value = "/form")
public class FormTypeController extends BaseController {

	@Autowired
	private FormTypeService service;

	@RequestMapping(value="/typeTree.action")
	public @ResponseBody
    Object tree() {
		return super.buildSucces(service.listcataTree());
	}

	@RequestMapping(value="/addType.action", method = RequestMethod.POST)
	public @ResponseBody
    Object addType(FormType type) {
		type.setCreateUser(RSBIUtils.getLoginUserInfo().getUserId());
		type.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		service.insertType(type);
		return super.buildSucces(type.getId());
	}

	@RequestMapping(value="/updateType.action", method = RequestMethod.POST)
	public @ResponseBody
    Object updateType(FormType type) {
		service.updateType(type);
		return super.buildSucces();
	}

	@RequestMapping(value="/deleteType.action")
	public @ResponseBody
    Object deleteType(String id) {
		if(service.cntReport(id) > 0){
			return super.buildError("分类下存在报表,不能删除。");
		}else{
			service.deleleType(id);
			return super.buildSucces();
		}
	}

	@RequestMapping(value="/getType.action")
	public @ResponseBody
    Object getType(String id) {
		return super.buildSucces(service.getType(id));
	}

}
