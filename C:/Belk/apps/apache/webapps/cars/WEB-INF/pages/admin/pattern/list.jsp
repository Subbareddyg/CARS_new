<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="pattern.title"/></title>
    <meta name="heading" content="<fmt:message key='pattern.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div id="search_for_pattern" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Pattern Search
	</div>
	<div class="x-panel-body">
<c:url value="/admin/pattern/search.html" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="pattern.name"/></label>
				<input type="text" id="vendorStyleName" name="vendorStyleName" value="<c:out value="${vendorStyleName}" />"/>
			</li>
			<li>
				<label><fmt:message key="pattern.style.number"/></label>
				<input type="text" id="vendorStyleNumber" name="vendorStyleNumber" value="<c:out value="${vendorStyleNumber}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="searchPattern" value="<fmt:message key="pattern.button.search"/>" />
				<c:url value="/admin/pattern/search.html" var="formAction"/>
				<a class="btn" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
			</li>
		</ol>
	</form>
</div></div>

<div id="pattern_list_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Patterns
	</div>
	<div class="x-panel-body">
<div class="patternButtons">	
	<a class="btn" href="<c:url value="/admin/pattern/edit.html"/>"><fmt:message key="pattern.button.add"/></a>
</div>

<display:table name="patternList" cellspacing="0" cellpadding="0" requestURI="/admin/pattern/search.html"  
    defaultsort="1" id="patternContent" pagesize="25" class="table" sort="list">
    <display:column  sortable="true" titleKey="pattern.style.number" style="width: 15%">
		<secureurl:secureAnchor name="PatternDetail"  arguments="${patternContent.vendorStyleId},detail" title="${patternContent.vendorStyleNumber}" hideUnaccessibleLinks="false"/>
    </display:column>
    <display:column property="vendorStyleName" sortable="true" titleKey="pattern.name" style="width: 30%"/>    
    <display:column property="vendorStyleType.name" sortable="true" titleKey="pattern.type" style="width: 15%"/>    
    <display:column property="classification.belkClassNumber" sortable="true" titleKey="pattern.class.number" style="width: 10%"/>
    <display:column property="vendorNumber" sortable="true" titleKey="pattern.vendor.number" style="width: 10%"/>    
    <display:column style="width: 10%">
    	<secureurl:secureAnchor name="PatternDetail" title="Detail" localized="true" hideUnaccessibleLinks="true" arguments="${patternContent.vendorStyleId},detail"/>
    </display:column>
    <display:column>
    	<secureurl:secureAnchor name="RemovePattern" cssStyle="remove" elementName="RemovePattern" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${patternContent.vendorStyleId},remove"/>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="pattern"/>
    <display:setProperty name="paging.banner.items_name" value="patterns"/>
</display:table>
</div></div>
</body>

<content tag="inlineStyle">
#pattern_list_pnl{
	margin-top:10px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="pattern.confirm.delete"/>');});
});
</script>
]]>
</content>