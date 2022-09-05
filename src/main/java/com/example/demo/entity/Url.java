package com.example.demo.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "reqres")
@Configuration("reqresProperties")
@Data
public class Url {

    private String usersApi;

    private String registrationBaseUrl;

    private String usersApiWithOffset;

    private String loginApi;

    private String usersApiWithOffsetAndLimit;
}
