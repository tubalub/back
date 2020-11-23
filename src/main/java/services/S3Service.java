package services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Service
public class S3Service {

    public static final String BUCKET_NAME = "tubalub";
    public static final Region S3_REGION = Region.US_EAST_1;
    public static final String BUCKET_PATH = "/";

    private static S3Client s3 = S3Client.builder().region(S3_REGION).credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();

    public String putObject(String objKey, byte[] file) {
        try {
            PutObjectResponse response = s3.putObject(PutObjectRequest.builder().bucket(BUCKET_NAME).key(BUCKET_PATH+objKey).build(), RequestBody.fromBytes(file));
            GetUrlRequest urlRequest = GetUrlRequest.builder().bucket(BUCKET_NAME).key(BUCKET_PATH+objKey).region(S3_REGION).build();

            return s3.utilities().getUrl(urlRequest).toExternalForm();
        } catch (S3Exception e) {
            // TODO log
            return null;
        }
    }

    public boolean deleteObject(String objKey) {
        try {
            DeleteObjectRequest req = DeleteObjectRequest.builder().key(BUCKET_PATH+objKey).bucket(BUCKET_NAME).build();
            DeleteObjectResponse resp = s3.deleteObject(req);
            if (resp.sdkHttpResponse().statusCode() / 100 == 2) {
                return true;
            }
        } catch (S3Exception e) {
            return false;
        }
        return false;
    }


}
