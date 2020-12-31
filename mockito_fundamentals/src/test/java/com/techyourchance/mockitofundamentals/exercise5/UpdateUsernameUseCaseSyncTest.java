package com.techyourchance.mockitofundamentals.exercise5;

import com.techyourchance.mockitofundamentals.exercise5.eventbus.EventBusPoster;
import com.techyourchance.mockitofundamentals.exercise5.networking.NetworkErrorException;
import com.techyourchance.mockitofundamentals.exercise5.networking.UpdateUsernameHttpEndpointSync;
import com.techyourchance.mockitofundamentals.exercise5.users.User;
import com.techyourchance.mockitofundamentals.exercise5.users.UsersCache;
import static com.techyourchance.mockitofundamentals.exercise5.UpdateUsernameUseCaseSync.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UpdateUsernameUseCaseSyncTest {

    public static final String USERNAME = "username";
    public static final String USER_ID = "user_id";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    UpdateUsernameUseCaseSync SUT;

    @Mock UpdateUsernameHttpEndpointSync updateUsernameHttpEndpointSyncMock;

    @Mock UsersCache usersCacheMock;

    @Mock EventBusPoster eventBusPosterMock;


    @Before
    public void setUp() throws Exception {
        SUT = new UpdateUsernameUseCaseSync(
                updateUsernameHttpEndpointSyncMock,
                usersCacheMock,
                eventBusPosterMock);
        success();
    }

    @Test
    public void updateUsernameSync_success_userIdAndUsernamePassed() throws Exception {
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(updateUsernameHttpEndpointSyncMock).updateUsername(ac.capture(), ac.capture());
        List<String> captures = ac.getAllValues();
        assertThat(captures.get(0), is(USER_ID));
        assertThat(captures.get(1), is(USERNAME));
        verify(usersCacheMock).cacheUser(any(User.class));
    }

    @Test
    public void updateUsernameSync_success_userIdAndUsernameCached() throws Exception {
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(usersCacheMock).cacheUser(ac.capture());
        List<User> captures = ac.getAllValues();
        User user = captures.get(0);
        assertThat(user.getUserId(), is(USER_ID));
        assertThat(user.getUsername(), is(USERNAME));
    }

    @Test
    public void updateUsernameSync_authError_userNotCached() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUsernameSync_generalError_userNotCached() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }

    @Test
    public void updateUsernameSync_serverError_userNotCached() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(usersCacheMock);
    }


    @Test
    public void updateUsernameSync_success_loggedInEventPosted() throws Exception {
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verify(eventBusPosterMock).postEvent(any());
    }

    @Test
    public void updateUsernameSync_generalError_noInteractionWithEventBusPoster() throws Exception {
        generalError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_authError_noInteractionWithEventBusPoster() throws Exception {
        authError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_serverError_noInteractionWithEventBusPoster() throws Exception {
        serverError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }
    @Test
    public void updateUsernameSync_networkError_noInteractionWithEventBusPoster() throws Exception {
        networkError();
        SUT.updateUsernameSync(USER_ID, USERNAME);
        verifyNoMoreInteractions(eventBusPosterMock);
    }

    @Test
    public void updateUsernameSync_success_successReturned() throws Exception {
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void updateUsernameSync_serverError_failureReturned() throws Exception {
        serverError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_authError_failureReturned() throws Exception {
        authError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_generalError_failureReturned() throws Exception {
        generalError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void updateUsernameSync_networkError_networkErrorReturned() throws Exception {
        networkError();
        UseCaseResult result = SUT.updateUsernameSync(USER_ID, USERNAME);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private void networkError() throws Exception{
        doThrow(new NetworkErrorException()).
        when(updateUsernameHttpEndpointSyncMock).updateUsername(any(String.class), any(String.class));
    }

    private void authError()  throws Exception {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, USER_ID, USERNAME));
    }

    private void generalError()  throws Exception {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, USER_ID, USERNAME));
    }

    private void serverError()  throws Exception {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, USER_ID, USERNAME));
    }

    private void success() throws Exception {
        when(updateUsernameHttpEndpointSyncMock.updateUsername(any(String.class), any(String.class)))
                .thenReturn(new UpdateUsernameHttpEndpointSync.EndpointResult(UpdateUsernameHttpEndpointSync.EndpointResultStatus.SUCCESS, USER_ID, USERNAME));
    }

}