<script type="text/javascript">
	function dropdwn(box){
		if(box.value == "fromVendor"){
			document.getElementById("importMap").style.display = 'block';
			document.getElementById("existingDataMapDrpdwn").style.display = 'none';
			
		}
		if(box.value == "newDataMap"){
			document.getElementById("importMap").style.display = 'none';
			document.getElementById("existingDataMapDrpdwn").style.display = 'none';
		}
		if(box.value == "existingDataMap"){
			document.getElementById("importMap").style.display = 'none';
			document.getElementById("existingDataMapDrpdwn").style.display = 'block';
		}
		if(box.value == "globalMap") {
			document.getElementById("importMap").style.display = 'none';
			document.getElementById("existingDataMapDrpdwn").style.display = 'block';
		}
	}
	
	function verifyCancel(){
		var stay=confirm("Are you sure you want to cancel?");
		if (stay){
			window.location="../cars/VendorCatlogSetup.jsp?mode=edit";
		}
		else{
			return false;
		}
	}
	
	function saveAndComplete(){
		document.getElementById('saveNote').style.display = 'inline';
		document.getElementById('saveNote').style.background = '#FFFF00';
		
		dataMapOptValue=document.getElementById("dataMapOpt").options[document.getElementById("dataMapOpt").selectedIndex].text;
		importMapVenValue=document.getElementById("importMapVen").options[document.getElementById("importMapVen").selectedIndex].text;
		existingMapValue=document.getElementById("existingMap").options[document.getElementById("existingMap").selectedIndex].text;
		
		document.getElementById("dataMapOpt").options[document.getElementById("dataMapOpt").selectedIndex].text =dataMapOptValue;
		document.getElementById("importMapVen").options[document.getElementById("importMapVen").selectedIndex].text =importMapVenValue;
		document.getElementById("existingMap").options[document.getElementById("existingMap").selectedIndex].text =existingMapValue;
	}
</script>
<div id="content"> <!-- Start of the div content -->
	<div id="main"> <!-- Start of the div main -->
	<div style="margin-left: 1%;">
		<a href="../cars/VendorCatlogManagement.jsp">Vendors</u></a>
		<a href="../cars/VendorCatalogs.jsp"><u>Lenox</u></a>Data Mapping<br>
		<br><b>Vendor Catalog </b><br><br>
	</div> <!-- End of the div main -->
	
	<!--  Start of  search_for_attribute div -->
	<div class="cars_panel x-panel" id="search_for_attribute">
		<div class="x-panel-tl"> <!-- Start of the div x-panel-tl -->
			<div class="x-panel-tr">
				<div class="x-panel-tc">
					<div class="x-panel-header x-unselectable" id="ext-gen10"
						style="-moz-user-select: none;">
					<div class="x-tool x-tool-toggle" id="ext-gen14"></div>
					<span class="x-panel-header-text"> Catalog Info </span></div>
				</div>
			</div>
		</div> <!-- Start of the div x-panel-tl -->

		<!--  Start of ext-gen12 div -->
		<div class="x-panel-bwrap" id="ext-gen12">
			<div class="x-panel-ml"> <!--  Start of x-panel-ml div -->
			
				<!--  Start of x-panel-mc div -->
				<div class="x-panel-mc">
					<!--  Start of x-panel-body div -->
					<div class="x-panel-body" id="ext-gen11" style="height: auto;">
						
						<form id="searchForm" action="/admin/searchattribute.html" method="post">
							<div>
								<ul class="car_info">
									<li class="car_id"><b>Vendor #:</b> 5600007</li>
									<li class="dept"><b>Catalog ID</b> 1236</li>
									<li><b>Status: </b> Open</li>
								</ul>
							</div>
							<div>
								<ul class="car_info">
									<li class="car_id"><b>Vendor Name:</b> Lenox</li>
									<li class="dept"><b>Date Created:</b> 06/22/2009</li>
									<li><b>Date Last Updated:</b> 06/24/2009</li>
								</ul>
							</div>
							<ul class="car_info">
								<li class="car_id"><b>Catalog Name:</b> Fall Catalog</li>
								<li class="dept"><b>Created By:</b> Joe User</li>
								<li><b>Last Updated By:</b> Jane User</li>
							</ul>
							<ul class="car_info">
								<li class="car_id"><b>Catalog Source:</b> Manual</li>
								<li class="dept"></li>
								<li></li>
							</ul>
						</form>

					</div> <!--  End of x-panel-body div -->
				</div> <!--  End of x-panel-mc div -->
			</div> <!--  End of x-panel-ml div -->
			<div class="x-panel-bl x-panel-nofooter">
				<div class="x-panel-br">
					<div class="x-panel-bc" id="ext-gen27" /></div>
				</div>
			</div>
		</div> <!--  End of ext-gen12 div -->
	</div> <!--  End of  search_for_attribute div -->	

	<!--  Start of user_list div -->
	<div id="user_list" class="cars_panel x-panel">
		<div class="x-panel-tl">
			<div class="x-panel-tr">
				<div class="x-panel-tc">
					<div class="x-panel-header x-unselectable" id="ext-gen10"
						style="-moz-user-select: none;">
						<div class="x-tool x-tool-toggle" id="ext-gen14"></div>
						<span class="x-panel-header-text"> Data Mapping Options </span>
					</div>
				</div>
			</div>
		</div>
		
		<!--  Start of ext-gen12 div -->
		<div class="x-panel-bwrap" id="ext-gen12">
			<div class="x-panel-ml"> <!--  Start of x-panel-m1 div -->
				<div class="x-panel-mr"> <!--  Start of x-panel-mr div -->
					<div class="x-panel-mc"> <!--  Start of x-panel-mc div -->
			
						<!--  Start of ext-gen11 div -->
						<div class="x-panel-body" id="ext-gen11" style="height: auto;">
							<form id="userForm" method="post" action="/vendor/vendorForm.html">
								<input type="hidden" id="id" name="id" value="" /> <input type="hidden"
								id="version" name="version" value="0" /> 
								<input type="hidden" name="from" value="list" />
								
								<ol> <!--  Start of the OL in form -->
									<b style="margin-left: 1%;">Uploaded Data File:</b>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<a href="#">Catalog 1238.xls</a>
									<br/><br/>
									<li class="first">
										<label for="firstName" class="desc">
											Data Mapping Option: 
										</label> 
										<select class="text" name="stateCd" id="dataMapOpt" onChange="dropdwn(this);">
											<option value="newDataMap">Create new data map</option>
											<option value="existingDataMap">Use existing map for this
											vendor</option>
											<option value="globalMap">Use globally fefined map</option>
											<option value="fromVendor">Use map from another vendor</option>
										</select>
									</li>
									<li class="first" id="importMap" style="display: none;">
										<label for="firstName" class="desc">
											Import Map from the vendor: 
										</label> 
										<select class="text" name="stateCd" id="importMapVen">
											<option value="AK">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;
											</option>
										</select>
									</li>
									<li class="first" id="existingDataMapDrpdwn" style="display: none;">
										<label for="firstName" class="desc">
											Existing Data Map: 
										</label> 
										<select class="text" name="stateCd" id="existingMap">
											<option value="AK">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
												&nbsp;&nbsp;&nbsp;
											</option>
										</select>
									</li>
									<li class="messages"></li>
								</ol> <!--  End of the OL in form -->
							</form> <!--  End form userForm -->
						</div> 	<!--  End of ext-gen11 div -->
					</div><!--  End of x-panel-mc div -->
				</div> <!--  End of x-panel-mr div -->
			</div> <!--  End of x-panel-m1 div -->
			
			<div class="x-panel-tl">
				<div class="x-panel-tr">
					<div class="x-panel-tc">
						<div class="x-panel-header x-unselectable" id="ext-gen10"
							style="-moz-user-select: none;">
							<div class="x-tool x-tool-toggle" id="ext-gen14"></div>
							<span class="x-panel-header-text"> Data Mapping Rules </span>
						</div>
					</div>
				</div>
			</div>

			<div class="x-panel-bwrap" id="ext-gen12">
				<div class="x-panel-ml">
					<div class="x-panel-mr">
						<div class="x-panel-mc">
							<div class="x-panel-body" id="ext-gen11" style="height: auto;">

								<form id="userForm" method="post" action="/vendor/vendorForm.html">
									</br>
									<li><b>CARS Product Type(s):</b>
									<ol
										style="border-style: solid; border-color: #8db2e3; border-width: 1px;; height: 50px; overflow-y: scroll; overflow-x: hidden; width: 150px; padding-left: 10px; margin-left: 150px;">
										<li><input type="checkbox" id="baby" />Baby & Toddler</li>
										<li><input type="checkbox" id="bath" />Bath & Body</li>
										<li><input type="checkbox" id="rug" />Bath/Rug</li>
										<li><input type="checkbox" id="rug" />Bath/Rug Accessories</li>
										<li><input type="checkbox" id="rug" />Bath/Baby</li>
										<li><input type="checkbox" id="rug" />Bath/Towels</li>
										<li><input type="checkbox" id="rug" />Bath/SoapCases</li>
										<li><input type="checkbox" id="rug" />Bath/Tiles</li>
									</ol>
									
									</li>
								</form>

								<br />
								<br />
								<!-- start of tables-->
			<div id="ext-gen11" class="x-panel-body" style="height: auto;">
			<table>
				<tbody>
					<tr>
						<td>
						<div
							style="height: 177px; overflow: auto; border-style: solid; border-color: #8db2e3; border-width: 1px;">
						<b>Vendor Suppiled Fields</b>
						<ol>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							UPC</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Product Name</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Image</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Color</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Cost</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							D/S Free</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Suggested Retail</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Description</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">
							Features</li>
						</ol>
						</div>
						</td>
						<td>
						<div
							style="height: 100px; overflow: auto; margin-left: 100px; border-style: solid; border-color: #8db2e3; border-width: 1px;">
						<b>CARS Attributes</b>
						<ol>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Vendor Style Description</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Product Name</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Brand</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Color</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Care</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Length</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Sleeve length</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Import/Domestic</li>
							<li
								style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 100px; background-color: rgb(246, 246, 246);">
							Material</li>
						</ol>
			
						</div>
						</td>
			
						<td>
						<div style="height: 111px; overflow: auto; padding-left: 72px;">
			
						<table align="center">
							<tbody>
			
								<tr>
									<input type="submit" value=">>" name="save" class="move_right" />
								</tr>
			
								<tr>
									<input type="submit" value="<<" name=" save" class="move_left" />
								</tr>
			
							</tbody>
						</table>
						</div>
						</td>
			
						<td>
						<div
							style="height: 70px; overflow: auto; margin-left: 15px; border-style: solid; border-color: #8db2e3; border-width: 1px;">
						<b>Mapped Fields</b>
						<table align="left" style="border: 1px solid rgb(208, 208, 208);">
							<tbody>
			
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">Vendor</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">CARS</td>
								</tr>
			
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">Mfg
									Style #</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">Vendor
									Style #</td>
								</tr>
			
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">Country</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">Import/Domestic</td>
								</tr>
			
							</tbody>
						</table>
						</div>
						</td>
			
						<td>
						<div
							style="height: 80px; margin-left: 114px; overflow: auto; border-style: solid; border-color: #8db2e3; border-width: 1px;">
						<b>Mapped Field Translation</b>
						<table align="center" style="border: 1px solid rgb(208, 208, 208);">
							<tbody>
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">Vendor
									Value</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; width: 129px; background-color: rgb(246, 246, 246);">CARS
									Value</td>
								</tr>
			
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">USA</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);"><select
										id="stateCd" class="text" name="stateCd" width="15">
										<option value="AK">Domestic</option>
										<option value="AK">Import</option>
			
									</select></td>
								</tr>
			
								<tr>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);">China</td>
									<td
										style="border: 1px solid rgb(208, 208, 208); padding: 3px; background-color: rgb(246, 246, 246);"><select
										id="stateCd" class="text" name="stateCd" width="15">
										<option value="AK">Domestic</option>
										<option value="AK">Import</option>
			
									</select></td>
								</tr>
			
							</tbody>
						</table>
						</div>
						</td>
			
			
						<div id="ext-gen46" class="x-grid3-header-inner"
							style="width: 264px;">
						<table align="center">
							<thead>
								<tr class="x-grid3-hd-row">
									<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor"
										style="width: 80px;"></td>
									<td class="x-grid3-hd x-grid3-cell x-grid3-td-vendor"
										style="width: 80px;"></td>
									<td class="x-grid3-hd x-grid3-cell x-grid3-td-status"
										style="width: 80px;"></td>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						</div>
			
						</div>
						</div>
						</div>
						</div>
						</div>




			<!-- end of tables-->

			<div class="x-panel-tl">
			<div class="x-panel-tr">
			<div class="x-panel-tc">
			<div class="x-panel-header x-unselectable" id="ext-gen10"
				style="-moz-user-select: none;">
			<div class="x-tool x-tool-toggle" id="ext-gen14"></div>
			<span class="x-panel-header-text"> Image Rules </span></div>
			</div>
			</div>
			</div>

			<div class="x-panel-bwrap" id="ext-gen12">
			<div class="x-panel-ml">
			<div class="x-panel-mr">
			<div class="x-panel-mc">

			<div class="x-panel-body" id="ext-gen11" style="height: auto;">
			<form id="userForm" action="/vendor/vendorForm.html" method="post">
			<input id="id" type="hidden" value="" name="id" /> <input
				id="version" type="hidden" value="0" name="version" /> <input
				type="hidden" value="list" name="from" />
			<table style="margin-top: 1%;">
				<tr>
					<td><input style="text-align: right" type="checkbox" id="baby" />
					</td>
					<td style="width: 50%; text-align: left;"><b>Field
					Included in the File Name(Main Image):</b></td>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<td style="margin-left: 90%;"><select>
						<option value="AK">Vendor Provided File Name(Main Image)</option>

					</select></td>

				</tr>
				<tr>
					<td><input type="checkbox" id="baby" /></td>
					<td style="width: 50%; text-align: left;"><b>Field
					Included in the File Name(Swatch):</b></td>
					<td><select id="stateCd" style="width: 100%;">
						<option value="AK">Vendor Provided File Name(Swatch)</option>

					</select></td>

				</tr>
				<tr>
					<td><input type="checkbox" id="baby" /></td>
					<td style="width: 50%; text-align: left;"><b>Field
					Included in the File Name(Alternative Image):</b></td>
					<td><select id="stateCd" style="width: 100%;">
						<option value="AK">Vendor Provided File Name(Alternative)</option>
						<option value="AK">Vendor Style</option>
						<option value="AK">Vendor Sku</option>
						<option value="AK">Vendor Provided File Name(Main Image)</option>
						<option value="AK">Vendor Provided File Name(Swatch
						Image)</option>
						<option value="AK">Vendor Provided File Name(Alternative
						Image)</option>

					</select></td>
				</tr>


			</table>
			</form>
			</br>
			</br>
			</br>
			</br>

			<div id="ext-gen29">
			<li style="margin-left: 10px"><input type="button"
				value="Cancel" name="cancel" class="btn cancel_btn"
				onClick="verifyCancel();" /> <a
				style="margin-left: 3px; BORDER-RIGHT: #aaaaaa 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; PADDING-LEFT: 5px; FONT-WEIGHT: bold; FONT-SIZE: 13px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-RIGHT: 10px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid; TEXT-DECORATION: none"
				href="#" title="">Save</a> <a
				style="margin-left: 3px; BORDER-RIGHT: #aaaaaa 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; PADDING-LEFT: 5px; FONT-WEIGHT: bold; FONT-SIZE: 13px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-RIGHT: 10px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid; TEXT-DECORATION: none"
				" onClick="saveAndComplete();" href="#" title="">Save and
			Complete</a>
			<div id="saveNote" style="display: none;">Mapping saved and
			completed successfully!</div>
			</li>
			</div>
			</div>

			</div>
			</div>
			</div>
			</div>
			<div class="x-panel-bl x-panel-nofooter">
			<div class="x-panel-br">
			<div class="x-panel-bc"></div>
			</div>
			</div>


			</div>

			</td>
		</tr>
	</tbody>
</table>