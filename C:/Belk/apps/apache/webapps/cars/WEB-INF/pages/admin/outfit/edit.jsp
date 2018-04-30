<!DOCTYPE html>
<%-- 
  - Author(s): Karthik  & Anto
  - Date: 13/03/2013
  - Copyright Notice: 2013 Belk Inc.
  - @(#)
  - Description: this page specifies Outfit and Collection Car 
  --%>

<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" import="java.util.ArrayList"%>
<head>
	<title><fmt:message key="outfit.detail" />
	</title>
</head>
<%
	// if car id in request is null,then set the null as as string
	// for url matching (RemoveOutfit in url-config.xml) 
	// this is required while creating new outfit 

	String outfitCarId=request.getParameter("CarId");
	if(outfitCarId==null){
		outfitCarId="null";
	}
%>
<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="outfit.detail"/>
	</div>
	<div class="x-panel-body">
		<c:url value="/admin/outfit/edit.html" var="formAction" />
		<form:form commandName="outfitForm" method="post" action="${formAction}" id="outfitForm">
			<div class="userButtons">
				<secureurl:secureAnchor cssStyle="btn" name="OutfitList" title="Back" localized="true" hideUnaccessibleLinks="true" />
			</div>
			<fieldset>
				<div style="float:left;width:650px;">
				<ul>
					<li>
						<label for="txt_outfitName">
							* <fmt:message key='outfit.name'/>:
						</label>
						<form:textarea path="outfitName" id="txt_outfitName" cssClass="text required" cssErrorClass="text medium error" cols="30" rows="5" onkeyup="textCounter();" onblur="textCounter();"/>
						<div class="text_counter" style="display: inline;">  
							Max Chars: 150   &nbsp;&nbsp;&nbsp;&nbsp; 
							Chars Count: <span id="outfit_name_length">0</span>
						</div>
						<input type="button" id="generate_btn" name="generate" value="Auto-Generate" class="btn" style="margin-left: 210px; padding: 1px 1px; display: inline;" />
					</li>
					<li>
						<br style="clear:both;" />
						<label for="txt_outfitDescr">
							* <fmt:message key='outfit.descr'/>:
						</label>
						<form:textarea path="description" id="txt_outfitDescr" cssClass="text required" cssErrorClass="text medium error" cols="30" rows="3"/>
						<div class="text_counter" style="width:100px;">  
							Max Chars: 2000    
						</div>						
					</li>
				</ul>
				<br />
				</div>
				<div style="float:left;width:250px">
					<div class="outfit_category">
						<label for="categoryFilter">
							<fmt:message key='outfit.category'/>:
						</label>
						<c:choose>
				    		<c:when test="${empty param.CarId}">	
				    			<select id="categoryFilter" name="categoryFilter" >
	                                <c:forEach var="tempType" items="${templateType}">
	                                	<option id="${tempType}">${tempType}</option>
	                            	</c:forEach>
	                            </select>
				  				<input type="hidden" name="templateTypeVal" id="templateTypeVal" value=""/>
							</c:when>
				    		<c:otherwise>
				    			<c:out value="${templateType}" />
				    			<input type="hidden" name="templateTypeVal" id="templateTypeVal" value="${templateType}"/>
				    		</c:otherwise>
		    			</c:choose>
					</div>
					<div class="isSearchable">
						<c:choose>
							<c:when test="${empty param.CarId}">	
								<input type="checkbox" name="searchable" checked="checked" value="Y"/>
							</c:when>
						    <c:otherwise>
						    	<input type="checkbox" name="searchable" value="Y" <c:if test="${isSearchable == 'Y'}">checked="checked"</c:if>/>
						    </c:otherwise>
				    	</c:choose>
						<label for="searchable">
							<fmt:message key='outfit.searchable'/>:   
						</label>
					</div>
					<div class="effDate">
						<label for="effectiveDate"><fmt:message key='outfit.effectivedate'/>:</label>
						<input type="text" class="date" name="effectiveDate" id="effectiveDate" value="${effectiveDate}"/></div>
					</div>
			<br/>
			</fieldset>
			
			<div id="child_cars_wrap" style="clear:left">
				<jsp:include page="carAndSkuDetails.jsp"></jsp:include>
			</div>
						
			<c:choose>
	    		<c:when test="${empty param.CarId}">
	    			<input type="submit" align="right" class="btn" value="<fmt:message key="outfit.create"/>" style="float:right"/>
	    		</c:when>
	    		<c:otherwise>
	    			<input type="submit" align="right" class="btn" value="<fmt:message key="button.save"/>" style="float:right"/>
	    			<input type="hidden" name="CarId" id="CarId" value="${param.CarId}"/>
	    		</c:otherwise>
	    	</c:choose>
		</form:form>
	</div>
</div>
</body>

<content tag="inlineStyle">
form li label{ float:left; font-weight:bold; width:200px;padding-right:10px; padding-top:3px; text-align:right; }
form li{clear:both; padding:5px 0; }
textarea{ width:400px; height:75px; }
input.text{ width:300px; }
</content>
<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value='/js/libs/jquery-1.3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value="/js/belk.cars.outfit.js"/>"></script>
<script type="text/javascript" src="<c:url value='/js/libs/jquery-plugins.js'/>"></script>
 <script>
$(document).ready(function(){
	$("#child_car_sku_list").find('.x-tool').click(function(){
	});
});
</script>
]]>

</content>

