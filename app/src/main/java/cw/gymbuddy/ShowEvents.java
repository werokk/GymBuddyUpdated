package cw.gymbuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class ShowEvents extends AppCompatActivity {

    TextView nome;
    TextView descrizione;
    TextView datap;
    TextView ora;
    TextView nomeposto;
    TextView indirizzo;
    DatabaseReference databaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_events );
        nome = findViewById( R.id.nome );
        descrizione = findViewById( R.id.descrizione );
        datap = findViewById( R.id.data );
        ora = findViewById( R.id.ora );
        nomeposto = findViewById( R.id.nomePosto );
        indirizzo = findViewById( R.id.indirizzo );
        nome.setText( ListEvents.lastClicked.getUsername() );
        descrizione.setText( ListEvents.lastClicked.getDescription() );
        datap.setText( ListEvents.lastClicked.getDate() );
        ora.setText( ListEvents.lastClicked.getTime() );
        nomeposto.setText(ListEvents.lastClicked.getNameLoc());
        indirizzo.setText( ListEvents.lastClicked.getAddrLoc() );

    }
}
