package com.tsystems.javaschool.logiweb.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Custom user model for Spring Security.
 * 
 * @author Andrew Baliushin
 *
 */
public class UserModel extends User{

    public UserModel(String username, String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    
}
