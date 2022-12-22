/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.mapper.frame.UserMapper;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Service
public class UserService {

	@Resource
	private UserMapper mapper;

	@Resource
	private RestTemplate restTemplate;

	@Value("${sso.url.userInfo.bytoken}")
	private String userInfoByTokenUrl;

	/**
	 * 通过工号获取用户信息
	 */
	@Value("${sso.url.userInfo.bystaff}")
	private String userInfoByStaffUrl;

	/**
	 * 用户列表URL
	 */
	@Value("${sso.url.userList}")
	private String userListUrl;

	@Autowired
	private SessionDao sessionDao;

	@Autowired
	private MenuService menuService;

	private static Logger log = Logger.getLogger(UserService.class);

	public User getUserByStaffId(String staffId){
		User u = mapper.getUserByStaffId(staffId);
		if(u != null){
			u.setUrls(menuService.listUserVisitUrl(u.getId()));
		}
		return u;
	}

	/**
	 * 根据 staffId 创建一个 User 对象
	 * @param staffId
	 * @return
	 */
	public User createSSOUser(String staffId){
		User u = new User();
		u.setStaffId(staffId);
		u.setId(staffId);
		u.setLoginName(staffId);
		u.setLoginDate(new Date());
		u.setState(1);
		return u;
	}

	public List<User> listOnlineUsers(){
		List<User> ret = new ArrayList<User>();
		Collection<Session> sessions = sessionDao.getActiveSessions();
		for(Session s : sessions) {
			User u = (User)s.getAttribute(ShiroDbRealm.SESSION_USER_KEY);
			if(u != null) {
				ret.add(u);
			}
		}
		return ret;
	}

	/**
	 * 用户强制下线
	 * @param staffId
	 */
	public void outUser(String staffId) {
		Collection<Session> sessions = sessionDao.getActiveSessions();
		List<String> rmk = new ArrayList<String>();
		for(Session s : sessions) {
			User u = (User)s.getAttribute(ShiroDbRealm.SESSION_USER_KEY);
			if(u != null && u.getStaffId().equals(staffId)) {
				//不能移除自己
				if(u.getStaffId().equals(SecurityUtils.getSubject().getPrincipal().toString())) {
					continue;
				}
				sessionDao.delSession(s);  //从 redis 移除
				rmk.add(s.getId().toString());
				//ShiroSessionCacheService.getCache().remove(s.getId());  //从缓存移除
			}
		}
	}

	public User getUserBySSO(String staffId){
		Map<String, Object> urlVariables = new HashMap<String, Object>();  //参数
		urlVariables.put("staffId", staffId);
		JSONObject user = this.restTemplate.getForEntity(userInfoByStaffUrl, JSONObject.class, urlVariables).getBody();
		if(user == null || user.isEmpty()) {
			return null;
		}
		User u = JSON.toJavaObject(user, User.class);
		return u;
	}

	public User getUserByUserId(String staffId){
		if(userInfoByStaffUrl != null && userInfoByStaffUrl.length() > 0) {
			return this.getUserBySSO(staffId);
		}else {
			return mapper.getUserByStaffId(staffId);
		}
	}

	public User getUserByWxid(String wxid){
		return mapper.getUserByWxid(wxid);
	}

	public void updateLogDateAndCnt(String userId){
		User u = new User();
		u.setId(userId);
		mapper.updateLogDateAndCnt(u);
	}

	public void updateWxidByStaffid(String staffId, String wxid){
		mapper.updateWxidByStaffid(staffId, wxid);
	}

	public void modPsd(User u){
		mapper.modPsd(u);
	}

	public String checkPsd(String userId){
		return mapper.checkPsd(userId);
	}

	public String shiroWXLogin(String wxid){
		RsbiUsernamePasswordToken token = new RsbiUsernamePasswordToken(wxid, "XXXXXX", null);

		token.setChkpsd(false);
		token.setWxLogin(true);
		try{
			 SecurityUtils.getSubject().login(token);
		}catch(Exception ex){
			log.error("登录出错。", ex);
	        return "内部错误，请重试！";
		}
		return "SUC";
	}

	/**
	 * 通过jwt登录系统
	 * @param userName
	 * @return
	 */
	public String shiroJWTLogin(String userName){
		RsbiUsernamePasswordToken token = new RsbiUsernamePasswordToken(userName, "XXXXXX", null);
		token.setChkpsd(false);
		token.setJwtLogin(true);
		token.setRememberMe(false);
		try{
			SecurityUtils.getSubject().login(token);
		}catch(Exception ex){
			log.error("登录出错。", ex);
			return "内部错误，请重试！";
		}
		return "SUC";
	}

	/**
	 * 通过token进行SSO登录 (采用同步关键字，防止多线程同时登录)
	 * @param rsbiToken
	 * @return
	 */
	public String shiroSSOLogin(String rsbiToken, HttpServletRequest request){
		RsbiUsernamePasswordToken token = new RsbiUsernamePasswordToken(rsbiToken, "XXXXXX", null);
		token.setRememberMe(false);
		token.setChkpsd(false);
		token.setRequest(request);
		SecurityUtils.getSubject().login(token);
		return "SUC";
	}

	public String shiroLogin(String userName, String password, Boolean rememberme, HttpServletRequest request){
		RsbiUsernamePasswordToken token = new RsbiUsernamePasswordToken(userName, password, null);
	    //token.setRememberMe(rememberme);
		token.setRememberMe(true);
		token.setRequest(request);
	    // shiro登陆验证
	    try {
	        SecurityUtils.getSubject().login(token);
	    } catch (UnknownAccountException ex) {
	        return "账号不存在！";
	    } catch (IncorrectCredentialsException ex) {
	        return "密码错误！";
	    } catch (AuthenticationException ex) {
	    	String ret = null;
	    	Throwable t = ex;
	    	while(true){
	    		if(t.getCause() == null) {
	    			ret = t.getMessage();
	    			break;
	    		}
	    		t = t.getCause();
	    		if(t.getCause() == null){
	    			ret = t.getMessage();
	    			break;
	    		}
	    	}
	    	return ret;
	    } catch (Exception ex) {
	    	log.error("登录出错。", ex);
	        return "内部错误，请重试！";
	    }
	    return "SUC";
	}

	/**
	 * 根据token 获取用户信息
	 * @param token
	 * @return
	 */
	public User getUserInfoByToken(String token){
		if(this.userInfoByTokenUrl == null || this.userInfoByTokenUrl.length() == 0){
			throw new AuthenticationException("未配置 sso.url.userInfo.bytoken 的URL");
		}
		try{
			String url = this.userInfoByTokenUrl + (this.userInfoByTokenUrl.indexOf("?") > 0 ?"&":"?") + "token=" + token;
			ResponseEntity<String> resp = this.restTemplate.getForEntity(url, String.class);
			String info = resp.getBody();
			JSONObject json = (JSONObject)JSON.parse(info);
			User user = JSONObject.toJavaObject(json, User.class);
			user.setUrls(menuService.listUserVisitUrl(user.getId()));
			return user;
		}catch(Exception ex){
			log.error("SSO获取用户信息失败", ex);
			throw new AuthenticationException("SSO获取用户信息失败：" + ex.getMessage());
		}
	}

	/**
	 * 通过SSO接口获取用户列表
	 * @param page
	 * @param keyword
	 * @return
	 */
	public List<User> listSSOUsers(PageParam page, String keyword){
		try {
			String url = userListUrl;
			//参数
			Map<String, Object> request = new HashMap<String, Object>();  //参数
			request.put("page", page.getPage());  //第几页
			request.put("rows", page.getRows());	//每页多少记录
			request.put("keyword", keyword);
			//ResponseEntity<JSONArray> users = this.restTemplate.postForEntity(url, request, JSONArray.class);
			/**
			 get 请求，返回 JSONOObject
			 返回数据格式：
			 {
			 total:31,
			 data:[{"userId":1,"staffId":"admin","loginName":"系统管理员","gender":"女","state":1,"deptCode":"002"}]
			 }
			 */
			ResponseEntity<JSONObject> users = this.restTemplate.getForEntity(url, JSONObject.class, request);
			JSONObject usersEntity = users.getBody();
			if(usersEntity != null && !usersEntity.isEmpty()){
				Integer total = usersEntity.getInteger("total");
				page.setTotal(total);
				List<User> ret = new ArrayList<>();
				JSONArray ls = usersEntity.getJSONArray("data");
				for(int i=0; i<ls.size(); i++) {
					User u = JSON.toJavaObject(ls.getJSONObject(i), User.class);
					ret.add(u);
				}
				return ret;
			}else{
				throw new Exception("返回数据为空！");
			}
		}catch(Exception ex) {
			log.error("SSO获取用户信息失败", ex);
			return null;
		}
	}

	/**
	 * 是否从sso获取登录用户
	 * @return
	 */
	public boolean isSSOUserList() {
		if(userListUrl == null || userListUrl.length() == 0) {
			return false;
		}else {
			return true;
		}
	}
	/**
	 * 查询不在角色下的用户列表
	 * @param roleId
	 * @return
	 */
	public List<User> listRoleNotUser(String roleId){
		return mapper.listRoleNotUser(roleId);
	}

}
