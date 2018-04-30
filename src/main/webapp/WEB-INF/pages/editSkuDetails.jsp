<%@ include file="/common/taglibs.jsp"%>

<%-- this is modal form for overriding sku attributes --%>
<body>
Enter any attribute value exceptions below. These exceptions will over-ride any Pattern attribute values for SKU and will only apply to this SKU.

<h3>Attribute Exceptions</h3>
<c:url value='/editSkuDetails.html?ajax=true' var="formAction"/>
<form:form action="${formAction}" method="post" id="editSkuDetailsForm">
<input type="hidden" name="sku_id" value="<c:out value="${sku.carSkuId}" />" />
<input type="hidden" name="car_id" value="<c:out value="${detailCar.carId}" />" />

<app:editSkuAttributes skuVar="sku" carVar="detailCar" styleClass="attrs" includeAttributeTypes="PRODUCT,SKU"></app:editSkuAttributes>

</form:form>
</body>