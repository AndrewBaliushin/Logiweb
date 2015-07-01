package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.TruckStatus;

/**
 * CRUD operations for Truck entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
public class TruckDaoJpa extends GenericDaoJpa<Truck> implements TruckDao {

    private static final Logger LOG = Logger.getLogger(GenericDaoJpa.class);
    
    public TruckDaoJpa(Class<Truck> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Truck> findByMinCapacityWhereStatusOkAndNotAssignedToOrder(
            float minCargoCapacity) throws DaoException {
        try {
            EntityManager em = getEntityManager();
            Class<Truck> truckEntityClass = getEntityClass();
            String classSimpleName = truckEntityClass.getSimpleName();

            Query query = em
                    .createQuery(
                            "SELECT t FROM "
                                    + classSimpleName
                                    + " t WHERE status = :status AND assignedDeliveryOrder IS NULL AND"
                                    + "  cargoCapacity >= :capacity",
                            truckEntityClass);
            query.setParameter("status", TruckStatus.OK);
            query.setParameter("capacity", minCargoCapacity);

            List<Truck> resultList = query.getResultList();
            Set<Truck> resultSet = new HashSet<Truck>(resultList);

            return resultSet;
        } catch (Exception e) {
            LOG.warn("Failed to find entity " + getEntityClass() + " by minCargoCapacity = "
                    + minCargoCapacity + ". Exception msg: " + e.getMessage());
            throw new DaoException(DaoExceptionCode.SEARCH_FAILED);
        }
    }

}
