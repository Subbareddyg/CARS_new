<%@ include file="/common/taglibs.jsp"%>

<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="headerTabsForVendor.jsp"%>
	
	<%@ include file="breadcrumbForVendorContacts.jsp"%>
	
</c:if>	
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
	<%@ include file="headerTab.jsp"%>
	<br>
	<%@ include file="breadcrumbForFulfillmentServiceContacts.jsp"%>
	<br>
</c:if>




<head>
    <title>
    	<c:choose>
	    <c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	      	 Vendor Contacts
		</c:when>
		<c:otherwise>
	      	 Fulfillment Service Contacts
		</c:otherwise>
	</c:choose>
    </title>
    
    <meta name="heading" content=""/>
    <meta name="menu" content="MainMenu"/>
  <style type=text/css>
   .lock{
   FONT-WEIGHT: 700;
   COLOR: #0000cc;
   }
  </style>       
</head>
<body class="admin">

 <c:choose>
	<c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
		<B><fmt:message key="contacts.vendorFulfillmentServiceContactsHeader"/></b>
	</c:when>
	<c:otherwise>
		<b><fmt:message key="contacts.fulfillmentServiceContactsHeader"/></b>
	</c:otherwise>
</c:choose>
<br> <br>
	<div class="cars_panel x-hidden" style="margin: 10px 0px 0px;">
		<%@ include file="omaInfoSection.jsp" %>
	</div>

		
	  			<div id="search_for_user" class="cars_panel x-hidden" style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		<span class="x-panel-header-text"><fmt:message key="contacts.search"/></span>
	</div>
	<div class="x-panel-body">
	  			<c:url value="/oma/searchFSContact.html?method=search" var="formAction"/>
					<form method="post" action="${formAction}" id="searchForm">
						<ol>
								
								<li>
									<label><b><fmt:message key="contacts.fname"/></b></label>
									<input type="text" value="<c:out value="${contactFirstName}" />" name="contactFirstName" id="contactFirstName" size="25" maxlength="50"/>
								</li>
								<li>
									<label><b><fmt:message key="contacts.lname"/></b></label>
									<input type="text" value="<c:out value="${contactLastName}" />" name="contactLastName" id="contactLastName" size="25" maxlength="50"/>
								</li>
								<li>
									<label><b><fmt:message key="contacts.status"/></b></label>
									<select  id="status" class="required" name="status" >
											 <option value="ALL" >All</option>
					                         <option value="ACTIVE">Active</option>
					                         <option  value="INACTIVE">Inactive</option>                              
								    </select>							
								</li>  
								<li>
									<input class="btn" type="submit" title="Search" name="searchFSContact" value="<fmt:message key="Search"/>" />
									<c:url value="/oma/searchFSContact.html?method=viewAll" var="formAction"/>
									<a style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><fmt:message key="contacts.viewall"/></a>				
								</li>	
						</ol>
					</form> 
			</div>
		</div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Contacts
	</div>  
	<div class="x-panel-body"> 
		<div class="userButtons">
				<c:if test="${sessionScope.mode=='viewOnly'}">
					<div class="buttons" style="float:right;">
						<input type="button" onclick="none" disabled="disabled" value="Add Contact" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="add_btn" class="add_note btn">
					<span id="add_note_msg"></span>
					</div>
				</c:if>
				<c:if test="${sessionScope.mode!='viewOnly'}">
				<div class="buttons" style="float:right;">	
					  <a class="btn" id="add_btn" style="color:#15428b" href="<c:url value="/oma/fulfillmentServiceContacts.html?method=add"/>" title="Add Contact">  
						<fmt:message key="contacts.button.add"/> 
					  </a>
				</div>
				</c:if>
		</div>
		<div> 
		<display:table name="contacts" cellspacing="0" cellpadding="0" requestURI="/oma/searchFSContact.html"  
		    defaultsort="1" id="contact" pagesize="25" class="dstable" sort="list">
		    
		    	<c:url value="/oma/searchFSContact.html?method=edit&id=${contact.contactId}&param=viewOnly" var="url"/>
		   
		    
		    <display:column  comparator="com.belk.car.util.LongComparator" sortProperty="contactId" sortable="true" titleKey="ID"	style="width: 7%; text-align: center"  >
				<a class="alink" id="${contact.contactId}" style="margin-top:0;" href='<c:out value="${url}" escapeXml="false"/>'  id="a_view_all"><c:out value="${contact.contactId}"/></a>
			</display:column>	
			<display:column   sortProperty="contactName" sortable="true" titleKey="Contact Name"	style="width: 15%"  >
				<a class="alink" id="${contact.contactId}" style="margin-top:0;" href='<c:out value="${url}" escapeXml="false"/>' id="a_view_all"><c:out value="${contact.contactName}"/></a>
			</display:column>			
		    <display:column property="contactType.contactTypeName" sortable="true" titleKey="Type" style="width: 15%"/> <!-- Type -->   
		    <display:column property="status" sortable="true" titleKey="Status" style="width: 10%; text-align: center"/>
		    <display:column property="updatedDate" format="{0,date,MM/dd/yyyy}" sortable="true" titleKey="Last Updated" style="width: 10%; text-align: center"/>
		    <display:column property="updatedBy" sortable="true" titleKey="Last Updated By" style="width: 15%"/>
		    <display:column title="Edit" class="lock" style=" WIDTH: 20%;"  >
		    		<c:if test="${sessionScope.mode!='viewOnly'}">
		    			 <c:choose>
		     			<c:when test="${contact.lockedBy != null}">   
		     			<input class="edit_car btn" type="button" onclick="none" disabled="disabled" value="Edit" style="font-size: 12px; 
		     					color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" title="Edit">
		     			</c:when>
		    		  <c:otherwise>
    		 			 <a class="btn"  id="edit_btn" style="color:#15428b"  name="edit_btn"  href="<c:url value="/oma/edit.html?d-1332818-p=${pagination}&method=edit&id=${contact.contactId}"/>" title="Edit">  
						  	Edit
					  	</a>
		    		  	</c:otherwise>
		    		</c:choose>
				</c:if>	
		  	</display:column>
		    <display:setProperty name="paging.banner.item_name" value="contact"/>
		    <display:setProperty name="paging.banner.items_name" value="contacts"/>
		</display:table>
	</div>
	<br>	
</div>
</div>
<script language="javascript">
		
		//dropship phase 2: added code fix for firefox browser onload
		var browserName=navigator.appName; // Get the Browser Name
		if(browserName=="Microsoft Internet Explorer") // For IE
		{
			window.onload=init; // Call init function in IE
		}
		else
		{
			if (document.addEventListener) // For Firefox
			{
				document.addEventListener("DOMContentLoaded", init, false); // Call init function in Firefox
			}
		}

		function init(){			
				 document.getElementById("tab4").className="activeTab"
				// disableFieldsAsPerRole();	
				 setStatusDropDwnValue();
				 var pos=<c:out value="${requestScope.scrollPos}" />
					
				 if(pos == 0){
					 setTimeout(function() { document.getElementById('contactFirstName').focus(); }, 10);
				 }
			}
			function unlockContact(pagination,contactId){
				document.forms[0].action ="unlock.html?method=unlock&id="+contactId+"&scrollPos="+document.documentElement.scrollTop+"&d-1332818-p="+pagination;
			    document.forms[0].submit();
			}
			function lockContact(pagination,contactId){
				document.forms[0].action ="lock.html?method=lock&id="+contactId+"&scrollPos="+document.documentElement.scrollTop+"&d-1332818-p="+pagination;
			    document.forms[0].submit();
			}	
			function disableFieldsAsPerRole(){
				var role='<c:out value="${sessionScope.displayRole}" />';
				//As for Vendor Contact, all user have all access rights.
				var vendorId='<c:out value="${sessionScope.vndrFulfillmentService.vendorID}" />';
				//alert(role);
				if(role == 'user' && vendorId == ''){
					//alert('Inside if');
					//alert(document.getElementById("AddBtn"));
					document.getElementById("add_btn").disabled = true;
					var lock_btn = document.getElementsByName("lock_btn");
					if(lock_btn != null){
						//alert(lock_btn.length);
						for(var i = 0; i< lock_btn.length ; i++){
							lock_btn[i].style.visibility='hidden';
						}
					}
					var unlock_btn =document.getElementsByName("unlock_btn");
					if(unlock_btn != null){
						for(i = 0; i< unlock_btn.length ; i++){
							unlock_btn[i].style.visibility='hidden';
						}
					}
					var edit_btn =document.getElementsByName("edit_btn");
					if(edit_btn != null){
						for(i = 0; i< edit_btn.length ; i++){
							edit_btn[i].style.visibility='hidden';
						}
					}
				}
			}
			function setStatusDropDwnValue(){
				var s = document.getElementById("status");
				 var v ='${requestScope.status}';
				 for (var i = 0; i < s.options.length ; i++) {
				  if( s.options[i].value == v){
				    s.options[i].selected = true;
					return;
				  }
				}
			}
		
		</script>
</body>

<content tag="inlineStyle">
#attr_list{
	margin-top:10px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	

	$('body.admin table.dstable tr').hover(function(){
              $('td',this).addClass('trHover');
              $(this).click(function(){
	              $('td',this).removeClass('trHover');
	              $('td',$(this).parent()).removeClass('trSelected');
	              $('td',this).addClass('trSelected');
              });
   },function(){
             $('td',this).removeClass('trHover');
   });
	 window.scrollTo(0,<c:out value="${requestScope.scrollPos}" />);
});
</script>
]]>
</content>

		
		