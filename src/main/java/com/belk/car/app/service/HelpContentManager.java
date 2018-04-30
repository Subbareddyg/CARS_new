package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.HelpContent;

public interface HelpContentManager extends UniversalManager{
	
	  List<HelpContent> getHelpContent(String contentKey);

	  List<HelpContent> getAllHelpContent();

	  HelpContent getHelpContent(String contentKey, String contentSection);

	  HelpContent getHelpContent(long id);

	  HelpContent save(HelpContent content);

}
