package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.UserDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.UserNotFoundDaoException;
import com.tsystems.javaschool.logiweb.model.User;

/**
 * CRUD operations for User entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
public class UserDaoJpa  extends GenericDaoJpa<User> implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoJpa.class);
    
    public UserDaoJpa(Class<User> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByMd5PassAndMail(String email, String passMd5) throws DaoException, UserNotFoundDaoException {
        try {
            Query q = getEntityManager().createQuery(
                    "SELECT u FROM " + getEntityClass().getSimpleName()
                            + " u WHERE mail = :email AND passMd5 = :passMd5");
            q.setParameter("email", email);
            q.setParameter("passMd5", passMd5);

            List<User> result = q.getResultList();
            if(result.isEmpty()) {
                throw new UserNotFoundDaoException();
            }
            return result.get(0);
        } catch (UserNotFoundDaoException e) {
            throw e;
        } catch (Exception e) {
            LOG.warn(e);
            throw new DaoException(e);
        }
    }

}
