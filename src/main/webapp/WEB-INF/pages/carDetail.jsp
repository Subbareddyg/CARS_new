<%@ include file="/common/taglibs.jsp"%>

<c:choose>
		<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PRODUCT'}">
			<c:set var="panelPrefix" value="Product" />
		</c:when>
		<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PYG'}">
			<c:set var="panelPrefix" value="Promotion" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='OUTFIT'}">
					<c:set var="panelPrefix" value="Outfit" />
				</c:when>
				<c:otherwise>
					<c:set var="panelPrefix" value="Pattern" />
				</c:otherwise>
			</c:choose>
		</c:otherwise>
</c:choose>

<ul class="car_info">
	<li class="car_id">
		<strong><fmt:message key="caredit.page.car.id"/></strong> 
		<c:out value="${selectedCarOnPage.carId }" default="" />
	</li>
	<li class="dept">
	<strong><fmt:message key="cardetail.page.department"/></strong> 
		<c:choose>
	 			<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carDptName" value="Dealbased_Department"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carDptName" value="${selectedCarOnPage.department.name}"></c:set>
	 			</c:otherwise>
	 	</c:choose>
	<c:out value="${selectedCarOnPage.department.deptCd}" default="isNull"/>-<c:out value="${carDptName}"/>
	</li>
	<li>
	<strong><fmt:message key="cardetail.page.class"/></strong> 
		<c:choose>
	 			<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carClassName" value="Dealbased_Class"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carClassName" value="${selectedCarOnPage.vendorStyle.classification.name}"></c:set>
	 			</c:otherwise>
	  	</c:choose>
	<c:out value="${selectedCarOnPage.vendorStyle.classification.belkClassNumber}" default=""/> - <c:out value="${carClassName}" default="isNull"/>
	</li>
	<li class="vendor_num">
		<strong>Vendor #:</strong> 
		<c:out value="${selectedCarOnPage.vendorStyle.vendor.vendorNumber}" default="isNull"/>
	</li>
	<li class="vendor_name">
		<strong>Vendor Name:</strong>
		<c:out value="${selectedCarOnPage.vendorStyle.vendor.name}" default="isNull" />
	</li>
	<li class="date">
		<strong>Date Car Generated:</strong>
		<fmt:formatDate pattern="MM/dd/yyyy" value="${selectedCarOnPage.createdDate}" />
	</li>
	<li class="style"><strong><fmt:message key="cardetail.page.vendor.style.number"/></strong> <c:out value="${selectedCarOnPage.vendorStyle.vendorStyleNumber}" default="isNull"/></li>
	<li class="style_name"><strong><fmt:message key="cardetail.page.vendor.style"/></strong> <c:out value="${selectedCarOnPage.vendorStyle.vendorStyleName}" default="isNull"/></li>
	<li class="status">
		<strong>Car Status:&nbsp;</strong> 
		<c:out value="${selectedCarOnPage.statusCd}" default="" />
	</li>
	<li class="status">
		<strong>Content Status:&nbsp;</strong> 
		<c:out value="${selectedCarOnPage.contentStatus.name}" default=""/>
	</li>
	<li>
		<strong>Workflow Status:&nbsp;</strong> 
		<c:out value="${selectedCarOnPage.currentWorkFlowStatus.name}" default="isNull"/>
	</li>
    <li class="status">
	<strong>Archive Status:&nbsp;</strong> 
	 <c:choose>
	 	<c:when test="${selectedCarOnPage.archive=='Y'}">
	 		<c:out value="Archived" default=""/>
	 	</c:when>
	 	<c:otherwise>
	 		<c:out value="Unarchived" default=""/>
	 	</c:otherwise>
	 </c:choose>
	</li>
	<li class="due_date">
		<strong><fmt:message key="cardetail.page.due.date"/></strong>
		<fmt:formatDate pattern="MM/dd/yyyy" value="${selectedCarOnPage.dueDate}" />
	</li>
	<li class="completion_date">
		<strong><fmt:message key="cardetail.page.completion.date"/></strong> 
		<fmt:formatDate pattern="MM/dd/yyyy" value="${selectedCarOnPage.expectedShipDate}" />
	</li>
	<li>
		<strong><fmt:message key="cardetail.page.assigned.to"/></strong> 
		<c:out value="${selectedCarOnPage.assignedToUserType.name}" default="isNull"/>
	</li>
	<li style="width: 300px">
		<strong>Last Updated By:</strong> 
		<c:out value="${selectedCarOnPage.updatedBy}" default="isNull"/>
	</li>
	<c:if test="${selectedCarOnPage.templateType =='PYG'}">
			<li>
			<strong>Template_Type: </strong> 
			<c:out value="${selectedCarOnPage.templateType}" default=""/>
			<li>
	</c:if>	
	
</ul>
<h2><c:out value="${panelPrefix}" /> Details</h2>
<ul class="attrs">
	<li>
		<label><c:out value="${panelPrefix}" /> Name</label>
		<c:out value="${selectedCarOnPage.vendorStyle.vendorStyleName}" escapeXml="false" />
	</li>
	<li>
		<label><c:out value="${panelPrefix}" /> Description</label>
		<c:out value="${selectedCarOnPage.vendorStyle.descr}" escapeXml="false" />
	</li>
	<c:if test="${selectedCarOnPage.vendorStyle.productType != null}">
	<li>
		<label><fmt:message key="caredit.page.product.type"/></label>
		<c:choose>
	 			<c:when test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:out value="Promotion"/>
	 			</c:when>
	 			<c:otherwise>
	 			<c:out value="${selectedCarOnPage.vendorStyle.productType.name}"/>
	 			</c:otherwise>
	 	</c:choose>
		
	</li>
	</c:if>
	<li>
		<label>Pattern Type</label>
	 	<c:out value="${selectedCarOnPage.vendorStyle.vendorStyleType.name}" default=""/>
	</li>
</ul>
<c:if test="${selectedCarOnPage.vendorStyle.vendorStyleType.code=='PYG'}">
<div>
<div id="child_car_list" class="cars_panel x-hidden">
    <h2>
        Child Cars :
    </h2>
    <div class="x-panel-body">
        <div id="child_cars_wrap">
		      <jsp:include page="childCarDetails.jsp"></jsp:include>
		</div>
		<br/>
    </div>
   </div>
				<script>
 					$(document).ready(function(){
 						$('#preview_container .cars_panel').removeClass('x-hidden').css('border','none');
						$('#preview_container .cars_panel .x-panel-header').css({'background':'none','border':'none','color':'#333333'});
						$('#preview_container .cars_panel .x-panel-body').css('border','none');
						$('#preview_container .cars_panel .x-panel-body .pagebanner').hide();
						$('#preview_container .cars_panel .x-panel-body :input').css({'background':'none','border':'none','color':'#333333'});
						$('#preview_container .cars_panel .x-panel-body :input').attr('readonly','readonly');
						var idArray = new Array();
						$("input[id^='parentCarId']").each(function(){
							idArray.push($(this).val());
						});
						function setID()
						{
							for (var i = 0; i < idArray.length; i++) 
							{
								$('.child_'+i).find('.x-panel-header').html('Child Car Id : '+idArray[i]);
							}
						}
						setID();
 					});
					
 				</script>
</div>
</c:if>

<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
<h2>Marketing Features</h2>
<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="MARKETING" displayOnly="true"></app:displayCarAttributes>
<h2>Category Left Nav</h2>
<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="CATEGORY" displayOnly="true"></app:displayCarAttributes>
</c:if>

<h2>Facet Attributes</h2>
<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="FACET" displayOnly="true"></app:displayCarAttributes>

<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
<h2>Features Left Nav</h2>
<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="FEATURE" displayOnly="true"></app:displayCarAttributes>
</c:if>
<c:set var="isPreviewPage" value="true" scope="session"/>

<h2><c:out value="${panelPrefix}" /> Features</h2>
<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="PRODUCT" displayOnly="true"></app:displayCarAttributes>

<app:displayCarAttributes var="selectedCarOnPage" displayType="CAR_SKU_ATTRIBUTE" styleClass="skus" displayOnly="true"></app:displayCarAttributes>

<h2>Image Management</h2>
<jsp:include page="imageManagement.jsp" />

<h2>Sample and Swatch Management</h2>
<jsp:include page="sampleManagement.jsp" />
