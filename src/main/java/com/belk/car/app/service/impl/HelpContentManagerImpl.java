/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.HelpContentDao;
import com.belk.car.app.model.HelpContent;
import com.belk.car.app.service.HelpContentManager;

/**
 * @author antoniog
 *
 */
public class HelpContentManagerImpl extends UniversalManagerImpl implements
		HelpContentManager {

	private HelpContentDao dao;

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * @param dao the dao implementation
	 */

	public void setDao(HelpContentDao dao) {
		this.dao = dao;
	}

	public List<HelpContent> getHelpContent(String contentKey) {
		return dao.getHelpContent(contentKey);
	}

	public HelpContent getHelpContent(String contentKey, String contentSection) {
		return dao.getHelpContent(contentKey, contentSection);
	}

	public HelpContent getHelpContent(long contentId) {
		return dao.getHelpContent(contentId);
	}

	public HelpContent save(HelpContent content) {
		return dao.save(content);
	}
	
	public List<HelpContent> getAllHelpContent() {
		return dao.getAllHelpContent();
	}
}
