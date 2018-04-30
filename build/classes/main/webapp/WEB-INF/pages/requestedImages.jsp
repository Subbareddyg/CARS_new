<%@ include file="/common/taglibs.jsp"%>

<%-- this is the ajax return for mark received, edit and remove images --%>
<body>
<c:choose>
<c:when
	test="${user.userType.userTypeCd == 'SAMPLE_COORDINATOR' || user.userType.userTypeCd == 'ART_DIRECTOR'}">
		<c:set var="incImages" value="${detailCar.allNonVendorActiveCarImages}" scope="request"/>
		<jsp:include page="imageManagement/artDirectorImagesTable.jsp"/>
</c:when>
<c:when test="${action=='AddOnHand'}">
	<c:set var="incImages" value="${detailCar.activeCarImages}" scope="request"/>
	<jsp:include page="imageManagement/onhandImagesTable.jsp"/>
</c:when>
<c:otherwise>
	<c:set var="incImages" value="${detailCar.activeCarRequestedImages}" scope="request"/>
	<jsp:include page="imageManagement/vendorUploadImageList.jsp"/>
</c:otherwise>
</c:choose>
	
</body>