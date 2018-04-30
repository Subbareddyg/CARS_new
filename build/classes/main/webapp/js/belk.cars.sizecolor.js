/*
 * belk.cars.sizecolor.js
 * Belk CARS - Super color/Size Conversion Management javascript
 */


$(document).ready(function(){
	
	$(".skus_size > li").each(function(){
	      var values = [];
	      var parent = this;
		  $(this).find(".exclusiveSColorSelection").each(function(){
		     values.push(this.value);
			}).change(function(){
		       var found = $.inArray(this.value,values);
			   if(found != -1){
				Ext.MessageBox.show({
			           title: 'Super Color Selection',
			           msg: 'This Super Color is already selected! Please select a different one.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
				$(this).find('option').attr('selected', false);
				$(this).val('-1');    //for select-box to have an index back to -1 in IE. 
	         } else {
			  var index = $(this).attr("class").match(/\d/)[0] - 1;
	          values[index] = this.value;
			 }
	     });
	
		  $(this).find(".colorcode").change(function(event) {
		   	var colorCode= $(this).val();
		   	var superColor1=$(parent).find(".superColor1");
		   	var hiddenSuperColor1=$(parent).find(".hiddenSuperColor1");
		   	if(colorCode.length < 3){
		   		Ext.MessageBox.show({
			           title: 'Required fields missing',
			           msg: 'Color code should have 3 digits, please change Color.',
			           buttons: Ext.MessageBox.OK,
			           icon: 'ext-mb-error'
			       	});
		   		superColor1.val('-1');
		   		return false;
		   	}
		   	
			$.get('getSuperColorName.html?method=getSuperColor&colorCode='+colorCode, function(data) {
			  //	var colormap = JSON.parse($(data).html().trim());
				var colormap = Ext.decode($(data).html().trim());
				var colorName=colormap.superColor1;
				if(colorName=='Error'){
					Ext.MessageBox.show({
				           title: 'Required fields missing',
				           msg: 'Super Color1 name not found, please change Color.',
				           buttons: Ext.MessageBox.OK,
				           icon: 'ext-mb-error'
				       	});
					superColor1.val('-1');
					return false;
				} else {
					 var color = $.inArray(colorName,values);
					 if(color > 0){
					   Ext.MessageBox.show({
					           title: 'Required fields missing',
					           msg: colorName +' name already selected, please change Color.',
					           buttons: Ext.MessageBox.OK,
					           icon: 'ext-mb-error'
					       	});
						superColor1.val('-1');
						return false;
					 } else {
						superColor1.val(colorName);
						hiddenSuperColor1.val(colorName);
						values[0] = colorName;
						}
				}
			});
		});	
	});
    
});

 $("#txt_colorCodeEnd").focus(function () {
	  $(this).after("<span class='focus'> (Note:End Code should be greater than a Begin Code)</span>");
	  $("span").filter('.focus').fadeOut(4000);
    });
 
	 
   
 $("#superColorForm").submit(function () {
	 var colorCode=$("#txt_superColorCode").val();
	 var colorName=$("#txt_superColorName").val();
	 var colorBegin=$("#txt_colorCodeBegin").val();
	 var colorEnd=$("#txt_colorCodeEnd").val();
	 if(colorCode==""){
	 		Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please enter Super Color Code.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
	 		return false;
	 	}

	 if( colorName==""){
	 		Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please enter Super Color Name.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
	 		return false;
	 	}
	 
	 if(colorBegin==""){
	 		Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please enter Super Color Code Begin.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
	 		return false;
	 	}
	 
	 if(colorEnd==""){
	 		Ext.MessageBox.show({
		           title: 'Required fields missing',
		           msg: 'Please enter Super Color Code End.',
		           buttons: Ext.MessageBox.OK,
		           icon: 'ext-mb-error'
		       	});
	 		return false;
	 	}
 });


	function onlyNumber(evt) {
		 var charCode = (evt.which) ? evt.which : event.keyCode
		 if (charCode > 31 && (charCode < 48 || charCode > 57))
			 return false;

	     return true;
	};

	function onlyAlphabets(e)
	{
		isIE=document.all? 1:0
		keyEntry = !isIE? e.which:event.keyCode;
		if(((keyEntry >= '65') && (keyEntry <= '90')) || ((keyEntry >= '97') && (keyEntry <= '122')) || (keyEntry=='46') || (keyEntry=='32') || keyEntry=='45' ) 
			return true;  
		else if (event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 46
	     || event.keyCode == 37 || event.keyCode == 39) {
	        return true;
	    }else
			return false;
	};
	
	
	//dom is ready...
	//$(document).ready(belk.cars.supercolor.init);
	
	