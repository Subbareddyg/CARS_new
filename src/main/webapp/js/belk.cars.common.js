/*
 * belk.cars.common.js
 * Belk CARS - Common javascript
 * -rlr
 * Copyright Belk, Inc.
 */
var belk={};
belk.cars={
	//configuration for js
	config: {
		urls: {
			car_preview: context+'ajaxMainMenu.html?review=',
			car_edit: context+'mainMenu.html?param=edit&car=',
			set_urgent: context+'ajaxMainMenu.html?param=setUrgFlg&ajax=true&urgFlg=urg&car=',
			set_not_urgent: context+'ajaxMainMenu.html?param=setUrgFlg&ajax=true&urgFlg=notUrg&car=',
			get_prod_types: context+'ajaxMainMenu.html?param=productType&ajax=true&car=',
			set_prod_type: context+'ajaxMainMenu.html?param=productTypeApply&ajax=true&car=',
			//unlock_car:context+'ajaxMainMenu.html?param=setLockedUnlock&unlock=Y&ajax=true&car=',
			//lock_car:context+'ajaxMainMenu.html?param=setLockedUnlock&lock=Y&ajax=true&car=',
			request_image:context+'requestImage.html?carId=',
			copy_car:context+'copyCar.html?',
			publish_content:context+'publishContent.html?ajax=true&',
			mark_received_images:context+'markReceivedImages.html?car_id=',
			get_prod_types_by_class:context+'getProductTypesByClass.html?ajax=true&class_id=',
			vendor_Image_Upload:context+'vendorImageUploadForm.html?carId=',
			vendor_Image_reupload:context+'replaceVendorImageForm.html?imageId='
			
		},
		dateFormat:'m/d/Y',
		dateAltFormats:'m/d/Y|m/d/y|n/j/Y|n/j/y|m/j/Y|m/j/y|n/d/Y|n/d/y|m/j|m/d|n/j|n/d|Y-m-d'
	},
	//utility functions
	util: {
		/* get query string parameter (url is optional- default is current browser url)*/
		getQParam:function(key, url){
			return belk.cars.util.getQParams(url)[key];
		},
		/* get an array with the query string parameters (url is optional- default is current browser url)*/
		getQParams:function(url){
			var args = {};
			var query = null;
			if (url) {
				var tmp = url.split('?');
				if (tmp.length > 1) {
					query = tmp[1];
				}
				else {
					query = url;
				}
			}
			else {
				query = location.search.substring(1);
			}
			var pairs = query.split("&");
			for (var i = 0; i < pairs.length; i++) {
				var pos = pairs[i].indexOf('=');
				if (pos == -1) 
					continue;
				var argname = pairs[i].substring(0, pos);
				var value = pairs[i].substring(pos + 1);
				args[argname] = unescape(value);
			}
			return args;
		},
		createCookie:function(name,value,days) {
			if (days) {
				var date = new Date();
				date.setTime(date.getTime()+(days*24*60*60*1000));
				var expires = "; expires="+date.toGMTString();
			}
			else var expires = "";
			document.cookie = name+"="+value+expires+"; path=/";
		},
		//New function to set cookie
		set_cookie:function(name,value,days)
		{
		if (days)
		{
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
		}
		else var expires = "";
		document.cookie = name + "=" + encodeURIComponent(value) + expires + "; path=/" ;
		},
		//End of new function
		readCookie:function(name) {
			var nameEQ = name + "=";
			var ca = document.cookie.split(';');
			for(var i=0;i < ca.length;i++) {
				var c = ca[i];
				while (c.charAt(0)==' ') c = c.substring(1,c.length);
				if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
			}
			return null;
		},
		//New get_cookie function
		get_cookie:function(name)
		{
 		var nameEQ = name + "=";
		var ca = document.cookie.split(';');       
		for(var i=0;i < ca.length;i++)
		{
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return decodeURIComponent(c.substring(nameEQ.length,c.length)); 
		}
		return null;
		},
		//End of new get cookie function
		eraseCookie:function(name) {
			createCookie(name,"",-1);
		},
		addValue2DD:function(val,ddSelector){
			var elOptNew=document.createElement('option');
			elOptNew.text=val;
			elOptNew.value=val;
			var elSel=$(ddSelector)[0];
			try {
				elSel.add(elOptNew, null); // standards compliant; doesn't work in IE
			}
			catch(ex) {
				elSel.add(elOptNew); // IE only
			}
		},
		getAjaxId:function(){
			return encodeURIComponent((new Date()).toLocaleString());
		},
		isBlank:function(val){
			if(val==null){
				return true;
			}
			for(var i=0;i<val.length;i++){
				if((val.charAt(i)!=' ')&&(val.charAt(i)!="\t")&&(val.charAt(i)!="\n")&&(val.charAt(i)!="\r")){
					return false;
				}
			}
			return true;
		},
		isInteger:function(val){
			if(belk.cars.util.isBlank(val)){
				return false;
			}
			for(var i=0;i<val.length;i++){
				if(!belk.cars.util.isDigit(val.charAt(i))){
					return false;
				}
			}
			return true;
		},
		isDigit:function(num){
			if(num.length>1){
				return false;
			}
			var string="1234567890";
			if(string.indexOf(num)!=-1){
				return true;
			}
			return false;
		}
	}
};

belk.cars.common={
	init:function(){
		$('#ft').append('dom:'+((new Date()).getTime()-sTime.getTime())+'ms ');
		var CMN=belk.cars.common;
		CMN._setupExtjs();
		CMN._setupPanels();
		CMN.setupHelp();
		CMN._setupMainNavHover();
		CMN._setupMainNavSearch();
		CMN._setupMainNavProfile();
		CMN._setupMainNavPassword();
		CMN._setupMainNavContact();
		CMN._handleAjaxErrors();
		CMN._setupConfirmButtons();
	},
	help_win:null,
	setupHelp:function($container){
		// context sensitive help
		$('a.context_help',$container).click(belk.cars.common._getHelpClickHandler());
	},
	getProductTypeDDOptions:function(types){
		var length=types.length;
		var options=[];
		for(var i=0;i<length;i++){
			options.push('<option value="',types[i][2],'">',types[i][0],'</option>');
		}
		return options.join('');
	},
	// private methods
	_setupExtjs:function(){
		// extjs setup
		
		// only enable in dashboard 
		// Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
		
		Ext.QuickTips.init();
	},
	_setupPanels:function(){
		var carId = $('input[name=CarId]').val();
		var isEditOutfitMng = (carId == null || carId == '') ? false : true
		$('div.cars_panel, div.collection_panel').each(function(){
			new Ext.Panel({
		        collapsible:true,
				autoShow:true,
				frame:true,
		        applyTo:this,
				height:'auto',
				collapsed:$(this).is('.collapsed')
	    	});
		});
		$('div.cars_sku_panel').each(function(){
			if (isEditOutfitMng){
				$(this).addClass('collapsed');
			}
			new Ext.Panel({
		        collapsible:true,
				autoShow:true,
				frame:true,
		        applyTo:this,
				height:'auto',
				collapsed:$(this).is('.collapsed'),
				listeners: {
				       'beforeexpand': function(){
							//event.preventDefault();
							var outfitCarId= $('#outfitCarId').val();
							
                            var containerId = this.el.id;
                            var childCarId = containerId.match(/[\d]+$/);
                           // alert(childCarId);

//							var childCarId = $(this).find('skuChildCarId').val();
//							$("#skuChildCarId").each(function(){
//		                        alert($(this).val());
//		                       });
							
//							if(outfitCarId != null && outfitCarId != '')
//							{
//								$.ajaxSetup({ cache: false});
//								href="editPopulateSkus.html?method=getSkusForCar&childCarId="+childCarId+"&CarId="+outfitCarId+"&paramVal=two";
//								$.get(href, function(resp){
//								$("#child_car_Sku_list_"+childCarId).html(resp);
//								}, "html");
//							}
				 		},
				       'beforecollapse': function(){
				            //alert('Called the beforecollapse');
				        }
				}
	    	});
		});
	},
	_getHelpClickHandler:function(){
		return function(){
			var UTIL=belk.cars.util;
			var CMN=belk.cars.common;
			$(this).blur();
			$('#help_content').load($(this).attr('href')+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
				if ($('#help_content div.ajaxContent').length < 1) {
					//session timeout or error - reload the page in the browser
					window.location.href = window.location.href;
				}
				else {
					if (!CMN.help_win) {
						CMN.help_win = new Ext.Window({
							el: 'help_win',
							layout: 'fit',
							width: 250,
							autoHeight: true,
							closeAction: 'hide',
							plain: true,
							items: new Ext.Panel({
								contentEl: 'help_content',
								deferredRender: false,
								border: false,
								autoHeight: true
							})
						});
					}
					else {
						CMN.help_win.syncSize();
						CMN.help_win.center();
					}
					CMN.help_win.show();
				}
			});
			return false;
		};
	},
	_setupMainNavHover:function(){
		// main nav hover for sub nav
		var hoverTimer=null;
		$('#primary-nav li.menubar').hover(
			function(){
				if(hoverTimer){
					clearTimeout(hoverTimer);
				}
				else{
					$(this).addClass('active');
				}
			},
			function(){
				var $this=$(this);
				hoverTimer=setTimeout(function(){
					$this.removeClass('active');
					hoverTimer=null;
				},200);
			}
		);
	},
	_setupMainNavSearch:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		// click on search link in main nav
		$('#primary-nav a.search').click(function(){
			var $li=$(this).blur().parent();
			if (!$li.is('.selected')) {
				$li.addClass('selected');
				var $pnl = $('#search_panel');
				$('div.content',$pnl).html('<div class="loading"></div>');
				$pnl.show();
				// load the search form into the panel from ajax call
				$('div.content',$pnl).load($(this).attr('href')+' div.ajaxContent', function(){
					if ($('div.ajaxContent',$pnl).length < 1) {
						//session timeout or error - reload the page in the browser
						window.location.href = window.location.href;
					}
					else {
						$('#searchForm input#carid').focus();
						$('#searchForm').bind("keyup", function(e) {
							if (e.which == 13) {
								$('#searchForm').submit();
								return true;
							}
						});
						// datefields
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
						new Ext.form.DateField({
							applyTo:'createDate',
					        fieldLabel:'Create Date',
					        allowBlank:true,
							format:CFG.dateFormat,
							altFormats:CFG.dateAltFormats
					    });
						new Ext.form.DateField({
							applyTo:'updateDate',
					        fieldLabel:'Update To',
					        allowBlank:true,
							format:CFG.dateFormat,
							altFormats:CFG.dateAltFormats
					    });
						
						// cancel button click
						$('#btn_cancel,a.close', $pnl).click(function(){
							$pnl.slideUp('fast', function(){
								$li.removeClass('selected');
								$('div.content', $pnl).empty();
							});
							return false;
						});
						
						// search button click
						$('#btn_search',$pnl).click(function(){
							$('#searchForm').submit();
							return false;
						});
						
						// validation
						$('#searchForm').submit(function(){
							var $form=$(this);
							$('input.error',$form).removeClass('error'); //clear existing errors
							var error=false;
							$('#carId,#classNumber,#deptCd',$form).each(function(){
								var $this=$(this);
								$this.val($.trim($this.val()));
								if(!UTIL.isBlank($this.val()) && !UTIL.isInteger($this.val())){
									error=true;
									$this.addClass('error');
								}
							});
							if(error){
								Ext.MessageBox.show({
						           title: 'Invalid Input',
						           msg:'CAR ID, Vendor Style Number, Department Code and must be integers or blank.',
						           buttons: Ext.MessageBox.OK,
						           icon: 'ext-mb-warning'
						       	});
								return false;
							}
							return true;
						});
						$('#searchForm').bind("keyup", function(e) {
							if (e.which == 13) {
								$('#searchForm').submit();
								return true;
							}
						});
					}
				});
			}
			return false;
		});
	},
	_setupMainNavProfile:function(){
		// click on profile link in main nav
		$('#primary-nav a.profile').click(function(){
			var $li=$(this).blur().parent().parent().parent();
			if (!$li.is('.selected')) {
				$li.addClass('selected');
				var $pnl = $('#profile_panel');
				$('div.content',$pnl).html('<div class="loading"></div>');
				$pnl.show();
				// load the user form into the panel from ajax call
				$('div.content',$pnl).load($(this).attr('href') +' div.ajaxContent', function(){
					if ($('div.ajaxContent',$pnl).length < 1) {
						//session timeout or error - reload the page in the browser
						window.location.href = window.location.href;
					}
					else {
						$('#userForm input#firstName').focus();
						// subscribe to cancel button click
						$('#btn_cancel,a.close', $pnl).click(function(){
							$pnl.slideUp('fast', function(){
								$li.removeClass('selected');
								$('div.content', $pnl).empty();
							});
							return false;
						});
						// hook up save button
						$('#btn_save').click(function(){
							$('#userForm').submit();
							return false;
						});
						// hook up context sensitive help
						$('a.context_help',$pnl).click(belk.cars.common._getHelpClickHandler());
						// ajaxify the user form
						$('#userForm').ajaxForm({
							dataType: 'json',
							beforeSubmit:function(){
								$('li.messages', $pnl).html();
								// hook in validation
								var $form=$('#userForm');
								$('input.error',$form).removeClass('error'); //clear existing errors
								var missing=false;
								// required fields
								$('#firstName,#lastName,#phoneAreaCode,#phoneNumber1,#phoneNumber2',$form).each(function(){
									if($(this).val()===''){
										missing=true;
										$(this).addClass('error');
									}
								});
								if(missing){
									Ext.MessageBox.show({
							           title: 'Required fields missing',
							           msg:'Please enter all required fields.',
							           buttons: Ext.MessageBox.OK,
							           icon: 'ext-mb-warning'
							       	});
									return false;
								}
								return true;
							},
							success: function(resp){
								if (resp.success) {
									$('li.messages', $pnl).html('Your profile was saved successfully. <a href="#" class="new_close">Close</a>');
									$('a.new_close', $pnl).click(function(){
										$('a.close', $pnl).click();
									});
								}
								else if(resp.messages && resp.messages.length>0) {
									var l=resp.messages.length;
									var h=['<ul>'];
									for(var i=0;i<l;i++){
										h.push('<li>'+resp.messages[i]+'</li>');
									}
									h.push('</ul>');
									$('li.messages', $pnl).html(h.join(''));
								}
								else {
									$('li.messages', $pnl).html('Oops!. There was an error, please try again.');
								}
							},
							url: $('#userForm').attr('action') + '?ajax=true'
						});
					}
				});
			}
			return false;
		});
	},
	_setupMainNavPassword:function(){
		// click on change password link in main nav
		$('#primary-nav a.change_password').click(function(){
			var $li=$(this).blur().parent().parent().parent();
			if (!$li.is('.selected')) {
				$li.addClass('selected');
				var $pnl = $('#password_panel');
				$('div.content',$pnl).html('<div class="loading"></div>');
				$pnl.show();
				// load the user form into the panel from ajax call
				$('div.content',$pnl).load($(this).attr('href')+' div.ajaxContent', function(){
					if ($('div.ajaxContent',$pnl).length < 1) {
						//session timeout or error - reload the page in the browser
						window.location.href = window.location.href;
					}
					else {
						// subscribe to cancel button click
						$('a.close', $pnl).click(function(){
							$pnl.slideUp('fast', function(){
								$li.removeClass('selected');
								$('div.content', $pnl).empty();
							});
							return false;
						});
						$('#btn_change_password').click(function(){
							$('#changePasswordForm').submit();
							return false;
						});
						// ajaxify the user form
						$('#changePasswordForm').ajaxForm({
							dataType: 'json',
							beforeSubmit:function(){
								// hook in validation
								var $form=$('#changePasswordForm');
								$('input.error',$form).removeClass('error'); //clear existing errors
								var missing=false;
								// required fields
								$('#password,#confirmPassword',$form).each(function(){
									if($(this).val()===''){
										missing=true;
										$(this).addClass('error');
									}
								});
								if(missing){
									Ext.MessageBox.show({
							           title: 'Required fields missing',
							           msg:'Please enter all required fields.',
							           buttons: Ext.MessageBox.OK,
							           icon: 'ext-mb-warning'
							       	});
									return false;
								} else if($('#password',$form).val()!==$('#confirmPassword',$form).val()){
									Ext.MessageBox.show({
							           title:'Invalid Input',
							           msg:'Password and Confirm Password must match.',
							           buttons: Ext.MessageBox.OK,
							           icon: 'ext-mb-warning'
							       	});
									return false;
								}
								return true;
							},
							success: function(resp){
								if (resp.success) {
									$('div.content', $pnl).append('Your password was changed successfully. <a href="#" class="new_close">Close</a>');
									$('a.new_close', $pnl).click(function(){
										$('a.close', $pnl).click();
									});
								}
								else {
									$('div.content', $pnl).append('Oops!. There was an error, please try again.');
								}
							},
							url: $('#changePasswordForm').attr('action') + '?ajax=true'
						});
					}
				});
			}
			return false;
		});
	},
	_setupMainNavContact:function(){
		// click on contact us link in main nav
		$('#primary-nav a.contact').click(function(){
			var $li=$(this).blur().parent();
			if (!$li.is('.selected')) {
				$li.addClass('selected');
				$('#contact_panel').slideDown('fast');
				$('#contact_panel a.close').one('click',function(){
					$('#contact_panel').slideUp('fast', function(){
						$li.removeClass('selected');
					});
					return false;
				});
			}
			return false;
		});
	},
	_handleAjaxErrors:function(){
		$('body').ajaxError(function(event, XMLHttpRequest, ajaxOptions, thrownError){
			if(!ajaxOptions.url.indexOf('review')>=0){
				alert('Oops! An error ajax occured.');
				//window.location.href = window.location.href;
			}
			
		});
	},
	_setupConfirmButtons:function(){
		$('input.confirm, a.confirm').click(function(ev){
			var text = $(this).is('a')?$(this).text():$(this).val();
			return confirm('Are you sure you want to ' + text + '?');
		});
	}
};

//dom is ready...
$(document).ready(belk.cars.common.init);

