<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<style type="text/css">
</style>
<script type="text/javascript">
	function To_unlock(id) {
		var id = id;
		$("#pagerForm").alertmsg('confirm', '您确定要解锁此库？', {
			okCall : function() {
				To_confirm(id);
			}
		});
	}
	function To_confirm(id) {
		$.ajax({
			type : "POST",
			url : "<c:url value='/back/warehouse/unlockStorage.do'/>",
			data : {
				id : id
			},
			success : function(msg) {
				if (msg == "success") {
					execOk();
				} else {
					execFail();
				}
			}
		});
	}
	function execOk() {
		$("#pagerForm").alertmsg('ok', '您已成功解锁！');
		$.CurrentNavtab.navtab("refresh");
	}
	function execFail() {
		$.CurrentNavtab.navtab("refresh");
	}
</script>
<div class="bjui-pageHeader">
	<form id="pagerForm" data-toggle="ajaxsearch"
		action="<c:url value=''/>" method="post"></form>
</div>
<div class="bjui-pageContent tableContent hm-back">
	<c:if test="${not empty locklist}">
		<table
			class="table table-bordered table-hover table-striped table-top"
			data-selected-multi="true">
			<thead>
				<tr>
					<th>仓位编号</th>
					<th>容限</th>
					<th>锁定状态</th>
					<th>解锁</th>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${locklist}" var="info" varStatus="s">
					<tr data-id="${info.id}">
						<td>${info.groupname}${info.name}</td>
						<td>${info.totalspace}</td>
						<td><c:if test="${info.locksign eq 0}">未锁定</c:if> 
							<c:if test="${info.locksign eq 1}">已锁定</c:if>
						</td>
						<td>
							<button type="submit" class="fa fa-unlock" onclick="To_unlock('${info.id}')">解锁</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>


		</table>
	</c:if>
	<c:if test="${empty locklist}">
		<div style="height:100%;width:100%;background:url(<c:url value='/skin/back/images/empty-unlock.png'/>) center;">
			<table
				class="table table-bordered table-hover table-striped table-top"
				data-selected-multi="true">
				<thead>
					<tr>
						<th>仓位编号</th>
						<th>容限</th>
						<th>锁定状态</th>
						<th>解锁</th>
					</tr>
				</thead>
			</table>
		</div>
	</c:if>
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