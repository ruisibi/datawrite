package com.ruisitech.bi.web.bireport;

import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.bireport.OlapInfo;
import com.ruisitech.bi.service.bireport.OlapService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/bireport")
public class MyReportController extends BaseController {

	@Autowired
	private OlapService service;

	@RequestMapping(value="/listReport.action")
	public @ResponseBody Object list(String keyword){
		List<OlapInfo> ret = service.listreport(keyword);
		return super.buildSucces(ret);
	}

	@RequestMapping(value="/getReport.action")
	public @ResponseBody Object getReport(String pageId){
		OlapInfo olap = service.getOlap(pageId);
		return super.buildSucces(olap);
	}

	@RequestMapping(value="/saveReport.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(OlapInfo info){
		if(service.olapExist(info.getPageName()) > 0){
			return super.buildError("报表名存在。");
		}
		if(info.getId() == null){
			info.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			info.setCreateUser(RSBIUtils.getLoginUserInfo().getUserId());
			info.setCreateDate(new Date());
			JSONObject page = JSONObject.parseObject(info.getPageInfo());
			page.put("id", info.getId());
			info.setPageInfo(page.toString());
			service.insertOlap(info);
		}else{
			info.setUpdateDate(new Date());
			service.updateOlap(info);
		}
		//返回ID
		return super.buildSucces(info.getId());
	}

	@RequestMapping(value="/deleteReport.action")
	public @ResponseBody
    Object deleteReport(String id){
		service.deleteOlap(id);
		return this.buildSucces();
	}

	@RequestMapping(value="/renameReport.action")
	public @ResponseBody
    Object rename(OlapInfo info){
		service.renameOlap(info);
		return this.buildSucces();
	}

}
