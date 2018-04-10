package com.bhnayak.messageapplication;

import java.util.ArrayList;

public interface IFetchMessagesCompletionHandler {
    void onCompleted(ArrayList<Message> messages);
}
