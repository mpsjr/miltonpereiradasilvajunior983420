package br.gov.mt.seplag.lista_api.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${app.minio.url}")
    private String url;

    @Value("${app.minio.access-key}")
    private String accessKey;

    @Value("${app.minio.secret-key}")
    private String secretKey;

    @Value("${app.minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * CommandLineRunner - executa na inicialização da aplicação
     * para garantir que o Bucket existe. Recebe o client já instanciado.
     */
    @Bean
    public CommandLineRunner initBucket(MinioClient minioClient) {
        return args -> {
            try {
                boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!found) {
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    System.out.println("Bucket '" + bucketName + "' criado com sucesso.");
                } else {
                    System.out.println("Bucket '" + bucketName + "' já existe.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Erro ao conectar no MinIO: " + e.getMessage());
            }
        };
    }
}