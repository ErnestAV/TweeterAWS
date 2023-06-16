package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.dynamoDAO.FactoryDynamoDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeedHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        StatusService statusService = new StatusService(new FactoryDynamoDAO());
        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            statusService.pushStatusToFeed(sqsMessage.getBody());
        }
        return null;
    }
}
