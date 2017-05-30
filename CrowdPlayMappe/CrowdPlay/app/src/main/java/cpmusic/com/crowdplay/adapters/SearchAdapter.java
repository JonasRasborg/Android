package cpmusic.com.crowdplay.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.SharedPreferencesConnector;

/**
 * Created by Jonas R. Hartogsohn on 17-05-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MySearchViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;
    Context mContext;

    DatabaseReference mTracksRef;
    Activity searchActivity;
    SharedPreferencesConnector sharedPreferencesConnector;
    String thisUserID;

    public SearchAdapter(Context context, List<Track> data, DatabaseReference root, Activity activity) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        mTracksRef = root.child("Tracks");
        searchActivity = activity;
        sharedPreferencesConnector = new SharedPreferencesConnector();
        thisUserID = sharedPreferencesConnector.getFacebookUID(mContext);
    }

    @Override
    public MySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_search, parent, false);
        MySearchViewHolder holder = new MySearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MySearchViewHolder holder, int position) {
        Log.i("RecyclerAdapter","onBindViewHolder "+position);
        Track current = mData.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void addTracks(ArrayList<Track> newTracks){

        if (newTracks.size() != 0)
        {
            mData = newTracks;
            notifyDataSetChanged();
        }
        else
        {
            mData.clear();
            notifyDataSetChanged();

            Toast.makeText(mContext, mContext.getString(R.string.no_search_results), Toast.LENGTH_SHORT).show();
        }

    }



    class MySearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView artist;
        ImageView imgThumb;
        int position;
        Track current;
        FloatingActionButton fabVote;

        public MySearchViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            artist       = (TextView)  itemView.findViewById(R.id.tvArtist);
            imgThumb    = (ImageView) itemView.findViewById(R.id.img_album);
            fabVote     = (FloatingActionButton) itemView.findViewById(R.id.fabSearch);
        }

        public void setData(Track current, int position) {
            this.title.setText(current.Title);
            this.artist.setText(current.Artist);
            Picasso.with(mContext).load(current.ImageURL).into(this.imgThumb);
            this.position = position;
            this.current = current;
        }

        public void setListeners(){
            fabVote.setOnClickListener(MySearchViewHolder.this);
        }

        @Override
        public void onClick(View v)
        {
            current.AddedBy = thisUserID;
            mTracksRef.child(current.URI).setValue(current);
            Toast.makeText(mContext, current.Title + " Added to playlist", Toast.LENGTH_SHORT).show();
        }
    }
}
