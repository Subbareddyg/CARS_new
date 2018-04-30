<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='userVendors.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">

		<div class="cars_panel x-hidden">

			<div class="x-panel-header">
					CAR User Details
			</div>
			
			<div class="x-panel-body">
					<spring:bind path="userForm.*">
								<c:if test="${not empty status.errorMessages && !userForm.searchUser}">
									<div class="error">
											<c:forEach var="error" items="${status.errorMessages}">
													<img src="<c:url value="/images/iconWarning.gif"/>" alt="<fmt:message key="icon.warning"/>" class="icon" />
														<c:out value="${error}" escapeXml="false" />
													<br />
											</c:forEach>
									</div>
								</c:if>
					</spring:bind>
					<c:set var="user" value="${userForm.user}" scope="request"/>
						<c:import url="userInfo.jsp"/>
			</div>
		</div>
		
		<div id="user_vendors_pnl" class="cars_panel x-hidden">
		
				<div class="x-panel-header">
							Associate With Vendor
				</div>
				<div class="x-panel-body">
						<c:url value="/admin/user/associateVendor.html" var="formAction"/>
						<form:form commandName="userForm" method="post" action="${formAction}" id="userForm">
								<input type="hidden" name="id" value="${userForm.user.id}"/>
								<input type="hidden" name="method" value="associateVendor"/>
								<input type="hidden" name="firstload" value="0"/>
								<input type="hidden" name="searchUser" value="false"/>
								<div  style="padding:5px 0 5px 20px;background:#f0f0f0;clear:both;margin-top:5px;"style ="width:98%">
											<div>
											         <strong>
														<table  style="border:0px;">
															<tr >
																<td width="85px" style="border:0px;">
																	<fmt:message key="vendor.search.name"/>
																</td>
																<td width="25px" style="border:0px;">
																	<input type="text" size="20" id="vendorName" name="vendorName"  value="<c:out value="${vendorName}"/>"/> 
																</td>
																
																<td width="85px" style="border:0px;">
																	<fmt:message key="vendor.search.number"/>
																</td>
																<td width="25px" style="border:0px;" >
																	<input type="text" size="20" id="vendorNumber"  name="vendorNumber"  value="<c:out value="${vendorNumber}"/>"/>
																</td>
																
																<td width="25px" style="border:0px;">
																	<input type="submit" class ="btn" id="search"  value="<fmt:message key="vendor.button.search"/>"/>
																</td>
																
																<td style="border:0px;">
																	<input type="submit" class="btn" id="displayAll"  value="<fmt:message key="vendor.button.diaplayall"/>"/>
																</td>
																
																														
															</tr>																
														</table>
														</strong>				
											</div>			
								</div><br>
				   
								<div id="searchValidateEr" class="error_message" style="display: none;"><b> Please enter Vendor Name or Vendor MIR # to search </b></div>
					<c:choose>
					
						<c:when test="${fn:length(vendors)== fn:length(userVendors) && fn:length(userVendors) > 0 && fn:length(vendors) > 0}">
							<div id="searchError"> All vendors have been selected for this user. </div>
						</c:when>
					
                        <c:when test="${ fn:length(vendors) == 0 && (fn:length(vendorName)!=0 || fn:length(vendorNumber)!=0 ) }">
                        <div id="searchNoReultError">No results found.</div></c:when>
					<c:otherwise>
						<c:if test="${not empty vendors}">
							<br style="clear:both;" />
								<div id="filterd" class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;clear:both;margin-top:5px;">
								<strong>Filter:</strong> <input type="text" id="txt_vendor_filter" />
								<span id="filter_results"></span>
						</div>
						<div id="vendorList">
					<ul class="vendors_for_add">	
						<app:extendedcheckboxes path="vendors" items="${vendors}" useritems="${userVendors}" itemValue="vendorIdAsString" itemLabel="displayName"  element="li" sortBy="vendorNumber"/>
					</ul></div>
					<div class="buttons" style="padding-top:20px;" id="associateBt">
						<input class="btn" id="save" type="submit" value="Associate with Vendor"/>
					</div>	
					</c:if>
				</c:otherwise>
			</c:choose>	
		</form:form>
	 </div>
  </div>
</body>

<content tag="inlineStyle">
#searchValidateEr{color:red;}
#user_vendors_pnl{ margin-top:10px; }
ul.vendors_for_add li{
	padding:5px 2px;
}


</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	var $ul=$('ul.vendors_for_add');
	$("li").remove(":contains('OUTFIT_VENDOR')");
	var $lis=$('li',$ul);
	$('#filter_results').html($lis.length+' Vendors');
	$('#txt_vendor_filter').keyup(function(){
		var $this=$(this);
		$lis.hide();
		if($this.val().length>0){
			$('#filter_results').html($('li:contains("'+$this.val().toUpperCase()+'")',$ul).show().length+' Vendors');
		}
		else{
			$lis.show();
			$('#filter_results').html($lis.length+' Vendors');
		}
	});	
});
$('#search').click(function(){
$('#searchValidateEr').hide();
$('#searchError').hide();
$('#searchNoReultError').hide();
$('input[name="searchUser"]').val('true');
	if(($('#vendorName').val().trim().length == 0) && ($('#vendorNumber').val().trim().length == 0) ){
		$('#searchValidateEr').show();
		$('#vendorList').hide();
		$('#filterd').hide();
		$('#associateBt').hide();
		return false;
	} 
});
$('#displayAll').click(function(){
	$('input[name="searchUser"]').val('true');
	$('#vendorName').val('');
	$('#vendorNumber').val('');
	return true;
});

</script>
]]>
</content>