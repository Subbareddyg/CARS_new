<%@include file="/common/taglibs.jsp" %>
<%@include file="headerTabsForVendor.jsp" %>
<%@include file="breadcrumbForRequestHistory.jsp" %>

<script type="text/javascript">

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
	 document.getElementById("tab8").className="activeTab";
	 setTimeout(function() { document.getElementById('requestId').focus(); }, 10);
}
function loadItemRequest() {
	window.location="../oma/addeditItemRequest.html?mode=add";
}
</script>
<style type="text/css">
.car_info {
	padding: 0 0 10px 0 !important;
	font-size: 11px !important;
	zoom: 1;
	border:none;
}

.styleskulabel1 {
	float: left;
	text-align: left;
	font-size: 11px !important;
	font-weight: bold;
}
.styleskulabel2 {
	float: left;
	text-align: left;
	font-size: 11px !important;
	font-weight: bold;
}
.styleskulabel3 {
	float: left;
	text-align: left;
	font-size: 11px !important;
	font-weight: bold;
}
.styleskutext1 {
	margin-left: 1%;
	font-size: 11px !important;

}
.styleskutext2 {
	margin-left: 1%;
	font-size: 11px !important;
}
.styleskutext3 {
	margin-left: 1%;
	font-size: 11px !important;
}
.styleskutext4 {
	margin-left: 1%;
	font-size: 11px !important;
}
.styleskutext5 {
	margin-left: 1%;
	font-size: 11px !important;
}
.stylestext {
	width: 30px;
}
</style>
<head>
<title>Request History</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Request History</b>
</div>

<c:url value="/oma/requestHistory.html?mode=search" var="formAction"/>
<form:form commandName="requestHistoryForm" method="post" action="${formAction}" id="requestHistoryForm" name="requestHistoryForm" enctype="multipart/form-data" >
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Vendor Info
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
		<c:if test="${requestHistoryForm.error}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="There was a severe error. Please contact administrator." escapeXml="false"/><br />
		</c:if>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:20%;">
				<b><fmt:message key="stylesku.vendorno"/></b>
				<c:out value="${requestHistoryForm.vendorNo}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:17%;">
				<b><fmt:message key="stylesku.serviceid"/></b>
				<c:out value="${requestHistoryForm.serviceId}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:26%;">
				<b>Date Last Created:</b>
				<c:out value="${requestHistoryForm.dateCreated}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>Date Last Updated:</b>
				<c:out value="${requestHistoryForm.updatedDate}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li  style="width:20%;">
				<b><fmt:message key="stylesku.vendorname"/></b>
				<c:out value="${requestHistoryForm.vendorName}"></c:out>
			</li>
			<li  style="width:17%;">
				<b><fmt:message key="stylesku.servicename"/></b>
				<c:out value="${requestHistoryForm.serviceName}"></c:out>
			</li>
			<li  style="width:26%;">
				<b>Created By:</b>
				<c:out value="${requestHistoryForm.createdBy}"></c:out>
			</li>
			<li  style="width:25%;">
				<b>Updated By:</b>
				<c:out value="${requestHistoryForm.updatedBy}"></c:out>
			</li>
		</ul>
	</div>
</div>

<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Request Search
	</div>
	<div class="x-panel-body">
	<spring:bind path="requestHistoryForm.*">
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
		<table class="car_info" cellspacing="0" cellpadding="0" style="border:0px;">
		<tr>
			<td style="border:0px;"><label class="styleskulabel1">ID:</label></td>
			<td style="border:0px;"><form:input path="requestId" id="requestId" cssClass="styleskutext1" cssErrorClass="text error" maxlength="12"/></td>
			
			<td style="border:0px;"><label class="styleskulabel2">Description:</label></td>
			<td style="border:0px;"><form:input path="requestDesc" id="requestDesc" cssClass="styleskutext2" maxlength="200"/></td>
			
			<td style="border:0px;"><label class="styleskulabel3">Eff. Date(Start):</label></td>
			<td style="border:0px;"><form:input path="effectiveStartDate" id="effectiveStartDate" cssClass="styleskutext5" cssErrorClass="text error"/></td>
			
			<td style="border:0px;"><input type="submit" value="Search" name="search" id="search" class="btn cancel_btn" />
				<c:url value="/oma/requestHistory.html" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a></td>
		</tr>
		
		<tr>
			<td style="border:0px;"><label class="styleskulabel1">Vendor Style #:</label></td>
			<td style="border:0px;"><form:input path="styleId" id="styleId" cssClass="styleskutext1" maxlength="20"/></td>
			
			<td style="border:0px;"><label class="styleskulabel2">Style Description:</label></td>
			<td style="border:0px;"><form:input path="styleDesc" id="styleDesc" cssClass="styleskutext2" maxlength="200"/></td>
			
			<td style="border:0px;"><label class="styleskulabel3">Eff. Date(End):</label></td>
			<td style="border:0px;"><form:input path="effectiveEndDate" id="effectiveEndDate" cssClass="styleskutext5" cssErrorClass="text error"/></td>
			
			<td style="border:0px;">&nbsp;</td>
		</tr>
		<tr>
			<td style="border:0px;"><label class="styleskulabel1">Vendor UPC:</label></td>
			<td style="border:0px;"><form:input path="vendorUpc" id="vendorUpc" cssClass="styleskutext1" maxlength="50"/></td>
			
			<td style="border:0px;"><label class="styleskulabel2">Belk UPC:</label></td>
			<td style="border:0px;"><form:input path="belkUpc" id="belkUpc" cssClass="styleskutext3" maxlength="50"/></td>
			
		<td style="border:0px;"><label class="styleskulabel2">Status:</label></td>
			<td style="border:0px;">&nbsp;<form:select path="statusFilter"  >
			<form:option  value="ALL">All</form:option>
			<form:option   value="APPROVED">Approved</form:option>
			<form:option  value="UNAPPROVED">Unapproved</form:option>
			<form:option value="REJECTED">Rejected</form:option>
			</form:select>
			
			<td style="border:0px;">&nbsp;</td>
		</tr>
	</table>
	</div>
</div>

<div id="user_form" class="cars_panel x-hidden" style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Requests
	</div>
	<div class="x-panel-body">
	
	<div class="userButtons">
		<div class="buttons" style="float:right;">
		
			<c:choose>
				<c:when test="${requestHistoryForm.role == 'readwrite'}">
				<input type="button" value="New Item Request" name="itemrequest" class="btn cancel_btn" onclick="loadItemRequest();"/>
				</c:when>
				<c:otherwise>
				<input type="button" onclick="none" disabled="disabled" value="New Item Request"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="btn_save" class="btn cancel_btn">
				</c:otherwise>
				</c:choose>
		</div>
	</div>
				
	<c:set var="counter" value="-1"></c:set>
	<div style="height: 200px;">
	
	<display:table name="${requestHistoryForm.requestDetails}" cellspacing="0" cellpadding="0" requestURI="/oma/requestHistory.html?pagination=true"  
		    id="history" class="dstable" sort="list" pagesize="25">
		    <c:set var="counter" value="${counter+1}"></c:set>
		    <display:column sortable="true" titleKey="ID" style="width: 10%;text-align: right" sortProperty="requestId">
		    	<secureurl:secureAnchor name="requestId" cssStyle="removeVendor" title="${requestHistoryForm.requestDetails[counter].requestId}" elementName="requestId"  
			 	localized="true"  hideUnaccessibleLinks="true" arguments="${requestHistoryForm.requestDetails[counter].requestId}"/>
		    </display:column>
		    <display:column property="requestDesc" sortable="true" titleKey="Description" style="width: 10%;text-align: left"/>
		    <display:column property="effectiveDate" sortable="true" titleKey="Effective Date" style="width: 10%;text-align: left"/>
		    <display:column property="action" sortable="true" titleKey="Action" style="width: 10%;text-align: left"/>
		    <display:column property="source" sortable="true" titleKey="Source" style="width: 10%;text-align: left"/>
		    <display:column property="status" sortable="true" titleKey="Status" style="width: 10%;text-align: left"/>
		    <display:column property="createdBy" sortable="true" titleKey="Created By" style="width: 10%;text-align: left"/>
		    <display:column property="createDate" sortable="true" titleKey="Date Created" style="width: 10%;text-align: left"/>
		    <c:if test="${requestHistoryForm.statusFilter == 'UNAPPROVED' || requestHistoryForm.statusFilter == 'ALL'}">
		    <display:column property="updatedBy" sortable="true" titleKey="Updated By" style="width: 10%;text-align: left"/>
		    <display:column property="updatedDate" sortable="true" titleKey="Date Updated" style="width: 10%;text-align: left"/>
		    </c:if>
		     <c:if test="${requestHistoryForm.statusFilter == 'APPROVED'}">
		     <display:column property="updatedBy" sortable="true" titleKey="Approved By" style="width: 10%;text-align: left"/>
		    <display:column property="updatedDate" sortable="true" titleKey="Date Approved" style="width: 10%;text-align: left"/>
		     </c:if>
		     <c:if test="${requestHistoryForm.statusFilter == 'REJECTED' }">
		     <display:column property="updatedBy" sortable="true" titleKey="Rejected By" style="width: 10%;text-align: left"/>
		    <display:column property="updatedDate" sortable="true" titleKey="Date Rejected" style="width: 10%;text-align: left"/>
		     </c:if>
		    <display:setProperty name="paging.banner.item_name" value="request"/>
    		<display:setProperty name="paging.banner.items_name" value="requests"/>
    		
	</display:table>
	</div>
	</div>
</div>
</form:form>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
 var CFG=belk.cars.config;
	new Ext.form.DateField({
		applyTo:'effectiveStartDate',
        fieldLabel:'Eff. Date(Start):',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });	
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
              
    
	new Ext.form.DateField({
		applyTo:'effectiveEndDate',
        fieldLabel:'Effective Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
});
</script>
]]>
</content>