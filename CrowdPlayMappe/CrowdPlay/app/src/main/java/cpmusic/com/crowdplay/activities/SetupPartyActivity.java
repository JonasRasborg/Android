package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.PartyAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Location;
import cpmusic.com.crowdplay.model.firebaseModel.Party;
import cpmusic.com.crowdplay.util.SharedPreferencesData;

public class SetupPartyActivity extends AppCompatActivity {

    EditText edtPartyName, edtPartyCode;
    Button btnStartParty;
    private Bundle bundle;
    Intent intentDJ;
    ArrayList<Party> partyList;
    RecyclerView recyclerView;

    String facebookID;

    SharedPreferencesData sharedPreferencesData;

    DatabaseReference mRoot;
    FirebaseDatabase database;

    // RecykleView

    PartyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_party);

        database = FirebaseDatabase.getInstance();
        mRoot = database.getReference();
        partyList = new ArrayList<>();



        sharedPreferencesData = new SharedPreferencesData();

        facebookID = sharedPreferencesData.getFacebookUID(SetupPartyActivity.this);


        intentDJ = new Intent(this, DJActivity.class);
        bundle = getIntent().getParcelableExtra("bundle");
        mRoot = FirebaseDatabase.getInstance().getReference();
        edtPartyName = (EditText)findViewById(R.id.edtPartyName);
        edtPartyCode = (EditText)findViewById(R.id.edtPartyCode);
        btnStartParty = (Button)findViewById(R.id.btnStartParty);




        btnStartParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtPartyName.getText().toString();
                LatLng latLong = bundle.getParcelable("Location");
                String password = edtPartyCode.getText().toString();
                String partyKey = UUID.randomUUID().toString();

                Location newLatLng = new Location(latLong.latitude,latLong.longitude);
                Party newParty = new Party(name,password,newLatLng, facebookID, partyKey);


                mRoot.child(partyKey).setValue(newParty);

                //mRoot.push().setValue(newParty);

                intentDJ.putExtra("PartyKey",partyKey);


                startActivity(intentDJ);
            }
        });

        mRoot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){

                    Party p = dataSnapshot.child(d.getKey()).getValue(Party.class);
                    if(p.userID.equals(sharedPreferencesData.getFacebookUID(SetupPartyActivity.this))){
                        adapter.addParty(p);
                        Log.d("SetupPartyActivity","party from this DJ found");
                        Toast.makeText(SetupPartyActivity.this,"You have ongoing Parties!",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        setUpRecyclerView();



    }

    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.PartyRecyclerView);
        adapter = new PartyAdapter(this);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

    }

}
