/*
 * belk.cars.outfit.js
 * Belk CARS - Outfit Details javascript
 */

belk.cars.outfit={	
	init:function(){
		var PD=belk.cars.outfit;
		// tying hide to doc.ready, as categoryFilter dropdown change event is no longer valid; change event code is also cleaned-up
		PD._hideFieldsForOutfit();
		PD._setupDateFields();
		PD._setupAddChildCar();
		PD._validation();
		textCounter();
		enableTemplateType();
		hideCollectionFields();
	},
	// private methods
	//Added for MAC project
	_setupDateFields:function(){
		var carId = $('input[name=CarId]').val();
		var isEditOutfitMng = (carId == null || carId == '') ? false : true
		var config=belk.cars.config;
		$('#effectiveDate').each(function(){
			var df;
			if (isEditOutfitMng){
				df = new Ext.form.DateField({
					applyTo:this,
					fieldLabel:'Effective Date',
					allowBlank:true,
					format:config.dateFormat,
					altFormats:config.dateAltFormats
				});
			}
			else {
				df = new Ext.form.DateField({
					applyTo:this,
					fieldLabel:'Effective Date',
					allowBlank:true,
					format:config.dateFormat,
					altFormats:config.dateAltFormats,
					minValue:new Date()
				});
			}
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
		});
	},
	_setupAddChildCar:function(){
		var CFG=belk.cars.config;
		var UTIL=belk.cars.util;
		var autocompletes={};
		var win,gen_id;
		
		var saveHandler=function(){
			var saveButton=win.buttons[1];
			// validation
			var missing=false;
			var child_car=$('#txt_child_car_id').val();
			child_car=jQuery.trim(child_car);

            // code to validate the special characters in car id
			var iChars = "!@#$%^&*()+=[]\\\';,./{}|\":<>?~_"; 
			for (var i = 0; i < child_car.length; i++) {
				if (iChars.indexOf(child_car.charAt(i)) != -1) {
				     missing=true;
				     break;
				} else if(!UTIL.isDigit(child_car.charAt(i))){
					 missing=true;
					 break;
				}
			}
				
			if(missing == false & child_car == ""){
				missing=true;
		    }
			
			if(missing){
				Ext.MessageBox.show({
		           title: 'Invalid Car Id',
		           msg: 'Please enter valid Car Id.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
				return false;
			}

			saveButton.disable();
			selectedBelkUpc();
//			var selBelkUpc = JSON.parse(sessionStorage.getItem('bUpc')); 
			var selBelkUpc = sessionStorage.getItem('bUpc'); 
			$('#selectedUpc').val(selBelkUpc);
//			var templateTypeHidden = JSON.parse(sessionStorage.getItem('templateTypeHidden')); 
			var templateTypeHidden = sessionStorage.getItem('templateTypeHidden'); 
			$('#hiddenTemplateType').val(templateTypeHidden);
			
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
					var msg="";
					if ($resp.html().indexOf('Car Id') != -1){ // checking the response has Car Id as a text if the Car Id found.
							$('#child_cars_wrap').html(resp);
							msg='Car Id ' + $('#txt_child_car_id').val() + ' added successfully!';
							disableTemplateType();
					} 
					if ($resp.html().indexOf('NotFound')!= -1){ // checking the response has Car Id as a text if the Car Id not found
						 	 msg='Car Id ' + $('#txt_child_car_id').val() + ' Not found. Please enter a valid CAR ID.';
					}
					if ($resp.html().indexOf('Exists')!= -1){ // checking the response has Car Id as a text if the Car Id already added
					 	 	msg='Car Id ' + $('#txt_child_car_id').val() + ' Already Exists!';
					}
					if ($resp.html().indexOf('SaveVendorStyle')!= -1){ // checking the response has Car Id as a text if the Car Id already added
				 	 	msg='Car Id ' + $('#txt_child_car_id').val() + ' with same Vendor Style already exists!';
					}
					if ($resp.html().indexOf('OutfitCar')!= -1){ // checking the response has Car Id as a text if the Car Id is an Outfit Car
				 	 	msg='Outfit Car can not be a Child CAR!';
					}
					if ($resp.html().indexOf('NoPYGInOutfit')!= -1){ // checking the response has Car Id as a text if the Car Id is an PYG Car
				 	 	msg='PYG Car cannot be a Child CAR!';
					}
					
					$('#add_child_car_msg')	
						.html(msg)
						.fadeIn(function(){
							var $this=$(this);
							setTimeout(function(){
								$this.fadeOut("slow");
							},3000);
					});
					$('#txt_child_car_id').val('');
					hideSku();
					saveButton.enable();
					

				}
			});
		};
		
		
			$('a.btn_add_child_car').live("click", function(){
						
			var $this=$(this).blur();
			 gen_id=Ext.id();
			$('body').append('<div id="'+gen_id+'" class="add_vendor_style_modal x-hidden"><div class="loading"></div></div>');
			 win =new Ext.Window({
				layout:'fit',
				width:400,
				autoHeight:true,
				title:'Add Child Car',
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
									win.close();
								}
							}, {
						text: 'Add',
						handler: function(){
							saveHandler();
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
				  $("#txt_child_car_id").keypress(function (evt) {
					 var charCode = evt.charCode || evt.keyCode;
					 
					 //allowing Ctrl+V(Paste)
					 if((evt.ctrlKey && charCode == 118)) 
							return true;
					 
					 // only numerics allowed
					 if (charCode > 31 && (charCode < 48 || charCode > 57)){
							return false;
					 }
					 
					 if (charCode  == 13) {
						 saveHandler();
						 return false;
					 }
				   });
				win.syncSize();
				win.center();

				});
			return false;
		});
			
		//Auto-Generate button functionality
		$('#generate_btn').click(function(){ 
		  var childCars = new Array();
		  var prdName = "";
		  var brandName = "";
		  var childOrder = "";
		  var carId="";
		  var error = false;
		  var orderArray = [];
		  $("#childCarID tr").each(function() {
			    carId   = $(this).find(".car_id").eq(0).html();
				prdName   = $(this).find(".prdName").eq(0).html();
				brandName = $(this).find(".brandName").eq(0).html();
				childOrder = $(this).find(".childOrder option:selected").eq(0).val();
				
				if(childOrder < 1){
					Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Please Select the Prioritization for all the Child CARS and then click on Auto-Generate.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					error = true;
				    return false;
				}else if(jQuery.inArray(childOrder,orderArray)>0){
					Ext.MessageBox.show({
				           title: 'Outfit Details Incorrect',
				           msg: 'Please select unique priority for all child cars and then click on Auto-Generate.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					error = true;
				    return false;
				}
				
				prdName = prdName==null? prdName : prdName.trim();
				brandName = brandName==null? brandName : brandName.trim();
				childCars.push({brandName: brandName, prdName: prdName, childOrder: childOrder});
				orderArray.push(childOrder);
		  });
		  
		  // To generate outfit name checking at least one carId present
		  if(carId == null){
				 Ext.MessageBox.show({
			           title: 'Outfit Details Missing',
			           msg: 'Please Add Child CARS and then click on Auto-Generate.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				    error = true;
					return false;
				 
		  }
		  var outfitName="";
		  if(childOrder >= 1 && !error) {
			  outfitName = processCarsArray(childCars);
			  $('#txt_outfitName').val(jQuery.trim(outfitName));
			  textCounter();
		  }
		  return false;
	 });
		
		$('.removeChildCar').live('click',function(event){
			 event.preventDefault();
			 var conf =confirm("Are you sure you want to remove this CAR, If YES change the Outfit Name accordingly.");
			 if(conf==false){
				 return false;
			 }
			var outfitCarId= $('input[name=CarId]').val();
			var templateTypeVal= $('input[name=templateTypeVal]').val();
			selectedBelkUpc();
			var selBelkUpc = sessionStorage.getItem('bUpc');
			// this check is for remove child car while
			// creating new outfit, since there is no outfit car id
			// passing null as string
			if(typeof outfitCarId == 'undefined'){
				outfitCarId='null';
			}
			
			$.ajaxSetup({ cache: false});
			$.get(this.href, {outfitCarId: outfitCarId,templateTypeVal: templateTypeVal,selectedUpc: selBelkUpc}, function(resp){
		            $("#child_cars_wrap").html(resp);
		            var hasChildCar = $("#childCarID div").hasClass(".car_id");
				    if(hasChildCar == false){
					   enableTemplateType();
				    }
		        }, "html");
			
		 });
		
	},

	_validation:function(){
		 $('#outfitForm').submit(function() {
			 	 	if( $('#txt_outfitName').val().trim().length == 0 || $('#txt_outfitDescr').val().trim().length == 0  ){
					Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Outfit Name and Description are required.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
			 	if( $('#txt_outfitName').val().length > 150 ){
					Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Outfit Name has exceeded the maximum character limit of 150.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
				if( $('#txt_outfitDescr').val().length > 2000 ){
					Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Description has exceeded the maximum character limit of 2000.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
				 var count=$('#childCarCount').val();				 
								 
				 if(count < 0 && (($("#templateTypeVal").val() == "COLLECTION") || ($('#categoryFilter').val()=== "COLLECTION"))){
					 Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Please add at least 1 child cars.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
					 
				 }
                 else if(count < 1 && (($("#templateTypeVal").val() == "OUTFIT") || ($('#categoryFilter').val()=== "OUTFIT"))){
                     Ext.MessageBox.show({
                          title: 'Outfit Details Missing',
                          msg: 'Please add at least 2 child cars.',
                          buttons: Ext.MessageBox.OK,
                          icon: 'ext-mb-error'
                           });
                           return false;
                     
                }

				 // check the duplicates in order selection and check for not selecting 'Select'.
				 var orderArray = [];
				 var not_order=false;
				 for(i=0;i<=count;i++){
					 var orderNum = $("#order"+i).get(0);
					 if(orderNum.value == '-1') {
						 Ext.MessageBox.show({
				           title: 'Outfit Details Missing',
				           msg: 'Please Select the Prioritization for all the Child CARS.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
					 }
					 if(jQuery.inArray(orderNum.value,orderArray)==-1){
						 orderArray.push(orderNum.value);
					} else{
						not_order=true;
						break;
					}
				}
				if(not_order == true){
						 Ext.MessageBox.show({
					           title: 'Outfit Details Missing',
					           msg: 'Please make sure the Prioritization is Unique for each Child CAR.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-error'
					       	});
						return false;
				}
					
				// check for not selecting 'Select' from Default Sku drop down.
				for(i=0;i<=count;i++){
					var skuNum = $("#sku"+i).get(0);
						  	 if(skuNum.value == '-1') {       					
								 Ext.MessageBox.show({
								   title: 'Outfit Details Missing',
								   msg: 'Please Select the Default SKU Color for all the Child CARS.',
								   msg: 'Please Select the Default Color SKU for all the Child CARS.',
								   buttons: Ext.MessageBox.OK,
								   icon: 'ext-mb-error'
								});
								return false;
							 }
				}
				// check Default Sku drop down value match with Selected skus.
				var j;
				if ($("#templateTypeVal").val() == "COLLECTION")
				{
					for(i=0;i<=count;i++){												
						 var skuNum = $("#sku"+i).get(0);	
						 var selDefColor = $("#sku"+i+" :selected").text();
						 var selCarId = $("#parentCarId"+i).val();	
						  	 if(skuNum.value != '-1') {
						  		    var skucount;
						  		  
						  		  $('input[id="skuCount"]').each(function(){
						  			skucount = ($(this).val());
						  			});
						  		    
						  		    var present="false";
						  		    for(j=0;j<=skucount;j++)
						  		 	{
						  		    var colorname = $("#colorName"+j).val();	
						  		    var skucarId = $("#skuCarid"+j).val();
						  		    if(skucarId == selCarId && selDefColor == colorname)
						  		    {
						  				if($('#chk'+j).is(':checked')) 
						  				{
						  					present = "true";
						  					
						  				}
						  		 	}
						  		 }
						  		    if(present == "false")
						  		    {
						  		    	Ext.MessageBox.show({
											   title: 'Outfit Details Missing',
											   msg: 'Please select atleast one child car having color matching with default color sku dropdown.',
											   msg: 'Please select atleast one child car having color matching with default color sku dropdown.',
											   buttons: Ext.MessageBox.OK,
											   icon: 'ext-mb-error'
											});
						  		    	return false;
						  		    }
							 }
				}
			}

		});
	}, 
	_hideFieldsForOutfit(){
		if ($('#categoryFilter').val()=== 'OUTFIT') {
			  $('.cars_sku_panel').each(function(){ $(this).hide(); })
			  $('.isSearchable').hide(); 
			  $('.effDate').hide();
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
 
 //Create Brand name vs Child cars map 
 for(i = 0; i < len; i++) {
	if((childProducts = brandProductMap[carsArray[i].brandName]) == null){
		childProducts = new Array();
	}
	childProducts.push(carsArray[i]);
	brandProductMap[carsArray[i].brandName]=childProducts;
 }
 
 //Loop through all child cars to create the outfit product name
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
					
					//append '&' symbol after last but one product.
					if(counter == len-2){
						productName = productName + " & ";
					}else if(counter < len-2){
						productName = productName + ", ";	
					}
				  }
				}
			}
			//crate outfit name appending the brand name + product name
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

function textCounter(){
	var total = $('#txt_outfitName').val().length;
	$('#outfit_name_length').text(total);	
}

//Adding for MAC project to hide the sku info for Outfit

/*$('#categoryFilter').change(function() {
		if ($('#categoryFilter').val()=== "OUTFIT") {
			  $('.cars_sku_panel').each(function(){ $(this).hide(); })
			  $('.isSearchable').hide(); 
			  $('.effDate').hide();
		  }
		else {
			$('.cars_sku_panel').each(function(){ $(this).show(); })
			$('.isSearchable').show();
			$('.effDate').show();
		}
});*/

function hideSku (){
	if ($('#categoryFilter').val()=== "OUTFIT") {
		  $('.cars_sku_panel').each(function(){ $(this).hide(); })
	  }
	if ($('#templateTypeVal').val()=== "OUTFIT") {
		  $('.isSearchable').hide(); 
		  $('.effDate').hide();
	  }
}

function hideCollectionFields (){
	if ($('#templateTypeVal').val()=== "OUTFIT") {
		  $('.isSearchable').hide(); 
		  $('.effDate').hide();
	  }
}

function disableTemplateType(){
	var category = $('#categoryFilter').val();
	$('#templateTypeVal').val(category);	
	$('#categoryFilter').attr('disabled','disabled');
}

function enableTemplateType(){
	$('#categoryFilter').removeAttr('disabled');
}
var idArray = new Array();
$("input[id^='parentCarId']").each(function(){
	idArray.push($(this).val());
});
function setID()
{
	for (var i = 0; i < idArray.length; i++) 
	{
		var allChildCars = $("input[id='allChildCarIds_"+i+"']").val();
		for (var j = 0; j < idArray.length; j++){
			if (allChildCars != null && allChildCars.indexOf((idArray[i]+"-"+j)) > -1){
				$('.child_'+j).find('.x-panel-header').html('Child Car Id : '+idArray[i]);
			}
		}
	}
}
setID();
// dom is ready...
$(document).ready(belk.cars.outfit.init);