<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/include/back_authority.jsp"%>
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
<div class="bjui-pageHeader">
	<a href="<c:url value="/back/role/saveRole.do?flag=1"/>" class="btn btn-green" data-on-close="beforeClose" data-on-load="DonLoad" data-toggle="dialog" data-width="800" data-height="400"
	   data-id="dialog-mask" data-mask="true">添加</a>
</div>
<div class="bjui-pageContent tableContent">
	<table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
		<thead>
		<tr>
			<th>角色名称</th>
			<th>角色描述</th>
			<th>排序</th>
			<th>创建时间</th>
			<th>更新时间</th>
			<th>操作</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${roleList}" var="info" varStatus="s">
			<tr data-id="${info.id}">
				<td>${info.name}</td>
				<td>${info.description}</td>
				<td>${info.sort}</td>
				<td>
					<fmt:formatDate value="${info.createdate }" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${info.updatedate }" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<a href="<c:url value="/back/role/saveRole.do?flag=1&id=${info.id}"/>" class="btn btn-blue" class="btn btn-blue" data-on-close="beforeClose" data-icon="search" data-toggle="dialog"
					   data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">编辑</a>
					<%--<button id="bt1" data-on-close="beforeClose" data-on-load="DonLoad" type="button" class="btn-orange" onclick="delAdminById('${info.id}')">删除</button>--%>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>