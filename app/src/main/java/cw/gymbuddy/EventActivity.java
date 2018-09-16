package cw.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EventActivity extends AppCompatActivity {

    TextView placeNameText;
    TextView placeNameAddress;
    WebView attribtionText;
    TextView usrF;
    EditText description;
    EditText date;
    EditText time;
    Button getPlaceButton;
    Button host;
    private final static int PLACE_PICKER_REQUEST =1;
    private final static LatLngBounds bounds = new LatLngBounds(new LatLng(51.5152192,-0.1321900), new LatLng(51.5166013,-0.1299262));
    DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_event );
        databaseUser = FirebaseDatabase.getInstance().getReference("events");
        description =(EditText)findViewById( R.id.descr );
        date =(EditText)findViewById( R.id.date );
        date.addTextChangedListener(mDateEntryWatcher);
        time =(EditText)findViewById( R.id.timeS);
        placeNameText = (TextView) findViewById( R.id.tvPlaceName );
        placeNameAddress = (TextView) findViewById( R.id.tvPlaceAddress );
        attribtionText = (WebView) findViewById( R.id.wvAttribution );
        getPlaceButton = (Button) findViewById( R.id.btnGetPlace );
        usrF = (TextView)  findViewById( R.id.usrF );
        usrF.setText( UsersResource.getInstance().username );
        host = (Button) findViewById( R.id.btnHost );
        host.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
            }
        } );
        getPlaceButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(bounds);
                try {
                    Intent intent = builder.build(EventActivity.this );
                    startActivityForResult( intent, PLACE_PICKER_REQUEST );
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        } );



    }
    private TextWatcher mDateEntryWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            System.out.println("SSS"+s);
            System.out.println("START"+start);
            System.out.println("BEFORE"+before);
            System.out.println("COUNT"+count);
            String errorMessage = "Enter a valid date DD/MM/YYYY";
            String working = s.toString();
            String[] workingArray = working.split("/");
            String day = null;
            String month = null;
            String years= null;
            boolean isValid = true;
            if(workingArray.length >=1)
            {
                day = workingArray[0];
                System.out.println("Current day"+day);
                if(day.toCharArray().length >2)
                {
                    isValid = false;
                    errorMessage = day+" is not a valid day";
                }



            }
            if(workingArray.length >=2)
            {
                month = workingArray[1];
                if(month.toCharArray().length >2)
                {
                    isValid = false;
                    errorMessage = month+" is not a valid month";
                }

            }
            if(workingArray.length >= 3)
            {
                years = workingArray[2];
            }




            if( s.length() == 2 && day != null ) {

                if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31)  {
                    isValid = false;
                    errorMessage = day+" is not a valid day";
                } else {
                    if(before != 1) {
                        working += "/";
                        date.setText(working);
                        date.setSelection(working.length());
                    }

                }
            }


            if(s.length() == 5 && month != null) {
                if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
                    isValid = false;
                    errorMessage = month+" is not a valid month";
                } else {
                    if(before != 1) {
                        working += "/";
                        date.setText(working);
                        date.setSelection(working.length());
                    }
                }

            }
            if(years != null) {
                System.out.println("GOT in");
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                System.out.println(currentYear);
                if (Integer.parseInt(years) <= currentYear) {
                    errorMessage = years+" is not a valid year";
                    isValid = false;
                }
            }

            if (!isValid) {
                date.setError(errorMessage);
            } else {
                date.setError(null);

            }

        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    };



        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace( EventActivity.this, data );
                placeNameText.setText( place.getName() );
                placeNameAddress.setText( place.getAddress() );
                if(place.getAttributions()== null){
                    attribtionText.loadData( "no attribution","textt/html; charset=utf-8", "UFT-8"  );
                } else {
                    attribtionText.loadData( place.getAttributions().toString(),"textt/html; charset=utf-8", "UFT-8"  );

                }
            }
        }
    }

    public void addEvent(){

        String username = UsersResource.getInstance().username;
        String descrEvent = description.getText().toString().trim();
        String dateSt = date.getText().toString().trim();
        String timeSt = time.getText().toString().trim();
        String place = placeNameText.getText().toString().trim();
        String addr = placeNameAddress.getText().toString().trim();

        Events evento = new Events(  username, descrEvent, dateSt,timeSt, place, addr );



        if (descrEvent.isEmpty() || dateSt.isEmpty() || timeSt.isEmpty()){
            Toast.makeText( this, "Please insert all fields!", Toast.LENGTH_LONG ).show();
        }else {
            String id=  databaseUser.push().getKey();
                    databaseUser.child( id ).setValue( evento );
            Toast.makeText( this, "Event created!", Toast.LENGTH_LONG ).show();
            Intent i = new Intent( EventActivity.this , ScrollingActivity.class);
            startActivity( i );

        }
    }
}
