<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<style type="text/css">
.PENDING_PAYMENT{background-color:#CBAEE7 !important;}
.PENDING_TRIAL{background-color:#C8E7A3 !important;}
.PENDING_CERTIFICATE{background-color:#73C6FF !important;}
.hm_text_center{text-align:center;}
</style>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/warehouse/warehouseList.do'/>" method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}"/>
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}"/>
        <input type="hidden" name="orderField" value="${param.orderField}"/>
        <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
        <input type="hidden" name="fid" value="${fid}"/>
        <div align="center">
        <input type="text" readonly="readonly" value="待缴费" size="6" class="PENDING_PAYMENT hm_text_center" />
        <input type="text" readonly="readonly" value="待分审" size="6" class="PENDING_TRIAL hm_text_center" />
        <input type="text" readonly="readonly" value="待制证" size="6" class="PENDING_CERTIFICATE hm_text_center" />
        </div>
    </form>
</div>
<div class="bjui-pageContent tableContent">
	<input type="hidden" id="ffid" name="ffid" value="${fid}"/>
    <table class="table table-bordered table-top">
        <thead>
            <tr>
			    <th>仓位编号</th>
                <th>流水号</th>
                <th>软件全称</th>
                <th>申请人</th>
                <th>制证日期</th>
                <th>业务员</th>
                <th>审查员</th>
                <th>仓位状态</th>
                <%--
                <th>是否废弃</th>
                 --%>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${po.results}" var="info" varStatus="s">
            <tr data-id="${info.id}" 
            	<c:if test="${info.software.softwarestatus eq '1'}">class="PENDING_PAYMENT"</c:if> 
            	<c:if test="${info.software.softwarestatus eq '2'}">class="PENDING_TRIAL"</c:if> 
            	<c:if test="${info.software.softwarestatus eq '4'}">class="PENDING_PAYMENT"</c:if> 
            	<c:if test="${info.software.softwarestatus eq '5'}">class="PENDING_CERTIFICATE"</c:if>
            	<c:if test="${not empty info.software and info.software.softwarestatus eq '1'}">title='${info.software.serialnum}  待缴费    <fmt:formatDate pattern='yyyy-MM-dd' value='${info.software.certificatedate}'/>'</c:if> 
            	<c:if test="${not empty info.software and info.software.softwarestatus eq '2'}">title='${info.software.serialnum}  待分审    <fmt:formatDate pattern='yyyy-MM-dd' value='${info.software.certificatedate}'/>'</c:if> 
            	<c:if test="${not empty info.software and info.software.softwarestatus eq '4'}">title='${info.software.serialnum}  待补正    <fmt:formatDate pattern='yyyy-MM-dd' value='${info.software.certificatedate}'/>'</c:if> 
            	<c:if test="${not empty info.software and info.software.softwarestatus eq '5'}">title='${info.software.serialnum}  待制证    <fmt:formatDate pattern='yyyy-MM-dd' value='${info.software.certificatedate}'/>'</c:if> 
            	<c:if test="${empty info.software}">title="${info.groupname}${info.name}-${info.houseorder}暂时空余"</c:if> 
            >
				<td>${info.groupname}${info.name}-${info.houseorder}</td>
                <td>${info.software.serialnum}</td>
                <td>${info.software.softwarename}</td>
                <td>${info.software.applyperson}</td>
                <td><fmt:formatDate pattern='yyyy-MM-dd' value='${info.software.certificatedate}'/></td>
                <td>${info.software.salesman}</td>
                <td>${info.software.trialAdmin.relName}</td>
                <td>
                	<c:if test="${info.isuse eq 0}">可用</c:if>
                	<c:if test="${info.isuse eq 1}">占用</c:if>
                </td>
                <%--
                <td>
                	<c:if test="${info.isabandon eq 0}">正常</c:if>
                	<c:if test="${info.isabandon eq 1}">废弃</c:if>
                </td>
                --%>
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