package com.businessrules.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.AcknowledgementOrdering;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;

import java.net.URI;
import java.time.Duration;

@Configuration
public class SqsConfig {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Value("${aws.sqs.endpoint:}")
    private String sqsEndpoint;

    @Value("${aws.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.secret-access-key:}")
    private String secretAccessKey;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        SqsAsyncClientBuilder builder = SqsAsyncClient.builder()
                .region(Region.of(awsRegion));

        // Configure for local SQS (LocalStack/ElasticMQ)
        if (sqsEndpoint != null && !sqsEndpoint.isEmpty()) {
            builder.endpointOverride(URI.create(sqsEndpoint));
            
            // Use dummy credentials for local development
            if (accessKeyId != null && !accessKeyId.isEmpty()) {
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)));
            } else {
                // Default dummy credentials for LocalStack
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")));
            }
        } else {
            // Use default AWS credentials for production
            builder.credentialsProvider(DefaultCredentialsProvider.create());
        }

        return builder.build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<?> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .configure(options -> options
                        .acknowledgementMode(AcknowledgementMode.ALWAYS)
                        .acknowledgementOrdering(AcknowledgementOrdering.ORDERED)
                        .acknowledgementInterval(Duration.ofSeconds(5))
                        .maxConcurrentMessages(10)
                        .maxMessagesPerPoll(10)
                )
                .build();
    }
}
