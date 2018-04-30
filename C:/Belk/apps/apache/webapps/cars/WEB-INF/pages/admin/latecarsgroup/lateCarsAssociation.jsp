<%@ include file="/common/taglibs.jsp"%>

<head>
<title><fmt:message key="userProfile.title" /></title>
<meta name="heading" content="<fmt:message key='userVendors.heading'/>" />
<meta name="menu" content="UserMenu" />
</head>
<body class="admin">
	<h3>Late Car Group Association</h3>
	<div id="vendor_list" class="">
		<jsp:include page="lateCarsAssociationVendor.jsp" />
	</div>
	<div>
		<div id="department_list" class="cars_panel x-hidden">
			<jsp:include page="lateCarsAssociationDepartment.jsp" />
		</div>
		<br> <input class="btn" type="submit" id="AddAssociationTemp"
			value="Add Association" />

		<div style="float: right;">
			<c:if test="${sessionScope.displayRole == 'admin'}">
				<secureurl:secureAnchor cssStyle="btn" arguments="${lateGroupID}"
					name="BackLateCarAssociation" elementName="BackLateCarAssociation"
					title="Back" localized="true" hideUnaccessibleLinks="true" />
			</c:if>
		</div>
		<br> <br>
		
				<div id="addAssociation_list" class="cars_panel x-hidden">
						<div class="x-panel-header">
							Added Associations		
						</div>
						<div class="x-panel-body">
						
								<div id="addAssociationDiv">
											<jsp:include page="lateCarsAddedAssociation.jsp" />
								</div>
						</div>
				</div>
		
		
		
	</div>
</body>

<content tag="inlineStyle"> #searchValidateEr{color:red;}
#user_vendors_pnl{ margin-top:10px; } ul.vendors_for_add li{ padding:5px
2px; } </content>

<content tag="jscript"> <![CDATA[
<script type="text/javascript" src="<c:url value='/js/belk.cars.latecargroup.js'/>"></script>
]]> </content>