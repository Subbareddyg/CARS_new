<%@ include file="/common/taglibs.jsp"%>
<c:if test="${sessionScope.isPreviewPage == 'true'}">
<c:set var="imageStatusTitle">
	Image Status 
</c:set>
</c:if>
<c:if test="${sessionScope.isPreviewPage != 'true'}">
<c:set var="imageStatusTitle">
	Image Status <app:helpLink section="ImageStatus" key="/editCarForm.html" title="&nbsp;" localized="false"/>
</c:set>
</c:if>
<display:table name="${carImagesFromSample}" export="false" cellpadding="0" cellspacing="0" id="carImage">
  <display:column style="width:5%;" property="image.imageId" title="Image#" sortable="false" />
  <display:column style="width:10%;" property="image.imageType.name" title="Image Type" sortable="false" />
  <display:column style="width:20%;" property="image.imageLocationType.name" title="Image Location" sortable="false" />
  <display:column style="width:20%;" property="image.imageLocation" sortable="false" title="Image Name"/>
  <display:column style="width:20%;" property="image.sample.sampleId" sortable="false" title="Sample ID"/>
  <display:column style="width:15%;" property="image.imageTrackingStatus.name" sortable="false" title="${imageStatusTitle}"/>
  <c:if test="${sessionScope.isPreviewPage != 'true'}">
  <display:caption>Images From Sample</display:caption>
  <display:footer></display:footer>
  </c:if>
</display:table>