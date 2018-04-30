<%@ include file="/common/taglibs.jsp"%>

<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<page:applyDecorator name="ajax">

<body>

<div style="margin-left:5px;">
<br/>
<b>Select Jobs</b>
<br/>
</div>
<br/>


<div style="margin-top:10px;">
	
	<div class="x-panel-body">

<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=importVendorCatalog"/>" title="Job to Process Imported Catalog">Job to Process Imported Catalog</a>
</div>
<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=readIDBFeed"/>" title="Job to process IDB Feed">Job to process IDB Feed</a>
</div>

<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=readReferenceFeed"/>" title="Job to process IDB CrossRefernce Feed">Job to process IDB CrossRefernce Feed</a>
</div>


<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=readPurgeFeed"/>" title="Job to process Purge Feed">Job to process Purge Feed</a>
</div>

<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=loadVendorCatalogDataIntoCARS"/>" title="Prefill data from header and records to master table and copy images from unmapped folder till car_image table.">Job to pPrefill data from header and records to master table and copy images from unmapped folder till car_image table</a>
</div>
<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=prefillCars"/>" title="Prefill car">Prefill car attributes </a>
</div>
<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=sendToCMP"/>" title="Export Info To CES">Export CARs Info To CES</a>
</div>
<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=createDropshipCars"/>" title="Create Dropship CARS">BR.12 Create Dropship CARS</a>
</div>
<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=importManualOrPoCars"/>" title="Import Manual or PO CARS">Import Manual or PO CARS</a>
</div>

<div class="userButtons" id="AddBtn1">
	<a class="btn" id="AddBtn" href="<c:url value="../oma/runJob.html?method=exportVenodrExpeditedShipping"/>" title="Export Vendor Expedited Shipping">Export Vendor Expedited Shipping</a>
</div>

</div>
</div>
</body>
</page:applyDecorator>

