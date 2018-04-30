<%@ include file="/common/taglibs.jsp"%>
<body>
<c:url value='/requestImage.html' var="formAction"/>
<form:form commandName="image" method="post" action="${formAction}" id="requestImageForm" >
<c:choose>
<c:when test="${image.imageTrackingStatus.imageTrackingStatusCd=='REQUESTED' && user.userTypeCd!='VENDOR' && action!='AddOnHand'}">
	<ul>
		<li>
			<label>Image Type *:</label>
			<c:set var="imageTypesList" value="${imageTypes}" scope="request" />
			<form:select path="imageTypeCd">
					<form:options items="${imageTypesList}" itemValue="imageTypeCd" itemLabel="name" />
			</form:select>
		</li>
		<li>
			<label>Image Note:</label>
			<form:textarea path="description" cssClass="maxChars" />
			<div class="chars_rem">
				Remaining: <span class="chars_rem_val">2000</span>
			</div>
			<div class="max_chars">
				Max Char: <span class="max_chars_val">2000</span>
			</div>
			<form:hidden path="imageId" />
			<input type="hidden" name="carId" value="<c:out value="${carId}" />" />
			<form:hidden path="imageTrackingStatusCd" />
		</li>
		<li class="buttons">
			<input class="btn" type="submit" value="Save" />
		</li>
	</ul>
</c:when>
<c:otherwise>
	<c:set var="approvalReadyOnly" value=""/>
	<c:set var="approvalChecked" value="checked='checked'"/>
	<c:set var="rejectionChecked" value=""/>
	<c:if test="${image.imageTrackingStatus.imageTrackingStatusCd == 'REJECTED'}">
		<c:set var="approvalChecked" value=""/>
		<c:set var="rejectionChecked" value="checked='checked'"/>
	</c:if>
	<ul>
		<li>
			<label>Image Location *:</label>
			<c:set var="imageLocationTypesList" value="${imageLocationTypes}" scope="request" />
			<form:select path="imageLocationTypeCd">
				<form:option value="Select an option" />
				<c:forEach var="imageLocationType" items="${imageLocationTypesList}">
					<c:if test="${imageLocationType.imageLocationTypeCd !='BELK_FTP'}">
						<form:option value="${imageLocationType.imageLocationTypeCd}" label="${imageLocationType.name}" />
					</c:if>
				</c:forEach>
			</form:select>
		</li>
		<li>
			<label>Image Type *:</label>
			<c:set var="imageTypesList" value="${imageTypes}" scope="request" />
			<form:select path="imageTypeCd">
				<form:options items="${imageTypesList}" itemValue="imageTypeCd" itemLabel="name" />
			</form:select>
		</li>
		<li>
			<label>Location, CAR ID and/or Note for Art Director *:</label>
			<form:textarea path="imageFinalUrl" cssClass="maxChars" />
			<div class="chars_rem">
				Remaining: <span class="chars_rem_val">200</span>
			</div>
			<div class="max_chars">
				Max Char: <span class="max_chars_val">200</span>
			</div>
		</li>
		<li>
			<label>Image Description:</label>
			<form:textarea path="description" cssClass="maxChars" />
			<div class="chars_rem">
				Remaining: <span class="chars_rem_val">500</span>
			</div>
			<div class="max_chars">
				Max Char: <span class="max_chars_val">500</span>
			</div>
		</li>
		<li>
			<label>Additional Information to Note:</label>
			<form:textarea path="notesText" cssClass="maxChars" />
			<div class="chars_rem">
				Remaining: <span class="chars_rem_val">2000</span>
			</div>
			<div class="max_chars">
				Max Char: <span class="max_chars_val">2000</span>
			</div>
		</li>
	  	<li>
			<c:choose>
				<c:when test="${user.userType.userTypeCd == 'BUYER'||user.userType.userTypeCd == 'ART_DIRECTOR'}">	
					<label>Approval?</label>
					<input type="radio" <c:out value="${approvalChecked}" escapeXml="false"/> name="imageTrackingStatusCd" value="APPROVED">Approved&nbsp;
					<input type="radio" <c:out value="${rejectionChecked}" escapeXml="false"/> name="imageTrackingStatusCd" value="REJECTED">Rejected
				</c:when>
				<c:otherwise>
					<label>Status:</label>
					<c:out value="${image.imageTrackingStatus.name}" default="Available"/>
					<form:hidden path="imageTrackingStatusCd" />
				</c:otherwise>
			</c:choose>
			<form:hidden path="imageId" />
			<input type="hidden" name="carId" value="<c:out value="${carId}" />" />
			<c:if test="${action=='AddOnHand'}">
				<input type="hidden" name="action" value="AddOnHand" />
			</c:if>
			<form:hidden path="imageSourceTypeCd" />
	  	</li>
		<c:if test="${user.userType.userTypeCd == 'BUYER'}">
			<li>
				<label>Approval/Rejection Comments:</label>
				<form:textarea path="approvalNotesText" cssClass="maxChars" />
				<div class="chars_rem">
					Remaining: <span class="chars_rem_val">2000</span>
				</div>
				<div class="max_chars">
					Max Char: <span class="max_chars_val">2000</span>
				</div>
			</li>
		</c:if>
		<li class="buttons">
			<input class="btn" type="submit" name="imageSave" value="Save"/>
		</li>
	</ul>
</c:otherwise>
</c:choose>
</form:form>
</body>