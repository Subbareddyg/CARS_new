<%@ include file="/common/taglibs.jsp"%>

<c:set var="counter" value="-1"></c:set>
<display:table name="${viewChildCarsSkus.list}" cellspacing="0" cellpadding="0" requestURI=""  id="childCarID" pagesize="25"  class="table" >
<c:set var="counter" value="${counter+1}"></c:set>

	<c:set var="counter" value="${counter+1}"></c:set>

<display:column sortable="false" titleKey='<input type="checkbox" disabled="disabled" id="checkall" name="checkall" />' style="width: 2%">
<input type="checkbox" disabled="disabled" id="${counter}" name="chk" />
	<spring:bind path="stylesSkuForm.list[${counter}].skuSelected">
	<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"
id="<c:out value="${status.expression}"/>" />
	</spring:bind>
	</display:column>

	<display:column titleKey="outfit.skuId" style="width: 15%">
		<div id="childSkuId${counter}" class="car_id"><c:out value="" /></div>
	</display:column>

	<display:column titleKey="outfit.carId" style="width: 25%" />

	<display:column titleKey="outfit.startDate" style="width: 25%">
		<div id="prdName${counter}" class="prdName"><c:out value="" /></div>
	</display:column>

	<display:setProperty name="paging.banner.item_name" value="child car"/>
	 <display:setProperty name="paging.banner.items_name" value="child cars"/>
</display:table>


<input type="hidden" name="childCarCount" id="childCarCount" value="${counter}"/>
