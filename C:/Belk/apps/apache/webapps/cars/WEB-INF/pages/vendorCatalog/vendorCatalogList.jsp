<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTabForVendorCatalog.jsp" %>

<head>
    <title><fmt:message key="catalogvendors.page.title"/></title>
    <c:if test="${pageContext.request.remoteUser != null}">
        <c:set var="headingUser">
            <authz:authentication operation="fullName"/>
        </c:set>
    </c:if>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>
<script>
	
	//dropship phase 2: added code fix for firefox browser onload
	var browserName=navigator.appName; // Get the Browser Name
	if(browserName=="Microsoft Internet Explorer") // For IE
	{
		window.onload=init; // Call init function in IE
	}
	else
	{
		if (document.addEventListener) // For Firefox
		{
		document.addEventListener("DOMContentLoaded", init, false); // Call init function in Firefox
		}
	}

    var scrollVal =0;
    function lockCatalog(catalogId,pagination) {
         //alert();
        document.forms[0].action ="catalogVendors.html?method=lockUnlockCatalog&mode=lock&vendorCatalogID=" + catalogId +"&d-6682707-p="+pagination+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
     function unLockCatalog(catalogId,pagination) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockCatalog&mode=unlock&vendorCatalogID=" + catalogId +"&d-6682707-p="+pagination+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
    function setValue123() {
        var val=document.getElementById("tempStatus").value;
       if(val=='All') {
         document.getElementById("status").options[0].selected=true;
       } else if(val=='Importing') {
         document.getElementById("status").options[3].selected=true;
       } else if(val=='Complete') {
        document.getElementById("status").options[1].selected=true;
       } else {
           document.getElementById("status").options[2].selected=true;
       }

        

    }
    function setChkValue() {
    var temp =document.getElementById("tempDepartmentOnly").checked;
    //alert(temp);
    if(temp) {
        document.getElementById("departmentOnly").value=true;
    } else {
        document.getElementById("departmentOnly").value=false;
    }

   
        
}
  
    function init() {
    	document.getElementById("tab1").className="activeTab";
        var pos = <c:out value="${scrollPos}" />;
          var element=document.getElementById('catalogName');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  && pos==0){
		 setTimeout(function() { element.focus(); }, 10);
		}
    }

</script>
<body class="admin" >
<!-- Added by Vikas for displaying bread crumb-->
	<div style="margin-left:5px;">
		<br/>
		<a href="../vendorCatalog/catalogVendors.html?method=viewAll">Vendors</a>
		> <c:out value="${vendorInfo.name}" default=""/>
		<br>
		<br>
		<b>Vendor Spreadsheets </b>
		<br/>
		<br/>
	</div>
    
    <div id="search_for_attribute" class="cars_panel x-hidden">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Vendor Info </span></div>

        <div class="x-panel-body">
            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">

                <li class = "car_id" style="width:24%;">
                    <b>Vendor #:</b>
                    <c:out value="${vendorInfo.vendorNumber}" />
                </li>
                <li class = "car_id" style="width:22%;">
                    <b># of Catalog Styles:</b>
                    <c:out value="${vendorInfo.numStyle}" />
                </li>
                <li class = "car_id" style="width:22%;">
                    <b>Date Last Import:</b>
                    <c:out value="${vendorInfo.dateLastImport}" />
                </li>
            </ul>

            <ul class="car_info" style="font-size:11px;padding:0 0 10px !important;">
                <li class ="car_id" style="width:24%;">
                    <b>Vendor Name:</b>
                    <c:out value="${vendorInfo.name}" />
                </li>
                <li class ="car_id" style="width:22%;">
                    <b># of Catalog SKUs:</b>
                    <c:out value="${vendorInfo.numSKUs}" />
                </li>
                <li class ="car_id" style="width:25%;">
                    <b>Last Spreadsheet Imported :</b>
                    <c:out value="${vendorInfo.catalogName}" />

                </li>

            </ul>

        </div>
    </div>
    <br>

    <div id="search_for_attribute1" class="cars_panel x-hidden">
        <div class="x-panel-header"><span class="x-panel-header-text">
                Spreadsheet Search </span></div>

        <div class="x-panel-body">
         	<spring:bind path="searchCatalogForm.*">
				<c:if test="${not empty errors}">
					<div class="error">
						<c:forEach var="error" items="${errors}">
							<img src="<c:url value="/images/iconWarning.gif"/>"
								alt="<fmt:message key="icon.warning"/>" class="icon" />
							<c:out value="${error}" escapeXml="false" />
							<br />
						</c:forEach>
					</div>
				</c:if>
        	</spring:bind>
            <c:url value="catalogVendors.html?method=searchVendorCatalogs" var="formAction"/>
            <form:form  name ="searchCatalogForm" commandName="searchCatalogForm" method="post"
                        action="${formAction}" onsubmit="return onFormSubmit(this)"
                        id="searchCatalogForm">
                 <spring:bind path="searchCatalogForm.vendorId">
                                <input type="hidden" id="vendorId" name="vendorId" value="<c:out value="${searchCatalogForm.vendorId}"  />" />
                        </spring:bind>

                <table style="border:0px">
                    <tr width="33%">
                        <td style="border:0px; font-weight:bold;"> <label for="txt_attr_name">Spreadsheet Name:</label>
                            </td><spring:bind path="searchCatalogForm.catalogName">
                        <td style="border:0px"> <input type="text" id="catalogName" name="catalogName" value="<c:out value="${searchCatalogForm.catalogName}" />"cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                        </td></spring:bind>

                        <td style="border:0px; font-weight:bold;"><label>Date Last Updated(Start):</label>
                            </td><spring:bind path="searchCatalogForm.dateLastUpdatedStart">
                        <td style="border:0px"><input type="text" id="dateLastUpdatedStart" name="dateLastUpdatedStart" size="12" value="<c:out value="${searchCatalogForm.dateLastUpdatedStart}" />" />
                           </td> </spring:bind>
                        
                        <td style="border:0px; "><input type="submit" class="btn" name="method" value="Search" />
                            <c:url value="/catalogVendors.html?method=search" var="formAction"/>&nbsp; &nbsp;
                            <a href='../vendorCatalog/catalogVendors.html?method=viewAllVendorCatalogs' id="a_view_all">View All</a>&nbsp;</td>
                    </tr>
                    <tr width="33%">
                        <td style="border:0px; font-weight:bold;"> <label for="txt_attr_name">Department #:</label>
                            </td>
                            <spring:bind path="searchCatalogForm.department">
                         <td style="border:0px"> <input type="text" id="department" name="department" value="<c:out value="${searchCatalogForm.department}" />"cssClass="text"
                                       cssErrorClass="text medium error" cssStyle="disabled:true;border:none"  />
                        </td></spring:bind>

                        <td style="border:0px; font-weight:bold;">
                            <label>Date Last Update(End):</label></td>
                            <spring:bind path="searchCatalogForm.dateLastUpdatedEnd">
                         <td style="border:0px">  <input type="text" name="dateLastUpdatedEnd" id="dateLastUpdatedEnd" size="12"  value="<c:out value="${searchCatalogForm.dateLastUpdatedEnd}" />" />
                            </td></spring:bind>
                        
                        <td style="border:0px">&nbsp; </td>

                    </tr>
                    <tr width="33%">
                        <td style="border:0px; font-weight:bold;">
                            <label>Status:</label>
                           </td>
                            <spring:bind path="searchCatalogForm.status">
                        <td style="border:0px"><select  id="status" class="required" name="status">
                                    <option value="All">All</option>
                                    <option id="Complete" value="Complete">Complete</option>
                                    <option  value="Data Mapping">Data Mapping</option>
                                    <option  value="Importing">Importing</option>
                                </select></td >
                            </spring:bind>
                        
                        <td style="border:0px; font-weight:bold;">
                            <label> User Departments only:</label></td>
                         <td style="border:0px;">   <input id="departmentOnly" type="hidden"  name="departmentOnly" value="<c:out value="${searchCatalogForm.departmentOnly}" />" />
                                    <c:if test="${searchCatalogForm.departmentOnly eq 'true'}" >
                                        <input id="tempDepartmentOnly" type="checkbox" checked name="tempDepartmentOnly"  style="width:16px;" onclick="setChkValue();" />
                                    </c:if>
                                       <c:if test="${searchCatalogForm.departmentOnly !='true'}" >
                                        <input id="tempDepartmentOnly" type="checkbox"  name="tempDepartmentOnly"  style="width:16px;" onclick="setChkValue();" />
                                    </c:if>
                                     
                        </td>
                      <td style="border:0px">&nbsp;</td>
                    </tr>
                </table>
                         <input type="hidden" id="tempStatus" name="tempStatus" value="<c:out value="${searchCatalogForm.tempStatus}" />" />
            </form:form>

        </div>
    </div>
    <br>

    <%! String stat = "Complete"; %>

    <div id="user_list" class="cars_panel x-hidden">

        <div class="x-panel-header"> Vendor Spreadsheet

        </div>
        <div  class="x-panel-body">
        	<div class="userButtons">
                <secureurl:secureAnchor name="newCatalog" cssStyle="btn"  title="New Spreadsheet" hideUnaccessibleLinks="true"/>

            </div>
            <display:table name="vendorCatalogList" cellspacing="0" sort="list" cellpadding="0" requestURI="/vendorCatalog/catalogVendors.html"
                           defaultsort="1" id="vendorCatalog" pagesize="25" class="dstable"  >
                <display:column comparator="com.belk.car.util.StringToLongComparator" sortable="true" defaultorder="ascending" titleKey="vendorCatalog.id" style="width:5%;white-space:normal" sortProperty="vendorCatalogID">
					<secureurl:secureAnchor name="vendorCatalogID" cssStyle="removeVendor" title="${vendorCatalog.vendorCatalogID}" 
			 		 hideUnaccessibleLinks="true" localized="true"  arguments="${vendorCatalog.vendorCatalogID},viewOnly"/>
			 	</display:column>

                <display:column property="vendorCatalogName" sortable="true" titleKey="vendorCatalog.name" style="width: 25%;white-space:normal"/>
                <display:column property="source" sortable="true" titleKey="vendorCatalog.source" style="width: 13%;white-space:normal"/>
                <display:column property="statusCD" sortable="true" titleKey="vendorCatalog.status" style="width: 10%;white-space:normal"/>

                <display:column  sortable="true" titleKey="vendorCatalog.complitiondate" style="width: 12%;white-space:normal">
                    <c:if test="${vendorCatalog.statusCD eq 'Complete'}" >
                        <c:out value="${vendorCatalog.updatedDate}" />
                    </c:if>
                </display:column>

                <display:column property="updatedDate"  sortable="true" titleKey="vendorCatalog.lastUpdatedDate" style="width: 12%;white-space:normal"/>
                <display:column property="updatedBy" sortable="true" titleKey="vendorCatalog.updatedby" style="width: 8%;white-space:normal"/>
                 <display:column  style="width: 15%">
                     
                      <c:choose>
                      <c:when test="${vendorCatalog.lockedBy !=null}">
                             <input class="edit_car btn" type="button" onclick="none" disabled="disabled" value="Edit" style="font-size: 12px; color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" title="Edit">
		             </c:when>
		             <c:otherwise>
		                   <secureurl:secureAnchor name="editVendorCatalog" cssStyle="btn" arguments="${vendorCatalog.vendorCatalogID},edit" title="Edit" hideUnaccessibleLinks="true"/>
		             </c:otherwise>
		             </c:choose>
		        </display:column>
		        
		         <display:column>
		          	<c:if test="${vendorCatalog.statusCD eq 'Importing'}" >
				    	<secureurl:secureAnchor name="RemoveCatalog" cssStyle="remove" elementName="RemoveCatalog" title="Remove"
				    	 localized="true" hideUnaccessibleLinks="true"
				    	  arguments="${param.vendorId},${param.numStyles},${param.numSKUs},removeCatalog,${vendorCatalog.vendorCatalogID}"/>
				    </c:if>
				    <c:if test="${vendorCatalog.statusCD ne 'Importing'}" >
				    	Remove
				    </c:if>
	    		</display:column>
                <display:setProperty name="paging.banner.item_name" value="catalog"/>
                <display:setProperty name="paging.banner.items_name" value="catalogs"/>

          </display:table>

           
        </div>
<script>
        setValue123();

</script>
</body>

<content tag="jscript">
    <![CDATA[
    <script type="text/javascript">
        $(document).ready(function(){
            // web 2.0 filter :)
            // datefields
            var CFG=belk.cars.config;
            new Ext.form.DateField({
                applyTo:'dateLastUpdatedStart',
                fieldLabel:'Date Last Import(Start):',
                allowBlank:true,
                format:CFG.dateFormat,
                altFormats:CFG.dateAltFormats
            });
            new Ext.form.DateField({
                applyTo:'dateLastUpdatedEnd',
                fieldLabel:'Start Ship Date',
                allowBlank:true,
                format:CFG.dateFormat,
                altFormats:CFG.dateAltFormats
            });
            $('select.autocomplete').each(function(){
                var $this=$(this);
                var name=$this.attr("name");
                var combo=new Ext.form.ComboBox({
                    typeAhead:true,
                    triggerAction:'all',
                    transform:this,
                    width:250,
                    forceSelection:false,
                    cls:$this.is('.recommended')?'recommended':'',
                    id:id,
                    disabled:$this.is(':disabled')
                });
                combo.on({
                    'blur':function(that){
                        $('input[name="'+name+'"]').val(that.getRawValue());
                    }
                });
            });

            var $dropdown = $('select.dropdown-show');
            if ($dropdown) {
                function showCurrent(){
                    $('ol.dropdown-show > li').hide();
                    $('ol.dropdown-show > li.' + $dropdown.val()).show();
                    $('ol.dropdown-show > li.*').show();
                }
                showCurrent();
                $dropdown.change(showCurrent);
            }
            $('body.admin table.dstable tr').hover(function(){
              $('td',this).addClass('trHover');
              $(this).click(function(){
              $('td',this).removeClass('trHover');
              $('td',$(this).parent()).removeClass('trSelected');
              $('td',this).addClass('trSelected');
              });
            },function(){
              $('td',this).removeClass('trHover');
              });
        window.scrollTo(0,<c:out value="${scrollPos}" />);
        
        $('a.remove').click(function(){return confirm('Are you sure you want to remove this template?');});
        });
    </script>
    ]]>
</content>
