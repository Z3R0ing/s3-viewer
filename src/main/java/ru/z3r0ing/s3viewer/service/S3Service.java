package ru.z3r0ing.s3viewer.service;

import ru.z3r0ing.s3viewer.dto.S3Credentials;
import ru.z3r0ing.s3viewer.dto.S3Item;

import java.util.List;

public interface S3Service {

    /**
     * Set credentials for connection to S3
     * @param s3Credentials credentials
     * @return true if there are no connection problems
     */
    boolean setS3Credentials(S3Credentials s3Credentials);

    List<S3Item> getFolderItems(String folderPath);

    /**
     * Check connection with S3
     * @return true if there are no connection problems
     */
    boolean checkConnection();

}
