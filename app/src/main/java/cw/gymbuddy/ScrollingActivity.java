package cw.gymbuddy;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    ImageButton login_button;
    ImageButton profile;
    ImageButton hostact;
    private String loggedInAs;
    private Location loc;
    private ProgressBar mProgressView;
    private GridLayout mLoginFormView;
    private ImageButton first;
    private ScrollView scroll;
    FirebaseStorage fstorage;
    private StorageReference mStorageRef;
    DatabaseReference databaseUser;
    LocationDistance ld;


    Users mp = null;



    public static Users  lastClicked ;
    public  static ArrayList<Bitmap> image = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        LocationService.getInstance(this).askForLocationPermision();


        ld = new LocationDistance( this );

        login_button  = (ImageButton) findViewById(R.id.login_button);
        mProgressView = findViewById(R.id.login_progress2);
        mLoginFormView = findViewById(R.id.containerGrid);
        UsersResource.getInstance().getList().clear();
        databaseUser = FirebaseDatabase.getInstance().getReference("users");


   //     LocationDistance.distance(ld.getLatitude(), ld.getLongtitude(), loc.getAltitude(), loc.getLongitude() );




        UsersResource.getInstance().getList().sort( new Comparator<Users>() {
            @Override
            public int compare(Users o1, Users o2) {
                return (int)(o1.getDistance()- o2.getDistance());
            }
        } );






        databaseUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot userSnapShot: dataSnapshot.getChildren()){
                    Users users = userSnapShot.getValue(Users.class);

                    System.out.println(users.getUsername() );
                    System.out.println("this is the id "+userSnapShot.getKey());
                    System.out.println("this is the jiiiiiiii id "+ users.id);

                    users.id = userSnapShot.getKey();



                    UsersResource.getInstance( ).addToList( users );


                    System.out.println("lets try "+UsersResource.getInstance().getList().get( 0 ).id);
                        try {
                            if(!users.getUsername().equals( UsersResource.getInstance().username ) ){



                                CreateCardView( users );

                            }else{
                                mp = users;
                                System.out.println("this is fifijfi " + mp.username);
                                System.out.println("this is blablabla " + mp.id);

                                try {
                                    if (mp != null) {
                                        for (int i = 0; i < 3; i++) {
                                            final File localFile = File.createTempFile( "photoName" + i, "jpg" );
                                            StorageReference riversRef = mStorageRef.child( "photos/users/" + mp.getUsername() + "/picture" + i );
                                            riversRef.getFile( localFile )
                                                    .addOnSuccessListener( new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                                            localFile.getAbsolutePath();
                                                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                                            Bitmap bitmap = BitmapFactory.decodeFile( localFile.getAbsolutePath(), bmOptions );
                                                            //bitmap = Bitmap.createScaledBitmap(bitmap,Name.getWidth(),Name.getHeight(),true);
                                                            mp.addToarray( bitmap );
                                                            int nh = (int) (bitmap.getHeight() * (512 / bitmap.getWidth()));
                                                            // Name.setLayoutParams(new LinearLayout.LayoutParams(500,nh));
                                                            BitmapDrawable bdrawable = new BitmapDrawable( ScrollingActivity.this.getResources(), bitmap );
                                                            // Name.setBackground(bdrawable);

                                                        }
                                                    } ).addOnFailureListener( new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle failed download
                                                    // ...
                                                }
                                            } );
                                        }
                                        UsersResource.getInstance().addToList( mp );

                                    }


                                }catch (Exception e){

                                }
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        } );


        System.out.println("the array is  " +UsersResource.getInstance().getList().size());
        mLoginFormView.removeAllViews();
        profile = findViewById(R.id.profile);
        login_button.setOnClickListener(this);
        profile.setOnClickListener(this);
        scroll = findViewById(R.id.scrol);
        scroll.setOnTouchListener(this);
        hostact = findViewById( R.id.hostact );
        hostact.setOnClickListener( this );

        showProgress(false);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            mLoginFormView.removeAllViews();
            UsersResource.getInstance().getList().clear();
            finish();
            // Close activity
            Intent i = new Intent( ScrollingActivity.this, LoginActivity.class );
            this.startActivity(i);}

            if(item.getItemId() == R.id.events) {

                Intent e = new Intent( ScrollingActivity.this, ListEvents.class );
                this.startActivity(e);

            }
        if(item.getItemId() == R.id.weather) {

            Intent e = new Intent( ScrollingActivity.this, TimerActivity.class );
            this.startActivity(e);

        }
            if (item.getItemId() == R.id.profile){

                Intent i = new Intent( ScrollingActivity.this, ProfileActivity.class );
                this.startActivity(i);


        }

            if(item.getItemId() == R.id.deleteprofile) {
                System.out.println("now");

                String userId;
                userId =   UsersResource.getInstance().username;

                databaseUser.child("users").child(userId).removeValue();
                System.out.println("User removed");

                finish();
                // Close activity
                Intent intent = new Intent( ScrollingActivity.this, LoginActivity.class );
                this.startActivity(intent);


            }



        return true;
    }





    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_button)
        {
            lastClicked = mp;

            Intent intent = new Intent(ScrollingActivity.this,UsersDetailsActivity.class);
            ScrollingActivity.this.startActivity(intent);


        }
        if (v.getId() == R.id.profile){
            Toast.makeText(getApplicationContext(), "Step Counter started", Toast.LENGTH_SHORT).show();

//            Intent intent = new Intent( ScrollingActivity.this, FitnessStep.class );
//            this.startActivity(intent);
        }


        if(v.getId() == R.id.hostact)
        {
            Intent intent = new Intent(ScrollingActivity.this,EventActivity.class);

            ScrollingActivity.this.startActivity(intent);

        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        System.out.println("sad"+requestCode);
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println("Granted");
                    LocationService.getInstance(this).getLastKnowLocation();
                    WritingPermision();


                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void WritingPermision()
    {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

                )

        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE )|| ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                System.out.println("Should not req");
                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        124);
                System.out.println("Should req");

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }



        }
        else
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    124);
            System.out.println("Should req ff");

        }

    }




    public void CreateCardView(final Users u) throws IOException

    {
        CardView card = new CardView(this);

        CardView.LayoutParams params = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.MATCH_PARENT
        );

        params.setMargins(10,30,10,30);
       // params.width = 500;
        //params.height = 500;
        card.setLayoutParams (new CardView.LayoutParams(700,650, 500 ));


        mLoginFormView = findViewById(R.id.containerGrid);
        card.setRadius(9);

        card.setCardElevation(9);
        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout lin = new LinearLayout(this);
        lin.setOrientation(LinearLayout.VERTICAL);
        lin.setLayoutParams(params);

        final  ImageButton Name = new ImageButton(this);
       Name. setImageDrawable(getResources().getDrawable(
               R.drawable.webuser));


        Name.setScaleType( ImageView.ScaleType.MATRIX);



        mLoginFormView.bringToFront();
        //  Name.setBackground(bdrawable);
        Name.setAdjustViewBounds(true);


        //Name.setImageBitmap(i.getImage().get(0));
        lin.bringToFront();
        mLoginFormView.addView(card);

        card.setElevation(15);
        System.out.println("Hello?");

        //Name.setImageBitmap( scaled);
        TextView username = new TextView(this);
        username.setText(u.getUsername());



        card.addView(lin);
        lin.addView(Name);
        lin.addView(username);

        System.out.println("Hello?");

        fstorage = FirebaseStorage.getInstance();
        mStorageRef = fstorage.getReference("images");

        for (int i = 0; i <3; i++){
        final File localFile = File.createTempFile("photoName" +i , "jpg");
        StorageReference riversRef = mStorageRef.child("photos/users/" + u.getUsername() +  "/picture"+ i);
        riversRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        localFile.getAbsolutePath();
                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath(),bmOptions);
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.nophoto);


                        //bitmap = Bitmap.createScaledBitmap(bitmap,Name.getWidth(),Name.getHeight(),true);
                        u.addToarray( bitmap );
                        int nh = (int) ( bitmap.getHeight() * (512 /bitmap.getWidth()) );
                        Name.setLayoutParams(new LinearLayout.LayoutParams(500,500));





                        BitmapDrawable bdrawable = new BitmapDrawable(ScrollingActivity.this.getResources(),bitmap);
                        Name.setImageDrawable(bdrawable);




                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });}




          Name.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                lastClicked = u;
                Intent i = new Intent( ScrollingActivity.this, UsersDetailsActivity.class );
                        ScrollingActivity.this.startActivity( i );

            }
     });







    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {

                break;
            }
            case MotionEvent.ACTION_UP:
            {

                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                System.out.println(event.getX());

                break;
            }
        }
        return true;
    }
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2) ;
        System.out.println("pro"+ distance);

        return Math.sqrt(distance);
    }
}
