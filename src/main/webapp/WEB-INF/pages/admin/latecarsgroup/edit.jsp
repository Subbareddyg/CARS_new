<%@ include file="/common/taglibs.jsp"%>


<head>
    <title><fmt:message key="lateCarsGroup.edit.title"/></title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="lateCarsGroup.edit.title"/>
	</div>
	<div class="x-panel-body">

<spring:bind path="lateCarsParamsForm.*">
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



<c:url value="/admin/latecarsgroup/addLateCarsParams.html" var="formAction"/>
	<form:form commandName="lateCarsParamsForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="lateCarsParamsFormId">
	  		
		<fieldset>
			<ul>
			
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="lateCarsGroup.owner.name"/>:
					</label>
					<form:select id="currentUserType" path="currentUserType" cssStyle="width:180px;">
						<c:forEach items="${availableUserTypes}" var="userType">
						   <c:if test="${userType.userTypeCd !='WEB_MERCHANT'}">
							<form:option value ="${userType.userTypeCd}"><c:out value="${userType.name}"/></form:option>
							</c:if>
						</c:forEach>
				   </form:select>
								
				</li>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="lateCarsGroup.owner.status"/>:
					</label>
					<form:select id="workflowStatus" path="workflowStatus" cssStyle="width:180px;">
						<c:forEach items="${workflowStatuses}" var="wfs">
							<c:choose>
								<c:when
									test="${wfs.statusCd != 'IMAGE_FAILED_IN_MC' and wfs.statusCd != 'IMAGE_FAILED_IN_CC'}">
									<form:option value="${wfs.statusCd}">
										<c:out value="${wfs.name}" />
									</form:option>
								</c:when>
								<c:otherwise>
									<c:if test="${wfs.statusCd == 'IMAGE_FAILED_IN_MC'}">
										<form:option value="${wfs.statusCd}">
											<c:out value="Image Failed in Mechanical Check" />
										</form:option>
									</c:if>
									<c:if test="${wfs.statusCd == 'IMAGE_FAILED_IN_CC'}">
										<form:option value="${wfs.statusCd}">
											<c:out value="Image Failed in Creative Check" />
										</form:option>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				  </form:select>	
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="lateCarsGroup.owner.weeksdue"/>:
						<span class="req" style="color:#FF0000">*</span>
					</label>
							<form:input path="weeksDue" id="txt_attr_weeks_due" cssClass="text" cssErrorClass="text medium error" maxlength="3" cssStyle="width:177px;"/>&nbsp;&nbsp;Weeks
				</li>				
			</ul>
				<br/>								
			<div class="buttons">
				<input type="submit" class="btn" title="Save" name="method" value="<fmt:message key="button.save"/>" />
				<secureurl:secureAnchor cssStyle="btn" name="LateCarsGroupDetail" title="Cancel" localized="true" hideUnaccessibleLinks="true" arguments="${lateCarsParamsForm.lateCarsGroupId}"/>
			</div>
			<input type="hidden" name="lateCarsGroupId" value="${lateCarsParamsForm.lateCarsGroupId}"/>
			<input type="hidden" name="lateCarsParamId" value="${lateCarsParamsForm.lateCarsParamId}"/>
		</fieldset>	
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
 $('#lateCarsParamsFormId').submit(function() {
 if($('#txt_attr_weeks_due').val() == ""){
      alert("Please enter Weeks Due");
      return false;
   }
   if (isNaN( $("#txt_attr_weeks_due").val() )) {
      alert("Please enter numbers in Weeks Due");
      return false;
   } 
   var weeksDue=$("#txt_attr_weeks_due").val();
   var iChars = "!@#$%^&*()+=[]\\\';,./{}|\":<>?~_"; 
   for (var i = 0; i < weeksDue.length; i++) {
  	if (iChars.indexOf(weeksDue.charAt(i)) != -1) {
  	  alert ("Special characters are not allowed");
  	return false;
  	}
  }
});
</script>
]]>
</content>