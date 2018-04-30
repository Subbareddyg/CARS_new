<%@ include file="/common/taglibs.jsp"%>
<br>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<c:choose>
<c:when test="${user.userType.userTypeCd == 'VENDOR'}">
<c:set var="imageRequirement" value=""/>
</c:when>
<c:otherwise>
<c:set var="imageRequirement" value="collapsed"/>
</c:otherwise>
</c:choose>
<div id="img_mgmt_req_pnl" class="cars_panel x-hidden ${imageRequirement}">
<div class="x-panel-header">
Image Requirements
</div>
<div class="x-panel-body">
<ul>
<b><fmt:message key="imagerequirements.photography"/>:</b>
<ul><fmt:message key="imagerequirements.product"/>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirement.product.requirement01"/></li>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirement.product.requirement02"/> </li></ul><br/>
<ul><fmt:message key="imagerequirements.onfigure"/>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirement.onfigure.requirement01"/> </li>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirements.onfigure.requirement02"/></li>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirements.onfigure.requirement03"/></li> </ul><br/>
<ul> <fmt:message key="imagerequirements.color"/>:<fmt:message key="imagerequirements.color.requirement"/></ul><br/>
<ul> <b><fmt:message key="imagerequirements.postproduction"/>:</b>
<li><img src="<c:url value='/images/bullet.png'/>" width="10" height="10"/><fmt:message key="imagerequirements.postproduction.requirement"/></li></ul><br/>
<ul> <b><fmt:message key="imagerequirements.size"/>:</b><br/><fmt:message key="imagerequirements.size.requirement"/></ul><br/>
<ul> <b><fmt:message key="imagerequirements.format"/>:</b><br/><fmt:message key="imagerequirements.format.requirement01"/><br/><fmt:message key="imagerequirements.format.requirement02"/></ul><br/>
<ul> <b><fmt:message key="imagerequirements.delivery"/>:</b><br/>
<li><fmt:message key="imagerequirements.delivery.requirement01"/></li><br/><br/>
<li><fmt:message key="imagerequirements.delivery.requirement02.line1"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement02.line2"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement02.line3"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement02.line4"/></li>
<br/>
<li><fmt:message key="imagerequirements.delivery.requirement03.line1"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement03.line2"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement03.line3"/></li>
<br/>
<li><fmt:message key="imagerequirements.delivery.requirement04.line1"/></li>
<li><fmt:message key="imagerequirements.delivery.requirement04.line2"/></li>
</ul>
</ul>
</div></div>
</c:if>