<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<meta http-equiv="refresh" content="3;url=<c:url value='/manage.jsp'/>">
<script type="text/javascript">
function severCheck(){
	$("#loginForm").submit();
	$.CurrentDialog.closeCurrent();
}
</script>
<div class="bjui-pageContent">
    <form action="<c:url value='/system/login.do'/>" data-toggle="dialog" method="post" name="loginForm" id="loginForm">
    		<input type="hidden" id="keyid" name="keyid"/>
			<input type="hidden" id="keyname" name="keyname"/>
        <hr>
        <div class="form-group">
            <label for="j_pwschange_oldpassword" class="control-label x85">用户名：</label>
            <input type="text" data-rule="required" name="j_username" value="" id="loginname" placeholder="用户名" size="20">
        </div>
        <div class="form-group">
            <label for="j_pwschange_oldpassword" class="control-label x85">密码：</label>
            <input type="password" data-rule="required" name="keypassword" id="keypassword" value="" placeholder="密码" size="20">
        </div>
<div class="bjui-pageFooter">
    <ul>
        <li><button type="button" class="btn-close">取消</button></li>
        <li><button type="button" onclick="severCheck();"  class="btn-default">提交</button></li>
    </ul>
</div>
    </form>
</div>