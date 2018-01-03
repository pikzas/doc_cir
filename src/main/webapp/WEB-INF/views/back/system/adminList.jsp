<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script	type="text/javascript">
function delAdminById(id){
	$("#bt1").alertmsg('confirm', '确认删除？',{ okCall:function(){
		toConfirm(id);
	}});
}
function toConfirm(id){
	$.ajax({
		type:"POST",
		data:{id:id},
		url:'<c:url value="/back/system/removeAdmin.do"/>',
		success:function(msg){
			if("success"==msg){
				$.CurrentNavtab.navtab("refresh");
			}
		}
	});
}
</script>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/system/queryAllAdmin.do'/>" method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}"/>
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}"/>
        <input type="hidden" name="orderField" value="${param.orderField}"/>
        <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
        <div class="bjui-searchBar">
            <label>登录名:</label><input name="loginName" type="text" value="${loginName}"  class="form-control" size="10"/>&nbsp;
            <label>真实姓名:</label><input name="relName" type="text" value="${relName}"  class="form-control" size="10"/>&nbsp;
            <label>角色${role}:</label>
            <select name="role" data-toggle="selectpicker">
				<option value="">请选择</option>
                <c:forEach var="role" items="${roleList}">
                    <option value="${role.id}" <c:if test="${role.id eq roleId}">selected="selected"</c:if>>${role.name}</option>
                </c:forEach>
			</select>&nbsp;
            <label>状态:</label>
            <select name="state" data-toggle="selectpicker">
				<option value="">请选择</option>
				<option value="${bxt:getEnumValue('AdminRoleStatusEnum','ACTIVATE')}" <c:if test="${state eq bxt:getEnumValue('AdminRoleStatusEnum','ACTIVATE')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","ACTIVATE")}</option>
				<option value="${bxt:getEnumValue('AdminRoleStatusEnum','ABANDONED')}" <c:if test="${state eq bxt:getEnumValue('AdminRoleStatusEnum','ABANDONED')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","ABANDONED")}</option>
				<option value="${bxt:getEnumValue('AdminRoleStatusEnum','INVALID')}" <c:if test="${state eq bxt:getEnumValue('AdminRoleStatusEnum','INVALID')}">selected</c:if>>${bxt:getEnumName("AdminRoleStatusEnum","INVALID")}</option>
			</select>&nbsp;
			<label>手机:</label><input name="phone" type="text" value="${phone}"  class="form-control" size="10"/>&nbsp;
			<label>EMail:</label><input name="email" type="text" value="${email}"  class="form-control" size="10"/>&nbsp;
            <button type="submit" class="btn-default" data-icon="search">查询</button>&nbsp;
            <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
            <a href="<c:url value="/back/system/saveAdminForm.do?flag=2"/>" class="btn btn-green" data-on-close="beforeClose" data-on-load="DonLoad" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">添加</a>
        </div>
    </form>
</div>
<div class="bjui-pageContent tableContent">
    <table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
        <thead>
            <tr>
			    <th>登录名</th>
                <th>真实姓名</th>
                <th>角色</th>
                <th>状态</th>
                <th>手机</th>
                <th>EMail</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${po.results}" var="info" varStatus="s">
            <tr data-id="${info.ID}">
				<td>${info.loginName}</td>
                <td>${info.relName}</td>
                <td>
                    <c:forEach var="userRole" items="${info.userRoleList }" varStatus="i">
                        <c:if test="${i.index !=0}">
                            ,
                        </c:if>
                        ${userRole.role.name }
                    </c:forEach>
                </td>
                <td>
	                <c:if test="${info.state eq bxt:getEnumValue('AdminRoleStatusEnum','ACTIVATE')}">${bxt:getEnumName("AdminRoleStatusEnum","ACTIVATE")}</c:if>
	               	<c:if test="${info.state eq bxt:getEnumValue('AdminRoleStatusEnum','ABANDONED')}">${bxt:getEnumName("AdminRoleStatusEnum","ABANDONED")}</c:if>
	               	<c:if test="${info.state eq bxt:getEnumValue('AdminRoleStatusEnum','INVALID')}">${bxt:getEnumName("AdminRoleStatusEnum","INVALID")}</c:if>
                </td>
                <td>${info.phone}</td>
                <td>${info.email}</td>
                <td>
                	<a href="<c:url value="/back/system/saveAdminForm.do?id=${info.ID}&flag=1"/>" class="btn btn-default" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">信息查看</a>
                	<a href="<c:url value="/back/system/saveAdminForm.do?id=${info.ID}&flag=2"/>" class="btn btn-blue" class="btn btn-blue" data-on-close="beforeClose" data-icon="search" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">编辑</a>
				    <button id="bt1" data-on-close="beforeClose" data-on-load="DonLoad" type="button" class="btn-orange" onclick="delAdminById('${info.ID}')">删除</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<div class="bjui-pageFooter">
    <div class="pages">
        <span>每页&nbsp;</span>
        <div class="selectPagesize">
            <select data-toggle="selectpicker" data-toggle-change="changepagesize">
                <option value="20">20</option>
                <option value="30">30</option>
                <option value="60">60</option>
                <option value="120">120</option>
                <option value="150">150</option>
            </select>
        </div>
        <span>&nbsp;条，共 ${po.total} 条</span>
    </div>
    <div class="pagination-box" data-toggle="pagination" data-total="${po.total}" data-page-size="${po.neverypage}" data-page-current="1">
    </div>
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