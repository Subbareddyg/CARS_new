<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="size.heading.add" />
	</title>
</head>
<%
// if car id in request is null,then set the null as as string
// for url matching (RemoveSuperColor in url-config.xml) 
// this is required while creating new super color 

 String scmId=request.getParameter("scmId");
 //System.out.println("scmId-========"+scmId);
 if(scmId==null){
	 scmId="null";
}
String searchParam=request.getParameter("searchParam");
if(searchParam == null)
	searchParam =(String) session.getAttribute("sessionSearchParams");
//System.out.println("searchparam="+searchParam);
%>
<body>
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<c:choose>
			<c:when test="${sizeConversionForm.isEditForm eq true}" ><fmt:message key="size.form.edit"/></c:when>
			<c:otherwise><fmt:message key="size.form.add"/></c:otherwise>
		</c:choose>
	</div>
	<div class="x-panel-body">
		<c:url value="/admin/size/edit.html" var="formAction" />
		<form:form commandName="sizeConversionForm" method="post" action="${formAction}" id="sizeConversionForm" name="sizeConversionForm" >
		
			<div class="userButtons">
<%
						if(searchParam != null){
							pageContext.setAttribute("sbf", "Search"+searchParam);	
%>
							<secureurl:secureAnchor cssStyle="btn" name="SizeConversionListSearch" title="Back" localized="true" hideUnaccessibleLinks="true" arguments="${sbf}"/>
<%	
						}else{
%>
							<secureurl:secureAnchor cssStyle="btn" name="SizeConversionList" title="Back" localized="true" hideUnaccessibleLinks="true" />
<%
						}
%>
			</div>
			
			<fieldset>
			<div id="errorDiv" style="float:right; padding-right:140px;"></div>
				<ul>
					<li>
						
						<table border="0" width="100%">
						<tr><td width="46%">
						<label for="txt_sizeName">
							<fmt:message key='size.name'/>* :
						</label> 
						
						<c:choose>
						<c:when test="${sizeConversionForm.isEditForm == 'true'}">
							<form:input path="sizeName" id="txt_sizeName" cssClass="text required" cssErrorClass="text medium error" disabled="true"/>
							</td><td width="54%">
							<form:hidden path="sizeName" />
						</c:when>
						<c:otherwise>
							<form:input path="sizeName" id="txt_sizeName" cssClass="text required" cssErrorClass="text medium error"/>
							</td><td width="54%">
							<form:errors path="sizeName" cssClass="error" />
						</c:otherwise>
						</c:choose>
						</td></tr></table>
					</li>
					<li>
						<label for="txt_sizeConversionName">
							<fmt:message key='size.conversion.name'/>* :
						</label>
						<form:input path="coversionSizeName" id="txt_sizeConversionName" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="coversionSizeName" cssClass="error" />
						
					</li>
					
					
					<c:choose>
	    			<c:when test="${empty param.scmId}">  
	    				<li>
						<table border="0" width="100%">
						<tr><td width="46%">
						<label for="txt_deptCode">
							<fmt:message key='size.department.code'/> :
						</label>
						<form:input path="deptCode" id="txt_deptCode" cssClass="text required" cssErrorClass="text medium error"/>
						</td><td width="54%">
						<form:errors path="deptCode" cssClass="error" />
						</td></tr></table>
						</li>
						<li>
							<table border="0" width="100%">
							<tr><td width="46%">
							<label for="txt_classNumber">
								<fmt:message key='size.class.number'/> :
							</label>
							<form:input path="classNumber" id="txt_classNumber" cssClass="text required" cssErrorClass="text medium error"/>
							</td><td width="54%">
							<form:errors path="classNumber" cssClass="error" />
							</td></tr></table>
						</li>
						<li>
							<table border="0" width="100%">
							<tr><td width="46%">
							<label for="txt_vendorNumber">
								 <fmt:message key='size.vendor.number'/> :
							</label>
							<form:input path="vendorNumber" id="txt_vendorNumber" cssClass="text required" cssErrorClass="text medium error"/>
							</td><td width="54%">
							<form:errors path="vendorNumber" cssClass="error" />
							</td></tr></table>
						</li>
	    			
					</c:when>
	    			<c:otherwise> 
	    				<li>
						<label for="txt_deptCode">
							<fmt:message key='size.department.code'/> :
						</label>
						<form:input path="deptCode" id="txt_deptCode" cssClass="text required" cssErrorClass="text medium error" maxlength="3" onkeypress="return onlyNumber(event)"/>
						<form:errors path="deptCode" cssClass="error" />
						</li>
						<li>
							<label for="txt_classNumber">
								<fmt:message key='size.class.number'/> :
							</label>
							<form:input path="classNumber" id="txt_classNumber" cssClass="text required" cssErrorClass="text medium error" maxlength="4" onkeypress="return onlyNumber(event)" />
							<form:errors path="classNumber" cssClass="error" />
						</li>
						<li>
							<label for="txt_vendorNumber">
								 <fmt:message key='size.vendor.number'/> :
							</label>
							<form:input path="vendorNumber" id="txt_vendorNumber" cssClass="text required" cssErrorClass="text medium error" maxlength="7" onkeypress="return onlyNumber(event)"/>
							<form:errors path="vendorNumber" cssClass="error" />
						</li>
					</c:otherwise>
					</c:choose>
					
					
					
					<li>
						<label for="txt_facetSize1">
							 <fmt:message key='size.facetsize.one'/> :
						</label>
						<form:input path="facetSize1" id="txt_facetSize1" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="facetSize1" cssClass="error" />
					</li>
					<li>
						<label for="txt_facetSize2">
							 <fmt:message key='size.facetsize.two'/> :
						</label>
						<form:input path="facetSize2" id="txt_facetSize2" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="facetSize2" cssClass="error" />
					</li>
					<li>
						<label for="txt_facetSize3">
							 <fmt:message key='size.facetsize.three'/> :
						</label>
						<form:input path="facetSize3" id="txt_facetSize3" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="facetSize3" cssClass="error" />
					</li>
					<li>
						<label for="txt_facetSubSize1">
							 <fmt:message key='size.facetsubsize.one'/> :
						</label>
						<form:input path="facetSubSize1" id="txt_facetSubSize1" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="facetSize1" cssClass="error" />
					</li>
					<li>
						<label for="txt_facetSubSize2">
							 <fmt:message key='size.facetsubsize.two'/> :
						</label>
						<form:input path="facetSubSize2" id="txt_facetSubSize2" cssClass="text required" cssErrorClass="text medium error" />
						<form:errors path="facetSize2" cssClass="error" />
					</li>
					<li>
						<label for="txt_sizeName">
							<fmt:message key='size.created.by'/> :
						</label> 
						
						<div style="padding-top:3px;"> 	<c:out value="${sizeConversionForm.createdBy}" /></div>
					</li>

					<li>
						<label for="txt_sizeName">
							<fmt:message key='size.created.date'/> :
						</label> 
							<div style="padding-top:3px;"><c:out value="${sizeConversionForm.createdDate}" /></div>
							
					</li>

					<li>
						<label for="txt_sizeName">
							<fmt:message key='size.updated.date'/> :
						</label> 
						
							<div style="padding-top:3px;"><c:out value="${sizeConversionForm.updatedDate}" /></div>
					</li>

				</ul>
				<br />
			</fieldset>
				
			<br/>
	
			<br/>
			<br/>
			<c:choose>
	    		<c:when test="${empty param.scmId}">  
	    			<input type="button" align="right" id="createrule" class="btn" value="<fmt:message key="button.create"/>" style="float:right"/>
	    		</c:when>
	    		<c:otherwise> 
	    			<input type="submit" align="right"  class="btn" value="<fmt:message key="button.save"/>" style="float:right"/>
	    			<input type="hidden" name="scmId" value="${param.scmId}"/>
	    			<input type="hidden" name="editForm" value="${true}"/>
	    		</c:otherwise>
	    	</c:choose>

	<%
			
			if(searchParam != null){//pass this for 'save', 'create'
	%>
				   		<input type="hidden" name="searchList" value="<%=searchParam%>"/>
	<%
			}
	%>
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
<script type="text/javascript" src="<c:url value='/js/belk.cars.sizecolor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/libs/jquery-plugins.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.size.js'/>"></script>
]]>
</content>



