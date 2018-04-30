<%@ include file="/common/taglibs.jsp"%>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<div id="img_mgmt_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Image Management		
	</div>
	<div class="x-panel-body">
	<div class="hd_help artDirectorImageStatusHelp hidden">
		<app:helpLink section="ImageStatus" key="/editCarForm.html" title="&nbsp;" localized="false"/>
	</div>
	
		<c:if
			test="${!(user.userType.userTypeCd == 'SAMPLE_COORDINATOR')}"> <!-- fix for visible vendo provided images section || user.userType.userTypeCd == 'ART_DIRECTOR' -->
			<div class="need_images">
				<h3>
					 <div style="float:left;">Vendor-Provided Images</div>
 				 <div style="float: right;"> <secureurl:secureAnchor cssStyle="btn" name="PimImageUpload"
			localized="true" title="Get Image From PIM"
			hideUnaccessibleLinks="false" arguments="${detailCar.carId}"
			elementName="btn_Pim_Image_Upload" /></div> 
					
							
				</h3>				
				<div id="vendor_images">
					<jsp:include page="imageManagement/vendorUploadImageList.jsp" />
				</div>
			</div>
		</c:if>
		<div class="images_more">
			<h3>
				Requests for Art Director
			</h3>
			<c:choose>
				<c:when
					test="${user.userType.userTypeCd == 'SAMPLE_COORDINATOR' || user.userType.userTypeCd == 'ART_DIRECTOR'}">
					<c:if test="${user.userType.userTypeCd == 'ART_DIRECTOR'}">
						<div class="buttons">
							<input id="btn_mark_as_received" class="btn" type="submit"
								name="receivedImages" value="Mark as Received" />
							<input id="btn_add_on_hand_image" class="btn" type="submit"
								name="imageSubmit" value="Add Request" />
							<span id="mark_as_received_loading" style="display: none;"><img
									src="images/ajaxLoading.gif" />
							</span>
						</div>
					</c:if>

					<!--  FIX BELOW ID -->
					<div id="onhand_images">
						<c:set var="incImages" value="${detailCar.allNonVendorActiveCarImages}"
							scope="request" />
						<jsp:include page="imageManagement/artDirectorImagesTable.jsp" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="buttons">
						<input id="btn_add_on_hand_image" class="btn" type="submit"
							name="imageSubmit" value="Add Request" />
					</div>
					<div id="onhand_images">
						<c:set var="incImages" value="${detailCar.activeCarImages}"
							scope="request" />
						<jsp:include page="imageManagement/onhandImagesTable.jsp" />
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if
			test="${(user.userType.userTypeCd == 'BUYER') && (!empty detailCar.carImagesFromSample)}">
			<div class="images_from_sample">
				<h3>
					Images From Sample
				</h3>
				<div id="sample_images">
					<c:set var="incImages"
						value="${detailCar.carImagesFromSample}" scope="request" />
					<jsp:include page="imageManagement/sampleImagesTable.jsp" />
				</div>
			</div>
		</c:if>
	</div>
</div>
<c:if test="${(user.userType.userTypeCd == 'ART_DIRECTOR' ||user.userType.userTypeCd == 'BUYER')}">
<c:choose><c:when test="${(user.userType.userTypeCd == 'BUYER')}"><div id="img_naming_pnl" class="cars_panel x-hidden collapsed"></c:when><c:otherwise><div id="img_naming_pnl" class="cars_panel x-hidden"></c:otherwise></c:choose>
	<div class="x-panel-header">
		Image Naming Convention
	</div>
	<div class="x-panel-body">
		(image name format: [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT CODE]_[NRF_COLOR_CODE].[EXT])<br />
		<display:table name="${vendorStyles}" export="false" cellpadding="0" cellspacing="0" id="vendorStyle">
			<display:column style="width:10%;" property="vendor.vendorNumber" title="Vendor #" sortable="false" />
			<display:column style="width:10%;" property="vendorStyleNumber" title="Style #" sortable="false" />
			<display:column style="width:10%;" property="vendorStyleType.name" title="Type" sortable="false" />
			<display:column style="width:20%;" property="vendorStyleName" sortable="false" title="Style Name" />
			<display:column style="width:20%;" class="img_loc" sortable="false" title="Image Name (Template)">
				<c:out value="${vendorStyle.vendor.vendorNumber}" default="" />_<c:out value="${vendorStyle.vendorStyleNumber}" default="" />_A_000.psd</display:column>
			<display:caption>Image Naming Convention</display:caption>
			<display:footer></display:footer>
		</display:table>
	</div>
</div>
</c:if>
</c:if>

<c:if test="${sessionScope.isPreviewPage == 'true'}">
<div class="need_images" id="img_mgmt_pnl">
				<h3>
					Vendor-Provided Images
				</h3>
				
				<div id="vendor_images">
					<c:set var="vendorImages"
						value="${selectedCarOnPage.carVendorImages}" scope="request" />
					<jsp:include page="imageManagement/vendorUploadImageListPreview.jsp" />
				</div>
				</br>
				<div class="images_more">
					<h3>
						Requests for Art Director
					</h3>
					<div id="onhand_images">
						<c:set var="incImages" value="${selectedCarOnPage.allNonVendorActiveCarImages}"
							scope="request" />
						<jsp:include page="imageManagement/artDirectorImagesTable.jsp" />
						
						
					</div>
					
				</div>
				<div class="images_from_sample">
				<h3>
					Images From Sample
				</h3>
				<div id="sample_images">
					<c:set var="incImages"
						value="${selectedCarOnPage.carImagesFromSample}" scope="request" />
					<jsp:include page="imageManagement/sampleImagesTable.jsp" />
				</div>
			</div>
			<div id="img_naming_pnl">
				<h2>Image Naming Convention</h2>
				(image name format: [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT CODE]_[NRF_COLOR_CODE].[EXT])<br />
				<display:table name="${vendorStyles}" export="false" cellpadding="0" cellspacing="0" id="vendorStyle">
					<display:column style="width:10%;" property="vendor.vendorNumber" title="Vendor #" sortable="false" />
					<display:column style="width:10%;" property="vendorStyleNumber" title="Style #" sortable="false" />
					<display:column style="width:10%;" property="vendorStyleType.name" title="Type" sortable="false" />
					<display:column style="width:20%;" property="vendorStyleName" sortable="false" title="Style Name" />
					<display:column style="width:20%;" class="img_loc" sortable="false" title="Image Name (Template)">
					<c:out value="${vendorStyle.vendor.vendorNumber}" default="" />_<c:out value="${vendorStyle.vendorStyleNumber}" default="" />_A_000.psd</display:column>
				</display:table>
			</div>
</div>

</c:if>