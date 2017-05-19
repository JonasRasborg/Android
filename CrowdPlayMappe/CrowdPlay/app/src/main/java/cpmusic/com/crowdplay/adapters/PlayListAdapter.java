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

import com.squareup.picasso.Picasso;

import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Track;

/**
 * Created by Jonas R. Hartogsohn on 19-05-2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;
    Context mContext;

    public PlaylistAdapter(Context context, List<Track> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        mContext = context;
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

    public void addAVote(int position){
        mData.get(position).Votes++;
        notifyItemChanged(position);
        checkPositions(position);
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
        TextView votes;
        ImageView imgThumb;
        int position;
        Track current;
        FloatingActionButton fabVote;

        public MyViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            votes       = (TextView) itemView.findViewById(R.id.tvVotes);
            imgThumb    = (ImageView) itemView.findViewById(R.id.img_album);
            fabVote     = (FloatingActionButton) itemView.findViewById(R.id.fabUpvote);
        }

        public void setData(Track current, int position) {
            this.title.setText(current.Title);
            this.votes.setText(Integer.toString(current.Votes));
            Picasso.with(mContext).load(current.ImageURL).into(this.imgThumb);
            this.position = position;
            this.current = current;
        }

        public void setListeners(){
            fabVote.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}