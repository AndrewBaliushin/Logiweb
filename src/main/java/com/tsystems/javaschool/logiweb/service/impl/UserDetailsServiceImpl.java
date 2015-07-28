package com.tsystems.javaschool.logiweb.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.UserDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.LogiwebUser;
import com.tsystems.javaschool.logiweb.entities.status.UserRole;
import com.tsystems.javaschool.logiweb.model.DriverUserModel;
import com.tsystems.javaschool.logiweb.model.UserModel;
import com.tsystems.javaschool.logiweb.service.UserService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    
    private static final Logger LOG = Logger.getLogger(UserDetailsServiceImpl.class);

    private UserDao userDao;
    
    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userName)
            throws UsernameNotFoundException {
        try {
            LogiwebUser user = userDao.findByEmail(userName);
            if (user == null) {
                throw new UsernameNotFoundException(userName);
            } else {
                return buildSecurityUserFromUserEntity(user);
            }
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new UsernameNotFoundException(userName);
        }
    }
    
    public int createNewUser(String userName, String pass, UserRole role) throws ServiceValidationException, LogiwebServiceException {
        if (userName == null || userName.isEmpty()) {
            throw new ServiceValidationException(
                    "Username can't be empty.");
        }
        
        try {
            LogiwebUser userWithSameMail = userDao.findByEmail(userName);
            if (userWithSameMail != null) {
                throw new ServiceValidationException(
                        "User with username: " + userName + " already exist.");
            }
            
            LogiwebUser newUser = new LogiwebUser();
            newUser.setMail(userName);
            newUser.setPassMd5(getMD5Hash(pass));
            newUser.setRole(role);
            
            userDao.create(newUser);
            
            LOG.info("User #" + newUser.getId() + " " + userName + " created");
            return newUser.getId();
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
    
    private User buildSecurityUserFromUserEntity(LogiwebUser userEntity) {
        String username = userEntity.getMail();
        String password = userEntity.getPassMd5();
        GrantedAuthority userRole = new SimpleGrantedAuthority(userEntity
                .getRole().name());

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(userRole);

        if (userEntity.getRole() == UserRole.ROLE_DRIVER) {
            return new DriverUserModel(username, password, authorities,
                    userEntity.getAttachedDriver().getId());
        } else {
            return new UserModel(username, password, authorities);
        }
    }
    
    private String getMD5Hash(String pass) throws LogiwebServiceException {
        try {
             MessageDigest md = MessageDigest.getInstance("MD5");
             md.reset();
             byte[] array = md.digest(pass.getBytes());
             StringBuffer sb = new StringBuffer();
             for (int i = 0; i < array.length; ++i) {
               sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
             return sb.toString();
         } catch (NoSuchAlgorithmException e) {
             LOG.warn("MD5 hashing failed", e);
             throw new LogiwebServiceException("MD5 hashing failed", e);
         }
     }

    @Override
    public void changeUsername(String oldUsername, String newUsername)
            throws ServiceValidationException, LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    @Transactional
    public LogiwebUser findUserById(int id) throws LogiwebServiceException {
        try {
            return userDao.find(id);
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }
    
    
}
