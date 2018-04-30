<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
#customers
{
font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
width:100%;
border-collapse:collapse;
}
#customers td, #customers th 
{
font-size:1em;
border:1px solid #8DB2E3;
padding:3px 7px 2px 7px;
}
#customers th 
{
font-size:1.1em;
text-align:left;
padding-top:5px;
padding-bottom:4px;
background-color:#CEDFF5;
color:#15428B;
}
#customers tr.alt td 
{
color:#000000;
background-color:#FEFEF2;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Scheduled Jobs</title>
</head>
<body>
<table id="customers">
<tr>
  <th>Serial No.</th>
  <th>Batch Job Description/Name</th>
  <th>Link to Run it </th>
  <th>Scheduled Time</th>
</tr>
<tr >
	<td>
	1-
	</td>
	<td>
	To process imported catalog.After this batch job got processed successfully then uploaded spreadsheet are ready for data mapping
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=importVendorCatalog" >Job to Process Imported Catalog</a>
	</td>
	<td>
	23:15
	</td>
</tr>
<tr >
	<td>
	2-
	</td>
	<td>
	Job to Prefill data from header and records to master table and copy images from unmapped folder till car_image table.
	The spreadsheet vendor styles will be ready to review on Spreadsheet items screen after this Job.
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=loadVendorCatalogDataIntoCARS">Translation of catalogs</a>
	</td>
	<td>
	5:15
	</td>
</tr>
<tr class="alt">
	<td>
	3-
	</td>
	<td>
	Prefill the CARS with the spreadsheet data for the mapped attributes during the data mapping phase.
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=prefillCars">Prefill car attributes</a>
	</td>
	<td>
	6:15
	</td>
</tr>
<tr class="alt">
	<td>
	4-
	</td>
	<td>
	Export CARs Info To CES
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=sendToCMP">Export CARs Info To CES</a>
	</td>
	<td>
	4:30
	</td>
</tr>
<tr >
	<td>
	5-
	</td>
	<td>
	Export Products in Trend
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingExportProductsInTrend">Export Products in Trend</a>
	</td>
	<td>
	4:45
	</td>
</tr>
<tr class="alt">
	<td>
	6-
	</td>
	<td>
	Send user notification
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendUserNotification">Send user notification</a>
	</td>
	<td>
	5:00
	</td>
</tr>
<!--
<tr >
<td>
	7-
	</td>
	<td>
	Send vendor notification
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendVendorNotification">Send vendor notification</a>
	</td>
	<td>
	5:30
	</td>
</tr>
-->
<tr class="alt">
<td>
	8-
	</td>
	<td>
	Close CARS
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingCloseCars">Close CARS</a>
	</td>
	<td>
	5:45
	</td>
</tr>
<tr >
<td>
	9-
	</td>
	<td>
	Export Hex values
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingExportHexValues">Export Hex values</a>
	</td>
	<td>
	6:30
	</td>
</tr>
<tr >
<td>
	10-
	</td>
	<td>
	Export RRD Feeds
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingExportRRDFeeds">Export RRD Feeds</a>
	</td>
	<td>
	22:15
	</td>
</tr>
<!--  
<tr class="alt">
<td>
	11-
	</td>
	<td>
	Send vendor Sample Escalation list
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=invokeSendVendorSampleEscalationList">Send vendor Sample Escalation list</a>
	</td>
	<td>
	22:30
	</td>
</tr>
-->
<!-- 
<tr >
<td>
	12-
	</td>
	<td>
	Send vendor CAR escalation
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendVendorCarEscalationList">Send vendor CAR escalation</a>
	</td>
	<td>
	23:00
	</td>
</tr>
-->
<tr >
<td>
	13-
	</td>
	<td>
	Import RRD Feed
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingImportRRDFeeds">Import RRD Feed</a>
	</td>
	<td>
	23:59
	</td>
</tr>
<tr>
<td>
	14-
	</td>
	<td>
	Unlock CARS
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingUnlockAllCars">UnLock CARS</a>
	</td>
	<td>
	23:45
	</td>
</tr>
<!-- <tr>
	<td>
		15-
		</td>
		<td>
		Send Late CARS report
		</td>
		<td>
		<a  href="../oma/runScheduledJob.html?method=methodInvokingSendLateCarReport">Send Late CARS report</a>
		</td>
		<td>
		23:10
	</td>
</tr> -->
<tr>
	<td>
	15-
	</td>
	<td>
	Import BEL_INV_DATA@BM_EXTERNAL Inventory into temp table
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingImportInventoryData">Import Inventory</a>
	</td>
	<td>
	13:12
	</td>
	</tr>
<tr>
	<td>
	16-
	</td>
	<td>
	load MC images
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingLoadMCImage">Load Media Compass Images</a>
	</td>
	<td>
	22:00
	</td>
</tr>
<tr>
<td>
	17-
	</td>
	<td>
	Import Media Compass temporary image feed
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingImportMCImageFeed">Import MC image feed</a>
	</td>
	<td>
	21:30
	</td>
</tr>
<tr>
<td>
	18-
	</td>
	<td>
	Import and Update FACET attribute into car_attribute table
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingImportAndUpdateFacetAttribute">Update Cars Attribute</a>
	</td>
	<td>
	
	</td>
</tr>
<!--
<tr>
	<td>
	19-
	</td>
	<td>
	Size conversion manager
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingResynchSizeValuesOfVendorSkus">Resync size values</a>
	</td>
	<td>
	13:12
	</td>
</tr>

<tr>
	<td>
	20-
	</td>
	<td>
	Super Color conversion manager
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingResynchColorValuesOfVendorSkus">Resync Color values</a>
	</td>
	<td>
	13:15
	</td>
</tr>
-->
<tr>
	<td>
	21-
	</td>
	<td>
	Send uploaded vendor image XML feed to RRD for mechanical check 
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendVendorImagesFeedForMCToRRD">send Vendor Images feed for MC</a>
	</td>
	<td>
	22:00
	</td>
</tr>
<tr>
	<td>
	22-
	</td>
	<td>
	Send vendor image update XML feed to RRD 
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendVendorImagesUpdateFeedToRRD">send Vendor Images update feed</a>
	</td>
	<td>
	24:00
	</td>
</tr>
<tr>
	<td>
	23-
	</td>
	<td>
	Automatically approve images
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingAutomaticallyApproveImage">Automatically approve images </a>
	</td>
	<td>
	20:00
	</td>
</tr>
<tr>
	<td>
	24-
	</td>
	<td>
	Send Image Rejection Mail
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingSendImageRejectionMail">Send Image Rejection Mail</a>
	</td>
	<td>
	23:55
	</td>
</tr>

<tr>
	<td>
	25-
	</td>
	<td>
		import vendor image history XML feed from RRD 
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingImportVendorImageHistoryFeed">import vendor image history feed  </a>
	</td>
	<td>
	23:00
	</td>
</tr>
<tr>
	<td>
	26-
	</td>
	<td>
	Import Vendor Image Mechanical & Creative check XML from RRD
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingimportVendorImageCheckFeedbackFromRRD">Import vendor image check feedback details feed </a>
	</td>
	<td>
	02:00 & 05:15
	</td>
</tr>
<tr>
	<td>
	27-
	</td>
	<td>
	Export Vendor Expedited Shipping
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=exportVenodrExpeditedShipping">Export Vendor Expedited Shipping</a>
	</td>
	<td>
	11:30
	</td>
</tr>
<tr>
	<td>
	28-
	</td>
	<td>
	Export PWP && GWP Attributes
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=exportPWPandGWPAttributes">Export PWP and GWP Attributes</a>
	</td>
	<td>
	17:30
	</td>
</tr>
<tr>
	<td>
	29-
	</td>
	<td>
	Export Resync Attribute  values
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodInvokingAttributeResynch">Export Resync Attribute  values</a>
	</td>
	<td>
	00:15
	</td>
</tr>
<tr>
	<td>
	30-
	</td>
	<td>
	Process PIM Attribute Updates To Cars
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodToProcessPIMAttributeUpdates">Process PIM Attribute Updates To Cars</a>
	</td>
	<td>
	Every 1 Hour
	</td>
</tr>
<tr>
	<td>
	30-
	</td>
	<td>
	Process PIM Attribute Updates To CMP for closed Cars
	</td>
	<td>
	<a  href="../oma/runScheduledJob.html?method=methodToProcessPIMAttributeUpdatesForClosedCars">Process PIM Attribute Updates To CMP for closed Cars</a>
	</td>
	<td>
	 00:15 AM Every Day
	</td>
</tr>
</table>
</body>
</html>