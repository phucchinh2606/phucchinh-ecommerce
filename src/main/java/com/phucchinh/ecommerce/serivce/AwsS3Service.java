package com.phucchinh.ecommerce.serivce;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class AwsS3Service {

    private final String bucketName = "phucchin-ecommerce";

    @Value("${aws.s3.access}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secret}")
    private String awsS3SecretKey;

    public String saveImageToS3(MultipartFile photo){
        try {


            String s3FileName = photo.getOriginalFilename();

            //create aes credentials using access and secret key
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey,awsS3SecretKey);

            //create an s3 client with config credentials and region
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_SOUTHEAST_2)
                    .build();

            //get input stream from photo
            InputStream inputStream = photo.getInputStream();

            //set metadata for the object
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());

            //create a put request to upload the img to s3
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,s3FileName,inputStream,metadata);
            s3Client.putObject(putObjectRequest);

            return "https://"+bucketName+".s3.ap-southeast-2.amazonaws.com/"+s3FileName;

        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Unable to upload img to s3 bucket: "+e.getMessage());
        }
    }
}
