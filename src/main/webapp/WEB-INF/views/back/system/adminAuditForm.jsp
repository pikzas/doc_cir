<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
$('#admin-form').bind('valid.form', function(){
	    $.ajax({
			type:"post",
			data:{
			id:'${adminForm.ID}',
			password:$("#password").val(),
			email:$("#email").val(),
			phone:$("#phone").val(),
			relName:$("#relName").val(),
			role:$("#station").val()
			},
			url:'<c:url value="/back/system/updateAdmin.do"/>',
			success:function(msg){
				if("success"==msg){
					$.CurrentDialog.dialog("closeCurrent");
				}else{
				
				}
			}
		});
});
		
</script>
<div class="bjui-pageContent">
	<form method="post" id='admin-form' name="adminForm"
		action="<c:url value=""/>"
		data-toggle="validate" data-alertmsg="false">
		<!-- <input type="hidden" name="custom.id"
			value="edce142bc2ed4ec6b623aacaf602a4de"> -->
			<table class="table table-condensed table-hover" width="100%">
				<tbody>
					<%-- <spring:bind path="adminForm.Ukey">
				    	<input name="<c:out value="${status.expression}"/>" value="${status.value}" type="hidden" size="15" />
					</spring:bind> --%>
					<tr>
						<td>
							<label for="j_custom_name" class="control-label x150">登录名称：</label>
							<spring:bind path="adminForm.loginName">
								<input
									<c:if test="${not empty adminForm.ID}">disabled="disabled"</c:if>
									name="<c:out value="${status.expression}"/>" id="loginName"
									value="<c:out value="${status.value}"/>" type="text"
									data-rule="required" size="15" onkeyup="validateName()" />
								<font color="red" id="erroInfo">${erroInfo}</font>
							</spring:bind>
						</td>
						<td>
							<label for="j_custom_name" class="control-label x150">电子邮箱：</label>
							<spring:bind path="adminForm.email">
								<input name="<c:out value="${status.expression}"/>" id="email"
									value="${status.value}" type="text" size="15"
									data-rule="required" />
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td>
							<label for="j_custom_name" class="control-label x150">登录密码：</label>
							<spring:bind path="adminForm.password">
								<input <c:if test="${not empty adminForm.ID}">value=""</c:if>
									id="password" type="password"
									name="adminForm.password"  data-rule="required; password" data-rule-password="[/^(?![\d]+$)(?![a-zA-Z]+$)(?![!#$%^&*]+$)[\da-zA-Z!#$%^&*]{6,20}$/, '请填写6-20位数字、字母、符号']" data-msg-required="请填写密码" 
									data-tip="密码由6-20位数字、字母、符号组成" data-ok="别担心，稍后您还可以更改" value="${status.value}" size="15" />
							</spring:bind>
						</td>
						<td>  
							<label for="j_custom_name" class="control-label x150">确认密码：</label>
							<input id="password2" name="password2" type="password" value=""
							type="text" size="15" data-rule="确认密码:required;match(adminForm.password)" />
						</td>
					</tr>
					<tr>
						<td>
							<label for="j_custom_name" class="control-label x150">手机：</label>
							<spring:bind path="adminForm.phone">
								<input name="<c:out value="${status.expression}"/>"
									value="${status.value}" type="text" size="15"
									data-rule="required;mobile" id="phone"/>
							</spring:bind>
						    </td>
						<td>
							<label for="j_custom_name" class="control-label x150">真实姓名：</label>
							<spring:bind path="adminForm.relName">
								<input name="<c:out value="${status.expression}"/>"
									value="${status.value}" type="text" size="15"
									data-rule="required" id="relName"/>
							</spring:bind>
						</td>
					</tr>
					<tr>
						<td>
							<label for="j_custom_name" class="control-label x150">角色：</label>
							<spring:bind path="adminForm.roleId">
							    <select  <c:if test="${not empty adminForm.ID}">disabled="disabled"</c:if> name="${status.expression}" id="station" data-toggle="selectpicker" data-rule="required">
							        <option value="${ bxt:getEnumValue('AdminRoleStatusEnum','ADMIN')}" <c:if test="${adminForm.roleId eq bxt:getEnumValue('AdminRoleStatusEnum','ADMIN')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","ADMIN")}</option>
									<option value="${ bxt:getEnumValue('AdminRoleStatusEnum','AUDITOR')}" <c:if test="${adminForm.roleId eq bxt:getEnumValue('AdminRoleStatusEnum','AUDITOR')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","AUDITOR")}</option>
									<option value="${ bxt:getEnumValue('AdminRoleStatusEnum','OPERATOR')}" <c:if test="${adminForm.roleId eq bxt:getEnumValue('AdminRoleStatusEnum','OPERATOR')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","OPERATOR")}</option>
							    </select>
							</spring:bind>
						</td>
						<td>
							<label for="j_custom_name" class="control-label x150">状态：</label>
							<spring:bind path="adminForm.state">
							    <select  <c:if test="${not empty adminForm.ID}">disabled="disabled"</c:if> name="${status.expression}" id="state" data-toggle="selectpicker" data-rule="required">
							        <option value="${bxt:getEnumValue('AdminRoleStatusEnum','ACTIVATE')}" <c:if test="${adminForm.state eq bxt:getEnumValue('AdminRoleStatusEnum','ACTIVATE')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","ACTIVATE")}</option>
									<option value="${bxt:getEnumValue('AdminRoleStatusEnum','ABANDONED')}" <c:if test="${adminForm.state eq bxt:getEnumValue('AdminRoleStatusEnum','ABANDONED')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","ABANDONED")}</option>
									<option value="${bxt:getEnumValue('AdminRoleStatusEnum','INVALID')}" <c:if test="${adminForm.state eq bxt:getEnumValue('AdminRoleStatusEnum','INVALID')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","INVALID")}</option>
							    </select>
							</spring:bind>
						</td>
						</tr>
					</tr>
					<tr>
						<td colspan="2" align="center">
						<input type="reset" value="重置" />
						</td>
					</tr>
				</tbody>
			</table>
	</form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li><button type="button" class="btn-close" data-icon="close">取消</button></li>
        <li><button id="qr1" type="submit" class="btn-green" onclick="">保存</button></li>
    </ul>
</div>