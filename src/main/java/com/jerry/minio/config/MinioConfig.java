package com.jerry.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author jerry
 * @Description minio配置类
 * @Date 2022-01-08 16:44
 * @Version 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    /**
     * 服务器url
     */
    private String endpoint;
    /**
     * 端口号
     */
    private int port;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名
     */
    private String bucketName;

    /**
     * http或者https 默认https
     */
    private boolean secure;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(endpoint, port, secure).credentials(accessKey, secretKey).build();
    }
}
