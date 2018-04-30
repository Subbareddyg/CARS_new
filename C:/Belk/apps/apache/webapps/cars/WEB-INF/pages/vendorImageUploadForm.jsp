<%@ include file="/common/taglibs.jsp"%>

<div class="admin container" id="container_1" >
	<div class="div_spacer">
		<!--  <iframe name="frame_1" id="frame_1" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
		<input type="text" id="alternateImageCount" value= "${alternateImageCount}" style="width:0;height:0;visibility:hidden"  >
		<img id="progressImage_1" src="/cars/images/progressBar.gif" class="progressImage"/>
		<form:form commandName="uploadImagesDTO" method="post" id="uploadForm_1" enctype="multipart/form-data">
		<ul>
			<li>				
				<label>Image Location *:</label>
				<form:select path="imageLocationType" id="imageLocationType_1" items="${imageLocList}" multiple="false" >  </form:select>																		
			</li>		
			<li>
				<label>Select Image *:</label> 
				<input type="file" id="cdImageFile_1" accept="Image/jpeg,image/jpg,image/gif,image/png,image/psd" name="cdImageFile" />
			</li>
			
			<div id="ftpDiv_1" class="ftpDiv hidden" >
				<li>	 
						<label class="ftpLabel">Image Path to FTP (URL) *:</label> 
						<form:input path="ftpUrl" id="ftpUrl1"/>
						<span class="suggestion">Example: ftp.xyz.com </span> 
				</li>
				<li>
						 <label class="ftpLabel" >Folder :</label> 
						<form:input id="ftpPath" path="ftpPath" />
						 <span class="suggestion">Example: /myPictures/belk/ </span>
				</li>	 
				<li>
						 <label class="ftpLabel">Image Name *:</label> 
						<form:input id="ftpFileName" path="ftpFileName"  />
						 <span class="suggestion" > Example: shirt.jpg </span> 
				</li>	
				<li>
						<label class="ftpLabel">Username *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
			<li>
				<div>
				<label style="width: 138px;">Image Type *:</label>
				<a class="imageTypeHelpIcon" href="javascript:showImageTypeHelp();"></a>
				<form:select id="imageType_1" path="imageType" items="${ImageTypeList}" multiple="false">  </form:select>
				</div>
				</li>
				<li>
					<div class="ShotTypeCd_1" style="display: none;">
						<label>Shot Type *:</label>
						<form:select id="shotType1" path="shotTypeValue"
							items="${shotTypeMapList}" multiple="false">
						</form:select>
					</div>
				</li>
		</ul>
		<div id="Add Image " class="addImageButtonDiv_1" align="right">
			<input type="button" style ="height: 23px; cursor:pointer;	white-space: nowrap;  padding-right:5px; padding-left:10px; padding-bottom:2px; padding-top :2px  font:normal 11px tahoma, verdana, helvetica; overflow:visible; margin-right: 5px;"; value="Add Next Image" class="addButton" name="formSubmit"  id="addButton_1">
		</div>
		</form:form>
		<div id="result_1" class="status_msg"></div>
	</div>
</div>

<div class="admin container" id="container_2" style="display:none">
	<div class="div_spacer">
		<!--  <iframe name="frame_2" id="frame_2" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
		<img id="progressImage_2" src="/cars/images/progressBar.gif" class="progressImage"/>
		<form:form commandName="uploadImagesDTO" method="post" id="uploadForm_2" enctype="multipart/form-data">
		<ul>
					
			<li>				
				<label>Image Location *:</label>
				<form:select path="imageLocationType" id="imageLocationType_2" items="${imageLocList}" multiple="false" >  </form:select>																		
			</li>		
			<li>
				<label>Select Image *:</label> 
				<input type="file" id="cdImageFile_2" accept="Image/jpeg,image/jpg,image/gif,image/png,image/psd" name="cdImageFile" />
			</li>
			<div id="ftpDiv_2" class="ftpDiv hidden"  >
				<li>	 
						<label class="ftpLabel">Image Path to FTP (URL) *:</label> 
						<form:input path="ftpUrl" id="ftpUrl1"/>
						<span class="suggestion">Example: ftp.xyz.com </span> 
				</li>
				<li>
						 <label class="ftpLabel" >Folder :</label> 
						<form:input id="ftpPath" path="ftpPath" />
						 <span class="suggestion">Example: /myPictures/belk/ </span>
				</li>	 
				<li>
						 <label class="ftpLabel">Image Name *:</label> 
						<form:input id="ftpFileName" path="ftpFileName"  />
						 <span class="suggestion" > Example: shirt.jpg </span> 
				</li>	
				<li>
						<label class="ftpLabel">Username *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
			<li>				
				<div>
				<label style="width: 138px;">Image Type *:</label>
				<a class="imageTypeHelpIcon" href="javascript:showImageTypeHelp();"></a>
				<form:select id="imageType_2" path="imageType" items="${ImageTypeList}" multiple="false">  </form:select>
				</div>
			</li>
			<li>
					<div class="ShotTypeCd_2" style="display: none;">
						<label>Shot Type *:</label>
						<form:select id="shotType2" path="shotTypeValue"
							items="${shotTypeMapList}" multiple="false">
						</form:select>
					</div>
				</li>
		</ul>
		<div id="Add Image " class="addImageButtonDiv_2" align="right">
			<input type="button" style ="height: 23px; cursor:pointer;	white-space: nowrap;  padding-right:5px; padding-left:10px; padding-bottom:2px; padding-top :2px  font:normal 11px tahoma, verdana, helvetica; overflow:visible; margin-right: 5px;" value="Add Next Image" class="addButton" name="formSubmit"  id="addButton_2">
		</div>
		</form:form>
		<div id="result_2" class="status_msg"></div>
	</div>
</div>


<div class="admin container" id="container_3" style="display:none">
	<div class="div_spacer">
	    <!--  <iframe name="frame_3" id="frame_3" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
		<img id="progressImage_3" src="/cars/images/progressBar.gif" class="progressImage"/>
		<form:form commandName="uploadImagesDTO" method="post" id="uploadForm_3" enctype="multipart/form-data">
		<ul>
				
			<li>				
				<label>Image Location *:</label>
				<form:select path="imageLocationType" id="imageLocationType_3" items="${imageLocList}" multiple="false" >  </form:select>																		
			</li>		
			<li>
				<label>Select Image *:</label> 
				<input type="file" id="cdImageFile_3" accept="Image/jpeg,image/jpg,image/gif,image/png,image/psd" name="cdImageFile" />
			</li>
	
			<div id="ftpDiv_3" class="ftpDiv hidden"  >
				<li>	 
	    				<label class="ftpLabel">Image Path to FTP (URL) *:</label> 
						<form:input path="ftpUrl" id="ftpUrl1"/>
						<span class="suggestion">Example: ftp.xyz.com </span> 
				</li>
				<li>
						 <label class="ftpLabel" >Folder :</label> 
						<form:input id="ftpPath" path="ftpPath" />
						 <span class="suggestion">Example: /myPictures/belk/ </span>
				</li>	 
				<li>
						 <label class="ftpLabel">Image Name *:</label> 
						<form:input id="ftpFileName" path="ftpFileName"  />
						 <span class="suggestion" > Example: shirt.jpg </span> 
				</li>	
				<li>
						<label class="ftpLabel">Username *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
			<li>				
				<div>
				<label style="width: 138px;">Image Type *:</label>
				<a class="imageTypeHelpIcon" href="javascript:showImageTypeHelp();"></a>
				<form:select id="imageType_3" path="imageType" items="${ImageTypeList}" multiple="false">  </form:select>
				</div>
			</li>
			<li>
					<div class="ShotTypeCd_3" style="display: none">
						<label>Shot Type *:</label>
						<form:select id="shotType3" path="shotTypeValue"
							items="${shotTypeMapList}" multiple="false">
						</form:select>
						
					</div>
			</li>
		</ul>
		<div id="Add Image " class="addImageButtonDiv_3" align="right">
	  		<input type="button" style ="height: 23px; cursor:pointer;	white-space: nowrap;  padding-right:5px; padding-left:10px; padding-bottom:2px; padding-top :2px  font:normal 11px tahoma, verdana, helvetica; overflow:visible; margin-right: 5px;" value="Add Next Image" class="addButton" name="formSubmit"  id="addButton_3">
		</div>
	    </form:form>
	    <div id="result_3" class="status_msg"></div>
    </div>
</div>

<div class="space"> </div>

<div class="admin container" id="container_4" style="display:none">
	<div class="div_spacer">
	    <!--  <iframe name="frame_4" id="frame_4" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
		<img id="progressImage_4" src="/cars/images/progressBar.gif" class="progressImage"/>
		<form:form commandName="uploadImagesDTO" method="post" id="uploadForm_4" enctype="multipart/form-data">
		<ul>
			<li>				
				<label>Image Location *:</label>
				<form:select path="imageLocationType" id="imageLocationType_4" items="${imageLocList}" multiple="false" >  </form:select>																		
			</li>		
			<li>
				<label>Select Image *:</label> 
				<input type="file" id="cdImageFile_4" accept="Image/jpeg,image/jpg,image/gif,image/png,image/psd" name="cdImageFile" />
			</li>
	
			<div id="ftpDiv_4" class="ftpDiv hidden"  >
				<li>	 
	    				<label class="ftpLabel">Image Path to FTP (URL) *:</label> 
						<form:input path="ftpUrl" id="ftpUrl1"/>
						<span class="suggestion">Example: ftp.xyz.com </span> 
				</li>
				<li>
						 <label class="ftpLabel" >Folder :</label> 
						<form:input id="ftpPath" path="ftpPath" />
						 <span class="suggestion">Example: /myPictures/belk/ </span>
				</li>	 
				<li>
						 <label class="ftpLabel">Image Name *:</label> 
						<form:input id="ftpFileName" path="ftpFileName"  />
						 <span class="suggestion" > Example: shirt.jpg </span> 
				</li>	
				<li>
						<label class="ftpLabel">Username *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
			<li>				
				<div>
				<label style="width: 138px;">Image Type *:</label>
				<a class="imageTypeHelpIcon" href="javascript:showImageTypeHelp();"></a>
				<form:select id="imageType_4" path="imageType" items="${ImageTypeList}" multiple="false">  </form:select>
				</div>
			</li>
			<li>
					<div class="ShotTypeCd_4" style="display: none">
						<label>Shot Type *:</label>
						<form:select id="shotType4" path="shotTypeValue"
							items="${shotTypeMapList}" multiple="false">
						</form:select>
					</div>
			</li>
		</ul>
		<div id="Add Image " class="addImageButtonDiv_4" align="right">
	  		<input type="button" style ="height: 23px; cursor:pointer;	white-space: nowrap;  padding-right:5px; padding-left:10px; padding-bottom:2px; padding-top :2px  font:normal 11px tahoma, verdana, helvetica; overflow:visible; margin-right: 5px;" value="Add Next Image" class="addButton" name="formSubmit"  id="addButton_4">
		</div>
	    </form:form>
	    <div id="result_4" class="status_msg"></div>
	 </div>
</div>


<div class="admin container" id="container_5" style="display:none">
	<div class="div_spacer">
	    <!--  <iframe name="frame_5" id="frame_5" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
		<img id="progressImage_5" src="/cars/images/progressBar.gif" class="progressImage"/>
		<form:form commandName="uploadImagesDTO" method="post" id="uploadForm_5" enctype="multipart/form-data">
		<ul>
			<li>				
				<label>Image Location *:</label>
				<form:select path="imageLocationType" id="imageLocationType_5" items="${imageLocList}" multiple="false" >  </form:select>																		
			</li>		
			<li>
				<label>Select Image *:</label> 
				<input type="file" id="cdImageFile_5" accept="Image/jpeg,image/jpg,image/gif,image/png,image/psd" name="cdImageFile" />
			</li>
	
			<div id="ftpDiv_5" class="ftpDiv hidden"  >
				<li>	 
	    				<label class="ftpLabel">Image Path to FTP (URL) *:</label> 
						<form:input path="ftpUrl" id="ftpUrl1"/>
						<span class="suggestion">Example: ftp.xyz.com </span> 
				</li>
				<li>
						 <label class="ftpLabel" >Folder :</label> 
						<form:input id="ftpPath" path="ftpPath" />
						 <span class="suggestion">Example: /myPictures/belk/ </span>
				</li>	 
				<li>
						 <label class="ftpLabel">Image Name *:</label> 
						<form:input id="ftpFileName" path="ftpFileName"  />
						 <span class="suggestion" > Example: shirt.jpg </span> 
				</li>	
				<li>
						<label class="ftpLabel">Username *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
			<li>				
				<div>
				<label style="width: 138px;">Image Type *:</label>
				<a class="imageTypeHelpIcon" href="javascript:showImageTypeHelp();"></a>
				<form:select id="imageType_5" path="imageType" items="${ImageTypeList}" multiple="false">  </form:select>
				</div>
			</li>
			<li>
					<div class="ShotTypeCd_5" style="display: none">
						<label>Shot Type *:</label>
						<form:select id="shotType5" path="shotTypeValue"
							items="${shotTypeMapList}" multiple="false">
						</form:select>
					</div>
			</li>
		</ul>
		<div id="Add Image " class="addImageButtonDiv_5 hidden" align="right">
	  		<input type="button" style ="height: 23px; cursor:pointer;	white-space: nowrap;  padding-right:5px; padding-left:10px; padding-bottom:2px; padding-top :2px  font:normal 11px tahoma, verdana, helvetica; overflow:visible; margin-right: 5px;" value="Add Next Image" class="addButton" name="formSubmit"  id="addButton_5">
		</div>
	    </form:form>
	    <div id="result_5" class="status_msg"></div>
	     <div class="status_msg">
			<fmt:message key="caredit.vendorImage.lastImageMessage"/>
		</div>
	</div>
</div>