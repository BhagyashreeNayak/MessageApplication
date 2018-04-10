package com.bhnayak.messageapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListItemHolder>
{
    private static String LOG_TAG = "MessageListAdapter";
    private Context mContext;
    private ArrayList<Message> mMessages;

    MessageListAdapter(Context context, ArrayList<Message> messages) {
        this.mContext = context;
        this.mMessages = messages;
    }

    @NonNull
    @Override
    public MessageListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_layout, parent, false);
        return new MessageListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListItemHolder holder, int position) {
        Message message = mMessages.get(position);
        holder.setProperties( message );
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void add( ArrayList<Message> messages )
    {
        mMessages.addAll( messages );
        notifyItemRangeInserted( mMessages.size() - messages.size(), mMessages.size() - 1 );
    }

    public void remove(int position) {
        mMessages.remove(position);
        notifyItemRemoved(position);
    }
}
