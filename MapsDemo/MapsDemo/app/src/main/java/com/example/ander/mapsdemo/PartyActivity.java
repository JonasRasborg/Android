package com.example.ander.mapsdemo;

import android.content.Intent;
import android.icu.text.MessagePattern;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PartyActivity extends AppCompatActivity {

    TextView partyTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        // Find Controls
        partyTextview = (TextView) findViewById(R.id.textViewParty);

        // Handle Intent
        Intent incomingIntent = getIntent();
        String partytitle = incomingIntent.getStringExtra("PartyTitle");
        partyTextview.setText(partytitle);
    }
}
