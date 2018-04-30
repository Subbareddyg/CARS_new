/*
 * belk.cars.editpattern.js
 * Belk CARS - Edit Pattern javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.editpattern={	
	init:function(){
		var EA=belk.cars.editpattern;
		EA._setupValidation();
		EA._setupProductTypesDD();
		textCounter();
		
	},
	// private methods
	_setupValidation:function(){
		$('input.save_btn').click(function(){
			var len= $('#txt_patternName').val().length;
			if( $('#txt_patternName').val().length > 150 ){
				Ext.MessageBox.show({
					title: 'Pattern Details Missing',
					msg: 'Pattern Name has exceeded the maximum character limit of 150.',
					buttons: Ext.MessageBox.OK,
					icon: 'ext-mb-error'
						});
				return false;
				}
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
	_setupProductTypesDD:function(){
		$('#txt_classNumber').blur(function(){
			$('#prod_types_loading').show();
			$.getJSON(belk.cars.config.urls.get_prod_types_by_class+$(this).val()+'&a_id='+belk.cars.util.getAjaxId(),function(resp){
				if(resp.success&&resp.product_types.product_type_data){
					$('#sel_productType').html(belk.cars.common.getProductTypeDDOptions(resp.product_types.product_type_data));
				}
				else{
					$('#sel_productType').html('');
					Ext.MessageBox.show({
			           title: 'Invalid Class ID?',
			           msg: 'No product types found for the Class ID.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				}
			});
			
			$('#prod_types_loading').hide();
		});
	}

};
function textCounter(){
	var total = $('#txt_patternName').val().length;
	$('#pattern_name_length').text(total);
	}

// dom is ready...
$(document).ready(belk.cars.editpattern.init);