package com.bhnayak.messageapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private MessageList mMessageListView;
    private TextView mNoInternetText;
    private ProgressBar mProgress;
    private View.OnClickListener mNoInternetTextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestMessages();
        }
    };

    private IFetchMessagesCompletionHandler mFetchMessageCompletionHandler = new IFetchMessagesCompletionHandler() {
        @Override
        public void onCompleted(ArrayList<Message> messages) {
            mProgress.setVisibility(View.GONE);
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
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        initializeViews();
        requestMessages();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initializeViews() {
        mMessageListView = findViewById(R.id.messageList);
        mNoInternetText = findViewById(R.id.noInternet);
        mProgress = findViewById(R.id.progress);
        mMessageListView.init();
        mNoInternetText.setClickable(true);
        mNoInternetText.setOnClickListener( mNoInternetTextClickListener );
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
}
