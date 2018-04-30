<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userProfile.title"/></title>
    <meta name="heading" content="<fmt:message key='userProfile.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR User Details
	</div>
	<div class="x-panel-body">
		<c:import url="userInfo.jsp" />
	</div>
</div>
<div id="user_depts_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		User Departments
	</div>
	<div class="x-panel-body">
<c:url var="showDepartments" value="/admin/user/addDepartment.html">
	<c:param name="id" value="${user.id}"/>    		
</c:url>
<ul class="departments">
	<li>
		<a class="btn" href="<c:out value="${showDepartments}" escapeXml="false" />"><fmt:message key="users.add.to.department"/></a>
		<br style="clear:both;" />
	</li>
	<li>
	<c:choose>
		<c:when test="${not empty user.departments}"> 
		<display:table name="user.departments" cellspacing="0" cellpadding="0" requestURI="" 
		    defaultsort="1" id="usrdept" pagesize="25" class="table">
		    <display:column property="deptCd" titleKey="department.deptCd"/>
		    <display:column property="name" titleKey="department.name"/>
		    <display:column property="description" titleKey="department.descr"/>
		 	 <display:column>    		
				<secureurl:secureAnchor name="RemoveDepartment" cssStyle="removeDepartment" elementName="RemoveDepartment"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrdept.deptId},${user.id},remove"  />	    	
			</display:column>
		    <display:setProperty name="paging.banner.item_name" value="department"/>
		    <display:setProperty name="paging.banner.items_name" value="departments"/>
		</display:table>
		</c:when>
		<c:otherwise>
		No Departments found
		</c:otherwise>
	</c:choose>
	</li>
</ul>
</div></div>

<c:if test="${user.userType.name == 'Vendor'}">
<div id="user_vendors_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		User Vendors
	</div>
	<div class="x-panel-body">
<ul class="vendors">
	<li>
		<secureurl:secureAnchor cssStyle="btn" name="ShowVendors"  title="users.associate.with.vendor" localized="true"  hideUnaccessibleLinks="true" arguments="${user.id}"/><br/>		    			
		<br style="clear:both;" />
	</li>
	<li>
	<c:choose>
		<c:when test="${not empty user.vendors}">
		<display:table name="user.vendors" cellspacing="0" cellpadding="0" requestURI="" 
		    defaultsort="1" id="usrvnd" pagesize="25" class="table">
		    <display:column property="vendorNumber" title="Vendor Number"/>
		    <display:column property="name" title="Vendor Name"/>
		    <display:column property="descr" title="Description"/>
		    <display:column>    		
			 	<secureurl:secureAnchor name="RemoveVendor" cssStyle="removeVendor" elementName="RemoveVendor"  localized="true"  hideUnaccessibleLinks="true" arguments="${usrvnd.vendorId},${user.id},remove"/>		   	
		    </display:column>
		    <display:setProperty name="paging.banner.item_name" value="vendor"/>
		    <display:setProperty name="paging.banner.items_name" value="vendors"/>
		</display:table>	
		</c:when>
		<c:otherwise>
		No Vendors were found
		</c:otherwise>
	</c:choose>
	</li>
</ul>
</div></div>
</c:if>

<div id="user_notes_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		User Notes
	</div>
	<div class="x-panel-body">
<ul class="notes">
	<c:choose>
		<c:when test="${not empty user.carUserNotes}">
		<c:forEach items="${user.carUserNotes}" var="notes">
			<li>
				<ul class="note">
					<li><c:out value="${notes.noteText}" /></li>
					<li><strong>Date Created:</strong> <c:out value="${notes.createdDate}" /></li>
				</ul>
			</li>
		</c:forEach>	
		</c:when>
		<c:otherwise>
		<li>No notes were found.</li>
		</c:otherwise>
	</c:choose>
	<li class="button">
		<secureurl:secureAnchor cssStyle="btn" name="AddNote" localized="true" title="Add Note" hideUnaccessibleLinks="false" arguments="${user.id},add" elementName="btn_add_note"/>
	</li>
</ul>
</div></div>

<div id="add_user_note_win" class="x-hidden">
<div class="x-window-header">Add a User Note</div>
<div id="add_user_note_form" class="content">
<c:url value="/admin/user/addUserNotes.html" var="formAction"/>	
<form method="post" action="${formAction}" id="userNotesForm">
	<input type="hidden" name="id" value="${user.id}"/>
	<input type="hidden" name="method" value="addUserNote"/>				
	<textarea class="note_text" name="notes"></textarea><br/><br/>		
</form>
</div></div>

</body>

<content tag="inlineStyle">
#user_depts_pnl,#user_vendors_pnl,#user_notes_pnl{ margin-top:10px; }
textarea.note_text{
	height:100px;
	width:309px;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.removeVendor').click(function(){return confirm('<fmt:message key="user.vendor.confirm.delete"/>');});
	$('a.removeDepartment').click(function(){return confirm('<fmt:message key="user.department.confirm.delete"/>');});
    // add note window
	var note_win=null;
	$('#btn_add_note').click(function(){
		// show add user note window when add note button clicked
		$(this).blur();
		if (!note_win) {
			note_win = new Ext.Window({
				el:'add_user_note_win',
				layout:'fit',
				width:350,
				autoHeight:true,
				closeAction:'hide',
				modal:true,
				plain:true,
				items: new Ext.Panel({
                    contentEl:'add_user_note_form',
                    deferredRender:false,
                    border:false,
					autoHeight:true
                }),
				buttons: [{
		                text: 'Cancel',
		                handler: function(){
		                    note_win.hide();
		                }
	            	},{
		                text: 'Add User Note',
		                handler: function(){
							// add note using normal form submit
							$('#add_user_note_form form').submit();
		                }
					}
				]
			});
		}
        note_win.show(function(){
			$('#add_user_note_form textarea.note_text').focus();
		});
		return false;
	});
});
</script>
]]>
</content>
