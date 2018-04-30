<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerVendorCatalogTab.jsp" %>
<br>
<head>
    <title>Vendor Catalog Management</title>
    <c:if test="${pageContext.request.remoteUser != null}">
        <c:set var="headingUser">
            <authz:authentication operation="fullName"/>
        </c:set>
    </c:if>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>

    <style>
 .car_info {
	padding: 0 0 5px 0 !important;
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
	width:80px;
}
.styleskutext2 {
	margin-left: 1%;
	width:80px;
}
.styleskutext3 {
	margin-left: 1%;
	width:80px;
}
.styleskutext4 {
	margin-left: 1%;
}
.styleskutext5 {
	margin-left: 1%;
	width:70px;
}
.stylestext {
	width: 30px;
}

   </style>
</head>
<script>

     function lockStyle(styleId,catalogId) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockVendorCatalogStyles&mode=lock&vendorCatalogID=" + catalogId +"&vendorStyleId=" + styleId+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
     function unLockStyle(styleId,catalogId) {
         document.forms[0].action ="catalogVendors.html?method=lockUnlockVendorCatalogStyles&mode=unlock&vendorCatalogID=" + catalogId +"&vendorStyleId=" + styleId+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }

    function viewStyle(vendorStyleId,vendorCatalogId,recordNum,catalogTemplateId,vendorUpc) {
        document.forms[0].action ="catalogVendors.html?method=getStylesProperties&vendorStyleId=" + vendorStyleId +"&vendorCatalogID=" + vendorCatalogId +"&recordNum="+ recordNum + "&catalogTemplateId=" + catalogTemplateId+"&vendorUpc="+vendorUpc;
        document.forms[0].submit();
    }
    function editStyle(vendorStyleId,vendorCatalogId,recordNum,catalogTemplateId,vendorUpc) {
        document.forms[0].action ="catalogVendors.html?method=editStylesProperties&vendorStyleId=" + vendorStyleId +"&vendorCatalogID=" + vendorCatalogId +"&recordNum="+ recordNum + "&catalogTemplateId=" + catalogTemplateId+"&vendorUpc="+vendorUpc;
        document.forms[0].submit();
    }

    function setChkValue() {
    var temp =document.getElementById("tempDepartmentOnly").checked;
    //alert(temp);
    if(temp) {
        document.getElementById("departmentOnly").value=true;
    } else {
        document.getElementById("departmentOnly").value=false;
    }
}
    window.onload = function(){
    	document.getElementById("tab4").className="activeTab";
        var pos = <c:out value="${scrollPos}" />;
         var element=document.getElementById('vendorStyle');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled && pos==0 ){
		 setTimeout(function() { element.focus(); }, 10);
		}

    }
    function setValue123() {
        var val=document.getElementById("tempStatus").value;
       if(val=='ACTIVE') {
         document.getElementById("status").options[1].selected=true;
       } else if(val=='INACTIVE') {
        document.getElementById("status").options[2].selected=true;
       } else {
           document.getElementById("status").options[0].selected=true;
       }

    }
</script>
<body class="admin">
<!--  Breadcrumbs  -->
	<div style="margin-left:5px; font-size:13px;">
		
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a> 
		> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  <c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  <c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendor.name}" default="isNull"/></a>
		  <c:url value="/vendorCatalog/vendorCatalogSetupForm.html?vcID=${vendorCatalog.vendorCatalogID}&mode=${mode}" var="formAction"/>
		> <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendorCatalogName}" default="isNull"/></a>   
		> Spreadsheet Items
		<br>
		<br>
		<b><fmt:message key="vendorstyle.title"/></b>
  	</div>
  	
     <spring:bind path="searchStyleForm.*">
	<c:if test="${not empty errors}">
	<br>
		<div class="error">
			<c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>

	</c:if>
        </spring:bind><br>
    <div id="search_for_attribute" class="cars_panel x-panel">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Vendor Catalog Style Info </span></div>

        <div class="x-panel-body">
           <div>
			<ul class="car_info">
				<li class="car_id"><b>Vendor #:</b> <c:out value="${catalogInfo.vendor.vendorNumber}" /></li>
				<li class="dept"><b>Catalog ID:</b> <c:out value="${catalogInfo.vendorCatalogID}" /></li>
				<li><b>Status: </b> <c:out value="${catalogInfo.statusCD}" /></li>
			</ul>
		</div>
		<div>
			<ul class="car_info">
				<li class="car_id"><b>Vendor Name:</b> <c:out value="${catalogInfo.vendor.name}" /></li>
				<li class="dept"><b>Date Created:</b> <fmt:formatDate value="${catalogInfo.createdDate}" pattern="${datePattern}"/></li>
				<li><b>Last Updated:</b> <fmt:formatDate value="${catalogInfo.updatedDate}" pattern="${datePattern}"/></li>
			</ul>
		</div>
		<div>
			<ul class="car_info">
				<li class="car_id"><b>Spreadsheet Name:</b> <c:out value="${catalogInfo.vendorCatalogName}" /></li>
				<li class="dept"><b>Created By:</b> <c:out value="${catalogInfo.createdBy}" /></li>
				<li><b>Last Updated By:</b> <c:out value="${catalogInfo.updatedBy}" /></li>
			</ul>
		</div>
		<div>
			<ul class="car_info">
				<li class="car_id"><b>Spreadsheet Source:</b> <c:out value="${catalogInfo.source}" /></li>
				<li class="dept"></li>
				<li></li>
			</ul>
		</div>
	</div> <!-- End of x-pane-body 1 -->

        </div>
        <br>

    <div id="search_for_attribute1" class="cars_panel x-panel">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Style Search</span></div>

        <div class="x-panel-body">
            <c:url value="catalogVendors.html?method=searchVendorCatalogStyles" var="formAction"/>
            <form:form  name ="searchStyleForm" commandName="searchStyleForm" method="post"
                        action="${formAction}" onsubmit="return onFormSubmit(this)"
                        id="searchStyleForm">
                        <spring:bind path="searchStyleForm.vendorId">
                                <input type="hidden" id="vendorId" name="vendorId"  value="<c:out value="${searchStyleForm.vendorId}" />"  />
                        </spring:bind>
                         <spring:bind path="searchStyleForm.catalogId">
                                <input type="hidden" id="catalogId" name="catalogId" value="<c:out value="${searchStyleForm.catalogId}" />"    />
                        </spring:bind>
                        <spring:bind path="searchStyleForm.vendorCatalogTemplaetId">
                                <input type="hidden" id="vendorCatalogTemplaetId" name="vendorCatalogTemplaetId" value="<c:out value="${searchStyleForm.vendorCatalogTemplaetId}" />"  />
                        </spring:bind>

               <table class="car_info" cellspacing="0" cellpadding="0" style="border:0px;">
			<tr>
				<td style="border:0px"><label class="styleskulabel1" for="txt_attr_name">Vendor Style #:</label></td>
				<td style="border:0px"><spring:bind path="searchStyleForm.vendorStyle"><input type="text" id="vendorStyle" name="vendorStyle" class="styleskutext1" value="<c:out value="${searchStyleForm.vendorStyle}" />"cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                         </spring:bind></td>

				<td style="border:0px"><label for="txt_attr_name"class="styleskulabel2">Description:</label> </td>
				<td style="border:0px"><spring:bind path="searchStyleForm.description"> <input type="text" id="description" name="description" class="styleskutext2" value="<c:out value="${searchStyleForm.description}" />"cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                         </spring:bind></td>

				<td style="border:0px"><label class="styleskulabel3">Date Last Updated(Start):</label></td>
				<td style="border:0px">	<spring:bind path="searchStyleForm.dateLastUpdatedStart"><input class="styleskutext5" type="text" id="dateLastUpdatedStart" name="dateLastUpdatedStart" size="12" cssClass="styleskutext5" value="<c:out value="${searchStyleForm.dateLastUpdatedStart}" />" />
                            </spring:bind></td>

				<td style="border:0px"><input type="submit" class="btn" name="method" value="Search" />
                           <c:url value="/catalogVendors.html?method=searchVendorStyles" var="formAction"/>
                            <a href='../vendorCatalog/catalogVendors.html?method=viewAllVendorCatalogStyles' id="a_view_all">View All</a>&nbsp;</td>

			</tr>
			<tr>
				<td style="border:0px"><label for="txt_attr_name" class="styleskulabel1">Department #:</label> </td>
				<td style="border:0px"><spring:bind path="searchStyleForm.department">	<input type="text" id="department" name="department" class="styleskutext1" value="<c:out value="${searchStyleForm.department}" />"cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                        </spring:bind></td>

				<td style="border:0px"><label for="txt_attr_name" class="styleskulabel2">Vendor UPC:</label></td>

				<td style="border:0px"><spring:bind path="searchStyleForm.department">
                                <input type="text" id="vendorUPC" name="vendorUPC" class="styleskutext3" value="<c:out value="${searchStyleForm.vendorUPC}" />" cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                        </spring:bind></td>


				<td style="border:0px"><label class="styleskulabel3">Date Last Update(End):</label></td>
				<td style="border:0px"><spring:bind path="searchStyleForm.dateLastUpdatedEnd"><input type="text" name="dateLastUpdatedEnd" class="styleskutext5" id="dateLastUpdatedEnd" size="12"  cssClass="styleskutext5" value="<c:out value="${searchStyleForm.dateLastUpdatedEnd}" />" />
                            </spring:bind></td>


				<td style="border:0px"></td>
			</tr>
			<tr>
				<td style="border:0px"><label class="styleskulabel1">Status:</label></td>
				<td style="border:0px"><spring:bind path="searchStyleForm.status">
                                <select  id="status" class="styleskutext5" name="status">
                                    <option value="">All</option>
                                    <option value="ACTIVE">Active</option>
                                    <option  value="INACTIVE">Inactive</option>
                                </select>
                            </spring:bind></td>
				  <td colspan="2" style="border:0px"><label class="styleskulabel2"> User Departments only: </label>&nbsp; &nbsp;
                                     <input id="departmentOnly" type="hidden"  name="departmentOnly" value="<c:out value="${searchStyleForm.departmentOnly}" />" />
                                    <c:if test="${searchStyleForm.departmentOnly eq 'true'}" >
                                        <input id="tempDepartmentOnly" type="checkbox" checked name="tempDepartmentOnly"  style="margin-left: 1%;width:16px;" onclick="setChkValue();" />
                                    </c:if>
                                       <c:if test="${searchStyleForm.departmentOnly !='true'}" >
                                        <input id="tempDepartmentOnly" type="checkbox"  name="tempDepartmentOnly"  style="margin-left: 1%;width:16px;" onclick="setChkValue();" />
                                    </c:if>
                                  </td>
				<td style="border:0px">&nbsp;</td>
				<td style="border:0px">&nbsp;</td>
				<td style="border:0px">&nbsp;</td>
			</tr>

		</table>
                                     <input type="hidden" id="tempStatus" name="tempStatus" value="<c:out value="${searchStyleForm.tempStatus}" />" />
            </form:form>

        </div>
    </div>
    <br>
    <div id="user_list" class="cars_panel x-panel">

        <div class="x-panel-header"> Vendors Spreadsheet Styles

        </div>
        <div  class="x-panel-body">
        	<div class="userButtons">
                <a class="btn"	href="<c:url value="vendorCatalogSetupForm.html"/>" title="New Catalog">New Spreadsheet</a>
            </div>
           <display:table name="vendorStyleList" cellspacing="0" cellpadding="0" requestURI="/vendorCatalog/catalogVendors.html"
                           defaultsort="1" id="vendorCatalogStyle" pagesize="25" class="dstable" sort="list" >
                <display:column sortable="true" titleKey="vendorstyle.image" style="width:10%">
                    <secureurl:secureAnchor name="viewVendorStyleProperties"    title="<img src= '${imageAppURL}${vendorCatalogStyle.imagePath}' width='50' height='50' >"  arguments="'${vendorCatalogStyle.vendorStyleId}',${vendorCatalogStyle.vendorCatalogId},${vendorCatalogStyle.recordNum},${searchStyleForm.vendorCatalogTemplaetId},'${vendorCatalogStyle.vendorUPC}'"   hideUnaccessibleLinks="true"/>
                </display:column>
               <display:column  sortable="true" titleKey="vendorstyle.vendorstyle" style="width:5%">
                      <secureurl:secureAnchor name="viewVendorStyleProperties" title="${vendorCatalogStyle.vendorStyleId}"  cssStyle="removeVendor" arguments="'${vendorCatalogStyle.vendorStyleId}',${vendorCatalogStyle.vendorCatalogId},${vendorCatalogStyle.recordNum},${searchStyleForm.vendorCatalogTemplaetId},'${vendorCatalogStyle.vendorUPC}'"   hideUnaccessibleLinks="true"/>
                </display:column>
                <display:column property="description" sortable="true" titleKey="vendorstyle.description" style="width:25%; white-space:normal"/>
                <display:column  property="status" sortable="true" titleKey="vendorstyle.status" style="width: 10%"/>
                <display:column property="numSKUs" sortable="true" titleKey="vendorstyle.numSKUs" style="width: 5%"/>
                <display:column property="dateLastUpdated" sortable="true" titleKey="vendorstyle.lastUpdated" style="width: 8%"/>
                <display:column property="lastUpdatedBy" sortable="true" titleKey="vendorstyle.lastUpdatedBy" style="width: 12%;white-space:normal"/>
               
                <display:setProperty name="paging.banner.item_name" value="style"/>
                 <display:setProperty name="paging.banner.items_name" value="styles"/>



                </display:table>

            
        </div>

            <script>
                setValue123();
            </script>

</body>

<content tag="jscript">
    <![CDATA[
    <script type="text/javascript">
        $(document).ready(function(){
            // web 2.0 filter :)
            // datefields
            var CFG=belk.cars.config;
            new Ext.form.DateField({
                applyTo:'dateLastUpdatedStart',
                fieldLabel:'Date Last Import(Start):',
                allowBlank:true,
                format:CFG.dateFormat,
                altFormats:CFG.dateAltFormats
            });
            new Ext.form.DateField({
                applyTo:'dateLastUpdatedEnd',
                fieldLabel:'Date Last Import(End):',
                allowBlank:true,
                format:CFG.dateFormat,
                altFormats:CFG.dateAltFormats
            });
            $('select.autocomplete').each(function(){
                var $this=$(this);
                var name=$this.attr("name");
                var combo=new Ext.form.ComboBox({
                    typeAhead:true,
                    triggerAction:'all',
                    transform:this,
                    width:250,
                    forceSelection:false,
                    cls:$this.is('.recommended')?'recommended':'',
                    id:id,
                    disabled:$this.is(':disabled')
                });
                combo.on({
                    'blur':function(that){
                        $('input[name="'+name+'"]').val(that.getRawValue());
                    }
                });
            });

            var $dropdown = $('select.dropdown-show');
            if ($dropdown) {
                function showCurrent(){
                    $('ol.dropdown-show > li').hide();
                    $('ol.dropdown-show > li.' + $dropdown.val()).show();
                    $('ol.dropdown-show > li.*').show();
                }
                showCurrent();
                $dropdown.change(showCurrent);
            }
             window.scrollTo(0,<c:out value="${scrollPos}" />);
        });
    </script>
    ]]>
</content>


