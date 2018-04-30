<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="login.title"/></title>
</head>

<body id="login_page">
<div id="login_tabs">
    <div id="login" title="Login" class="tab x-hide-display">
    	<p class="help">
    		<app:helpLink key="/login.html" section="login" />
    	</p>
    	<%-- HACK TO FIX SECURITY ISSUE/PCI TEST ISSUE.  Unable to fix it via Tomcat Configuration --%>
    	<%
    	String action = "/j_security_check" ;
    	if (request.getServerName().equals("cars.belk.com") && !request.getProtocol().equals("https")) {
    		action = "https://" + request.getServerName()+ request.getContextPath()+ action ;
    	}
   		request.setAttribute("action", action) ;
   		
   		// remove cars_filter cookie so that cars_filter set to default on every login
   		
   		Cookie myCookie = new Cookie("cars_filter", "");
   		myCookie.setMaxAge(0);
   		myCookie.setPath("/");
   		response.addCookie(myCookie);
   		
    	%>
		<c:url value="${action}" var="formAction"/>
        <form method="post" id="loginForm" action="${formAction}">
        <fieldset>
			<ol>
            <c:choose>
			    <c:when test="${param.vendor != null}">
			        <li class="error">
                        <img src="${ctx}/images/iconWarning.gif" alt="<fmt:message key='icon.warning'/>" class="icon"/>
                        <fmt:message key="errors.vendor.disabled"/>
                    </li>
			    </c:when>    
			    <c:otherwise>
				    <c:if test="${param.error != null}">
		                <li class="error">
		                    <img src="${ctx}/images/iconWarning.gif" alt="<fmt:message key='icon.warning'/>" class="icon"/>
		                   <!--  <fmt:message key="errors.password.mismatch"/>  -->
		                   <!--   <c:out value="${sessionScope.ACEGI_SECURITY_LAST_EXCEPTION.message}"/> -->
		                    <fmt:message key="errors.invalid.user.name.or.password"/>
		                </li>
		            </c:if>
			     <li class="text">
                    <label for="j_username"><fmt:message key="label.username"/>:</label>
                    <input type="text" id="j_username" name="j_username" />
                 </li>
                 <li class="text">
                    <label for="j_password"><fmt:message key="label.password"/>:</label>
                    <input type="password" id="j_password" name="j_password" />
                 </li>
                 <li>
                    <input type="submit" class="btn" value="<fmt:message key='button.login'/>" />
                    <a href='<c:url value="forgotPassword.html"/>'><fmt:message key="login.passwordHint"/></a>
                 </li>
			    </c:otherwise>
			</c:choose>
            </ol>
		</fieldset>
		</form>
    </div>
    <c:if test="${not empty param.vendorUserId}">
    	<c:import url="activateVendor.jsp"/>
    </c:if>    
	<div id="contact_belk" title="Contact Belk" class="tab x-hide-display">
		<%@ include file="/common/contact.jsp"%>
	</div>
</div>
</body>

<content tag="inlineStyle">
#ee_jd{
	display:none;
}
#ee_car{
	display:none;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/js/belk.cars.login.js'/>"></script>
]]>
</content>

