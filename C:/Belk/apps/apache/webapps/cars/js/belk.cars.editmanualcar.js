/*
 * belk.cars.editmanualcar.js
 * Belk CARS - Edit Attribute javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.editmanualcar={	
	init:function(){
		var EA=belk.cars.editmanualcar;	
		EA._setupButtons();
	},
	// private methods
	_setupButtons:function(){
		$('#btn_cancel').click(function(){
			$('input.cancel_btn').click();
			return false;
		});
		$('#btn_save').click(function(){
			$('input.save_btn').click();
			return false;
		});
	}

};

// dom is ready...
$(document).ready(belk.cars.editmanualcar.init);