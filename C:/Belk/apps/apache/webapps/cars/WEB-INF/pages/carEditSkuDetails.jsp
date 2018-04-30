<!DOCTYPE html>
<%-- 
  - Author(s): Karthik & Anto
  - Date: 13/03/2013
  - Copyright Notice: 2013 Belk Inc.
  - @(#)
  - Description: this page specifies listing the Sku header information 
 --%>
<%@ include file="/common/taglibs.jsp"%>
<%
String preVal;
%>
			
<c:set var="car_counter" value="-1"></c:set>

<%
int carCounter = -1;
%>
<c:set var="nameCnt" value="-1" scope="session" />

<c:forEach var="entry" items="${viewChildCarsSkuList}">

<c:choose>
	<c:when test="${entry.parentCarStyleId ne prevCarId}">
		<br/>
		<div id="child_car_Sku_list_${entry.parentCarStyleId}" class="cars_panel collapsed x-hidden child_${car_counter+1}">
				<div class="x-panel-header">
					<input type="hidden" name="skuChildCarId" id="skuChildCarId" value="${entry.skuCarid}"/>
				</div>			
				<div class="x-panel-body">
					<c:choose>
						<c:when test="${entry.parentCarStyleId ne prevCarId}">
						<c:set var="car_counter" value="${car_counter+1}"></c:set>
						
						<%
						carCounter++;
						%>
							<div>
								<jsp:include page="carEditChildSkus.jsp">
									<jsp:param name="paramVal" value="two" />
									<jsp:param name="paramNewVal" value="${entry.skuCarid}" />
									<jsp:param name="childCarSkuListArr" value="viewChildCarsSkuList_${car_counter}" />
									<jsp:param name="chkSkuListCheckBox" value="chkSkuList_${car_counter}" />
								</jsp:include>
							</div>
						</c:when>
					</c:choose>
					<br/>
				</div>
			</div>
	</c:when>
</c:choose>

<c:set var="prevCarId" value="${entry.parentCarStyleId}" />
</c:forEach>


