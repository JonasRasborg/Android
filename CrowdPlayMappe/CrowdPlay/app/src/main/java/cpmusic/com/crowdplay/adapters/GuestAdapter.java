package cpmusic.com.crowdplay.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Guest;

/**
 * Created by Jonas R. Hartogsohn on 21-05-2017.
 */

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.MyViewHolder>{
    private static final String TAG = GuestAdapter.class.getSimpleName();

    private List<Guest> mData;
    private LayoutInflater mInflater;
    Context mContext;

    public GuestAdapter(Context context, List<Guest> data) {
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void addGuest(Guest newGuest){
        mData.add(newGuest);
        notifyItemChanged(mData.size()-1);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_guest, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Guest currentObj = mData.get(position);
        holder.setData(currentObj);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imgThumb;
        TextView tvPoints;

        public MyViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.txv_row);
            imgThumb    = (ImageView) itemView.findViewById(R.id.imgProfilePicture);
            tvPoints    = (TextView)  itemView.findViewById(R.id.tvPoints);
        }

        public void setData(Guest current) {
            this.title.setText(current.name);

            tvPoints.setText(Integer.toString(current.Points));
            if (current.picURI != null)
            {
                Picasso.with(mContext).load(current.picURI).into(this.imgThumb);
            }
        }
    }

    public void SetPoints(Guest guest)
    {
        for(int i = 0; i<mData.size();i++)
        {
            Guest g = mData.get(i);
            if(g.userID.equals(guest.userID))
            {
                g.Points = guest.Points;
                notifyItemChanged(i);
            }
        }
    }
}
