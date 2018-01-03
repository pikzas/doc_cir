package com.bjca.ecopyright.soft.back;

import com.bjca.ecopyright.soft.model.Software;
import com.bjca.ecopyright.soft.service.SoftwareService;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.model.JsonResult;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonDateValueProcessor;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.ecopyright.warehouse.service.StorageService;
import com.bjca.ecopyright.warehouse.service.StorageSoftwareService;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 回库
 * @author chenping
 */
@Controller
@RequestMapping("/back/checkout")
public class CheckoutController {
	Log log = LogFactory.getLog(CheckoutController.class);
	
	@Autowired
	private SoftwareService softwareService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private StorageSoftwareService storageSoftwareService;
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * Excel导入（制证出库）
	 * @param filepath 文件路径
	 * @return
	 * @author humin
	 * @date 2016.5.19
	 */
	@RequestMapping("/importExcel.do")
	@ResponseBody
    public JsonResult importExcel(HttpServletRequest request,HttpServletResponse response,MultipartFile file){
		JsonResult result = new JsonResult();
		boolean flag = false;
		List<Software> SoftList = new ArrayList<Software>();//数据库中已存在数据
		try {
			InputStream inputStream = file.getInputStream();
			SoftList = this.softwareService.importExcelForZZCK(inputStream);
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		result.setState(flag);
		result.setData(JsonUtils.objectToJsonString(SoftList));
		return result;
    } 

	/**
	 * 跳转至出证页面
	 * @return
	 */
	@RequestMapping("/certificatedSoftwareCheckout.do")
	public String toCheckoutIndex(ModelMap modelMap){
		/**查询所有审核员：分审时选择分审员用**/
		List<Admin> shenheAdminList = this.systemService.queryAdminByRole("2");
		Map<String,String> map = new HashMap<String,String>();
		for (Admin auditor : shenheAdminList) {
			map.put(auditor.getID(), auditor.getRelName());
		}
		modelMap.addAttribute("records",map);
		return "back/software/certificatedSoftwareCheckout";
	}
	
	/**
	 * 异常出库页面初始化
	 * @date 2016-9-7下午3:15:43
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/exceptionCheckout.do")
	public String fenshen(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		log.debug("异常出库页面初始化");
		log.debug("方法名：exceptionCheckout.do");
		Admin admin = Admin.sessionAdmin();
		if (admin == null) {
			log.debug("操作员未登录。。。");
			return "redirect:/manage.jsp";
		}
		return "back/software/exceptionCheckout";
	}
	
	/**
	 * 异常出库操作
	 * @date 2016-9-7下午3:41:37
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toExceptionCheckout.do")
	@ResponseBody
	public JsonResult toExceptionCheckout(HttpServletRequest request, HttpServletResponse response) {
		log.debug("异常出库操作");
		log.debug("方法名：toExceptionCheckout.do");
		JsonResult result = new JsonResult();
		String serialNum = request.getParameter("serialNum");
		if (Function.isEmpty(serialNum) || Function.getLeagalLengthOfSerialNum() != serialNum.length()) {
			result.setState(false);
			result.setMsg("流水号输入有误！");
			return result;
		}
		//只进行出库操作，不进行状态变更
		result = this.softwareService.exceptionCheckout(serialNum);
		return result;
	}
	
	/**
	 * 批量核费导入execl
	 * @date 2016-9-18上午11:44:48
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping("/accountingExcel.do")
	@ResponseBody
    public JsonResult accountingExcel(HttpServletRequest request,HttpServletResponse response,MultipartFile file){
		log.debug("批量核费导入Excel");
		log.debug("方法名：accountingExcel");
		Admin operationAdmin = Admin.sessionAdmin();
		String adminId = operationAdmin.getID();
		JsonResult result = new JsonResult();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor());
		jsonConfig.setExcludes(new String[]{"storage"});
		boolean flag = false;
		List<Software> SoftList = new ArrayList<Software>();//数据库中已存在数据
		try {
			InputStream inputStream = file.getInputStream();
			SoftList = this.softwareService.importExcelForAccount(inputStream,adminId);
			flag = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		result.setState(flag);
		result.setData(JsonUtils.objectToJsonString(SoftList));
		return result;
    } 
}
