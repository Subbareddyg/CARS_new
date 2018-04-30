/*
 * belk.cars.uploadimage.js
 * Belk CARS - Upload Image javascript
 * -rlr
 * Copyright Belk, Inc.
 */
var err_msg_cdUploadFailed="Not able to upload image, Please try again.";
var err_msg_ftpUploadFailed ="Problem with FTP location, Please try again.";
var err_msg_reqFieldMissing ="Please complete information for the highlighted areas.";
var err_msg_incorrectImageType="This file format is not acceptable. Please try again using EPS, JPG, PSD, TIFF, or JPEG.";
var err_msg_incorrectFTPLocation="Incorrect image type/location, please try again."
var err_msg_incorrectFTPLogin = "Incorrect FTP location or login credentials, Please try again.";	
var err_msg_incorrectFTPPath = "Specified file not found on FTP location, Please try again.";
var sucess_msg ="Image image_name successfully uploaded.";
var err_msg_ImageUploadFailed = "Image uploading failed.";
var err_all_image_uploaded = "Maximum number of images uploaded for this style/color."
var allowed_file_extn =['eps','jpg','psd','tiff','tif','jpeg'];
var isMainImageSelected = false;
var isSwatchImageSelected = false;
var alternateImageCount=0;
var previousValue = 0;
var fullValue = 0;
var shotType_array = 0;
var shotType_count = 0;

$(document).ready(function(){
	var UTIL=belk.cars.util;
	var CFG=belk.cars.config;
	// Added by AFUSYQ3 - VIP 
	$("[name='imageLocationType']").live('change',function(){
		var current_id = $(this).attr('id');
		var id_array = current_id.split('_');
		var cur_form_number = parseInt(id_array[1]);		
		if('CD'==$(this).val()){
			$('#ftpDiv_'+cur_form_number).hide();
			$('#cdImageFile_'+cur_form_number).parent().removeClass('hidden');	
			$('#result_'+cur_form_number).html('');
			$('#cdImageFile_'+cur_form_number).siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpUrl"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpFileName"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpUserId"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpPassword"]').siblings('label').removeClass('fieldError');
			$('#shotType' + cur_form_number).siblings('label').removeClass('fieldError');
		}else{
			$('#cdImageFile_'+cur_form_number).parent().addClass('hidden');
			$('#ftpDiv_'+cur_form_number).show();
			$('#result_'+cur_form_number).html('');
			$('#cdImageFile_'+cur_form_number).siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpUrl"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpFileName"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpUserId"]').siblings('label').removeClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpPassword"]').siblings('label').removeClass('fieldError');
			$('#shotType' + cur_form_number).siblings('label').removeClass('fieldError');
		}
	});
	
	$("[name='imageType']").live('change',function(){
		var current_id = $(this).attr('id');
		var id_array = current_id.split('_');
		var cur_form_number = parseInt(id_array[1]);		
		if($(this).val() == 'MAIN' || $(this).val() == 'SWATCH') {
			$('.ShotTypeCd_'+cur_form_number).hide('slow');
			previousValue = 0;
		}else{
			$('.ShotTypeCd_'+cur_form_number).show('slow');
			previousValue = $("#shotType" + cur_form_number).val();
		}
	});
	
	$("[name='shotTypeValue']").live('change', function() {
		previousValue = $(this).val();
	});
	  	
	$('.addButton').live('click',function(){
		//get the current form number
		var current_id = $(this).attr('id');
		var id_array = current_id.split('_');
		var cur_form_number = parseInt(id_array[1]);  
		if(submitVendorImageForm(cur_form_number)){
			//code to open second form
			var next_form_number = cur_form_number+1;
			$(this).addClass('hidden');
			var shotTypeid = 'shotType'+ next_form_number;
			$('#container_'+next_form_number).show('slow');	
			//if main or swatch image is already selected in previous vendor image form then donot show main/swatch image option in Image type dropdown
			if(isMainImageSelected){
				$('#uploadForm_'+next_form_number+' select[name="imageType"] option[value="MAIN"]').remove();
			}
			if(isSwatchImageSelected){
				$('#uploadForm_'+next_form_number+' select[name="imageType"] option[value="SWATCH"]').remove();
			}
			if(alternateImageCount<1){
				$('#uploadForm_'+next_form_number+' select[name="imageType"] option[value="ALT"]').remove();
			}
			if($('#uploadForm_'+next_form_number+ ' select[name="imageType"]').val() == 'ALT' && alternateImageCount<=1 && $('#uploadForm_'+next_form_number+ ' select[name="imageType"] option').size()==1){
				$('.addButton').addClass('hidden');
			}
			if ($('#uploadForm_'+ next_form_number+ ' select[name="imageType"]').val() == 'ALT') {
				$('.ShotTypeCd_'+ next_form_number).show('slow');

					if ($('#uploadForm_'+ cur_form_number+ ' select[name="imageType"]').val() == 'ALT') {
					previousValue = $("#shotType"+ cur_form_number).val();
				}
			}
			else if($('#uploadForm_'+next_form_number+ ' select[name="imageType"]').val() != 'ALT' && $('#uploadForm_'+next_form_number+ ' select[name="imageType"] option').size()==1){
				$('.addButton').addClass('hidden');
			}
			//hide close button
			$('.closeButtonCls').hide();
			
			fullValue = fullValue + "_"+ previousValue;
			shotType_array = fullValue.split('_');
			

			$("select#" + shotTypeid+ " option").each(function() {

								var currentValue = $(this).val();
								var currentObject = $(this);

								$.each(shotType_array,function(i,val) {
													if (currentValue == shotType_array[i]) {
														$(currentObject).remove();
													}

												});
							});
			
		}
	});
	 
	 
	 $('.uploadButton').click(function() {
		// $(this).blur();
		 var buttonClicked = $(this);
		 var gen_id=Ext.id();
		 fullValue = 0;
		 $('body').append('<div id="'+gen_id+'" class="add_vendor_style_modal x-hidden"><div class="loading"></div></div>');
		 var elementId=$(this).attr('id');
		 var carsArray = new Array();
		 carsArray=elementId.split(":");
		 var cName=carsArray[2];
		 cName=cName.replace(/ /g,"_");
		 var styleNumber= carsArray[5];
		 var sampleId = carsArray[6];
		 var sampleType = $('input:radio[name="sample:'+sampleId+'"]:checked').val();
		 if(sampleType == 'NoSampleRequired'){
			 sampleType = 'NITHER';
		 }else{
			 sampleType = 'SW';
		 }
		 var url=CFG.urls.vendor_Image_Upload+carsArray[1]+"&cName="+cName+"&cCode="+carsArray[3]+"&vNo="+carsArray[4]+"&vsNo="+carsArray[5]+"&sampleType="+sampleType;
		 
		 var win = new Ext.Window({
			title: 'Upload Images',
			autoHeight:true,	
			closable: false,
			modal:true,
			plain:true,
			bodyStyle:'background:white;',
			items: new Ext.Panel({
				    contentEl:gen_id,
				    border:false,
				    autoHeight:true
					}),
			height: 275,
			width: 550,
			buttons:[{
						text: 'Cancel',
						cls:'cancelButtonCls',
						handler: function(){
							//cancell all open vendor image upload form
							var showConfirmationMessage = false;
														
							$('.container:visible form:visible').each(function(){
								 var currentOpenForm_id = $(this).attr('id');
								 var id_array = currentOpenForm_id.split('_');
								 var cur_form_number = parseInt(id_array[1]);
								 if($('#imageLocationType_'+cur_form_number).val()=='CD'){
									 if($('#cdImageFile_'+cur_form_number).val().trim().length !=0){
										 showConfirmationMessage=true;
										 return true;
									}
								 }else{
								     $('#ftpDiv_'+cur_form_number+' input').each(function(){
										 if($(this).val().trim(). length !=0){
											 showConfirmationMessage=true;
											 return true;
										 }
								     });
								 }
							});
							
							//console.log('showConfirmationMessage : '+ showConfirmationMessage);
							if(showConfirmationMessage){
								Ext.MessageBox.confirm('Confirm', 'Last image not uploaded. Are you sure you want to continue? ', function(btn){
									if(btn==='yes'){
										$('.container:visible form:visible').each(function(){
											$(this).parent().parent('.container').hide();
										});
										if($('.container:visible').length==0){
										   $('.container').remove();
										    win.hide();
										    $('#vendor_images').load("refreshVendorImageTable.html?car_id="+$('#carId').val());
										}else{
											win.syncShadow();
											$('.cancelButtonCls').hide();
											$('.uploadButtonCls').hide();
											$('.closeButtonCls').show();
										}
										return false;
								    }else if(btn==='no'){
								        return false;
								    }	
								});
							}else{
								$('.container:visible form:visible').each(function(){
									$(this).parent().parent('.container').hide();
								});
								if($('.container:visible').length==0){
									   $('.container').remove();
									    win.hide();
									    $('#vendor_images').load("refreshVendorImageTable.html?car_id="+$('#carId').val());
									}else{
										win.syncShadow();
										$('.cancelButtonCls').hide();
										$('.uploadButtonCls').hide();
										$('.closeButtonCls').show();
								}
							}
							$(buttonClicked).removeAttr('disabled');
							$(buttonClicked).css('color','#15428B');
						}		
					}			
				   , {
						text: 'Close',
						//hidden:true,
						cls:'closeButtonCls',
						handler: function(){
							//close the window if none of image uploding is in progress
							var imageUploadInprogress=false;
							if($('.container:visible form:visible').length==0){
								//check if image is inprogress or uploaded
								$('.container:visible').each(function(){
									if($(this).find('.progressImage:visible').length!=0){
										imageUploadInprogress = true;
									}
								});
								if(imageUploadInprogress){
									alert('Please wait until all images are uploaded successfully');
								}else{
									$('.container').remove();
									win.hide();
									$.ajaxSetup({ cache: false });
									$('#vendor_images').load("refreshVendorImageTable.html?car_id="+$('#carId').val());
								}
							}else{
								 alert('Please upload all the images before closing form');
							}
						}
					
				},	{	
						text: 'Upload',
						cls:'uploadButtonCls',
						handler: function(){
							//submit all the open vendorImage forms one by one
							var allFormUploaded = true;
							$('.container:visible form:visible').each(function(){
								var current_id = $(this).attr('id');
								var id_array = current_id.split('_');
								var cur_form_number = parseInt(id_array[1]);
								if(!submitVendorImageForm(cur_form_number)){
									allFormUploaded = false;
								}
							});	
							if(allFormUploaded){
							  $('.cancelButtonCls').hide();
							  $('.uploadButtonCls').hide();
							  $('.closeButtonCls').show();
							  win.syncShadow();
							}
								
							 
						}
				}]
		});
		win.show();
		
		$('#'+gen_id).load(url+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
			var $modal=$('#'+gen_id);
			if ($('div.ajaxContent',$modal).length < 1) {
				//session timeout or error - reload the page in the browser
				window.location.href = window.location.href;
			}
			 var patternType= $('.patternType').text();
			 if((!(patternType =='Product' || patternType =='OUTFIT')) && $('.vstyleNumber').val()==styleNumber){
				 cur_form_number =1;
				 $('.addImageButtonDiv_1').hide();
				 $('#imageType_1').parent().parent('li').addClass('hidden');
				 win.syncShadow();
				 $(buttonClicked).attr('disabled','disabled');
				 $(buttonClicked).css('color','gray');
			 }
			 
			 if ($('#uploadForm_1 select[name="imageType"]').val() == 'MAIN' || $('#uploadForm_1 select[name="imageType"]').val() == 'SWATCH') {
				 	$('.ShotTypeCd_1').hide('slow');
			 } 
			 else {
				 	$('.ShotTypeCd_1').show('slow');
				 	previousValue = $("#shotType1").val();
			 }
			 
			 alternateImageCount=$('#alternateImageCount').val();
			 if($('#uploadForm_1 select[name="imageType"]').val()==null){
				 //all images uploaded for this style+color
				 $('#uploadForm_1').hide();
			 	 $('#result_1').addClass('msgError');
			 	$('#result_1').html(err_all_image_uploaded);
				 $('.cancelButtonCls').hide();
				 $('.uploadButtonCls').hide();
				 $('.closeButtonCls').show();
				 
			 }else if($('#uploadForm_1 select[name="imageType"]').val() != 'ALT' && $('#uploadForm_1 select[name="imageType"] option').size()==1){
					$('.addButton').addClass('hidden');
			  }
			 win.syncShadow();
			 win.syncSize();
			 win.center();
		});
		
		
		$('.closeButtonCls').hide();
	});

	 
	 $('.replaceButton').live('click',function(){
		 var gen_id=Ext.id();		
		 $('body').append('<div id="'+gen_id+'" class="add_vendor_style_modal x-hidden"><div class="loading"></div></div>');
		 var currImageId=$(this).attr('id');
		 var url=CFG.urls.vendor_Image_reupload+currImageId;
		 Ext.MessageBox.confirm('Confirm', 'This action will delete the previous image and replace with the new one. Are you sure you want to replace the existing image?', function(btn){
			 if(btn==='yes'){
		 var win = new Ext.Window({
			title: 'Upload Images',
			autoHeight:true,	
			closable: false,
			modal:true,
			plain:true,
			bodyStyle:'background:white;',
			items: new Ext.Panel({
				    contentEl:gen_id,
				    border:false,
				    autoHeight:true
					}),
			height: 275,
			width: 550,
			buttons:[{
						text: 'Cancel',
						cls:'cancelReplaceCls',
						handler: function(){
							var showConfirmationMessage = false;
							if($('#imageLocationType_1').val()=='CD'){
								 if($('#cdImageFile_1').val().trim().length !=0){
									 showConfirmationMessage=true;
									
								}
							 }else{
							     $('#ftpDiv_1 input').each(function(){
									 if($(this).val().trim(). length !=0){
										 showConfirmationMessage=true;
										 return true;
									 }
							     });
							 }
							if(showConfirmationMessage){
								Ext.MessageBox.confirm('Confirm', 'Last image not uploaded. Are you sure you want to continue? ', function(btn){
									if(btn==='yes'){
										$('.container').remove();
										win.hide();
										return false;
								    }else if(btn==='no'){
								        return false;
								    }	
								});
							}else{
								$('.container').remove();
							    win.hide();
							}
						}		
					}, {
						text: 'Close',
						//hidden:true,
						cls:'closeReplaceCls',
						handler: function(){
							imageUploadInprogress = false;
							if($('.progressImage:visible').length!=0){
								alert('Please wait until all images are uploaded successfully');
							}else{
								$('.container').remove();
								win.hide();
								$.ajaxSetup({ cache: false });
								$('#vendor_images').load("refreshVendorImageTable.html?car_id="+$('#carId').val());
							}
							 
						}
				    },{	
						text: 'Upload',
						cls:'uploadReplaceCls',
						handler: function(){
						  var flag = submitVendorImageForm(1);
						  if(flag){
							$('.cancelReplaceCls').hide();
							$('.uploadReplaceCls').hide();
							$('.closeReplaceCls').show();
						  }
						  win.syncShadow();
						}
				   }]
		});
		win.show();
		$('.closeReplaceCls').hide();
		$('#'+gen_id).load(url+'&a_id='+UTIL.getAjaxId()+' div.ajaxContent',function(){
			var $modal=$('#'+gen_id);
			if ($('div.ajaxContent',$modal).length < 1) {
				//session timeout or error - reload the page in the browser
				window.location.href = window.location.href;
			}
			win.syncSize();
			win.center();
	     });
	 }
		if(btn==='no'){
			return false;
		}
	});	
		$('.closeReplaceCls').hide();		
	 }); 
//Added by AFUSYS3 for VIP

$('input[name="approveButton"]').live('click',function(){	
	 var approveId=$(this).attr('id');		
	 var rejectId= approveId.replace("approve","reject");  
	 //var replaceId= approveId.replace("approve","replace");  
	 Ext.MessageBox.confirm('Confirm', 'Are you sure you want to Approve this image?', function(btn){
		if(btn==='yes'){			
			var carId= $('input[name=carId]').val();
			var approvedVal="APPROVED";
			 var carsArray = new Array();
			 carsArray=approveId.split("_");				
			$.ajax({
				type: "GET",
				url: "editCarForm.html?carId="+carId,
				 cache: false,
				data:"happrove="+approvedVal+"&ImageId="+carsArray[1],
				success: function(data){				
					$("#t"+approveId).text('BUYER APPROVED');										
					$('#'+approveId).css('color','grey');
					$('#'+rejectId).css('color','grey');
					$('#'+rejectId).attr('disabled',true);
					$('#'+approveId).attr('disabled',true);
					$('#'+rejectId).removeAttr("name");
					$('#'+approveId).removeAttr("name");				
					}				
				,
				dataType:'JSON'
			});
			return false;
		}
		if(btn==='no'){
			return false;
		}
	});			
});
$('input[name="rejectButton"]').live('click',function(){	
	 var rejectId=$(this).attr('id');	
	 var approveId= rejectId.replace("reject","approve");	  
	 Ext.MessageBox.confirm('Confirm', 'Rejecting this image will delete it. Are you sure?', function(btn){
		if(btn==='yes'){																		
			var carId= $('input[name=carId]').val();
			var rejectedVal="REJECTED";
			 var carsArray = new Array();
			 carsArray=rejectId.split("_");
			 var replaceId=carsArray[1];
			$.ajax({
				type: "GET",
				url: "editCarForm.html?carId="+carId,
				 cache: false,
				data:"happrove="+rejectedVal+"&ImageId="+carsArray[1],
				success: function(data){
					$("#t"+approveId).text('FAILED');
					$("#l"+approveId).text('Deleted');
					$('#'+approveId).css('color','grey');
					$('#'+rejectId).css('color','grey');
					$('#'+rejectId).attr('disabled','true');
					$('#'+approveId).attr('disabled','true');
					$('#'+rejectId).removeAttr("name");
					$('#'+approveId).removeAttr("name");					
					$('#'+replaceId).attr("name");
					$('#'+replaceId).removeAttr('disabled');
					$('#'+replaceId).css('color','#15428B');
					$('#'+replaceId).addClass('replaceButton');
				 }
				,
				dataType:'JSON'
			});
			return false;
		}
		if(btn==='no'){
			return false;
		}
	});			
});
$('input[name="removeButton"]').live('click',function(){
	var removeId=$(this).attr('id');
	var carId= $('input[name=carId]').val();
	var approvedVal="REMOVED";
	var carsArray = new Array();
	carsArray=removeId.split("_");
	var activateUploadBtn = $(this).hasClass('patternImage');
	
	Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this image?', function(btn){
			if(btn==='yes'){																						
				$.ajax({
					type: "GET",
					 cache: false,
					url: "editCarForm.html?carId="+carId,
					data:"happrove="+approvedVal+"&ImageId="+carsArray[1],
					success: function(data){
						$('#'+removeId).closest('tr').hide('slow')									
					}
					,
					dataType:'JSON'
				});			
				if(activateUploadBtn){
					 $('.uploadButton').removeAttr('disabled');
					 $('.uploadButton').css('color','#15428B');
				}
					return false;	
			}
			if(btn==='no'){
				return false;
			}
	 });						 	
});		
//dom is ready...
});                                                    

function isFtpURLValid(strFtpUrl) {
    if (strFtpUrl!=null && strFtpUrl!= '' && strFtpUrl.substring(0,3).toLowerCase()=='ftp') {
        return true;
    }
    return false;
}

function isImageTypeAllowed(strFileName){
	var strArray = strFileName.split('.');
	var ext = strArray[strArray.length-1].toLowerCase();
	if($.inArray(ext, allowed_file_extn) == -1) {
		return false;
	}
	return true;
}

$('.viewImageLink').click (function() {
	var imageName = $(this).parent().children('input.imgName').val();
	$.ajax({
		type: "GET",
		dataType:'JSON',
		 cache: false,
		contentType: "application/json",
		url: "mcImageLink.html",
		data:"imageName="+imageName,
		success: function(data){		
		var w = 400;
        var h = 400;
        var left = Number((screen.width/2)-(w/2));
        var tops = Number((screen.height/2)-(h/2));
        window.open(data,'window_name', 'toolbar=no,location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no,copyhistory=no, width='+w+', height='+h+', top='+tops+', left='+left);		
		  return false;
			}		
	});
});


function submitVendorImageForm(cur_form_number){
	var isError = false;
	var stat_msg;
	var ftpUrl = null;
	var ftpFileName = null;
	//validate current form data -- validation start
	if($('#imageLocationType_'+cur_form_number).val()=='CD'){
			if($('#cdImageFile_'+cur_form_number).val()==null || $('#cdImageFile_'+cur_form_number).val()==''){
				 $('#cdImageFile_'+cur_form_number).siblings('label').addClass('fieldError');
				 isError=true;
				 stat_msg=err_msg_reqFieldMissing;
			}else if(!isImageTypeAllowed($('#cdImageFile_'+cur_form_number).val())){
				$('#cdImageFile_'+cur_form_number).siblings('label').addClass('fieldError');
				 isError=true;
				 stat_msg=err_msg_incorrectImageType;
			}else{
					$('#cdImageFile_'+cur_form_number).siblings('label').removeClass('fieldError');
			}
	}else{
		$('#ftpDiv_'+cur_form_number+' input').each(function(){
			if($(this).val()==null ||$(this).val()==''){
				if('ftpPath'!=$(this).attr('name')){
					$(this).siblings('label').addClass('fieldError');
					isError=true;
					stat_msg=err_msg_reqFieldMissing;
				}
			}else{
				$(this).siblings('label').removeClass('fieldError');
				if($(this).attr('name')=='ftpUrl'){
					ftpUrl = $(this).val();
				}else if($(this).attr('name')=='ftpFileName'){
					ftpFileName = $(this).val();
				}
				
			}
		});
		
		if(!isError && !isFtpURLValid(ftpUrl)){
			//$(this).siblings('label').addClass('fieldError');
			$('#ftpDiv_'+cur_form_number+' [name="ftpUrl"]').siblings('label').addClass('fieldError');
			isError=true;
			stat_msg=err_msg_incorrectFTPLocation;
			
		}else if(!isError && !isImageTypeAllowed(ftpFileName)){
			$('#ftpDiv_'+cur_form_number+' [name="ftpFileName"]').siblings('label').addClass('fieldError');
			isError=true;
			stat_msg=err_msg_incorrectImageType;
		}
		
	}

	if ($('#imageType_' + cur_form_number).val() == 'ALT') {
		if ($('#shotType' + cur_form_number).val() == '') {
			$('#shotType' + cur_form_number).siblings('label').addClass('fieldError');
			isError = true;
			stat_msg = err_msg_reqFieldMissing;
		} else {
			$('#shotType' + cur_form_number).siblings('label').removeClass('fieldError');
		}
	}
	//form data validation ends			
	
	if(isError){
		$('#result_'+cur_form_number).addClass('msgError');
		$('#result_'+cur_form_number).html(stat_msg);
		$('.cancelButtonCls').show();
		$('.uploadButtonCls').show();
		$('.closeButtonCls').hide();
		return false;
	}
		
	//create new ifame to hold the form submission result
	var iframe = "<iframe name='frame_"+cur_form_number+"' id='frame_"+cur_form_number+"' style='width:0;height:0;border:none;visibility:hidden' onload='onFrameLoad("+cur_form_number+");'></iframe>";
	$('#container_'+cur_form_number).append(iframe);
	
	//assign the new iframe as current form target
	$('#uploadForm_'+cur_form_number).attr('target','frame_'+cur_form_number);
	
	//submit current form and show progressbar
	$('#uploadForm_'+cur_form_number+ ' select[name="imageType"]').removeAttr('disabled');
	$('#uploadForm_'+cur_form_number).submit();
	if($('#uploadForm_'+cur_form_number+ ' select[name="imageType"]').val() == 'MAIN'){
	   isMainImageSelected=true;
	}else if($('#uploadForm_'+cur_form_number+ ' select[name="imageType"]').val() == 'SWATCH'){
	   isSwatchImageSelected=true;
	}else{
		alternateImageCount=alternateImageCount-1;
	}
	$('#uploadForm_'+cur_form_number).hide();
	$('#result_'+cur_form_number).removeClass('msgError');
	$('#result_'+cur_form_number).addClass('msgInfo');
	$('#result_'+cur_form_number).html('Uploading...');
	$('#progressImage_'+cur_form_number).css('display','block');
	return true;	
}

function onFrameLoad(cur_form_number) {
    var ajaxReponse =$("#frame_"+cur_form_number).contents().find("div").html();
	 var status = $.parseJSON(ajaxReponse);
	 var status_msg;
	 var result = false;
	// alert(status.uploaded);
	 if(status==null || status=='null'){
		 status_msg = err_msg_ImageUploadFailed;
	 }else if (status.uploaded == 'success') {
		 status_msg = sucess_msg.replace('image_name',status.fileName)
		 result = true;
		 
	 }else if(status.type=='CD'){
	   	 status_msg = err_msg_cdUploadFailed;
	 }else if(status.type=='FTP'){
	   	 //status_msg = err_msg_ftpUploadFailed;
	   	 status_msg = (status.ftpUploadFailedReason == 'unableToConnectFTP')? err_msg_incorrectFTPLogin : err_msg_incorrectFTPPath;
	 }else{
	   	 status_msg = err_msg_ImageUploadFailed;
	 }

   $('#result_'+cur_form_number).html(status_msg);
   $('#progressImage_'+cur_form_number).css('display','none');
   
   if(!result){
  	 	$('#uploadForm_'+cur_form_number).show('slow');
  	 	$('#result_'+cur_form_number).addClass('msgError');
		//disable the Image type dropdown if error while downloading the image 
		$('#uploadForm_'+cur_form_number+ ' select[name="imageType"]').attr('disabled','disabled');
		$('.cancelButtonCls').show();
		$('.uploadButtonCls').show();
		$('.closeButtonCls').hide();
   }else{
  	 	$('#result_'+cur_form_number).removeClass('msgError');
  	 	$('#result_'+cur_form_number).addClass('msgInfo');
   }
   $('#frame_'+cur_form_number).remove();
	$('#uploadForm_'+cur_form_number).removeAttr('target');
}

function showImageTypeHelp(){
	 $('.imageTypeHelpIconHidden a').click();
}
function showVendorImageApproveBtnHelp(){
	 $('.imageTypeHelpIconApprove a').click();
}
function showVendorImageRejectBtnHelp(){
	 $('.imageTypeHelpIconReject a').click();
}
function showVendorImageRemoveBtnHelp(){
	 $('.imageTypeHelpIconRemove a').click();
}
function showVendorImageReplaceBtnHelp(){
	 $('.imageTypeHelpIconReplace a').click();
}
function showArtDirectorImageStatusHelp(){
	 $('.artDirectorImageStatusHelp a').click();
}
