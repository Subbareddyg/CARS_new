<%@ include file="/common/taglibs.jsp"%>
<ul class="details">
<li>
<label for="txt_attr_name">Product Name: </label> <c:out value="${product.name}"/>
</li>
<li>
<label for="txt_description">Description:</label> <c:out value="${product.description}"/>
</li>
<li>
<label for="txt_description">Product Type Group:</label> <c:out value="${product.productGroup.name}"/>
</li>

<li>
<label for="sel_type">Active:</label> 
<c:choose>
	<c:when test="${product.statusCd == 'ACTIVE'}">
		<fmt:message key="product.yes"/>
	</c:when>
	<c:otherwise>
		<fmt:message key="product.no"/>
	</c:otherwise>
</c:choose>
</li>

</ul>




