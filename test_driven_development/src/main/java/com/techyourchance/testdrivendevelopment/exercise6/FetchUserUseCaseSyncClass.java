package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserUseCaseSyncClass implements FetchUserUseCaseSync {

    public static final String USERNAME = "username";
    private final FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private final UsersCache mUsersCache;

    public FetchUserUseCaseSyncClass(FetchUserHttpEndpointSync fetchUserHttpEndpointSync, UsersCache usersCache) {
        this.mFetchUserHttpEndpointSync = fetchUserHttpEndpointSync;
        this.mUsersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {

        EndpointResult endpointResult;
        try {
            endpointResult = mFetchUserHttpEndpointSync.fetchUserSync(userId);
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }
        switch(endpointResult.getStatus()){
            case SUCCESS:
                User user = mUsersCache.getUser(userId);
                if(user == null){
                    user = new User(userId, USERNAME);
                    mUsersCache.cacheUser(user);
                }
                return new UseCaseResult(Status.SUCCESS, user);
            case GENERAL_ERROR:
            case AUTH_ERROR:
                return new UseCaseResult(Status.FAILURE, null);
        }

        return new UseCaseResult(Status.FAILURE, null);

    }
}
