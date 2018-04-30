<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:prod="http://services.belkinc.com/product">
  	<soapenv:Header/>
   	<soapenv:Body>
   		<xsl:choose>
   			<xsl:when test="getItemRequest/requestType = 'Complex Pack'">
   				<prod:getPackRequest>
   				<xsl:choose>
   				<xsl:when test="getItemRequest/pack_data != ''">
   				<prod:vendorStyleList>
   					<xsl:for-each select="getItemRequest/pack_data">
   					<prod:vendorStyle>
		     		   <prod:vendorNumber><xsl:value-of select="vendor_number"></xsl:value-of></prod:vendorNumber>
		     		   <prod:vendorProductNumber><xsl:value-of select="vendor_product_number"></xsl:value-of></prod:vendorProductNumber>
		     		   <xsl:if test="color_code != ''">
		     		       <prod:colorCode><xsl:value-of select="color_code"></xsl:value-of></prod:colorCode>
		     		   </xsl:if>
		     		   <xsl:if test="size_code != ''">
		     		       <prod:sizeCode><xsl:value-of select="size_code"></xsl:value-of></prod:sizeCode>
		     		   </xsl:if>
		     		</prod:vendorStyle>
		     		</xsl:for-each>
		     	</prod:vendorStyleList>
		     	</xsl:when>
		     	<xsl:otherwise>
   					<xsl:for-each select="getItemRequest/inputData">
		     		   <prod:pack><xsl:value-of select="."></xsl:value-of></prod:pack>
		     		</xsl:for-each>
		     	</xsl:otherwise>
		     	</xsl:choose>
   				</prod:getPackRequest>
   			</xsl:when>
   			<xsl:when test="getItemRequest/requestType = 'Pack Color'">
                <prod:getPackRequest>
                <xsl:choose>
                <xsl:when test="getItemRequest/pack_data != ''">
                <prod:PackColorvendorStyle>
                    <xsl:for-each select="getItemRequest/pack_data">
                    <prod:vendorStyle>
                       <prod:vendorNumber><xsl:value-of select="vendor_number"></xsl:value-of></prod:vendorNumber>
                       <prod:vendorProductNumber><xsl:value-of select="vendor_product_number"></xsl:value-of></prod:vendorProductNumber>
                       <xsl:if test="color_code != ''">
                           <prod:colorCode><xsl:value-of select="color_code"></xsl:value-of></prod:colorCode>
                       </xsl:if>
                       <xsl:if test="size_code != ''">
                           <prod:sizeCode><xsl:value-of select="size_code"></xsl:value-of></prod:sizeCode>
                       </xsl:if>
                    </prod:vendorStyle>
                    </xsl:for-each>
                </prod:PackColorvendorStyle>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:for-each select="getItemRequest/inputData">
                       <prod:pack><xsl:value-of select="."></xsl:value-of></prod:pack>
                    </xsl:for-each>
                </xsl:otherwise>
                </xsl:choose>
                </prod:getPackRequest>
            </xsl:when>
   			<xsl:otherwise>
     	<prod:getItemRequest>
     		<xsl:if test="getItemRequest/requestType = 'Style'">
     			<prod:styleList>
     			<xsl:for-each select="getItemRequest/inputData">
     				<prod:style><xsl:value-of select="."></xsl:value-of></prod:style>
     			</xsl:for-each>
     			</prod:styleList>
     		</xsl:if>
     		<xsl:if test="getItemRequest/requestType = 'SKU'">
     		<prod:skuList>
     			<xsl:for-each select="getItemRequest/inputData">
		     				<prod:sku><xsl:value-of select="."></xsl:value-of></prod:sku>
     			</xsl:for-each>
     		</prod:skuList>
     		</xsl:if>     		        
		     		<xsl:if test="getItemRequest/requestType = 'StyleColor'">
                   <prod:StylecolorList>          
     			<xsl:for-each select="getItemRequest/inputData">
     				<prod:Stylecolor><xsl:value-of select="."></xsl:value-of></prod:Stylecolor>
     			</xsl:for-each>
                   </prod:StylecolorList>    
     		</xsl:if>
       	</prod:getItemRequest>
   			</xsl:otherwise>
   		</xsl:choose>
   	</soapenv:Body>
</soapenv:Envelope>
</xsl:template>
</xsl:stylesheet>