package com.bhnayak.messageapplication;

public class Message
{
    private static String LOG_TAG = "Message";
    private String mPersonName;
    private String mTimeStamp;
    private String mImageUrl;
    private String mContent;

    public Message(String personName, String timeStamp, String imageUrl, String content) {
        this.mPersonName = personName;
        this.mTimeStamp = timeStamp;
        this.mImageUrl = imageUrl;
        this.mContent = content;
    }
    Message()
    {
    }

    public String getPersonName() {
        return mPersonName;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getContent() {
        return mContent;
    }

    public void setPersonName(String mPersonName) {
        this.mPersonName = mPersonName;
    }

    public void setTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }
}
