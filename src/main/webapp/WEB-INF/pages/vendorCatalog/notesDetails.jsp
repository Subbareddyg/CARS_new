<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="vendorProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='vendorProfile.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">

<div id="user_form" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Edit Vendor Catalog Note
	</div>
	<div class="x-panel-body">



<c:url value="/admin/vendorcatalog/addCatalogNote.html" var="formAction"/>	
			<form method="post" action="${formAction}" id="vendorCatalogNote">
				<input type="hidden" name="noteID" value="2149"/>
				<input type="hidden" name="method" value="addUserNote"/>
				
				<p class="required"><span class="req">*</span> Indicates required field</p>				
			<ol>
				<li>	 
				<strong>Subject:<span class="req">* </span></strong>
				<input type="text" name="noteSubject" size="5" value="${id}"/>
				</li>
				<li>
				<strong>Note: </strong>
				<textarea class="note_text" name="note" rows="5" cols="25"></textarea><br/><br/>
				</li>
				<li class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
				<br style="clear:both;" />
		</li>
			</ol>		
			</form>

	</div></div>

</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
<![CDATA[
]]>
</content>










