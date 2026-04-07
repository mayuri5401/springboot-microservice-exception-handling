package com.UserService.model;

import jakarta.persistence.*;


//✅ Auto‑creates SQL table
//✅ ORM mapping


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String name;

    public long getId () {
        return id;
    }

    public void setId (long id) {
        this.id = id;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }


}

