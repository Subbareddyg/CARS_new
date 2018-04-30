package com.belk.car.app.dao;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.exceptions.UserRankException;
import com.belk.car.app.model.UsersRankTmp;

public interface UserRankDao extends UniversalDao {
	public UsersRankTmp save(UsersRankTmp usersRankTmp)
			throws UserRankException;

	public void cleanUsersRankTmpTable() throws UserRankException;

	public void callBuildUsersRankTmp() throws UserRankException;
}
