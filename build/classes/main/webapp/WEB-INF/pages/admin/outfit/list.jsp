<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="outfit.title"/></title>
    <meta name="heading" content="<fmt:message key='outfit.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">
<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Outfit Search
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/outfit/outfitSearch.html" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			
			<li>
				<label><fmt:message key="outfit.name"/></label>:
				<input type="text" id="outfitName" name="outfitName" value="<c:out value="${outfitName}" />"/>
			</li>
			<li>
				<label><fmt:message key="outfit.style"/></label>:
				<input type="text" id="styleNumber" name="styleNumber" value="<c:out value="${styleNumber}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="method" value="Search" />
				<c:url value="/admin/outfit/outfits.html?method=getAllOutfitCars" var="formAction"/>
				<input type="button" name="method" value="View All" class="btn" onclick="location.href='${formAction}'" />				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Outfits
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
		<a class="btn" href="<c:url value="/admin/outfit/populateData.html?method=getTemplateType"/>"><fmt:message key="outfit.button.create"/></a>
</div>
<display:table name="outfitList" cellspacing="0" cellpadding="0" requestURI="/admin/outfit/outfitSearch.html"  
    defaultsort="1" defaultorder="descending" id="outfit" pagesize="25"  class="table dstable" sort="list">
    <display:column property="carId"  sortable="true" titleKey="outfit.car" style="width: 15%"/>  
    <display:column property="vendorStyle.vendorStyleName"  sortable="true" titleKey="outfit.name" style="width: 25%"/>  
    <display:column property="vendorStyle.vendorStyleNumber"  sortable="true" titleKey="outfit.style" style="width: 25%"/>  
    <display:column property="createdBy"  sortable="true" titleKey="outfit.created.by" style="width: 25%"/>  
  	
    <display:column style="width: 15%">
	    	<secureurl:secureAnchor name="EditOutfit"  cssStyle="edit" elementName="EditOutfit"  title="Edit" localized="true" hideUnaccessibleLinks="true" arguments="${outfit.carId}"/>
    </display:column>
    
    <display:column>
		    <secureurl:secureAnchor name="RemoveOutfit" cssStyle="remove" elementName="RemoveOutfit" title="Remove"  hideUnaccessibleLinks="true" arguments="${outfit.carId}"/>
	</display:column>
	
    <display:setProperty name="paging.banner.item_name" value="outfit car"/>
    <display:setProperty name="paging.banner.items_name" value="outfit cars"/>
</display:table>
</div></div>

</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="outfit.main.confirm.delete"/>');});
	
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