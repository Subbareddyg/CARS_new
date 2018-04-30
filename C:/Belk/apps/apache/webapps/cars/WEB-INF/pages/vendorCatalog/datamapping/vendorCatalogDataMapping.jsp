<%@ include file="/common/taglibs.jsp"%>
<script>

//dropship phase 2:added code fix for firefox browser onload 
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
	 // commented as part of Dropship 2012
        /* var element=document.getElementById('dataMapOpt');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}*/
}
function verifyCancel(urlToRedirect) {
	var stay=confirm("Are you sure you wish to cancel? All the non saved information will be lost.");
	var id = '<c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}"/>';
	if (stay) {
		window.location=urlToRedirect;
	}
	else
		return false;
}

function disableGlobalTemplateName(val) {

 if(val=='Y') {
 document.getElementById("globalTemplateName").disabled= false;
 } else {
   document.getElementById("globalTemplateName").disabled= true;
 }
    
}
</script>
<head>
    <title><fmt:message key="datamapping.title"/></title>
    <meta name="heading" content="<fmt:message key='datamapping.title'/>"/>
    <meta name="menu" content="AdminMenu"/>
    <!-- Style sheet for the popup box -->
    <link rel="stylesheet" type="text/css" media="all" href="/cars/css/popup.css" />
</head>
<body class="admin">
	<div id="mainContent">
<!--  Importing the header tabs -->
<%@ include file="headerDataMappingTab.jsp"%>
	

<!--  Breadcrumbs  -->
	<div style="margin-left:5px; font-size:13px;">
		<br/>
		<a href="../../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a> 
		> <c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
		  <c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
		  <c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
		  <c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="cancelUrl"/>
		  <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendor.name}" default="isNull"/></a>
		  <c:url value="/vendorCatalog/vendorCatalogSetupForm.html?vcID=${vendorCatalog.vendorCatalogID}&mode=${mode}" var="formAction"/>
		> <a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out value="${vendorCatalog.vendorCatalogName}" default="isNull"/></a>   
		> Data Mapping
		<br><br>

		<b><fmt:message key="datamapping.title"/></b>
  	</div>
  	<br>


<!-- Catalog info panel -->
<div class="cars_panel x-hidden"> 
	<div class="x-panel-header">
		<fmt:message key="datamapping.catalog.info"/>
	</div>
	<div class="x-panel-body"> <!-- Start x panel body 1 -->
	<c:set var="statusCode" value="${vendorCatalogDataMappingForm.vendorCatalog.statusCD}" scope="page" />
	<c:set var="catalogID" value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}" scope="page" />
		
		<table style="font-size:11px; border:0px;">
			<tr>
				<td width="33%" class="car_id" style="border:0px;"><b>Vendor #:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendor.vendorNumber}" /></td>
				<td width="33%" class="dept" style="border:0px;"><b>Catalog ID:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogID}" /></td>
				<td width="33%" style="border:0px"><b>Status: </b> <c:out value="${statusCode}" />
				<input type="hidden" id="catalogIDVal" value='<c:out value="${CATALOG_ID_FOR_MAPPING}" />' /></td>
			</tr>
			
			<tr>
				<td width="33%" style="border:0px;" class="car_id"><b>Vendor Name:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendor.name}" /></td>
				<td width="33%" style="border:0px;" class="dept"><b>Date Created:</b> <fmt:formatDate pattern="MM/dd/yyyy" value="${vendorCatalogDataMappingForm.vendorCatalog.createdDate}" /></td>
				<td width="33%" style="border:0px;"><b>Last Updated:</b> <fmt:formatDate pattern="MM/dd/yyyy" value="${vendorCatalogDataMappingForm.vendorCatalog.updatedDate}" /></td>
			</tr>
			
			<tr>
				<td width="33%" style="border:0px;" class="car_id"><b>Catalog Name:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogName}" /></td>
				<td width="33%" style="border:0px;" class="dept"><b>Created By:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.createdBy}" /></td>
				<td width="33%" style="border:0px;"><b>Last Updated By:</b>  <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.updatedBy}" /></td>
			</tr>
			
			<tr>
				<td width="33%" style="border:0px;" class="car_id"><b>Catalog Source:</b> <c:out value="${vendorCatalogDataMappingForm.vendorCatalog.source}" /></td>
				<td width="33%" style="border:0px;" class="dept">&nbsp;</td>
				<td style="border:0px;">&nbsp;</td>
			</tr>
			
		</table>

	</div> <!-- End of x-pane-body 1 -->
</div> 
<!--  End of catalog info panel -->
<br>
<!-- Data Mapping options panel -->

<div class="cars_panel x-hidden"> 
	<div class="x-panel-header">
		<fmt:message key="datamapping.mapping.options"/>
	</div>
	
						<div class="x-panel-body" style="height: auto;">
						<spring:bind path="vendorCatalogDataMappingForm.*">
							<c:if test="${not empty error}">
								<div class="error">
									<br>
									<c:forEach var="error" items="${error}">
										<img src="<c:url value="/images/iconWarning.gif"/>"
											alt="<fmt:message key="icon.warning"/>" class="icon" />
										<c:out value="${error}" escapeXml="false" />
										<br />
									</c:forEach>
								</div>
							</c:if>
						</spring:bind>
							<form id="userForm" method="post" action="/vendor/vendorForm.html">
								<input type="hidden" id="id" name="id" value="" /> <input type="hidden"
								id="version" name="version" value="0" /> 
								<input type="hidden" name="from" value="list" />
								
								<ol> <!--  Start of the OL in form -->
									<b style="margin-left: 1%;  font-size:11px;">Uploaded Data File:</b>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<a href="../../vendorCatalog/datamapping/vendorCatalogDataMapping.html?method=openCatalogFile&catalogStatus=${statusCode}&catalogId=${vendorCatalog.vendorCatalogID}&fileName=${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogImport.vendorCatalogFileName}&catalogVendor=${vendorCatalogDataMappingForm.vendorCatalog.vendor.vendorNumber}"><c:out value="${vendorCatalogDataMappingForm.vendorCatalog.vendorCatalogImport.vendorCatalogFileName}" /></a>
										<br/><br/>
									<li class="first" style="font-size:11px;">
										<label for="firstName" class="desc">
											Data Mapping Option: 
										</label> 
										<select class="text" name="templateSelect" id="dataMapOpt">
											<option value="newDataMap">Create new data map</option>
											<option value="existingDataMap">Use existing map for this
											vendor</option>
											<option value="globalMap">Use globally defined map</option>
											<option value="fromVendor">Use map from another vendor</option>
										</select>
									</li>
									<li class="first" id="existMap" style="display:none;">
										<label for="lblExitMap" class="desc">
											Existing Map from the vendor: 
										</label> 
										<form:select path="vendorCatalogDataMappingForm.sExistTemplateForVendor" id="importMapping">
											<form:option value ="-1">Select a Value</form:option>
      										<form:options items= "${vendorCatalogDataMappingForm.existingTemplateForVendor}" 
      										itemLabel = "vendorCatalogName" itemValue = "vendorCatalogTmplID"/>
										</form:select>
									</li>
									<li class="first" id="globalMap" style="display: none;">
										<label for="firstName" class="desc">
											Existing Data Map: 
										</label> 
										<form:select path="vendorCatalogDataMappingForm.sGlobalTemplate" id="globalMapping">
											<form:option value ="-1">Select a Value</form:option>
      										<form:options items= "${vendorCatalogDataMappingForm.globalTemplate}" 
      										itemLabel = "vendorCatalogName" itemValue = "vendorCatalogTmplID"/>
										</form:select>
									</li>
										<li class="first" id="importMapVen" style="display:none;" width="100%">
												<label for="firstName" class="desc">Import Map from Vendor :</label>
												<label for="firstName" class="desc">
													<form:select path="vendorCatalogDataMappingForm.selectedVendor" id="selectedVendor">
														<form:option value ="-1">Select a Value</form:option>
			      										<form:options items= "${vendorCatalogDataMappingForm.vendors}" 
			      										itemLabel = "name" itemValue = "vendorId"/>
													</form:select>

												</label>
												<li id="importMapVenCat" style="display:none;"/>
												<%@ include file="inc_OtherVendors.jsp"%>		
											</li>

									<li class="messages"></li>
								</ol> <!--  End of the OL in form -->
							</form> <!--  End form userForm -->
						</div> 	<!--  End of x-panel-body div -->
					</div> <!-- End Data Mapping options panel -->
					
<br>
					<!-- Start of data mapping rules panel -->
					<div class="cars_panel x-hidden"> 
						<div class="x-panel-header">
							<fmt:message key="datamapping.mapping.rules"/>
						</div>
						<div class="x-panel-body" style="height: auto;">
							<%@ include file="inc_mappingBody.jsp"%>
						</div>
					</div>	
					<!-- End of Mapping body -->

<br>
<c:if test="${statusCode == 'Data Mapping'}" >
	<div>
	<li style="margin-left: 10px;">
	<input type="button" class="btn cancel_btn" name="cancel" value="Cancel" id="cancel" onclick="verifyCancel('<c:out value="${cancelUrl}" escapeXml="false"/>');">
	<c:choose>
		<c:when test="${sessionScope.edit == 'true'}">
			<input type="button" class="btn cancel_btn" name="save" value="Save" id="saveButton" ref="${formAction}">
			<input type="button" class="btn cancel_btn" name="saveComplete" value="Save And Complete" id="saveCompleteButton" ref="${formCompleteAction}">
		</c:when>
		<c:otherwise>
			<input type="button" class="btn cancel_btn" name="save" value="Save" id="saveButton" ref="${formAction}" disabled="true">
			<input type="button" class="btn cancel_btn" name="saveComplete" value="Save And Complete" id="saveCompleteButton" ref="${formCompleteAction}" disabled="true">
		</c:otherwise>
	</c:choose>
	
	<div style="display: none;" id="saveNote">
		Mapping saved and completed successfully!	
		</div>
	</li>
	</div>
</c:if>					
<br>
<br>
<!-- Popup Message -->
<!-- Cancel Clicked -->

<div id="remove_global_template_win" class="x-hidden">
<div class="x-window-header">Save and Complete Click</div>
<div id="remove_global_template_form" class="content">
<c:url value='vendorCatalogDataMapping.html?method=saveCompleteTemplate' var="formAction"/>
<form method="post" action="${formAction}" id="productTypeGroupForm" >
<ul style="padding:8px;">
		<li style="float:left;">
			<strong>Do you want to save this as a global template ?  &nbsp;&nbsp;  </strong></li>
			
                <li>
                    <select id="isGlobal" name="isGlobal" onchange="disableGlobalTemplateName(this.value)">
                        <option value="Y"><b>Yes</b></option>
                        <option value="N" selected><b>No</b></option>

                    </select>
                   
		</li>
		
		<li style="float:left;"></br><strong>Global Template Name :   </strong></li>
		<li></br><input type="name" name="globalTemplateName"  style="margin-left:17px;" id="globalTemplateName" disabled="true"/>	</li>
		
</ul>					
<br/><br/>		
</form>
</div></div>

</div> <!-- End of div mainContent -->	
</body>
<content tag="inlineStyle">
div.buttons{
	padding-left:160px;
}
.olheader{
	border: 1px solid rgb(141, 178, 227); 
	overflow:auto;
	height: 200px;
	padding-left:5px; 
	padding-right:5px;
}
div.olheader .vendorList{
	background-color:#F6F6F6;
}

.limasterstyle,
.libmstyle,
.listyle{
	border: 1px solid rgb(208, 208, 208); 
	padding-left: 5px; 
	width: 129px; 
	background-color: rgb(246, 246, 246);
}
.olgroups{
	border: 1px solid rgb(141, 178, 227); 
	height: 80px;  
	overflow-y:	scroll; 
	overflow-x: hidden; 
	width: 150px; 
	padding-left: 10px; 
	}
.trRowDisp{}
.tdOthers{
	border: 1px solid rgb(208, 208, 208);
	padding: 3px; 
	background-color: rgb(246, 246, 246);
}
select.fldMap{
	font-size:5;
}
.cursor_pointer {
    cursor:hand;
}

</content>
<content tag="jscript">
<![CDATA[
<!-- Included for displaying the modal box-->
<script type="text/javascript" src="<c:url value='/js/libs/jquery.simplemodal-1.3.3.js'/>"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var vall = '<%=request.getAttribute("scrollPos") %>';
		var remove_win=null;
		var prevBM = null;
		var prevM = null;
		callAjaxCalls();
		getCarAttributes();
		mappedSelection();
		changeTemplates();
		fldDataMap();
		chooseTemplates();
		otherVendorTemplates();
		globalTemplate();
		chkUniqueTemplateName();
		$("#saveButton").click(function(){
		//alert('Alert clicked');
			var action = $("#saveButton").attr("ref") +'&scrollPos=524';
			var valueEntered = 0;
			$('.jserrors').html('');
			$('.error').html('');
			$('.chkBox').each(function(){
				 var isChecked = $(this).attr('checked');
				 if(isChecked){
				 	//alert('cHECK BOX IS CHECKED.');
				 	valueEntered = 1;
				 } 
			});  
			//<c:url value="/images/iconWarning.gif"/>
			if(valueEntered === 0){
				//alert('Value is zero.');
				$('.jserrors').append('<img src="<c:url value="/images/iconWarning.gif"/>" class="icon" />');
				$('.jserrors').append(' Please select a product Group.<br><br>');
			}else{
				$("#vendorCatalogDataMappingForm2").attr("action",action);
				$("#vendorCatalogDataMappingForm2").submit();
			}
			
		});
	
		$("#saveCompleteButton").click(function(){
		location.hash = window.pageYOffset; 
			var action = $("#saveCompleteButton").attr("ref") +'&scrollPos=524';
			$("#vendorCatalogDataMappingForm2").attr("action",action);
			$(this).blur();
		if (!remove_win) {
			
			remove_win = new Ext.Window({
				el:'remove_global_template_win',
				layout:'fit',
				width:450,
				autoHeight:true,
				closeAction:'hide',
				modal:true,
				plain:true,
				items: new Ext.Panel({
                    contentEl:'remove_global_template_form',
                    deferredRender:false,
                    border:false,
					autoHeight:true
                }),
				buttons: [{
		                text: 'Cancel',
		                handler: function(){
		                    remove_win.hide();
		                }
	            	},{
		                text: 'Continue Saving',
		                id: 'contSave',
		                handler: function() {
		                	var globalTemplateName = $('#globalTemplateName').val();
							var isGlobalTemplate = $('#isGlobal').val();
                             //  alert($('#isGlobal').val());                         
							var loadUrl="vendorCatalogDataMapping.html?method=chkUniqueName&ajax=true&isGlobalTemplate="+isGlobalTemplate;
							var dataString='globalTemplateName=' + globalTemplateName +'&isGlobalTemplate='+isGlobalTemplate;
							$('#remove_global_template_form form').ajaxSubmit({
									cache: false,
									dataType: 'json',
									url: loadUrl,
									data: dataString,
									success: function(htmlResponse) {
											if(htmlResponse.success){
													//window.location.reload(true);
													if($('#isGlobal').val() =='Y'){
														//If it is a global template set the values back on the form
                                                                                                        //        alert($("#globalTemplateName").val());
														$("#vendorCatalogTemplateName").val($("#globalTemplateName").val());
														$("#globalTemplateValue").val('true');
													}else{
														//Set the global template value to false
														$("#globalTemplateValue").val('false');
													}
													$("#vendorCatalogDataMappingForm2").submit();
											} else {
												var error_message = htmlResponse.error_message;
												Ext.MessageBox.show({
													title: 'Error',
													msg: error_message,
													buttons: Ext.MessageBox.OK,
													icon: 'ext-mb-error'
													});
											}
									}
		     		 });
		             }
					}
				]
			});
		}
		remove_win.show(function(){
			
		});
		return false;
		});
	
		$("#btnCarAttributes").click(function(){
			$("#vendorCatalogDataMappingForm1").submit();
		});
		//alert('Position :'+ <c:out value="${requestScope.scrollPos}" />);
		//window.scrollTo(0,524);
		//window.scrollTo(0, location.hash); 
		//alert(vall);
		if( vall > 0){
			 window.scrollTo(0,vall);
		} else{
		window.scrollTo(0,0);
		}
	
	//Fade in the successful messages.		
		var msg_size = $('#successful').size();
		if(msg_size == 1){
			$('#successful').fadeIn(function(){
								var $this = $(this);
								setTimeout(function(){
									$this.fadeOut("slow");
								}, 3000);
							});
		}
	//document.ready ends	
	});
	//Ajax functionality for getting the car attributes based on the group selection
	function chkUniqueTemplateName(){
		var loadUrl="vendorCatalogDataMapping.html?method=chkUniqueName&ajax=true";
		$("#contSave").click(function(){
			var globalTemplateName = $('#globalTemplateName').val();
			var isGlobalTemplate = $('#isGlobal').val();
			var dataString='globalTemplateName=' + globalTemplateName +'&isGlobalTemplate='+isGlobalTemplate;
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxpopupError")
					.html(htmlResponse)
					.hide()
					.fadeIn(1500, function() {
						$('#message').append("Loading...");
					});
			}
		     });
		});
	}
	
	
	
	//When the popup Shows this does the enabling and disabling of the template name
	function globalTemplate(){
		$("#isGlobal").click(function(){
			if($('#isGlobal').val() =='Y'){
				//Open the name value 
				$("#globalTemplateName").removeAttr('disabled');
			}else{
				$("#globalTemplateName").val('');
				$("#globalTemplateName").attr('disabled','disabled');
			}
		});
	}
	
	//This is for confirming the cancelation	
	function confirmSave(divShow, callback) {
		$('#globalTemplate').modal({
			closeHTML:"<a href='#' title='Close' class='modal-close'>x</a>",
			overlayId:'confirm-overlay',
			containerId:'confirm-container', 
			escClose:'false',
			onShow: function (dialog) {
					
				// if the user clicks "yes"
				$('.yes', dialog.data[0]).click(function () {
					// call the callback
					if ($.isFunction(callback)) {
						callback.apply();
					}
					// close the dialog
					$.modal.close();
				});
			}
		});
	}


	//All the ajax functions to be reintialized
	function callAjaxCalls(){
		mapRules();
		selectMapping();
		unMapRules();
	}
	
	//Ajax functionality for getting the car attributes based on the group selection
	function getCarAttributes(){
		var loadUrl="vendorCatalogDataMapping.html?method=getCarAttributes&ajax=true";
		$("input.chkBox").click(function(){
			var dataString=$("#vendorCatalogDataMappingForm1").serialize();
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxDivRules")
					.html(htmlResponse)
					.hide()
					.fadeIn(1500, function() {
						$('#message').append("Loading...");
					});
					callAjaxCalls()
			}
		     });
		});
	}

	//Functionality for selecting the groups
	function selectMapping(){
		var bgColorValue= '#CEDFF5';
		var bgColorGrey = '#F6F6F6';
		$("li.listyle").click(function(){
			$("li.listyle").attr("style","listyle");
			$(this).css("background-color",bgColorValue);
			$("#mapVendorSuppliedFieldID").val($(this).attr("refVenKey"));
			$("#mapVendorSuppliedField").val($(this).attr("refVenVal"));
			return false;
		});
		$("li.limasterstyle").click(function(){
			$(".limasterstyle").css("background-color",bgColorGrey);
			$(".libmstyle").css("background-color",bgColorGrey);
			$("li.limasterstyle").attr("style","limasterstyle");
			$(this).css("background-color",bgColorValue);
			$("#mapMasterMappingId").val($(this).attr("refMsId"));
			$("#mapMasterAttribute").val($(this).attr("refMsNm"));
			$("#mapBlueMartiniAttribute").val($(this).attr("refBM"));	
			$("#mapBlueMartiniKey").val($(this).attr("refBMKey"));
			return false;
		});

		$("li.libmstyle").click(function(){
			$(".limasterstyle").css("background-color",bgColorGrey);
			$(".libmstyle").css("background-color",bgColorGrey);
			$("li.libmstyle").attr("style","libmstyle");
			$("#mapBlueMartiniAttribute").val($(this).attr("refBM"));	
			$("#mapBlueMartiniKey").val($(this).attr("refBMKey"));
			$("#mapMasterMappingId").val(-1);
			$("#mapMasterAttribute").val($(this).attr("refMsNm"));
			$(this).css("background-color",bgColorValue);
			return false;
		});
		
	}

	// Ajax functionality for getting the data mapping values
	function mapRules(){
		var loadUrl="vendorCatalogDataMapping.html?method=mapAttributes&ajax=true";
		//,#move_left
		$("#move_right").click(function(){
			var dataString=$("#vendorCatalogDataMappingForm2").serialize();
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxDivRules")
					.html(htmlResponse)
					.hide()
					.fadeIn(1500, function() {
						$('#message').append("Loading...");
					});
					callAjaxCalls();
					mappedSelection();
					fldDataMap();
		      		}
		     	});
		});
	}
	//Ajax functionality for mapped field values	
	function fldDataMap(){
		var loadUrl="vendorCatalogDataMapping.html?method=mapFieldData&ajax=true";
		$("select.fldMap").change(function(){
			$("#mapVenFldDataMapping").val($(this).attr("refVenFld"));	
			$("#mapVenHeaderData").val($(this).attr("refVenHdr"));	
			$("#mapCarFldDataMapping").val($(this).val());	
			var dataString=$("#vendorCatalogDataMappingForm2").serialize();
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxFldDataMapping")
					.html(htmlResponse)
					.hide()
					.fadeIn(3000, function() {
						$('#message').append("Loading...");
					
					});
					fldDataMap();
		      		}

		      		
		     	});
		});
	}
	//Ajax functionality for getting the data field mapping already
	function mappedSelection(){
		var bgColorValue= '#CEDFF5';
		var loadUrl="vendorCatalogDataMapping.html?method=selectMappedData&ajax=true";
		$("tr.trRowDisp").click(function(){
			$("td.tdRowDisp").attr("style","border: 1px solid rgb(208, 208, 208);padding: 3px; background-color: rgb(246, 246, 246);");
			$(this).children("td").css("background-color",bgColorValue);		
			$("#mapRowNumber").val($(this).attr("refRowNum"));	
			var dataString=$("#vendorCatalogDataMappingForm2").serialize();
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxFldDataMapping")
					.html(htmlResponse)
					.hide()
					.fadeIn(3000, function() {
						$('#message').append("Loading...");
					});
					fldDataMap();
		      		}
		     	});
		});

	}
	
	// Ajax functionality for removing the data mapping values
	function unMapRules(){
		var loadUrl="vendorCatalogDataMapping.html?method=unMapAttributes&ajax=true";
		$("#move_left").click(function(){
			var dataString=$("#vendorCatalogDataMappingForm2").serialize();
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxDivRules")
					.html(htmlResponse)
					.hide()
					.fadeIn(1500, function() {
						$('#message').append("Loading...");
					});
					callAjaxCalls();
					mappedSelection();
					fldDataMap();
				}
			});
		});
	}
	
	//Ajax functionality for changing template
	function changeTemplates(){
		$("#importMapping, #globalMapping").change(function(){
			getTemplateValues($(this),"OLD");
		});
	}
	
	function chooseTemplates(){
		$("#dataMapOpt").change(function(){
		var optVal = $("#dataMapOpt").val();
		if(optVal == "fromVendor"){
			$("#existMap").hide();
			$("#globalMap").hide();
			$("#importMapVen").show();
			$("#importMapVenCat").show();
			importMapVenCat
		}
		else if(optVal == "newDataMap"){
			$("#importMapVen").hide();
			$("#importMapVenCat").hide();
			$("#globalMap").hide();
			$("#existMap").hide();
			
			getTemplateValues($(this),"NEW");
		}
		else if(optVal == "existingDataMap"){
			$("#importMapVen").hide();
			$("#importMapVenCat").hide();
			$("#globalMap").hide();
			$("#existMap").show();
		}
		else if(optVal == "globalMap") {
			$("#importMapVen").hide();
			$("#importMapVenCat").hide();
			$("#existMap").hide();
			$("#globalMap").show();
		}

		
	});
		
		
	
	}
	//Ajax submit for getting the data template mapping values
	function getTemplateValues(objThis,mappingOption){
		var loadUrl="vendorCatalogDataMapping.html?method=changeTemplates&ajax=true&mapOption="+mappingOption;
		var selectedTemplate = objThis.val();
		var catalogID = $("#catalogIDVal").val();
		var selectedValue = objThis.attr("name");
		var dataString='selectedTemplate=' + selectedTemplate + '&catalogIDVal=' + catalogID +'&selectedValue=' + selectedValue; 
		//alert(dataString);
		$.ajax({
			cache: false,
			type: "POST",
			url: loadUrl,
			data: dataString,
			success: function(htmlResponse) {
				$("#ajaxMappingBody")
				.html(htmlResponse)
				.hide()
				.fadeIn(1500, function() {
					$('#message').append("Loading...");
				});
				//Reinitializing the car attributes value
				getCarAttributes();
			}
		});
	}
	//Ajax for getting all the templates for another vendor
	function otherVendorTemplates(){
		var loadUrl="vendorCatalogDataMapping.html?method=getOtherVendorTemplates&ajax=true";
		$("#selectedVendor").change(function(){
			var selectedVendorID = $(this).val();
			if(selectedVendorID == '-1') {
				$("#templateFrmAnotherVendor>option").remove();
				//$("#templateFrmAnotherVendor").append($('<option></option>').val('-1').html('Choose a Vendor Name'));
				return false;
				
			}
			var dataString='selVendorID=' + selectedVendorID ;
			//alert(dataString);
			$.ajax({
				cache: false,
				type: "POST",
				url: loadUrl,
				data: dataString,
				success: function(htmlResponse) {
					$("#ajaxOtherVen")
					.html(htmlResponse)
					.hide()
					.fadeIn(1500, function() {
						$('#message').append("Loading...");
					});
					changeAnotherVendorTemplate();
				}
			});
		});

	}
	
	//Ajax for selecting another template for other vendor
	function changeAnotherVendorTemplate(){
		$("#templateFrmAnotherVendor").change(function(){
			var selectedTemplateID = $(this).val();
			if(selectedTemplateID == '-1') {
				return false;
			}
			getTemplateValues($(this),"OLD");
		});
	}

</script>

]]>
</content>