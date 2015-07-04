package com.tsystems.javaschool.logiweb.dao;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.UserNotFoundDaoException;
import com.tsystems.javaschool.logiweb.model.User;

/**
 * CRUD operations for application user entity.
 * 
 * @author Andrey Baliushin
 */
public interface UserDao extends GenericDao<User> {
    
    User getByMd5PassAndMail(String email, String md5pass) throws UserNotFoundDaoException, DaoException;

}
