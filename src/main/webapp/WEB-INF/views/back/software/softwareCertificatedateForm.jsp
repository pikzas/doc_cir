<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	//接收表单验证通过的事件
	$('#software-form').bind('valid.form', function(){
	    $.ajax({
	        url: '<c:url value="/back/softinfo/updateSoftware.do"/>',
	        type: 'POST',
	        data: {
				id:'${softwareCertificatedateForm.id}',
				adminid:'${softwareCertificatedateForm.adminid}',
				certificatedate:$(this).find("#certificatedate").val(),
				authorizationcode:$("#authorizationcode").val()
			},
	        success: function(msg){
	        	if(msg == 'errorCode'){
	        		$("#qr1").alertmsg('error', '授权码错误！您没有权限修改出证日期');
	        	}else if(msg == 'success'){
 	      			 ok();	
	        	}else{
	        		$("#qr1").alertmsg('error', '修改出证日期失败');
	        	}
	        }
	    });
	});
	function ok(){
		$("#qr1").alertmsg('ok', '恭喜您，修改出证日期成功！',{displayMode:"none",alertTimeout:1500});
		$.CurrentDialog.dialog("closeCurrent");
	}
</script>
<div class="bjui-pageContent">
	<form method="post" id='software-form' name="softwareCertificatedateForm" action="<c:url value=""/>" data-toggle="validate" data-alertmsg="false" autocomplete="off" data-validator-option="{theme:'simple_right'}">
			<table class="table table-condensed table-hover" width="100%">
				<tbody>
					<spring:bind path="softwareCertificatedateForm.id">
				    	<input name="<c:out value="${status.expression}"/>" value="${status.value}" type="hidden" size="15" />
					</spring:bind>
					<spring:bind path="softwareCertificatedateForm.adminid">
				    	<input name="<c:out value="${status.expression}"/>" value="${status.value}" type="hidden" size="15" />
					</spring:bind>
					<c:if test="${flag eq '2'}">
					<br/>
					<br/>
					<br/>
						<tr>
							    
					   		<td>
								<label for="j_custom_name" class="control-label">制证日期：</label>
								<input id="certificatedate" name="certificatedate" value="<fmt:formatDate pattern="yyyy-MM-dd" value='${softwareCertificatedateForm.certificatedate}'/>" type="text" data-toggle="datepicker" data-rule="required" size="15" />
							</td> 
							    
							<td>
								<label for="j_custom_name" class="control-label">授权码：</label>
								<spring:bind path="softwareCertificatedateForm.admin.authorizationcode">
									<input name="<c:out value="${status.expression}"/>"
										value="" type="password" size="15"
										data-rule="required" id="authorizationcode"/>
								</spring:bind>
					    	</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
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
        <c:if test="${flag eq 2}">
        	<li><button id="qr1" type="submit" class="btn-green" >保存</button></li>
        </c:if>
    </ul>
</div>