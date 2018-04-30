<%@ include file="/common/taglibs.jsp"%>
<head>
    <title>Edit Attributes | Belk CARS</title>
</head>


<spring:bind path="attributeForm.*">
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

<body class="admin">
<div id="attr_container" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Add/Edit Attribute
	</div>
	<div class="x-panel-body">
<c:url value="/admin/attributes/attributeForm.html" var="formAction"/>	
	<form:form commandName="attributeForm" method="post"
		action="${formAction}" onsubmit="return onFormSubmit(this)"
		id="attributeForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="attributeProfile.name"/>
						<span class="req">*</span>
					</label>
					<form:input path="name" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" />
					<span><fmt:message key="attributeProfile.name.unique" /></span>
					<form:errors path="name" cssClass="fieldError" />	
				</li>
				<li>
					<label for="txt_bm_attr_name">
					<fmt:message key="attributeProfile.blue.martini.name"/>
					<span class="req">*</span>
					</label>
					<form:input path="blueMartiniAttribute" id="txt_bm_attr_name"
						cssClass="text" cssErrorClass="text medium error" maxlength="50" />
					<form:errors path="blueMartiniAttribute" cssClass="fieldError" />
				</li>

				<li>
					<label for="txt_description">
					<fmt:message key="attributeProfile.description"/>
					<span class="req">*</span>						
					</label>
					<form:textarea cols="15" rows="2" path="description"
						id="txt_description" cssErrorClass="text medium error" />
					<form:errors path="description" cssClass="fieldError" />						
				</li>
				<li>
					<label for="txt_attr_label">
					<fmt:message key="attributeProfile.attr.label"/>
					<span class="req">*</span>						
					</label>
					<form:input maxlength="200" path="displayName" id="txt_attr_label" cssClass="text"
						cssErrorClass="text medium error" />
				</li>
				<li>
					<label for="sel_type">
					<fmt:message key="attributeProfile.attr.type.group"/>						
					</label>

					<form:select path="attrTypeCd" id="sel_data_type"
						items="${attributeGroup}" itemLabel="name" itemValue="attrTypeCd"
						cssClass="text" cssErrorClass="text medium error">
					</form:select>
				</li>
				<li>
					<label for="sel_attr_type">
					<fmt:message key="attributeProfile.attr.displaytype"/>						
					</label>
					<form:select path="htmlDisplayTypeCd" id="sel_attr_type"
						cssClass="text" cssErrorClass="text medium error">
						<c:forEach var="dType" items="${displayType}">
							<form:option value="${dType.htmlDisplayTypeCd}"
								label="${dType.name}" />
						</c:forEach>
					</form:select>
				</li>
				<li class="attr_values">
					<fieldset class="attr_values">
						<legend>
						<fmt:message key="attributeProfile.attr.values"/>							
						</legend>
						<ol>
						<input type="hidden" name="addValueList" id="addValueList" />
						<input type="hidden" name="removeValueList" id="removeValueList" />
						<input type="hidden" name="editValueList" id="editValueList" />
							<li>
								<div style="width:85px;float:right;">
									<a href="#" class="btn" id="btn_add">Add</a>
								</div>
								<input type="text" class="text" id="txt_attr_value"
									name="txt_attr_value" />
							</li>
							<li>
								<div style="width:85px;float:right;">
									<a href="#" class="btn" id="btn_remove">Remove</a>
								</div>
								<select name="tempAttrValues"  id="sel_attr_values" size="7"></select>
								<div style="width:85px;float:right;">
									<a href="#" class="btn" id="btn_edit">Edit</a>
								</div>
								<form:hidden  path="attributeValues" />
							</li>
						</ol>
					</fieldset>
				</li>
				<li>
					<label for="sel_data_type">
					<fmt:message key="attributeProfile.attr.datatype"/>						
					</label>
					<form:select path="dataTypeCd" id="sel_data_type"
						items="${datatTypes}" itemLabel="name" itemValue="attrDatatypeCd"
						cssClass="text large" cssErrorClass="text medium error" />
				</li>
				<li>
					<label for="sel_attr_type">
					<fmt:message key="attributeProfile.attr.validation.expression"/>
					</label>
					<form:select path="validationExpression" id="sel_attr_type"
						cssClass="text" cssErrorClass="text medium error">
						<c:forEach var="vType" items="${validationRules}">
							<form:option value="${vType.validationExpressionCd}"
								label="${vType.name}" />
						</c:forEach>
					</form:select>
				</li>
				<li>				
					<label for="isSearchable"><fmt:message key="attributeProfile.attr.is.searchable"/></label>
					<form:checkbox path="isSearchable" id="isSearchable"/>
				</li>
				<li>			
					<label for="isDisplayable"><fmt:message key="attributeProfile.attr.is.displayable"/></label>
					<form:checkbox path="isDisplayable" id="isDisplayable"/>
				</li>
				
				<%-- -- Start of code added for Outfit Management --  --%>
				<li>			
					<label for="isOutfit"><fmt:message key="attributeProfile.attr.is.outfit"/></label>
					<form:checkbox path="isOutfit" id="isOutfit"/>
				</li>
			    <%-- -- End of code added for Outfit Management --  --%>
			    
			    <%-- -- Start of code added for Deal Based Management --  --%>
				<li>			
					<label for="isPYG"><fmt:message key="attributeProfile.attr.is.pyg"/></label>
					<form:checkbox path="isPYG" id="isPYG"/>
				</li>
			    <%-- -- End of code added for Deal Based Management --  --%>
			    
			    <%-- -- Start of code added for Required Field --  --%>
				<li>			
					<label for="isRequired"><fmt:message key="attributeProfile.attr.is.required"/></label>
					<form:checkbox path="isRequired" id="isRequired"/>
				</li>
			    <%-- -- End of code added for Required Filed --  --%>
				
			</ul>
			<div class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>
		<form:hidden path="delimiter" id="delimiter"/>
	    <input type="hidden" name="id" value="${attributeForm.attribute.attributeId}"/>
	    <c:choose>
		    <c:when test="${not empty param.action}">
		    	<input type="hidden"  name="action" value="<c:out value='${param.action}'/>" />
		    </c:when>
		    <c:otherwise>
		    	<input type="hidden"  name="action" value="none" />
		    </c:otherwise>
	    </c:choose>
	</form:form>
</div></div>


<!--- moved from detail -->

<div id="attr_class_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Classifications
	</div>
	<div class="x-panel-body">
	<c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="disablebtnleft" onclick="return false" disabled href=""><fmt:message key="Associate With Classification"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="AssociateAttributeToClass" title="Associate With Classification" localized="true"  hideUnaccessibleLinks="true" arguments="${attributeForm.attribute.attributeId},associateWithClass"/>
			</c:otherwise>
    	</c:choose>



<br/><br/>
<c:choose>
<c:when test="${not empty attribute.classAttributes}"> 
<fmt:message key="attribute.details.class.attributes"/> &nbsp;   	
<display:table name="attribute.classAttributes" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="classAttr" pagesize="25" class="table">
    <display:column property="classification.belkClassNumber" style="width: 15%"/>
    <display:column property="classification.name" style="width: 25%"/>
    <display:column property="classification.descr"/>
 	 <display:column> 
	 
		<c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="disablebtnleft" onclick="return false" disabled href=""><fmt:message key="Remove"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="RemoveClassAttribute" elementName="RemoveClassAttribute" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${classAttr.classAttrId},${attributeForm.attribute.attributeId},remove"/>	
			</c:otherwise>
    	</c:choose>

		    	
	</display:column>
    <display:setProperty name="paging.banner.item_name" value="classification"/>
    <display:setProperty name="paging.banner.items_name" value="classifications"/>
</display:table>
</c:when>
<c:otherwise>
<fmt:message key="attribute.details.no.attributes.found"/>
</c:otherwise>
</c:choose>
</div></div>
<div id="attr_dept_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Departments
	</div>
	<div class="x-panel-body">
		<c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="disablebtnleft" onclick="return false" disabled href=""><fmt:message key="Add To Department"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="AssociateAttributeToDepartment" localized="true" hideUnaccessibleLinks="true" arguments="${attributeForm.attribute.attributeId},associateWithDepartment"  />
			</c:otherwise>
    	</c:choose>

<br/><br/>
<c:choose>
<c:when test="${not empty attribute.departments}"> 
<fmt:message key="attribute.details.department.attributes"/>&nbsp;   	
<display:table name="attribute.departments" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="attrdept" pagesize="25" class="table">
    <display:column property="department.deptCd"/>
    <display:column property="department.name"/>
    <display:column property="department.description"/>
 	 <display:column>
		<c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="disablebtnleft" onclick="return false" disabled href=""><fmt:message key="Remove"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="RemoveDepartmentAttribute" elementName="RemoveDepartmentAttribute" title="Remove" localized="true" hideUnaccessibleLinks="true" arguments="${attrdept.deptAttrId},${attributeForm.attribute.attributeId},remove"  />	 
			</c:otherwise>
    	</c:choose>
		   	
	</display:column>
    <display:setProperty name="paging.banner.item_name" value="department"/>
    <display:setProperty name="paging.banner.items_name" value="departments"/>
</display:table>
</c:when>
<c:otherwise>
<fmt:message key="attribute.details.no.departments.found"/>
</c:otherwise>
</c:choose>
</div></div>
<div id="attr_prod_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Types
	</div>
	<div class="x-panel-body">
	<c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="disablebtnleft" onclick="return false" disabled href=""><fmt:message key="Associate With Product Type"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="AssociateAttributeToProductType" title="Associate with Product Type" localized="true"  hideUnaccessibleLinks="true" arguments="${attributeForm.attribute.attributeId},associateWithProductType"  /> 
			</c:otherwise>
    	</c:choose>



<br/><br/>
<c:choose>
<c:when test="${not empty attribute.productTypeAttributes}"> 
<fmt:message key="attribute.details.product.type.attributes"/> &nbsp;   	
<display:table name="attribute.productTypeAttributes" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="productTypeAttr" pagesize="25" class="table">
    <display:column property="productType.productTypeId"/>
    <display:column property="productType.name"/>
    <display:column property="productType.description"/>
 	 <display:column>    	
	 
	 <c:choose>
			<c:when test="${(isCopyTextAllowed == 'true' && isBuyer == 'true') ||(empty attributeForm.attribute.attributeId)}">
				<a class="btn" disabled href=""><fmt:message key="Remove"/></a>
			</c:when>
			<c:otherwise>
				<secureurl:secureAnchor cssStyle="btn" name="RemoveProductTypeAttribute" elementName="RemoveProductTypeAttribute" title="Remove" localized="true"  hideUnaccessibleLinks="true" arguments="${productTypeAttr.productTypeAttrId},${attributeForm.attribute.attributeId},remove"  />	
			</c:otherwise>
    	</c:choose>

		    	
	</display:column>
    <display:setProperty name="paging.banner.item_name" value="product type"/>
    <display:setProperty name="paging.banner.items_name" value="product types"/>
</display:table>
</c:when>
<c:otherwise>
<fmt:message key="attribute.details.no.product.type.found"/>No Product Type Attributes Found
</c:otherwise>
</c:choose>
</div></div>



</body>

<content tag="inlineStyle">
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a[name="RemoveProductTypeAttribute"]').click(function(){return confirm('<fmt:message key="producttype.confirm.delete"/>');});
	$('a[name="RemoveClassAttribute"]').click(function(){return confirm('<fmt:message key="classification.confirm.delete"/>');});
	$('a[name="RemoveDepartmentAttribute"]').click(function(){return confirm('<fmt:message key="department.confirm.delete"/>');});
});
</script>
<script type="text/javascript" src="<c:url value="/js/belk.cars.editattribute.js"/>"></script>
]]>
</content>