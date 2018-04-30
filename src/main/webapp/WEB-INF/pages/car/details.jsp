<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="manualcar.details.title"/></title>
    <meta name="heading" content="<fmt:message key='manualcars.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div id="car_details"><div id="car_details_content" class="pnl_content x-hidden">
	<div class="productButtons">
		<a class="btn" href="<c:url value="edit.html?id=${manualCar.manualCarId}" />" title="Edit Manual Car"><fmt:message key="button.edit"/></a>
		<a class="btn" href="<c:url value="manualcars.html"/>" title="Back to Manual Car List"><fmt:message key="button.back"/></a>	
	</div>
	<br/>
	<br/>

	<h1>Manual Car Details</h1>
	<br/>
	<ul>
		<li>
			<label for="txt_manualcarid"><fmt:message key="manualcar.carid"/></label>: <c:out value="${manualCar.manualCarId}"/>					
		</li>
		<li>
			<label for="txt_vendornumber"><fmt:message key="manualcar.edit.processstatuscd"/></label>: <c:out value="${manualCar.processStatus.name}"/>					
		</li>
		<li>
			<label for="txt_vendornumber"><fmt:message key="manualcar.edit.vendornumber"/></label>: <c:out value="${manualCar.vendorNumber}"/>					
		</li>
		<li>
			<label for="txt_vendorstylenumber"><fmt:message key="manualcar.edit.vendorstylenumber"/></label>: <c:out value="${manualCar.vendorStyleNumber}"/>										
		</li>
		<li>
			<label for="txt_colordescription"><fmt:message key="manualcar.edit.colordescription"/></label>: <c:out value="${manualCar.colorDescription}"/>										
		</li>
		<li>
			<label for="txt_sizedescription"><fmt:message key="manualcar.edit.sizedescription"/></label>: <c:out value="${manualCar.sizeDescription}"/>										
		</li>
		<li>
			<label for="txt_statuscd"><fmt:message key="manualcar.edit.statuscd"/></label>: <c:out value="${manualCar.statusCd}"/>										
		</li>
	</ul>
</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// panels
	new Ext.Panel({
        title:'CAR Details',
        collapsible:true,
		frame:true,
        applyTo:'car_details',
		contentEl:'car_details_content',
		height:'auto'
    });
});
</script>
]]>
</content>