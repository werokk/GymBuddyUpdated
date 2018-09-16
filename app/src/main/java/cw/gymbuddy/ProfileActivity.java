package cw.gymbuddy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 10;
    private LocationService service;
    private Button uploadbtn;
    private ImageView imgSpecimentPhoto;
    private ImageView imgSpecimentPhoto2;
    private ImageView imgSpecimentPhoto3;
    FirebaseStorage fstorage;
    DatabaseReference databaseUser;
    private StorageReference mStorageRef;
    private ProgressDialog mProgress;
    Uri photoURI;
    Bitmap cameraImage;
    TextView usrField;
    EditText descrField;
    ArrayList<Bitmap> imgs = new ArrayList<Bitmap>();
    // storage/photos/users/userID/photoName
    Users users;






    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile );
        service = new LocationService(this);
        descrField = findViewById( R.id.descrField );
        usrField = findViewById( R.id.usrsField );
        uploadbtn = (Button) findViewById(R.id.upload);
        usrField.setText( UsersResource.getInstance().username );


        //get access to the image view
        imgSpecimentPhoto = findViewById(R.id.camerabtn);
        imgSpecimentPhoto2 = findViewById(R.id.camerabtn5 );
        imgSpecimentPhoto3 = findViewById(R.id.camerabtn6);



        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Bitmap b : imgs){
                    // Upload it to our reference
                // storage/photos/users/userID/photoName
                fstorage = FirebaseStorage.getInstance();
                mStorageRef = fstorage.getReference("images");
                Uri temp = getImageUri( ProfileActivity.this, b );
                Uri file = Uri.fromFile(new File(getRealPathFromURI( temp )));
                StorageReference riversRef = mStorageRef.child("photos/users/" + UsersResource.getInstance().username +  "/picture" + imgs.indexOf( b ));
/*
                    databaseUser = FirebaseDatabase.getInstance().getReference("users" );
                    databaseUser.addValueEventListener( new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot userSnapShot: dataSnapshot.getChildren()){
                                if (userSnapShot.getKey().equals( id )){
                                    users = userSnapShot.getValue(Users.class);
                                    System.out.println(users.getUsername() );
                                    users.setId( userSnapShot.getKey() );
                                    System.out.println("this is the id "+userSnapShot.getKey());
                                    String descr = descrField.getText().toString();
                                    String id= UsersResource.getInstance().findUser( UsersResource.getInstance().username ).id;

                                    System.out.println(id);

                                    databaseUser = FirebaseDatabase.getInstance().getReference("users");


                                    users.description = descr;

                                    databaseUser.child( id ).setValue(users  );
                                }

                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    } );*/






                    Toast.makeText( ProfileActivity.this, "Images uploaded", Toast.LENGTH_SHORT ).show();


                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });}
                final String id= UsersResource.getInstance().findUser( UsersResource.getInstance().username ).id;

                users= UsersResource.getInstance().findUser( UsersResource.getInstance().username );



                users.removeArray();


                users.description =  descrField.getText().toString();

                databaseUser = FirebaseDatabase.getInstance().getReference("users");
                databaseUser.child(  id ).setValue( users  );


                Intent i = new Intent( ProfileActivity.this, ScrollingActivity.class );

                ProfileActivity.this.startActivity( i );



            }
        });
    }



    private Context mContext=ProfileActivity.this;

    private static final int REQUEST = 112;





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

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)


                 {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }
    }

    public void btnTakePhotoClicked(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        System.out.println("first");
        currentView= v;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoURI = data.getData();
        //did the user chose okay
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){

            //we are hearing back grom the camera
             cameraImage = (Bitmap) data.getExtras().get( "data" );
             imgs.add( cameraImage );

            //now we have the image
            ImageView cur = (ImageView) currentView;
            cur.setImageBitmap( cameraImage );
            if (cur.getId() == imgSpecimentPhoto.getId()) {
                imgSpecimentPhoto2.setVisibility( View.VISIBLE );
            }
            if (cur.getId() == imgSpecimentPhoto2.getId()) {
                imgSpecimentPhoto3.setVisibility( View.VISIBLE );





                                }

            }


        }}

