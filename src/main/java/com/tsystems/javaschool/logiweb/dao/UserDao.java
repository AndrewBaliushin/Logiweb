package com.tsystems.javaschool.logiweb.dao;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.UserNotFoundDaoException;
import com.tsystems.javaschool.logiweb.model.User;

/**
 * CRUD operations for user entity.
 * 
 * @author Andrey Baliushin
 */
public interface UserDao extends GenericDao<User> {
    
    /**
     * Get user by his pass (hash) and mail.
     * 
     * @param email of user
     * @param md5pass -- md5 hash of user password
     * @return user
     * @throws UserNotFoundDaoException if user does not exist.
     * @throws DaoException if something unexpected happend.
     */
    User getByMd5PassAndMail(String email, String md5pass) throws UserNotFoundDaoException, DaoException;

}
