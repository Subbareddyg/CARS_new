<HTML>
<HEAD>
<TITLE>Upload from CD</TITLE>
</HEAD>
<BODY>
<applet code="ZUpload" Archive="ZUpload.jar" width="450" height="300" border="0" style="background: navy;">
<param name="host" value="${ftpDetail.hostName}">
<param name="user" value="${ftpDetail.userName}">
<param name="pass" value="${ftpDetail.userPassword}">
<param name="path" value="${sessionScope.imageDirectory}">
<param name="postscript" value="url_of_script">
</applet> 
</BODY>
</HTML>