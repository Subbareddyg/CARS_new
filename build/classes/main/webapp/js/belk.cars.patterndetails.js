/*
 * belk.cars.patterndetails.js
 * Belk CARS - Pattern Details javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.patterndetails={	
	init:function(){
		var PD=belk.cars.patterndetails;
		PD._setupAddVendorStyle();
		PD._setupValidation();
		PD._setupRemoveVendorStyle();
		
	},
	// private methods
	_setupAddVendorStyle:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var autocompletes={};
		var win,gen_id;

		var saveHandler=function(){
			var saveButton=win.buttons[1];
			// validation
			$('input.error,textarea.error,select.error').removeClass('error');
			var missing=false;
			$('input.required,select.required,textarea.required').each(function(){
				var $this=$(this);
				var val=$this.val();
				if(val===null || val===''){
					missing=true;
					$this.addClass('error');
				}
			});
			if(missing){
				Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please fill in all the required fields.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
				return false;
			}

			saveButton.disable();
			// request image using ajax submit
			$('#'+gen_id+' form').ajaxSubmit({
				success: function(resp){
					var $resp=$(resp);
					if(!$resp.is('.ajaxContent')){
						Ext.MessageBox.show({
				           title: 'Error',
				           msg: 'Oops there was an error! Please verify your input and try again.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return;
					}
					$('#vendor_styles_wrap').html(resp);
					
					belk.cars.patterndetails._setupRemoveVendorStyle();
					
					$('#add_vendor_style_msg')
						.html('Vendor style ' + $('#txt_vendor_style_num').val() + ' added successfully!')
						.fadeIn(function(){
							var $this=$(this);
							setTimeout(function(){
								$this.fadeOut("slow");
							},3000);
						});
					$('#txt_vendor_style_num').val('');
					saveButton.enable();
				}
			});
		};
		
		$('a.btn_add_vendor_style').click(function(){
			var $this=$(this).blur();
			gen_id=Ext.id(); // a generated ID for the div
			$('body').append('<div id="'+gen_id+'" class="add_vendor_style_modal x-hidden"><div class="loading"></div></div>');
			win=new Ext.Window({
				layout:'fit',
				width:450,
				autoHeight:true,
				title:'Add Vendor Style',
				modal:true,
				plain:true,
				items: new Ext.Panel({
	                contentEl:gen_id,
	                border:false,
					autoHeight:true
	            }),
				buttons: [{
					text: 'Close',
					handler: function(){
						win.hide();
						setTimeout(function(){
							$('#'+gen_id).remove();
						},2000);
					}
				}, {
					text: 'Save',
					handler: saveHandler
				}]
			});
	        win.show();

			$('#'+gen_id).load($(this).attr('href')+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
				var $modal=$('#'+gen_id);
				if ($('div.ajaxContent',$modal).length < 1) {
					//session timeout or error - reload the page in the browser
					window.location.href = window.location.href;
				}
				$('#txt_vendor_style_num').keyup(function(ev){
					if(ev.keyCode===13){
						saveHandler();
					}
				});
				win.syncSize();
				win.center();
			});
			return false;
		});
	},
	_setupValidation:function(){
		$('input.save_btn').click(function(){
			$('input.error,textarea.error,select.error').removeClass('error');
			var missing=false;
			$('input.required,select.required,textarea.required').each(function(){
				var $this=$(this);
				var val=$this.val();
				if(val===null || val===''){
					missing=true;
					$this.addClass('error');
				}
			});
			
			if(missing){
				Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please fill in all the required fields.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
				return false;
			}
		});
	},
	_setupRemoveVendorStyle:function(){
		$('a.remove').click(function(){
			return confirm('Are you sure you want to remove this vendor style from the pattern?');
		});
	}
};

// dom is ready...
$(document).ready(belk.cars.patterndetails.init);