package cpmusic.com.crowdplay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;

/**
 * Created by Jonas R. Hartogsohn on 17-05-2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>
{
    List<Track> mData;
    private LayoutInflater inflater;

    public SearchAdapter(Context context, List<Track> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void addToList(Tracks tracks)
    {
    for (Track t : tracks.tracks)
    {
        mData.add(t);
    }
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchAdapter.MyViewHolder holder, int position) {
        Track current = mData.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView votes;
        ImageView imgThumb, imgAdd;
        int position;
        Track current;

        public MyViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            votes       = (TextView) itemView.findViewById(R.id.tvVotes);
            imgThumb    = (ImageView) itemView.findViewById(R.id.img_row);
            imgAdd      = (ImageView) itemView.findViewById(R.id.img_add);
        }

        public void setData(Track current, int position) {
            this.title.setText(current.Artist);
            this.votes.setText(Integer.toString(current.Votes));
            //this.imgThumb.setImageResource(current.getImageID);
            this.position = position;
            this.current = current;
        }

        public void setListeners(){
            imgAdd.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_add:
                    //addAVote(position);
                    break;
            }
        }
    }
}
