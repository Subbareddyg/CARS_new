<%@ include file="/common/taglibs.jsp"%>
<br/>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<div id="img_mgmt_pnl_${carId}" class="cars_panel x-hidden img_mgmt_pnl">
	<div class="x-panel-header">
		Image Management
	</div>
	<div class="x-panel-body">
		<div class="hd_help">
			<app:helpLink section="ImageRequirements" key="/editCarForm.html"
				title="&nbsp;" localized="false" />
		</div>
		<c:if
			test="${!(user.userType.userTypeCd == 'SAMPLE_COORDINATOR' || user.userType.userTypeCd == 'ART_DIRECTOR')}">
			<div class="need_images">
				<h3>
					Vendor-Provided Images 
				</h3>
				<!-- commented for ph3 -->
				<%-- <c:if test="${reqImageNote == null}">
					<div class="buttons" style="padding: 0 10px;">
						<input class="btn" id="btn_request_iamge" type="submit"
							name="imageRequestSubmit" value="Request Image" />
					</div>
				</c:if> --%>
				<div id="requested_images">
					<c:set var="incImages"
						value="${detailCar.activeCarRequestedImages}" scope="request" /> 
					<jsp:include page="previewImageManagement/previewRequestedImagesTable.jsp" />
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
							<input id="btn_add_on_hand_image" class="btn"  type="submit"
								name="imageSubmit" value="Add Request" />
							<span id="mark_as_received_loading" style="display: none;"><img
									src="images/ajaxLoading.gif" />
							</span>
						</div>
					</c:if>

					<!--  FIX BELOW ID -->
					<div id="onhand_images_${carId}">
						<%-- <c:set var="incImages" value="${detailCar.allActiveCarImages}"
							scope="request" /> --%>
						<jsp:include page="previewImageManagement/previewArtDirectorImagesTable.jsp" />
					</div>
				</c:when>
				<c:otherwise>
					<div class="buttons" style="padding: 0 10px ;">
						<input id="btn_add_on_hand_image" class="btn" type="submit"
							name="imageSubmit" value="Add Request" />
					</div>
					<div id="onhand_images_${carId}">
						<%-- <c:set var="incImages" value="${detailCar.activeCarImages}"
							scope="request" /> --%>
						<jsp:include page="previewImageManagement/previewOnhandImagesTable.jsp" />
					</div>
				</c:otherwise>
			</c:choose>
		</div>
				<c:if test="${(user.userType.userTypeCd == 'BUYER') && (!empty carImagesFromSample)}">
			<div class="images_from_sample">
				<h3>
					Images From Sample
				</h3>
				<div id="sample_images">
<%-- 					<c:set var="incImages" --%>
<%-- 						value="${detailCar.carImagesFromSample}" scope="request" /> --%>
<%-- 						<c:out value="${detailCar.carImagesFromSample}" /> --%>
					<jsp:include page="previewImageManagement/previewSampleImagesTable.jsp" />
				</div>
			</div>
		</c:if>
	</div>
</div>

<c:if test="${(user.userType.userTypeCd == 'ART_DIRECTOR' ||user.userType.userTypeCd == 'BUYER')}">
<c:choose><c:when test="${(user.userType.userTypeCd == 'BUYER')}">
<div id="img_naming_pnl_${carId}" class="cars_panel x-hidden collapsed img_naming_pnl">
</c:when>
	<c:otherwise>
<div id="img_naming_pnl_${carId}" class="cars_panel x-hidden">
	</c:otherwise>
</c:choose>
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

