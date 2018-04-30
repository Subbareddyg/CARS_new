<%@ include file="/common/taglibs.jsp"%>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="headerTabsForVendor.jsp"%>
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="headerTab.jsp"%>
</c:if>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="breadcrumbForVndrShippingOptions.jsp"%>
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="breadcrumbForFSShippingOptions.jsp"%>
</c:if>
<SCRIPT LANGUAGE="JavaScript">
function setStatus(box){
	if(box.value == "on") {
		alert(box.value);
		box.value = 'Y';
	}	
}
function verifyCancel()
{
        var stay=confirm("Are you sure you want to cancel?");
        if (stay){
        
        document.vendorFSShippingOptionsForm.action="../oma/shippingOptions.html?cancel=cancel";
        document.vendorFSShippingOptionsForm.submit();
        }
         else
		return false;
               
}
function disableEnable(box){
	var size='<c:out value="${sessionScope.shippingOptionsSize}" />';
	//alert(size);
	if(!box.checked) {
	//	alert(box.value);
		var i=0;
		
		for (i=0;i<=size-1;i++)
		{
			//alert("shippingOptionsModel["+i+"].isAllowed");
			document.getElementById("shippingOptionsModel["+i+"].isAllowed").disabled=true;
			document.getElementById("shippingOptionsModel["+i+"].account").disabled=true;
			document.getElementById("shippingOptionsModel["+i+"].account").className="disable";
		}
		
	}
	else{
		var i=0;
		for (i=0;i<=size-1;i++)
		{
			//alert("shippingOptionsModel["+i+"].isAllowed");
			document.getElementById("shippingOptionsModel["+i+"].isAllowed").disabled=false;
			
		}
		disableAccountOnLoad();
	}
	
}

function disableAccount(box){
	var size='<c:out value="${sessionScope.shippingOptionsSize}" />';
	if(!box.checked) {
		var i=0;
		
		for (i=0;i<=size-1;i++)
		{		
			if("shippingOptionsModel["+i+"].isAllowed"==box.id){
				document.getElementById("shippingOptionsModel["+i+"].account").disabled=true;
				document.getElementById("shippingOptionsModel["+i+"].account").className="disable";
			}
		}
	}
	else{
		var i=0;
		for (i=0;i<=size-1;i++)
		{		
			if("shippingOptionsModel["+i+"].isAllowed"==box.id){
			//	alert(box.id);
				document.getElementById("shippingOptionsModel["+i+"].account").disabled=false;
				document.getElementById("shippingOptionsModel["+i+"].account").className="enable";
				
			}
		}
	}

}
function disableAccountOnLoad(){
	var size='<c:out value="${sessionScope.shippingOptionsSize}" />';
	var i=0;
		
		for (i=0;i<=size-1;i++)
		{		
			if(document.getElementById("shippingOptionsModel["+i+"].isAllowed").checked){
				document.getElementById("shippingOptionsModel["+i+"].account").disabled=false;
				document.getElementById("shippingOptionsModel["+i+"].account").className="enable";
				
			}
			else{
				document.getElementById("shippingOptionsModel["+i+"].account").disabled=true;
				document.getElementById("shippingOptionsModel["+i+"].account").className="disable";
			}
			
		}
	
}
function disableRole(){
	var role='<c:out value="${requestScope.displayRole}" />';
	
	if(role == 'user'){
		var size='<c:out value="${sessionScope.shippingOptionsSize}" />';
		var i=0;
			
			for (i=0;i<=size-1;i++)
			{		
					document.getElementById("shippingOptionsModel["+i+"].isAllowed").disabled=true
					document.getElementById("shippingOptionsModel["+i+"].account").disabled=true;
					document.getElementById("shippingOptionsModel["+i+"].account").className="disable";
					document.getElementById("allowBelk").disabled=true;
					document.getElementById("allowVendor").disabled=true;
					document.getElementById("save_edit").disabled=true;
					document.getElementById("cancel").disabled=true;
					
			}
		}
}
</SCRIPT>
<head>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
    <title>Fulfillment Service Shipping Options</title>
    </c:if>
    <c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
     <title>Vendor Shipping Options</title>
    </c:if>
    <c:if test="${pageContext.request.remoteUser != null}">
    	<c:set var="headingUser">
      	 <authz:authentication operation="fullName"/>
       </c:set> 
	</c:if>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>"/>
    <meta name="menu" content="MainMenu"/>
    
     <style>
     	input.disable{
     	 background-color:LightGrey;
     	 
     	}
     	input.enable{
     	 background-color:white;
     	}
     </style>    
        
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
	
	function init() {
		 document.getElementById("tab6").className="activeTab";
		 disableEnable(document.getElementById("allowBelk"));
		 disableAccountOnLoad();
		 disableRole();
		}   

</script>
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">

<%@ include file="omaInfoSection.jsp"%>
</div>
<div class="cars_panel x-hidden" style="margin-top:10px;">
<div class="x-panel-header">
	Edit Shipping Options
</div>
		<div class="x-panel-body">
		<c:if test="${empty sessionScope.fulfillmentService.fulfillmentServiceID}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="Please select fulfillment service." escapeXml="false"/><br />
		</c:if>
		<spring:bind path="vendorFSShippingOptionsForm.*">
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
<br>
<c:if test="${sessionScope.showMsg=='yes'}">
 <span style="background:#FFFF00;"> <c:out value=" Saved Successfully!!" escapeXml="false"/> </span><br/>
 <%session.setAttribute("showMsg","no"); %>
</c:if>
<br>
<div id="attr_container">
<c:url value="/oma/shippingOptions.html" var="formAction"/>
<form:form commandName="vendorFSShippingOptionsForm" method="post" action="${formAction}"   id="v" name="vendorFSShippingOptionsForm">
<form:checkbox path="directBelk" value="Y" id="allowBelk" onclick="disableEnable(this);" />	<b>Allow Belk Direct Bill Of Shipping</b>
<c:set var="counter" value="-1"></c:set>


			<display:table name="${vendorFSShippingOptionsForm.shippingOptionsModel}" cellspacing="0" cellpadding="0"  style="width:65%" 
						    id="addr" class="dstable" >
						     <c:set var="counter" value="${counter+1}"></c:set>
						 
						     <c:forEach var="shipping" items="${sessionScope.shippingOptionsModelList}" varStatus="loop">
     							     <c:if test="${loop.count  == counter+1 }">
       								 <c:set var="shipping1" value="${shipping}"/>
         							 </c:if>
         						</c:forEach>
						      <display:column  sortable="false" titleKey="Carrier" style="width: 10%">
					    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].carrierName">
					    		<input type="text" name="<c:out value="${status.expression}"/>" readonly="readonly"  Style="border:none;font-size:100%" value="<c:out value="${status.value}"/>"
    								id="<c:out value="${status.expression}"/>"  class="stylestext"  />
					    	</spring:bind>
					    	
		    				</display:column>
		    				<display:column  sortable="false" titleKey="Class" style="width: 28%">
					    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].carrierClass">
					    		<input type="text" name="<c:out value="${status.expression}"/>" readonly="readonly"  Style="border:none;font-size:100%" value="<c:out value="${status.value}"/>"
    								id="<c:out value="${status.expression}"/>"  class="stylestext" />
					    	</spring:bind>
					    	
					    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].shippingOptionId">
  				 <input type="hidden" style="display:none;" name="<c:out value="${status.expression}"/>"
   				 id="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
				</spring:bind>
		    				</display:column>
						  
						
						    <c:choose>
						    
						    <c:when test="${shipping1.isAllowed=='Y'}" >
						    <display:column  sortable="false" titleKey="Allowed" style="width: 10%">
					    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].isAllowed">
					    		<input type="checkbox" checked="checked" name="<c:out value="${status.expression}"/>"
			    					id="<c:out value="${status.expression}"/>" value="Y" onclick="setStatus(this);disableAccount(this);"/>
					    	</spring:bind>
					    	
		    </display:column>
		    </c:when>
		    <c:otherwise>
		    <display:column  sortable="false" titleKey="Allowed" style="width: 10%">
					    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].isAllowed">
					    		<input type="checkbox"  name="<c:out value="${status.expression}"/>"
			    					id="<c:out value="${status.expression}"/>" value="Y" onclick="setStatus(this);disableAccount(this);"/>
					    	</spring:bind>
					    	
		    </display:column>
		    
		    </c:otherwise>
		    </c:choose>
		     <display:column  sortable="false" titleKey="Direct Bill Account Number" style="width: 40%" >
		    	<spring:bind path="vendorFSShippingOptionsForm.shippingOptionsModel[${counter}].account">
		    		<input MAXLENGTH="20" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
    					id="<c:out value="${status.expression}"/>" style="width:95%;font-size:100%" class="stylestext" />
		    	</spring:bind>
		    	
		   	
		    </display:column>
		    		   
						</display:table>
						<br>
<form:checkbox  path="directVendor" id="allowVendor" value="Y" />	<b>Allow vendors to bill shipping back to Belk on Invoice</b>
<br>
<br>

<c:if test="${sessionScope.mode!='viewOnly'}">
	<input type="button" value="Cancel" id="cancel" name="cancel" class="btn cancel_btn" onClick="verifyCancel();" />
	<input type="submit" class="btn save_btn" id="save_edit" name="save" value="<fmt:message key="button.save"/>" />
</c:if>
<c:if test="${sessionScope.mode=='viewOnly'}">
		<input type="button" onclick="none" disabled="disabled" value="Cancel"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="cancel" class="btn cancel_btn">
							
		<input type="button" onclick="none" disabled="disabled" value="<fmt:message key="button.save"/>"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="save_edit" class="btn save_btn">
	</c:if>
</form:form>

</div>
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
</content>
