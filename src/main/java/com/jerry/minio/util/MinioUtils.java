package com.jerry.minio.util;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author jerry
 * @Description minio工具类
 * @Date 2022-01-08 17:02
 * @Version 1.0
 **/
@Slf4j
@Component
public class MinioUtils {

    @Autowired
    private MinioClient minioClient;

    private static final int EXPIRY_TIME = 7 * 24 * 3600;

    /**
     * 检查桶是否存在
     *
     * @param bucketName 桶名
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建桶
     *
     * @param bucketName 桶名
     * @return
     */
    @SneakyThrows
    public boolean createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        }
        return false;
    }

    /**
     * 获取所有桶
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> getBucketList() {
        return minioClient.listBuckets();
    }

    /**
     * 删除桶
     *
     * @param bucketName 桶名
     * @return
     */
    @SneakyThrows
    public boolean deleteBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            //先判断桶里面是否还有文件
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                //有文件则不删除
                if (item.size() > 0) {
                    return false;
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return bucketExists(bucketName);
        }
        return false;
    }

    /**
     * 列出桶里面所有文件名
     *
     * @param bucketName
     * @return
     */
    @SneakyThrows
    public List<String> objectNameList(String bucketName) {
        List<String> objectNameList = new ArrayList<>();
        if (bucketExists(bucketName)) {
            //先判断桶里面是否还有文件
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                //有文件则返回
                if (item.size() > 0) {
                    objectNameList.add(item.objectName());
                }
            }
        }
        return objectNameList;
    }

    /**
     * 文件上传(MultipartFile)
     *
     * @param bucketName 桶名
     * @param file       spring文件流
     * @param fileName   文件名
     * @return
     */
    @SneakyThrows
    public boolean uploadFile(String bucketName, MultipartFile file, String fileName) {
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).contentType(file.getContentType()).stream(file.getInputStream(), file.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE).build());
        log.info("上传文件返回的信息为: {}", objectWriteResponse.etag());
        return true;
    }

    /**
     * 文件上传(InputStream)
     *
     * @param bucketName
     * @param fileName
     * @param inputStream
     * @param contentType
     * @return
     */
    @SneakyThrows
    public boolean uploadFile(String bucketName, String fileName, InputStream inputStream, String contentType) {
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).contentType(contentType).stream(inputStream, -1, PutObjectOptions.MIN_MULTIPART_SIZE).build());
        log.info("上传文件返回的信息为: {}", objectWriteResponse.etag());
        return true;
    }
}
