package com.ruisitech.bi.web.model;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.model.Cube;
import com.ruisitech.bi.service.model.CubeService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/model")
public class CubeController extends BaseController {

	@Autowired
	private CubeService service;

	private static final Logger log = Logger.getLogger(CubeController.class);

	@RequestMapping(value="/listCube.action")
	public @ResponseBody
    Object list(){
		return super.buildSucces(service.listCube(null));
	}

	@RequestMapping(value="/pageCube.action")
	public @ResponseBody
    Object page(String key, PageParam page){
		PageHelper.startPage(page.getPage(), page.getRows());
		List<Cube> ls = service.listCube(key);
		PageInfo<Cube> pageInfo=new PageInfo<Cube>(ls);
		return super.buildSucces(pageInfo);
	}

	@RequestMapping(value="/saveCube.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(@RequestBody Cube cube){
		try {
			service.insertCube(cube);
			return super.buildSucces();
		}catch (Exception ex){
			log.error("保存出错", ex);
			return super.buildError(ex.getMessage());
		}
	}

	@RequestMapping(value="/updateCube.action", method = RequestMethod.POST)
	public @ResponseBody
    Object update(@RequestBody Cube cube){
		try {
			service.updateCube(cube);
			return super.buildSucces();
		}catch (Exception ex){
			log.error("更新立方体异常", ex);
			return super.buildError(ex.getMessage());
		}
	}

	@RequestMapping(value="/delCube.action")
	public @ResponseBody
    Object delete(String cubeId){
		try {
			service.deleteCube(cubeId);
			return super.buildSucces();
		}catch (Exception ex){
			log.error("更新立方体异常", ex);
			return super.buildError(ex.getMessage());
		}
	}

	@RequestMapping(value="/getCube.action")
	public @ResponseBody
    Object get(String cubeId){
		JSONObject cube = service.getCubeById(cubeId);
		return super.buildSucces(cube);
	}

	@RequestMapping(value="/treeCube.action")
	public @ResponseBody
    Object tree(String cubeId){
		List<Map<String, Object>> ret = service.treeCube(cubeId);
		if(ret.size() == 0){
			return super.buildError("无数据");
		}
		return super.buildSucces(ret);
	}
}
