<!DOCTYPE html>
<%-- 
  - Author(s): Karthik & Anto
  - Date: 13/03/2013
  - Copyright Notice: 2013 Belk Inc.
  - @(#)
  - Description: this page specifies listing the SKU information 
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

%>
<c:set var="paramNewVal" value="<%=paramNewVal %>"></c:set>

<c:set var="chekdef" value="checked"></c:set>	


						<display:table name="<%=childCarSkuListArr %>" cellspacing="0" cellpadding="0" requestURI=""  id="childCarSkuArr" pagesize="25"  class="table" >
										<c:set var="counter" value="${counter+1}"></c:set>
										<c:set var="nameCnt" value="${nameCnt+1}"></c:set>									
									 									
										<display:column sortProperty="enabled" sortable="true" titleKey="" media="html">
											<input type="checkbox" disabled name="chkSkuList${nameCnt}" class="<%=chkSkuListCheckBox %>"   <c:if test="${childCarSkuArr.skuSelValues == '1'}">checked="checked"</c:if> />
									    </display:column>
										
										   <display:column titleKey="outfit.car" style="width: 10%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.skuCarid}" /></div>
										  		<input type="hidden" name="skuCarid${nameCnt}" id="skuCarid" value="${childCarSkuArr.skuCarid}"/>
										  	</display:column>
										
											<display:column titleKey="outfit.vendorname" style="width: 30%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorName}" /></div>
										  		<input type="hidden" name="vendorName${nameCnt}" id="vendorName" value="${childCarSkuArr.vendorName}"/>
										  	</display:column>

										  	
										  	<display:column titleKey="outfit.stylename" style="width: 25%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.skuStyleName}" /></div>
										  		<input type="hidden" name="styleName${nameCnt}" id="styleName" value="2"/>
										  	</display:column>
										  	
										  	<display:column titleKey="outfit.style" style="width: 10%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorStyle}" /></div>
										  		<input type="hidden" name="vendorStyle${nameCnt}" id="vendorStyle" value="${childCarSkuArr.vendorStyle}"/>
										  	</display:column>
										  	
										  	<display:column titleKey="outfit.upc" style="width: 15%">  
										  		<div class="car_id"><c:out value="${childCarSkuArr.vendorUpc}" /></div>
										  		<input type="hidden" name="vendorUpc${nameCnt}" id="vendorUpc" value="${childCarSkuArr.vendorUpc}"/>
										  		<input type=hidden name="belkSku${nameCnt}" id="belkSku" value="${childCarSkuArr.belkSku}"/>
										  	</display:column>
										  	
										  	<display:column titleKey="outfit.colorcode" style="width: 5%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.skuColor}" /></div>
						    		     		<input type="hidden" name="skuColor${nameCnt}" id="skuColor" value="${childCarSkuArr.skuColor}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="outfit.colorname" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.colorName}" /></div>
						    		     		<input type="hidden" name="colorName${nameCnt}" id="colorName" value="${childCarSkuArr.colorName}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="outfit.sizename" style="width: 30%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.sizeName}" /></div>
						    		     		<input type="hidden" name="sizeName${nameCnt}" id="sizeName" value="${childCarSkuArr.sizeName}"/>
						    		     	</display:column>
						    		     	
						    		     	<display:column titleKey="outfit.completiondate" style="width: 25%">
						    		     		<div id="prdName${counter}" class="prdName"> <c:out value="${childCarSkuArr.compDt}" /></div>
						    		     		<input type="hidden" name="completiondate${nameCnt}" id="completiondate" value="${childCarSkuArr.compDt}"/>
						    		     		<input type="hidden" name="skuCount" id="skuCount" value="${nameCnt}"/>
						    		      	</display:column>
									
						    			<display:setProperty name="paging.banner.item_name" value="sku"/>
							    		<display:setProperty name="paging.banner.items_name" value="skus"/>
							    		
							    			
						</display:table>
						<c:set var="nameCnt" value="${nameCnt}" scope="session" />


<input type="hidden" name="childCarCount" id="childCarCount" value="${counter}"/>




