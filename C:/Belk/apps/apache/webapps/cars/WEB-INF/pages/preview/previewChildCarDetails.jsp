<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="-1"></c:set>
<display:table name="childCar" cellspacing="0" cellpadding="0" requestURI=""  id="childCarID" pagesize="25"  class="table" >
				<c:set var="counter" value="${counter+1}"></c:set>
				  	<display:column property="carId" titleKey="outfit.car"/>  
    		    	<display:column property="styleNumber"  titleKey="outfit.style"/>
    		     	<display:column property="productName" class="prdName" titleKey="child.product.name" />
    		     	<display:column  property="brandName"  titleKey="child.brand.name" />
    		     	<display:column  property="order"  sortable="false" titleKey="child.order"   />
					<display:column property="sku"  titleKey="child.defualt.sku"  />
				    <display:setProperty name="paging.banner.item_name" value="child car"/>
	    		<display:setProperty name="paging.banner.items_name" value="child cars"/>
</display:table>
<input type="hidden" name="childCarCount" id="childCarCount" value="${counter}"/>

