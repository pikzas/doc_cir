<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript;">
	function to_clearValue(){
		$("#storeName").val("");
		$("#surplus").val("");
		alert($(".isabandon option:first").text());
		$(".isabandon").find("option[text='请选择']").attr("selected",true);
	}
</script>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/warehouse/FwarehouseList.do'/>" method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}"/>
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}"/>
        <input type="hidden" name="orderField" value="${param.orderField}"/>
        <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
        <input type="hidden" id="ffid" name="fid" value="${fid}"/>
        <div class="bjui-searchBar">
            <label>仓位编号:</label><input id="storeName" name="storeName" type="text" value="${storeName}"  class="form-control" size="10"/>&nbsp;
            <label>剩余容量:</label><input id="surplus" name="surplus" type="text" value="${surplus}"  class="form-control" size="10"/>&nbsp;
            <label>状态:</label>
            <select class="isabandon" name="isabandon" data-toggle="selectpicker">
				<option value="">请选择</option>
				<option value="${bxt:getEnumValue('StorageStatusEnum','IS_USE')}" <c:if test="${isabandon eq bxt:getEnumValue('StorageStatusEnum','IS_USE')}">selected</c:if>>${bxt:getEnumName("StorageStatusEnum","IS_USE")}</option>
				<option value="${bxt:getEnumValue('StorageStatusEnum','IS_ISABANDON')}" <c:if test="${isabandon eq bxt:getEnumValue('StorageStatusEnum','IS_ISABANDON')}">selected</c:if>>${bxt:getEnumName("StorageStatusEnum","IS_ISABANDON")}</option>
			</select>&nbsp;
            <button type="submit" class="btn-default" data-icon="search">查询</button>&nbsp;
            <%--
            <a class="btn btn-orange" data-clear-query="true" data-icon="undo" onclick="to_clearValue()">重置</a>
            <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
            --%>
            <a href="<c:url value="/back/warehouse/addFWarehouseForm.do?fid=${fid}"/>" class="btn btn-green" data-on-close="beforeClose" data-toggle="dialog" data-width="800" data-height="300" data-id="dialog-mask" data-mask="true">添加二级仓库</a>
        </div>
    </form>
</div>
<div class="bjui-pageContent tableContent">
    <table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
        <thead>
            <tr>
			    <th>仓位编号</th>
                <th>容限</th>
                <th>剩余可存放数量</th>
                <th>是否废弃</th>
                <th>备注</th>
                <th>废弃原因</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${po.results}" var="info" varStatus="s">
            <tr data-id="${info.id}">
				<td>${info.groupname}${info.name}</td>
                <td>${info.totalspace}</td>
                <td>${info.surplus}</td>
                <td>
                	<c:if test="${info.isabandon eq 0}">正常</c:if>
                	<c:if test="${info.isabandon eq 1}"><font color="red">废弃</font></c:if>
                </td>
                <td>${info.description}</td>
                <td>${info.abandonreason}</td>
                <td>
                	<c:if test="${info.isabandon eq 0}">
	                	<a href="<c:url value="/back/warehouse/expandStorageForm.do?id=${info.id}"/>" class="btn btn-default" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">扩容</a>
	                	<a href="<c:url value="/back/warehouse/abandonStorageForm.do?id=${info.id}"/>" class="btn btn-default" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">废弃</a>
                	</c:if>
                	<c:if test="${info.isabandon eq 1}">
                		<a href="<c:url value="/back/warehouse/abandonStorageView.do?id=${info.id}"/>" class="btn btn-default" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">查看</a>
                	</c:if>
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