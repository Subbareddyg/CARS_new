<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>CAR Reports</title>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div id="execute_report" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Execute Report
	</div>
	<div class="x-panel-body">
	<c:url value="/admin/report/list.html" var="formAction"/>
	<form:form commandName="reportForm" method="post" 
		action="${formAction}">
		<ol class="dropdown-show">
			<li class="dropdown">
				<label>Report Name</label><br/>
				<form:select path="reportType" id="reportType"
					items="${reportTypes}" itemLabel="name" itemValue="code"
					cssClass="text dropdown-show" cssErrorClass="text medium error">
				</form:select>
			</li>
			<li class="ALL">
				<label>Start Expected Ship Date</label>
				<input type="text" name="StartExpectedShipDate" id="StartExpectedShipDate"/>
			</li>			
			<li class="ALL">
				<label>End Expected Ship Date</label>
				<input type="text" name="EndExpectedShipDate" id="EndExpectedShipDate"/>
			</li>		
			
			<!-- ----------------- Start of code added for LATE CAR MONITORING REPORT ------------- -->
			<li class="LATE_CAR_MONITORING_REPORT">
				<label>Start Due Date</label>
				<input type="text" class="date restricted" name="StartDueDate" id="StartDueDate"/>
			</li>			
			<li class="LATE_CAR_MONITORING_REPORT">
				<label>End Due Date</label>
				<input type="text"  class="date restricted" name="EndDueDate" id="EndDueDate" />
			</li>			
			<li class="LATE_CAR_MONITORING_REPORT">
				<input type="radio" name="Department" id="UserDepartmentOnly" checked="checked"/> User Department Only
				<input type="radio" name="Department" id="DeptNumber"/> Department Number
				<input type="text" name="DeptNumberText" id="DeptNumberText" disabled="disabled" maxlength="4"  style="width:50px;" />
			</li>
			<!-- ----------------- End of code added for LATE CAR MONITORING REPORT ------------- 	 -->
			
			<li class="CAR_SAMPLE_COORDINATOR_TEMPLATE">
				<label>Start Turnin Date</label>
				<input type="text" name="StartTurninDate" id="StartTurninDate"/>
			</li>			
			<li class="CAR_SAMPLE_COORDINATOR_TEMPLATE">
				<label>End Turnin Date</label>
				<input type="text" name="EndTurninDate" id="EndTurninDate"/>
			</li>	
			<li class="OMA">
				<label>Start Date</label>
				<input type="text" name="startDate" id="StartDate"/>
			</li>
			<li class="OMA">
				<label>End Date</label>
				<input type="text" name="endDate" id="EndDate"/>
			</li>
			<li class="OMA*">
				<input type="radio" name="group1" value="WithSubTotal"> With SubTotal<br>
				<input type="radio" name="group1" value="WithOutSubTotal" checked> Without SubTotal<br>

			</li>		
			<li class="CAR_ATTRIBUTE_VALUE">
				<label>Attributes</label><br/>
				<form:select path="attributeName" id="attributeName"
					items="${attributes}" itemLabel="attrName" itemValue="attrValue"
					cssClass="text autocomplete" cssErrorClass="text medium error">
				</form:select>
			</li>
			<li class="userdeptonly_chk">
				<input type="checkbox" name="UserDepartmentOnly" id="UserDepartmentOnly" value="true" checked="checked"/>
				<label>User Department Only</label>
			</li>
			<li class="ARCHIVED">
				<input type="checkbox" name="IncludeArchiveCars" id="IncludeArchiveCars" value="true" checked="checked"/>
				<label>Include Archived Cars</label>
			</li>	
						
			<li class="CAR_EXECUTIVE_SUMMARY">
				<input type="checkbox" name="IncludeClosedCars" id="IncludeClosedCars" value="true" checked="checked"/>
				<label>Include Closed Cars</label>
			</li>	
				
			<li  class="BTN">
				<input class="btn" type="submit" name="execute" value="Execute"  />
			</li>
		</ol>
	</form:form>
</div></div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">

$(document).ready(function(){
	// web 2.0 filter :)
	// datefields
	var CFG=belk.cars.config;
	new Ext.form.DateField({
		applyTo:'StartExpectedShipDate',
        fieldLabel:'Start Ship Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
	new Ext.form.DateField({
		applyTo:'EndExpectedShipDate',
        fieldLabel:'End Ship Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
	new Ext.form.DateField({
		applyTo:'StartTurninDate',
        fieldLabel:'Start Turnin Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
	new Ext.form.DateField({
		applyTo:'EndTurninDate',
        fieldLabel:'End Turnin Date',
        allowBlank:true,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
	 new Ext.form.DateField({
    	applyTo:'StartDate',
    	fieldLabel:'Start Date',
    	allowBlank:true,
    	format:CFG.dateFormat,
    	altFormats:CFG.dateAltFormats
    });
	 new Ext.form.DateField({
    	applyTo:'EndDate',
    	fieldLabel:'End Date',
    	allowBlank:true,
    	format:CFG.dateFormat,
    	altFormats:CFG.dateAltFormats
    });
       
	$('select.autocomplete').each(function(){
		var $this=$(this);
		var name=$this.attr("name");
		var combo=new Ext.form.ComboBox({
	        typeAhead:true,
	        triggerAction:'all',
	        transform:this,
	        width:250,
	        forceSelection:false,
			cls:$this.is('.recommended')?'recommended':'',
			id:id,
			disabled:$this.is(':disabled')
	    });
		combo.on({
				'blur':function(that){
				$('input[name="'+name+'"]').val(that.getRawValue());
			}
		});
	});
	
	$('.btn').click(function() {
	var $check = $dropdown.val();
	if ($check.indexOf('LATE_CAR_MONITORING_REPORT') > -1 ) {
		var dateReg=/^(0[1-9]|1[012])[/](0[1-9]|[12][0-9]|3[01])[/](19|20)\d\d$/;
	    var errorDate=false;
		var startDueDate=$('#StartDueDate').val();
		var endDueDate=$('#EndDueDate').val();
		var currentDate=new Date();	
		if($('#StartDueDate').val().length > 0 || $('#EndDueDate').val().length > 0){
			if ( (!dateReg.test(startDueDate)) ||(!dateReg.test(endDueDate))
			     ||(new Date (startDueDate) > new Date (currentDate)) 
			     || (new Date (endDueDate) > new Date (currentDate)) ) {
				errorDate=true;
			}	
		}
	    var errorDept=false;
	    if ($('#DeptNumber').is(':checked')) {
		   var inputVal = $('#DeptNumberText').val();
		   if (! $('#DeptNumberText').val().length > 0) {
		       errorDept=true;
		   }
		   if ($('#DeptNumberText').val().length > 0){
		 	   var numericReg = /^\d*[0-9](|.\d*[0-9]|,\d*[0-9])?$/;
		  	   if(!numericReg.test(inputVal) || $('#DeptNumberText').val()==='') {
			  	   errorDept=true;
			  	   $('#DeptNumberText').val('');
		 	   }  	   
	 	   } 
	 	}
 	   if (errorDate && !errorDept) {
	   Ext.MessageBox.show({
           title: 'Invalid Date',
           msg: 'The date entered is invalid or in the future.',
           buttons: Ext.MessageBox.OK,
           icon: 'ext-mb-error'
				       	});
 		return false;
 	    }
 		 
 	    if(errorDept && !errorDate){
 		Ext.MessageBox.show({
  			 title: 'Numeric value required',
  			 msg: 'Please enter valid department number.',
  			 buttons: Ext.MessageBox.OK,
  			 icon: 'ext-mb-error'
  			 });
 		 return false;
 		 }
 		 
 		 if(errorDate && errorDept){
 		 Ext.MessageBox.show({
  			 title: 'Invalid fields',
  			 msg: 'Please enter valid date and department.',
  			 buttons: Ext.MessageBox.OK,
  			 icon: 'ext-mb-error'
  			 });
 		 return false;
 		 } }
 	   });
 		
		$('input.date').each(function(){
		if ($(this).is('.restricted')) {
			var df=new Ext.form.DateField({
				applyTo:this,
				fieldLabel:'Change Date',
				allowBlank:true,
				format:CFG.dateFormat,
				altFormats:CFG.dateAltFormats,
				maxValue:new Date()
			});
		}
		else {
			var df = new Ext.form.DateField({
				applyTo: this,
				fieldLabel: 'Change Date',
				allowBlank: true,
				format:CFG.dateFormat,
				altFormats:CFG.dateAltFormats
			});
		}
	});
   		
	var userTypeCd = '<c:out value="${UserTypeCd}" />';
	var $dropdown = $('#reportType');
	if ($dropdown) {
		function showCurrent(){
			$('ol.dropdown-show > li').hide();
			var $check = $dropdown.val();
			if($check.indexOf('OMA') > -1 ){
				if(($check.indexOf('VENDOR_SETUP_BY_MONTH') > -1 )  ){
					$('ol.dropdown-show > li.OMA').show();
				}else{
					
				}
				$('ol.dropdown-show > li.OMA*').show();
			}else {
			if($check.indexOf('LATE_CAR_MONITORING_REPORT') > -1 ){
			$('ol.dropdown-show > li.LATE_CAR_MONITORING_REPORT').show();
			}else{
			$('ol.dropdown-show > li.' + $dropdown.val()).show();
			$('ol.dropdown-show > li.ALL').show();
			}
			//ARCHIVED
				if($check.indexOf('SEARCH') > -1){
					// Do not show the archived checkbox
				}else{
					$('ol.dropdown-show > li.ARCHIVED').show();
				}
			}
			$('ol.dropdown-show > li.dropdown').show();
			$('ol.dropdown-show > li.BTN').show();
			if($check.indexOf('LATE_CAR_MONITORING_REPORT') > -1 ){
			$('ol.dropdown-show > li.LATE_CAR_MONITORING_REPORT').show();
			//Do not show the check box for the User Deparments
			}else{
			$('ol.dropdown-show > li.userdeptonly_chk').show();
			}
			if(userTypeCd == 'BUYER'){
				$('ol.dropdown-show > li.userdeptonly_chk').attr('checked', true);
				$('ol.dropdown-show > li.userdeptonly_chk').show();
			  }
	     }
	     
		 // Added for late car monittoring report
	     $('#UserDepartmentOnly').click(function() {
	     $('#UserDepartmentOnly').attr("value", "false");
	     $('#DeptNumberText').attr("disabled", "disabled"); });
	     
    	 $('#DeptNumber').click(function() {
    	 $('#DeptNumberText').removeAttr("value");
    	 $('#UserDepartmentOnly').attr("value", "false");
	     $('#DeptNumberText').removeAttr("disabled");
	     $('#DeptNumberText').focus();  });
	     		
		showCurrent();
		$dropdown.change(showCurrent);
		
	}
});


</script>
]]>
</content>