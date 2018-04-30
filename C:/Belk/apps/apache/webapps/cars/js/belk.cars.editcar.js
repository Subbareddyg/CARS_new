/*
 * belk.cars.editcar.js
 * Belk CARS - Edit Car javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.editcar={
	init:function(){
		var EC=belk.cars.editcar;
		EC._setupNotes();
		EC._setupAjaxSave();
		EC._setupEditSkuDetails();
		EC._setupRequestImage();
		EC._setupMarkReceivedImages();
		EC._setupAutoCompletes();
		EC._setupDateFields();
		EC._setupMaxCharsPlugin();
		EC._setupMaxChars();
		EC._setupPrintCAR();
		EC._setupSpellcheck();
		EC._setupVendorContact();
		EC._setupSubmitValidation();
		EC._setupConfirmDelete() ;
		EC._eatEnter();
		EC._setupDropDownShow();
		EC._showTextCounter();
		EC._showPromotionNameTextCounter();
		EC._shipdate();
		EC._generateOutfitName();
		EC._showAfterSubmitValidationError();
		$(function(){
		  var target = location.hash && $(location.hash)[0];
		  if( target )
		      $.scrollTo( target, {speed:1000} );
		}); 
	},
	// private methods
	_setupNotes:function(){
		// Add Note functionality
		var setupNotes=function(btnSel,winId,contentId,notesULSel,btnTxt,msgSel,msg){
			var win=null;
			$(btnSel).click(function(){
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
				                }
			            	},{
				                text: btnTxt,
				                handler: function(){
									// add note using ajax submit
									$('#'+contentId+' form').ajaxSubmit({
										dataType:'json',
										success: function(resp){
											if(resp.success&&resp.notes_data&&resp.notes_data.length){
												var html=[];
												var notes=resp.notes_data;
												var l=notes.length;
												for(var i=0;i<=l-1;i++){
													html.push('<li><ul><li class="date"><strong>Date:</strong> ',notes[i][2]);
													html.push('</li><li class="name"><strong>Name:</strong> ',notes[i][3]);
													html.push('</li><li class="text">',notes[i][1],'</li></ul></li>');
												}
												$(notesULSel).html(html.join(''));
												win.hide();
												$(msgSel).html(msg).fadeIn(function(){
													var $this=$(this);
													setTimeout(function(){
														$this.fadeOut("slow");
													},3000);
												});
												$('#'+contentId+' textarea').val('').maxCharsReset();
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
											var missing=false;
											$('textarea.note_text',$form).each(function(){
												var $this=$(this);
												if($this.val()===''){
													missing=true;
													$this.addClass('error');
												}
											});
											if(missing){
												Ext.MessageBox.show({
										           title: 'Required fields missing',
										           msg: 'Please enter a note to add.',
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
		setupNotes('#add_car_note_btn','add_car_note_win','add_car_note_form','ul.car_notes','Add Car Note','#add_note_msg','CAR Note added successfully!');
		//sample notes
		setupNotes('#add_sample_note_btn','add_sample_note_win','add_sample_note_form','ul.sample_notes','Add Sample Note','#add_sample_note_msg','Sample Note added successfully!');
		//return notes
		setupNotes('#add_return_note_btn','add_return_note_win','add_return_note_form','ul.return_notes','Add Return Note','#add_sample_note_msg','Return Note added successfully!');
		
		setupNotes('#add_pim_note_btn','add_pim_note_win','add_pim_note_form','ul.pim_notes','Add Comment','#add_pim_note_msg','PIM comment added successfully!');
	},
	_setupPrintCAR:function(){
		// print car button
		$('#btn_print_car').click(function(){
			$(this).blur();
			window.print();
			return false;
		});
	},
	_setupAutoCompletes:function($container){
		$('select.autocomplete',$container).each(function(){
			var $this=$(this);
			var name=$this.attr("name");
			var combo=new Ext.form.ComboBox({
		        typeAhead:true,
		        triggerAction:'all',
		        transform:this,
		        width:250,
		        forceSelection:false,
				cls:$this.is('.recommended')?'recommended':'',
				id:id,
				disabled:$this.is(':disabled')
		    });
			combo.on({
 				'blur':function(that){
					$('input[name="'+name+'"]').val(that.getRawValue());
				}
			});
		});
	},
	//Added by AFUSYS3
	_shipdate:function(){ 		
		$("input[type='text']").each(function(){             	
                var test=this.id; 
                var fullDate = new Date();
                var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);
                var currentDate = twoDigitMonth+ "/" +fullDate.getDate() + "/" +  fullDate.getFullYear();
                if (test.substring(0, 14) == "sampleShipDate") { 
                      	  var sDate=$("input[id='"+test+"']").val();
                      	  if (sDate == '' || sDate == 'undefined' || sDate == 'Select Date'){
                      		  $("input[id='"+test+"']").val('Select Date');                        	    	
                      	  }   
                      	 sDate=$("input[id='"+test+"']").val();
                     	 if(sDate !='Select Date' && (new Date(sDate) < new Date(currentDate))){
                     		$("input[id='"+test+"']").css("color","red");
                     	 } 
                }
        });
    },
	_setupDateFields:function(){
		var CFG=belk.cars.config;
		$('#changeDate').each(function(){
			var df=new Ext.form.DateField({
				applyTo:this,
				fieldLabel:'Change Date',
				allowBlank:true,
				format:CFG.dateFormat,
				altFormats:CFG.dateAltFormats,
				minValue:new Date()
			});
			df.on({
				'blur':function(that){
					if(!that.isValid()){
						Ext.MessageBox.show({
				           title: 'Invalid Date',
				           msg: 'The date entered is invalid or in the past.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-warning'
				       	});
						$('#btn_update_due_date').attr('disabled','disabled');
					}
					else if($('#changeDate').val()!=='') {
						$('#btn_update_due_date').removeAttr('disabled');
					}
					else{
						$('#btn_update_due_date').attr('disabled','disabled');
					}
				}
			});
		});
		
		//Added by AFUSYQ3
		var loadedShipDate;
		$('.shipDate').click(function() {
			var elementId=$(this).attr('id');	
			loadedShipDate=$("input[id='"+elementId+"']").val();
		});
				
	   $('.shipDate').bind('blur', function () {
	 	  if ($(this).is('.sampleShipDateNew')) {
		  var elementId=$(this).attr('id');	
		  var ShipDateVal=$("input[id='"+elementId+"']").val(); 
		  $("input[id='"+elementId+"']").css("color","black");
		  if(ShipDateVal != 'Select Date'){
			    var fullDate = new Date();
                var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);
                var currentDate = twoDigitMonth+ "/" +fullDate.getDate() + "/" +  fullDate.getFullYear();
				var dateReg=/^(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\d\d$/;
				if(!dateReg.test(ShipDateVal) || ShipDateVal === '' || (new Date(ShipDateVal) < new Date(currentDate))
																				  && (ShipDateVal != loadedShipDate)){
				$("input[id='"+elementId+"']").css("color","red");
				Ext.MessageBox.show({
				           title: 'Invalid Date',
				           msg: 'The Sample Ship date to buyer entered is invalid or in the past.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-warning'
				});
			}
		 }
	   }
	   if ($(this).is('.sampleShipDateOld')) {
			  var elementId=$(this).attr('id');	
			  var ShipDateVal=$("input[id='"+elementId+"']").val(); 
				    var fullDate = new Date();
	                var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);
	                var currentDate = twoDigitMonth+ "/" +fullDate.getDate() + "/" +  fullDate.getFullYear();
					var dateReg=/^(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\d\d$/;
					$("input[id='"+elementId+"']").css("color","black");
					if((new Date(ShipDateVal) < new Date(currentDate))){
						$("input[id='"+elementId+"']").css("color","red");
					}
				  	if(!dateReg.test(ShipDateVal) || ShipDateVal === '' || (new Date(ShipDateVal) < new Date(currentDate)) 
				  														&& (ShipDateVal != loadedShipDate)){
					$("input[id='"+elementId+"']").css("color","red");
					Ext.MessageBox.show({
					           title: 'Invalid Date',
					           msg: 'The Sample Ship date to buyer entered is invalid or in the past.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-warning'
					});
				}
		   }
	  });
	  // END by AFUSYQ3  - Ph2

	 $('input.date').each(function(){
		 if ($(this).is('.shipDate')) {
				var df=new Ext.form.ShipDate({
					applyTo:this,
					fieldLabel:'Change Date',
					allowBlank:true,
					format:CFG.dateFormat,
					altFormats:CFG.dateAltFormats,
					minValue:new Date()
				}); 
			}else 
				if ($(this).is('.restricted')) {
				var df=new Ext.form.DateField({
					applyTo:this,
					fieldLabel:'Change Date',
					allowBlank:true,
					format:CFG.dateFormat,
					altFormats:CFG.dateAltFormats,
					minValue:new Date()
				});
				df.on({
					'blur':function(that){
						if(!that.isValid()){
							Ext.MessageBox.show({
					           title: 'Invalid Date',
					           msg: 'The date entered is invalid or in the past.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-warning'
					       	});
						}
					}
				});
			}
			else {
				var df = new Ext.form.DateField({
					applyTo: this,
					fieldLabel: 'Change Date',
					allowBlank: true,
					format:CFG.dateFormat,
					altFormats:CFG.dateAltFormats
				});
			}
		});
	 
	 	$("input[id^='globalDate_']").click(function(){
			
			 var buttonId = this.id;
			 var count = 0;
			 count = buttonId.match(/[\d]+$/);			 
			 var globalDate = $(".globalDate_"+count).val();
			 count++;
			 $('.startDate_'+count).val(globalDate); 
	 	});
	},
	_setupMaxCharsPlugin:function(){
		// custom jquery plugin for the max/remaining characters functionality
		jQuery.fn.maxChars = function(){
			return this
			.keydown(function(ev){
				// check max chars
				var $this=$(this);
				var max=parseInt($('span.max_chars_val',$this.parent()).html());
				if($this.val().length>=max && ev.keyCode!==8 && ev.keyCode!==46){
					if($this.val().length>max){
						$this.val($this.val().substring(0,max-1));
					}
					return false;
				}
				return true;
			});
		};
		jQuery.fn.maxCharsReset = function(){
			return this.each(function(){
				/*var $this=$(this);
				var max=parseInt($('span.max_chars_val',$this.parent()).html());
				$('span.chars_rem_val',$this.parent()).html(max-$this.val().length||'0');*/
			});
		};
	},
	_setupMaxChars:function(){
		$('input.maxChars,textarea.maxChars').maxChars();
	},
	_setupRequestImage:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;

		var getHandler=function(title, button, imgId, action){
			return function(){
				$(this).blur();
				var gen_id=Ext.id(); // a generated ID for the div
				$('body').append('<div id="'+gen_id+'" class="request_image_modal x-hidden"><div class="loading"></div></div>');
				var win=new Ext.Window({
					layout:'fit',
					width:500,
					autoHeight:true,
					title:title,
					modal:true,
					plain:true,
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
					}, {
						text: button,
						handler: function(){
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
									if(action==="AddOnHand"){
										$('#onhand_images').html($resp);
										setupEditOnHand();
										belk.cars.common.setupHelp($('#onhand_images'));
									}
									else{
										$('#requested_images').html($resp);
										setupEditRequest();
										belk.cars.common.setupHelp($('#requested_images'));
									}
									win.hide();
								},
								beforeSubmit:function(){
									// validation before submitting
									var $form=$('#'+gen_id+' form');
									$('textarea.error,input.error,select.error',$form).removeClass('error'); //clear existing errors
									var missing=false;
									$('#imageFinalUrl',$form).each(function(){
										var $this=$(this);
										if($this.val()===''){
											missing=true;
											$this.addClass('error');
										}
									});
									$('#imageLocationTypeCd',$form).each(function(){
										var $this=$(this);
										if($this.val()==='Select an option'){
											missing=true;
											$this.addClass('error');
										}
									});
									if(missing){
										Ext.MessageBox.show({
								           title: 'Required fields missing.',
								           msg: 'Please enter all the required fields.',
								           buttons: Ext.MessageBox.OK,
								           icon: 'ext-mb-warning'
								       	});
										return false;
									}
									return true;
								}
							});
						}
					}]
				});
		        win.show();
				$('#'+gen_id).load(CFG.urls.request_image+$('#carId').val()+(imgId?'&imgId='+imgId:'')+(action?'&action='+action:'')+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
					if ($('div.ajaxContent',$('#'+gen_id)).length < 1) {
						//session timeout or error - reload the page in the browser
						window.location.href = window.location.href;
					}
					win.syncSize();
					win.center();
					$('input.maxChars,textarea.maxChars',$('#'+gen_id)).maxChars();
				});
				return false;
			};
		};
		$('#btn_request_iamge,#btn_add_image_vendor').click(getHandler('Request Image From Vendor', 'Request Image'));
		var setupEditRequest = function(){
			$('#requested_images td.edit a').each(function(){
				var $this = $(this);
				$this.click(getHandler('Edit Image', 'Save', UTIL.getQParam('image', $this.attr('href'))));
			});
			$('#requested_images td.delete a').each(function(){
				var $this = $(this);
				//$this.click(getHandler('Edit Image', 'Save', UTIL.getQParam('image', $this.attr('href'))));
				$this.click(function(){
					Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this image?', function(btn){
						if(btn==='yes'){
							$.get($this.attr('href'),function(resp){
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
								$('#requested_images').html($resp);
								setupEditRequest();
							});
						}
					});
					return false;
				});
			});
		};
		setupEditRequest();
		$('#btn_add_on_hand_image').click(getHandler('Add On-Hand Image', 'Add Image', null, 'AddOnHand'));
		var setupEditOnHand = function(){
			$('#onhand_images td.edit a').each(function(){
				var $this = $(this);
				$this.click(getHandler('Edit Image', 'Save', UTIL.getQParam('image', $this.attr('href')), 'AddOnHand'));
			});
			$('#onhand_images td.delete a').each(function(){
				var $this = $(this);
				//$this.click(getHandler('Edit Image', 'Save', UTIL.getQParam('image', $this.attr('href')), 'AddOnHand'));
				$this.click(function(){
					Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this image?', function(btn){
						if(btn==='yes'){
							$.get($this.attr('href'),function(resp){
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
								$('#onhand_images').html($resp);
								setupEditRequest();
							});
						}
					});
					return false;
				});
			});
		};
		setupEditOnHand();
	},
	_setupMarkReceivedImages:function(){
		$('#btn_mark_as_received').click(function(ev){
			$('#mark_as_received_loading').show();
			var imageIdParams=[];
			$('#onhand_images input[name="receivedImageId"]:checked').each(function(){
				imageIdParams.push('receivedImageId=',$(this).val(),'&');
			});
			$.get(belk.cars.config.urls.mark_received_images+$('input[name="carId"]').val()+'&'+imageIdParams.join(''),function(resp){
				var $resp=$(resp);
				if(!$resp.is('.ajaxContent')){
					Ext.MessageBox.show({
			           title: 'Error',
			           msg: 'Oops there was an error! Please try again.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				}
				else{
					$('#onhand_images').html($resp);
				}
				$('#mark_as_received_loading').hide();
			});

			ev.preventDefault();
		});
	},
	_setupVendorContact:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var getHandler=function(title, button, imgId, action){
			return function(){
				var $this=$(this).blur();
				var gen_id=Ext.id(); // a generated ID for the div
				$('body').append('<div id="'+gen_id+'" class="vendor_modal x-hidden"><div class="loading"></div></div>');
				var win=new Ext.Window({
					layout:'fit',
					width:400,
					autoHeight:true,
					title:title,
					modal:true,
					plain:true,
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
					}, {
						text: button,
						handler: function(){
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
									if($('form',$resp).length>0){
										$('#'+gen_id).html($resp);
										return;
									}
									$('#add_vendor_li').replaceWith($resp);
									setupAddEditRemove();
									win.hide();
								},
								beforeSubmit:function(){
									// validation before submitting
									var $form=$('#'+gen_id+' form');
									$('input.error',$form).removeClass('error'); //clear existing errors
									var missing=false;
									$('#emailAddress,#firstName,#lastName,#phoneAreaCode,#phoneNumber1,#phoneNumber2',$form).each(function(){
										var $this=$(this);
										if($this.val()===''){
											missing=true;
											$this.addClass('error');
										}
									});
									if(missing){
										Ext.MessageBox.show({
								           title: 'Required fields missing.',
								           msg: 'Please enter all the required fields.',
								           buttons: Ext.MessageBox.OK,
								           icon: 'ext-mb-warning'
								       	});
										return false;
									}
									var error=false;
									$('#phoneAreaCode,#phoneNumber1,#phoneNumber2',$form).each(function(){
										var $this=$(this);
										$this.val($.trim($this.val()));
										if(!UTIL.isBlank($this.val()) && !UTIL.isInteger($this.val())){
											error=true;
											$this.addClass('error');
										}
									});
									if(error){
										Ext.MessageBox.show({
								           title: 'Invalid Phone Number',
								           msg: 'The phone number must only contain numbers.',
								           buttons: Ext.MessageBox.OK,
								           icon: 'ext-mb-warning'
								       	});
										return false;
									}
									return true;
								}
							});
						}
					}]
				});
		        win.show();
				$('#'+gen_id).load($this.attr('href')+'&a_id='+belk.cars.util.getAjaxId(),function(){
					win.syncSize();
					win.center();
				});
				return false;
			};
		};
		var setupAddEditRemove=function(){
			$('#btn_add_vendor_contact').click(getHandler('Add Vendor Contact','Add Vendor'));
			$('#vendorContact td.edit a').each(function(){
				var $this=$(this);
				$this.click(getHandler('Edit Vendor','Save'));
			});
			$('#vendorContact td.remove a').click(function(){
				var $this=$(this);
				Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this user?', function(btn){
					if(btn==='yes'){
						$.get($this.attr('href'),function(resp){
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
							$('#add_vendor_li').replaceWith($resp);
							setupAddEditRemove();
						});
					}
				});
				return false;
			});
			$('#VENDOR').click(function(){
				$('.chld_vendor_num').show(100);
			});
		};
		setupAddEditRemove();
	},
	_setupSpellcheck:function(){
		$('textarea.spellcheck').each(function(){
			var $this=$(this);
			var googie = new GoogieSpell("googiespell/", context+"spellCheck.jsp?lang=");
			googie.decorateTextarea($this.attr('id'));
		});
	},
	_eatEnter:function(){
		$('input[type="text"]').keydown(function(ev){
			if(ev.keyCode===13){
				return false;
			}
			return true;
		});
	},
	_setupAjaxSave:function(){
		var getHandler=function(loadingId,msgId){
			return function(){
				$(this).blur();
				var gen_id = Ext.id();
				$('#detailCar').append('<input type="hidden" id="' + gen_id + '" name="ajax" value="true"/>');
				$(loadingId).show();
				$(msgId).html('CAR saved successfully!');
				$('#detailCar').ajaxSubmit({
					dataType: 'json',
					beforeSubmit:function(){
						// validation before saving
						var $form=$('#detailCar');
						$('textarea.error',$form).removeClass('error'); //clear existing errors
						var missing=false;
						$('textarea.required',$form).each(function(){
							var $this=$(this);
							if($this.val()===''){
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
					       	$(loadingId).hide();
							return false;
						}
						return true;
					},
					success: function(resp){
						if (resp && resp.success) {
							$(loadingId).hide();
							if(resp.colorCodeError){
								alert("Color code "+resp.colorCode+" already exist, please choose a unique color code."); 
							}else if(resp.sampleRequestedError){
								alert("Color code cannot be changed at this time due to sample request."); 
							}else{
								$(msgId).html('CAR saved successfully!').fadeIn(function(){
									var $this = $(this);
									setTimeout(function(){
										$this.fadeOut("slow");
									}, 3000);
								});
						    }
						}
						else {
							Ext.MessageBox.show({
								title: 'Error',
								msg: 'Oops!. There was an error, please try again.',
								buttons: Ext.MessageBox.OK,
								icon: 'ext-mb-error'
							});
							$(loadingId).hide();
						}
						$('#' + gen_id).remove();
					},
					url: $('#detailCar').attr('action') + '?ajax=true'
				});
				return false;
			};
		};
		$('#btn_save_car_atts').click(getHandler('#save_car_loading','#save_car_msg'));
		$('#btn_save_car_bot').click(getHandler('#save_car_bot_loading','#save_car_bot_msg'));
		
		// Copy Complete CAR
		$("#btn_copy_complete_car").click(function(){
			var carId= $('input[name=carId]').val();
			$.ajax({
				type: "GET",
				url: "completeCopyCar.html",
				data:"carId="+carId,
				success: function(data){
					 var $resp=$(data);
					 if ($resp.html().indexOf('Yes') != -1){ 
						 	Ext.MessageBox.alert('Complete CAR', 'Copy Complete Car successfully.');
						 // redirect the url to refresh the skus if copy complete
							var redirect = 'editCarForm.html?carId='+carId; 
							window.location = redirect;
						  
						} else  if ($resp.html().indexOf('NoSkus') != -1){
							Ext.MessageBox.alert('Complete CAR', 'No Skus Found to Copy Complete Car');
						}
					 	
					}
				,
				dataType:'JSON'
			});
		});
		},
	_setupSubmitValidation:function(){
		$('input[name="submitCar"]').click(function(){
			var assignedTo = $('#assigned').val();
			if(assignedTo != "Art Director"){  
			var workflowStat=$('#wfs').val();  
			var userStatus = workflowStat == "Ready for Review"  && assignedTo == "Buyer";
			var buyerIntiated= workflowStat == "Initiated"  && assignedTo == "Buyer";
			var turn_in=$('#sampleTurninDate').val();
			var missing=false;
			var skuNameMissing= false;
			//Added for validation of turn-in date
			var turninDateAlertMessage = "";	
			var buyerStatus = (workflowStat == "Ready for Review" || workflowStat == "Waiting for Sample") && (assignedTo == "Buyer");
			var webmStatus = $('#BUYER').attr("checked") != undefined ||$('#BUYER').attr("checked") || $('#BUYER2').attr("checked");
			//var webmStatus = undefined;
			//if(assignedTo == "Buyer") {
			//	webmStatus=true;
			//} else {
			 // webmStatus = $('#BUYER').attr("checked") != undefined ||$('#BUYER').attr("checked") || $('#BUYER2').attr("checked");
			
			//}
			var sendCarToUserCheck=(buyerIntiated && $('#VENDOR').attr("checked"));
			var transitionID = $('input:radio[name=transtionId]:checked').val();
			//alert("transitionID : "+transitionID+ " assignedTO : "+assignedTo+" workflowStats : "+workflowStat);
			if(assignedTo == 'Buyer' && (workflowStat == "Ready for Review") && (transitionID=='22' || transitionID=='23')){
				//alert("before returning true");
				
				return true;
			}
			if(sendCarToUserCheck){
				//coloring back to black if there is proper turn-in date
				if ($('#sampleTurninDate').parent().prev().is('label')){
					$('#sampleTurninDate').parent().prev().css("color","black"); 
				}
			}
			var curentuser = $('#currentUser').val();
			if((buyerStatus || webmStatus && !sendCarToUserCheck) && curentuser !='ART_DIRECTOR'){	
				var $this=$(this);
				//coloring back to black if there is proper turn-in date
				if ($('#sampleTurninDate').parent().prev().is('label')){
					$('#sampleTurninDate').parent().prev().css("color","black"); 
				}
				var dateReg=/^(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\d\d$/;
				if(!dateReg.test(turn_in) || turn_in === ''){		
					if ($('#sampleTurninDate').parent().prev().is('label')){
						$('#sampleTurninDate').parent().prev().css("color","red"); 
					} 
					turninDateAlertMessage = "The date entered is invalid or in the past. <br/>";
					//missing = true;					
				}else{
					var dateParam = turn_in.split('/');
					var turninDt = new Date(dateParam[2], dateParam[0]-1, dateParam[1]);
					var diff = (turninDt - new Date())/(1000*60*60*24);
					if(diff>7 || diff<-1){
					   //$('#sampleTurninDate').addClass('error');
					   turninDateAlertMessage = "Turn in date cannot be greater than 7 days from current date. <br/>";
					}
				}
			}													
			// validation for quar quality compulsory
			var qualitySelected = $("input[name=cquality]:checked").attr('id');
			
			$('.carQualityRow').css("color", "black");
			if ((curentuser == "BUYER") && (qualitySelected== null  || qualitySelected == 'undefined' || qualitySelected === undefined)){
				$('.carQualityRow').css("color", "red");
				missing = true;
			} 
			if(userStatus || webmStatus || sendCarToUserCheck){
				// Modified for required field attribute
				$('input.recommended,textarea.recommended,select.recommended').each(function(){
					var $this=$(this);
					$(this).siblings().eq(0).css("color","black"); 
					if ($(this).parent().prev().is('label')){
						$(this).parent().prev().css("color","black"); 
					}
					if(!sendCarToUserCheck){
					var elementId= $(this).attr('id');
						if ($this.val().trim()==='' && !(elementId.indexOf('carSkuItemName') > -1) 
								                    && !(elementId.indexOf('carSkuSizeName') > -1)){
						$(this).siblings().eq(0).css("color","red"); 
						if ($(this).parent().prev().is('label')){
							$(this).parent().prev().css("color","red"); 
						}
						missing=true;
					}  } else { 
						//removing existing errors
						$(this).siblings().eq(0).css("color","black"); 
						if ($(this).parent().prev().is('label')){
							$(this).parent().prev().css("color","black"); 
						}
					}
				});
				// Added code for required field attribute validation
				$(".attrs > li").each(function() {
					if(sendCarToUserCheck){
						var ourLabel = "";
						var selections = $(this).find("input[type=radio], input[type=checkbox]").filter('.recommended');
						if(selections.length == 0){
							return;
						}
						selections.eq(0).each(function(){
							if ($(this).is(":radio")) {
								ourLabel = $(this).siblings().eq(0);
							} else {
								ourLabel = $(this).parent().parent().prev();
							} 
						});
						if (!selections.is(":checked"))	{
							ourLabel.css("color", "black");
						} else { ourLabel.css("color", ""); }
					}
					if(!sendCarToUserCheck){
						var ourLabel = "";
						var selections = $(this).find("input[type=radio], input[type=checkbox]").filter('.recommended');
						if(selections.length == 0){
							return;
						}
						selections.eq(0).each(function(){
							if ($(this).is(":radio")) {
								ourLabel = $(this).siblings().eq(0);
							} else {
								ourLabel = $(this).parent().parent().prev();
							} 
						});
						//console.log(selections);
						if (!selections.is(":checked"))	{
							ourLabel.css("color", "red");
							missing = true;
						} else { ourLabel.css("color", ""); }
				}
			}); 
		}	
			if(missing){
				Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Some of the recommended fields have not been filled. Please fill in all required fields to continue.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
				return false;
			 }	
			//coloring back to black if there is proper turn-in date
			if ($('#sampleTurninDate').parent().prev().is('label')){
				$('#sampleTurninDate').parent().prev().css("color","black"); 
			}
			if(turninDateAlertMessage != ""){
				if ($('#sampleTurninDate').parent().prev().is('label')){
					$('#sampleTurninDate').parent().prev().css("color","red"); 
				}
			} 
			var textLength = $('.textCount').val().length;
			var maxCharsCheckCriteria = (buyerIntiated && $('#VENDOR').attr("checked"));
			if (textLength < 40 && !maxCharsCheckCriteria) {					
				turninDateAlertMessage = turninDateAlertMessage + "Minimum requirement of 40 characters has not been met in the product description field("
				+ textLength
				+ " number of characters entered).";
				$('.textCount').siblings().eq(0).css("color","red"); 
			} 
			if(turninDateAlertMessage != ""){
				Ext.MessageBox.show({
			           title: 'Required fields missing',
			           msg: turninDateAlertMessage,
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});	  				  		
	  			return false;
	  		}
			
			if($('#chld_vendor_num').val()== "" && $('input[@name=transtionId]:checked').attr('id')=='VENDOR'){
				Ext.MessageBox.alert('Select Vendor','Please select vendor to send Outfit CAR');
				return false;
			}
			
			// checking if any super color1 is not selected
			// added for Faceted CARS
			var superColor1s = [];
	     	$(".superColor1").each(function(){
				superColor1s.push(this.value);
			});
			var superColor1Found = $.inArray('-1',superColor1s);
			if(superColor1Found != -1){ // if found return 0
				Ext.MessageBox.show({
			           title: 'One of the Super Color1 missing',
			           msg: 'Please select the Super Color1',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			    });
				return false;
			}
		return true;
		} 
	});
	},
	_setupConfirmDelete:function(){
		$('input[name="deleteCar"]').click(function(){
			Ext.MessageBox.confirm('Confirm Delete Car', 'Are you sure you want to Delete this CAR?', function(btn){
				if(btn==='yes'){
					// submit form
					$('#detailCar').append('<input type="hidden" name="deleteCar" value="Delete CAR" />').submit();
				}
			});
			return false;
		});
	},
	_setupEditSkuDetails:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var autocompletes={};

		$('a.edit_sku_details').click(function(){
			var $this=$(this).blur();
			var $sku_ul=$this.parent();
			while(!$sku_ul.is('ul')){$sku_ul=$sku_ul.parent()};
			var $sku_li=$sku_ul.parent();
			while(!$sku_li.is('li')){$sku_li=$sku_li.parent()};
			var gen_id=Ext.id(); // a generated ID for the div
			$('body').append('<div id="'+gen_id+'" class="edit_sku_details_modal x-hidden"><div class="loading"></div></div>');
			var win=new Ext.Window({
				layout:'fit',
				width:600,
				autoHeight:true,
				title:'Edit SKU Details: '+UTIL.getQParam('upc',$this.attr('href')),
				modal:true,
				plain:true,
				items: new Ext.Panel({
	                contentEl:gen_id,
	                border:false,
					autoHeight:true
	            }),
				buttons: [{
					text: 'Cancel',
					handler: function(){
						win.hide();
						setTimeout(function(){
							$('#'+gen_id).remove();
						},2000);
					}
				}, {
					text: 'Save',
					handler: function(){
						this.disable();
						// request image using ajax submit
						$('#'+gen_id+' form').ajaxSubmit({
							success: function(resp){
								var json=eval('(' + resp + ')');
								if(!json.success){
									Ext.MessageBox.show({
							           title: 'Error',
							           msg: 'Oops there was an error! Please verify your input and try again.',
							           buttons: Ext.MessageBox.OK,
							           icon: 'ext-mb-error'
							       	});
									this.enable();
									return;
								}
								else {
									var $form=$('#'+gen_id+' form');
									var html=[];
									$('input.skuAttrCheckbox:checked',$form).each(function(){
										var $this=$(this);
										var $li=$this.parent();
										while(!$li.is('li')){$li=$li.parent();}
										var ac=autocompletes[$this.attr('name')];
										var val=ac?ac.getRawValue():$('input.attrVal:checked,input.attrVal,select.attrVal,textarea.attrVal', $li).val();
										var label=$('label',$li).text().replace(' *','');
										html.push('<li><label>',label,'</label>',val,'</li>');
									});
									var $ul_exc=$('ul.attr_exceptions',$sku_li);
									if($ul_exc.length<1){
										$sku_li.append('<ul class="attr_exceptions">'+html.join('')+'</ul>');
									}
									else{
										$ul_exc.html(html.join(''));
									}
								}
								win.hide();
								setTimeout(function(){
									$('#'+gen_id).remove();
								},2000);
							}
						});
					}
				}]
			});
	        win.show();

			$('#'+gen_id).load($(this).attr('href')+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
				var $modal=$('#'+gen_id);
				if ($('div.ajaxContent',$modal).length < 1) {
					//session timeout or error - reload the page in the browser
					window.location.href = window.location.href;
				}
				win.syncSize();
				win.center();
				
				$('select.autocomplete',$modal).each(function(){
					var $this=$(this);
					var name=$this.attr("name");
					var $li=$this.parent();
					while(!$li.is('li')){$li=$li.parent();}
					var index=$('input.skuAttrCheckbox',$li).attr('name');
					autocompletes[index]=new Ext.form.ComboBox({
				        typeAhead:true,
				        triggerAction:'all',
				        transform:this,
				        width:250,
				        forceSelection:false,
						cls:$this.is('.recommended')?'recommended':'',
						id:id,
						disabled:$this.is(':disabled')
				    });
					autocompletes[index].on({
		 				'blur':function(that){
							$('input[name="'+name+'"]',$li).removeAttr('disabled').val(that.getRawValue());
						}
					});
				});
				$('input.skuAttrCheckbox',$modal).click(function(){
					var $this=$(this);
					var $li=$this.parent();
					while(!$li.is('li')){$li=$li.parent();}
					var ac=autocompletes[$this.attr('name')];
					if($this.is(':checked')){
						if(ac){
							ac.enable();
							$('input[type="hidden"]', $li).removeAttr('disabled');
						}
						else{
							$('input.attrVal,select.attrVal,textarea.attrVal', $li).removeAttr('disabled');
						}
					}
					else{
						ac?ac.disable():$('input.attrVal,select.attrVal,textarea.attrVal', $li).attr('disabled', 'disabled');
					}
				});
			});
			return false;
		});
	},
	_setupDropDownShow:function(){
		var $dropdown = $('select.dropdown-show');
		if ($dropdown) {
			function showCurrent(){
				$('ul.dropdown-show > li').hide();
				$('ul.dropdown-show > li.' + $dropdown.val()).show();
			}
			showCurrent();
			$dropdown.change(showCurrent);
		}
	},
	
	_showTextCounter:function(){
			var total = ($('#txt_outfitName').length > 0)? $('#txt_outfitName').val().length : 0;
			$('#outfit_name_length').text(total);	
			var prdtotal = $('.textCount').val().length;
			$('#product_desc_length').text(prdtotal);

		},	

	_showPromotionNameTextCounter:function(){
			
			var dbpromototal = ($('#txt_dbPromotionName').length > 0)? $('#txt_dbPromotionName').val().length : 0;
			$('#dbPromotion_name_length').text(dbpromototal);	
			var prdtotal = $('.textCount').val().length;
			$('#product_desc_length').text(prdtotal);
		    },
		
		
	_generateOutfitName:function(){
		$('#generate_outfitName_btn').click(function(){ 
			  var childCars = [];
			  var prdName = "", brandName = "", childOrder = "";
			  $("#childCarID tr").each(function() {
				prdName   = $(this).find(".prdName").eq(0).html();
				brandName = $(this).find(".brandName").eq(0).html();
				childOrder = $(this).find('#childOrder').val();
				
				prdName = prdName==null? prdName : prdName.trim();
				brandName = brandName==null? brandName : brandName.trim();
				if(childOrder < 1){
					childOrder = 1;
				}
				
				if ((prdName != null) && (brandName != null)) {
				  childCars.push({brandName: brandName, prdName: prdName, childOrder: childOrder});
				}
			  });
			  var outfitName = processCarsArray(childCars);
			  $('#txt_outfitName').val(jQuery.trim(outfitName));
			  textCounter();			  
			  return false;
		 });
	},
		
	_showAfterSubmitValidationError:function(){
		if($("#vendorImageError").html() != null || $("#vendorImageApproveError").html() != null ){
			$("#imageerrorfocus").focus();
	     }
		if($('#colorCodeError').text().trim().length > 1){
			alert($('#colorCodeError').text());
		}
   }
};

function processCarsArray(carsArray) {
 var i, j, m, n, len;
 var outfitName= "";
 var childProducts = "";
 len = carsArray.length ;
 var counter = 0;
 var brandProductMap = new Object(); 
 for(i = 0; i < len; i++) {
	if((childProducts = brandProductMap[carsArray[i].brandName]) == null){
		childProducts = new Array();
	}
	childProducts.push(carsArray[i]);
	brandProductMap[carsArray[i].brandName]=childProducts;
 }
 
 for(i = 1; i <= len; i++) {
   for(j = 0; j < len; j++) {
	if(carsArray[j].childOrder == i){
		//Retrieving brand names as j = brand_name, as per above note.
		var childProducts = brandProductMap[carsArray[j].brandName];
		if(childProducts != null && childProducts != ""){
			var productName="";
			for(m=1; m<=len ; m++){
				for(n=0; n<childProducts.length ; n++){
				  if(childProducts[n].childOrder == m){
					counter++;
					productName = productName + childProducts[n].prdName;
					if(counter == len-1){
						productName = productName + " & ";
					}else if(counter < len-1){
						productName = productName + ", ";	
					}
					
				  }
				}
			}
			
				outfitName = outfitName + carsArray[j].brandName  + " "+ productName;
		
			brandProductMap[carsArray[j].brandName]="";
		}
	 }
   }
 }
  outfitName = escapeChars(outfitName);
  return outfitName;
}

//Escape all HTML encoded ampersand characters with '&' from outfitName
function escapeChars(outfitName) {
 	if (outfitName.indexOf("&amp;") >= 0) {
 		outfitName= outfitName.replace(/\&amp;/g,'&');
	}
 	return outfitName;
}

//Counter for adding product description added on April 20 2012
function textCounter(){
	var total = ($('#txt_outfitName').length > 0)? $('#txt_outfitName').val().length : 0;
	$('#outfit_name_length').text(total);	
	var prdtotal = $('.textCount').val().length;
	$('#product_desc_length').text(prdtotal);

}
function promotionNameTextCounter(){
	var dbpromototal = ($('#txt_dbPromotionName').length > 0)? $('#txt_dbPromotionName').val().length : 0;
	$('#dbPromotion_name_length').text(dbpromototal);	

}

// Track Sku Changes
$('.trackupdate').click(function() {
    var current_class =$(this).attr('class');
    var id_array = current_class.split(',');
    var skuId=id_array[0];
    var existingSkus=$('#updatedSkus').val();
    if (existingSkus == null || existingSkus == 'undefined') {
       $('#updatedSkusDiv').
                  append('<input type="text" id="updatedSkus" name="updatedSkus" style="display:none" value="'+skuId+'"/>');    
    } else if(existingSkus.indexOf(skuId) == -1 ){
	    $('#updatedSkusDiv').empty();
	    $('#updatedSkusDiv').
	              append('<input type="text" id="updatedSkus" name="updatedSkus" style="display:none" value="'+existingSkus+','+skuId+'"/>');    
    }   
});

//Track Shade Descr Changes
$('.trackshadedesc').click(function() {
    var current_class =$(this).attr('class');
    var id_array = current_class.split(',');
    var colorCode=id_array[0];
    var existingColorCodes=$('#updatedShades').val();
    if (existingColorCodes == null || existingColorCodes == 'undefined') {
       $('#updatedShadesDiv').
                  append('<input type="text" id="updatedShades" name="updatedShades" style="display:none" value="'+colorCode+'"/>');    
    } else if(existingColorCodes.indexOf(colorCode) == -1 ){
	    $('#updatedShadesDiv').empty();
	    $('#updatedShadesDiv').
	              append('<input type="text" id="updatedShades" name="updatedShades" style="display:none" value="'+existingColorCodes+','+colorCode+'"/>');    
    }   
});

//Track Start Date Changes
$('.trackstartdate').bind('blur', function () {
    var current_class =$(this).attr('class');
    var id_array = current_class.split(',');
    var startDateSkus=id_array[0];
    var existingSkus=$('#updatedStartDateSkus').val();
    if (existingSkus == null || existingSkus == 'undefined') {
       $('#updatedStartDateDiv').
                  append('<input type="text" id="updatedStartDateSkus" name="updatedStartDateSkus"  style="display:none" value="'+startDateSkus+'"/>');    
    } else if(existingSkus.indexOf(startDateSkus) == -1 ){
	    $('#updatedStartDateDiv').empty();
	    $('#updatedStartDateDiv').
	              append('<input type="text" id="updatedStartDateSkus" name="updatedStartDateSkus" style="display:none"  value="'+existingSkus+','+startDateSkus+'"/>');    
    }   
});



//Track Start Date Changes
$('.tracksupercolor').click(function() {
    var current_class =$(this).attr('class');
    var id_array = current_class.split(',');
    var superColorSkus=id_array[0];
    var existingSuperColorSkus=$('#updatedSuperColorSkus').val();
    if (existingSuperColorSkus == null || existingSuperColorSkus == 'undefined') {
       $('#updatedSuperColorDiv').
                  append('<input type="text" id="updatedSuperColorSkus" name="updatedSuperColorSkus" style="display:none"  value="'+superColorSkus+'"/>');    
    } else if(existingSuperColorSkus.indexOf(superColorSkus) == -1 ){
	    $('#updatedSuperColorDiv').empty();
	    $('#updatedSuperColorDiv').
	              append('<input type="text" id="updatedSuperColorSkus" name="updatedSuperColorSkus" style="display:none"  value="'+existingSuperColorSkus+','+superColorSkus+'"/>');    
    }   
});



var idArray = new Array();
$("input[id^='parentCarId']").each(function(){
	idArray.push($(this).val());
});
function setID()
{
	for (var i = 0; i < idArray.length; i++) 
	{
		$('.child_'+i).find('.x-panel-header').html('Child Car Id : '+idArray[i]);
	}
}
setID();
//dom is ready...
$(document).ready(belk.cars.editcar.init);

//code for completion date
window.onload = function() {
var shipDate= $('#shipmentupdateddate').val();
var shipmentupdateddate = Date.parseDate(shipDate,"Y-m-d");
var currentDate= new Date();
shipmentupdateddate.setMonth(shipmentupdateddate.getMonth()+3);
if(currentDate<shipmentupdateddate){
$('.completion_date').css("color","red");
}
}