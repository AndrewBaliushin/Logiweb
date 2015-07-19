package com.tsystems.javaschool.logiweb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.tsystems.javaschool.logiweb.entities.status.UserRole;



/**
 * Entity representation of application user.
 * 
 * @author Andrew Baliushin
 *
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "user_mail"))
public class LogiwebUser {
    
    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private int id;

    @Column(name = "user_mail", nullable = false, unique = true)
    private String mail;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;
    
    @Column(name = "md5_pass", nullable = false)
    private String passMd5;

    public LogiwebUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassMd5() {
        return passMd5;
    }

    public void setPassMd5(String passMd5) {
        this.passMd5 = passMd5;
    }

}