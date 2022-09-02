package com.example.demo.entity;

import lombok.Data;

import java.util.List;


@Data
public class UsersDto {

    Integer page;
    Integer per_page;
    Integer total;
    Integer total_pages;
    List<User> data;
}
