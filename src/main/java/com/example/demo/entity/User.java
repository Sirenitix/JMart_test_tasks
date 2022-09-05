package com.example.demo.entity;


import lombok.Data;

@Data
public class User implements Comparable<User> {

    Long id;
    String email;
    String first_name;
    String last_name;
    String avatar;

    @Override
    public int compareTo(User o)
    {
        return this.getId().compareTo( o.getId() );
    }

}
