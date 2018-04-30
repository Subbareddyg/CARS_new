<%@ include file="/common/taglibs.jsp"%>
<c:choose>
	<c:when test="${not empty errors}">
		<div class="error">
			<c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<!-- If there is error increase the size of the container -->
		<script type="text/javascript">
			$("#confirm-container").height(200);
		</script>
	</c:when>
	<c:otherwise>
		<!-- If the name does not exists set the name and submit the form -->
		<c:if test="${tmplNameError == false}">
			<script type="text/javascript">
				if($('#isGlobal:checked').val() =='Y'){
					//If it is a global template set the values back on the form
					$("#vendorCatalogTemplateName").val($("#globalTemplateName").val());
					$("#globalTemplateValue").val('true');
				}else{
					//Set the global template value to false
					$("#globalTemplateValue").val('false');
				}
				$("#vendorCatalogDataMappingForm2").submit();
			</script>
		</c:if>
	</c:otherwise>
</c:choose>
