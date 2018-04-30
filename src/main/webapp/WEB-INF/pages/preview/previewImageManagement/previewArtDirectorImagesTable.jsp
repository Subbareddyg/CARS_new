<%@ include file="/common/taglibs.jsp"%>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<c:set var="imageStatusTitle">
	Image Status <app:helpLink section="ImageStatus" key="/editCarForm.html" title="&nbsp;" localized="false"/>
</c:set>
</c:if>
<c:if test="${sessionScope.isPreviewPage == 'true'}">
<c:set var="imageStatusTitle">
	Image Status
</c:set>
</c:if>
<display:table name="${incImages}" export="false" cellpadding="0" cellspacing="0" id="carImage">
<c:if test="${sessionScope.isPreviewPage != 'true'}">
  <display:column style="width:2%;" sortable="false">
  	<c:if test="${(carImage.image.imageTrackingStatus.imageTrackingStatusCd=='REQUESTED' && carImage.image.imageSourceType.imageSourceTypeCd == 'VENDOR') || (carImage.image.imageSourceType.imageSourceTypeCd == 'ON_HAND' && carImage.image.imageTrackingStatus.imageTrackingStatusCd=='APPROVED')}">
  		<input type="checkbox" class="checkbox" name="receivedImageId" value="<c:out value='${carImage.image.imageId}' />" />
  	</c:if>
  </display:column>
  </c:if>
  <display:column style="width:5%;" property="image.imageId" title="Image#" sortable="false" />
  <display:column style="width:10%;" property="image.imageType.name" title="Image Type" sortable="false" />
  <display:column style="width:15%;" class="img_loc" sortable="false" title="Image Location">
  <div style="width:200px;overflow:auto;word-wrap:break-word;">
  	Location: <c:out value="${carImage.image.imageLocationType.name}"/><br/>
  	Name/URL: <c:out value="${carImage.image.imageLocation}"/>/<c:out value="${carImage.image.imageFinalUrl}"/>
	<c:if test="${carImage.image.notesText != null && carImage.image.notesText != ''}">
	<br/>Additional Info: <c:out value="${carImage.image.notesText}" />
	</c:if>
	<c:if test="${carImage.image.imageSourceType.imageSourceTypeCd == 'FROM_SAMPLE'}">
	<br/><c:out value="${carImage.image.imageSourceType.name}" /> (<c:out value="${carImage.image.sample.sampleId}" />)
	</c:if>
  	<%--
  	Template URL: <c:out value="${detailCar.vendorStyle.vendor.vendorNumber}" default=""/>_<c:out value="${detailCar.vendorStyle.vendorStyleNumber}" default=""/>_X_000.psd<br/>
  	(image name format: [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT CODE]_[NRF_COLOR_CODE].[EXT]) --%>
  </div>  	 
  </display:column>
  <display:column style="width:15%;" property="image.imageTrackingStatus.name" sortable="false" title="${imageStatusTitle}"/>
  <display:column style="width:20%;"  sortable="false" title="Requested Image Note">
	  <div style="width:200px;overflow:auto;word-wrap:break-word;">
	  	<c:out value="${carImage.image.description}"/>
	  </div>
  </display:column>
  <display:column style="width:20%;" sortable="false" title="Approval Notes">
		 <div style="width:200px;overflow:auto;word-wrap:break-word;">
		  	<c:out value="${carImage.image.approvalNotesText}"/>
		  </div>
  	 </display:column>
  <c:if test="${sessionScope.isPreviewPage != 'true'}">
  <display:column style="width:5%;" class="edit" href="mainMenu.html?car=${carImage.car.carId}&" paramId="image" paramProperty="image.imageId" value="Edit" sortable="false" title=""/>
  <display:column style="width:5%;" class="delete" href="removeImage.html?car=${carImage.car.carId}&" paramId="image" paramProperty="image.imageId" value="Remove" sortable="false" title=""/>
  <display:caption>Images</display:caption>
  <display:footer></display:footer>
  </c:if>
</display:table>