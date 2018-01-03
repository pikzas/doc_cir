<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/back_authority.jsp"%>
<script	type="text/javascript">
function delNewsById(id){
	$("#bt1").alertmsg('confirm', '确认删除？',{ okCall:function(){
		toConfirm(id);
	}});
}
function toConfirm(id){
	$.ajax({
		type:"POST",
		data:{id:id},
		url:'<c:url value="/back/system/removeNews.do"/>',
		success:function(msg){
			if("success"==msg){
				$.CurrentNavtab.navtab("refresh");
			}
		}
	});
}
</script>
<div class="bjui-pageHeader">
    <form id="pagerForm" data-toggle="ajaxsearch" action="<c:url value='/back/system/queryNews.do'/>" method="post">
        <input type="hidden" name="pageSize" value="${model.pageSize}"/>
        <input type="hidden" name="pageCurrent" value="${model.pageCurrent}"/>
        <input type="hidden" name="orderField" value="${param.orderField}"/>
        <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
        <div class="bjui-searchBar">
            <label>新闻标题:</label><input name="newsName" type="text" value="${newsName}"  class="form-control" size="10"/>&nbsp;&nbsp;
            <label>新闻副标题:</label><input name="title" type="text" value="${title}"  class="form-control" size="10"/>&nbsp;&nbsp;
			<label>作者:</label><input name="author" type="text" value="${author}"  class="form-control" size="10"/>&nbsp;&nbsp;
            <label>新闻状态:</label>
            <select name="newsStatus" data-toggle="selectpicker">
				<option value="">--请选择--</option>
				<option value="${bxt:getEnumValue('NewsStatusEnum','WAIT_NEWS')}" <c:if test="${newsStatus eq bxt:getEnumValue('NewsStatusEnum','WAIT_NEWS')}">selected</c:if>>${bxt:getEnumName("NewsStatusEnum","WAIT_NEWS")}</option>
				<option value="${bxt:getEnumValue('NewsStatusEnum','SUCCESS_NEWS')}" <c:if test="${newsStatus eq bxt:getEnumValue('NewsStatusEnum','SUCCESS_NEWS')}">selected</c:if>>${bxt:getEnumName("NewsStatusEnum","SUCCESS_NEWS")}</option>
				<option value="${bxt:getEnumValue('NewsStatusEnum','FAILURE_NEWS')}" <c:if test="${newsStatus eq bxt:getEnumValue('NewsStatusEnum','FAILURE_NEWS')}">selected</c:if>>${bxt:getEnumName("NewsStatusEnum","FAILURE_NEWS")}</option>
			</select>&nbsp;&nbsp;
            <button type="submit" class="btn-default" data-icon="search">查询</button>&nbsp;&nbsp;
            <a href="<c:url value="/back/system/newsForm.do?&flag=2"/>" class="btn btn-green" data-on-close="beforeClose" data-on-load="DonLoad" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">添加新闻</a>
        </div>
    </form>
</div>
<div class="bjui-pageContent tableContent">
    <table class="table table-bordered table-hover table-striped table-top" data-selected-multi="true">
        <thead>
            <tr>
			    <th>新闻标题</th>
				<th>新闻副标题</th>
				<th>作者</th>
				<th>发布时间</th>
				<th>排序</th>
				<th>新闻状态</th>
				<th>可用状态</th>
				<th>新闻类目</th>
				<th>文章关键词</th>
				<th>是否设为文章推荐</th>
				<th>操作</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${po.results}" var="info" varStatus="s">
            <tr data-id="${info.id}">
				<td>${info.newsName}</td>
				<td>${info.title}</td>
				<td>${info.author}</td>
				<td>
					<fmt:formatDate pattern="yyyy-MM-dd" value="${info.publishDate}" />
				</td>
				<td>${info.sorts}</td>
				<td>
					<c:if test="${info.newsStatus eq bxt:getEnumValue('NewsStatusEnum','WAIT_NEWS')}">${bxt:getEnumName("NewsStatusEnum","WAIT_NEWS")}</c:if>
					<c:if test="${info.newsStatus eq bxt:getEnumValue('NewsStatusEnum','SUCCESS_NEWS')}">${bxt:getEnumName("NewsStatusEnum","SUCCESS_NEWS")}</c:if>
					<c:if test="${info.newsStatus eq bxt:getEnumValue('NewsStatusEnum','FAILURE_NEWS')}">${bxt:getEnumName("NewsStatusEnum","FAILURE_NEWS")}</c:if>
				</td>
				<td>
					<c:if test="${info.delFlag eq false }">
						可用
					</c:if>
					<c:if test="${info.delFlag eq true }">
						不可用
					</c:if>
				</td>
				<td>
					<c:forEach items="${Type }" var="type">
							<c:choose>
								<c:when test="${info.pid == type.key}">
									${type.value }
								</c:when>
							</c:choose>
						</c:forEach>
				</td>
				<td>${info.isRecommend}</td>
				<td>
					<c:if test="${info.isRecommend eq 0 }">
						否
					</c:if>
					<c:if test="${info.isRecommend eq 1 }">
						是
					</c:if>
				</td>
                <td>
             		<a href="<c:url value="/back/system/newsForm.do?id=${info.id}&flag=1"/>" class="btn btn-default" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">预览</a>
                	<a href="<c:url value="/back/system/newsForm.do?id=${info.id}&flag=2"/>" class="btn btn-blue" data-on-close="beforeClose" data-on-load="DonLoad" data-icon="search" data-toggle="dialog" data-width="800" data-height="400" data-id="dialog-mask" data-mask="true">编辑</a>
                    <button id="bt1" data-on-close="beforeClose" data-on-load="DonLoad" type="button" class="btn-orange" onclick="delNewsById('${info.id}')">删除</button>
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