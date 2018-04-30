<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="producttype.profile.title.details"/></title>
    <meta name="heading" content="<fmt:message key='product.type.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Type
	</div>
	<div class="x-panel-body">
		<div class="attributeButtons">
			 <a class="btn" href="<c:url value="/admin/producttype/productTypeForm.html?from=list&id=${product.productTypeId}" />" title="Edit Product Type"><fmt:message key="button.edit"/></a>
			<a class="btn" href="<c:url value='/admin/producttypesearch.html?${productTypeSearchCriteria.criteriaAsQueryString}'/>" title="Back to Product Type List"><fmt:message key="button.back"/></a>	
		</div>
		<c:import url="productInfo.jsp"/>
</div></div>
<div id="productType_class_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Classifications
	</div>
	<div class="x-panel-body">
<secureurl:secureAnchor cssStyle="btn" name="AssociateProductTypeToClass" title="Associate With Classification" localized="true"  hideUnaccessibleLinks="true" arguments="${product.productTypeId},associateWithClass"/>
<br/><br/>
<c:choose>
<c:when test="${not empty product.classifications}"> 
<fmt:message key="producttype.details.classifications"/> &nbsp;   	
<display:table name="product.classifications" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="classification" pagesize="25" class="table">
    <display:column property="belkClassNumber" style="width: 15%"/>
    <display:column property="name" style="width: 25%"/>
    <display:column property="descr"/>
 	 <display:column>    		
		<secureurl:secureAnchor cssStyle="remove" name="RemoveClassProductType" elementName="RemoveClassProductType" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${classification.classId},${product.productTypeId},remove"/>	    	
	</display:column>
    <display:setProperty name="paging.banner.item_name" value="classification"/>
    <display:setProperty name="paging.banner.items_name" value="classifications"/>
</display:table>
</c:when>
<c:otherwise>
<fmt:message key="producttype.details.no.classifications.found"/>
</c:otherwise>
</c:choose>
</div></div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="classification.confirm.delete"/>');});
});
</script>
]]>
</content>