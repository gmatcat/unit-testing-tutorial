package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent;
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USER_ID = "user_id";
    public static final String FULL_NAME = "Full Name";
    public static final String IMAGE_URL = "Image Url";

    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;
    private FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    //Success User Sync

    @Test
    public void fetchUserProfileSync_success_passedUserIdToEndpoint() throws Exception {
        SUT.fetchUserProfileSync(USER_ID);
        assertThat(userProfileHttpEndpointSyncTd.mUserId, is(USER_ID));
    }

    //Failed AuthError User Sync

    @Test
    public void fetchUserProfileSync_authErrorFailed_userNotCached() throws Exception {
        userProfileHttpEndpointSyncTd.mIsAuthError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    //Failed ServerError User Sync

    @Test
    public void fetchUserProfileSync_serverErrorFailed_userNotCached() throws Exception {
        userProfileHttpEndpointSyncTd.mIsServerError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    //Failed GeneralError User Sync

    @Test
    public void fetchUserProfileSync_generalErrorFailed_userNotCached() throws Exception {
        userProfileHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }
    //Failed NetworkError User Sync

    @Test
    public void fetchUserProfileSync_networkErrorFailed_userNotCached() throws Exception {
        userProfileHttpEndpointSyncTd.mIsNetworkError = true;
        SUT.fetchUserProfileSync(USER_ID);
        assertNull(usersCacheTd.getUser(USER_ID));
    }

    //Success User Sync

    @Test
    public void fetchUserProfileSync_success_userCachedSuccess() {
        SUT.fetchUserProfileSync(USER_ID);
        User mUser = usersCacheTd.getUser(USER_ID);
        assertNotNull(mUser);
        assertThat(mUser.getUserId(), is(USER_ID));
        assertThat(mUser.getFullName(), is(FULL_NAME));
        assertThat(mUser.getImageUrl(), is(IMAGE_URL));
    }

    //Success User Sync, cached only once

    @Test
    public void fetchUserProfileSync_success_userCachedOnceSuccess() {
        SUT.fetchUserProfileSync(USER_ID);
        User mUser = usersCacheTd.getUser(USER_ID);
        int userCount = 0;
        for(User user : usersCacheTd.mUsers){
            if(user.getUserId().equals(USER_ID)){
                userCount++;
            }
        }
        assertThat(userCount, is(1));
    }

    public class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync{

        EndpointResultStatus endpointResultStatus;
        String mUserId = "";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            this.mUserId = userId;
            if (mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            } else if (mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "", "");
            }  else if (mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            } else if (mIsNetworkError) {
                throw new NetworkErrorException();
            } else {
                return new EndpointResult(EndpointResultStatus.SUCCESS,  userId, FULL_NAME, IMAGE_URL);
            }
        }
    }

    public class UsersCacheTd implements UsersCache{

        List<User> mUsers = new ArrayList<>();

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}