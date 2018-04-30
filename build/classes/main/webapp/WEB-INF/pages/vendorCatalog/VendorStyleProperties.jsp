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
    function viewUPCDetails(vendorUpc,vendorStyleId,vendorCatalogId,index) {
        window.location ="catalogVendors.html?method=getUPCDetails&vendorUpc="+vendorUpc+"&vendorStyleId=" + vendorStyleId +"&vendorCatalogID=" + vendorCatalogId +"&index="+ index;
       // document.forms[0].submit();
    }
     function cancel123() {
    	 var stay=confirm("Are you sure you want to cancel?");
    	 if (stay){
        	window.location ="catalogVendors.html?method=viewAllVendorStyles";
    	 } else{
    			return false;
    		}
        }
 function init(){
     	document.getElementById("tab2").className="activeTab";
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
		   <c:url value="/vendorCatalog/catalogVendors.html?method=viewAllVendorStyles" var="formAction2"/>
		> <a href='<c:out value="${formAction2}" escapeXml="false"/>'>Catalog Styles</a>
		> <c:out value="${vendorStyleInfo.vendorStyleId}"/>
		<br/>
		<br/>
<b>Vendor Style Properties </b>
<br/>
<br/>
</div>
    
    <div id="search_for_attribute" class="cars_panel x-panel">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Vendor Style Info </span></div>

        <div class="x-panel-body">
            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">

                <li class = "car_id" style="width:24%;">
                    <b>Vendor Style#:</b>
                    <c:out value="${vendorStyleInfo.vendorStyleId}"/>
                </li>
                <li class = "car_id" style="width:22%;">
                    <b>Vendor #:</b>
                    <c:out value="${vendorStyleInfo.vendorNumber}" />
                </li>
                <li class = "car_id" style="width:22%;">
                    <b>Date Last Import:</b>
                    <c:out value="${vendorStyleInfo.dateLastImport}" />
                  
                </li>
            </ul>

            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
                <li class ="car_id" style="width:24%;">
                    <b>Style Description:</b>
                    <c:out value="${vendorStyleInfo.description}" />
                </li>
                <li class ="car_id" style="width:22%;">
                    <b>Vendor Name:</b>
                    <c:out value="${vendorStyleInfo.vendorName}" />
                </li>
                <li class ="car_id" style="width:25%;">
                    <b>Last Catalog Imported :</b>
                    <c:out value="${vendorStyleInfo.lastCatalogImported}" />

                </li>

            </ul>

        </div>
    </div>
    <br>

    <div id="search_for_attribute1" class="cars_panel x-panel">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Style Attributes & Image(s) </span></div>

        <div class="x-panel-body">
    
    <table width="100%">
		<tr>
			<td width="45%">
				<div>
					<b>Mapped CARS Attributes</b>
						<table style="border: 1px solid #D0D0D0; white-space:normal" width="100%">
					         <c:forEach var="mappedAttribute" items="${mappedAttributeList}">
								<tr>
									<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0; white-space:normal"><c:out value="${mappedAttribute.attributeName}" /></td>
							        <td style="padding: 3px;border: 1px solid #D0D0D0; white-space:normal"><c:out value="${mappedAttribute.attributeValue}" /></td>
						        </tr>
							</c:forEach>
						</table>
				</div>
			</td>
			
			<td width="45%">
				<div>
					<b>Unmapped Fields</b><BR/>
					<table style="border: 1px solid #D0D0D0; white-space:normal" width="100%">
		            		<c:forEach var="unMappedAttribute" items="${unMappedAttributeList}">
								<tr>
									<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${unMappedAttribute.attributeName}" /></td>
		                    		<td style="padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${unMappedAttribute.attributeValue}" /></td>
		                		</tr>
							</c:forEach>
						</table>
				</div>
			</td>
		</tr>
	</table>

     <table  style="border: 1px solid #D0D0D0;width:100% ">
			<thead>
				<tr class="x-grid3-hd-row">
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;">
                    <b>Image</B>
				</td>
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 20%;" align="center">
				    <b>Type</b>
				</td>
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
                     <b>Status</b>
				</td>
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 20%;"align="center">
				    Filename
				</td>
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
				    Date Last updated
				</td>
				<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
				    Updated By
				</td>
                                <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
				    &nbsp;
				</td>
				</tr>
			</thead>

                               <c:forEach var="styleImage" items="${styleImageList}">

				<tr>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;">
					<img src="<c:out value="${imageAppURL}${styleImage.imageName}" />" width='50' height='50' " width="65px" height="75px"/>
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 20%;"align="center">
				<c:out value="${styleImage.imageType}" />
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
				<c:out value="${styleImage.status}" />
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 25%;" align="center">
				<c:out value="${styleImage.imageName}" />
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
				<c:out value="${styleImage.updatedDate}" />
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
				<c:out value="${styleImage.updatedBy}" />
				</td>
                                <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
                                     <input type="button" onclick="none" disabled="disabled" value="Remove"  name="cancel" style="font-size: 12px;
										color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 				class="btn cancel_btn"/>
				</td>
				</tr>
                               </c:forEach>


			</table>
            <br>
            <input type="button" onclick="none" disabled="disabled" value="Add"  name="cancel" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 class="btn cancel_btn"/>

        </div>
    </div>
    <br>



    <div id="user_list" class="cars_panel x-panel">

        <div class="x-panel-header"> SKUs / UPCs Attributes and Images

        </div>
       
        <div  class="x-panel-body">
        
            <display:table name="vendorUPCList" cellspacing="0" cellpadding="0" requestURI="/vendorCatalog/catalogVendors.html"
                           defaultsort="1" id="vendorUPC" pagesize="25" class="dstable" sort="list" >
                <display:column  sortable="false" titleKey="vendorstyleproperties.vendorupc" style="width:10%">
                <secureurl:secureAnchor name="viewVendorUPCDetails" title="${vendorUPC.vendorUPC}"  cssStyle="removeVendor" arguments="'${vendorUPC.vendorUPC}','${vendorUPC.vendorStyleId}','${vendorUPC.vendorCatalogId}',${vendorUPC.indx}"  hideUnaccessibleLinks="true"/>
                </display:column>
                <display:column property="description" sortable="false" titleKey="vendorstyleproperties.description" style="width:20%; white-space:normal"/>
                <display:column  property="status" sortable="false" titleKey="vendorstyleproperties.status" style="width: 10%"/>
                <display:column property="catalogName" sortable="false" titleKey="vendorstyleproperties.catalog" style="width: 15%"/>
                <display:column property="updatedDate" sortable="false" titleKey="vendorstyle.lastUpdated" style="width: 8%"/>
                <display:column property="updatedBy" sortable="false" titleKey="vendorstyle.lastUpdatedBy" style="width: 12%"/>
                </display:table>
            <br>
            <c:if test ="${upcDetails != null}" >
	            <div style="height: 177px;">
	                 <b>Mapped CARS Attributes</b>
	                <table style="border: 1px solid #D0D0D0" width="100%">
						<tr>
		                    <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;">Vendor UPC</td>
		                     <td style="padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${upcDetails.vendorUPC}" /></td>
		                </tr>
		                <tr>
		                    <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;">Color</td>
		                     <td style="padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${upcDetails.color}" /></td>
		                </tr>
	                </table>
	              </div>
                  <br>
	              <table  style="border: 1px solid #D0D0D0;width:100% ">
					<thead>
						<tr class="x-grid3-hd-row">
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;">
			                    <b>Image</B>
							</td>
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 20%;" align="center">
							    <b>Type</b>
							</td>
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
			                     <b>Status</b>
							</td>
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 20%;"align="center">
							    Filename
							</td>
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
							    Date Last updated
							</td>
							<td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
							   Updated By
							</td>
			                                <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
							    &nbsp;
							</td>
						</tr>
					</thead>
                    <c:forEach var="skuImage" items="${skuImageList}">
						<tr>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;">
								<img src="<c:out value="${imageAppURL}${skuImage.imageName}" />" width="65px" height="75px"/>
							</td>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 20%;"align="center">
								<c:out value="${skuImage.imageType}" />
							</td>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
								<c:out value="${skuImage.status}" />
							</td>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 25%;" align="center">
								<c:out value="${skuImage.imageName}" />
							</td>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
								<c:out value="${skuImage.updatedDate}" />
							</td>
							<td style="padding: 3px;border: 1px solid #D0D0D0; width: 10%;" align="center">
								<c:out value="${skuImage.updatedBy}" />
							</td>
			                <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;width: 10%;"align="center">
			                    <input type="button" onclick="none" disabled="disabled" value="Remove"  name="cancel" style="font-size: 12px;
										color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 				class="btn cancel_btn"/>
			                    
							</td>
						</tr>
                     </c:forEach>
                     	<tr></tr>
	        			    
				  </table>
		           <ol>
		           <li>
            				<input type="button" onclick="none" disabled="disabled" value="Add"  name="cancel" style="font-size: 12px;
										color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 				class="btn cancel_btn"/>
            			</li>
            		</ol>
              </c:if>
          <div>
          <ol>
          		<li>
                <input align="left" type="button" value="Cancel" name="cancel" class="btn cancel_btn"  onclick="javascript:cancel123();" />
				<input type="button" onclick="none" disabled="disabled" value="Save"  name="cancel" style="font-size: 12px;
										color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 				class="btn cancel_btn"/>
               
                </li>
             </ol>
         </div>
        </div>
         
    </div>
         

</body>

