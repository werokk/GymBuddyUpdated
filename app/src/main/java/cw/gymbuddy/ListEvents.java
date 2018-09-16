package cw.gymbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class ListEvents extends AppCompatActivity {

    DatabaseReference databaseUser;
    private GridLayout mLoginFormView;
    public static Events  lastEvents ;
    FloatingActionButton floatingActionButton;
    Events mp = null;
    LinearLayout containerEvents;
    public static Events lastClicked;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_events );
        containerEvents = findViewById( R.id.containerEvents );
        containerEvents.removeAllViews();




        UsersResource.getInstance().getList().clear();
        databaseUser = FirebaseDatabase.getInstance().getReference("events");


        databaseUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot eventSnapShot: dataSnapshot.getChildren()){
                    Events events = eventSnapShot.getValue(Events.class);
                    EventResource.getInstance( ).addToList( events );

                    try {



                            CreateCardView( events );





                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        } );


        System.out.println("the array is  " +EventResource.getInstance().getList().size());



    }















    public void CreateCardView(final Events u) throws IOException

    {
        CardView card = new CardView(this);
        containerEvents.addView( card );



        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params1.bottomMargin = 10;
        card.setLayoutParams( params1 );



        card.setCardBackgroundColor( Color.GRAY );

        card.setRadius(9);
        card.setContentPadding(15, 15, 15, 15);

        card.setBottom(10);
        card.setFocusable( true );



        card.setCardElevation(9);

        final TextView txtDesc = new TextView( this );
        txtDesc.setLayoutParams(params);

        final TextView txtUser = new TextView( this );
        txtUser.setTextColor(Color.GREEN );



        LinearLayout lin = new LinearLayout(this);
        lin.setOrientation(LinearLayout.VERTICAL);
        card.addView( lin );
        txtUser.setText( u.getUsername() );
        lin.addView( txtUser );
        LinearLayout lino = new LinearLayout(this);
        lino.setOrientation(LinearLayout.HORIZONTAL);
        lin.addView( lino );
        txtDesc.setText( u.getDescription() );
        lino.addView( txtDesc );
        FloatingActionButton floats = new FloatingActionButton( this );
        floats.setImageResource(R.drawable.com_facebook_button_like_icon_selected);
        lino.addView( floats );



        card.setElevation(15);






        card.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastClicked = u;
                Intent i = new Intent( ListEvents.this, ShowEvents.class );
                ListEvents.this.startActivity( i );


            }
        });








    }



}
