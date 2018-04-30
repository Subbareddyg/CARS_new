<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerVendorCatalogTab.jsp" %>
<head>
    <title>Add Vendor Catalog</title>
</head>	
<body class="admin">
<div style="margin-left:5px;">
<br/>
	<c:choose>
		<c:when test="${sessionScope.edit == 'true' || sessionScope.viewOnly == 'true'}">
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a> 
		> <a href="../vendorCatalog/catalogVendors.html?method=viewAll"><c:out value="${vendorCatalog.vendor.name}" default="isNull"/></a>
		> <c:out value="${vendorCatalog.vendorCatalogName}" default="isNull"/>
		</c:when>
		<c:otherwise>
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
		</c:otherwise>
	</c:choose>
	<br>
	<br>
<b><fmt:message key="vendorcatalog.vendorcatalog"/> </b>
<br/>
</div>
<br/>
<c:if test="${sessionScope.edit == 'true'}">
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
<div id="user_dept_pnl" class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Add Department(s)
	</div>
	<div class="x-panel-body">
	<spring:bind path="vendorCatalogDepartmentForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				&nbsp;&nbsp;&nbsp;<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

		<c:url value="addDepartment.html" var="formAction">
		<c:param name="cname" value="${cname1}" ></c:param></c:url>
		<form:form commandName="vendorCatalogDepartmentForm" method="post" action="${formAction}" id="vendorCatalogDepartmentForm">
			<input type="hidden" name="id" value="${userId}"/>
			<input type="hidden" name="method" value="addDepartment"/>
			<c:choose>
				<c:when test="${fn:length(departments)== fn:length(userDepartments)}">
						All departments have been selected for this user.
				</c:when>	
				<c:otherwise>
					<c:if test="${not empty departments}">
						<div style="padding:0 0 10px 600px;" class="buttons">
							<secureurl:secureAnchor cssStyle="btn" name="NewVendorCatalog" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="${userId},${mode},${vcID}"/>
							<input class="btn" type="submit" value="Add Department(s)"/>	
						</div>
						<br style="clear:both;" />
						<div class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;clear:both;margin-top:5px;">
							<input type="checkbox" id="chk_select_all" />&nbsp;<strong>Filter:</strong> <input type="text" id="txt_dept_filter" />
							<span id="filter_results"></span>
						</div>
						<ul class="depts_for_add">
							<app:extendedcheckboxes path="departments" items="${departments}" useritems="${userDepartments}" itemValue="deptIdAsString" itemLabel="descriptiveName" element="li" sortBy="deptCd" />
						</ul>
						<div style="padding:10px 0 0 600px;" class="buttons">
							<secureurl:secureAnchor cssStyle="btn" name="NewVendorCatalog" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="${userId},${mode},${vcID}"/>
							<input class="btn" type="submit" value="Add Department(s)"/>
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>
</div>
</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	var $ul=$('ul.depts_for_add');
	var $lis=$('li',$ul);
	$('#filter_results').html($lis.length+' Departments');
	$('#txt_dept_filter').keyup(function(){
		var $this=$(this);
		$lis.hide();
		if($this.val().length>0){
			$('#filter_results').html($('li:contains("'+$this.val().toUpperCase()+'")',$ul).show().length+' Departments');;
		}
		else{
			$lis.show();
			$('#filter_results').html($lis.length+' Departments');
		}
	});
	$('#chk_select_all').click(function(){
    	if($(this).is(':checked')){
    		$('input[type="checkbox"]:not(:checked)').attr('checked','checked');
    	}
    	else{
    		$('input[type="checkbox"]:checked').removeAttr('checked');
    	}
    });
});
</script>
]]>
</content>