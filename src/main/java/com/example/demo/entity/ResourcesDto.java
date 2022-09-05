package com.example.demo.entity;

import com.example.demo.entity.Resource;
import com.example.demo.entity.Support;
import lombok.Data;

import java.util.List;

@Data
public class ResourcesDto {
    Integer page;
    Integer per_page;
    Integer total;
    Integer total_pages;
    List<Resource> data;
}
