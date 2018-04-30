<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="signup.title"/></title>
    <meta name="heading" content="<fmt:message key='signup.heading'/>"/>
</head>

<body id="signup"/>

<spring:bind path="user.*">
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

<div class="separator"></div>
<c:url value="signup.html" var="formAction"/>
<form:form commandName="user" method="post" action="${formAction}" onsubmit="return validateUser(this)" id="signupForm">
<ul>
    <li class="info">
        <fmt:message key="signup.message"/>
    </li>
    <li>
        <appfuse:label styleClass="desc" key="user.emailAddress"/>
        <form:errors path="emailAddress" cssClass="fieldError"/>
        <form:input path="emailAddress" id="emailAddress" cssClass="text large" cssErrorClass="text large error"/>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="user.password"/>
                <form:errors path="password" cssClass="fieldError"/>
                <form:password path="password" id="password" cssClass="text medium" cssErrorClass="text medium error" showPassword="true"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="user.confirmPassword"/>
                <form:errors path="confirmPassword" cssClass="fieldError"/>
                <form:password path="confirmPassword" id="confirmPassword" cssClass="text medium" cssErrorClass="text medium error" showPassword="true"/>
            </div>
        </div>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="user.firstName"/>
            <form:errors path="firstName" cssClass="fieldError"/>
            <form:input path="firstName" id="firstName" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
            <appfuse:label styleClass="desc" key="user.lastName"/>
            <form:errors path="lastName" cssClass="fieldError"/>
            <form:input path="lastName" id="lastName" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li>
					<appfuse:label styleClass="desc" key="user.address.phone" />
					(<form:input path="phoneAreaCode" id="phoneAreaCode" 
						cssErrorClass="text small error" onkeydown="TabNext(this,'down',3)" onkeyup="TabNext(this,'up',3,this.form.phoneNumber1)" size="3" maxlength="3" />)
					<form:errors path="phoneAreaCode" cssClass="fieldError" />
					<form:input path="phoneNumber1" id="phoneNumber1" 
						cssErrorClass="text small error" onkeydown="TabNext(this,'down',3)" onkeyup="TabNext(this,'up',3,this.form.phoneNumber2)" size="3" maxlength="3"/>
					<form:errors path="phoneNumber1" cssClass="fieldError" />
					<form:input path="phoneNumber2" id="phoneNumber2" cssErrorClass="text small error" size="4" maxlength="4"/>
					<form:errors path="phoneNumber1" cssClass="fieldError" />

			</li>
				<li>
					<appfuse:label styleClass="desc" key="user.address.alt.phone" />
					(<form:input path="altPhoneAreaCode" id="altPhoneAreaCode" 
						cssErrorClass="text small error"  onkeydown="TabNext(this,'down',3)" onkeyup="TabNext(this,'up',3,this.form.altPhoneNumber1)"  size="3" maxlength="3" />)
					<form:errors path="altPhoneAreaCode" cssClass="fieldError" />
					<form:input path="altPhoneNumber1" id="altPhoneNumber1" cssErrorClass="text small error" onkeydown="TabNext(this,'down',3)" onkeyup="TabNext(this,'up',3,this.form.altPhoneNumber2)" size="3" maxlength="3" />
					<form:errors path="altPhoneNumber1" cssClass="fieldError"  />
					<form:input path="altPhoneNumber2" id="altPhoneNumber2" cssErrorClass="text small error" size="4" maxlength="4" />
					<form:errors path="altPhoneNumber2" cssClass="fieldError" />			
		</li>						
    <li>
        <label class="desc"><fmt:message key="user.address.address"/></label>
        <div class="group">
            <div>
                <form:input path="addr1" id="addr1" cssClass="text large" cssErrorClass="text large error"/>
                <form:errors path="addr1" cssClass="fieldError"/>
                <p><appfuse:label key="user.address.address"/></p>
            </div>
            <div class="left">
                <form:input path="city" id="city" cssClass="text medium" cssErrorClass="text medium error"/>
                <form:errors path="city" cssClass="fieldError"/>
                <p><appfuse:label key="user.address.city"/></p>
            </div>
            <div>
                <form:input path="stateCd" id="stateCd" cssClass="text state" cssErrorClass="text state error"/>
                <form:errors path="stateCd" cssClass="fieldError"/>
                <p><appfuse:label key="user.address.province"/></p>
            </div>
            <div class="left">
                <form:input path="zipcode" id="zipcode" cssClass="text medium" cssErrorClass="text medium error"/>
                <form:errors path="zipcode" cssClass="fieldError"/>
                <p><appfuse:label key="user.address.postalCode"/></p>
            </div>            
        </div>
    </li>
    <li class="buttonBar bottom">
        <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.register"/>"/>
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('signupForm'));
    highlightFormElements();
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>


