package cw.gymbuddy;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cw.gymbuddy.data.Channel;
import cw.gymbuddy.data.Item;
import cw.gymbuddy.service.WeatherServiceCallback;
import cw.gymbuddy.service.YahooWeatherService;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private YahooWeatherService service;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_weather );

        weatherIconImageView = (ImageView) findViewById( R.id.weatherIconTextView );
        temperatureTextView = (TextView)findViewById( R.id.temperatureTextView );
        conditionTextView = (TextView)findViewById( R.id.conditionTextView );
        locationTextView = (TextView)findViewById( R.id.locationTextView );



        service = new YahooWeatherService( this );

        dialog = new ProgressDialog( this );
        dialog.setMessage( "Loading..." );
        dialog.show();
        service.refreshWeather( "United States, AK" );
    }

    @Override
    public void serviceSuccess(Channel channel) {

        dialog.hide();
        Item item = channel.getItem();
        int resourceId = getResources().getIdentifier( "drawable/icon_"+ channel.getItem().getCondition().getCode(),null, getPackageName() );

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable( resourceId );
        weatherIconImageView.setImageDrawable( weatherIconDrawable );
        conditionTextView.setText( item.getCondition().getDescription() );
        temperatureTextView.setText( item.getCondition().getTemperature()+"\u00B0 "+ channel.getUnits().getTemperature() );
        locationTextView.setText(service.getLocation()  );
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(),Toast.LENGTH_LONG).show();
        System.out.println(exception);
    }
}
