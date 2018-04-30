<%@include file="/common/taglibs.jsp" %>
<%@include file="headerItemRequest.jsp" %>
<%@include file="breadcrumbForUpdateHistory.jsp" %>

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
	document.getElementById("tab4").className="activeTab";
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

li.request_id,li.status,li.last_update {
	width: 200px !important;
	font-size: 11px;
	font: tahoma;
}

li.created_by,li.create_date{
	width: 200px !important;
}
li.updated_by {
	width: 200px !important;
} 
li.vendor_no,li.service_id,li.vendor_name,li.service_name {
	width: 180px !important;
}

LABEL.item_request {
	FONT-WEIGHT: bold;
}
.stylestext {
	width: 50px;
}
</style>
<head>
<title>Fulfillment Service Item Request Update History</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Fulfillment Service Item Request Update History</b>
</div>

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
		<ul class="car_info" style="font-size:11px !important;padding:0 0 10px !important;">
			<li class = "request_id">
				<appfuse:label styleClass="item_request" key="itemrequest.requestid" />
				<c:out value="${itemRequestForm.requestId}"></c:out>
			</li>
			<li class = "created_by">
				<appfuse:label styleClass="item_request" key="itemrequest.createdby" />
				<c:out value="${itemRequestForm.createdBy}"></c:out>
			</li>
			<li class="vendor_no">
				<appfuse:label styleClass="item_request" key="itemrequest.vendorno" />
				<c:out value="${itemRequestForm.vendorNumber}"></c:out>
			</li>
			<li class="service_id">
				<appfuse:label styleClass="item_request" key="itemrequest.serviceid" />
				<c:out value="${itemRequestForm.serviceId}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px !important;padding:0 0 10px !important;">
			<li class = "status">
				<appfuse:label styleClass="item_request" key="itemrequest.status" />
				<c:out value="${itemRequestForm.status}"></c:out>
			</li>
			<li class = "create_date">
				<appfuse:label styleClass="item_request" key="itemrequest.createddate" />
				<c:out value="${itemRequestForm.createdDate}"></c:out>				
			</li>
			<li class="vendor_name">
				<appfuse:label styleClass="item_request" key="itemrequest.vendorname" />
				<c:out value="${itemRequestForm.vendorName}"></c:out>
			</li>
			<li class="service_name">
				<appfuse:label styleClass="item_request" key="itemrequest.servicename" />
				<c:out value="${itemRequestForm.serviceName}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px !important;padding:0 0 10px !important;">
			<li class = "last_update">
				<appfuse:label styleClass="item_request" key="itemrequest.updatedate" />
				<c:out value="${itemRequestForm.updatedDate}"></c:out>
			</li>
			<li class = "updated_by">
				<appfuse:label styleClass="item_request" key="itemrequest.updateby" />
				<c:out value="${itemRequestForm.updatedBy}"></c:out>
			</li>
			<li class = "vendor_name">
				<appfuse:label styleClass="item_request" key="itemrequest.action" />
				<c:out value="${itemRequestForm.itemAction}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px !important;padding:0 0 10px !important;">
			<li class = "last_update">
				<appfuse:label styleClass="item_request" key="itemrequest.description" />
				<c:out value="${itemRequestForm.description}"></c:out>
			</li>
			<li class = "updated_by">
				<appfuse:label styleClass="item_request" key="itemrequest.effectivedate" />
				<c:out value="${itemRequestForm.createDate}"></c:out>
			</li>
			<li class = "vendor_name">
				<appfuse:label styleClass="item_request" key="itemrequest.source" />
				<c:out value="${itemRequestForm.sourceId}"></c:out>
			</li>
		</ul>
	</div>
</div>
<div id="user_form" class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Request Update History
	</div>
	<div class="x-panel-body">
	<display:table name="${itemRequestForm.updateDetails}" cellspacing="0" cellpadding="0" requestURI="/oma/requestUpdateHistory.html"  
		    id="history" class="dstable" sort="list" pagesize="25">
		    <display:column property="updatedDate" sortable="true" titleKey="Date" style="width: 10%;text-align: left" format="{0,date,MM/dd/yyyy hh:mm aaa}">
		    </display:column>
		    <display:column property="action" sortable="true" titleKey="Action" style="width: 10%;text-align: left"/>
		    <display:column property="rejectReason" sortable="true" titleKey="Reject Reason" style="width: 10%;text-align: left"/>
		    <display:column property="workflowStatus.name" sortable="true" titleKey="Status" style="width: 10%;text-align: left"/>
		    <display:column property="updatedBy" sortable="true" titleKey="Updated By" style="width: 10%;text-align: left"/>
	</display:table>
	</div>
</div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	
	            $('body.admin table.dstable tr').hover(function(){
              $('td',this).addClass('trHover');
              $(this).click(function(){
              $('td',this).removeClass('trHover');
              $('td',$(this).parent()).removeClass('trSelected');
              $('td',this).addClass('trSelected');
              });
            },function(){
              $('td',this).removeClass('trHover');
              });
});
</script>
]]>