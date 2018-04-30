<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.belk.car.app.model.oma.*" %>

<div class="x-panel-header">
	<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
		Service Info
		</c:if>
		<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
		Vendor Info
		</c:if>
</div>
<div class="x-panel-body"><c:choose>
	<c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">

			<li class="txt_attr_name" style="width: 35%;"><b>Vendor #:</b> <c:out
				value="${sessionScope.vndrFulfillmentService.venNum}" /></li>

			<li class="txt_attr_name" style="width: 30%;"><b>Service ID:</b>
			<c:out
				value="${sessionScope.vndrFulfillmentService.fulfillmentServId}" />
			</li>
			<li class="txt_attr_name"><b>Date Created:</b> <c:out
				value="${sessionScope.vndrFulfillmentService.strCreatedDate}" /></li>

		</ul>


		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">

			<li class="ctxt_attr_name" style="width: 35%;"><b>Vendor
			Name:</b> <c:out value="${sessionScope.vndrFulfillmentService.venName}" />
			</li>

			<li class="txt_attr_name" style="width: 30%;"><b>Service
			Name:</b> <c:out
				value="${sessionScope.fulfillmentService.fulfillmentServiceName}" />
			</li>
			<li class="txt_attr_name"><b>Created By:</b> <c:out
				value="${sessionScope.vndrFulfillmentService.createdBy}" /></li>


		</ul>

		<ul class="car_info"
			style="font-size: 11px; padding: 0 0 10px !important;">
		<li class = "txt_attr_name" style="width: 35%;" >
				<b>Vendor Status:</b>
				<c:out value="${sessionScope.vndrFulfillmentService.statusCd}"/>	
			</li>
			<li class = "txt_attr_name" style="width:30%;">
				<b>Service Status: </b>
				<c:out value="${sessionScope.fulfillmentService.fulfillmentServiceStatusCode}"/>
			</li>
			<li class="txt_attr_name" ><b>Date Last
			Updated: </b> <c:out
				value="${sessionScope.vndrFulfillmentService.strUpdatedDate}" /></li>
		
			
		</ul>
		<ul class="car_info" style="font-size:11px;
padding:0 0 10px !important;">
			<li class = "txt_attr_name" style="width:35%;"><b>Last
			Updated By:</b> <c:out
				value="${sessionScope.vndrFulfillmentService.updatedBy}" />
			</li>
			<li class = "txt_attr_name" style="width:30%;">&nbsp;
			</li>
			<li class="txt_attr_name" >&nbsp;</li>
		
		
	
		</ul>
	</c:when>
	<c:otherwise>
		<c:if
			test="${sessionScope.fulfillmentService.fulfillmentServiceID != null}">
			<ul class="car_info"
				style="font-size: 11px; padding: 0 0 10px !important;">

				<li class="txt_attr_name" style="width: 35%;"><b>Service
				ID:</b> <c:out
					value="${sessionScope.fulfillmentService.fulfillmentServiceID}" />
				</li>
				<%String formattedDate= "";%>
				<%DateFormat df;%>
				<%Date date;%>
				<li class="txt_attr_name" style="width: 25%;"><b>Date Created :</b>

<%date =((FulfillmentService)session.getAttribute("fulfillmentService")) != null ?((FulfillmentService)session.getAttribute("fulfillmentService")).getCreatedDate():new Date(); %>

                                                <%df = new SimpleDateFormat("MM/dd/yyyy"); %> 

                                                <%formattedDate = df.format(date); %>

                                                 <c:out  value="<%=formattedDate%>" escapeXml="false"/>

                        </li>
				<li class="txt_attr_name" style="width: 25%;"><b>Date Last Updated :</b>

<%date =((FulfillmentService)session.getAttribute("fulfillmentService")) != null ?((FulfillmentService)session.getAttribute("fulfillmentService")).getUpdatedDate():new Date(); %>

                                                <%df = new SimpleDateFormat("MM/dd/yyyy"); %> 

                                                <%formattedDate = df.format(date); %>

                                                 <c:out  value="<%=formattedDate%>" escapeXml="false"/>

                        </li>




			</ul>


			<ul class="car_info"
				style="font-size: 11px; padding: 0 0 10px !important;">

				<li class="ctxt_attr_name" style="width: 35%;"><b>Service
				Name:</b> <c:out
					value="${sessionScope.fulfillmentService.fulfillmentServiceName}" />
				</li>

				<li class="txt_attr_name" style="width: 25%;"><b>Created
				By:</b> <c:out value="${sessionScope.fulfillmentService.createdBy}" /></li>
				<li class="txt_attr_name"><b>Last Updated By:</b> <c:out
					value="${sessionScope.fulfillmentService.updatedBy}" /></li>

		
			</ul>
			<ul class="car_info" style="font-size:11px;
padding:0 0 10px !important;">
		
		<li class = "txt_attr_name" style="width:35%;">
				<b>Service Status: </b>
				<c:out value="${sessionScope.fulfillmentService.fulfillmentServiceStatusCode}"/>
			</li>
		
	
		</ul>
		</c:if>
	</c:otherwise>
</c:choose></div>