<%@ include file="/common/taglibs.jsp"%>

<head>  
     <title>
    	Vendor Shipping Fees
    </title>
    
    <meta name="heading" content=""/>
    <meta name="menu" content="MainMenu"/>
     
        
</head>
<body class="admin">
<script language="javascript">

//dropship phase 2: added code fix for firefox browser onload
var browserName=navigator.appName; // Get the Browser Name
if(browserName=="Microsoft Internet Explorer") // For IE
{
	window.onload=init; // Call init function in IE
}
else
{
	if (document.addEventListener) // For Firefox
	{
		document.addEventListener("DOMContentLoaded", init, false); // Call init function in Firefox
	}
}

	 function init(){
		 document.getElementById("tab7").className="activeTab";
		 var x= new Date();
		 //alert(x);
		 if(document.getElementById("EffectiveDate").value == ''){
			 var  m = x.getMonth() +1,
			  d = x.getDate(),
			  y = x.getYear();
			 document.getElementById("EffectiveDate").value = (m<10?"0"+m:m)+"/"+(d<10?"0"+d:d)+"/"+y;
             //document.getElementById("EffectiveDate").value = "07/07/2000";
			 }
	}
	function checkForDate(){
		var effectiveDate=document.getElementById("EffectiveDate").value;
		var date = effectiveDate.split("/");
		var x= new Date();
		var currentMonth = x.getMonth() +1,
			  currentDay = x.getDate(),
			  currentYear = x.getYear();
		 if(currentYear >= date[2]){
			if(currentMonth == date[0]){
				if(currentDay > date[1]){
					document.getElementById("EffectiveDate").value = (currentMonth<10?"0"+currentMonth:currentMonth)+"/"+(currentDay<10?"0"+currentDay:currentDay)+"/"+currentYear;
				}
			} else if(currentMonth > date[0]){
				document.getElementById("EffectiveDate").value = (currentMonth<10?"0"+currentMonth:currentMonth)+"/"+(currentDay<10?"0"+currentDay:currentDay)+"/"+currentYear;
			}
		 } 
	}
	function editClicked(){
		//alert(); 
	 	document.getElementById("changeDetails").style.display = 'none';
		document.getElementById("editDetails").style.display = 'block';
		document.getElementById("editButton").disabled = 'disabled';
		enableOtherFields();
		checkForDate();
	 }  

	 function enableOtherFields(){
		 var i=0;
		 var allowable;
		 var allow;
		 for(i=0;i<5;i++){
			 allow=document.getElementById("vendorFees["+i+"].allow").value;
			 if(allow == 'Y' && document.getElementById("perOrderAmt"+i) != null){
			 	 document.getElementById("perOrderAmt"+i).disabled=false;
			 }
			 if(allow == 'Y' && document.getElementById("perItemAmt"+i) != null){
				 document.getElementById("perItemAmt"+i).disabled=false;
			 }
			 document.getElementById("allowable"+i).disabled=false;
		 }
		 if(document.getElementById("error") != null){
		 	document.getElementById("error").innerHTML='&nbsp;';
		 }
		 document.getElementById("EffectiveDate").disabled=false;
		 document.getElementById("summary").disabled=false;
		 document.getElementById("cancelBtn").disabled=false;
		 document.getElementById("saveBtn").disabled=false;
	 }
	function imposeMaxLength(Object, MaxLen)
	{
	  return (Object.value.length < MaxLen);
	}

function setFeeAllowable(box , counter1) {
	var allowable  = "vendorFees[" + counter1 +"].allow";
	var perOrderAmt = "perOrderAmt" + counter1;
	var perItemAmt = "perItemAmt" + counter1;
	var feeId= "feeId"+counter1;
	var valFeeId = document.getElementById(feeId).value; 
	
	if(box.checked && (valFeeId =='1' || valFeeId =='2')) {
		//alert("Enabling");
		document.getElementById(perOrderAmt).disabled=false;
		document.getElementById(perItemAmt).disabled=false;
	} else {
		if(valFeeId =='1' || valFeeId =='2'){
				document.getElementById(perOrderAmt).value='00.00';
				document.getElementById(perItemAmt).value = '00.00';
				document.getElementById(perOrderAmt).disabled=true;
				document.getElementById(perItemAmt).disabled=true;
		}else{
				//Commented this part because the text fields are removed as per instructions of Daneen.
				//document.getElementById(perOrderAmt).value='';
				//document.getElementById(perItemAmt).value = '';		
		}
		document.getElementById(allowable).value = 'N';
		
		
	}
	if(box.checked)
	{
		document.getElementById(allowable).value = 'Y';
	}	
}  
function verifyCancel()
{
	var stay=confirm("Are you sure you want to cancel?");
	var formAction = "vendorShippingFee.html?method=load";
	if (stay){
		document.vendorFeesForm.action=formAction;
		document.vendorFeesForm.submit();
	}
	else{
	 return false;
	}
}

function disableField(box,counter,x)
{
	var field;
	if(x > 0 ){
		field = "perItemAmt" + counter;
	}else if(x < 0){
		field = "perOrderAmt" + counter;
	}
	if(box.value > 0 || box.value < 0){
	document.getElementById(field).disabled=true;
	
	} else{
	//alert(box.value);
	document.getElementById(field).disabled=false;
	}	
}

function any_real_number_with_set_number_decimals(value,min,max) { 
  //Accepts number with decimal but it must have at least the min and at most the max places after the decimal
  var re = new RegExp("^-?\\d+\\.\\d{" + min + "," + max + "}?$");
  return re.test(value);
}
function checkEnteredNumber(numberField,fieldValue){
	var decallowed = 2;  // how many decimals are allowed?
	var dectext = 0;
	
	var valueOfField = 0;
	var numberBits = 0;
	//End of parameteres
		 if (isNaN(fieldValue)) {
			dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
			if(fieldValue.indexOf('.') == 0 && dectext.indexOf('.') == -1){
				return true;
			}
			valueOfField = fieldValue.substring(0, (fieldValue.length-1));
			//alert('Field Value : '+ valueOfField );
			while(isNaN(valueOfField) && valueOfField.length > 0){
				//alert('Field Value : '+ valueOfField );
				valueOfField = valueOfField.substring(0, (valueOfField.length-1));
			}
			numberField.value=valueOfField;
			numberField.focus();
		}
		
		if (fieldValue.indexOf('.') == -1){
			if(fieldValue.length <= 10){
				return true;
			}else{
				numberField.value=fieldValue.substring(0, 10);
				numberField.focus();
			}
		}else {
			//Number bits check
			numberBits = fieldValue.substring(0,fieldValue.indexOf('.')-1);
			//alert('Number bits:'+ numberBits);
			dectext = fieldValue.substring(fieldValue.indexOf('.')+1, fieldValue.length);
			if(numberBits.length > 10){
				//alert('Changing the bits as they are exceeding to 10');
				fieldValue = numberBits.substring(0, 10);
				fieldValue = fieldValue +'.'+ dectext;
				numberField.value = fieldValue;
			}
			//Decimal point check
			
			if (dectext.length > (decallowed)){
				//alert('dectext is greater.');
				numberField.value=fieldValue.substring(0, (fieldValue.length-(dectext.length - decallowed)));
				//numberField.value=fieldValue.substring(0, 2);
				numberField.focus();
			 }
			else {
					if(fieldValue.substring(0,fieldValue.indexOf('.')-1).length < 10){
						return true;
					}else{
						//alert('Inside Else');
						fieldValue = numberBits.substring(0, 10);
						fieldValue = fieldValue +'.'+ dectext;
						numberField.value = fieldValue;
						return false;
					}
			}
		}
	
}
</script>
<c:if test="${sessionScope.vndrFulfillmentService == null}">
	<c:url value="/mainMenu.html" var="dashboardUrl"/>
	<jsp:forward page="${dashboardUrl}" />
</c:if>

<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
				<%@ include file="headerTabsForVendor.jsp"%>
			</c:if>	
			<c:if test="${sessionScope.vndrFulfillmentService.vendorID == null}">
				<%@ include file="headerTab.jsp"%>
			</c:if>


	<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
	<%@ include file="breadcrumbForVendorFees.jsp"%>
</c:if>	

<b>Vendor Shipping Fees</b>
<div class="cars_panel x-hidden" style="margin: 10px 0px 0px;">
		<%@ include file="omaInfoSection.jsp" %>
	</div>

<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">
	<div class="x-panel-body">
		<b>Change History</b>
		<br>
		<div id="viewFeesRequest">
			<display:table name="venderFeeRequestList" cellspacing="0" cellpadding="0" requestURI="/oma/vendorShippingFee.html?method=load"  
				    defaultsort="1" id="vendorFeeRequest" pagesize="25" class="dstable" sort="list">
				<div>
				<c:url value="/oma/vendorShippingFee.html?d-4314083-p=${pagination}&method=show&id=${vendorFeeRequest.vendorFeeRequestId}" var="url"/>
				<display:column   comparator="com.belk.car.util.LongComparator" sortable="true" titleKey="ID"	class="vendorFeeRequestId" style="width: 7%; text-align: center "  >
					<a class="alink" id="${vendorFeeRequest.vendorFeeRequestId}" style="margin-top:0;" href='<c:out value="${url}" escapeXml="false"/>' id="a_view_all"><c:out value="${vendorFeeRequest.vendorFeeRequestId}"/></a>
				</display:column>
				
				
				<display:column property="effectiveDate" format="{0,date,MM/dd/yyyy}" titleKey="Change Effective Date" style="width: 15%; text-align: center"/> 
				
				<display:column  titleKey="Current"  style="width: 10%; text-align: center" class="currentFee">
					<c:choose>
						<c:when test="${sessionScope.currentVendorFeeRequestId == vendorFeeRequest.vendorFeeRequestId}">
							<c:out value="Yes"/>
						</c:when>
						<c:otherwise>
							<c:out value="No"/>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column property="feeRequestDesciption" titleKey="Change Summary" style="width: 30%"/>
				<display:column property="createdDate" format="{0,date,MM/dd/yyyy}" titleKey="Date Created" style="width: 25%; text-align: center"/>
				<display:column property="createdBy" titleKey="Created By" style="width: 25%"/>
				</div>
			</display:table>
		</div>
		<c:set var="vendorFeeRequestId" value=""></c:set>
			<br><br>
	<c:set var="handlingFeeName" value="Handling Fee"/>
    <c:set var="restockingFeeName" value="Restocking Fee"/>
	<!-- Change Details-->
	<c:choose> 
			<c:when test="${isEditMode}">
				
				<input type="button" onclick="editClicked();" name="edit" value="Edit" class="btn edit_btn" id="editButton"><br>
				
				<div id="changeDetails" style="display:none">
			</c:when>
			<c:otherwise>
			  <c:choose>
			  	<c:when test="${mode == 'edit' || isEditMode}">
			  		<input type="button" onclick="editClicked();" class="btn edit_btn" name="edit" value="Edit" id="editButton" ><br>
					<div id="changeDetails" style="display:block">
			  	</c:when>
			  	<c:otherwise>
			  		<input type="button" onclick="none" disabled="disabled" value="Edit" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="editButton" class="btn edit_btn">
			  		<br>
			  		
					<div id="changeDetails" style="display:block">
			  	</c:otherwise>
			  </c:choose>
				
					
			</c:otherwise>
	</c:choose>
			<br>
			<b>Change Details</b>
			<br>
			<display:table name="vendorFeeModel" cellspacing="0" cellpadding="0" requestURI="/oma/vendorShippingFee.html"  
				    defaultsort="1" id="vendorFee" pagesize="25" class="dstable" sort="list">
				<div>
				<c:set var="counter" value="${counter+1}"></c:set>
				
				<display:column property="fee.feeDesc" titleKey="Fee" style="width: 15%; text-align: left" sortProperty="fee.feeId"/>
				 
				<display:column  titleKey="Allowable" style="width: 10%; text-align: center">
					<c:set var="allow" value="${vendorFee.allow}" />
					<c:choose>
							<c:when test="${allow == 'Y'}">
								<c:out value="Yes"/>
							</c:when>
							<c:otherwise>
								<c:out value="No"/>
							</c:otherwise>
					</c:choose>
				</display:column>
				<display:column  titleKey="Per Order Amount"  style="width: 35%; text-align: center" >
				<c:choose>
					<c:when test="${vendorFee.perOrderAmount == 0 }">
					    <c:choose>
					    	<c:when test="${vendorFee.fee.feeDesc == handlingFeeName || vendorFee.fee.feeDesc == restockingFeeName}">
					    		<c:out value="$00.00"/>
					    	</c:when>
					    	<c:otherwise>
					    		<c:out value="n/a"/>
					    	</c:otherwise>
					    </c:choose>
						
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${vendorFee.fee.feeDesc == handlingFeeName || vendorFee.fee.feeDesc == restockingFeeName}">
								<c:out value="$"/>
								<c:out value="${vendorFee.perOrderAmt}"/>
							</c:when>
							<c:otherwise>
								<c:out value="n/a"/>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				
				</c:choose>
					
				</display:column>
				<display:column titleKey="Per Item Amount" style="width: 35%; text-align: center">
					<c:choose>
						<c:when test="${vendorFee.perItemAmount == 0}">
							<c:choose>
						    	<c:when test="${vendorFee.fee.feeDesc == handlingFeeName || vendorFee.fee.feeDesc == restockingFeeName}">
						    		<c:out value="$00.00"/>
						    	</c:when>
						    	<c:otherwise>
						    		<c:out value="n/a"/>
						    	</c:otherwise>
						    </c:choose>
						</c:when>
						<c:otherwise>
						<c:choose>
							<c:when test="${vendorFee.fee.feeDesc == handlingFeeName || vendorFee.fee.feeDesc == restockingFeeName}">
								<c:out value="$"/>
								<c:out value="${vendorFee.perItemAmt}"/>
							</c:when>
							<c:otherwise>
								<c:out value="n/a"/>
							</c:otherwise>
						</c:choose>
						</c:otherwise>
				
					</c:choose>
				</display:column>
				</div>
			</display:table>
		</div>
		
		
		<!--  fee Change -->
		
			
		<c:choose>
			<c:when test="${isEditMode}">
				<div id="editDetails" style="display:block">
				<c:set var="disabled" value="disabled"/>
			</c:when>
			<c:otherwise>
				<div id="editDetails" style="display:none">	
			</c:otherwise>
		</c:choose>
		<c:set var="success" value="false"/>
		
		<br><br>
				<b> Fee Change</b> 
		
			<spring:bind path="vendorFeesForm.*">
				<div class="error" id="error">
					<c:if test="${not empty status.errorMessages}">
						
							<c:forEach var="error" items="${status.errorMessages}">
							<c:choose>
							   <c:when test="${error == 'Saved Successfully!'}">
							   		<br>
									<span style="background:#FFFF00;"> <c:out value="${error}" escapeXml="false"/> </span><br/>
									<c:set var="success" value="true"/>
									
							   </c:when>
								<c:otherwise>
								<br>
									<img src="<c:url value="/images/iconWarning.gif"/>"
										alt="<fmt:message key="icon.warning"/>" class="icon"/>
									<c:out value="${error}" escapeXml="false"/><br/>
									
							   </c:otherwise>
							   
							</c:choose>	
							</c:forEach>
				  	</c:if>
				  </div>
				  <div class="jserrors" id="jserrors">
				  </div>
				</spring:bind>
				<c:url value="/oma/saveFees.html" var="formAction"></c:url>
			<form:form commandName="vendorFeesForm" name="vendorFeesForm" action="${formAction}" method="post" id="vendorFeesForm">
				<br>
				<div>  
				<ol>	
					
					<table border="0">
						<tr>
							<td width="17%">
								<label><b><fmt:message key="vendorFee.effectiveDate"/>:</b><span class="req" style="color: #FF0000"> *</span></label>
							</td>
							<td width="20">
								<form:input path="formEffectiveDate" id="EffectiveDate" cssClass="text" disabled="${isEditMode}" cssErrorClass="text medium error"/>
							</td>
						</tr>
						
						<tr>
							<td width="17%">
								<label><b><fmt:message key="vendorFee.summary"/>:</b><span class="req" style="color: #FF0000"> *</span></label>
							</td>
							<td width="20">
								<form:textarea path="vendorFeeRequestModel.feeRequestDesciption" id="summary" onkeypress="return imposeMaxLength(this, 200);" disabled="${isEditMode}" cssClass="text" cssErrorClass="text medium error"/>
							</td>
						</tr>
					</table>
					
					
					
					<c:set var="counter" value="-1"></c:set>
					<display:table name="${vendorFeesForm.vendorFees}" cellspacing="0" cellpadding="0" requestURI="/oma/vendorShippingFee.html"   
				    defaultsort="1" id="vendorFee" pagesize="25" class="dstable" sort="list" >
						<div>
						
						<c:set var="counter" value="${counter+1}"></c:set>
							
							<display:column property="fee.feeDesc"  titleKey="Fee" style="width: 10%" headerClass="vendorFeeHeader" sortProperty="fee.feeId" sortable="false"/>
							
							<display:column  sortable="false" titleKey="Allowable" style="width: 10%; text-align: center">
								<spring:bind path="vendorFeesForm.vendorFees[${counter}].allow">
									<c:set var="valOfAllowable" value="${status.value}"></c:set>
									<input type="hidden" name="<c:out value="${status.expression}"/>" align="middle" 
										id="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
								</spring:bind>
								<spring:bind path="vendorFeesForm.vendorFees[${counter}].fee.feeId">
										<input type="hidden" name="<c:out value="${status.expression}"/>" align="middle" class="feeId"
											id="<c:out value="feeId${counter}"/>" value="<c:out value="${status.value}"/>" />
								</spring:bind>
								<c:choose>
								
									<c:when test="${valOfAllowable == 'Y'}">
										
												<input type="checkbox"  checked="checked"  name="<c:out value="allowable${counter}" />"  class="checkBoxes" ${disabled}
											id="<c:out value="allowable${counter}"/>" onclick="setFeeAllowable(this,'<c:out value="${counter}"/>');"/>
									</c:when>
									<c:otherwise>
										
											<input  type="checkbox" name="<c:out value="allowable${counter}"/>"  class="checkBoxes" ${disabled}
											id="<c:out value="allowable${counter}"/>" onclick="setFeeAllowable(this,'<c:out value="${counter}"/>');"/>
											<c:set var="disable" value=""></c:set>
									</c:otherwise>
								
								</c:choose>
							</display:column>
							
										<display:column  sortable="false" titleKey="Per Order Amount" style="width: 10%; text-align: right" >
											
											
											<spring:bind path="vendorFeesForm.vendorFees[${counter}].perOrderAmt">
												<input type="hidden" name="<c:out value="${status.expression}"/>"  class="perOrderAmmount"    
													id="<c:out value="perOrderAmmount${counter}"/>"  value="<c:out value="${status.value}"/>" />
													<c:set var="orderAmount" value="${status.value}"> </c:set>
											</spring:bind>		
											<c:choose>
											
												<c:when test="${vendorFee.fee.feeDesc == handlingFeeName || vendorFee.fee.feeDesc == restockingFeeName}">
													<c:set var="disableTextBox" value=""/>    
												</c:when>
												<c:otherwise>
													<c:set var="disableTextBox" value="disabled"/>
												</c:otherwise>       
											</c:choose>
											<c:choose>
											
												<c:when test="${valOfAllowable == 'Y' && disableTextBox == '' }">
													<input type="text" name="<c:out value="${status.expression}"/>"  class="perOrderAmount" onkeyup="disableField(this,'<c:out value="${counter}"/>',1); return checkEnteredNumber(this,this.value)" ${disabled} onkeypress="return checkEnteredNumber(this,this.value)"  
													id="<c:out value="perOrderAmt${counter}"/>"  value="<c:out value="${orderAmount}"/>" />
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${disableTextBox == ''}">
															<input type="text" name="<c:out value="${status.expression}"/>" disabled="${isEditMode}" class="perOrderAmount" onkeyup="disableField(this,'<c:out value="${counter}"/>',1); return checkEnteredNumber(this,this.value)" ${disabled} onkeypress="return checkEnteredNumber(this,this.value)" 
													id="<c:out value="perOrderAmt${counter}"/>"  value="<c:out value="${orderAmount}"/>" />
														</c:when>
														<c:otherwise>
														</c:otherwise>
													</c:choose>	
													
													
												</c:otherwise>
											</c:choose>
											
											
										</display:column>
										<display:column  sortable="false" titleKey="Per Item Amount" style="width: 10%; text-align: right" >
											<spring:bind path="vendorFeesForm.vendorFees[${counter}].perItemAmt">
												<input type="hidden" name="<c:out value="${status.expression}"/>" class="perItemAmmount"
														id="<c:out value="perItemAmmount${counter}"/>" value="<c:out value="${status.value}"/>" />
													<c:set var="itemAmount" value="${status.value}"> </c:set>	
											</spring:bind>	
											<c:choose>
												<c:when test="${valOfAllowable == 'Y' && disableTextBox == ''}">
													<input type="text" name="<c:out value="${status.expression}"/>" class="perItemAmount"  onkeyup="disableField(this,'<c:out value="${counter}"/>',-1); return checkEnteredNumber(this,this.value)" ${disabled} onkeypress="return checkEnteredNumber(this,this.value)" 
														id="<c:out value="perItemAmt${counter}"/>" value="<c:out value="${itemAmount}"/>" />
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${disableTextBox == ''}">
															
															<input type="text" name="<c:out value="${status.expression}"/>" disabled="${isEditMode}" class="perItemAmount" onkeyup="disableField(this,'<c:out value="${counter}"/>',-1); return checkEnteredNumber(this,this.value)" ${disabled} onkeypress="return checkEnteredNumber(this,this.value)" 
																id="<c:out value="perItemAmt${counter}"/>" value="<c:out value="${itemAmount}"/>" />
														</c:when>
														<c:otherwise>
														</c:otherwise>
													</c:choose>			
												</c:otherwise>
											</c:choose>
											
										</display:column>					
							
						</div>
			</display:table>
					</ol>
					</div>
					<div align="center">
							<br>
							<input type="button" align="right" value="Cancel" id="cancelBtn" ${disabled} name="cancel" class="btn cancel_btn" onClick="verifyCancel();"/>
							<input type="submit" align="right" value="Save" id="saveBtn" ${disabled}  name="save" class="btn save_btn" />			
						
					</div>	
			</form:form>
		</div> 
	</div>
	</div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
//Date fields
var CFG=belk.cars.config;
	new Ext.form.DateField({
		applyTo:'EffectiveDate',   
        fieldLabel:'Effective Date',
        allowBlank:false,
		format:CFG.dateFormat,
		altFormats:CFG.dateAltFormats
    });
    
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
   //Logic to highlight the current selected row.
   var clickedVendorFeeRequest =  '<%=request.getAttribute("vendorFeeRequestId") %>';
	   
   if(clickedVendorFeeRequest > 0){
	  //Highlight the record which is clicked.
	   $('table.dstable tr td.vendorFeeRequestId').each(function(){
	   		var v = $(this).text();
	   		var vallue = jQuery.trim(v);
	   		
	   		
	   		//alert('Value from session: '+ <%=session.getAttribute("vendorFeeRequestId") %>);
	   		 if ( vallue === clickedVendorFeeRequest){
				 //alert('Highlighting the record.');
			   var tr = $(this).parent();
			   //alert('Got Yes Record.--'+ $(this).text());
			   $('td',$(this).parent()).removeClass('trSelected');
					$('td',tr).addClass('trSelected');
		   }
		   
	   });
   }
   else{
	   //Highlight the current Fee
	   $('table.dstable tr td.currentFee').each(function(){
	   //alert($(this).text());
	   var v = $(this).text();
	   var vallue = jQuery.trim(v);
	   
		   if ( vallue === 'Yes'){
			   var tr = $(this).parent();
			   $('td',$(this).parent()).removeClass('trSelected');
					$('td',tr).addClass('trSelected');
		   }
		   
	   });
	   
   }
   		
   //Disable the text fields
   var index =0;
   $('.checkBoxes').each(function(){
      
   	  var isChecked = $(this).attr('checked');
   	  //alert(' Index:'+index+' checked ->'+ isChecked);
   	  var perItem = $('.perItemAmount'); 
	  var perOrder = $('.perOrderAmount');
   	 if(isChecked){
   	 	if($(perOrder[index]).val() < 0){
   	 		
   	 		//alert('Disabling the Per Item amount '+index);
   	 		$(perItem[index]).attr('disabled', 'disabled');
   	 	}
   	 	if($(perItem[index]).val() < 0){
   	 		//alert('Disabling the Per Order amount '+index);
   	 		$(perOrder[index]).attr('disabled', 'disabled');
   	 	}
   	 	
   	 }
   		index = index + 1;
   })
   
   });
  $('.save_btn').click(function(ev){
		  //alert('$(#EffectiveDate).val()-->'+$('#EffectiveDate').val());
		  
		  $('.jserrors').html('');
		   var checkedCheckBoxCounter = 0;
		   var hasErrors =false;
		   var icon = '<img src="../images/iconWarning.gif" />'; //Image Icon
		   //Following check is for Date field
		  	if($('#EffectiveDate').val()===''){
		  	  //alert('enter date value');
			  
		  	  $('.jserrors').append($(icon));
			  $('.jserrors').append(' Date is a required field.<br>');
			  $('#EffectiveDate').css("border","thin solid red");
			  $('#EffectiveDate').css("border-width","1px");
			  hasErrors=true;
		  	}
		  	 //Check the date for greater than or equal to current date
		  	  var enteredDate = $('#EffectiveDate').val();
			  var currentDate = new Date();
			  var dateToCompare = new Date();
			  var splitDate= enteredDate.split('/');
			  dateToCompare.setFullYear(splitDate[2],splitDate[0]-1,splitDate[1]-0);
			  if (dateToCompare < currentDate)
			  {   //Date is less than Todays date
			  //alert('Current Date check');
			  	  $('.jserrors').append($(icon));
				  $('.jserrors').append(' Enter Date greater than or equal to Todays Date.<br>');
				  $('#EffectiveDate').css("border","thin solid red");
				  $('#EffectiveDate').css("border-width","1px");
				  hasErrors=true;
			  }
			 //Following check is for Summary field.
			if($('#summary').val() === ''){
				$('#summary').css("border","thin solid red");
				$('#summary').css("border-width","1px");  
		  	  $('.jserrors').append($(icon));
			  $('.jserrors').append(' Summary is a required field.<br>');
			  hasErrors=true;
		  	}
		   //Following checkes are for Per Item amounts and Per Order Amounts.
		   var perItem = $('.perItemAmount'); 
		   var minusPerItemAmount =0;
		   var perOrder = $('.perOrderAmount');
		   var minusPerOrderAmount =0;
		   var feeId = $('.feeId');
		   var index =0;
		   var valueEntered = 1;
		   //alert('Checkbox check');
			$('.checkBoxes').each(function(){
				valueEntered = 1;
			   var isChecked = $(this).attr('checked');
			   //alert('Condition check:'+($(feeId[index]).val() == 1 || $(feeId[index]).val() == 2));
			   if(isChecked){
			   	checkedCheckBoxCounter = checkedCheckBoxCounter + 1;
			   }
			   //Add the condition for handling fees and restocking fees.
				if(isChecked && ($(feeId[index]).val() == 1 || $(feeId[index]).val() == 2)){
						  //Check box is checked.
						
						  //alert($(perItem[index]).val());
						  //alert($(perOrder[index]).val());
						
						  if(($(perItem[index]).attr("disabled") != true) && ($(perItem[index]).val() < 0)){
						    //alert('Got per item amount');
							$(perItem[index]).css("border","thin solid red"); 
							$(perItem[index]).css("border-width","1px");
							minusPerItemAmount = minusPerItemAmount + 1;
						  } else if($(perItem[index]).val() > 0 ){
							//alert ('per Item is greater than 0');
								valueEntered = 1;
						  } 
						   
						  
						  //alert('Per Order fields is :'+ $(perOrder[index]).attr("disabled"));
						  if(($(perOrder[index]).attr("disabled") != true) && ($(perOrder[index]).val() < 0)){
							//alert('Got per order amount');
							$(perOrder[index]).css("border","thin solid red"); 
							$(perOrder[index]).css("border-width","1px");
							minusPerOrderAmount = minusPerOrderAmount + 1;
						  } else if($(perOrder[index]).val() > 0 ){
							//alert ('per Order is greater than 0');
						  	valueEntered = 1;
						  } 
						  if((($(perItem[index]).attr("disabled") != true) && $(perItem[index]).val() === '' )&& (($(perOrder[index]).attr("disabled") != true) && ($(perOrder[index]).val()=== ''))){
							//alert('Values are blank');
						  	valueEntered = 0;
						  } 
						  
						  
						  //alert('valueEntered:'+valueEntered);
						  if(valueEntered === 0){
						  	$(perItem[index]).css("border","thin solid red");
						  	$(perItem[index]).css("border-width","1px");
						  	$(perOrder[index]).css("border","thin solid red");
						  	$(perOrder[index]).css("border-width","1px");
						  	$('.jserrors').append('<img src="../images/iconWarning.gif" />');
							$('.jserrors').append(' Enter the value in either text box.<br>');
							hasErrors=true;
						  }
						  if($(perOrder[index]).val() === '' && ($(perItem[index]).val() != '')){
						 // alert('I am inside of per Order amt.');
							$(perOrder[index]).val('00.00');
							//alert($(perOrder[index]).val());
						  }
						  if(($(perItem[index]).val() === '') && ($(perOrder[index]).val()!= '')){
						  //alert('I am inside of Per Item amt.');
							$(perItem[index]).val('00.00');
						  }
				}
				index++;
			});
			 
			if(checkedCheckBoxCounter === 0){
				$('.jserrors').append('<img src="../images/iconWarning.gif" />');
				$('.jserrors').append(' Check at least one of the checkbox.<br>');
				hasErrors=true;
			}
			if(minusPerItemAmount > 0){
				$('.jserrors').append('<img src="../images/iconWarning.gif" />');
				$('.jserrors').append(' Per Item Amount values should be greater than 0.<br>');
				hasErrors=true;
			}
			if(minusPerOrderAmount > 0){
				$('.jserrors').append('<img src="../images/iconWarning.gif" />');
				$('.jserrors').append(' Per Order Amount values should be greater than 0.<br>');
				hasErrors=true;
			}
			
			if(hasErrors === true){
			//alert('There are errors.');
				ev.stopPropagation();
			  return false;
			}
			//If no errors appear, then set all the disable Order/Item amount fields to 0.0
			var index =0;
			var perHiddenItem = $('.perItemAmmount'); 
		    var perHiddenOrder = $('.perOrderAmmount');
			$('.checkBoxes').each(function(){
				//Logic to make all zero if checkbox is not clicked.
				if($(perItem[index]).attr("disabled") === true){
				//alert('Making per Item index to 0');
					$(perItem[index]).val('00.00');
					$(perHiddenItem[index]).val('00.00');
				}else{
					if($(perItem[index]).val() != ''){
						$(perHiddenItem[index]).val($(perItem[index]).val());
					}
				}
				if($(perOrder[index]).attr("disabled") === true){
				//alert('Making per order index to 0');
					$(perOrder[index]).val('00.00');
					$(perHiddenOrder[index]).val('00.00');
				}else{
					if($(perOrder[index]).val() != ''){
						$(perHiddenOrder[index]).val($(perOrder[index]).val());
					}
				}
				index++;
			});		
			return true;
  });  

</script>
]]>
</content>    