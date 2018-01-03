<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<title>案件查询(已制证)</title>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/softinfo/queryAllSoftwareHistory.do'/>" method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> <input
			type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> <input
			type="hidden" name="orderField" value="${param.orderField}" /> <input
			type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号:</label><input name="serialnum" type="text" value="${serialnum}" class="form-control" size="16" />&nbsp; 
			<label>操作员:</label> 
			<select name="adminid" data-toggle="selectpicker">
				<option value="">请选择</option>
				<c:forEach items="${recordadmin}" var="info">
					<option value="${info.key}" <c:if test="${adminid eq info.key}">selected</c:if>>${info.value}</option>
				</c:forEach>
			</select>&nbsp;  
			<label>案件状态:</label> 
			<select name="softwarestatus" data-toggle="selectpicker">
				<option value="">请选择</option>
				<option value="${bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","CERTIFICATE_OK")}</option>
			</select>&nbsp;
			<br/>
			<label>出证日期开始于</label><input  id="certificatedate1" name='certificatedate1' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${certificatedate1}"/>"/>
            <label>结束于</label><input id="certificatedate2" name='certificatedate2' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${certificatedate2}" />"/>
            
			<button type="submit" class="btn-default" data-icon="search">查询</button>&nbsp;
			<a class="btn btn-orange" href="javascript:;"data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
		</div>
	</form>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
			<tr>
				<th>流水号</th>
				<th>软件全称</th>
				<th>申请人</th>
				<th>制证日期</th>
				<th>业务员</th>
				<th>审查员</th>
				<th>当前状态</th>
				<th>历史记录</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty po}">
				<c:forEach items="${po.results}" var="info" varStatus="s">
					<tr data-id="${info.id}">
						<td>${info.serialnum}</td>
						<td>${info.softwarename}</td>
						<td>${info.applyperson}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.certificatedate}"/></td>
						<td>${info.salesman}</td>
						<td>${info.trialAdmin.relName}</td>
						<td>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TO_BE_IN_STORAGE')}">${bxt:getEnumName("SoftWareStatusEnum","TO_BE_IN_STORAGE")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_PAYMENT")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_TRIAL")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TRIAL_OK')}">${bxt:getEnumName("SoftWareStatusEnum","TRIAL_OK")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CORRECT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CORRECT")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CERTIFICATE")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}">${bxt:getEnumName("SoftWareStatusEnum","CERTIFICATE_OK")}</c:if>
							<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','REVOKE')}">${bxt:getEnumName("SoftWareStatusEnum","REVOKE")}</c:if>
						</td>
						<td>
							<a href="<c:url value='/back/softinfo/softwareHistoryBySerialnum.do?serialnum=${info.serialnum}'/>" class="btn btn-blue" data-icon="fa-history" data-toggle="dialog" data-width="900" data-height="450" data-id="dialog-mask" data-mask="true">历史记录</a>
						</td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${not empty po and po.total eq 0}">
				<tr>
					<td colspan="9" style="text-align:center;"><font id="onlyNo" style="color:red;font-size:14;font-weight:bold;">未找到符合条件的记录</font></td>
				</tr>
			</c:if>
		</tbody>
	</table>
</div>
<div class="bjui-pageFooter">
	<div class="pages">
		<span>每页&nbsp;</span>
		<div class="selectPagesize">
			<select data-toggle="selectpicker"
				data-toggle-change="changepagesize">
				<option value="150">150</option>
			</select>
		</div>
		<span>&nbsp;条，共 ${po.total} 条</span>
	</div>
	<div class="pagination-box" data-toggle="pagination"
		data-total="${po.total}" data-page-size="${po.neverypage}"
		data-page-current="1"></div>
</div>
<script type="text/javascript">
	//加载前
	function DonLoad() {
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
	//页面关闭刷新当前页面
	function beforeClose() {
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
	var num = 0;
	function changeColor(){  
//		var arr = ["blue","red","yellow","green","pink","black","gray","purple","orange"];
//		if(num < 9){
//			num++;
//		}else{
//			num = 0;
//		}
	    var color="#f00|#0f0|#00f|#880|#808|#088|yellow|green|blue|gray|#234|#534";
	    color=color.split("|"); 
	    var hmOnly = document.getElementById("onlyNo");
	    if(hmOnly == null){
	    	
	    }else{
//	    	hmOnly.style.color=arr[num];
		    hmOnly.style.color=color[parseInt(Math.random() * color.length)];
	    }
	} 
	$(function(){
		if(${not empty po and po.total eq 0}){
			setInterval("changeColor()",500);
		}
	});
</script>