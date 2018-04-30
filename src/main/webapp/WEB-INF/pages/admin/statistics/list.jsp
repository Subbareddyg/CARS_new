<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="statistics.title"/></title>
    <meta name="heading" content="<fmt:message key='statistics.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Statistics
	</div>
	<div class="x-panel-body">
		<ol>
			<li>
				<fmt:message key="statistics.total.name" />
				<c:out value="${statistics.name}" />
			</li>
			<li>
				<fmt:message key="statistics.total.hit" />
				<c:out value="${statistics.totalCacheHits}" />
			</li>
			<li>
				<fmt:message key="statistics.total.miss" />
				<c:out value="${statistics.totalCacheMiss}" />
			</li>
		</ol>
		<display:table name="statistics.domainStats" cellspacing="0"
			cellpadding="0" requestURI="cacheStatistics.html" defaultsort="1"
			id="stats" pagesize="50" class="table">
			<display:column property="name" sortable="true"
				titleKey="statistics.name" style="width: 25%" />
			<display:column property="totalCacheHits" sortable="true"
				titleKey="statistics.hit" style="width: 15%" />
			<display:column property="totalCacheMiss" sortable="true"
				titleKey="statistics.miss" style="width: 15%" />
			<display:column property="inMemoryCount" sortable="true"
				titleKey="statistics.count" style="width: 15%" />
			<display:setProperty name="paging.banner.item_name"
				value="statistic" />
			<display:setProperty name="paging.banner.items_name"
				value="statistics" />
		</display:table>
	</div>
</div>
</body>