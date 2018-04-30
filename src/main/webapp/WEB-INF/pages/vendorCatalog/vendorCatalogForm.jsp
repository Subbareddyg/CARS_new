<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerVendorCatalogTab.jsp" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
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

function setAndReset(box) {
	if(box.value == "Y") 
	{
		document.getElementById("address").style.display = 'block';
		document.getElementById("img_loc_desc").style.display = 'block';
	}
	else {
		
		document.getElementById("txt_ftp_url").value="";
		document.getElementById("txt_ftp_uname").value="";
		document.getElementById("txt_ftp_pwd").value="";
		document.getElementById("isAnonymousFTP").checked=false;
		document.getElementById("image_loc_choice").value='Select Option';
		
		
		document.getElementById("address").style.display = 'none';
		document.getElementById("ftpAddr").style.display = 'none';
		document.getElementById("img_loc_desc").style.display = 'none';
	}
}

function imposeMaxLength(Object, MaxLen)
{
	if(Object.value.length >MaxLen){
		  document.getElementById("txt_catalog_name").value=Object.value.substring(0,MaxLen-1);
	}
  //return (Object.value.length < MaxLen);
}
function setAndResetImg(id){
     var box = document.getElementById(id);
   if(document.getElementById("img_loc_desc").style.display != 'none' && box.value == "Retrieve from Vendor FTP site" && document.getElementById("ftpAddr").style.display == 'none') 
	{
		document.getElementById("ftpAddr").style.display = 'block';
		document.getElementById("previousCatalog").style.display = 'none';
		document.getElementById("ftpAccess").style.display= "block";
		
		document.getElementById("isAnonymousFTP").checked = false;
	}
	else {
		document.getElementById("ftpAddr").style.display = 'none';
	}
	if(box.value == "Upload Files") 
	{
		document.getElementById("ftpAddr").style.display = 'none';
		window.open('../applet/uploadImages.jsp','welcome','width=500,height=350,menubar=no,status=no,location=no,toolbar=no,scrollbars=yes');
	}
	if(box.value == "Upload from Previous Catalog"){
		document.getElementById("ftpAddr").style.display = 'none';
		document.getElementById("previousCatalog").style.display = 'block';
	}else{
		document.getElementById("previousCatalog").style.display = 'none';
	}	
}

function displaymsg(box){
	if(box.value=="Overwrite Existing Catalog and Replace")
		document.getElementById("msg").style.display="block";
	else
		document.getElementById("msg").style.display="none";
}
function enableFtpDtl(){
	if(document.getElementById("isAnonymousFTP").checked)
		document.getElementById("ftpAccess").style.display="none";
	else
		document.getElementById("ftpAccess").style.display="block";
}
function changeDelimeter(box){
	if(box.value=="Text Files(*.txt,*.csv)")
		document.getElementById("delimeter").style.display="block";
	else{
		document.getElementById("delimeter").style.display="none";
	}	

}
function verifyCancel()
{
	var stay=confirm("Are you sure you want to cancel?");
	if (stay){
		document.vendorCatalogForm.action="../vendorCatalog/vendorCatalogSetupForm.html?cancel=cancel";
        document.vendorCatalogForm.submit();
	}
	else{
	 return false;
	}
}

function setFileStatus() {
	document.getElementById("filePath").value='<c:out value="${vendorCatalogForm.filePath}"></c:out>';
	//document.getElementById("vendorName").value='<c:out value="${vendorCatalogForm.vendorName}"></c:out>';
}	
function setStatus(sbutton){
	document.getElementById("buttonClicked").value=sbutton.value;
}
function init(){
	document.getElementById("tab1").className="activeTab";
	var modeTab1='<%=session.getAttribute("enable") %>';
	if(modeTab1 === 'true'){
		document.getElementById("txt_vendor_number").disabled = true;
	}else {
		document.getElementById("txt_vendor_number").disabled = false;
	}
        var element=document.getElementById('txt_vendor_number');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
}
function loadAddDept(id,mode,vcID) {
	var cname=document.getElementById("txt_catalog_name").value;
	window.location = "../vendorCatalog/addDepartment.html?id=" + id + "&mode=" + mode + "&vcID=" + vcID + "&cname=" + cname;
}
</SCRIPT>
<v:javascript formName="vendorCatalogForm" staticJavascript="false" />
<style type="text/css">
h2.headingLabel {
	background:#F0F0F0 none repeat scroll 0 0;
	border-bottom:1px solid #D0D0D0;
	border-top:1px solid #D0D0D0;
	margin:10px 0;
	padding:5px 10px;
}
</style>

<head>
	<title>Add/Edit Vendor Spreadsheet</title>
</head>


<body class="admin">
<div style="margin-left:5px;">
<br/>
	<c:choose>
		<c:when test="${sessionScope.edit == 'true' || sessionScope.viewOnly == 'true'}">
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a> 
		> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  <c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  <c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendor.name}" default="isNull"/></a>
		> <c:out value="${vendorCatalog.vendorCatalogName}" default="isNull"/>
		</c:when>
		<c:otherwise>
			<c:choose>
			<c:when test="${vendorInfo.name != null}">
				<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a> 
				> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  		<c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  		<c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  		<c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  		<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorInfo.name}"/></a>
			</c:when>
			<c:otherwise>
				<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
			</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
<b><fmt:message key="vendorcatalog.vendorcatalog"/> </b>
<br/>
</div>
<br/>
<c:if test="${sessionScope.edit == 'true' || sessionScope.viewOnly == 'true'}">
<!-- Start Catalog Info -->
<div id="car_info_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="vendorcatalog.cataloginfo"/>
	</div>
	<div class="x-panel-body">
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.vendornumber"/></strong> 
			<c:out value="${vendorCatalog.vendor.vendorNumber}" default="" />
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.catalogid"/></strong> 
			<c:out value="${vendorCatalog.vendorCatalogID }" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.catalogstatus"/></strong>
			<c:out value="${vendorCatalog.statusCD}" default="isNull"/>
		</li>
	</ul>
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">	
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.vendorname"/></strong> 
			<c:out value="${vendorCatalog.vendor.name}" default="isNull"/>
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.datecreated"/> </strong><fmt:formatDate value="${vendorCatalog.createdDate}" pattern="${datePattern}"/>
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.datelastupted"/> </strong><fmt:formatDate value="${vendorCatalog.updatedDate}" pattern="${datePattern}"/> 
		</li>
	</ul>
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.catalogsource"/></strong>
			<c:out value="${vendorCatalog.source}" default="isNull" />
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.createdby"/></strong> 
			<c:out value="${vendorCatalog.createdBy}" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.lastupdatedby"/></strong> 
			<c:out value="${vendorCatalog.updatedBy}" default="isNull"/>
		</li>	
	</ul>
</div></div><!-- End Catalog Info -->
</c:if>


<div class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Add/Edit Vendor Spreadsheet
	</div>
	<div class="x-panel-body">
	<spring:bind path="vendorCatalogForm.*">
			<c:if test="${not empty status.errorMessages}">
              <div class="error">
                  <c:forEach var="error" items="${status.errorMessages}">
                        <c:choose>
                           <c:when test="${error == 'Saved Successfully!'}">
                            <span style="background: #FFFF00;" id="successful"> <c:out value="Catalog Uploaded Successfully." escapeXml="false"/> </span><br/>
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
		<c:url value="/vendorCatalog/vendorCatalogSetupForm.html?vcID=${sessionScope.vcNewID}&mode=${mode}&saved=${enable}" var="formAction"/>	
	<form:form commandName="vendorCatalogForm" method="post"
		action="${formAction}" onsubmit="return onFormSubmit(this)"
		id="vendorCatalogForm" name="vendorCatalogForm" enctype="multipart/form-data">
		<fieldset>	
		<c:if test="${vendorCatalogForm.error}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="There was a severe error. Please contact administrator." escapeXml="false"/><br />
		</c:if>		
			<ul class="details">
				
					<c:if test="${not empty errors}">
						<div class="error">
							<c:forEach var="error" items="${errors}">
								<c:choose>
			                        <c:when test="${error == 'Saved Successfully!'}">
			                            <span style="background: #FFFF00;" id="successful"> <c:out value="Catalog Uploaded Successfully." escapeXml="false"/> </span><br/>
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
					<li>
					<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
				</li>
				<li>
            		<label>
					<fmt:message key="vendorcatalog.vendornumber"/>
            		<span class="req" style="color:#FF0000">*</span>
            		</label>
            		<c:choose>
					<c:when test="${sessionScope.edit == 'true'}">
					<form:input path="vendorNumber" id="txt_vendor_number" cssClass="text"
						cssErrorClass="text medium error" disabled="true" />
					</c:when>
					<c:otherwise>
            		<form:input path="vendorNumber" id="txt_vendor_number" cssClass="text"
						cssErrorClass="text medium error" maxlength="7" disabled = "${sessionScope.viewOnly}" />
					</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${sessionScope.viewOnly != 'true' &&  sessionScope.edit != 'true' && sessionScope.enable != 'true'}">
						<input type="submit" id="verify_btn" name="verify" value="Verify" onclick="setStatus(this);" />
						</c:when>
						<c:otherwise>
						<input type="submit" id="verify_btn" name="verify" value="Verify" disabled="disabled"/>
						</c:otherwise>
					</c:choose>	
					<form:hidden path="buttonClicked" id="buttonClicked"/>
					<form:hidden path="catalogId"/> 
					<!--<form:hidden path="vendorName" id="vendorName"/>-->
				</li>
				<li>
					<label style="float:left; font-weight:bold; margin-right:10px; text-align:right; width:139px;">
					<fmt:message key="vendorcatalog.vendorname"/>
					</label>
					<div style="float:left; margin-right:10px;margin-left:13px; text-align:left; width:200px;">
					<c:out value="${vendorCatalogForm.vendorName}"/>
					</div>
				</li>
				
				<%if(null != session.getAttribute("enable") && ((String)session.getAttribute("enable")).equalsIgnoreCase("true") ){ %>					
					<c:choose>
					<c:when test="${sessionScope.edit == 'true'}">
					<form:hidden path="vendorNumber" id="vendorNumber"/>
					</c:when>
					</c:choose>
				<li>
				<br>
					<label>
					<fmt:message key="vendorcatalog.catalogname"/>	
					<span class="req" style="color:#FF0000">*</span>				
					</label>
					<form:input path="catalogName" id="txt_catalog_name" onkeypress="return imposeMaxLength(this, 50);" onchange="return imposeMaxLength(this, 50);" onkeyup="return imposeMaxLength(this, 50);"
						cssClass="text" cssErrorClass="text medium error" maxlength="100" disabled = "${sessionScope.viewOnly}" />
				</li>
				</ul>
				
	<h2 class="headingLabel">Departments</h2>
		<c:url var="showDepartments" value="/vendorCatalog/addDepartment.html">
		<c:param name="id" value="${userId}"/> 
		<c:param name="mode" value = "${mode}"/>
		<c:param name="vcID" value = "${vcID}" />
		</c:url>
		<ul class="departments">
			<li>
						<c:choose>
							<c:when test="${sessionScope.viewOnly != 'true'}">
							<input type="button" value='<fmt:message key="vendorcatalog.add.department"/>' name="addDept" class="btn cancel_btn" onclick="loadAddDept('${userId}','${mode}','${vcID}');"/>
							</c:when>
						</c:choose>	
				<br style="clear:both;" />
			</li>
			<c:choose>
			<c:when test="${sessionScope.viewOnly != 'true' && sessionScope.edit != 'true'}">
			<li>
					<display:table  name="sessionScope.departments" cellspacing="0" cellpadding="0" requestURI="" 
					    defaultsort="1" id="usrdept" pagesize="25" class="table">
					    <display:column property="deptCd" titleKey="department.deptCd"/>
					    <display:column property="name" titleKey="department.name"/>
					    <display:column property="description" titleKey="department.descr"/>
					    <display:column>    		
							<secureurl:secureAnchor name="RemoveVendorCatalogDepartment" cssStyle="removeDepartment" elementName="RemoveDepartment"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrdept.deptId},${userId},remove,${mode}"  />
						</display:column>
					    <display:setProperty name="paging.banner.item_name" value="department"/>
					    <display:setProperty name="paging.banner.items_name" value="departments"/>
					</display:table> 
			</li>
			</c:when>
			</c:choose>
			
			<c:choose>
			<c:when test="${sessionScope.viewOnly == 'true'}">
			<li>
					<display:table  name="sessionScope.departments" cellspacing="0" cellpadding="0" requestURI="" 
					    defaultsort="1" pagesize="25" class="table">
					    <display:column property="deptCd" titleKey="department.deptCd"/>
					    <display:column property="name" titleKey="department.name"/>
					    <display:column property="description" titleKey="department.descr"/>
					    <display:setProperty name="paging.banner.item_name" value="department"/>
					    <display:setProperty name="paging.banner.items_name" value="departments"/>
					</display:table>
			</li>
			</c:when>
			</c:choose>
			<c:choose>
			<c:when test="${sessionScope.edit == 'true'}">
			<li>
					<display:table  name="sessionScope.departments" cellspacing="0" cellpadding="0" requestURI="" 
					    defaultsort="1" id="usrdept" pagesize="25" class="table">
					    <display:column property="deptCd" titleKey="department.deptCd"/>
					    <display:column property="name" titleKey="department.name"/>
					    <display:column property="description" titleKey="department.descr"/>
					    <display:column>    		
							<secureurl:secureAnchor name="RemoveVendorCatalogDepartment" cssStyle="removeDepartment" elementName="RemoveDepartment"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrdept.deptId},${userId},remove,${mode}"  />
						</display:column>
					    <display:setProperty name="paging.banner.item_name" value="department"/>
					    <display:setProperty name="paging.banner.items_name" value="departments"/>
					</display:table>
			</li>
			</c:when>
			</c:choose>
		</ul>
				
				
	<h2 class="headingLabel">File Details</h2>
		<ul class="details">
				<li style = "padding-left:19%;">
				<a href="../vendorCatalog/datamapping/vendorCatalogDataMapping.html?method=openCatalogFile&vendorCatalog=true&fileName=${vendorCatalogForm.filePath}&catalogStatus=${vendorCatalog.statusCD}&catalogId=${vendorCatalog.vendorCatalogID}&catalogVendor=${vendorCatalog.vendor.vendorNumber}"><c:out value="${vendorCatalogForm.filePath}"></c:out></a>
				</li>
				<li>				
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.filename"/>
					<span class="req" style="color:#FF0000">*</span>
					</label>
					<spring:bind path="vendorCatalogForm.fileName">
						<c:choose>
							<c:when test="${sessionScope.viewOnly != 'true'}">
							<input type="file" maxlength="250" name="fileName" id="fileName" class="file medium" accept ="application/pdf" />
							</c:when>
							<c:otherwise>
							<input type="file" maxlength="250" name="fileName" id="fileName" class="file medium" accept ="application/pdf" disabled = "${sessionScope.viewOnly}" />
							</c:otherwise>
						</c:choose>
					</spring:bind>
					
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="vendorcatalog.fileformat"/>
						<span class="req" style="color:#FF0000">*</span>
					</label>
					<form:select path="fileFormat" id="file_format"
						items="${fileFormatList}" itemLabel="fileFormatName" itemValue="fileFormatName"
						cssClass="text" cssErrorClass="text medium error" onchange="changeDelimeter(this);" disabled = "${sessionScope.viewOnly}">
					</form:select>
				</li>
				<c:choose>
					 <c:when test="${vendorCatalogForm.fileFormat == 'Text Files(*.txt,*.csv)'}">
					 	<li id="delimeter" style="display:block">
					 </c:when>
					 <c:otherwise>
					 	<li id="delimeter" style="display:none">
					 </c:otherwise>
				</c:choose>
				
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.delimeter"/>					
					</label>
					<form:select path="delimeter" id="file_format"						
						cssClass="text" cssErrorClass="text medium error" disabled = "${sessionScope.viewOnly}">
						<form:option value=",">Comma (,)</form:option>
						<form:option value=";">Semi-colon (;)</form:option>
						<form:option value="|">Pipe (|)</form:option>
					</form:select>					
				</li>
			</ul>
				
	<h2 class="headingLabel">Image Details</h2>
		<ul class="details">
		<ul>
					 <li>
					 	<label for="txt_attr_name">
							<fmt:message key="vendorcatalog.upload"/>	
							<span class="req" style="color:#FF0000">*</span>				
						</label>					
						<form:select path="uploadImage" id="imgLocn"						
							cssClass="text" cssErrorClass="text medium error" onchange="setAndReset(this);setAndResetImg('image_loc_choice');" disabled = "${sessionScope.viewOnly}">
							<form:option value="Y">Yes</form:option>
							<form:option value="N">No</form:option>
						</form:select>					
					</li></ul>
					<ul id="address" style="display:block">
							<c:choose>
							 <c:when test="${vendorCatalogForm.uploadImage == 'N'}">
							 <li id="img_loc_desc" style="display:none;">
							 </c:when>
							 <c:otherwise>
							 <li id="img_loc_desc" style="display:block;">
							 </c:otherwise>
							</c:choose>
								<label for="txt_attr_name">
								<fmt:message key="vendorcatalog.imglocation"/>	
								<span class="req" style="color:#FF0000">*</span>				
								</label>
										 <!--  CARs Dropship P1 2012 changed the itemValue from imageLocationDesc to imageSourceName -->
	                                      <form:select path="imgLocn" id="image_loc_choice" 
											items="${imageLocation}" itemLabel="imageLocationDesc" itemValue="imageSourceName"
											cssClass="text" cssErrorClass="text medium error" onchange="setAndResetImg('image_loc_choice');" disabled = "${sessionScope.viewOnly}">
										  </form:select>					
							</li>
							<c:choose>
					 		 <c:when test="${vendorCatalogForm.imgLocn == 'Upload from Previous Catalog' && vendorCatalogForm.uploadImage == 'Y'}">
							 <li id="previousCatalog" style="display:block">
							 </c:when>
							 <c:otherwise>
							 <li id="previousCatalog" style="display:none">
							 </c:otherwise>
							</c:choose>
								<label for="txt_attr_name">
								<fmt:message key="vendorcatalog.previouscatalog"/>	
								<span class="req" style="color:#FF0000">*</span>				
								</label>
								<!--<c:set var="previousVendorCatalogs" value="${previousVendorCatalogs}" scope="session" />-->
								 <form:select path="previousCatalogID" id="previousCatalogID" cssErrorClass="text medium error" disabled = "${sessionScope.viewOnly}"  >
								 	
								 	<c:forEach items="${vendorCatalogForm.previousVendorCatalogs}" var="previousVendorCatalogs">
								 	<form:option value="${previousVendorCatalogs.vendorCatalogID.vendorCatalogID}"><c:out value="${previousVendorCatalogs.vendorCatalogID.vendorCatalogName}"></c:out>  </form:option>
								 	</c:forEach>
									
								  </form:select>
								
							 </li>
					</ul>
					<c:choose>
					 <c:when test="${vendorCatalogForm.imgLocn == 'Retrieve from Vendor FTP site' && vendorCatalogForm.uploadImage == 'Y'}">
					 	<ul id="ftpAddr" style="display:block">
					 </c:when>
					 <c:otherwise>
					 	<ul id="ftpAddr" style="display:none">
					 </c:otherwise>
					</c:choose>
					<li>
						<label for="txt_attr_name">
						<fmt:message key="vendorcatalog.ftpurl"/>	
						<span class="req" style="color:#FF0000">*</span>				
						</label>
						<form:input path="ftpUrl" id="txt_ftp_url"
							cssClass="text" cssErrorClass="text medium error" maxlength="250" disabled = "${sessionScope.viewOnly}"/>
					</li>
					<li>
						<label></label>									
						<form:checkbox path="isAnonymousFTP" id="isAnonymousFTP" onclick="enableFtpDtl();" disabled = "${sessionScope.viewOnly}"/>
						Anonymous FTP Access(no User Name or Password Required)
					</li>
					<c:choose>
					 <c:when test="${vendorCatalogForm.isAnonymousFTP == true}">
					 	<ul id="ftpAccess" style="display:none">
					 </c:when>
					 <c:otherwise>
					 	<ul id="ftpAccess" style="display:block">
					 </c:otherwise>
					</c:choose>
					<li>
						<label for="txt_attr_name">
						<fmt:message key="vendorcatalog.ftpusername"/>
						<span class="req" style="color:#FF0000">*</span>										
						</label>
						<form:input path="ftpUname" id="txt_ftp_uname"
							cssClass="text" cssErrorClass="text medium error" maxlength="50" disabled = "${sessionScope.viewOnly}"/>
					</li>
					<li>
						<label for="txt_attr_name">
						<fmt:message key="vendorcatalog.ftppassword"/>
						<span class="req" style="color:#FF0000">*</span>										
						</label>
						<c:choose>
							<c:when test="${sessionScope.viewOnly != 'true'}">
							<spring:bind path="vendorCatalogForm.ftpPwd">
							<input type="password" id="txt_ftp_pwd" name="ftpPwd" maxlength="50"/>
							</spring:bind>
							</c:when>
							<c:otherwise>	
							<spring:bind path="vendorCatalogForm.ftpPwd">
							<input type="password" id="txt_ftp_pwd" name="ftpPwd" maxlength="50" disabled = "${sessionScope.viewOnly}"/>
							</spring:bind>
							</c:otherwise>
						</c:choose>	
						</li></ul>
					</ul>
				</ul>
	<h2 class="headingLabel">Options</h2>
			<ul class="details">
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.updateaction"/>	
					<span class="req" style="color:#FF0000">*</span>				
					</label>
					<form:select path="updateAction" id="sel_data_type"
						items="${updateActionList}" itemLabel="updateActionDesc" itemValue="updateActionDesc"
						cssClass="text" cssErrorClass="text medium error" onchange="displaymsg(this);" disabled = "${sessionScope.viewOnly}">
					</form:select>					
				</li>
				<li  id="msg" style="color:#FF0000;display:none;
				float:left; margin-right:30px;  text-align:left; width:800px;">
					**Overwriting and Existing Catalog and replacing with new will delete all existing style information stored for this Vendor
					
				</li>
				
			</ul>
		<div style="padding:25px 0 0 165px;">
			<br>
			<input type="button" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" onClick="verifyCancel();" />
			<c:choose>
					<c:when test="${sessionScope.viewOnly != 'true'}">
					<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" onclick="setFileStatus();" />
					</c:when>
					<c:otherwise>
					<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" onclick="setFileStatus();" disabled="disabled" />
					</c:otherwise>
			</c:choose>	
			<form:hidden path="filePath" id="filePath"/>
		</div>
			
			<%}else{%>
			<br/><br/><ul>
				<li>
				<label>
				<fmt:message key="vendorcatalog.catalogname"/>
					<span class="req" style="color:#FF0000">*</span>
				</label>	
					<form:input path="catalogName" id="txt_catalog_name"
						cssClass="text" cssErrorClass="text medium error" maxlength="100" disabled = "true" />
				</li>
	<h2 class="headingLabel">Departments</h2>			
			<ul class="departments">
				<li>
				<display:table  name="" cellspacing="0" cellpadding="0" requestURI="" 
				    defaultsort="1" id="usrdept" pagesize="5" class="table">
				    <display:column property="deptCd" titleKey="department.deptCd"/>
				    <display:column property="name" titleKey="department.name"/>
				    <display:column property="description" titleKey="department.descr"/>
				    
				    <display:setProperty name="paging.banner.item_name" value="department"/>
				    <display:setProperty name="paging.banner.items_name" value="departments"/>
				</display:table>
				</li>
			</ul>
	<h2 class="headingLabel">File Details</h2>
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.filename"/>
					<span class="req" style="color:#FF0000">*</span>
					</label>
					<input type="file" name="file" id="file" class="file medium" disabled="disabled" />
				</li>
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.fileformat"/>
					<span class="req" style="color:#FF0000">*</span>
					</label>
					<form:select disabled="true" path="fileFormat" id="txt_attr_name"
						cssClass="text" cssErrorClass="text medium error">
						<form:option value="xls">Microsoft Office Excel File</form:option>
						<form:option value="txt">Text Files(*txt,*cvs)</form:option>
						<form:option value="xml">XML Files(*.xml)</form:option>
					</form:select>					
				</li>
				
	<h2 class="headingLabel">Image Details</h2>				
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.upload"/>	
					<span class="req" style="color:#FF0000">*</span>				
					</label>
					<form:select disabled="true" path="" id="imgLocn"						
						cssClass="text" cssErrorClass="text medium error">
						<form:option value="Y">Yes</form:option>
						<form:option value="N">No</form:option>
					</form:select>					
				</li>
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.imglocation"/>	
					<span class="req" style="color:#FF0000">*</span>				
					</label>
					<form:select disabled="true" path="imgLocn" id="imgLocn"						
						cssClass="text" cssErrorClass="text medium error">
						<!--  Dropshp CARS 2012 Removed the CD from the dropdown -->
						<form:option value="uploadCD">Upload Files</form:option>
						<form:option value="fromFTP">Retreive from Vendor FTP Site</form:option>
						<form:option value="prevUpload">Upload from Previous Catalog</form:option>
					</form:select>					
				</li>
				
				
	<h2 class="headingLabel">Options</h2>				 
				<li>
					<label for="txt_attr_name">
					<fmt:message key="vendorcatalog.updateaction"/>	
					<span class="req" style="color:#FF0000">*</span>				
					</label>
					<form:select disabled="true" path="updateAction" id="updAction"						
						cssClass="text" cssErrorClass="text medium error">
						<form:option value="existingStyle">Update existing styles</form:option>
						<form:option value="newStyle">Append new styles only</form:option>
						<form:option value="overwriteStyle">Overwrite existing catalog and replace**</form:option>
					</form:select>					
				</li>				
			</ul>
			<div style="padding:25px 0 0 165px;">
				<input type="button"  class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" onClick="verifyCancel();"/>
				<input type="submit" disabled="disabled" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
			<%}%>
		
</fieldset>	
	</form:form>
	</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){
		return confirm('<fmt:message key="transition.confirm.delete"/>');
	});
	
	//If Spreadsheet/Catalog uploaded successfully, then fadein the message slowly.
		var msg_size = $('#successful').size();
		if(msg_size == 1){
			$('#successful').fadeIn(function(){
								var $this = $(this);
								setTimeout(function(){
									$this.fadeOut("slow");
								}, 3000);
							});
		}
		
});
</script>
]]>
</content>