<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="supercolor.detail" />
	</title>
	
</head>
<%
// if car id in request is null,then set the null as as string
// for url matching (RemoveSuperColor in url-config.xml) 
// this is required while creating new super color 

 String cmmId=request.getParameter("cmmId");
 if(cmmId==null){
	 cmmId="null";
}
%>
<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
			<c:choose>
			<c:when test="${superColorForm.isEditForm eq true}"><fmt:message key="supercolor.form.edit"/></c:when>
			<c:otherwise><fmt:message key="supercolor.form.add"/></c:otherwise>
			</c:choose>
	</div>
	<div class="x-panel-body">
		<c:url value="/admin/supercolor/edit.html" var="formAction" />
		<form:form commandName="superColorForm" method="post" action="${formAction}" id="superColorForm">
		
			<div class="userButtons">
					 <secureurl:secureAnchor cssStyle="btn" name="SuperColorList" title="Back" localized="true" hideUnaccessibleLinks="true" />
			</div>
			
			<fieldset>
				<ul>
					
					<li>
						<label for="txt_superColorCode">
							<fmt:message key='supercolor.code'/>* :
						</label>
						<form:input path="superColorCode" id="txt_superColorCode" cssClass="text required" maxlength="3" cssErrorClass="text medium error" onkeypress="return onlyNumber(event)"/>
						<form:errors path="superColorCode" cssClass="error" />
					</li>
					<li>
						<label for="txt_superColorName">
							<fmt:message key='supercolor.name'/>* :
						</label>
						<form:input path="superColorName" id="txt_superColorName" cssClass="text required" cssErrorClass="text medium error" onkeypress="return onlyAlphabets(event)"/>
				    	<form:errors path="superColorName" cssClass="error" />
					</li>
					<li>
						<label for="txt_colorCodeBegin">
							 <fmt:message key='supercolor.colorcode.begin'/>* :
						</label>
						<form:input path="colorCodeBegin" id="txt_colorCodeBegin" maxlength="3" cssClass="text required" cssErrorClass="text medium error" onkeypress="return onlyNumber(event)"/>	
						<form:errors path="colorCodeBegin" cssClass="error"/>
					</li>
					<li>
						<label for="txt_colorCodeEnd">
							<fmt:message key='supercolor.colorcode.end'/>* :
						</label>
						<form:input path="colorCodeEnd" id="txt_colorCodeEnd" maxlength="3" cssClass="text required" cssErrorClass="text medium error" onkeypress="return onlyNumber(event)"/>
						<form:errors path="colorCodeEnd" cssClass="error" />
					</li>
					
				</ul>
				<br />
			</fieldset>
				
			<br/>
	
			<br/>
			<br/>
			<c:choose>
	    		<c:when test="${empty param.cmmId}">  
	    			<input type="submit" align="right" class="btn" value="<fmt:message key="button.create"/>" style="float:right"/>
	    		</c:when>
	    		<c:otherwise> 
	    			<input type="submit" align="right" class="btn" value="<fmt:message key="button.save"/>" style="float:right"/>
	    			<input type="hidden" name="cmmId" value="${param.cmmId}"/>
	    			<input type="hidden" name="editForm" value="true"/>
	    		</c:otherwise>
	    	</c:choose>
	    	
		</form:form>
	</div>
</div>
</body>

<content tag="inlineStyle">
form li label{ float:left; font-weight:bold; width:200px;padding-right:10px; padding-top:3px; text-align:right; }
form li{clear:both; padding:5px 0; }
input.text{ width:200px; }
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/js/libs/jquery-1.3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value="/js/belk.cars.sizecolor.js"/>"></script>
<script type="text/javascript" src="<c:url value='/js/libs/jquery-plugins.js'/>"></script>
]]>
</content>



