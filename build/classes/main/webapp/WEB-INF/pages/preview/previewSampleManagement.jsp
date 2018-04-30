<%@ include file="/common/taglibs.jsp"%>
<br/>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<div id="spl_mgmt_pnl_${carId}" class="cars_panel x-hidden spl_mgmt_pnl">
	<div class="x-panel-header">
		Sample and Swatch Management 
	</div>
	<div class="x-panel-body">
	<div class="hd_help">
		<app:helpLink section="SampleManagement" key="/editCarForm.html" title="&nbsp;" localized="false"/>
	</div>
	<c:if test="${not empty carSamples }">
		<table>
			<tr>
				<th class="empty" colspan="4"></th>
				<th colspan="2" class="onhand">On Hand</th>
				<th colspan="2" class="fromvendor">From Vendor</th>
				<th colspan="4" class="empty"></th>
				<th colspan="1" class="studio">Buyer</th>
				<th colspan="3" class="studio">Studio</th>
			</tr>
			<tr>
				<th>ID</th>
				<th>Style #</th>
				<th>Color</th>
				<th>Color Name</th>
				<th>Sample</th>
				<th>Swatch</th>
				<th>Sample</th>
				<th>Swatch</th>
				<th>Neither</th>
				<th>Sample Ship<br/>Date to Buyer</th>
				<th>Must be Returned</th>
				<th>Silhouette Required</th>
				<%--<th>Requested</th>--%>
				<th>Received</th>
				<th>Shipped</th>
				<th>Received</th>
				<th>Returned</th>
			</tr>
			<c:forEach items="${carSampleList}" var="carsample" varStatus="vSkuStatus">
			<tr>
				<td>
					<c:out value="${carsample.sample.sampleId}"/>
				</td>
				<td>
					<c:out value="${carsample.sample.vendorStyle.vendorStyleNumber}"/>
				</td>
				<td>
					<c:out value="${carsample.sample.swatchColor}"/>
				</td>
				<td style="text-align:left">
					<c:out value="${carsample.sample.colorName}"/>
				</td>
				<td>
					<c:set var="requestChecked" value=""/>
					<c:if test="${carsample.sample.sampleDisplayStatus == 0}">
						<c:set var="requestChecked" value="checked='checked'"/>						
					</c:if>
					<input type="radio" name="sample:${carsample.sample.sampleId}" value="OHsample" <c:out value="${requestChecked}" escapeXml="false"/>/>
				</td>
				<td>
					<c:set var="requestChecked" value=""/>
					<c:if test="${carsample.sample.sampleDisplayStatus == 1}">
						<c:set var="requestChecked" value="checked='checked'"/>						
					</c:if>
					<input type="radio" name="sample:${carsample.sample.sampleId}" value="OHswatch" <c:out value="${requestChecked}" escapeXml="false"/>/>
				</td>
				<td>
					<c:set var="requestChecked" value=""/>
					<c:if test="${carsample.sample.sampleDisplayStatus == 2}">
						<c:set var="requestChecked" value="checked='checked'"/>						
					</c:if>
					<input type="radio" name="sample:${carsample.sample.sampleId}" value="FVsample" <c:out value="${requestChecked}" escapeXml="false"/>/>
				</td>
				<td>
					<c:set var="requestChecked" value=""/>
					<c:if test="${carsample.sample.sampleDisplayStatus == 3}">
						<c:set var="requestChecked" value="checked='checked'"/>						
					</c:if>
					<input type="radio" name="sample:${carsample.sample.sampleId}" value="FVswatch" <c:out value="${requestChecked}" escapeXml="false"/>/>
				</td>
				<td>
					<c:set var="requestChecked" value=""/>
					<c:if test="${carsample.sample.sampleDisplayStatus == 4}">
						<c:set var="requestChecked" value="checked='checked'"/>						
					</c:if>
					<input type="radio" name="sample:${carsample.sample.sampleId}" value="NoSampleRequired" <c:out value="${requestChecked}" escapeXml="false"/>/>
				</td>
				<c:choose>
				<c:when test="${carsample.sample.expectedShipDateFormatted == '01/01/1900'}">		
				<td style="text-align:left">
					<input id="sampleShipDate:${carsample.sample.sampleId}" type="text" class="sampleShipDateNew shipDate date" size="10" name="sampleShipDate:${carsample.sample.sampleId}" value="Select Date" />
				</td>
				</c:when>				
				<c:otherwise>
				<td style="text-align:left">
					<input id="sampleShipDateOld:${carsample.sample.sampleId}" type="text" class="sampleShipDateOld shipDate date" size="10" name="sampleShipDate:${carsample.sample.sampleId}" value="${carsample.sample.expectedShipDateFormatted}"/>
				</td>
				</c:otherwise>
				</c:choose>
				<td>
					<c:set var="checkReturnable" value=""/>
					<c:if test="${carsample.sample.isReturnable == 'Y' }">
						<c:set var="checkReturnable" value="checked='checked'"/>
					</c:if>
					<input type="checkbox" name="mustBeReturned:${carsample.sample.sampleId}" value="Y" <c:out value="${checkReturnable}"/> />
				</td>
				<td>
					<c:set var="checkSilhouette" value=""/>
					<c:if test="${carsample.sample.silhoutteRequired == 'Y' }">
						<c:set var="checkSilhouette" value="checked='checked'"/>
					</c:if>
					<input type="checkbox" name="silhouetteReq:${carsample.sample.sampleId}" value="Y" <c:out value="${checkSilhouette}"/> />
				</td>
				<%-- Following code is commented, because it may be used in future. 
				<td>
			      <c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd == 'REQUESTED'}">
			         <input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="REQUESTED" checked="checked"/>
			      </c:if>
			      <c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd != 'REQUESTED'}">
			         <input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="REQUESTED"/>
			      </c:if>
			    </td>
			    --%>
				<td>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd == 'RECEIVED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="RECEIVED" checked="checked"/>
					</c:if>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd != 'RECEIVED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="RECEIVED"/>
					</c:if>
				</td>
				<td>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd == 'SHIPPED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="SHIPPED" checked="checked"/>
					</c:if>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd != 'SHIPPED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="SHIPPED"/>
					</c:if>
				</td>
				<td>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd == 'RETURNED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="RETURNED" checked="checked"/>
					</c:if>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd != 'RETURNED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="RETURNED"/>
					</c:if>
				</td>
				<td>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd == 'CLOSED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="CLOSED" checked="checked"/>
					</c:if>
					<c:if test="${carsample.sample.sampleTrackingStatus.sampleTrackingStatusCd != 'CLOSED'}">
						<input type="radio" name="trackingStatus:${carsample.sample.sampleId}" value="CLOSED"/>
					</c:if>
				</td>
			</tr>
			</c:forEach>
		</table>
	</c:if>
	<c:if test="${empty carSamples}">
	  <input type="hidden" name="retCarrier" value="USPS"/>
	  <input type="hidden" name="carrierAccount" value=""/>
	</c:if>
	<c:if test="${not empty carSamples }">
		<div class="buttons">
			<input id="add_sample_note_btn" class="btn" type="submit" value="Add Note" name="AddSampleNote"/>
		<c:if test="${user.userType.userTypeCd == 'BUYER'}">
			<input id="add_return_note_btn" class="btn" type="submit" value="Add Return Note" name="AddReturnNote"/>
		</c:if>
			<span id="add_sample_note_msg"></span>
		<c:if test="${user.userType.userTypeCd == 'ART_DIRECTOR' && detailCar.imageProvider != null && detailCar.imageProvider.name == 'Belk Pineville Studio' }">
			<input id="images_rcvd_from_pineville" class="btn" type="submit" value="Recieved Images From Pineville" name="ImagesRecievedFromPineville"/>
		</c:if>
		</div>
		<h3>Notes for Samples/Swatches:</h3>
		<ul class="sample_notes">
		<c:forEach items="${sampleNotes}" var="sampleNote" >
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
		<h3>Return Notes:</h3>
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
		<h3>Return Samples/Swatches to:</h3>
		<ol>
			<li>
				<label>Carrier:</label>
				<c:set var="enableRetCarrier" value="disabled='disabled'"/>
				<c:if test="${user.userType.userTypeCd == 'BUYER'}">
					<c:set var="enableRetCarrier" value=""/>
				</c:if>
				<select name="retCarrier" <c:out value="${enableRetCarrier}" escapeXml="false"/>>
					<c:forEach items="${shippingTypes}" var="st">
						<c:set var="stSelected" value=""/>
						<c:if test="${st.shippingTypeCd == detailCar.returnCarrier.shippingTypeCd}">
							<c:set var="stSelected" value="selected='selected'"/>
						</c:if>
						<option label="${st.name}" value="${st.shippingTypeCd}" <c:out value="${stSelected}" escapeXml="false"/>>${st.name }</option>
					</c:forEach>
				</select>
				<c:set var="returnShippingType" value="UPS"/>
				<c:if test="${detailCar.returnCarrier != null  && detailCar.returnCarrier.shippingTypeCd != null}">
					<c:set var="returnShippingType" value="${detailCar.returnCarrier.shippingTypeCd}"/>
				</c:if>
				<c:if test="${user.userType.userTypeCd != 'BUYER'}">
				<input type="hidden" name="retCarrier" value="${returnShippingType}"/>
				</c:if>
			</li>
			<li>
				<label>Carrier Account #:</label>
				<c:set var="enableCarrierAcct" value="disabled='disabled' readonly='readonly'"/>
				<c:if test="${user.userType.userTypeCd == 'BUYER'}">
					<c:set var="enableCarrierAcct" value=""/>
				</c:if>
				<input type="text" name="carrierAccount" value="${detailCar.carrierAccount}" maxlength="30" <c:out value="${enableCarrierAcct}" escapeXml="false"/>/>
				<c:if test="${user.userType.userTypeCd != 'BUYER'}">
				<input type="hidden" name="carrierAccount" value="${detailCar.carrierAccount}"/>
				</c:if>
			</li>
			<li>
				<label>Photographs to be taken at:</label>
				<c:forEach items="${imageProviders}" var="ip">
					<c:set var="ipSelected" value=""/>
					<c:if test="${imageProvider != null && imageProvider.imageProviderId == ip.imageProviderId}">
						<c:set var="ipSelected" value="checked='checked'"/>
					</c:if>
				    <input type="radio" class="radio" name="imageProvider" value="${ip.imageProviderId}" <c:out value="${ipSelected}" escapeXml="false"/>/><c:out value="${ip.name}"/>
				</c:forEach>
			</li>
			<c:if test="${user.userType.userTypeCd == 'BUYER'}">
			<li>	
			<c:choose>
			<c:when test="${(detailCar.currentWorkFlowStatus.name =='Ready for Review' || detailCar.currentWorkFlowStatus.name == 'Waiting for Sample' && detailCar.assignedToUserType.name == 'Buyer')&&(detailCar.assignedToUserType.name != 'Vendor')}">		
				<label>Turn-in Date:*</label><input type="text" class="date" name="sampleTurninDate" id="sampleTurninDate_${carId}" value="${turninDate}"/>
				</c:when>				
				<c:otherwise>
				<label>Turn-in Date:</label><input type="text" class="date" name="sampleTurninDate" id="sampleTurninDate_${carId}" value="${turninDate}"/>
				</c:otherwise>
				</c:choose>
			</li>
			</c:if>			
		</ol>
	</c:if>
</div></div>
</c:if>