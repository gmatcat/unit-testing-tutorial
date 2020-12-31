package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.example9.AddToCartUseCaseSync;
import com.techyourchance.testdrivendevelopment.example9.networking.CartItemScheme;
import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import static com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentCaptor.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserUseCaseSyncClassTest {
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";

    // region constants

    // endregion constants

    // region helper fields

    @Mock
    FetchUserHttpEndpointSync fetchUserHttpEndpointSyncMock;

    @Mock
    UsersCache usersCacheMock;

    // enregion helper fields

    FetchUserUseCaseSyncClass SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchUserUseCaseSyncClass(fetchUserHttpEndpointSyncMock, usersCacheMock);
        success();
    }

    // fetchUserSync userId passed Success

    @Test
    //methodName_Input_Output
    public void fetchUserSync_passUserId_httpEndpointSyncPassed() throws Exception {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.fetchUserSync(USER_ID);
        //Assert
        verify(fetchUserHttpEndpointSyncMock).fetchUserSync(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    //methodName_Input_Output
    public void fetchUserSync_authError_failureReturned() throws NetworkErrorException {
        //Arrange
        authError();
        //Act
        UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.FAILURE));

    }

    @Test
    //methodName_Input_Output
    public void fetchUserSync_generalError_failureReturned() throws NetworkErrorException {
        //Arrange
        generalError();
        //Act
        UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.FAILURE));
    }

    @Test
    //methodName_Input_Output
    public void fetchUserSync_networkError_failureReturned() throws NetworkErrorException {
        //Arrange
        networkError();
        //Act
        UseCaseResult useCaseResult = SUT.fetchUserSync(USER_ID);
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.NETWORK_ERROR));
    }

    @Test
    //methodName_Input_Output
    public void fetchUserSync_passUserId_userCached() {
        //Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        //Act
        SUT.fetchUserSync(USER_ID);
        //Assert
        verify(usersCacheMock).cacheUser(ac.capture());
        User user = ac.getValue();
        assertThat(user.getUserId(), is(USER_ID));
    }

    // region helper methods

    private void authError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.AUTH_ERROR, "", ""));
    }

    private void generalError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.GENERAL_ERROR, "", ""));
    }

    private void networkError() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenThrow(new NetworkErrorException());
    }

    private void success() throws NetworkErrorException {
        when(fetchUserHttpEndpointSyncMock.fetchUserSync(anyString()))
                .thenReturn(new FetchUserHttpEndpointSync.EndpointResult(FetchUserHttpEndpointSync.EndpointStatus.SUCCESS, USER_ID, USERNAME));
    }


    //endregion helper methods

    //region helper classes


    //endregion helper classes
}