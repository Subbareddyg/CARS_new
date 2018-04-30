/*
 * belk.cars.login.js
 * Belk CARS - Login javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.login={
	init:function(){
		var LGN=belk.cars.login;
		LGN._setupTabs();
		LGN._setupEE();
		
		// focus on the username textbox
		$('#j_username').focus();
	},
	// private methods
	_setupTabs:function(){
		// login/create account/contact belk tabs
		var items=[];
		$('#login_tabs > div.tab').each(function(){
			var $this=$(this);
			items.push({
				contentEl:$this.attr('id'),
				title:$this.attr('title')
			});
		});
		var activeTab=0;
		if($('#create_new_account').length>0){
			activeTab=1;
		}
		var tabs = new Ext.TabPanel({
		    renderTo:'login_tabs',
		    activeTab:activeTab,
		    frame:true,
		    defaults:{autoHeight:true},
		    items:items
		});
	},
	_setupEE:function(){
		var once=false,onceRS=false;
		$('#loginForm').submit(function(){
			//var val=$('#j_username').val().toLowerCase();
			//$('#j_username').val(jQuery.trim(val));
			var val=$('#j_username').val();
			val = val.replace(/^\s*|\s*|\s*$/g,'');
			$('#j_username').val(val);
			
			var password = $('#j_password').val();
			password = password.replace(/^\s*|\s*$/g,'');
			$('#j_password').val(password);
			
			if ( val === 'joshua') {
				if (!once) {
					$('#content').append('<div id="ee_jd"><img src="images/jd.jpg" alt="TA" /></div><div id="ee_car"><img src="images/car.gif" alt="CAR" /></div>');
					once = true;
				}
				$('#ee_jd').css({
					'position': 'absolute',
					'z-index': '1',
					'top': '40px',
					'left': '435px',
					'opacity': '1'
				}).show();
				$('#ee_car').css({
					'position': 'absolute',
					'z-index': '2',
					'top': '0',
					'left': '-200px',
					'opacity': '1'
				}).show().animate({
					'left': '400px'
				}, 1000, function(){
					$('#ee_jd').animate({
						'top': '300px',
						'opacity': '0'
					}, 1000, function(){
						$('#ee_car').animate({
							'left': '800px',
							'opacity': '0'
						}, 1000);
					});
				});
				
				return false;
			}
			else if (val === 'vasan') {
				if (!onceRS) {
					$('#content').append('<div id="ee_rs"><img src="images/rs.jpg" alt="Rock Star" /></div>');
					onceRS = true;
				}
				$('#ee_rs').css({
					'position': 'absolute',
					'z-index': '1',
					'top': '40px',
					'left': '230px',
					'display': 'none'
				}).fadeIn();
				setTimeout(function(){
					$('#ee_rs').fadeOut();
				},2000);
				return false;
			}
			return true;
		});
	}
};

// dom is ready...
$(document).ready(belk.cars.login.init);