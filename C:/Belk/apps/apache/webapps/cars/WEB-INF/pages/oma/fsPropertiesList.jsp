
<%@ page import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTab.jsp"%>


<script type="text/javascript">
function verifyCancel()
{
var stay=confirm("Are you sure you want to cancel?");
if (stay){
window.location="../oma/orderManagement.html?method=load";
}
	else{
		return false;
	}
}
// dropship phase 2: added code fix for firefox browser onload
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
	setAndResetIntType();
	viewOnlyMode();
	 var element=document.getElementById('fulfillmentService.fulfillmentServiceName');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
}


function restrictMaxLength(Object, MaxLen)
{
  if(Object.value.length>MaxLen){
	 document.getElementById("fulfillmentService.fulfillmentServiceDesc").value=Object.value.substring(0,MaxLen);
  }
}


function viewOnlyMode(){
	
	var mode1='<%=request.getParameter("mode")%>';
	
	if(mode1==='viewOnly'){
		
		 for(i=0; i<document.forms[1].elements.length; i++){
					 document.forms[1].elements[i].disabled=true;
			 
		 }			
	}
}
//new jquery function for dropdown functionality
function a(obj){ 
var create = '<select id="test">'; 
for(var i = 0; i < obj.length;i++)
{         
create += '<option value="'+obj[i]+'">'+obj[i]+'</option>';     
} 
create += '</select>';     
jQuery('#some_element').append(create); 
}

function setAndResetShip(){

if(document.getElementById("fmID").options[document.getElementById("fmID").selectedIndex].text == "Belk Owned Inventory")
{
	document.getElementById("address").style.display = 'block';
	document.getElementById("intID").options[1]=null;
	document.getElementById("intID").options[0]=new Option("Direct");
	document.getElementById("intID").options[0].value="1";	
}
else if(document.getElementById("fmID").options[document.getElementById("fmID").selectedIndex].text =="Vendor DropShip"){
document.getElementById("address").style.display = 'none';
document.getElementById("intID").options[0]=new Option("Order Broker Network");
document.getElementById("intID").options[1]=new Option("Direct");
document.getElementById("intID").options[0].value="2";
document.getElementById("intID").options[1].value="1";
document.getElementById("intID").options[0].selected = true;
document.getElementById("address").style.display = 'none';

} 
}
function setAndResetIntType(){
if(document.getElementById("intID").options[document.getElementById("intID").selectedIndex].text == "Direct")
{
document.getElementById("address").style.display = 'block';
}
else{
document.getElementById("address").style.display = 'none';
}
}

</script>
<style type=text/css>
LABEL.fulfillmentServicedetails {
	FONT-WEIGHT: bold;
	font-size: 11px;
	MARGIN-LEFT: 1%;
	WIDTH: 13%;
	float: left;
	text-align: left;
}
</style>

<html>
<head>
<title>Fulfillment Service Properties</title>
<meta name="heading" content="My First Screen" />
<meta name="menu" content="AdminMenu" />
</head>

<body class="admin">
<div style="margin-left: 5px;"><br />
<%
String mode=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"";  
  
if(mode.equals("edit") || (mode.equals("viewOnly"))){
%>
<form:form commandName="fulfillmentServiceForm" method="post"
	action="${formAction}" id="fulfillmentServiceForm" >

	<a href="../oma/orderManagement.html?method=load">Fulfillment
	Services</a> ><c:out
		value="${fulfillmentServiceForm.fulfillmentService.fulfillmentServiceName}"></c:out>
	<br>
	<br>
	
</form:form>
<%}
else if(mode.equals("add")){
%>
<form:form commandName="fulfillmentServiceForm" method="post"
	action="${formAction}" id="fulfillmentServiceForm" name="fulfillmentServiceForm">

	<a href="../oma/orderManagement.html?method=load">Fulfillment
	Services</a>
	<br>
	<br>
	
</form:form>
	<%} %>
 <b>Fulfillment Service Properties </b><br>
<br>
</div>

<% 

  if(mode.equals("edit")|| mode.equals("viewOnly")){
	  
   %>
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
<%@ include file="omaInfoSection.jsp"%>

</div>

<%}
if(mode.equals("add")||(mode.equals("edit") || mode.equals("")) || (mode.equals("viewOnly"))){

%>
 <div class="cars_panel x-hidden" style="margin-top:10px;">
 <div class="x-panel-header">
 Edit Fulfillment Service Properties
 </div>
	<div class="x-panel-body">
	<c:url
	value="/oma/fsPropertiesList.html" var="formAction" /> <form:form
	commandName="fulfillmentServiceForm" method="post"
	action="${formAction}" id="fulfillmentServiceForm"
	onsubmit="return onFormSubmit(this)">

	
<spring:bind
	path="fulfillmentServiceForm.*">
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


	<c:if test="${sessionScope.UniqueServiceName=='yes'}">
 <img src="<c:url value="/images/iconWarning.gif"/>"	alt="<fmt:message key="icon.warning"/>" class="icon" />
 <c:out value="Service Name must be Unique" escapeXml="false"/> <br/>
 <%session.removeAttribute("UniqueServiceName"); %>
</c:if>

<c:if test="${sessionScope.saveMsg=='yes'}">
 <span style="background:#FFFF00;"> <c:out value="Saved Successfully!!" escapeXml="false"/> </span><br/>
 <%session.removeAttribute("saveMsg"); %>
</c:if>

<div id="attr_container">
	<ol>
		<li class="first"><span class="req" style="color: #FF0000">*
		</span>Indicates Required Fields</li>
		

		<li id="fulfillmentServiceName">
		<form:hidden path="fulfillmentService.fulfillmentServiceID" id="fsID" />
		<form:hidden path="fulfillmentService.createdDate" id="createdDate" />
		<form:hidden path="fulfillmentService.updatedDate" id="updatedDate" />
		<form:hidden path="fulfillmentService.createdBy" id="createdBy"/>
		<form:hidden path="fulfillmentService.updatedBy" id="updatedBy"/>
		
		<label><b><fmt:message key="fulfillmentService.servicename" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
		
			<form:input
			path="fulfillmentService.fulfillmentServiceName"
			id="fulfillmentService.fulfillmentServiceName" cssClass="text"
			cssErrorClass="text error" maxlength="50" />
			
		 	</li>
			
		<li class="first" id="fulfillmentServiceDesc">
		<label><b><fmt:message key="fulfillmentService.description" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
			
		<form:textarea onkeypress="return restrictMaxLength(this, 200);" path="fulfillmentService.fulfillmentServiceDesc"
			id="fulfillmentService.fulfillmentServiceDesc" cssClass="text"
			cssErrorClass="text error" cols="15" rows="5" />
			
			</li>
			
		<li class="first" id="fulfillmentmethod">
		<label><b><fmt:message key="fulfillmentService.fulfillmentmethod" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
			 <c:set var="FulfillmentMethod"
			value="${FulfillmentMethod}" scope="session" />
			
			 <form:select id="fmID" path="fulfillmentService.fulfillmentMethodID.fulfillmentMethodID"
			onchange="setAndResetShip();">
			<c:forEach items="${FulfillmentMethod}" var="FulfillmentMethod" >
				<form:option value="${FulfillmentMethod.fulfillmentMethodID}" >
					<c:out value="${FulfillmentMethod.fulfillmentMethodDesc}" />
				</form:option>
			</c:forEach>
		</form:select>
		
		</li>


		<li class="first" id="IntegrationType">
		<label><b><fmt:message key="fulfillmentService.integrationtype" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
		
		 <c:set var="IntegrationType" value="${IntegrationType}" scope="session" /> 
		 
		 <form:select id="intID" path="fulfillmentService.fulfillmentServiceIntTypeID.integrationTypeID" onchange="setAndResetIntType();">
			<c:forEach items="${IntegrationType}" var="IntType">
				<form:option value="${IntType.integrationTypeID}">
					<c:out value="${IntType.integrationTypeDesc}" />
				</form:option>
			</c:forEach>
		</form:select>
		</li>
		
		<ul id="address" style="display: none">
			<li><appfuse:label styleClass="fulfillmentServicedetails"
				key="fulfillmentService.defaultShippingLocation" />:</li>

			<li class="first">
			<label><b><fmt:message key="fulfillmentService.locationName" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
			 <form:input path="address.locName"
				id="txt_style_descr" maxlength="50" cssClass="text"
				cssErrorClass="text error" /></li>
			<li class="first">
			<label><b><fmt:message key="fulfillmentService.address1" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
			<form:input path="address.addr1" id="txt_style_descr" maxlength="100"
				cssClass="text" cssErrorClass="text error" /></li>
			<li class="first">
			<label><b><fmt:message key="fulfillmentService.address2" />:</b> </label>
			<form:input path="address.addr2" id="txt_style_descr" maxlength="100"
				cssClass="text" cssErrorClass="text error" /></li>
			<li class="first"><form:hidden path="address.addressID" />
			<label><b><fmt:message key="fulfillmentService.city" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>

				 <form:input
				path="address.city" id="txt_style_descr" maxlength="50"
				cssClass="text" cssErrorClass="text error" /></li>
			<li>
			<label><b><fmt:message key="fulfillmentService.state" />:</b> <span
			class="req" style="color: #FF0000">*</span></label>
			
			<c:set var="stateList" value="${states}" scope="session"/>  
				<form:select id="stateCd" path="address.state"  cssErrorClass="select medium error">
				 <form:option value=""></form:option>
                                         <c:forEach items="${stateList}" var="state" >
                                        
                                               <form:option value="${state.stateCd}"><c:out value="${state.stateName}"/></form:option>
                                         </c:forEach>
                              </form:select></li>
			<li>
			<label><b><fmt:message key="fulfillmentService.zip" />:</b><span
			class="req" style="color: #FF0000">*</span></label>
			<form:input path="address.zip" id="txt_style_descr" maxlength="5" cssClass="text"
				cssErrorClass="text error" />
				</li>
		</ul>
		<li class="first" id="fulfillmentServiceStatusCode">
		<label><b><fmt:message key="fulfillmentService.status" />:</b><span
			class="req" style="color: #FF0000">*</span></label>		
			<form:select id="statusCd"
			path="fulfillmentService.fulfillmentServiceStatusCode"
			cssClass="text">
			<form:option value="Active"></form:option>
			<form:option value="Inactive"></form:option>
			<form:option value="Testing"></form:option>
		</form:select></li>


	</ol>
	
<ul>	 
	<li id="submit_buttons">
	<div class="buttons" style="margin-left: 17.5%;">
	
	
	<c:if test="${sessionScope.mode!='viewOnly'}">
		<input type="button" class="btn cancel_btn" id="btn_cancel" name="cancel" value="<fmt:message key="button.cancel"/>" onClick="verifyCancel();" />
		<input type="submit" class="btn save_btn" id="btn_save" name="save" value="<fmt:message key="button.save"/>" />
	</c:if>
	
	<c:if test="${sessionScope.mode=='viewOnly'}">
		
		<input type="button" onclick="none" disabled="disabled" value="<fmt:message key="button.cancel"/>"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="btn_cancel" class="btn cancel_btn">
		<input type="button" onclick="none" disabled="disabled" value="<fmt:message key="button.save"/>"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="btn_save" class="btn">
	</c:if>
	
	</div>
	</li>
 </ul>	
</form:form></div>
</div>
</div>

<%
}
%>
</body>
</html>