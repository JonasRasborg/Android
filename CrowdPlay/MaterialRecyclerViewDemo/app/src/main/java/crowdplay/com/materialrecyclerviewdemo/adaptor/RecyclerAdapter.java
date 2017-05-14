package crowdplay.com.materialrecyclerviewdemo.adaptor;

/**
 * Created by rrask on 13-05-2017.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import crowdplay.com.materialrecyclerviewdemo.R;
import crowdplay.com.materialrecyclerviewdemo.model.Track;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<Track> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
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
        mData.get(position).addVote();
        notifyItemChanged(position);
        checkPositions(position);
    }

    public void checkPositions(int position){
        for (int i = position; i > 0; i--){
            if(mData.get(i).getVotes()>mData.get(i-1).getVotes()){
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
            this.title.setText(current.getTitle());
            this.votes.setText(Integer.toString(current.getVotes()));
            this.imgThumb.setImageResource(current.getImageID());
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
                    addAVote(position);
                    break;
            }
        }
    }
}
