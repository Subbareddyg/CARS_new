<%@include file="/common/taglibs.jsp" %>
<%@include file="headerTabsForVendor.jsp" %>
<%@include file="breadcrumbForStyleSku.jsp" %>

<script type="text/javascript">
checked=false;

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
	 var userDeptChecked = '<c:out value="${stylesSkuForm.userDeptsOnly}" />';
	 if(userDeptChecked == "Y") {
		 alert("Checking checkbox" + document.getElementById("userDeptsOnly").name);
		 document.getElementById("userDeptsOnly").value = true;
	 }	 
	 document.getElementById("tab9").className="activeTab";
	 setTimeout(function() { document.getElementById('styleId').focus(); }, 10);
}
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
function loadItemRequest(box) {
	var aa= document.getElementById('checkall');
	if(aa.checked) {
		document.stylesSkuForm.action="../oma/styleskus.html?mode=item&checkall=true";
		document.stylesSkuForm.submit();
	} else {
		document.stylesSkuForm.action="../oma/styleskus.html?mode=item";
		document.stylesSkuForm.submit();
	}
}
</script>
<style type="text/css">
table{
	border-color:#ff0000;
}
.car_info {
	padding: 0 0 10px 0 !important;
	font-size: 11px !important;
	zoom: 1;
	border:none;
}

.styleskulabel1 {
	float: left;
	text-align: left;
	font-weight: bold;
}
.styleskulabel2 {
	float: left;
	text-align: left;
	font-weight: bold;
}
.styleskulabel3 {
	float: left;
	text-align: left;
	font-weight: bold;
}
.styleskutext1 {
	margin-left: 1%;
	width:130px;
}
.styleskutext2 {
	margin-left: 1%;
	width:130px;
}
.styleskutext3 {
	margin-left: 1%;
	width:130px;
}
.styleskutext4 {
	margin-left: 1%;
}
.styleskutext5 {
	margin-left: 1%;
	width:90px;
}
.stylestext {
	width: 30px;
}
</style>
<head>
<title>Vendor Styles & SKU's</title>
    <meta name="heading" content="My First Screen"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">

<div style="height:20px">
<b>Vendor Styles & SKU's</b>
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
				<b><fmt:message key="stylesku.activestyles"/></b>
				<c:out value="${stylesSkuForm.noOfActiveStyles}"></c:out>
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
				<b><fmt:message key="stylesku.activeskus"/></b>
				<c:out value="${stylesSkuForm.noOfActiveSkus}"></c:out>
			</li>
		</ul>
	</div>
</div>

<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Item Search
	</div>
	<div class="x-panel-body">
	<spring:bind path="stylesSkuForm.*">
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
				<td style="border:0px"><label class="styleskulabel1"><fmt:message key="stylesku.styleno"/></label></td>
				<td style="border:0px"><form:input path="styleId" id="styleId" cssClass="styleskutext1" maxlength="20"/></td>
				<td style="border:0px"><label class="styleskulabel2"><fmt:message key="stylessku.styledesc"/></label></td>
				<td style="border:0px"><form:input path="styleDesc" id="styleDesc" cssClass="styleskutext2" maxlength="200"/></td>
				<td style="border:0px"><label class="styleskulabel3"><fmt:message key="stylesku.dropshipstatus"/></label></td>
				<td style="border:0px"><form:select id="dropShipStatus" path="dropShipStatus" cssClass="styleskutext5">
						<form:option value ="">ALL</form:option>
						<form:option value ="ACTIVE">ACTIVE</form:option>
						<form:option value ="INACTIVE">INACTIVE</form:option>
					</form:select>
				</td>
				<td style="border:0px"><input type="submit" value="Search" name="search" id="search" class="btn cancel_btn" />
				<c:url value="/oma/styleskus.html" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a></td>
			</tr>
			<tr>
				<td style="border:0px"><label class="styleskulabel1"><fmt:message key="stylesku.vendorupc"/></label></td>
				<td style="border:0px"><form:input path="vendorUpc" id="vendorUpc" cssClass="styleskutext1" maxlength="50"/></td>
				<td style="border:0px"><label class="styleskulabel2"><fmt:message key="stylesku.belkupc"/></label></td>
				<td style="border:0px"><form:input path="belkUpc" id="belkUpc" cssClass="styleskutext3" maxlength="50"/></td>
				<td style="border:0px"><label class="styleskulabel3"><fmt:message key="stylesku.idbstatus"/></label></td>
				<td style="border:0px"><form:select id="idbStatus" path="idbStatus" cssClass="styleskutext5">
					<form:option value ="">ALL</form:option>
					<form:option value ="Y">Y</form:option>
					<form:option value ="N">N</form:option>
					<form:option value ="V">V</form:option>
				</form:select></td>
				<td style="border:0px"></td>
			</tr>
			<tr>
				<td colspan="2" style="border:0px"><b><fmt:message key="stylesku.userdepts"></fmt:message></b> &nbsp; <form:checkbox path="userDeptsOnly" id="userDeptsOnly" /></td>
				<td style="border:0px"></td>
				<td style="border:0px"></td>
				<td style="border:0px"></td>
				<td style="border:0px"></td>
				<td style="border:0px"></td>
			</tr>
			
		</table>
	</div>
</div>

<br />

<div id="user_form" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Style Listing
	</div>
	<div class="x-panel-body">
	
	<div class="userButtons">
				<div class="buttons" style="float:right;">
			
				<c:choose>
				<c:when test="${stylesSkuForm.role == 'readwrite'}">
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
	
	<display:table name="${stylesSkuForm.list}" cellspacing="0" cellpadding="0" requestURI="/oma/styleskus.html?pagination=true"  
		    id="history" class="dstable" sort="list" pagesize="25">
		    <c:set var="counter" value="${counter+1}"></c:set>
		    <display:column sortable="false" titleKey='<input type="checkbox" id="checkall" name="checkall" onclick="checkedAll(stylesSkuForm);" />' style="width: 2%">
		    	<input type="checkbox" onchange="setStatusOfStyles(this,'<c:out value="${counter}"/>');" id="${counter}" name="chk"  onclick="setStatusOfStyles(this,'<c:out value="${counter}"/>');"/>
		    	<spring:bind path="stylesSkuForm.list[${counter}].styleSelected">
		    		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
		    		id="<c:out value="${status.expression}"/>" />
		    	</spring:bind>
		    </display:column>
		    <display:column sortable="true" titleKey="Vendor Style #" style="width: 10%; text-align: left" >
			<secureurl:secureAnchor name="StyleId" cssStyle="removeVendor"
				title="${stylesSkuForm.list[counter].styleId}" elementName="StyleId"
				localized="true" hideUnaccessibleLinks="true"
				arguments="${stylesSkuForm.list[counter].styleId},${stylesSkuForm.list[counter].styleDesc},${stylesSkuForm.list[counter].dropShipStatus},${stylesSkuForm.list[counter].idbAllowDropship}" />
			</display:column>
		    <display:column property="styleDesc" sortable="true" titleKey="Style Description" style="width: 15%; text-align: left"/>
		    <display:column property="dropShipStatus" sortable="true" titleKey="Drop Ship Status" style="width: 5%; text-align: left"/>
		    <display:column property="noOfDropshipSkus" sortable="true" titleKey="# of Drop ship SKU's" style="width: 5%; text-align: right"/>
		    <display:column property="idbAllowDropship" sortable="true" titleKey="IDB Allow Drop Ship" style="width: 5%; text-align: center" />
		    <display:column property="noOfIdbDropshipSkus" sortable="true" titleKey="# of IDB Drop Ship SKU's" style="width: 5%; text-align: center"/>
		    <display:setProperty name="paging.banner.item_name" value="style"/>
    		<display:setProperty name="paging.banner.items_name" value="styles"/>
	</display:table>
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