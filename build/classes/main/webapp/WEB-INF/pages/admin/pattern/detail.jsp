<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pattern.detail.title" /></title>
	<meta name="heading"
		content="<fmt:message key='pattern.detail.heading'/>" />
	<meta name="menu" content="UserMenu" />
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Pattern Detail
	</div>
	<div class="x-panel-body">
		<div class="userButtons">
			<secureurl:secureAnchor name="PatternEdit" title="button.edit"
				localized="true" hideUnaccessibleLinks="true"
				arguments="${pattern.vendorStyleId},edit" cssStyle="btn"/>
			<a class="btn" href="<c:url value="/admin/pattern/search.html?${patternSearchCriteria.criteriaAsQueryString}"/>"
				title="Back to Patterns"><fmt:message key="button.back" /> </a>
		</div>
		<ul>
			<li>
				<label for="txt_patternCode">
					<fmt:message key='pattern.style.number'/>:
				</label>
				<c:out value="${pattern.vendorStyleNumber}" />
			</li>
			<li>
				<label for="txt_patternName">
					<fmt:message key='pattern.name'/>:
				</label>
				<c:out value="${pattern.vendorStyleName}" />
			</li>
			<li>
				<label for="txt_patternDescr">
					<fmt:message key='pattern.descr'/>:
				</label>
				<c:out value="${pattern.descr}" />
			</li>
			<li>
				<label for="txt_classNumber">
					<fmt:message key='pattern.class.number'/>:
				</label>
				<c:out value="${pattern.classification.belkClassNumber}" />
			</li>
			<li>
				<label for="txt_productType">
					<fmt:message key='pattern.product.type'/>:
				</label>
				<c:out value="${pattern.productType.name}" />
			</li>
			<li>
				<label for="txt_productType">
					<fmt:message key='pattern.vendor.number'/>:
				</label>
				<c:out value="${pattern.vendorNumber}" />
			</li>
			<li>
				<label for="txt_productType">
					<fmt:message key='pattern.type'/>:
				</label>
				<c:out value="${pattern.vendorStyleType.name}" />
			</li>
			<li>
				<label for="txt_status">
					<fmt:message key='pattern.status'/>:
				</label>
				<c:out value="${pattern.statusCd}" />
			</li>
		</ul>
	</div>
</div>
<div id="vendor_style_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Pattern Vendor Styles
	</div>
	<div class="x-panel-body">
		<secureurl:secureAnchor cssStyle="btn confirm"
			name="PatternMergeCars"
			title="Merge Cars" localized="true"
			hideUnaccessibleLinks="true"
			arguments="${pattern.vendorStyleId}" />
		<secureurl:secureAnchor cssStyle="btn confirm"
			name="PatternConvertCars"
			title="Convert Cars" localized="true"
			hideUnaccessibleLinks="true"
			arguments="${pattern.vendorStyleId}" />
		<secureurl:secureAnchor cssStyle="btn btn_add_vendor_style"
			name="AddPatternVendorStyle"
			title="Add Vendor Style" localized="true"
			hideUnaccessibleLinks="true"
			arguments="${pattern.vendorStyleId},0,add" />
		<br />
		<br />
		<div id="vendor_styles_wrap">
		<c:choose>
			<c:when test="${not empty patternProducts}">
				<jsp:include page="patternVendorStylesTable.jsp"></jsp:include>
			</c:when>
			<c:otherwise>
				<fmt:message key="pattern.details.no.vendor.styles.found" />
			</c:otherwise>
		</c:choose>
		</div>
	</div>
</div>
</body>

<content tag="inlineStyle">
.cars_panel label{
	float:left;
	font-weight:bold;
	padding-right:15px;
	text-align:right;
	width:150px;
}
.cars_panel li{
	padding:5px 0;
	clear:both;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.patterndetails.js"/>"></script>
]]>
</content>