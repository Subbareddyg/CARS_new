package com.belk.car.app.service;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.exceptions.UserRankException;
import com.belk.car.app.model.UsersRankTmp;

/**
 * @author AFUTXD3
 *
 */
public interface UserRankManager extends UniversalManager {
	public UsersRankTmp save(UsersRankTmp usersRankTmp)
			throws UserRankException;

	public void cleanUsersRankTmpTable() throws UserRankException;

	public void callBuildUsersRankTmp() throws UserRankException;
}
