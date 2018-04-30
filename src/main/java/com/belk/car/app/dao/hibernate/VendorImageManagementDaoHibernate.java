package com.belk.car.app.dao.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.util.DateUtil;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.belk.car.app.dao.VendorImageManagementDao;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.vendorimage.VendorImage;

public class VendorImageManagementDaoHibernate extends UniversalDaoHibernate implements
VendorImageManagementDao  {

	@Override 
	public List<Car> getCarsForUser(User usr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus) throws Exception{
		
		StringBuffer query = new StringBuffer(); 
				query.append("FROM Image i inner join fetch i.vendorImage vi ")
					 .append("WHERE i.statusCd='ACTIVE' and vi.vendorImageStatus.vendorImageStatusCd in (");
		for(int i=0; i<vendorImageStatus.length-1; i++){
			query.append(" ?,");
		}
		query.append(" ? ) ");
		
		List<Image> images = getHibernateTemplate().find(query.toString(), vendorImageStatus);

		if (images == null || images.isEmpty()) {
				return new ArrayList<Image>(0);
		} else {
			return images;
		}
	}
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus, Date date) throws Exception{
		
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
        dateFormat.format(date);
		String strDate=  dateFormat.format(date);
		
		StringBuffer query = new StringBuffer(); 
				query.append("FROM Image i inner join fetch i.vendorImage vi ")
					 .append("WHERE i.statusCd='ACTIVE' and vi.vendorImageStatus.vendorImageStatusCd in (");
		for(int i=1; i<vendorImageStatus.length; i++){
			query.append(" ?,");
		}
		query.append(" ? ) ");
		query.append("and vi.updatedDate >= to_date('"+strDate+"','MM/DD/YYYY HH24:MI:SS')");
		List<Image> images = getHibernateTemplate().find(query.toString(), vendorImageStatus);

		if (images == null || images.isEmpty()) {
				return new ArrayList<Image>(0);
		} else {
			return images;
		}
	}
	
	public Object save(Object ob) {
		this.getHibernateTemplate().saveOrUpdate(ob);
        getHibernateTemplate().flush();
		return ob;
	}

	
	public Object saveOrUpdate(Object ob) {
		HibernateTemplate hbt =  getHibernateTemplate();
		try {			
			hbt.getSessionFactory().getCurrentSession().beginTransaction();		
			hbt.saveOrUpdate(ob);
			hbt.getSessionFactory().getCurrentSession().getTransaction().commit();
			hbt.flush();
		} catch(Exception ex) {
			if (log.isErrorEnabled())
			log.error("Caught Transaction Exception while saving the object" +ex);
		}	
		return ob;
	}
	
	public List<Image> getRemovedVendorImages(String strLastJobRunTime) {
		StringBuffer query = new StringBuffer();
		if(strLastJobRunTime.isEmpty()){
			strLastJobRunTime = DateUtil.formatDate(new Date(),  "MM/dd/yyyy HH:mm:ss");
		}
		List<Image> images = null;
		query.append("SELECT {i.*} ")
			 .append(" FROM IMAGE i " )
			 .append(" inner join VENDOR_IMAGE vi on i.IMAGE_ID = vi.IMAGE_ID ")
			 .append(" WHERE ((vi.BUYER_APPROVED in ('APPROVED', 'REJECTED') " +
			 		 " AND vi.VENDOR_IMAGE_STATUS_CD = 'MQ_PASSED' ")
			 .append(" AND i.IMAGE_SOURCE_TYPE_CD = 'VENDOR_UPLOADED')  ")
			 .append(" OR vi.VENDOR_IMAGE_STATUS_CD = 'DELETED')")
			 .append(" AND vi.UPDATED_DATE > to_date('" + strLastJobRunTime +"', 'MM/DD/YYYY HH24:MI:SS')") ;
		
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try{
		SQLQuery q = session.createSQLQuery(query.toString())
					.addEntity("i", Image.class);
					//.addJoin("vi", "i.vendorImage");
		images = q.list();
		}catch(Exception e){
			log.error("Hibernate Exception : "+e);
			e.printStackTrace();
		}

		if (images == null || images.isEmpty()) {
			return new ArrayList<Image>(0);
		} else {
			return images;
		}
	}

	/**
	 *  This method gets the image id from sequence
	 *  @return long
	 */
	@Override
	public Long getImageIdFromSeq() {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorImageManagementDaoHibernate's getImageIdFromSeq method");
		}
		long imageId = 0;
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String query = "SELECT VENDOR_IMAGE_SEQ.NEXTVAL FROM dual";
		List list = session.createSQLQuery(query).list();
		imageId = Long.parseLong(list.get(0).toString());
		if (log.isDebugEnabled()) {
			log.debug("sequence for image id is..."+imageId);
		}
		return imageId;
	}
	/**
	 *  This method gets the shot type code for vendor images
	 *  @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNextShotTypeCodeList(long vendorStyleId, String colorCode, Long carId) {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorImageManagementDaoHibernate's getShotTypeCode method");
		}
		List<String> listShotTyes = new ArrayList<String>();
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		String query = " SELECT SWATCH_TYPE_CD FROM SWATCH_TYPE WHERE SWATCH_TYPE_CD " + 
			       " NOT IN (SELECT SWATCH_TYPE_CD FROM VENDOR_IMAGE VI  " +
			       " INNER JOIN CAR_IMAGE CI ON  CI.IMAGE_ID=VI.IMAGE_ID " +
			       " INNER JOIN IMAGE I ON I.IMAGE_ID=VI.IMAGE_ID " +
			       " WHERE VENDOR_STYLE_ID = "+vendorStyleId +
			       " AND COLOR_CODE="+colorCode + " " +
			       " AND VENDOR_IMAGE_STATUS_CD <> 'DELETED' AND I.STATUS_CD <> 'DELETED' AND CI.CAR_ID="+carId+") " +
		           " ORDER BY SWATCH_TYPE_CD ";
		listShotTyes = session.createSQLQuery(query).list();
		//listShotTyes = list.get(0).toString());
		return listShotTyes;
	}
	
	
		
	public Car getCarByVendorImageId(long vendorImageId){
		List<Car> carList = null;				
		String query = "";				
		query = "from Car as c inner join fetch c.carImages as ci " +
				" inner join fetch ci.image as i" +
				" inner join fetch i.vendorImage as vi " +
				" inner join fetch vi.vendorImageStatus as vis" +
				" left join fetch c.carNotes as notes" +
				" where  vi.vendorImageId = ?";			
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);
			
			q.setLong(0,vendorImageId);				
			carList = q.list();	
			if(carList!= null){
				return carList.get(0);
			}
			
		} catch (Exception e) {
			log.error("Hibernate Exception",e);			
		}
		return null;
	}
	
	public VendorImage getVendorImageDetailsById(long vendorImageId){
		List<VendorImage> venImageList = null;				
		String query = "";
		query="from VendorImage as vi inner join fetch vi.image as i where vi.vendorImageId = ?";
		//query = "from Car as c inner join fetch c.carImages as ci inner join fetch ci.image as i" +
		//" inner join fetch i.vendorImage as vi where  vi.vendorImageId = ?";			
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);		
			q.setLong(0,vendorImageId);				
			venImageList = q.list();	
			if(venImageList!= null){
				return venImageList.get(0);
			}
			
		} catch (Exception e) {
			log.error("Hibernate Exception",e);			
		}
		return null;
	}
	
	/**
	 *  Below method added for production issue
	 *  This method gets the media compass image object for 
	 *  CAR iD
	 *  @input carId
	 *  @return MediaCompassImage
	 */
	public MediaCompassImage getMediaComapssByCarId(long carId){
		
		List<Object> mediaCompassImageList = null;
		MediaCompassImage mediaCompassImage= null;
		String query = "";	
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		query = "SELECT m.* from MEDIA_COMPASS_IMAGE m,CAR c where m.car_id=c.car_id and c.car_id="+carId;
		mediaCompassImageList =session.createSQLQuery(query).list();
		if(mediaCompassImageList!= null && mediaCompassImageList.size() > 0){
			mediaCompassImage=new MediaCompassImage();
		}
		return mediaCompassImage;
	}

	@Override
	public VendorImage getVendorImageDetailsByPimImageId(String vendorPimImageId) {
		List<VendorImage> venImageList = null;				
		String query = "";
		query="from VendorImage vi where vi.vendorImagePimId = ?";
		//query = "from Car as c inner join fetch c.carImages as ci inner join fetch ci.image as i" +
		//" inner join fetch i.vendorImage as vi where  vi.vendorImageId = ?";			
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			Query q = session.createQuery(query);		
			q.setString(0,vendorPimImageId);	
			if(log.isDebugEnabled())
				log.debug(query.toString());
			 venImageList = q.list();
			 log.debug(venImageList);
			if(venImageList!= null && !venImageList.isEmpty()){
				for( VendorImage vendorImage:venImageList) {
					if(!vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED")){
							return vendorImage;
					}
				}
					
				return venImageList.get(0);
			}
			
			
		} catch (Exception e) {
			log.error("Hibernate Exception",e);			
		}
		return null;
	}
}
