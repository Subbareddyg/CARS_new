<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='userProfile.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<c:if test="${param.from != 'profile'}">
<div id="user_form" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Add/Edit CAR User
	</div>
	<div class="x-panel-body">
</c:if>

<spring:bind path="user.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>


<c:url value='/admin/user/userForm.html' var="formAction"/>
<form:form commandName="user" method="post" action="${formAction}" id="userForm" >
	<input type="hidden" id="id" name="id" value="${user.id}" />
	<form:hidden path="version" />
	<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />
	
	<c:if test="${cookieLogin == 'true'}">
		<form:hidden path="password" />
		<form:hidden path="confirmPassword" />
	</c:if>

	<c:if test="${empty user.version}">
		<input type="hidden" name="encryptPass" value="true" />
	</c:if>
	<p class="required">Required fields are noted with an *</p>
	<ol>
	<c:if test="${cookieLogin != 'true'}">
		<li class="email">
			<appfuse:label styleClass="desc" key="user.emailAddress" />			
			<c:choose>
				<c:when test="${param.from != 'profile'}">
					<form:input path="emailAddress" id="emailAddress" cssClass="text large"	cssErrorClass="text medium error" />
				</c:when>
				<c:otherwise>
					<form:input path="emailAddress" id="emailAddress" cssClass="text large readonly" cssErrorClass="text medium error" readonly="true" />
				</c:otherwise>
			</c:choose>		
		</li>
		<li class="first">
			<appfuse:label styleClass="desc" key="user.firstName" />			
			<form:input path="firstName" id="firstName" cssClass="text"
				cssErrorClass="text error" maxlength="50" />
		</li>
		<li class="last">
			<appfuse:label styleClass="desc" key="user.lastName" />			
			<form:input path="lastName" id="lastName" cssClass="text"
				cssErrorClass="text error" maxlength="50" />
		</li>
		<li class="addr1">
			<appfuse:label styleClass="desc" key="user.address.address1" />
			<form:input path="addr1" id="addr1" cssClass="text"
				cssErrorClass="text large error" />
		</li>
		<li class="addr2">
			<appfuse:label styleClass="desc" key="user.address.address2" />
			<form:input path="addr2" id="addr2" cssClass="text"
				cssErrorClass="text large error" />
		</li>
		<li class="city">
			<appfuse:label styleClass="desc" key="user.address.city" />
			<form:input path="city" id="city" cssClass="text"
				cssErrorClass="text city error" />	
		</li>
		<li class="state">
			<appfuse:label key="user.address.province" styleClass="desc" />
			<form:select path="stateCd" id="stateCd" cssClass="text" cssErrorClass="text medium error">
					<form:option value=""></form:option>
                    <form:option value="AK">Alaska</form:option>
                    <form:option value="AL">Alabama</form:option>
                    <form:option value="AR">Arkansas</form:option>
                    <form:option value="AZ">Arizona</form:option>
                    <form:option value="CA">California</form:option>
                    <form:option value="CO">Colorado</form:option>
                    <form:option value="CT">Connecticut</form:option>
                    <form:option value="DC">Washington D.C.</form:option>
                    <form:option value="DE">Delaware</form:option>
                    <form:option value="FL">Florida</form:option>
                    <form:option value="GA">Georgia</form:option>
                    <form:option value="HI">Hawaii</form:option>
                    <form:option value="IA">Iowa</form:option>
                    <form:option value="ID">Idaho</form:option>
                    <form:option value="IL">Illinois</form:option>
                    <form:option value="IN">Indiana</form:option>
                    <form:option value="KS">Kansas</form:option>
                    <form:option value="KY">Kentucky</form:option>
                    <form:option value="LA">Louisiana</form:option>
                    <form:option value="MA">Massachusetts</form:option>
                    <form:option value="MD">Maryland</form:option>
                    <form:option value="ME">Maine</form:option>
                    <form:option value="MI">Michigan</form:option>
                    <form:option value="MN">Minnesota</form:option>
                    <form:option value="MO">Missourri</form:option>
                    <form:option value="MS">Mississippi</form:option>
                    <form:option value="MT">Montana</form:option>
                    <form:option value="NC">North Carolina</form:option>
                    <form:option value="ND">North Dakota</form:option>
                    <form:option value="NE">Nebraska</form:option>
                    <form:option value="NH">New Hampshire</form:option>
                    <form:option value="NJ">New Jersey</form:option>
                    <form:option value="NM">New Mexico</form:option>
                    <form:option value="NV">Nevada</form:option>
                    <form:option value="NY">New York</form:option>
                    <form:option value="OH">Ohio</form:option>
                    <form:option value="OK">Oklahoma</form:option>
                    <form:option value="OR">Oregon</form:option>
                    <form:option value="PA">Pennsylvania</form:option>
                    <form:option value="PR">Puerto Rico</form:option>
                    <form:option value="RI">Rhode Island</form:option>
                    <form:option value="SC">South Carolina</form:option>
                    <form:option value="SD">South Dakota</form:option>
                    <form:option value="TN">Tennessee</form:option>
                    <form:option value="TX">Texas</form:option>
                    <form:option value="UT">Utah</form:option>
                    <form:option value="VA">Virginia</form:option>
                    <form:option value="VT">Vermont</form:option>
                    <form:option value="WA">Washington</form:option>
                    <form:option value="WI">Wisconsin</form:option>
                    <form:option value="WV">West Virginia</form:option>
                    <form:option value="WY">Wyoming</form:option>
				</form:select>			
		</li>
		<li class="zip">
			<appfuse:label key="user.address.postalCode" styleClass="desc" />
			<form:input path="zipcode" id="zipcode" cssClass="text"
				cssErrorClass="text medium error" />
		</li>		
		<li class="phone">
			<appfuse:label styleClass="desc" key="user.phoneAreaCode" />
			(<form:input path="phoneAreaCode" id="phoneAreaCode" 
				cssErrorClass="text small error" size="3" maxlength="3" />)
			<form:input path="phoneNumber1" id="phoneNumber1" 
				cssErrorClass="text small error" size="3" maxlength="3"/>
			<form:input path="phoneNumber2" id="phoneNumber2" cssErrorClass="text small error" size="4" maxlength="4"/>
		</li>
		<li class="alt_phone">
			<appfuse:label styleClass="desc" key="user.address.alt.phone" />
			(<form:input path="altPhoneAreaCode" id="altPhoneAreaCode" 
				cssErrorClass="text small error" size="3" maxlength="3" />)
			<form:input path="altPhoneNumber1" id="altPhoneNumber1" cssErrorClass="text small error" size="3" maxlength="3" />
			<form:input path="altPhoneNumber2" id="altPhoneNumber2" cssErrorClass="text small error" size="4" maxlength="4" />			
		</li>	
						
	</c:if>
	<c:choose>
	<c:when test="${loggedInUser.admin && param.from == 'list' and param.method != 'Add'}">
		<li>		
			<div>
				<appfuse:label key="user.type" styleClass="desc" />
				<c:set var="userTypeList" value="${availableUserTypes}"
					scope="request" />
				<form:select path="userTypeCd">
					<c:forEach items="${userTypeList}" var="userTypeCd">
						<c:if
							test="${userTypeCd.userTypeCd !='VENDOR_PROVIDED_IMAGE'}">
							<c:if
								test="${userTypeCd.userTypeCd !='WEB_MERCHANT' && userTypeCd.userTypeCd != 'VENDOR'}">
							<form:option value="${userTypeCd.userTypeCd}">${userTypeCd.name}</form:option>
						</c:if>
						</c:if>
					</c:forEach>
				</form:select>
			</div>	
		</li>
		
		<li class="primary">
				<label>Primary User:</label>
				<ul>
				<li><form:radiobutton path="primary" value="Y"/>Yes  &nbsp;&nbsp; <form:radiobutton path="primary" value="N" />No</li> 
				</ul>
			</li>
	
		
			 <li class="roles">
				<label>User Roles:</label>
				<ul>
					<c:forEach items="${availableRoles}" var="role">
						<li><form:checkbox path="roles" label=" ${role.description}" value="${role.id}" /></li>
					</c:forEach>
				</ul>
			 </li>
	</c:when>
	<c:when test="${param.from == 'profile'}">
		<input type="hidden" value="<c:out value='${loggedInUser.userType.userTypeCd}'/>" name="userTypeCd" />
	</c:when>
	</c:choose>
		<c:if test="${cookieLogin != 'true'}">
			<c:choose>
			<c:when test="${param.from == 'list' and param.method == 'Add'}">
			<li>
				<div>
					<appfuse:label key="user.type" styleClass="desc" />
					<c:set var="userTypeList" value="${availableUserTypes}"
						scope="request" />
					<form:select path="userTypeCd">
						<c:forEach items="${userTypeList}" var="userTypeCd">
						
							<c:if
								test="${userTypeCd.userTypeCd !='VENDOR_PROVIDED_IMAGE'}">
								<c:if
								test="${userTypeCd.userTypeCd !='WEB_MERCHANT' && userTypeCd.userTypeCd != 'VENDOR'}">
								<form:option value="${userTypeCd.userTypeCd}">${userTypeCd.name}</form:option>
							</c:if>
							</c:if>
						</c:forEach>
					</form:select>
				</div>
	
			</li>
			 <li class="primary">
				<label>Primary User:</label>
				<ul>
				   <li><form:radiobutton path="primary" value="Y"/>Yes  &nbsp;&nbsp; <form:radiobutton path="primary" value="N" />No</li> 
				</ul>
			</li>
			 <li class="roles">
				<label>User Roles:</label>
				<ul>
					<c:forEach items="${availableRoles}" var="role">
						<li><form:checkbox path="roles" label=" ${role.description}" value="${role.id}" /></li>
					</c:forEach>
				</ul>
			 </li>	
			
		
									
			</c:when>
			</c:choose>
		</c:if>
		<li class="buttons">
			<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
			<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			<c:if test="${param.from == 'profile'}">
				<app:helpLink section="profile" key="profileHelp" title="&nbsp;" localized="false"/>
			</c:if>
			<br style="clear:both;" />
		</li>
		<li class="messages">
			
		</li>
	</ol>
</form:form>
<c:if test="${param.from != 'profile'}">
	</div></div>
</c:if>
</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
<![CDATA[
]]>
</content>
