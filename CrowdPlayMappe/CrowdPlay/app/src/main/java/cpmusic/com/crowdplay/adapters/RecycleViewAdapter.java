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

import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Track;

/**
 * Created by rrask on 19-05-2017.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;
    Context mContext;
    DatabaseReference mTracksRef;

    public RecycleViewAdapter(Context context, List<Track> data, DatabaseReference root) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
        mTracksRef = root.child("Tracks");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("RecyclerAdapter","onBindViewHolder "+position);
        Track current = mData.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAVote(Track track)
    {
        for (int i = 0; i<mData.size()-1;i++)
        {
            if (mData.get(i).URI == track.URI)
            {
                notifyItemChanged(i);
                checkPositions(i);
            }
        }
    }

    public Track getTopTrack(){
        return mData.get(0);
    }

    public void checkPositions(int position){
        for (int i = position; i > 0; i--){
            if(mData.get(i).Votes>mData.get(i-1).Votes){
                Track a = mData.get(i);
                Track b = mData.get(i-1);
                mData.set(i-1,a);
                mData.set(i,b);
                notifyItemMoved(i,i-1);
                notifyItemChanged(i-1);
                notifyItemChanged(i);
            }
        }
    }

    public void addTrack(Track newTrack){
        mData.add(newTrack);
        notifyDataSetChanged();
    }




    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView artist;
        TextView votes;
        ImageView imgThumb;
        int position;
        Track current;
        FloatingActionButton fabVote;

        public MyViewHolder(View itemView) {
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
            fabVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, current.Title + " Upvoted", Toast.LENGTH_SHORT).show();
                    current.Votes++;
                    mTracksRef.child(current.URI).child("Votes").setValue(current.Votes);
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
