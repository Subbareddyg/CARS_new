<%@ include file="/common/taglibs.jsp"%>

<script>
function verifyCancel() {
	var stay=confirm("Are you sure you wish to cancel?");
	if (stay)
		window.location="../../admin/latecarsgroup/lateCarsGroup.html?method=getAllLateCarsGroup";
	else
		return false;
}
</script>
<head>
    <title><fmt:message key="lateCarsGroup.title"/></title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="lateCarsGroup.add.title"/>
	</div>
	<div class="x-panel-body">
<spring:bind path="lateCarsGroupform.*">
	<c:if test="${not empty errors}">
		<div class="error">
			<c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<div id="attr_container">
<ol>
		<li>
			<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		</li>
	</ol>
<c:url value="/admin/latecarsgroup/addLateCarsGroup.html?method=addLateCarsGroup" var="formAction"/>
	<form:form commandName="lateCarsGroupform" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="lateCarsGroupform">
		<fieldset>
			<ul>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="lateCarsGroup.display.name"/>
						<span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="name" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" maxlength="40" />					
				</li>
				
			</ul>
				<br/>								
			<div class="buttons">
				
				<input type="button" class="btn save_btn" name="method" value="<fmt:message key="button.cancel"/>"  onclick="verifyCancel();"/>
				<input type="submit" class="btn save_btn" name="method" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>	
	    <input type="hidden" name="lateCarsGroupId" value="${lateCarsGroupform.lateCarsGroupId}"/>
	    <input type="hidden" name="action" value="addLateCarsGroup"/>	   	    
	</form:form>
</div>
</div></div>
</body>
<content tag="inlineStyle">
div.buttons{
	padding-left:160px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
 $('#lateCarsGroupform').submit(function() {
 if($('#txt_attr_name').val() == ""){
      alert("Please enter Late CARS Group Name");
      return false;
   }
});
</script>
]]>
</content>