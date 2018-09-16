package cw.gymbuddy.service;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import cw.gymbuddy.data.Channel;

public class YahooWeatherService {
    private WeatherServiceCallback callBack;
    String location;
    private Exception error;


    public YahooWeatherService(WeatherServiceCallback callBack) {
        this.callBack = callBack;
    }
    public String getLocation() {
        return location;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(final String location){

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                String YQL = String.format( "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%S\")", location );
                String endpoint = String.format( "http://query.yahooapis.com/v1/public/streaming/yql?q=%S&format=json", Uri.encode(YQL) );
                try {
                    URL url = new URL(endpoint);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream =connection.getInputStream();
                    BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );
                    StringBuilder result = new StringBuilder(  );
                    String line;
                    while((line = reader.readLine())!= null){
                        result.append( line );
                    }
                    return reader.toString();
                } catch (Exception e){
                    error = e;
                }
                return null;

            }


                @Override
                protected void onPostExecute(String s) {
                    if (s == null && error != null){
                        callBack.serviceFailure( error );
                        return;
                    }
                    try {
                        JSONObject data = new JSONObject( s );
                        JSONObject queryResult = new JSONObject( "query" );
                        int count = queryResult.optInt( "count" );
                        if (count == 0) {
                            callBack.serviceFailure(new LocationWeatherException( "no location information found for "+ location ) );
                        }

                        Channel channel = new Channel();
                        channel.populate( queryResult.optJSONObject("results000"  ) );
                    }catch(Exception e){

                    }
                }
            }.execute( location );

    }
    public class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super( detailMessage );
        }
    }

}
