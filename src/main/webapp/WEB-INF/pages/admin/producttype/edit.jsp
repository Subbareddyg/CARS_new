<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="product.edit.title"/></title>
</head>
<script>
function setOptionSequence()     {
    var productGroupSelect=document.getElementById("productGroupID");
    var val ="";
    var flag=false;
    var oldVal=productGroupSelect.value;
    var ind = productGroupSelect.selectedIndex;
    for(cnt=0;cnt<productGroupSelect.length;cnt++) {
        
         if(productGroupSelect.options[cnt].text=='Unassigned') {
             val=productGroupSelect.options[cnt].value;
             productGroupSelect.remove(cnt);
             flag=true;
         }
         }
    
    if(flag) {
       var newOpt = document.createElement('option');
       newOpt.text = '****Unassigned****';
       newOpt.value = val;
       productGroupSelect.add(newOpt,productGroupSelect.length);
        if (oldVal=='')  {
            productGroupSelect.selectedIndex  =0;
        } else {
            productGroupSelect.value=oldVal;
            productGroupSelect.selectedIndex=ind;
        }

    }
    
}
</script>
<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Add/Edit Product Type
	</div>
	<div class="x-panel-body">
<spring:bind path="productTypeForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<div id="attr_container">
<c:url value="/admin/producttype/productTypeForm.html" var="formAction"/>
	<form:form commandName="productTypeForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="productTypeForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="product.edit.name"/>
					</label>
					<form:input path="name" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" maxlength="50" />					
				</li>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="product.edit.description"/>
					</label>
					<form:textarea cols="10" rows="2" path="description" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" />		
				</li>
				
				<li>
					<label for="txt_attr_name">
						<fmt:message key="product.edit.productGroup"/>
					</label>
                                                <form:select path="productGroupID" id="txt_attr_name" cssStyle="width:190px;"
						cssErrorClass="text medium error">
                                            <form:option value=""  label="Select Product Type Group"></form:option>
						<form:options items="${productTypeForm.productGroups}" itemLabel="name" itemValue="productGroupId"/>
					</form:select>						
				</li>	
				
				<li>
					<label for="txt_attr_name">
						<fmt:message key="product.edit.status"/>
					</label>
					<form:select path="statusCd" id="txt_attr_name" cssClass="select"
						cssErrorClass="text medium error">
						<form:option value="ACTIVE">
							<fmt:message key="flag.active"/>
						</form:option>
						<form:option value="INACTIVE">
							<fmt:message key="flag.inactive"/>
						</form:option>
					</form:select>						
				</li>				
			</ul>
				<br/>								
			<div class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>	
	    <input type="hidden" name="id" value="${productTypeForm.productType.productTypeId}"/>
	    <input type="hidden" name="action" value="addProductType"/>	   	    
	</form:form>
</div>
</div></div>
        <script>
            setOptionSequence();
        </script>
</body>
<content tag="inlineStyle">
div.buttons{
	padding-left:160px;
}
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.editproducttype.js"/>"></script>
]]>
</content>