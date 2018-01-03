<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch"
		action="<c:url value='/back/statistic/hexiao.do'/>"
		method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" />
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" />
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<div style="display: inline-block;margin:5px;">
				<label>总导入量:</label>
				<span><strong>${total}</strong></span>&nbsp;&nbsp;&nbsp;&nbsp;
				<label>实际入库量:</label>
				<span><strong>${checkIn}</strong></span>
			</div>
			<br/>
			&nbsp;<label>导入日期开始于</label>
				<input  id="createdate1" name='createdate1' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${createdate1}"/>"/>
            <label>结束于</label>
				<input id="createdate2" name='createdate2' data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${createdate2}" />"/>
			<button type="submit" class="btn-green" data-icon="search">查询</button>&nbsp;
			<button type="button" class="btn-blue" data-confirm-msg="确定要删除吗？" data-idname="expids" data-group="ids"  data-icon="fa-trash" onclick="batchDelete()">批量删除入库信息</button>
			<a class="btn btn-orange" href="javascript:;"data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
		</div>
	</form>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
			<tr>
				<th width="26"><input id="checkall" type="checkbox" class="checkboxCtrl" data-group="ids" data-toggle="icheck"></th>
				<th>流水号</th>
				<th>软件全称</th>
				<th>申请人</th>
				<th>制证日期</th>
				<th>业务员</th>
				<th>导入日期</th>
				<th>当前状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${po.results}" var="info" varStatus="s">
				<tr data-id="${info.id}">
					<td><input type='checkbox' name='ids' data-toggle='icheck' id='ids' value='${info.id}'>
					<td>${info.serialnum}</td>
					<td>${info.softwarename}</td>
					<td>${info.applyperson}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.certificatedate}"/></td>
					<td>${info.salesman}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.createdate}"/></td>
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
	function batchDelete() {
		var ids = '';
		var idsObj = document.getElementsByName('ids');//获取所有列表数据

		for (i = 0; i < idsObj.length; i++) {
			//获取所有选中数据
			if (idsObj[i].checked == true) {
				ids = idsObj[i].value + ',' + ids;
			}
		}
		if(ids==""){
			$(this).alertmsg('info', '请先选择要删除的数据！');
			return;
		}
		alert(11);
		$.ajax({
			url:"<c:url value='/back/statistic/removeUncheckin.do'/>",
			data:{"ids":ids},
			type:"POST",
			success:function (ret) {
				if(ret=="y"){
					$(this).alertmsg('ok','删除成功！');
					$.CurrentNavtab.navtab("refresh");
				}else{
					$(this).alertmsg('error','删除失败！');
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
		$.CurrentNavtab.navtab("refresh");
		return true;
	}
</script>