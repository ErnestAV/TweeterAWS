package edu.byu.cs.tweeter.server.dao.dynamoDAO;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQS {
    private static AmazonSQS amazonSQS = AmazonSQSClientBuilder.defaultClient();

    public static void postStatusQueueHandler(String postStatusMessage) {
        String sqsUrl = "https://sqs.us-east-1.amazonaws.com/546546156748/PostStatusQueue";

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsUrl)
                .withMessageBody(postStatusMessage);

        amazonSQS.sendMessage(sendMessageRequest);
    }

    public static void updateFeedQueueHandler(String feedMessage) {
        String sqsUrl = "https://sqs.us-east-1.amazonaws.com/546546156748/UpdateFeedQueue";

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(sqsUrl)
                .withMessageBody(feedMessage);

        amazonSQS.sendMessage(sendMessageRequest);
    }
}
