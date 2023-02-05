package ru.z3r0ing.s3viewer.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class S3Item {

    @NonNull
    private String path;
    @NonNull
    private Boolean isFile;

    public boolean isFile() {
        return isFile;
    }

}
