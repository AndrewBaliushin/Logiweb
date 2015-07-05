package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.model.User;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

/**
 * Data manipulation related to User of this app.
 * 
 * @author Andrey Baliushin
 */
public interface UserService {

    User getUserByMd5PassAndMail(String mail, String pass) throws LogiwebServiceException;

}
