<%@ include file="/common/taglibs.jsp"%>
<div id="ajaxOtherVen">
	<label for="catalogName" class="desc">Existing Data Map :</label>
	<form:select path="vendorCatalogDataMappingForm.templateFrmAnotherVendor" id="templateFrmAnotherVendor">
		<form:option value ="-1">Select a Value</form:option>
		<form:options items= "${vendorCatalogDataMappingForm.templatesForOtherVendor}"
			itemLabel = "vendorCatalogName" itemValue = "vendorCatalogTmplID"/>
	</form:select>
</div>
