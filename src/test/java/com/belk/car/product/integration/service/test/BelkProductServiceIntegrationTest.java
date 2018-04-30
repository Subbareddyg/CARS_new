/**
 * 
 */
package com.belk.car.product.integration.service.test;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import com.belk.car.product.integration.exception.BelkProductIntegrationException;
import com.belk.car.product.integration.request.data.GetRequestType;
import com.belk.car.product.integration.request.data.IntegrationRequestData;
import com.belk.car.product.integration.request.data.PackItemRequestData;
import com.belk.car.product.integration.response.data.AttributeData;
import com.belk.car.product.integration.response.data.DifferentiatorsCodeData;
import com.belk.car.product.integration.response.data.IntegrationResponseData;
import com.belk.car.product.integration.response.data.ItemCatalogData;
import com.belk.car.product.integration.response.data.ItemComplexPackSpecData;
import com.belk.car.product.integration.response.data.ItemDifferentiatorsData;
import com.belk.car.product.integration.response.data.ItemPackComponentData;
import com.belk.car.product.integration.service.BelkProductService;

/**
 * @author afupxs5
 *
 */
public class BelkProductServiceIntegrationTest {
	
	private IntegrationRequestData requestData;
	
	private BelkProductService belkProductService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		requestData = new IntegrationRequestData();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.belk.car.product.integration.service.BelkProductService#getResponse()}.
	 */
	@Test
	public void testGetResponse_ForStyleRequest() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("100143888");
		requestData.setRequestType(GetRequestType.STYLE.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			ItemCatalogData data = catalogData.get(0);
			Assert.assertEquals(GetRequestType.STYLE.toString(), data.getResponseType());
			Assert.assertNotNull(data.getPimEntry().getEntries().getItemStyleSpecData());
			Assert.assertEquals(Long.valueOf("100143888") , data.getPimEntry().getEntries()
					.getItemCatalogSpecData().getPrimaryKey());
			Assert.assertEquals("1060006470", data.getPimEntry().getEntries().getItemCatalogSpecData().getDescriptionData().getDescriptionCode());
			Assert.assertEquals("7 WHT JUST DO ITTEE", 
					data.getPimEntry().getEntries().getItemCatalogSpecData().getDescriptionData().getLongDescription());
			Assert.assertEquals(Long.valueOf("100143888"), data.getPimEntry()
					.getItemHeaderInformation().getPrimaryKey());
			Assert.assertEquals(Boolean.TRUE, 
					data.getPimEntry().getEntries().getItemCatalogSpecData().getIsInventoryFlag());
			Assert.assertEquals(2, data.getPimEntry().getEntries().getItemStyleSpecData().getDifferentiators().size());
			ItemDifferentiatorsData styleSpecData = data.getPimEntry().getEntries().getItemStyleSpecData().getDifferentiators().get(0);
			Assert.assertEquals(4, styleSpecData.getCodes().size());
			Assert.assertEquals("SIZE", styleSpecData.getDifferentiatorsType());
			DifferentiatorsCodeData codes = styleSpecData.getCodes().get(0);
			Assert.assertEquals("1060006475", codes.getCodeIndicator());
			ItemDifferentiatorsData colorSpecData = data.getPimEntry().getEntries().getItemStyleSpecData().getDifferentiators().get(1);
			Assert.assertEquals(2, colorSpecData.getCodes().size());
			Assert.assertEquals("COLOR", colorSpecData.getDifferentiatorsType());
			DifferentiatorsCodeData colorCodes = colorSpecData.getCodes().get(0);
			Assert.assertEquals("100143888.100", colorCodes.getStyleDifferentiatorId());
			Assert.assertEquals("0400630161827", colorCodes.getIdbId());
			List<AttributeData> allAttributes = data.getPimEntry().getAllAttributes().get(Long.valueOf("100143888"));
			for (AttributeData attribute : allAttributes) {
				if (attribute.getAttributeName().equals("Vendor_Style")) {
					Assert.assertEquals("424717", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Sleeve_Length")) {
					Assert.assertEquals("SUBCLASS_S SLV", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Selling_Season")) {
					Assert.assertEquals("05-FALL", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Channel_Exclusive")) {
					Assert.assertEquals("", attribute.getAttributeValue());
				}
				
			}
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForMultipleStyleRequests() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("100143888");
		inputDataRequest.add("100143887");
		inputDataRequest.add("100143886");
		inputDataRequest.add("100143885");
		requestData.setRequestType(GetRequestType.STYLE.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(4, response.getResponseList().size());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}

	@Test
	public void testGetResponse_ForInvalidStyleRequests() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("400");
		requestData.setRequestType(GetRequestType.STYLE.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertNotNull(response.getErrorResponseData());
			Assert.assertEquals("Client", response.getErrorResponseData().getErrorCode());
			Assert.assertEquals("No items found for the specified search criteria", response.getErrorResponseData().getErrorMessage());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForComboInvalidStyleRequests() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("400");
		inputDataRequest.add("100143888");
		requestData.setRequestType(GetRequestType.STYLE.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForSkuRequest() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("100005004");
		requestData.setRequestType(GetRequestType.SKU.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			ItemCatalogData skuResponse = response.getResponseList().get(0);
			Assert.assertEquals(GetRequestType.SKU.toString(), skuResponse.getResponseType());
			Assert.assertEquals(1, skuResponse.getPimEntry().getEntries()
					.getItemCatalogSpecData().getSupplierData().getUpcodes().size());
			Assert.assertEquals("0611852074134", skuResponse.getPimEntry().getEntries().getItemCatalogSpecData()
					.getSupplierData().getUpcodes().get(0).getUpc());
			Assert.assertEquals("EAN13", skuResponse.getPimEntry().getEntries().getItemCatalogSpecData()
					.getSupplierData().getUpcodes().get(0).getType());
			Assert.assertEquals(Boolean.TRUE, skuResponse.getPimEntry().getEntries().getItemCatalogSpecData()
					.getSupplierData().getIsPrimaryFlag());
			Assert.assertEquals("", skuResponse.getPimEntry().getEntries().getItemCatalogSpecData().getPurchaseType());
			Assert.assertNotNull(skuResponse.getPimEntry().getEntries().getItemSkuSpecData());
			ItemDifferentiatorsData styleSpecData = skuResponse.getPimEntry().getEntries().getItemSkuSpecData().getDifferentiators().get(0);
			Assert.assertNull(styleSpecData.getCodes());
			Assert.assertEquals("SIZE", styleSpecData.getDifferentiatorsType());
			Assert.assertEquals("70715", styleSpecData.getDifferentiatorsIndicator());
			Assert.assertEquals("Teacup Cup", styleSpecData.getVendorDescription());
			ItemDifferentiatorsData colorSpecData = skuResponse.getPimEntry().getEntries().getItemSkuSpecData().getDifferentiators().get(1);
			Assert.assertEquals("COLOR", colorSpecData.getDifferentiatorsType());
			Assert.assertEquals("199", colorSpecData.getDifferentiatorsIndicator());
			Assert.assertEquals("MULTI/WHT", colorSpecData.getVendorDescription());
			List<AttributeData> allAttributes = skuResponse.getPimEntry().getAllAttributes().get(Long.valueOf("100759603"));
			for (AttributeData attribute : allAttributes) {
				if (attribute.getAttributeName().equals("Bridal_Registry")) {
					Assert.assertEquals("Y", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Color_Family")) {
					Assert.assertEquals("100-WHITE", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Fashion_Vs_Basic")) {
					Assert.assertEquals("BASIC", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("SourcedDomestically")) {
					Assert.assertEquals("", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("SKU_Active_Start_Date")) {
					Assert.assertEquals("", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Source_Size_Code")) {
					Assert.assertEquals("895070715", attribute.getAttributeValue());
				}
				
			}
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForStyleColorRequest() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("224233268530");
		requestData.setRequestType(GetRequestType.STYLE_COLOR.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			ItemCatalogData data = catalogData.get(0);
			Assert.assertEquals(GetRequestType.STYLE_COLOR.toString(), data.getResponseType());
			Assert.assertNotNull(data.getPimEntry().getEntries().getItemStyleColorSpecData());
			Assert.assertEquals(Long.valueOf("224233268"), data.getPimEntry().getEntries().getItemStyleColorSpecData().getStyleId());
			Assert.assertEquals(Integer.valueOf("530"), data.getPimEntry().getEntries().getItemStyleColorSpecData().getColorCode());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForPackRequest() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("216546862");
		requestData.setRequestType(GetRequestType.PACK.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			ItemCatalogData data = catalogData.get(0);
			Assert.assertEquals(GetRequestType.PACK.toString(), data.getResponseType());
			Assert.assertNotNull(data.getPimEntry().getEntries().getItemComplexPackSpecData());
			ItemComplexPackSpecData packSpecData = data.getPimEntry().getEntries().getItemComplexPackSpecData();
			Assert.assertEquals(Long.valueOf("0400683963775"), packSpecData.getPackIdbId());
			Assert.assertEquals(10, packSpecData.getPackComponents().size());
			Assert.assertEquals("V", packSpecData.getPackType());
			List<AttributeData> allAttributes = data.getPimEntry().getAllAttributes().get(Long.valueOf("216546862"));
			Assert.assertEquals(100, allAttributes.size());
			for (AttributeData attribute : allAttributes) {
				if (attribute.getAttributeName().equals("Vendor_Style")) {
					Assert.assertEquals("NWBLONDEYW", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Color_Family")) {
					Assert.assertEquals("250-BEIGE", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Selling_Season")) {
					Assert.assertEquals("00-YEAR ROUND", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("SourcedDomestically")) {
					Assert.assertEquals("", attribute.getAttributeValue());
				} else if (attribute.getAttributeName().equals("Brand")) {
					Assert.assertEquals("", attribute.getAttributeValue());
				} 
			}
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForMultiplePackRequests() {
		List<String> inputDataRequest = new ArrayList<String>();
		inputDataRequest.add("216546862");
		inputDataRequest.add("223021474");
		requestData.setRequestType(GetRequestType.PACK.toString());
		requestData.setInputData(inputDataRequest);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			ItemCatalogData data = catalogData.get(0);
			Assert.assertEquals(GetRequestType.PACK.toString(), data.getResponseType());
			Assert.assertNotNull(data.getPimEntry().getEntries().getItemComplexPackSpecData());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForPackRequestWithAllVendorData() {
		requestData.setRequestType(GetRequestType.PACK.toString());
		PackItemRequestData packData = new PackItemRequestData();
		packData.setVendorNumber("2900065");
		packData.setVendorProductNumber("NWBLONDEYW");
		packData.setColorCode("240");
		packData.setSizeCode(90904);
		List<PackItemRequestData> packDatas = new ArrayList<PackItemRequestData>();
		packDatas.add(packData);
		requestData.setInputPackData(packDatas);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(1, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			for (ItemCatalogData data : catalogData) {
				Assert.assertEquals(GetRequestType.PACK.toString(), data.getResponseType());
				Assert.assertNotNull(data.getPimEntry().getEntries().getItemComplexPackSpecData());
				Assert.assertNull(data.getPimEntry().getEntries().getItemStyleColorSpecData());
				ItemComplexPackSpecData specData = data.getPimEntry().getEntries().getItemComplexPackSpecData();
				for (ItemPackComponentData componentData : specData.getPackComponents()) {
					Assert.assertNotNull(componentData.getComponentColor());
					Assert.assertNotNull(componentData.getComponentSize());
					Assert.assertEquals("NWBLONDEYW", componentData.getComponentStyle());
				}
			}
			
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForPackRequestWithColorVendorData() {
		requestData.setRequestType(GetRequestType.PACK.toString());
		PackItemRequestData packData = new PackItemRequestData();
		packData.setVendorNumber("2900065");
		packData.setVendorProductNumber("NWBLONDEYW");
		packData.setColorCode("240");
		List<PackItemRequestData> packDatas = new ArrayList<PackItemRequestData>();
		packDatas.add(packData);
		requestData.setInputPackData(packDatas);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(19, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			for (ItemCatalogData data : catalogData) {
				Assert.assertEquals(GetRequestType.PACK.toString(), data.getResponseType());
				Assert.assertNotNull(data.getPimEntry().getEntries().getItemComplexPackSpecData());
				Assert.assertNull(data.getPimEntry().getEntries().getItemStyleColorSpecData());
				ItemComplexPackSpecData specData = data.getPimEntry().getEntries().getItemComplexPackSpecData();
				for (ItemPackComponentData componentData : specData.getPackComponents()) {
					Assert.assertEquals("240", componentData.getComponentColor());
					Assert.assertNotNull(componentData.getComponentSize());
					Assert.assertEquals("NWBLONDEYW", componentData.getComponentStyle());
				}
			}
			
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForPackRequestWithSizeVendorData() {
		requestData.setRequestType(GetRequestType.PACK.toString());
		PackItemRequestData packData = new PackItemRequestData();
		packData.setVendorNumber("2900065");
		packData.setVendorProductNumber("NWBLONDEYW");
		packData.setSizeCode(90904);
		List<PackItemRequestData> packDatas = new ArrayList<PackItemRequestData>();
		packDatas.add(packData);
		requestData.setInputPackData(packDatas);
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals(10, response.getResponseList().size());
			List<ItemCatalogData> catalogData = response.getResponseList();
			for (ItemCatalogData data : catalogData) {
				Assert.assertEquals(GetRequestType.PACK.toString(), data.getResponseType());
				Assert.assertNotNull(data.getPimEntry().getEntries().getItemComplexPackSpecData());
				Assert.assertNull(data.getPimEntry().getEntries().getItemStyleColorSpecData());
				ItemComplexPackSpecData specData = data.getPimEntry().getEntries().getItemComplexPackSpecData();
				for (ItemPackComponentData componentData : specData.getPackComponents()) {
					Assert.assertNotNull(componentData.getComponentColor());
					Assert.assertNotNull(componentData.getComponentSize());
					Assert.assertEquals("NWBLONDEYW", componentData.getComponentStyle());
				}
			}
			
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForEmptyStyleColorRequestData() {
		requestData.setRequestType(GetRequestType.STYLE_COLOR.toString());
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals("Client", response.getErrorResponseData().getErrorCode());
			Assert.assertEquals("No items found for the specified search criteria", response.getErrorResponseData().getErrorMessage());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForEmptySkuRequestData() {
		requestData.setRequestType(GetRequestType.SKU.toString());
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals("Client", response.getErrorResponseData().getErrorCode());
			Assert.assertEquals("No items found for the specified search criteria", response.getErrorResponseData().getErrorMessage());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForEmptyStyleRequestData() {
		requestData.setRequestType(GetRequestType.STYLE.toString());
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals("Client", response.getErrorResponseData().getErrorCode());
			Assert.assertEquals("No items found for the specified search criteria", response.getErrorResponseData().getErrorMessage());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
	
	@Test
	public void testGetResponse_ForEmptyPackRequestData() {
		requestData.setRequestType(GetRequestType.PACK.toString());
		belkProductService = new BelkProductService(requestData);
		try {
			IntegrationResponseData response = belkProductService.getResponse();
			Assert.assertEquals("Client", response.getErrorResponseData().getErrorCode());
			Assert.assertEquals("No packs found for the specified search criteria", response.getErrorResponseData().getErrorMessage());
		} catch (BelkProductIntegrationException e) { 
			fail("unexpected exception while processing style request ");
		}
	}
}
