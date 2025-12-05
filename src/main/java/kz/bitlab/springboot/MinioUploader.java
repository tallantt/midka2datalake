package kz.bitlab.springboot;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MinioUploader {
    private final MinioClient minioClient;
    private final String bucketName;

    public MinioUploader(String endpoint, String accessKey, String secretKey, String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    public void uploadSamples() throws Exception {
        ensureBucket();
        uploadText();
        uploadPng();
        uploadJson();
    }

    private void ensureBucket() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    private void uploadText() throws Exception {
        byte[] content = "Hello MinIO from Java".getBytes(StandardCharsets.UTF_8);
        putBytes("file.txt", content, "text/plain");
    }



    private void putBytes(String objectName, byte[] data, String contentType) throws Exception {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .contentType(contentType)
                            .stream(stream, data.length, -1)
                            .build()
            );
        }
    }
}

