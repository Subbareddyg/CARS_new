<display:table name="viewChildCarsSkuList" cellspacing="0" cellpadding="0" requestURI=""  id="childCarSku" pagesize="25"  class="table" >
										<c:set var="counter" value="${counter+1}"></c:set>
										
											<display:column titleKey="dbPromotion.car" style="width: 15%">  
										  		<div class="car_id"><c:out value="${childCarSku.carId}" /></div>
										  	</display:column>
										
											<display:column titleKey="dbPromotion.vendorname" style="width: 15%">  
										  		<div class="car_id"><c:out value="${childCarSku.vendorUpc}" /></div>
										  	</display:column>
										  	
										  	<display:column titleKey="dbPromotion.upc" style="width: 15%">  
										  		<div class="car_id"><c:out value="${childCarSku.skuID}" /></div>
										  	</display:column>
										  	
										  	<display:column titleKey="dbPromotion.colorcode" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSku.skuColor}" /></div>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="dbPromotion.colorname" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSku.colorName}" /></div>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="dbPromotion.sizename" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSku.sizeName}" /></div>
						    		     	</display:column>
										
															
						    			<display:setProperty name="paging.banner.item_name" value="child car"/>
							    		<display:setProperty name="paging.banner.items_name" value="child cars"/>
						</display:table>
