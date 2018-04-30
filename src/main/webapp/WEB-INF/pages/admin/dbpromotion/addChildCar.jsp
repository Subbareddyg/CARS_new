<!DOCTYPE html>
<%-- 
  - Description: this page is responsible for loading "Enter the Child car Id" panel in modal dialog box to add child cars.
  --%>
<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="dbPromotion.add.child" />
	</title>
</head>
<%
//check the car id in request, if its null, pass empty
String CarId=request.getParameter("CarId");

if(CarId==null)
	CarId="";
%>
<body class="admin">
			<c:url value="/admin/dbpromotion/addChildCar.html" var="formAction" />
			<form:form commandName="dbPromotionForm" method="post"
				action="${formAction}" onsubmit="return onFormSubmit(this)" id="dbPromotionForm">
				<fieldset>
				<ul>
					<li>
						<span>
						<label for="txt_child_car_id">
							* <fmt:message key='dbPromotion.car'/>:
						</label>
							<form:input path="childCarId" id="txt_child_car_id" size="12" maxlength="12"/>
						</span>
					</li>
					<li style="height:20px;">
					<label>&nbsp;</label><span id="add_child_car_msg"></span>
					</li>
				</ul>
				</fieldset>
					<input type="hidden" name="dbPromotionCarId" value="<%=CarId%>"/>
					<input type="hidden" name="selectedUpc" id="selectedUpc" value=""/>
					<input type="hidden" name="hiddenTemplateType" id="hiddenTemplateType" value=""/>
			</form:form>

</body>



