package com.hotel.hotel.config.minio;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs; // Adicionado para a política
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucket}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        try {
            boolean foundBucket = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            
            if (!foundBucket) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' successfully created!", bucketName);

                String policyJson = """
                    {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Effect": "Allow",
                                "Principal": {"AWS": ["*"]},
                                "Action": ["s3:GetBucketLocation", "s3:GetObject"],
                                "Resource": ["arn:aws:s3:::%s/*"]
                            }
                        ]
                    }
                    """.formatted(bucketName);

                client.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build()
                );
                log.info("Public read-only policy successfully applied to bucket '{}'", bucketName);
            }
        } catch (Exception e) {
            log.error("It was not possible to verify/create/configure bucket on MinIO: " + e.getMessage());
        }

        return client;
    }
}