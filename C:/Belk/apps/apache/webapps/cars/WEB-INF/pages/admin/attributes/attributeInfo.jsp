<%@ include file="/common/taglibs.jsp"%>
<ul class="details">
	<li>
		<label for="txt_attr_name">Attribute Name: </label> <c:out value="${attribute.name}"/>
	</li>
	<li>
		<label for="txt_bm_attr_name">Blue Martini:</label> <c:out value="${attribute.blueMartiniAttribute}"/>
	</li>
	<li>
	<label for="txt_description">Description:</label> <c:out value="${attribute.description}"/>
	</li>
	<li>
	<label for="txt_attr_label">Attribute Label:</label> <c:out value="${attribute.attributeConfig.displayName}"/>
	</li>
	<li>
		<label for="sel_type">Attribute Group:</label> <c:out value="${attribute.attributeType.name}"/>
	</li>
	<li>
		<label for="sel_attr_type">Display Type:</label> <c:out value="${attribute.attributeConfig.htmlDisplayType.name}"/>
	</li>
	<li>
		<label for="sel_data_type">Data Type:</label> <c:out value="${attribute.datatype.name}"/>
	</li>
	<li>
		<label for="sel_validation">Validation Rule:</label> <c:out value="${attribute.attributeConfig.validationExpression.validationExpressionCd}"/>
	</li>
	<li class="attr_values">
		<label>Attribute Values:</label>
		<div class="values">
			<c:forEach items="${attribute.values}" var="attr">
				<c:out value="${attr.value}" escapeXml="false"/>, 
			</c:forEach>
		</div>
	</li>
<%--
<li>
<label class="default" for="txt_attr_default">Default Value</label> <c:out value="${attribute.attributeConfig.name}"/>
</li>
--%>
</ul>