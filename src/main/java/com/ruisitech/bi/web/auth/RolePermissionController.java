/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.auth;

import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.service.auth.AuthRoleService;
import com.ruisitech.bi.service.frame.UserService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色授权
 * @author gdp
 *
 */
@Controller
@RequestMapping(value = "/auth")
public class RolePermissionController extends BaseController {

	@Autowired
	private AuthRoleService service;

	@Autowired
	private UserService userService;

	@RequestMapping(value="/role/roleMenu.action")
	public @ResponseBody Object roleMenu(String roleId) {
		Map<String, Object> menus = service.listRoleMenus(roleId);
		return super.buildSucces(menus);
	}

	@RequestMapping(value="/role/menuSave.action", method = RequestMethod.POST)
	public @ResponseBody
    Object roleMenuSave(String menuIds, String roleId) {
		service.roleMenu(menuIds, roleId);
		return super.buildSucces();
	}

	@RequestMapping(value="/role/userSave.action", method = RequestMethod.POST)
	public @ResponseBody
    Object roleUserSave(@RequestBody Map<String, Object> pms) {
		service.roleUserSave(pms);
		return super.buildSucces();
	}

	/**
	 * 查询不在角色列表的用户
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value="/role/notInRole/list.action", method = RequestMethod.POST)
	public @ResponseBody
    Object notInRoleUsers(String roleId, String search) {
		if(userService.isSSOUserList()) {
			PageParam page = new PageParam();
			page.setRows(10000);
			List<User> ls = userService.listSSOUsers(page, page.getSearch());
			List<Integer> roleUsers = service.listRoleUsers(roleId).stream().map(m->(Integer)m.get("userId")).collect(Collectors.toList());  //角色下的用户
			//查询用户列表不在角色中的用户
			ls = ls.stream().filter(u->!roleUsers.contains(u.getId())).collect(Collectors.toList());
			return ls;
		}else {
			List<User> ls = userService.listRoleNotUser(roleId);
			return ls;
		}
	}

	@RequestMapping(value="/role/user/list.action", method = RequestMethod.GET)
	public @ResponseBody
    Object userList(String roleId) {
		return service.listRoleUsers(roleId);
	}
}
