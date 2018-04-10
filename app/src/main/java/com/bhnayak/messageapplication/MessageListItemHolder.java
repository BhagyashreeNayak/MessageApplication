package com.bhnayak.messageapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MessageListItemHolder extends RecyclerView.ViewHolder {

    private static final String URL_PREFIX = "http://message-list.appspot.com";
    private ImageView mPersonImage;
    private TextView mPersonNameText;
    private TextView mTimeStampText;
    private TextView mMessageContentText;
    MessageListItemHolder(View itemView) {
        super(itemView);
        mPersonImage = itemView.findViewById( R.id.person_icon );
        mPersonNameText = itemView.findViewById( R.id.personName );
        mTimeStampText = itemView.findViewById(R.id.timestamp);
        mMessageContentText = itemView.findViewById(R.id.messageContent);
    }

    public ImageView getPersonImage() {
        return mPersonImage;
    }

    public TextView getPersonNameText() {
        return mPersonNameText;
    }

    public TextView getTimeStampText() {
        return mTimeStampText;
    }

    public TextView getMessageContentText() {
        return mMessageContentText;
    }

    public void setProperties(Message message) {
        if( message == null )
            return;
        mPersonNameText.setText(message.getPersonName());
        mTimeStampText.setText(message.getTimeStamp());
        mMessageContentText.setText(message.getContent());
        String url = URL_PREFIX + message.getImageUrl();
        if( DownloadedImages.getInstance().contains(url))
        {
            mPersonImage.setImageDrawable(DownloadedImages.getInstance().get(url));
        }
        else
        {
            new DownloadImageTask().execute(new ImageViewWraper(url, mPersonImage));
        }
    }

    private class ImageViewWraper
    {
        String imageUrl;
        ImageView view;

        ImageViewWraper(String url, ImageView view) {
            this.imageUrl = url;
            this.view = view;
        }
    }
    private static class DownloadImageTask extends AsyncTask<ImageViewWraper, Void, Bitmap> {

        private ImageViewWraper mImageViewWraper;
        @Override
        protected Bitmap doInBackground(ImageViewWraper... params) {
            try {
                if(params[0] == null )
                    return null;
                mImageViewWraper = params[0];
                URL url = new URL(params[0].imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result!=null)
            {
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(mImageViewWraper.view.getResources(), result);
                drawable.setCircular(true);
                mImageViewWraper.view.setImageDrawable(drawable);
                DownloadedImages.getInstance().add(mImageViewWraper.imageUrl,drawable);
            }
        }

    }
}
