
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerCatalogVendorsTab.jsp"%>
<script type="text/javascript">

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
	document.getElementById("tab1").className="activeTab";
	setStatusDropDwnValue();
	// commented as part of CARS Dropship P1 
       /* var element=document.getElementById('vendorNumber');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}*/
}
function setStatusDropDwnValue(){
	var s = document.getElementById("display");
	//alert('Element on JSP :'+s);
	 var v ='${requestScope.catalogVendorsForm.display}';
	 //alert('Element from request scope:'+v);	 
	 for (var i = 0; i < s.options.length ; i++) {
	  if( s.options[i].value == v){
	    s.options[i].selected = true;
		return;
	  }else if(v == ''){
		s.options[2].selected = true;
		return;
	  }
	}
        
        
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
</script>
<head>
<title><fmt:message key="catalogvendors.page.title" /></title>
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

<body class="admin">
<!-- Added by Vikas for displaying bread crumb-->
<div style="margin-left: 5px;"><br />
<b>Vendor Catalog Management</b> <br />
</div>
<c:url value="catalogVendors.html?method=search" var="formAction" />
<form:form name="catalogVendorsForm" commandName="catalogVendorsForm"
	method="post" action="${formAction}" id="catalogVendorsForm">
	
	<br>
	<div id="search_for_user" class="cars_panel x-hidden">
	<div class="x-panel-header"><fmt:message
		key="catalogvendors.label.vendorsearch" /></div>

	<div class="x-panel-body"><spring:bind
		path="catalogVendorsForm.*">
		<c:if test="${not empty errors}">
			<div class="error"><c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach></div>
			<br>
		</c:if>
	</spring:bind>
	<table class="car_info" cellspacing="0" cellpadding="0"
		style="border: 0px;">
		<tr>
			<td style="border: 0px; font-weight: bold;"><label
				class="styleskulabel1"> <fmt:message
				key="vendorcatalog.vendornumber" /></label></td>
			<spring:bind path="catalogVendorsForm.vendorNumber">
				<td style="border: 0px;"><input type="text" maxlength="7"
					id="vendorNumber" name="vendorNumber"
					value="<c:out value="${catalogVendorsForm.vendorNumber}" />"
					class="styleskutext1" /></td>
			</spring:bind>

			<td style="border: 0px; font-weight: bold;"><label
				class="styleskulabel2">Vendor Name:</label></td>
			<spring:bind path="catalogVendorsForm.vendorName">
				<td style="border: 0px"><input type="text" maxlength="50"
					id="vendorName" name="vendorName" class="styleskutext2"
					value="<c:out value="${catalogVendorsForm.vendorName}" />" /></td>
			</spring:bind>

			<td style="border: 0px; font-weight: bold;"><label
				class="styleskulabel3">Date Last Import (Start):</label></td>
			<spring:bind path="catalogVendorsForm.dateLastImportStart">
				<td style="border: 0px"><input type="text"
					id="dateLastImportStart" name="dateLastImportStart" size="12"
					class="styleskutext5"
					value="<c:out value="${catalogVendorsForm.dateLastImportStart}" />" />
				</td>
			</spring:bind>


			<td style="border: 0px"><input type="submit" class="btn"
				name="method" value="Search" /> <c:url
				value="/catalogVendors.html?method=search" var="formAction" /> <a
				href='../vendorCatalog/catalogVendors.html?method=viewAllActive'
				id="a_view_all">View All</a></td>
		</tr>
		<tr>
			<td style="border: 0px; font-weight: bold;" align="left"><label
				class="styleskulabel1"> User Departments only:</label></td>
			<td style="border: 0px"><spring:bind
				path="catalogVendorsForm.departmentOnly">
				<input id="departmentOnly" type="hidden" name="departmentOnly"
					style="margin-left: 1%; width: 16px;"
					value="<c:out value="${catalogVendorsForm.departmentOnly}"  />" />
			</spring:bind> <c:if test="${catalogVendorsForm.departmentOnly eq 'true'}">
				<input id="tempDepartmentOnly" type="checkbox" checked
					name="tempDepartmentOnly" style="margin-left: 1%; width: 16px;"
					onclick="setChkValue();" />
			</c:if> <c:if
				test="${catalogVendorsForm.departmentOnly eq 'false'|| catalogVendorsForm.departmentOnly eq '' || catalogVendorsForm.departmentOnly ==null}">
				<input id="tempDepartmentOnly" type="checkbox"
					name="tempDepartmentOnly" style="margin-left: 1%; width: 16px;"
					onclick="setChkValue();" />
			</c:if></td>

			<td style="border: 0px; font-weight: bold;"><label
				class="styleskulabel2">Display:</label></td>
			<spring:bind path="catalogVendorsForm.display">
				<td style="border: 0px"><select id="display" name="display"
					class="styleskutext5">
					<option value="Y" selected>Yes</option>
					<option value="N" id="N">No</option>
					<option id="All" value="">All</option>
				</select></td>
			</spring:bind>


			<td style="border: 0px; font-weight: bold;"><label
				class="styleskulabel2">Date Last Import (End):</label></td>
			<td style="border: 0px"><spring:bind
				path="catalogVendorsForm.dateLastImportEnd">
				<input type="width100" name="dateLastImportEnd"
					id="dateLastImportEnd" size="12" class="styleskutext5"
					value="<c:out value="${catalogVendorsForm.dateLastImportEnd}" />" />
			</spring:bind></td>

			<td style="border: 0px">&nbsp;</td>
		</tr>

	</table>
</div>
	</div>
	<br>



	<div id="user_list" class="cars_panel x-hidden">

	<div class="x-panel-header">Vendor Catalogs</div>
	<div class="x-panel-body">
	<c:if test="${requestScope.user.userType.userTypeCd != 'BUYER'}">
	<div class="userButtons"><secureurl:secureAnchor
		name="newCatalog" cssStyle="btn" title="New Spreadsheet"
		hideUnaccessibleLinks="true" /></div>
	</c:if>
	<display:table name="catalogVendorList" cellspacing="0" cellpadding="0"
		requestURI="/vendorCatalog/catalogVendors.html" defaultsort="1"
		id="catalogVendor" pagesize="25" class="dstable" sort="list">
		<display:column sortable="true" titleKey="vendor.number"
		 comparator="com.belk.car.util.LongComparator" style="width:8%" sortProperty="vendorNumber">

			<secureurl:secureAnchor name="catalogVendorId"
				cssStyle="removeVendor" title="${catalogVendor.vendorNumber}"
				elementName="vendorId" localized="true" hideUnaccessibleLinks="true"

				arguments="${catalogVendor.vendorId},${catalogVendor.numStyle},${catalogVendor.numSKUs},viewAllVendorCatalogs" />
		</display:column>
		<display:column property="name" sortable="true" titleKey="vendor.name"
			style="width: 40%" />
		<display:column property="numCatalogs" sortable="true"
		 comparator="com.belk.car.util.LongComparator"	titleKey="vendor.noOfCatalogs" style="width: 10%" />
		<display:column property="numStyle" sortable="true"
		 comparator="com.belk.car.util.LongComparator"	titleKey="vendor.noOfStyles" style="width: 10%" />
		<display:column property="numSKUs" sortable="true"
		 comparator="com.belk.car.util.LongComparator"	titleKey="vendor.noOfSKUs" style="width: 10%" />
		<display:column property="dateLastImport" sortable="true"
			titleKey="vendor.dateLastImport" style="width: 12%" />
		<display:column sortable="true" property="display"
			titleKey="vendor.display" style="width: 8%" />
		<display:setProperty name="paging.banner.item_name" value="vendor" />
		<display:setProperty name="paging.banner.items_name" value="vendors" />
	</display:table></div>


	</div>
</form:form>
</body>

<content tag="jscript">
<![CDATA[
    <script type="text/javascript">
        $(document).ready(function(){
            // web 2.0 filter :)
            // datefields
            var CFG=belk.cars.config;
            new Ext.form.DateField({
                applyTo:'dateLastImportStart',
                fieldLabel:'Date Last Import(Start):',
                allowBlank:true,
                format:CFG.dateFormat,
                altFormats:CFG.dateAltFormats
            });
           new Ext.form.DateField({
                applyTo:'dateLastImportEnd',
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