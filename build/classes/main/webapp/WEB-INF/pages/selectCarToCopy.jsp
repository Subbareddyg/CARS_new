<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Select CAR to copy from...</title>
</head>
	
<body>
 <c:if test="${not empty error}">
    <div class="error">       
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />       
    </div>
    </c:if>
<div id="car_info_pnl"><div id="car_info_content" class="pnl_content x-hidden">
	<ul class="car_info">
		<li class="car_id">
			<strong><fmt:message key="caredit.page.car.id"/></strong> 
			<c:out value="${toCar.carId }" default="isNull" />
		</li>
		<li class="dept"><strong><fmt:message key="cardetail.page.department"/></strong> <c:out value="${toCar.department.deptCd}" default="isNull"/>-<c:out value="${toCar.department.name}"/></li>
		<li><strong><fmt:message key="cardetail.page.class"/></strong> <c:out value="${toCar.vendorStyle.classification.name}" default="isNull"/></li>
		<li class="vendor_num">
			<strong>Vendor #:</strong> 
			<c:out value="${toCar.vendorStyle.vendor.vendorNumber}" default="isNull"/>
		</li>
		<li class="vendor_name">
			<strong>Vendor Name:</strong>
			<c:out value="${toCar.vendorStyle.vendor.name}" default="isNull" />
		</li>
		<li class="style"><strong><fmt:message key="cardetail.page.vendor.style.number"/></strong> <c:out value="${toCar.vendorStyle.vendorStyleNumber}" default="isNull"/></li>
		<li class="style_name"><strong><fmt:message key="cardetail.page.vendor.style"/></strong> <c:out value="${toCar.vendorStyle.vendorStyleName}" default="isNull"/></li>
		<li class="due_date">
			<strong><fmt:message key="cardetail.page.due.date"/></strong>
			<fmt:formatDate pattern="MM/dd/yyyy" value="${toCar.dueDate}" />
		</li>
		<li class="status">
			<strong><fmt:message key="cardetail.page.current.status"/></strong> 
			<c:out value="${toCar.currentWorkFlowStatus.name}" default="isNull"/>
		</li>
		<li>
			<strong><fmt:message key="cardetail.page.assigned.to"/></strong> 
			<c:out value="${toCar.assignedToUserType.name}" default="isNull"/>
		</li>
	</ul>
</div></div>

<div id="cars_container">
	<div id="cars_toolbar" class="x-hidden"></div>
</div>

<div id="preview_container" class="x-hidden">
	<div class="x-window-header">CAR Preview</div>
	<div id="preview_content">
	</div>
</div>

	<%--  this form will be put into the grid toolbar --%>
	<div id="div_search_form" style="display:none;">
		<p id="grid_msg">Please search for a CAR</p>
		<c:url value='/search/searchresult.html?ajax=true&copyCars=true' var="formAction"/>
		<form id="searchForm" action="${formAction}" method="post">
			<label>
				Car ID:
			</label>
			<input name="carId" type="text" class="text" />
			<label>
				Vendor Style #:
			</label>
			<input name="vendorStyleNumber" type="text" class="text" />
			<label>
				Dept Cd:
			</label>
			<input name="deptCd" type="text" class="text" />
			<input name="classNumber" type="hidden" value="<c:out value="${toCar.vendorStyle.classification.belkClassNumber}" default="isNull" />" />
			<label>
				Due From:
			</label>
			<input id="dueDateFrom" name="dueDateFrom" type="text"
				class="text date" />
			<label>
				To:
			</label>
			<input id="dueDateTo" name="dueDateTo" type="text" class="text date" />
			<input type="submit" class="btn" value="Search" />
			<span id="search_loading" style="display: none;"><img src="images/ajaxLoading.gif" />
			</span>
		</form>
	</div>

</body>

<content tag="inlineStyle">
#cars_container{
	margin-top:10px;
}
.x-panel-tbar input.btn{
	display:inline;
	float:none;
	margin-left:10px;
}
.x-panel-tbar form label{
	margin:0 5px;
	font-weight:bold;
}
.x-panel-tbar input.text{
	width:60px;
}
.x-panel-tbar .x-form-field-wrap {
	display:inline;
	margin-right:15px;
	*margin-right:5px;
}
.x-panel-tbar .x-form-date-trigger{
	margin-top:-2px;
	*margin-top:0;
}
.x-panel-tbar{
	height:44px !important;
}
#cars_container .x-grid3-viewport{
	zoom:1;
	clear:both;
	margin:0 !important;
}
#grid_msg{
	font-weight:bold;
	font-size:13px;
	padding-left:5px;
}
</content>

<content tag="printStyle">
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
belk.cars.data=<%= request.getAttribute("jsonObj").toString() %>;
</script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.copycar.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
]]>
</content>