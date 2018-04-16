package com.bhnayak.messageapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import java.util.ArrayList;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

public class MessageList extends RecyclerView {
    private LinearLayoutManager mLayoutManager;
    ItemTouchHelper mItemTouchHelper;
    private MessageListAdapter mMessageListAdapter;
    private boolean mRequestInProgress = false;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mMessageListAdapter.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mRequestInProgress) {
                if (firstVisibleItemPosition >= 0 && totalItemCount - (visibleItemCount + firstVisibleItemPosition) <=  getThreshold()) {
                    requestMessages();
                }
            }
        }
    };
    private IFetchMessagesCompletionHandler mFetchMessageCompletionHandler = new IFetchMessagesCompletionHandler() {
        @Override
        public void onCompleted(ArrayList<Message> messages) {
            if( messages != null && !messages.isEmpty() )
            {
                addMessages(messages);
            }
            mRequestInProgress = false;
        }
    };
    private ItemTouchHelper.SimpleCallback mSwipeCallback = new ItemTouchHelper.SimpleCallback( 0,  ItemTouchHelper.RIGHT ) {
        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            mMessageListAdapter.remove( viewHolder.getAdapterPosition() );
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if( actionState == ACTION_STATE_SWIPE ) {
                int totalWidth = viewHolder.itemView.getWidth();
                float alpha = totalWidth != 0 ? 1 - dX / totalWidth : 1;
                viewHolder.itemView.setAlpha(alpha);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public MessageList(Context context) {
        super(context);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init()
    {
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.setLayoutManager(mLayoutManager);
        this.setItemAnimator(new DefaultItemAnimator());
        mMessageListAdapter = new MessageListAdapter( getContext(), new ArrayList<Message>());
        this.setAdapter(mMessageListAdapter);
        this.addOnScrollListener(mOnScrollListener);
        mItemTouchHelper = new ItemTouchHelper(mSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    public void addMessages(ArrayList<Message> messages) {
        mMessageListAdapter.add(messages);
    }

    private void requestMessages()
    {
        try {
            FetchMessagesAsyncTask fetchMessagesAsyncTask = new FetchMessagesAsyncTask();
            fetchMessagesAsyncTask.setCompletionHandler(mFetchMessageCompletionHandler);
            fetchMessagesAsyncTask.execute();
            mRequestInProgress = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getThreshold() {
        return 20;
    }
}
