package com.belk.car.app.dao.hibernate;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.belk.car.app.dao.UserRankDao;
import com.belk.car.app.exceptions.UserRankException;
import com.belk.car.app.model.UsersRankTmp;

public class UserRankDaoHibernate extends UniversalDaoHibernate implements
		UserRankDao {

	public UserRankDaoHibernate() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public UsersRankTmp save(UsersRankTmp usersRankTmp) {
		try {
			getHibernateTemplate().saveOrUpdate(usersRankTmp);
			getHibernateTemplate().flush();
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Exception occured when saving User Rank Tmp data : " + e);
			}
		}
		return usersRankTmp;
	}

	@Override
	public void cleanUsersRankTmpTable() throws UserRankException {
		log.info("In method to truncate USERS_RANK_TMP Table");
		SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
		Session session = sessionFactory.getCurrentSession();
		StringBuilder query = new StringBuilder();
		
		try{
		query.append("TRUNCATE TABLE USERS_RANK_TMP");
		SQLQuery sQuery = (SQLQuery) session.createSQLQuery(query
				.toString());
		sQuery.executeUpdate();
		session.flush();
		}catch(Exception dae){
			log.error("Exception while truncating USERS_RANK_TMP table"+dae);
			throw new UserRankException("","TRUNCATE  USERS_RANK_TMP TABLE FAILED:",dae);
		}/* finally {
			try {
				if(session !=null) {
					session.close();
				}
			} catch (Exception e) {
				throw new UserRankException(e);
			}
		}*/
	}

	@Override
	public void callBuildUsersRankTmp() throws UserRankException {
		if (log.isInfoEnabled()) {
			log.info("Calling build_userrank_tmp procedure..."); 
		}
		final String query = "{Call build_userrank}";
		try {
			
		this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException {
						Transaction tx =  session.beginTransaction();
						SQLQuery sq =session.createSQLQuery(query.toString());
						int cnt = sq.executeUpdate();
						log.info("Query in executeSQLQuery() method: "+query);
						session.flush();
						session.close();
						log.info("Flush Count"+cnt);
						return cnt;
					}
				});
		if (log.isInfoEnabled()) {
			log.info("Completed executing build_userrank_tmp procedure..."); 
		}
		} catch (Exception e) {
			throw new UserRankException("URERR1005", "build_userrank", e);
		}
	}
	
	

}
