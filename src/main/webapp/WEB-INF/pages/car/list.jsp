<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="manualcars.title"/></title>
    <meta name="heading" content="<fmt:message key='manualcars.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div id="search_for_cars"><div id="search_for_cars_content" class="pnl_content x-hidden">
<c:url value="/car/search.html" var="formAction"/>
<form  action="${formAction}" id="searchForm">
	<ol>
		<li>
			<label>Process Status Code:</label>
			<select id="processStatusCd" name="processStatusCd">
				<option value ="">---</option>
				<c:forEach items="${manualCarProcessStatusList}" var="mcpsClass">
					<option value ="${mcpsClass.statusCd}"><c:out value="${mcpsClass.name}"/></option>
				</c:forEach>	
			</select>
		</li>
		<li>
			<label>Vendor Number:</label>
			<input type="text" id="vendorNo" name="vendorNo" value="<c:out value="${vendorNo}" />"/>
		</li>
		<li>
			<label>Vendor Style Number:</label>
			<input type="text" id="vendorStyleNo" name="vendorStyleNo" value="<c:out value="${vendorStyleNo}" />"/>
		</li>
		<li class="">
			<a href="#" class="btn" id="btn_search_cars">Search</a>
			<input style="display:none;" type="submit" name="searchManualCar" value="Search" />
		</li>
	</ol>
</form>
</div></div>
<div id="car_list" style="margin-top:10px;"><div id="car_list_content" class="pnl_content x-hidden">
	<div class="userButtons">
		<!--  <a class="btn" href="<c:url value="/car/search.html"/>"><fmt:message key="manualcar.button.search"/></a> -->
		<a class="btn" href="<c:url value="/car/edit.html"/>"><fmt:message key="manualcar.button.add"/></a>
		<a class="btn" href="<c:url value="/car/export.html"/>"><fmt:message key="manualcar.button.export"/></a>
	</div>

	<display:table name="manualCarList" cellspacing="0" cellpadding="0"
		requestURI="/car/search.html" defaultsort="1" id="manualcar"
		pagesize="25" class="table" sort="list">
		<display:column sortable="true" titleKey="manualcar.carid"
			style="width: 5%" comparator="com.belk.car.util.LongComparator" sortProperty="manualCarId">
			<secureurl:secureAnchor name="ManualCarDetail"
				arguments="${manualcar.manualCarId}"
				title="${manualcar.manualCarId}" hideUnaccessibleLinks="true" />
		</display:column>
		<display:column property="vendorNumber" sortable="true"
			titleKey="manualcar.vendor" style="width: 15%" />
		<display:column property="vendorStyleNumber" sortable="true"
			titleKey="manualcar.vendor.style" style="width: 15%" />
		<display:column property="colorDescription" sortable="true"
			titleKey="manualcar.color.description" style="width: 10%" />
		<display:column property="sizeDescription" sortable="true"
			titleKey="manualcar.size.description" style="width: 15%" />
		<display:column property="processStatus.name" sortable="true"
			titleKey="manualcar.processstatus" style="width: 15%" />
		<display:column property="statusCd" sortable="true"
			titleKey="manualcar.statusCd" style="width: 15%" />
		<display:column>
			<secureurl:secureAnchor name="RemoveManualCar" title="Remove"
				localized="true" hideUnaccessibleLinks="true"
				arguments="${manualcar.manualCarId},remove" />
		</display:column>
		<display:column>
			<secureurl:secureAnchor name="EditManualCar" title="Edit"
				localized="true" hideUnaccessibleLinks="true"
				arguments="${manualcar.manualCarId},edit" />
		</display:column>
		<display:setProperty name="paging.banner.item_name"
			value="manualcar" />
		<display:setProperty name="paging.banner.items_name"
			value="manualcars" />
	</display:table></div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('#btn_search_cars').click(function(){
		$('#searchForm').submit();
		return false;
	});
	// panels
	new Ext.Panel({
        title:'Manual CAR Search',
        collapsible:true,
		frame:true,
        applyTo:'search_for_cars',
		contentEl:'search_for_cars_content',
		height:'auto'
    });
	// panels
	new Ext.Panel({
        title:'Manual CARs',
        collapsible:true,
		frame:true,
        applyTo:'car_list',
		contentEl:'car_list_content',
		height:'auto'
    });
});
</script>
]]>
</content>