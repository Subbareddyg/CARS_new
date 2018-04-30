<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="search.heading" /></title>
    <meta name="heading" content="<fmt:message key='search.heading'/>"/>
    <meta name="menu" content="Search"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<div>
<c:url value="/search/search.html" var="formAction"/>
	<form action="${formAction}" id="searchForm">
		<ol>
			<li>
				Car ID:<input type="text" name="carid" id="carid" value="<c:out value="${context.carid}"/>"/>
				Department CD:<input type="text" name="deptcd" id="deptcd" value="<c:out value="${context.deptcd}"/>"/>
				Vendor Style Number: <input type="text" name="vendorStyleNumber" id="vendorStyleNumber" value="<c:out value="${context.vendorStyleNumber}"/>"/>
			</li>
			<li>
				<input type="submit" class="" name="search" value="<fmt:message key="button.search"/>" />
			</li>
		</ol>
	</form>
</div>

<div>
	<hr>
	<b><c:out value="${context.noofcarsfound}"/></b> Search Results Found
	<hr>
</div>

<div>
	<ul>
	<c:forEach var="car" items="${context.cars}">
		<li>
		Car ID: <c:out value="${car.carId}"/> &nbsp; Created By: <c:out value="${car.createdBy}"/> Dept #:<c:out value="${car.vendorStyle.classification.department.deptCd}"/> 
		</li>
	</c:forEach>
	</ul>
</div>