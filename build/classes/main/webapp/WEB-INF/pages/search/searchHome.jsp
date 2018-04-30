<%@ include file="/common/taglibs.jsp"%>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$('#searchForm').bind("keyup", function(e) {
	  if (e.which == 13) {
	   $(this).submit();
	  }
	 });
]]>
</content>

<head>
    <title><fmt:message key="search.heading"/></title>
    <meta name="heading" content="<fmt:message key='search.heading'/>"/>
    <meta name="menu" content="Search"/>
</head>

<body>
<div>
	<fmt:message key="search.page.title"/>
	<c:url value="/search/searchresult.html?dboard=true" var="formAction"/>
	<form:form commandName="carSearchForm" method="post" action="${formAction}" id="searchForm" >
		<ol>
			<li>
				<label><fmt:message key="search.page.car.id"/></label>
				<form:input path="carId" id="carId"/>
			</li>
			<li>
				<label><fmt:message key="search.page.vendor.style.number"/></label>
				<form:input path="vendorStyleNumber" id="vendorStyleNumber"/>
			</li>
			<li>
				<label><fmt:message key="search.page.parent.vendor.style.number"/></label>
				<form:input path="parentVendorStyleNumber" id="parentVendorStyleNumber"/>
			</li>
			<li>
				<label>Vendor Number</label>
				<form:input path="vendorNumber" id="vendorNumber"/>
			</li>
			<li>
				<label>Vendor Name</label>
				<form:input path="vendorName" id="vendorName"/>
			</li>
			<li>
				<label><fmt:message key="deparment.deptCd"/></label>
				<form:input path="deptCd" id="deptCd"/>
			</li>
			<li>
				<label><fmt:message key="search.page.class.number"/></label>
				<form:input path="classNumber" id="classNumber"/>
			</li>
			<li>
				<label><fmt:message key="search.page.due.date.range.start"/></label>
				<form:input path="dueDateFrom" id="dueDateFrom"/>
			</li>
			<li>
				<label> <fmt:message key="search.page.due.date.range.end"/></label>
				<form:input path="dueDateTo" id="dueDateTo"/>
			</li>
			<li>
				<label><fmt:message key="search.page.workflow.status"/></label>
				<form:select id="workflowStatus" path="workflowStatus">
					<form:option value =""><fmt:message key="search.page.all"/></form:option>
					<c:forEach items="${workflowStatuses}" var="wfs">
						 <c:if test="${wfs.statusCd != 'IMAGE_FAILED_IN_MC'}">
							<form:option value ="${wfs.statusCd}"><c:out value="${wfs.name}"/></form:option>
						</c:if>
					</c:forEach>
					<form:option value ="LATE">Late CARS</form:option>
				</form:select>
			</li>
			<c:if test="${currentUserType eq 'BUYER' || currentUserType eq 'ART_DIRECTOR' }">
			<li>
				<label>Assigned To</label>
				<form:select id="currentUserType" path="currentUserType">
					<form:option value =""><fmt:message key="search.page.all"/></form:option>
					<c:forEach items="${availableUserTypes}" var="userType">
					<c:if test="${userType.userTypeCd !='WEB_MERCHANT'}">
						<form:option value ="${userType.userTypeCd}"><c:out value="${userType.name}"/></form:option>
					</c:if>
					</c:forEach>
				</form:select>
			</li>
			<c:if test="${currentUserType eq 'BUYER'}">
			<li>
				<label>Content Status</label>
				<form:select id="contentStatus" path="contentStatus">
					<form:option value =""><fmt:message key="search.page.all"/></form:option>
					<c:forEach items="${contentStatuses}" var="contentStatus">
						<form:option value ="${contentStatus.code}"><c:out value="${contentStatus.name}"/></form:option>
					</c:forEach>
				</form:select>
			</li>
			<li>
				<label>Car Status</label>
				<form:select id="statusCd" path="statusCd">
					<form:option value ="ACTIVE">Active</form:option>
					<form:option value ="DELETED">Deleted</form:option>
				</form:select>
			</li>
			</c:if>
			</c:if>
			<li>
				<label>Promo Type</label>
				<form:select id="promotionType" path="promotionType">
					<form:option value ="-1">Select</form:option>
					<form:option value ="GWP">GWP</form:option>
					<form:option value ="PYG">PYG</form:option>
				</form:select>
			</li>
			<li>
				<label>Request Type</label>
				<form:select id="sourceTypeCd" path="sourceTypeCd">
					<form:option value =""><fmt:message key="search.page.all"/></form:option>
					<c:forEach items="${sourceType}" var="sourceType">
						<form:option value ="${sourceType.sourceTypeCd}"><c:out value="${contentStatus.sourceTypeCd}"/></form:option>
					</c:forEach>
				</form:select>
			</li>
			<li>
				<label>Archive Status</label>
				<form:select id="archive" path="archive">
					<form:option value ="false">Unarchived</form:option>
					<form:option value ="true">Archived</form:option>
					<form:option value ="">All</form:option>
				</form:select>
			</li>
			<li>
				<label>Product Type</label>
				<form:input path="productType" id="productType"/>
			</li>
			<li>
				<label>Belk UPC</label>
				<form:input path="belkUPC" id="belkUPC"/>
			</li>
			<li>
				<label>Create Date</label>
				<form:input path="createDate" id="createDate"/>
			</li>
			<li>
				<label>Update Date</label>
				<form:input path="updateDate" id="updateDate"/>
			</li>
			<li class="buttons">
				<a href="#" id="btn_cancel" class="btn"><fmt:message key="search.page.button.cancel"/></a>
				<a href="#" id="btn_search" class="btn"><fmt:message key="search.page.button.search"/></a>
				<input type="submit" value="Search" style="display:none"/>
			</li>
		</ol>
	</form:form>
</div>
</body>

