<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<title>修改出证日期</title>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/softinfo/queryAllSoftwareCertificatedateList.do'/>" method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> 
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" /> 
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号：</label><input id="serialnum" name="serialnum" value="${serialnum}"/>
			<label>出证日期:</label><input  id="certificatedate" name='certificatedate' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${certificatedate}"/>"/>
			<button type="submit" id="softwareCertificateSearch" class="btn-default" data-icon="search">查询</button>&nbsp; <a class="btn btn-orange" href="javascript:;"data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
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
				<th>库位号</th>
				<th>当前状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${po.results}" var="info" varStatus="s">
				<tr data-id="${info.id}">
					<td>${info.serialnum}</td>
					<td>${info.softwarename}</td>
					<td>${info.applyperson}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.certificatedate}"/></td>
					<td>${info.salesman}</td>
					<td>${info.trialAdmin.relName}</td>
					<td>
						<c:if test="${not empty info.storage}">${info.storage.groupname}${info.storage.name}-${info.storage.houseorder}</c:if>
					</td>
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
						<a href="<c:url value="/back/softinfo/saveSoftwareForm.do?id=${info.id}&flag=2"/>" data-on-close="beforeClose" class="btn btn-blue" data-icon="cogs" data-toggle="dialog" data-id="dialog-mask" data-mask="true">修改出证日期</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<div class="bjui-pageFooter">
	<div class="pages">
		<span>每页&nbsp;</span>
		<div class="selectPagesize">
			<select data-toggle="selectpicker"
				data-toggle-change="changepagesize">
				<option value="20">20</option>
				<option value="30">30</option>
				<option value="60">60</option>
				<option value="120">120</option>
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
		$("#softwareCertificateSearch").trigger("click");
		return true;
	}
</script>