package ru.z3r0ing.s3viewer.util;

import ru.z3r0ing.s3viewer.dto.S3Credentials;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

public final class AWSSDKUtil {

    private AWSSDKUtil() {}

    public static S3Client buildS3Client(S3Credentials s3Credentials) {
        AwsCredentialsProvider awsCredentialsProvider = getAwsCredentialsProvider(s3Credentials);
        if (s3Credentials.getEndpointUrl() != null && !s3Credentials.getEndpointUrl().isEmpty()) {
            return S3Client.builder()
                    .credentialsProvider(awsCredentialsProvider)
                    .endpointOverride(URI.create(s3Credentials.getEndpointUrl()))
                    .region(Region.of(s3Credentials.getRegionName()))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(s3Credentials.getPathStyleAccessEnabled())
                            .build())
                    .build();
        } else {
            return S3Client.builder()
                    .credentialsProvider(awsCredentialsProvider)
                    .region(Region.of(s3Credentials.getRegionName()))
                    .serviceConfiguration(S3Configuration.builder()
                            .pathStyleAccessEnabled(s3Credentials.getPathStyleAccessEnabled())
                            .build())
                    .build();
        }
    }

    public static AwsCredentialsProvider getAwsCredentialsProvider(S3Credentials s3Credentials) {
        AwsCredentials awsCredentials = AwsBasicCredentials.create(s3Credentials.getAccessKey(), s3Credentials.getSecretAccessKey());
        return StaticCredentialsProvider.create(awsCredentials);
    }

}
