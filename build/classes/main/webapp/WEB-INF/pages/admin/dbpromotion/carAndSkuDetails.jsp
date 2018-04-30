<!DOCTYPE html>
<%-- 
  - Description: this page displays Child car Panels' and Sku's header Information . Internally includes childCars.jsp to view actual panels.
  --%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java" import="java.util.ArrayList"%>
<%

// if car id in request is null,then set the null as as string
// for url matching (RemoveDBPromotion in url-config.xml) 
// this is required while creating new DBPromotion 

String dbPromotionCarId=request.getParameter("CarId");
if(dbPromotionCarId==null){
	dbPromotionCarId="null";
}
String preVal;

String attrChildSkuValue=(String)request.getAttribute("attrChildSkuValue");

	
%>
			<div id="child_car_list" class="collection_panel x-panel">
			
					<div class="x-panel-header">
						Promotion Child Cars
					</div>
					
					<div class="x-panel-body">
						<div class="buttons">
			    			<secureurl:secureAnchor cssStyle="btn btn_add_child_car" name="AddDBPChildCars" title="Add Child Car" localized="true"
									hideUnaccessibleLinks="true" arguments="<%=dbPromotionCarId %>" onclick="selectedBelkUpc();" />
							<br/>
							<%
							System.out.println("Inside Car and SKU Details");
							%>
						</div>
						<br/>
						<div>
							<jsp:include page="childCars.jsp">
								<jsp:param name="paramVal" value="one" />
							</jsp:include>
						</div>
						<br/>
					</div>
			</div>
			<br></br>

<c:set var="car_counter" value="-1"></c:set>

<%
int carCounter = -1;
%>
<c:set var="nameCnt" value="-1" scope="session" />

<c:forEach var="entry" items="${viewChildCarsSkuList}">

<c:choose>
	<c:when test="${entry.parentCarStyleId ne prevCarId}">
		<div id="child_car_Sku_list_${entry.parentCarStyleId}" class="cars_sku_panel x-panel child_${car_counter+1}">
				<div class="x-panel-header">
					<input type="hidden" name="skuChildCarId" id="skuChildCarId" value="${entry.skuCarid}"/>
				</div>
			
			
				<div class="x-panel-body">
					<div class="buttons">
						<a href="javascript:void(0)" class="btn" title="Select All" id="chk_select_all_${car_counter}" onclick="checkAllCheckbox(${car_counter})">Select All</a> <a href="javascript:void(0)" class="btn" title="Un Select All" id="chk_Unselect_all" onclick="unCheckAllCheckbox(${car_counter})">UnSelect All</a>
					<br/>
					</div>
					<br/>
					<c:choose>
						<c:when test="${entry.parentCarStyleId ne prevCarId}">
						<c:set var="car_counter" value="${car_counter+1}"></c:set>
						
						<%
						carCounter++;
						
						%>
							<div>
								<jsp:include page="childCars.jsp">
									<jsp:param name="paramVal" value="two" />
									<jsp:param name="paramNewVal" value="${entry.skuCarid}" />
									<jsp:param name="childCarSkuListArr" value="viewChildCarsSkuList_${car_counter}" />
									<jsp:param name="chkSkuListCheckBox" value="chkSkuList_${car_counter}" />
									<jsp:param name="parentCarStyleId" value="${entry.parentCarStyleId}" />
									<jsp:param name="carCounter" value="${car_counter}" />
								</jsp:include>
							</div>
						</c:when>
					</c:choose>
					<br/>
				</div>
			</div>
	</c:when>
</c:choose>
<input type="hidden" name=dbPromotionCarId id="dbPromotionCarId" value="<%=dbPromotionCarId %>"/>
<c:set var="prevCarId" value="${entry.parentCarStyleId}" />
</c:forEach>
<input type="hidden" name="skuAllCount" id="skuAllCount" value="${nameCnt}"/>
<script type="text/javascript">
$('div.collection_panel').each(function(){
	new Ext.Panel({
        collapsible:true,
		autoShow:true,
		frame:true,
        applyTo:this,
		height:'auto',
		collapsed:$(this).is('.collapsed')
	});
			
});

$('div.cars_sku_panel').each(function(){
	new Ext.Panel({
        collapsible:true,
		autoShow:true,
		frame:true,
        applyTo:this,
		height:'auto',
		collapsed:$(this).is('.collapsed'),
		listeners: {
		       'beforeexpand': function(){				
		 		},
		       'beforecollapse': function(){
		          
		        }
		}
	});
});

function checkAllCheckbox(car_counter_val)
{
	car_counter_val++;
   	$('.chkSkuList_'+car_counter_val+':not(:checked)').attr('checked','checked');
   	selectedBelkUpc();
}

function unCheckAllCheckbox(car_counter_val)
{
	car_counter_val++;
	$('.chkSkuList_'+car_counter_val+':checked').removeAttr('checked');
	selectedBelkUpc();
}
</script>