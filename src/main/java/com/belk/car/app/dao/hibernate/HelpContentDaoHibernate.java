package com.belk.car.app.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.HelpContentDao;
import com.belk.car.app.model.HelpContent;

public class HelpContentDaoHibernate extends UniversalDaoHibernate implements
		HelpContentDao {

	public HelpContent save(HelpContent content) {
		getHibernateTemplate().saveOrUpdate(content);
        getHibernateTemplate().flush();
		return content;
	}

	public HelpContent getHelpContent(String contentKey, String contentSection) {
		Object[] args = {contentKey, contentSection};
		List l = getHibernateTemplate().find(
				"from HelpContent hc where hc.contentKey = ? and hc.contentSection = ?", args);
		HelpContent c = null ;
		if (l != null && !l.isEmpty()) {
			c = (HelpContent) l.get(0);
		}
		return c ;
	}

	public HelpContent getHelpContent(long contentId) {
		return (HelpContent) getHibernateTemplate().get(HelpContent.class, contentId);
	}
	
	public List<HelpContent> getHelpContent(String contentKey) {
		return getHibernateTemplate().find(
				"from HelpContent hc where hc.contentKey = ?", contentKey);
	}

	public Object get(Class clazz, Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<HelpContent> getAllHelpContent() {
		return getHibernateTemplate().find(
		"from HelpContent content order by content.contentName");
	}

	public void remove(Class clazz, Serializable id) {
		// TODO Auto-generated method stub

	}

	public Object save(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

}
