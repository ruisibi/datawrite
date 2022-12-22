/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 判断用户能访问哪些URL,不能访问哪些URL
 * @author hq
 * @Date 2019年12月23日
 */
@Service
public class UserUrlControlService {
	
	private List<String> controlUrls; //所有需要控制的URL
	
	@Autowired
	private MenuService menuService;

	/**
	 * 是否可以访问某个URL
	 * @param user
	 * @param url
	 * @return
	 */
	public boolean canViewUrl(User user, String url) {
		if(user == null){
			return false;
		}
		List<String> urls = user.getUrls();
		if(urls ==  null){
			return true; //如果未配置用户访问的url,直接通过
		}
		synchronized(this) {
			if(controlUrls == null) {
				controlUrls = menuService.listAllUrl();
			}
		}
		//如果用户访问的url不属于 整体url, 说明这个url未控制， 直接通过。
		//如果访问的url属于控制的url并且不存在用户的url列表中，直接拒绝访问
		if(!controlUrls.contains(url)){
			return true;
		}else{
			if(urls.contains(url)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public boolean checkUrl(HttpServletRequest req) {
		String path = req.getServletPath();
		User u = RSBIUtils.getLoginUserInfo();
		return this.canViewUrl(u, path);
	}
}
