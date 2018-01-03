<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
	$(function(){
		var all = 0;
		$("#mcontent>tr").each(function(){
			if($(this).children('td').size()>1){
				s = $(this).children('td').eq(1).text();
				i = parseInt(s);
				all = all + i;
			}
		});
		$("#usetotal").text("使用量总计："+all);
	});
</script>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/warehouse/findWarehouse.do'/>" method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}"/>
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}"/>
        <input type="hidden" name="orderField" value="${param.orderField}"/>
        <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
        <div class="bjui-searchBar">
            <label>出证日期:</label><input name="certificatedate" data-toggle="datepicker" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${certificatedate}"/>"/>&nbsp;
            <label>剩余容量:</label><input name="surplus" type="text" value="${surplus}"  class="form-control" size="10"/>&nbsp;
            <label>案件状态:</label>
            <select name="softWareState" data-toggle="selectpicker">
				<option value="">请选择</option>
				<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}" <c:if test="${softWareState eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_PAYMENT')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_PAYMENT")}</option>
				<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}" <c:if test="${softWareState eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_TRIAL')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_TRIAL")}</option>
				<option value="${bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}" <c:if test="${softWareState eq bxt:getEnumValue('SoftWareStatusEnum','PENDING_CERTIFICATE')}">selected</c:if>>${bxt:getEnumName("SoftWareStatusEnum","PENDING_CERTIFICATE")}</option>
			</select>&nbsp;
            <button type="submit" class="btn-default" data-icon="search">查询</button>&nbsp;
            <a class="btn btn-orange" href="javascript:;" data-toggle="reloadsearch" data-clear-query="true" data-icon="undo">清空查询</a>
        </div>
    </form>
</div>
<div class="bjui-pageContent tableContent">
    <table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
        <thead>
            <tr>
			    <th>仓位编号</th>
			    <th>使用数量</th>
                <th>容限</th>
                <th>剩余数量</th>
                <th>明细</th>
            </tr>
        </thead>
        <tbody id="mcontent">
        <c:forEach items="${list}" var="info" varStatus="s">
            <tr data-id="${info.id}">
				<td>${info.groupname}${info.name}</td>
                <td>${info.usespace}</td>
                <td>${info.totalspace}</td>
                <td>${info.surplus}</td>
                <td>
                	<a href="<c:url value="/back/warehouse/warehouseList.do?fid=${info.id}"/>" class="btn btn-default" data-toggle="dialog" data-icon="search" data-width="800" data-height="400" data-id="details-message" data-mask="true">查看</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${not empty list}">
	        <tr><td colspan="5" id="usetotal"></td></tr>
        </c:if>
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