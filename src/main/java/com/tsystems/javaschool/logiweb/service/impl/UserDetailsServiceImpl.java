package com.tsystems.javaschool.logiweb.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import com.tsystems.javaschool.logiweb.model.UserModel;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
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
    
//    public User createNewUser(String userName, String pass) throws LogiwebServiceException {
//        if (userName == null || userName.isEmpty()) {
//            throw new ServiceValidationException(
//                    "Username can't be empty.");
//        }
//        
//        try {
//            LogiwebUser userWithSameMail = userDao.findByEmail(userName);
//            if (userWithSameMail != null) {
//                throw new ServiceValidationException(
//                        "User with username: " + userName + " already exist.");
//            }
//            
//            LogiwebUser newUser = new LogiwebUser();
//            newUser.setMail(userName);
//            newUser.setPassMd5(passMd5);
//            
//            userDao.create(new LogiwebUser())
//        } catch (DaoException e) {
//            LOG.warn("Something unexpected happend.", e);
//            throw new LogiwebServiceException(e);
//        }
//    }
    
    private User buildSecurityUserFromUserEntity(LogiwebUser userEntity) {
        String username = userEntity.getMail();
        String password = userEntity.getPassMd5();
        GrantedAuthority userRole = new SimpleGrantedAuthority(userEntity.getRole()
                .name());

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(userRole);
        
        return new UserModel(username, password, authorities);
    }

}
