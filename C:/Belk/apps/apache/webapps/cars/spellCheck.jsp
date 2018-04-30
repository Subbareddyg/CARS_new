<%@ include file="/common/taglibs.jsp"%>
<%@page import="java.util.Enumeration"%><do:GetPost url="https://www.google.com/tbproxy/spell?lang=en" method="post">
<%
Enumeration params = request.getParameterNames();
String paramName = null;
String[] paramValues = null;
StringBuffer output=new StringBuffer();

while (params.hasMoreElements()) {
  paramName = (String) params.nextElement();
  paramValues = request.getParameterValues(paramName);
  
  for (int i = 0; i < paramValues.length; i++) {
  	if(!paramName.equals("lang")){
	  	output.append(paramName).append("=");
	    output.append(paramValues[i].toString());
    }
  }
}
%>
<%= output %>
</do:GetPost> 