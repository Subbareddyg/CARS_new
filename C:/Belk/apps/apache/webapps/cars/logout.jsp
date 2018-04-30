<%@ include file="/common/taglibs.jsp"%>
<%@ page import="javax.servlet.http.Cookie" %>
<%@ page import="org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices" %>

<%
if (request.getSession(false) != null) {
    session.invalidate();
}
Cookie terminate = new Cookie(TokenBasedRememberMeServices.ACEGI_SECURITY_HASHED_REMEMBER_ME_COOKIE_KEY, null);
String contextPath = request.getContextPath();
terminate.setPath(contextPath != null && contextPath.length() > 0 ? contextPath : "/");
terminate.setMaxAge(0);
response.addCookie(terminate);
%>
<c:choose>
	 <c:when test="${param.vendor != null}">
	     <c:redirect url="signin.html">
            <c:param name="vendor" value="${param.vendor}"></c:param>
        </c:redirect>
	 </c:when>    
	 <c:otherwise>
	     <c:redirect url="/signin.html"/>
	 </c:otherwise>
</c:choose>