<%@ include file="/common/taglibs.jsp"%>
<div id="ajaxDivRules">
	<c:url value='vendorCatalogDataMapping.html?method=saveTemplate' var="formAction"/>
	<c:url value='vendorCatalogDataMapping.html?method=saveCompleteTemplate' var="formCompleteAction"/>
	<form:form commandName="vendorCatalogDataMappingForm" method="post" action="${formCompleteAction}" id="vendorCatalogDataMappingForm2" name="vendorCatalogDataMappingForm">
		<input type="hidden" name="catalogId" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}"/>
		<input type="hidden" name="vendorCatalogTmplID" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogTemplate.vendorCatalogTmplID}"/>
		<input type="hidden" name="vendorCatalogName" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogName}"/>
		<input type="hidden" name="productGroupSelected" value="${vendorCatalogDataMappingForm.productGroupSelected}"/>
		<input type="hidden" name="teeeeeee" value="printval"/>
	<table>
		<tbody>
			<tr>
				<td>
					<div class="olheader">
						<b><fmt:message key="datamapping.mapping.vendorfields"/></b>
						<ol>
							<c:if test="${not empty vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogHeaderList}" >
								<c:forEach items="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogHeaderList}" var="catalogHeader">
									<li class="listyle" refVenVal='<c:out value="${catalogHeader.vendorCatalogHeaderFieldName}"/>'><c:out value="${catalogHeader.vendorCatalogHeaderFieldName}"/></li>
								</c:forEach>
								<input type="hidden" name="mapVendorSuppliedField" value="" id="mapVendorSuppliedField"/>
							</c:if>
						</ol>
					</div>
				</td>
				<td>
				<div class="olheader">
					<b><fmt:message key="datamapping.mapping.carsFields"/></b>
						<ol>
							<c:if test="${not empty vendorCatalogDataMappingForm.catalogMasterAttribute}" >
								<c:forEach items="${vendorCatalogDataMappingForm.catalogMasterAttribute}" var="mastAttr">
									<li class="limasterstyle" refMsId="<c:out value="${mastAttr.catalogMasterAttrId}"/>" refMsNm="<c:out value="${mastAttr.catalogMasterAttrName}"/>">
										<c:out value="${mastAttr.catalogMasterAttrName}"/>
									</li>
								</c:forEach>
								<input type="hidden" name="mapMasterMappingId" value="" id="mapMasterMappingId"/>
								<input type="hidden" name="mapMasterAttribute" value="" id="mapMasterAttribute"/>
							</c:if>
						
							<c:if test="${not empty vendorCatalogDataMappingForm.blueMartiniAttributes}" >
								<c:forEach items="${vendorCatalogDataMappingForm.blueMartiniAttributes}" var="bmAttr">
									<li class="libmstyle" refbm='<c:out value="${bmAttr}"/>'><c:out value="${bmAttr}"/></li>
								</c:forEach>
								<input type="hidden" name="mapBlueMartiniAttribute" value="" id="mapBlueMartiniAttribute"/>
							</c:if>
						</ol>
				</div>
	</td>

	<td>
	<div style="overflow: auto; height: 177px; width: 50px; margin-left: 34px;">
		<table style="border:0">
		<tbody>
		<tr>&nbsp;<br><br><br></tr>
		<tr align="middle">
			<input type="button" class="move_right" name="save" value="&gt;&gt;" id="move_right">
		</tr>
		<tr>
		<br><br>
		</tr>
	
		<tr align="middle">
			<input type="button" class="move_left" name="save" value="&lt;&lt;" id="move_left">
		</tr>
		</tbody>
		</table>
	</div>
	</td>

	<td>
	
	<div class="olheader">
	<b><fmt:message key="datamapping.mapping.mappedFields"/></b>	
	<table align="left" style="border: 1px solid rgb(208, 208, 208); width="100%">
	<tr>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
		<div class="vendorList">
			Vendor
		</div>
	</td>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
		<div class="vendorList">
			CARS
		</div>
		
	
	</td>
	</tr>


	</table>
	</div>
	</td>

	<td>
	<div class="olheader">
	<b>Mapped Field Translation</b>
	<table align="center" style="border: 1px solid rgb(208, 208, 208);">
	<tbody>
	<tr>
	<td class="vendorList">Vendor Value</td>
	<td class="vendorList">CARS Value</td>
	</tr>

	<tr>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">USA</td>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);"><select width="15" name="stateCd" class="text" id="stateCd">
	<option value="AK">Domestic</option>
	<option value="AK">Import</option>

	</select></td>
	</tr>

	<tr>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">China</td>
	<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);"><select width="15" name="stateCd" class="text" id="stateCd">
	<option value="AK">Domestic</option>
	<option value="AK">Import</option>

	</select></td>
	</tr>

	</tbody>
	</table>
	</div>
	</td>


	</tr></tbody></table>
	</form:form>
</div>