package com.tsystems.javaschool.logiweb.controllers.ext;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

@Component
public class CityUtils {
    
    @Autowired
    private CityService cityService;

    public Model addCitiesToModel(Model model) throws LogiwebServiceException {
        Map<Integer, City> cities = new HashMap<Integer, City>();
        for (City c : cityService.findAllCities()) {
            cities.put(c.getId(), c);
        }
        model.addAttribute("cities", cities);
        return model;
    }
}
