/*
 * belk.cars.size.js
 * Belk CARS - Size Conversion Management javascript
 */


$(document).ready(function(){
	
	var arch_cnt = 0;
	//alert("this iss");
	var listCookie = belk.cars.util.readCookie('sizelist');
	//alert("cookies"+listCookie);
	if(listCookie != null && listCookie != 'undefined' && listCookie != undefined && listCookie != '') {
		var cookieIds = listCookie.split(',');
		$('#tempRuleList').val(listCookie);
		$(".chkbox").each( function() {
			////alert("looping");
			$(this).attr("checked",status);
			if (cookieIds.indexOf($(this).attr('value')) != -1) {
				//alert("set checkbox"+$(this).attr('value'));
				$(this).attr('checked', 'checked');
		}

		});
		var vals = $('#tempRuleList').val();
		//alert("size="+vals.length+"vals="+vals);
		if(vals.length > 0){
			//alert("size enable ");
			setDeleteEnable();
		}else{
			//alert("size disab");
			setCreateEnable();
		}
		//alert("before calling");
		//alert("isAllChecked():"+isAllChecked());
		if(isAllChecked()){
			//alert("checkingggg");
			$('#checkall').attr('checked', 'checked');
		}else{
			//alert("not cehcking");
			$('#checkall').removeAttr('checked');
		}
	}else{
		//alert("enable create");
		setCreateEnable();
		$('#checkall').removeAttr('checked');
	}

  
	$(':checkbox').click(function () {
		//alert("first");
		var chk_status = $(this).attr('checked');
		//alert("stats="+chk_status);
		var vals = $('#tempRuleList').val();
		var val = $(this).attr('value');
		//alert("vals="+vals+"  val="+val);
		if(chk_status === true || chk_status === 'checked'){
			add(val)
		}else{
			remove(val);
		}
		vals = $('#tempRuleList').val();
		//alert("size="+vals.length);
		if(vals.length > 0 && vals != 'on'){
			//alert("size enable ");
			setDeleteEnable();
		}else{
			//alert("size disab");
			setCreateEnable();
		}
		if(isAllChecked()){
			$('#checkall').attr('checked', 'checked');
		}else{
			$('#checkall').removeAttr('checked');
		}
	});  
  
  
	  
	$("#delete").click( function()
           {
             //alert('button clicked');
			 Ext.MessageBox.show ({
					    title: 'Confirm',
						msg: 'Are you sure you want to delete these rules?',
						buttons: { yes: 'Yes',cancel: 'Cancel' } ,
						fn: function(btn) {
													
                         if(btn === 'cancel') {
							 //alert("cancel");
						       $(".chkbox").each( function() {
							   var status = $(this).attr('checked');
							       if (status === true || status === 'checked'){
							         $(this).removeAttr("checked");
							       }
							   });
							   $('#checkall').removeAttr('checked');
								setCreateEnable();
								$('#tempRuleList').val('');
								return false;
							} else {
								//alert("yess");
								var listVal = $('#tempRuleList').val();
								$('#sizeRulesList').val(listVal);
								//assign search values
								var urlValues='';
								var searchValues = getSearchValues();
								if(searchValues != '')
									urlValues = getURLValues();
								searchValues = searchValues+urlValues;
								//return false;
								$('#searchList').val(searchValues);
								//alert("searchValues"+searchValues);
								//remove cookie and session value
								$('#tempRuleList').val('');
								belk.cars.util.createCookie('sizelist','');
								$("form#deleteForm").submit(); 
							}
						}
							
		        });
           });


	$('a.remove').click(function(){return confirm('<fmt:message key="size.main.confirm.delete"/>');});
	
	$('body.admin table.dstable tr').hover(function(){
              $('td',this).addClass('trHover');
              $(this).click(function(){
              $('td',this).removeClass('trHover');
              $('td',$(this).parent()).removeClass('trSelected');
              $('td',this).addClass('trSelected');
              });
            },function(){
              $('td',this).removeClass('trHover');
              });
	$(window).bind('beforeunload', function (){
			//alert("unloading");
			//var listCookie = belk.cars.util.readCookie('sizelist');
			var vals = $('#tempRuleList').val();
			belk.cars.util.createCookie('sizelist',vals);
			  
	    } );	

	
    
	});

	$("#searchSubmit").click( function()
   {
             $('#tempRuleList').val('');//clear the hidden value to delete rules
			
    });
	
	function clearHidden()
   {
             $('#tempRuleList').val('');//clear the hidden value to delete rules
			
   };
	
	$("#createrule").click( function()
   {
             //alert('button clicked');
			 var deptCode=$("#txt_deptCode").val();
			 var classNumber=$("#txt_classNumber").val();
			 var vendorNumber=$("#txt_vendorNumber").val();
			 var sizeName = $("#txt_sizeName").val();
			 var conversionSize = $("#txt_sizeConversionName").val();
			 var vars;
			 var dept=0;
			 var classN=0;
			 var vendor=0;
			 var isValid=true;
			 var classVals;
			 var vendorVals;
			 var deptVals;
			 
			 if(deptCode != ""){
				 vars = deptCode.split(",");
				 for (var i=0;i<vars.length;i++) {
						if(vars[i] != ""){
							if(isValidStr(vars[i])){
								dept++;
								if(i==0)
									deptVals = vars[i].trim();
								else
									deptVals += ","+vars[i].trim();

							}else
								isValid=false;
						}
				  }
			 } 
			 if(classNumber != ""){
				 vars = classNumber.split(",");
				 for (var i=0;i<vars.length;i++) {
						if(vars[i] != ""){
							if(isValidStr(vars[i])){
								classN++;
								if(i==0)
									classVals = vars[i].trim();
								else
									classVals += ","+vars[i].trim();
							}else
								isValid=false;
						}
				  }
			 }
			 if(vendorNumber != ""){
				 vars = vendorNumber.split(",");
				 for (var i=0;i<vars.length;i++) {
						if(vars[i] != ""){
							if(isValidStr(vars[i])){
								vendor++;
							if(i==0)
									vendorVals = vars[i].trim();
								else
									vendorVals += ","+vars[i].trim();
							}else
								isValid=false;
						}
				  }
			 }

			var deptValid = checkForDuplicate(deptCode);
			var classValid = checkForDuplicate(classNumber);
			var vendorValid = checkForDuplicate(vendorNumber);

			//alert("dept="+dept+" classN="+classN+"  vendor="+vendor+"  isValid="+isValid);
			if( (dept > 1 && classN > 1) || (classN > 1 && vendor > 1) || (vendor > 1 && dept > 1) || !isValid || !deptValid || !classValid || !vendorValid){
				//alert("inside error1");
				$("#errorDiv").html('Size Rule cannot be created due to incorrect configuration. Please try again');
				//$testerror.innerHTML='Size Rule cannot be created due to incorrect configuration. Please try again';
				return false;
			}else{
				//alert("inside else before submitting");
				//document.getElementById("error").innerHTML='&nbsp;';
				$('#txt_deptCode').val(deptVals);
				$('#txt_classNumber').val(classVals);
				$('#txt_vendorNumber').val(vendorVals);

				$("form#sizeConversionForm").submit(); 
				return false;
			}	
         });

	 function checkedAll(status) {
		$(".chkbox").each( function() {
			//alert("satu check all="+ status+"   value"+$(this).attr('value'));
			if(status == true){
				add($(this).attr('value'));
				//alert("adddddd");
			}else{
				remove($(this).attr('value'));
				//alert("removeeee--");
			}
			$(this).attr("checked",status);
		})
	  };

	function isAllChecked(){
		var cnt=0;
		var allCheck = true;
		$(".chkbox").each( function() {
			//alert("looping "+$(this).attr('value'));
			var chk_status = $(this).attr('checked');
			//alert("stats="+chk_status);
			//alert("value="+$(this).attr('value'));			
			if($(this).attr('value') != undefined){
				if (chk_status === true || chk_status === 'checked'){
					cnt++;
				}else {
					//alert("before returning false");
					allCheck= false;
				}
			}
			
	
		})
			//alert("cnt="+cnt);
			//if(cnt > 0)
				return allCheck;
			
	};

	function add(val){
		var vals = $('#tempRuleList').val();
		//var val = $(this).attr('value');
		//alert("vals="+vals+"  val="+val);
				if(vals==''){
					////alert("insde empty");
					//alert("tru"+val);
					vals = val;	
					$('#tempRuleList').val(vals);
				}else if(val!='on' && typeof val!='undefined'){
					//alert("vals.indexOf(val)"+vals.indexOf(val));
					if (vals.indexOf(val) == -1){
						//alert("inside add");
					//	//alert("tru1"+val);
						vals += ","+val; // or ", id" depending on how you are seperating the ids
						//alert("hidden 1 ="+vals);
						$('#tempRuleList').val(vals);
					}else{
						//alert("alreay dont do anythiong");
					}
				}
	};

	function remove(val) {
		
				// remove from hidden field
				
				var tempIds = $('#tempRuleList').val().split(',');
				//alert("tempid="+tempIds);
				tempIds.splice(tempIds.indexOf(val), 1);
				////alert("befor join");
				tempIds.join(',');
				//alert("hidden 2="+tempIds);
				$('#tempRuleList').val(tempIds);

	};

	function setDeleteEnable(){
		$('#delete').removeAttr('disabled');
		$('#delete').removeAttr('style');
		$('#create').attr('disabled','disabled');
		$('#create').attr('class','disablebtnright');
		$('#create').attr('onclick','return false');
		//$('#create').attr('disabled','disabled');
	};

	function setCreateEnable(){
		$('#create').removeAttr('onclick');
		$('#create').removeAttr('disabled');
		$('#create').attr('class','btn');
		$('#delete').attr('disabled','disabled');
		$('#delete').attr('style','color:gray');
	};

	function getSearchValues(){
		var result='';
		var comma;
		if($('#sizeName').val() != '')
			result=result+"&sizeName="+$('#sizeName').val();
		if($('#conversionName').val() != '')
			result=result+"&conversionName="+$('#conversionName').val();
		if($('#deptCode').val() != '')
			result=result+"&deptCode="+$('#deptCode').val();
		if($('#classNumber').val() != '')
			result=result+"&classNumber="+$('#classNumber').val();
		if($('#vendorCode').val() != '')
			result=result+"&vendorCode="+$('#vendorCode').val();
		if($('#facetSize').val() != '')
			result=result+"&facetSize="+$('#facetSize').val();
		if($('#facetSubSize').val() != '')
			result=result+"&facetSubSize="+$('#facetSubSize').val();
		
		return result;

	};

	function getURLValues(){
		var result='';
		var prmstr = window.location.search.substr(1);
		var prmarr = prmstr.split ("&");
		//var params = {};

		for ( var i = 0; i < prmarr.length; i++) {
			var tmparr = prmarr[i].split("=");
			if(tmparr[0].indexOf("d-") > -1){
				result=result+"&"+tmparr[0]+"="+tmparr[1];
			}
		}
		//result=result+"&facetSubSize="+$('#facetSubSize').val();
		
		return result;

	};


	function isValidStr(str){
		return !/[~`!#$%\^&*+=\-\[\]\\;/{}|:<>\?]/g.test(str); 
	};

	function checkForDuplicate(str) {
		var unique_values = {};
		var list_of_values = [];
		var vars;
		var isNotDuplicate = true;

		if(str != ""){
				 vars = str.split(",");
				 for (var i=0;i<vars.length;i++) {
						if(vars[i] != ""){
							if ( ! unique_values[vars[i]] ) {
								unique_values[vars[i]] = true;
								list_of_values.push(vars[i]);
								//alert("not unique "+vars[i]);
							} else {
								// We have duplicate values!
								//alert("uniquuuu"+ vars[i]);
								isNotDuplicate = false;
								
							}
						}
				  }
		} 
		return isNotDuplicate;

	};

 	
	//dom is ready...
	//$(document).ready(belk.cars.supercolor.init);
	
	