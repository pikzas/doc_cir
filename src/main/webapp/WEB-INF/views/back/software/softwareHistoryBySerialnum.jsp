<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
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
				<th>状态</th>
				<th>操作时间</th>
				<th>操作人</th>
				<th>操作类型</th>
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
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TO_BE_IN_STORAGE')}">${bxt:getEnumName("SoftWareStatusEnum","TO_BE_IN_STORAGE")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_PAYMENT")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_TRIAL")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TRIAL_OK')}">${bxt:getEnumName("SoftWareStatusEnum","TRIAL_OK")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CORRECT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CORRECT")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CERTIFICATE")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}">${bxt:getEnumName("SoftWareStatusEnum","CERTIFICATE_OK")}</c:if>
						<c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','REVOKE')}">${bxt:getEnumName("SoftWareStatusEnum","REVOKE")}</c:if>
					</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${info.createdate}"/></td>
					<td>${info.admin.relName}</td>
					<td>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','NEW_IN_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","NEW_IN_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','CARD_IN_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","CARD_IN_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','EXCEPTION_IN_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","EXCEPTION_IN_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','TRIAL_OUT_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","TRIAL_OUT_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','CARD_OUT_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","CARD_OUT_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','UPDATE_CERTIFICATE_DATE')}">${bxt:getEnumName("SoftwareOperationEnum","UPDATE_CERTIFICATE_DATE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','VERIFICATION_FEE')}">${bxt:getEnumName("SoftwareOperationEnum","VERIFICATION_FEE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','BATCH_CORRECTION')}">${bxt:getEnumName("SoftwareOperationEnum","BATCH_CORRECTION")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','EXCEPTION_CHECKOUT')}">${bxt:getEnumName("SoftwareOperationEnum","EXCEPTION_CHECKOUT")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','UPDATE_SOFTWARE_STATUS')}">${bxt:getEnumName("SoftwareOperationEnum","UPDATE_SOFTWARE_STATUS")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','DO_REVOKE')}">${bxt:getEnumName("SoftwareOperationEnum","DO_REVOKE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','OVERDUE_IN_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","OVERDUE_IN_STORAGE")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','MODIFY_AUDITOR')}">${bxt:getEnumName("SoftwareOperationEnum","MODIFY_AUDITOR")}</c:if>
						<c:if test="${info.operationType eq bxt:getEnumValue('SoftwareOperationEnum','QUICK_IN_STORAGE')}">${bxt:getEnumName("SoftwareOperationEnum","QUICK_IN_STORAGE")}</c:if>
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