<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/common/include/back_authority.jsp"%>
<script type="text/javascript">
function do_open_layout(event, treeId, treeNode) {
    $(event.target).bjuiajax('doLoad', {url:treeNode.url, target:treeNode.divid});
    event.preventDefault();
}
</script>
<div class="bjui-pageContent">
	<div style="float:left; width:20%;">
		<a href="<c:url value="/back/warehouse/addFFWarehouseForm.do"/>"class="btn btn-green" data-on-close="beforeClose" data-toggle="dialog" data-width="800" data-height="300" data-id="dialog-mask" data-mask="true">添加一级仓库</a>
		 <ul id="layout-tree" class="ztree" data-toggle="ztree" data-expand-all="false" data-on-click="do_open_layout">
			<c:forEach items="${slist}" var="info" varStatus="s">
				<c:if test="${info.level eq 1 or info.level eq 2}">
					<c:if test="${info.isabandon eq 0}">
						<li data-id="${info.id}" data-pid="${info.fid}" 
							<c:if test="${info.level eq 1}">data-faicon="home"</c:if>
							<c:if test="${info.level eq 2}">data-faicon="database"</c:if>
							<c:if test="${info.level eq 1}">data-url='<c:url value="/back/warehouse/FwarehouseList.do?fid=${info.id}"/>'</c:if>
							<c:if test="${info.level eq 2}">data-url='<c:url value="/back/warehouse/warehouseList.do?fid=${info.id}"/>'</c:if>
							<c:if test="${info.level eq 2 and info.surplus gt 10}"></c:if>
						data-divid="#layout-02">
							<c:if test="${info.level eq 1 and info.mark eq 1}">${info.name}</c:if>
							<c:if test="${info.level eq 1 and info.mark eq 2}">${info.name}(逾期仓库)</c:if>
							<c:if test="${info.level eq 2}">${info.groupname}${info.name}--▶${info.surplus}/${info.totalspace}</c:if>
						</li>
					</c:if>
				</c:if>
			</c:forEach>
		</ul>
	</div>
    <div id="storage" style="width:80%; height:99.9%; overflow:hidden;">
        <div style="height:100%; overflow:hidden;">
        	<fieldset style="height:100%;">
        		<div id="layout-02" style="height:94%; overflow:hidden;">
                    
                </div>
    	</fieldset>
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
