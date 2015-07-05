package com.tsystems.javaschool.logiweb.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.UserDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.UserNotFoundDaoException;
import com.tsystems.javaschool.logiweb.dao.jpa.GenericDaoJpa;
import com.tsystems.javaschool.logiweb.model.User;
import com.tsystems.javaschool.logiweb.service.UserService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.UserNotFoundServiceException;

/**
 * Data manipulation related to Users of this app.
 * 
 * @author Andrey Baliushin
 *
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private EntityManager em;
    
    private UserDao userDao;

    public UserServiceImpl(EntityManager em, UserDao userDao) {
        this.userDao = userDao;
        this.em = em;
    }

    @Override
    public User getUserByMd5PassAndMail(String mail, String pass) throws LogiwebServiceException {
        try {
            em.getTransaction().begin();
            User user = userDao.getByMd5PassAndMail(mail, getMD5Hash(pass));
            em.getTransaction().commit();
            return user;
        } catch (UserNotFoundDaoException e) {
            LOG.info("Failed attemt to get user by mail " + mail);
            throw new UserNotFoundServiceException();
        } catch (Exception e) {
            LOG.warn(e);
            throw new LogiwebServiceException(e);
        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }
    
    private String getMD5Hash(String md5) throws LogiwebServiceException {
        try {
             MessageDigest md = MessageDigest.getInstance("MD5");
             md.reset();
             byte[] array = md.digest(md5.getBytes());
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

    

}
