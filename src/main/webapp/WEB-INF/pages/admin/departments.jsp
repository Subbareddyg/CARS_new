<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="departments.title"/></title>
    <meta name="heading" content="<fmt:message key='departments.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>


<display:table name="deptsList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="departments" pagesize="25" class="table" export="false">
    <display:column property="name" url="/departmentform.html?from=list" paramId="id" paramProperty="id" escapeXml="true" sortable="true" title="Name"/> 
      
</display:table>