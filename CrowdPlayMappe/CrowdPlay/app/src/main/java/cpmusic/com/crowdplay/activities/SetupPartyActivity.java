package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.CustomLatLng;
import cpmusic.com.crowdplay.model.firebaseModel.Party;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;
import cpmusic.com.crowdplay.util.FirebaseConnector;

public class SetupPartyActivity extends AppCompatActivity {

    EditText edtPartyName, edtPartyCode;
    Button btnStartParty;
    FirebaseConnector firebaseConnector;
    DatabaseReference db;
    private Bundle bundle;
    Intent intentDJ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_party);

        intentDJ = new Intent(this, DJActivity.class);
        bundle = getIntent().getParcelableExtra("bundle");
        db = FirebaseDatabase.getInstance().getReference();
        firebaseConnector = new FirebaseConnector(db);
        edtPartyName = (EditText)findViewById(R.id.edtPartyName);
        edtPartyCode = (EditText)findViewById(R.id.edtPartyCode);
        btnStartParty = (Button)findViewById(R.id.btnStartParty);

        btnStartParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtPartyName.getText().toString();
                LatLng latLong = bundle.getParcelable("Location");
                String password = edtPartyCode.getText().toString();

                CustomLatLng newLatLng = new CustomLatLng(latLong.latitude,latLong.longitude);
                Party newParty = new Party(name,password,newLatLng);

                firebaseConnector.putNewParty(newParty);
                startActivity(intentDJ);
            }
        });
    }
}
