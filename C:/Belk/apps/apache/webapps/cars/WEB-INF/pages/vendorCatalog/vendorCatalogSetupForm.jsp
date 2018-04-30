<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="transition.profile.title"/></title>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div class="x-panel-tl"><div class="x-panel-tr"><div class="x-panel-tc"><div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;"><div class="x-tool x-tool-toggle" id="ext-gen14"> </div><span class="x-panel-header-text">
			Add Catalog
	</span></div></div></div></div>
	<div id="user_list" class="cars_panel x-panel">
		<div class="x-panel-bwrap" id="ext-gen12">
			<div class="x-panel-ml"><div class="x-panel-mr">
				<div class="x-panel-mc">
					<div class="x-panel-body" id="ext-gen11" style="height: auto;">



    




<form action="../cars/VendorCatlogSetup.jsp?mode=edit" method="post" id="userForm">
	<input type="hidden" value="" name="id" id="id"/>
	<input type="hidden" value="0" name="version" id="version"/>
	<input type="hidden" value="list" name="from"/>
	
	

	
	
	<ol>
		<li class="first">
		<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		</li>
		<li class="email">
		
		
			<label class="desc" for="emailAddress">Vendor # <span class="req">*</span></label>			
				<input type="text" value="" class="text large" name="emailAddress" id="VendorId"/>
				
				<a href="#" onClick="verify();" value="Verify" name="Verify" class="btn1" style="background:#CEDFF5 url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) repeat-x scroll center bottom;
border:1px solid #AAAAAA;
color:#15428B;
cursor:pointer;
font-size:13px;
font-weight:bold;
margin-right:10px;
padding:0;
text-decoration:none;
"/>Verify</a>
			
		
		
		</li>
		
		<li class="first">
			<label class="desc" for="firstName">Vendor Name</label>
			<label id="VendorName1" class="desc" for="firstName" style="display:none;text-align:left;">Lenox</label>			
			<!--<label id="VendorName1"></label>-->
	<!--	<input type="text" maxlength="50" value="" class="text" name="firstName" id="VendorName1" disabled/>-->
				
		</li>
		<li class="last">
			<label class="desc" for="lastName">Catalog Name <span class="req">*</span></label>			
			<input type="text" maxlength="50" value="" class="text" name="lastName" id="catalogName" disabled/>
			
		</li>
		<li class="last" >
		
			<label >Department(s) <span class="req">*</span></label>
			<ol style="float:left;width:140px;height:70px;border:1px solid #336699;overflow-y: scroll; overflow-x: hidden;">
			
			<li id="dept" disabled>
				<input type="checkbox" name="wow[]"> 827-HSEWRS<br>
				<input type="checkbox" name="wow[]"> 627-LUGGAGE<br>
				<input type="checkbox" name="wow[]"> 743-UTILITY<br>
				<input type="checkbox" name="wow[]"> 744-TABLE LINENS<br>
				<input type="checkbox" name="wow[]"> 769-Bed<br>
				<input type="checkbox" name="wow[]"> 746-BATH/RUGS<br>
				<input type="checkbox" name="wow[]"> 747-MOD BEDDING<br>
				<input type="checkbox" name="wow[]"> 748-BTR TOP OF BED<br>
				<input type="checkbox" name="wow[]"> 734-Bedding<br>
				<input type="checkbox" name="wow[]"> 761-CHINA<br>
				<input type="checkbox" name="wow[]"> 766-CRYSTAL<br>
				<input type="checkbox" name="wow[]"> 771-SILVER/STNLS<br>
			</li>
			</ol>
			
		</li>
		
		<li class="messages">
			
		</li>
	</ol>
</form>
<div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;">
<div class="x-tool x-tool-toggle" id="ext-gen14"> 
</div>
<span class="x-panel-header-text" id="ext-gen48">
		File Details
	</span>
	</div>
	<form id="userForm" method="post" action="../cars/VendorCatlogSetup.jsp?mode=edit">
	<input type="hidden" id="id" name="id" value=""/>
	<input type="hidden" id="version" name="version" value="0"/>
	<input type="hidden" name="from" value="list"/>
	
	

	
	
	<ol>
		
		<li class="email">
		
		
			<label for="emailAddress" class="desc">File Name:<span class="req">*</span></label>			
				<input type="text" id="fileName" name="emailAddress" style="width:175px;" class="text large" value="" disabled/>
		<a href="#">	<input type="submit" value="Browse" name="Verify" style="background:#CEDFF5 url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) repeat-x scroll center bottom;
border:1px solid #AAAAAA;
color:#15428B;
cursor:pointer;
font-size:13px;
font-weight:bold;
margin-right:10px;
padding:0;
text-decoration:none;
" /></a>
		
		</li>
		
		
		<div id="test14">
			
		
			</div>
			
		
		
		<li class="first">
			<label for="firstName" class="desc">File Format: <span class="req">*</span></label>			
			<select class="text" name="stateCd" id="fileFormat" onChange="showdelimiter(this);" disabled>
					
                    <option value="M">Microsoft Office Excel File</option>
                    <option value="T">Text Files(*.txt , *.cvs)</option>
                    <option value="X">XML Files(*.xml)</option>
                </select>
				
		</li>
		<li id="delimiter"  style="display:none" class="first">
			<label for="firstName" class="desc">Delimiter: <span class="req">*</span></label>			
			<select class="text" name="stateCd"  id="stateCd">
					
                    <option value="M">|</option>
                    <option value="T">,</option>
                    <option value="X">;</option>
					<option value="X">:</option>
                </select>
				
		</li>		
	
	
	
	
	
		
		<li class="messages">
			
		</li>
	</ol>
</form>
	
	<div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;">
<div class="x-tool x-tool-toggle" id="ext-gen14"> 
</div>
<span class="x-panel-header-text" id="ext-gen48">
		Image Details
	</span></div>
	<form id="userForm" method="post" action="../cars/VendorCatlogSetup.jsp?mode=edit">
	<input type="hidden" id="id" name="id" value=""/>
	<input type="hidden" id="version" name="version" value="0"/>
	<input type="hidden" name="from" value="list"/>
	
	

	
	
	<ol>
		
		<li class="email">
			<label for="emailAddress" class="desc">Upload Images: <span class="req">*</span></label>
					<select class="text" name="uploadImg" onchange="setAndReset(this);" id="imgLoc1" disabled>					
                    <option value="Y">Yes</option>
                    <option value="N">No</option>                    
                </select>			
		</li>
		<ul id="address" style="display:block">
		<li class="email">
			<label for="emailAddress" class="desc">Image Location: <span class="req">*</span></label>			
				<select class="text" name="imgFrom" onchange="setAndResetImg(this);" id="imgLoc" disabled>
					
                    <option value="CDs">Upload from CD</option>
                    <option value="FTP">Retreive from Vendor FTP site</option>
                    <option value="CDs">CD Upload from previous Catalog</option>
                </select>
		</li>
		<li class="first" id="CD" style="display:block;">
			<label for="firstName" class="desc">CD Location :<span class="req">*</span></label>			
			<input type="text" id="cdLoc" name="firstName" class="text" value="" style="width:220px;" maxlength="80" disabled/>
			<a href="#"><input type="submit" value="Browse" name="Verify" style="background:#CEDFF5 url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) repeat-x scroll center bottom;
border:1px solid #AAAAAA;
color:#15428B;
cursor:pointer;
font-size:13px;
font-weight:bold;
margin-right:10px;
padding:0;
text-decoration:none;
"/>	</a>
		</li>
		</ul>
		<ul id="ftpAddr" style="display:none">
		<li class="last">
			<label for="lastName" class="desc">FTP URL:<span class="req">*</span></label>			
			<input type="text" id="ftpURL" name="lastName" style="width:220px;" class="text" value="" maxlength="50" disabled/>
			
		</li>
		<li class="addr1">
			<label id="ext-gen20" for="address.address1" class="desc">FTP User Name:</label>
			<input type="text" id="ftpUserName" style="width:220px;" name="addr1" class="text" value="" disabled/>
			
		</li>
		<li class="addr1">
			<label id="ext-gen20" for="address.address1" class="desc">FTP Password:</label>
			<input type="text" id="ftpPswd" style="width:220px;" name="addr1" class="text" value="" disabled/>
			
		</li>
		</ul>
		<li class="messages">
			
		</li>
	</ol>
</form>			

	<div class="x-panel-header x-unselectable" id="ext-gen10" style="-moz-user-select: none;">
<div class="x-tool x-tool-toggle" id="ext-gen14"> 
</div>
<span class="x-panel-header-text" id="ext-gen48">
		Options
	</span></div>
<form id="userForm" method="post" action="../cars/OpenCatalog.jsp">
	<input type="hidden" id="id" name="id" value=""/>
	<input type="hidden" id="version" name="version" value="0"/>
	<input type="hidden" name="from" value="list"/>
	
	

	
	
	<ol>
		
		<li class="email">
		
		
			<label for="emailAddress" class="desc">Update Existing  Styles: <span class="req">*</span></label>			
				<select class="text" name="stateCd" onChange="displaymsg(this)" id="updateAction" disabled>
					
                    <option value="U">Update Existing Styles</option>
                    <option value="A">Append New styles only</option>
                    <option value="O">Overwrite Existing Catalog and Replace **</option>
                </select>
				
			
		
		
		</li>
		
	<fieldset id="test17">
	<ol>
		<li class="messages" id="msg" style="color:#FF0000;display:none">
		**Overwriting and Existing Catalog and replacing with new will delete all existing style information stored for this vendor <br></br></br>
		
		</li>
		<li id="test13">
		
	<input type="button" value="Cancel" name="cancel" class="btn cancel_btn" onClick="verifyCancel();"/>
			<a style="BORDER-RIGHT: #aaaaaa 1px solid; PADDING-RIGHT: 5px; BORDER-TOP: #aaaaaa 1px solid; DISPLAY: block; FONT-WEIGHT: bold; FONT-SIZE: 13px; BACKGROUND: url(../extjs-resources/images/default/tabs/tab-strip-bg.gif) #cedff5 repeat-x center bottom; FLOAT: left; PADDING-BOTTOM: 2px; BORDER-LEFT: #aaaaaa 1px solid; COLOR: #15428b; MARGIN-RIGHT: 10px; PADDING-TOP: 2px; BORDER-BOTTOM: #aaaaaa 1px solid; TEXT-DECORATION: none" onClick="" href="#">Save</a>
			<br style="clear: both;"/>
		
		</li>
	</ol>
	</fieldset>	
	</ol>
	
</form>	


	</div></div></div></div><div class="x-panel-bl x-panel-nofooter"><div class="x-panel-br"><div class="x-panel-bc"/></div></div></div>
		</div> 	<!--end division ext-gen27-->
		
	
		 		
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