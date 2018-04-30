/*
 * belk.cars.editattribute.js
 * Belk CARS - Edit Attribute javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.editproducttype={	
	init:function(){
		var EA=belk.cars.editproducttype;
		EA._setupAutocompletes();
		
	},
	// private methods
	_setupAutocompletes:function(){
		$('select.autocomplete').each(function(){
			var $this=$(this);
			var name=$this.attr("name");
			var combo=new Ext.form.ComboBox({
		        typeAhead:true,
		        triggerAction:'all',
		        transform:this,
		        width:450,
		        forceSelection:true,
				id:id
		    });
			combo.on({
 				'blur' : function(that){
					$('input[name="'+name+'"]').val(that.getValue());
				}
			});
		});
	}

};

// dom is ready...
$(document).ready(belk.cars.editproducttype.init);