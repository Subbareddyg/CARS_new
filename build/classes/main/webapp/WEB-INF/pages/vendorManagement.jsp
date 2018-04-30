<%@ include file="/common/taglibs.jsp"%>
<li id="add_vendor_li" class="add_vendor">
<display:table length="10" name="vendorContacts" export="false" cellpadding="10" cellspacing="10" id="vendorContact"> 
    <display:column style="width:15%;" property="emailAddress" title="<b>Email Address</b>" sortable="false" />
    <display:column style="width:25%;" property="firstName" sortable="false" title="<b>First Name</b>" />
    <display:column style="width:25%;" property="lastName" sortable="false" title="<b>Last Name</b>"/>
    <display:column class="edit" style="width:10%;" paramId="vu" paramProperty="id" href="ajaxMainMenu.html?car=${detailCar.carId}&oper=edit" value="Edit" sortable="false" title=""/>
    <display:column class="remove" style="width:15%;" paramId="vu" paramProperty="id" href="ajaxMainMenu.html?car=${detailCar.carId}&oper=del" value="Remove" sortable="false" title=""/>
    <%-- onclick="javascript:return confirm('<fmt:message key="user.department.confirm.delete"/>');" --%>
	<display:caption>Vendor Contacts</display:caption>
    <display:footer></display:footer>
</display:table>
  <% java.util.Collection col=(java.util.Collection)request.getAttribute("vendorContacts");
     if (col.size()<2){
   %>
    <c:url var="userFormUrl" value="/vendorUserForm.html">
      <c:param name="car" value="${detailCar.carId}"></c:param>
      <c:param name="method" value="add"></c:param>
    </c:url>
	<div class="buttons">
    	<a id="btn_add_vendor_contact" class="btn" href="${userFormUrl}" name="vcLink">Add Vendor Contact</a>
    	<app:helpLink section="VendorContact" key="/editCarForm.html" title="&nbsp;" localized="false"/>
	</div>
  <%} %>
</li>
