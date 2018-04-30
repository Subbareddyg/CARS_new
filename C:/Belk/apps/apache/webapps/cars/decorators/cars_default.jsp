<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <%@ include file="/common/meta.jsp" %>
     <title><decorator:title/> | <fmt:message key="webapp.name"/></title>
	 <link rel="stylesheet" type="text/css" href="<c:url value='/extjs-resources/css/ext-all.css'/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value='/css/vendorImage.css'/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value='/css/popup.css'/>" />
	 <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/css/cars.css'><c:param name="v" value="${jsVersion}"/></c:url>" />
	 <link href="<c:url value='/googiespell/googiespell.css'/>" rel="stylesheet" type="text/css" />	    
	<decorator:head/>
<style type="text/css">
<decorator:getProperty property="page.inlineStyle" />
</style>
<style type="text/css" media="print">
@import url('<c:url value='/css/cars_print.css'/>');
<decorator:getProperty property="page.printStyle" />
</style>
<script type="text/javascript">
var sTime=new Date();
</script>
</head>
<body<decorator:getProperty property="body.id" writeEntireProperty="true"/><decorator:getProperty property="body.class" writeEntireProperty="true"/>>
    <div id="pg">
        <div id="hd">
            <jsp:include page="/common/header.jsp"/>
			<jsp:include page="/common/menu.jsp"/>
        </div>
        <div id="content">
            <div id="main">
                <!-- <%@ include file="/common/messages.jsp" %> -->
                <decorator:body/>
            </div>
        </div>
        <div id="ft">
            <jsp:include page="/common/footer.jsp"/>
        </div>
    </div>
<div id="help_win" class="x-hidden">
    <div class="x-window-header">Help Window</div>
	<div id="help_content" class="content"></div>
</div>
<script type="text/javascript">
	var context='<c:url value="/"/>';
</script>
<script type="text/javascript" src="<c:url value='/js/libs/jq-plugins-adapter.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/libs/ext-all.js'/>"></script>
<script type="text/javascript" src="<c:url value='/js/libs/jquery.scrollTo-min.js'/>"></script>
<!--<script type="text/javascript" src="<c:url value='/js/libs/jquery-1.3.min.js'/>"></script>-->
<script type="text/javascript" src="<c:url value='/js/belk.cars.common.js'><c:param name="v" value="${jsVersion}"/></c:url>"></script>
<decorator:getProperty property="page.jscript" />
</body>
</html>
