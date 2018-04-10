package com.bhnayak.messageapplication;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private MessageList mMessageListView;
    private TextView mNoInternetText;
    private View.OnClickListener mNoInternetTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestMessages();
        }
    };

    private IFetchMessagesCompletionHandler mFetchMessageCompletionHandler = new IFetchMessagesCompletionHandler() {
        @Override
        public void onCompleted(ArrayList<Message> messages) {
            if( messages == null || messages.isEmpty() )
            {
                mMessageListView.setVisibility(View.GONE);
                mNoInternetText.setVisibility(View.VISIBLE);
            }
            else
            {
                mMessageListView.addMessages( messages );
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initializeViews() {
        mMessageListView = findViewById(R.id.messageList);
        mNoInternetText = findViewById(R.id.noInternet);
        mMessageListView.init();
        mNoInternetText.setClickable(true);
        mNoInternetText.setOnClickListener( mNoInternetTextClickListener );

        requestMessages();

    }

    private void requestMessages() {
        try {
            FetchMessagesAsyncTask fetchMessagesAsyncTask = new FetchMessagesAsyncTask();
            fetchMessagesAsyncTask.setCompletionHandler(mFetchMessageCompletionHandler);
            fetchMessagesAsyncTask.execute();

        } catch (Exception e) {
            e.printStackTrace();
            mMessageListView.setVisibility(View.GONE);
            mNoInternetText.setVisibility(View.VISIBLE);
        }
    }

    /*private void addDummyMessages() {
        String dummyText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        mMessages.add( new Message( "Virginia Schultz", "12 minutes ago", "", dummyText));
        mMessages.add( new Message("Jesse Ramos","29 minutes ago", "", dummyText));
        mMessages.add( new Message("Carol Stone", "4 hous ago", "", dummyText ));
        mMessages.add( new Message("Zachary Bradley", "5 hours ago", "", dummyText));

    }*/

    private boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
