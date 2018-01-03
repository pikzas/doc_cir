<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改审核人员</title>
<%--<script type="text/javascript">--%>

	<%--function confirmAuditor(id) {--%>
		<%--var aduitID = $("#auditor").val();--%>
		<%--if(aduitID==""||aduitID==null){--%>
			<%--$.alertmsg('info','请选择分审核人！');--%>
		<%--}--%>
		<%--alert(222);--%>
		<%--$.ajax({--%>
			<%--url:"<c:url value="/back/softinfo/modifyAuditor.do"/>",--%>
			<%--data:{"id":id,"auditID":aduitID},--%>
			<%--type:"get",--%>
			<%--success:function (ret) {--%>
				<%--if(ret=="y"){--%>
					<%--$.alertmsg('ok','修改成功！');--%>
					<%--$.CurrentNavtab.navtab("refresh");--%>
				<%--}else{--%>
					<%--$.alertmsg('error','修改失败！');--%>
				<%--}--%>
			<%--}--%>
		<%--});--%>
	<%--}--%>

<%--</script>--%>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/softinfo/toModifyAuditor.do'/>" method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> 
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" /> 
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号：</label><input id="serialnum" name="serialnum" value="${serialnum}"/>
			<label>出证日期:</label><input  id="certificatedate" name='certificatedate' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${certificatedate}"/>"/>
			<button type="submit" id="modifyAuditor" class="btn-default" data-icon="search">查询</button>&nbsp; <a class="btn btn-orange" href="javascript:;"data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
			<lable>分审员：</lable>
			<select id="auditor" name="auditor" data-toggle="selectpicker">
				<option value="">请选择分审员</option>
				<c:forEach items="${records}" var="opt">
					<option value="${opt.key }">${opt.value }</option>
				</c:forEach>
			</select>
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
						<button id="bt1" data-on-close="beforeClose" data-on-load="DonLoad" type="button" data-icon="cogs" class="btn-blue" onclick="confirmAuditor('${info.id}')" <c:if test="${info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TO_BE_IN_STORAGE') or info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')  or info.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}"> disabled="true"</c:if>>修改分审人员</button>
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

	function confirmAuditor(id) {
		var aduitID = $("#auditor").val();
		if(aduitID==""||aduitID==null){
			$(this).alertmsg('info','请选择分审核人！');
			return;
		}
		$.ajax({
			url:"<c:url value="/back/softinfo/modifyAuditor.do"/>",
			data:{"id":id,"auditID":aduitID},
			type:"get",
			success:function (ret) {
				if(ret=="y"){
					$(this).alertmsg('ok','修改成功！');
					$.CurrentNavtab.navtab("refresh");
				}else{
					$(this).alertmsg('error','修改失败！');
				}
			}
		});
	}

	//加载前
	function DonLoad() {
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
	//页面关闭刷新当前页面
	function beforeClose() {
		$("#modifyAuditor").trigger("click");
		return true;
	}
</script>