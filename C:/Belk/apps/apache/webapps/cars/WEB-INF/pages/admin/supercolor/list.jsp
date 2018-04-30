<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="supercolor.title"/></title>
    <meta name="heading" content="<fmt:message key='supercolor.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">


<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Super Color Search:
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/supercolor/superColorSearch.html?method=Search" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			
			<li>
				<label><fmt:message key="supercolor.name"/></label>:
				<input type="text" id="superColorName" name="superColorName" value="<c:out value="${superColorName}" />"/>
			</li>
			<li>
				<label><fmt:message key="supercolor.code"/></label>:
				<input type="text" id="superColorCode" name="superColorCode" value="<c:out value="${superColorCode}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="method" value="Search" />
				<c:url value="/admin/supercolor/supercolors.html?method=getAllSuperColors" var="formAction"/>
				<input type="button" name="method" value="View All" class="btn" onclick="location.href='${formAction}'" />				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Super Colors:
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
		<a class="btn" href="<c:url value="/admin/supercolor/edit.html"/>"><fmt:message key="supercolor.create"/></a>
</div>

<display:table name="superColorList" cellspacing="0" cellpadding="0" requestURI="/admin/supercolor/superColorSearch.html"  
    defaultsort="1" defaultorder="descending" id="supercolor" pagesize="25"  class="table dstable" sort="list">
    <display:column property="superColorCode"  sortable="true" titleKey="supercolor.code" style="width: 15%"/>  
    <display:column property="superColorName"  sortable="true" titleKey="supercolor.name" style="width: 25%"/>  
    <display:column sortable="true" titleKey="supercolor.range" style="width: 25%">
     <c:out value="${supercolor.colorCodeBegin}"/> 
     <c:out value="-"/> 
     <c:out value="${supercolor.colorCodeEnd}"/>
    </display:column>  
    <display:column sortable="true" titleKey="supercolor.created.by" style="width: 25%" >  <c:out value="${supercolor.createdBy}"/> </display:column>   
  	
  	 <display:column sortable="true" titleKey="size.created.date" style="width: 25%" > 
    			<fmt:formatDate pattern="MM/dd/yyyy" value="${supercolor.createdDate}" />
    </display:column>
    <display:column sortable="true" titleKey="size.updated.date" style="width: 25%" > 
    	<fmt:formatDate pattern="MM/dd/yyyy" value="${supercolor.updatedDate}" /> 
    </display:column>  
    
    
    <display:column style="width: 15%">
	    	<secureurl:secureAnchor name="EditSuperColor"  cssStyle="edit" elementName="EditSuperColor"  title="Edit" localized="true" hideUnaccessibleLinks="true" arguments="${supercolor.cmmId}"/>
    </display:column>
    
    <display:column>
		    <secureurl:secureAnchor name="RemoveSuperColor" cssStyle="remove" elementName="RemoveSuperColor" title="Remove"  hideUnaccessibleLinks="true" arguments="${supercolor.cmmId}"/>
	</display:column>
	
    <display:setProperty name="paging.banner.item_name" value="super color"/>
    <display:setProperty name="paging.banner.items_name" value="super colors"/>
</display:table>
</div></div>

</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="supercolor.main.confirm.delete"/>');});
	
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