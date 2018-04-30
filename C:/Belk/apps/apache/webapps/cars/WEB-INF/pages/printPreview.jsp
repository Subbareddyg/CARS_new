<%@ include file="/common/taglibs.jsp"%>
<c:set var="isPreviewPage" value="false" scope="session"/>
<head>
		<title>Print Preview | Belk CARS</title>
</head>
<body class="car_edit">
<c:forEach items="${previewList}" var="printPreview">
<c:set var="counter" value="${counter+1}"></c:set>
<%-- <c:if test="${fn:length(previewList) gt 1 }"> --%>
<c:if test="${counter gt 1 }">
	<br/>
	<div id="ft">
  			<jsp:include page="/common/footer.jsp"/>
	</div>
	<div id="help_win" class="x-hidden">
    	<div class="x-window-header">Help Window</div>
		<div id="help_content" class="content"></div>
	</div>
   <h1 style="color:red;page-break-before:always;"></h1>
   <div id="hd">
            <jsp:include page="/common/header.jsp"/>
			<jsp:include page="/common/menu.jsp"/>
   </div>
</c:if>
<!-- Added for setting the objects into request -->
<c:set var="childCar"  value="${printPreview.childCar}" scope="request"/>
<c:set var="carId" value="${printPreview.car.carId}" scope="request" />
<c:set var='detailCar' value="${printPreview.car}" scope="request"/>
<c:set var="incImages" value="${detailCar.allActiveCarImages}" scope="request" />
<c:set var='vendorStyles' value="${printPreview.vendorStyle}" scope="request"/>
<c:set var='carSampleList' value="${detailCar.carSampleList}" scope="request"/>
<c:set var='carSamples' value="${detailCar.carSamples}" scope="request"/>
<c:set var='sampleNotes' value="${detailCar.sampleNotes}" scope="request"/>
<c:set var='carHistories' value="${detailCar.carHistories}" scope="request"/>
<c:set var="user" value="${user}" scope="request" />
<c:set var='carQualityCode' value="${detailCar.carQualityCode}" scope="request"/>
<c:set var='workflowTransition' value="${printPreview.workflowTransition}" scope="request"/>
<c:set var='rejectionCount' value="${detailCar.rejectionCount}" scope="request"/>
<c:set var='turninDate' value="${detailCar.turninDate}" scope="request"/>
<c:set var='imageProvider' value="${detailCar.imageProvider}" scope="request"/>
<c:set var='carImagesFromSample' value="${detailCar.carImagesFromSample}" scope="request"/>
<c:set var='activeCarImages' value="${detailCar.activeCarImages}" scope="request"/>
<c:set var='activeCarRequestedImages' value="${detailCar.activeCarRequestedImages}" scope="request"/>
<c:choose>
		<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PRODUCT'}">
			<c:set var="panelPrefix" value="Product" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT'}">
					<c:set var="panelPrefix" value="Outfit" />
				</c:when>
				<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
					<c:set var="panelPrefix" value="Promotion" />
				</c:when>
				<c:otherwise>
					<c:set var="panelPrefix" value="Pattern" />
				</c:otherwise>
			</c:choose>
		</c:otherwise>
</c:choose>
	<h1><fmt:message key="caredit.page.edit.car"/></h1>
<c:url value="mainMenuForm.html" var="formAction"/>
<form:form action="${formAction}" commandName="detailCar" method="post">
	<c:set var="user" value="${user}" scope="request" />
	<input type="hidden" name="<c:out value="carId" />" id="<c:out value="carId"/>" value="<c:out value="${detailCar.carId }"/>" />
	<input type="hidden" name="currenUser" id="currentUser" value="<c:out value="${user.userTypeCd}"/>"  />
	<div id="car_info_pnl_${detailCar.carId }" class="cars_panel">
	<div class="x-panel-header">
		CAR Info
	</div>
	<div class="x-panel-body">
	<ul class="car_info">
		<li class="car_id">
			<strong><fmt:message key="caredit.page.car.id"/></strong> 
			<c:out value="${detailCar.carId }" default="" />
		</li>
		<li class="dept">
			<strong><fmt:message key="caredit.page.department"/></strong> 
			<c:choose>
	 			<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carDptName" value="Dealbased_Department"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carDptName" value="${detailCar.department.name}"></c:set>
	 			</c:otherwise>
	 		</c:choose>
			<c:out value="${detailCar.department.deptCd}-${carDptName}" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="caredit.page.class"/></strong>
			<c:choose>
	 			<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carClassName" value="Dealbased_Class"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carClassName" value="${detailCar.vendorStyle.classification.name}"></c:set>
	 			</c:otherwise>
	 		</c:choose>
			<c:out value="${detailCar.vendorStyle.classification.belkClassNumber}" default=""/> - <c:out value="${carClassName}" default="isNull" />
		</li>
		<li class="vendor_num">
			<strong>Vendor #:</strong> 
			<c:out value="${detailCar.vendorStyle.vendor.vendorNumber}" default="isNull"/>
		</li>
		<li class="vendor_name">
			<strong>Vendor Name:</strong>
			<c:out value="${detailCar.vendorStyle.vendor.name}" default="isNull" />
		</li>
		<li class="date">
			<strong>Date Car Generated:</strong>
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.createdDate}" />			
		</li>
	
		<li class="style">
			<strong><fmt:message key="caredit.page.vendor.style.number"/></strong> 
			<c:out value="${detailCar.vendorStyle.vendorStyleNumber}" default="isNull"/>
		</li>
		<li class="style_name">
			<strong><fmt:message key="caredit.page.vendor.style"/></strong>
			<c:out value="${detailCar.vendorStyle.vendorStyleName}" default="isNull" />
		</li>
		<li class="status">
			<strong>Car Status: </strong> 
			<c:out value="${detailCar.statusCd}" default="" />
		</li>
		<li class="status">
			<strong>Content Status: </strong> 
			<c:out value="${detailCar.contentStatus.name}" default=""/>
		</li>
		<li>
			<strong>Workflow Status: </strong> 
			<c:out value="${detailCar.currentWorkFlowStatus.name}" default="isNull"/>
		</li>
		<li class="status">
			<strong>Archive Status:&nbsp;</strong> 
	 		<c:choose>
	 			<c:when test="${selectedCarOnPage.archive=='Y'}">
	 				<c:out value="Archived" default=""/>
	 			</c:when>
	 			<c:otherwise>
	 				<c:out value="Unarchived" default=""/>
	 			</c:otherwise>
	 		</c:choose>
		</li>
		<li class="due_date">
			<strong><fmt:message key="caredit.page.due.date"/></strong>
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.dueDate}" />
		</li>
		<li class="completion_date">
			<strong><fmt:message key="caredit.page.completion.date"/></strong> 
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.expectedShipDate}" />
		</li>
		<li>
			<strong><fmt:message key="caredit.page.assigned.to"/></strong> 
			<c:out value="${detailCar.assignedToUserType.name}" default="isNull"/>
		</li>
	</ul>
</div></div>
<br/>
<c:if test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT' or detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
  <div id="child_car_list_${detailCar.carId }" class="cars_panel ">
    <div class="x-panel-header">
        Child Cars Info 
    </div>
    <div class="x-panel-body">
        <div id="child_cars_wrap">
		      <jsp:include page="preview/previewChildCarDetails.jsp"></jsp:include>
		</div>
    </div>
   </div>
   <br/>
</c:if>
<div id="car_notes_pnl_${detailCar.carId }" class="cars_panel ">
	<div class="x-panel-header">
		CAR Notes
	</div>
	<div class="x-panel-body">
	<ul class="car_notes">
		<c:forEach var="carNote" items="${detailCar.carNotesList}">
			<c:if test="${carNote.noteType.noteTypeCd == 'CAR_NOTES'}">
			<li>
				<ul>					
					<li class="date">
						<strong><fmt:message key="caredit.page.notes.date"/></strong>
						<c:out value="${carNote.createdDate}" />
					</li>
					<li class="name">
						<strong><fmt:message key="caredit.page.notes.name"/></strong>
						<c:out value="${carNote.createdBy}" />
					</li>
					<li class="text">
						<c:out value="${carNote.noteText}" />
					</li>
				</ul>
			</li>
			</c:if>
		</c:forEach>
	</ul>
	<div class="buttons">
		<input type="submit" id="add_car_note_btn" class="add_note btn" value="Add Note" />
		<span id="add_note_msg"></span>
	</div>
</div></div>
<br/>
<div id="prod_detail_pnl_${detailCar.carId }" class="cars_panel ">
	<div class="x-panel-header">
		<c:out value="${panelPrefix}" /> Details
	</div>
	<div class="x-panel-body">
	<div id="prod_detail">
	<ul class="attrs">
		<li class="prod_name">
			<label><c:out value="${panelPrefix}" /> Name *</label>
			<c:choose>
				<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT'}">
					 <textarea  id ="txt_outfitName"  name="vendorStyle.Name:${detailCar.vendorStyle.vendorStyleId}" class="maxChars  recommended" cols="30" rows="5" onkeyup="textCounter();" onblur="textCounter();" ><c:out value="${detailCar.vendorStyle.vendorStyleName}" escapeXml="true"/></textarea>
						 <div align="center" class="text_counter">
								Max Chars: 150  &nbsp; &nbsp; 
								Chars Count: <span id="outfit_name_length">0</span>
								<br style="clear:both;" />
						 </div>
						<input type="button" id="generate_outfitName_btn" name="generateOutfitName" 
						       value="Auto-Generate" class="btn" style="margin-left: 170px; padding: 1px 1px" />
				 </c:when>
				<c:otherwise>
					 <textarea  id ="txt_outfitName"  name="vendorStyle.Name:${detailCar.vendorStyle.vendorStyleId}"  class="maxChars recommended"  cols="30" rows="5" ><c:out value="${detailCar.vendorStyle.vendorStyleName}" escapeXml="true"/></textarea>
					     <div class="chars_rem">
						     <fmt:message key="caredit.page.remaining"/> <span class="chars_rem_val">200</span>
					     </div>
					     <div class="max_chars">
						     <fmt:message key="caredit.page.max.char"/> <span class="max_chars_val">200</span>
					</div>
					<br style="clear:both;" />
				</c:otherwise>
				</c:choose>	
			</li> 
		<li class="prod_desc">
			<label><c:out value="${panelPrefix}" /> Description *</label>			
			<textarea id="vendorStyle.Descr:${detailCar.vendorStyle.vendorStyleId}"	class="maxChars textCount recommended" name="vendorStyle.Descr:${detailCar.vendorStyle.vendorStyleId}" onkeyup="textCounter();" onblur="textCounter();"><c:out value="${detailCar.vendorStyle.descr}" escapeXml="true" /></textarea>		
			<div class="chars_rem">
				<fmt:message key="caredit.page.product.remaining"/> <span class="chars_rem_val">40</span>
			</div>
			<div class="max_chars"
			style="margin-right: 150px; display: inline; white-space: nowrap;">
			<fmt:message key="caredit.page.product.maximum.char" />&nbsp;&nbsp;
			<fmt:message key="caredit.page.product.minimum.char" />
			<span class="max_chars_val"></span>&nbsp;&nbsp;

			Chars Count: <span id="product_desc_length">0</span>
			</div> 
			<br style="clear:both;" />
		</li>
		<c:if test="${detailCar.vendorStyle.productType != null}">
		<li>
			<label><fmt:message key="caredit.page.product.type"/></label>
			<span style="vertical-align: -4px"><c:out value="${detailCar.vendorStyle.productType.name}"/></span>
		</li>
		</c:if>
		<li>
			<label>Pattern Type</label>
		 	<span style="vertical-align: -4px"><c:out value="${detailCar.vendorStyle.vendorStyleType.name}" default=""/></span>
		</li>
	</ul>
	</div>
</div></div>
<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
<br/>
<div id="marketing_pnl_${detailCar.carId }" class="cars_panel ">
	<div class="x-panel-header">
		Marketing Features
	</div>
	<div class="x-panel-body">
		<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="MARKETING"></app:displayCarAttributes>
	</div>
</div>
<br/>
<div id="category_pnl_${detailCar.carId }" class="cars_panel x-hidden">
				<div class="x-panel-header">Category Left Nav Attributes</div>
				<div class="x-panel-body">
					<app:displayCarAttributes var="detailCar"
						displayType="CAR_ATTRIBUTE" styleClass="attrs"
						includeAttributeTypes="CATEGORY"></app:displayCarAttributes>
				</div>
</div>
<br/>
</c:if>
<div id="facet_pnl_${detailCar.carId }" class="cars_panel x-hidden">
			<div class="x-panel-header">Facet Attributes</div>
			<div class="x-panel-body">
				<app:displayCarAttributes var="detailCar"
					displayType="CAR_ATTRIBUTE" styleClass="attrs"
					includeAttributeTypes="FACET"></app:displayCarAttributes>
			</div>
		</div>
		<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
		<br/>
			<div id="feature_pnl_${detailCar.carId }" class="cars_panel x-hidden">
				<div class="x-panel-header">Features Left Nav Attributes</div>
				<div class="x-panel-body">
					<app:displayCarAttributes var="detailCar"
						displayType="CAR_ATTRIBUTE" styleClass="attrs"
						includeAttributeTypes="FEATURE"></app:displayCarAttributes>
				</div>
			</div>
		</c:if>
<c:if test="${user.userType.userTypeCd == 'CONTENT_MANAGER' || user.userType.userTypeCd == 'CONTENT_WRITER'}">
<div id="content_info_pnl_${detailCar.carId }" class="cars_panel ">
	<div class="x-panel-header">
		Content Details
	</div>
	<div class="x-panel-body">
	<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="CONTENT"></app:displayCarAttributes>
	</div>
</div>
</c:if>
<br/>
<div id="style_pnl_${detailCar.carId }" class="cars_panel ">
	<div class="x-panel-header">
		<c:out value="${panelPrefix}" /> Features
	</div>
	<div class="x-panel-body">
	<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="PRODUCT"></app:displayCarAttributes>
	<div class="buttons">
		<input id="btn_save_car_atts" class="btn" type="submit" name="Save Car" value="Save Car" />
		<c:set var='isCopyCarAllowed' value='false'/>
		
	<c:forEach var="role1" items="${user.roles}">
		<c:if test="${(role1 == 'ROLE_ADMIN' || role1 == 'ROLE_BUYER' || role1 == 'ROLE_USER' || Copied == 'true') && (panelPrefix != 'Outfit') && (panelPrefix != 'Promotion')}">
			<c:set var='isCopyCarAllowed' value='true'/>
		</c:if>
	</c:forEach>
		<!-- Commented the previous code
		<secureurl:secureAnchor cssStyle="btn" name="CopyCar" title="Copy Car" localized="true" onclick="copyCar(${detailCar.carId});" hideUnaccessibleLinks="true" arguments="${detailCar.carId}"  />
		 -->
		<c:if test="${isCopyCarAllowed == 'true'}">
		    <input type="button" name="Copy Car" class="btn" value= "Copy Car" onclick="copyCar(${detailCar.carId});"/>
		    <input type="button" name="Copy Complete Car" class="btn" value= "Copy Complete Car" id="btn_copy_complete_car"/>
		</c:if>
		<span id="save_car_msg"></span>
		<span style="background:#FFFF00;" id="copy_car_msg"></span>
		<span id="save_car_loading" style="display:none;"><img src="images/ajaxLoading.gif" /></span>
	</div>
</div></div> <br/>
<c:if test="${isPrintSku == 'false'}">
<c:set var="skuInfoState" value="collapsed"/> 
</c:if>
<!-- Do not show sku Info section for outfit car -->
<c:if test="${detailCar.vendorStyle.vendorStyleType.code != 'OUTFIT' and detailCar.vendorStyle.vendorStyleType.code != 'PYG'}">
	<div id="sku_pnl_${detailCar.carId }" class="cars_panel  ${skuInfoState}">
		<div class="x-panel-header">
			SKU Information
		</div>
			<div class="x-panel-body">
				<app:displayCarAttributes var="detailCar" displayType="CAR_SKU_ATTRIBUTE" styleClass="skus"></app:displayCarAttributes>
			</div>
		</div>
		<br/>
</c:if>

<jsp:include page="preview/previewImageManagement/previewImageRequirements.jsp" />
<c:choose>
<c:when test="${detailCar.assignedToUserType.userTypeCd == 'VENDOR' && user.userTypeCd == 'VENDOR'}">
	<jsp:include page="preview/previewVendorImageSampleView.jsp" />
</c:when>
<c:otherwise>
	<jsp:include page="preview/previewImageManagement.jsp" />
	<jsp:include page="preview/previewSampleManagement.jsp" />	
</c:otherwise>
</c:choose>	
<jsp:include page="preview/previewManageCar.jsp" />
</form:form>
<jsp:include page="preview/previewCarHistoryDetails.jsp" />
<div id="add_car_note_win_${detailCar.carId }" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.car.note"/></div>
<div id="add_car_note_form_${detailCar.carId }" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.car.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.car.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="CAR_NOTES" />
		<input type="hidden" name="wfs" id="wfs" value="${detailCar.currentWorkFlowStatus.name}"/>
		<input type="hidden" name="assigned" id="assigned" value="${detailCar.assignedToUserType.name}"/>
	</form>
</div>
</div>
<div id="add_sample_note_win_${detailCar.carId }" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.sample.note"/></div>
<div id="add_sample_note_form_${detailCar.carId }" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.sample.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.sample.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="SAMPLE_NOTES" />
	</form>
</div>
</div>
<div id="add_return_note_win_${detailCar.carId }" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.return.note"/></div>
<div id="add_return_note_form_${detailCar.carId }" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.return.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.return.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="RETURN_NOTES" />
	</form>
</div>
</div>
</c:forEach>
</body>
<content tag="inlineStyle">
#sku_content *{
	zoom:1;
}
span.googie_lang_3d_on{
	display:none;
}
li.prod_name table,li.prod_desc table{
	float:left;
}
textarea.onerow{
	height:14px !important;
	overflow:hidden;
}
table.googie_list {
	font-size:12px !important;
}
</content>
<content tag="jscript">
<![CDATA[

<script type="text/javascript" src="<c:url value='/js/belk.cars.editcar.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
<script type="text/javascript" src="<c:url value='/googiespell/AJS.js'/>"></script>
<script type="text/javascript" src="<c:url value='/googiespell/googiespell.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
<script type="text/javascript" src="<c:url value='/googiespell/cookiesupport.js'/>"></script>

<script type="text/javascript">
$(document).ready(function(){
	$(window).bind('load', function(){window.print(); window.close();});
});
</script>
]]>
</content>
