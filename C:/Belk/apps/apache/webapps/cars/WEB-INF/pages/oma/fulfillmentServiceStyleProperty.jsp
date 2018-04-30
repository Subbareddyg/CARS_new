<%@include file="/common/taglibs.jsp" %>
<%@include file="headerTabsForVendor.jsp" %>
<%@include file="breadcrumbForStyleProperty.jsp" %>

<script type="text/javascript">
checked=false;
function checkedAll (frm1) {
	var aa= document.getElementById('stylesSkuForm');
	var val = "N";
	 if (checked == false)
          {
           checked = true
           val = "Y";
          }
        else
          {
          checked = false
          val = "N"
          }
	for (var i =0; i < aa.elements.length; i++) 
	{
		var elementName = aa.elements[i].name;
		if(elementName == "chk") {
	 		aa.elements[i].checked = checked;
	 		aa.elements[i].value = val;
	 		var counter = aa.elements[i].id;
	 		var idOfBox = "list[" + counter + "].styleSelected";
	 		document.getElementById(idOfBox).value = val;
		}
	}
}
function setStatusOfStyles(box , counter1) {
	var idAllSize = "list[" + counter1 + "].styleSelected";
	if(box.checked) {
		document.getElementById(idAllSize).value = "Y";
	} else {
		document.getElementById(idAllSize).value = "N";
	}	
}
function chkUserDept(box) {
	if(box.checked) {
		document.getElementById("userDeptsOnly").value = "Y";
	} else {
		document.getElementById("userDeptsOnly").value = "N";
	}		
}
function loadItemRequest() {
	document.stylesSkuForm.action="../oma/styleskus.html?mode=item";
	document.stylesSkuForm.submit();
}
</script>
<style type="text/css">
.styleskulabel1 {
	float: left;
	width: 38%;
	text-align: left;
	font-weight: bold;
}
.styleskulabel2 {
	float: left;
	width: 33%;
	text-align: left;
	font-weight: bold;
}
.styleskulabel3 {
	float: left;
	width: 60%;
	text-align: left;
	font-weight: bold;
}
.styleskutext1 {
	margin-left: 1%;
	width: 50%;
}
.styleskutext2 {
	margin-left: 1%;
	width: 61%;
}
.styleskutext3 {
	margin-left: 1%;
	width: 30%;
}
.styleskutext4 {
	margin-left: 1%;
	width: 35%;
}
.styleskutext5 {
	margin-left: 1%;
	width: 30%;
}
.stylestext {
	width: 30px;
}
</style>
<head>
<title>Fulfillment Service Style Details</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Style Property ${stylesSkuForm.styleId}</b>
</div>

<c:url value="/oma/styleskus.html?mode=search" var="formAction"/>
<form:form commandName="stylesSkuForm" method="post" action="${formAction}" id="stylesSkuForm" name="stylesSkuForm" enctype="multipart/form-data" >
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
		<c:if test="${stylesSkuForm.error}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="There was a severe error. Please contact administrator." escapeXml="false"/><br />
		</c:if>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:30%;">
				<b><fmt:message key="stylesku.vendorno"/></b>
				<c:out value="${stylesSkuForm.vendorNo}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b><fmt:message key="stylesku.serviceid"/></b>
				<c:out value="${stylesSkuForm.serviceId}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>IDB Drop Ship Status:</b>
				<c:out value="${stylesSkuForm.idbStatus}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:30%;">
				<b><fmt:message key="stylesku.vendorname"/></b>
				<c:out value="${stylesSkuForm.vendorName}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b><fmt:message key="stylesku.servicename"/></b>
				<c:out value="${stylesSkuForm.serviceName}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>Date Last IDB Update:</b>
				<c:out value="${stylesSkuForm.dateLastIDBUpdate}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:30%;">
				<b>Style #:</b>
				<c:out value="${stylesSkuForm.styleId}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b>Style Status:</b>
				<c:out value="${stylesSkuForm.dropShipStatus}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>All Sku:</b>
				<c:out value="${stylesSkuForm.allSku}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:30%;">
				<b>Description:</b>
				<c:out value="${stylesSkuForm.styleDesc}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b>Date Last Request:</b>
				<c:out value="${stylesSkuForm.dateLastRequest}"></c:out>
			</li>
			<li class = "txt_attr_name" style="width:25%;">
				<b>All Color:</b>
				<c:out value="${stylesSkuForm.allColor}"></c:out>
			</li>
		</ul>
		<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:30%;">
			</li>
			<li class = "txt_attr_name" style="width:30%;">
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b>All Size:</b>
				<c:out value="${stylesSkuForm.allSize}"></c:out>
			</li>
		</ul>
	</div>
</div>
<br />

<div id="user_form" class="cars_panel x-hidden">
	<div class="x-panel-header">
		SKU/UPC's
	</div>
	<div class="x-panel-body">
	<c:set var="counter" value="-1"></c:set>
	<c:set var="counter1" value="-1" scope="session"></c:set>
	<div>
	<display:table name="${stylesSkuForm.list}" cellspacing="0" cellpadding="0" requestURI="/oma/styleproperty.html?pagination=pagination"  
		    id="history" class="dstable" sort="list" pagesize="25" uid="abc">
		    <c:set var="counter" value="${counter+1}"></c:set>
		    
		    <c:choose>
		    <c:when test="${stylesSkuForm.list[counter].approvedRequest == 'Y'}" >
		    <display:column sortable="true" titleKey="Vendor UPC" style="width: 10%;text-align: left">
			<secureurl:secureAnchor name="VendorUpc" cssStyle="removeVendor"
				title="${stylesSkuForm.list[counter].vendorUpc}"
				elementName="VendorUpc" localized="true"
				hideUnaccessibleLinks="true"
				arguments="${stylesSkuForm.styleId},${stylesSkuForm.styleDesc},${stylesSkuForm.dropShipStatus},${stylesSkuForm.idbStatus},${stylesSkuForm.list[counter].vendorUpc}" />
			</display:column>
			</c:when>
			<c:otherwise>
				<display:column sortable="true" titleKey="Vendor UPC" style="width: 10%;text-align: left" property="vendorUpc"/>
			</c:otherwise>
			</c:choose>
		    <display:column property="belkUpc" sortable="true" titleKey="Belk UPC" style="width: 10%;text-align: left"/>
		    <display:column property="styleDesc" sortable="true" titleKey="Description" style="width: 10%;text-align: left"/>
		    <display:column property="dropShipStatus" sortable="true" titleKey="Status" style="width: 10%;text-align: left"/>
		    <display:column property="idbAllowDropship" sortable="true" titleKey="IDB Drop Ship" style="width: 10%;text-align: center"/>
		    <display:column property="source" sortable="true" titleKey="Last Request" style="width: 10%;text-align: left"/>
		    <display:column property="updatedDate" sortable="true" titleKey="Date Last Update" style="width: 10%;text-align: left"/>
		    <display:column property="updatedBy" sortable="true" titleKey="Last Updated By" style="width: 10%;text-align: left"/>
		    <display:setProperty name="paging.banner.item_name" value="upc"/>
    		<display:setProperty name="paging.banner.items_name" value="upcs"/>
	</display:table>
	
	
	<c:if test="${not empty stylesSkuForm.requestDetails}">
	<br>
	<label><b>Request History</b></label>
	<br>
	<br>
	
		<display:table name="${stylesSkuForm.requestDetails}" cellspacing="0" cellpadding="0" requestURI="/oma/styleproperty.html?pagination=pagination"  
		    id="history" class="dstable" sort="list" pagesize="25" uid="xyz">
		    <c:set var="counter1" value="${counter1+1}"></c:set>
		    <display:column sortable="true" titleKey="ID" style="width: 10%;text-align: right" sortProperty="requestId">
				<secureurl:secureAnchor name="requestId" cssStyle="removeVendor"
					title="${stylesSkuForm.requestDetails[counter1].requestId}"
					elementName="requestId" localized="true"
					hideUnaccessibleLinks="true"
					arguments="${stylesSkuForm.requestDetails[counter1].requestId}" />
			</display:column>
		    <display:column property="requestDesc" sortable="true" titleKey="Description" style="width: 10%;text-align: left"/>
		    <display:column property="effectiveDate" sortable="true" titleKey="Effective Date" style="width: 10%;text-align: left"/>
		    <display:column property="action" sortable="true" titleKey="Action" style="width: 10%;text-align: left"/>
		    <display:column property="source" sortable="true" titleKey="Source" style="width: 10%;text-align: left"/>
		    <display:column property="status" sortable="true" titleKey="Status" style="width: 10%;text-align: left"/>
		    <display:column property="unitCost" sortable="true" titleKey="Unit Cost" style="width: 10%;text-align: right"/>
		    <display:column property="unitFee" sortable="true" titleKey="Unit Handling Fee" style="width: 10%;text-align: right"/>
		    <display:column property="override" sortable="true" titleKey="Override" style="width: 10%;text-align: center"/>
		    <display:column property="updatedBy" sortable="true" titleKey="Approved By" style="width: 10%;text-align: left"/>
		    <display:setProperty name="paging.banner.item_name" value="request"/>
    		<display:setProperty name="paging.banner.items_name" value="requests"/>
	</display:table>
	
	</c:if>
	</div>
	</div>
</div>
</form:form>
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
</content>