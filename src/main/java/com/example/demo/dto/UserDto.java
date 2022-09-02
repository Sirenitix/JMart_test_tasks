package com.example.demo.dto;

import com.example.demo.entity.Support;
import com.example.demo.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    User data;
    Support support;
}
