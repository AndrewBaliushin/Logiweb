package com.tsystems.javaschool.logiweb.dao;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.LogiwebUser;

/**
* CRUD operations for user entity.
* 
* @author Andrey Baliushin
*/
public interface UserDao extends GenericDao<LogiwebUser> {
   
   LogiwebUser findByEmail(String email) throws DaoException;
}