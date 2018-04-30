package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.HelpContent;

public interface HelpContentDao extends UniversalDao {

	  List<HelpContent> getHelpContent(String contentKey);

	  List<HelpContent> getAllHelpContent();

	  HelpContent getHelpContent(String contentKey, String contentSection);

	  HelpContent getHelpContent(long id);

	  HelpContent save(HelpContent content);
	  

}
