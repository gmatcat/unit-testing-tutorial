package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.GetCartItemsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint.Callback;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {
    public static final String FILTER_TERM = "filter_term";
    public static final String ID = "id";
    public static final String FULL_NAME = "full_name";
    public static final String IMAGE_URL = "image_url";
    public static final int AGE = 0;
    public static final String FULL_PHONE_NUMBER = "full_phone_number";

    // region constants

    // endregion constants

    // region helper fields

    @Mock
    GetContactsHttpEndpoint mGetContactsHttpEndpointMock;
    @Mock
    FetchContactsUseCase.Listener mListenerMock1;
    @Mock
    FetchContactsUseCase.Listener mListenerMock2;
    @Captor
    ArgumentCaptor<List<Contact>> mAcContacts;

    // enregion helper fields

    FetchContactsUseCase SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchContactsUseCase(mGetContactsHttpEndpointMock);
        success();
    }

    @Test
    //methodName_Input_Output
    public void fetchContactsAndNotify_correctFilterPassed() {
        //Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        //Act
        SUT.fetchContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mGetContactsHttpEndpointMock).getContacts(ac.capture(), any(Callback.class));
        assertThat(ac.getValue(), is(FILTER_TERM));
    }


    @Test
    //methodName_Input_Output
    public void fetchContactsAndNotify_success_observersNotifiedWithCorrectData() {
        //Arrange
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mListenerMock1).onContactsFetched(mAcContacts.capture());
        verify(mListenerMock2).onContactsFetched(mAcContacts.capture());
        List<List<Contact>> captures = mAcContacts.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);
        assertThat(capture1, is(getContacts()));
        assertThat(capture2, is(getContacts()));
    }

    @Test
    //methodName_Input_Output
    public void fetchContactsAndNotify_success_unscubscribedObserversNotified() {
        //Arrange
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock1);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        //Assert
        verifyNoMoreInteractions(mListenerMock1);
        verify(mListenerMock2).onContactsFetched(mAcContacts.capture());
    }

    @Test
    //methodName_Input_Output
    public void fetchContactsAndNotify_generalError_observersNotifiedOfFailure() {
        //Arrange
        generalError();
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mListenerMock1).onFetchContactsFailed();
        verify(mListenerMock2).onFetchContactsFailed();
    }

    @Test
    //methodName_Input_Output
    public void fetchContactsAndNotify_networkError_observersNotifiedOfFailure() {
        //Arrange
        networkError();
        //Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContactsAndNotify(FILTER_TERM);
        //Assert
        verify(mListenerMock1).onFetchContactsFailed();
        verify(mListenerMock2).onFetchContactsFailed();
    }


    // region helper methods


    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }

    private List<ContactSchema> getContactsSchemes() {
        List<ContactSchema> contacts = new ArrayList<>();
        contacts.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
        return contacts;
    }


    private void generalError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
                return null;
            }
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(Callback.class));
    }

    private void networkError() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
                return null;
            }
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(Callback.class));
    }


    private void success() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                Callback callback = (Callback) args[1];
                callback.onGetContactsSucceeded(getContactsSchemes());
                return null;
            }
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(Callback.class));
    }


    //endregion helper methods

    //region helper classes

    //endregion helper classes
}