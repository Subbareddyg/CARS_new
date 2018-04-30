<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="productGroup.title"/></title>
    <meta name="heading" content="<fmt:message key='productGroup.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">
<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Type Group Search
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/productgroupsearch.html?method=Search" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="productGroup.search.name"/></label>
				<input type="text" id="productGroupName" name="productGroupName" value="<c:out value="${productGroupName}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="method" value="Search" />
				<c:url value="/admin/producttype/productGroups.html?method=getAllProductGroups" var="formAction"/>
				<input type="button" name="method" value="View All" class="btn" onclick="location.href='${formAction}'" />				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Type Groups
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
	 <c:if test="${sessionScope.displayRole == 'admin'}">
		<a class="btn" href="<c:url value="/admin/productgroup/productGroupForm.html?method=addProductGroup"/>"><fmt:message key="productGroup.button.add"/></a>
	</c:if>
</div>
<display:table name="productGroupList" cellspacing="0" cellpadding="0" requestURI="/admin/productgroupsearch.html"  
    defaultsort="1" id="prdGrp" pagesize="25"  class="table dstable" sort="list">
    <display:column property="name"  sortable="true" titleKey="productGroup.display.name" style="width: 30%"/>   	     
    <display:column property="description"  sortable="false" titleKey="productGroup.description" style="width: 50%"/>
    <c:if test="${sessionScope.displayRole == 'admin'}">
    	<display:column>
    		<secureurl:secureAnchor name="RemoveProductGroup" cssStyle="remove" elementName="RemoveProductGroup" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${prdGrp.productGroupId}"/>
    	</display:column>
    </c:if>
    <display:column>
    	<secureurl:secureAnchor name="ProductGroupDetail"  arguments="${prdGrp.productGroupId}" title="Detail" hideUnaccessibleLinks="true"/>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="product group"/>
    <display:setProperty name="paging.banner.items_name" value="product groups"/>
</display:table>
</div></div>
</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="productGroup.main.confirm.delete"/>');});
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