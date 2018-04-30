<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="productGroup.profile.title.details" /></title>
<meta name="heading" content="<fmt:message key='productGroup.heading'/>" />
<meta name="menu" content="UserMenu" />
</head>

<body class="admin">
<div class="cars_panel x-hidden">
<div class="x-panel-header"><fmt:message key='productGroup.heading'/></div>
<div class="x-panel-body">
<c:import url="productGroupInfo.jsp"/>
</div></div>
<div id="productType_class_pnl" class="cars_panel x-hidden">
<div class="x-panel-header">Verify Product Type Change</div>
	<div class="x-panel-body">
	<c:forEach var="message" items="${sessionScope.successMessages}">
		<c:if test="${message == 'Saved Successfully.'}">
		
			<span style="background: #FFFF00;"><c:out value="${message}"></c:out>	</span>
		</c:if>
	</c:forEach>
	
	<div class="attributeButtons">
		<a class="btn"
		href='<c:url value="/admin/productgroup/associateGroupProductType.html?method=getNonAssociatedProducts&amp;id=${productTypeGroupForm.id}"/>'
		title="Back to Product Type List"> < <fmt:message key="button.back" />  </a>
	</div>
	
	<c:url value='associateGroupProductType.html?method=save' var="formAction"/>
	<form:form commandName="productTypeGroupForm" method="post" action="${formAction}" id="productTypeGroupForm" name="productTypeGroupForm">
		<input type="hidden" name="id" value="${productTypeGroupForm.id}"/>
		<input type="hidden" name="action" value="${productTypeGroupForm.action}"/>
	</form:form>	

	<display:table name="productTypeGroupForm.productTypesList" cellspacing="0" cellpadding="0" requestURI="/admin/productgroup/associateGroupProductTypeConfirm.html"  
	    defaultsort="1" id="prdGrp" pagesize="25" class="table" sort="list">
	    <display:column property="productTypeId"  sortable="false" title="Product Type Id" style="width: 20%"/>
	    <display:column property="name"  sortable="false" title="Product Type" style="width: 20%"/>   	     
	    <display:column property="productGroupDesc"  sortable="false" title="Old Group Association" style="width: 30%"/>   	 
	    <display:column property="newProdGroup"  sortable="false" title="New Group Association" value="1111" style="width: 70%"/>   	 
	    <display:setProperty name="paging.banner.item_name" value="product group association"/>
	    <display:setProperty name="paging.banner.items_name" value="product group associations"/>
	</display:table>
	
	</div>


</div>
</div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="productGroup.confirm.delete"/>');});
});

</script>
]]>
</content>