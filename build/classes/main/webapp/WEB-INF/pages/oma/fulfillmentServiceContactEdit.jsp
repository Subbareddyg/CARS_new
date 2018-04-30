
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="headerTabsForVendor.jsp"%>
	
	<%@ include file="breadcrumbForVendorContacts.jsp"%>
	
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="headerTab.jsp"%>
	<br>
	<%@ include file="breadcrumbForFulfillmentServiceContacts.jsp"%>
	<br>
</c:if>

<head>
	 <title>
    	<c:choose>
	    <c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	      	 Vendor Contacts
		</c:when>
		<c:otherwise>
	      	 Fulfillment Service Contacts
		</c:otherwise>
	</c:choose>
    </title>
    
    <meta name="heading" content=""/>
    <meta name="menu" content="MainMenu"/>
	<style type="text/css">
	#contact_attr label{
		width:180px;
	}
	</style>
</head>
<body class="admin">
<c:choose>
	<c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
		<B><fmt:message key="contacts.vendorFulfillmentServiceContactsHeader"/></b>
	</c:when>
	<c:otherwise>
		<b><fmt:message key="contacts.fulfillmentServiceContactsHeader"/></b>
	</c:otherwise>
</c:choose>

<div class="cars_panel x-hidden" style="margin: 10px 0px 0px;">
		<%@ include file="omaInfoSection.jsp" %>
	</div>
<div id="attr_container" class="cars_panel x-hidden" style="margin-top: 10px;">
<div class="x-panel-header"><c:choose>
	<c:when test="${contacts.contactId != null}">
		<c:out value="Edit Contact"></c:out>
	</c:when>
	<c:otherwise>
		<c:out value="Add Contact"></c:out>
	</c:otherwise>
</c:choose></div>
<div class="x-panel-body"><spring:bind path="fsContactForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error"><c:forEach var="error"
			items="${status.errorMessages}">
			<c:choose>
				<c:when test="${error == 'Saved Successfully!'}">
					<span style="background: #FFFF00;"> <c:out value="${error}"
						escapeXml="false" /> </span>
					<br />
				</c:when>
				<c:otherwise>
					<img src="<c:url value="/images/iconWarning.gif"/>"
						alt="<fmt:message key="icon.warning"/>" class="icon" />
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:otherwise>
			</c:choose>
		</c:forEach></div>
	</c:if>
</spring:bind> 
 <form:form commandName="fsContactForm" name="fsContactForm"
	action="save.html" method="post" id="fsContactForm">
	<font color="red">*</font> Indicates required field
			<br />
	<div id="contact_attr">
	<ol>
		<li><label><b><fmt:message key="contacts.email" /></b> <span
			class="req" style="color: #FF0000">*</span></label> <form:input tabindex="1"
			disabled="${readOnly}" path="contacts.emailAddress" id="emailAddress" maxlength="50"
			cssClass="text" cssErrorClass="text medium error" /></li>
		<li><label><b><fmt:message key="contacts.fname" /></b><span
			class="req" style="color: #FF0000">*</span></label> <form:input  tabindex="2" maxlength="50"
			path="contacts.firstName" disabled="${readOnly}" id="firstName"
			cssClass="text" cssErrorClass="text medium error" /></li>
		<li><label><b><fmt:message key="contacts.lname" /></b><span
			class="req" style="color: #FF0000">*</span></label> <form:input tabindex="3" maxlength="50"
			path="contacts.lastName" disabled="${readOnly}" id="lastName"
			cssClass="text" cssErrorClass="text medium error" /></li>
		<li><label><b><fmt:message key="contacts.type" /></b><span
			class="req" style="color: #FF0000">*</span></label> <c:set var="otherTypeId"
			value="${sessionScope.OtherContactTypeId}"></c:set> <form:select tabindex="4" 
			id="contactType" disabled="${readOnly}"
			path="contactType.contactTypeId" onchange="dropdwn(this.value);"
			cssClass="input" cssErrorClass="select medium error">
			<form:option value="0">&nbsp;</form:option>
			<form:options items="${FSContactTypes}" itemLabel="contactTypeName"
				itemValue="contactTypeId" />
		</form:select></li>
		<c:choose>
			<c:when
				test="${fsContactForm.contactType.contactTypeId == sessionScope.OtherContactTypeId }">
				<li id="otherTypeDesc" style="display: block">
			</c:when>
			<c:otherwise>
				<li id="otherTypeDesc" style="display: none">
			</c:otherwise>
		</c:choose>
		<label><b><fmt:message key="contacts.otherTypeDesc" /></b><span
			class="req" style="color: #FF0000">*</span></label>
		<form:input path="contacts.otherTypeDesc" disabled="${readOnly}" maxlength="100"
			id="txt_style_descr" cssClass="text"
			cssErrorClass="text medium error" />
		</li>

		<li><label><b><fmt:message key="contacts.jobTitle" /></b></label>
		<form:input path="contacts.jobTitle" disabled="${readOnly}" tabindex="5" maxlength="50"
			id="txt_style_descr" cssClass="text"
			cssErrorClass="text medium error" /></li>
		<li><label ><b><fmt:message
			key="contacts.address.addr1" /></b></label> <form:input path="address.addr1" maxlength="100"
			disabled="${readOnly}" id="txt_style_descr" cssClass="text" tabindex="6" 
			cssErrorClass="text medium error" /></li>
		<li><label><b><fmt:message
			key="contacts.address.addr2" /></b></label> <form:input path="address.addr2" maxlength="100"
			disabled="${readOnly}" id="txt_style_descr" cssClass="text" tabindex="7"
			cssErrorClass="text medium error" /></li>
		<li class="city"><label><b><fmt:message
			key="contacts.address.city" /></b></label> <form:input path="address.city" maxlength="100"
			disabled="${readOnly}" id="txt_style_descr" cssClass="text" tabindex="8"
			cssErrorClass="text small error" size="10"  /></li>
		<li class="state"><label><b><fmt:message
			key="contacts.address.state" /></b></label> <c:set var="States"
			value="${States}" scope="session"></c:set> <form:select id="state" tabindex="9"
			disabled="${readOnly}" path="address.state" cssClass="input">
			<form:option value=""></form:option>
			<form:options items="${States}" itemLabel="stateName"
				itemValue="stateCd" />
		</form:select></li>
		<li class="zip"><label><fmt:message
			key="contacts.address.zip" /></label> <form:input path="address.zip" tabindex="10"
			disabled="${readOnly}" id="zipcode" cssClass="text"
			cssErrorClass="text medium error" size="5" maxlength="5"/></li>
		<li class="phone">
			<label><b><fmt:message key="contacts.address.phoneAreaCode" /></b>
			<span class="req" style="color: #FF0000">*</span> </label>(<form:input path="phoneAreaCode" tabindex="11"
			id="phoneAreaCode" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="3" maxlength="3" />) <form:input tabindex="12"
			path="phoneNumber1" id="phoneNumber1" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="3" maxlength="3" /> <form:input tabindex="13"
			path="phoneNumber2" id="phoneNumber2" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="4" maxlength="4" /></li>
		<li class="alt_phone"><appfuse:label styleClass="desc"
			key="contacts.address.alt.PhoneAreaCode" /> (<form:input tabindex="14"
			path="altPhoneAreaCode" id="altPhoneAreaCode" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="3" maxlength="3" />) <form:input tabindex="15"
			path="altPhoneNumber1" id="altPhoneNumber1" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="3" maxlength="3" /> <form:input tabindex="16"
			path="altPhoneNumber2" id="altPhoneNumber2" disabled="${readOnly}" onkeyup="checkLen(this,this.value)"
			cssErrorClass="small error" size="4" maxlength="4" /></li>
		<li><label><b><fmt:message key="contacts.notes" /></b></label> <form:textarea
			path="contacts.notes" id="txt_contact_notes" tabindex="17" onkeypress="return imposeMaxLength(this, 255);" onchange="return imposeMaxLength(this, 255);"
			disabled="${readOnly}" cssClass="text" onkeyup="return imposeMaxLength(this, 255);" 
			cssErrorClass="text medium error" /> 
			</li>
		<li> <label></label>
			<c:out value="(Max Char:255)"></c:out>
			</li>
		<li><label><b><fmt:message key="contacts.status" /></b></label>
		<form:select path="contacts.status" disabled="${readOnly}" tabindex="18">
			<form:option value="ACTIVE">ACTIVE</form:option>
			<form:option value="INACTIVE">INACTIVE</form:option>
		</form:select></li>
		<li><form:hidden path="contacts.contactId" /> <form:hidden
			path="contacts.lockedBy" /> <form:hidden path="address.addressID" />
			<form:hidden path="fulfillmentServiceId" />
			<form:hidden path="vendorId" />
			<form:hidden path="contacts.createdBy" />		
		</li>
	<li>
		<label></label>
		<c:if
			test="${readOnly != 'true'}">
			<input type="button" align="right" value="Cancel" name="cancel" title="Cancel"
				class="btn cancel_btn" onClick="verifyCancel();" />
			<input type="submit" align="right" value="Save" name="save" title="Save Contact"
				class="btn save_btn" />
		</c:if>
	</li>
	</ol>
	</div>


</form:form></div>
<!-- End of x-panel-body --></div>
<!--End of cars_panel-->



<script type="text/javascript">

function imposeMaxLength(Object, MaxLen)
{
	if(Object.value.length>MaxLen){
		  document.getElementById("txt_contact_notes").value=Object.value.substring(0,MaxLen);
	}
}
 function verifyCancel()
	{
		var stay=confirm("Are you sure you want to cancel?");
		if (stay){
			document.fsContactForm.action="../oma/save.html?cancel=cancel";
			document.fsContactForm.submit();
		}
		else{
		 return false;
		}
	}
//dropship phase 2: added code fix for firefox browser onload
 var browserName=navigator.appName; // Get the Browser Name
	if(browserName=="Microsoft Internet Explorer") // For IE
	{
		window.onload=init; // Call init function in IE
	}
	else
	{
		if (document.addEventListener) // For Firefox
		{
			document.addEventListener("DOMContentLoaded", init, false); // Call init function in Firefox
		}
	}
 
 function init(){
	 document.getElementById("tab4").className="activeTab";
	 var element=document.getElementById('emailAddress');
	if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
	 setTimeout(function() { element.focus(); }, 10);
	}
	 
	}
 function checkLen(x,y){
 //alert("Inside the function");
	if(y.length==x.maxLength){
		var next=x.tabIndex;
		if(next<document.getElementById("fsContactForm").length){
		//alert("Inside the condition");
			document.getElementById("fsContactForm").elements[next+1].focus();
		}
	}
 }
function dropdwn(selectVal){
	var compareVal = <%=request.getSession().getAttribute("OtherContactTypeId")%>
	//alert(selectVal); 
	//alert(compareVal);
 if(selectVal == (compareVal)) 
 {
 	document.getElementById("otherTypeDesc").style.display = 'block';
 }else{
 	document.getElementById("otherTypeDesc").style.display = 'none';
 }
}
</script>
</body>
<v:javascript formName="fsContactForm" staticJavascript="false" />
<script type="text/javascript"
	src="<c:url value="/scripts/validator.jsp"/>"></script>

