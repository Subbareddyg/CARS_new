/*
 * belk.cars.dbPromotion.js
 * Belk CARS - DBPromotion Details javascript
 */

belk.cars.dbPromotion={	
	init:function(){
		var PD=belk.cars.dbPromotion;
		PD._setupDateFields();
		PD._setupAddChildCar();
		PD._validation();
		textCounter();
		enableTemplateType();
		showDBPromotionCollectionFields();
	},
	// private methods
	//Added for MAC project
	_setupDateFields:function(){
		var carId = $('input[name=CarId]').val();
		var isEditDBPromotionMng = (carId == null || carId == '') ? false : true
		var config=belk.cars.config;
		$('#effectiveDate').each(function(){
			var df;
			if (isEditDBPromotionMng){
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
					if ($resp.html().indexOf('DBPromotionCar')!= -1){ // checking the response has Car Id as a text if the Car Id is an DBPromotion Car
				 	 	msg='Promotion or Outfit Car can not be a Child CAR!';
					}
					if ($resp.html().indexOf('PYGGWPTrueCar')!= -1){ // checking the response has Car Id as a text if the Car Id is an DBPromotion Car
				 	 	
						Ext.MessageBox.show({
					           title: 'Ineligible car to be added as child',
					           msg: 'GWP and PYG attributes should be associated with the child CAR with a value of \'Yes\' before adding it to a Promotional CAR.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-warning'
					       	});
					}
					if ($resp.html().indexOf('ProdCodeStoreLimitOver')!= -1){ // child's system limit to store further parent product codes is over in an attribute PROMO_PARENT_PRODUCTS
				 	 	
						Ext.MessageBox.show({
					           title: 'Child Association Limit Exceeded',
					           msg: 'One or more of the child CARs requested are associated to too many other parent products. Please remove the child CAR from old promotions and then try creating current promotion again.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-warning'
					       	});
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
				           title: 'Promotion Details Missing',
				           msg: 'Please Select the Prioritization for all the Child CARS and then click on Auto-Generate.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					error = true;
				    return false;
				}else if(jQuery.inArray(childOrder,orderArray)>0){
					Ext.MessageBox.show({
				           title: 'Promotion Details Incorrect',
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
		  
		  // To generate dbPromotion name checking at least one carId present
		  if(carId == null){
				 Ext.MessageBox.show({
			           title: 'Promotion Details Missing',
			           msg: 'Please Add Child CARS and then click on Auto-Generate.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				    error = true;
					return false;
				 
		  }
		  var dbPromotionName="";
		  if(childOrder >= 1 && !error) {
			  dbPromotionName = processCarsArray(childCars);
			  $('#txt_dbPromotionName').val(jQuery.trim(dbPromotionName));
			  textCounter();
		  }
		  return false;
	 });
		
		$('.removeChildCar').live('click',function(event){
			 event.preventDefault();
			 var conf =confirm("Are you sure you want to remove this CAR, If YES change the Promotion Name accordingly.");
			 if(conf==false){
				 return false;
			 }
			var dbPromotionCarId= $('input[name=CarId]').val();
			var templateTypeVal= $('input[name=templateTypeVal]').val();
			selectedBelkUpc();
			var selBelkUpc = sessionStorage.getItem('bUpc');
			// this check is for remove child car while
			// creating new dbPromotion, since there is no dbPromotion car id
			// passing null as string
			if(typeof dbPromotionCarId == 'undefined'){
				dbPromotionCarId='null';
			}
			
			$.ajaxSetup({ cache: false});
			$.get(this.href, {dbPromotionCarId: dbPromotionCarId,templateTypeVal: templateTypeVal,selectedUpc: selBelkUpc}, function(resp){
		            $("#child_cars_wrap").html(resp);
		            var hasChildCar = $("#childCarID div").hasClass(".car_id");
				    if(hasChildCar == false){
					   enableTemplateType();
				    }
		        }, "html");
			
		 });
		
	},

	_validation:function(){
		 $('#dbPromotionForm').submit(function() {
					var carchildIdArray = '';
					$('.cars_sku_panel').each(function(){
						var checkboxChecked = $(this).find('input:checked').size();
						var curentChildId = $(this).find('.hiddencardchildid').text();
						if(checkboxChecked==1){
							$(this).addClass('onecarschildiselected');
							if(carchildIdArray==''){
								carchildIdArray=curentChildId;
							}else{
								carchildIdArray = carchildIdArray+","+curentChildId;
							}
						}
					});
					//alert('sizeddddd '+$('input#onestepschildcars').size()+'carchildIdArray: '+carchildIdArray);
					$('#onestepschildcars').val(carchildIdArray);
					
			 	 	if( $('#txt_dbPromotionName').val().trim().length == 0 || $('#txt_dbPromotionDescr').val().trim().length == 0  ){
					Ext.MessageBox.show({
				           title: 'Promotion Details Missing',
				           msg: 'Promotion Name and Description are required.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
			 	if( $('#txt_dbPromotionName').val().length > 150 ){
					Ext.MessageBox.show({
				           title: 'Promotion Details Missing',
				           msg: 'Promotion Name has exceeded the maximum character limit of 150.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
				if( $('#txt_dbPromotionDescr').val().length > 2000 ){
					Ext.MessageBox.show({
				           title: 'Promotion Details Missing',
				           msg: 'Description has exceeded the maximum character limit of 2000.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
						return false;
				  }
				
				 var count=$('#childCarCount').val();
				 var rowCount=$('#childCarID tbody tr').not('.empty').size();
				// alert('childCarID tbody tr count'+rowCount);
				 if(rowCount==0 ){
					 Ext.MessageBox.show({
				           title: 'Promotion Details Missing',
				           msg: 'Please add at least 1 child car.',
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
				           title: 'Promotion Details Missing',
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
					           title: 'Promotion Details Missing',
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
								   title: 'Promotion Details Missing',
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
				//alert("What is Templ val?");
				if ($("#templateTypeVal").val() == "PYG") //COL ws value
				{
				//alert("It i had chenged is PYG");
				//alert("Total count"+count);
					for(i=0;i<=count;i++){
						//alert("Inside for loop");
						 var skuNum = $("#sku"+i).get(0);	
						 var selDefColor = $("#sku"+i+" :selected").text();
						 
						 var selCarId = $("#parentCarId"+i).val();	
						  	 if(skuNum.value != '-1') {
						  		    var skucount;
						  		  //alert($('input[id="skuCount"]').size());
						  		  $('input[id="skuCount"]').each(function(){
						  			skucount = ($(this).val());
						  			});
						  		    
						  		    var present="false";
						  		    //alert("Value of skucount"+skucount);
						  		    for(j=0;j<=skucount;j++)
						  		 	{
						  		    var colorname = $("#colorName"+j).val();	
						  		    var skucarId = $("#skuCarid"+j).val();	
						  		  //  alert("Value of colorname"+colorname);   
						  		 //////   alert("Value of skucarId"+skucarId);
						  		  //  alert("Value of selCarId"+selCarId);
						  		   // alert("Value of selDefColor"+selDefColor);
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
											   title: 'Promotion Details Missing',
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
	}
	
};

function processCarsArray(carsArray) {
 var i, j, m, n, len;
 var dbPromotionName= "";
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
 
 //Loop through all child cars to create the dbPromotion product name
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
			//crate dbPromotion name appending the brand name + product name
			dbPromotionName = dbPromotionName + carsArray[j].brandName  + " "+ productName;
			brandProductMap[carsArray[j].brandName]="";
		}
	 }
   }
 }
 dbPromotionName = escapeChars(dbPromotionName);
 return dbPromotionName;
}

//Escape all HTML encoded ampersand characters with '&' from dbPromotionName
function escapeChars(dbPromotionName) {
 	if (dbPromotionName.indexOf("&amp;") >= 0) {
 		dbPromotionName= dbPromotionName.replace(/\&amp;/g,'&');
	}
 	return dbPromotionName;
}

function textCounter(){
	var total = $('#txt_dbPromotionName').val().length;
	$('#dbPromotion_name_length').text(total);	
}

//Adding for MAC project to hide the sku info for DBPromotion

$('#categoryFilter').change(function() {
		if ($('#categoryFilter').val()=== "PYG") {
			  $('#generate_btn').show();
		  }
		else {
			$('.cars_sku_panel').each(function(){ $(this).show(); })
			$('#generate_btn').show();

		}
});

function hideSku (){
	if ($('#categoryFilter').val()=== "PYS") {
		  $('.cars_sku_panel').each(function(){ $(this).hide(); })
	  }
}

function showDBPromotionCollectionFields (){
	if ($('#templateTypeVal').val()=== "PYG") {
		  $('#generate_btn').show();
	  }
	else {
		 $('#generate_btn').show();
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
				//$('.child_'+j).find('.x-panel-header').html('Child Car Id : '+idArray[i]); original below is for onseStep requirement
				$('.child_'+j).find('.x-panel-header').html('Child Car Id : '+idArray[i]+'<span class=hiddencardchildid style="display:none">'+idArray[i]+'</span>');
			}
		}
	}
}
setID();
// dom is ready...
$(document).ready(belk.cars.dbPromotion.init);