package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import org.hibernate.SQLQuery;
import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UserDaoHibernate;
import org.appfuse.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import com.belk.car.app.dao.CarUserDao;

public class BelkUserDaoHibernate extends UserDaoHibernate implements CarUserDao {

	/** 
	 * {@inheritDoc}
	 */
	public String getUserPassword(String username) {
		SimpleJdbcTemplate jdbcTemplate = new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
		Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
		String password = null;
		try {
			password = jdbcTemplate.queryForObject("select password from " + table.name() + " where " + table.uniqueConstraints()[0].columnNames()[0] + " =?",
					String.class, username);
		} catch (EmptyResultDataAccessException erx) {
			//invalid user Or User is not available
		}
		return password;

	}
	/*
	 * This method is used to search only 'ACTIVE' users. 
	 * Pagination implementation changes by modifying below method.
	 * added order by 
	 * Added hibernate pagination by setting First and Max result
	 */

	public List<User> searchUsers(String firstName, String lastName, String emailAddress, String userTypeCd,Integer page,String sortedOn,String sortedOrder) {

		List<User> userList = null;
		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		StringBuffer sb = new StringBuffer("select cu.*,cu.first_name as firstName,cu.email_addr as email,cu.user_type_cd as userType  from car_user cu where 0=0 ");
		if (StringUtils.isNotBlank(userTypeCd)) {
			sb.append(" and cu.user_type_cd = '"+ userTypeCd.toUpperCase() +"'");
		}
		if (StringUtils.isNotBlank(firstName)) {
			sb.append(" and upper(cu.first_name) like '%" + firstName.toUpperCase() + "%'");
		}

		if (StringUtils.isNotBlank(lastName)) {
			sb.append(" and upper(cu.last_name) like '%"+ lastName.toUpperCase() +"%'");
		}

		if (StringUtils.isNotBlank(emailAddress)) {
			sb.append(" and upper(cu.email_addr) like '%"+ emailAddress.toUpperCase() +"%'");
		}

		sb.append(" and cu.status_cd ='ACTIVE' ");
		try {
				if(StringUtils.isNotBlank(sortedOn) && StringUtils.isNotBlank(sortedOrder)){
					if("1".equals(sortedOn))
						sb.append("order by upper(firstName) ");
					if("2".equals(sortedOn))
						sb.append("order by upper(email)  ");
					if("4".equals(sortedOn))
						sb.append("order by upper(userType) ");
					
					if("descending".equals(sortedOrder)){
						sb.append("desc");
					}else{
						sb.append("asc");
					}
				}else{
					sb.append("order by upper(firstName) asc ");
				}
				SQLQuery q =session.createSQLQuery(sb.toString());
				q.addEntity(User.class);
				if(page != null){
					q.setFirstResult(25*(page.intValue()-1));
					q.setMaxResults(25);
				}
				userList = q.list();
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("Hibernate Exception", e);
		}

		if (userList == null) {
			userList = new ArrayList<User>();
		}
		return userList;

	}

	/*
	 * This method is used for updating/removing(Status update to 'DELETE') vendor or user.
	 */
	
	public void updateVendorOrUser(User vendor){
		getHibernateTemplate().saveOrUpdate(vendor);
		getHibernateTemplate().flush();
	}
	
	 /*
	 * This method is used for get vendor or user which are in 'DELETED' status.
	 */
	public User getVendorOrUser(String userName){
		List list = getHibernateTemplate().find("from User where username = '" + userName +"' and statusCd='DELETED' ");
		User user = null;
		if(list!=null && list.size()>0)
			user  = (User) list.get(0);
		getHibernateTemplate().flush();
		return user;
	}
	
}
