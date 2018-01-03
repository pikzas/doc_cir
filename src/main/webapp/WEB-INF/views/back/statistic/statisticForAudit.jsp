<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch"
		action="<c:url value='/back/statistic/audit.do'/>"
		method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" />
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" />
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>审查员:</label>
			<select name="trialId" data-toggle="selectpicker">
				<option value="">请选择</option>
				<c:forEach items="${recordTrialId}" var="info">
					<option value="${info.key}" <c:if test="${trialId eq info.key}">selected</c:if>>${info.value}</option>
				</c:forEach>
			</select>&nbsp;  
			<label>软件状态:</label>
			<select name="status" data-toggle="selectpicker">
				<option value="">请选择</option>
				<c:forEach items="${softwareStatus}" var="info">
					<option value="${info.key}" <c:if test="${myStatus eq info.key}">selected</c:if>>${info.value}</option>
				</c:forEach>
			</select>&nbsp;
			<label>分审日期开始于</label>
				<input  id="auditdate1" name='auditdate1' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${auditdate1}"/>"/>
            <label>结束于</label>
				<input id="auditdate2" name='auditdate2' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${auditdate2}" />"/>
			<button type="submit" class="btn-green" data-icon="search">查询</button>&nbsp;
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
				<th>库位号</th>
				<th>分审日期</th>
				<th>当前状态</th>
				<th>历史记录</th>
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
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.auditdate}"/></td>
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
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
</script>