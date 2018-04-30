<%@ include file="/common/taglibs.jsp"%>
<!--[if lt IE 9]>
<style>
	.imageTypeHelpIconVimageBtn { position: absolute; }
</style>
<![endif]-->
<c:set var="vendorImages" value="${detailCar.carVendorImages}" scope="request" />
<input type="hidden" id="happrove" name="happrove" />
<c:set var="vendorImages" value="${detailCar.carVendorImages}" scope="request" />
<input type="hidden" id="happrove" name="happrove" />
<c:set var="approveTitle">
	Approve<a class="imageTypeHelpIconVimageBtn" href="javascript:showVendorImageApproveBtnHelp();"></a>
</c:set>
<c:set var="rejectTitle">
	Reject<a class="imageTypeHelpIconVimageBtn" href="javascript:showVendorImageRejectBtnHelp();"></a>
</c:set>
<c:set var="removeTitle">
	Remove<a class="imageTypeHelpIconVimageBtn" href="javascript:showVendorImageRemoveBtnHelp();"></a>
</c:set>
<c:set var="replaceTitle">
	Replace<a class="imageTypeHelpIconVimageBtn" href="javascript:showVendorImageReplaceBtnHelp();"></a>
</c:set>

<display:table name="${vendorImages}" defaultsort="1" defaultorder="ascending"
	export="false" cellpadding="0"
	cellspacing="0" id="vImage">	
		<c:set var='patternImage' value=''/>
		<c:if test="${!(detailCar.vendorStyle.vendorStyleType.code=='OUTFIT' || detailCar.vendorStyle.vendorStyleType.code=='PRODUCT' ) && (vImage.image.vendorImage.vendorStyle.vendorStyleNumber == detailCar.vendorStyle.vendorStyleNumber)}">
			<c:set var='vendorImageUploadedForPattern' value='true' scope="request"/>
			<c:set var='patternImage' value='patternImage'/>
		</c:if>
			
		<display:column style="width:2%;"
		property="image.vendorImage.vendorImageId" title="Image ID"
		sortable="false" />
	<display:column style="width:5%;" property="image.imageLocation"
		title="Image Name" sortable="false"/>
	<display:column style="width:5%;" sortable="false" title="Original Image Name">
				<div style="width:60px;overflow:auto;word-wrap:break-word;">
							<c:out value="${vImage.image.description}"/>
				</div>
		</display:column>
		
		<display:column style="width:3%;" property="image.vendorImage.swatchTypeCd" title="Shot Type" sortable="false" />
		
		
		<display:column style="width:5%;" sortable="false" title="Style Number">
				<div style="width:28px;overflow:auto;word-wrap:break-word;">
							<c:out value="${vImage.image.vendorImage.vendorStyle.vendorStyleNumber}"/>
				</div>
		</display:column>
		
		
		<display:column style="width:5%;" property="image.vendorImage.colorCode" title="Color" sortable="false" />
		
		<display:column style="width:5%;" sortable="false" title="Color Name">
				<div style="width:40px;overflow:auto;word-wrap:break-word;">
							<c:out value="${vImage.image.vendorImage.colorName}"/>
				</div>
		</display:column>
		
	<c:choose>
		<c:when test="${vImage.image.vendorImage.isImageOnMC == 'Y'}">
			<display:column style="width:5%;" title="Link" sortable="false">
				<div id="lapprove_${vImage.image.imageId}">
					<a class="viewImageLink" href="javascript:void(0);">View</a>
					<input type="hidden" id="imgName" class ="imgName" value="${vImage.image.imageLocation}">
				</div>
			</display:column>
		</c:when>
		
		<c:when test="${vImage.image.vendorImage.buyerApproved == 'REJECTED' || (vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'MQ_FAILED')}">
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
				
	<display:column style="width:15%;" title="Image Status"
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
					test="${vImage.image.vendorImage.buyerApproved == 'NONE' && (vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd != 'CREATIVE_FAILED' && vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd != 'MQ_FAILED')}">
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
	<display:column style="width:9%;" title="${approveTitle}"
		sortable="false">
		<div>
			<c:choose>
				<c:when
					test="${user.userType.userTypeCd == 'BUYER' && vImage.image.imageSourceTypeCd == 'VENDOR_UPLOADED' 
					&& vImage.image.vendorImage.buyerApproved == 'NONE' && vImage.image.vendorImage.isImageOnMC == 'Y'}">
					<input class="btn approveChange"
						id="approve_${vImage.image.imageId}" type="button"
						name="approveButton" 
						title="Select Approve if the vendor-uploaded image matches the adjacent style and color detailed in the CAR and can be used on Belk.com." 
						value="Approve" style="width:71px;"/>
				</c:when>
				<c:otherwise>
					<input class="btn" id="approve_${vImage.image.imageId}"
						style="color: grey;width:71px;" disabled="disabled" type="button"					
						title="Select Approve if the vendor-uploaded image matches the adjacent style and color detailed in the CAR and can be used on Belk.com." 
						value="Approve" />
				</c:otherwise>
			</c:choose>
		</div>
	</display:column>
	<display:column style="width:7%;"  title="${rejectTitle}"
		sortable="false">
		<c:choose>
			<c:when
				test="${user.userType.userTypeCd == 'BUYER' && vImage.image.imageSourceTypeCd == 'VENDOR_UPLOADED' 
				&& vImage.image.vendorImage.buyerApproved == 'NONE' && vImage.image.vendorImage.isImageOnMC == 'Y'}">
				<input class="btn rejectChange" id="reject_${vImage.image.imageId}"
					type="button" name="rejectButton" 
					title="Select Reject if the vendor-uploaded image does not match the adjacent style and color detailed in the CAR and CANNOT be used on Belk.com.The image link will be deleted and a new image should be requested from the vendor. Note: This option cannot be selected if the image was previously approved." 
					value="Reject" style="width:68px;"/>
			</c:when>
			<c:otherwise>
				<input class="btn" id="reject_${vImage.image.imageId}"
					style="color: grey;width:68px;" disabled="disabled" type="button"					
					title="Select Reject if the vendor-uploaded image does not match the adjacent style and color detailed in the CAR and CANNOT be used on Belk.com.The image link will be deleted and a new image should be requested from the vendor. Note: This option cannot be selected if the image was previously approved." 
					value="Reject" />
			</c:otherwise>
		</c:choose>
	</display:column>
	<c:choose>
		<c:when
			test= "${(((vImage.image.imageSourceTypeCd == uploadedBy && user.userType.userTypeCd == 'VENDOR') || user.userType.userTypeCd == 'BUYER') && 
				vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd != 'RECEIVED'
					) || ((user.userType.userTypeCd == 'BUYER') 
					&& (vImage.image.vendorImage.buyerApproved == 'REJECTED'
					|| vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'MQ_FAILED' 
					|| vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'CREATIVE_FAILED') 
					&& ((detailCar.assignedToUserType.userTypeCd == 'VENDOR_PROVIDED_IMAGE' && detailCar.currentWorkFlowStatus.statusCd == 'PENDING' )
	   			    || (detailCar.assignedToUserType.userTypeCd == 'CONTENT_MANAGER' && detailCar.currentWorkFlowStatus.statusCd == 'APPROVED')
	   			    || (detailCar.assignedToUserType.userTypeCd == 'SAMPLE_COORDINATOR' && detailCar.currentWorkFlowStatus.statusCd == 'SAMPLE_MANAGEMENT')))}">
			<display:column  style="width:9%;" title="${removeTitle}" class="removeVendorImage" sortable="false" >
				<input class="btn removeChange ${patternImage}" id="remove_${vImage.image.imageId}"
					type="button" name="removeButton" 
					title="Select Remove when an image is mistakenly added to the CAR.Removing an image will delete all information for the image that appears in the Vendor-Provided Image section of the CAR.Note: This option can only be used by the person who uploaded the image."
					value="Remove" style="width:71px;"/>
			</display:column>
		</c:when>
		<c:otherwise>
			<display:column style="width:9%;"  title="${removeTitle}"
				sortable="false">
				<input class="btn ${patternImage}" id="remove_${vImage.image.imageId}" type="button"
					style="color: grey;width:71px;"  disabled="disabled" 
					title="Select Remove when an image is mistakenly added to the CAR.Removing an image will delete all information for the image that appears in the Vendor-Provided Image section of the CAR.Note: This option can only be used by the person who uploaded the image."
					value="Remove" />
			</display:column>
		</c:otherwise>
	</c:choose>

	<display:column style="width:5%;" title="${replaceTitle}"
		sortable="false">
		<c:choose>
			<c:when
				test="${(vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'MQ_FAILED'  
											|| vImage.image.vendorImage.vendorImageStatus.vendorImageStatusCd == 'CREATIVE_FAILED'
											|| vImage.image.vendorImage.buyerApproved == 'REJECTED') 
											&&  ((vImage.image.imageSourceTypeCd == uploadedBy && user.userType.userTypeCd == 'VENDOR') ||
											user.userType.userTypeCd == 'BUYER')}">
				<input class="btn replaceButton"
					id="${vImage.image.imageId}" type="button"
					name="replaceButton"
					title="Select Replace to upload a replacement image for one that is in failed status." 
					value="Replace" style="width:71px;"/>
			</c:when>
			<c:otherwise>
				<input class="btn" id="${vImage.image.imageId}"
					style="color: grey;width:71px;" disabled="disabled" type="button"					
					title="Select Replace to upload a replacement image for one that is in failed status." 
					value="Replace"/>
			</c:otherwise>
		</c:choose>
	</display:column>
</display:table>