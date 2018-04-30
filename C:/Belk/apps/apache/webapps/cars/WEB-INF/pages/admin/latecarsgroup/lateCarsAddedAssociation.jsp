<%@ include file="/common/taglibs.jsp"%>

<c:set var="AddAssociation" value="${lateCarsAssociationDetails}" />
	<display:table name="${AddAssociation}" cellspacing="0"
		cellpadding="0" requestURI="" defaultsort="1" id="LcaDetails" pagesize="1000">
		<display:column value="${LcaDetails.deptNO}"
			titleKey="lateCarsAssociation.Department.Number" style="width:10%;" />
		<display:column value="${LcaDetails.deptName}"
			titleKey="lateCarsAssociation.Department.Name" style="width: 20%;" />
		<display:column value="${LcaDetails.vendorNo}"
			titleKey="lateCarsAssociation.Vendor.Number"
			style="width: 20%;" />
		<display:column value="${LcaDetails.vendorName}"
			titleKey="lateCarsAssociation.Vendor.Name"
			style="width: 40%; " />

		<c:if test="${sessionScope.displayRole == 'admin'}">
			<display:column style="width: 10%;">					
					<a href="javascript:void(0)" onclick="modalDialog(this);" class="aaf" id="test_${LcaDetails.deptIds}_${LcaDetails.lateCarsAssociationID}">Remove</a>
			</display:column>
		</c:if>
	</display:table>
