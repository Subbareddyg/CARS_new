<%@ include file="/common/taglibs.jsp"%>
<c:if test="${not empty pageContext.request.remoteUser}">
<menu:useMenuDisplayer name="Velocity" config="cssHorizontalMenu.vm" permissions="rolesAdapter">
<ul id="primary-nav" class="menuList">
	<!--
    <c:if test="${empty pageContext.request.remoteUser}">
    	<li><a href="<c:url value="/login.jsp"/>" class="current"><fmt:message key="login.title"/></a></li>
	</c:if>
	-->
    <menu:displayMenu name="MainMenu"/>	
	<li>
		<a class="search" href="<c:url value="/search.html"/>"><fmt:message key="menu.search"/></a>
	</li>	
	<li class="menubar">
	<a href="#"><fmt:message key="menu.profile"/></a>		
		<ul>		
			<li><a class="profile" href="<c:url value="/ajaxUserForm.html"/>?from=profile"><fmt:message key="menu.profile"/></a></li>			
			<li><a class="change_password" href="<c:url value="/changePassword.html"/>"><fmt:message key="menu.profile.change.password"/></a> </li>
		</ul>
	</li> 
	 
	<li>
		<a class="contact" href="#"><fmt:message key="menu.contact.belk"/></a>
	</li>
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="Logout"/>
	<li class="last">		
		<app:helpLink section="PAGE" />
	</li>
</ul>
</menu:useMenuDisplayer>
<%-- placeholders for the panels (initially hidden) --%>
<div id="search_panel" class="mpanel">
	<div class="header"><a class="close" href="#"><fmt:message key="menu.close"/></a></div>
	<div class="content"></div>
</div>
<div id="profile_panel" class="mpanel">
	<div class="header"><a class="close" href="#"><fmt:message key="menu.close"/></a></div>
	<div class="content"></div>
</div>
<div id="password_panel" class="mpanel">
	<div class="header"><a class="close" href="#"><fmt:message key="menu.close"/></a></div>
	<div class="content"></div>
</div>
<div id="contact_panel" class="mpanel">
	<div class="header"><a class="close" href="#"><fmt:message key="menu.close"/></a></div>
	<div class="content"><%@ include file="/common/contact.jsp"%></div>
</div>
<div id="help_panel" class="mpanel">
	<div class="header"><a class="close" href="#">close</a></div>
	<div class="content"></div>
</div>
</c:if>