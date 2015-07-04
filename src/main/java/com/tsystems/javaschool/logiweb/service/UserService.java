package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.model.User;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public interface UserService {

    User getUserByMd5PassAndMail(String mail, String pass) throws LogiwebServiceException;

}
