package com.bhnayak.messageapplication;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class FetchMessagesAsyncTask extends AsyncTask<Void, Void, ArrayList<Message>> {
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static final String URL = "http://message-list.appspot.com/messages?limit=25&pageToken=";
    private static String PAGE_TOKEN = "";
    private IFetchMessagesCompletionHandler mCompletionHandler = null;

    public void setCompletionHandler( IFetchMessagesCompletionHandler completionHandler )
    {
        mCompletionHandler = completionHandler;
    }

    @Override
    protected ArrayList<Message> doInBackground(Void... params) {
        String strUrl = URL + PAGE_TOKEN;
        ArrayList<Message> messages = null;
        try {
            //Create a URL object holding our url
            URL url = new URL(strUrl);
            //Create a connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            messages = parseInputStream( streamReader );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    /*{
        “count”: number of messages,
        'messages': {
            'id': integer message ID,
            'author': {
                'name': name of the message author,
                'photoUrl': photo of the message author,
            },
            'updated': UTC timestamp of the messages creation time,
            'content': message content,
        },
        'pageToken': a continuation token for the next page of messages
    }*/
    private ArrayList<Message> parseInputStream(InputStreamReader streamReader) {
        JsonReader jsonReader = new JsonReader(streamReader);
        ArrayList<Message> messages = null;
        try
        {
            jsonReader.beginObject();
            int messageCount = 0;
            while( jsonReader.hasNext() )
            {
                String name = jsonReader.nextName();
                switch (name) {
                    case "count":
                        messageCount = jsonReader.nextInt();
                        break;
                    case "messages":
                        messages = readMessages(jsonReader, messageCount);
                        break;
                    case "pageToken":
                        PAGE_TOKEN = jsonReader.nextString();
                        break;
                }
            }
            jsonReader.endObject();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        finally {
            //Close our Json reader
            try {
                jsonReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return messages;
    }

    private ArrayList<Message> readMessages(JsonReader jsonReader, int count) {
        ArrayList<Message> messages = new ArrayList<Message>();
        try
        {
            jsonReader.beginArray();
            while( jsonReader.hasNext())
            {
                jsonReader.beginObject();
                Message message = new Message();
                while( jsonReader.hasNext())
                {
                    String name = jsonReader.nextName();
                    switch (name) {
                        case "updated":
                            message.setTimeStamp(jsonReader.nextString());
                            break;
                        case "content":
                            message.setContent(jsonReader.nextString());
                            break;
                        case "author":
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String authorObjectName = jsonReader.nextName();
                                switch (authorObjectName) {
                                    case "name":
                                        message.setPersonName(jsonReader.nextString());
                                        break;
                                    case "photoUrl":
                                        message.setImageUrl(jsonReader.nextString());
                                        break;
                                    default:
                                        jsonReader.skipValue();
                                        break;
                                }
                            }
                            jsonReader.endObject();
                            break;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }
                jsonReader.endObject();
                messages.add(message);
            }
            jsonReader.endArray();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return messages;
    }

    protected void onPostExecute(ArrayList<Message> result) {
        super.onPostExecute(result);
        if( mCompletionHandler != null )
        {
            mCompletionHandler.onCompleted( result );
        }
    }
}
