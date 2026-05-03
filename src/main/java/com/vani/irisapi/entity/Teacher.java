package com.vani.irisapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teacher {

    @Id
    private String username;
    private String password;

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}