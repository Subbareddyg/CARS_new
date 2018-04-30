<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="producttype.title"/></title>
    <meta name="heading" content="<fmt:message key='producttype.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">
<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Type Search
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src="<c:url value="/images/iconWarning.gif"/>"
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>
	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/producttypesearch.html" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="productType.product.name"/></label>
				<input type="text" id="productTypeName" name="productTypeName" value="<c:out value="${productTypeName}" />"/>
			</li>
			<li>
				<label><fmt:message key="productType.classification.id"/></label>
				<input type="text" id="classificationId" name="classificationId" value="<c:out value="${classificationId}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="searchProductType" value="Search" />
				<c:url value="/admin/producttype/productTypes.html" var="formAction"/>
				<input type="button" name="viewAll" value="View All" class="btn" onclick="location.href='${formAction}'" />				
				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Types
	</div>
	<div class="x-panel-body">
<div class="userButtons">
	<a class="btn" href="<c:url value="/admin/producttype/productTypeForm.html"/>"><fmt:message key="product.button.add"/></a>
</div>
<display:table name="productList" cellspacing="0" cellpadding="0" requestURI="/admin/producttypesearch.html"  
    defaultsort="1" id="prd" pagesize="25" class="table" sort="list">
    <display:column property="name"  sortable="true" titleKey="product.name" style="width: 30%"/>   	     
    <display:column property="description"  sortable="false" titleKey="productType.description" style="width: 50%"/>   	 
    <display:column >
    	<secureurl:secureAnchor name="RemoveProduct" cssStyle="remove" elementName="RemoveProduct" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${prd.productTypeId},remove"/>
    </display:column>
    <display:column>
    	<secureurl:secureAnchor name="ProductDetail"  arguments="${prd.productTypeId}" title="Detail" hideUnaccessibleLinks="true"/>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="product"/>
    <display:setProperty name="paging.banner.items_name" value="products"/>
</display:table>
</div></div>
</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="producttype.main.confirm.delete"/>');});
});
</script>
]]>
</content>