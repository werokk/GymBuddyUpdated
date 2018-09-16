package cw.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatInterface extends AppCompatActivity  {
    private FirebaseListAdapter<ChatMessage> adapter;

    private static final String TAG = "MyFirebaseMsgService";
    DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_interface);
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                sendMessage( input );

                // Clear the input
                input.setText("");
            }
        });

            Toast.makeText(this,
                    "Welcome " + UsersResource.getInstance().username,
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }//*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                System.out.println("clicked on" +ScrollingActivity.lastClicked.getUsername());
                System.out.println( "sent to " + model.getSentTo());
                System.out.println("this is from " + messageUser.getText().toString());
                // Set their text
                if (ScrollingActivity.lastClicked.getUsername().equals( model.getMessageUser()) && UsersResource.getInstance().username.equals( model.getSentTo()) || UsersResource.getInstance().username.equals( model.getMessageUser() )&& ScrollingActivity.lastClicked.getUsername().equals( model.getSentTo() )) {
                    messageText.setText( model.getMessageText() );
                    messageUser.setText( model.getMessageUser() );

                // Format the date before showing it
                messageTime.setText( DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));}
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {



                    finish();
                            // Close activity
                            Intent i = new Intent( ChatInterface.this, LoginActivity.class );
                            this.startActivity(i);


        }
        if(item.getItemId() == R.id.events) {

            finish();
            // Close activity
            Intent e = new Intent( ChatInterface.this, EventActivity.class );
            this.startActivity(e);

        }
        if (item.getItemId() == R.id.deleteprofile){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            databaseUser = FirebaseDatabase.getInstance().getReference("users");
            databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        System.out.println("I think this one here "+appleSnapshot.getKey());
                        System.out.println("yeeeeeeee "+ScrollingActivity.lastClicked.id);


                        if (ScrollingActivity.lastClicked.id.equals( appleSnapshot.getKey() )
                                ) {
                            System.out.println(ScrollingActivity.lastClicked.id);
                            System.out.println(dataSnapshot.getKey().toString());

                            appleSnapshot.getRef().removeValue();


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });

            System.out.println("now");







            finish();
            // Close activity
            Intent intent = new Intent( ChatInterface.this, LoginActivity.class );
            this.startActivity(intent);

        }

        return true;
    }


    public void sendMessage(View view) {
        EditText input = (EditText)findViewById(R.id.input);

        // Read the input field and push a new instance
        // of ChatMessage to the Firebase database
        FirebaseDatabase.getInstance()
                .getReference()
                .push()
                .setValue(new ChatMessage(input.getText().toString(),
                        UsersResource.getInstance().username , ScrollingActivity.lastClicked.getUsername()));

        // Clear the input
        input.setText("");
    }



}
