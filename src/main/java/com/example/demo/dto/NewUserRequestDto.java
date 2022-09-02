package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUserRequestDto {

    String name;
    String job;

}
