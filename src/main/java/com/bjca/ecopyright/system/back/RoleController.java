package com.bjca.ecopyright.system.back;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.system.model.Role;
import com.bjca.ecopyright.system.service.MenuService;
import com.bjca.ecopyright.system.service.RoleMenuService;
import com.bjca.ecopyright.system.service.RoleService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 权限角色管理
 * @author wangw
 * @date 2016年9月7日
 */
@Controller
@RequestMapping("/back/role")
public class RoleController {
	
	Log log = LogFactory.getLog(RoleController.class);
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private RoleMenuService roleMenuService;

	/**
	 * 跳转至角色显示列表
	 * @param request
	 * @param response
	 * @param modelMap
     * @return
     */
	@RequestMapping("/roleList.do")
	public String roleList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
		List<Role> roleList = roleService.selectAll();
		modelMap.put("roleList",roleList);
		return "/back/system/roleList";
	}

	/**
	 * 跳转至新建角色列表
	 * @param request
	 * @param response
	 * @param modelMap
     * @return
     */
	@RequestMapping(value="/saveRole.do")
	public String updateRole(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
		String roleId = request.getParameter("id");
		String flag = request.getParameter("flag");
		Role role = null;
		if (Function.isEmpty(roleId)) {
			role = new Role();
			modelMap.put("roleForm", role);
		} else {
			role = roleService.findRole(roleId);
			if (role != null) {
				modelMap.put("roleForm", role);
			} else {
				modelMap.put("roleForm", new Role());
			}

		}
		modelMap.put("flag", flag);
		return "/back/system/roleForm";
	}


	/**
	 * 更新或者保存角色
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/updateRole.do", method = { RequestMethod.POST })
	@ResponseBody
	public String updateAdmin(HttpServletRequest request, ModelMap modelMap) throws Exception {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String sort = request.getParameter("sort");

		Role role = this.roleService.findRole(id);
		if(role!=null){
			role.setName(name);
			role.setDescription(description);
			role.setUpdatedate(new Date());
			role.setSort(Integer.parseInt(sort));
			roleService.saveRole(role);
			return "success";
		}else{
			role = new Role();
			role.setName(name);
			role.setDescription(description);
			role.setCreatedate(new Date());
			role.setUpdatedate(new Date());
			role.setSort(Integer.parseInt(sort));
			roleService.saveRole(role);
			return "success";
		}
	}

	/**
	 * 跳转至权限管理首页
	 * @param request
	 * @param modelMap
     * @return
     */
	@RequestMapping("/powerList.do")
	public String powerList(HttpServletRequest request, ModelMap modelMap){
		//查出所有的角色
		List<Role> roleList = roleService.selectAll();
		modelMap.put("roleList",roleList);
		//查出所有的菜单
		//查询list列表
		String ret = menuService.queryMenuForTree();
		modelMap.put("treeData", ret);
		return "/back/system/powerList";
	}

	/**
	 * 通过角色获取菜单
	 * @param request
	 * @param modelMap
     * @return
     */
	@RequestMapping("/getPowerByRole.do")
	@ResponseBody
	public String getPowerByRole(HttpServletRequest request, ModelMap modelMap){
		String roleID = request.getParameter("roleID");
		List<Menu> menuList = roleMenuService.getMenuByRole(roleID);
		return JsonUtils.objectToJsonString(menuList);
	}



	/**
	 * 更新菜单数据
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value="/updatePower.do",method = RequestMethod.POST)
	@ResponseBody
	public String updatePower(HttpServletRequest request, ModelMap modelMap){
		String roleID = request.getParameter("roleID");
		String menuIDs = request.getParameter("menuIDs");
		boolean flag = false;
		if (!Function.isEmpty(menuIDs)||!Function.isEmpty(roleID)) {
			flag = roleMenuService.updateMenuByRole(roleID,menuIDs);
			return flag?"success":"fail";
		}else{
			return "fail";
		}

	}

}
