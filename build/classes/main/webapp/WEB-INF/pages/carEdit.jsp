<!DOCTYPE html>
<%-- 
  - Author(s): 
  - Date: 13/03/2013
  - Copyright Notice: 2013 Belk Inc.
  - @(#)
  - Description: this page specifies listing the Car information 
  --%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="isPreviewPage" value="false" scope="session"/>
<c:if test="${detailCar.carUserByAssignedToUserId.username == user.username}">
	<head>
		<title>Edit Car | Belk CARS</title>
	</head>
</c:if>
<c:if test="${detailCar.carUserByAssignedToUserId.username != user.username}">
	<head>
		<title><fmt:message key="caredit.page.view.car"/></title>
	</head>
</c:if>

<c:choose>
		<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PRODUCT'}">
			<c:set var="panelPrefix" value="Product" />
		</c:when>
		<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
			<c:set var="panelPrefix" value="Promotion" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT'}">
					<c:set var="panelPrefix" value="Outfit/Collection" />
				</c:when>
				<c:otherwise>
					<c:set var="panelPrefix" value="Pattern" />
				</c:otherwise>
			</c:choose>
		</c:otherwise>
</c:choose>

<body class="car_edit">
<script language="javascript">
	
	function copyCar(carId){
		//alert(document.documentElement.scrollTop);
		document.forms[0].action ="selectCarToCopy.html?carId="+carId+"&scrollPos="+document.documentElement.scrollTop;
	    document.forms[0].submit();
	}
	function copyCompleteCar(carId){
		//alert(document.documentElement.scrollTop);
		document.forms[0].action ="completeCopyCar.html?CarId="+carId+"&scrollPos="+document.documentElement.scrollTop;
	    document.forms[0].submit();
	}
	function ShowHide() {
		var visible_link = true;
	    if (visible_link) {
	       document.getElementById("btn_refresh_car").className = "hidden";
	     }
	     visible_link = !visible_link;
	  }
		
</script>
	<c:if test="${detailCar.carUserByAssignedToUserId.username == user.username}">
		<h1><fmt:message key="caredit.page.edit.car"/></h1>
	</c:if>
	<c:if test="${detailCar.carUserByAssignedToUserId.username != user.username}">
		<h1><fmt:message key="caredit.page.view.car"/></h1>
	</c:if>
	<c:if test="${detailCar.carUserByAssignedToUserId.username == user.username && detailCar.statusCd != 'DELETED'}">
		<secureurl:secureAnchor cssStyle="btn" name="RefreshCar"
			localized="true" title="Retrieve PIM Data"
			hideUnaccessibleLinks="false" arguments="${detailCar.carId}"
			elementName="btn_refresh_car" onclick = "ShowHide();" />
	</c:if>
	<c:if test="${detailCar.statusCd == 'DELETED'}">
		<input style="color: grey;" type="button" class="btn" disabled="disabled" value="Retrieve PIM Data"></input>
	</c:if>
<c:url value="mainMenuForm.html" var="formAction"/>
<form:form action="${formAction}" commandName="detailCar" method="post">
	<c:set var="user" value="${user}" scope="request" />
	<input type="hidden" name="<c:out value="carId" />" id="<c:out value="carId"/>" value="<c:out value="${detailCar.carId }"/>" />
	<input type="hidden" name="currenUser" id="currentUser" value="<c:out value="${user.userTypeCd}"/>"  />
	<div id="car_info_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Info
	</div>
	<div class="x-panel-body">
	<ul class="car_info">
		<li class="car_id">
			<strong><fmt:message key="caredit.page.car.id"/></strong> 
			<c:out value="${detailCar.carId }" default="" />
		</li>
		<li class="dept">
			<strong><fmt:message key="caredit.page.department"/></strong> 
			<c:choose>
	 			<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carDptName" value="Dealbased_Department"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carDptName" value="${detailCar.department.name}"></c:set>
	 			</c:otherwise>
	 		</c:choose>
			<c:out value="${detailCar.department.deptCd}-${carDptName}" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="caredit.page.class"/></strong>
			<c:choose>
	 			<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<c:set var="carClassName" value="Dealbased_Class"></c:set>
	 			</c:when>
	 			<c:otherwise>
	 				<c:set var="carClassName" value="${detailCar.vendorStyle.classification.name}"></c:set>
	 			</c:otherwise>
	 		</c:choose>
			<c:out value="${detailCar.vendorStyle.classification.belkClassNumber}" default=""/> - <c:out value="${carClassName}" default="isNull" />
		</li>
		<li class="vendor_num">
			<strong>Vendor #:</strong> 
			<c:out value="${detailCar.vendorStyle.vendor.vendorNumber}" default="isNull"/>
		</li>
		<li class="vendor_name">
			<strong>Vendor Name:</strong>
			<c:out value="${detailCar.vendorStyle.vendor.name}" default="isNull" />
		</li>
		<li class="date">
			<strong>Date Car Generated:</strong>
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.createdDate}" />			
		</li>
	
		<li class="style">
			<strong><fmt:message key="caredit.page.vendor.style.number"/></strong> 
			<c:out value="${detailCar.vendorStyle.vendorStyleNumber}" default="isNull"/>
			<input name="vstyleNumber" class="vstyleNumber" type="hidden" value="${detailCar.vendorStyle.vendorStyleNumber}"/>
		</li>
		<li class="style_name">
			<strong><fmt:message key="caredit.page.vendor.style"/></strong>
			<c:out value="${detailCar.vendorStyle.vendorStyleName}" default="isNull" />
		</li>
		<c:if test="${detailCar.vendorStyle.orinNumber != null}">
		<li class="style_orin">
			<strong><fmt:message key="caredit.page.vendor.style.orin"/></strong>
			<c:out value="${detailCar.vendorStyle.orinNumber}" default="isNull" />
		</li>
		</c:if>
		<li class="status">
			<strong>Car Status: </strong> 
			<c:out value="${detailCar.statusCd}" default="" />
		</li>
		<li class="status">
			<strong>Content Status: </strong> 
			<c:out value="${detailCar.contentStatus.name}" default=""/>
		</li>
		<li class="status">
			<strong>Workflow Status: </strong> 
			<c:out value="${detailCar.currentWorkFlowStatus.name}" default="isNull"/>
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
		<li>
			<strong><fmt:message key="caredit.page.isPostCutOver"/></strong> 
			<c:out value="${detailCar.postCutOver}" default=""/>
		</li>	
		<li class="due_date">
			<strong><fmt:message key="caredit.page.due.date"/></strong>
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.dueDate}" />
		</li>
		<li class="completion_date">
			<strong><fmt:message key="caredit.page.completion.date"/></strong>
			<input type="hidden" name="shipmentupdateddate" id="shipmentupdateddate" value="<c:out value="${detailCar.shipDateUpdatedDate}"/>"/> 
			<fmt:formatDate pattern="MM/dd/yyyy" value="${detailCar.expectedShipDate}" />
		</li>
		<li>
			<strong><fmt:message key="caredit.page.assigned.to"/></strong> 
			<c:out value="${detailCar.assignedToUserType.name}" default="isNull"/>
		</li>     
		<c:if test="${detailCar.templateType =='COLLECTION'}">
			<li>
  				<strong>Belk.com Searchable: </strong> 
				<c:out value="${detailCar.isSearchable}" default=""/>
			</li>
		
			<li>
				<strong>Effective Date: </strong> 
				<c:out value="${detailCar.effectiveDate}" default=""/>	
			</li>
			<li>
			<strong>Template_Type: </strong> 
			<c:out value="${detailCar.templateType}" default=""/>
			<li>
		</c:if>
		<c:if test="${detailCar.templateType =='PYG'}">
			<li>
			<strong>Template_Type: </strong> 
			<c:out value="${detailCar.templateType}" default=""/>
			<li>
		</c:if>		
	</ul>
</div></div>
<br/>

<div id="car_notes_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Notes
	</div>
	<div class="x-panel-body">
	<ul class="car_notes">
		<c:forEach var="carNote" items="${detailCar.carNotesList}">
			<c:if test="${carNote.noteType.noteTypeCd == 'ITEM_FAILURE_NOTES' && carNote.isExternallyDisplayable == 'Y' && carNote.statusCd == 'ACTIVE'}">
			<c:out value="${carNote.noteType.noteTypeCd}"/>
			
			<li>
				<ul>					
					<li class="date">
						<strong><fmt:message key="caredit.page.notes.date"/></strong>
						<p style="color:red"><c:out value="${carNote.createdDate}" /></p>
					</li>
					<li class="name">
						<strong><fmt:message key="caredit.page.notes.name"/></strong>
						<p style="color:red"><c:out value="${carNote.createdBy}" /></p>
					</li>
					<li class="text">
						<p style="color:red"><c:out value="${carNote.noteText}" /></p>
					</li>
				</ul>
			</li>
			</c:if>
			<c:if test="${carNote.noteType.noteTypeCd == 'PIM_IMAGE_FAIL_NOTES' && carNote.isExternallyDisplayable == 'Y' && carNote.statusCd == 'ACTIVE'}">
					<c:out value="${carNote.noteType.noteTypeCd}"/>
			
			<li>
				<ul>					
					<li class="date">
						<strong><fmt:message key="caredit.page.notes.date"/></strong>
						<p style="color:red"><c:out value="${carNote.createdDate}" /></p>
					</li>
					<li class="name">
						<strong><fmt:message key="caredit.page.notes.name"/></strong>
						<p style="color:red"><c:out value="${carNote.createdBy}" /></p>
					</li>
					<li class="text">
						<p style="color:red"><c:out value="${carNote.noteText}" /></p>
					</li>
				</ul>
			</li>
			</c:if>
			<c:if test="${carNote.noteType.noteTypeCd == 'CAR_NOTES'}">
					<c:out value="${carNote.noteType.noteTypeCd}"/>
			
			<li>
				<ul>					
					<li class="date">
						<strong><fmt:message key="caredit.page.notes.date"/></strong>
						<c:out value="${carNote.createdDate}" />
					</li>
					<li class="name">
						<strong><fmt:message key="caredit.page.notes.name"/></strong>
						<c:out value="${carNote.createdBy}" />
					</li>
					<li class="text">
						<c:out value="${carNote.noteText}" />
					</li>
				</ul>
			</li>
			</c:if>
		</c:forEach>
	</ul>
	<div class="buttons">
		<input type="submit" id="add_car_note_btn" class="add_note btn" value="Add Note" />
		<span id="add_note_msg"></span>
	</div>
</div></div>
<br/>

<c:if test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT' or detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
  <div id="child_car_list" class="cars_panel x-hidden">
    <div class="x-panel-header">
        Child Cars Info
    </div>
    <div class="x-panel-body">
        <div id="child_cars_wrap">
		      <jsp:include page="childCarDetails.jsp"></jsp:include>
		</div>
		<br/>
    </div>
   </div>
</c:if>

<c:if test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT' or detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
 				<jsp:include page="carEditSkuDetails.jsp"></jsp:include>
</c:if>



<div id="prod_detail_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		<c:out value="${panelPrefix}" /> Details
	</div>
	<div class="x-panel-body">
	<div id="prod_detail">
	<ul class="attrs">
		<li class="prod_name">
			<label><c:out value="${panelPrefix}" /> Name *</label>
			<c:choose>
				<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='OUTFIT'}">
									<textarea id="txt_outfitName"
										name="vendorStyle.Name:${detailCar.vendorStyle.vendorStyleId}"
										class="maxChars spellcheck recommended" cols="30" rows="5"
										onkeyup="textCounter();" onblur="textCounter();"
										maxlength="150"><c:out value="${detailCar.vendorStyle.vendorStyleName}"	escapeXml="true" /></textarea>
									<div align="center" class="text_counter">
								Max Chars: 150  &nbsp; &nbsp; 
								Chars Count: <span id="outfit_name_length">0</span>
								<br style="clear:both;" />
						 </div>
						<c:if test="${detailCar.templateType !='COLLECTION'}">
						<input type="button" id="generate_outfitName_btn" name="generateOutfitName" 
						       value="Auto-Generate" class="btn" style="margin-left: 14px; padding: 1px 1px" />
						</c:if>
				 </c:when>
				 <c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
									<textarea id="txt_dbPromotionName"
										name="vendorStyle.Name:${detailCar.vendorStyle.vendorStyleId}"
										class="maxChars spellcheck recommended" cols="30" rows="5"
										onkeyup="promotionNameTextCounter();"
										onblur="promotionNameTextCounter();" 
										maxlength="150"><c:out value="${detailCar.vendorStyle.vendorStyleName}" escapeXml="true" /></textarea>
									<div align="center" class="text_counter">
								Max Chars: 150  &nbsp; &nbsp; 
								Chars Count: <span id="dbPromotion_name_length">0</span>
						<br style="clear:both;" />
					</div>
				</c:when>
				<c:otherwise>
 					 <textarea  id ="txt_outfitName"  
 					 name="vendorStyle.Name:${detailCar.vendorStyle.vendorStyleId}"  
 					 class="maxChars spellcheck recommended"  cols="30" rows="5" 
 					 onkeyup="textCounter();"
 					 onblur="textCounter();"
 					 maxlength="150"><c:out value="${detailCar.vendorStyle.vendorStyleName}" escapeXml="true"/></textarea>
					     <div class="chars_rem">
						     <fmt:message key="caredit.page.remaining"/> <span class="chars_rem_val">200</span>
					     </div>
					     <div class="max_chars" style="margin-right: 70px; display: inline;white-space: nowrap;">
						     <fmt:message key="caredit.page.max.char"/> <span class="max_chars_val">150</span>&nbsp;&nbsp;
						     Chars Count: <span id="outfit_name_length">0</span>
					</div>
					<br style="clear:both;" />
				</c:otherwise>
				</c:choose>	
			</li> 
	
		<li class="prod_desc">
			<label><c:out value="${panelPrefix}" /> Description *</label>			
			<textarea id="vendorStyle.Descr:${detailCar.vendorStyle.vendorStyleId}"	class="maxChars spellcheck textCount recommended" name="vendorStyle.Descr:${detailCar.vendorStyle.vendorStyleId}" onkeyup="textCounter();" onblur="textCounter();"><c:out value="${detailCar.vendorStyle.descr}" escapeXml="true" /></textarea>		
				
			<div class="chars_rem">
				<fmt:message key="caredit.page.product.remaining"/> <span class="chars_rem_val">40</span>
			</div>
			<div class="max_chars"
			style="margin-right: 150px; display: inline; white-space: nowrap;">
			<fmt:message key="caredit.page.product.max.char" /><span class="max_chars_val">2000</span>&nbsp;&nbsp;
			<fmt:message key="caredit.page.product.minimum.char" />&nbsp;&nbsp;
			

			Chars Count: <span id="product_desc_length">0</span>
			</div> 
			<br style="clear:both;" />
		</li>
		<c:if test="${detailCar.vendorStyle.productType != null}">
		<li>
			<label><fmt:message key="caredit.page.product.type"/></label>
			<c:choose>
	 			<c:when test="${detailCar.vendorStyle.vendorStyleType.code=='PYG'}">
	 				<span style="vertical-align: -4px">Promotion</span>
	 			</c:when>
	 			<c:otherwise>
	 				<span style="vertical-align: -4px"><c:out value="${detailCar.vendorStyle.productType.name}"/></span>
	 			</c:otherwise>
	 		</c:choose>
		</li>
		</c:if>
		<li>
			<label>Pattern Type</label>
		 	<span style="vertical-align: -4px" class="patternType"><c:out value="${detailCar.vendorStyle.vendorStyleType.name}" default=""/></span>
		</li>
	</ul>
	</div>
</div></div>
<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
<div id="marketing_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Marketing Features
	</div>
	<div class="x-panel-body">
		<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="MARKETING"></app:displayCarAttributes>
	</div>
	</div>
</c:if><br/>
	
	<c:choose>
	<c:when test="${user.userType.userTypeCd == 'VENDOR' || user.userType.userTypeCd == 'BUYER'}">
			<div id="pimAttribute_pnl" class="cars_panel x-hidden">
	</c:when>
	<c:otherwise>
			<div id="pimAttribute_pnl" class="cars_panel x-hidden collapsed">
	</c:otherwise>
</c:choose>
			<div class="x-panel-header">PIM Attributes</div>
			<div class="x-panel-body">
				<app:displayCarAttributes var="detailCar"
					displayType="PIM_ATTRIBUTE" styleClass="attrs"></app:displayCarAttributes>
				<h4 style="clear:both">PIM Comments: </h4><hr style="border:none"/>
				<ul class="pim_notes">
					<c:forEach var="carNote" items="${detailCar.carNotesList}">
						<c:if test="${carNote.noteType.noteTypeCd == 'PIM_NOTES'}">
							<li>
								<ul>
									<li class="date"><strong><fmt:message
												key="caredit.page.notes.date" /></strong> <c:out
											value="${carNote.createdDate}" /></li>
									<li class="name"><strong><fmt:message
												key="caredit.page.notes.name" /></strong> <c:out
											value="${carNote.createdBy}" /></li>
									<li class="text"><c:out value="${carNote.noteText}" /></li>
								</ul>
							</li>
						</c:if>
					</c:forEach>
				</ul>
				<div class="buttons">
					<input type="submit" id="add_pim_note_btn" class="add_note btn"
						value="Add Comment" /> <span id="add_pim_note_msg"></span>
				</div>
			</div>
		</div>
	
	<c:if test="${user.userType.userTypeCd != 'VENDOR'}">	
	<div id="category_pnl" class="cars_panel x-hidden">
				<div class="x-panel-header">Category Left Nav Attributes</div>
				<div class="x-panel-body">
					<app:displayCarAttributes var="detailCar"
						displayType="CAR_ATTRIBUTE" styleClass="attrs"
						includeAttributeTypes="CATEGORY"></app:displayCarAttributes>
				</div>
	</div>
	</c:if><br/>


		<div id="facet_pnl" class="cars_panel x-hidden">
		<div class="x-panel-header">Facet Attributes</div>
		<div class="x-panel-body">
			<app:displayCarAttributes var="detailCar"
	displayType="CAR_ATTRIBUTE" styleClass="attrs"
	includeAttributeTypes="FACET"></app:displayCarAttributes>
		</div>
	</div>

		<c:if test="${user.userType.userTypeCd != 'VENDOR'}">
			<div id="feature_pnl" class="cars_panel x-hidden">
				<div class="x-panel-header">Features Left Nav Attributes</div>
				<div class="x-panel-body">
					<app:displayCarAttributes var="detailCar"
						displayType="CAR_ATTRIBUTE" styleClass="attrs"
						includeAttributeTypes="FEATURE"></app:displayCarAttributes>
				</div>
			</div>
		</c:if>
		
<c:if test="${user.userType.userTypeCd == 'CONTENT_MANAGER' || user.userType.userTypeCd == 'CONTENT_WRITER'}">
<div id="content_info_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Content Details
	</div>
	<div class="x-panel-body">
	<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="CONTENT"></app:displayCarAttributes>
	</div>
</div>
</c:if>

<div id="style_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		<c:out value="${panelPrefix}" /> Features
	</div>
	<div class="x-panel-body">
	<app:displayCarAttributes var="detailCar" displayType="CAR_ATTRIBUTE" styleClass="attrs" includeAttributeTypes="PRODUCT"></app:displayCarAttributes>
	<div class="buttons">
		<input id="btn_save_car_atts" class="btn" type="submit" name="Save Car" value="Save Car" />
		<c:set var='isCopyCarAllowed' value='false'/>
		
	<c:forEach var="role1" items="${user.roles}">
		<c:if test="${(role1 == 'ROLE_ADMIN' || role1 == 'ROLE_BUYER' || role1 == 'ROLE_USER' || Copied == 'true') && (panelPrefix != 'Outfit') && (panelPrefix != 'Promotion')}">
			<c:set var='isCopyCarAllowed' value='true'/>
		</c:if>
	</c:forEach>
		<!-- Commented the previous code
		<secureurl:secureAnchor cssStyle="btn" name="CopyCar" title="Copy Car" localized="true" onclick="copyCar(${detailCar.carId});" hideUnaccessibleLinks="true" arguments="${detailCar.carId}"  />
		 -->
		<c:if test="${isCopyCarAllowed == 'true'}">
		    <input type="button" name="Copy Car" class="btn" value= "Copy Car" onclick="copyCar(${detailCar.carId});"/>
		    <input type="button" name="Copy Complete Car" class="btn" value= "Copy Complete Car" id="btn_copy_complete_car"/>
		</c:if>
		<span id="save_car_msg"></span>
		<span style="background:#FFFF00;" id="copy_car_msg"></span>
		<span id="save_car_loading" style="display:none;"><img src="images/ajaxLoading.gif" /></span>
	</div>
</div></div>
<c:choose>
<c:when test="${(detailCar.assignedToUserType.userTypeCd == 'BUYER' || detailCar.assignedToUserType.userTypeCd == 'VENDOR' )}">
<c:set var="skuInfoState" value=""/>
</c:when>
<c:otherwise>
<c:set var="skuInfoState" value="collapsed"/>
</c:otherwise>
</c:choose>
<c:if test="${param.delSku eq 'true'}">
	<c:set var="skuInfoState" value=""/>
</c:if>

<!-- Do not show sku Info section for outfit car -->
<c:if test="${detailCar.vendorStyle.vendorStyleType.code != 'OUTFIT'}">
	<div id="sku_pnl" class="cars_panel x-hidden ${skuInfoState}">
		<div class="x-panel-header">
			SKU Information
		</div>
			<div class="x-panel-body">
			<div id="updatedSkusDiv"> </div>
			<div id="updatedShadesDiv"> </div>
			<div id="updatedStartDateDiv"> </div>
			<div id="updatedSuperColorDiv"> </div>
				<app:displayCarAttributes var="detailCar" displayType="CAR_SKU_ATTRIBUTE" styleClass="skus"></app:displayCarAttributes>
			</div>
		</div>
</c:if>
<jsp:include page="imageManagement/imageRequirements.jsp" />
<c:choose>
<c:when test="${detailCar.assignedToUserType.userTypeCd == 'VENDOR' && user.userTypeCd == 'VENDOR'}">
	<jsp:include page="vendorImageSampleView.jsp" />
</c:when>
<c:otherwise>
	<jsp:include page="imageManagement.jsp" />
</c:otherwise>
</c:choose>	
<jsp:include page="sampleManagement.jsp" />
<jsp:include page="manageCar.jsp" />
</form:form>

<jsp:include page="carHistoryDetails.jsp" />

<div id="add_car_note_win" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.car.note"/></div>
<div id="add_car_note_form" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.car.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.car.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="CAR_NOTES" />
		<input type="hidden" name="wfs" id="wfs" value="${detailCar.currentWorkFlowStatus.name}"/>
		<input type="hidden" name="assigned" id="assigned" value="${detailCar.assignedToUserType.name}"/>
	</form>
</div>
</div>
<div id="add_sample_note_win" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.sample.note"/></div>
<div id="add_sample_note_form" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.sample.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.sample.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="SAMPLE_NOTES" />
	</form>
</div>
</div>
<div id="add_return_note_win" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.return.note"/></div>
<div id="add_return_note_form" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.return.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.return.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="RETURN_NOTES" />
	</form>
</div>
</div>
<div id="add_pim_note_win" class="x-hidden">
<div class="x-window-header"><fmt:message key="caredit.page.add.a.pim.note"/></div>
<div id="add_pim_note_form" class="content">
	<form action="ajaxNotesForm.html" method="post">
		<textarea name="carNotes.Message:" class="note_text maxChars"></textarea>
		<div class="chars_rem">
			<fmt:message key="caredit.page.add.a.pim.note.remaining"/> <span class="chars_rem_val">2000</span>
		</div>
		<div class="max_chars">
			<fmt:message key="caredit.page.add.a.pim.note.max.char"/> <span class="max_chars_val">2000</span>
		</div>
		<br style="clear:both;" />
		<input type="hidden" name="carId" value="${detailCar.carId}" />
		<input type="hidden" name="carNotes.noteTypeCd:" value="PIM_NOTES" />
		<input type="hidden" name="wfs" id="wfs" value="${detailCar.currentWorkFlowStatus.name}"/>
		<input type="hidden" name="assigned" id="assigned" value="${detailCar.assignedToUserType.name}"/>
	</form>
</div>
</div>

</body>
<c:set var="carLock" scope="session" value="${detailCar.carId}"/>
<content tag="inlineStyle">
#sku_content *{
	zoom:1;
}
#car_info_pnl{
	margin-top:40px;
}
span.googie_lang_3d_on{
	display:none;
}
li.prod_name table,li.prod_desc table{
	float:left;
}
textarea.onerow{
	height:14px !important;
	overflow:hidden;
}
table.googie_list {
	font-size:12px !important;
}
</content>
	
<content tag="printStyle">
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/js/belk.cars.editcar.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.uploadimage.js'/>"></script>

<script type="text/javascript" src="<c:url value='/googiespell/AJS.js'/>"></script>
<script type="text/javascript" src="<c:url value='/googiespell/googiespell.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
<script type="text/javascript" src="<c:url value='/googiespell/cookiesupport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.sizecolor.js'/>"></script> 
<script type="text/javascript" src="<c:url value='/js/libs/jquery-latest.min.js'/>"></script> 
<script type="text/javascript" src="<c:url value='/js/libs/jquery-plugins.js'/>"></script> 
<script type="text/javascript">
$(document).ready(function(){
	var logout='false';
	var carlock = null;
	var carId = ${detailCar.carId};
   	$('input[name="isGlobalStartDateClicked"]').val('');
	$('.chld_vendor_num').hide();
	var isCarCopied =  '<%=request.getAttribute("Copied") %>';
	var scrollPos = '<%=request.getAttribute("scrollPos") %>';
		//alert(scrollPos);
		if(isCarCopied == 'true'){
			//alert("Car Copied Successfully..");
			window.scrollTo(0,scrollPos);
			$('#copy_car_msg').append('Car Copied Successfully.').fadeIn(function(){
								var $this = $(this);
								setTimeout(function(){
									$this.fadeOut("slow");
								}, 5000);
							});
		}
		$('#BUYER').attr("id", "BUYER1");
		$('#BUYER').attr("id", "BUYER2");
		$('#BUYER1').attr("id", "BUYER");
		$("[title$='Logout']").click(function() {
			logout = 'true';
		});
		carlock ='<%=request.getSession(false).getAttribute("carLock")%>';
		$(window).bind('beforeunload', function (){
			if((logout != 'true') && (carlock != null)){
				$.ajax({
					type:"GET",
					url:"unlockCar.html?car_id="+carId,
					dataType: "html",
					async:false
				 });
			  }
			  
	    } );
});

</script>

]]>
</content>


