<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="attributevalues.attr.title"/></title>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Attributes
	</div>
	<div class="x-panel-body">
<form:form commandName="attributeForm" method="post"
		action="${formAction}" id="attributeForm">
<div class="attributeButtons">
	<input class="btn" type="submit" name="action" value="<fmt:message key='button.accept.attribute.values'/>" />
	<input class="btn" type="submit" name="action" value="<fmt:message key='button.reject.attribute.values'/>" />			
</div>
<c:set var="checkBoxTitle">
<input type="checkbox" id="chk_select_all" />
</c:set>
<display:table name="attributeForm.missingAttributeValues" cellspacing="0" cellpadding="0" requestURI="/admin/attributes/attributeValues.html"  
    defaultsort="1" id="missingAttr" pagesize="25" class="table" sort="list">
    <display:column title="${checkBoxTitle}">
    	<form:checkbox path="attributeIds" label="" value="${missingAttr.carAttributeId}" />
    	<input type="hidden" value="${missingAttr.carAttributeId}" name="carAttributeId${missingAttr.carAttributeId}"/>
    	<input type="hidden" value="${missingAttr.carAttributeIds}" name="carAttributeIds${missingAttr.carAttributeId}"/>
    	<input type="hidden" value="${missingAttr.oldValue}" name="oldValue${missingAttr.carAttributeId}"/>
    </display:column>
    <display:column property="attribute.name" sortable="true" titleKey="attributevalues.attr.name" url="/admin/attributes/details.html?" paramId="id" paramProperty="attribute.attributeIdAsString"/> 
    <display:column property="attribute.attributeConfig.displayName" sortable="true" titleKey="attributevalues.attr.label"/>  
    <display:column property="attribute.blueMartiniAttribute" sortable="true" titleKey="attributevalues.attr.bm.name"/>  
	<display:column  titleKey="attributevalues.attr.value" style="width:400px">
		<input type="text" class="text" name="v_<c:out value="${missingAttr.carAttributeId}" />" value="<c:out value="${missingAttr.value}" />"/>
		<a href="#" class="fix_case" title="Fix Case (to Proper Case)">Fix</a>
	</display:column>
    <display:setProperty name="paging.banner.item_name" value="attribute value"/>
    <display:setProperty name="paging.banner.items_name" value="attribute values"/>
    <display:setProperty name="paging.banner.all_items_found">
    	<span class="pagebanner">
			{0} {1} <fmt:message key="attributevalues.attr.values.added"/>
		</span>
    </display:setProperty>
     <display:setProperty name="paging.banner.one_item_found">
    	<span class="pagebanner">
			One {0} <fmt:message key="attributevalues.attr.value.added"/>
		</span>
    </display:setProperty>
</display:table>
<br/><br/>
<div class="attributeButtons">
	<input class="btn" type="submit" name="action" value="<fmt:message key='button.accept.attribute.values'/>" />
	<input class="btn" type="submit" name="action" value="<fmt:message key='button.reject.attribute.values'/>" />			
</div>	
</form:form>
</div></div><br /><br /></body>

<content tag="inlineStyle">
#attr_value_list{
	margin-top:10px;
}
#missingAttr input.text{
	width:350px;
}
#missingAttr a.fix_case{
	padding-left:10px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
    var toProperCase = function(s){
		return s.toLowerCase().replace(/^(.)|\s(.)/g, 
	          function($1) { return $1.toUpperCase(); });
	}
	var removeExtraWhitespace=function(s){
		return s.replace(/^\s+|\s+$/g,'').replace(/\s+/g,' ');
	}
    $('a.fix_case').click(function(ev){
    	var $t=$('input.text',$(this).parent())
    	$t.val(toProperCase(removeExtraWhitespace($t.val())));
    	ev.preventDefault();
    });
    $('#chk_select_all').click(function(){
    	if($(this).is(':checked')){
    		$('input[type="checkbox"]:not(:checked)').attr('checked','checked');
    	}
    	else{
    		$('input[type="checkbox"]:checked').removeAttr('checked');
    	}
    });
});
</script>
]]>
</content>