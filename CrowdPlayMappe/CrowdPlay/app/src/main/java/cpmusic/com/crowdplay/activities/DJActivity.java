package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.services.MusicPlayerService;

public class DJActivity extends AppCompatActivity {

    MusicPlayerService musicPlayerService;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        //serviceIntent = new Intent(MainActivity)


    }
}
