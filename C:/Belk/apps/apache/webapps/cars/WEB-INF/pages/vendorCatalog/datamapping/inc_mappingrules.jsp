<%@ include file="/common/taglibs.jsp"%>
<div id="ajaxDivRules" style="font-size:11px;">
   
	<c:url value='vendorCatalogDataMapping.html?method=saveTemplate' var="formAction"/>
	<c:url value='vendorCatalogDataMapping.html?method=saveCompleteTemplate' var="formCompleteAction"/>
	<table>
		<tbody>
			<tr><td>
	<form:form commandName="vendorCatalogDataMappingForm" method="post" action="${formCompleteAction}" id="vendorCatalogDataMappingForm2" name="vendorCatalogDataMappingForm">
		<input type="hidden" name="catalogId" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}"/>
		<input type="hidden" name="vendorCatalogTmplID" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogTemplate.vendorCatalogTmplID}"/>
		<input type="hidden" name="vendorCatalogTemplateName" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogTemplate.vendorCatalogName}" id="vendorCatalogTemplateName"/>
		<input type="hidden" name="vendorCatalogName" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogName}"/>
		<input type="hidden" name="selectedProductGroups" value="${vendorCatalogDataMappingForm.selectedProductGroups}"/>
		<input type="hidden" name="glbTemplate" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogTemplate.global}" id="globalTemplateValue"/>
		
		<input type="hidden" name="mapVendorSuppliedFieldID" value="" id="mapVendorSuppliedFieldID"/>
		<input type="hidden" name="mapVendorSuppliedField" value="" id="mapVendorSuppliedField"/>
		
		
		<input type="hidden" name="mapMasterMappingId" value="" id="mapMasterMappingId"/>
		<input type="hidden" name="mapMasterAttribute" value="" id="mapMasterAttribute"/>
		<input type="hidden" name="mapBlueMartiniKey" value="" id="mapBlueMartiniKey"/>
		<input type="hidden" name="mapBlueMartiniAttribute" value="" id="mapBlueMartiniAttribute"/>
		
		<input type="hidden" name="mapRowNumber" value="" id="mapRowNumber"/>
								
	
				<td style="width:180px;">
					<div class="olheader" style="width:170px; border-bottom:none; height:20px">
					<b><fmt:message key="datamapping.mapping.vendorfields"/></b>
					</div>
					<div class="olheader" style="width:170px; border-top:none">
						<ol> 
							<c:if test="${not empty sessionScope.sessionVendorHeaderList}" >
								<c:forEach items="${sessionScope.sessionVendorHeaderList}" var="catalogHeader">
									<li class="listyle" refVenKey='<c:out value="${catalogHeader.key}"/>' refVenVal='<c:out value="${catalogHeader.value.vendorCatalogHeaderFieldName}"/>'>
										<c:out value="${catalogHeader.value.vendorCatalogHeaderFieldName}"/>
									</li>
								</c:forEach>
								
							</c:if>  
						</ol>
					</div>
				</td>
				<td>&nbsp;</td>
				<td style="width:180px;">
				<div class="olheader" style="width:170px; border-bottom:none; height:20px">
					<b><fmt:message key="datamapping.mapping.carsFields"/></b>
				</div>
				<div class="olheader" style="width:170px; border-top:none">
						<ol>
							<c:if test="${not empty sessionScope.sessionCarAttribute}" >
								<c:forEach items="${sessionScope.sessionCarAttribute}" var="attr">
									<c:choose>
									<c:when test="${attr.masterAttribute == 'Y'}">
										<li class="limasterstyle" refMsId="<c:out value="${attr.attributeKey}"/>" refMsNm="<c:out value="${attr.attributeName}"/>">
										<c:out value="${attr.attributeName}"/>
										</li>
									</c:when>
									<c:otherwise>
										<li class="libmstyle" refBM='<c:out value="${attr.attributeName}"/>' refBMKey='<c:out value="${attr.attributeKey}"/>'>
										<c:out value="${attr.attributeName}"/>
										</li>	
									</c:otherwise>
									</c:choose>
								</c:forEach>
								
							</c:if>
						</ol>
				</div>
	</td>

	<td>
	<div style="overflow: auto; height: 175px; width: 25px; margin-left: 10px; font-size:11px;">
		<table style="border:0; font-size:11px;">
		
		<tr>&nbsp;<br><br><br></tr>
		<tr align="middle">
			<input type="button" class="move_right" name="save" value="&gt;&gt;" id="move_right">
		</tr>
		<tr>
		<br>
		</tr>
		<tr><br></tr>
		<tr align="middle">
			<input type="button" class="move_left" name="save" value="&lt;&lt;" id="move_left">
		</tr>
		
		</table>
	</div>
	</td>

	<td style="width:195px;">
	<div class="olheader" style="width: 170px; border-bottom:none; height: 20px">
	<b><fmt:message key="datamapping.mapping.mappedFields"/></b>
	</div>
	<div class="olheader" style="width: 170px; border-top:none; height: 210px;">
	<table align="left" style="border: 1px solid rgb(208, 208, 208); margin-top:0px; width="100%">
	<tr>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
		Vendor
	</td>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
		CARS
	</td>
	</tr>
	<c:if test="${not empty sessionScope.sessionCatalogMappedField}" >
		<c:forEach items="${sessionScope.sessionCatalogMappedField}" var="bmMappedAttr" varStatus="rowCounter">
		<tr class="trRowDisp" refRowNum='<c:out value="${rowCounter.count}"/>' >
			<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);" class="tdRowDisp">
			<c:out value="${bmMappedAttr.vendorSuppliedField}"/>
		</td><td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);"class="tdRowDisp">
			<c:out value="${bmMappedAttr.mappingAttribute}"/>
		</td></tr>
		</c:forEach>
		
	</c:if>
	</table>
	</div>
	</td>
<td>&nbsp;</td>


	
	</form:form>
	<td style="width:195px;">
		<%@ include file="inc_mappingFldrules.jsp"%>
	</td>
	</td>
	</tr></tbody></table>
</div>