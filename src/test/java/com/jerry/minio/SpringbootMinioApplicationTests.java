package com.jerry.minio;

import com.jerry.minio.util.MinioUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class SpringbootMinioApplicationTests {

    @Autowired
    private MinioUtils minioUtils;

    @Test
    void contextLoads() throws Exception {
        minioUtils.createBucket("public-bucket");
        minioUtils.uploadFile("public-bucket","background",new FileInputStream("background.jpg"),"image/jpeg");
        //boolean b = minioUtils.bucketExists("123123");
        //System.out.println("api返回结果为: "+b);
    }


}
