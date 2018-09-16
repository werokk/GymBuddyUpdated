package cw.gymbuddy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends  Activity  {
    String name , email, username, pass, address;
    double longtitude, latitude;
    EditText nameStr ;
    EditText emailStr;
    EditText usernameStr;
    EditText passStr;
    EditText pass2 ;
    Button regBtn;
    DatabaseReference databaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        LocationService.getInstance( this ).askForLocationPermision();
       databaseUser = FirebaseDatabase.getInstance().getReference("users");

         nameStr = (EditText)findViewById(R.id.name);
         emailStr = (EditText)findViewById(R.id.email);
         usernameStr = (EditText)findViewById(R.id.username);
         passStr = (EditText)findViewById(R.id.password);
         pass2 = (EditText)findViewById(R.id.repassword);
         regBtn = (Button)findViewById( R.id.regBtn );
        regBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        } );

    }


    public void addUser() {

        String name = nameStr.getText().toString().trim();
        String email = emailStr.getText().toString().trim();
        String username = usernameStr.getText().toString().trim();
        String password = passStr.getText().toString().trim();
        String password2 = pass2.getText().toString().trim();

        LocationService.getInstance( this ).getLastKnowLocation();
        Location lc = LocationService.getInstance( this ).getLoc();
        double longtitude = lc.getLongitude();
        double latitude = lc.getLatitude();
        String address = LocationService.getInstance( this ).GetAddress();

        Users user = new Users( name, email, username, password, longtitude, latitude, address, "" );





        if (!password.equals( password2 )) {
            //POP up
            Toast pass = Toast.makeText( RegisterActivity.this, "Password don't match", Toast.LENGTH_SHORT );
            pass.show();
        }
        for(Users usr: UsersResource.getInstance().getList()){
            if(usr.getUsername().equals( username )){
                Toast.makeText( this, "Username Already Existing!", Toast.LENGTH_LONG ).show();
                return;
            }

        }
        if (!TextUtils.isEmpty( name )){
            Toast.makeText( this, "Insert all fields", Toast.LENGTH_LONG ).show();

        }

    {
        String id = databaseUser.push().getKey();

        databaseUser.child( id ).setValue( user );
        Toast.makeText( this, "Registrered", Toast.LENGTH_LONG ).show();
        Intent i = new Intent( RegisterActivity.this , LoginActivity.class);
        UsersResource.getInstance().getList().clear();
        startActivity( i );

    }}




        public void onRegisterClick(View v){
        if (v.getId()==R.id.regBtn){


             name = nameStr.getText().toString();
             email = emailStr.getText().toString();
             username = usernameStr.getText().toString();
             pass = passStr.getText().toString();
             String pass2str = pass2.getText().toString();
            System.out.println("here");




        }




}
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        System.out.println("sss"+requestCode);
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("Granted");
                    LocationService.getInstance(this).getLastKnowLocation();


                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }}
