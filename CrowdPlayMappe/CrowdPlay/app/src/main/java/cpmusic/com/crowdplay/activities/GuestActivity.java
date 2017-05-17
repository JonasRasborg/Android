package cpmusic.com.crowdplay.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.util.APIConnector;
import cpmusic.com.crowdplay.util.NetworkChecker;

public class GuestActivity extends AppCompatActivity {

    EditText editSearch;
    FloatingActionButton fabSearch;
    ListView listView;

    NetworkChecker networkChecker;
    APIConnector apiConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        networkChecker = new NetworkChecker();
        apiConnector = new APIConnector();

        editSearch = (EditText)findViewById(R.id.editSearch);
        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);
        listView = (ListView)findViewById(R.id.listView);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkChecker.getNetworkStatus(this))
                {
                    apiConnector.Search(editSearch.getText().toString(), this);
                }
            }
        });
    }
}
