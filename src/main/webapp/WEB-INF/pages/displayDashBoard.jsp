<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <c:if test="${pageContext.request.remoteUser != null}">
    	<c:set var="headingUser">
      	 <authz:authentication operation="firstName"/>
       </c:set>
	</c:if>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>"/>
    <meta name="menu" content="MainMenu"/>
</head>

<p><fmt:message key="mainMenu.message"/>
</p>

<div class="separator"></div>




<display:table name="carsList" id="car" pagesize="25" class="table" export="false">
    <display:column sortable="true" title="Urgency" style="width: 10%">
    	<c:choose>
    		<c:when test="${car.isUrgent == 'N'}"><br> 
    			<fmt:message key="displaydashboard.pages.urgent"/>
    		</c:when>
    		<c:otherwise>
    		   <fmt:message key="displaydashboard.pages.normal"/>
    		</c:otherwise>
    	</c:choose>
    </display:column> 
    <display:column sortable="false" title="Id" style="width: 14%" >
      <a href="?review=${car.carId}">${car.carId}</a>
    </display:column> 
    <display:column sortable="false" title="Source" style="width: 5%">
        ${car.sourceType.sourceTypeCd}
    </display:column>  
    <display:column sortable="false" title="Department" style="width: 14%">
        ${car.department.deptCd}
    </display:column>  
    <display:column sortable="false" title="Vendor" style="width: 14%">
        ${car.vendorStyle.vendor.name}
    </display:column> 
    <display:column sortable="false" title="Vendor Style" style="width: 14%">
        ${car.vendorStyle.vendorStyleName}
    </display:column> 
    <display:column sortable="false" title="Due By" style="width: 14%">
        ${car.dueDate}
    </display:column>  
    <display:column sortable="false" title="Last Updated By" style="width: 14%">
        ${car.updatedBy}
    </display:column>  
    <display:column sortable="false" title="Last Updated" style="width: 14%">
        ${car.updatedDate}
    </display:column> 
    <display:column sortable="false" title="" style="width: 14%">
        <a href="?car=${car.carId}&param=edit"><fmt:message key="displaydashboard.pages.edit.cars"/></a>
    </display:column> 
    <display:setProperty name="paging.banner.item_name" value="CAR"/>
    <display:setProperty name="paging.banner.items_name" value="CARs"/>
</display:table>
<%Object ob=request.getSession().getAttribute("selectedCar");
com.belk.car.app.model.Car sCar=(com.belk.car.app.model.Car)ob;
%>
<% if (sCar!=null){ %>
<TABLE width="85%">
<TR><TD ><fmt:message key="displaydashboard.pages.cars.preview"/></TD></TR>
</TABLE>
 <TABLE width="100%">
 <TR>
  <TD><fmt:message key="displaydashboard.pages.department"/> <c:out value="${selectedCar.department.deptCd}" default="isNull"/></TD>
  <TD><fmt:message key="displaydashboard.pages.class"/> <c:out value="${selectedCar.vendorStyle.classification.name}" default="isNull"/></TD>
  <TD><fmt:message key="displaydashboard.pages.vendor.style"/> <c:out value="${selectedCar.vendorStyle.vendorStyleName}" default="isNull"/></TD>
  </TR>
  </TABLE>
<TABLE border="1" width="85%">
<caption><fmt:message key="displaydashboard.pages.style.attributes"/></caption>
<THEAD>
<TR >
<TD width="20%"><fmt:message key="displaydashboard.pages.attribute.name"/> </TD><TD width="80%"><fmt:message key="displaydashboard.pages.attribute.value"/></TD>
</TR>
</THEAD>
<tbody>
<c:forEach var="carAttr" items="${selectedCar.carAttributes}" >
<TR width="100%">
<TD width="20%">
<c:out value="${carAttr.attribute.name}"/>
</TD>
<TD width="80%">
<c:out value="${carAttr.attrValue}"/>
</TD>
</TR>
</c:forEach>
</tbody>
</TABLE>
<TABLE>
<caption><fmt:message key="displaydashboard.pages.sku.attributes"/></caption>
<THEAD>
<TR>
<TD><fmt:message key="displaydashboard.pages.sku.name"/></TD><TD><fmt:message key="displaydashboard.pages.sku.value"/>
</TD>
</TR>
</THEAD>
<tbody>
<c:forEach var="carSku" items="${selectedCar.vendorSkus}" >
 <c:forEach var="attr" items="${carSku.carSkuAttributes}" >
<TR>
<TD width="20%">
<c:out value="${attr.attribute.name}"/>
</TD>
<TD width="80%">
<c:out value="${attr.attrValue}"/>
</TD>
</TR>
</c:forEach>
</c:forEach>
</tbody>
<%} %>
