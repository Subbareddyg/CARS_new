<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript"><!--


function showBelkLocation(box){
	//alert(box.value);
	if(box.value=="2"){
		document.getElementById("belkLocation").style.display="block";
	}
	else if(box.value == "3"){
		//alert('box.value========='+box.value);
			document.getElementById("belkLocation").style.display="none";
			document.getElementById("selectLoc").value="";
			document.getElementById("locationName").value='<c:out value="${sessionScope.belkAddress.locName}" />';
			document.getElementById("addressLine1").value='<c:out value="${sessionScope.belkAddress.addr1}" />';
			document.getElementById("addressLine2").value="";
			document.getElementById("city").value='<c:out value="${sessionScope.belkAddress.city}" />';
			document.getElementById("stateCd").value='<c:out value="${sessionScope.belkAddress.state}" />';
			document.getElementById("zip").value='<c:out value="${sessionScope.belkAddress.zip}" />';
			document.getElementById("locationName").readOnly = true;
			document.getElementById("addressLine1").readOnly = true;
			document.getElementById("addressLine2").readOnly = true;
			document.getElementById("city").readOnly = true;
			document.getElementById("stateCd").disabled= true;
			document.getElementById("stateCdHidden").value='<c:out value="${sessionScope.belkAddress.state}" />';
			document.getElementById("zip").readOnly = true;
	}
	else{
		//alert('box.value='+box.value);
		document.getElementById("belkLocation").style.display="none"; 
		document.getElementById("selectLoc").value="";
		document.getElementById("locationName").value='';
		document.getElementById("addressLine1").value='';
		document.getElementById("addressLine2").value="";
		document.getElementById("city").value='';
		document.getElementById("stateCd").value='<c:out value="${sessionScope.states[0].stateCd}" />';
		document.getElementById("zip").value='';
		document.getElementById("locationName").readOnly=false;
		document.getElementById("addressLine1").readOnly=false;
		document.getElementById("addressLine2").readOnly=false;
		document.getElementById("city").readOnly=false;
		document.getElementById("stateCd").disabled=false;
		document.getElementById("zip").readOnly=false;
	}
	
}
function verifyCancel(form)
{
                var stay=confirm("Are you sure you want to cancel?");
                if (stay){
             
                form.action="../oma/fulfillmentVendorReturns.html?cancel=cancel";
                form.submit();
                }
                 else
    			 return false;
               
}
function changeLoc(box){
	//alert('inside change loc box.value='+box.value);
	if(box.value=="2"){
		document.getElementById("locationName").value='<c:out value="${sessionScope.belkAddress.locName}" />';
		document.getElementById("addressLine1").value='<c:out value="${sessionScope.belkAddress.addr1}" />';
		document.getElementById("addressLine2").value="";
		document.getElementById("city").value='<c:out value="${sessionScope.belkAddress.city}" />';
		document.getElementById("stateCd").value='<c:out value="${sessionScope.belkAddress.state}" />';
		document.getElementById("zip").value='<c:out value="${sessionScope.belkAddress.zip}" />';
		document.getElementById("locationName").readOnly = true;
		document.getElementById("addressLine1").readOnly = true;
		document.getElementById("addressLine2").readOnly = true;
		document.getElementById("city").readOnly = true;
		document.getElementById("stateCd").disabled= true;
		document.getElementById("stateCdHidden").value='<c:out value="${sessionScope.belkAddress.state}" />';
		document.getElementById("zip").readOnly = true;
			
	}
	else{
		document.getElementById("locationName").value="";
		document.getElementById("addressLine1").value="";
		document.getElementById("addressLine2").value="";
		document.getElementById("city").value="";
		
		document.getElementById("stateCd").value='<c:out value="${sessionScope.states[0].stateCd}" />';
		document.getElementById("zip").value="";
		document.getElementById("locationName").readOnly=false;
		document.getElementById("addressLine1").readOnly=false;
		document.getElementById("addressLine2").readOnly=false;
		document.getElementById("city").readOnly=false;
		document.getElementById("stateCd").disabled=false;
		document.getElementById("zip").readOnly=false;
	}
}
function disableAddress(){
	var returnMeth=document.getElementById("returnMethod").value;
	var loc=document.getElementById("selectLoc").value;
	//alert(returnMeth+'returnMeth');
	if(returnMeth=="3" || (returnMeth=="2" && loc=="2")){
		//alert('Inside if');
		document.getElementById("locationName").readOnly = true;
		document.getElementById("addressLine1").readOnly = true;
		document.getElementById("addressLine2").readOnly = true;
		document.getElementById("city").readOnly = true;
		document.getElementById("stateCd").disabled= true;
		document.getElementById("stateCdHidden").value='<c:out value="${sessionScope.belkAddress.state}" />';
		document.getElementById("zip").readOnly = true;
	}
	
}
function formfocus() {
	if(document.forms.length > 0)
	{
	var formElements = ["text", "checkbox", "radio", "select-one", "select-multiple", "textarea"];
	var form = document.forms[document.forms.length-1];
	for (var j = 0; j < form.elements.length; j++)
	{
	var field = form.elements[j];
	for(var x = 0; x < formElements.length; x++)
	{
	if (field.getAttribute("type") == formElements[x])
	{
	field.focus();
	return false;
	}
	}
	}
	}
	}


function disableRole(){
	var role='<c:out value="${requestScope.displayRole}" />';
	
	if(role == 'user'){
		//Following code might be required when any particular role is to be set.
		/*document.getElementById("locationName").disabled="true";
		document.getElementById("addressLine1").disabled="true";
		document.getElementById("addressLine2").disabled="true";
		document.getElementById("city").disabled="true";
		document.getElementById("stateCd").disabled="true";
		document.getElementById("zip").disabled="true";
		document.getElementById("belkLocation").disabled="true";
		document.getElementById("selectLoc").disabled="true";	
		document.getElementById("rma").disabled="true";
		document.getElementById("returnMethod").disabled="true";
		document.getElementById("save").disabled="true";
		document.getElementById("cancel").disabled="true";*/
		for(i=0; i<document.forms[0].elements.length; i++){
			//	alert(document.vendorReturnForm.elements[i].type);
				if(document.forms[0].elements[i].type==='text' || document.forms[0].elements[i].type==='select-one' || document.forms[0].elements[i].type==='button' || document.forms[0].elements[i].type==='submit'){
				
					document.forms[0].elements[i].disabled=true;
				}
			
		}
		}
	
}

      
--></script>

<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="headerTabsForVendor.jsp"%>
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="headerTab.jsp"%>
</c:if>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="breadcrumbForVendorReturns.jsp"%>
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="breadcrumbForFSReturns.jsp"%>
</c:if>
<head>
    <c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
    <title>Fulfillment Service Returns</title>
    </c:if>
    <c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
     <title>Vendor Returns</title>
    </c:if>
    <c:if test="${pageContext.request.remoteUser != null}">
    	<c:set var="headingUser">
      	 <authz:authentication operation="fullName"/>
       </c:set> 
	</c:if>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>"/>
    <meta name="menu" content="MainMenu"/>
    
         
        
</head>
<body class="admin" >
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
	 document.getElementById("tab10").className="activeTab";
	 disableRole();
	 disableAddress();
	 setTimeout(function() { document.getElementById('rma').focus(); }, 10);
	}
        

</script>
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">

<%@ include file="omaInfoSection.jsp"%>
	
</div>
 <div class="cars_panel x-hidden" style="margin-top:10px;">
<div class="x-panel-header">
	Edit Returns
</div>
	<div class="x-panel-body">
	<c:if test="${empty sessionScope.fulfillmentService.fulfillmentServiceID}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="Please select fulfillment service." escapeXml="false"/><br />
		</c:if>
	<spring:bind path="vendorReturnForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<c:if test="${sessionScope.showMsg=='yes'}">
 <span style="background:#FFFF00;"> <c:out value="Saved Successfully!!" escapeXml="false"/> </span><br/>
 <% session.setAttribute("showMsg","no"); %>
</c:if>
<div id="attr_container">
<c:url value="/oma/fulfillmentVendorReturns.html" var="formAction"/>
<form:form commandName="vendorReturnForm" method="post" action="${formAction}"  id="vendorReturnForm" name="vendorReturnForm">

<fieldset>
			<form:hidden path="venId" />
			<form:hidden path="fsId" />
			<form:hidden path="returnId" />
			<form:hidden path="addrId" />
			<form:hidden path="createdBy" />
			<ul>
			<li>
		<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		</li>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="fsVendor.rma"/>
					</label>
					<form:input path="dropShipRma" id="rma" cssClass="text"
						cssErrorClass="text medium error" maxlength="12" />					
				</li>
				<li>
				<label for="txt_attr_name">						
						<fmt:message key="fsVendor.return_method"/><span class="req" style="color:#FF0000">*</span>
					</label>
				<c:set var="ReturnMethodList" value="${returnMethods}" scope="session"/>                 
                             <form:select id="returnMethod" path="returnMethod" onchange="showBelkLocation(this)" cssStyle="width:35%" cssErrorClass="text medium error" cssClass="input">
                                         
                                          <c:forEach items="${ReturnMethodList}" var="rtnMeth" >
                                             <c:if test="${rtnMeth.rtnMethName!='Other'}">
                                               <form:option value="${rtnMeth.rtnMethCode}"><c:out value="${rtnMeth.rtnMethName}"/></form:option>
                                             </c:if>
                                         </c:forEach>
                              </form:select>
				
					
						
				</li>
				<c:if test="${vendorReturnForm.returnMethod == '2'}">
				<li id="belkLocation" style="display:block">
				<label for="txt_attr_name">						
						Belk Location:<span class="req" style="color:#FF0000">*</span>
					</label>
					  <form:select  path="returnMethodType" onchange="changeLoc(this)" id="selectLoc"  cssErrorClass="text medium error" cssClass="input">
					  <form:option value="" ></form:option>
						<form:option value="2">Belk Fulfillment Center</form:option>
						<form:option value="4">Other</form:option>
					</form:select>
				</li>
				</c:if>
				<c:if test="${vendorReturnForm.returnMethod != '2'}">
				<li id="belkLocation"  style="display:none">
				<label for="txt_attr_name">						
						Belk Location:<span class="req" style="color:#FF0000">*</span>
					</label>
					  <form:select  path="returnMethodType" onchange="changeLoc(this)" id="selectLoc" cssErrorClass="text medium error" cssClass="input">
					  <form:option value="" ></form:option>
						<form:option value="2">Belk Fulfillment Center</form:option>
						<form:option value="4">Other</form:option>
					</form:select>
				</li>
				</c:if>
				<li>
					<label for="txt_attr_name">
						Default Return Address
					</label>
				</li>
				
				<li>
					<label for="txt_attr_name">
						<fmt:message key="fsVendor.addr.loc"/><span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="locName" id="locationName" cssClass="text"
						cssErrorClass="text medium error" maxlength="50" />						
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="fsVendor.addr.addr1"/><span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="addr1" id="addressLine1" cssClass="text"
						cssErrorClass="text medium error" maxlength="100" />		
										
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="fsVendor.addr.addr2"/>
					</label>
					<form:input path="addr2" id="addressLine2" cssClass="text"
						cssErrorClass="text medium error" maxlength="100" />		
										
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="fsVendor.addr.city"/><span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="city" id="city" cssClass="text"
						cssErrorClass="text medium error" maxlength="100" />	
										
				</li>	
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="fsVendor.addr.state"/><span class="req" style="color:#FF0000">*</span>
					</label>
					<c:set var="stateList" value="${states}" scope="session"/>                 
                             <form:select id="stateCd" path="state"  cssErrorClass="select medium error">
                                         
                                                <c:forEach items="${stateList}" var="state" >
                                               <form:option value="${state.stateCd}"><c:out value="${state.stateCd}"/></form:option>
                                         </c:forEach>
                              </form:select>	
                             
                              <input type="hidden" id="stateCdHidden" name="stateCdHidden"  />
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="fsVendor.addr.zip"/><span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="zip" id="zip" cssClass="text"
						cssErrorClass="text medium error" maxlength="5" size="5"/>	
										
				</li>			
			</ul>
				<br/>		
										
			<div class="buttons" style="padding-left:18.1%">
			<c:if test="${sessionScope.mode!='viewOnly'}">
				<input type="button" value="Cancel" id="cancel" name="cancel" class="btn cancel_btn" onClick="verifyCancel(this.form);" />
				<input type="submit" class="btn save_btn" id="save" name="save" value="<fmt:message key="button.save"/>" />
			</c:if>
			<c:if test="${sessionScope.mode=='viewOnly'}">
				
				<input type="button" onclick="none" disabled="disabled" value="Cancel"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="cancel" class="btn cancel_btn">
							
				<input type="button" onclick="none" disabled="disabled" value="<fmt:message key="button.save"/>"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="btn_save" class="btn">
			</c:if>
			</div>
		</fieldset>	
	   </form:form>

</div>
</div>
</div>
</body>
</html>
