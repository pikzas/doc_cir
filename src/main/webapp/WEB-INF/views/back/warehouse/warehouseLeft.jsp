<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ include file="/common/include/back_authority.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
	<!-- zTree -->
	<link rel="stylesheet" href="<c:url value="/static/zTree_v3/css/demo.css"/>" type="text/css"/>
	<link rel="stylesheet" href="<c:url value="/static/zTree_v3/css/zTreeStyle/zTreeStyle.css"/>" type="text/css"/>
	<script type="text/javascript" src="<c:url value="/static/zTree_v3/js/jquery-1.4.4.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/zTree_v3/js/jquery.ztree.core-3.5.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/zTree_v3/js/jquery.ztree.excheck-3.5.js"/>"></script>

    <link href="<c:url value="/static/bxt/css/front.css"/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value="/static/bxt/css/admin.css"/>" rel="stylesheet" type="text/css" />
	<link href="<c:url value="/static/bxt/css/theme.css"/>" rel="stylesheet" type="text/css" />

	<script type="text/javascript">
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			}
		};

		var zNodes =[<c:forEach items="${po.results}" var="info" varStatus="s">{ id:"${info.id}", fid:"${info.fid}", name:"${info.name}",url:"<c:url value='/back/warehouse/warehouseList.do?fid=${info.id}'/>",target:"rightFrame"}<c:if test="${not s.last}">,</c:if></c:forEach>];

		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		//-->
	</script>

    <script type="text/javascript"><%--
        function goto(url) {
            parent.rightFrame.location.href = url;
        }

        function gotoAction(fid) {
            goto('<c:url value="/admin/menuList.af?fid="/>'+fid);
        }

        function gotoInit() {
            goto('<c:url value="/admin/menuList.af"/>');
        }

    --%></script>
</head>
<body class="lbody">
<div class="lttop">
    <a href="javascript:;" onclick="location.reload();">刷新目录</a>
    &nbsp; </div>
<hr/>

	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
</body>
</html>