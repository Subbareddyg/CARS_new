<%@include file="/common/taglibs.jsp" %>
<%@include file="headerItemRequest.jsp" %>
<%@include file="breadcrumbForRequestStyle.jsp" %>
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
	document.getElementById("tab2").className="activeTab";
	setReadonly();
	disableBasedOnRole();
}
function disableBasedOnRole() {
	<c:if test="${itemRequestForm.role == 'read'}">
			for(i=0; i<document.itemRequestForm.elements.length; i++){
				if(document.itemRequestForm.elements[i].name!="exceptionScreen" && document.itemRequestForm.elements[i].name!="requestScreen" ){
					document.itemRequestForm.elements[i].disabled=true; 
				}
			}
	</c:if>
	var approveRole = '<c:out value="${itemRequestForm.approveRole}"></c:out>';
	if(approveRole != 'readwriteapprove') {
		document.getElementById('reject').disabled = true;
		document.getElementById('approve').disabled = true;
	}
	var enableDisableSkuException = '<c:out value="${itemRequestForm.enableDisableSkuException}"></c:out>';
	if(enableDisableSkuException == 'false') {
		document.getElementById('se').disabled = false;
	}
}
function loadItemRequest() {
	window.location="../oma/addeditItemRequest.html?mode=edit";	
}
function loadSkuException() {
	window.location="../oma/requestskuexception.html";	
}
function setReadonly() {
	var isPosted = '<c:out value="${isposted}"></c:out>';
	var isApproveReject = '<c:out value="${isapprovereject}"></c:out>';
	if(isPosted == "true" || isApproveReject == "true") {
		var aa= document.getElementById('itemRequestForm');

		for (var i =0; i < aa.elements.length; i++) 
		{
		 	aa.elements[i].readOnly = true;
		 	aa.elements[i].disabled = true;
		}
		document.getElementById('btn_add_note').disabled = true;
		document.getElementById('rp').disabled = false;
		document.getElementById('cancel').disabled = false;

		if(isPosted == "true" && isApproveReject != "true") {
		document.getElementById('reject').disabled = false;
		document.getElementById('approve').disabled = false;
		}
	}
}
function setStatusOfAllSku(box , counter1) {
	var idAllSku = "styleDetails[" + counter1 + "].allSkuIndicator";
	var idAllColor = "styleDetails[" + counter1 + "].allColorIndicator";
	var idAllSize = "styleDetails[" + counter1 + "].allSizeIndicator";
	var idColor = "allColor" + counter1;
	var idSize = "allSize" + counter1;
	
	if(box.checked) {
		document.getElementById(idAllSku).value = "Y";
		document.getElementById(idAllSize).value = "Y";
		document.getElementById(idAllColor).value = "Y";
		document.getElementById(idColor).checked=true;
		document.getElementById(idSize).checked=true;
		document.getElementById(idColor).disabled=true;
		document.getElementById(idSize).disabled=true;
	} else {
		document.getElementById(idAllSku).value = "N";
		document.getElementById(idColor).disabled=false;
		document.getElementById(idSize).disabled=false;
	}	
}
function setStatusOfAllSize(box , counter1) {
	var idAllSize = "styleDetails[" + counter1 + "].allSizeIndicator";
	if(box.checked) {
		document.getElementById(idAllSize).value = "Y";
	} else {
		document.getElementById(idAllSize).value = "N";
	}	
}
function setStatusOfAllColor(box , counter1) {
	var idAllColor = "styleDetails[" + counter1 + "].allColorIndicator";
	if(box.checked) {
		document.getElementById(idAllColor).value = "Y";
	} else {
		document.getElementById(idAllColor).value = "N";
	}	
}
function setStatusOfButton(sbutton){
	var buttonClicked = sbutton.value;

	if(buttonClicked == "Save & Post") {
		buttonClicked = "post";
	}
			
	document.itemRequestForm.action="../oma/itemrequeststyles.html?mode=save&statusofrequest=" + buttonClicked;
	document.itemRequestForm.submit();
}
function removeAction(counter1) {
	alert(counter1);
	var removeId = "styleDetails[" + counter1 + "].vfirStyleId";
	var removeUrl = "../oma/itemrequeststyles.html?mode=remove&id=" + document.getElementById(removeId).value;
	document.itemRequestForm.action=removeUrl;
	document.itemRequestForm.submit();
}
function verifyCancel()
{
var stay=confirm("Are you sure you want to cancel?");
if (stay)
window.location="../oma/styleskus.html";
else
 return false;
}
function verifyRemove(styleId,requestId)
{
	var stay=confirm("Are you sure you want to remove?");
	if (stay)
		window.location="../oma/itemrequeststyles.html?mode=remove&styleid=" + styleId + "&requestid=" + requestId;
	else
		return false;	
}

function checkEnteredNumber(numberField,pasteEvent){
    var decallowed = 2;  // how many decimals are allowed?
    var dectext = 0;
    var fieldValue = "";
                var textvalue = numberField.value;;
    if(pasteEvent) {
      fieldValue = window.clipboardData.getData('Text');
    }
    if (isNaN(fieldValue) ) {
          document.getElementById(numberField.id).value=textvalue;
                                  fieldValue ="";
          window.event.returnValue = 0;
    }
    else if (isNaN(textvalue) ) {
    	//Fixes by Swapnil
    	dectext = textvalue.substring(textvalue.indexOf('.')+1, textvalue.length);
		if(textvalue.indexOf('.') == 0 && dectext.indexOf('.') == -1){
			return true;
		}
          numberField.value=textvalue.substring(0, (textvalue.length-1));
                } else if(!isNaN(textvalue)){
                                textvalue=textvalue+fieldValue;
                }
    if (textvalue.indexOf('.') == -1){
          if(textvalue.length <= 10){
                return true;
          }else{
                document.getElementById(numberField.id).value = textvalue.substring(0, (textvalue.length-1));
          }
    }else {
          dectext = textvalue.substring(textvalue.indexOf('.')+1, textvalue.length);
          if (dectext.length > (decallowed)){
                numberField.value=textvalue.substring(0, (textvalue.length-1));
                numberField.focus();
          }
          else {
                if(textvalue.substring(0,textvalue.indexOf('.')-1).length < 10){
                         return true;
               }else{
                         return false;
               }
          }
    }
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
<title>Fulfillment Service Item Request Styles</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Fulfillment Service Item Request Styles</b>
</div>
<%session.removeAttribute("showMsg"); %>
<form:form commandName="itemRequestForm" method="post" action="../oma/itemrequeststyles.html?mode=save" id="itemRequestForm" name="itemRequestForm">
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
		Style Listing
	</div>
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
	<c:forEach var="styleErrors" items="${AddStylesErrors}">
		<img src="<c:url value="/images/iconWarning.gif"/>"
                            alt="<fmt:message key="icon.warning"/>" class="icon"/>
                            <c:out value="${styleErrors}" escapeXml="false"/><br/>
	</c:forEach>
	<c:set var="counter" value="-1"></c:set>
	<c:set var="valOfAllSku" value="N"></c:set>
	<div style="overflow-x: hidden; width: 100%;">
	<display:table name="${itemRequestForm.styleDetails}" cellspacing="0" cellpadding="0" requestURI="/oma/itemrequeststyles.html?pagination=true"  
		    id="styles" class="dstable" sort="list" pagesize="25" > <!-- Here we have taken out attribute defaultPage="${itemRequestForm.pageNo}", since displaytag1.2 library is not supporting defaultPage attribute-->
		    
		    <display:setProperty name="paging.banner.items_name" value="Styles" />
			<display:setProperty name="paging.banner.item_name" value="Style" />
			
		    <c:set var="counter" value="${counter+1}"></c:set>
		    <display:column property="compositeKeyVIFRStyle.vendorStyleId" sortable="true" titleKey="Vendor Style #" style="width: 10%;text-align: left"/>
		    <display:column property="styleDescription" sortable="true" titleKey="Style Description" style="width: 15%;text-align: left"/>
		    <display:column  sortable="false" titleKey="All Sku" style="width: 5%">
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].allSkuIndicator">
		    		<c:set var="valOfAllSku" value="${status.value}"></c:set>
		    		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
		    		id="<c:out value="${status.expression}"/>" />
		    	</spring:bind>
		    	<c:if test="${valOfAllSku == 'Y'}">
		    		<input type="checkbox" checked="checked" name="<c:out value="allSku${counter}"/>"  
		    		id="<c:out value="allSku${status.expression}"/>" onclick="setStatusOfAllSku(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllSku == 'N'}">
		    		<input type="checkbox" name="<c:out value="allSku${counter}"/>"  
		    		id="<c:out value="allSku${status.expression}"/>" onclick="setStatusOfAllSku(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    </display:column>
		    <display:column  sortable="false" titleKey="Size Exception" style="width: 5%">
		    	<c:set var="valOfAllSize" value="${itemRequestForm.styleDetails[counter].allSizeIndicator}"></c:set>
		    	<c:if test="${valOfAllSize == 'Y' && valOfAllSku == 'Y'}">
		    		<input type="checkbox" checked="checked" name="allSize${counter}"  id="allSize${counter}" disabled="disabled"
		    		onclick="setStatusOfAllSize(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllSize == 'Y' && valOfAllSku == 'N'}">
		    		<input type="checkbox" checked="checked" name="allSize${counter}"  id="allSize${counter}" 
		    		onclick="setStatusOfAllSize(this, '<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllSize == 'N' && valOfAllSku == 'Y'}">
		    		<input type="checkbox" name="allSize${counter}" id="allSize${counter}" disabled="disabled"
		    		onclick="setStatusOfAllSize(this, '<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllSize == 'N' && valOfAllSku == 'N'}">
		    		<input type="checkbox" name="allSize${counter}" id="allSize${counter}" 
		    		onclick="setStatusOfAllSize(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].allSizeIndicator">
		    		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
		    		id="<c:out value="${status.expression}"/>" />	
				</spring:bind>	
		    </display:column>
		    <display:column  sortable="false" titleKey="Color Exception" style="width: 5%">
		    	<c:set var="valOfAllColor" value="${itemRequestForm.styleDetails[counter].allColorIndicator}"></c:set>
		    	<c:if test="${valOfAllColor == 'Y' && valOfAllSku == 'Y'}">
		    		<input type="checkbox" checked="checked" name="allColor${counter}"  id="allColor${counter}" disabled="disabled"
		    		onclick="setStatusOfAllColor(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllColor == 'Y' && valOfAllSku == 'N'}">
		    		<input type="checkbox" checked="checked" name="allColor${counter}"  id="allColor${counter}" 
		    		onclick="setStatusOfAllColor(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllColor == 'N' && valOfAllSku == 'Y'}">
		    		<input type="checkbox" name="allColor${counter}" id="allColor${counter}" disabled="disabled"
		    		onclick="setStatusOfAllColor(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfAllColor == 'N' && valOfAllSku == 'N'}">
		    		<input type="checkbox" name="allColor${counter}" id="allColor${counter}" 
		    		onclick="setStatusOfAllColor(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].allColorIndicator">
		    		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
		    		id="<c:out value="${status.expression}"/>" />
		    	</spring:bind>
		    </display:column>
		    <display:column property="unitCost" sortable="false" titleKey="IDB Unit Cost" style="width: 10%;text-align: right" format="{0,number,currency}"/>
		    <display:column property="unitHandlingFee" sortable="false" titleKey="Vendor Unit Handling Fee" style="width: 10%;text-align: right" format="{0,number,currency}"/>
		    <display:column  sortable="false" titleKey="Override Unit Cost" style="width: 10%" >
		    	<c:out value="$"></c:out>
		    	<c:choose>
		    	<c:when test="${itemRequestForm.itemAction == 'Remove Styles/SKUs'}">
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].overrideUnitCost">
		    		<input type="text" name="<c:out value="${status.expression}"/>"
    					id="<c:out value="${status.expression}"/>" value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext" 
    					disabled="disabled"/>
		    	</spring:bind>
		    	</c:when>
		    	<c:otherwise>
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].overrideUnitCost">
		    		<input type="text" name="<c:out value="${status.expression}"/>"
    					id="<c:out value="${status.expression}"/>" value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext" 
    					onkeyup="checkEnteredNumber(this,false);" onkeypress="checkEnteredNumber(this,false);" onpaste="checkEnteredNumber(this,true);"/>
		    	</spring:bind>
		    	</c:otherwise>
		    	</c:choose>
		    </display:column>
		    <display:column  sortable="false" titleKey="Override Unit Handling Fee" style="width: 10%" >
		    	<c:out value="$"></c:out>
		    	<c:choose>
		    	<c:when test="${itemRequestForm.itemAction == 'Remove Styles/SKUs'}">
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].overrideFee">
		    		<input type="text" name="<c:out value="${status.expression}"/>"
    					id="<c:out value="${status.expression}"/>" value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext" 
    					disabled="disabled"/>
		    	</spring:bind>
		    	</c:when>
		    	<c:otherwise>
		    	<spring:bind path="itemRequestForm.styleDetails[${counter}].overrideFee">
		    		<input type="text" name="<c:out value="${status.expression}"/>"
    					id="<c:out value="${status.expression}"/>" value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext" 
    					onkeyup="checkEnteredNumber(this,false);" onkeypress="checkEnteredNumber(this,false);" onpaste="checkEnteredNumber(this,true);"/>
		    	</spring:bind>
		    	</c:otherwise>
		    	</c:choose>
		    </display:column>
		    <display:column  sortable="false" style="width: 5%">
		    	<c:choose>
		    	<c:when test="${itemRequestForm.role == 'readwrite'}">
		    	<input type="button" value="Remove" name="remove" class="btn" onclick="verifyRemove('<c:out value="${itemRequestForm.styleDetails[counter].compositeKeyVIFRStyle.vendorStyleId}" />','<c:out value="${itemRequestForm.requestId}" />');"/>
		    	</c:when>
		    	<c:otherwise>
		    	<input type="button" value="Remove" name="remove" class="btn" disabled="disabled"/>
		    	</c:otherwise>
		    	</c:choose>
		    </display:column>
	</display:table>
	</div>
	<ul>
		<li style="margin-top: 30px!important">
			<c:choose>
			<c:when test="${itemRequestForm.role == 'readwrite'}">
			<secureurl:secureAnchor cssStyle="btn" name="AddStyle"
			localized="true" title="Add Style" hideUnaccessibleLinks="false"
			arguments="${itemRequestForm.requestId},add"
			elementName="btn_add_note" />
			</c:when>
			<c:otherwise>
			<input type="button" value="Add Style" name="AddStyle" class="btn" disabled="disabled" id="btn_add_note"/>
			</c:otherwise>
			</c:choose>
			<input type="button" value="Cancel" name="cancel" class="btn cancel_btn"  id = "cancel" onclick="verifyCancel();"/>
			<c:choose>
			<c:when test="${itemRequestForm.role == 'readwrite'}">
			<input type="button" value="Save" name="save" id="save" class="btn cancel_btn"  onclick="setStatusOfButton(this);"/>
			<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" onclick="setStatusOfButton(this);"/>
			</c:when>
			<c:otherwise>
			<input type="button" value="Save" name="save" id="save" class="btn cancel_btn" disabled="disabled"/>
			<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" disabled="disabled"/>
			</c:otherwise>
			</c:choose>
			<c:choose>
			<c:when test="${itemRequestForm.approveRole == 'readwriteapprove'}">
			<input type="button" value="Reject" name="reject" id="reject" class="btn cancel_btn" onclick="setStatusOfButton(this);"/>
			<input type="button" value="Approve" name="approve" id="approve" class="btn cancel_btn" onclick="setStatusOfButton(this);"/>
			</c:when>
			<c:otherwise>
			<input type="button" value="Reject" name="reject" id="reject" class="btn cancel_btn" disabled="disabled"/>
			<input type="button" value="Approve" name="approve" id="approve" class="btn cancel_btn" disabled="disabled"/>
			</c:otherwise>
			</c:choose>
			<input type="button" value="&lt; Request Properties" id="rp" name="requestScreen" style="width: 18%;" class="btn cancel_btn" onclick="loadItemRequest();"/>
			
			<c:choose>
			<c:when test="${itemRequestForm.enableDisableSkuException == false}">
			<input type="button" value="SKU Exception >" name="exceptionScreen" id="se" class="btn cancel_btn" onclick="loadSkuException();"/>
			</c:when>
			<c:otherwise>
			<input type="button" value="SKU Exception >" name="exceptionScreen" id="se" class="btn cancel_btn" disabled="disabled" readonly="readonly"/>
			</c:otherwise>
			</c:choose>
			<form:hidden path="userdepts" id="userdepts"/>
			<form:hidden path="locationName" id="locationName"/>
			<form:hidden path="filePath" id="filePath"/>
		</li>
	</ul>
	</div>
</div>
</form:form>

<div id="add_user_note_win" class="x-hidden">
<div class="x-window-header">Add Style</div>
<div id="add_user_note_form" class="content">
<c:url value="../oma/addrequeststyles.html" var="formAction"/>
<form method="post" action="${formAction}" id="itemRequestStylesForm" name="itemRequestStylesForm">
	<ul>
		<li>
			<label id="lstyle1">Vendor Style #</label>
			<input type="text" name="style1" id="style1" />
		</li>
		<li>
			<label id="lstyle2">Vendor Style #</label>
			<input type="text" name="style2" id="style2" />
		</li>
		<li>
			<label id="lstyle3">Vendor Style #</label>
			<input type="text" name="style3" id="style3" />
		</li>
		<li>
			<label id="lstyle4">Vendor Style #</label>
			<input type="text" name="style4" id="style4" />
		</li>
		<li>
			<label id="lstyle5">Vendor Style #</label>
			<input type="text" name="style5" id="style5" />
		</li>
		<li>
			<label id="lstyle6">Vendor Style #</label>
			<input type="text" name="style6" id="style6" />
		</li>
		<li>
			<label id="lstyle7">Vendor Style #</label>
			<input type="text" name="style7" id="style7" />
		</li>
		<li>
			<label id="lstyle8">Vendor Style #</label>
			<input type="text" name="style8" id="style8" />
		</li>
		<li>
			<label id="lstyle9">Vendor Style #</label>
			<input type="text" name="style9" id="style9" />
		</li>
		<li>
			<label id="lstyle0">Vendor Style #</label>
			<input type="text" name="style0" id="style0" />
		</li>
	</ul>
	<input type="hidden" name="requestId" value="${itemRequestForm.requestId}"/>		
</form>
</div></div>

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
	
    // add style window
	var note_win=null;
	$('#btn_add_note').click(function(){
		// show add user note window when add note button clicked
		$(this).blur();
		if (!note_win) {
			note_win = new Ext.Window({
				el:'add_user_note_win',
				layout:'fit',
				width:250,
				autoHeight:true,
				closeAction:'hide',
				modal:true,
				plain:true,
				items: new Ext.Panel({
                    contentEl:'add_user_note_form',
                    deferredRender:false,
                    border:false,
					autoHeight:true
                }),
				buttons: [{
		                text: 'Cancel',
		                handler: function(){
		                    //note_win.hide();
		                    window.location.reload(true);
		                }
	            	},{
		                text: 'Save',
		                handler: function(){
							// add note using normal form submit
							$('#add_user_note_form form').ajaxSubmit({
								dataType:'json',
								success: function(resp){
									if(resp.success){
										window.location.reload(true);
									} else {
										var invalidIds = resp.invalid_ids;
										Ext.MessageBox.show({
										title: 'Error',
										msg: 'Please enter valid style numbers.' + invalidIds,
										buttons: Ext.MessageBox.OK,
										icon: 'ext-mb-error'
										});
									}
								},
								url:$('#add_user_note_form form').attr('action')+'?ajax=true'
							});
		                }
					}
				]
			});
		}
        note_win.show(function(){
        	alert('window showing');
			$('#add_user_note_form itemRequestStylesForm text.style1').focus();
		});
		return false;
	});
});
</script>
]]>
</content>