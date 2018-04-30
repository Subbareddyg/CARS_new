<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="lateCarsGroup.title" /></title>
<meta name="heading"
	content="<fmt:message key='lateCarsGroup.default.heading'/>" />
<meta name="menu" content="AdminMenu" />
</head>


<body class="admin">

	<div id="group_details_list" class="cars_panel x-hidden">
		<div class="x-panel-header">Late CAR Group Details</div>
		<div class="x-panel-body">
			<%@ include file="/common/messages.jsp"%>
			<div class="userButtons">
				<c:if test="${sessionScope.displayRole == 'admin'}">
					<a class="btn"
						href="<c:url value="/admin/latecarsgroup/addLateCarsParams.html">
	 <c:param name="lateCarsGroupId" value="${lateCarsGroupDetails.lateCarsGroupId}"/> </c:url>"><fmt:message
							key="lateCarsGroup.button.add.params" /> </a>
					<secureurl:secureAnchor cssStyle="btn" name="LateCarList"
						title="Back" localized="true" hideUnaccessibleLinks="true" />
				</c:if>
			</div>


			<display:table name="lateCarsGroupDetails.lateCarsParams"
				cellspacing="0" cellpadding="0" requestURI="" defaultsort="1"
				id="lateCarParams" pagesize="25" class="table">

				<display:column value="${lateCarParams.owner.name}"
					titleKey="lateCarsGroup.owner.name" style="width: 30%" />
				<display:column value="${lateCarParams.status.name}"
					titleKey="lateCarsGroup.owner.status" style="width: 30%" />
				<display:column value="${lateCarParams.weeksdue}"
					titleKey="lateCarsGroup.owner.weeksdue" style="width: 30%" />
				<c:if test="${sessionScope.displayRole == 'admin'}">
					<display:column>
						<secureurl:secureAnchor name="EditLateCarsParams"
							elementName="EditLateCarsParams" title="Edit" localized="true"
							hideUnaccessibleLinks="true"
							arguments="${lateCarParams.lateCarsParamId},${lateCarsGroupDetails.lateCarsGroupId}" />
					</display:column>
					<display:column>
						<secureurl:secureAnchor name="RemoveLateCarsParams"
							cssStyle="removeLateCarsParams"
							elementName="RemoveLateCarsParams" title="Remove"
							localized="true" hideUnaccessibleLinks="true"
							arguments="${lateCarParams.lateCarsParamId},${lateCarsGroupDetails.lateCarsGroupId}" />
					</display:column>
				</c:if>


			</display:table>
			<br />
		</div>
	</div>
	<br>
	<c:if test="${(lateCarsGroupDetails.lateCarsGroupId != '1') && (sessionScope.displayRole == 'admin')}">
				<a class="btn"
					href="<c:url value="/admin/latecarsgroup/addLateCarsAssociation.html">
											<c:param name="lateCarsGroupId" value="${lateCarsGroupDetails.lateCarsGroupId}"/>
				                            </c:url>">
					<fmt:message key="lateCarsGroup.button.add.assoc" />
				</a>
    </c:if>
</body>


<content tag="jscript"> <![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.removeLateCarsParams').click(function(){return confirm('<fmt:message key="lateCarsGroup.param.confirm.delete"/>');});
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


$(document).ready(function(){
	$('a.removeLateCarsDepts').click(function(){return confirm('<fmt:message key="lateCarsGroup.dept.confirm.delete"/>');});
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
]]> </content>