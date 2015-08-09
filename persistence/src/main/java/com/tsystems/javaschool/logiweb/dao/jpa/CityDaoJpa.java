package com.tsystems.javaschool.logiweb.dao.jpa;

import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.entities.City;

/**
 * CRUD operations for City entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
@Component
public class CityDaoJpa extends GenericDaoJpa<City> implements CityDao {

}
