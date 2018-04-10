package com.bhnayak.messageapplication;

import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class DownloadedImages {

    private HashMap<String,Drawable> mDownloadedImages;
    private static DownloadedImages sInstance = null;

    private DownloadedImages()
    {
        mDownloadedImages = new HashMap<String, Drawable>();
    }

    public static DownloadedImages getInstance()
    {
        if( sInstance == null )
        {
            sInstance = new DownloadedImages();
        }
        return sInstance;
    }

    public boolean contains( String url )
    {
        return mDownloadedImages.containsKey(url);
    }

    public void add( String url, Drawable drawable )
    {
        mDownloadedImages.put(url, drawable );
    }

    public Drawable get(String url) {
        return mDownloadedImages.get(url);
    }
}
