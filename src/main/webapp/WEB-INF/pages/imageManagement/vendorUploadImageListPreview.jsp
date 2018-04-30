<%@ include file="/common/taglibs.jsp"%>
<display:table name="${vendorImages}" export="false" cellpadding="0"
	cellspacing="0" id="vImage">	
		<display:column style="width:1%;"
		property="image.vendorImage.vendorImageId" title="Image ID"
		sortable="false" />
	<display:column style="width:10%;" property="image.imageLocation"
		title="Image Name" sortable="false"/>
	<display:column style="width:2%;"
		property="image.vendorImage.swatchTypeCd" title="Shot Type"
		sortable="false" />
	<display:column style="width:5%;"
		property="image.vendorImage.vendorStyle.vendorStyleNumber"
		title="Style Number" sortable="false" />
	<display:column style="width:5%;"
		property="image.vendorImage.colorCode" title="Color" sortable="false" />
	<display:column style="width:10%;"
		property="image.vendorImage.colorName" title="Color Name"
		sortable="false" />
	<c:choose>
		<c:when test="${vImage.image.vendorImage.isImageOnMC == 'Y'}">
			<display:column style="width:5%;" title="Link" sortable="false">
				<div id="lapprove_${vImage.image.imageId}">
					<u>View</u>
					<input type="hidden" id="imgName" class ="imgName" value="${vImage.image.imageLocation}">
				</div>
			</display:column>
		</c:when>
		
		<c:when test="${vImage.image.vendorImage.buyerApproved == 'REJECTED'}">
			<display:column style="width:5%;" title="Link" sortable="false">
				<div id="lapprove_${vImage.image.imageId}">Deleted</div>
			</display:column>
		</c:when>
		<c:otherwise>
			<display:column style="width:5%;" title="Link" sortable="false">
				<div id="lapprove_${vImage.image.imageId}">Pending</div>
			</display:column>
		</c:otherwise>
	</c:choose>
				
	<display:column style="width:10%;" title="Image Status"
		sortable="false">
		<div id="tapprove_${vImage.image.imageId}">	
		<input id="hideDiv" type="hidden" 
				value="<%=pageContext.getAttribute("vImage_rowNum")%>" /> 			
			<c:choose>
				<c:when
					test="${vImage.image.imageTrackingStatusCd == 'RECEIVED'}">
										RECEIVED
				</c:when>
				<c:when
					test="${vImage.image.vendorImage.buyerApproved == 'NONE' && (vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd != 'CREATIVE_FAILED' || vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd != 'MQ_FAILED')}">
										PENDING
				</c:when>
				<c:when
					test="${vImage.image.vendorImage.buyerApproved == 'REJECTED' || (vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'CREATIVE_FAILED' || vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'MQ_FAILED')}">
										FAILED
										</c:when>
				<c:when
					test="${vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'IMAGE_UPLOAD_FAILED'}">
										IMAGE UPLOAD FAILED
				</c:when>
				<c:when
					test="${vImage.image.vendorImage.buyerApproved == 'APPROVED'}">
										BUYER APPROVED
				</c:when>
			</c:choose>
		</div>
	</display:column>
	
	<c:set var="uploadedBy" value="${user.userType.userTypeCd}_UPLOADED"/>
	<display:column style="width:10%;" title="Approve"
		sortable="false">
		<div>
			<input class="btn" id="approve_${vImage.image.imageId}"
				style="color: grey;" disabled="disabled" type="button"
				name="approveButton" title="Approve" value="Approve" />
		</div>
	</display:column>
	<display:column style="width:4%;" title="Reject" sortable="false">		
				<input class="btn" id="reject_${vImage.image.imageId}" style="color: grey;" disabled="disabled" type="button"
					name="rejectButton" title="Reject" value="Reject" />
	</display:column>
	<display:column style="width:4%;" title="Remove"
		class="removeVendorImage " sortable="false">
		<input class="btn removeChange ${patternImage}" id="remove_${vImage.image.imageId}"
			type="button" style="color: grey;" name="removeButton" title="Remove" value="Remove" />
	</display:column>
		
	<display:column style="width:10%;" title="Replace" sortable="false">
		
		<input class="btn" id="${vImage.image.imageId}" style="color: grey;" disabled="disabled" type="button"
			 name="replaceButton" title="Replace" value="Replace" />
			
	</display:column>
</display:table>


