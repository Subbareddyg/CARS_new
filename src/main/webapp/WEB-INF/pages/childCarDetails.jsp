<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="-1"></c:set>
<display:table name="viewChildCars" cellspacing="0" cellpadding="0" requestURI=""  id="childCarID" pagesize="25"  class="table" >
				<c:set var="counter" value="${counter+1}"></c:set>
				  	<display:column property="carId" titleKey="outfit.car" style="width: 15%">
					</display:column>  
    		    	<display:column property="styleNumber"  titleKey="outfit.style" style="width: 25%"/>
    		    
    		     	<display:column property="productName" class="prdName" titleKey="child.product.name" style="width: 25%"/>
    		     	<display:column titleKey="child.brand.name" style="width: 25%">
					  <div id="brandName${counter}" class="brandName"> <c:out value="${childCarID.brandName}" /></div>
    			    </display:column>
    		     	<display:column sortable="false" titleKey="child.order"  style="width: 15%" >
				        <input type="text" id ="childOrder" readonly=readonly" value="${childCarID.order}" style="border: 0" >
				       </display:column>  
					<display:column titleKey="child.defualt.sku"  style="width: 15%" >
				        <input type="text" readonly="readonly"  style="border: 0"  value="${childCarID.sku}" >  
				        <input type="hidden" id="parentCarId${counter}" value="${childCarID.carId}">
					</display:column>  
				<display:setProperty name="paging.banner.item_name" value="child car"/>
	    		<display:setProperty name="paging.banner.items_name" value="child cars"/>
</display:table>

<input type="hidden" name="childCarCount" id="childCarCount" value="${counter}"/>




