<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTabFulfillmentService.jsp" %>
<head>
    <title>Potential Vendor Tax States</title>
    <c:if test="${pageContext.request.remoteUser != null}">
    	<c:set var="headingUser">
      	 <authz:authentication operation="fullName"/>
       </c:set> 
	</c:if>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>"/>
    <meta name="menu" content="MainMenu"/>
       
</head>
<script>
function verifyRemove(box)
{//alert(box);
                var stay=confirm("Are you sure you want to remove?");
                if (stay){
                
                document.vendorTaxForm.action="../oma/vendorTaxStates.html?method=remove&taxId="+box;
                document.vendorTaxForm.submit();
                }
                 else
    			 return false;
               
}
function disableRole(){
	var role='<c:out value="${sessionScope.displayRole}" />';
	
	if(role == 'user'){
		
		document.getElementById("add_car_note_btn").disabled=true;
		
		
	}
}
</script>
<body class="admin">
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
	 document.getElementById("tab2").className="activeTab";
	 //disableRole();
	 var element=document.getElementById('state');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
}
</script>
<br>
<span style="font-weight:bold">Potential Vendor Tax States</span>
<br>
<div id="search_for_user" class="cars_panel x-hidden" style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Vendor Tax Search
	</div>
	<div class="x-panel-body">
		<c:if test="${not empty searchFormError}">
		    <div class="error">        
		            <img src="<c:url value="/images/iconWarning.gif"/>"
		                alt="<fmt:message key="icon.warning"/>" class="icon"/>
		            <c:out value="${searchFormError}" escapeXml="false"/><br />        
		    </div>
	    </c:if>
		<c:url value="/oma/vendorTaxStates.html?method=search" var="formAction"/>
		<form method="post"   action="${formAction}" id="searchForm">
			<ol>
				<li>
					<label><b><fmt:message key="fsvendorTax.search.state.name"/></b></label>
					<input type="text" id="state" name="stateName" value="<c:out value="${stateName}" />"/>
					
				</li>
				
				<li>
					<label><b><fmt:message key="fsvendor.search.status"/></b></label>
					<select id="status" name="status" >
					<c:choose>
     				<c:when test="${status =='all'}">  
   							<option id="ext-gen106" selected="selected" value="all">All</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="all">All</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
     				<c:when test="${status =='active'}">  
   							<option id="ext-gen106" selected="selected" value="active">Active</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="active">Active</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
     				<c:when test="${status =='inactive'}">  
   							<option id="ext-gen106" selected="selected" value="inactive">Inactive</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="inactive">Inactive</option>
					</c:otherwise>
				</c:choose>
				
				
					</select>
				</li>
				<li class="">
					<input class="btn" type="submit" name="searchUser" value="<fmt:message key="fsvendor.search.button"/>" />
					<c:url value="/oma/vendorTaxStates.html?method=viewAllActive" var="formAction"/>
					<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
				</li>
			</ol>
		</form>
	</div>
</div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Vendors  Tax States
	</div>
	<div class="x-panel-body">
	<div class="userButtons">
	<span id="add_note_msg"></span>
	<c:if test="${sessionScope.showMsg=='yes'}">
 <span id="edit_tax_msg" style="margin-left: 15px;
	padding: 5px 10px;
	background: yellow;
	float: left; "> <c:out value="Saved Successfully!!" escapeXml="false"/> </span><br/>
 <% session.setAttribute("showMsg","no"); %>
</c:if>
		<div class="buttons" style="float:right" >
		
			
			
			<c:choose>
				<c:when test="${sessionScope.displayRole == 'admin'}">
						<input type="submit" id="add_car_note_btn" class="add_note btn" value="Add Vendor Tax States" />
				</c:when>
				<c:otherwise>
				<input type="button" onclick="none" disabled="disabled" value="Add Vendor Tax States"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="add_car_note_btn" class="add_note btn">
				</c:otherwise>
				</c:choose>
			
		</div>
	</div>
	<c:set var="role" value="${sessionScope.displayRole}" />
	<form:form name="vendorTaxForm">
	<div id="add_car_note_btn_1">
		<display:table name="vendorTaxStates" cellspacing="0" cellpadding="0" requestURI="/oma/vendorTaxStates.html?method=search"  
		    defaultsort="2"  pagesize="25" class="table dstable" sort="list"  id="vndrTax" htmlId="mytable">
		    <display:column sortable="true" property="vendorTaxStateId" title="ID" class="hidden" headerClass="hidden" media="html"  />
		    <display:column sortable="true" property="state.stateId" title="StateId" class="hidden" headerClass="hidden" media="html" />
		    <display:column  sortable="true" titleKey="fsvendorTax.statecode"	style="width: 7%"  sortProperty="state.stateCd">
		    
		    <c:choose>
		    <c:when test="${role == 'admin' && vndrTax.status !='INACTIVE'  }">
					<c:url value="/oma/vendorTaxStates.html?method=viewAll&taxId=${vndrTax.vendorTaxStateId}" var="formAction"/>
					<a class="alink" id="${vndrTax.vendorTaxStateId}" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vndrTax.state.stateCd}"/></a>
				
				</c:when>
				<c:otherwise>
					<c:out value="${vndrTax.state.stateCd}"/>
				</c:otherwise>
				</c:choose>
			</display:column>
		        
		    <display:column property="state.stateName" defaultorder="ascending" sortable="true" titleKey="fsvendorTax.stateName" style="width: 10%"/>
		    <display:column property="taxIssue"  sortable="true" titleKey="fsvendorTax.taxissue" style="width: 25%"/>
		    <display:column property="updatedDate"  format="{0,date,MM/dd/yyyy}" sortable="true" titleKey="fsvendorTax.dateLastUpdated" style="width: 15%"/>
		    <display:column property="updatedBy"  sortable="true" titleKey="fsvendorTax.lastUpdatedBy" style="width: 10%"/>    	 
		    <display:column property="status"  sortable="true" titleKey="fsvendorTax.status" style="width: 10%"/>
		  
		    
			 <c:choose>
		    <c:when test="${sessionScope.displayRole == 'admin'}">
		    	
				 <display:column  titleKey="Remove" style="width: 10%" >
				
				 <c:if test="${vndrTax.status=='ACTIVE' }" >
				<input type="button"
						value="Remove" name="remove" class="btn cancel_btn"
						onClick="verifyRemove(${vndrTax.vendorTaxStateId}) ;" />
				</c:if>
			</display:column>
				</c:when>
				<c:otherwise>
				 <display:column   titleKey="Remove" style="width: 10%">
				
				 <c:if test="${vndrTax.status=='ACTIVE'}" >
					<input type="button" onclick="none" disabled="disabled" value="Remove"  style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							 class="btn">
				</c:if>
				</display:column>
				</c:otherwise>
				</c:choose>
			
		    <display:setProperty name="paging.banner.item_name" value="vendor tax state"/>
		    <display:setProperty name="paging.banner.items_name" value="vendor tax states"/>
		   
		    
		</display:table>
	</div>
	</form:form>	
	<div id="add_car_note_win" class="x-hidden">
		<div class="x-window-header"></div>
		<div id="add_car_note_form" class="content">
		
		<form action="ajaxVendorTaxStatesForm.html" method="post">
			<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
			<ul>
			<li class="state">
					<label class="desc">State :<span class="req" style="color:#FF0000">*</span></label>
					<c:set var="stateList" value="${states}" scope="session"/>                 
                    <select name="state" id="stateCd" Class="text" >
                              
                     <c:forEach items="${stateList}" var="state" >
                           <option value="${state.stateId}"><c:out value="${state.stateCd}"/></option>
                     </c:forEach>
                     </select>
			</li>
			<li>
			
			<label class="desc">Potential Tax Issues :<span class="req" style="color:#FF0000">*</span></label>
			<textarea id="issue" name="issue" onkeypress="return imposeMaxLength(this, 3999);"   class=" maxChars text"></textarea>
			<br>
				<fmt:message key="caredit.page.add.a.sample.note.max.char"/> <span class="max_chars_val">4000</span>			<input type="hidden" name="taxid" id="taxid"/>
			
			
		</form>
	</div>
	</div>
	
<div id="add_car_note_win_edit" class="x-hidden">
	<div class="x-window-header"></div>
	<div id="add_car_note_form_edit" class="content" style="margin: 10px" >
	
	<form action="ajaxVendorTaxStatesForm.html" method="post">
		<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		<br>
		<br>
		<ul>
		<li class="state">
				<b><label class="desc">State :<span class="req" style="color:#FF0000">*</span></label></b>
				        
                         <label class="desc" id="stateNm_edit"></label>
			
		</li>
		<li>
		 <input type="hidden" name="state" Class="text" id="stateCd_edit"/>
		 </li>
		
		<li>
		<br>
		<b><label class="desc">Potential Tax Issues :<span class="req" style="color:#FF0000">*</span></label></b>
		<br>
		<br>
		<textarea id="issue_edit" name="issue" onkeypress="return imposeMaxLength(this, 3999);"   class=" maxChars text"></textarea>
		<br>
			<fmt:message key="caredit.page.add.a.sample.note.max.char"/> <span class="max_chars_val">4000</span>
			<input type="hidden" name="taxid" id="taxid_edit"/>
		</li>
		</ul>
		
	</form>
</div>
</div>	

</div></div>
</body>
<content tag="jscript">
<![CDATA[

<script type="text/javascript">
function imposeMaxLength(Object, MaxLen)
{
  return (Object.value.length <= MaxLen);
}

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
	
		// Add Note functionality
		var setupNotes=function(btnSel,winId,contentId,notesULSel,btnTxt,msgSel,msg){
			
			var win=null;
			$(btnSel).click(function(){
			var $id = parseInt(this.id); 
			//alert($id); 
			var resp=null;
			// iterating through table row to find if id exist in the table
											$('#mytable tr').each(function() { 
    												
    												if (!this.rowIndex) return; 
    												var customerId =this.cells[0].innerHTML; 
    											//	alert(customerId);
    												if($id==customerId) {
    													$("form input#taxid_edit").val(this.cells[0].innerHTML);
    													$("form input#stateCd_edit").val(this.cells[1].innerHTML);
    													$("#stateNm_edit").text(this.cells[3].innerHTML);
    													$("form textarea#issue_edit").text(this.cells[4].innerHTML);
    												}
												}) ;
												
												
					
		
				// show add car note window when add note button clicked
				$(this).blur();
				if (!win) {
					win = new Ext.Window({
						el:winId,
						layout:'fit',
						width:350,
						autoHeight:true,
						closeAction:'hide',
						modal:true,
						plain:true,
						title:'Add/Edit Vendor Tax States',
						items: new Ext.Panel({
		                    contentEl:contentId,
		                    deferredRender:false,
		                    border:false,
							autoHeight:true
		                }),
						buttons: [{
				                text: 'Cancel',
				                handler: function(){
				                    win.hide();
				                   // window.location.reload(true);
				                }
			            	},{
				                text: btnTxt,
				                handler: function(){
									// add note using ajax submit
									$('#'+contentId+' form').ajaxSubmit({
									
										
										dataType:'json',
										success: function(resp){
											if(resp.success){
												
												win.hide();
													var html=[];										
    												var notes=resp.tax_data;
													var l=notes.length;
												//	alert(notes+'id='+$id);
													var flag=0;
													$('#mytable tr').each(function() { 
    												
    													if (!this.rowIndex) return; 
    													var customerId =this.cells[0].innerHTML; 
    													if(notes[0][0]==customerId) {
    													
    													//setting state issue	
    														this.cells[4].innerHTML=notes[0][1];
    														this.cells[5].innerHTML=notes[0][3];
    														this.cells[6].innerHTML=notes[0][4];
    														
    														flag=1;
    													
    													}
												}) ;
											 if(flag==0){
											 window.location.href=window.location.href;
											 
											// history.go(0); 
											 // window.location.reload(true);
											 }
												$('#edit_tax_msg').hide();
												$(msgSel).html(msg).fadeIn(function(){
													
													var $this=$(this);
													
												});
												
											}
											else{
												Ext.MessageBox.show({
										           title: 'Error',
										           msg: 'Oops!. There was an error, please try again.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-error'
										       	});
											}
										},
										beforeSubmit:function(){
											// validation before submitting
											var $form=$('#'+contentId+' form');
											$('textarea.error',$form).removeClass('error'); //clear existing errors
												$('select.error',$form).removeClass('error'); //clear existing errors
											var missing=false;
											$('textarea.text',$form).each(function(){
												var $this=$(this);
												if($this.val()===''){
													missing=true;
													$this.addClass('error');
												}
											});
											
											$('select.text',$form).each(function(){
												var $this=$(this);
												if($this.val()===''){
													missing=true;
													$this.addClass('error');
												}
											});
											if(missing){
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter all the required fields to add.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-warning'
										       	});
												return false;
											}
											
											return true;
										},
										url:$('#'+contentId+' form').attr('action')+'?ajax=true'
									});
				                }
							}
						]
					});
				}
		        win.show(function(){
					$('#add_car_note_form textarea.note_text').focus();
				});
				return false;
			});
		};
		//car notes
		setupNotes('#add_car_note_btn','add_car_note_win','add_car_note_form','ul.car_notes','Save','#add_note_msg','Saved successfully!!');
		setupNotes('#add_car_note_btn_1 a.alink','add_car_note_win_edit','add_car_note_form_edit','ul.car_notes','Save','#add_note_msg','Saved successfully!!');
		
});


</script>
]]>
</content>

