<%@ include file="/common/taglibs.jsp"%>

<div id="create_new_account" title="Create New Account" class="tab x-hide-display">
		<p class="help">
			<app:helpLink key="/login.html" section="createAccount" />
		</p>
		
<spring:bind path="vendorCreationForm.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">    
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<c:url value="signin.html" var="formAction"/>
<form:form  commandName="vendorCreationForm" method="post" action="${formAction}" onsubmit="return validateUser(this)" id="activateVendorForm">
 <fieldset>
        	<legend></legend>
<ol>
    <li class="info">
        <fmt:message key="signup.message"/>
    </li>
	    <li>
    	<fmt:message key="activatevendor.page.assigned.departments"/>
	    	<ul>
	    		<c:forEach items="${vendorCreationForm.departments}" var="dept">    		
	    			<li><b><c:out value="${dept}"/></b></li>
	    		</c:forEach>
	    	</ul>
    	</li>
     <li>
    	<fmt:message key="activatevendor.page.assigned.vendors"/>
    	<ul>
    		<c:forEach items="${vendorCreationForm.vendors}" var="vendor">    		
    			<li><b><c:out value="${vendor}"/></b></li>
    		</c:forEach>
    	</ul>
    </li>
    <li class="text">
        <appfuse:label styleClass="desc" key="user.emailAddress"/>        
        <form:input path="emailAddress" id="emailAddress" cssClass="text large" cssErrorClass="text large error"/>
        <form:errors path="emailAddress" cssClass="fieldError"/>
    </li>
    <li class="text">
        <appfuse:label styleClass="desc" key="user.firstName"/>       
        <form:input path="firstName" id="firstName" cssClass="text large" cssErrorClass="text large error"/>
         <form:errors path="firstName" cssClass="fieldError"/>
    </li>
     <li class="text">
        <appfuse:label styleClass="desc" key="user.lastName"/>
        <form:errors path="lastName" cssClass="fieldError"/>
        <form:input path="lastName" id="lastName" cssClass="text large" cssErrorClass="text large error"/>
    </li>
       <form:hidden path="vendorId"/>
    <li>
        <input type="submit"  style="margin-left:180px;" id="btn_activate_vendor" class="btn" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>"/>
    </li>
</ol>
</form:form>
</div>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


