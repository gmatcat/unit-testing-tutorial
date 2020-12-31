package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.example11.FetchCartItemsUseCase;
import com.techyourchance.testdrivendevelopment.example11.cart.CartItem;
import com.techyourchance.testdrivendevelopment.example11.networking.CartItemSchema;
import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

class FetchContactsUseCase {

    GetContactsHttpEndpoint mGetContactsHttpEndpoint;

    private final List<Listener> mListeners = new ArrayList<>();

    public FetchContactsUseCase(GetContactsHttpEndpoint mGetContactsHttpEndpoint) {
        this.mGetContactsHttpEndpoint = mGetContactsHttpEndpoint;
    }

    public void fetchContactsAndNotify(String filterTerm) {
        mGetContactsHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contacts) {
                notifySucceeded(contacts);
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                notifyFailed();
            }
        });
    }

    private void notifyFailed() {
        for (Listener listener : mListeners) {
            listener.onFetchContactsFailed();
        }
    }

    private void notifySucceeded(List<ContactSchema> contacts) {
        for (Listener listener : mListeners) {
            listener.onContactsFetched(contactsFromSchemas(contacts));
        }
    }

    private List<Contact> contactsFromSchemas(List<ContactSchema> contactsSchemas) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactSchema schema : contactsSchemas) {
            contacts.add(new Contact(
                    schema.getId(),
                    schema.getFullName(),
                    schema.getImageUrl()
            ));
        }
        return contacts;
    }


    public void registerListener(Listener mListener) {
        mListeners.add(mListener);
    }

    public void unregisterListener(Listener mListener) {
        mListeners.remove(mListener);
    }

    public interface Listener {
        void onContactsFetched(List<Contact> capture);

        void onFetchContactsFailed();
    }
}
