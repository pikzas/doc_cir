package com.bjca.ecopyright.warehouse.back;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.MyDate;
import com.bjca.ecopyright.warehouse.model.Storage;
import com.bjca.ecopyright.warehouse.service.StorageService;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;

/**
 * 仓库管理
 * @className WareHouseController.java
 * @date 2016-5-19下午7:29:09
 * @mail humin@bjca.org.cn
 * @author humin
 *
 */
@Controller
@RequestMapping("/back/warehouse")
public class StorageController {
	Log log = LogFactory.getLog(StorageController.class);
	
	@Autowired
	private StorageService storageService;
	
	
	/**
	 * 
	 * @date 2016-5-20下午1:40:59
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/warehouseIndex.do")
	public String warehouseIndex(HttpServletRequest request, ModelMap modelMap) throws Exception {
		QueryParamater param = new QueryParamater();
		
		List<Storage> slist = this.storageService.selectAllStorage();
		modelMap.put("slist", slist);
		
		return "/back/warehouse/warehouseIndex";
	}
	
	
	
	/**
	 * 操作树左侧
	 * @date 2016-5-20下午1:46:43
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param pid
	 * @return
	 * @throws Exception
	 */
/*	@RequestMapping("/warehouseLeft.do")
	public String left(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap,String fid) throws Exception {
		QueryParamater param = new QueryParamater();
		param.setCurrentPage(Function.parseInt(request.getParameter("npage"), 0));
		param.setNeverypage(9999);
		if(!Function.isEmpty(fid)){
			param.put("fid", fid);
			modelMap.put("fid", fid);
		}
		
		PageObject po = this.storageService.queryStorage(param);
		
		modelMap.put("po", po);
		return "/back/sysconfig/warehouseLeft";
	
	}*/
	
	
	/**
	 * 添加一级目录页面
	 * @date 2016-5-21下午12:10:06
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("addFFWarehouseForm.do")
	public String addFFWarehouseForm(HttpServletRequest request, ModelMap modelMap){
			
			return "/back/warehouse/addFFWarehouseForm";
		}
	
	/**
	 * 添加一级目录
	 * @date 2016-5-21下午12:24:54
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param name
	 * @return
	 */
	@RequestMapping("/addFFWarehouse.do")
	@ResponseBody
	@Transactional
	public String addFFWarehouse(HttpServletRequest request, ModelMap modelMap,String name,String mark){
		Storage storage = this.storageService.findStorageByFFstorageName(name);
		if(storage == null){
			boolean flag = this.storageService.addFStorage(name,mark);
			if(flag){
				return "success";
			}else{
				return "fail";
			}
		}else{
			return "notNull";
		}
	}
	
	
	/**
	 * 仓库查询列表页面(二级仓库---父级节点)
	 * @date 2016-5-19下午7:54:07
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/FwarehouseList.do")
	public String FwarehouseList(HttpServletRequest request, ModelMap modelMap,String fid,String storeName,String surplus,String isabandon){
		QueryParamater param = new QueryParamater();
		//分页
		if(!Function.isEmpty(request.getParameter("pageCurrent"))){
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0)-1);//当前页
			param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));//每页个数
		}
		if(!Function.isEmpty(storeName)){
			//对仓位进行正则切割处理
			Pattern pattern = Pattern.compile("[0-9]+$");
			Matcher m = pattern.matcher(storeName);
			if(m.find()){
				String ms = m.group();
				param.put("storeName", ms);
			}else{
				param.put("storeName", storeName);
			}
			modelMap.put("storeName", storeName);
		}
		if(!Function.isEmpty(surplus)){
			param.put("surplus", surplus);
			modelMap.put("surplus", surplus);
		}
		if(!Function.isEmpty(isabandon)){
			param.put("isabandon", isabandon);
			modelMap.put("isabandon", isabandon);
		}
		
		if(!Function.isEmpty(fid)){
			param.put("fid", fid);
			modelMap.put("fid", fid);
		}else{
			param.put("fid_isNull", "fid_isNull");
		}
		
		PageObject po = this.storageService.queryStorage(param);
		modelMap.put("po", po);
		
		return "/back/warehouse/fwarehouseList";
	}
	
	
	/**
	 * 仓库位置查看(三级仓库----子节点)
	 * @date 2016-5-21下午3:51:15
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param fid
	 * @return
	 */
	@RequestMapping("/warehouseList.do")
	public String warehouseList(HttpServletRequest request, ModelMap modelMap,String fid){
		QueryParamater param = new QueryParamater();
		if(!Function.isEmpty(request.getParameter("pageCurrent"))){
			param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0)-1);//当前页
			param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));//每页个数
		}
		param.put("fid", fid);
		modelMap.put("fid", fid);
		
		PageObject po = this.storageService.queryStorage(param);
		modelMap.put("po", po);
		
		return "/back/warehouse/warehouseList";
	}
	
	/**
	 * 进入父级仓位添加页面
	 * @date 2016-5-20上午11:00:58
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/addFWarehouseForm.do")
	public String addFWarehouseForm(HttpServletRequest request, ModelMap modelMap,String fid){
		modelMap.put("fid", fid);
		if(!Function.isEmpty(fid)){
			Storage FFstorage = this.storageService.findStorageByStorageId(fid);
			modelMap.put("FFstorage", FFstorage);
		}
		return "/back/warehouse/warehouseForm";
	}
	
	
	
	/**
	 * 添加二级仓位
	 * @date 2016-5-20上午11:38:07
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/addFWarehouse.do")
	@ResponseBody
	@Transactional
	public String addFWarehouse(HttpServletRequest request, ModelMap modelMap,String ffid,String name,String totalspace,String description){
		boolean flag = this.storageService.addFStorage(ffid, name, totalspace, description);
		if(flag){
			return "success";
		}else{
			return "fail";
		}
	}
	
	/**
	 * 添加二级仓库时候命名去重验证
	 * @date 2016-6-1下午1:09:16
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param ffid
	 * @return
	 */
	@RequestMapping("/duplicateFWarehouse.do")
	@ResponseBody
	@Transactional
	public String duplicateFWarehouse(HttpServletRequest request, ModelMap modelMap,String ffid,String name){
		boolean flag = false;
		List<Storage> flist = this.storageService.getAllFStorageByFFid(ffid);
		if(flist != null && flist.size()>0){
			for(Storage s : flist){
				if(s.getName().equals(name.trim())){
					flag = false;
					break;
				}else{
					flag = true;
				}
			}
			if(flag){
				return "success";
			}else{
				return "fail";
			}
		}else{
			return "success";
		}
	}
	
	/**
	 * 进入二级仓位废弃操作页面
	 * @date 2016-5-23上午11:56:35
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping("/abandonStorageForm.do")
	public String abandonStorageForm(HttpServletRequest request, ModelMap modelMap,String id){
		Storage storage = this.storageService.findStorage(id);
		
		modelMap.put("storage", storage);
		modelMap.put("id", id);
		return "/back/warehouse/abandonStorageForm";
	}
	
	
	/**
	 * 进入二级仓位废弃查看页面
	 * @date 2016-5-23下午1:32:39
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping("/abandonStorageView.do")
	public String abandonStorageView(HttpServletRequest request, ModelMap modelMap,String id){
		Storage storage = this.storageService.findStorage(id);
		
		modelMap.put("storage", storage);
		modelMap.put("id", id);
		return "/back/warehouse/abandonStorageView";
	}
	
	
	/**
	 * 仓库废弃(二级仓库废弃)
	 * @date 2016-5-23下午4:34:49
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param id
	 * @param abandonreason
	 * @return
	 */
	@RequestMapping("/abandonStorage.do")
	@ResponseBody
	@Transactional
	public String abandonStorage(HttpServletRequest request, ModelMap modelMap,String id,String abandonreason){
		boolean flag = this.storageService.abandonStorage(id,abandonreason);
		if(flag){
			return "success";
		}else{
			return "fail";
		}
	}
	
	
	/**
	 * 二级仓库扩容界面
	 * @date 2016-5-23下午3:48:58
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param id
	 * @return
	 */
	@RequestMapping("/expandStorageForm.do")
	public String expandStorageForm(HttpServletRequest request, ModelMap modelMap,String id){
		Storage storage = this.storageService.findStorage(id);
		
		modelMap.put("storage", storage);
		modelMap.put("id", id);
		return "/back/warehouse/expandStorageForm";
	}
	
	
	/**
	 * 二级仓库扩容操作
	 * @date 2016-5-23下午4:37:11
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @param id
	 * @param addTotalspace  扩容数量
	 * @return
	 */
	@RequestMapping("/expandStorage.do")
	@ResponseBody
	@Transactional
	public String expandStorage(HttpServletRequest request, ModelMap modelMap,String id,String addTotalspace){
		boolean flag = this.storageService.expandStorage(id,addTotalspace);
		if(flag){
			return "success";
		}else{
			return "fail";
		}
	}
	
	/**
	 * 仓位查询界面跳转
	 * @date 2016-5-30上午11:43:24
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/findWarehouse.do")
	public String findWarehouse(HttpServletRequest request, ModelMap modelMap,String surplus,String certificatedate,String softWareState){
		List<Storage> list = null;
		Map<String,Object> param = new HashMap<String,Object>();
		if(!Function.isEmpty(surplus)){
			param.put("surplus_gt",surplus);
			modelMap.put("surplus",surplus);
		}
		if(!Function.isEmpty(certificatedate)){
			param.put("certificatedate", certificatedate);
			modelMap.put("certificatedate",MyDate.get(certificatedate));
		}
		if(!Function.isEmpty(softWareState)){
			param.put("softWareState", softWareState);
			modelMap.put("softWareState",softWareState);
		}
		param.put("level", 2);
		if(Function.isEmpty(surplus) && Function.isEmpty(certificatedate) && Function.isEmpty(softWareState)){
			modelMap.put("list", list);
		}else{
			list = this.storageService.searchWarehouse(param);
			modelMap.put("list", list);
		}
		
		return "/back/warehouse/findWarehouseList";
	}
	
	/**
	 * 锁定仓库列表
	 * @date 2016-5-31下午6:23:26
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/unlockStorageList.do")
	public String unlockStorageList(HttpServletRequest request, ModelMap modelMap){
		
		List<Storage> locklist = null;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("level",2);
		param.put("locksign",1);
		
		locklist = this.storageService.searchWarehouse(param);
		modelMap.put("locklist", locklist);
		return "/back/warehouse/unlockStorageList";
	}
	
	/**
	 * 解锁锁定仓库
	 * @date 2016-5-31下午6:24:46
	 * @mail humin@bjca.org.cn
	 * @author humin
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unlockStorage.do")
	public String unlockStorage(HttpServletRequest request, ModelMap modelMap,String id){
		
		boolean flag = this.storageService.unlockStorage(id);
		if(flag){
			return "success";
		}else{
			return "fail";
		}
	}
	
}
