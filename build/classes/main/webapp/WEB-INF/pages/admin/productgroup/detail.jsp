<%@ include file="/common/taglibs.jsp"%>
<script>
	function setValue(typeId, typeName) {
		document.getElementById("productTypeID").value=typeId;
	}

        function setOptionSequence()     {

   
    var productGroupSelect=document.getElementById("newProductGroupID1");
    var val ="";
    var flag=false;
    var oldVal=productGroupSelect.value;
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



    }

}
</script>

<head>
<title><fmt:message key="productGroup.profile.title.details" /></title>
<meta name="heading" content="<fmt:message key='productGroup.heading'/>" />
<meta name="menu" content="UserMenu" />
</head>

<body class="admin">
<div class="cars_panel x-hidden">
<div class="x-panel-header"><fmt:message key='productGroup.heading'/></div>
<div class="x-panel-body">
<div class="attributeButtons">
	<c:if test="${sessionScope.displayRole == 'admin'}">
	<a class="btn"
	href="<c:url value="/admin/productgroup/productGroupForm.html?method=edit&id=${productTypeGroupForm.id}" />"
	title="Edit Product Group"><fmt:message key="button.edit" /></a>
	</c:if>
	 <a
	class="btn"
	href="<c:url value='/admin/producttype/productGroups.html?method=getAllProductGroups'/>"
	title="Back to Product Type List"><fmt:message key="button.back" /></a>
	
</div>
<c:import url="productGroupInfo.jsp"/>
</div></div>
<div id="productType_class_pnl" class="cars_panel x-hidden">
<div class="x-panel-header">Product Types</div>
<div class="x-panel-body">
	<div class="userButtons">
<secureurl:secureAnchor cssStyle="btn"
	name="AssociateProductGroupToType"
	title="Associate With Product Types" localized="true"
	hideUnaccessibleLinks="true"
	arguments="${productTypeGroupForm.id}" /> 
</div>
<c:choose>
	<c:when test="${not empty productTypeGroupForm.productTypes}">
		<fmt:message key="productGroup.details.productType"/> &nbsp;   	
		
<display:table name="productTypeGroupForm.productTypes" cellspacing="0"
			cellpadding="0" requestURI="" defaultsort="1" id="productGroupType"
			pagesize="25" class="table dstable" htmlId="mytable" >
			<display:column property="productTypeId" style="width: 25%" title="Product Id" />
			<display:column sortable="true" property="name" title="StateId" class="hidden" headerClass="hidden" media="html" />
			<c:choose>
			<c:when test="${sessionScope.displayRole == 'admin'}">
				<display:column style="width: 25%" title="Product Name">
					<secureurl:secureAnchor name="ProductDetail"  arguments="${productGroupType.productTypeId}" title="${productGroupType.name}" hideUnaccessibleLinks="true"/>
				</display:column>
			</c:when>
			<c:otherwise>
				<display:column property="name" style="width: 25%" title="Product Name"/>
			</c:otherwise>
			</c:choose>
			<display:column property="description" title="Product Description"/>
			<c:if test="${sessionScope.displayRole == 'admin'}">
			<display:column >
					<c:url value="/admin/productgroup/removeProductFromGroup.html?method=removeProdTypeView&typeId=${productGroupType.productTypeId}&id=${productTypeGroupForm.id}" var="formAction" />
					<a class="remove" id="${productGroupType.productTypeId}"   onclick="setValue('${productGroupType.productTypeId}', '${productGroupType.name}')" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>'>Remove</a>
				
			</display:column>
			</c:if>
			<display:setProperty name="paging.banner.item_name"
				value="product type" />
			<display:setProperty name="paging.banner.items_name"
				value="product types" />
		</display:table>
	</c:when>
	<c:otherwise>
		<fmt:message key="productGroup.details.no.productType.found" />
	</c:otherwise>
</c:choose></div>
</div>

<div id="remove_product_group_win" class="x-hidden">
<div class="x-window-header">Remove Product Type From Product Group </div>
<div id="remove_product_group_form" class="content" style="margin: 10px;">
<strong>All Product Types must be associated with a Group.<br/> 
		You are requesting to remove <label id="nameLabel"></label> &nbsp;
		From the <c:out value="${productTypeGroupForm.productGroup.name}"/>  group, please indicate the new group it will be associated with.<br/><br/> </strong>
		
<c:url value="/admin/productgroup/removeProductFromGroup.html" var="formAction" />	
<form method="post" action="${formAction}" id="productTypeGroupForm" >
		<input type="hidden" name="id" value="${productTypeGroupForm.id}"/>
		<input type="hidden" name="method" value="removeProdType"/>
		<input type="hidden" name="newProductGroupID" />
		<input type="hidden" name="productTypeID" />
		<strong>New Product Type Group:</strong>
		<select name="newProductGroupID" class="product_group_drop"  id="newProductGroupID1" >
			<c:forEach var="itemName" varStatus="status" items="${productTypeGroupForm.productGroupList}" >   
                        <option value="${itemName.productGroupId}"><c:out value="${itemName.name}"/></option>  
                    </c:forEach> 
		</select>
		
					
<br/><br/>		
</form>
</div></div>
<script>
            setOptionSequence();
        </script>

</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	var remove_win=null;
	$('a.remove').click(function(){
		$(this).blur();
			var $id = parseInt(this.id);
			$('#mytable tr').each(function() { 
    												
    												if (!this.rowIndex) return; 
    												var customerId =this.cells[0].innerHTML; 
    												
    												if($id==customerId) {
    												//alert(customerId);
    												//alert(this.cells[2].innerHTML);
    													$("#nameLabel").text(this.cells[1].innerHTML);
    													
    												}
												}) ; 
		if (!remove_win) {
			remove_win = new Ext.Window({
				el:'remove_product_group_win',
				layout:'fit',
				width:550,
				autoHeight:true,
				closeAction:'hide',
				modal:true,
				plain:true,
				items: new Ext.Panel({
                    contentEl:'remove_product_group_form',
                    deferredRender:false,
                    border:false,
					autoHeight:true
                }),
				buttons: [{
		                text: 'Cancel',
		                handler: function(){
		                    remove_win.hide();
		                }
	            	},{
		                text: 'Save',
		                handler: function(){
							// form submit. After selecting
							$('#remove_product_group_form form').submit();
		                }
					}
				]
			});
		}
		remove_win.show(function(){
			$('#remove_product_group_form select.product_group_drop').focus();
		});
		return false;
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
	
});
</script>
]]>
</content>