package kz.bitlab.springboot;

public class Main {
    public static void main(String[] args) {
        String endpoint = envOrDefault("MINIO_ENDPOINT", "http://localhost:9000");
        String accessKey = envOrDefault("MINIO_ACCESS_KEY", "minioadmin");
        String secretKey = envOrDefault("MINIO_SECRET_KEY", "minioadmin");
        String bucket = envOrDefault("MINIO_BUCKET", "my-bucket-demo");

        MinioUploader uploader = new MinioUploader(endpoint, accessKey, secretKey, bucket);
        try {
            uploader.uploadSamples();
            System.out.println("Uploaded file.txt, file.png, file.json to bucket " + bucket);
        } catch (Exception e) {
            System.err.println("Failed to upload files: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}