<%@include file="/common/taglibs.jsp" %>
<%@include file="headerItemRequest.jsp" %>
<%@include file="breadcrumbForSkuException.jsp" %>
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
	document.getElementById("tab3").className="activeTab";
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
}
function loadRequestStyle() {
	window.location="../oma/itemrequeststyles.html";	
}
function loadUpdateHistory() {
	window.location="../oma/requestUpdateHistory.html";	
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
		document.getElementById('rs').disabled = false;
		document.getElementById('uh').disabled = false;
		document.getElementById('cancel').disabled = false;

		if(isPosted == "true" && isApproveReject != "true") {
		document.getElementById('reject').disabled = false;
		document.getElementById('approve').disabled = false;
		}
	}
}
function setStatusOfException(box , counter1) {
	var idExpInd = "skuDetails[" + counter1 + "].skuexceptionInd";
	var cost = "skuDetails[" + counter1 + "].overrideCost";
	var fee = "skuDetails[" + counter1 + "].overrideFee";
	
	if(box.checked) {
		document.getElementById(idExpInd).value = "Y";
		document.getElementById(cost).disabled=false;
		document.getElementById(fee).disabled=false;
	} else {
		document.getElementById(idExpInd).value = "N";
		document.getElementById(cost).disabled=true;
		document.getElementById(fee).disabled=true;
	}	
}
function setStatusOfButton(sbutton){
	var buttonClicked = sbutton.value;

	if(buttonClicked == "Save & Post") {
		buttonClicked = "post";
	}
			
	document.itemRequestForm.action="../oma/requestskuexception.html?mode=save&statusofrequest=" + buttonClicked;
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
function verifyRemove(requestId,styleid,color,size,upc)
{
	var stay=confirm("Are you sure you want to remove?");
	if (stay)
		window.location="../oma/requestskuexception.html?mode=remove&requestid=" + requestId + "&styleid=" + styleid + "&color=" + color + "&size=" + size + "&upc=" + upc;
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
    	 //Fixes added by swapnil
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
<title>Fulfillment Service Item Request SKU Exception</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Fulfillment Service Item Request SKU Exception</b>
</div>

<form:form commandName="itemRequestForm" method="post" action="../oma/requestskuexception.html?mode=save" id="itemRequestForm" name="itemRequestForm">
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
		SKU Exceptions
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

	<c:set var="counter" value="-1"></c:set>
	<div style="overflow-x: hidden; width: 100%;">
	<display:table name="${itemRequestForm.skuDetails}" cellspacing="0" cellpadding="0" requestURI="/oma/requestskuexception.html?pagination=true"  
		    id="styles" class="dstable" sort="list" pagesize="25"> <!-- Here we have taken out attribute defaultPage="${itemRequestForm.pageNo}", since displaytag1.2 library is not supporting defaultPage attribute-->
		    
		    <display:setProperty name="paging.banner.items_name" value="SKUs" />
			<display:setProperty name="paging.banner.item_name" value="SKU" />
	
		    <c:set var="counter" value="${counter+1}"></c:set>
		    <display:column property="vendorStyleId" sortable="true" titleKey="Vendor Style #" style="width: 5%;text-align: left"/>
		    <display:column property="styleDesc" sortable="true" titleKey="Style Description" style="width: 15%;text-align: left"/>
		    <display:column property="sizeDescription" sortable="false" titleKey="Size" style="width: 10%;text-align: left"/>
		    <display:column property="color" sortable="false" titleKey="UPC Color" style="width: 10%;text-align: left"/>
		    <display:column  sortable="false" titleKey="Sku Exception" style="width: 5%">
		    	<spring:bind path="itemRequestForm.skuDetails[${counter}].skuexceptionInd">
		    		<c:set var="valOfExInd" value="${status.value}"></c:set>
		    		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
		    		id="<c:out value="${status.expression}"/>" />
		    	</spring:bind>
		    	<c:if test="${valOfExInd == 'Y'}">
		    		<input type="checkbox" checked="checked" name="<c:out value="skuInd${counter}"/>"  
		    		id="<c:out value="skuInd${counter}"/>" onclick="setStatusOfException(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    	<c:if test="${valOfExInd == 'N'}">
		    		<input type="checkbox" name="<c:out value="skuInd${counter}"/>"  
		    		id="<c:out value="skuInd${counter}"/>" onclick="setStatusOfException(this,'<c:out value="${counter}"/>');"/>
		    	</c:if>
		    </display:column>
		    <display:column property="unitCost" sortable="false" titleKey="IDB Unit Cost" style="width: 10%;text-align: right" format="{0,number,currency}"/>
		    <display:column property="unitHandlingfee" sortable="false" titleKey="Vendor Unit Handling Fee" style="width: 10%;text-align: right" format="{0,number,currency}"/>
		    <display:column  sortable="false" titleKey="Override Unit Cost" style="width: 10%" >
		    	<c:out value="$"></c:out>
			<spring:bind
				path="itemRequestForm.skuDetails[${counter}].overrideCost">
				<c:if test="${valOfExInd == 'Y'}">
					<input type="text" name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext"
						onkeyup="checkEnteredNumber(this,false);"
						onkeypress="checkEnteredNumber(this,false);"
						onpaste="checkEnteredNumber(this,true);" />
				</c:if>
				<c:if test="${valOfExInd == 'N'}">
					<input type="text" disabled="disabled"
						name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext"
						onkeyup="checkEnteredNumber(this,false);"
						onkeypress="checkEnteredNumber(this,false);"
						onpaste="checkEnteredNumber(this,true);" />
				</c:if>
			</spring:bind>
		</display:column>
		    <display:column  sortable="false" titleKey="Override Unit Handling Fee" style="width: 10%" >
		    	<c:out value="$"></c:out>
		    	<spring:bind path="itemRequestForm.skuDetails[${counter}].overrideFee">
				<c:if test="${valOfExInd == 'Y'}">
					<input type="text" name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext"
						onkeyup="checkEnteredNumber(this,false);"
						onkeypress="checkEnteredNumber(this,false);"
						onpaste="checkEnteredNumber(this,true);" />
				</c:if>
				<c:if test="${valOfExInd == 'N'}">
					<input type="text" disabled="disabled"
						name="<c:out value="${status.expression}"/>"
						id="<c:out value="${status.expression}"/>"
						value='<fmt:formatNumber type="number" minFractionDigits="2"
            value="${status.value}"/>' class="stylestext"
						onkeyup="checkEnteredNumber(this,false);"
						onkeypress="checkEnteredNumber(this,false);"
						onpaste="checkEnteredNumber(this,true);" />
				</c:if>
			</spring:bind>
		    </display:column>
		    <display:column  sortable="false" style="width: 10%">
		    <c:choose>
		    <c:when test="${itemRequestForm.role == 'readwrite'}">
			<input type="button" value="Remove" name="remove" class="btn"
				onclick="verifyRemove('<c:out value="${itemRequestForm.requestId}" />','<c:out value="${itemRequestForm.skuDetails[counter].vendorStyleId}" />','<c:out value="${itemRequestForm.skuDetails[counter].color}" />','<c:out value="${itemRequestForm.skuDetails[counter].sizeDescription}" />','<c:out value="${itemRequestForm.skuDetails[counter].belkUpc}" />');" />
			</c:when>
			<c:otherwise>
		    	<input type="button" value="Remove" name="remove" class="btn" disabled="disabled"/>
		    </c:otherwise>
			</c:choose>
			</display:column>
	</display:table>
	</div>
	<input type="button" value="Cancel" name="cancel" class="btn cancel_btn" id="cancel" style="margin-top: 30px!important" onclick="verifyCancel();"/>
	<c:choose>
	<c:when test="${itemRequestForm.role == 'readwrite'}">
	<input type="button" value="Save" name="save" id="save" class="btn cancel_btn" style="margin-top: 30px!important" onclick="setStatusOfButton(this);"/>
	<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" style="margin-top: 30px!important" onclick="setStatusOfButton(this);"/>
	</c:when>
	<c:otherwise>
	<input type="button" value="Save" name="save" id="save" class="btn cancel_btn" style="margin-top: 30px!important" disabled="disabled"/>
	<input type="button" value="Save & Post" name="post" id="post" class="btn cancel_btn" style="margin-top: 30px!important" disabled="disabled"/>
	</c:otherwise>
	</c:choose>
	<c:choose>
	<c:when test="${itemRequestForm.approveRole == 'readwriteapprove'}">
	<input type="button" value="Reject" name="reject" id="reject" class="btn cancel_btn" style="margin-top: 30px!important" onclick="setStatusOfButton(this);"/>
	<input type="button" value="Approve" name="approve" id="approve" class="btn cancel_btn" style="margin-top: 30px!important" onclick="setStatusOfButton(this);"/>
	</c:when>
	<c:otherwise>
	<input type="button" value="Reject" name="reject" id="reject" class="btn cancel_btn" style="margin-top: 30px!important" disabled="disabled"/>
	<input type="button" value="Approve" name="approve" id="approve" class="btn cancel_btn" style="margin-top: 30px!important" disabled="disabled"/>
	</c:otherwise>
	</c:choose>
	<input type="button" value="&lt; Request Styles" name="requestScreen" id="rs" class="btn cancel_btn" style="margin-top: 30px!important" onclick="loadRequestStyle();"/>
	<input type="button" value="Update History >" name="exceptionScreen" id="uh" class="btn cancel_btn" style="margin-top: 30px!important" onclick="loadUpdateHistory();"/>
	<form:hidden path="requestStatus" id="requestStatus"/>
	<form:hidden path="userdepts" id="userdepts"/>
	<form:hidden path="locationName" id="locationName"/>
	<form:hidden path="filePath" id="filePath"/>
	<form:hidden path="pageNo" id="pageNo"/>
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