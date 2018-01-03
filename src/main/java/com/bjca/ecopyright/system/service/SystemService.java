package com.bjca.ecopyright.system.service;

import java.util.List;
import java.util.Map;

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;


public interface SystemService {

	/**
	 * 系统管理员登录
	 * @param userName
	 * @return
	 */
	public Admin loginByName(String userName);
	
	
	/**
	 * 添加管理员或更新管理员
	 * @param admin
	 * @return
	 */
	public boolean saveAdmin(Admin admin);
	
	/**
	 * 根据删除管理员
	 * @param id
	 * @return
	 */
	public boolean deleteAadmin(String id);
	/**
	 * 查询所有用户
	 * @param param
	 * @return
	 */
	public PageObject queryAdmin(QueryParamater param);

    /**
     * 按条件查询
     * @param param
     * @return
     */
	public List<Admin> queryAdminByParam(Map<String, Object> param);
	/**
	 * 根据ID查询用户
	 * @return
	 */
	public Admin queryAdminByID(String adminID);

	/**
	 * 根据查询所有用户
	 * @return
	 */
	public List<Admin> queryAllAdmin();

	/**
	 * 根据角色查询
	 * @param string
	 * @return
	 * @author bxt-chen
	 */
	public List<Admin> queryAdminByRole(String string);

	/**
	 * 保存用户和角色信息
	 * @author chenping
	 * @param admin
	 * @param roleIdArray
     * @return
     */
	public boolean saveAdminAndRole(Admin admin, String roleIdArray);

	/**
	 * 查询出所有的审核员
	 * @return
     */
	public List<Admin> queryAdminByRoleName(Map map);
	
	
	/**
	 * 根据类型查找审查员（其中此方法为了雍和操作添加分审人员而添加，）
	 * @date 2016-12-14上午11:45:58
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param type
	 * @return
	 */
	public Admin searchAdminByType(String type);


}
