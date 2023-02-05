package ru.z3r0ing.s3viewer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.z3r0ing.s3viewer.dto.S3Credentials;
import ru.z3r0ing.s3viewer.dto.S3Item;
import ru.z3r0ing.s3viewer.service.S3Service;
import ru.z3r0ing.s3viewer.util.AWSSDKUtil;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Scope("session")
public class S3ServiceImpl implements S3Service {

    private S3Credentials s3Credentials;
    private S3Client currentS3Client = null;

    @Override
    public boolean setS3Credentials(S3Credentials s3Credentials) {
        this.currentS3Client = null;
        this.s3Credentials = s3Credentials;
        return checkConnection();
    }

    @Override
    public List<S3Item> getFolderItems(String folderPath) {
        List<S3Item> s3ItemList = new ArrayList<>();
        ListObjectsRequest listObjectsRequest;
        if (!folderPath.isEmpty()) {
            listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(s3Credentials.getBucket())
                    .prefix(folderPath)
                    .delimiter("/")
                    .build();
        } else {
            listObjectsRequest = ListObjectsRequest.builder()
                    .bucket(s3Credentials.getBucket())
                    .delimiter("/")
                    .build();
        }
        ListObjectsResponse objects = getS3Client().listObjects(listObjectsRequest);
        objects.commonPrefixes().forEach(commonPrefix -> {
            s3ItemList.add(new S3Item(commonPrefix.prefix(), false));
        });
        objects.contents().forEach(s3Object -> {
            s3ItemList.add(new S3Item(s3Object.key(), true));
        });
        return s3ItemList;
    }

    @Override
    public boolean checkConnection() {
        try {
            ListBucketsResponse bucketList = getS3Client()
                    .listBuckets();
            return bucketList != null && bucketList.buckets() != null;
        } catch (SdkException e) {
            currentS3Client = null;
            log.error("Error with S3 connection", e);
            return false;
        }
    }

    private S3Client getS3Client() {
        if (currentS3Client == null) {
            currentS3Client = AWSSDKUtil.buildS3Client(s3Credentials);
        }
        return currentS3Client;
    }

}
