package cpmusic.com.crowdplay.adapters;

import android.content.Context;
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

/**
 * Created by Jonas R. Hartogsohn on 17-05-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MySearchViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;
    Context mContext;

    DatabaseReference mTracksRef;


    public SearchAdapter(Context context, List<Track> data, DatabaseReference root) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;

        mTracksRef = root.child("-KkS3kuJdL4tOH_zDjCR").child("Tracks");
    }

    @Override
    public MySearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
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


    public void addTrack(ArrayList<Track> newTracks){
        mData = newTracks;
        notifyDataSetChanged();
    }



    class MySearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView artist;
        TextView votes;
        ImageView imgThumb;
        int position;
        Track current;
        FloatingActionButton fabVote;

        public MySearchViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            artist       = (TextView)  itemView.findViewById(R.id.tvArtist);
            votes       = (TextView) itemView.findViewById(R.id.tvVotes);
            imgThumb    = (ImageView) itemView.findViewById(R.id.img_album);
            fabVote     = (FloatingActionButton) itemView.findViewById(R.id.fabUpvote);
        }

        public void setData(Track current, int position) {
            this.title.setText(current.Title);
            this.artist.setText(current.Artist);
            this.votes.setText(Integer.toString(current.Votes));
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
            mTracksRef.child(current.Title).setValue(current);
            Toast.makeText(mContext, current.Title + " Added to playlist", Toast.LENGTH_SHORT).show();
        }
    }
}
