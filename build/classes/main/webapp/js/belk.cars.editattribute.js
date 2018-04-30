/*
 * belk.cars.editattribute.js
 * Belk CARS - Edit Attribute javascript
 * -rlr
 * Copyright Belk, Inc.
 */
belk.cars.editattribute={
	delimiter:$('#delimiter').val(),
	init:function(){
		var EA=belk.cars.editattribute;
		EA._setupAttrTypeDD();
		EA._setupAddValue();
		EA._setupRemoveValue();
		EA._setupEditValue();
	},
	// private methods
	_setupAttrTypeDD:function(){
		var attrType=$('#sel_attr_type').val();
		if(attrType=='SELECT'||attrType=='FREE_SELECT'||attrType=='RADIO'||attrType=='CHECKBOX'){
			$('li.attr_values').show();
			var attrVals=$('input[name="attributeValues"]').val();
			var array=attrVals.split(belk.cars.editattribute.delimiter);
			var length=array.length;
			for(var i=0;i<length;i++){
				var val=array[i];
				if (val&&val!=''){
					belk.cars.util.addValue2DD(val,'#sel_attr_values');
				}
			}
		}
		
		$('#sel_attr_type').change(function(){
			var $t=$(this);
			var val=$t.val();
			if(val=='SELECT' || val=='FREE_SELECT' ||val=='RADIO'||val=='CHECKBOX'){
				$('li.attr_values').slideDown();
			}
			else{
				$('li.attr_values').slideUp();
			}
		});
	},
	_setupAddValue:function(){
		$('#btn_add').click(function(){
			$(this).blur();
			var $t=$('#txt_attr_value');
			var val=$t.val();
			var addVals = $('#addValueList').val();
			if(val&&val!=''){
				belk.cars.util.addValue2DD(val,'#sel_attr_values');
				//add value to hidden field
				var $in=$('input[name="attributeValues"]')
				$in.val($in.val()+val+belk.cars.editattribute.delimiter);
				if(addVals==''){
					//alert("insde empty");
					//alert("val"+val);
					addVals = val;
				}else{
					addVals+=","+val;
				}	
				$('#addValueList').val(addVals);
				//alert("addvals="+addVals);
				$t.val('');
				$t.focus();
			}
			return false;
		});
	},
	_setupRemoveValue: function(){
		$('#btn_remove').click(function(){
		var checkSel = $('#sel_attr_values option:selected');
		if(checkSel.size()>0) {
				Ext.MessageBox.show ({
					    title: 'Confirm',
						msg: 'Are you sure you want to delete the Value',
						buttons: { yes: 'Yes',cancel: 'Cancel' } ,
						fn: function(btn) {
													
                         if(btn === 'cancel') {
							 return false;
							} else {
								//alert("yess");
								//$(this).blur();
								var elSel = $('#sel_attr_values')[0];
								//alert("sel="+elSel);
								var remVals = $('#removeValueList').val();
								for (i = elSel.length - 1; i >= 0; i--) {
									if (elSel.options[i].selected) {
										//remove value from hidden field
										var $in = $('input[name="attributeValues"]');
										//alert("values"+$in.val());
										//alert("values "+elSel.options[i].text);
										$in.val($in.val().replace(elSel.options[i].text + belk.cars.editattribute.delimiter, ''));
										if(remVals==''){
											//alert("insde empty");
											//alert("val"+elSel.options[i].text);
											remVals = elSel.options[i].text;
										}else{
											remVals+=","+elSel.options[i].text;
										}	
									$('#removeValueList').val(remVals);
									//alert("remVals="+remVals);
										elSel.remove(i);
									}
								}
								return false;
							}
						}
							
		        });
				} else {
					Ext.MessageBox.show ({
					    title: 'Message',
						msg: 'Please select at least one value',
						buttons: { ok: 'Ok'} 
						});
				}
		});
	},
	
	_setupEditValue: function(){
		$('#btn_edit').click(function(){
			//alert("inside");
			$(this).blur();
			var elSel = $('#sel_attr_values')[0];
			for (i = elSel.length - 1; i >= 0; i--) {
				if (elSel.options[i].selected) {
					//remove value from hidden field
					//alert("i==="+i);
					//belk.cars.util.createCookie('editValue',elSel.options[i].text);
					belk.cars.util.set_cookie('editValue',elSel.options[i].text);
					belk.cars.util.createCookie('editValueIndex',i);
					Ext.MessageBox.show({
						title : 'Update',
						msg : 'Please update the value',
						width : 300,
						multiline: 20,
						value : elSel.options[i].text,
						buttons: { yes: 'Update', cancel: 'Cancel' } ,
						fn: function(btn, text) {
						//alert('You pressed ' + btn);
							if(btn === 'cancel') {
								return false;
							}else{
								var $in = $('input[name="attributeValues"]');
								//alert($in.val());
								//var origText = belk.cars.util.readCookie('editValue');
								var origText = belk.cars.util.get_cookie('editValue');
								var valueIndex = belk.cars.util.readCookie('editValueIndex');
								//alert("updated text"+text);
								//alert("original"+origText);
								//alert("index="+valueIndex);
								$in.val($in.val().replace(origText,text));
								//alert("after"+$in.val());
								elSel.remove(valueIndex);
								belk.cars.util.addValue2DD(text,'#sel_attr_values');
								var editVals = $('#editValueList').val();
								if(editVals==''){
									//alert("insde empty");
									//alert("val"+origText+"new"+text);
									editVals = origText+"~@~"+text;
								}else{
									editVals+=","+origText+"~@~"+text;
								}
								$('#editValueList').val(editVals);
								//remove cookie
								belk.cars.util.createCookie('editValue','');
								belk.cars.util.createCookie('editValueIndex','');
							}
						}

											
					});

				}
			}
			return false;
		});
	}
};

// dom is ready...
$(document).ready(belk.cars.editattribute.init);