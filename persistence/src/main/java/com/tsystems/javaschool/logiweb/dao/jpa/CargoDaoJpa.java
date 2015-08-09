package com.tsystems.javaschool.logiweb.dao.jpa;

import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.entities.Cargo;

/**
 * CRUD operations for Cargo entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
@Component("cargoDao")
public class CargoDaoJpa extends GenericDaoJpa<Cargo> implements CargoDao {

}
