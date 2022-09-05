package com.example.demo.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
