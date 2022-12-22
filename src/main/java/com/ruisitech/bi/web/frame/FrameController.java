/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.frame;

import com.ruisitech.bi.entity.frame.Menu;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.service.frame.MenuService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.IsModel;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/frame")
public class FrameController extends BaseController {

	@Autowired
	private MenuService service;

	public final static String MOBILE_REPORT = "$m-report";  //移动端report节点
	public final static String MOBILE_DASHBOARD = "$m-ybp";	 //移动端驾驶舱节点


	@RequestMapping(value="/Menus.action")
	public @ResponseBody Object menus() {
		User user = RSBIUtils.getLoginUserInfo();
		//查询PC端菜单
		List<Menu> menus = service.listUserMenusInPc(user.getId());
		return super.buildSucces(menus);
	}

	@RequestMapping(value="/MobileMenus.action")
	public @ResponseBody Object mobileMenus() {
		User user = RSBIUtils.getLoginUserInfo();
		Map<String, Object> p = new HashMap<>();
		//查询报表，仪表盘列表
		p.put("reports", service.listUserMenusInMobile(user.getId(), MOBILE_REPORT));
		p.put("ybp", service.listUserMenusInMobile(user.getId(), MOBILE_DASHBOARD));
		return super.buildSucces(p);

	}

	@RequestMapping(value="/Welcome.action")
	public @ResponseBody Object welcome(HttpServletRequest req) {

		return super.buildSucces();
	}
}
