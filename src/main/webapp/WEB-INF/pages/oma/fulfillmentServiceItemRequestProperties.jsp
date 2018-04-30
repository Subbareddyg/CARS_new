<%@include file="/common/taglibs.jsp" %>
<%@include file="headerItemRequest.jsp" %>
<%@include file="breadcrumbForItemRequest.jsp" %>
<SCRIPT LANGUAGE="JavaScript">

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
	document.getElementById("tab1").className="activeTab";
	enableDisableChecksale();
	displayOnStyleSelection(document.getElementById("stylePopMethodId"));
	displayOnSourceSelection(document.getElementById("sourceId"));
	setReadonly();
	enableDisableRejectReason();
	disableBasedOnRole();
	disableBasedOnAction();
	 var element=document.getElementById('effectiveDate');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
		 var element1=document.getElementById('effectiveDateDisabled');
		 if(element1!=null && element1.type != "hidden" && element1.style.display != "none"  && !element1.disabled  ){
			 setTimeout(function() { element1.focus(); }, 10);
			}
}
function disableBasedOnAction() {
	var requestAction = document.getElementById("itemaction").value;
	var requestMode = '<c:out value="${mode}" />';
	var skuExceptionValue = '<c:out value="${itemRequestForm.enableDisableSkuException}" />';
	var stylePopulationMethod = '<c:out value="${itemRequestForm.stylePopMethodId}" />';
	
	if(requestAction == "Remove Styles/SKUs") {
		document.getElementById("stylePopMethodId").value = "Manual";
		document.getElementById("stylePopMethodId").disabled=true;
		document.getElementById("tab2").disabled=true;
		document.getElementById("tab3").disabled=true;
		if(requestMode == "add") {
			document.getElementById("save").value="Save";
		} else {
			document.getElementById("requeststyle").disabled=true;
		}
	} else {
		document.getElementById("stylePopMethodId").value = stylePopulationMethod;
		document.getElementById("stylePopMethodId").disabled=false;
		document.getElementById("tab2").disabled=false;
		if(skuExceptionValue == "false") {
			document.getElementById("tab3").disabled=false;
		} else {
			document.getElementById("tab3").disabled=true;
		}
		if(requestMode == "add") {
			document.getElementById("save").value="Next";
		} else {
			document.getElementById("requeststyle").disabled=false;
		}
	}
}
function disableBasedOnRole() {
	<c:if test="${itemRequestForm.role == 'read'}">
			for(i=0; i<document.rqstForm.elements.length; i++){
				if(document.rqstForm.elements[i].name!="styleScreen"){
					document.rqstForm.elements[i].disabled=true; 
				}
			}
	</c:if>
}
function loadRequestStyle() {
	window.location="../oma/itemrequeststyles.html";	
}
function setReadonly() {
	var isPosted = '<c:out value="${isposted}"></c:out>';
	var statusApprovedReject = '<c:out value="${itemRequestForm.status}"></c:out>';
	if(isPosted == "true" || statusApprovedReject == "Approved" || statusApprovedReject == "Reject") {
		document.getElementById("createDate").readOnly=true;
		document.getElementById("description").readOnly=true;
		document.getElementById("sourceId").disabled=true;
		document.getElementById("itemaction").disabled=true;
		document.getElementById("checkuserdepts").disabled=true;
		document.getElementById("merchandise").disabled=true;
		document.getElementById("userdepts").disabled=true;
		document.getElementById("minimumMarkup").readOnly=true;
		document.getElementById("stylePopMethodId").disabled=true;
		document.getElementById("file").readOnly=true;
		document.getElementById("locationName").readOnly=true;
		document.getElementById("exceptionReason").disabled=true;
		document.getElementById("rejectReason").readOnly=true;
		document.getElementById("save").disabled=true;
		document.getElementById("reject").disabled=true;
		document.getElementById("approve").disabled=true;
		document.getElementById("post").disabled=true;
	}
}
function setStatus(sbutton,mode){
	//document.getElementById("requestStatus").value=sbutton.value;
	var buttonClicked = sbutton.value;

	if(buttonClicked == "Save & Post") {
		buttonClicked = "post";
	}
	document.rqstForm.action="../oma/addeditItemRequest.html?mode=" + mode + "&statusofrequest=" + buttonClicked;
	document.rqstForm.submit();	
}
function setFileStatus() {
	var style = document.getElementById("stylePopMethodId").value;

	if(style == "Upload") {
		document.getElementById("showTable").value='<c:out value="${itemRequestForm.showTable}" />';
		document.getElementById("filePath").value='<c:out value="${itemRequestForm.filePath}"></c:out>';
		document.getElementById("uploadedStyles").value='<c:out value="${itemRequestForm.uploadedStyles}"></c:out>';
		document.getElementById("failedStyles").value='<c:out value="${itemRequestForm.failedStyles}"></c:out>';
		document.getElementById("validStyles").value='<c:out value="${itemRequestForm.validStyles}"></c:out>';
	} else {
		document.getElementById("showTable").value="0";
		document.getElementById("filePath").value="";
		document.getElementById("uploadedStyles").value="0";
		document.getElementById("failedStyles").value="0";
		document.getElementById("validStyles").value="";
	}
}
function displayOnSourceSelection(box) {
	if(box.value == "IDB Cost Change") 
	{
		var role = '<c:out value="${itemRequestForm.role}"></c:out>';
		if(role != "readwriteapprove") {
			document.getElementById("reject").disabled=true;
			document.getElementById("approve").disabled=true;
		}
		document.getElementById("approve").style.display = 'block';
		document.getElementById("reject").style.display = 'block';
	} else {
		document.getElementById("approve").style.display = 'none';
		document.getElementById("reject").style.display = 'none';
	}
}
function enableDisableChecksale() {
	if(document.getElementById("merchandise").checked){
		document.getElementById("checksale").style.display = 'block';
	}
	else{
		document.getElementById("checksale").style.display = 'none';
	}
}
function displayOnStyleSelection(box) {
	if (box.value == "Drop Ship In IDB Not In ECommerce") {
			document.getElementById("file").style.display = 'none';
			document.getElementById("msg").style.display = 'none';
			document.getElementById("requestid").style.display = 'none';
			document.getElementById("userdept").style.display = 'block';
			document.getElementById("showTable").value="0";
			document.getElementById("filePath").value="";
			if(null != document.getElementById("table")) {
				document.getElementById("table").style.display='none';
			}
			setUserDepartment(document.getElementById("userdept"));
	}
	else if (box.value == "Manual") {
			document.getElementById("file").style.display = 'none';
			document.getElementById("msg").style.display = 'none';
			document.getElementById("requestid").style.display = 'none';
			document.getElementById("userdept").style.display = 'none';
			document.getElementById("showTable").value="0";
			document.getElementById("filePath").value="";
			if(null != document.getElementById("table")) {
				document.getElementById("table").style.display='none';
			}
	} 
	else if (box.value == "Upload") {
			document.getElementById("file").style.display = 'block';
			document.getElementById("msg").style.display = 'block';
			document.getElementById("requestid").style.display = 'none';
			document.getElementById("userdept").style.display = 'none';
	} 
	else if (box.value == "Previous Request") {
			document.getElementById("file").style.display = 'none';
			document.getElementById("msg").style.display = 'none';
			document.getElementById("requestid").style.display = 'block';
			document.getElementById("userdept").style.display = 'none';
			document.getElementById("showTable").value="0";
			document.getElementById("filePath").value="";
			if(null != document.getElementById("table")) {
				document.getElementById("table").style.display='none';
			}
	}
}
function uploadFile(modeSelected) {
	document.rqstForm.action="../oma/addeditItemRequest.html?mode=" + modeSelected;
	document.rqstForm.submit();
}
function rejectStatus() {
	var rejectDisplay = document.getElementById("rejectArea").style.display;
	
	if(rejectDisplay == 'none' ) {
		document.getElementById("rejectArea").style.display = 'block';
	} else {
		var reason = document.getElementById("rejectReason").value;
		var trimmedReason = trimAll(reason);
		if(trimmedReason == "") {
			alert("Please enter reject reason!");
		} else {	
			document.rqstForm.action="../oma/addeditItemRequest.html?mode=edit&statusofrequest=Reject";
			document.rqstForm.submit();
		}
	}
}
function trimAll(sString) 
{ 
while (sString.substring(0,1) == ' ') 
{ 
sString = sString.substring(1, sString.length); 
} 
while (sString.substring(sString.length-1, sString.length) == ' ') 
{ 
sString = sString.substring(0,sString.length-1); 
} 
return sString; 
}
function enableDisableRejectReason() {
	var rejectReason = '<c:out value="${itemRequestForm.rejectReason}" />';

	if(null == 	rejectReason || rejectReason == "") {
		document.getElementById("rejectArea").style.display = 'none';
	} else {
		document.getElementById("rejectArea").style.display = 'block';
	}
}
function setUserDepartment(chkbox) {
	var mode = '<c:out value="${mode}" />';
	var isChecked = '<c:out value="${itemRequestForm.userdepts}" />';
	if(chkbox.checked == true && mode == "add") {
		chkbox.value = "Y";
	} else if(mode == "edit" &&	isChecked == "Y") {
		document.getElementById("userdepts").checked = true;
		document.getElementById("userdepts").disabled=true;
	} else if(mode == "edit") {
		document.getElementById("userdepts").disabled=true;
	}
}
function verifyCancel()
{
var stay=confirm("Are you sure you want to cancel?");
if (stay)
window.location="../oma/styleskus.html";
else
 return false;
}
</SCRIPT>
<style type="text/css">
label.desc {
	font-weight: bold;
	margin-left: 7%;
	width: 13%;
}

.input {
	margin-left: 15%;
}

.datelabels {
	FONT-WEIGHT: bold;
	font-size:11px;	
	WIDTH: 100px;
	text-align: right;	
}
li.request_id,li.status,li.last_update {
	width: 200px !important;
	font-size: 13px;
	font: tahoma;
}

li.created_by,li.create_date{
	width: 200px !important;
}
li.updated_by {
	width: 250px !important;
} 
li.vendor_no,li.service_id,li.vendor_name,li.service_name {
	width: 180px !important;
}

LABEL.item_request {
	FONT-WEIGHT: bold;
}
LABEL.requestdetails {	
	FONT-WEIGHT: bold;
	font-size:11px;	
	text-align: right;
}
LABEL.requestsubelements {	
	FONT-WEIGHT: bold;
	font-size:11px;	
	text-align: left;
}
LABEL.item_request_file {	
	FONT-WEIGHT: bold;
	font-size:11px;	
	text-align: right;
}
.userdept {
	MARGIN-LEFT: 21%;
}
select.styleerror {
	border: solid 1px #ff0000 !important;
}
</style>
<head>
<title>Fulfillment Service Item Request</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>
<body class="admin">

<div style="height:20px">
<b>Fulfillment Service Item Request</b>
</div>

<c:url value="/oma/addeditItemRequest.html?mode=add" var="formAction"/>
<c:if test="${mode == 'add'}">
	<c:url value="/oma/addeditItemRequest.html?mode=add" var="formAction"/>
</c:if>
<c:if test="${mode == 'edit'}">
	<c:url value="/oma/addeditItemRequest.htmlmode=edit" var="formAction"/>
</c:if>

<form:form commandName="itemRequestForm" method="post" action="${formAction}" id="itemRequestForm" name="rqstForm" enctype="multipart/form-data" >
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		<fmt:message key="itemrequest.requestinfo"></fmt:message>
	</div>
	<div class="x-panel-body">
		<c:if test="${empty sessionScope.fulfillmentService.fulfillmentServiceID}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="Please select fulfillment service." escapeXml="false"/><br />
		</c:if>
		<c:if test="${empty sessionScope.vndrFulfillmentService.vndrFulfillmentServId}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="Please select vendor." escapeXml="false"/><br />
		</c:if>
		<c:if test="${itemRequestForm.error}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="There was a severe error. Please contact administrator." escapeXml="false"/><br />
		</c:if>
		
		<c:if test="${mode == 'add'}">
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;">
				<b><fmt:message key="stylesku.vendorno"/></b>
				<c:out value="${itemRequestForm.vendorNumber}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:20%;">
				<b><fmt:message key="stylesku.serviceid"/></b>
				<c:out value="${itemRequestForm.serviceId}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:22%;">
				<b>Date Last Created:</b>
				<c:out value="${itemRequestForm.createdDate}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>Date Last Updated:</b>
				<c:out value="${itemRequestForm.updatedDate}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;">
				<b><fmt:message key="stylesku.vendorname"/></b>
				<c:out value="${itemRequestForm.vendorName}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:20%;">
				<b><fmt:message key="stylesku.servicename"/></b>
				<c:out value="${itemRequestForm.serviceName}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:22%;">
				<b>Created By:</b>
				<c:out value="${itemRequestForm.createdBy}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>Updated By:</b>
				<c:out value="${itemRequestForm.updatedBy}"></c:out>
			</li>
		</ul>
		</c:if>
		<c:if test="${mode == 'edit'}">
		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;"><appfuse:label styleClass="item_request"
				key="itemrequest.requestid" /> <c:out
				value="${itemRequestForm.requestId}"></c:out></li>
			<li class = "txt_attr_name" style="width:20%;"><appfuse:label styleClass="item_request"
				key="itemrequest.createdby" /> <c:out
				value="${itemRequestForm.createdBy}"></c:out></li>
			<li class = "txt_attr_name" style="width:22%;"><appfuse:label styleClass="item_request"
				key="itemrequest.vendorno" /> <c:out
				value="${itemRequestForm.vendorNumber}"></c:out></li>
			<li class = "txt_attr_name" style="width:25%;"><appfuse:label styleClass="item_request"
				key="itemrequest.serviceid" /> <c:out
				value="${itemRequestForm.serviceId}"></c:out></li>
		</ul>
		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;"><appfuse:label styleClass="item_request"
				key="itemrequest.status" /> <c:out
				value="${itemRequestForm.status}"></c:out></li>
			<li class = "txt_attr_name" style="width:20%;"><appfuse:label styleClass="item_request"
				key="itemrequest.createddate" /> <c:out
				value="${itemRequestForm.createdDate}"></c:out></li>
			<li class = "txt_attr_name" style="width:22%;"><appfuse:label styleClass="item_request"
				key="itemrequest.vendorname" /> <c:out
				value="${itemRequestForm.vendorName}"></c:out></li>
			<li class = "txt_attr_name" style="width:25%;"><appfuse:label
				styleClass="item_request" key="itemrequest.servicename" /> <c:out
				value="${itemRequestForm.serviceName}"></c:out></li>
		</ul>
		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;"><appfuse:label styleClass="item_request"
				key="itemrequest.updatedate" /> <c:out
				value="${itemRequestForm.updatedDate}"></c:out></li>
			<li class = "txt_attr_name" style="width:23%;"><appfuse:label styleClass="item_request"
				key="itemrequest.updateby" /> <c:out
				value="${itemRequestForm.updatedBy}"></c:out></li>
		</ul>
	    </c:if>
	</div>
</div>
<div id="user_form" class="cars_panel x-hidden" style="margin-top:10px;">
	<c:choose>
	<c:when test="${mode == 'add'}">
	<div class="x-panel-header">
		<fmt:message key="itemrequest.headerlabel"/>
	</div>
	</c:when>
	<c:otherwise>
	<div class="x-panel-header">
	</div>
	</c:otherwise>
	</c:choose>
<div class="x-panel-body">
<spring:bind path="itemRequestForm.*">
    	<c:if test="${not empty status.errorMessages}">
        	<div class="error">
            	<c:forEach var="error" items="${status.errorMessages}">
                	<c:choose>
                    	<c:when test="${error == 'Saved Successfully!'}">
                        	<span style="background:#FFFF00;"> <c:out value="${error}" escapeXml="false"/> </span><br/>
                        </c:when>
                        <c:otherwise>
                            <img src="<c:url value="/images/iconWarning.gif"/>"
                            alt="<fmt:message key="icon.warning"/>" class="icon"/>
                            <c:out value="${error}" escapeXml="false"/><br/>
                        </c:otherwise>
            		</c:choose>       
               </c:forEach>
           </div>
       </c:if>
	</spring:bind>
	<c:if test="${sessionScope.showMsg=='yes'}">
	<span style="background: #FFFF00;"> <c:out
		value="Saved Successfully!!" escapeXml="false" /> </span>
	<br />
	<%session.setAttribute("showMsg","no"); %>
</c:if>
	<ol>
		<li>
			<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		</li>
	</ol>
		
		<table style="border-width: 0px;">
			<tr>
				<td width="20%" align="right" style="border-width: 0px;">
					<appfuse:label styleClass="datelabels" key="itemrequest.effectivedate" /><span style="color:#FF0000">*</span>
				</td>
				<td align="left" style="border-width: 0px;">
					<c:choose>
						<c:when test="${isposted == 'true' || itemRequestForm.status == 'Approved' || itemRequestForm.status == 'Reject'}">
							<form:input path="createDate" id="effectiveDateDisabled" maxlength="10" cssClass="text" cssErrorClass="text error"/>
						</c:when>
						<c:otherwise>
							<form:input path="createDate" id="effectiveDate" maxlength="10" cssClass="text" cssErrorClass="text error"/>
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.description" /><span style="color:#FF0000">*</span></td>
				<td align="left" style="border-width: 0px;"><form:input path="description" id="description" maxlength="50" cssClass="text" cssErrorClass="text error"/></td>
			</tr>

			<tr>
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.source" /><span style="color:#FF0000">*</span></td>
				<td align="left" style="border-width: 0px;"><c:set var="sourceItem" value="${sourceItem}" scope="session" />
            		<form:select id="sourceId" path="sourceId" disabled="true" cssClass="text">
            		<c:forEach items="${sourceItem}" var="itmsrc" >
            			<form:option value="${itmsrc.name}"><c:out value="${itmsrc.name}"/></form:option>
            		</c:forEach>
					</form:select>
					<c:if test="${mode == 'add'}">
						<form:hidden path="sourceId" id="sourceValue"/>
					</c:if>
				</td>
			</tr>
			
			<tr>
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.action" /><span style="color:#FF0000">*</span></td>			
            	<td align="left" style="border-width: 0px;"><form:select id="itemaction" path="itemAction" cssClass="text" onchange="disableBasedOnAction();">
					<form:option value ="Add/Edit Styles/SKUs"><fmt:message key="itemrequest.addaction"/></form:option>
					<form:option value ="Remove Styles/SKUs"><fmt:message key="itemrequest.removeaction"/></form:option>
					</form:select>
				</td>	
			</tr>
			<tr>
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.options" /></td>
				<td align="left" style="border-width: 0px;"><form:checkbox path="merchandise" id="merchandise" value="Y" onclick="enableDisableChecksale();" cssClass="text" /><fmt:message key="itemrequest.merchendise"></fmt:message></td>
			</tr>

			<tr id="checksale" style="display:none;">
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label
				styleClass="requestsubelements" key="itemrequest.markup" /><span
				style="color: #FF0000">*</span></td>
				<td align="left" style="border-width: 0px;"><form:input
				path="minimumMarkup" id="minimumMarkup" maxlength="50"
				cssClass="text" cssErrorClass="text error" /></td>
			</tr>

			<tr>
				<td width="20%" align="right" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.style" /><span style="color:#FF0000">*</span></td>
				<td align="left" style="border-width: 0px;">
				<c:set var="StylePopulationMethods" value="${StylePopulationMethods}" scope="session"></c:set>			
            	<form:select id="stylePopMethodId" path="stylePopMethodId"  
            	onchange="displayOnStyleSelection(this);" cssClass="text" cssErrorClass="select error" >
					<c:forEach items="${StylePopulationMethods}" var="smethod" >
						<form:option value="${smethod.name}"><c:out value="${smethod.name}"/></form:option>
					</c:forEach>
				</form:select>
				</td>
			</tr>
		
		<tr id="userdept" style="display:none;">
			<td width="20%" align="right" style="border-width: 0px;">
				<c:if test="${mode == 'add'}">
					<form:checkbox path="userdepts" id="checkuserdepts" cssClass="userdept" />
				</c:if>
				<c:if test="${mode == 'edit'}">
					<form:checkbox path="userdepts" id="checkuserdepts" cssClass="userdept" disabled="true"/>
				</c:if>
			</td>
			<td align="left" style="border-width: 0px;"><b><fmt:message key="itemrequest.userdept"></fmt:message></b></td>
		</tr>
		
		<tr id="file" style="display:none;">
			<td width="20%" align="right" style="border-width: 0px;">
				<appfuse:label styleClass="item_request_file" key="itemrequest.filename" /><span style="color:#FF0000">*</span>
			</td>
			<td align="left" style="border-width: 0px;">
				<c:if test="${mode == 'add'|| itemRequestForm.filePath == null }">
					<spring:bind path="itemRequestForm.file">
						<input type="file" name="file" id="file" class="file medium" onchange="uploadFile('<c:out value="${mode}" />');" />	
					</spring:bind>
				</c:if>
				<c:if test="${mode == 'edit'}">
					<c:out value="${itemRequestForm.filePath}"></c:out>
				</c:if>
			</td>
		</tr>

		<c:if test="${itemRequestForm.showTable == '1'}">
		<tr id="table">
			<td width="20%" align="right" style="border-width: 0px;">&nbsp;</td>
			<td align="left" style="border-width: 0px;" width="80%">
				<table width="50%" style="border:0px;">
					<tr>
						<td width="25%" style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
							<fmt:message key="itemrequest.fileuploaded"></fmt:message>
						</td>
						<td width="20%" style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
						<c:out value="${itemRequestForm.filePath}"></c:out>
						</td>
						<td style="border:0px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
							<fmt:message key="itemrequest.uploadedstyles"></fmt:message>
						</td>
						<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
							<c:out value="${itemRequestForm.uploadedStyles}"></c:out>
						</td>
						<td style="border:0px;">&nbsp;</td>
					</tr>
					<tr>
						<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
							<fmt:message key="itemrequest.failedstyles"></fmt:message>
						</td>
						<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
							<c:out value="${itemRequestForm.failedStyles}"></c:out>
						</td>
						<td style="border:0px;">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
		</c:if>
		
		<tr id="msg" style="display:none;">
			<td width="20%" align="right"  style="border-width: 0px;">&nbsp;</td>
			<td align="left" style="border-width: 0px;"><fmt:message key="itemrequest.filemessage"></fmt:message></td>
		</tr>

		<tr id="requestid" style="display:none;">
			<td width="20%" align="right"  valign="top" style="border-width: 0px;"><appfuse:label styleClass="requestsubelements" key="itemrequest.previousrequestid" /><span style="color:#FF0000">*</span></td>
			<td align="left" style="border-width: 0px;"><form:input path="locationName" id="locationName" maxlength="50" cssClass="text" cssErrorClass="text error" /></td>
		</tr>
		
		
		<tr id="rejectArea" style="display:none;">
			<td width="20%" align="right"  valign="top" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.rejectreasons" /><span style="color:#FF0000">*</span></td>
			<td align="left" style="border-width: 0px;"><form:textarea path="rejectReason" id="rejectReason" rows="5" cols="25" cssClass="text" cssErrorClass="textarea error"/></td>
		</tr>
		
		<tr id="exceptionReasons">
			<td width="20%" align="right" valign="top" style="border-width: 0px;"><appfuse:label styleClass="requestdetails" key="itemrequest.exreasons" /></td>
			<td align="left" style="border-width: 0px;"><form:textarea path="exceptionReason" id="exceptionReason" rows="5" cols="25" cssClass="text"/></td>
		</tr>
	</table>
	<br/><br/>
	
	<div style="margin-left:20.2%;">
		<c:choose>
		<c:when test="${itemRequestForm.role == 'readwriteapprove' || itemRequestForm.role == 'readwrite'}">
		<input type="button" value="Cancel" name="cancel" class="btn cancel_btn" onClick="verifyCancel();"/>
		<c:if test="${mode == 'add'}">
			<input type="button" value="Next" name="save" id="save" class="btn cancel_btn" onclick="setFileStatus();setStatus(this,'<c:out value="${mode}" />');" />
		</c:if>
		</c:when>
		<c:otherwise>
		<input type="button" value="Cancel" name="cancel" class="btn cancel_btn"  disabled="disabled" />
		<c:if test="${mode == 'add'}">
			<input type="button" value="Next" name="save"  disabled="disabled"  id="save" class="btn cancel_btn" />
		</c:if>
		</c:otherwise>
		</c:choose>
		
		<c:choose>
		<c:when test="${itemRequestForm.role == 'readwriteapprove' || itemRequestForm.role == 'readwrite'}">
		<c:if test="${mode == 'edit'}">
			<input type="button" value="Save" name="save" id="save" class="btn cancel_btn" onclick="setStatus(this,'<c:out value="${mode}" />');"/>
		</c:if>
		<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" onclick="setFileStatus();setStatus(this,'<c:out value="${mode}" />');"/>
		</c:when>
		<c:otherwise>
		<c:if test="${mode == 'edit'}">
			<input type="button" value="Save" name="save" id="save" class="btn cancel_btn" disabled="disabled" />
		</c:if>
		<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" disabled="disabled"/>
		</c:otherwise>
		</c:choose>
		
		<c:if test="${mode == 'edit'}">
			<input type="button" value="Request Styles >" name="styleScreen" class="btn cancel_btn" id="requeststyle" onclick="loadRequestStyle();"/>
		</c:if>
		
		<input type="button" value="Reject" name="reject" id="reject" class="btn cancel_btn" onclick="setFileStatus();rejectStatus();"/>
		<input type="button" value="Approve" name="approve" id="approve" class="btn cancel_btn" onclick="setFileStatus();setStatus(this,'<c:out value="${mode}" />');"/>
		
		<form:hidden path="showTable" id="showTable"/>
		<form:hidden path="filePath" id="filePath"/>
		<form:hidden path="uploadedStyles" id="uploadedStyles"/>
		<form:hidden path="failedStyles" id="failedStyles"/>
		<form:hidden path="validStyles" id="validStyles"/>
	</div>
</div>
</div>
</form:form>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	// datefields
	var CFG=belk.cars.config;
	new Ext.form.DateField({
		applyTo:'effectiveDate',
        fieldLabel:'Effective Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
});
</script>
]]>
</content>

