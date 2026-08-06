package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class NotificationService {

    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public NotificationService() {
        this.sqsClient = SqsClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    public void notifyTaskCreated(Task task) {
        String message = "Task created: " + task.getId() + " - " + task.getTitle();
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .build());
        System.out.println("SQS message sent: " + message);
    }
}