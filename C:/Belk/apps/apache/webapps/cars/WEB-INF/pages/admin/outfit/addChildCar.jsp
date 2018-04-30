<!DOCTYPE html>
<%-- 
  - Author(s): 
  - Date: 13/03/2013
  - Copyright Notice: 2013 Belk Inc.
  - @(#)
  - Description: this page specifies adding the Child car 
  --%>
<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="outfit.add.child" />
	</title>
</head>
<%
//check the car id in request, if its null, pass empty
String CarId=request.getParameter("CarId");

if(CarId==null)
	CarId="";
%>
<body class="admin">
			<c:url value="/admin/outfit/addChildCar.html" var="formAction" />
			<form:form commandName="outfitForm" method="post"
				action="${formAction}" onsubmit="return onFormSubmit(this)" id="outfitForm">
				<fieldset>
				<ul>
					<li>
						<span>
						<label for="txt_child_car_id">
							* <fmt:message key='outfit.car'/>:
						</label>
							<form:input path="childCarId" id="txt_child_car_id" size="12" maxlength="12"/>
						</span>
					</li>
					<li style="height:20px;">
					<label>&nbsp;</label><span id="add_child_car_msg"></span>
					</li>
				</ul>
				</fieldset>
					<input type="hidden" name="outfitCarId" value="<%=CarId%>"/>
					<input type="hidden" name="selectedUpc" id="selectedUpc" value=""/>
					<input type="hidden" name="hiddenTemplateType" id="hiddenTemplateType" value=""/>
			</form:form>

</body>



