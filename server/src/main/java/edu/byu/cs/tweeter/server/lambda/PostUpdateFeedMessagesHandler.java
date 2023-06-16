package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.dynamoDAO.FactoryDynamoDAO;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        StatusService statusService = new StatusService(new FactoryDynamoDAO());
        for (SQSEvent.SQSMessage sqsMessage : sqsEvent.getRecords()) {
            Gson gson = new Gson();
            Status status = gson.fromJson(sqsMessage.getBody(), Status.class);
            statusService.postFollowersBatch(status);
        }
        return null;
    }
}
