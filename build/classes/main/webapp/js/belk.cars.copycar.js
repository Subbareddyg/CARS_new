/*
 * belk.cars.copycar.js
 * Belk CARS - Copy Car javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.copycar={
	getGridColumns:function(){
		// define the columns for the CARS grid
		return [
			{id:"car_id",header:"ID",width:60,sortable:true,dataIndex:'car_id'},
			{id:"source",header:"Source",/*width:85,*/sortable:true,dataIndex:'source'},
			{id:"dept_num",header:"Dept #",/*width:60,*/sortable:true,dataIndex:'dept_num'},
			{id:"vendor",header:"Vendor",sortable:true,dataIndex:'vendor'},
			{id:"vendor_style",header:"Vendor Style",width:85,sortable:true,dataIndex:'vendor_style'},
			{id:"preview",header:"",width:50,renderer:belk.cars.copycar.renderers.renderPreview,dataIndex:'link',align:"center",sortable:false},
			{id:"link",header:"",width:50,renderer:belk.cars.copycar.renderers.renderLink,dataIndex:'link',align:"center",sortable:false}
		];
	},
	grid:null,
	store:null,
	preview_win:null,
	init:function(){
		var CC=belk.cars.copycar;
		CC._setupPanels();
		CC._setupGrid();
		CC._setupSearch();
		CC._setupPreview();
	},
	renderers:{
		renderLink:function(val,meta,record,rowIndex,colIndex,store){
			var CFG=belk.cars.config;
			var UTIL=belk.cars.util;
			return '<a href="'+CFG.urls.copy_car+'copyToCarId='+UTIL.getQParam('carId')+'&copyFromCarId='+record.get('car_id')+'&copyCar=true" class="btn">Copy</a>';
	    },
	    renderPreview:function(val,meta,record,rowIndex,colIndex,store){
	    	var CFG=belk.cars.config;
	    	return '<a href="'+CFG.urls.car_preview+record.get('car_id')+'" class="preview btn">Preview</a>';
	    }
	},
	// private methods
	_setupPanels:function(){
		var panelInfo=[
			['Copy To CAR Info','car_info_pnl','car_info_content']
		];
		// panels
		var l=panelInfo.length;
		for(var i=0;i<l;i++){
			new Ext.Panel({
		        title:panelInfo[i][0],
		        collapsible:true,
				frame:true,
		        applyTo:panelInfo[i][1],
				contentEl:panelInfo[i][2],
				height:'auto'
		    });
		}
	},
	_setupGrid:function(){
		var CFG=belk.cars.config;
		var CC=belk.cars.copycar;
		// create and load the data store of car data
	    CC.store = new Ext.data.SimpleStore({
	        fields: belk.cars.data.car_fields
	    });
		if(belk.cars.data.car_data){
			CC.store.loadData(belk.cars.data.car_data);
			$('#grid_msg').html(belk.cars.data.car_data.length+' CARs found.');
		}
		
		var tb = new Ext.Toolbar();
		// create and render the car grid
		CC.grid = new Ext.grid.GridPanel({
		    store: CC.store,
		    columns: CC.getGridColumns(),
			enableColumnHide:false,
			enableColumnMove:false,
			title:'Copy From CAR Listing',
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
		    tbar:tb,
		    iconCls:'icon-grid'
		});
		//TODO: declare form in jsp then shove it in here. also have class id hidden and prefilled.
		tb.add($('#div_search_form').remove().show()[0]);
		CC.grid.render('cars_container');
		CC.grid.on({
	 		'sortchange' : function(){
	 			CC._setupPreview();
	 		}
	 	});
	 	
	 	new Ext.form.DateField({
			applyTo:'dueDateFrom',
	        fieldLabel:'Due Date From',
	        allowBlank:true,
			format:CFG.dateFormat,
			altFormats:CFG.dateAltFormats
	    });
		new Ext.form.DateField({
			applyTo:'dueDateTo',
	        fieldLabel:'Due Date To',
	        allowBlank:true,
			format:CFG.dateFormat,
			altFormats:CFG.dateAltFormats
	    });
	},
	_setupPreview:function(){
		var CC=belk.cars.copycar;
		$('a.preview').click(function(){
			$(this).blur();
			$('#preview_content').html('<div class="loading"></div>');
			if (!CC.preview_win) {
				CC.preview_win = new Ext.Window({
					el:'preview_container',
					layout:'fit',
					width:975,
					autoHeight:true,
					autoScroll:true,
					closeAction:'hide',
					modal:true,
					plain:true,
					items: new Ext.Panel({
	                    contentEl:'preview_content',
	                    deferredRender:false,
	                    border:false,
						autoHeight:true
	                })
	            });
	        }
	        CC.preview_win.show();
	        $('#preview_content').load($(this).attr('href'),function(){
				CC.preview_win.center();
			});
	        return false;
	    });
	},
	_setupSearch:function(){
		var CC=belk.cars.copycar;
		$('#searchForm').ajaxForm({
			dataType: 'json',
			beforeSubmit:function(){
				CC.store.removeAll();
				$('#search_loading').show();
			},
			success: function(resp){
				if (resp.car_data) {
					CC.store.loadData(resp.car_data);
					setTimeout(function(){
						CC._setupPreview();
					},500);
					$('#grid_msg').html(resp.car_data.length+' CARs found.')
				}
				else{
					$('#grid_msg').html('No CARs found.');
				}
				$('#search_loading').hide();
			}
		});
	}
};

//dom is ready...
$(document).ready(belk.cars.copycar.init);