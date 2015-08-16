package com.tsystems.javaschool.logiweb.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class DriverUserModel extends UserModel {

    private int driverLogiwebId;

    public DriverUserModel(String username, String password,
            Collection<? extends GrantedAuthority> authorities,
            int driverLogiwebId) {
        super(username, password, authorities);
        this.driverLogiwebId = driverLogiwebId;
    }

    public int getDriverLogiwebId() {
        return driverLogiwebId;
    }
}
