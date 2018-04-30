
<%@page import="com.belk.car.security.RolesPermissionsAdapter"%>

<%= ((com.belk.car.security.UrlRepository)getServletContext().getAttribute("com.belk.car.security.URL_REPOSITORY")).getUrlByRole() %>

<%
com.belk.car.security.RolesPermissionsAdapter  permissions = new RolesPermissionsAdapter(request) ;
boolean permit = permissions.isAllowed(((com.belk.car.security.UrlRepository)getServletContext().getAttribute("com.belk.car.security.URL_REPOSITORY")).getUrl("MainMenu"));
%>

<%=permit %>

