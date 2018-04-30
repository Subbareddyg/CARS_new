/*
 * belk.cars.dashboard.js
 * Belk CARS - Dashboard javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.dashboard={
	getGridColumns:function(){
		// define the columns for the CARS grid
		return [
			{id:"flag",header:"Flag",width:40,sortable:true,renderer:belk.cars.dashboard.renderers.renderFlag,dataIndex:'flag'},
			{id:"car_id",header:"ID",width:75,sortable:true,renderer:belk.cars.dashboard.renderers.renderID,dataIndex:'car_id'},
			{id:"source",header:"Source",width:85,sortable:true,renderer:belk.cars.dashboard.renderers.renderSource,dataIndex:'source'},
			{id:"dept_num",header:"Dept #",width:60,sortable:true,dataIndex:'dept_num'},
			{id:"vendor",header:"Vendor",sortable:true,dataIndex:'vendor'},
			{id:"vendor_style",header:"Vendor Style",width:85,sortable:true,dataIndex:'vendor_style'},
			{id:"source_type_cd",header:"Request Type",width:60,sortable:true,dataIndex:'source_type_cd'},
			{id:"status",header:"Status",sortable:true,dataIndex:'status'},
			{id:"assigned_to_user_type",header:"Assigned To",sortable:true,dataIndex:'assigned_to_user_type'},
			{id:"due_date",header:"Status Due",sortable:true,renderer:Ext.util.Format.dateRenderer('m/d/Y'),dataIndex:'due_date'},
			{id:"completion_date",header:"Completion Date",sortable:true,renderer:Ext.util.Format.dateRenderer('m/d/Y'),dataIndex:'completion_date'},
			{id:"last_updated_by",header:"Last Updated By",sortable:true,renderer:belk.cars.dashboard.renderers.renderLastUpdatedBy,dataIndex:'last_updated_by'},
			{id:"link",header:"",width:80,renderer:belk.cars.dashboard.renderers.renderLink,dataIndex:'link',align:"center",sortable:false}
		];
	},
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
	],
	carListingFilters:[
		{
			value:'assigned_to_me',
			descr:'Assigned To Me'
		},
		{
			value:'over_due',
			descr:'Over Due'
		},
		{
			value:'copy_incomplete',
			descr:'Copy Incomplete'
		},
		{
			value:'created_today',
			descr:'Created Today'
		},
		{
			value:'no_updates',
			descr:'No Updates (last 10 days)'
		},
		{
			value:'requested_images',
			descr:'Has Requested Images'
		},
		{
			value:'recieved_images',
			descr:'Has Recieved Images'
		},
		{
			value:'last_search',
			descr:'Last Search'
		},
		{
			value:'view_all',
			descr:'View All'
		}
	],
	filterCookieName:'cars_filter',
	//previewPanel:null,
	grid:null,
	store:null,
	leaving:false,
	once:false,
	init:function(){
		
		Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		
		var DASH=belk.cars.dashboard;
		$('#main').append('<div id="loading_cars" style="position:absolute;top:100px;left:60px;font-size:16px;font-weight:bold;">Loading...</div>').css('opacity',0.5);
		//belk.cars.data=resp;
		DASH._setupGrid();
		
		var loc = window.location.href;
		DASH._setupCarListingData( loc.indexOf('searchresult')<0 );
		DASH._setupFilterChange();
		DASH._setupRowSelection();
		DASH._setupSortChange();
	},
	renderers:{
		// column value renderers
		renderFlag:function(val,meta,record,rowIndex,colIndex,store){
			var car_id=record.get('car_id'),img=null,title=null,cssClass=null;
			var canChangeFlag=record.get('can_do_urgent') || false;
			var btag,etag;
			if(canChangeFlag&&canChangeFlag==="Y"){
				btag='<a href="?car='+car_id+'" ';
				etag='</a>'
			}
			else{
				btag='<span ';
				etag='</span>'
			}
			var html;
			if(val=='urg'){
				html = btag + 'title="urgent" class="unset_flag"><img src="'+context+'images/blueFlag.gif" alt="urgent" />' + etag;
			}
			else{
				if(canChangeFlag){
					html=btag + 'title="click to set flag" class="set_flag">&nbsp;' + etag;
				}
				else{
					html='';
				}
			}
			return html;
		},
		renderID:function(val,meta,record,rowIndex,colIndex,store){
			var html;
			if(record.get('vendor_style_type')==='PRODUCT'){
				html='<span class="car_id">'+val+'</span>';
			}
			else{
				html='<span class="car_id pattern">'+val+'</span>';
			}
			return html;
		},
	    renderSource:function(val,meta,record,rowIndex,colIndex,store){
			return '<tpl for="."><a href="#" ext:qtip="Source Type: ' + record.get('source_type') + '<br/>Shipping Date: '+Ext.util.Format.date(record.get('shipping_date'))+'">'+val+'</a></tpl>';
	    },
		renderLink:function(val,meta,record,rowIndex,colIndex,store){
			var CFG=belk.cars.config;
			var tmp;
			if (record.get('owned_by') === belk.cars.data.car_user) {
				if (val === 'edit') {
					tmp = '<a class="edit_car btn" href="' + CFG.urls.car_edit + record.get('car_id') + '" title="Edit CAR">Edit</a> <a class="menu btn" href="#/?car_id='+record.get('car_id')+'">+</a>';
				}
				else if (val === 'set') {
					tmp = '<a class="set_prod_type btn" href="' + CFG.urls.get_prod_types + record.get('car_id') + '" title="Set Product Type">Set</a>';
				}
			}
			return tmp;
	    },
		renderLastUpdatedBy:function(val,meta,record,rowIndex,colIndex,store){
			return '<tpl for="."><span ext:qtip="Last Updated Date: '+Ext.util.Format.date(record.get('last_updated_date'))+'">'+val+'</span></tpl>';
		}
	},
	//private methods
	_setupGrid:function(){
		var DASH=belk.cars.dashboard;
		// create and load the data store of car data
	    DASH.store = new Ext.data.SimpleStore({
	        fields: belk.cars.data.car_fields
	    });
		
		// create and render the car grid
		DASH.grid = new Ext.grid.GridPanel({
		    store: DASH.store,
		    columns: DASH.getGridColumns(),
			enableColumnHide:false,
			enableColumnMove:false,
			//title:'<a class="btn filter" style="font-weight:bold;float:right;font-size:15px;margin-right:10px;padding:0pt 2px;" href="#">+</a>CAR Listing',
			title:'CAR LISTING ' + DASH._getFilterHTML(),
			collapsible:true,
	        applyTo:'cars_container',
		    viewConfig: {
		        forceFit: true
		    },
		    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
		    height:300,
			width:950,
			stripeRows:true,
		    frame:true,
		    iconCls:'icon-grid'
		});
		DASH.grid.render('cars_container');
	},
	_setupCarListingData:function(doAjax){
		$('#loading_cars').show();
		$('#main').css('opacity',0.5);
		var DASH=belk.cars.dashboard;
		var loadData=function(data){
			DASH.store.loadData(data);
			if (!DASH.once) {
				$('#ft').append('grid:' + ((new Date()).getTime() - sTime.getTime()) + 'ms ');
				DASH.once=true;
			}
			$('#loading_cars').hide();
			$('#main').css('opacity', 1);
			
			DASH._setupRowEvents();
		};
		if (doAjax) {
			var filter=$('#sel_listing_filter').val();
			if(!filter || filter==''){
				filter='view_all';
			}
			$.getJSON(context + 'getCars.html?ajax=true&filter='+filter+'&a_id='+belk.cars.util.getAjaxId(), function(resp){
				if (!resp.car_data) {
					DASH.store.removeAll();
					$('#ph_message').html('<strong>NO CARS FOUND.</strong>');
					$('#loading_cars').hide();
					$('#main').css('opacity', 1);
				}
				else {
					loadData(resp.car_data);
					$('#ph_message').html('');
				}
			});
		}
		else{
			if (belk.cars.data && belk.cars.data.car_data) {
				loadData(belk.cars.data.car_data);
			}
			else{
				$('#loading_cars').hide();
				$('#main').css('opacity', 1);
			}
			$('#sel_listing_filter').val('last_search');
			belk.cars.util.createCookie(DASH.filterCookieName,'last_search',365);
		}
	},
	_getFilterHTML:function(){
		var returnVal='';
		if (belk.cars.data.car_user_type === 'ART_DIRECTOR' || belk.cars.data.car_user_type === 'BUYER' ) {
			var UTIL = belk.cars.util;
			var DASH = belk.cars.dashboard;
			var filters = DASH.carListingFilters;
			var val = UTIL.readCookie(DASH.filterCookieName);
			if (val === null) {
				val = "assigned_to_me";
			}
			var html = ['<select id="sel_listing_filter" style="margin-left:25px;font-weight:normal;font-size:11px;">'];
			html.push('<option value="">&nbsp;</option>');
			var l = filters.length;
			var isSearch = window.location.href.indexOf('searchresult') >= 0;
			for (var i = 0; i < l; i++) {
				html.push('<option value="' + filters[i].value + '" ');
				if (val === filters[i].value && !isSearch) {
					html.push('selected="selected"');
				}
				html.push('>' + filters[i].descr + '</option>');
			}
			html.push('</select>');
			returnVal=html.join('');
		}
		return returnVal;
	},
	_setupFilterChange:function(){
		var DASH=belk.cars.dashboard;
		$('#sel_listing_filter').change(function(){
			var val=$(this).val();
			if (val !== '') {
				belk.cars.util.createCookie(DASH.filterCookieName,val,365);
				DASH._setupCarListingData(true);
			}
		});
	},
	_setupRowSelection:function(){
		var CFG=belk.cars.config;
		var DASH=belk.cars.dashboard;
		var lastPrev=null;
		// subscribe to row selection event for showing the CAR Preview
		DASH.grid.getSelectionModel().on({
	 		'selectionchange' : function(sm, node){
				setTimeout(function(){
					if(sm.last!==lastPrev && !belk.cars.dashboard.leaving){
						lastPrev=sm.last;
						var lastItem=DASH.store.data.items[sm.last];
						if (lastItem) {
							var $prev = $('#preview_content').css('opacity', 0.75).append('<div class="loading"></div>').load(CFG.urls.car_preview + lastItem.get('car_id') + '&a_id=' + belk.cars.util.getAjaxId() + ' div.ajaxContent', function(){
								if ($('#preview_content div.ajaxContent').length < 1) {
									//session timeout or error - reload the page in the browser
									window.location.href = window.location.href;
								}
								else {
									//DASH.previewPanel.setTitle('CAR Preview (ID = ' + DASH.store.data.items[sm.last].get('car_id') + ')');
									$(this).css('opacity', 1);
								}
							});
						}
						else{
							$('#preview_content div.ajaxContent').html('No CAR Selected');
						}
					}
				},250);
	 		}
		});
	},
	_setupSortChange:function(){
		var DASH=belk.cars.dashboard;
		DASH.grid.on({
			'sortchange':function(that,sortInfo){
				DASH._setupRowEvents();
			}
		});
	},
	_setupRowEvents:function(){
		var DASH=belk.cars.dashboard;
	
	
		DASH._setupSetButtons();
		DASH._setupSetFlag();
		DASH._setupUnsetFlag();
		DASH._setupContextMenu();
		
		$('a.edit_car').click(function(ev){
			belk.cars.dashboard.leaving=true;
	    });
	},
	
	_setupSetButtons:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var DASH=belk.cars.dashboard;
		// setup the "set product type" buttons
		$('#cars_container a.set_prod_type').unbind().click(function(){
			var gen_id=Ext.id(); // a generated ID for the div
			var car_id=UTIL.getQParam('car',$(this).attr('href'));
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
									var index=DASH.store.find('car_id',car_id);
									var record=DASH.store.getAt(index);
									record.beginEdit();
									record.set('link','edit');
									record.endEdit();
									DASH._setupRowEvents();
									win.hide();
								}
								else{
									Ext.MessageBox.show({
							           title:'Error',
							           msg:'Oops. There was an error setting the Product Type. Please try again.',
							           buttons: Ext.MessageBox.OK,
							           icon: 'ext-mb-error'
							       	});
								}
							});
		                }
					}
				]
			});
			
			// set the window position and show it
			var extThis=Ext.get(this);
			win.setPosition(extThis.getLeft()-350,extThis.getTop());
	        win.show();
			
			$('#'+gen_id).html('<div class="loading"></div>').show();
			
			// get the product types and create the dropdown
			$.get($this.attr('href')+'&a_id='+belk.cars.util.getAjaxId(),function(response){
				var html=null;
				response=eval("("+response+")");
				if(response&&response.product_type_data){
					var dd=['<select name="product_types">'];
					/*
					var data=response.product_type_data
					var length=data.length;
					for(var i=0;i<length;i++){
						dd.push('<option value="',data[i][2],'">',data[i][0],'</option>');
					}
					*/
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
	},
	_setupSetFlag:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var DASH=belk.cars.dashboard;
		// setup the set flag functionality
		var setflagHandler=function(){
			var $this=$(this).blur();
			var gen_id=Ext.id(); // a generated ID for the div
			var car_id=UTIL.getQParam('car','foo'+$(this).attr('href'));
			$('body').append('<div id="'+gen_id+'" class="set_flag_modal"></div>');
			var win = new Ext.Window({
				layout:'fit',
				width:180,
				autoHeight:true,
				plain:true,
				title:'Set Flag for CAR (id = '+car_id+')',
				modal:true,
				items: new Ext.Panel({
	                contentEl:gen_id,
	                border:false,
					autoHeight:true
	            })
			});
			
			// set the window position and show it
			var extThis=Ext.get(this);
			win.setPosition(extThis.getLeft()+50,extThis.getTop());
	        win.show();
			var $tmp=$('#'+gen_id).html('<a href="#" class="set_urgent btn">Set Urgent Flag</a>').show();
			$('a.set_urgent',$tmp).click(function(){
				$.getJSON(CFG.urls.set_urgent+car_id+'&a_id='+belk.cars.util.getAjaxId(),function(response){
					if(response&&response.success){
						var index=DASH.store.find('car_id',car_id);
						var record=DASH.store.getAt(index);
						record.beginEdit();
						record.set('flag','urg');
						record.endEdit();
						DASH._setupRowEvents();
						win.hide();
					}
					else{
						Ext.MessageBox.show({
				           title:'Error',
				           msg:'Oops. There was an error setting the flag. Please try again.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					}
				});
			});
			win.syncSize();
			return false;
		};
		$('#cars_container a.set_flag').unbind().click(setflagHandler);
	},
	_setupUnsetFlag:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var DASH=belk.cars.dashboard;
		// setup the unset flag functionality
		var unsetflagHandler=function(){
			var $this=$(this).blur();
			var gen_id=Ext.id(); // a generated ID for the div
			var car_id=UTIL.getQParam('car','foo'+$(this).attr('href'));
			$('body').append('<div id="'+gen_id+'" class="set_flag_modal"></div>');
			var win = new Ext.Window({
				layout:'fit',
				width:210,
				autoHeight:true,
				plain:true,
				title:'Remove Flag for CAR (id = '+car_id+')',
				modal:true,
				items: new Ext.Panel({
	                contentEl:gen_id,
	                border:false,
					autoHeight:true
	            })
			});
			
			// set the window position and show it
			var extThis=Ext.get(this);
			win.setPosition(extThis.getLeft()+50,extThis.getTop());
	        win.show();
			var $tmp=$('#'+gen_id).html('<a href="#" class="unset_urgent btn">Remove Urgent Flag</a>').show();
			$('a.unset_urgent',$tmp).click(function(){
				$.getJSON(CFG.urls.set_not_urgent+car_id+'&a_id='+belk.cars.util.getAjaxId(),function(response){
					if(response.success){
						var index=DASH.store.find('car_id',car_id);
						var record=DASH.store.getAt(index);
						record.beginEdit();
						record.set('flag','');
						record.endEdit();
						DASH._setupRowEvents();
						win.hide();
					}
					else{
						Ext.MessageBox.show({
				           title:'Error',
				           msg:'Oops. There was an error setting the flag. Please try again.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					}
				});
			});
			win.syncSize();
			return false;
		};
		$('#cars_container a.unset_flag').unbind().click(unsetflagHandler);
	},
	_setupContextMenu:function(){
		var UTIL=belk.cars.util;
		var DASH=belk.cars.dashboard;
		$('a.menu').unbind().click(function(ev){
	    	var $this=$(this).blur();
			var car_id=UTIL.getQParam('car_id',$this.attr('href'));
			var record=DASH.store.getAt(DASH.store.find('car_id',car_id));
			var status=record.get('content_status');
			var privs=DASH.carUserContentPrivileges;
			var car_user_type=belk.cars.data.car_user_type;
	    	var menu=new Ext.menu.Menu({
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
											record.beginEdit();
											record.set('content_status',UTIL.getQParam('status',privilege.url));
											record.endEdit();
											DASH._setupRowEvents();
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
			var car_id=UTIL.getQParam('car_id',$this.attr('href'));
			var record=DASH.store.getAt(DASH.store.find('car_id',car_id));
			var status=record.get('content_status');
			var privs=DASH.carUserContentPrivileges;
			var car_user_type=belk.cars.data.car_user_type;
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
	}
};

// dom ready...
$(document).ready(belk.cars.dashboard.init);