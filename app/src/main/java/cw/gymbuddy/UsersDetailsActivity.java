package cw.gymbuddy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class UsersDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    FirebaseStorage fstorage;
    StorageReference mStorageRef;
    ImageButton chat;
    Users u;
    TextView usrField;
    EditText usrDescription;
    private MapView mapview;
    private GoogleMap map;
    DatabaseReference databaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_users_details );
        mapview= findViewById(R.id.map);
        mapview.onCreate( savedInstanceState );
        mapview.getMapAsync( this );
        viewPager = findViewById( R.id.view_pager );
        usrField= findViewById( R.id.usrField );
        usrDescription = (EditText)findViewById( R.id.usrDescription );
        usrDescription.setEnabled(false);
        adapter = new CustomSwipeAdapter( this );
        fstorage = FirebaseStorage.getInstance();
        mStorageRef = fstorage.getReference("images");
        chat= findViewById( R.id.chat );

       if (UsersResource.getInstance().username.equals( ScrollingActivity.lastClicked.getUsername() )){
            chat.setVisibility( View.INVISIBLE );
        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (UsersDetailsActivity.this, ChatInterface.class );
                UsersDetailsActivity.this.startActivity( i );

            }
        });

        final File localFile;
        usrField.setText( ScrollingActivity.lastClicked.getUsername() );
        usrDescription.setText ( ScrollingActivity.lastClicked.description);
        System.out.println(ScrollingActivity.lastClicked.getUsername());
        adapter.addToArray( ScrollingActivity.lastClicked.getImg() );


        viewPager.setAdapter( adapter );

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("From map");
        map = googleMap;

        LatLng loc = new LatLng(ScrollingActivity.lastClicked.getLatitude(), ScrollingActivity.lastClicked.getLongtitude());
// 293949#23939
        String []address = ScrollingActivity.lastClicked.getAddress().split(",");
        map.addMarker(new MarkerOptions().position(loc).title(address[1])).showInfoWindow();
        map.moveCamera( CameraUpdateFactory.newLatLngZoom(loc, 13));



    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapview.onLowMemory();
        super.onLowMemory();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {



            finish();
            // Close activity
            Intent i = new Intent( UsersDetailsActivity.this, LoginActivity.class );
            this.startActivity(i);


        }

        if (item.getItemId() == R.id.deleteprofile){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            databaseUser = FirebaseDatabase.getInstance().getReference("users");
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        Users users = appleSnapshot.getValue(Users.class);
                        if (ScrollingActivity.lastClicked.username.equals( users.getUsername() )) {
                            System.out.println(ScrollingActivity.lastClicked.id);
                            System.out.println(dataSnapshot.getKey().toString());
                            appleSnapshot.getRef().removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });








            finish();
            // Close activity
            Intent intent = new Intent( UsersDetailsActivity.this, LoginActivity.class );
            this.startActivity(intent);

        }

        return true;
    }




}
