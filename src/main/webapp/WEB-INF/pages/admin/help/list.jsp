<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="helpList.title"/></title>
    <meta name="heading" content="<fmt:message key='helpList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Help Content
	</div>
	<div class="x-panel-body">
<div class="helpButtons">	
	<a class="btn" href="<c:url value="/admin/help/helpForm.html"/>"><fmt:message key="help.button.add"/></a>
	<br style="clear:both;" />
</div>
<br/>
<display:table name="helpContentList" cellspacing="0" cellpadding="0" requestURI="helpList.html"  
    defaultsort="1" id="helpContent" pagesize="25" class="table" sort="list">
    <display:column  sortable="true" titleKey="help.content.key" style="width: 25%">
   	 <secureurl:secureAnchor name="HelpContentDetail"  arguments="${helpContent.contentId},detail" title="${helpContent.contentKey}" hideUnaccessibleLinks="false"/>
    </display:column>
    <display:column property="contentSection" sortable="true" titleKey="help.content.section" style="width: 25%"/>    
    <display:column property="contentName" sortable="true" titleKey="help.content.name" style="width: 25%"/>
    <display:column>
    	<secureurl:secureAnchor name="RemoveHelpContent" cssStyle="remove" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${helpContent.contentId},remove"/>
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
	$('a.remove').click(function(){
		return confirm('Are you sure you want to delete this help content?');
	});
});
</script>
]]>
</content>