package com.techyourchance.mockitofundamentals.exercise5.networking;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentCaptor.*;

@RunWith(MockitoJUnitRunner.class)
public class NetworkErrorExceptionTest {

    // region constants

    // endregion constants

    // region helper fields

    // endregion helper fields



    NetworkErrorException SUT;

    @Before
    public void setup() throws Exception {
        SUT = new NetworkErrorException();

    }

    // region helper methods

    //endregion helper methods

    //region helper classes

    //endregion helper classes
}