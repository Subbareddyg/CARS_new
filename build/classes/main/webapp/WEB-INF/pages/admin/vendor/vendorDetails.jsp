<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="heading" content="<fmt:message key='vendorDetails.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Vendor Details
	</div>
	<div class="x-panel-body">
<c:import url="vendorInfo.jsp"/>
</div></div>
<div id="user_depts_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Vendor Departments
	</div>
	<div class="x-panel-body">
<c:url var="showDepartments" value="/vendor/addDepartment.html">
	<c:param name="id" value="${user.id}"/>    		
</c:url>
<ul class="departments">
	<li >
		<a class="btn" href="<c:out value="${showDepartments}" escapeXml="false" />"><fmt:message key="vendors.add.to.department"/></a>
		<br style="clear:both;" />
	</li>
	<li>
	<c:choose>
		<c:when test="${not empty user.departments}"> 
		<display:table name="user.departments" cellspacing="0" cellpadding="0" requestURI="" 
		    defaultsort="1" id="usrdept" pagesize="25" class="table">
		    <display:column property="deptCd" titleKey="department.deptCd"/>
		    <display:column property="name" titleKey="department.name"/>
		    <display:column property="description" titleKey="department.descr"/>
		 	 <display:column>    		
				<secureurl:secureAnchor name="RemoveVendorDepartment" cssStyle="removeDepartment" elementName="RemoveVendorDepartment"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrdept.deptId},${user.id},remove"  />	    	
			</display:column>
		    <display:setProperty name="paging.banner.item_name" value="department"/>
		    <display:setProperty name="paging.banner.items_name" value="departments"/>
		</display:table>
		</c:when>
		<c:otherwise>
		No Departments found
		</c:otherwise>
	</c:choose>
	</li>	
</ul>
</div></div>
<br/>
<c:if test="${user.userType.name == 'Vendor'}">
<div id="user_vendors_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Vendor Vendors
	</div>
	<div class="x-panel-body">
<ul class="vendors">
	<li>
		<c:url var="showVendorVendors" value="/vendor/associateVendorVendors.html">
			<c:param name="id" value="${user.id}"/>    		
		</c:url>
		<a class="btn" href="<c:out value="${showVendorVendors}" escapeXml="false" />"><fmt:message key="vendor.add.to.vendors"/></a>
		<br style="clear:both;" />
	</li>
	<li>
	<c:choose>
		<c:when test="${not empty user.vendors}">
		<display:table name="user.vendors" cellspacing="0" cellpadding="0" requestURI="" 
		    defaultsort="1" id="usrvnd" pagesize="25" class="table">
		    <display:column property="vendorNumber" title="Vendor Number"/>
		    <display:column property="name" title="Vendor Name"/>
		    <display:column property="descr" title="Description"/>
		    <display:column>    		
			 	<secureurl:secureAnchor name="RemoveVendorVendors" cssStyle="removeVendors" elementName="RemoveVendorVendors"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrvnd.vendorId},${user.id},remove"/>		   	
		    </display:column>
		    <display:setProperty name="paging.banner.item_name" value="vendor"/>
		    <display:setProperty name="paging.banner.items_name" value="vendors"/>
		</display:table>	
		</c:when>
		<c:otherwise>
		No Vendors were found
		</c:otherwise>
	</c:choose>
	</li>
</ul>
</div></div>
</c:if>
<br/>

<div id="user_depts_vendors_pnl" class="cars_panel x-hidden">
    <div class="x-panel-header">
        Send CARS Direct to Vendors
    </div>
    
    
    
    	<div class="x-panel-body">
		<ul class="vendors">
		<li>
			<c:url var="userVendorDeptDetail" value="/vendor/vendorDeptForm.html">
				<c:param name="id" value="${user.id}"/>    		
			</c:url>
			<a class="btn" href="<c:out value="${userVendorDeptDetail}" escapeXml="false" />"><fmt:message key="button.add.sendcars"/></a>
			<br style="clear:both;" />
		</li>
	<li>
	    <c:choose>
	        <c:when test="${not empty vendorDepts}">
	            <display:table name="vendorDepts" cellspacing="0" cellpadding="0" requestURI=""
	                defaultsort="1" id="usrvnddept" pagesize="25" class="table">
	                <display:column property="dept.deptCd" title="DeptCd."/>
	                <display:column property="vendor.vendorNumber" title="Vendor #"/>
	                <display:column>
	                     <secureurl:secureAnchor
								name="RemoveDeptVendors" cssStyle="removeDeptVendors"
								elementName="RemoveDeptVendors"  localized="true" 
								hideUnaccessibleLinks="true" arguments="${usrvnddept.carUserVendorDepartmentId},${user.id},remove"/>
	                </display:column>
	            </display:table>
	        </c:when>
	        <c:otherwise>
	            No Vendors were found
	        </c:otherwise>
	    </c:choose>
	</li>
</ul>
</div></div>

</body>

<content tag="inlineStyle">
#user_depts_pnl{ margin-top:10px; }
textarea.note_text{
	height:100px;
	width:309px;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.removeDepartment').click(function(){return confirm('<fmt:message key="vendor.department.confirm.delete"/>');});
	$('a.removeVendors').click(function(){return confirm('<fmt:message key="user.vendor.confirm.delete"/>');});
	$('a.removeDeptVendors').click(function(){return confirm('<fmt:message key="user.vendor.department.confirm.delete"/>');});
});
</script>
]]>
</content>
