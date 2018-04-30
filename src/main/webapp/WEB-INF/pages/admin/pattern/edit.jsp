<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pattern.edit.title" />
	</title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="pattern.edit.h1"/>
	</div>
	<div class="x-panel-body">
		<spring:bind path="patternForm.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="error">
					<c:forEach var="error" items="${status.errorMessages}">
						-<img src="<c:url value="/images/iconWarning.gif"/>"
							alt="<fmt:message key="icon.warning"/>" class="icon" />
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>
		<c:url value="/admin/pattern/edit.html" var="formAction" />
		<form:form commandName="patternForm" method="post"
			action="${formAction}" id="patternForm">
		<fieldset>
		<ul>
			<li>
				<label for="txt_patternName">
					* <fmt:message key='pattern.name'/>:
				</label>
				<form:textarea path="vendorStyleName" id="txt_patternName"
								cssClass="text required" cssErrorClass="text medium error"
								cols="30" rows="5" onkeyup="textCounter();"
								onblur="textCounter();" />
							<form:hidden path="patternId" /><br>
						<div class="text_counter"
								style="display: inline; margin-right: 360px;">
								Max Chars: 150 &nbsp;&nbsp;&nbsp;&nbsp; Chars Count: 
								<span id="pattern_name_length">0</span>
							</div>
						</li>
			<li>
				<label for="txt_patternDescr">
					* <fmt:message key='pattern.descr'/>:
				</label>
				<form:textarea path="descr" id="txt_patternDescr" cssClass="text required" cssErrorClass="text medium error" cols="30" rows="5"/>
			</li>
			<li>
				<label for="txt_classNumber">
					* <fmt:message key='pattern.class.number'/>:
				</label>
				<form:input path="classNumber" id="txt_classNumber" cssClass="text required" cssErrorClass="text medium error" maxlength="4"/>
				<span id="prod_types_loading" style="display:none;"><img src="images/ajaxLoading.gif" /></span>
			</li>
			<li>
				<label for="sel_productType">
					* <fmt:message key='pattern.product.type'/>:
				</label>
				<form:select path="productTypeId" id="sel_productType" items="${classProductTypes}" itemLabel="name" itemValue="productTypeId"
					cssClass="required" cssErrorClass="text medium error" />
			</li>
			<li>
				<label for="txt_vendorNumber">
					* <fmt:message key='pattern.vendor.number'/>:
				</label>
				<form:input path="vendorNumber" id="txt_vendorNumber" cssClass="text required" cssErrorClass="text medium error" maxlength="10"/>
			</li>
			<li>
				<label for="sel_vendorStyleType">
					* <fmt:message key='pattern.type'/>:
				</label>
				<form:select path="vendorStyleTypeCode" id="sel_vendorStyleType" items="${patternTypes}" itemLabel="name" itemValue="code"
					cssClass="required" cssErrorClass="text medium error">
				</form:select>
			</li>
		</ul>
			<br />
			<div class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
			</fieldset>
			<input type="hidden" name="contentId" value="${patternContentForm.contentId}" />
			<input type="hidden" name="statusCd" value="${patternContentForm.statusCd}" />
			<input type="hidden" name="action" value="addHelpContent" />
		</form:form>
	</div>
</div>
</body>

<content tag="inlineStyle">
form li label{ float:left; font-weight:bold; width:200px;padding-right:10px; padding-top:3px; text-align:right; }
form li{clear:both; padding:5px 0; }
textarea{ width:300px; height:75px; }
input.text{ width:300px; }
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.editpattern.js"/>"></script>
]]>
</content>