<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	//接收表单验证通过的事件
	$('#role-form').bind('valid.form', function() {
		$.ajax({
			url : '<c:url value="/back/role/updateRole.do"/>',
			type : 'POST',
			data : {
				id : '${roleForm.id}',
				name : $("#name").val(),
				description : $("#description").val(),
				sort : $("#sort").val(),
			},
			success : function(msg) {
				if(msg=="success"){
					ok();
				}else{
					$(this).alertmsg("error","更新失败");
				}

			}
		});
	});
	function ok() {
		$.CurrentDialog.dialog("closeCurrent");
	}
</script>
<div class="bjui-pageContent">
	<form method="post" id='role-form' name=roleForm" action="<c:url value=""/>" data-toggle="validate" data-alertmsg="false" >
		<table class="table table-condensed table-hover" width="100%">
			<tbody>
				<spring:bind path="roleForm.id">
					<input name="<c:out value="${status.expression}"/>" value="${status.value}" type="hidden" size="15" />
				</spring:bind>

				<c:if test="${flag eq '2'}">
					<tr>
						<label  class="control-label x150">角色名称：</label>
						<input >
						${roleForm.name}
					</tr>
					<tr>
						<label class="control-label x150">角色描述：</label> ${roleForm.description}
					</tr>
					<tr>
						<label class="control-label x150">排序：</label> ${roleForm.sort}
					</tr>
				</c:if>
				<c:if test="${flag eq '1'}">
					<tr>
						<td>
							<label class="control-label x150">角色名称：</label>
							<spring:bind path="roleForm.name">
								<input name="<c:out value="${status.expression}"/>" id="name" value="<c:out value="${status.value}"/>" type="text" size="15" />
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td>
							<label class="control-label x150">角色描述：</label>
							<spring:bind path="roleForm.description">
								<input name="<c:out value="${status.expression}"/>"  id="description"  value="<c:out value="${status.value}"/>"  type="text" size="15" />
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td>
							<label class="control-label x150">排序：</label>
							<spring:bind path="roleForm.sort">
								<input name="<c:out value="${status.expression}"/>"  id="sort" value="<c:out value="${status.value}"/>" type="text"  size="15" />
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td  align="center">
							<input type="reset" value="重置" />
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
	</form>
</div>
<div class="bjui-pageFooter">
	<ul>
		<li><button type="button" class="btn-close" data-icon="close">取消</button></li>
		<c:if test="${flag eq 1}">
			<li><button id="save" type="submit" class="btn-green">保存</button></li>
		</c:if>
	</ul>
</div>