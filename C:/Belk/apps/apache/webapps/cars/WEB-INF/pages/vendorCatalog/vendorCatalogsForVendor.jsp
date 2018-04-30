<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="transition.profile.title"/></title>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">


  <div class="cars_panel x-panel" id="search_for_attribute"><div class="x-panel-tl"><div class="x-panel-tr"><div class="x-panel-tc"><div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;"><div class="x-tool x-tool-toggle" id="ext-gen14"> </div><span class="x-panel-header-text">
	Vendor Info </span></div></div></div></div>

<div id="ext-gen12" class="x-panel-bwrap">
<div class="x-panel-ml">
<div class="x-panel-mr" id="ext-gen174">
<div class="x-panel-mc">
<div id="ext-gen11" class="x-panel-body" style="height: auto;">

<div>
		<ul class="car_info">
			
			<li class = "car_id" style="width:24%;">
				<b>Vendor #:</b>
				7600056
			</li>			
			<li class = "car_id" style="width:22%;">
				<b># of Catalog Styles:</b>
				2,098
			</li>
			<li class = "car_id" style="width:22%;">
				<b>Date Last Import:</b>
				6/1/2009
			</li>			
		</ul>
		</div>
		<div>
		<ul class="car_info">
			<li class ="car_id" style="width:24%;">
				<b>Vendor Name:</b>
				Lenox
			</li>
			<li class ="car_id" style="width:22%;">
				<b># of Catalog SKUs:</b>
				3,494				
			</li>						
			<li class ="car_id" style="width:25%;">
				<b>Last Catalog Imported</b>
				FTP Catalog
			</li>
				
		</ul>
		</div>
</div>



</div>
</div>
</div>
<!--The box for vendor info ends-->

  <div class="cars_panel x-panel" id="search_for_attribute"><div class="x-panel-tl"><div class="x-panel-tr"><div class="x-panel-tc"><div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;"><div class="x-tool x-tool-toggle" id="ext-gen14"> </div><span class="x-panel-header-text">
		Catalog Search
	</span></div></div></div></div>
	
	<div class="x-panel-bwrap" id="ext-gen12"><div class="x-panel-ml"><div class="x-panel-mr"><div class="x-panel-mc"><div class="x-panel-body" id="ext-gen11" style="height: auto;">

	<form id="searchForm" action="#" method="post">
		<div>
		<ul class="car_info">
			<li style="width:20%" class = "car_id">
				<b style="float:left">Catalog Name:</b>
				<input style="float:right" size="13" type="text" value="" name="attributeName" />	
			</li>
			<li style="width:21%" class = "dept">
				<b style="float:left"> Department #:</b>
				<input  style="float:right" size="10" type="text" value="" name="classificationId" />
			</li>
			<li style="width:30%">
				<b style="float:left">Date Last Import(Start): </b>
			<input style="float:right" size="13" type="text" value="" name="classificationId" />	
			</li>

			<li >
				<input style="BORDER-RIGHT: #aaaaaa 1px solid; PADDING-RIGHT: 2px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; PADDING-LEFT: 2px; FONT-WEIGHT: bold; FONT-SIZE: 13px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-RIGHT: 1px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid; TEXT-DECORATION: none" type="submit" name="searchPattern" value="Search" />
				
								
			</li>
		</ul>
		</div>
		<div>
		<ul class="car_info">
		
			<li style="width:22%" class = "car_id">
				<b style="float:left">User Departments only</b>
				<input style="float:left"  style="width:15%" id="isSearchable" type="checkbox" value="true" name="isSearchable"/>
			</li>
			<li style="width:19%" class = "dept">
				<b style="float:left;width:10px" >Status:</b>
				<select style="float:right" id="sel_vendorStyleType" class="required" name="vendorStyleTypeCode">
	<option value="PATTERN-SGBS-VS">Active</option>
	<option id="ext-gen96" value="PATTERN-SSKU-VS">Inactive</option>
	<option id="ext-gen106" value="PATTERN-SGBMT-VS">All</option>
  
	</select>				
			</li>
			<li style="margin-left:2%">
				<b style="float:left">Date Last Import(End): </b>&nbsp;&nbsp;
			<input style="" size="13" type="text" value="" name="classificationId" />
			</li>
		
		  
			<li>
			<a href='#' id="a_view_all">View All</a>	
			</li>
			
		</ul>
		</div>

		</form>
</div></div></div></div><div class="x-panel-bl x-panel-nofooter"><div class="x-panel-br"><div class="x-panel-bc" id="ext-gen27"/></div></div></div></div>
</div>
	<div id="user_list" class="cars_panel x-panel">
		<div id="ext-gen27" class="x-panel-tl">
			<div class="x-panel-tr">
				<div class="x-panel-tc">
					<div id="ext-gen18" class="x-panel-header x-unselectable" style="-moz-user-select: none;">
						<div id="ext-gen22" class="x-tool x-tool-toggle"> </div>
							
													
							<span id="ext-gen43" class="x-panel-header-text"> Vendors Catalogs

	

	</span>
							
							
						</div>
					</div> <!--end division ext-gen18-->
				</div> <!--end division x-panel-tc-->
			</div> <!--end division x-panel-tr-->
		</div> 	<!--end division ext-gen27-->
		<div id="ext-gen18" class="x-panel-bwrap">
          <div class="x-panel-ml" id="ext-gen107">
          	<div class="x-panel-mr">
				<div class="x-panel-mc" id="ext-gen93">
              		<div id="ext-gen19" class="x-panel-body" style="width: 915px; height: 261px;">
		              	<div id="ext-gen37" class="x-grid3" hidefocus="true" style="width: 910px; height: 261px;">
						<div style="margin-bottom:7px;">
                                3 vendor catalogs found,displaying 1 to 3.
                                                                        [ <a href="#">  First</a> /<a href="#">  Prev</a>] <a href="#"> 1</a>  <a href="#"> 2</a> <a href="#">  3</a>[ <a href="#"> <a href="#">   Next</a> /<a href="#">Last</a>]                                                                          
                             </div>
							<div id="ext-gen38" class="x-grid3-viewport">
                              <div id="ext-gen39" class="x-grid3-header">
                              	<div id="ext-gen46" class="x-grid3-header-inner" style="width: 930px;">
                              		<div class="x-grid3-header-offset" id="ext-gen113">
										<table cellspacing="0" cellpadding="0" border="0" style="width: 930px;">
											<thead>
												<tr class="x-grid3-hd-row">
													
													 <td class="x-grid3-hd x-grid3-cell x-grid3-td-car_id" style="width:4%;">
                                                        <div class="x-grid3-hd-inner x-grid3-hd-car_id" style="" unselectable="on" id="ext-gen95">
                                                        
                                                        ID
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                      </td>		
													  <td class="x-grid3-hd x-grid3-cell x-grid3-td-source" style="width: 9%;">
                                                        <div id="ext-gen97" class="x-grid3-hd-inner x-grid3-hd-source" style="" unselectable="on">
                                                        
                                                        Name
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                        </td>		
 														<td class="x-grid3-hd x-grid3-cell x-grid3-td-dept_num" style="width: 7%;">
                                                         <div id="ext-gen139" class="x-grid3-hd-inner x-grid3-hd-dept_num" style="" unselectable="on">
                                                         
                                                        Source
                                                         <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                         </div>
                                                         </td>	
														 <td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

Status
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>			
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

Completion Date
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>	
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

Date Last Updated
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>	
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 6%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

Last Updated By
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>		
<td class="x-grid3-hd x-grid3-cell x-grid3-td-lock" style="width: 10%;">
<div id="ext-gen149" class="x-grid3-hd-inner x-grid3-hd-lock" style="" unselectable="on">

Lock
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</td>		
												</tr>
												
											</thead>											
                                        </table>
									</div> <!--end division x-grid3-header-offset-->
								</div> <!--end division ext-gen46-->
							  </div>	<!--end division ext-gen39-->	
							  <div id="ext-gen40" class="x-grid3-scroller" style="overflow-x: hidden; width: 936px; height: 237px;">
								<div id="ext-gen41" class="x-grid3-body">
									<div class="x-grid3-row" style="width: 930px;">
<table class="x-grid3-row-table" cellspacing="0" cellpadding="0" border="0" style="width: 930px;">
<tbody>
<tr>
													
													 <td class="x-grid3-hd x-grid3-cell x-grid3-td-car_id" style="width:4%;">
                                                        <div class="x-grid3-hd-inner x-grid3-hd-car_id" style="" unselectable="on" id="ext-gen95">
                                                        
                                                       <a href="../cars/VendorCatlogSetup.jsp?mode=view"> 756</a>
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                      </td>		
													  <td class="x-grid3-hd x-grid3-cell x-grid3-td-source" style="width: 9%;">
                                                        <div id="ext-gen97" class="x-grid3-hd-inner x-grid3-hd-source" style="" unselectable="on">
                                                        
                                                       DropShip Product Upload
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                        </td>		
 														<td class="x-grid3-hd x-grid3-cell x-grid3-td-dept_num" style="width: 7.5%;">
                                                         <div id="ext-gen139" class="x-grid3-hd-inner x-grid3-hd-dept_num" style="" unselectable="on">
                                                         
                                                         Manual upload
                                                         <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                         </div>
                                                         </td>	
														 <td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">
 Importing
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>			
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

05/29/09
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>	
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

05/29/09
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 6%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

John User
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>		
<td tabindex="0" style="width: 4%;" class="x-grid3-col x-grid3-cell x-grid3-td-lock x-grid3-dirty-cell"><div unselectable="on" class="x-grid3-cell-inner x-grid3-col-lock" id="ext-gen98"><tpl for="."><a ext:qtip="Locked By: carsadmin@belk.com" ajax_href="/cars/ajaxMainMenu.html?param=setLockedUnlock&amp;unlock=Y&amp;ajax=true&amp;car=35493" href="#" class="unlock btn" id="ext-gen68">Unlock</a></tpl></div></td>
<td tabindex="0" style="width: 4%; " class="x-grid3-col x-grid3-cell x-grid3-td-link x-grid3-cell-last"><div unselectable="on" class="x-grid3-cell-inner x-grid3-col-link" id="ext-gen114"><a title="Edit CAR" href="../cars/VendorCatlogSetup.jsp?mode=edit" class="edit_car btn" id="ext-gen96">Edit</a> </div></td>		

												</tr>

<tr style ="BACKGROUND-COLOR: #fafafa">
													
													 <td class="x-grid3-hd x-grid3-cell x-grid3-td-car_id" style="width: 4%;">
                                                        <div class="x-grid3-hd-inner x-grid3-hd-car_id" style="" unselectable="on" id="ext-gen95">
                                                        
                                                       <a href="../cars/VendorCatlogSetup.jsp?mode=view"> 4877</a>
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                      </td>		
													  <td class="x-grid3-hd x-grid3-cell x-grid3-td-source" style="width: 9%;">
                                                        <div id="ext-gen97" class="x-grid3-hd-inner x-grid3-hd-source" style="" unselectable="on">
                                                        
                                                       CH-Feed 5-29-09
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                        </td>		
 														<td class="x-grid3-hd x-grid3-cell x-grid3-td-dept_num" style="width:7%;">
                                                         <div id="ext-gen139" class="x-grid3-hd-inner x-grid3-hd-dept_num" style="" unselectable="on">
                                                         
                                                         CommerceHub
                                                         <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                         </div>
                                                         </td>	
														 <td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

Data Mapping
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>			

<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

05/29/09
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

05/29/09
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 6%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

System
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>		
<td class="x-grid3-col x-grid3-cell x-grid3-td-lock" tabindex="0" style="width: 5%;">
<div class="x-grid3-cell-inner x-grid3-col-lock" unselectable="on">
<a class="lock btn" title="Lock CAR for edit" ajax_href="/cars/ajaxMainMenu.html?param=setLockedUnlock&lock=Y&ajax=true&car=37362" href="#" id="ext-gen109">Lock</a>
</div>
</td>
<td tabindex="0" style="width: 5%;" class="x-grid3-col x-grid3-cell x-grid3-td-link x-grid3-cell-last"><div unselectable="on" class="x-grid3-cell-inner x-grid3-col-link"> </div></td>		

												</tr>	
<tr>
													
													 <td class="x-grid3-hd x-grid3-cell x-grid3-td-car_id" style="width:4%;">
                                                        <div class="x-grid3-hd-inner x-grid3-hd-car_id" style="" unselectable="on" id="ext-gen95">
                                                        
                                                       <a href="../cars/VendorCatlogSetup.jsp?mode=view"> 4987</a>
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                      </td>		
													  <td class="x-grid3-hd x-grid3-cell x-grid3-td-source" style="width: 9	%;">
                                                        <div id="ext-gen97" class="x-grid3-hd-inner x-grid3-hd-source" style="" unselectable="on">
                                                        
                                                       FTP Catalog
                                                        <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                        </div>
                                                        </td>		
 														<td class="x-grid3-hd x-grid3-cell x-grid3-td-dept_num" style="width: 7%;">
                                                         <div id="ext-gen139" class="x-grid3-hd-inner x-grid3-hd-dept_num" style="" unselectable="on">
                                                         
                                                         Vendor Direct
                                                         <img class="x-grid3-sort-icon" src="images/s.gif"/>
                                                         </div>
                                                         </td>	
														 <td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

 Complete
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>			

<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

6/1/2009
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 5%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

05/29/09
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>
<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor" style="width: 6%;">
<div id="ext-gen58" class="x-grid3-hd-inner x-grid3-hd-vendor" style="" unselectable="on">

System
<img class="x-grid3-sort-icon" src="images/s.gif"/>
</div>
</td>		
<td class="x-grid3-col x-grid3-cell x-grid3-td-lock" tabindex="0" style="width: 5%;">
<div class="x-grid3-cell-inner x-grid3-col-lock" unselectable="on">
<a class="lock btn" title="Lock CAR for edit" ajax_href="/cars/ajaxMainMenu.html?param=setLockedUnlock&lock=Y&ajax=true&car=37362" href="#" id="ext-gen109">Lock</a>
</div>
</td>
<td tabindex="0" style="width: 5%; " class="x-grid3-col x-grid3-cell x-grid3-td-link x-grid3-cell-last"><div unselectable="on" class="x-grid3-cell-inner x-grid3-col-link"> </div></td>		

												</tr>												

										</tbody>
									</table>
									</div> <!--end division x-grid3-row x-grid3-row-selected-->
									
</br>
</br>
</br>
</br>
</br>
</br>
</br>
</br>
									<br/><div id="ext-gen29">
<a style="BORDER-RIGHT: #aaaaaa 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; PADDING-LEFT: 5px; FONT-WEIGHT: bold; FONT-SIZE: 13px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-left: 10px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid; TEXT-DECORATION: none" title="" href="../cars/VendorCatlogSetup.jsp?mode=addForVendor">New Catalog</a>
</div>								

								</div> <!--end division ext-gen41-->
								
							  </div>		<!--end division ext-gen40-->
		
							</div> 	<!--end division ext-gen38-->
													
						</div><!--end division ext-gen37-->

					</div><!--end division ext-gen19-->

				</div><!--end division x-panel-mc-->

			</div><!--end division x-panel-mr-->
	
		  </div>	<!--end division x-panel-ml-->
	
		  <div class="x-panel-bl x-panel-nofooter">
<div class="x-panel-br">
<div id="ext-gen66" class="x-panel-bc"/>
</div>
</div>			
		</div>		<!--end division ext-gen18-->				
		
	</div>  <!--end division user_list-->



</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){
		return confirm('<fmt:message key="transition.confirm.delete"/>');
	});
});
</script>
]]>
</content>