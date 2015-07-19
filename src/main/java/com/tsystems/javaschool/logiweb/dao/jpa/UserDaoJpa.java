package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.dao.UserDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.LogiwebUser;

@Component
public class UserDaoJpa extends GenericDaoJpa<LogiwebUser> implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoJpa.class);

    @Override
    public LogiwebUser findByEmail(String email) throws DaoException {
        try {
            Query q = getEntityManager().createQuery(
                    "SELECT u FROM " + getEntityClass().getSimpleName()
                            + " u WHERE mail = :email");
            q.setParameter("email", email);

            List<LogiwebUser> result = q.getResultList();
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.warn(e);
            throw new DaoException(e);
        }
    }
}
