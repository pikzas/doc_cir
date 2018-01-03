<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	$('#addFF-storage-form').bind('valid.form', function(){
		var mark = $("#mark input[name='radio1']:checked").val();
		$.ajax({
			type:"POST",
			url:"<c:url value='/back/warehouse/addFFWarehouse.do'/>",
			data:{
				name:$("#name").val(),
				mark:mark
				},
			success:function(msg){
				if(msg=="success"){
					softFunctionOk();
				}else if(msg=="fail"){
					softFunctionFail();
				}else if(msg=="notNull"){
					$(this).alertmsg('error', "该仓位号已存在，请命名其他名称！");
				}else{
					softFunctionOk();
				}
			}
		});
	});
	function softFunctionOk(){
		$.CurrentDialog.dialog("closeCurrent");
	}
	function softFunctionFail(){
		$.CurrentDialog.dialog("closeCurrent");
	} 
</script>
<div class="bjui-pageContent" data-toggle="refreshlayout">
	<form method="post" id='addFF-storage-form' name="addFF-storage-form" action="<c:url value=""/>" data-toggle="validate" data-alertmsg="false">
        <table class="table table-condensed table-hover">
            <tbody>
	            <tr>
	                <td align="center"><h4>添加一级仓库</h4></td>
	            </tr>
	            <tr align="center">
	                <td>
	                    <label for="j_dialog_operation" class="control-label">仓位编号：</label>
	                    <input type="text" id="name" name="name" data-tip="请输入英文仓位名称！" data-rule="required;EnglishCall;" data-rule-EnglishCall="[/^[a-zA-Z]*$/, '请您输入英文仓位！']"/>
	                </td>
	            </tr>
	            <tr align="center">
	                <td id="mark">
	                    <label for="j_dialog_operation" class="control-label">仓库分类：</label>
	                    <input type="radio" name="radio1" id="j_form_radio1" value="1" data-toggle="icheck" data-label="普通仓库" data-rule="checked">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                    <input type="radio" name="radio1" id="j_form_radio2" value="2" data-toggle="icheck" data-label="逾期仓库" data-rule="checked">
	                </td>
	            </tr>
            </tbody>
        </table>
    </form>
</div>
<div class="bjui-pageFooter">
    <ul>
        <li><button type="button" class="btn-close" data-icon="close">关闭</button></li>
        <li><button id="AFFWHbtn1" type="submit" class="btn-green">添加</button></li>
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
