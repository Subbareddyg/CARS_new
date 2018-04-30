<%@ include file="/common/taglibs.jsp" %>

    <div id="divider"><div></div></div>
    <span class="left">
        <c:if test="${pageContext.request.remoteUser != null}">
        <authz:authentication operation="fullName"/>
        </c:if>
    </span>
    <span class="right">
        &copy;  <a href=""/></a>
    </span>
  <%--	path: <c:out value="${pageContext.request.servletPath}"/>  --%>
	<app:helpLink />
<%--
    <secureurl:hasAccessToUrl id="test" name="MainMenu" />
	MainMenu: <c:out value="${test}"/>
	
	<secureurl:hasAccessToUrl id="test" name="Login" />
	Login: <c:out value="${test}"/>
	
	<secureurl:secureAnchor href="/test.html" name="Login" localized="true" />

	<secureurl:secureAnchor href="/main.html" name="MainMenu" arguments="${test}"  localized="true" />
	
--%>    