/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.auth;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.frame.Role;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.mapper.frame.RoleMapper;
import com.ruisitech.bi.util.RSBIUtils;
import com.ruisitech.bi.util.TreeInterface;
import com.ruisitech.bi.util.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 权限管理 - 角色管理模块
 * @author gdp
 *
 */
@Service
public class AuthRoleService {

	@Resource
	private RoleMapper mapper;

	@Resource(name = "sysDaoHelper")
	private DaoHelper daoHelper;

	@Value("${spring.datasource.dbType}")
	private String dbName;


	public Map<String, Object> listRoleMenus(String roleId){
		List<Map<String, Object>> menus = mapper.listRoleMenus(roleId);
		TreeService tree = new TreeService();
		List<Map<String, Object>> ret = tree.createTreeData(menus, new TreeInterface(){
			@Override
			public void processMap(Map<String, Object> m) {
				Object id2 = m.get("id2");
				if(id2 != null){
					Map<String, Object> state = new HashMap<>();
					state.put("selected", true);
					m.put("state", state);
				}
			}

			@Override
			public void processEnd(Map<String, Object> m, boolean hasChild) {
				if(hasChild){
					m.put("icon", "fa fa-folder-o");
					m.remove("state");
				}else {
					m.put("icon", "fa fa-file-o");
				}
			}

		});
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", "root");
		m.put("text", "系统菜单树");
		m.put("icon", "fa fa-home");
		m.put("children", ret);
		return m;
	}

	public 	List<Role> listUserHasRole(String userId){
		return mapper.listUserHasRole(userId);
	}

	public List<Role> list(String keyword){
		return mapper.list(keyword);
	}

	/**
	 * 查询所有角色及用户所有的角色
	 * @param userId
	 * @return
	 */
	public List<Role> listUserRole(String userId){
		return mapper.listUserRole(userId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void addUserRole(String[] roleIds, String userId) {
		String dateKey = RSBIUtils.getDbDateFunction(dbName);
		//删除角色
		daoHelper.execute("delete from role_user_rela where user_id = '" + userId+"'");
		for(int i=0; roleIds != null && i<roleIds.length; i++)
		{
			String roleId = roleIds[i];
			String sql = "insert into role_user_rela(id,user_id,role_id,create_user,create_date,update_date) " +
					"values(?,?,?,?,"+dateKey+","+dateKey+")";
			daoHelper.execute(sql, ps -> {
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				ps.setString(1, id);
				ps.setString(2, userId);
				ps.setString(3, roleId);
				String logUser = RSBIUtils.getLoginUserInfo().getId();
				ps.setString(4, logUser);
				ps.executeUpdate();
				return null;
			});
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveRole(Role role) {
		String dateKey = role.getDateString();
		User user = RSBIUtils.getLoginUserInfo();
		String sql = "insert into sc_role(id, role_name,role_desc,create_date,create_user, ord, update_date) values(?,?,?,"+dateKey+",?,?, "+dateKey+")";
		daoHelper.execute(sql, ps -> {
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			ps.setString(1, id);
			ps.setString(2, role.getRoleName());
			ps.setString(3, role.getRoleDesc());
			ps.setString(3, user.getLoginName());
			ps.setInt(4, role.getOrd());
			ps.setString(5, user.getId());
			ps.executeUpdate();
			return null;
		});
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateRole(Role role) {
		String datekey = RSBIUtils.getDbDateFunction(dbName);
		String sql = "update sc_role set role_name = ?,role_desc = ?, ord=?, update_date="+datekey+" where id = ?";
		daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, role.getRoleName());
				ps.setString(2, role.getRoleDesc());
				ps.setInt(3, role.getOrd());
				ps.setString(4, role.getId());
				ps.executeUpdate();
				return null;
			}
		});
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(String id) {
		String sql = "delete from sc_role where id = '" + id+"'" ;
		daoHelper.execute(sql);
		//删除角色菜单关系
		daoHelper.execute("delete from role_menu_rela where role_id = '" + id + "'");
		//删除角色用户关系
		daoHelper.execute("delete from role_user_rela where role_id = '" + id + "'");
	}

	public Role getRole(String id) {
		return mapper.getById(id);
	}

	@Transactional(rollbackFor = Exception.class)
	public void roleMenu(String menuIds, String roleId) {
		String dateKey = RSBIUtils.getDbDateFunction(dbName);
		//删除以前数据
		String delSql = "delete from role_menu_rela where role_id = '" + roleId+"'";
		daoHelper.execute(delSql);
		if(menuIds.length() > 0) {
			String[] ids = menuIds.split(",");//处理获取的菜单ID格式
			String sql = "insert into role_menu_rela(id,role_id,menu_id,create_user,create_date,update_date) " +
					"values(?,?,?,?,"+dateKey+","+dateKey+")";
			daoHelper.execute(sql, ps -> {
				for (String tmp : ids) {
					ps.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
					ps.setString(2, roleId);
					ps.setString(3, tmp);
					ps.setString(4, RSBIUtils.getLoginUserInfo().getId());
					ps.addBatch();
				}
				ps.executeBatch();
				return null;
			});
		}
	}

	public void roleOneMenu(String menuId, String roleId){
		String dateKey = RSBIUtils.getDbDateFunction(dbName);
		String sql = "insert into role_menu_rela(id, role_id, menu_id, create_user, create_date, update_date) " +
				"values(?,?,?,?,"+dateKey+","+dateKey+")";
		daoHelper.execute(sql, (ps)->{
			ps.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
			ps.setString(2, roleId);
			ps.setString(3, menuId);
			ps.setString(4, RSBIUtils.getLoginUserInfo().getId());
			ps.executeUpdate();
			return null;
		});
	}

	@Transactional(rollbackFor = Exception.class)
	public void roleUserSave(Map<String, Object> opms){
		String dateKey = RSBIUtils.getDbDateFunction(dbName);
		String roleId = (String) opms.get("roleId");
		List<String> users = (List<String>)opms.get("users");
		String sql = "insert into role_user_rela(id,user_id,role_id,create_user, create_date, update_date) " +
				"values(?,?,?,?,"+dateKey+","+dateKey+")";
		this.daoHelper.execute(sql, ps -> {
			for(String u : users){
				ps.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
				ps.setString(2, u);
				ps.setString(3, roleId);
				ps.setString(4, RSBIUtils.getLoginUserInfo().getId());
				ps.addBatch();
			}
			ps.executeBatch();
			return null;
		});
	}
	/**
	 * 查询角色下的用户列表
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> listRoleUsers(String roleId){
		return mapper.listRoleUser(roleId);
	}
}
