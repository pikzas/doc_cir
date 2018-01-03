package com.bjca.ecopyright.soft.back;

import com.bjca.ecopyright.soft.model.SoftwareAuditVO;
import com.bjca.ecopyright.soft.service.SoftwareService;
import com.bjca.ecopyright.statuscode.SoftWareStatusEnum;
import com.bjca.ecopyright.system.model.Admin;
import com.bjca.ecopyright.system.service.SystemService;
import com.bjca.ecopyright.util.Function;
import com.bjca.ecopyright.util.JsonUtils;
import com.bjca.ecopyright.util.MyDate;
import com.bjca.framework.page.PageObject;
import com.bjca.framework.page.QueryParamater;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pikzas on 2016/8/31.
 */
@Controller
@RequestMapping("/back/statistic")
public class StatisticController {
	
	Log log = LogFactory.getLog(StatisticController.class);

    @Autowired
    private SystemService systemService;
    @Autowired
    private SoftwareService softwareService;

    /**
     * 移除未入库数据
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/removeUncheckin.do",method = RequestMethod.POST)
    @ResponseBody
    public String removeUncheckin(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        String ids = request.getParameter("ids");
        try {
            if (!Function.isEmpty(ids)) {
                //判断是多条选中还是只选中一条
                if (ids.indexOf(",") != -1) {
                    String[] idArray = ids.split(",");
                    for (String id : idArray) {
                        this.softwareService.removeSoftware(id);
                    }
                } else {
                    this.softwareService.removeSoftware(ids);
                }
                return "y";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "n";
    }


    /**
     * 跳转入库核销页面
     * @return
     */
    @RequestMapping("/hexiao.do")
    public String hexiao(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        QueryParamater param = new QueryParamater();
        // 分页
        if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
            param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
            param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
        }
        // 入库时间段
        String createdate1 = request.getParameter("createdate1");
        String createdate2 = request.getParameter("createdate2");

        /** 添加时间查询 **/
        if (!Function.isEmpty(createdate1) && !Function.isEmpty(createdate2)) {
            param.put("createdate1", createdate1);
            modelMap.put("createdate1", MyDate.get(createdate1));

            Date newDate = MyDate.get(createdate2);
            param.put("createdate2", MyDate.toString(MyDate.add(newDate,1)));
            modelMap.put("createdate2", MyDate.get(createdate2));
        }
        //查询所有导入数据库中的数据
        PageObject po = softwareService.querySoftware(param);
        //查询所有导入 但是 尚未入库的数据
        param.put("softwarestatus", SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue());
        PageObject po2 = softwareService.querySoftware(param);

        int total = po.getTotal();
        int toCheckIn = po2.getTotal();
        int checkIn = total - toCheckIn;

        modelMap.put("total",total);
        modelMap.put("checkIn",checkIn);
        modelMap.put("po", po2);
        return "back/statistic/statisticForRuku";
    }


    @RequestMapping("/toAuditCheck.do")
    public String toAuditCheck(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("roleName", "审查员");
        paramMap.put("state", "1");
        List<Admin> shenheAdminList = this.systemService.queryAdminByRoleName(paramMap);
        Map<String, String> map = new HashMap<String, String>();
        for (Admin auditor : shenheAdminList) {
            map.put(auditor.getID(), auditor.getRelName());
        }
        modelMap.put("records", map);
        return "back/statistic/auditCheck";
    }

    /**
     * 分审核销页面
     */
    @RequestMapping(value = "/auditCheck.do",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String fenshenhexiao(HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,Object> ret = new HashMap<String,Object>();
        // 审核时间段
        String auditdate1 = request.getParameter("auditdate1");
        String auditdate2 = request.getParameter("auditdate2");
        String auditID = request.getParameter("auditID");

        /** 添加时间查询 **/
        if (!Function.isEmpty(auditdate1) && !Function.isEmpty(auditdate2)) {
            map.put("auditdate1", auditdate1);
            ret.put("auditdate1", auditdate1);
            Date newDate = MyDate.get(auditdate2);
            map.put("auditdate2", MyDate.toString(MyDate.add(newDate,1)));
            ret.put("auditdate2", auditdate2);
        }
        if(!Function.isEmpty(auditID)){
            map.put("auditID", auditID);
            ret.put("auditID", auditID);
        }
        List<SoftwareAuditVO> voLists = softwareService.statisticForAudit(map);

        /** 查询所有审核员：分审时选择分审员用 **/
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put("roleName", "审查员");
        List<Admin> shenheAdminList = this.systemService.queryAdminByRoleName(paramMap);
        Map<String, String> map2 = new HashMap<String, String>();
        for (Admin auditor : shenheAdminList) {
            map2.put(auditor.getID(), auditor.getRelName());
        }
        ret.put("records",map2);
        ret.put("results",voLists);
        return JsonUtils.objectToJsonString(ret);
    }

    /**
     * 查看未回库数据
     */
    @RequestMapping(value = "/checkUnBack.do")
    public String checkUnBack(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        QueryParamater param = new QueryParamater();
        // 审核时间段
        String time = request.getParameter("time");

        String[] times = time.split("_");
        String auditdate1 = "";
        String auditdate2 = "";
        if(times.length!=0){
             auditdate1 = times[0];
             auditdate2 = times[1];
        }
        String auditID = request.getParameter("id");
        param.put("trialId",auditID);
        param.put("softwarestatus",3);
        /** 添加时间查询 **/
        if (!Function.isEmpty(auditdate1) && !Function.isEmpty(auditdate2)) {
            param.put("auditdate1", auditdate1);
            Date newDate = MyDate.get(auditdate2);
            param.put("auditdate2", MyDate.toString(MyDate.add(newDate,1)));
        }
        PageObject po = softwareService.querySoftware(param);
        modelMap.put("po",po);
        return "back/statistic/checkUnBack";
    }


    /**
     * 分审统计
     * @author chenping
     */
    @RequestMapping("/audit.do")
    public String audit(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
    	log.debug("分审统计");
    	log.debug("方法名：audit");
        QueryParamater param = new QueryParamater();
        // 分页
        if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
            param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
            param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
        }

        // 操作员
        String trialId = request.getParameter("trialId");
        // 分审时间段
        String auditdate1 = request.getParameter("auditdate1");
        String auditdate2 = request.getParameter("auditdate2");
        String status = request.getParameter("status");
        // 软件状态
        Map<String,String> map = new HashMap<String,String>();
        map.put(SoftWareStatusEnum.TRIAL_OK.getValue()+"",SoftWareStatusEnum.TRIAL_OK.getName());
        map.put(SoftWareStatusEnum.PENDING_CORRECT.getValue()+"",SoftWareStatusEnum.PENDING_CORRECT.getName());
        map.put(SoftWareStatusEnum.PENDING_CERTIFICATE.getValue()+"",SoftWareStatusEnum.PENDING_CERTIFICATE.getName());
        Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("roleName", "审查员");
		List<Admin> admins = this.systemService.queryAdminByRoleName(paramMap);
        Map<String, Object> ret1 = new HashMap<String, Object>();
        for (Admin s : admins) {
            ret1.put(s.getID(), s.getRelName());
        }
        // 添加软件状态
        if(!Function.isEmpty(status)){
            modelMap.put("myStatus",status);
            param.put("softwarestatus",status);
        }
        // ** 添加操作员查询 **//*
        if (!Function.isEmpty(trialId)) {
            param.put("trialId", trialId.trim());
            modelMap.put("trialId", trialId.trim());
        }else {
            param.put("trialIdNull","1");
        }
        //** 添加时间查询 **/
        if (!Function.isEmpty(auditdate1) && !Function.isEmpty(auditdate2)) {
            param.put("auditdate1", auditdate1);
            modelMap.put("auditdate1", MyDate.get(auditdate1));
            Date newDate = MyDate.get(auditdate2);
            param.put("auditdate2", MyDate.toString(MyDate.add(newDate,1)));
            modelMap.put("auditdate2", MyDate.get(auditdate2));
        }
        PageObject po = softwareService.queryAllSoftware(param);
        modelMap.put("po", po);
        modelMap.put("softwareStatus",map);
        modelMap.put("recordTrialId", ret1);
        return "back/statistic/statisticForAudit";
    }
    
    /**
     * 核消统计
     * @date 2016-12-14下午2:02:27
     * @mail humin@bjca.org.cn
     * @author humin
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @RequestMapping("/writeOffStatistics.do")
    public String writeOffStatistics(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap){
        QueryParamater param = new QueryParamater();
        // 分页
        if (!Function.isEmpty(request.getParameter("pageCurrent"))) {
            param.setCurrentPage(Function.parseInt(request.getParameter("pageCurrent"), 0) - 1);// 当前页
            param.setNeverypage(Function.parseInt(request.getParameter("pageSize"), 0));// 每页个数
        }
        // 入库时间段
        String createdate1 = request.getParameter("createdate1");
        String createdate2 = request.getParameter("createdate2");

        /** 添加时间查询 **/
        if (!Function.isEmpty(createdate1) && !Function.isEmpty(createdate2)) {
            param.put("createdate1", createdate1);
            modelMap.put("createdate1", MyDate.get(createdate1));

            Date newDate = MyDate.get(createdate2);
            param.put("createdate2", MyDate.toString(MyDate.add(newDate,1)));
            modelMap.put("createdate2", MyDate.get(createdate2));
        }
        //查询所有导入数据库中的数据
        PageObject po = softwareService.querySoftware(param);
        //查询所有导入 但是 尚未入库的数据
        param.put("softwarestatus", SoftWareStatusEnum.TO_BE_IN_STORAGE.getValue());
        PageObject po2 = softwareService.querySoftware(param);

        int total = po.getTotal();
        int toCheckIn = po2.getTotal();
        int checkIn = total - toCheckIn;

        modelMap.put("total",total);
        modelMap.put("checkIn",checkIn);
        modelMap.put("po", po2);
        return "back/statistic/writeOffStatistics";
    }
}
