package com.bjca.ecopyright.system.back;

import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.soft.service.SoftwareService;
import com.bjca.ecopyright.statuscode.Constants;
import com.bjca.ecopyright.system.model.*;
import com.bjca.ecopyright.system.service.OperationLogService;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.ecopyright.util.MyDate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/system")
public class AdminLoginController {
	Log log = LogFactory.getLog(AdminLoginController.class);
	@Autowired
	private SystemService systemService;
	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private OperationLogService operationLogService;
	/**
	 * 管理员登录
	 * 
	 * @param request
	 * @param j_username
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login.do",method = {RequestMethod.POST})
	 	public String login(HttpServletRequest request,String j_username,String keypassword, ModelMap model)throws Exception{
	 		HttpSession session = request.getSession();
	 		//用户名
	        String loginName = j_username;
	        //密码
	        String loginPsw = keypassword;
	        //用户名，密码或者验证码为空
	        if (Function.isEmpty(loginName) || Function.isEmpty(loginPsw)) {
	            session.setAttribute("flag", "passwordError");
	            return "redirect:/manage.jsp";
	        }
	     	// 加密后的用户输入密码
	     	loginPsw = Function.MD5(loginPsw);
	     	Admin admin = null;
	        //根据用户名查询当前用户
        	admin = systemService.loginByName(loginName);
	        if(admin==null){
				//用户不存在
				session.setAttribute("flag", "usererror");
			 	return "redirect:/manage.jsp";
	        }
	        String loginPassword = admin.getPassword(); // 加密后的数据库中的正确密码
			if (!loginPsw.equals(loginPassword)) {
				// 密码错误
				log.debug("密码错误。。。。");
				return "redirect:/manage.jsp";
			}
		//2016-10-09 添加权限功能 开始
		List<String> menus0 = new ArrayList<>();//当前用户所有对应按钮的id
		List<Menu> menus1 = new ArrayList<>();//当前用户对应的所有一级按钮
		/** 查询当前用户所有角色 **/
		List<UserRole> userRoles = admin.getUserRoleList();
		for (UserRole userRole : userRoles) {
			Role role = userRole.getRole();
			/** 当前角色对应所有的角色菜单关系列表 **/
			List<RoleMenu> roleMenus = role.getRoleMenus();
			for (RoleMenu roleMenu : roleMenus) {
				Menu menu = roleMenu.getMenu();
				if(menu!=null){
					menus0.add(menu.getId());
					if(menu.getLevel() == 1){//一级
						menus1.add(roleMenu.getMenu());
					}
				}
			}
		}
		/** 去除该用户没选中的按钮，只显示当前用户选中的按钮，最终形成一个menus1，然后依次获取二级三级四级按钮  **/
		for (Menu menu : menus1) {
			List<Menu> child2 = new ArrayList<>();
			List<Menu> children2 = menu.getChildren();
			for (Menu menu2 : children2) {
				List<Menu> child3 = new ArrayList<>();
				if(menus0.contains(menu2.getId())){//二级
					child2.add(menu2);
					List<Menu> children3 = menu2.getChildren();
					for (Menu menu3 : children3) {
						List<Menu> child4 = new ArrayList<>();
						if(menus0.contains(menu3.getId())){//三级
							child3.add(menu3);
							List<Menu> children4 = menu3.getChildren();
							for (Menu menu4 : children4) {
								if(menus0.contains(menu4.getId())){//四级
									child4.add(menu4);
								}
							}
						}
						menu3.setChildren(child4);
					}
				}
				menu2.setChildren(child3);
			}
			menu.setChildren(child2);
		}

		//2016-10-09 添加权限功能 结束

		// 保存当前管理员session
		session.setAttribute(Constants.SESSION_ADMIN, admin);
		session.setAttribute("menus1", menus1);
		
		model.put("admin",admin);
		int todayNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(new Date()));
		Date date1 = new Date();
		date1.setDate(date1.getDate()+1);
		Date date2 = new Date();
		date2.setDate(date2.getDate()+2);
		Date date3 = new Date();
		date3.setDate(date3.getDate()+3);
		Date date4 = new Date();
		date4.setDate(date4.getDate()+4);
		int tomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date1));
		int afterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date2));
		int nextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date3));
		int doubleNextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date4));
		model.put("todayNum", todayNum);
		model.put("tomorrowNum", tomorrowNum);
		model.put("afterTomorrowNum", afterTomorrowNum);
		model.put("nextAfterTomorrowNum", nextAfterTomorrowNum);
		model.put("doubleNextAfterTomorrowNum", doubleNextAfterTomorrowNum);
		
		
		return "back/system/menuIndex";
	       /* if ("1".equals(admin.getRoleId())) {
	 			log.debug("管理员登录");
	 			return "redirect:index.do";
	 		}else
	        if ("3".equals(admin.getRoleId())) {
	        	log.debug("文件流转操作员登录");
	        	return "redirect:auditIndex.do";
	        }else{
	        	log.error("未知类型");
	        	return "redirect:/manage.jsp";
	        }*/
	 	}

	/**
	 * 管理员注销
	 * 
	 * @param request
	 * @param httpServletResponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout.do", method = { RequestMethod.GET })
	public String logout(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute(Constants.SESSION_ADMIN);
		session.invalidate();
		return "redirect:/manage.jsp";
	}

	@RequestMapping(value = "/tab.do", method = { RequestMethod.GET })
	public String sysTab(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws Exception {
		return "back/system/tab";
	}

	@RequestMapping(value = "/default.do", method = { RequestMethod.GET })
	public String sysDefault(){
		return "back/system/default";
	}

	/**
	 * 首页日历数据
	 * @param request
	 * @param httpServletResponse
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/calendar.do", method = { RequestMethod.GET })
	@ResponseBody
	public String calendar(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
		String beginDate = request.getParameter("start");
		String endDate = request.getParameter("end");

		log.debug(beginDate);
		log.debug(endDate);

		List<SoftwareVO> expectzhizhengList = new LinkedList<SoftwareVO>();//预期待制证列表
		List<SoftwareVO> daizhizhengList = new LinkedList<SoftwareVO>();//带制证列表
		List<SoftwareVO> shenheList = new LinkedList<SoftwareVO>();//待审核列表
		List<SoftwareVO> retList = new LinkedList<SoftwareVO>();//待审核列表

		expectzhizhengList = softwareService.queryExpectMakeCardList(beginDate,endDate);
		daizhizhengList = softwareService.queryMakeCardList(beginDate,endDate);
		shenheList = softwareService.queryAuditList(beginDate,endDate);

		for (SoftwareVO softVO:expectzhizhengList) {
			softVO.setTotal("预期制证量"+softVO.getTotal());
			softVO.setColor("orange");
			retList.add(softVO);
		}
		for (SoftwareVO softVO:daizhizhengList) {
			softVO.setTotal("待制证量"+softVO.getTotal());
			softVO.setColor("blue");
			retList.add(softVO);
		}
		for (SoftwareVO softVO:shenheList) {
			softVO.setTotal("待分审量"+softVO.getTotal());
			softVO.setColor("green");
			retList.add(softVO);
		}
		String retString = JsonUtils.objectToJsonString(retList);
		return retString;
	}


	/**
	 * 查询出证日期
	 * 根据出证日期获取当日待出证的数量
	 * @author wangna
	 */
	@RequestMapping(value = "/index.do", method = { RequestMethod.GET })
	public String index(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelMap model) throws Exception {
		HttpSession session = request.getSession();
		Admin admin = (Admin)session.getAttribute(Constants.SESSION_ADMIN);
		model.put("admin",admin);
		int todayNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(new Date()));
		Date date1 = new Date();
		date1.setDate(date1.getDate()+1);
		Date date2 = new Date();
		date2.setDate(date2.getDate()+2);
		Date date3 = new Date();
		date3.setDate(date3.getDate()+3);
		Date date4 = new Date();
		date4.setDate(date4.getDate()+4);
		int tomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date1));
		int afterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date2));
		int nextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date3));
		int doubleNextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date4));
		model.put("todayNum", todayNum);
		model.put("tomorrowNum", tomorrowNum);
		model.put("afterTomorrowNum", afterTomorrowNum);
		model.put("nextAfterTomorrowNum", nextAfterTomorrowNum);
		model.put("doubleNextAfterTomorrowNum", doubleNextAfterTomorrowNum);
		return "back/system/index";
	}

	@RequestMapping(value = "/auditIndex.do", method = { RequestMethod.GET })
	public String auditIndex(HttpServletRequest request, HttpServletResponse httpServletResponse, ModelMap model) throws Exception {
		HttpSession session = request.getSession();
		Admin admin = (Admin)session.getAttribute(Constants.SESSION_ADMIN);
		model.put("admin",admin);
		int todayNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(new Date()));
		Date date1 = new Date();
		date1.setDate(date1.getDate()+1);
		Date date2 = new Date();
		date2.setDate(date2.getDate()+2);
		Date date3 = new Date();
		date3.setDate(date3.getDate()+3);
		Date date4 = new Date();
		date4.setDate(date4.getDate()+4);
		int tomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date1));
		int afterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date2));
		int nextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date3));
		int doubleNextAfterTomorrowNum = softwareService.findSoftwareByCertificatedate(MyDate.toString(date4));
		model.put("todayNum", todayNum);
		model.put("tomorrowNum", tomorrowNum);
		model.put("afterTomorrowNum", afterTomorrowNum);
		model.put("nextAfterTomorrowNum", nextAfterTomorrowNum);
		model.put("doubleNextAfterTomorrowNum", doubleNextAfterTomorrowNum);
		return "back/system/auditIndex";
	}
	@RequestMapping(value = "/proxyIndex.do", method = { RequestMethod.GET })
	public String proxyIndex(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws Exception {
		return "back/system/proxyIndex";
	}
	@RequestMapping(value = "/makeCertIndex.do", method = { RequestMethod.GET })
	public String makeCertIndex(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws Exception {
		return "back/system/makeCertIndex";
	}
	@RequestMapping(value = "/financeIndex.do", method = { RequestMethod.GET })
	public String financeIndex(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws Exception {
		return "back/system/financeIndex";
	}
	@RequestMapping(value = "/reviewIndex.do", method = { RequestMethod.GET })
	public String reviewIndex(HttpServletRequest request, HttpServletResponse httpServletResponse, Model model) throws Exception {
		return "back/system/reviewIndex";
	}

}
