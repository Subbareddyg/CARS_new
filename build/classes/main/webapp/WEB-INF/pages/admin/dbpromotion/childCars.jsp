<!DOCTYPE html>
<%-- 
  - Description: this page deals with  Child car and Sku information listing 
  --%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" import="java.util.ArrayList"%>
<%@ page language="java" import="java.util.Arrays"%>


<c:set var="counter" value="-1"></c:set>
<c:set var="nameCnt" value="${sessionScope.nameCnt}"></c:set>


<%String paramVal = request.getParameter("paramVal");


String paramNewVal = request.getParameter("paramNewVal");
String childCarSkuListArr = request.getParameter("childCarSkuListArr");
String chkSkuListCheckBox = request.getParameter("chkSkuListCheckBox");
String parentCarStyleId = request.getParameter("parentCarStyleId");
String carCounterVal = request.getParameter("carCounter");
String allChildCars = request.getParameter("allChildCars");

if (paramVal.equals("one"))
{%>
<display:table name="viewChildCars" cellspacing="0" cellpadding="0" requestURI=""  id="childCarID" pagesize="25"  class="table" >
				<c:set var="counter" value="${counter+1}"></c:set>
				
				  	<display:column titleKey="" style="width: 5%">  
				  		<c:choose>
							<c:when test="${childCarID.styleTypeCd != 'OUTFIT' && childCarID.styleTypeCd != 'PRODUCT'}">
								<img src="<c:url value='/images/pattern.gif' />" />
							</c:when>			
						</c:choose>
				  	</display:column>
				  	
				  	<display:column titleKey="dbPromotion.car" style="width: 15%">  
				  		<div class="car_id"><c:out value="${childCarID.carId}" /></div>
				  		<input type="hidden" id="parentCarId${counter}" value="${childCarID.carId}"/>
				  	</display:column>
				  	
    		    	<display:column property="styleNumber"  titleKey="dbPromotion.style" style="width: 25%"/>
    		    
    		     	<display:column titleKey="child.product.name" style="width: 25%">
    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarID.productName}" /></div>
    		     	</display:column>
    		     	
    		     	<display:column titleKey="child.brand.name" style="width: 25%">
    		     		<div id="brandName${counter}" class="brandName"> <c:out value="${childCarID.brandName}" /></div>
    		     	</display:column>
					
    		     			     
    		     	<display:column  sortable="false" titleKey="child.order"  style="width: 15%">
    		     	
	    		     	<div id="childOrder" class="childOrder">
				    		<select name="order" id="order${counter}" >
				    		 					<option value="-1">Select</option>
	      						<c:forEach items="${viewChildCars}" varStatus="rowCounter">
	      						    <c:choose>
      							 			<c:when test="${childCarID.order eq rowCounter.count}">
				    							<option value="${childCarID.order}" selected ><c:out value="${rowCounter.count}" /></option>
				    						</c:when>
				    						<c:otherwise>
				    								<option value="${rowCounter.count}"><c:out value="${rowCounter.count}" /></option>
				    						</c:otherwise>
				    				</c:choose>
				    						
				    			</c:forEach>
				    		</select>
				    	</div>
			    	
					</display:column>
					
					<display:column  titleKey="child.defualt.sku"  style="width: 15%">
			    		
			    		 <select name="sku" id="sku${counter}" style="width: 130px">
			    		  
			    						 <option value="-1">Select</option>
       							 <c:forEach items="${childCarID.colorSkuMap}" var="item">
								 <c:choose>
      							 			<c:when test="${childCarID.sku eq item.key}">
														<option value="${item.value}" selected >${item.key}</option>  
											</c:when>
				    						<c:otherwise>
													<option value="${item.value}">${item.key}</option> 
											</c:otherwise>
				    				</c:choose>
       							  	 
        						</c:forEach>
 						</select>
					</display:column>
				
    		    <display:column sortable="false" titleKey="dbPromotion.remove"  style="width: 15%">
			    		<!-- <input type="hidden" name="removeFlag" id="chk_selector" class="chkbox" value="${viewChildCars[counter].carId}"/> --> 
			    		<a class="removeChildCar" href="removeChildCar.html?method=removeDBPromotionChild&ChildCarId=${childCarID.carId}">Remove</a>
				</display:column>
				<display:setProperty name="paging.banner.item_name" value="child car"/>
	    		<display:setProperty name="paging.banner.items_name" value="child cars"/>
</display:table>
<%}
else {%>	
<c:set var="paramNewVal" value="<%=paramNewVal %>"></c:set>
<c:set var="chekdef" value="checked"></c:set>	


						<display:table name="<%=childCarSkuListArr %>" cellspacing="0" cellpadding="0" requestURI=""  id="childCarSkuArr" pagesize="1000"  class="table" >
										<c:set var="counter" value="${counter+1}"></c:set>
										<c:set var="nameCnt" value="${nameCnt+1}"></c:set>									
									 									
										<display:column sortProperty="enabled" sortable="true" titleKey="" media="html">
											<input type="checkbox" name="chkSkuList${nameCnt}" id ="chk${nameCnt}"  class="<%=chkSkuListCheckBox %>"   <c:if test="${childCarSkuArr.skuSelValues == '1'}">checked="checked"</c:if> />
									    </display:column>
										
										   <display:column titleKey="dbPromotion.car" style="width: 10%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.skuCarid}" /></div>
										  		<input type="hidden" name="skuCarid${nameCnt}" id="skuCarid${nameCnt}" value="${childCarSkuArr.skuCarid}"/>
										  	</display:column>
										
											<display:column titleKey="dbPromotion.vendorname" style="width: 30%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorName}" /></div>
										  		<input type="hidden" name="vendorName${nameCnt}" id="vendorName" value="${childCarSkuArr.vendorName}"/>
										  	</display:column>

										  	
										  	<display:column titleKey="dbPromotion.stylename" style="width: 25%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.skuStyleName}" /></div>
										  		<input type="hidden" name="styleName${nameCnt}" id="styleName" value="2"/>
										  	</display:column>

										  	<display:column titleKey="dbPromotion.style" style="width: 10%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorStyle}" /></div>
										  		<input type="hidden" name="vendorStyle${nameCnt}" id="vendorStyle" value="${childCarSkuArr.vendorStyle}"/>
										  	</display:column>
										  	
										  	<display:column titleKey="dbPromotion.upc" style="width: 15%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorUpc}" /></div>
										  		<input type="hidden" name="vendorUpc${nameCnt}" id="vendorUpc" value="${childCarSkuArr.vendorUpc}"/>
										  		<input type=hidden name="belkSku${nameCnt}" id="belkSku${nameCnt}" value="${childCarSkuArr.belkSku}"/>
										  	</display:column>
										  	
										  	<display:column titleKey="dbPromotion.colorcode" style="width: 5%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.skuColor}" /></div>
						    		     		<input type="hidden" name="skuColor${nameCnt}" id="skuColor" value="${childCarSkuArr.skuColor}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="dbPromotion.colorname" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.colorName}" /></div>
						    		     		<input type="hidden" name="colorName${nameCnt}" id="colorName${nameCnt}" value="${childCarSkuArr.colorName}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="dbPromotion.sizename" style="width: 30%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.sizeName}" /></div>
						    		     		<input type="hidden" name="sizeName${nameCnt}" id="sizeName" value="${childCarSkuArr.sizeName}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="dbPromotion.completiondate" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.compDt}" /></div>
						    		     		<input type="hidden" name="completiondate${nameCnt}" id="completiondate" value="${childCarSkuArr.compDt}"/>
						    		     		<input type="hidden" name="skuCount" id="skuCount" value="${nameCnt}"/>
						    		     	</display:column>
									
						    			<display:setProperty name="paging.banner.item_name" value="sku"/>
							    		<display:setProperty name="paging.banner.items_name" value="skus"/>
							    		
							    			
						</display:table>
						<c:set var="nameCnt" value="${nameCnt}" scope="session" />
						
						

<%} %>

<input type="hidden" name="allChildCarIds_<%=carCounterVal%>" id="allChildCarIds_<%=carCounterVal%>" value="${childCarSkuArr.allChildCarIds}"/>
<input type="hidden" name="childCarCount" id="childCarCount" value="${counter}"/>

<script type="text/javascript">


function selectedBelkUpc()
{
//	sessionStorage.setItem('bUpc', JSON.stringify(""));
	sessionStorage.setItem('bUpc', '');
	var selBelkUpc = "";
	var skucount = $('#skuAllCount').val();
	for(j=0;j<=skucount;j++)
	{
	    	if($('#chk'+j).is(':checked')) 
			{
				var belkSkuUpc = $("#belkSku"+j).val();
				selBelkUpc = selBelkUpc +","+belkSkuUpc;
			}
	 }
	sessionStorage.setItem('bUpc', selBelkUpc);

	var tempType ="";
	if($('#CarId').val() == null || $('#CarId').val() =="")
		tempType = $('#categoryFilter').val();
	else
		tempType = $('#templateTypeVal').val();
	sessionStorage.setItem('templateTypeHidden', tempType);
}
var idArray = new Array();
$("input[id^='parentCarId']").each(function(){
	idArray.push($(this).val());
});
function setID()
{
	for (var i = 0; i < idArray.length; i++) 
	{
		var text = $('.child_'+i).children('.x-panel-header').html('Child Car Id : '+idArray[i]+'<span class=hiddencardchildid style="display:none">'+idArray[i]+'</span>');
	}
}
setID();
</script>