<%@ include file="/common/taglibs.jsp"%>
	<div id="ajaxFldDataMapping" >
	<div class="olheader" style="width:220px; font-size:11px; height:15px; border-bottom:none">
		<b>Mapped Field Translation</b>
	</div>
		
		<div class="olheader" style="width:220px; font-size:11px; border-top:none; height:210px">
		<c:url value='vendorCatalogDataMapping.html?method=mapFieldData' var="mapFieldAction"/>
		<form:form commandName="vendorCatalogDataMappingForm" method="post" action="${mapFieldAction}" id="vendorCatalogDataMappingForm3" name="vendorCatalogDataMappingForm">
		<table align="left" style="border: 1px solid rgb(208, 208, 208); font-size:11px; margin-top: 0px; width="100%">
		<tbody>
		<tr>
		<td class="vendorList">Vendor Value</td>
		<td class="vendorList">CARS Value</td>
		</tr>
			<c:set var="counter" value="-1"></c:set>
			<c:if test="${(not empty sessionScope.sesionVendorFldDataMapping) && (not empty sessionScope.sesionCarFldDataMapping)}" >
				<c:forEach items="${sessionScope.sesionVendorFldDataMapping}" var="venFldMapData">
					<c:set var="counter" value="${counter+1}"></c:set>
					<c:if test="${not empty venFldMapData.key && not empty venFldMapData.value.carValueList}">
						<tr>
							<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
								<c:out value="${venFldMapData.key}"/>
							</td>
							<td style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">
								<spring:bind path="vendorCatalogDataMappingForm.mapCarFldDataMapping[${counter}]">
										<select class="fldMap2" refVenFld='<c:out value="${venFldMapData.key}"/>'
											refVenHdr='<c:out value="${venFldMapData.value.vendorHeaderField}"/>' name="${status.expression}" onchange="change('${venFldMapData.value.vendorHeaderField}', '${venFldMapData.key}', '${counter}');">
												<option value =''>Choose a value</option> 
												<c:forEach items="${venFldMapData.value.carValueList}" var="carFldMapData">
												<!-- fix for population of selected value -->
												<!-- drpship phase 2 -->
													<option value='<c:out value="${carFldMapData}"/>'
															<c:if test="${venFldMapData.value.carValue == carFldMapData}">selected="selected"</c:if>
													>${carFldMapData}</option>
												</c:forEach>
											</select>
								</spring:bind>
							</td>
						</tr>
					</c:if>
				</c:forEach>
				<input type="hidden" name="mapVenHeaderData" value="" id="mapVenHeaderData"/>
				<input type="hidden" name="mapVenFldDataMapping" value="" id="mapVenFldDataMapping"/>
			</c:if>
		</tbody>
		</table>
		</form:form>
		</div>	
	</div>
	<script>
		function change(vendorHeaderField, vendorField, count) {
			$("#mapVenFldDataMapping").val(vendorField);	
			$("#mapVenHeaderData").val(vendorHeaderField);
			var loadUrl="vendorCatalogDataMapping.html?method=mapFieldData&ajax=true&count="+count;
			var dataString=$("#vendorCatalogDataMappingForm3").serialize();
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxFldDataMapping")
					.html(htmlResponse)
					.hide()
					.fadeIn(3000, function() {
						$('#message').append("Loading...");
					
					});
					//fldDataMap();
		      		}
		      		
		     	});
		}
	</script>