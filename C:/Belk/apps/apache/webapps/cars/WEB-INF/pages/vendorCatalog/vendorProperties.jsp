<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTabForVendorCatalog.jsp" %>

<head>
    <title><fmt:message key="catalogvendors.page.title"/></title>
    <c:if test="${pageContext.request.remoteUser != null}">
        <c:set var="headingUser">
            <authz:authentication operation="fullName"/>
        </c:set>
    </c:if>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>
<script>
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
        var element=document.getElementById('isDisplayable');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
    }
    function setValue() {
        var val=document.getElementById("temp").value;
       if(val=='N') {
         document.getElementById("isDisplayable").options[1].selected=true;
       }

       var mode=document.getElementById("mode").value;
       if(mode=='readOnly') {
           document.getElementById("isDisplayable").disabled=true;
            document.getElementById("submitbtn").disabled=true;

       }
       
    }
      function cancel123() {
    	  var stay=confirm("Are you sure you want to cancel?");
     	  if (stay){
          	window.location ="catalogVendors.html?method=viewAllVendorCatalogs";
     	  } else{
  			return false;
  			}
        }
    
</script>
<body class="admin">
<!-- Added by Vikas for displaying bread crumb-->
	<div style="margin-left:5px;">
		<br/>
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
		> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  <c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  <c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorInfo.name}" default="isNull"/></a>
		> Vendor Properties
		<br>
		<br>
		<b>Vendor Properties </b>
		<br/>
		<br/>
	</div>
    <div id="search_for_attribute" class="cars_panel x-hidden">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Vendor Info </span></div>

        <div class="x-panel-body">
            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">

                <li class = "car_id" style="width:24%;">
                    <b>Vendor #:</b>
                    <c:out value="${vendorInfo.vendorNumber}" />
                </li>
                <li class = "car_id" style="width:22%;">
                    <b># of Catalog Styles:</b>
                    <c:out value="${vendorInfo.numStyle}" />
                </li>
                <li class = "car_id" style="width:22%;">
                    <b>Date Last Import:</b>
                    <c:out value="${vendorInfo.dateLastImport}" />
                </li>
            </ul>

            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
                <li class ="car_id" style="width:24%;">
                    <b>Vendor Name:</b>
                    <c:out value="${vendorInfo.name}" />
                </li>
                <li class ="car_id" style="width:22%;">
                    <b># of Catalog SKUs:</b>
                    <c:out value="${vendorInfo.numSKUs}" />
                </li>
                <li class ="car_id" style="width:25%;">
                    <b>Last Spreadsheet Imported :</b>
                    <c:out value="${vendorInfo.catalogName}" />

                </li>

            </ul>

        </div>
    </div>
    <br>

    <div id="search_for_attribute1" class="cars_panel x-hidden">
        <div class="x-panel-header"><span class="x-panel-header-text">
               Display Vendor Catalog </span></div>

        <div class="x-panel-body">
           
            <c:url value="catalogVendors.html?method=saveVendorProperties" var="formAction"/>
            <form:form  name ="VendorPropertiesForm" commandName="VendorPropertiesForm" method="post"
                        action="${formAction}" onsubmit="return onFormSubmit(this)"
                        id="VendorPropertiesForm">

                                        <spring:bind path="VendorPropertiesForm.vendorId">
                                         <input type="hidden" id="vendorId" name="vendorId" value="<c:out value="${VendorPropertiesForm.vendorId}" />"  />
                                        </spring:bind>
                                         <spring:bind path="VendorPropertiesForm.temp">
                                         <input type="hidden" id="temp" name="temp" value="<c:out value="${VendorPropertiesForm.temp}" />"  />
                                        </spring:bind>
                                         <spring:bind path="VendorPropertiesForm.mode">
                                         <input type="hidden" id="mode" name="mode" value="<c:out value="${VendorPropertiesForm.mode}" />"  />
                                        </spring:bind>
                  <table>
		<tr>
		<td class="first">
			<span class="req"><span class="req" style="color:#FF0000">*</span> Indicates Required Fields</span>
		</td>
		<tr>
			<td>&nbsp;</td>
		</tr>
		</tr>
		<tr>
			<td>
				<b>Display Vendor Catalogs:<span style="color:#FF0000" class="req">*</span></b>

                                <spring:bind path="VendorPropertiesForm.isDisplayable">
                                <select  id="isDisplayable" class="required" name="isDisplayable" >
                                    <option value="Y" >Yes</option>
                                    <option value="N">No</option>
                                </select>
                               </spring:bind>

			</td>		
		</tr>
	          </table>
                                         <br>
                                         <div id="ext-gen29" align="right" >
                                             <input type="button" value="Cancel" name="cancel" class="btn cancel_btn" onclick="javascript:cancel123();"/>
                        <input type="submit" id="submitbtn" value="Save" name="cancel" class="btn cancel_btn"  />
			
			</div>
                      
                        

            </form:form>
            
        </div>
    </div>
            <script>
               setValue();
            </script>