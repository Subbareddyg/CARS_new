package com.belk.car.app;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.appfuse.model.User;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.belk.car.app.dao.hibernate.CarDaoHibernate;
import com.belk.car.app.dao.hibernate.CarManagementDaoHibernate;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeDatatype;
import com.belk.car.app.model.AttributeType;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.util.ReadIDBFile;


public class CarMgmtTest extends AbstractDependencyInjectionSpringContextTests {
	public CarDaoHibernate carDao;
	public CarManagementDaoHibernate mgmtDao;
	public CarManager carManager;
	
    public void testTest() throws Exception {
        assertEquals("true","true");
    }
    protected String[] getConfigLocations(){
    	String arr[]={ "classpath:test-Ctxt.xml"
                };
    	return arr;
    }
    
    public void testNotNull() throws Exception{
    	
    	assertNotNull(carDao);
    }
    
	public CarDaoHibernate getCarDao() {
		return carDao;
	}
	public void setCarDao(CarDaoHibernate carDao) {
		this.carDao = carDao;
	}
/*
	public Car addCarNote(Car car, boolean externallyDisplayed,
			String noteText, String noteTypeCd, String updatedBy) {
		return mgmtDao.addCarNote(car, externallyDisplayed, noteText,
				noteTypeCd, updatedBy);
	}
/*
	public Car assignProductTypeToCar(ProductType productType, Car car) {
		return mgmtDao.assignProductTypeToCar(productType, car);
	}
	public Car copyCar(Car oldCar, SourceType sourceType, User user) {
		return mgmtDao.copyCar(oldCar, sourceType, user);
	}
/*	public void testCreateCar(){
		IdbCarDataTO idbCarTO=new IdbCarDataTO();
		idbCarTO.setClassName("TESTClass");
		idbCarTO.setClassNumber("123");
		idbCarTO.setDepartmentName("TESTDept");
		idbCarTO.setDepartmentNumber("122");
		idbCarTO.setValidItemFlag("Y");
		idbCarTO.setVendorName("TESTVendor");
		idbCarTO.setVendorNumber("1111");
		idbCarTO.setVendorStyle("TESTVendorStyle");
		idbCarTO.setVendorStyleDescription("TESTVendorStyleDesc");
		IdbCarSkuTO sku=new IdbCarSkuTO();
		Date dt=new Date();
		sku.setBelkUPC((new Long(dt.getTime()).toString()));
		sku.setEndDeliveryDate("01-JAN-2008");
		sku.setStartDeliveryDate("27-DEC-2007");
		sku.setUpcAddDate("27-DEC-2008");
		sku.setVendorColor("1:2:3");
		sku.setVendorColorName("red");
		sku.setVendorSizeCode("14 P");
		sku.setVendorSizeDesc("14 Petite");
		sku.setVendorSizeSide("22 inches");
		sku.setVendorSizeTop("2 inches");
		Vector <IdbCarSkuTO> v=new Vector();
		v.add(sku);
		idbCarTO.setStyleInfo(v);
		SourceType sourceType=mgmtDao.getSourceTypeForCode("PO");
		User user=mgmtDao.getUserForUsername("TEST");
		Car car= mgmtDao.createCar(idbCarTO, sourceType, user);
        assertNull(car);
	}

	public void testGetClassification() {
		Department dept=mgmtDao.getDepartment("TestDepartment", "1", "CARTEST");
		Classification c= mgmtDao.getClassification("TESTCLASSIFICATION", "1456","CARTEST", dept);
		assertNotNull(c);
	}
	public void testGetDepartment() {
		Department dept= mgmtDao.getDepartment("TestDepartment", "1", "CARTEST");
		assertNotNull(dept);
	}
	public void testGetVendor() {
		Vendor vendor=mgmtDao.getVendor("TEST1", "1234","test@test.com","CARTEST");
		assertNotNull(vendor);
	}
	public void testGetVendorStyle() {
	    Department department=mgmtDao.getDepartment("TestDepartment", "1","CARTEST");
	    Classification classification=mgmtDao.getClassification("class1", "145", "CARTEST",department);
	    VendorStyle vs=mgmtDao.getVendorStyle("TEST1VS", "1234", "TEST1Vendor", "142",  "test@test.com", classification, "CARTEST");
		assertNotNull(vs);
	}
	/*
	public void doCarAttributes(Long carId, Long classId, Long deptId,
			Long productTypeId) {
		mgmtDao.doCarAttributes(carId, classId, deptId, productTypeId);
	}
	*/
	public void testLoadIDB(){
		ReadIDBFile fl=new ReadIDBFile();
		try {
			Collection <IdbCarDataTO>col=fl.process(new File("c:/CARSFiles/CARS.txt"));
			SourceType sourceType=mgmtDao.getSourceTypeForCode("PO");
			User user=mgmtDao.getUserForUsername("josh@fcps.com");
			for (IdbCarDataTO to:col){
				this.getCarManager().createCar(to, sourceType, user);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Attribute> getAllAttributes() {
		return carDao.getAllAttributes();
	}
	public List<Department> getAllDepartments() {
		return carDao.getAllDepartments();
	}
	public List<AttributeDatatype> getAttributeDataTypes() {
		return carDao.getAttributeDataTypes();
	}
	public Attribute getAttributeForName(String name) {
		return mgmtDao.getAttributeForName(name);
	}
	public List<CarAttribute> getAttributesForCar(Car car) {
		return carDao.getAttributesForCar(car);
	}
	public List<AttributeType> getAttributeTypes() {
		return carDao.getAttributeTypes();
	}
	public CarAttribute getCarAttributeById(Long carAttributeId) {
		return mgmtDao.getCarAttributeById(carAttributeId);
	}
	public Car getCarFromId(Long carNumber) {
		return mgmtDao.getCarFromId(carNumber);
	}
	public List<Car> getCarsForUser(User usr) {
		return carDao.getCarsForUser(usr);
	}
	public CarManagementDaoHibernate getMgmtDao() {
		return mgmtDao;
	}
	public void setMgmtDao(CarManagementDaoHibernate mgmtDao) {
		this.mgmtDao = mgmtDao;
	}
   /*
	public void testGetList(){
		User usr=this.mgmtDao.getUserForUsername("joshua.davis@avenuea-razorfish.com");
		this.carDao.getCarsForUser(usr);
	}
	*/
	public CarManager getCarManager() {
		return carManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
}
