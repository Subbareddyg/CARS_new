<%@ include file="/common/taglibs.jsp"%>
<div id="ajaxMappingBody">
	<div class="jserrors" id="jserrors">
				  		</div>
				<c:url value='vendorCatalogDataMapping.html?method=getCarAttributes' var="formAction"/>
				<form:form commandName="vendorCatalogDataMappingForm" method="post" action="${formAction}" id="vendorCatalogDataMappingForm1" name="vendorCatalogDataMappingForm">
					<input type="hidden" name="catalogId" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}"/>
					<input type="hidden" name="vendorCatalogTmplID" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogTemplate.vendorCatalogTmplID}"/>
					<input type="hidden" name="vendorCatalogName" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogName}"/>
					

					<li style="font-size:11px;">
						<spring:bind path="vendorCatalogDataMappingForm.*">
							<c:if test="${not empty errors}">
								<div class="error"> 
									<c:forEach var="error" items="${errors}">
									<c:choose>
										<c:when test="${error == 'Mapping saved and completed successfully!' || error == 'Mapping Saved Successfully!'}">
											<span style="background: #FFFF00;" id="successful"><c:out value="${error}" escapeXml="false" />
											 </span>
											<br /> <br/>
										</c:when>
										<c:otherwise> 
											<img src="<c:url value="/images/iconWarning.gif"/>"
												alt="<fmt:message key="icon.warning"/>" class="icon" />
											<c:out value="${error}" escapeXml="false" />
											<br /> <br/>
										</c:otherwise>
									</c:choose>
									</c:forEach>
								</div>
						            
							</c:if>
						        </spring:bind>
						
						<b><fmt:message key="datamapping.mapping.groups"/></b><br><br></br>
						<c:if test="${not empty vendorCatalogDataMappingForm.productGroupList}" var="productGroup">			
							<ol class="olgroups">
							<c:forEach items="${vendorCatalogDataMappingForm.productGroupList}" var="productGroup">
								<li><form:checkbox path="productGroupSelected" label="${productGroup.name}" value="${productGroup.productGroupId}"  cssClass="chkBox"/></li>
							</c:forEach>
							</ol>
						</c:if>
					</li>
				</form:form><br>
				<%@ include file="inc_mappingrules.jsp"%>
	
</div>
