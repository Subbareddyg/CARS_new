<%@ include file="/common/taglibs.jsp"%>
<c:set var="venfsid"
	value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}" />
<c:choose>
	<c:when test="${venfsid!=null}">
		<%@ include file="headerTabsForVendor.jsp"%>
	</c:when>
	<c:otherwise>
		<%@ include file="headerTabsForAddVendor.jsp"%>
	</c:otherwise>
</c:choose>




<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="breadcrumbForVendorProperties.jsp"%>
<head>
<title>Vendor Properties</title>

</head>
<body class="admin">

<script language="javascript">

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
		
	 document.getElementById("tab100").className="activeTab";
	 if(document.getElementById('venNumAddMode') !=null){
	 	setTimeout(function() { document.getElementById('venNumAddMode').focus(); }, 10);
	 }
	 
	

	}
	
	
</script>
<div class="cars_panel x-hidden" style="margin: 10px 0px 0px;">


<%@ include file="omaInfoSection.jsp"%>
</div>


<div class="cars_panel x-hidden" style="margin-top: 10px;">
<div class="x-panel-header">
<%if(request.getAttribute("mode")!=null && request.getAttribute("mode").equals("edit") ){%>
Edit Fulfillment Service Vendor <%}else{ %> Add Fulfillment Service Vendor
<%} %>
</div>

<div class="x-panel-body"><spring:bind path="fsVendorForm.*">
		<c:if test="${empty sessionScope.fulfillmentService.fulfillmentServiceID}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="Please select fulfillment service." escapeXml="false"/><br />
		</c:if>
		
		<c:if test="${not empty status.errorMessages}">
			<div class="error"><c:forEach var="error"
			items="${status.errorMessages}">
			<c:choose>
				<c:when test="${error == 'Saved Successfully!!'}">
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

</spring:bind> <c:if test="${sessionScope.showMsg=='yes'}">
	<span style="background: #FFFF00;"> <c:out
		value="Saved Successfully!!" escapeXml="false" /> </span>
	<br />
	<%session.setAttribute("showMsg","no"); %>
</c:if>

<div id="attr_container"><c:url value="/oma/AddVendor.html"
	var="formAction" /> <form:form commandName="fsVendorForm"
	method="post" action="${formAction}" id="fsVendorForm"
	name="fsVendorForm">
	<fieldset><span class="req" style="color: #FF0000">*
	</span>Indicates Required Fields
	<ul>
	<table Cellspacing="0" Cellpadding="0" style="border:0px;">
		<%if((request.getParameter("param")!=null && request.getParameter("param").equals("param"))){ 
					session.setAttribute("param","param");
					
				} else{
					session.removeAttribute("param");
				}
			%>
		<%if((request.getAttribute("mode")!=null && request.getAttribute("mode").equals("viewOnly")) 
				&& (request.getParameter("param")!=null && request.getParameter("param").equals("param")) ){ %>
		<tr>
		<td width="18%" style="border:0px;">
			<label for="txt_attr_name"> Vendor #:
				<span class="req" style="color: #FF0000">*</span> 
			</label>
			</td>
			<td width="82%" style="border:0px;">
			 <label style="width: 15%; text-align: left; font-weight: normal">
			  <c:out value="${fsVendorForm.venNum}"></c:out> 
			  <form:hidden path="venNum" />
			</label> &nbsp; &nbsp; 
			<span>
			<input type="button" onclick="none" disabled="disabled" value="Verify"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="verify_btn" class="btn"/>
			</span>
			</td>
	
		
		</tr>
		<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> Vendor Name : </label>
			</td>
			<td style="border:0px;"> 
			<label 	class="text" style="width: 35%; text-align: left; font-weight: normal"> 
				<c:out	value="${fsVendorForm.venName}"></c:out>
			 	<form:hidden path="venName" />
				<form:hidden path="vndrId" /> 
			</label>
			</td>
			
		</tr>
		<%} else{%>
		<tr>
		<td width="18%" style="border:0px;">
		<label for="txt_attr_name"> Vendor #:<span	class="req" style="color: #FF0000">*</span></label>
		</td>
		<td width="82%" style="border:0px;">
		 
		  <form:input path="venNum" id="venNumAddMode" cssClass="text"	cssErrorClass="text medium error" maxlength="7"
			onkeypress="disableFields();" onchange="disableFields();" /> &nbsp; &nbsp; 
			<span><input	type="submit" id="verify_btn" name="verify" value="Verify" /></span>
			</td>
			
			 <form:hidden path="vndrId" />
		</tr>
		<tr>
		<td style="border:0px;">
		<label for="txt_attr_name"> Vendor Name : </label> 
		</td>
		<td style="border:0px;">
			<form:input	path="venName" readonly="true" id="venName" cssClass="text"
			cssErrorClass="text medium error" cssStyle="border:none;width:50%" />
		</td>
			
		</tr>
		<%} %>
		
		<%if((request.getAttribute("mode")!=null && (request.getAttribute("mode").equals("edit") || request.getAttribute("mode").equals("verify") ))&&(!(request.getParameter("param")!=null && request.getParameter("param").equals("param"))) ){%>

	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> Corporate Headquarters:<span class="req"	style="color: #FF0000">*</span></label>
		 </td>
		<td style="border:0px;">
			<display:table	name="sessionScope.addrList" cellspacing="0" cellpadding="0" requestURI="/oma/fulfillmentServiceVendors.html?action=search" style="width:50%"
			id="addr">
				<display:column property="locName" titleKey="Location"
					style="width: 7%" />
				<display:column property="completeAddr" titleKey="Address"
					style="width: 7%" />
				<display:column property="city" titleKey="City" style="width:7%" />
				<display:column property="state" titleKey="State" style="width: 5%" />
				<display:column property="zip" titleKey="ZIP" style="width: 5%" />
				<%if(request.getAttribute("mode")!=null && (request.getAttribute("mode").equals("edit") )){%>
				<display:column titleKey="Remove" style="width: 5%">
					<input type="submit" id="${addr.addressID}" class="add_note btn"
						name="Remove" onclick="setValue(this,'edit');" value="Remove" />
				</display:column>
				<%}%>
				<%if(request.getAttribute("mode")!=null && (request.getAttribute("mode").equals("verify") )&&(!(request.getParameter("param")!=null && request.getParameter("param").equals("param")))){%>
				<display:column titleKey="Remove">
					<input type="submit" id="${addr.addressID }" class="add_note btn"
						name="Remove" onclick="setValue(this,'verify');"
					value="Remove" />
				</display:column>
			<%}%>
		</display:table> 
		
		</td>
	
	</tr>
	<input type="hidden" name="addressIdForRemove"	id="addressIdForRemove" /> <input type="hidden" name="modeForRemove" id="modeForRemove" /> 
			
	<tr>
		<td style="border:0px;"></td>
		<td style="border:0px;">
			<%if(request.getAttribute("mode")!=null &&( request.getAttribute("mode").equals("edit") ||
					request.getAttribute("mode").equals("verify"))&&(!(request.getParameter("param")!=null 
							&& request.getParameter("param").equals("param")))){%>
				<div class="buttons">
			
				<input type="submit" id="add_car_note_btn" class="add_note btn"	value="Add Location" /> <span id="add_note_msg"></span>
				</div>
			<%}else{ %>
				<div class="buttons" >
				<input type="button" onclick="none" disabled="disabled" value="Add Location"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="add_car_note_btn" class="add_note btn"/>
				 <span id="add_note_msg"></span>
				</div>
			<%} %>
		</td>
		
	</tr>

	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> <fmt:message key="fsvendor.edit.safetyinv" />
			</label>
		</td>
		<td style="border:0px;"> <form:input path="safetyInvAmt" id="safetyInvAmt" cssClass="text"
			cssErrorClass="text medium error" maxlength="12" /> &nbsp; &nbsp;
			
				<span><form:select path="safetyInvAmtTyp" id="safetyInvAmtTyp" cssClass="select"	cssErrorClass="text medium error">
					<form:option value="perc_inventory">	% of inventory	</form:option>
					<form:option value="num_units">	# of units	</form:option>
				</form:select></span>
			</td>
	</tr>
	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> Vendor service Level(Business days order to fulfill):<span class="req"
			style="color: #FF0000">*</span> </label>
			
		</td>
		<td style="border:0px;"> 
		<form:select path="vndrServLvl"	id="vndrServLvl" cssClass="select" cssErrorClass="text medium error">

			<form:option value="1">
							1
						</form:option>
			<form:option value="2">
							2
						</form:option>
			<form:option value="3">
							3
						</form:option>
			<form:option value="4">
							4
						</form:option>
			<form:option value="5">
							5
						</form:option>
			<form:option value="6">
							6+
						</form:option>
		</form:select>
		</td>
		
		
	</tr>


		<c:if
			test="${sessionScope.fulfillmentService.fulfillmentMethodID.fulfillmentMethodID != '2'}">
			
			<tr>
				<td style="border:0px;">
					<label for="txt_attr_name"> Invoicing Method:<span class="req"	style="color: #FF0000">*</span> </label> 
				</td>
					<c:set var="invoiceMeths" value="${invoiceMethods}"		scope="session" />
				 <td style="border:0px;">
				 
				 <form:select id="invoiceMethod"	path="invoiceMethod" cssErrorClass="select medium error">
				
					<c:forEach items="${invoiceMeths}" var="meth">
						<form:option value="${meth.invoiceMethodCode}">
							<c:out value="${meth.name}" />
						</form:option>
					</c:forEach>
				</form:select>
				</td>
				
			</tr>
		</c:if>
	<c:if
			test="${sessionScope.fulfillmentService.fulfillmentMethodID.fulfillmentMethodID == '2'}">
			
	<tr >
		<td style="border:0px;">
			<label for="txt_attr_name"> Invoicing Method:<span class="req"	style="color: #FF0000">*</span  >  </label>
		</td>
			<c:set var="invoiceMeths" value="${invoiceMethods}"	scope="session" />
		<td style="border:0px;"> 
			<span disabled="disabled"> 
				<form:select  id="invoiceMethod"	path="invoiceMethod"  cssErrorClass="select medium error">
				<form:option value=""></form:option>
				<c:forEach items="${invoiceMeths}" var="meth">
					<form:option value="${meth.invoiceMethodCode}">
						<c:out value="${meth.name}" />
					</form:option>
				</c:forEach>
				</form:select>
			</span>
		</td>

	</tr>
		</c:if>
	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> Invoice to Receipt Days:</label>
		</td>
		<td style="border:0px;"> 
			<form:checkbox onclick="disab();" path="isInvToRcpt" id="checkbox" value="true" /> Override ACP Calculated Invoice to Receipt Days
		</td>
	
	</tr>
	
	<tr>
		<td style="border:0px;">
			<label for="txt_expited_shipping"> Expedited Shipping:</label>
		</td>
		<td style="border:0px;"> 
			<form:checkbox path="isExpeditedShipping" id="isExpeditedShipping"/> Vendor Support Expedited Shipping
		</td>
	
	</tr>
		<c:if test="${fsVendorForm.isInvToRcpt == 'true'}">
	<tr id="override" Style="display: block">
	
			<td style="border:0px;">
				<label	for="txt_attr_name"> Override # of Days:<span class="req"	style="color: #FF0000">*</span> </label>
			</td> 
			<td style="border:0px;">
			 <form:select path="overrideDays"	cssClass="select" cssErrorClass="text medium error"	id="overrideDays">
				<form:option value="0">
							0
						</form:option>
				<form:option value="1">
							1
						</form:option>
				<form:option value="2">
							2
						</form:option>
				<form:option value="3">
							3
						</form:option>
				<form:option value="4">
							4
						</form:option>
				<form:option value="5">
							5
						</form:option>
				<form:option value="6">
							6
						</form:option>
				<form:option value="7">
							7
						</form:option>
				<form:option value="8">
							8
						</form:option>
				<form:option value="9">
							9
						</form:option>
				<form:option value="10">
							10
						</form:option>

			</form:select>
			</td>
			
	
	</tr>
</c:if>
		<c:if test="${fsVendorForm.isInvToRcpt != 'true'}">
			<tr id="override" Style="display: none">
				<td style="border:0px;">
					<label	for="txt_attr_name"> Override # of Days:<span class="req"	style="color: #FF0000">*</span> </label>
				</td>
				<td style="border:0px;"> <form:select path="overrideDays"	id="overrideDays" cssClass="select"	cssErrorClass="text medium error">
					<form:option value="0">
								0
							</form:option>
					<form:option value="1">
								1
							</form:option>
					<form:option value="2">
								2
							</form:option>
					<form:option value="3">
								3
							</form:option>
					<form:option value="4">
								4
							</form:option>
					<form:option value="5">
								5
							</form:option>
					<form:option value="6">
								6
							</form:option>
					<form:option value="7">
								7
							</form:option>
					<form:option value="8">
								8
							</form:option>
					<form:option value="9">
								9
							</form:option>
					<form:option value="10">
								10
							</form:option>

				</form:select>
			</td>

	</tr>
</c:if>

	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> <fmt:message key="product.edit.status" /></label> 
		</td>
		<td style="border:0px;">
			<form:select path="statusCd"	id="statusCd" cssClass="select" cssErrorClass="text medium error">
			<form:option value="ACTIVE">
				<fmt:message key="flag.active" />
			</form:option>
			<form:option value="INACTIVE">
				<fmt:message key="flag.inactive" />
			</form:option>
			<form:option value="TESTING">
							Testing
						</form:option>
			</form:select>
		</td>

		 
	</tr>
		<%}
				else{%>
		<tr>
			<td style="border:0px;">
				<label for="txt_attr_name"> Corporate Headquarters: <span class="req"	style="color: #FF0000">*</span> </label> 
			</td>
			<td style="border:0px;">
				<display:table	name="sessionScope.addrList" cellspacing="0" cellpadding="0" requestURI="/oma/fulfillmentServiceVendors.html?action=search" style="width:50%"
					id="addr">
					<display:column property="locName" titleKey="Location"
						style="width: 7%" />
					<display:column property="completeAddr" titleKey="Address"
						style="width: 7%" />
					<display:column property="city" titleKey="City" style="width:7%" />
					<display:column property="state" titleKey="State" style="width: 5%" />
					<display:column property="zip" titleKey="ZIP" style="width: 5%" />
					<% if(!(request.getParameter("param")!=null && request.getParameter("param").equals("param"))){%>
					<display:column titleKey="Remove" class="add_note btn"
						style="width: 5%">
						<input type="submit" id="${addr.addressID}" class="add_note btn"
							name="Remove" disabled="disabled" onclick="setValue(this,'edit');" value="Remove" />
					</display:column>
					<%} %>
				</display:table> 
			</td>

	</tr>
	<tr>
		<td style="border:0px;">&nbsp;</td>
		<td style="border:0px;">
	<%if(request.getAttribute("mode")!=null &&( request.getAttribute("mode").equals("edit") ||
			request.getAttribute("mode").equals("verify")) &&(!(request.getParameter("param")!=null &&
					request.getParameter("param").equals("param")))){%>
	
			
				<input type="submit" id="add_car_note_btn" class="add_note btn"	value="Add Location" /> 
				<span id="add_note_msg"></span>
			
			<%}else{ %>
		
			<input type="button" onclick="none" disabled="disabled" value="Add Location"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="add_car_note_btn" class="add_note btn"/> <span id="add_note_msg"></span>
		
		<%} %>
		</td>

	</tr>
		<tr>
			<td style="border:0px;">
				<label for="txt_attr_name"> <fmt:message	key="fsvendor.edit.safetyinv" /> </label> 
			</td>
			<td style="border:0px;">
				<form:input disabled="true"	path="safetyInvAmt" id="safetyInvAmt" cssClass="text" cssErrorClass="text medium error" maxlength="12" /> &nbsp; &nbsp; 
				
		<span>	<form:select disabled="true" path="safetyInvAmtTyp" id="safetyInvAmtTyp" cssClass="select" cssErrorClass="text medium error">
				<form:option value="perc_inventory">% of inventory	</form:option>
				<form:option value="num_units">	# of units	</form:option>
				</form:select></span>
			</td>
		</tr>
		<tr>
			<td style="border:0px;">
				<label for="txt_attr_name"> Vendor service Level(Business days order to fulfill):<span class="req" style="color: #FF0000">*</span> </label> 
			</td>
			<td style="border:0px;">	
				<form:select disabled="true" path="vndrServLvl" id="vndrServLvl" cssClass="select"	cssErrorClass="text medium error">

					<form:option value="1">
									1
								</form:option>
					<form:option value="2">
									2
								</form:option>
					<form:option value="3">
									3
								</form:option>
					<form:option value="4">
									4
								</form:option>
					<form:option value="5">
									5
								</form:option>
					<form:option value="6">
									6+
								</form:option>
				</form:select>
			</td>
		
		</tr>


	<c:if test="${sessionScope.fulfillmentService.fulfillmentMethodID.fulfillmentMethodID != '2'}">
		<tr>
		 <td style="border:0px;">		
			<label for="txt_attr_name"> Invoicing Method: </label>
		</td>
		<td style="border:0px;">
			<c:set var="invoiceMeths" value="${invoiceMethods}"	scope="session" /> 
			<form:select disabled="true" id="invoiceMethod"	path="invoiceMethod" cssErrorClass="select medium error">
				<form:option value=""></form:option>
				<c:forEach items="${invoiceMeths}" var="meth">
					<form:option value="${meth.invoiceMethodCode}">
						<c:out value="${meth.name}" />
					</form:option>
				</c:forEach>
			</form:select>
		</td>
	
	</tr>
		</c:if>
<c:if test="${sessionScope.fulfillmentService.fulfillmentMethodID.fulfillmentMethodID == '2'}">
	<tr>
		<td style="border:0px;">	
			<label for="txt_attr_name"> Invoicing Method: </label>
		</td>
		<td style="border:0px;">
			<span disabled="disabled"><c:set var="invoiceMeths" value="${invoiceMethods}"	scope="session" />
			<form:select  id="invoiceMethod"
				path="invoiceMethod" cssErrorClass="select medium error">
				<form:option value=""></form:option>
				<c:forEach items="${invoiceMeths}" var="meth">
					<form:option value="${meth.invoiceMethodCode}">
						<c:out value="${meth.name}" />
					</form:option>
				</c:forEach>
			</form:select>
			</span>
		</td>
	
	</tr>
</c:if>
	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> Invoice to Receipt Days:</label> 
		</td>
		<td style="border:0px;">	
			<form:checkbox onclick="disab();" disabled="true" path="isInvToRcpt" id="checkbox" value="true" /> Override ACP Calculated Invoice to
		Receipt Days
		</td>
	
	</tr>
	
	<c:if test="${fsVendorForm.isInvToRcpt == 'true'}">
	<tr id="override" Style="display: block">
	  <td style="border:0px;">
	  	<label for="txt_attr_name"> Override # of Days:<span class="req"style="color: #FF0000">*</span> </label>
	  </td>
	  <td style="border:0px;">
	  	 <form:select disabled="true"path="overrideDays" id="overrideDays" cssClass="select" cssErrorClass="text medium error">
				<form:option value="0">
							0
						</form:option>
				<form:option value="1">
							1
						</form:option>
				<form:option value="2">
							2
						</form:option>
				<form:option value="3">
							3
						</form:option>
				<form:option value="4">
							4
						</form:option>
				<form:option value="5">
							5
						</form:option>
				<form:option value="6">
							6
						</form:option>
				<form:option value="7">
							7
						</form:option>
				<form:option value="8">
							8
						</form:option>
				<form:option value="9">
							9
						</form:option>
				<form:option value="10">
							10
						</form:option>

			</form:select>
			</td>
		
	</tr>
		</c:if>
	<tr>
		<td style="border:0px;">
			<label for="txt_expited_shipping"> Expedited Shipping:</label>
		</td>
		<td style="border:0px;"> 
			<form:checkbox path="isExpeditedShipping" id="isExpeditedShipping" disabled="true"/> Vendor Support Expedited Shipping
		</td>
	
	</tr>
		
	<tr>
		<td style="border:0px;">
			<label for="txt_attr_name"> <fmt:message	key="product.edit.status" /> </label>
		</td>
		<td style="border:0px;">
			 <form:select disabled="true"	path="statusCd" id="txt_attr_name" cssClass="select" cssErrorClass="text medium error">
			<form:option value="ACTIVE">
				<fmt:message key="flag.active" />
			</form:option>
			<form:option value="INACTIVE">
				<fmt:message key="flag.inactive" />
			</form:option>
		</form:select>
		</td>
		
	</tr>
		<%} %>
	
	<br />
	<input type="hidden" name="id"
		value="${fsVendorForm.vndrFulfillmentServId}" /> <form:hidden
		path="vndrFulfillmentServId" />
	<div class="buttons" style="margin-left:19.5%">
	
	<%if(request.getAttribute("mode")!=null &&( request.getAttribute("mode").equals("edit") || 
			request.getAttribute("mode").equals("verify")) && !(request.getParameter("param")!=null 
					&& request.getParameter("param").equals("param"))){%>
	<tr>
		<td style="border:0px;">
		</td>
		<td style="border:0px;">
			<input type="button"
				value="Cancel" name="cancel" class="btn cancel_btn"
				onClick="verifyCancel() ;" />
			<input type="submit" class="btn save_btn" id="save_edit" name="save"
				value="<fmt:message key="button.save"/>" /> 
		</td>
	</tr>
	<%}else {%>  
	<tr>
		<td style="border:0px;">
		</td>
		<td style="border:0px;">
			<input type="button" onclick="none" disabled="disabled" value="Cancel"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 class="btn cancel_btn"/>
			
			<input type="hidden" id="save_edit"/>
				
				<input type="button" onclick="none" disabled="disabled" value="<fmt:message key="button.save"/>"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 class="btn save_btn"/>
		</td>
	</tr>
	<%} %>
			</table>
	</div>
	</fieldset>



</form:form>
<div id="add_car_note_win" class="x-hidden">
<div class="x-window-header"></div>
<div id="add_car_note_form" class="content">

<form action="ajaxAddressForm.html" method="post"><span class="req"
	style="color: #FF0000">* </span>Indicates Required Fields

<ul>

	<li class="addr1"><label class="desc">Location Name:<span
		class="req" style="color: #FF0000">*</span></label> <input name="location"
		id="locname" Class="text" /></li>
	<li class="addr1"><label class="desc">Address 1:<span
		class="req" style="color: #FF0000">*</span></label> <input name="addr1"
		id="addr1" Class="text" /></li>
	<li class="addr2"><label class="desc">Phone/Fax:</label> <input
		name="addr2" id="addr2" /></li>
	<li class="city"><label class="desc">City:<span
		class="req" style="color: #FF0000">*</span></label> <input name="city"
		id="city" Class="text" /></li>
	<li class="state"><label class="desc">State :<span
		class="req" style="color: #FF0000">*</span></label> <c:set var="stateList"
		value="${states}" scope="session" /> <select name="state"
		id="stateCd" Class="text" >

		<c:forEach items="${stateList}" var="state">
			<option value="${state.stateCd}"><c:out
				value="${state.stateCd}" /></option>
		</c:forEach>
	</select></li>
	<li class="zip"><label class="desc">ZIP:<span class="req"
		style="color: #FF0000">*</span></label> <input
		onBlur=validateZIP(this.value);; MAXLENGTH="5"
		onBlur="Minimum(this,5);" name="zip" id="zip" Class="text zip" /></li>
</ul>
<br style="clear: both;" />
<input type="hidden" name="carId" value="${detailCar.carId}" /> <input
	type="hidden" name="carNotes.noteTypeCd:" value="CAR_NOTES" /></form>
</div>
</div>
</div>
</div>
</div>

</body>
</html>
<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.editproducttype.js"/>">


</script>

]]>
</content>


<content tag="jscript">
<![CDATA[

<script type="text/javascript">

function disableFields()
{ document.getElementById("venName").value="";
      
           document.getElementById("add_car_note_btn").disabled=true;
       document.getElementById("safetyInvAmt").disabled=true;
        document.getElementById("safetyInvAmtTyp").disabled=true;
         	document.getElementById("vndrServLvl").disabled=true;
    		//document.getElementById("invoiceMethod").disabled=true;
           	document.getElementById("checkbox").disabled=true;
           	<%if(request.getAttribute("mode")!=null &&( request.getAttribute("mode").equals("edit") || request.getAttribute("mode").equals("verify")) && !(request.getParameter("param")!=null && request.getParameter("param").equals("param"))){%>
            document.getElementById("save_edit").disabled=true;
           
            document.getElementById("overrideDays").disabled=true;
            document.getElementById("statusCd").disabled=true;
            document.getElementsByName("Remove").disabled=true;
            <%};%>

		   for(i=0; i<document.forms[0].elements.length; i++){
		    // alert(document.forms[0].elements[i].name);
			   if(document.forms[0].elements[i].name==='Remove' ){
			 	//  alert(document.forms[0].elements[i].name);
			 	  document.forms[0].elements[i].disabled=true;
			   }
			   if(document.forms[0].elements[i].name==='invoiceMethod' ){
			 	//  alert(document.forms[0].elements[i].name);
			 	  document.forms[0].elements[i].disabled=true;
			   }
		   }

   
             
           
 }
function Minimum(obj,min){
 if (obj.value.length<min) alert("Invalid zip code.  Please try again.");
}

function disab(){
if(document.getElementById("checkbox").checked){

document.getElementById("override").style.display = 'block';
}
else{
document.getElementById("override").style.display = 'none';
}
}
function setValue(box,mode){
var id= box.id;
//alert(id+mode);
document.getElementById("addressIdForRemove").value=id;
document.getElementById("modeForRemove").value=mode;
}
function verifyCancel()
      {
            var stay=confirm("Are you sure you want to cancel?");
            if (stay){
            	document.fsVendorForm.action="../oma/AddVendor.html?cancel=cancel";
            	document.fsVendorForm.submit();
            }
            else
             return false;
      }

$(document).ready(function(){
	
		// Add Note functionality
		var setupNotes=function(btnSel,winId,contentId,notesULSel,btnTxt,msgSel,msg){
			
			var win=null;
			$(btnSel).click(function(){
				// show add car note window when add note button clicked
				$(this).blur();
				if (!win) {
					win = new Ext.Window({
						el:winId,
						layout:'fit',
						width:350,
						autoHeight:true,
						closeAction:'hide',
						modal:true,
						plain:true,
						title:'Add Address',
						items: new Ext.Panel({
		                    contentEl:contentId,
		                    deferredRender:false,
		                    border:false,
							autoHeight:true
		                }),
						buttons: [{
				                text: 'Cancel',
				                handler: function(){
				                    win.hide();
				                }
			            	},{
				                text: btnTxt,
				                handler: function(){
									// add note using ajax submit
									$('#'+contentId+' form').ajaxSubmit({
									
										
										dataType:'json',
										success: function(resp){
											if(resp.success){
												
												win.hide();
												window.location.reload(true);
												$(msgSel).html(msg).fadeIn(function(){
													var $this=$(this);
													setTimeout(function(){
														$this.fadeOut("slow");
													},3000);
												});
												
											}
											else{
												Ext.MessageBox.show({
										           title: 'Error',
										           msg: 'Oops!. There was an error, please try again.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-error'
										       	});
											}
										},
										beforeSubmit:function(){
											// validation before submitting
											var $form=$('#'+contentId+' form');
											$('input.error',$form).removeClass('error'); //clear existing errors
											var missing=false;
											$('input.text',$form).each(function(){
												var $this=$(this);
												if($this.val()===''){
													missing=true;
													$this.addClass('error');
												}
											});
											
											if(missing){
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter all the required fields to add.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-warning'
										       	});
												return false;
											}
											
											return true;
										},
										url:$('#'+contentId+' form').attr('action')+'?ajax=true'
									});
				                }
							}
						]
					});
				}
		        win.show(function(){
					$('#add_car_note_form textarea.note_text').focus();
				});
				return false;
			});
		};
		//car notes
		setupNotes('#add_car_note_btn','add_car_note_win','add_car_note_form','ul.car_notes','Add Address','#add_note_msg','CAR Note added successfully!');
		
});
function validateZIP(field) {
var valid = "0123456789";
var hyphencount = 0;


for (var i=0; i < field.length; i++) {
temp = "" + field.substring(i, i+1);
if (temp == "-") hyphencount++;
if (valid.indexOf(temp) == "-1") {
alert("Invalid characters in your zip code.  Please try again.");
return false;
}
if ((hyphencount > 1) || ((field.length==10) && ""+field.charAt(5)!="-")) {
alert("The hyphen character should be used with a properly formatted 5 digit+four zip code, like '12345-6789'.   Please try again.");
return false;
   }
}
return true;
}

</script>
]]>
</content>

