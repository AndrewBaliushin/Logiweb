package com.tsystems.javaschool.logiweb.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.LogiwebUser;
import com.tsystems.javaschool.logiweb.entities.status.UserRole;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public interface UserService extends UserDetailsService {
    
    /**
     * Create new logiweb user. 
     * 
     * @param userName
     * @param pass
     * @param role in system 
     * @return
     * @throws ServiceValidationException if user with this name already exist
     * @throws LogiwebServiceException if something unexpected happened
     */
    public int createNewUser(String userName, String pass, UserRole role) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Change username
     * 
     * @param olUsername
     * @param newUsername
     * @throws ServiceValidationException if user with this name already exist
     * @throws LogiwebServiceException if something unexpected happened
     */
    public void changeUsername(String oldUsername, String newUsername) throws ServiceValidationException, LogiwebServiceException;

    /**
     * Find user by id.
     * 
     * @param id
     * @return user or null
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    LogiwebUser findUserById(int id) throws LogiwebServiceException;
}
