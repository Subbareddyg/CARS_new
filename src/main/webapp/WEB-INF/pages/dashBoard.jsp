<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<head>
<title><fmt:message key="mainMenu.title" /></title>
<c:if test="${pageContext.request.remoteUser != null}">
	<c:set var="headingUser">
		<authz:authentication operation="fullName" />
	</c:set>
</c:if>
<meta name="heading"
	content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>" />
<meta name="menu" content="MainMenu" />

</head>
<style type="text/css">
	.htable {
		font-size:50px;
	}
</style>
<body class="admin" style="overflow: auto;">

<div class="cars_panel x-hidden" style="margin-top: 10px;">
<div class="x-panel-header">
Car Listing
<c:choose>
  <c:when test="${userTypeCd == 'BUYER' || userTypeCd == 'ART_DIRECTOR' }">
	<select id="sel_listing_filter" style="margin-left:25px;font-weight:normal;font-size:11px;" >
	<option value="">&nbsp;</option>
	<option id="a1" value="assigned_to_me">Assigned To Me</option>
	<option  value="over_due">Late CARS</option>
	<option  value="copy_incomplete">Copy Incomplete</option>
	<option  value="created_today">Created Today</option>
	<option  value="no_updates">No Updates (last 10 days)</option>
	<option  value="requested_images">Has Requested Images</option>
	<option  value="recieved_images">Has Received Images</option>
	<option  value="last_search">Last Search</option>
	<option  value="outfit_cars">Outfit CARS</option>
	<option  value="promo_pyg_cars">Promotional PYG CARS</option>
	<option  value="view_all">View All</option>
</select>
</c:when>
	<c:otherwise>
	<c:choose>
	<c:when test="${userTypeCd == 'BUYER'}">
	<select id="sel_listing_filter" style="margin-left:25px;font-weight:normal;font-size:11px;" >
	<option value="">&nbsp;</option>
	<option id="a1" value="assigned_to_me">Assigned To Me</option>
	<option  value="over_due">Late CARS</option>
	<option  value="outfit_cars">Outfit CARS</option>
	<option  value="promo_pyg_cars">Promotional PYG CARS</option>
	<option  value="last_search">Last Search</option>
	</select>
	 </c:when>
		<c:otherwise>
		<select id="sel_listing_filter" style="margin-left:25px;font-weight:normal;font-size:11px;display: none">
			<option value="">&nbsp;</option>
			<option id="a1" value="assigned_to_me">Assigned To Me</option>
			<option  value="over_due">Late CARS</option>
			<option  value="copy_incomplete">Copy Incomplete</option>
			<option  value="created_today">Created Today</option>
			<option  value="no_updates">No Updates (last 10 days)</option>
			<option  value="requested_images">Has Requested Images</option>
			<option  value="recieved_images">Has Received Images</option>
			<option  value="last_search">Last Search</option>
			<option  value="outfit_cars">Outfit CARS</option>
			<option  value="promo_pyg_cars">Promotional PYG CARS</option>
			<option  value="view_all">View All</option>
		</select>
		</c:otherwise>
		</c:choose>
		</c:otherwise>
</c:choose>
</div>

<!-- Added FNY14 -->
<c:if test="${empty sessionScope.isBuyer}" >
	<c:set var='isCopyTextAllowed' value='true' scope="session"/>
	<c:set var='isBuyer' value='false' scope="session"/>
	<c:set var='isDataGovernanceAdmin' value='false' scope="session"/>
			<c:if test="${userTypeCd == 'BUYER'}">
				<c:set var='isBuyer' value='true' scope="session"/>
				<c:forEach var="role1" items="${user.roles}">
						<c:if test="${(role1 == 'ROLE_ADMIN')}">
							<c:set var='isCopyTextAllowed' value='false' scope="session"/>
						</c:if>
				</c:forEach>
			</c:if>
			<c:forEach var="role1" items="${user.roles}">
						<c:if test="${(role1 == 'ROLE_SIZE_COLOR')}">
							<c:set var='isDataGovernanceAdmin' value='true' scope="session"/>
						</c:if>
			</c:forEach>
</c:if>
	
<!-- ends FNY14 -->

<div class="x-panel-body" style="width:915px;">

<div id="car_listing" style="height: 500px;width:920px; ">

</div>
</div>
</div>

<div id="preview_container" class="cars_panel x-hidden" style="width:950px;">
	<div class="x-panel-header">
		CAR Preview
	</div>
	<div id="preview_content" class="x-panel-body">
		<div id="details_car">
			No CAR Selected
		</div>
	</div>
</div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/js/libs/jquery-1.3.min.js'/>"></script>
<script type="text/javascript">
//--Performing pagination and sorting through ajax-------
if (!com) var com = {};
  com.dashboard = {

    onCarsTableLoad: function() {

		var UTIL = belk.cars.util;
      // Gets called when the data loads
      $("table.dstable th.sortable").each(function() {
        $(this).click(function() {
          // "this" is scoped as the sortable th element
          var link = $(this).find("a").attr("href");
          $("div#car_listing").load(link, {}, com.dashboard.onCarsTableLoad);
          return false;
        });
      });

      $("div#car_listing .pagelinks a").each(function() {
        $(this).click(function() {
          var link = $(this).attr("href");
          $("div#car_listing").load(link, {}, com.dashboard.onCarsTableLoad);
          return false;
        });
      });
    }
  };
//----------------Used for "plus" button on dashboard--------------------------------------------  
  belk.cars.contentstatus = {
  	carUserContentPrivileges:[
		{
			car_user_type:'BUYER',
			content_status:'IN-PROGRESS',
			action:'Complete Content',
			url:belk.cars.config.urls.publish_content+'status=PUBLISHED&car_id='
		},
		{
			car_user_type:'BUYER',
			content_status:'IN-PROGRESS',
			action:'Send Image Only',
			url:belk.cars.config.urls.publish_content+'status=SENT_TO_CMP&car_id='
		},
		{
			car_user_type:'BUYER',
			content_status:'IN-PROGRESS',
			action:'Send Data Only',
			url:belk.cars.config.urls.publish_content+'status=RESEND_DATA_TO_CMP&car_id='
		},
		{
			car_user_type:'BUYER',
			content_status:'SENT_TO_CMP',
			action:'Resend Content',
			url:belk.cars.config.urls.publish_content+'status=RESEND_TO_CMP&car_id='
		},
		{
			car_user_type:'BUYER',
			content_status:'SENT_TO_CMP',
			action:'Resend Data Only',
			url:belk.cars.config.urls.publish_content+'status=RESEND_DATA_TO_CMP&car_id='
		},
		{
			car_user_type:'CONTENT_WRITER',
			content_status:'PUBLISHED',
			action:'Request Content Approval',
			url:belk.cars.config.urls.publish_content+'status=APPROVAL_REQUESTED&car_id='
		},
		{
			car_user_type:'CONTENT_WRITER',
			content_status:'REJECTED',
			action:'Request Content Approval',
			url:belk.cars.config.urls.publish_content+'status=APPROVAL_REQUESTED&car_id='
		},
		{
			car_user_type:'CONTENT_MANAGER',
			content_status:'APPROVAL_REQUESTED',
			action:'Approve Content',
			url:belk.cars.config.urls.publish_content+'status=APPROVED&car_id='
		},
		{
			car_user_type:'CONTENT_MANAGER',
			content_status:'APPROVAL_REQUESTED',
			action:'Reject Content',
			url:belk.cars.config.urls.publish_content+'status=REJECTED&car_id='
		}
	]
  };
  function checkedAll(status) {
  	$(".chkbox").each( function() {
		$(this).attr("checked",status);
	})
  };

$(document).ready(function(){
	var arch_cnt = 0;
	var unarch_cnt = 0;
	var UTIL = belk.cars.util; //this is used for setting into and reading from the cookie.
	var valueOption = null; //This variable contains the filter option selected on dashboard.
	var cookieValue = UTIL.readCookie('cars_filter'); //This contains the filter value last selected by the user.
	var userTypeCd = '<c:out value="${userTypeCd}" />'; //This contains the type code of the logged in user
	var rowToRemember = null; //This contains the row number of the car that was last selected.
	
	//Condition if the logged in user is not  art_director and buyer then don't set the filter value in cookie.
	//Filter option is accessible only to art_director and buyer
	if(userTypeCd != 'ART_DIRECTOR' && userTypeCd != 'BUYER') {
		belk.cars.util.createCookie('cars_filter',null,365);
	} 
	//
	if(cookieValue === 'last_search' && userTypeCd != 'ART_DIRECTOR' || userTypeCd == 'BUYER') {
		valueOption = "last_search";
	}  
	//
	if(userTypeCd == 'ART_DIRECTOR' || userTypeCd == 'BUYER') {
		valueOption = UTIL.readCookie('cars_filter');
	} 
	if(valueOption === null || valueOption === 'null'){
		valueOption = "assigned_to_me";
	}
	if (valueOption === 'last_search' && (userTypeCd == 'ART_DIRECTOR' || userTypeCd == 'BUYER')) {
		belk.cars.util.createCookie('cars_filter',valueOption,365);
	}
	//Setting the filter
	$('#sel_listing_filter').val(valueOption);
	
	//Displaying the "loading..." message on dashboard screen
	$('#car_listing').html('').show();
	$('#car_listing').html('<br><br><h1><b><span font-size="180px">Loading...<span></b></h1>').show();
	
	//Load the CAR details on dashboard.
	$('#car_listing').load(context + 'getCars.html?ajax=true&filter=' + valueOption + '&a_id='+belk.cars.util.getAjaxId(), {}, com.dashboard.onCarsTableLoad);
	
	//-----Functionality to highlight the row on taking the mouse cursor on a row.
	$('body.admin table.dstable tr').live('mouseover',function(){
	  	$('td',this).addClass('trHover');
	});
	$('body.admin table.dstable tr').live('mouseout',function(){
	  	$('td',this).removeClass('trHover');
	});
	
	
	//-------Functionality to display the CAR preview section on selecting a particular row-----
	$('body.admin table.dstable tr').live('click',function(event){
		var senderElement = event.target;
	  	var CFG=belk.cars.config;
	  	var carId = $(this).find("td").eq(2).html();
	  	var elementName = senderElement.name;
	  	//Do not display the CAR preview section when a Lock or edit button is clicked or header row is clicked
		if(carId != null && senderElement.id != carId && elementName != "lockBtn") {
			$('#details_car').load(CFG.urls.car_preview + carId + '&a_id=' + belk.cars.util.getAjaxId());
		}
		
		if(rowToRemember != null) {
	  		$('td', rowToRemember).removeClass('trSelectedDash');
	  	}
	  	$('td',this).addClass('trSelectedDash');
	  	rowToRemember = $(this);
	});

	//--------Functionality to enable/disable archive/unarchive button on selecting the checkbox----	
	$('body.admin .chkbox').live('click',function(event){
		var style = $(this).parents("td").attr('style');
		var chk_status = $(this).attr('checked');
		var isArchived = (style.toLowerCase().match("color") == null) ? false : true;
		if(chk_status === true && isArchived)
			arch_cnt += 1;
		else if(chk_status === false && isArchived)
			arch_cnt -= 1;
		else if(chk_status === true && !isArchived)
			unarch_cnt += 1;
		else if(chk_status === false && !isArchived) 
			unarch_cnt -= 1;		
		if(arch_cnt >= 1 )
			$('#unarchive').removeAttr('disabled');
		else
			$('#unarchive').attr('disabled','disabled');
		if(unarch_cnt >= 1 )
			$('#archive').removeAttr('disabled');
		else
			$('#archive').attr('disabled','disabled');
		if(arch_cnt <1 && unarch_cnt <1){
			$('#unarchive').removeAttr('disabled');
			$('#archive').removeAttr('disabled');
			}
	});
	
		
	//----- Functionality to print multiple CARs---------	
	$('.print').live('click',function(event){
		var id = $(this).attr('id');
		var carsArray = new Array();
		arch_cnt = 0;
		unarch_cnt = 0;
		selectedCars = null;
		$(".chkbox").each( function() {
			var status = $(this).attr('checked');
			if(status === true) {
				if(selectedCars != null) {
					selectedCars = selectedCars + ',' + $(this).attr('value');
					
				} else {
					selectedCars =$(this).attr('value');
					
				}
			}
		});
		if(selectedCars != null) {
		  carsArray = selectedCars.split(","); 
		}
		if (carsArray.length == 0) {
			Ext.MessageBox.show({
						title: 'Error',
						msg: 'No CAR(S) selected for ' + id + ' operation!',
						buttons: Ext.MessageBox.OK,
						icon: 'ext-mb-error'
		});
		}else if (carsArray.length > 15) {
			Ext.MessageBox.show({
						title: 'Error',
						msg: 'You may print a maximum of 15 CARS. Please re-select CARS and try again.',
						buttons: Ext.MessageBox.OK,
						icon: 'ext-mb-error'
		}); 
		} else {
		       Ext.MessageBox.show ({
					    title: 'Confirm',
						msg: 'Would you like to print SKU Information?',
						buttons: { yes: 'Yes', no: 'No',cancel: 'Cancel Print' } ,
						fn: function(btn) {
							var url; 
						url= window.location.href.replace(window.location.pathname,'');
						    var protocol;
							protocol=window.location.protocol;
							if (protocol=='https:'){							
							url=url+"/printPreview.html";
							} else 
							{ url=url+"/cars/printPreview.html"; }						
                         if(btn === 'yes') {
						       $(".chkbox").each( function() {
							   var status = $(this).attr('checked');
							       if (status === true) {
							         $(this).removeAttr("checked");
							       }
							   }); 
							var printSku=true;	
							if ( $.browser.msie ) {
    								var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=250');
 								} else {
		 							var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=200');
 							}
							//var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=200');
							if (window.focus) {newwindow.focus()}
							return false;
							} else 
							if (btn === 'no') { 
				                $(".chkbox").each( function() {
										var status = $(this).attr('checked');
										   if(status === true) {
										      $(this).removeAttr("checked");
										   }
								}); 
								var printSku=false;	
								if ( $.browser.msie ) {
    								var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=250');
 								} else {
		 							var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=200');
 								}
								//var newwindow=window.open(url+'?carId='+carsArray+'&printSkuInfo='+printSku,'name','height=400,width=1037,left=169,top=200');
								if (window.focus) {newwindow.focus()}
								return false;
							}
					     },
   						icon: 'ext-mb-question'
		        });
			}
	});
	
	//----- Functionality to generate the popup if no car is selected----------	
	$('body.admin .buttons .btn').live('click',function(event){
		var id = $(this).attr('id');
		arch_cnt = 0;
		unarch_cnt = 0;
		selectedCars = null;
		$(".chkbox").each( function() {
			var status = $(this).attr('checked');
			if(status === true) {
				if(selectedCars != null) {
					selectedCars = selectedCars + ',' + $(this).attr('value');
					
				} else {
					selectedCars = 'ids=' + $(this).attr('value');
				}
			}
		});
		if(selectedCars === null) {
			  Ext.MessageBox.show({
					title: 'Error',
					msg: 'No CAR(S) selected for ' + id + ' operation!',
					buttons: Ext.MessageBox.OK,
					icon: 'ext-mb-error'
					});
		} else {
			$('#car_listing').html('<br><br><h1><b><span font-size="180px">Loading...<span></b></h1>').show();
			$('#car_listing').load(context + 'getCars.html?ajax=true&filter=' + valueOption + '&a_id='+belk.cars.util.getAjaxId() + '&oper=' + id, selectedCars, com.dashboard.onCarsTableLoad);
			$('#details_car').html('<br><br><h1><b><span font-size="180px">No CAR Selected<span></b></h1>').show();
		}
	});
	
	$('.edit_car').live('click',function(event){
	
		$this = $(this);
		var CFG=belk.cars.config;
		var carId = $this.attr('id');
		
		$(location).attr('href',CFG.urls.car_edit + carId);
	});
	
	//----Functionality of Source link-------------
	$('body.admin table.dstable tr a.carsrc').live('click',function(){
		var CFG=belk.cars.config;
		var carId = $(this).attr('id');
		var str = CFG.urls.car_preview + carId + '&a_id=' + belk.cars.util.getAjaxId();
			
		$('#details_car').load(CFG.urls.car_preview + carId + '&a_id=' + belk.cars.util.getAjaxId());
	});
	


	//---------------Functionality of set button----------------------------
	$('body.admin table.dstable tr a.set_prod_type').live('click',function(){
		var CFG=belk.cars.config;
		var gen_id=Ext.id(); // a generated ID for the div
		var car_id=$(this).attr('id');
		var $this=$(this).blur();
			
		$('body').append('<div id="'+gen_id+'" class="set_prod_type_modal"></div>');
			
		var win = new Ext.Window({
									layout:'fit',
									width:300,
									autoHeight:true,
									plain:true,
									title:'Set Product Type for CAR (id = '+car_id+')',
									modal:true,
									items: new Ext.Panel({
	                										contentEl:gen_id,
	                										border:false,
															autoHeight:true
	            										}),
									buttons: [{
		                						text: 'Cancel',
		                						handler: function(){
		                    								win.hide();
		                								 }
	            							  },{
		                						text: 'Submit',
		                						handler: function(){
															var val=$('#'+gen_id+' select').val();
		                    								$.getJSON(CFG.urls.set_prod_type+car_id+'&productType='+val+'&a_id='+belk.cars.util.getAjaxId(),function(response){
																if(response&&response.success){
																	$this.text('Edit');
																	$this.removeClass('set_prod_type btn');
																	$this.addClass('edit_car btn');
																	$this.attr('title','Edit CAR');
																	$this.parents('tr').find("td").eq(13).append('<a class="menu btn" style="font-size:11px;font-weight: 700px;" href="#" id="' + response.userTypeCd + '" name="' + response.contentStatus + '">+</a>');
																	win.hide();
																} else {
																	Ext.MessageBox.show({
							           														title:'Error',
							           														msg:'Oops. There was an error setting the Product Type. Please try again.',
							           														buttons: Ext.MessageBox.OK,
							           														icon: 'ext-mb-error'
							       														});
											  					}
											  				});
											  	}
											}]
								});
			
		// set the window position and show it
		var extThis=Ext.get(this);
		win.setPosition(extThis.getLeft()-350,extThis.getTop());
	    win.show();
			
		$('#'+gen_id).html('<div class="loading"></div>').show();
			
		// get the product types and create the dropdown
		$.get(CFG.urls.get_prod_types + car_id +'&a_id='+belk.cars.util.getAjaxId(),function(response){
				var html=null;
				response=eval("("+response+")");
				if(response&&response.product_type_data){
					var dd=['<select name="product_types">'];
					dd.push(belk.cars.common.getProductTypeDDOptions(response.product_type_data));
					dd.push('</select>');
					html=dd.join('');
				}
				else{
					html="<strong>Error!</strong><br/>Please associate a [Product Type] with [Class] for the [Car]."
				}
				$('#'+gen_id).html(html);
				win.syncSize();
			});
			return false;
	});
	
	//-------------Functionality of plus button----------------------
	$('body.admin table.dstable tr a.menu').live('click',function(ev){
		var DASH=belk.cars.contentstatus;
		var $this=$(this).blur();
		var car_id=$this.parents('tr').find("td").eq(2).html();
		var status=$this.attr('name');
		var privs=DASH.carUserContentPrivileges;
		var car_user_type=$this.attr('id');
		
	    var menu = new Ext.menu.Menu({
	    			defaultAlign:'tr-tl?'
	    		});
	    var isCarReady = $this.attr('style').indexOf('gray') == -1;		
		var l=privs.length;
		var itemAdded=false;
		
		for(var i=0;i<l;i++){
				if(privs[i].car_user_type===car_user_type && privs[i].content_status===status && isCarReady){
					(function(){
						var privilege=privs[i];
						menu.add(
							{
								text:privs[i].action,
								handler:function(){
									$.getJSON(privilege.url+car_id,function(response){
										if(response.success){
											$this.attr('name', privilege.url);
											$this.attr('disabled','true');
											$this.css('color','gray');
											$this.css('background','#CCCCCC');
											
										}
										else{
											Ext.MessageBox.show({
									           title:'Error',
									           msg:'Oops. There was an error updating the content status. Please try again.',
									           buttons: Ext.MessageBox.OK,
									           icon: 'ext-mb-error'
									       	});
										}
									});
								}
							}
						);
					})();
					itemAdded=true;
				}
			}
			if(!itemAdded){
				var item=menu.add({
					text:'Nothing to do...'
				});
				item.disable();
			}
	    	menu.show(this);
	    	ev.preventDefault();
	    }).each(function(){
			var $this=$(this);
			var car_id=$this.parents('tr').find("td").eq(2).html();
			var status=$this.attr('name');
			var privs=DASH.carUserContentPrivileges;
			var car_user_type=$this.attr('id');
			var l=privs.length;
			var itemFound=false;
			for(var i=0;i<l;i++){
				if(privs[i].car_user_type===car_user_type && privs[i].content_status===status){
					itemFound=true;
					if (privs[i].content_status === 'SENT_TO_CMP') {
						$this.css({
							'background': '#111765',
							'color':'#FFFFFF'
						});
					}
				}
			}
			if(!itemFound){
				$this.css({
					'background': '#dddddd',
					'color':'#777777'
				});
			}	
	});

	//---------------Functionality of filter-------------------------
	$('#sel_listing_filter').keyup(function(){
		valueOption=$('#sel_listing_filter').val();
		if (valueOption != '') {
			belk.cars.util.createCookie('cars_filter',valueOption,365);
			$('#car_listing').html('<br><br><h1><b><span font-size="180px">Loading...<span></b></h1>').show();
			$('#car_listing').load(context + 'getCars.html?ajax=true&filter=' + valueOption + '&a_id='+belk.cars.util.getAjaxId(), {}, com.dashboard.onCarsTableLoad);
			$('#details_car').html('<br><br><h1><b><span font-size="180px">No CAR Selected<span></b></h1>').show();
		} 
	});
	$("#sel_listing_filter").attr("onChange","$(this).keyup()"); //$(this).keyup($(this).val()
});
</script>
]]>
</content>

