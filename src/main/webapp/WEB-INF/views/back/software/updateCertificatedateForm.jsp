<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/softinfo/updateCertificatedateForm.do'/>" method="post">
		<input type="hidden" name="pageSize" value="${model.pageSize}" /> 
		<input type="hidden" name="pageCurrent" value="${model.pageCurrent}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" /> 
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
		<div class="bjui-searchBar">
			<label>流水号:</label>&nbsp;<input id="serachSerialnum" name="serialnum" type="text" value="${serialnum}" class="form-control" size="15"/>&nbsp; 
			<button type="submit" class="btn-default" data-icon="search">查询</button>
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
				<!-- 
				<th>接收日期</th>
				 -->
				<th>库位号</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="searchSoftwareMain">
			<c:if test="${not empty sw}">
			<tr data-id="${sw.id}">
                <td>${sw.serialnum}</td>
                <td>${sw.softwarename}</td>
                <td>${sw.applyperson}</td>
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${sw.certificatedate}"/></td>
				<td>${sw.salesman}</td>
                <!-- 
				<td><fmt:formatDate pattern="yyyy-MM-dd" value="${info.acceptdate}"/></td>
				 -->
                <td>
                <c:if test="${not empty sw.storage}">${sw.storage.groupname}${sw.storage.name}-${sw.storage.houseorder}</c:if></td>
                <td>
					<c:if test="${not empty sw}"><a href="<c:url value='/back/softinfo/saveSoftwareForm.do?id=${sw.id}&flag=2'/>" data-on-close='beforeClose' data-icon='search' data-toggle='dialog' data-id='dialog-mask' data-mask='true'>修改制证日期</a></c:if>
				</td>
            </tr>
            </c:if>
		</tbody>
	</table>
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
