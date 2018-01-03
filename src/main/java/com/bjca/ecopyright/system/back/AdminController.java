package com.bjca.ecopyright.system.back;

/**
 * 管理员操作
 */

import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.Role;
import com.bjca.ecopyright.system.model.UserRole;
import com.bjca.ecopyright.system.service.RoleService;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/back/system")
public class AdminController {


	@Autowired
	private SystemService systemService;
	@Autowired
	private RoleService roleService;

	/**
	 * 查询所有管理员用户
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAllAdmin.do")
	public String queryAllAdmin(HttpServletResponse response, HttpServletRequest request
			,ModelMap modelMap) throws Exception {
		QueryParamater param = new QueryParamater();
		//分页
		if(!Function.isEmpty(request.getParameter("pageCurrent"))){
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0)-1);//当前页
			param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));//每页个数
		}
		
		String loginName = request.getParameter("loginName");
		String relName = request.getParameter("relName");
		String roleId = request.getParameter("role");
		String state = request.getParameter("state");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		/** 添加登录名查询 **/
		if (!Function.isEmpty(loginName)) {
			param.put("loginName", loginName.trim());
			modelMap.put("loginName", loginName.trim());
		}
		/** 添加真实姓名查询 **/
		if (!Function.isEmpty(relName)) {
			param.put("relName", relName.trim());
			modelMap.put("relName", relName.trim());
		}
		/** 添加角色查询 **/
		if (!Function.isEmpty(roleId)) {
			param.put("roleId", roleId);
			modelMap.put("roleId", roleId);
		}
		/** 添加状态查询 **/
		if (!Function.isEmpty(state)) {
			param.put("state", state);
			modelMap.put("state", state);
		}
		/** 添加手机号查询 **/
		if (!Function.isEmpty(phone)) {
			param.put("phone", phone.trim());
			modelMap.put("phone", phone.trim());
		}
		/** 添加email查询 **/
		if (!Function.isEmpty(email)) {
			param.put("email", email.trim());
			modelMap.put("email", email.trim());
		}
		PageObject po = systemService.queryAdmin(param);
		modelMap.put("po", po);
		// 查询角色列表
		List<Role> roleList = roleService.selectAll();
		modelMap.put("roleList", roleList);
		return "back/system/adminList";
	}

	/**
	 * 更新或者保存首先调用页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveAdminForm.do")
	public String form(HttpServletRequest request, ModelMap model) {
		String adminID = request.getParameter("id");
		String flag = request.getParameter("flag");
		Admin admin = null;
		// 查询角色列表
		List<Role> roleList = roleService.selectAll();
		model.put("roleList", roleList);
		if (Function.isEmpty(adminID)) {
			admin = new Admin();
			model.put("adminForm", admin);
		} else {
			admin = this.systemService.queryAdminByID(adminID);
			if (admin != null) {
				// 如果admin不为空的话，将role拼成字符串
				List<UserRole> userRoleList = admin.getUserRoleList();
				String roleIdStr = "";
				for (int i = 0; i < userRoleList.size(); i++) {
					UserRole role = userRoleList.get(i);
					if (i != 0) {
						roleIdStr += ",";
					}
					roleIdStr += role.getRoleId();
				}
				model.put("roleIdStr", roleIdStr);
				model.put("adminForm", admin);
			} else {
				// 获取ID失败 。。。。
				model.put("adminForm", new Admin());
			}

		}
		model.put("flag", flag);

		return "/back/system/adminForm";// form
	}
	
	/**
	 * 初始化管理员信息页面
	 * @date 2016-5-19上午10:53:42
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/saveAdminAuditForm.do")
	public String showAdminMessage(HttpServletRequest request, ModelMap model) {
		Admin admin1 = Admin.sessionAdmin();
		String id = admin1.getID();
		Admin admin = this.systemService.queryAdminByID(id);
		if (admin != null) {
			if ( admin.getRoleId()!=null) {
				model.put("adminForm", admin);
			}
		}else {
			model.put("adminForm", new Admin());
		}
		return "/back/system/adminAuditForm";// form
	}
	/**
	 * 更新或者添加保存提交
	 * @param admin
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveAdmin.do", method = { RequestMethod.POST })
	// post
	public String saveAdmin( Admin admin, ModelMap model) {

		if (Function.isEmpty(admin.getID())) {// 保存
			
			//首先判断数据库中是否存在该用户名
			String nowName =  admin.getLoginName();//获取当前输入用户名
			//根据用户名查询用户
			Admin admin2 = this.systemService.loginByName(nowName);
			if(admin2==null){//查询出来为空则数据库中不存在该用户名
				this.systemService.saveAdmin(admin);
			}else{
				model.put("adminForm", admin);
				model.put("erroInfo", "该用户名已存在");
				System.out.println("该用户名已存在");
				return "/back/system/adminForm";
			}
			
			
		} else {// 更新
			
			this.systemService.saveAdmin(admin);
		}

		return "redirect:/back/system/queryAllAdmin.do";// 保存成功后的页面
	}
	
	/**
	 * 根据用户ID删除用户
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/removeAdmin.do")
	@ResponseBody
	public String removeAdmin(HttpServletResponse response, HttpServletRequest request) throws Exception {
		/*String adminID = request.getParameter("ids");
				systemService.deleteAadmin(adminID);
		return "redirect:/back/system/queryAllAdmin.do";*/
		String id =request.getParameter("id");
		if (!Function.isEmpty(id)) {
			systemService.deleteAadmin(id);
			return "success";
		}
		return "";
	}
	
	/**
	 * 修改账户后保存提交
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateAdmin.do", method = { RequestMethod.POST })
	@ResponseBody
	public String updateAdmin(HttpServletRequest request, ModelMap modelMap
			) throws Exception {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String relName = request.getParameter("relName");
		String state = request.getParameter("state");
		String authorizationcode = request.getParameter("authorizationcode");
		String roleIdArray = request.getParameter("roleIdArray");
		/** 执行数据保存 **/
		Admin admin = this.systemService.queryAdminByID(id);
		if(admin!= null){
			if (!Function.isEmpty(password)) {
				admin.setPassword(password);
			}
			admin.setEmail(email);
			admin.setPhone(phone);
			admin.setRelName(relName);
			admin.setRelName(relName);
			admin.setState(state);
			admin.setAuthorizationcode(authorizationcode);
			//admin.setAuthorizationcode("");
			//systemService.saveAdmin(admin);
			systemService.saveAdminAndRole(admin, roleIdArray);
			return "success";
		}else {
			admin = new Admin();
			String loginName = request.getParameter("loginName");
			admin.setLoginName(loginName);
			admin.setPassword(password);
			admin.setEmail(email);
			admin.setPhone(phone);
			admin.setRelName(relName);
			admin.setState(state);
			admin.setAuthorizationcode(authorizationcode);
			/*if("2".equals(role)){
				admin.setAuthorizationcode("");
			}*/
			systemService.saveAdminAndRole(admin, roleIdArray);
			//systemService.saveAdmin(admin);
			return "success";
		}
	}

}
