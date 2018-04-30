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
//-------------------------------------------------------------------------------------------
$(document).ready(function(){

	var UTIL = belk.cars.util; //this is used for setting into and reading from the cookie.
	var valueOption = null; //This variable contains the filter option selected on dashboard.
	var cookieValue = UTIL.readCookie('cars_filter'); //This contains the filter value last selected by the user.
	var userTypeCd = '<c:out value="${userTypeCd}" />'; //This contains the type code of the logged in user
	var rowToRemember = null; //This contains the row number of the car that was last selected.
	
	if(userTypeCd == 'BUYER' || userTypeCd == 'ART_DIRECTOR' || cookieValue === 'last_search') {
		valueOption = UTIL.readCookie('cars_filter');
	} 
	if(valueOption === null || valueOption === 'null'){
		valueOption = "assigned_to_me";
	}
	if (valueOption === 'last_search' && (userTypeCd == 'BUYER' || userTypeCd == 'ART_DIRECTOR')) {
		belk.cars.util.createCookie('cars_filter',valueOption,365);
	}
	//Setting the filter
	$('#sel_listing_filter').val(valueOption);
	
	//Displaying the "loading..." message on dashboard screen
	$('#car_listing').html('').show();
	$('#car_listing').html('<br><br><h1><b><span font-size="180px">Loading...<span></b></h1>').show();
	
	//Load the CAR details on dashboard.
	$('#car_listing').load(context + 'getCars.html?ajax=true&filter=' + valueOption + '&a_id='+belk.cars.util.getAjaxId(), {}, com.dashboard.onCarsTableLoad);
	
	//Condition if the logged in user is not a BUYER or art_director then don't set the filter value in cookie.
	//Filter option is accessible only to BUYER and art_director
	if(userTypeCd != 'BUYER' && userTypeCd != 'ART_DIRECTOR' && cookieValue === 'last_search') {
		belk.cars.util.createCookie('cars_filter',null,365);
	} 
	
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
	  	var carId = $(this).find("td").eq(1).html();
	  	var elementName = senderElement.name;
	  	//Do not display the CAR preview section when a Lock or edit button is clicked
		if(senderElement.id != carId || elementName === "lockBtn") {
			$('#details_car').load(CFG.urls.car_preview + carId + '&a_id=' + belk.cars.util.getAjaxId());
		}
		if(rowToRemember != null) {
	  		$('td', rowToRemember).removeClass('trSelectedDash');
	  	}
	  	$('td',this).addClass('trSelectedDash');
	  	rowToRemember = $(this);
	});
	
	//----Functionality of edit button------------------
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
	
	//-------Functionality of unlock button------------
	$('body.admin table.dstable tr input.unlock').live('click',function(){
		var $this=$(this);
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var carId = $(this).attr('id');
		var str = CFG.urls.unlock_car + carId;
		
		$.getJSON(str + '&a_id=' + UTIL.getAjaxId(),function(response){
				if (response.success){
					$this.val('Lock');
					$this.removeClass('unlock btn');
					$this.addClass('lock btn');
					$this.attr('title','Lock CAR for edit');
					
					$this.parents('tr').find("td").eq(12).before('<td></td>').remove();//Edit button
					$this.parents('tr').find("td").eq(13).before('<td></td>').remove();//Set button
					
					$this.parents('tr').find("td").eq(12).addClass('trSelectedDash');
					$this.parents('tr').find("td").eq(13).addClass('trSelectedDash');
					//$this.parents('tr').find("td").eq(11).html(response.userUpdate); 
				}
				else{
					Ext.MessageBox.show({
			           title:'Error',
			           msg:'Oops. There was an error unlocking the CAR. Please try again.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				}
			});
		
	});
	
	//-----------Functionality of unlock button---------------------
	$('body.admin table.dstable tr input.lock').live('click',function(){
		var $this=$(this);
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var carId = $(this).attr('id');
		var str = CFG.urls.lock_car + carId;
		$.getJSON(str + '&a_id=' + UTIL.getAjaxId(),function(response){
				if (response.success){
					$this.val('Unlock');
					$this.removeClass('lock btn');
					$this.addClass('unlock btn');
					$this.attr('title','Locked By:' + response.userUpdate);
					//$this.parents('tr').find("td").eq(11).html(response.userUpdate);
					
					$this.parents('tr').find("td").eq(12).before('<td></td>').remove();//Edit button
					$this.parents('tr').find("td").eq(13).before('<td></td>').remove();//Set button
					
					if(response.setEdit === 'edit') {
						$this.parents('tr').find("td").eq(12).html('<input type="button" class="edit_car btn" style="font-size:11px;font-weight: 700px;" title="Edit CAR" id="' + response.carId + '" value="Edit"/>');
						$this.parents('tr').find("td").eq(13).html('<a class="menu btn" style="font-size:11px;font-weight: 700px;" href="#" id="' + response.userTypeCd + '" name="' + response.contentStatus + '">+</a>');
					}
					if(response.setEdit === 'set') {
						$this.parents('tr').find("td").eq(12).append('<a class="set_prod_type btn" style="font-size:11px;" href="#" title="Set Product Type" id="' + response.carId + '">Set</a>');
					}
					$this.parents('tr').find("td").eq(12).addClass('trSelectedDash');
					$this.parents('tr').find("td").eq(13).addClass('trSelectedDash');
				}
				else{
					Ext.MessageBox.show({
			           title:'Error',
			           msg:'Oops. There was an error unlocking the CAR. Please try again.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				}
			});
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
																	$this.parents('tr').find("td").eq(14).append('<a class="menu btn" style="font-size:11px;" href="#" id="' + response.userTypeCd + '" name="' + response.contentStatus + '">+</a>');
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
		var car_id=$this.parents('tr').find("td").eq(1).html();
		var status=$this.attr('name');
		var privs=DASH.carUserContentPrivileges;
		var car_user_type=$this.attr('id');
		
	    var menu = new Ext.menu.Menu({
	    			defaultAlign:'tr-tl?'
	    		});
	    		
		var l=privs.length;
		var itemAdded=false;
		
		for(var i=0;i<l;i++){
				if(privs[i].car_user_type===car_user_type && privs[i].content_status===status){
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
			var car_id=$this.parents('tr').find("td").eq(1).html();
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