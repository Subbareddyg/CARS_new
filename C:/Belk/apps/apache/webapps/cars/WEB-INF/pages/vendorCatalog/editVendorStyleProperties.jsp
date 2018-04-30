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

function verifyCancel()
{
var stay=confirm("Are you sure you want to cancel?");
if (stay){
window.location="../vendorCatalog/catalogVendors.html?method=viewAllVendorStyles";
}
	else{
		return false;
	}
}
function editUPCDetails(vendorUpc,vendorStyleId,vendorCatalogId,index,recordNum,catalogTemplateId) {

       var obj= document.getElementById("colorHeaderNbr").value;
       var oldVendorUpc= document.getElementById("oldVendorUpc").value;
       if(obj!=null&& obj!='' && vendorUpc!=oldVendorUpc){
           var answer = confirm("The Unsaved Data will be lost.To save data click on cancel and save to continue ckick on OK button.")
           if ( answer) {
           } else {
               return;
           }
        } else if(vendorUpc==oldVendorUpc) {
            return;
        }
        window.location ="catalogVendors.html?method=editUPCDetails&vendorUpc="+vendorUpc+"&vendorStyleId=" + vendorStyleId +"&vendorCatalogID=" + vendorCatalogId +"&index="+ index + "&recordNum="+recordNum+"&catalogTemplateId="+catalogTemplateId;
    }
    function saveStyleProperties() {
        if(document.getElementById("vendorUpc123") !=null) {
        var newVendorUpc = document.getElementById("vendorUpc123").value;
        var newColor= document.getElementById("color123").value;
        document.editVendorStylePropertiesForm.vendorUpc.value =newVendorUpc;
        document.editVendorStylePropertiesForm.color.value =newColor;
        }
        document.forms[0].action ="catalogVendors.html?method=saveStyleProperties";
        document.forms[0].submit();
    }
    function removeImage(imageId) {
        document.forms[0].action ="catalogVendors.html?method=removeStyleSkuImages&imageId="+imageId;
        document.forms[0].submit();
    }

    function PopupCenter(pageURL, title,w,h,vendorCatalogId,vendor) {

                        var left = (screen.width/2)-(w/2);

                        var top = (screen.height/2)-(h/2);
                        alert(pageURL);

                        var targetWin = window.open(pageURL, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=400, height=300, top='+top+', left='+left);

                  }

     function addImage(vendorCatalogId,vendorStyleId,vendorUpc) {
                        var temp='vendorCatalogId='+vendorCatalogId+'&vendorStyleId='+vendorStyleId+'&vendorUpc='+vendorUpc;
                        var pageURL="../vendorCatalog/fileUploadPopup.html?"+temp;
                        var left = (screen.width/2)-200;
                        
                        var top = (screen.height/2)-200;
                        var targetWin = window.open(pageURL, 'AddImage', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+510+', height='+240+', top='+top+', left='+left);
                       
                      

     }
      function cancel123() {
        window.location ="catalogVendors.html?method=viewAllVendorStyles";
       }
    
      function init(){
     	document.getElementById("tab2").className="activeTab";
     }

</script>
<body class="admin">
<!-- Added by Vikas for displaying bread crumb-->
  <c:url value="catalogVendors.html?method=saveStyleProperties" var="formAction"/>
 <form:form  name ="editVendorStylePropertiesForm" commandName="editVendorStylePropertiesForm" method="post"
		action="${formAction}"
		id="editVendorStylePropertiesForm" onsubmit="return onFormSubmit(this)">

                                        <spring:bind path="editVendorStylePropertiesForm.vendorStyleId">
                                         <input type="hidden" id="vendorStyleId" name="vendorStyleId" value="<c:out value="${editVendorStylePropertiesForm.vendorStyleId}" />"  />
                                        </spring:bind>

                                          <spring:bind path="editVendorStylePropertiesForm.vendorCatalogId">
                                         <input type="hidden" id="vendorCatalogId" name="vendorCatalogId" value="<c:out value="${editVendorStylePropertiesForm.vendorCatalogId}" />" />
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.recordNum">
                                         <input type="hidden" id="recordNum" name="recordNum" value="<c:out value="${editVendorStylePropertiesForm.recordNum}" />" />
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.mappedFieldSeq">
                                         <input type="hidden" id="mappedFieldSeq" name="mappedFieldSeq" value="<c:out value="${editVendorStylePropertiesForm.mappedFieldSeq}" />"/>
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.unMappedFieldSeq">
                                         <input type="hidden" id="unMappedFieldSeq" name="unMappedFieldSeq" value="<c:out value="${editVendorStylePropertiesForm.unMappedFieldSeq}" />"/>
                                        </spring:bind>
                                           <spring:bind path="editVendorStylePropertiesForm.styleVendorUpc">
                                         <input type="hidden" id="styleVendorUpc" name="styleVendorUpc" value="<c:out value="${editVendorStylePropertiesForm.styleVendorUpc}" />"/>
                                        </spring:bind>
                                          <spring:bind path="editVendorStylePropertiesForm.catalogTemplateId">
                                         <input type="hidden" id="catalogTemplateId" name="catalogTemplateId" value="<c:out value="${editVendorStylePropertiesForm.catalogTemplateId}" />"/>
                                        </spring:bind>
                                         <!-- Start UPC level fields-->
                                         <spring:bind path="editVendorStylePropertiesForm.upcRecordNum">
                                         <input type="hidden" id="upcRecordNum" name="upcRecordNum" value="<c:out value="${editVendorStylePropertiesForm.upcRecordNum}" />" />
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.upcCatalogId">
                                         <input type="hidden" id="upcCatalogId" name="upcCatalogId" value="<c:out value="${editVendorStylePropertiesForm.upcCatalogId}" />"/>
                                        </spring:bind>
                                        <spring:bind path="editVendorStylePropertiesForm.upcCatalogTemplateId">
                                         <input type="hidden" id="upcCatalogTemplateId" name="upcCatalogTemplateId" value="<c:out value="${editVendorStylePropertiesForm.upcCatalogTemplateId}" />"/>
                                        </spring:bind>

                                        <spring:bind path="editVendorStylePropertiesForm.oldColor">
                                         <input type="hidden" id="oldColor" name="oldColor" value="<c:out value="${editVendorStylePropertiesForm.oldColor}" />"/>
                                        </spring:bind>
                                           <spring:bind path="editVendorStylePropertiesForm.vendorUpcHeadderNbr">
                                         <input type="hidden" id="vendorUpcHeadderNbr" name="vendorUpcHeadderNbr" value="<c:out value="${editVendorStylePropertiesForm.vendorUpcHeadderNbr}" />"/>
                                        </spring:bind>
                                        <spring:bind path="editVendorStylePropertiesForm.colorHeaderNbr">
                                         <input type="hidden" id="colorHeaderNbr" name="colorHeaderNbr" value="<c:out value="${editVendorStylePropertiesForm.colorHeaderNbr}" />"/>
                                        </spring:bind>
                                           <spring:bind path="editVendorStylePropertiesForm.oldVendorUpc">
                                         <input type="hidden" id="oldVendorUpc" name="oldVendorUpc" value="<c:out value="${editVendorStylePropertiesForm.oldVendorUpc}"/>"/>
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.vendorUpc">
                                         <input type="hidden" id="vendorUpc" name="vendorUpc" value="<c:out value="${editVendorStylePropertiesForm.colorHeaderNbr}" />"/>
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.color">
                                         <input type="hidden" id="color" name="color" value="<c:out value="${editVendorStylePropertiesForm.color}" />"/>
                                        </spring:bind>


                                         <spring:bind path="editVendorStylePropertiesForm.updateDescription">
                                         <input type="hidden" id="updateDescription" name="updateDescription" value="<c:out value="${editVendorStylePropertiesForm.updateDescription}" />"/>
                                        </spring:bind>
                                         <spring:bind path="editVendorStylePropertiesForm.updateColor">
                                         <input type="hidden" id="updateColor" name="updateColor" value="<c:out value="${editVendorStylePropertiesForm.updateColor}" />"/>
                                        </spring:bind>
                                          <spring:bind path="editVendorStylePropertiesForm.descriptionFieldNo">
                                         <input type="hidden" id="descriptionFieldNo" name="descriptionFieldNo" value="<c:out value="${editVendorStylePropertiesForm.descriptionFieldNo}" />"/>
                                        </spring:bind>
                                          <spring:bind path="editVendorStylePropertiesForm.colorFieldNo">
                                         <input type="hidden" id="colorFieldNo" name="colorFieldNo" value="<c:out value="${editVendorStylePropertiesForm.colorFieldNo}"/>"/>
                                        </spring:bind>


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

             <spring:bind path="editVendorStylePropertiesForm.*">
	    <c:if test="${not empty errors}">
		<div class="error">
			<c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
                <br>
	   </c:if>
           </spring:bind>

          <table width="100%">
	<tr><td vAlign="top" width="45%">
	<div >
	<b>Mapped CARS Attributes</b>
	<table style="border: 1px solid #D0D0D0" width="100%">
         <c:forEach var="mappedAttribute" items="${mappedAttributeList}">
		<tr><td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${mappedAttribute.attributeName}" /></td>
                                    <td style="padding: 3px;border: 1px solid #D0D0D0;">
                                        <spring:bind path="editVendorStylePropertiesForm.mappedFields">
                                         <input type="text" id="mappedFields" name="mappedFields" value="<c:out value="${mappedAttribute.attributeValue}" />"cssClass="text"
							cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                                        </spring:bind>

                                       </td>
                </tr>
	</c:forEach>

	
	</table></div></td>
	<td width="45%">
	<div >
	<b>Unmapped Fields</b><BR/>
	<table style="border: 1px solid #D0D0D0" align="center" width="100%">
            <c:forEach var="unMappedAttribute" items="${unMappedAttributeList}">
		<tr><td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;"><c:out value="${unMappedAttribute.attributeName}" /></td>
                                    <td style="padding: 3px;border: 1px solid #D0D0D0;">
                                    <spring:bind path="editVendorStylePropertiesForm.unmappedFields">
                                         <input type="text" id="unmappedFields" name="unmappedFields" value="<c:out value="${unMappedAttribute.attributeValue}" />"cssClass="text"
							cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                                        </spring:bind>

                                    </td>
                </tr>
	</c:forEach>
	</table></div></td>
	</tr></table>

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
					<img src="<c:out value="${imageAppURL}${styleImage.imageName}" />" width="65px" height="75px"/>
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
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 15%;" align="center">
                                        <secureurl:secureAnchor name="removeStyleSkuImages" cssStyle="btn" arguments="${styleImage.imageId}"  title="Remove" hideUnaccessibleLinks="true"/>
                                        
				</td>
				</tr>
                               </c:forEach>


			</table>
                <br>        
                        <secureurl:secureAnchor name="addStyleSkuImages" cssStyle="btn" arguments="'${editVendorStylePropertiesForm.vendorCatalogId}','${editVendorStylePropertiesForm.vendorStyleId}','${editVendorStylePropertiesForm.styleVendorUpc}'"  title="Add" hideUnaccessibleLinks="true"/>
             

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
                <secureurl:secureAnchor name="editVendorUPCDetails" title="${vendorUPC.vendorUPC}"  cssStyle="removeVendor" arguments="'${vendorUPC.vendorUPC}','${vendorUPC.vendorStyleId}',${vendorUPC.vendorCatalogId},${vendorUPC.indx},${vendorUPC.recordNum},${vendorUPC.catalogTemplateId}"  hideUnaccessibleLinks="true"/>
                </display:column>
                <display:column property="description" sortable="false" titleKey="vendorstyleproperties.description" style="width:20%; white-space:normal"/>   
                <display:column  property="status" sortable="false" titleKey="vendorstyleproperties.status" style="width: 10%"/>
                <display:column property="catalogName" sortable="false" titleKey="vendorstyleproperties.catalog" style="width: 15%"/>
                <display:column property="updatedDate" sortable="false" titleKey="vendorstyle.lastUpdated" style="width: 8%"/>
                <display:column property="updatedBy" sortable="false" titleKey="vendorstyle.lastUpdatedBy" style="width: 12%"/>
                 <display:column property="updatedBy" sortable="false" titleKey="vendorstyle.lastUpdatedBy" style="width: 12%"/>
                </display:table>
            <br>
            <c:if test ="${upcDetails != null}" >
            <div style="height: 177px;">
                 <b>Mapped CARS Attributes</b>
                <table style="border: 1px solid #D0D0D0" width="100%">
		<tr>
                    <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;">Vendor UPC</td>
                     <td style="padding: 3px;border: 1px solid #D0D0D0;">
                     <input type="text" id="vendorUpc123" name="tempVendorUpc" value="<c:out value="${editVendorStylePropertiesForm.vendorUpc}" />" />
                </tr>
                <tr>
                    <td style="background-color:#f6f6f6;padding: 3px;border: 1px solid #D0D0D0;">Color</td>
                     <td style="padding: 3px;border: 1px solid #D0D0D0;">

                     <input type="text" id="color123" name="tempColor" value="<c:out value="${editVendorStylePropertiesForm.color}" />"cssClass="text"
							cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />

                     </td>
                </tr>

                </table></div>

                                        
                                         
           

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
				    Date created
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
				<c:out value="${skuImage.createdDate}" />
				</td>
				<td style="padding: 3px;border: 1px solid #D0D0D0; width: 15%;" align="center">

                                        <secureurl:secureAnchor name="removeStyleSkuImages" cssStyle="btn" arguments="${skuImage.imageId}"  title="Remove" hideUnaccessibleLinks="true"/>
				</td>
				</tr>
                               </c:forEach>


			</table>
                       <secureurl:secureAnchor name="addStyleSkuImages" cssStyle="btn" arguments="'${editVendorStylePropertiesForm.upcCatalogId}','${editVendorStylePropertiesForm.vendorStyleId}','${editVendorStylePropertiesForm.oldVendorUpc}'"  title="Add" hideUnaccessibleLinks="true"/>
                       <br>
                        </c:if>
           
            
            <div>
		          <br><ol>
		          		<li>
		                <input  type="button" value="Cancel" name="cancel" class="btn cancel_btn"  onclick="javascript:verifyCancel();" />
						
		                <input  type="button" value="Save" name="cancel" class="btn cancel_btn" onclick="javascript:saveStyleProperties();" />
		                </li>
		             </ol>
       		</div>
					
			
        </div>
                              


    </form:form>

        

</body>

