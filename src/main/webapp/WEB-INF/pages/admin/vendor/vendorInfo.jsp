<%@ include file="/common/taglibs.jsp"%>
<div class="userButtons">
	<secureurl:secureAnchor cssStyle="btn" name="VendorEdit" title="Edit" localized="true" hideUnaccessibleLinks="true" arguments="${user.id},editUser,list"/>	
	<secureurl:secureAnchor cssStyle="btn" name="VendorList" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="list"/>
	<secureurl:secureAnchor cssStyle="btn" name="ResetVendorPassword" title="Reset Password"  localized="true" hideUnaccessibleLinks="true" arguments="${user.username}"/>
</div>
<ul class="user_info">
	<li class="first"><strong><fmt:message key="user.details.first.name"/>: </strong> <c:out value="${user.firstName}"/></li>
	<li class="last"><strong><fmt:message key="user.details.last.name"/>: </strong> <c:out value="${user.lastName}"/>
	<li class="email"><strong><fmt:message key="user.details.email"/>: </strong> <c:out value="${user.username}"/>
	<li class=""><strong><fmt:message key="user.details.type"/>: </strong> <c:out value="${user.userType.name}"/></li>
	<c:if test="${not empty user.roles}">
	<li class="roles">
		<strong><fmt:message key="user.details.roles"/>: </strong>
		<ul>
			<c:forEach items="${user.roles}" var="role">
				<li><c:out value="${role.description}"/></li>	
			</c:forEach>
		</ul>
	</li>
	</c:if>
	<li class="addr1"><strong><fmt:message key="user.details.address1"/>: </strong> <c:out value="${user.addr1}"/></li>
	<li class="addr2"><strong><fmt:message key="user.details.address2"/>: </strong> <c:out value="${user.addr2}"/></li>
	<li class="zip"><strong><fmt:message key="user.details.city"/>, <fmt:message key="user.details.state"/> <fmt:message key="user.details.zip"/>: </strong> <c:out value="${user.city}"/>, <c:out value="${user.stateCd}"/> <c:out value="${user.zipcode}"/><br/></li>
	<li class="phone"><strong><fmt:message key="user.details.phone"/>: </strong> <c:out value="${user.formattedPhoneNumber}"/></li>
	<li class="alt_phone"><strong><fmt:message key="user.details.phone.alt"/>: </strong> <c:out value="${user.formattedAltPhoneNumber}"/></li>
</ul>
