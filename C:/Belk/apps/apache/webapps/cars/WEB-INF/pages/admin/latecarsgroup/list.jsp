<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="lateCarsGroup.title"/></title>
    <meta name="heading" content="<fmt:message key='lateCarsGroup.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">
<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Late CAR Group Search
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/latecarsgroupsearch.html?method=Search" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			
			<li>
				<label><fmt:message key="lateCarsGroup.search.name"/></label>
				<input type="text" id="lateCarsGroupName" name="lateCarsGroupName" value="<c:out value="${lateCarsGroupName}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="method" value="Search" />
				<c:url value="/admin/latecarsgroup/lateCarsGroup.html?method=getAllLateCarsGroup" var="formAction"/>
				<input type="button" name="method" value="View All" class="btn" onclick="location.href='${formAction}'" />				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Late CARS Groups
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
	 <c:if test="${sessionScope.displayRole == 'admin'}">
		<a class="btn" href="<c:url value="/admin/latecarsgroup/addLateCarsGroup.html?method=addLateCarsGroupView"/>"><fmt:message key="lateCarsGroup.button.add"/></a>
	</c:if>
</div>
<display:table name="lateCarsGroupList" cellspacing="0" cellpadding="0" requestURI="/admin/latecarsgroupsearch.html?"  
    defaultsort="1" id="lateCarsGrp" pagesize="25"  class="table dstable" sort="list">
    <display:column property="name"  sortable="true" titleKey="lateCarsGroup.display.name" style="width: 30%"/>  
    
    <!-- checking  lateCarsGroupList is empty or not for the displaying 'Remove' for 'Default' group-->
    
    <c:if test="${not empty lateCarsGroupList}">
    
	     <c:set var="defaultGroup" value="${lateCarsGrp.name}" />
	     <jsp:useBean id="defaultGroup" type="java.lang.String" />
	   <display:column>  
		<c:choose>
			<c:when test="${sessionScope.isCopyTextAllowed == 'true' && sessionScope.isBuyer == 'true'}">
				Remove
			</c:when>
		    <c:otherwise>	
				<c:choose>
					<c:when test="${defaultGroup == 'Default' || defaultGroup == 'OUTFIT' }">  
							Remove
					</c:when>
					<c:otherwise>
							<secureurl:secureAnchor name="RemoveLateCarsGroup" cssStyle="remove" elementName="RemoveLateCarsGroup" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${lateCarsGrp.lateCarsGroupId}"/>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	    </display:column>
	    
	   <display:column>
		    <c:choose>
			    	<c:when test='<%=defaultGroup.equalsIgnoreCase("OUTFIT")%>'>  
			    			Detail
			   		</c:when>
			    	<c:otherwise>
				    		<secureurl:secureAnchor name="LateCarsGroupDetail"  arguments="${lateCarsGrp.lateCarsGroupId}" title="Detail" hideUnaccessibleLinks="true"/>
			    	</c:otherwise>
		      </c:choose> 
	    </display:column>
	    <display:setProperty name="paging.banner.item_name" value="late cars group"/>
	    <display:setProperty name="paging.banner.items_name" value="late cars groups"/>
	    
    </c:if>
    
</display:table>
</div></div>

</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="lateCarsGroup.main.confirm.delete"/>');});
	
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