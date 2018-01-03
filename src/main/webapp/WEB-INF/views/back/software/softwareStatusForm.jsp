<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	//接收表单验证通过的事件
	$('#software-status-form').bind('valid.form', function(){
	    $.ajax({
	        url: '<c:url value="/back/softinfo/updateSoftwareStatus.do"/>',
	        type: 'POST',
	        data: {
				id:'${softwareStatusForm.id}',
				softwarestatus:$(this).find("#softwarestatus").val(),
			},
	        success: function(msg){
	        	if(msg == 'nullSoftware'){
	        		$("#qr1").alertmsg('error', '该软件信息库中不存在！');
	        	}else if(msg == 'InStorage'){
	        		$("#qr1").alertmsg('error', '该软件在库中，禁止修改软件状态操作！');
	        	}else if(msg == 'success'){
 	      			ok();	
	        	}else{
	        		$("#qr1").alertmsg('error', '修改软件状态失败');
	        	}
	        }
	    });
	});
	function ok(){
		$("#qr1").alertmsg('correct', '恭喜您，修改软件状态成功！',{displayMode:"none",alertTimeout:1500});
		$.CurrentDialog.dialog("closeCurrent");
	}
</script>
<div class="bjui-pageContent">
	<form method="post" id='software-status-form' name="softwareStatusForm" action="<c:url value=""/>" data-toggle="validate" data-alertmsg="false" autocomplete="off" data-validator-option="{theme:'simple_right'}">
			<table class="table table-condensed table-hover" width="100%">
				<tbody>
					<br/>
					<br/>
					<br/>
						<tr>
							    
					   		<td>
								<label for="j_custom_name" class="control-label">当前软件状态：</label>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TO_BE_IN_STORAGE')}">${bxt:getEnumName("SoftWareStatusEnum","TO_BE_IN_STORAGE")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_PAYMENT")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_TRIAL")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TRIAL_OK')}">${bxt:getEnumName("SoftWareStatusEnum","TRIAL_OK")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CORRECT')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CORRECT")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}">${bxt:getEnumName("SoftWareStatusEnum","PENDING_CERTIFICATE")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}">${bxt:getEnumName("SoftWareStatusEnum","CERTIFICATE_OK")}</c:if>
								<c:if test="${softwareStatusForm.softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','REVOKE')}">${bxt:getEnumName("SoftWareStatusEnum","REVOKE")}</c:if>
							</td> 
							    
							<td>
								<label for="j_custom_name" class="control-label">预期设置状态 ：</label>
								<select name="softwarestatus" id="softwarestatus" data-toggle="selectpicker" data-rule="required;">
									<option value="">请选择</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_PAYMENT")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_TRIAL")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','TRIAL_OK')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','TRIAL_OK')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","TRIAL_OK")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_CORRECT')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CORRECT')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_CORRECT")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_CERTIFICATE")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','CERTIFICATE_OK')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","CERTIFICATE_OK")}</option>
									<option value="${bxt:getEnumValue('SoftWareStatusEnum','REVOKE')}" <c:if test="${softwarestatus eq bxt:getEnumValue('SoftWareStatusEnum','REVOKE')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","REVOKE")}</option>
								</select>
					    	</td>
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
        <li><button id="qr1" type="submit" class="btn-green" >保存</button></li>
    </ul>
</div>