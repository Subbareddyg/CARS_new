<%@ include file="/common/taglibs.jsp"%>

<div id="img_mgmt_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Image Management
	</div>
	<div class="x-panel-body">
    <h3>Vendor-Provided Images</h3>
	<c:if test="${reqImageNote != null }">
	<ul>
	<li>
		<label>Image Location</label>
		<select name="imageLocationType">
			<c:forEach items="${imageLocationTypes}" var="ilType">
				<c:set var="imageLocationTypeSelected" value=""/>
				<c:if test="${reqImageNote.imageLocationType !=null && reqImageNote.imageLocationType.imageLocationTypeCd == ilType.imageLocationTypeCd}">
					<c:set var="imageLocationTypeSelected" value="selected='selected'"/>
				</c:if>
				<option label="${ilType.name}" value="${ilType.imageLocationTypeCd}" <c:out value="${imageLocationTypeSelected}" escapeXml="false"/>>${ilType.description }</option>
			</c:forEach>
		</select>
	</li>
	<li>
		<label>Image Type</label>
		<select name="imageType">
			<c:forEach items="${imageTypes}" var="imageType">
				<c:set var="imageTypeSelected" value=""/>
				<c:if test="${reqImageNote.imageType !=null && reqImageNote.imageType.imageTypeCd == ilType.imageTypeCd}">
					<c:set var="imageTypeSelected" value="selected='selected'"/>
				</c:if>
				<option label="${imageType.name}" value="${imageType.imageTypeCd}" <c:out value="${imageTypeSelected}" escapeXml="false"/>>${imageType.description }</option>
			</c:forEach>
		</select>
		<input type="hidden" name="image.imageTrackingStatusCd" value="AVAILABLE"/>
	</li>
	<li>
		<label>Location, CAR ID and/or Note for Art Director</label>
		<textarea class="maxChars" name="image.imageFinalUrl">${reqImageNote.imageFinalUrl}</textarea>
		<div class="chars_rem">
			Remaining: <span class="chars_rem_val">100</span>
		</div>
		<div class="max_chars">
			Max Char: <span class="max_chars_val">100</span>
		</div>
		<input type="hidden" name="image.imageId" value="${reqImageNote.imageId}"/>
		<input type="hidden" name="image.sourceTypeCd" value="${reqImageNote.imageSourceType.imageSourceTypeCd}"/>
	</li>
	<li>
		<label>Image Description</label>
		<textarea class="maxChars" name="imageDescription">${reqImageNote.description}</textarea>
		<div class="chars_rem">
			Remaining: <span class="chars_rem_val">500</span>
		</div>
		<div class="max_chars">
			Max Char: <span class="max_chars_val">500</span>
		</div>
	</li>
	<li>
		<label>Additional Information to Note</label>
		<textarea class="maxChars" name="image.notesText">${reqImageNote.notesText}</textarea>
		<div class="chars_rem">
			Remaining: <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			Max Char: <span class="max_chars_val">2000</span>
		</div>
	</li>
	<li class="buttons">
		<input class="btn" type="submit" name="imageSave" value="Save"/>
	</li>
</ul>
</c:if>
<div id="vendor_images">
	<c:set var="incImages" value="${detailCar.activeCarRequestedImages}" scope="request"/>
	<jsp:include page="imageManagement/vendorUploadImageList.jsp"/>
</div>
</div></div>

