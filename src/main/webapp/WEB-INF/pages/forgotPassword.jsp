<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="forgotpassword.title" /></title>
</head>
<body>
<div id="forgot_password_pnl"><div id="forgot_password_content" class="pnl_content x-hidden">
	<spring:bind path="forgotPasswordForm">
		<c:if test="${not empty status.errorMessages}">
			<div class="error">
				<c:forEach var="error" items="${status.errorMessages}">
					<img src="<c:url value="/images/iconWarning.gif"/>" alt="<fmt:message key="icon.warning"/>" class="icon" />
					<c:out value="${error}" escapeXml="false" />
					<br />
				</c:forEach>
			</div>
		</c:if>
	</spring:bind>
	<c:url value="/forgotPassword.html" var="formAction"/>
	<form:form commandName="forgotPasswordForm" method="post" id="forgotPasswordForm" action="${formAction}">
		<fieldset>
			<legend></legend>
			<ol>
				<li class="text">
					<label for="emailAddress">
						<fmt:message key="label.emailAddress"/>:
					</label>
					<form:input path="emailAddress" id="emailAddress"/><form:errors path="emailAddress" id="emailAddress"/>
				</li>
				<li class="buttons">
					<input type="submit" class="btn" name="forgotPassword"
						value="<fmt:message key='button.forgotPassword'/>" />
				</li>
			</ol>
		</fieldset>
	</form:form>
</div></div>	
</body>

<content tag="inlineStyle">
label{
	float:left;
	width:150px;
	font-weight:bold;
	text-align:right;
	margin-right:10px;
}
li.buttons{
	padding-left:160px;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// panels
	new Ext.Panel({
        title:'Forgot Password',
        collapsible:true,
		frame:true,
        applyTo:'forgot_password_pnl',
		contentEl:'forgot_password_content',
		height:'auto'
    });
    $('#forgotPasswordForm').submit(function(){
		//var val=$('#emailAddress').val().toLowerCase();
		//$('#emailAddress').val(jQuery.trim(val));
		var emailId=$('#emailAddress').val();
				emailId = emailId.replace(/^\s*|\s*|\s*$/g,'');
			$('#emailAddress').val(emailId);
		//alert(emailId);
	});
});
</script>
]]>
</content>