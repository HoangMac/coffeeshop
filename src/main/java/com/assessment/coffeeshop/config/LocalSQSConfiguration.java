package com.assessment.coffeeshop.config;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@Profile({"local"})
public class LocalSQSConfiguration {

  @Value("${cloud.aws.sqs.endpoint}")
  private String endpoint;

  @Value("${cloud.aws.credentials.access-key:test}")
  private String accessKey;

  @Value("${cloud.aws.credentials.secret-key:test}")
  private String secretKey;

  @Value("${cloud.aws.sqs.region}")
  private String region;

  @Bean
  public SqsTemplate sqsTemplate() {
    return SqsTemplate.builder()
        .sqsAsyncClient(sqsAsyncClient())
        .build();
  }

  @Bean
  public SqsAsyncClient sqsAsyncClient() {
    return SqsAsyncClient.builder()
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create(accessKey, secretKey)))
        .region(Region.of(region))
        .endpointOverride(URI.create(endpoint))
        .build();
  }
}
