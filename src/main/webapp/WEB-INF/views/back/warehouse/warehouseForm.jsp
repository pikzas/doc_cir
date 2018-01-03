<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	$("#name").change(function(){
		$.ajax({
			type:"POST",
			url:"<c:url value='/back/warehouse/duplicateFWarehouse.do'/>",
			data:{ffid:$("#FFid").val(),name:$("#name").val()},
			success:function(msg){
				if(msg=="success"){
					
				}else{
					doubleFName();
				}
			}
		});
	});
	$('#add-fwareHouse-form').bind('valid.form', function(){
		$("#AFWHbtn1").hide();
		$.ajax({
			type:"POST",
			url:"<c:url value='/back/warehouse/addFWarehouse.do'/>",
			data:{ffid:$("#FFid").val(),name:$("#name").val(),totalspace:$("#totalspace").val(),description:$("#description").val()},
			success:function(msg){
				if(msg=="success"){
					addFOk();
				}else{
					addFFail();
				}
			}
		});
	});
	function addFOk(){
		$.CurrentDialog.dialog("closeCurrent");
	}
	function addFFail(){
		$.CurrentDialog.dialog("closeCurrent");
	} 
	function doubleFName(){
		$("#name").val("");
		$("#FFid").alertmsg('error', '您的仓库名已存在，请重新输入仓库名！');
	}
</script>
<div class="bjui-pageContent" data-toggle="refreshlayout">
	<form method="post" id='add-fwareHouse-form' name="add-fwareHouse-form" action="<c:url value=""/>" data-toggle="validate" data-alertmsg="false">
		<input type="hidden" id="FFid" name="ffid" value="${fid}"/>
        <table class="table table-condensed table-hover">
            <tbody>
	            <tr>
	                <td colspan="3" align="center"><h4>添加仓位</h4></td>
	            </tr>
	            <tr align="center">
	                <td>
	                    <label for="j_dialog_operation" class="control-label">仓位编号：${FFstorage.name}</label>
	                    <input type="text" id="name" name="name" data-tip="请输入整数！" data-rule="required;digits"/>
	                </td>
	                <td>
	                    <label for="j_dialog_code" class="control-label">容限：</label>
	                    <input type="text" id="totalspace" name="totalspace" data-tip="请输入整数！" data-rule="required;digits"/>
	                </td>
	                <td>
	                    <label for="j_dialog_code" class="control-label">备注：</label>
	                    <input type="text" id="description" name="description"/>
	                </td>
	            </tr>
            </tbody>
        </table>
     </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li><button type="button" class="btn-close" data-icon="close">关闭</button></li>
        <li><button id="AFWHbtn1" type="submit" class="btn-green" >添加</button></li>
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
