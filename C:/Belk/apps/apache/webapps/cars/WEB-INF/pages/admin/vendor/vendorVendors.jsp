<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='userVendors.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR User Details
	</div>
	<div class="x-panel-body">
	<spring:bind path="userForm.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="error">
				<c:forEach var="error" items="${status.errorMessages}">
					<img src="<c:url value="/images/iconWarning.gif"/>"
						alt="<fmt:message key="icon.warning"/>" class="icon" />
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>
	<c:set var="user" value="${userForm.user}" scope="request"/>
	<c:import url="vendorInfo.jsp"/>
</div></div>
<div id="user_vendors_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Associate With Vendor
	</div>
	<div class="x-panel-body">
	<c:url value="/vendor/associateVendorVendors.html" var="formAction"/>
	<form:form commandName="userForm" method="post" action="${formAction}" id="userForm">
			<input type="hidden" name="id" value="${userForm.user.id}"/>
			<input type="hidden" name="method" value="associateVendor"/>
			<c:choose>
				<c:when test="${fn:length(vendors)== fn:length(userVendors)}">
					All vendors have been selected for this user.
				</c:when>
				<c:otherwise>
					<c:if test="${not empty vendors}">
					<br style="clear:both;" />
					<div class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;clear:both;margin-top:5px;">
						<strong>Filter:</strong> <input type="text" id="txt_vendor_filter" />
						<span id="filter_results"></span>
					</div>
					<ul class="vendors_for_add">	
						<app:extendedcheckboxes path="vendors" items="${vendors}" useritems="${userVendors}" itemValue="vendorIdAsString" itemLabel="displayName"  element="li" sortBy="vendorNumber" />
					</ul>
					<div class="buttons" style="padding-top:20px;">
						<input class="btn" id="save" type="submit" value="Associate with Vendor"/>	
					</div>	
					</c:if>
				</c:otherwise>
			</c:choose>		
	</form:form>
</div></div>
</body>

<content tag="inlineStyle">
#user_vendors_pnl{ margin-top:10px; }
ul.vendors_for_add li{
	padding:5px 2px;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	var $ul=$('ul.vendors_for_add');
	$("li").remove(":contains('OUTFIT_VENDOR')");
	var $lis=$('li',$ul);
	$('#filter_results').html($lis.length+' Vendors');
	$('#txt_vendor_filter').keyup(function(){
		var $this=$(this);
		$lis.hide();
		if($this.val().length>0){
			$('#filter_results').html($('li:contains("'+$this.val().toUpperCase()+'")',$ul).show().length+' Vendors');
		}
		else{
			$lis.show();
			$('#filter_results').html($lis.length+' Vendors');
		}
	});
	
	
});
</script>
]]>
</content>
