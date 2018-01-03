package com.bjca.ecopyright.soft.back;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.model.SoftwareVO;
import com.bjca.ecopyright.soft.service.SoftwareHistoryService;
import com.bjca.ecopyright.soft.service.SoftwareOperationLogService;
import com.bjca.ecopyright.soft.service.SoftwareService;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.statuscode.SoftwareOperationEnum;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonDateValueProcessor;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.ecopyright.util.MyDate;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.service.StorageService;
import com.bjca.ecopyright.warehouse.service.StorageSoftwareService;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

/**
 * 软件基本信息
 * 
 * @author hm
 * 
 */
@Controller
@RequestMapping("/back/softinfo")
public class SoftwareController extends AbstractExcelView {
	Log	log	= LogFactory.getLog(SoftwareController.class);

	private int	exp	= 30 * 60;

	@Autowired
	private SoftwareService			softwareService;

	@Autowired
	private StorageService			storageService;

	@Autowired
	private StorageSoftwareService	storageSoftwareService;

	@Autowired
	private MemcachedClient			memcachedClient;

	@Autowired
	private SystemService			systemService;
	
	@Autowired
	private SoftwareOperationLogService logService;
	
	@Autowired
	private SoftwareHistoryService softwareHistoryService;

	/**
	 * Excel导入（只针对初始Excel导入）
	 * 
	 * @param
	 * @return
	 * @author bxt-chenjian
	 * @date 2016.5.19
	 */
	@RequestMapping("/importExcel.do")
	@ResponseBody
	public JsonResult importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
		JsonResult result = new JsonResult();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		jsonConfig.setExcludes(new String[] { "storage" });
		boolean flag = false;
		List<Software> failSoftList = new ArrayList<Software>();// 数据库中已存在数据
		// 将上传文件缓存
		try{
			failSoftList = this.softwareService.importExcelForRuku(file.getInputStream());
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		log.debug("数据库中已存在数据条数：" + failSoftList.size());
		result.setState(flag);
		result.setMsg("该记录已导入数据库中");
		result.setData(JSONArray.fromObject(failSoftList, jsonConfig).toString());
		return result;
	}


	/**
	 * 跳转至分审界面
	 * 
	 * @param request
	 * @return
	 * @author bxt-chenjian
	 * @date 2016.5.25
	 */
	@RequestMapping("/fenshen.do")
	public String fenshen(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		/** 查询所有审核员：分审时选择分审员用 **/
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "审查员");
		paramMap.put("state", "1");
		List<Admin> shenheAdminList = this.systemService.queryAdminByRoleName(paramMap);
		Map<String, String> map = new HashMap<String, String>();
		for (Admin auditor : shenheAdminList) {
			map.put(auditor.getID(), auditor.getRelName());
		}
		modelMap.addAttribute("records", map);
		return "back/software/exportForAudit";
	}

	/**
	 * 分审案件 进行分审操作
	 * 
	 * @param request
	 * @return
	 * @author bxt-chenping
	 * @date 2016.5.25
	 */
	@RequestMapping("/exportForAudit.do")
	@ResponseBody
	public JsonResult exportForAudit(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String person = request.getParameter("auditor");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String serialNum = request.getParameter("num");
		// 后台数据校验
		if (Function.isEmpty(person)) {
			result.setState(false);
			result.setMsg("请选择审核员！");
			return result;
		}
		if (Function.isEmpty(startDate) || Function.isEmpty(endDate)) {
			result.setState(false);
			result.setMsg("请选择制证日期！");
			return result;
		}
		if (Function.isEmpty(serialNum) || Function.getLeagalLengthOfSerialNum() != serialNum.length()) {
			result.setState(false);
			result.setMsg("流水号输入有误！");
			return result;
		}
		// 对正确的文档进行出库操作
		// 对异常的文档进行出库操作
		result = this.softwareService.exportForAudit(serialNum, person, startDate, endDate);
		return result;
	}

	/**
	 * 案件查询(未制证)
	 * 
	 * @date 2016-5-25下午4:58:11
	 * @author wangna
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAllSoftware.do")
	public String queryAllSoftware(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws Exception {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
		//	param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		param.setNeverypage(150);//设置每页条数
		// 流水号
		String serialnum = request.getParameter("serialnum");
		// 操作员
		String adminid = request.getParameter("adminid");
		// 案件状态
		String softwarestatus = request.getParameter("softwarestatus");
		// 制证时间段
		String certificatedate1 = request.getParameter("certificatedate1");
		String certificatedate2 = request.getParameter("certificatedate2");
		// String relname = request.getParameter("relname");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "审查员");
		List<Admin> admins = this.systemService.queryAdminByRoleName(paramMap);
		Map<String, Object> ret1 = new HashMap<String, Object>();
		for (Admin s : admins) {
			ret1.put(s.getID(), s.getRelName());
		}
		modelMap.put("recordadmin", ret1);
		/*
		 * if(!Function.isEmpty(relname)){ param.put("relname", relname.trim());
		 * modelMap.put("relname", relname.trim()); }
		 */
		// 仓位查询
		String nameLevel = request.getParameter("nameLevel");
		List<Storage> storages = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "2");
		storages = (List<Storage>) this.storageService.queryStorageByParam(map);
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		for (Storage s : storages) {
			ret.put(s.getId(), s.getGroupname() + s.getName());
		}
		modelMap.put("records", ret);
		if(!Function.isEmpty(nameLevel) || 
		   !Function.isEmpty(serialnum) || 
		   !Function.isEmpty(adminid)   || 
		   !Function.isEmpty(softwarestatus) || 
		   (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2))){
			
			if (!Function.isEmpty(nameLevel)) {
				param.put("nameLevel", nameLevel.trim());
				modelMap.put("nameLevel", nameLevel.trim());
			}
			
			/** 添加流水号查询 **/
			if (!Function.isEmpty(serialnum)) {
				param.put("serialnum", serialnum.trim());
				modelMap.put("serialnum", serialnum.trim());
			}
			// ** 添加操作员查询 **//*
			if (!Function.isEmpty(adminid)) {
				param.put("adminid", adminid.trim());
				modelMap.put("adminid", adminid.trim());
			}
			/** 添加案件状态查询 **/
			if (!Function.isEmpty(softwarestatus)) {
				param.put("softwarestatus", softwarestatus);
				modelMap.put("softwarestatus", softwarestatus);
			}
			/** 添加时间查询 **/
			if (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2)) {
				
				param.put("certificatedate1", certificatedate1);
				modelMap.put("certificatedate1", MyDate.get(certificatedate1));
				
				param.put("certificatedate2", certificatedate2);
				modelMap.put("certificatedate2", MyDate.get(certificatedate2));
			}
			PageObject po = softwareService.querySoftInfoList(param);
			modelMap.put("po", po);
		}
		return "back/software/softwareList";
	}

	/**
	 * 跳转至入库页面 校验解锁二级仓库 同时查询所有可用的二级仓库
	 * 
	 * @author chenping
	 * @return
	 */
	@RequestMapping("/softInfoAuditList.do")
	public String jumpToRuKu(ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		List<Storage> list = this.storageService.getFStorage("1");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Storage storage : list) {
			result.put(storage.getId(), storage.getName());
		}
		modelMap.put("opts", result);
		return "back/software/ruku";
	}

	/**
	 * 锁定数据库
	 * 
	 * @author chenping
	 * @param request
	 * @return
	 */
	@RequestMapping("/lockStorage.do")
	@ResponseBody
	public String lockStorage(HttpServletRequest request) {
		String pos = request.getParameter("pos");
		Storage store = this.storageService.findStorage(pos);
		//判断是不是已经有人
		if(store.getLocksign()==1){
			return "alreadyLock";
		}else{
			boolean flag = this.storageService.lock(pos);
			if(flag){
				return "success";
			}else{
				return "fail";
			}
		}
	}

	/**
	 * 关闭页面 解锁仓库 清除缓存中的数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/shutdown.do", method = RequestMethod.POST)
	public void shutdown(HttpServletRequest request, HttpServletResponse response) {
		String pos = request.getParameter("pos");
		if (pos != null && !pos.equalsIgnoreCase("")&& !pos.equals("null")) {
			this.storageService.unlock(pos);
			memcachedClient.delete(pos);
		}
	}

	/**
	 * 入库扫描动作
	 *
	 * @date 2016-5-23下午1:35:23
	 * @author chenping
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/ruku.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult ruku(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		JsonResult result = new JsonResult();
		String date = request.getParameter("date");
		String num = request.getParameter("num");
		String pos = request.getParameter("pos");
		result = this.softwareService.preCheckIn(date, num, pos,SoftWareStatusEnum.TO_NEW_CHECKIN.getValue());
		return result;
	}

	/**
	 * 入库操作
	 * 
	 * @author chenping
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/rukuCheckIn.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult rukuCheckIn(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String pos = request.getParameter("pos");
		// 提交需要做的操作 解锁仓库 清除缓存 插入数据
		if (pos == null || pos == "" || Function.isEmpty(pos)||"null".equals(pos)) {
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		List<Software> softs = (List<Software>) memcachedClient.get(pos);
		this.storageService.unlock(pos);
		if (softs == null) {
			memcachedClient.delete(pos);
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		else {
			//2016-09-29 陈平更新 此处无论新入库的数据成功与否 都应该把缓存里的数据清除掉 代码实现的功能是将未成功的数据 抛向页面 要求剔除出来 再做处理
			result = storageService.CheckInStorage(softs,SoftwareOperationEnum.NEW_IN_STORAGE.getValue());
			memcachedClient.delete(pos);
			return result;
		}
	}


	/**
	 * 制作证书回库
	 *
	 * @date 2016-5-25下午19:35:23
	 * @author chenping
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zzhk.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult zzhk(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String date = request.getParameter("date");
		String num = request.getParameter("num");
		String pos = request.getParameter("pos");
		result = this.softwareService.preCheckIn(date, num, pos,SoftWareStatusEnum.TO_MARKCARD_CHECKIN.getValue());
		return result;
	}

	/**
	 * 制证回库确认入库操作
	 *
	 * @author chenping
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/zzhkCheckIn.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult zzhkCheckIn(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String pos = request.getParameter("pos");
		// 提交需要做的操作 解锁仓库 清除缓存 插入数据
		if (pos == null || pos == "" || Function.isEmpty(pos)||"null".equals(pos)) {
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		List<Software> softs = (List<Software>) memcachedClient.get(pos);
		this.storageService.unlock(pos);
		if (softs == null) {
			memcachedClient.delete(pos);
			result.setState(false);
			result.setMsg("入库失败！");
			return result;
		}
		else {
			//2016-09-29 陈平更新 此处无论新入库的数据成功与否 都应该把缓存里的数据清除掉 代码实现的功能是将未成功的数据 抛向页面 要求剔除出来 再做处理
			result = storageService.CheckInStorage(softs,SoftwareOperationEnum.CARD_IN_STORAGE.getValue());
			memcachedClient.delete(pos);
			return result;

		}
	}


	/**
	 * 异常入库 预入库
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/rukuForException.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult rukuForException(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String date = request.getParameter("date");
		String num = request.getParameter("num");
		String pos = request.getParameter("pos");
		result = this.softwareService.preCheckIn(date, num, pos,SoftWareStatusEnum.TO_EXECPTION_CHECKIN.getValue());
		return result;
	}

	/**
	 * 异常入库 预入库
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exceptionCheckIn.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult exceptionCheckIn(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String pos = request.getParameter("pos");
		// 提交需要做的操作 解锁仓库 清除缓存 插入数据
		if (pos == null || pos == "" || Function.isEmpty(pos)||"null".equals(pos)) {
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		List<Software> softs = (List<Software>) memcachedClient.get(pos);
		this.storageService.unlock(pos);
		if (softs == null) {
			memcachedClient.delete(pos);
			result.setState(false);
			result.setMsg("入库失败！");
			return result;
		}
		else {
			//2016-09-29 陈平更新 此处无论新入库的数据成功与否 都应该把缓存里的数据清除掉 代码实现的功能是将未成功的数据 抛向页面 要求剔除出来 再做处理
			result = storageService.CheckInStorage(softs,SoftwareOperationEnum.EXCEPTION_IN_STORAGE.getValue());
			memcachedClient.delete(pos);
			return result;

		}
	}

	/**
	 * 核费功能页面跳转
	 * 
	 * @date 2016-5-24下午4:35:23
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/accountingSoftwareList.do")
	public String accountingSoftwareList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		return "back/software/accountingSoftwareList";
	}

	/**
	 * 更新或者保存首先调用页面
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @author wangna
	 */
	@Deprecated
	@RequestMapping("/saveSoftwareForm.do")
	public String saveSoftwareForm(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id) {
		String softwareID = request.getParameter("id");
		String adminID = request.getParameter("adminid");
		// String authorizationcode = request.getParameter("authorizationcode");
		String flag = request.getParameter("flag");
		Software software = null;
		Admin admin = null;
		if (!Function.isEmpty(id)) {
			software = this.softwareService.findSoftware(id);
			admin = this.systemService.queryAdminByID(adminID);
			if (software != null) {
				String serialnum = software.getSerialnum();
				model.put("softwareCertificatedateForm", software);

			}
			else {
				// 获取ID失败 。。。。
				model.put("softwareCertificatedateForm", new Software());
			}
		}
		model.put("flag", flag);

		return "/back/software/softwareCertificatedateForm";// form
	}

	/**
	 * 修改出证日期
	 * @date 2016-8-15下午5:43:37
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param certificatedate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateSoftware.do", method = { RequestMethod.POST })
	@ResponseBody
	@Transactional
	public String updateSoftware(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String certificatedate) throws Exception {
		Admin operationAdmin = Admin.sessionAdmin();
		String operationAdminId = operationAdmin.getID();
		String id = request.getParameter("id");
		String authorizationcode = request.getParameter("authorizationcode");
		/** 执行数据保存 **/
		Software software = this.softwareService.findSoftware(id);
		List<Admin> admin = new ArrayList<>();
		admin = this.systemService.queryAdminByRole("1");
		if (software != null) {
			for (Admin admin2 : admin) {
				if (admin2.getAuthorizationcode().equals(Function.MD5(authorizationcode))) {
					
					boolean ok = this.softwareService.updateCreDate(certificatedate, software,operationAdminId);
					
					if(ok){
						return "success";
					}else{
						return "fail";
					}
				}
			}
		}
		return "errorCode";
	}

	/**
	 * 修改出证日期查询
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * @author wangna
	 */
	@RequestMapping("/queryAllSoftwareCertificatedateList.do")
	public String queryAllSoftwareCertificatedateList(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws Exception {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
	//		param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		param.setNeverypage(150);
		// 出证日期
		String certificatedate = request.getParameter("certificatedate");
		// 流水号
		String serialnum = request.getParameter("serialnum");
		/** 添加时间查询 **/
		if(!Function.isEmpty(certificatedate) || !Function.isEmpty(serialnum) ){
			if (!Function.isEmpty(certificatedate)) {
				param.put("certificatedate", certificatedate);
				modelMap.put("certificatedate", MyDate.get(certificatedate));
			}
			if (!Function.isEmpty(serialnum)) {
				param.put("serialnum", serialnum);
				modelMap.put("serialnum", serialnum);
			}
			PageObject po = softwareService.querySoftInfoListParam(param);
			modelMap.put("po", po);
		}
			
		return "back/software/softwareCertificatedateList";
	}


	/**
	 * 修改分审核人员查询
	 *
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * @author chenping
	 */
	@RequestMapping(value = "/toModifyAuditor.do")
	public String toModifyAuditor(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap){
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 出证日期
		String certificatedate = request.getParameter("certificatedate");
		// 流水号
		String serialnum = request.getParameter("serialnum");
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
		}
		/** 添加时间查询 **/
		if(!Function.isEmpty(certificatedate) || !Function.isEmpty(serialnum) ){
			if (!Function.isEmpty(certificatedate)) {
				param.put("certificatedate", certificatedate);
				modelMap.put("certificatedate", MyDate.get(certificatedate));
			}
			if (!Function.isEmpty(serialnum)) {
				param.put("serialnum", serialnum);
				modelMap.put("serialnum", serialnum);
			}
			PageObject po = softwareService.querySoftInfoListParam(param);
			modelMap.put("po", po);
		}

		/** 查询所有审核员：分审时选择分审员用 **/
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "审查员");
		paramMap.put("state", "1");
		List<Admin> shenheAdminList = this.systemService.queryAdminByRoleName(paramMap);
		Map<String, String> map = new HashMap<String, String>();
		for (Admin auditor : shenheAdminList) {
			map.put(auditor.getID(), auditor.getRelName());
		}
		modelMap.addAttribute("records", map);
		return "back/software/modifyAuditor";
	}

	/**
	 * 修改审核人员
	 *
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modifyAuditor.do")
	@ResponseBody
	public String modifyAuditor(HttpServletResponse response, HttpServletRequest request,ModelMap modelMap){
		Admin admin = Admin.sessionAdmin();
		String aduitID = request.getParameter("auditID");
		String softId = request.getParameter("id");
		try {
			Software software = softwareService.findSoftware(softId);
			software.setTrialId(aduitID);
			software.setUpdatedate(new Date());
			softwareService.saveSoftware(software);
			logService.saveLog(software,SoftwareOperationEnum.MODIFY_AUDITOR.getValue(),admin);
			return "y";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "n";
	}




	/**
	 * 案件查询--跳转到案件查询页面（按照流水号查询）
	 * 
	 * @date 2016-5-26下午2:35:44
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/updateCertificatedateForm.do")
	public String updateCertificatedateForm(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap, String serialnum) throws Exception {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		Software sw = this.softwareService.querySoftwareBySerialnum(serialnum);
		if (sw != null) {
			modelMap.put("sw", sw);
		}
		else {
			modelMap.put("error", "不存在此信息");
		}
		return "back/software/updateCertificatedateForm";
	}

	/**
	 * 根据流水号对软件信息执行核费操作
	 * 
	 * @date 2016-5-24下午5:15:06
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param serialnum
	 * @return
	 */
	@RequestMapping("/doAccountingSoftware.do")
	@ResponseBody
	@Transactional
	public JsonResult doAccountingSoftware(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String serialnum) {
		Admin admin = Admin.sessionAdmin();
		JsonResult result = new JsonResult();
		// 校验流水号长度
		if (!(serialnum.trim().length() == 14)) {
			result.setState(false);
			result.setMsg("流水号输入长度不合法！");
			return result;
		}
		// 校验流水号非空
		if (Function.isEmpty(serialnum)) {
			result.setState(false);
			result.setMsg("请输入流水号！");
			return result;
		}
		// 根据序列号查询软件信息
		Software software = this.softwareService.querySoftwareBySerialnum(serialnum);
		//该软件不存在
		if (software == null) {
			return returnFalseMessage(result,serialnum,"没有该流水号记录");
		}
		//该软件状态为待缴费状态，执行核费操作
		if ((SoftWareStatusEnum.PENDING_PAYMENT.getValue() == software.getSoftwarestatus())) {
			software.setSoftwarestatus(SoftWareStatusEnum.PENDING_TRIAL.getValue());
			software.setUpdatedate(new Date());
			boolean flag = this.softwareService.saveSoftware(software);
			Storage storage = this.storageService.findStorageBysoftwareId(software.getId());
			if (flag) {
				//添加软件操作日志操作
				logService.saveLog(software,SoftwareOperationEnum.VERIFICATION_FEE.getValue(),admin);
				software.setTemphouseorder(storage.getGroupname() + "" + storage.getName() + "-" + storage.getHouseorder());
				result.setState(true);
				result.setMsg("√ 核费成功！");
				result.setData(JsonUtils.objectToJsonString(software));
				return result;
			} else {
				software.setTemphouseorder(storage.getGroupname() + "" + storage.getName() + "-" + storage.getHouseorder());
				result.setState(false);
				result.setMsg("× 核费失败！");
				result.setData(JsonUtils.objectToJsonString(software));
				return result;
			}
		//该记录还没有入库
		}else if (SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue() == software.getSoftwarestatus()) {
			result.setState(false);
			result.setMsg("该记录还未入库！");
			result.setData(JsonUtils.objectToJsonString(software));
			return result;
		}else {
			Storage storage = this.storageService.findStorageBysoftwareId(software.getId());
			if(storage != null){
				software.setTemphouseorder(storage.getGroupname() + "" + storage.getName() + "-" + storage.getHouseorder());
				result.setState(false);
				result.setMsg("该记录已核费！");
				result.setData(JsonUtils.objectToJsonString(software));
				return result;
			}else{
				software.setTemphouseorder("该记录不在库");
				result.setState(false);
				result.setMsg("该记录不在库！");
				result.setData(JsonUtils.objectToJsonString(software));
				return result;
			}
		}

	}

	/**
	 * 跳转至制证回库页面 校验解锁二级仓库 同时查询所有可用的二级仓库
	 * 
	 * @author chenping
	 * @return
	 */
	@RequestMapping("/backSoftInfoAuditList.do")
	public String backSoftInfoAuditList(ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		List<Storage> list = this.storageService.getFStorage("1");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Storage storage : list) {
			result.put(storage.getId(), storage.getName());
		}
		modelMap.put("opts", result);
		/** 查询所有审核员：分审时选择分审员用 **/
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("state", "1");
		paramMap.put("roleName", "审查员");
		List<Admin> shenheAdminList = this.systemService.queryAdminByRoleName(paramMap);
		Map<String, String> map = new HashMap<String, String>();
		for (Admin auditor : shenheAdminList) {
			map.put(auditor.getID(), auditor.getRelName());
		}
		modelMap.put("records", map);

		return "back/software/zhizhenghuiku";
	}

	/**
	 * 批量补正功能页面跳转
	 * 
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/correctingSoftwareList.do")
	public String correctingSoftwareList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		return "back/software/correctingSoftwareList";
	}

	/**
	 * 根据流水号对软件信息执行补正操作
	 * 
	 * @date 2016-5-24下午5:15:06
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param serialnum
	 * @return
	 */
	@RequestMapping("/doCorrectingSoftware.do")
	@ResponseBody
	public JsonResult doCorrectingSoftware(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String serialnum) {
		JsonResult result = new JsonResult();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		jsonConfig.setExcludes(new String[] { "storage" });
		Admin admin = Admin.sessionAdmin();
		// 校验流水号长度
		if (Function.isEmpty(serialnum) || serialnum.trim().length() != 14) {
			result.setState(false);
			result.setMsg("流水号输入长度不合法！");
			return result;
		}
		Software sw = this.softwareService.querySoftwareBySerialnum(serialnum);
		if (sw != null) {
			//满足批量补正的案件的条件是待制证和已分审的软件，但是前提条件是该软件必须出库状态
			if (SoftWareStatusEnum.TRIAL_OK.getValue() == sw.getSoftwarestatus()  || SoftWareStatusEnum.PENDING_CERTIFICATE.getValue() == sw.getSoftwarestatus()) {
				if(sw.getStorage() != null && sw.getIsexist() == 1){
					result.setState(false);
					result.setMsg("请先出库后再补正操作！");
					return result;
				}else{
					sw.setSoftwarestatus(SoftWareStatusEnum.PENDING_CORRECT.getValue());
					sw.setAdminid(Admin.sessionAdmin().getID());
					sw.setUpdatedate(new Date());
					boolean flag = this.softwareService.saveSoftware(sw);
					
					if (flag) {
						
						//添加软件操作日志操作-----------批量补正
						logService.saveLog(sw,SoftwareOperationEnum.BATCH_CORRECTION.getValue(),admin);
						result.setState(true);
						result.setMsg("√");
						result.setData(JsonUtils.objectToJsonString(sw));
						return result;
					} else {
						result.setState(false);
						result.setMsg("修改状态失败！");
						return result;
					}
				}
			} else {
				int currentState = sw.getSoftwarestatus();
				switch (currentState) {
				case 0:
					result.setState(false);
					result.setMsg("该记录的当前状态为 未入库！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;
				case 1:
					result.setState(false);
					result.setMsg("该记录的当前状态为 待缴费！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;
				case 2:
					result.setState(false);
					result.setMsg("该记录的当前状态为 待分审！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;
				case 4:
					result.setState(false);
					result.setMsg("该记录的当前状态为 待补正！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;
				case 6:
					result.setState(false);
					result.setMsg("该记录的当前状态为 已制证！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;
				default:
					result.setState(false);
					result.setMsg("该记录异常！");
					result.setData(JsonUtils.objectToJsonString(sw));
					break;

				}

			}
		}
		else {
			Software nullSoft = new Software();
			nullSoft.setSerialnum(serialnum);
			nullSoft.setDescription("没有该流水号记录！");
			result.setState(false);
			result.setMsg("无记录");
			result.setData(JsonUtils.objectToJsonString(nullSoft));
			return result;
		}

		return result;
	}

	/**
	 * 导出excel 表格
	 *
	 * @author wangna
	 * 2016-08-29chenping 更改
	 * 
	 */
	@RequestMapping("/exportExcelAll.do")
	public ModelAndView exportExcelAll(HttpServletRequest request, HttpServletResponse response, String ids,ModelMap model) throws Exception {
		Software software = null;
		SoftwareController controller = new SoftwareController();
		Map<String, Object> obj = null;
		List<Software> list = new ArrayList<Software>();
		if (!Function.isEmpty(ids)) {
			//判断是多条选中还是只选中一条
			if (ids.indexOf(",") != -1) {
				String[] idArray = ids.split(",");
				for (String id : idArray) {
					software = this.softwareService.findSoftware(id);
					list.add(software);
				}
			} else {
				software = this.softwareService.findSoftware(ids);
				list.add(software);
			}

			HSSFWorkbook workbook =null;
			workbook = this.softwareService.export(list);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			String filename = sdf.format(date) + ".xls";// 设置下载时客户端Excel的名称
			System.out.println("文件名称是：" + filename);
			controller.buildExcelDocument(obj, workbook, request, response, filename);
		}else{
			log.debug("未选中任何软件数据");
		}
		return new ModelAndView(controller, model);
	}

	/**
	 * 异常回库
	 * 
	 * @return
	 */
	@RequestMapping("/backForException.do")
	public String backForException(ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		List<Storage> list = this.storageService.getFStorage("1");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Storage storage : list) {
			result.put(storage.getId(), storage.getName());
		}
		modelMap.put("opts", result);
		return "back/software/checkInForException";
	}


	@RequestMapping("/exportExcelAllhm.do")
	public void exportExcelAllhm(HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {
		Software software = null;
		System.out.println(ids);
		List<Software> list = new ArrayList<Software>();
		if (!Function.isEmpty(ids)) {
			//判断是多条选中还是只选中一条
			if (ids.indexOf(",") != -1) {
				String[] idArray = ids.split(",");
				for (String id : idArray) {
					software = this.softwareService.findSoftware(id);
					list.add(software);
				}
			}
			else {
				software = this.softwareService.findSoftware(ids);
				list.add(software);
			}
			
			OutputStream sos = response.getOutputStream();
			try {
				HSSFWorkbook workbook = new HSSFWorkbook();
				workbook = this.softwareService.export(list);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-ddhhmm");
				String time = sdf.format(new Date());
				response.setHeader("Content-Type","application/force-download");
				response.setHeader("Content-Type","application/vnd.ms-excel");
				// response.setContentType("APPLICATION/OCTET-STREAM");
				//response.setContentType("application/vnd.ms-excel;charset=UTF-8");
				// response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition", "attachment; filename=" + time + ".xls");
				workbook.write(sos);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				sos.flush();
				sos.close();
			}

		}
		else {
			log.error("未选中导出数据~~");
		}
	}
	
	
	
	@RequestMapping("/softwareHistoryBySerialnum.do")
	public String softwareHistoryBySerialnum(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap,String serialnum)throws Exception{
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
			param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		if(!Function.isEmpty(serialnum)){
			param.put("serialnum", serialnum);
			PageObject po = logService.querySoftwareOperationLog(param);
			modelMap.put("po", po);
		}
		
		return "/back/software/softwareHistoryBySerialnum";
	}


	/**
	 * 下拉选框获取二级仓位(普通仓库的二级仓库)
	 * @param request
	 * @return
     */
	@RequestMapping("/getSecStorage.do")
	@ResponseBody
	public String getSecStorage(HttpServletRequest request){
		String pos = request.getParameter("pos");
		long time1 = System.currentTimeMillis();
		this.storageService.checkLock();
		long time2 = System.currentTimeMillis();
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<SoftwareVO> softwareVOList = this.storageService.getSecStorage(pos);
		for (SoftwareVO softvo:softwareVOList) {
			result.put(softvo.getCategory(),softvo.getTotal());
		}
		long time3 = System.currentTimeMillis();
		log.debug(time2-time1+"-----------------------------------------------------------------");
		log.debug(time3-time2+"-----------------------------------------------------------------");
//		long time1 = System.currentTimeMillis();
//		String fid = request.getParameter("pos");
//		Map<String, Object> param = new HashMap<String, Object>();
//		// 先对数据库锁进行校验 锁定超过30分钟的数据storage 将被解锁
//		this.storageService.checkLock();
//		long time2 = System.currentTimeMillis();
//		log.debug(System.currentTimeMillis());
//		param.put("level", 2);
//		param.put("isabandon", 0);
//		param.put("locksign", 0);
//		param.put("surplus_gt",1);//可用量大于等于1
//		param.put("fid",fid);
//		//查出所有的二级仓库
//		List<Storage> storages = this.storageService.queryStorageByParam(param);
//		long time3 = System.currentTimeMillis();
//		Map<String, String> result = new LinkedHashMap<String, String>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		for (Storage storage : storages) {
//			//查出该二级仓位下MAX(三级仓位) 并和totalSpace
//			Storage thirdStorage = storageService.findMaxThirdStorage(storage.getId());
//			int totalNum = storage.getTotalspace();
//			if(thirdStorage!=null){ //该二级仓库下有记录
//				int maxNum = thirdStorage.getHouseorder();
//				int spare = totalNum - maxNum;
//				//如果当前三级仓库的最大值小于二级仓库的总量 则可以接着放入
//				if(maxNum<totalNum){
//					//2016-08-30 新添加功能 要求在选定仓库的时候 在后面添加 每个二级仓库存放的都是哪天的软件和什么状态的
//					Software soft = softwareService.findSoftwareByStorageId(storage.getId());
//					if(soft==null){
//						result.put(storage.getId(), storage.getGroupname() + storage.getName() + "可用余量：" + spare);
//					}else{
//						result.put(storage.getId(), storage.getGroupname() + storage.getName() + "可用余量：" + spare +"_出证日期:"+sdf.format(soft.getCertificatedate()));
//					}
//				}
//			}else{ //若该二级仓库下是空的
//				Software soft = softwareService.findSoftwareByStorageId(storage.getId());
//				if(soft==null){
//					result.put(storage.getId(), storage.getGroupname() + storage.getName() + "可用余量：" + storage.getSurplus());
//				}else{
//					result.put(storage.getId(), storage.getGroupname() + storage.getName() + "可用余量：" + storage.getSurplus()+"_出证日期:"+sdf.format(soft.getCertificatedate()));
//				}
//			}
//		}
//		long time4 = System.currentTimeMillis();
//		log.debug(time1);
//		log.debug(time2-time1);
//		log.debug(time3-time2);
//		log.debug(time4-time3);
		String retString = JsonUtils.objectToJsonString(result);
		return retString;
	}
	
	/**
	 * 下拉选框获取二级仓位(逾期仓库的二级仓库)
	 * @date 2016-12-7下午3:34:11
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOverdueSecStorage.do")
	@ResponseBody
	public String getOverdueSecStorage(HttpServletRequest request){
		String pos = request.getParameter("pos");
		// 先对数据库锁进行校验 锁定超过30分钟的数据storage 将被解锁
		this.storageService.checkLock();
		Map<String, String> result = new LinkedHashMap<String, String>();
		List<SoftwareVO> softwareVOList = this.storageService.getOverDueSecStorage(pos);
		for (SoftwareVO softvo:softwareVOList) {
			result.put(softvo.getCategory(),softvo.getTotal());
		}
		String retString = JsonUtils.objectToJsonString(result);
		return retString;
	}


	protected void buildExcelDocument(Map<String, Object> obj, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response, String filename) throws Exception {
		//filename = Function.encodeFilename(request, filename, "UTF-8");// 处理中文文件名
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	/**
	 * Subclasses must implement this method to create an Excel HSSFWorkbook document,
	 * given the model.
	 *
	 * @param model    the model Map
	 * @param workbook the Excel workbook to complete
	 * @param request  in case we need locale etc. Shouldn't look at attributes.
	 * @param response in case we need to set cookies. Shouldn't write to it.
	 */
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

	}
	
	
	@RequestMapping("/queryAllSoftwareToStatusList.do")
	public String queryAllSoftwareToStatusList(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("查询软件信息，修改软件状态------跳转查询页面");
		log.debug("方法名：queryAllSoftwareToStatusList.do");
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
	//		param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		param.setNeverypage(150);
		// 流水号
		String serialnum = request.getParameter("statusSerialnum");
		/** 添加时间查询 **/
		if(!Function.isEmpty(serialnum) ){
			param.put("serialnum", serialnum.trim());
			modelMap.put("statusSerialnum", serialnum.trim());
			PageObject po = softwareService.querySoftInfoListParam(param);
			modelMap.put("po", po);
		}
			
		return "back/software/softwareToStatusList";
	}
	
	/**
	 * 修改软件状态页面加载
	 * @date 2016-9-12上午11:27:20
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateSoftwareStatusForm.do")
	public String updateSoftwareStatusForm(HttpServletRequest request, HttpServletResponse response, ModelMap model, String id) {
		log.debug("修改软件状态页面跳转");
		log.debug("方法名：updateSoftwareStatusForm.do");
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		Software software = null;
		Admin admin = null;
		if (!Function.isEmpty(id)) {
			software = this.softwareService.findSoftware(id);
			if (software != null) {
				String serialnum = software.getSerialnum();
				model.put("softwareStatusForm", software);
			}
			else {
				model.put("softwareStatusForm", new Software());
			}
		}
		return "/back/software/softwareStatusForm";
	}
	
	/**
	 * 修改软件状态操作
	 * @date 2016-9-12下午3:23:56
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param id
	 * @param softwarestatus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateSoftwareStatus.do", method = { RequestMethod.POST })
	@ResponseBody
	public String updateSoftwareStatus(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap, String id,String softwarestatus) throws Exception {
		log.debug("修改软件状态执行方法");
		log.debug("方法名：updateSoftwareStatus.do");
		Admin operationAdmin = Admin.sessionAdmin();
		String operationAdminId = operationAdmin.getID();
		/** 执行数据保存 **/
		Software software = this.softwareService.findSoftware(id);
		if (software != null) {
			Storage storage = software.getStorage();
			if(storage != null){
				return "InStorage";
			}else{
				boolean ok = this.softwareService.updateSoftwareStatus(softwarestatus, software, operationAdminId);
				if(ok){
					return "success";
				}else{
					return "fail";
				}
			}
		}
		return "nullSoftware";
	}
	
	
	/**
	 * 撤回操作页面跳转
	 * @date 2016-10-8上午11:26:32
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toRevokeList.do")
	public String toRevokeList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		log.debug("撤回操作页面跳转方法");
		log.debug("方法名：toRevokeList");
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}

		return "back/software/toRevokeList";
	}
	
	/**
	 * 撤回操作-----执行具体方法
	 * @date 2016-10-8下午2:43:10
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/toRevoke.do", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public JsonResult toRevoke(HttpServletRequest request, HttpServletResponse response) {
		log.debug("撤回操作-----执行具体方法");
		log.debug("方法名：toRevoke");
		JsonResult result = new JsonResult();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		jsonConfig.setExcludes(new String[] { "storage" });

		String num = request.getParameter("num");

		// 流水号长度不合法
		if (num.trim().length() != 14) {
			Software badSoft = new Software();
			badSoft.setTemphouseorder("流水号输入长度不合法！");
			result.setData(JsonUtils.objectToJsonString(badSoft));
			result.setState(false);
			result.setMsg("流水号输入长度不合法！");
			return result;
		}
		result = this.softwareService.toRevoke(num);
		return result;
	}

	/**
	 * 软件状态不合法返回页面的方法
	 * @param result
	 * @param serialNumber
	 * @param message
	 * @return
	 */
	private JsonResult returnFalseMessage(JsonResult result,String serialNumber,String message){
		Software soft = new Software();
		soft.setSerialnum(serialNumber);
		soft.setTemphouseorder(message);
		soft.setSoftwarename("");
		soft.setApplyperson("");
		soft.setSalesman("");
		result.setMsg("异常");
		result.setState(false);
		result.setData(JsonUtils.objectToJsonString(soft));
		return result;
	}




	/**
	 * 逾期回库页面跳转
	 * @date 2016-11-23下午2:22:49
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/overdueBackSoftInfoAuditList.do")
	public String overdueBackSoftInfoAuditList(ModelMap modelMap) {
		log.debug("逾期回库页面跳转");
		log.debug("方法名：--------------overdueBackSoftInfoAuditList");
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		List<Storage> list = this.storageService.getFStorage("2");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Storage storage : list) {
			result.put(storage.getId(), storage.getName());
		}
		modelMap.put("opts", result);

		return "back/software/overdueList";
	}
	
	/**
	 * 逾期回库操作的预入库阶段
	 * @date 2016-11-24上午10:28:39
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/odhk.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult odhk(HttpServletRequest request, HttpServletResponse response) {
		log.debug("逾期回库预入库阶段");
		log.debug("方法名----------odhk");
		JsonResult result = new JsonResult();
		String num = request.getParameter("num");
		String pos = request.getParameter("pos");
		String date = new Date().toString();
		result = this.softwareService.preCheckIn(date, num, pos,SoftWareStatusEnum.TO_OVERDUE_CHECKIN.getValue());
		return result;

	}
	
	/**
	 * 逾期回库确认操作
	 * @date 2016-11-24下午3:41:02
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/odhkCheckIn.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult odhkCheckIn(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String pos = request.getParameter("pos");
		// 提交需要做的操作 解锁仓库 清除缓存 插入数据
		if (pos == null || pos == "" || Function.isEmpty(pos)||"null".equals(pos)) {
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		List<Software> softs = (List<Software>) memcachedClient.get(pos);
		if (softs == null) {
			memcachedClient.delete(pos);
			result.setState(false);
			result.setMsg("入库失败！");
			this.storageService.unlock(pos);
			return result;
		} else {
			result = storageService.CheckInStorage(softs, SoftwareOperationEnum.OVERDUE_IN_STORAGE.getValue());
			memcachedClient.delete(pos);
			this.storageService.unlock(pos);
			return result;
		}
	}
	
	/**
	 * 案件查询(已制证)
	 * @date 2016-12-6下午5:04:04
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryAllSoftwareHistory.do")
	public String queryAllSoftwareHistory(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws Exception {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
		//	param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		param.setNeverypage(150);//设置每页条数
		// 流水号
		String serialnum = request.getParameter("serialnum");
		// 操作员
		String adminid = request.getParameter("adminid");
		// 案件状态
		String softwarestatus = request.getParameter("softwarestatus");
		// 制证时间段
		String certificatedate1 = request.getParameter("certificatedate1");
		String certificatedate2 = request.getParameter("certificatedate2");
		// String relname = request.getParameter("relname");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "操作员");
		List<Admin> admins =  this.systemService.queryAdminByRoleName(paramMap);
		Map<String, Object> ret1 = new HashMap<String, Object>();
		for (Admin s : admins) {
			ret1.put(s.getID(), s.getRelName());
		}
		modelMap.put("recordadmin", ret1);
		/*
		 * if(!Function.isEmpty(relname)){ param.put("relname", relname.trim());
		 * modelMap.put("relname", relname.trim()); }
		 */
		if(!Function.isEmpty(serialnum) || 
		   !Function.isEmpty(adminid)   || 
		   !Function.isEmpty(softwarestatus) || 
		   (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2))){
			
			/** 添加流水号查询 **/
			if (!Function.isEmpty(serialnum)) {
				param.put("serialnum", serialnum.trim());
				modelMap.put("serialnum", serialnum.trim());
			}
			// ** 添加操作员查询 **//*
			if (!Function.isEmpty(adminid)) {
				param.put("adminid", adminid.trim());
				modelMap.put("adminid", adminid.trim());
			}
			/** 添加案件状态查询 **/
			if (!Function.isEmpty(softwarestatus)) {
				param.put("softwarestatus", softwarestatus);
				modelMap.put("softwarestatus", softwarestatus);
			}
			/** 添加时间查询 **/
			if (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2)) {
				
				param.put("certificatedate1", certificatedate1);
				modelMap.put("certificatedate1", MyDate.get(certificatedate1));
				
				param.put("certificatedate2", certificatedate2);
				modelMap.put("certificatedate2", MyDate.get(certificatedate2));
			}
			PageObject po = softwareHistoryService.querySoftwareHistoryList(param);
			modelMap.put("po", po);
		}
		return "back/software/softwareHistoryList";
	}
	
	
	/**
	 * 案件查询（在库和历史）
	 * @date 2016-12-8上午11:04:34
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/querySoftwareAndSfhistory.do")
	public String querySoftwareAndSfhistory(HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws Exception {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		QueryParamater param = new QueryParamater();
		// 分页
		if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
		//	param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
		}
		param.setNeverypage(150);//设置每页条数
		// 流水号
		String serialnum = request.getParameter("serialnum");
		// 操作员
		String adminid = request.getParameter("adminid");
		// 案件状态
		String softwarestatus = request.getParameter("softwarestatus");
		// 制证时间段
		String certificatedate1 = request.getParameter("certificatedate1");
		String certificatedate2 = request.getParameter("certificatedate2");
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "审查员");
		List<Admin> admins = this.systemService.queryAdminByRoleName(paramMap);
		Map<String, Object> ret1 = new HashMap<String, Object>();
		for (Admin s : admins) {
			ret1.put(s.getID(), s.getRelName());
		}
		modelMap.put("recordadmin", ret1);
		// 仓位查询
		String nameLevel = request.getParameter("nameLevel");
		List<Storage> storages = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("level", "2");
		storages = (List<Storage>) this.storageService.queryStorageByParam(map);
		Map<String, Object> ret = new LinkedHashMap<String, Object>();
		for (Storage s : storages) {
			ret.put(s.getId(), s.getGroupname() + s.getName());
		}
		modelMap.put("records", ret);
		if(!Function.isEmpty(nameLevel) || 
		   !Function.isEmpty(serialnum) || 
		   !Function.isEmpty(adminid)   || 
		   !Function.isEmpty(softwarestatus) || 
		   (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2))){
			
			if (!Function.isEmpty(nameLevel)) {
				param.put("nameLevel", nameLevel.trim());
				modelMap.put("nameLevel", nameLevel.trim());
			}
			
			/** 添加流水号查询 **/
			if (!Function.isEmpty(serialnum)) {
				param.put("serialnum", serialnum.trim());
				modelMap.put("serialnum", serialnum.trim());
			}
			// ** 添加操作员查询 **//*
			if (!Function.isEmpty(adminid)) {
				param.put("adminid", adminid.trim());
				modelMap.put("adminid", adminid.trim());
			}
			/** 添加案件状态查询 **/
			if (!Function.isEmpty(softwarestatus)) {
				param.put("softwarestatus", softwarestatus);
				modelMap.put("softwarestatus", softwarestatus);
			}
			/** 添加时间查询 **/
			if (!Function.isEmpty(certificatedate1) && !Function.isEmpty(certificatedate2)) {
				
				param.put("certificatedate1", certificatedate1);
				modelMap.put("certificatedate1", MyDate.get(certificatedate1));
				
				param.put("certificatedate2", certificatedate2);
				modelMap.put("certificatedate2", MyDate.get(certificatedate2));
			}
			PageObject po = softwareService.querySoftwareAndSfhistory(param);
			modelMap.put("po", po);
		}
		return "back/software/softwareAndHistoryList";
	}

	@RequestMapping("/getStandardExcel.do")
	public void downLoad(HttpServletRequest request,HttpServletResponse response){
		try {
			String base = Function.getAppPath();
			String filepath = "static/resource/StandardExcel.xls";
			File file = new File(base + filepath);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();

			// 以流的形式下载文件。
			InputStream inputStream = new BufferedInputStream(new FileInputStream(base + filepath));
			byte[] buffer = new byte[inputStream.available()];
			inputStream.read(buffer);
			inputStream.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			outputStream.write(buffer);
			outputStream.flush();
			outputStream.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 快捷入库（雍和）页面加载
	 * @date 2016-12-12上午11:21:00
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/quickSoftInfoAuditList.do")
	public String quickSoftInfoAuditList(ModelMap modelMap) {
		Admin sessionAdmin = Admin.sessionAdmin();
		if (sessionAdmin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		List<Storage> list = this.storageService.getFStorage("1");
		Map<String, String> result = new LinkedHashMap<String, String>();
		for (Storage storage : list) {
			result.put(storage.getId(), storage.getName());
		}
		modelMap.put("opts", result);
		return "back/software/quickSoftInfoAuditList";
	}

	/**
	 * 快捷预入库
	 * @date 2016-12-12上午11:41:26
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/quickRuku.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult quickRuku(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		JsonResult result = new JsonResult();
		String date = request.getParameter("date");
		String num = request.getParameter("num");
		String pos = request.getParameter("pos");
		result = this.softwareService.preCheckIn(date, num, pos,SoftWareStatusEnum.TO_QUICK_CHECKIN.getValue());
		return result;
	}

	/**
	 * 快捷入库（实际入库操作）
	 * @date 2016-12-12下午2:22:19
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/quickRukuCheckIn.do", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult quickRukuCheckIn(HttpServletRequest request, HttpServletResponse response) {
		JsonResult result = new JsonResult();
		String pos = request.getParameter("pos");
		// 提交需要做的操作 解锁仓库 清除缓存 插入数据
		if (pos == null || pos == "" || Function.isEmpty(pos)||"null".equals(pos)) {
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		}
		List<Software> softs = (List<Software>) memcachedClient.get(pos);
		this.storageService.unlock(pos);
		if (softs == null) {
			memcachedClient.delete(pos);
			result.setState(false);
			result.setMsg("没有数据！");
			return result;
		} else {
			//2016-09-29 陈平更新 此处无论新入库的数据成功与否 都应该把缓存里的数据清除掉 代码实现的功能是将未成功的数据 抛向页面 要求剔除出来 再做处理
			result = storageService.CheckInStorage(softs,SoftwareOperationEnum.QUICK_IN_STORAGE.getValue());
			memcachedClient.delete(pos);
			return result;
		}
	}



}
