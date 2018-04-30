<%@ include file="/common/taglibs.jsp"%>

<display:table name="${incImages}" export="false" cellpadding="0" cellspacing="0" id="carImage">
	<display:column style="width:5%;" property="image.imageId" title="Image#" sortable="false" />
	<display:column style="width:10%;" property="image.imageType.name" title="Image Type" sortable="false" />
	
	<display:column style="width:20%;"  sortable="false" title="Requested Image Note">
	  <div style="width:200px;overflow:auto;word-wrap:break-word;">
	  	<c:out value="${carImage.image.description}"/>
	  </div>
  	</display:column>
	<display:column style="width:20%;" class="img_loc" sortable="false" title="Image Location">
	<div style="width:200px;overflow:auto;word-wrap:break-word;">
		Location: <c:out value="${carImage.image.imageLocationType.name}" />
		<br />
		Name/URL: <c:out value="${carImage.image.imageFinalUrl}" />
		<c:if test="${carImage.image.notesText != null && carImage.image.notesText != ''}">
		<br/>
		Additional Info: <c:out value="${carImage.image.notesText}" />
		</c:if>
	</div>
	</display:column>
	<display:column style="width:10%;" class="status" property="image.imageTrackingStatus.name" sortable="false" title="Image Status" />
	<display:column style="width:20%;" sortable="false" title="Approval Notes">
		 <div style="width:200px;overflow:auto;word-wrap:break-word;">
		  	<c:out value="${carImage.image.approvalNotesText}"/>
		  </div>
  	 </display:column>
	<c:if test="${sessionScope.isPreviewPage != 'true'}">
	<display:column style="width:5%;" class="edit" href="mainMenu.html?car=${carImage.car.carId}&" paramId="image" paramProperty="image.imageId" value="Edit" sortable="false" title=""/>
	<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
		<display:column style="width:5%;" class="delete" href="removeImage.html?car=${carImage.car.carId}&" paramId="image" paramProperty="image.imageId" value="Remove" sortable="false" title=""/>
	</c:if>
	<display:caption>Images</display:caption>
	<display:footer></display:footer>
	</c:if>
</display:table>