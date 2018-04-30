<%@include file="/common/taglibs.jsp" %>
<script type="text/javascript"><!--
var saveFlag=false;

function verifyCancel(form)
{
        
             window.close();
                
               
}
function refreshParentWindow() {
if(saveFlag)    {

var vendorStyleId = window.opener.document.getElementById("vendorStyleId").value;
var vendorCatalogId = window.opener.document.getElementById("vendorCatalogId").value;
var recordNum = window.opener.document.getElementById("recordNum").value;
var styleVendorUpc = window.opener.document.getElementById("styleVendorUpc").value;
var catalogTemplateId = window.opener.document.getElementById("catalogTemplateId").value;

window.close();
if (window.opener && !window.opener.closed) {
window.opener.location="catalogVendors.html?method=editStylesProperties&vendorStyleId="+vendorStyleId+"&vendorCatalogID="+vendorCatalogId+"&recordNum="+recordNum+"&catalogTemplateId="+catalogTemplateId+"&vendorUpc="+styleVendorUpc;
}


}

}
--></script>
<html>
<head>
<title>Upload Image</title>
<html:base/>
<link HREF="../css/cars.css" REL="stylesheet" TYPE="text/css" >
</head>
<style type="text/css">
.x-panel-header{
overflow:hidden;
zoom:1;

color:#15428b;
font:bold 11px tahoma,arial,verdana,sans -serif;
padding:5px 3px 4px 5px;
border:1px solid #56A5EC;
line-height:15px;
background-color: #C2DFFF;
}

.x-unselectable {
-moz-user-select:none;
-khtml-user-select:none;
}

.cars_panel {
	background-color: #E3E4FA;
	padding: 3px;
	border: 1px solid #56A5EC;
	zoom: 1;
}
.x-panel-body{
border: 1px solid #56A5EC;
text-align:center;
background-color:#fff;
padding-bottom: 40px;
padding-top: 40px;
}

.tbClass{
border: 1px solid #D0D0D0 ;
}

</style>
<body>
	<c:url value="/vendorCatalog/fileUploadPopup.html?method=upload" var="formAction"/>
	<form:form commandName="vendorCatalogImageUploadForm" method="post" action="${formAction}" id="vendorCatalogImageUploadForm" name="uploadImageForm" enctype="multipart/form-data" >
	<spring:bind path="vendorCatalogImageUploadForm.*">
    	<c:if test="${not empty status.errorMessages}">
        	<div class="error">
            	<c:forEach var="error" items="${status.errorMessages}">
                	<c:choose>
                    	<c:when test="${error == 'Saved Successfully!'}">
                        	<span style="background:#FFFF00;"> <c:out value="${error}" escapeXml="false"/> </span><br/>
                                <script>
                                      saveFlag = true;
                                </script>
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
	

        <div class="x-panel-header x-unselectable" style="BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom;" id="ext-gen26"> Add Image

        </div>
       <div class="cars_panel">
        <div  class="x-panel-body">
		
	<table class="tbClass">
	<tr>
	<td></td>
	</tr>
	<tr>
			<td style="background-color:#f6f6f6;">
			<b  style="font-family: sans-serif,arial;font-size: 11px;font-weight: 700;">File:</b>
			</td>
			<td>
				<spring:bind path="vendorCatalogImageUploadForm.file">
				
				<input type="file" name="file" id="file" class="file medium"  title="Browse" value="Browse" size="25"/>	
				</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="background-color:#f6f6f6;"> <b style="font-family: sans-serif,arial;font-size: 11px;font-weight: 700;">Image Type: </b></td> 
				<td style="padding: 3px;width: 70%">
					<form:select path="imageType"  tabindex="1">
						<form:option value="MAIN">Main Image</form:option>
						<form:option value="SWATCH">Swatch Image</form:option>
						<form:option value="ALT">Alternate Image</form:option>
					</form:select>
				 </td>
			</tr>
			
			
			
	</table>
	<br/>
	<input style="BORDER-RIGHT: #aaaaaa 1px solid; height:23px;align:right;PADDING-RIGHT: 7px; BORDER-TOP: #aaaaaa 1px solid;white-space:nowrap; PADDING-LEFT: 8px; FONT-WEIGHT: bold; FONT-SIZE: 12px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: right; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-left: 10px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid;
					TEXT-DECORATION: none;margin-right:250px;" type="submit" name="Upload" title="Upload" value="Upload"/> 
	<input onClick="verifyCancel(this.form);" style="BORDER-RIGHT: #aaaaaa 1px solid; height:23px;align:center;PADDING-RIGHT: 7px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; PADDING-LEFT: 10px; FONT-WEIGHT: bold; FONT-SIZE: 12px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left;PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b;PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid;
					TEXT-DECORATION: none;margin-left:25px;" type="button" name="Cancel" title="Cancel" value="Cancel"/>
	</div>
	</div>
</form:form>
        <script>
            refreshParentWindow();
        </script>
</body>
</html>