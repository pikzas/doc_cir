<%@page contentType="text/html; charset=GBK" %>
<%

String what = request.getParameter("what");
String value = request.getParameter("value");
System.out.println("what="+what+" value="+value);
//���ش�����Ϣ���ͻ���
if("email".equals(what) && "badqiu@gmail.com".equals(value)) {
	out.println("email exists: message from server");
}
%> 