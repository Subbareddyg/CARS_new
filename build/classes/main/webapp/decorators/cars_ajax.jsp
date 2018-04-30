<%@ include file="/common/taglibs.jsp"%>
<div class="ajaxContent">
	
	<decorator:body/>
</div>
<%-- DONT INCLUDE IN AJAX RESPONSE
<decorator:getProperty property="page.inlineStyle" />
<decorator:getProperty property="page.jscript"/>
--%>
<%--
<%=request.setAttribute("ajax", "true")%>
--%>