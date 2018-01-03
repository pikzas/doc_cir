package com.bjca.ecopyright.system.back;

import com.bjca.ecopyright.system.model.Menu;
import com.bjca.ecopyright.system.service.MenuService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能菜单管理
 * @author wangw
 * @date 2016年9月7日
 */
@Controller
@RequestMapping("/back/menu")
public class MenuController {
	
	Log log = LogFactory.getLog(MenuController.class);
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 功能菜单list
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/menuList.do")
	public String menuList(HttpServletRequest request, ModelMap model) {
		//查询list列表
		//JSONArray json = menuService.queryMenuForTree();
		String ret = menuService.queryMenuForTree();
		//model.put("treeData", json.toString());
		model.put("treeData", ret);
		return "/back/system/menuList";
	}
	
	/**
	 * 保存或修改菜单
	 * @param request
	 * @return
	 */
	@RequestMapping("/saveMenu.do")
	@ResponseBody
	public String saveMenu(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		String data = request.getParameter("myMenus");
		if(Function.isEmpty(data)){
			map.put("result", "n");
			return JsonUtils.objectToJsonString(map);
		}
		JSONArray jsonArray = JSONArray.fromObject(data);
		List<Menu> menus = (List<Menu>) JSONArray.toCollection(jsonArray,Menu.class);
		boolean flag = menuService.updateMenu(menus);
		if(flag){
			map.put("result", "y");
			return JsonUtils.objectToJsonString(map);
		}
		map.put("result", "n");
		return JsonUtils.objectToJsonString(map);
	}
	
	/**
	 * 删除菜单
	 * @param request
	 * @param MenuId
	 * @return
	 */
	@RequestMapping("/removeMenu.do")
	@ResponseBody
	public boolean removeMenu(HttpServletRequest request,String MenuId){
		//TODO 同时删除role表内的数据
		boolean result = menuService.removeMenu(MenuId);
		return result;
	}
}
