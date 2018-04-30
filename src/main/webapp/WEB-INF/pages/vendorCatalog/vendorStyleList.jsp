<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTabForVendorCatalog.jsp"%>

<head>
<title>Vendor Catalog Management</title>
<c:if test="${pageContext.request.remoteUser != null}">
	<c:set var="headingUser">
		<authz:authentication operation="fullName" />
	</c:set>
</c:if>
<meta name="heading" content="<fmt:message key='workflow.heading'/>" />
<meta name="menu" content="UserMenu" />


<style>
.car_info {
	padding: 0 0 5px 0 !important;
	font-size: 11px !important;
	zoom: 1;
	border: none;
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
	width: 80px;
}

.styleskutext2 {
	margin-left: 1%;
	width: 80px;
}

.styleskutext3 {
	margin-left: 1%;
	width: 80px;
}

.styleskutext4 {
	margin-left: 1%;
}

.styleskutext5 {
	margin-left: 1%;
	width: 80px;
}

.stylestext {
	width: 30px;
}
</style>

</head>
<script>
    function lockStyle(styleId,catalogId) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockVendorStyles&mode=lock&vendorCatalogID=" + catalogId +"&vendorStyleId=" + styleId+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
     function unLockStyle(styleId,catalogId) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockVendorStyles&mode=unlock&vendorCatalogID=" + catalogId +"&vendorStyleId=" + styleId+"&scrollPos="+document.documentElement.scrollTop;
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

    function setValue123() {
        var val=document.getElementById("tempStatus").value;
       if(val=='ACTIVE') {
         document.getElementById("status").options[1].selected=true;
         document.getElementById("status").value='ACTIVE';
       } else if(val=='INACTIVE') {
        document.getElementById("status").options[2].selected=true;
       } else {
           document.getElementById("status").options[0].selected=true;
       }

    }
     window.onload = function() {
     	document.getElementById("tab2").className="activeTab";
        var pos = <c:out value="${scrollPos}" />;
        var element=document.getElementById('vendorStyle');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled && pos==0 ){
		 setTimeout(
                 function() {
                     element.focus();
                 }, 10);
		}
     }

</script>
<body class="admin">
<!-- Added by Vikas for displaying bread crumb-->
<div style="margin-left: 5px;"><br />
<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
> <c:set var="vendorId"
	value="${sessionScope.vendorCatalogInfo.vendorId}" /> <c:set
	var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}" /> <c:set
	var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}" /> <c:url
	value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs"
	var="formAction" /> <a
	href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
	value="${vendorInfo.name}" default="" /></a> > Catalog Styles <br>
<br>
<b>Vendor Catalog Styles </b> <br />
<br />
</div>

<div id="search_for_attribute" class="cars_panel x-hidden">
<div class="x-panel-header"><span class="x-panel-header-text">
Vendor Catalog Style Info </span></div>

<div class="x-panel-body">
<ul class="car_info">

	<li class="car_id" style="width: 24%;"><b>Vendor #:</b> <c:out
		value="${vendorInfo.vendorNumber}" /></li>
	<li class="car_id" style="width: 22%;"><b># of Catalog Styles:</b>
	<c:out value="${vendorInfo.numStyle}" /></li>
	<li class="car_id" style="width: 22%;"><b>Date Last Import:</b> <c:out
		value="${vendorInfo.dateLastImport}" /></li>
</ul>

<ul class="car_info">
	<li class="car_id" style="width: 24%;"><b>Vendor Name:</b> <c:out
		value="${vendorInfo.name}" /></li>
	<li class="car_id" style="width: 22%;"><b># of Catalog SKUs:</b> <c:out
		value="${vendorInfo.numSKUs}" /></li>
	<li class="car_id" style="width: 25%;"><b>Last Spreadsheet
	Imported :</b> <c:out value="${vendorInfo.catalogName}" /></li>

</ul>

</div>
</div>
<br>

<div id="search_for_attribute1" class="cars_panel x-hidden">
<div class="x-panel-header"><span class="x-panel-header-text">
Style Search</span></div>

<div class="x-panel-body"><spring:bind path="searchStyleForm.*">
	<c:if test="${not empty errors}">
		<div class="error"><c:forEach var="error" items="${errors}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
				alt="<fmt:message key="icon.warning"/>" class="icon" />
			<c:out value="${error}" escapeXml="false" />
			<br />
		</c:forEach></div>
		<br>
	</c:if>
</spring:bind> <c:url value="catalogVendors.html?method=searchVendorStyles"
	var="formAction" /> <form:form name="searchStyleForm"
	commandName="searchStyleForm" method="post" action="${formAction}"
	onsubmit="return onFormSubmit(this)" id="searchStyleForm">
	<spring:bind path="searchStyleForm.vendorId">
		<input type="hidden" id="vendorId" name="vendorId" value="${vendorId}" />
	</spring:bind>

	<table class="car_info" cellspacing="0" cellpadding="0"
		style="border: 0px;">
		<tr>
			<td style="border: 0px"><label class="styleskulabel1"
				for="txt_attr_name">Vendor Style #:</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.vendorStyle">
				<input type="text" id="vendorStyle" name="vendorStyle"
					class="styleskutext1"
					value="<c:out value="${searchStyleForm.vendorStyle}" />"
					cssClass="text" cssErrorClass="text medium error"
					cssStyle="disabled:true;border:none" />
			</spring:bind></td>

			<td style="border: 0px"><label for="txt_attr_name"
				class="styleskulabel2">Description:</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.description">
				<input type="text" id="description" name="description"
					class="styleskutext2"
					value="<c:out value="${searchStyleForm.description}" />"
					cssClass="text" cssErrorClass="text medium error"
					cssStyle="disabled:true;border:none" />
			</spring:bind></td>

			<td style="border: 0px"><label class="styleskulabel3">Date
			Last Updated(Start):</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.dateLastUpdatedStart">
				<input type="text" id="dateLastUpdatedStart"
					name="dateLastUpdatedStart" size="12" cssClass="styleskutext5"
					value="<c:out value="${searchStyleForm.dateLastUpdatedStart}" />" />
			</spring:bind></td>

			<td style="border: 0px"><input type="submit" class="btn"
				name="method" value="Search" /> <c:url
				value="/catalogVendors.html?method=searchVendorStyles"
				var="formAction" /> <a
				href='../vendorCatalog/catalogVendors.html?method=viewAllVendorStyles'
				id="a_view_all">View All</a>&nbsp;</td>
		</tr>
		<tr>
			<td style="border: 0px"><label for="txt_attr_name"
				class="styleskulabel1">Department #:</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.department">
				<input type="text" id="department" name="department"
					class="styleskutext1"
					value="<c:out value="${searchStyleForm.department}" />"
					cssClass="text" cssErrorClass="text medium error"
					cssStyle="disabled:true;border:none" />
			</spring:bind></td>

			<td style="border: 0px"><label for="txt_attr_name"
				class="styleskulabel2">Vendor UPC:</label></td>

			<td style="border: 0px"><spring:bind
				path="searchStyleForm.vendorUPC">
				<input type="text" id="vendorUPC" name="vendorUPC"
					class="styleskutext3"
					value="<c:out value="${searchStyleForm.vendorUPC}"/>"
					cssErrorClass="text medium error"
					cssStyle="disabled:true;border:none" />
			</spring:bind></td>


			<td style="border: 0px"><label class="styleskulabel3">Date
			Last Update(End):</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.dateLastUpdatedEnd">
				<input type="text" name="dateLastUpdatedEnd" class="styleskutext5"
					id="dateLastUpdatedEnd" size="12"
					value="<c:out value="${searchStyleForm.dateLastUpdatedEnd}" />" />
			</spring:bind></td>


			<td style="border: 0px"></td>
		</tr>
		<tr>
			<td style="border: 0px"><label class="styleskulabel1">Status:</label></td>
			<td style="border: 0px"><spring:bind
				path="searchStyleForm.status">
				<select id="status" class="styleskutext5" name="status">
					<option value="">All</option>
					<option value="ACTIVE">Active</option>
					<option value="INACTIVE">Inactive</option>
				</select>
			</spring:bind></td>
			<td colspan="2" style="border: 0px"><label
				class="styleskulabel2"> User Departments only: </label>&nbsp; &nbsp;
			<input id="departmentOnly" type="hidden" name="departmentOnly"
				value="<c:out value="${searchStyleForm.departmentOnly}" />" /> <c:if
				test="${searchStyleForm.departmentOnly eq 'true'}">
				<input id="tempDepartmentOnly" type="checkbox" checked
					name="tempDepartmentOnly" style="margin-left: 1%; width: 16px;"
					onclick="setChkValue();" />
			</c:if> <c:if test="${searchStyleForm.departmentOnly !='true'}">
				<input id="tempDepartmentOnly" type="checkbox"
					name="tempDepartmentOnly" style="margin-left: 1%; width: 16px;"
					onclick="setChkValue();" />
			</c:if></td>
			<td style="border: 0px">&nbsp;</td>
			<td style="border: 0px">&nbsp;</td>
			<td style="border: 0px">&nbsp;</td>
		</tr>

	</table>
                                  <input type="hidden" id="tempStatus" name="tempStatus" value="<c:out value="${searchStyleForm.tempStatus}" />" />
</form:form></div>
</div>
<br>
<div id="user_list" class="cars_panel x-hidden">

<div class="x-panel-header">Vendors Styles</div>
<div class="x-panel-body"><display:table name="vendorStyleList"
	cellspacing="0" cellpadding="0"
	requestURI="/vendorCatalog/catalogVendors.html" defaultsort="1"
	id="vendorCatalogStyle" pagesize="25" class="dstable" sort="list">
	<display:column sortable="true" titleKey="vendorstyle.image"
		style="width:10%">

		<c:choose>
			<c:when
				test="${vendorCatalogStyle.catalogStatus eq 'Complete'|| vendorCatalogStyle.catalogStatus eq 'Translating'}">
				<secureurl:secureAnchor name="viewVendorStyleProperties"
					title="<img src= '${imageAppURL}${vendorCatalogStyle.imagePath}' width='50' height='50'  >"
					arguments="'${vendorCatalogStyle.vendorStyleId}',${vendorCatalogStyle.vendorCatalogId},${vendorCatalogStyle.recordNum},${vendorCatalogStyle.catalogTemplateId},'${vendorCatalogStyle.vendorUPC}'"
					hideUnaccessibleLinks="true" />
			</c:when>

			<c:otherwise>
				<img src='${imageAppURL}${vendorCatalogStyle.imagePath}' width='50' height='50' > 
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:column sortable="true" titleKey="vendorstyle.vendorstyle"
		style="width:5%">
		<c:choose>
			<c:when
				test="${vendorCatalogStyle.catalogStatus eq 'Complete'|| vendorCatalogStyle.catalogStatus eq 'Translating'}">
				<secureurl:secureAnchor name="viewVendorStyleProperties"
					title="${vendorCatalogStyle.vendorStyleId}" cssStyle="removeVendor"
					arguments="'${vendorCatalogStyle.vendorStyleId}',${vendorCatalogStyle.vendorCatalogId},${vendorCatalogStyle.recordNum},${vendorCatalogStyle.catalogTemplateId},'${vendorCatalogStyle.vendorUPC}'"
					hideUnaccessibleLinks="true" />
			</c:when>

			<c:otherwise>
				<c:out value="${vendorCatalogStyle.vendorStyleId}" />
			</c:otherwise>
		</c:choose>

	</display:column>
	<display:column property="description" sortable="true"
		titleKey="vendorstyle.description" style="width:20%; white-space:normal" />
	<display:column property="status" sortable="true"
		titleKey="vendorstyle.status" style="width: 10%" />
	<display:column property="catalogName" sortable="true"
		titleKey="vendorstyle.catalog" style="width: 15%" />
	<display:column property="numSKUs" sortable="true"
		titleKey="vendorstyle.numSKUs" style="width: 5%" />
	<display:column property="dateLastUpdated" sortable="true"
		titleKey="vendorstyle.lastUpdated" style="width: 8%" />
	<display:column property="lastUpdatedBy" sortable="true"
		titleKey="vendorstyle.lastUpdatedBy" style="width: 12%" />
	<display:column title="" style="width: 30%">
		<c:choose>

			<c:when
				test="${vendorCatalogStyle.catalogStatus eq 'Complete'|| vendorCatalogStyle.catalogStatus eq 'Translating'}">
						<secureurl:secureAnchor name="editVendorStyleProperties"
									title="Edit" cssStyle="btn"
									arguments="'${vendorCatalogStyle.vendorStyleId}',${vendorCatalogStyle.vendorCatalogId},${vendorCatalogStyle.recordNum},${vendorCatalogStyle.catalogTemplateId},'${vendorCatalogStyle.vendorUPC}'"
									hideUnaccessibleLinks="true" />
			</c:when>
			<c:otherwise>
				<!--If catalog is not in in Translating or Complete status then nothing will be displayed. -->
			</c:otherwise>
		</c:choose>
	</display:column>
	<display:setProperty name="paging.banner.item_name" value="style" />
	<display:setProperty name="paging.banner.items_name" value="styles" />
</display:table></div>
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
                fieldLabel:'Start Ship Date',
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


