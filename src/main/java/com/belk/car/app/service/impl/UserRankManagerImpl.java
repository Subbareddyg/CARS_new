package com.belk.car.app.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.UserRankDao;
import com.belk.car.app.exceptions.UserRankException;
import com.belk.car.app.model.UsersRankTmp;
import com.belk.car.app.service.UserRankManager;

/**
 * @author AFUTXD3
 * 
 */
public class UserRankManagerImpl extends UniversalManagerImpl implements
		UserRankManager {
	private transient final Log log = LogFactory
			.getLog(ManualCarManagerImpl.class);
	private UserRankDao userRankDao; 

	public void setUserRankDao(UserRankDao userRankDao) {
		this.userRankDao = userRankDao;
	}

	@Override
	public UsersRankTmp save(UsersRankTmp usersRankTmp) throws UserRankException {

		return userRankDao.save(usersRankTmp);
	}

	@Override
	public void cleanUsersRankTmpTable() throws UserRankException {
		userRankDao.cleanUsersRankTmpTable();
	}

	@Override
	public void callBuildUsersRankTmp() throws UserRankException{
		userRankDao.callBuildUsersRankTmp();
	}

}
