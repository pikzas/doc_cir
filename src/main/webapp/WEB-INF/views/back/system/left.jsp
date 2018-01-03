<%@ page contentType="text/html;charset=utf-8" language="java"%>
<%@ include file="/common/include/taglib.jsp"%>
<!-- 本页面涉及的js函数，都在head.jsp页面中     -->
<div id="sidebar" class="menu-min">
	<c:choose>
		<c:when test="${sessionScope.SESSION_admin.role eq '1' }">
			<ul class="nav nav-list">
				<li class="active" id="fhindex"><a href="<c:url value="/system/index.do"/>"><i class="icon-dashboard"></i><span>后台首页</span> </a>
				</li>
				<li id="lm00001"><a href="#" class="dropdown-toggle"> <i class="icon-desktop"></i> <span>系统管理</span> <b class="arrow icon-angle-down"></b> </a>
					<ul class="submenu">
						<li id="x0001"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('x0001','lm00001','系统账号','<c:url value="/back/system/queryAllAdmin.do"/>')"><i class="icon-double-angle-right"></i>系统账号</a>
						</li>
					</ul>
				</li>
				<li id="lm00004"><a href="#" class="dropdown-toggle"> <i class="icon-desktop"></i> <span>软件审核</span> <b class="arrow icon-angle-down"></b> </a>
					<ul class="submenu">
						<li id="z0001"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('z0001','lm00004','待审核信息','<c:url value="/back/audit/softList.do?type=wait"/>')"><i class="icon-double-angle-right"></i>待审核列表</a>
						</li>
						<li id="z0004"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('z0003','lm00004','历史审核列表','<c:url value="/back/audit/softList.do?type=history"/>')"><i class="icon-double-angle-right"></i>历史审核列表</a>
						</li>
					</ul>
				</li>
			</ul>
			
		</c:when>
		<c:when test="${sessionScope.SESSION_admin.role eq '2' }">
			<ul class="nav nav-list">
				<li class="active" id="fhindex"><a href="<c:url value="/system/index.do"/>"><i class="icon-dashboard"></i><span>后台首页</span> </a>
				</li>
				<li id="lm00004"><a href="#" class="dropdown-toggle"> <i class="icon-desktop"></i> <span>软件审核</span> <b class="arrow icon-angle-down"></b> </a>
					<ul class="submenu">
						<li id="z0001"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('z0001','lm00004','待审核信息','<c:url value="/back/softinfo/listDec.do?isBack=3"/>')"><i class="icon-double-angle-right"></i>待审核列表</a>
						</li>
						<li id="z0002"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('z0002','lm00004','待审核信息','<c:url value="/back/softinfo/listDec.do?isBack=3"/>')"><i class="icon-double-angle-right"></i>审核失败</a>
						</li>
						<li id="z0004"><a style="cursor:pointer;" target="mainFrame"
							onclick="siMenu('z0003','lm00004','待审核信息','<c:url value="/back/softinfo/listDec.do?isBack=3"/>')"><i class="icon-double-angle-right"></i>已完成列表</a>
						</li>
					</ul>
				</li>

			</ul>
		</c:when>
	</c:choose>
	<div id="sidebar-collapse">
		<i class="icon-double-angle-left"> </i>
	</div>

</div>
<!--/#sidebar-->

