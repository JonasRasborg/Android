package cpmusic.com.crowdplay.Fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.SearchAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.APIConnector;
import cpmusic.com.crowdplay.util.NetworkChecker;


public class SearchFragment extends Fragment
{


    EditText editSearch;
    FloatingActionButton fabSearch;

    NetworkChecker networkChecker;
    APIConnector apiConnector;

    SearchAdapter adapter;

    RecyclerView recyclerView;

    ArrayList<Track> tracks;

    FirebaseDatabase database;
    DatabaseReference mPartyRef;

    String partyID;

    View view;
    Context mContext;
    Activity mActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search,container,false);

        mContext = getActivity();
        mActivity = getActivity();


        partyID = getArguments().getString("PartyKey");


        networkChecker = new NetworkChecker();
        apiConnector = new APIConnector(mContext);

        editSearch = (EditText)view.findViewById(R.id.editSearch);
        fabSearch = (FloatingActionButton)view.findViewById(R.id.fabSearch);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiveFromService, new IntentFilter("SearchData"));

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search();
            }
        });

        tracks = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mPartyRef = database.getReference().child(partyID);

        setUpRecyclerView();

        return view;
    }


    private BroadcastReceiver mReceiveFromService = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            tracks = (ArrayList<Track>) intent.getExtras().getSerializable("tracks");
            adapter.addTrack(tracks);
        }
    };

    public void Search()
    {
        if(networkChecker.getNetworkStatus(mContext))
        {
            apiConnector.Search(editSearch.getText().toString(), mContext);
        }

        InputMethodManager inputManager = (InputMethodManager) mActivity.getSystemService(mContext.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new SearchAdapter(mContext, tracks, mPartyRef, mActivity);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(mContext); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);
    }
}
