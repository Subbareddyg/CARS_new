<%@ include file="/common/taglibs.jsp"%>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
<%@ include file="headerTab.jsp" %>
</c:if>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
<%@ include file="headerTabsForVendor.jsp" %>
</c:if>
<head>
   <c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
    <title>Fulfillment Service Notes</title>
    </c:if>
    <c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
     <title>Vendor Notes</title>
    </c:if>
    <meta name="heading" content="<fmt:message key=''/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<br/>
<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
<form:form commandName="fulfillmentServiceNotesForm" method="post"
	action="${formAction}" id="fulfillmentServiceNotesForm">

	<a href="../oma/orderManagement.html?method=load">Fulfillment
	Services</a> >
	<c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
			value="${sessionScope.fulfillmentService.fulfillmentServiceName}"/></a>>
	Fulfillment Service Notes
	<br>
	<br>
	<b>Fulfillment Service Notes </b><br>
</form:form>
</c:if>

<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
<form:form commandName="fulfillmentServiceNotesForm" method="post"
	action="${formAction}" id="fulfillmentServiceNotesForm">

	<a href="../oma/orderManagement.html?method=load">Fulfillment
	Services</a> >
	<c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
			value="${sessionScope.fulfillmentService.fulfillmentServiceName}"/></a>>
	
	<c:set var="venfsid" value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}"/>
	<c:choose>
	<c:when test="${sessionScope.param == 'param'}">
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit&param=param" var="formAction"/>
		<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="${sessionScope.vndrFulfillmentService.venName}"/></a>>
	
	</c:when>
	<c:otherwise>
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit" var="formAction"/>
		<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="${sessionScope.vndrFulfillmentService.venName}"/></a>>
	
	</c:otherwise>
	</c:choose>
	
	Vendor Notes
	<br>
	<br>
	<b>Vendor Notes </b><br>
</form:form>
</c:if>

<br>
<!-- Start Service Info -->
<div class="cars_panel x-hidden" style="margin: 10px 0px 0px;">


<%@ include file="omaInfoSection.jsp"%>
</div>

<!-- End Service Info -->
<br /> 
<!-- Start Notes Search -->
<div id="search_for_user" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Notes Search
	</div>
	<div class="x-panel-body">
	<c:if test="${not empty error}">
		    <div class="error">        
		            <img src="<c:url value="/images/iconWarning.gif"/>"
		                alt="<fmt:message key="icon.warning"/>" class="icon"/>
		            <c:out value="${error}" escapeXml="false"/><br />        
		    </div>
	    </c:if>
<c:url value="/oma/fulfillmentServiceNotes.html?method=searchFS" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><b><fmt:message key="fulfillmentServiceNotes.subject"/>:</b></label>
				<input type="text" id="fulfillmentServiceNotesSubject" name="fulfillmentServiceNotesSubject" value="<c:out value="${fulfillmentServiceNotesSubject}" />"/>
			</li>
			
			<li class="">
				<input type="submit" class="btn" name="searchFS" value="<fmt:message key="fulfillmentService.button.search"/>" />
				<c:url value="/oma/fulfillmentServiceNotes.html?method=viewAll" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
				
			</li> 
		</ol>
	</form>
</div></div>
<!-- End Notes Search -->
<br/>
<!-- Start Note Listing -->

<div id="user_list" class="cars_panel x-hidden">

	<div class="x-panel-header">
		Notes Listing
	</div>
	<div class="x-panel-body">
	
				<div class="userButtons">
				<c:if test="${sessionScope.saveMsg=='yes'}">
 <span style="background:#FFFF00;"> <c:out value="Saved Successfully!!" escapeXml="false"/> </span><br/>
 <% session.setAttribute("saveMsg","no"); %>
</c:if>
				<!--  show the Add Note only in edit mode -->
				<c:if test="${sessionScope.mode=='viewOnly'}">
					<div class="buttons" style="float:right;">
						<input type="button" onclick="none" disabled="disabled" value="Add Note" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="add_car_note_btn" class="add_note btn">
					<span id="add_note_msg"></span>
					</div>
				</c:if>
				<c:if test="${sessionScope.mode!='viewOnly'}">
					<div class="buttons" style="float:right;">
					<input type="submit" id="add_car_note_btn" class="add_note btn" value="Add Note"  />
					<span id="add_note_msg"></span>
					</div>
				</c:if>
				</div>			
			
<div id="add_car_note_btn_1">
<!-- Note listing for vendor -->
<c:choose>
		<c:when test="${sessionScope.vndrFulfillmentService.vendorID != null}">
<display:table name="VendorFulfillmentNotes" cellspacing="0" cellpadding="0" requestURI="/oma/fulfillmentServiceNotes.html?method=loadVendorNote"  
    defaultsort="1" pagesize="3" class="dstable" sort="list" id="VendorFulfillmentNotes" htmlId="mytable">
     	<display:column property="vendorFulfillmentNoteID" sortable="true" titleKey="ID" style="width: 8%;text-align:center;" class="noteSubject" />
     	<display:column  property="vendorFulfillmentNotesSubject" sortable="true" titleKey="Subject" class="noteSubject" />
   		 <display:column property="vendorFulfillmentNotesText" title="ID" class="hidden" headerClass="hidden" media="html" />
  		<display:column property="createdDate" format="{0,date,MM/dd/yyyy}" sortable="true" style="width: 22%;text-align:center;"  titleKey="Date Created" />
  		<display:column property="createdBy" sortable="true" style="width: 22%;" titleKey="Created By" />  		
    	<display:setProperty name="paging.banner.item_name" value="note"/>
    	<display:setProperty name="paging.banner.items_name" value="notes"/>
</display:table>
</c:when>
<c:otherwise>
<!-- End vendor note listing -->
<display:table name="FulfillmentServiceNotes" cellspacing="0" cellpadding="0" requestURI="/oma/fulfillmentServiceNotes.html?method=load"  
    defaultsort="1" pagesize="25" class="dstable" sort="list" id="FulfillmentServiceNotes" htmlId="mytable">
    		<display:column  property="fulfillmentServiceNoteID"  sortable="true" titleKey="ID" style="width: 8%;text-align:center;" class="noteSubject"/>
    	 	<display:column  property="fulfillmentServiceNotesSubject" sortable="true" titleKey="Subject" class="noteSubject" />
			<display:column property="fulfillmentServiceNotesDesc" title="ID" class="hidden" headerClass="hidden" media="html" />
  			<display:column property="createdDate" format="{0,date,MM/dd/yyyy}" sortable="true" style="width: 22%;text-align:center;"  titleKey="Date Created" />
  			<display:column property="createdBy" sortable="true" style="width: 22%;" titleKey="Created By"/>  		
    		<display:setProperty name="paging.banner.item_name" value="note"/>
    		<display:setProperty name="paging.banner.items_name" value="notes"/>
</display:table>
</c:otherwise>
</c:choose>
</div>
<!--End note Listing  -->
<!-- Add Note -->
			
			
			<div id="add_car_note_win" class="x-hidden" onmouseover="document.notesForm.noteSubject.focus();" >
			<div class="x-window-header">Add a Catalog Note</div>
			<div id="add_car_note_form" class="content">
			
			<c:url value="/oma/addFulfillmentServiceNote.html" var="formAction"/>	
			<form method="post" action="${formAction}" id="FulfillmentServiceNotes" name="notesForm" >
				<p class="required"><span class="req" style="color:#FF0000">*</span> Indicates required field</p><br/>				
			<ul>
				<li style="float:left;">	 
				<strong>Subject:<span class="req" style="color:#FF0000">* </span></strong></li>
				<li>&nbsp;<input type="text" name="noteSubject" id="noteSubject" maxlength="100" size="15" value="<c:out value="${subject}"></c:out>"/>
				</li>
				<br/>
				<li style="float:left;">				
				<strong>Note:<span class="req" style="color:#FF0000">* </span></strong></li>
				
				<li><textarea onclick="return imposeMaxLength(this, 4000);"  style="margin-left:20px;" class="textarea_form" name="note" id="note" rows="5" cols="25"><c:out value="${noteText}"/></textarea><br/><br/>
				<input type="hidden" name="noteidnew" id="noteidnew"/>
				<span style="padding-left:55px;">(Max Char:4000)</span>
				</li>
								
			</ul>			
			</form>
			</div>
			</div>
			
			
</div></div>
<!-- End Note Listing -->
</body>

<content tag="jscript">
<![CDATA[

<script type="text/javascript">

// dropship phase 2: added code fix for firefox browser onload

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
	document.getElementById("tab5").className="activeTab";
	viewOnlyMode();
	setTimeout(function() { document.getElementById('fulfillmentServiceNotesSubject').focus(); }, 10);
}


function viewOnlyMode(){
	
	var modeTab1='<%=session.getAttribute("mode") %>';
	
	if(modeTab1==='viewOnly'){
	
		document.getElementById("noteSubject").disabled = true;
		document.getElementById("note").disabled = true;
		document.getElementById("add_car_note_btn").disabled = true;			
	}
}
function imposeMaxLength(Object, MaxLen)
{
	var len=Object.value.length;
  if(Object.value.length>MaxLen){
  
 		document.getElementById("note").value=Object.value.substring(0,MaxLen);
 }
   	return (len <= MaxLen);
 // }
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
	var tds;
	var win;
		// Add Note functionality
		var setupNotes=function(btnSel,winId,contentId,notesULSel,btnTxt,msgSel,msg){
		
			$(btnSel).click(function(){
			var resp=null;
			
			if(btnTxt==='Save'){
				
				tds = $('td',$(this).parent()); 
				$("form input#noteidnew").val($(tds[0]).text());
	    		$("form input#noteSubject").val($(tds[1]).text());
	    		$("form textarea#note").text($(tds[2]).text());
	    		
		}
			if(btnTxt==='Add'){
			
			if(win){
	    		
	    		var btns=$('div#add_car_note_win button.x-btn-text');
	    		if(btns.length>1){
	    		$(btns[1]).val(btnTxt);
	    		}
	    	}
	    	
				$("form input#noteidnew").val('');
	    		$("form input#noteSubject").val('');
	    		$("form textarea#note").text('');
	    		
			}
																						
					
		
				// show add note window when add note button clicked
				$(this).blur();
				var modeTab1='<%=session.getAttribute("mode") %>';
				
				if (!win && modeTab1==='viewOnly' ) {
				
					win = new Ext.Window({
						el:winId,
						layout:'fit',
						width:350,
						autoHeight:true,
						closeAction:'hide',
						modal:true,
						plain:true,
						title:'Add/Edit Notes',
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
				                   //window.location.reload(true);
				                }
			            	},{
				                text: btnTxt,
				                disabled:true,
				                handler: function(){
									
				                }
				                
							}
							
						]
					});
				}
				else if(!win){
				
				win = new Ext.Window({
						el:winId,
						layout:'fit',
						width:350,
						autoHeight:true,
						closeAction:'hide',
						modal:true,
						plain:true,
						title:'Add/Edit Notes',
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
				                   //window.location.reload(true);
				                }
			            	},{
				                text: btnTxt,
				               
				                handler: function(){
									// add note using ajax submit
									$('#'+contentId+' form').ajaxSubmit({
									
										
										dataType:'json',
										success: function(resp){
											if(resp.success){
												var flag=0;
												win.hide();
												var notes=resp.notes_data;
												if(notes[0][0]){
																					
														if(notes[0][1]==($(tds[0]).text())){
															$(tds[1]).text(notes[0][2]);
															$(tds[2]).text(notes[0][3]);
															flag=1;
															
														}
													
													}
												
											}
											else{
												Ext.MessageBox.show({
										           title: 'Error',
										           msg: 'Oops!. There was an error, please try again.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-error'
										       	});
											}
											window.location.reload(true);
										},
											beforeSubmit:function(){
											var $form=$('#'+contentId+' form');
											$("form input#noteSubject").removeClass('error'); //clear existing errors
											$("form textarea#note").removeClass('error'); //clear existing errors
									
											var noteLengthExceed=false;
											var noteMissing=false;
											var subjectMissing=false;
											var bothMissing=false;
											
											if(($("form input#noteSubject").val() ==='')&&($("form textarea#note").val() ==='')){
													var $this=$(this);
													bothMissing=true;
													$this.addClass('error');
																						
											}
											if($("form textarea#note").val() ===''){
													var $this=$(this);
													noteMissing=true;
													$this.addClass('error');
																						
											}
											if($("form textarea#note").val().length >4000){
													var $this=$(this);
													noteLengthExceed=true;
													$this.addClass('error');
																						
											}
											
											if($("form input#noteSubject").val() ===''){
													var $this=$(this);
													subjectMissing=true;
													$this.addClass('error');
																						
											}
											if(bothMissing){
											
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter required fields.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-warning'
										       	});
												return false;
											}
											if(noteLengthExceed){
											
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Note length exceeds 4000 characters.',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-warning'
										       	});
												return false;
											}
											
											if(subjectMissing){
											
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter the Subject .',
										           buttons: Ext.MessageBox.OK,
										           icon: 'ext-mb-warning'
										       	});
												return false;
											}
											if(noteMissing){
											
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter a Note.',
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
				else{
			if(win){
	    		
	    		var btns=$('div#add_car_note_win button.x-btn-text');
	    		if(btns.length>1){
	    		$(btns[1]).val(btnTxt);
	    		}
	    	}
				
		      }
		        win.show(function(){
		        
					$('#add_car_note_form textarea.note_text').focus();
				});
				return false;
			});
		};
		//car notes
		setupNotes('#add_car_note_btn','add_car_note_win','add_car_note_form','ul.car_notes','Add','#add_note_msg','Note added successfully!');
		setupNotes('#add_car_note_btn_1 .noteSubject','add_car_note_win','add_car_note_form','ul.car_notes','Save','#add_note_msg','Note saved successfully!');
		
});

</script>
]]>
</content>
</body>
