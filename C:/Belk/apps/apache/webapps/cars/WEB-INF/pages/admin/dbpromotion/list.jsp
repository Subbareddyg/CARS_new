<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="dbPromotion.title"/></title>
    <meta name="heading" content="<fmt:message key='dbPromotion.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">
<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Deal Based Promotions Search
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/dbpromotion/dbPromotionSearch.html" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			
			<li>
				<label><fmt:message key="dbPromotion.name"/></label>:
				<input type="text" id="dbPromotionName" name="dbPromotionName" value="<c:out value="${dbPromotionName}" />"/>
			</li>
			<li>
				<label><fmt:message key="dbPromotion.style"/></label>:
				<input type="text" id="styleNumber" name="styleNumber" value="<c:out value="${styleNumber}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="method" value="Search" />
				<c:url value="/admin/dbpromotion/dbPromotions.html?method=getAllDBPromotionCars" var="formAction"/>
				<input type="button" name="method" value="View All" class="btn" onclick="location.href='${formAction}'" />				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Deal Based Promotions
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
		<a class="btn" href="<c:url value="/admin/dbpromotion/populateData.html?method=getTemplateType"/>"><fmt:message key="dbPromotion.button.create"/></a>
</div>
<display:table name="promotionList" cellspacing="0" cellpadding="0" requestURI="/admin/dbpromotion/dbPromotionSearch.html"  
    defaultsort="1" defaultorder="descending" id="dbPromotion" pagesize="25"  class="table dstable" sort="list">
    <display:column property="carId"  sortable="true" titleKey="dbPromotion.car" style="width: 15%"/>  
    <display:column property="vendorStyle.vendorStyleName"  sortable="true" titleKey="dbPromotion.name" style="width: 25%"/>  
    <display:column property="vendorStyle.vendorStyleNumber"  sortable="true" titleKey="dbPromotion.style" style="width: 25%"/>  
    <display:column property="createdBy"  sortable="true" titleKey="dbPromotion.created.by" style="width: 25%"/>  
  	
    <display:column style="width: 15%">
	    	<secureurl:secureAnchor name="EditDBPromotion"  cssStyle="edit" elementName="EditDBPromotion"  title="Edit" localized="true" hideUnaccessibleLinks="true" arguments="${dbPromotion.carId}"/>
    </display:column>
    
    <display:column>
		    <secureurl:secureAnchor name="RemoveDBPromotion" cssStyle="remove" elementName="RemoveDBPromotion" title="Remove"  hideUnaccessibleLinks="true" arguments="${dbPromotion.carId}"/>
	</display:column>
	
    <display:setProperty name="paging.banner.item_name" value="Promotion car"/>
    <display:setProperty name="paging.banner.items_name" value="Promotion cars"/>
</display:table>
</div></div>

</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="dbPromotion.main.confirm.delete"/>');});
	
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