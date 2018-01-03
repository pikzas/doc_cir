<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/include/taglib.jsp"%>
<c:if test="${empty sessionScope.SESSION_admin} ">
 <script language="javascript" type="text/javascript">
  window.location.href="<c:url value='/manage.jsp'/>"; 
  </script>
</c:if>
