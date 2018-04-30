<%@ include file="/common/taglibs.jsp"%>

<div id="img_mgmt_pnl_${carId}" class="cars_panel ">
	<div class="x-panel-header">
		Image Management
	</div>
	<div class="x-panel-body">
	<div class="hd_help">
		<app:helpLink section="ImageRequirements" key="/editCarForm.html" title="&nbsp;" localized="false"/>
	</div>
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
<div id="requested_images">
	<c:set var="incImages" value="${detailCar.activeCarRequestedImages}" scope="request"/>
	<jsp:include page="previewImageManagement/previewRequestedImagesTable.jsp"/>
</div>
</div></div>

<div id="spl_mgmt_pnl" class="cars_panel ">
	<div class="x-panel-header">
		Sample and Swatch Management
	</div>
	<div class="x-panel-body">
	<div class="hd_help">
		<app:helpLink section="SampleManagement" key="/editCarForm.html" title="&nbsp;" localized="false"/>
	</div>
<table cellpadding="0" cellspacing="0">
	<tr>
		<th class="empty" colspan="3"></th>
		<th colspan="4" class="fromvendor">From Vendor</th>
	</tr>
	<tr>
		<th>Style #</th>
		<th>Color</th>
		<th>Color Name</th>
		<th>Sample</th>
		<th>Swatch</th>
		<th>Expected Ship Date</th>
		<th>Must Be Returned</th>
	</tr>
<c:set var="hasSamples" value="false" />
<c:forEach items="${detailCar.carSampleList}" var="carsample" varStatus="vSkuStatus">
   <c:if test="${carsample.sample.sampleDisplayStatus == 2 || carsample.sample.sampleDisplayStatus == 3}">
	<c:set var="hasSamples" value="true" />
   	<tr>
		<td>
			<c:out value="${carsample.sample.vendorStyle.vendorStyleNumber}"/>
		</td>
   		<td>
   			<c:out value="${carsample.sample.swatchColor}"/>
		</td>
   		<td style="text-align: left">
   			<c:out value="${carsample.sample.colorName}"/>
		</td>
		<td>
			<c:set var="requestChecked" value=""/>
			<c:if test="${carsample.sample.sampleDisplayStatus == 2}">
				<c:set var="requestChecked" value="checked='checked'"/>						
				<input type="hidden" name="sample:${carsample.sample.sampleId}" value="FVsample"/>
			</c:if>
			<input type="radio" name="sample:${carsample.sample.sampleId}" value="FVsample" disabled='disabled' <c:out value="${requestChecked}" escapeXml="false"/>/>
		</td>
		<td>
			<c:set var="requestChecked" value=""/>
			<c:if test="${carsample.sample.sampleDisplayStatus == 3}">
				<c:set var="requestChecked" value="checked='checked'"/>
				<input type="hidden" name="sample:${carsample.sample.sampleId}" value="FVswatch"/>
			</c:if>
			<input type="radio" name="sample:${carsample.sample.sampleId}" value="FVswatch" disabled='disabled' <c:out value="${requestChecked}" escapeXml="false"/>/>
		</td>
		<td>
			<input type="text" class="restricted date" name="sampleShipDate:${carsample.sample.sampleId}" value="${carsample.sample.expectedShipDateFormatted}"/>
		</td>
		<td>
			<c:set var="checkReturnable" value=""/>
			<c:if test="${carsample.sample.isReturnable == 'Y' }">
				<c:set var="checkReturnable" value="checked='checked'"/>
			</c:if>
			<input type="checkbox" name="mustBeReturned:${carsample.sample.sampleId}" value="Y" <c:out value="${checkReturnable}"/> />
		</td>
   	</tr>
  </c:if>
</c:forEach>
<c:if test="${hasSamples eq 'false'}">
	<tr>
		<td colspan="7">No samples have been requested.</td>
	</tr>
</c:if>
</table>
<div class="buttons">
	<input id="add_sample_note_btn" class="btn" type="submit" value="Add Note" name="AddSampleNote"/>
	<input id="add_return_note_btn" class="btn" type="submit" value="Add Return Note" name="AddReturnNote"/>
	<span id="add_sample_note_msg"></span>
</div>

<h3>Notes for Samples/Swatches:</h3>
<ul class="sample_notes">
<c:forEach items="${detailCar.sampleNotes}" var="sampleNote" >
<li>
	<ul>
	<li class="date">
		<strong>Date:</strong> <fmt:formatDate pattern="MM/dd/yyyy" value="${sampleNote.createdDate}" />
	</li>
	<li class="name">
		<strong>Name:</strong> <c:out value="${sampleNote.createdBy}" />
	</li>
	<li class="text">
		<c:out value="${sampleNote.noteText}"/>
	</li>
	</ul>
</li>
</c:forEach>
</ul>
<h3>Return Samples/Swatches to:</h3>
<ul class="return_notes">
<c:forEach items="${detailCar.returnNotes}" var="notes">
<li>
	<ul>
	<li class="date">
		<strong>Date:</strong> <fmt:formatDate pattern="MM/dd/yyyy" value="${notes.createdDate}" />
	</li>
	<li class="name">
		<strong>Name:</strong> <c:out value="${notes.createdBy}" />
	</li>
	<li class="text">
		${notes.noteText}
	</li>
	</ul>
</li>
</c:forEach>
</ul>
<ol>
	<li>
		<label>Carrier:</label>
		<select name="retCarrier">
			<c:forEach items="${shippingTypes}" var="st">
				<c:set var="stSelected" value=""/>
				<c:if test="${st.shippingTypeCd == detailCar.returnCarrier.shippingTypeCd}">
					<c:set var="stSelected" value="selected='selected'"/>
				</c:if>
				<option label="${st.name}" value="${st.shippingTypeCd}" <c:out value="${stSelected}" escapeXml="false"/>>${st.name }</option>
			</c:forEach>
		</select>
	</li>
	<li>
		<label>Carrier Account #:</label>
		<input type="text" name="carrierAccount" value="${detailCar.carrierAccount}" maxlength="30"/>
	</li>
	<li>
		<label>Photographs to be taken at:</label>
		<c:forEach items="${imageProviders}" var="ip">
			<c:set var="ipSelected" value=""/>
			<c:if test="${detailCar.imageProvider != null && detailCar.imageProvider.imageProviderId == ip.imageProviderId}">
				<c:set var="ipSelected" value="checked='checked'"/>
			</c:if>
		    <input type="radio" class="radio" name="imageProvider" value="${ip.imageProviderId}" <c:out value="${ipSelected}" escapeXml="false"/>/><c:out value="${ip.name}"/>
		</c:forEach>
	</li>
</ol>
</div></div>
