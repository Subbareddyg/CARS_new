<%@ include file="/common/taglibs.jsp"%>

<div class="admin container" id="container_1" >
	<div class="div_spacer">
		<!--  <iframe name="frame_1" id="frame_1" style="width:0;height:0;border:none;visibility:hidden"></iframe> -->
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
						<label class="ftpLabel">User name *:</label> 
						<form:input id="ftpUserId" path="ftpUserId" />
						<span class="suggestion">Example: FTP Username</span>
				</li>
				<li>
						 <label class="ftpLabel">Password *:</label>
						<form:password path="ftpPassword" id="ftpPassword1"/>
						 <span class="suggestion">Example: FTP Password</span>
				</li>	
			</div>			
		</ul>
		</form:form>
		<div id="result_1" class="status_msg"></div>
	</div>
</div>