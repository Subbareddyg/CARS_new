<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>Import Cosmetics Data | Belk CARS</title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Import Excel Data File
	</div>
	<div class="x-panel-body">
		<spring:bind path="catalogFileUpload.*">
			<c:if test="${not empty status.errorMessages}">
				<div class="error">
					<c:forEach var="error" items="${status.errorMessages}">
						<img src="<c:url value="/images/iconWarning.gif"/>"
							alt="<fmt:message key="icon.warning"/>" class="icon" />
						<c:out value="${error}" escapeXml="false" />
						<br />
					</c:forEach>
				</div>
			</c:if>
		</spring:bind>
		
		<div id="attr_container">
		<c:url value="/admin/catalog/import.html" var="formAction" />
		<form:form commandName="catalogFileUpload" method="post"
			action="${formAction}" enctype="multipart/form-data"
			id="catalogFileUpload">
			<!-- onsubmit="return validateFileUpload(this)"  -->
			<fieldset>
				<ul>
					<li>
						<label for="txt_attr_name">
							Vendor Number
						</label>
						<form:input path="vendorNumber" id="txt_vendorNumber"
							cssClass="text" cssErrorClass="text medium error" />
					</li>
					<li>
						<label for="txt_template">
							Template
						</label>
						<form:select path="templateId" id="sel_template"
							items="${templates}" itemLabel="name" itemValue="templateId"
							cssClass="text" cssErrorClass="text medium error">
						</form:select>
					</li>

					<li>
						<label for="sel_attr_type">
							Select File:
						</label>
						<spring:bind path="catalogFileUpload.file">
							<input type="file" name="file" id="file" class="file medium" />
						</spring:bind>
					</li>
					<li>
						<label for="sel_attr_type">
							Create Only New Attribute:
						</label>
						<form:checkbox path="createOnly" id="createOnly" value="Y" />
					</li>

				</ul>
				<br/>
				<div class="buttons" style="padding-left: 160px">
						<input type="submit" class="btn cancel_btn" name="cancel"
							value="<fmt:message key="button.cancel"/>" />
						<input type="submit" class="btn save_btn" name="save"
							value="<fmt:message key="button.save"/>" />
					</div>
				</div>
			</fieldset>
		</form:form>
		</div>
	</div></div>
</body>

<content tag="jscript">
<![CDATA[
]]>
</content>