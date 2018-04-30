<%@ include file="/common/taglibs.jsp"%>
<br>
<div id="car_history_details_pnl_${carId}" class="cars_panel x-hidden collapsed">
	<div class="x-panel-header">
		Car History
	</div>
	<div class="x-panel-body" style="font-size: 11px;font-family: tahoma,arial,helvetica,sans-serif;">
	<table style="width: 350px;">
		<c:forEach items="${carHistories}" var="hist">
		<tr>
			<c:choose>
		   <c:when test="${empty hist.assignedToUserType.name}">
			 <td><c:out value="Web Merchant" />:	</td>
			  <td><c:out value="${hist.workflowStatus.name}" /></td>
			</c:when>
			<c:otherwise>
			 	<td><c:out value="${hist.assignedToUserType.name}" />:	</td>
			<td><c:out value="${hist.workflowStatus.name}" /></td>
			</c:otherwise>
		</c:choose>
			<td><c:out value="${hist.createdDate}" /></td>
		</tr>
		</c:forEach>
	</table>

		
		<br>
		<Div>
		<table style="width: 100px;">
		<tr><td style="white-space: nowrap;">Copy Quality:</td>
		<td style="white-space: nowrap;"><c:choose>
			<c:when test="${carQualityCode=='G'}">
				Good
			</c:when>
			<c:otherwise>
			<c:choose>
					<c:when test="${carQualityCode=='B'}">
					    Bad
				    </c:when>
					<c:otherwise>
					   Not-Specified
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose></td>
		</tr>
		</table>		
		</Div>
		
		<div>
		<table style="width: 100px;">
		<tr><td style="white-space: nowrap;"> Number of Rejections:</td>
		    <td>
		     <c:choose>
			     <c:when test="${rejectionCount=='-1'}">
					0
				</c:when>
				<c:otherwise>
				  <c:out value="${rejectionCount}" />
				</c:otherwise>
			</c:choose>
			</td>
			</tr>
			</table>
		</div>
	</div>
</div>