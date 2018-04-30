<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="manualcar.edit.title"/></title>
</head>

<body class="admin">
<div id="car_details"><div id="car_details_content" class="pnl_content x-hidden">
<spring:bind path="manualCarForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<div id="attr_container">
<h1><fmt:message key="manualcar.edit.h1"/></h1>
	<c:url value="/car/edit.html" var="formAction"/>
	<form:form commandName="manualCarForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="manualCarForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_vendornumber"><fmt:message key="manualcar.edit.vendornumber"/></label>
					<form:input path="vendorNumber" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error" size="7" maxlength="7"/>					
				</li>
				<li>
					<label for="txt_vendorstylenumber"><fmt:message key="manualcar.edit.vendorstylenumber"/></label>
					<form:input path="vendorStyleNumber" id="txt_attr_name" cssClass="text vsNumber" cssErrorClass="text medium error" maxlength="20" />										
				</li>
				<li>
					<label for="txt_colordescription"><fmt:message key="manualcar.edit.colordescription"/></label>
					<form:input path="colorDescription" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error" size="3" maxlength="3" />										
				</li>
				<li>
					<label for="txt_sizedescription"><fmt:message key="manualcar.edit.sizedescription"/></label>
					<form:input path="sizeDescription" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error" size="5" maxlength="5"/>										
				</li>
				<li>
					<label for ="txt_pack"><fmt:message key ="manualcar.edit.pack"/></label>					
					<c:choose>
					<c:when test="${manualCarForm.isPack =='Y'}">					
					<input type="checkbox" id="checkPack" name="checkPack" checked="checked"/>
					</c:when>
					<c:otherwise>
					<input type="checkbox" id="checkPack" name="checkPack"/>
					</c:otherwise>		
					</c:choose>					
				</li>
			</ul>
			<br/>
			<div class="buttons">
				<a id="btn_cancel" href="#" class="btn"><fmt:message key="button.cancel"/></a>
				<a id="btn_save" href="#" class="btn"><fmt:message key="button.save"/></a>
				<div style="display:none;">
					<input type="submit" class="cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
					<input type="submit" id= "saveManuacar" class="save_btn" name="save" value="<fmt:message key="button.save"/>" />
				</div>
			</div>
		</fieldset>	
	    <input type="hidden" name="id" value="${manualCarForm.manualCarId}"/>
	    <input type="hidden" id="isPack" name ="isPack" value ="${manualCarForm.isPack}"/>
	    <input type="hidden" name="action" value="addManualCar"/>
	</form:form>
</div>
</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.editmanualcar.js"/>"></script>
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

    $('#saveManuacar').click(function(){
	    if ($('#checkPack').is(':checked')) {
	        $('#isPack').val('Y');
	    }
	    else {
	       $('#isPack').val('N');
	    }
	    return true; 
    });
    
    $('.vsNumber').bind('blur', function () {
        var name =$('.vsNumber').val();               
        $('.vsNumber').val(name.toUpperCase());
     });  
});
</script>
]]>
</content>