package com.jerry.minio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.jerry"})
public class SpringbootMinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMinioApplication.class, args);
    }

}
