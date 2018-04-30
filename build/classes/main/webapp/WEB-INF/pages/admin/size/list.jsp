<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="size.title"/></title>
    <meta name="heading" content="<fmt:message key='size.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<body class="admin">


<div id="search_for_producttype" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Size Conversion Search :
	</div>
	<div class="x-panel-body">
    <c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src='<c:url value="/images/iconWarning.gif"/>'
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>

	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>

<c:url value="/admin/size/sizeConversionSearch.html?method=Search" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			
			<li>
				<label><fmt:message key="size.name"/></label>:
				<input type="text" id="sizeName" name="sizeName" size="10" value="<c:out value="${sizeName}" />"/>
			</li>
			<li>
				<label><fmt:message key="size.conversion.name"/></label>:
				<input type="text" id="conversionName" name="conversionName"  size="10" value="<c:out value="${conversionName}" />"/>
			</li>
			<li>
				<label><fmt:message key="size.department.code"/></label>:
				<input type="text" id=deptCode name="deptCode" size="10" value="<c:out value="${deptCode}"/>" onkeypress="return onlyNumber(event)"/>
			</li>
			<li>
				<label><fmt:message key="size.class.number"/></label>:
				<input type="text" id="classNumber" name="classNumber" size="10" value="<c:out value="${classNumber}"/>" onkeypress="return onlyNumber(event)"/>
			</li>
			<li>
				<label><fmt:message key="size.vendor"/></label>:
				<input type="text" id=vendorCode name="vendorCode" size="10" value="<c:out value="${vendorNumber}"/>" onkeypress="return onlyNumber(event)"/>
			</li>
			<li>
				<label><fmt:message key="size.facetsize"/></label>:
				<input type="text" id="facetSize" name="facetSize" size="10" value="<c:out value="${facetSize}"/>"/>
			</li>
			<li>
				<label><fmt:message key="size.facetsubsize"/></label>:
				<input type="text" id="facetSubSize" name="facetSubSize" size="10" value="<c:out value="${facetSubSize}"/>"/>
			</li>
			<li style="float: right;">
				<div style="float: right;">
				<input class="btn" type="submit" id="searchSubmit" name="method" value="Search" />
				<c:url value="/admin/size/sizeconversions.html?method=getAllSizeConversions" var="formAction"/>
				<input type="button" name="method" id="viewAll" value="View All" class="btn" onclick="clearHidden();location.href='${formAction}'" />				
				</div>
			</li>
		</ol>
	</form>
</div></div>
<%
session.removeAttribute("sessionSearchParams");//remove previous search params
StringBuffer sbf = new StringBuffer();
if(request.getParameter("sizeName") != null && request.getParameter("sizeName") != "")
	sbf.append("&sizeName="+request.getParameter("sizeName"));
if(request.getParameter("conversionName") != null && request.getParameter("conversionName") !="")
	sbf.append("&conversionName="+request.getParameter("conversionName"));
if(request.getParameter("deptCode") != null && request.getParameter("deptCode") !="")
	sbf.append("&deptCode="+request.getParameter("deptCode"));
if(request.getParameter("classNumber") != null && request.getParameter("classNumber") !="")
	sbf.append("&classNumber="+request.getParameter("classNumber"));
if(request.getParameter("vendorCode") != null && request.getParameter("vendorCode") !="")
	sbf.append("&vendorCode="+request.getParameter("vendorCode"));
if(request.getParameter("facetSize") != null && request.getParameter("facetSize") !="")
	sbf.append("&facetSize="+request.getParameter("facetSize"));
if(request.getParameter("facetSubSize") != null && request.getParameter("facetSubSize") !="")
	sbf.append("&facetSubSize="+request.getParameter("facetSubSize"));
//System.out.println("search======"+sbf.toString());	
if(sbf != null && sbf.length() > 0){
	java.util.Map<String, String[]> parameters = request.getParameterMap();
	for(String parameter : parameters.keySet()) {
		if(parameter.toLowerCase().startsWith("d-")) {
			String[] values = parameters.get(parameter);
			
			for(int i=0; i <values.length; i++)
				sbf.append("&").append(parameter).append("=").append(values[i]);
		}
	}
	session.setAttribute("sessionSearchParams", sbf.toString());
}
%>
<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Size Conversions:
	</div>
	<div class="x-panel-body">
	<%@ include file="/common/messages.jsp" %>
<div class="userButtons">
		
		<c:url value="/admin/size/sizeConversionSearch.html?method=deletSizeConversions" var="formAction"/>
		<form method="post" action="${formAction}" id="deleteForm">
			<input type="hidden" name="tempRuleList" id="tempRuleList" />
			<input type="hidden" name="sizeRulesList" id="sizeRulesList" />
			<input type="hidden" name="searchList" id="searchList" />
			<input type="button" style="color:gray" value="Delete" id="delete" class="btn cancel_btn"  disabled="disabled" />
		</form>

			<a class="btn" id="create" disabled="" onclick= "" href="<c:url value="/admin/size/edit.html"/>"><fmt:message key="size.create"/></a>
			
		
		
</div>

<display:table name="sizeConversionList" cellspacing="0" cellpadding="0" requestURI="/admin/size/sizeConversionSearch.html"  
    defaultsort="2" defaultorder="descending" id="sizeconversion" pagesize="25"  class="table dstable" sort="list">

	
	<display:column sortable="false" class="chkbox" titleKey='<input type="checkbox" id="checkall" name="checkall" onclick="checkedAll(this.checked);" />' headerClass="chk" >
	<input type="checkbox" name="chk" class="chkbox" value="${sizeconversion.scmId}"/>
	</display:column>
	
   <display:column property="sizeName"  sortable="true" titleKey="size.name" style="width: 15%"/>  
   <display:column sortable="true" titleKey="size.map" style="width: 5%"> 
		 <c:choose>
			<c:when test="${!empty sizeconversion.facetSize1}">
						Y
			</c:when>
			<c:otherwise>
						N
			</c:otherwise>
		</c:choose>
	</display:column>
    <display:column property="coversionSizeName"  sortable="true" titleKey="size.conversion.name" style="width: 15%"/> 
	
	<c:set var='displayComma' value=''/>
	<display:column sortable="true" titleKey="size.facetsize" style="width: 15%" >
	<c:if test="${!empty fn:trim(sizeconversion.facetSize1) }">${sizeconversion.facetSize1}<c:set var='displayComma' value=','/></c:if><c:if test="${!empty fn:trim(sizeconversion.facetSize2) }"><c:out value="${displayComma}" />${sizeconversion.facetSize2}<c:set var='displayComma' value=','/></c:if><c:if test="${!empty fn:trim(sizeconversion.facetSize3) }"><c:out value="${displayComma}" />${sizeconversion.facetSize3}</c:if>
	</display:column>
	
	<c:set var='displayComma' value=''/>
	<display:column sortable="true" titleKey="size.facetsubsize" style="width: 15%" >
	

	<c:if test="${!empty fn:trim(sizeconversion.facetSubSize1)}">${sizeconversion.facetSubSize1}<c:set var='displayComma' value=','/></c:if><c:if test="${!empty fn:trim(sizeconversion.facetSubSize2)}"><c:out value="${displayComma}" />${sizeconversion.facetSubSize2}</c:if>
		
	</display:column>
    <display:column sortable="true" titleKey="size.department.code" style="width: 15%"> ${sizeconversion.department.deptCd}</display:column>  
    <display:column sortable="true" titleKey="size.class.number" style="width: 10%" >${sizeconversion.classification.belkClassNumber}</display:column>  
    <display:column sortable="true" titleKey="size.vendor" style="width: 10%" >${sizeconversion.vendor.vendorNumber} </display:column>
	
		
    <%--<display:column>
		    <secureurl:secureAnchor name="RemoveSizeConversion" cssStyle="remove" elementName="RemoveSizeConversion" title="Remove"  hideUnaccessibleLinks="true" arguments="${sizeconversion.scmId}"/>
	</display:column>--%>
    
     
  	
    <display:column style="width: 15%">

			
	    	<secureurl:secureAnchor name="EditSizeConversion"  cssStyle="edit" elementName="EditSizeConversion"  title="Edit" localized="true" hideUnaccessibleLinks="true" arguments="${sizeconversion.scmId}"/>			

	</display:column>
    
    <display:setProperty name="paging.banner.item_name" value="size conversion"/>
    <display:setProperty name="paging.banner.items_name" value="size conversions"/>
</display:table>
</div></div>

</body>


<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/googiespell/cookiesupport.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.sizecolor.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/belk.cars.size.js'/>"></script>
]]>
</content>