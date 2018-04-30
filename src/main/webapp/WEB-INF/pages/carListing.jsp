<%@ include file="/common/taglibs.jsp"%>
 <div class="buttons" style="float:right; -moz-margin-end: -33px;">
<c:if test="${userTypeCd == 'BUYER'}" >
<input type="button"  value="Print" id="print" class="printbtn print" /> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
</c:if>
<c:if test="${userTypeCd == 'BUYER'}" >
	<c:choose>
		<c:when test="${enableBoth}">
			<input type="button" value="Archive" id="archive" class="btn cancel_btn btnClass"  />
			<input type="button" value="Unarchive" id="unarchive" class="btn cancel_btn btnClass" />
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${enableArchive == 'Y'}">
					<input type="button" value="Archive" id="archive" class="btn cancel_btn btnClass" />
					<input type="button" value="Unarchive" id="unarchive" class="btn cancel_btn btnClass" disabled="disabled"/>
				</c:when>
				<c:otherwise>
					<input type="button" value="Archive" id="archive" class="btn cancel_btn btnClass" disabled="disabled"/>
					<input type="button" value="Unarchive" id="unarchive" class="btn cancel_btn btnClass" />
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	
	<c:choose>
		<c:when test="${enableDelete == 'Y'}">
			<input type="button" value="Delete" id="delete" class="btn cancel_btn btnClass" />
		</c:when>
		<c:otherwise>
			<input type="button" value="Delete" id="delete" class="btn cancel_btn btnClass" disabled="disabled"/>
		</c:otherwise>
	</c:choose>
</c:if>

</div>

<div style="width: 100%;hidden;">
		<display:table name="abc" defaultorder="${dashboardSortedOrder}" defaultsort="${dashboardSortedOn}"
	cellspacing="0" cellpadding="0"  requestURI="/getCars.html" style="width:900px;"
	id="history" class="ftable dstable" sort="list" pagesize="250" size="totalResultSize" partialList="true" export="false" >
	 
	<display:setProperty name="paging.banner.items_name" value="CARS" />
	<display:setProperty name="paging.banner.item_name" value="CAR" />
	 <display:setProperty name="basic.show.header" value="true"/>
	<!-- <display:setProperty name="basic.msg.empty_list_row" value=""/> -->
	<display:setProperty name="paging.banner.some_items_found" value="{0} {1} found, displaying 250 CARS "/>
	<!-- Check Box header -->
	<c:choose>
		<c:when test="${userTypeCd == 'BUYER' }">
			<display:column sortable="false" class="chkbox" titleKey='<input type="checkbox" id="checkall" name="checkall" onclick="checkedAll(this.checked);" />' headerClass="chk" />
		</c:when>
		<c:otherwise>
			<display:column sortable="false" class="chkbox" titleKey='<input type="checkbox" id="checkall" name="checkall" disabled="disabled"/>' headerClass="chk" style="border-left:1px solid #D0D0D0;"/>
		</c:otherwise>
	</c:choose>
	<!-- Flag -->
 	<display:column class="patternImg" headerClass=" patternImg" title="F" style="text-align:right;"/> 
<%-- 	 <display:column class="patternImg" headerClass=" patternImg" title="F" />   --%>
	<!-- ID -->
	<display:column  comparator="com.belk.car.util.LongComparator" class="carId" headerClass=" carId" sortProperty="carId" sortable="true" title="ID" style="text-align:left;" />
	<display:column sortable="true" sortProperty="source" title="Source"  class="sourcetype" headerClass=" sourcetype" style="text-align:left;" />
	<display:column sortable="true" title="Dept #" style="text-align:left;border-right-width:1px;border-right-style: solid;"  class ="deptno" headerClass=" deptno" />
	<display:column sortable="true" title="Vendor" style="text-align:left;"  class="vendor" headerClass=" vendor"/>
	<display:column sortable="true" title="Vendor Style" style="text-align:left;"  class="vendorstyle" headerClass=" vendorstyle"/>
	<display:column sortable="true" title="Request Type" style="text-align:left;"  class="requesttype" headerClass="requesttype"/>
	<display:column sortable="true" title="Status" style="text-align:left;"  class="statusboard" headerClass=" status"/>
	<display:column sortable="true" title="Assigned To" style="text-align:left;" class="assignedto" headerClass=" assignedto"/>
	<display:column sortable="true" title="Status Due" style="text-align:left;"  class="duedate" headerClass=" duedate "/>
	<display:column sortable="true" title="Completion Date" style="text-align:left;"  class="completiondate" headerClass=" completiondate"/>
	<display:column class="editbtn" headerClass=" editbtn"/> 
	<display:column class="plusbtn" headerClass=" plusbtn"/>
	
	</display:table>
	</div>

<div id="23" class="tableContainer" style="height:430px;*height:423px;position:relative;overflow-y:auto;overflow-x:hidden;">

<c:set var="counter" value="-1"></c:set>

<display:table name="DashBoard" cellspacing="0" cellpadding="0"  requestURI="/getCars.html" defaultsort="${dashboardSortedOn}" defaultorder="${dashboardSortedOrder}"
	id="history" class="ftable dstable" sort="list" pagesize="250"  style="width:900px;table-layout:fixed;font-size:4px;margin-top:0px;border-width:0px;" >
	
	<display:setProperty name="paging.banner.group_size" value=""/>
	<display:setProperty name="paging.banner.no_items_found" value=""/>
	<display:setProperty name="paging.banner.one_item_found" value=""/>
	<display:setProperty name="paging.banner.all_items_found" value=""/>
	<display:setProperty name="paging.banner.some_items_found" value=""/>
	<display:setProperty name="paging.banner.onepage" value=""/>
	<display:setProperty name="paging.banner.full" value=""/>
	<display:setProperty name="paging.banner.first" value=""/>
	<display:setProperty name="paging.banner.last" value=""/>
	<display:setProperty name="basic.msg.empty_list_row" value=""/>
	<display:setProperty name="basic.show.header" value="false"/>
	<c:set var="counter" value="${counter+1}"></c:set>
	<c:set var="arch" value="${DashBoard[counter].archived}"></c:set>
	<c:set var="YES" value="Y"></c:set>
	<c:set var="bgColor" value="${DashBoard[counter].buyerApprovalFlag}"></c:set>
		<c:choose>
							<c:when test="${not empty arch}">
									<c:set var="changeCSS" value="color:#3366EF;"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="changeCSS" value=""></c:set>
								</c:otherwise>
	</c:choose>
	<c:choose>
							<c:when test="${not empty bgColor}">
									<c:set var="backGroundCSS" value="background:#01DF3A;"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="backGroundCSS" value=""></c:set>
								</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${userTypeCd == 'BUYER'}">
			<display:column sortable="false" titleKey='<input type="checkbox" id="checkall" name="checkall" onclick="checkedAll(this.checked);" />' headerClass="fixedHeader chk" class="chk" style="${changeCSS};width: 19px;">
			    <input type="checkbox" name="chk" class="chkbox" value="${DashBoard[counter].carId}"/>
				
			</display:column>
		</c:when>
	<c:otherwise>
	<display:column sortable="false" titleKey='<input type="checkbox" id="checkall" name="checkall" disabled="disabled"/>' headerClass="fixedHeader chk" class="chk" style="${changeCSS};width: 19px;">
		    <input type="checkbox" name="chk" class="chkbox" disabled="disabled" />
	</display:column>
	</c:otherwise>
	</c:choose>
	 
	<c:choose>
							<c:when test="${DashBoard[counter].contentStatus=='PUBLISHED' ||DashBoard[counter].contentStatus =='RESEND_DATA_TO_CMP'
									 ||DashBoard[counter].contentStatus =='RESEND_TO_CMP' ||DashBoard[counter].readyToSendToCMPFlag =='false'}">
									<c:set var="plusCSS" value="font-size:12px;font-weight:700px;color:gray;background:#CCCCCC;"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="plusCSS" value="font-size:12px;font-weight:700px;"></c:set>
								</c:otherwise>
	</c:choose>
  	 
	<display:column class="patternImg" headerClass="fixedHeader patternImg" title="F" style="${changeCSS};width: 10px;${backGroundCSS};">
		<c:choose>
			<c:when test="${DashBoard[counter].styleTypeCd == 'OUTFIT'}">
				<img src="<c:url value='/images/outfit.gif' />" />
			</c:when>
			<c:when test="${DashBoard[counter].styleTypeCd == 'PYG'}">
				<img src="<c:url value='/images/icoGiftBox.gif' />" />
			</c:when>
			<c:otherwise>
				<c:if test="${DashBoard[counter].styleTypeCd != 'PRODUCT'}">
					<img src="<c:url value='/images/pattern.gif' />" />
				</c:if>
			</c:otherwise>
		</c:choose>
		
	</display:column>
	<display:column  comparator="com.belk.car.util.LongComparator" class="carId" headerClass="fixedHeader carId" sortProperty="carId" sortable="true" title="ID"
		style="text-align:left;${changeCSS};width: 31px;" property="carId" />

	<display:column sortable="true" sortProperty="source" title="Source" class="sourcetype" headerClass="fixedHeader sourcetype" style="text-align:left;${changeCSS};width: 60px;">
		<a href="#" title="Source Type:${DashBoard[counter].sourceName}
Shipping Date:${DashBoard[counter].completionDate}" id="${DashBoard[counter].carId}" class="carsrc" >${DashBoard[counter].source}</a>
	</display:column>
	<display:column property="deptNo" sortable="true" title="Dept #" style="text-align:left;${changeCSS};width: 38px;"  class ="deptno" headerClass="fixedHeader deptno"/>
	<display:column property="vendor" sortable="true" title="Vendor" style="text-align:left;${changeCSS};width: 76px;" class="vendor" headerClass="fixedHeader vendor"/>
	<display:column property="style" sortable="true" title="Vendor Style" style="text-align:left;${changeCSS};width: 107px;" class="vendorstyle" headerClass="fixedHeader vendorstyle"/>
	<display:column property="requestType" sortable="true" title="Request Type" style="text-align:left;${changeCSS};width: 58px;" class="requesttype" headerClass="fixedHeader requesttype"/>
	<display:column property="status" sortable="true" title="Status" style="text-align:left;padding:2px;${changeCSS};width: 80px;" class="statusboard" headerClass="fixedHeader status"/>
	<display:column property="assignedTo" sortable="true" title="Assigned To" style="text-align:left;${changeCSS};width: 70px;" class="assignedto" headerClass="fixedHeader assignedto"/>
	<c:choose>	
		<c:when test="${DashBoard[counter].dueFlag == 'T'}">
			<display:column property="dueDatte" sortable="true" format="{0,date,MM/dd/yyyy}" title="Status Due" style="text-align:left;${changeCSS};width: 55px;background:red;" class="duedate" headerClass="fixedHeader duedate "/>	
		</c:when> 
		<c:when test="${DashBoard[counter].statusDueFlag == 'T'}">
			<display:column property="dueDatte" sortable="true" format="{0,date,MM/dd/yyyy}" title="Status Due" style="text-align:left;${changeCSS};width: 55px;background:yellow;" class="duedate" headerClass="fixedHeader duedate "/>	
		</c:when>
		<c:when test="${DashBoard[counter].strImmediateFlag == 'T'}">
			<display:column value="Immediate" sortable="true" title="Status Due" style="text-align:left;${changeCSS};width: 55px;background:#2E64FE;" class="duedate" headerClass="fixedHeader duedate "/>	
		</c:when>
		<c:otherwise>
			<display:column property="dueDatte" sortable="true" format="{0,date,MM/dd/yyyy}" title="Status Due" style="text-align:left;${changeCSS};width: 55px;" class="duedate" headerClass="fixedHeader duedate "/>
		</c:otherwise>
	</c:choose>
	
	<display:column property="completionDatte" format="{0,date,MM/dd/yyyy}" sortable="true" title="Completion Date" style="text-align:left;${changeCSS};width: 75px;" class="completiondate" headerClass="fixedHeader completiondate"/>
	<display:column class="editbtn" headerClass="fixedHeader editbtn" title="E" style="${changeCSS};width: 30px;">
		<c:if test="${DashBoard[counter].setEdit == 'edit'}">
			<input type="button" class="edit_car btn" title="Edit CAR" id="${DashBoard[counter].carId}"  style="font-size:11px;font-weight: 700px;" value="Edit"/>
			</c:if>
		<c:if test="${DashBoard[counter].setEdit == 'set'}">
			<a class="set_prod_type btn" href="#" title="Set Product Type" id="${DashBoard[counter].carId}"  style="font-size:11px;font-weight: 700px;">Set</a>
		</c:if> 
		<c:if test="${DashBoard[counter].setEdit == 'gray'}">
			<input type="button" title="Edit CAR" class="btn" id="${DashBoard[counter].carId}" style="font-size:11px;font-weight:700px;color:gray;background:#CCCCCC;" value="Edit" disabled="disabled" onClick="none;"/>
		</c:if> 
	</display:column>
	<display:column class="plusbtn" headerClass="fixedHeader plusbtn" title="p" style="${changeCSS};width: 20px;">
					<c:if test="${DashBoard[counter].setEdit == 'edit'}">
						<a class="menu btn" href="#" id="${DashBoard[counter].userTypeCd}" name="${DashBoard[counter].contentStatus}"  style="${plusCSS}" >+</a>
					</c:if>
	</display:column>
	
</display:table>
</div>


	
	