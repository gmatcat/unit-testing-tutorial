package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;

import static com.techyourchance.testdrivendevelopment.exercise7.GetReputationSyncUseCase.GetReputationSyncUseCaseResult.*;

public class GetReputationSyncUseCase {

    GetReputationHttpEndpointSync getReputationHttpEndpointSync;


    private static final int REPUTATION = 1;
    private static final int DEFAULT_REPUTATION = 0;

    public GetReputationSyncUseCase(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public UseCaseResult getReputationSync() {
        EndpointResult endpointResult = getReputationHttpEndpointSync.getReputationSync();
        switch(endpointResult.getStatus()){
            case SUCCESS:
                return new UseCaseResult(Status.SUCCESS, REPUTATION);
            case GENERAL_ERROR:
            case NETWORK_ERROR:
                return new UseCaseResult(Status.FAILURE, DEFAULT_REPUTATION);
            default:
                throw new RuntimeException("invalid endpoint result");

        }
    }

    public interface GetReputationSyncUseCaseResult {

        enum Status {FAILURE, SUCCESS}

        class UseCaseResult {
            private final Status status;
            private final int reputation;

            public UseCaseResult(Status status, int reputation) {
                this.status = status;
                this.reputation = reputation;
            }

            public Status getStatus() {
                return status;
            }

            public int getReputation() {
                return reputation;
            }
        }
    }
}
