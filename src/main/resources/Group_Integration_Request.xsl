<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:prod="http://services.belkinc.com/product">
  	<soapenv:Header/>
   	<soapenv:Body>
   		<prod:getGroupRequest>
     		<xsl:if test="getGroupRequest/requestType = 'GroupID'">
     			<prod:groupList>
     			<xsl:for-each select="getGroupRequest/inputData">
     				<prod:group><xsl:value-of select="."></xsl:value-of></prod:group>
     			</xsl:for-each>
     			</prod:groupList>
     		</xsl:if>
     		<xsl:if test="getGroupRequest/requestType = 'VendorStyle'">
     			<prod:vendorStyleList>
   					<xsl:for-each select="getGroupRequest/pack_data">
   					<prod:vendorStyle>
		     		   <prod:vendorNumber><xsl:value-of select="vendor_number"></xsl:value-of></prod:vendorNumber>
		     		   <prod:vendorProductNumber><xsl:value-of select="vendor_product_number"></xsl:value-of></prod:vendorProductNumber>
		     		</prod:vendorStyle>
		     		</xsl:for-each>
		     	</prod:vendorStyleList>
     		</xsl:if>
       	</prod:getGroupRequest>
   	</soapenv:Body>
</soapenv:Envelope>
</xsl:template>
</xsl:stylesheet>