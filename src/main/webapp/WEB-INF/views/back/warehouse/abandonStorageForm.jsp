<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	function ABStorage(){
		if($("#usespace").val()!=0){
			$("#ABFWHbtn1").alertmsg('error', '您的仓库还有使用，请检查后操作！');
			return;
		}
		ABStorageToConfirm();
		$("#ABFWHbtn1").hide();
	}
	function ABStorageToConfirm(){
		$.ajax({
			type:"POST",
			url:"<c:url value='/back/warehouse/abandonStorage.do'/>",
			data:{id:$("#id").val(),abandonreason:$("#abandonreason").val()},
			success:function(msg){
				if(msg=="success"){
					ABStorageOk();
				}else{
					ABStorageFail();
				}
			}
		});
	}
	
	function ABStorageOk(){
		$.CurrentDialog.dialog("closeCurrent");
	}
	function ABStorageFail(){
		$.CurrentDialog.dialog("closeCurrent");
	} 
</script>
<div class="bjui-pageContent" data-toggle="refreshlayout">
		<input type="hidden" id="id" name="id" value="${id}"/>
        <table class="table table-condensed table-hover">
            <tbody>
	            <tr>
	                <td colspan="4" align="center"><h4>仓位废弃</h4></td>
	            </tr>
	            <tr align="center">
	                <td>
	                    <label for="j_dialog_operation" class="control-label">仓位编号：</label>
	                    <input type="text" id="name" name="name" value="${storage.name}" disabled="disabled"/>
	                </td>
	                <td>
	                    <label for="j_dialog_code" class="control-label">容限：</label>
	                    <input type="text" id="totalspace" name="totalspace" value="${storage.totalspace}" disabled="disabled"/>
	                </td>
	                <td>
	                    <label for="j_dialog_code" class="control-label">已使用量：</label>
	                    <input type="text" id="usespace" name="usespace" value="${storage.usespace}" disabled="disabled"/>
	                </td>
	                <td>
	                    <label for="j_dialog_code" class="control-label">备注：</label>
	                    <input type="text" id="description" name="description" value="${storage.description}" disabled="disabled"/>
	                </td>
	                
	            </tr>
	            <tr align="center">
	            	<td colspan="4">
	                    <label for="j_dialog_code" class="control-label">废弃原因：</label>
	                    <textarea id="abandonreason" name="abandonreason" >${storage.abandonreason}</textarea>
	                </td>
	            </tr>
            </tbody>
        </table>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li><button type="button" class="btn-close" data-icon="close">关闭</button></li>
        <li><button id="ABFWHbtn1" type="button" class="btn-green" onclick="ABStorage()">废弃</button></li>
    </ul>
</div>
<script type="text/javascript">
//加载前
function DonLoad(){
	$.CurrentNavtab.navtab("refresh");
	return true;
} 
//页面关闭刷新当前页面
function beforeClose(){
	$.CurrentNavtab.navtab("refresh");
	return true;
} 
</script>
