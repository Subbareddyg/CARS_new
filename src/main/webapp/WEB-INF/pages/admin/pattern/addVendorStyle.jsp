<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pattern.addVendorStyle.title" />
	</title>
</head>

<body class="admin">
<%--
	<div id="pattern_edit_pnl">
		<div id="pattern_edit_content" class="pnl_content x-hidden">
			<spring:bind path="patternForm.*">
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
--%>
			<c:url value="/admin/pattern/addVendorStyle.html" var="formAction" />
			<form:form commandName="patternForm" method="post"
				action="${formAction}" onsubmit="return onFormSubmit(this)"
				id="patternForm">
				<fieldset>
			<ul>
				<li>
					<label for="txt_vendorNumber">
						* <fmt:message key='pattern.vendor.number'/>:
					</label>
					<form:input path="vendorNumber" id="txt_productTypeId"
						cssClass="text required" cssErrorClass="text medium error" />
					<form:hidden path="patternId"/>
				</li>
				<li>
					<label for="txt_patternName">
						* <fmt:message key='pattern.style.number'/>:
					</label>
					<form:input path="vendorStyleNumber" id="txt_vendor_style_num"
						cssClass="text required" cssErrorClass="text medium error" />
				</li>
				<li style="height:20px;">
					<label>&nbsp;</label><span id="add_vendor_style_msg"></span>
				</li>
			</ul>
				</fieldset>
				<input type="hidden" name="contentId"
					value="${patternContentForm.contentId}" />
				<input type="hidden" name="statusCd"
					value="${patternContentForm.statusCd}" />
				<input type="hidden" name="action" value="addHelpContent" />
			</form:form>
<%--
		</div>
	</div>
--%>
</body>
<%--
<content tag="inlineStyle">
form li label{ float:left; font-weight:bold; width:200px;
padding-right:10px; padding-top:3px; text-align:right; } form li{
clear:both; padding:5px 0; } textarea{ width:300px; height:75px; }
input.text{ width:300px; } div.buttons{ padding-left:350px; }
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('#btn_save').click(function(){
		$('#patternForm').submit();
		return false;
	});
	$('#btn_cancel').click(function(){
		$('input.cancel_btn').click();
		return false;
	});
	// panels
	new Ext.Panel({
        title:'<fmt:message key="pattern.addVendorStyle.h1"/>',
        collapsible:true,
		frame:true,
        applyTo:'pattern_edit_pnl',
		contentEl:'pattern_edit_content',
		height:'auto'
    });
});
</script>
]]>
</content>
--%>