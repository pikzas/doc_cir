<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<c:if test="${empty sessionScope.SESSION_member} ">
 <script language="javascript" type="text/javascript">
 /* <meta http-equiv="refresh" content="20;url=http://www.wyxg.com">  指隔20秒后跳转到http://www.wyxg.com页面 */
  window.location.href="<c:url value="/index.do"/>"; 
  </script>
</c:if>
