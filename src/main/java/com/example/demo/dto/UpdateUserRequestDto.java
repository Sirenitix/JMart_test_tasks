package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequestDto {

    String name;
    String job;

}
