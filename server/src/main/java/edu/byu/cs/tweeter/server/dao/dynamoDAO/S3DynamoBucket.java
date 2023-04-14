package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Base64;

import edu.byu.cs.tweeter.server.dao.ImageDAOInterface;

public class S3DynamoBucket implements ImageDAOInterface {
    private final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().withRegion(
            "us-east-1").build();
    private static final String BUCKET_URL = "https://[replace-to-bucket-name].s3.us-east-1.amazonaws.com/"; // TODO: FIX THIS THING
    private static final String BUCKET_NAME = "[replace-to-bucket-name]"; // TODO: Replace bucket name

    @Override
    public String storeImage(String image, String userAlias) {
        try {
            if (!amazonS3.doesBucketExistV2(BUCKET_URL)) {
                return null;
            }
            byte[] decode = Base64.getDecoder().decode(image);
            ObjectMetadata metadata = new ObjectMetadata();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
            metadata.setContentLength(decode.length);
            metadata.setContentType("image/jpeg");
            PutObjectRequest request = new PutObjectRequest(BUCKET_URL, userAlias, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);
            return BUCKET_URL + userAlias;
        } catch (SdkClientException e) {
            e.printStackTrace();
            return null;
        }
    }
}
