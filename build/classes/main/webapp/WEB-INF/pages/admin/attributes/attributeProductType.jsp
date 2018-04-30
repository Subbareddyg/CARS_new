<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Associate Attribute With Product Type</title>
	<meta name="heading" content="<fmt:message key='attribute.product.type.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Attribute Details
	</div>
	<div class="x-panel-body">
<c:set var="attribute" value="${attributeProductTypeForm.attribute}" scope="request"/>
<div class="attributeButtons">	
	<secureurl:secureAnchor cssStyle="btn" name="AttributeEdit" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="${attribute.attributeId}"/>
</div>
<c:import url="attributeInfo.jsp"/>
</div></div>

<div id="attr_prod_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Associate With Product Types
	</div>
	<div class="x-panel-body">
<spring:bind path="attributeProductTypeForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				&nbsp;&nbsp;&nbsp; <br><img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<c:url value="associateProductTypeAttribute.html" var="formAction"/>	
<form:form commandName="attributeProductTypeForm" method="post" action="${formAction}" id="attributeProductTypeForm">
		<input type="hidden" name="productTypeAttrId" value="${attributeProductTypeForm.attribute.attributeId}"/>
		<input type="hidden" name="action" value="associateWithProductType"/>
		<c:choose>
			<c:when test="${fn:length(attributeProductTypes)==fn:length(productTypes)}">
				All Product types are already associated with this attribute
			</c:when>	
			<c:otherwise>
			  <c:if test="${not empty productTypes}">
				<div style="padding:0 0 10px 600px;" class="buttons">
					<input class="btn" type="submit" value="Add Product Type(s)"/>	
				</div>
				<div class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;">
					<strong>Filter:</strong> <input type="text" id="txt_producttypes_filter" />
					<span id="filter_results"></span>
				</div>
			  	<ul class="depts_for_add">
					<app:extendedcheckboxes  useritems="${attributeProductTypes}" path="products" items="${productTypes}"  itemValue="productIdAsString" itemLabel="name" element="li" sortBy="name"/><br/><br/>
				</ul>
				<div style="padding:10px 0 0 600px;" class="buttons">
					<input class="btn" id="save" type="submit" value="Add Product Type(s)"/>	
				</div>
				</c:if>	
			</c:otherwise>
		</c:choose>	
</form:form>
</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	var $ul=$('ul.depts_for_add');
	var $lis=$('li',$ul);
	$('#filter_results').html($lis.length+' Product Types');
	$('#txt_producttypes_filter').keyup(function(){
		var $this=$(this);
		$lis.hide();
		if($this.val().length>0){
			$('#filter_results').html($('li:contains("'+$this.val()+'")',$ul).show().length+' Product Types');;
		}
		else{
			$lis.show();
			$('#filter_results').html($lis.length+' Product Types');
		}
	});
});
</script>
]]>
</content>