package com.example.demo.entity;

import lombok.Data;

import java.util.Iterator;
import java.util.List;


@Data
public class UsersDto implements Iterable<User> {

    Integer page;
    Integer per_page;
    Integer total;
    Integer total_pages;
    List<User> data;

    @Override
    public Iterator<User> iterator() {
        return data.iterator();
    }
}
