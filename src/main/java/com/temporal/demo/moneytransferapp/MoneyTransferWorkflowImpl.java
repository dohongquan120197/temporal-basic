package com.temporal.demo.moneytransferapp;

import com.temporal.demo.Response;
import com.temporal.demo.common.Constants;
import com.temporal.demo.request.TransferRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

// money-transfer-project-template-java-workflow-implementation
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {
    // RetryOptions specify how to automatically handle retries when Activities fail.
//    private final RetryOptions retryoptions = RetryOptions.newBuilder()
//            .setInitialInterval(Duration.ofSeconds(1))
//            .setMaximumInterval(Duration.ofSeconds(100))
//            .setBackoffCoefficient(2)
//            .setMaximumAttempts(500)
//            .build();
    private final ActivityOptions options = ActivityOptions.newBuilder()
            // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            .setStartToCloseTimeout(Duration.ofSeconds(5))
            // Optionally provide customized RetryOptions.
            // Temporal retries failures by default, this is simply an example.
            .build();
    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private final AccountActivity account = Workflow.newActivityStub(AccountActivity.class, options);

    // The transfer method is the entry point to the Workflow.
    // Activity method executions can be orchestrated here or from within other Activity methods.
    @Override
    public Response transfer(TransferRequest transferRequest) {
        Response valid = account.validate(transferRequest);
        if (!valid.getCode().equals(Constants.STATUS.ERROR)) {
            return Response
                    .builder()
                    .code(Constants.STATUS.ERROR)
                    .message("Valid fail")
                    .build();
        }
        Response transfer = account.transfer(transferRequest);
        if (!transfer.getCode().equals(Constants.STATUS.ERROR)) {
            return Response
                    .builder()
                    .code(Constants.STATUS.ERROR)
                    .message("transfer fail")
                    .build();
        }
        Response deposit = account.deposit(transferRequest);
        if (!deposit.getCode().equals(Constants.STATUS.ERROR)) {
            return Response
                    .builder()
                    .code(Constants.STATUS.ERROR)
                    .message("transfer fail")
                    .build();
        }

        return Response
                .builder()
                .code(Constants.STATUS.SUCCESS)
                .message("Transaction successfully!")
                .build();

    }
}
