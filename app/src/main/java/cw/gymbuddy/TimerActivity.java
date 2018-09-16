package cw.gymbuddy;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TimerActivity extends AppCompatActivity {
    Button btnStart, btnPause, btnLap;
    TextView txtTimer;
    Handler customHandler = new Handler();
    String text;
    LinearLayout container;
    long startTime= 0L, timeinMilliseconds=0L, timeSwapBuff=0L, updateTime=0L;
    Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {
            timeinMilliseconds = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeinMilliseconds;
            int secs = (int)(updateTime/1000);
            int mins = secs/60;
            secs%=60;
            int milliseconds =(int)(updateTime%1000);
            txtTimer.setText( ""+ mins+":"+String.format("%02d",secs)+":"
        +String.format( "%03d", milliseconds ));
            customHandler.postDelayed( this, 0 );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_timer );

        btnStart= (Button)findViewById( R.id.brnStart );
        btnPause= (Button)findViewById( R.id.brnPause );
        txtTimer =(TextView)findViewById( R.id.TimerValue );



        btnStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed( updateTimerThread, 0 );
            }
        } );
        btnPause.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick (View view){
                timeSwapBuff+=timeinMilliseconds;
                customHandler.removeCallbacks( updateTimerThread );
            }
        } );







    }
}
