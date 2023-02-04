package ru.z3r0ing.s3viewer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class S3Credentials implements Serializable {
    private static final long serialVersionUID = 7485584536540574442L;

    @NonNull
    String accessKey;

    @NonNull
    String secretAccessKey;

    @NonNull
    String regionName;

    String endpointUrl;

    @NonNull
    String bucket;

    @NonNull
    Boolean pathStyleAccessEnabled;
}
