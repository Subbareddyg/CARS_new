<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerVendorCatalogTab.jsp" %>

<head>
    <title><fmt:message key="vendorcatalogNotes.title"/></title>
    <meta name="heading" content="<fmt:message key=''/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>
<body class="admin">
<div style="margin-left:5px;">
<br/>
	<c:choose>
		<c:when test="${sessionScope.edit == 'true' || sessionScope.viewOnly == 'true'}">
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
		> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  <c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  <c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendor.name}" default="isNull"/></a>
		  <c:url value="/vendorCatalog/vendorCatalogSetupForm.html?vcID=${vendorCatalog.vendorCatalogID}&mode=${mode}" var="formAction"/>
		> <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendorCatalogName}" default="isNull"/></a>   
		> Vendor Catalog Notes
		</c:when>
	</c:choose>
	<br>
	<br>
<b><fmt:message key="vendorcatalogNotes.title"/> </b>
<br/>
</div>
<br/>
<!-- Start Catalog Info -->
<div id="car_info_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="vendorcatalog.cataloginfo"/>
	</div>
	<div class="x-panel-body">
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.vendornumber"/></strong> 
			<c:out value="${vendorCatalog.vendor.vendorNumber}" default="" />
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.catalogid"/></strong> 
			<c:out value="${vendorCatalog.vendorCatalogID }" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.catalogstatus"/></strong>
			<c:out value="${vendorCatalog.statusCD}" default="isNull"/>
		</li>
	</ul>	
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.vendorname"/></strong> 
			<c:out value="${vendorCatalog.vendor.name}" default="isNull"/>
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.datecreated"/> </strong><fmt:formatDate value="${vendorCatalog.createdDate}" pattern="${datePattern}"/>
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.datelastupted"/> </strong><fmt:formatDate value="${vendorCatalog.updatedDate}" pattern="${datePattern}"/> 
		</li>
	</ul>
	<ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
		<li  style="width:30%;">
			<strong><fmt:message key="vendorcatalog.catalogname"/></strong>
			<c:out value="${vendorCatalog.vendorCatalogName}" default="isNull" />
		</li>
		<li style="width:30%;">
			<strong><fmt:message key="vendorcatalog.createdby"/></strong> 
			<c:out value="${vendorCatalog.createdBy}" default="isNull" />
		</li>
		<li>
			<strong><fmt:message key="vendorcatalog.lastupdatedby"/></strong> 
			<c:out value="${vendorCatalog.updatedBy}" default="isNull"/>
		</li>
	</ul>
</div></div><!-- End Catalog Info -->

<!-- Start Notes Search -->
<div id="search_for_user" class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Notes Search
	</div>
	<div class="x-panel-body">
<c:url value="/vendorCatalog/CatalogNotes.html?method=searchNotes" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><b><fmt:message key="vendorcatalogNotes.search.subject"/></b></label>
				<input type="text" id="subject" name="subject" value="<c:out value="${subject}" />"/>
			</li>
			<li class="">
				<input type="submit" class="btn" name="searchVendor" value="<fmt:message key="vendor.search.button"/>" />
				<c:url value="/vendorCatalog/CatalogNotes.html?method=viewAll" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
			</li>
		</ol>
	</form>
</div></div>
<!-- End Notes Search -->

<!-- Start Note Listing -->
<div id="vendor_catalog_note_list" class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Notes Listing
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
				<div class="buttons" style="float:right;">
				<c:if test="${sessionScope.mode eq 'viewOnly'}">
				<input type="submit" id="add_car_note_btn"  disabled="disabled" class="add_note btn" value="Add Note" />
				</c:if>
				<c:if test="${sessionScope.mode != 'viewOnly'}">
				<input type="submit" id="add_car_note_btn" class="add_note btn" value="Add Note" />
				</c:if>
				</div>
				</div>
				
<div id="add_car_note_btn_1">

<display:table name="vendorCatalogNote" cellspacing="0" cellpadding="0" requestURI="/vendorCatalog/CatalogNotes.html"  
    defaultsort="1" pagesize="25" class="dstable" sort="list" id="catlogNotes" htmlId="mytable">
    	<display:column property="catalogNoteID" sortable="true" titleKey="ID" class="noteSubject"  style="width: 5%"/>
		<display:column  property="subject" sortable="true" titleKey="Subject" class="noteSubject" />
		<display:column property="noteText" title="ID" class="hidden" headerClass="hidden" media="html" />
  		<display:column property="createdDate" format="{0,date,MM-dd-yyyy}"  sortable="true" titleKey="Date Created"  style="width: 15%"/>
  		<display:column property="createdBy" sortable="true" titleKey="Created By"  style="width: 10%"/>
    	<display:setProperty name="paging.banner.item_name" value="catalog notes"/>
    	<display:setProperty name="paging.banner.items_name" value="catalog notes"/>
</display:table>
</div>
<!-- Add Note -->
			
			
			<div id="add_car_note_win" class="x-hidden">
			<div class="x-window-header">Add a Catalog Note</div>
			<div id="add_car_note_form" class="content">
			
			<c:url value="/vendorCatalog/addCatalogNote.html" var="formAction"/>	
			<form method="post" action="${formAction}" id="vendorCatalogNote">
				<!--<input type="hidden" name="noteID" value="2149"/>
				<input type="hidden" name="method" value="addUserNote"/>
				
				--><p class="required"><span class="req" style="color:#FF0000">*</span> Indicates required field</p>				
			<br />  
			<ul>
				<li style="float:left;">	 
				<strong>Subject:<span class="req" style="color:#FF0000">* </span></strong></li>
				<li>&nbsp;<input type="text" name="noteSubject" id="noteSubject" maxlength="100" size="15" value="<c:out value="${subject}"></c:out>"/>
				</li>
				
				<br />		
				<li style="float:left;"><strong>Note:<span class="req" style="color:#FF0000">* </span></strong></li>
				
				<li><textarea onkeydown="return imposeMaxLength(this, 4000);"  style="margin-left:20px;" onclick="return imposeMaxLength(this, 4000);" onchange="return imposeMaxLength(this, 4000);" class="textarea_form" name="note" id="note" rows="5" cols="25"><c:out value="${noteText}"/></textarea><br/><br/>
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
	
function viewOnlyMode(){
	
	var modeTab1='<%=session.getAttribute("mode") %>';
	//alert('modeTab1'+modeTab1);
	if(modeTab1==='viewOnly'){
	
		document.getElementById("noteSubject").disabled = true;
		document.getElementById("note").disabled = true;
	}
}
function checkButton(sbutton){

	document.getElementById("add_car_note_btn").value=sbutton.value;
	
	if(document.getElementById("add_car_note_btn").value ==='Add Note'){
	
	    document.getElementById("noteSubject").disabled = false;
		document.getElementById("note").disabled = false;
	}
}
function imposeMaxLength(Object, MaxLen)
{
  if(Object.value.length>MaxLen){
  document.getElementById("note").value=Object.value.substring(0,MaxLen);
  }
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
	var win=null;
		// Add Note functionality
		var setupNotes=function(btnSel,winId,contentId,notesULSel,btnTxt,msgSel,msg){
			
			$(btnSel).click(function(){
			var resp=null;
			
			if(btnTxt==='Save'){
				tds = $('td',$(this).parent()); 
				$("form input#noteidnew").val($(tds[0]).text());
	    		$("form input#noteSubject").val($(tds[1]).text());
	    		$("form textarea#note").text($(tds[2]).text());
	    		if(win){
	    		
	    		var btns=$('div#add_car_note_win button.x-btn-text');
	    		if(btns.length>1){
	    		
	    		$(btns[1]).val(btnTxt);
	    		var modeTab1='<%=session.getAttribute("mode") %>';
	    		if(modeTab1==='viewOnly'){
				$(btns[1]).attr('disabled',true);
					}
	    		}
	    		
	    	}
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
				if (!win && modeTab1==='viewOnly') {
					win = new Ext.Window({
						el:winId,
						layout:'fit',
						width:350,
						autoHeight:true,
						closeAction:'hide',
						modal:true,
						plain:true,
						title:'Add/Edit Vendor Catalog Notes',
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
						title:'Add/Edit Vendor Catalog Notes',
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
											
											if(($("form input#noteSubject").val() ==='')&&($("form textarea#note").text() ==='')){
													var $this=$(this);
													bothMissing=true;
													$this.addClass('error');
																						
											}
											if($("form textarea#note").text() ===''){
													var $this=$(this);
													noteMissing=true;
													$this.addClass('error');
																						
											}
											if($("form textarea#note").text().length >4000){
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
										           msg: 'Please enter the Subject.',
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


 function init(){
	document.getElementById("tab3").className="activeTab";
	viewOnlyMode();
}

</script>
]]>
</content>