package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;

import static com.techyourchance.testdrivendevelopment.exercise7.GetReputationSyncUseCase.*;
import static com.techyourchance.testdrivendevelopment.exercise7.GetReputationSyncUseCase.GetReputationSyncUseCaseResult.*;
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
public class GetReputationSyncUseCaseTest {

    // region constants

    // endregion constants

    // region helper fields
    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;

    // enregion helper fields

    GetReputationSyncUseCase SUT;

    private static final int REPUTATION = 1;
    private static final int DEFAULT_REPUTATION = 0;

    @Before
    public void setup() throws Exception {
        SUT = new GetReputationSyncUseCase(getReputationHttpEndpointSyncMock);
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);

    }

    @Test
    //methodName_Input_Output
    public void getReputationSync_success_successReturned() {
        //Arrange
        success();
        //Act
        UseCaseResult useCaseResult = SUT.getReputationSync();
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.SUCCESS));
    }

    @Test
    public void getReputationSync_success_reputationReturned() {
        //Arrange
        success();
        //Act
        UseCaseResult useCaseResult = SUT.getReputationSync();
        //Assert
        assertThat(useCaseResult.getReputation(), is(REPUTATION));
    }


    @Test
    //methodName_Input_Output
    public void getReputationSync_generalError_failureReturned() {
        //Arrange
        generalError();
        //Act
        UseCaseResult useCaseResult = SUT.getReputationSync();
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.FAILURE));
    }

    @Test
    //methodName_Input_Output
    public void getReputationSync_networkError_failureReturned() {
        //Arrange
        networkError();
        //Act
        UseCaseResult useCaseResult = SUT.getReputationSync();
        //Assert
        assertThat(useCaseResult.getStatus(), is(Status.FAILURE));
    }


    // region helper methods
    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.GENERAL_ERROR, DEFAULT_REPUTATION));
    }


    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.NETWORK_ERROR, DEFAULT_REPUTATION));
    }

    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new GetReputationHttpEndpointSync.EndpointResult(GetReputationHttpEndpointSync.EndpointStatus.SUCCESS, REPUTATION));
    }

    //endregion helper methods

    //region helper classes

    //endregion helper classes
}