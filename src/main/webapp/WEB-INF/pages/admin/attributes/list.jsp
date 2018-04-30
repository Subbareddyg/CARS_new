<%@ include file="/common/taglibs.jsp"%>

<head>
    <title>CAR Attributes</title>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<c:url value="/admin/searchattribute.html" var="formAction"/>
<form method="post" action="${formAction}" id="searchForm">
<div id="search_for_attribute" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Attribute Search
	</div>
	<div class="x-panel-body">
<c:url value="/admin/searchattribute.html" var="formAction"/>
	<form method="post" action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="attribute.search.name"/></label>
				<input type="text" id="attributeName" name="attributeName" value="<c:out value="${attributeName}" />"/>
			</li>
			<li>
				<label><fmt:message key="attribute.class.id"/></label>
				<input type="text" id="classificationId" name="classificationId" value="<c:out value="${classificationId}" />"/>
			</li>
			<li>
				<label>Product Type: </label>
				<input type="text" id="productTypeName" name="productTypeName" value="<c:out value="${productTypeName}" />"/>
			</li>
			<li>
				<label><fmt:message key="attribute.search.blue.martini.name"/></label>
				<input type="text" id="blueMartiniName" name="blueMartiniName" value="<c:out value="${blueMartiniName}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="searchAttribute" value="<fmt:message key="attribute.search.button"/>" />
				<c:url value="/admin/attributes/attributes.html" var="formAction"/>
				<a class="btn" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
			</li>
		</ol>
	
</div></div>
<div id="attr_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Attributes
	</div>
	<div class="x-panel-body">
	<div class="attributeButtons">
	<c:choose>
			<c:when test="${sessionScope.isCopyTextAllowed == 'true' && sessionScope.isBuyer == 'true' && sessionScope.isDataGovernanceAdmin == 'false'}">
				<a class="disablebtnright" disabled="disabled" onclick="return false" href="<c:url value="/admin/attributes/attributeForm.html"/>"><fmt:message key="attribute.button.add"/></a>
			</c:when>
			<c:otherwise>
				<a class="btn" href="<c:url value="/admin/attributes/attributeForm.html"/>"><fmt:message key="attribute.button.add"/></a>
			</c:otherwise>
    </c:choose>
	</div>
<display:table name="attributesList" cellspacing="0" cellpadding="0" requestURI="/admin/searchattribute.html"  
    defaultsort="1" id="attr" pagesize="25" class="table" sort="list" >
    <display:column property="name" sortable="true" titleKey="attribute.name" style="width: 30%; word-break:break-all"/>
    <display:column property="attributeConfig.displayName" sortable="true" titleKey="attribute.label" style="width: 30%;word-break:break-all"/>
    <display:column property="blueMartiniAttribute" sortable="true" titleKey="attribute.bmName" style="width: 25%;word-break:break-all"/>
    <display:column property="attributeConfig.htmlDisplayType.name" sortable="true" titleKey="attribute.type" style="width: 25%"/>
    <display:column  sortable="false" titleKey="attribute.displayable" style="width: 5%">
    	<c:choose>
    		<c:when test="${attr.isDisplayable == 'Y'}">
    			<fmt:message key="attribute.yes"/>
    		</c:when>
    		<c:otherwise>
    			<fmt:message key="attribute.no"/>
    		</c:otherwise>
    	</c:choose>
    </display:column>
    <display:column  sortable="true" titleKey="attribute.searchable" style="width: 5%">
	   	<c:choose>
	   		<c:when test="${attr.isSearchable == 'Y'}">
	   			<fmt:message key="attribute.yes"/>
    		</c:when>
    		<c:otherwise>
    			<fmt:message key="attribute.no"/>
    		</c:otherwise>
	   	</c:choose>
    </display:column>
    
    <%-- -- Start of code added for Outfit Management --  --%>
    <display:column  sortable="true" titleKey="attribute.outfit" style="width: 5%">
	   	<c:choose>
	   		<c:when test="${attr.isOutfit == 'Y'}">
	   			<fmt:message key="attribute.yes"/>
    		</c:when>
    		<c:otherwise>
    			<fmt:message key="attribute.no"/>
    		</c:otherwise>
	   	</c:choose>
    </display:column>
    <%-- -- End of code added for Outfit Management --  --%>
    
     <%-- -- Start of code added for Deal Based Management --  --%>
    <display:column  sortable="true" titleKey="attribute.isPyg" style="width: 5%">
	   	<c:choose>
	   		<c:when test="${attr.isPYG == 'Y'}">
	   			<fmt:message key="attribute.yes"/>
    		</c:when>
    		<c:otherwise>
    			<fmt:message key="attribute.no"/>
    		</c:otherwise>
	   	</c:choose>
    </display:column>
    <%-- -- End of code added for Deal Based Management --  --%>
    
    
     <%-- -- Start of code added for Required Attribute --  --%>
    <display:column  sortable="true" titleKey="attribute.required" style="width: 5%">
	   	<c:choose>
	   		<c:when test="${attr.isRequired == 'Y'}">
	   			<fmt:message key="attribute.yes"/>
    		</c:when>
    		<c:otherwise>
    			<fmt:message key="attribute.no"/>
    		</c:otherwise>
	   	</c:choose>
    </display:column>
    <%-- -- End of code added for Required Attribute --  --%>
    
   <!-- Disable Copy for Faceted CARS color attributes -->

	<c:choose>
			<c:when test="${sessionScope.isCopyTextAllowed == 'true' && sessionScope.isBuyer == 'true'}">


				<display:column>
							Copy
				</display:column>
				
				
				<!-- Disable remove for OUTFIT,DROPSHIP,Faceted CARS color attributes -->
				<c:choose>
					<c:when test="${sessionScope.isDataGovernanceAdmin == 'false'}">

						<display:column>
									 remove
									
						</display:column>
					</c:when>
					<c:otherwise>
							<display:column>
								<c:choose>
									<c:when test="${attr.attributeType.attrTypeCd == 'OUTFIT'  || attr.name == 'IS_DROPSHIP'  ||
									  attr.name == 'SDF_Online Only'  || attr.name == 'Super_Color_1' || attr.name == 'Super_Color_2' || attr.name == 'Super_Color_3'||
									  attr.name == 'Super_Color_4' }">
									 remove
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${attr.statusCd == 'ACTIVE'}">
												<secureurl:secureAnchor name="AttributeRemove" cssStyle="remove" elementName="AttributeRemove"  arguments="${attr.attributeId},remove" title="Remove" hideUnaccessibleLinks="true"/>
											</c:when>
											<c:otherwise>
												Remove
											</c:otherwise>
										</c:choose>
								</c:otherwise>
								</c:choose>
							</display:column>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<display:column>
					<c:choose>
						<c:when test="${attr.name == 'Super_Color_1' || attr.name == 'Super_Color_2' || attr.name == 'Super_Color_3'||
							  attr.name == 'Super_Color_4' }">
							  Copy
						</c:when>
						<c:otherwise>
							<secureurl:secureAnchor name="AttributeCopy"  arguments="${attr.attributeId},copy" title="Copy" hideUnaccessibleLinks="true"/>
						</c:otherwise>
					</c:choose>
				</display:column>
				
				
				<!-- Disable remove for OUTFIT,DROPSHIP,Faceted CARS color attributes -->
				
				<display:column>
						<c:choose>
							<c:when test="${attr.attributeType.attrTypeCd == 'OUTFIT'  || attr.name == 'IS_DROPSHIP'  ||
							  attr.name == 'SDF_Online Only'  || attr.name == 'Super_Color_1' || attr.name == 'Super_Color_2' || attr.name == 'Super_Color_3'||
							  attr.name == 'Super_Color_4' }">
							 remove
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${attr.statusCd == 'ACTIVE'}">
										<secureurl:secureAnchor name="AttributeRemove" cssStyle="remove" elementName="AttributeRemove"  arguments="${attr.attributeId},remove" title="Remove" hideUnaccessibleLinks="true"/>
									</c:when>
									<c:otherwise>
										Remove
									</c:otherwise>
								</c:choose>
						</c:otherwise>
					</c:choose>
				</display:column>
			</c:otherwise>
		</c:choose>
   <!-- Disable Detail for Faceted CARS color attributes -->
    
    <display:column>
    	<c:choose>
    	<c:when test="${attr.attributeType.attrTypeCd == 'OUTFIT'  || attr.name == 'IS_DROPSHIP'  ||
				  attr.name == 'SDF_Online Only'  || attr.name == 'SUPER_COLOR_1' || attr.name == 'SUPER_COLOR_2' || attr.name == 'SUPER_COLOR_3'||
	    		  attr.name == 'SUPER_COLOR_4' }">
    			Detail
    		</c:when>
    		<c:otherwise>
					<c:choose>
							<c:when test="${attr.statusCd == 'ACTIVE'}">
    			<secureurl:secureAnchor name="AttributeDetail"  arguments="${attr.attributeId}" title="Detail" hideUnaccessibleLinks="true"/>
							</c:when>
							<c:otherwise>
							Detail
							</c:otherwise>
					</c:choose>	
    		</c:otherwise>
    	</c:choose>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="attribute"/>
    <display:setProperty name="paging.banner.items_name" value="attributes"/>
</display:table>
</div></div>

<c:if test="${isDataGovernanceAdmin==true}">
<div id="search_for_pimattribute" class="cars_panel x-hidden">
	<div class="x-panel-header">
		PIM Attribute Search
	</div>
	<div class="x-panel-body">
		<div id="message_frm_validation" class="error">
				<img src="<c:url value="/images/iconWarning.gif"/>"
						alt="<fmt:message key="icon.warning"/>" class="icon" />
					<c:out value="Please enter search criteria." escapeXml="false" />
					<br />
		</div>
	
	<input type="hidden" id="action" name="action" value="${action}"/>
	<input type="hidden" id="lastSerch" name="lastSerch" value="${lastSerch}"/>
		<input type="hidden" id="selectedPIMAttributeOld" name="selectedPIMAttributeOld" value=""/>
	<input type="hidden" id="selectedPIMAttributeNew" name="selectedPIMAttributeNew" value=""/>
		<ol>
			<li>
				<label><fmt:message key="pim.attribute.search.name"/></label>
				<input type="text" id="pimAttributeName" name="pimAttributeName" value="${pimAttributeName}"/>
			</li>
				<li class="">
				<input class="btn" style="margin-right:5px" type="submit" id="searchPIMAttribute" name="searchPIMAttribute" value="<fmt:message key="pim.attribute.search.button"/>" />
				<input class="btn" type="submit" id="ViewAllPIMAttribute" name="ViewAllPIMAttribute" value="<fmt:message key="pim.attribute.viewall.button"/>" />

			</li>
		</ol>
	
</div></div>
<c:set var="PIMAttributesState" value="collapsed"/>
 <c:if test="${not empty action}"> 
<c:set var="PIMAttributesState" value=""/>
 </c:if>
<div id="pimattr_list" class="cars_panel x-hidden ${PIMAttributesState}">	<div class="x-panel-header">
		PIM Attributes
	</div>
	<div class="x-panel-body">
	 <c:if test="${not empty message}"> 
	<div id="save_pim_attr_msg" class="error">
			<c:out value="${message}" escapeXml="false" />
		</div>
		</c:if>
		
		<div id="pimattr_no_chnage_error" class="error">
			<c:out value="No changes made." escapeXml="false" />
		</div>
		
		<div style="width: 100%">
		<c:set var="counter" value="-1"></c:set>
		<display:table name="pimAttributes" cellspacing="0" cellpadding="0" requestURI="/admin/searchattribute.html"  
    defaultsort="2" id="pimid" pagesize="25" class="table" sort="list" >
	<c:set var="counter" value="${counter+1}"></c:set>
			<display:column property="name" sortable="true" titleKey="pim.attribute.name" style="width: 60%"/>
			<display:column property="displaybleDate" sortable="true" titleKey="pim.attribute.dateavailable" style="width: 20%"/>
			<display:column  sortable="true" titleKey="pim.attribute.displayincars" style="width: 20%">
			<c:choose>
    		<c:when test="${pimid.isPimAttrDisplayble == 'Y'}">
    			<input type="checkbox" name="chklist" id ="chklist" class="chklist" value="${pimAttributes[counter].pimAttrId}" checked="checked" />
    		</c:when>
    		<c:otherwise>
    			<input type="checkbox" name="chklist" id ="chklist" class="chklist" value="${pimAttributes[counter].pimAttrId}" />
    		</c:otherwise>
    	</c:choose>
			
			
			
			</display:column>
    	</display:table>

		</br>
		<div>
		<input class="btn" type="submit" id="savePIMAttribute" name="searchPIMAttribute" value="<fmt:message key="pim.attribute.save.button"/>" />
		</div>
</div></div></div>	
</c:if>


</form>
</body>

<content tag="inlineStyle">
#attr_list{
	margin-top:10px;
}
#search_for_pimattribute{
	margin-top:10px;
}
#pimattr_list_temp{
	margin-top:10px;
}
#pimattr_list_temp_error{
	margin-top:10px;
}
#pimattr_list{
	margin-top:10px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('#message_frm_validation').hide();
	$('#pimattr_no_chnage_error').hide();
	if(($('#action').val().length>0)){
	$('#pimAttributeName').focus();
	}
		
	var oldselectedCars = null;
	$(".chklist").each( function() {
		var status = $(this).attr('checked');
		if(status === true) {
			if(oldselectedCars != null) {
				oldselectedCars = oldselectedCars + ',' + $(this).attr('value');
			} else {
				oldselectedCars =$(this).attr('value');
			}
		}
	});	
		$('#selectedPIMAttributeOld').val(oldselectedCars);
	
	$('a.remove').click(function(){
		return confirm('<fmt:message key="attribute.confirm.delete"/>');
	});
	
	$('#searchAttribute').click(function(){	
		$('#action').val('');
		return true;
	});
	
	$('#viewAllAttribute').click(function(){
		$('#action').val('');	
		$('#attributeName').val('');
		$('#classificationId').val('');
		$('#productTypeName').val('');
		$('#blueMartiniName').val('');
		return true;
	});
	
	$('#searchPIMAttribute').click(function(){	
		if(($('#pimAttributeName').val().length==0) ){
			$('#save_pim_attr_msg').hide();
			$('#pimattr_no_chnage_error').hide();
			$('#message_frm_validation').show();
			return false;
		}
		else{
			$('#action').val("searchPIMAttribute");
			return true;
		}
	});



	
	$('#ViewAllPIMAttribute').click(function(){	
		$('#action').val("searchPIMAttribute");
		$('#pimAttributeName').val('');
		return true;
	});
	
	$('#savePIMAttribute').click(function(){
		var selectedCars = null;
		$(".chklist").each( function() {
			var status = $(this).attr('checked');
			if(status === true) {
				if(selectedCars != null) {
					selectedCars = selectedCars + ',' + $(this).attr('value');
				} else {
					selectedCars =$(this).attr('value');
				}
			}
		});
		$('#selectedPIMAttributeNew').val(selectedCars);
		var newpim = $('#selectedPIMAttributeNew').val();
		var oldpim = $('#selectedPIMAttributeOld').val();
		if(newpim===oldpim){
			$('#save_pim_attr_msg').hide();
			$('#pimattr_no_chnage_error').show();
			return false;
		}
		$('#action').val("savePIMAttribute");
		return true;
	});	
	
	
	
	

});

</script>
]]>
</content>
