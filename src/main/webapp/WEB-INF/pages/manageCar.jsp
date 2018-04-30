<%@ include file="/common/taglibs.jsp"%>
<div id="manage_car_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Managing This CAR
	</div> 
	<div class="x-panel-body">
	<c:if test="${user.userTypeCd != 'VENDOR'}">
		<div class="hd_help">
			<app:helpLink section="ManageThisCar" key="/editCarForm.html" title="&nbsp;" localized="false"/>
		</div>
	</c:if>
		<c:set var="user" value="${user}" scope="request" />
		<div id="addModifyCar">
		<%--<c:if test="${not empty workflowStatuses}">--%>
		<c:if test="${user.userType.userTypeCd == 'BUYER'}">
					<div id="wmo" style="width:820px;">
					<h3>Buyer Options:</h3>
					<div style="float: right; width: 300px; white-space: nowrap;">
						<table>
							<tr>
								<td class="carQualityRow"><b><font size="1">CAR Quality</font> *</b> 
								</td>
								<td>&nbsp;</td>
								<c:choose>
									<c:when test="${detailCar.carQualityCode=='G'}">
										<td align="center" >
										<input type="radio" checked="checked" id="Good" style="margin-right: 0px;"
											name="cquality"  value="G">
										</td>
										<td>&nbsp;</td>
										<td align="left">Good</td>
									</c:when>
									<c:otherwise>
										<td align="center"><input type="radio" id="Good" style="margin-right: 0px;"
											name="cquality"  value="G">
										</td>
										<td>&nbsp;</td>
										<td align="left">Good</td>
									</c:otherwise>
								</c:choose>
								<td>&nbsp;</td><td>&nbsp;</td>
								<c:choose>
									<c:when test="${detailCar.carQualityCode=='B'}">
										<td align="center"><input type="radio" id="Bad" style="margin-right: 0px;"
											name="cquality" value="B" checked="checked">
										</td>
										<td>&nbsp;</td>
										<td align="left">Bad</td>
									</c:when>
									<c:otherwise>
										<td align="center"><input type="radio" id="Bad" style="margin-right: 0px;"										
											name="cquality" value="B">
										</td>
										<td>&nbsp;</td>
										<td align="left">Bad</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</table>
					</div>
					<c:choose>
					<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT' || detailCar.vendorStyle.vendorStyleType.code=='PYG' }">
					<select class="dropdown-show">
						<option value="changeDate">Change Date</option>
						<c:choose>
							<c:when test="${detailCar.currentWorkFlowStatus.name != 'Pending' && detailCar.assignedToUserType.name != 'Vendor Provided Image'}">
								<option value="changeWorkflowStatus">Change WorkflowStatus</option>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${detailCar.statusCd == 'ACTIVE'}"><option value="deleteCar">Delete Car</option></c:when>
							<c:otherwise><option value="undeleteCar">Activate Car</option></c:otherwise>
						</c:choose>
						<option value="resyncAttributes">Resync Attributes</option>
						<option value="changeShipDate">Change Ship Date</option>
					</select>
					</c:when>
					<c:otherwise>
					<select class="dropdown-show">
						<option value="changeDate">Change Date</option>
						<c:choose>
							<c:when test="${detailCar.currentWorkFlowStatus.name != 'Pending' && detailCar.assignedToUserType.name!= 'Vendor Provided Image'}">
								<option value="changeWorkflowStatus">Change WorkflowStatus</option>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${detailCar.statusCd == 'ACTIVE'}"><option value="deleteCar">Delete Car</option></c:when>
							<c:otherwise><option value="undeleteCar">Activate Car</option></c:otherwise>
						</c:choose>
						<option value="resyncAttributes">Resync Attributes</option>
						<option value="resetProductType">Change Product Type</option>
						<option value="resetClass">Change Classification</option>
						<option value="changeShipDate">Change Ship Date</option>
                        <option value="resetDepartment">Change Department</option>
					</select>
					</c:otherwise>
					</c:choose>
					<ul class="dropdown-show">
						<c:if test="${workflowTransitionError != null}">
						<li class="changeWorkflowStatus">
							<c:out value="${workflowTransitionError}"/>
						</li>
						</c:if>
						<li class="changeDate">
							<label for="changeDate">
								Change Date
							</label>
							<div style="float: left; margin-right: 10px;">
								<input style="float: none; display: inline; margin-right: 0;"
									id="changeDate" type="text" name="changeDate" />
							</div>
							<input class="btn" id="btn_update_due_date" disabled="disabled" type="submit" name="updateDueDate" value="Update" />
						</li>
						<li class="changeWorkflowStatus">
							<label for="workflowStatus">
								Change Workflow Flow
							</label>
							<select name="wmWorkflowStatus" id="wmWorkflowStatus">
								<option value="">Select Status</option>
								<c:forEach items="${workflowStatuses}" var="wfStatus">
									<c:if test="${wfStatus.statusCd != 'IMAGE_FAILED_IN_MC' && wfStatus.statusCd != 'WITH_VENDOR'}">
										<option value="${wfStatus.statusCd}">
											<c:out value="${wfStatus.name}" />
										</option>
									</c:if>
								</c:forEach>
						</select>
						</li>
						<li class="changeWorkflowStatus">
							<label></label>
							<select name="wmWorkflowUserType" id="wmWorkflowUserType">
								<option value="">Select User Type</option>
								<c:forEach
									items="${availableUserTypes}"
									var="uType">
									<c:if test="${uType.userTypeCd !='WEB_MERCHANT' && uType.userTypeCd !='VENDOR'}">
									<option value="${uType.userTypeCd}">
										<c:out value="${uType.name}" />
									</option>
									</c:if>
								</c:forEach>
							</select>
							<input type="submit" class="btn" value="Submit" name="submitWebmerchantCar" />
						</li>
						<c:choose>
							<c:when test="${detailCar.statusCd == 'ACTIVE'}">
							<li class="deleteCar">
								<label>
									This CAR is no longer required
								</label>
								<input type="submit" class="btn" name="deleteCar" value="Delete CAR" />
							</li>
							</c:when>
							<c:otherwise>
							<li class="undeleteCar">
								<label>
									ReActivate this Deleted CAR
								</label>
								<input type="submit" class="btn confirm" name="undeleteCar" value="ReActivate CAR" />
							</li>
							</c:otherwise>
						</c:choose>
						<li class="resyncAttributes">
							<label>
								Re-Synchronize Attributes
							</label>
							<input type="submit" class="btn confirm" name="resyncAttr" value="Resync Attributes" />
						</li>
						<li class="resetProductType">
							<label>
								Change "Product Type" of Product (Vendor Style)
							</label>
							<a class="btn confirm" href="<c:url value="mainMenu.html?param=resetProductType&car=${detailCar.carId}" />" title="Reset Product Type">Reset Product Type</a>
						</li>
						<li class="resetClass">
							<label>
								Change (Vendor Style) Classification
							</label>
							<input type="text" maxlength="4" size="4" name="classNumber"/>
						</li>
						<li class="resetClass">
							<label></label>
							<input type="submit" class="btn confirm" name="resetClass" value="Reset Classification" />
						</li>

                                                <li class="resetDepartment">
                                                        <label>
								Change Department
							</label>
							<input type="text" maxlength="4" size="4" name="departmentNo"/><br><br>
							<label>
								Change (Vendor Style) Classification
							</label>
							<input type="text" maxlength="4" size="4" name="deptClassNumber"/>
						</li>
						<li class="resetDepartment">
							<label></label>
							<input type="submit" class="btn confirm" name="resetDepartment" value="Reset Department" />
						</li>
						<li class="changeShipDate">
							<label for="changeShipDate">
								Change Ship Date
							</label>
							<div style="float: left; margin-right: 10px;">
								<input style="float: none; display: inline; margin-right: 0;"
									id="changeShipDate" type="text" name="changeShipDate" value="${detailCar.expectedShipDateFormatted}" class="date"/>
							</div>
							<input class="btn" id="btn_update_ship_date" type="submit" name="updateShipDate" value="Update Ship Date" />
						</li>
					</ul>
				</div>
			</c:if>
			<div id="manage_car">
				<ul class="buttons">
					<li>
						<input type="submit" class="btn" id="btn_print_car"
							value="Print CAR" />
					</li>
					<li>
						<input id="btn_save_car_bot" class="btn" type="submit" name="Save Car" value="Save Car" />
						<span id="save_car_bot_msg"></span>
						<span id="save_car_bot_loading" style="display:none;"><img src="images/ajaxLoading.gif" /></span>
					</li>
				</ul>
				<div style="display:none" id="colorCodeError">
					<c:if test="${colorCodeErrorMsg!= null}">
						<c:if test="${colorCodeErrorMsg == 'colorCodeError'}">
					    	<fmt:message key="managecar.page.colorcodeerror" >
      						   <fmt:param value="${colorCode}"/>
						    </fmt:message>
						</c:if>
						<c:if test="${colorCodeErrorMsg == 'sampleRequestedError'}">
					    	<fmt:message key="managecar.page.samplerequesterror" />
						</c:if>
					</c:if>
				</div> 
				<div id="colorCode" style="color: red; float: right;">							
					<li class="error"><c:if test="${errorColorCodeMessage!= null}">
							<fmt:message key="managecar.page.colorcodeerror" />
						</c:if></li>

				</div>
				<div style="color: red;">
					<c:if test="${errorApproveMessage!= null}">
			    		<li class="error">
			       	   		<table style="border-spacing: 0px;border-collapse: 0px;">
							<tr><td style="width: 400px;">
							<div id="vendorImageApproveError"> <fmt:message key="managecar.page.imageapproveerror"/> </div>
							</td></tr>						
							</table>
			    		</li>
					</c:if>
				</div>
				<div style="color: red;">
					<c:if test="${errorMessage!= null}">
			    		<li class="error">
			       	   		<table style="border-spacing: 0px;border-collapse: 0px;">
							<tr><td style="width: 400px;">
							<div id="vendorImageError"> <fmt:message key="managecar.page.imageerror"/> </div>
							</td></tr>						
							</table>
			    		</li>
					</c:if>
				</div>		
				<ul class="options">
					<c:set var="displayOptions" value="true"/>
					<c:if test="${fn:length(workflowTransition.workflowTransitionList)== 1}">
						<c:set var="displayOptions" value="false"/>
					</c:if>
					<c:forEach items="${workflowTransition.workflowTransitionList}" var="wmTransition">
						<li>	
						    <c:choose>
							<c:when test="${displayOptions eq 'true'}">
								<input type="radio" name="transtionId" value="${wmTransition.workflowTransitionId}" id="${wmTransition.transitionToUserType.userTypeCd}" />
								<c:out value="${wmTransition.transitionName}" />
							</c:when>
							<c:otherwise>
								<input type="hidden" name="transtionId" value="${wmTransition.workflowTransitionId}" />
							</c:otherwise>
							</c:choose>
							<c:if test="${wmTransition.transitionToUserType.userTypeCd == 'VENDOR' }">
								<c:choose>
									<c:when test="${detailCar.sourceType.sourceTypeCd != 'OUTFIT' and detailCar.sourceType.sourceTypeCd != 'PYG'}">
										<jsp:include page="vendorManagement.jsp" />
									</c:when>
									<c:otherwise>
										<select name="child_car_id" class="chld_vendor_num" id="chld_vendor_num">
											<option>  </option>
							 				 <c:forEach items="${childVendorNumbers}" var="childVendor">
							 				 		    <!--  key= child car id , value= child vendor number -->
														<option value="${childVendor.key}">${childVendor.value}</option> 
        									</c:forEach>
										</select>
									</c:otherwise>
								</c:choose>
							</c:if>
						</li>
					</c:forEach>
				   
					<li class="submit">
						<input class="btn" type="submit" value="Submit" name="submitCar" />
					</li>
						
				</ul>
				
				<div style="color: red;">
						
			    <c:set var="isSampleRequested" value="false"/>
				<c:if test="${(detailCar.contentStatus.code == 'PUBLISHED'  || detailCar.contentStatus.code == 'RESEND_TO_CMP' || 
				    detailCar.contentStatus.code == 'RESEND_DATA_TO_CMP') && detailCar.mediaCompassImage==null}">				
					<c:forEach items="${detailCar.carSamples}" var="carSample" >
							<c:if test="${carSample.sample.sampleSourceType.sampleSourceTypeCd != 'NEITHER' && (empty carSample.sample.images)}">
								<c:set var="isSampleRequested" value="true" />								
							</c:if>							
					</c:forEach>	
					
					<c:choose>
						<c:when test="${isSampleRequested eq 'true'}">	<br/><br/><br/><br/>
							<fmt:message key="managecar.page.error"/>		
						</c:when>
			    	</c:choose>				
				</c:if>
				</div>
			</div>
			<a href ="#" id="imageerrorfocus"></a>
		</div>
	</div>
</div>