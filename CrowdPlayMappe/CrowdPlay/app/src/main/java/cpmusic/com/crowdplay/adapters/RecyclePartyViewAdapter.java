package cpmusic.com.crowdplay.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Party;

/**
 * Created by ander on 20/05/2017.
 */

public class RecyclePartyViewAdapter extends RecyclerView.Adapter<RecyclePartyViewAdapter.MyViewHolder> {


    private List<Party> parties;
    private LayoutInflater inflater;
    Context mContext;


    public RecyclePartyViewAdapter(Context context)
    {
        mContext = context;
        parties = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_party, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Party party = parties.get(position);
        holder.setData(party, position);

    }

    public void addParty(Party party){
        parties.add(party);
        notifyItemChanged(parties.size()-1);
    }

    public void AddParties(List<Party> _parties)
    {
        parties = _parties;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return parties.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtGuests;
        TextView txtTracks;
        TextView txtPartyName;
        int position;
        Party current;

        public MyViewHolder(View itemView)
        {super(itemView);
            txtGuests = (TextView) itemView.findViewById(R.id.textViewGuests);
            txtTracks = (TextView) itemView.findViewById(R.id.textViewTracks);
            txtPartyName = (TextView) itemView.findViewById(R.id.textViewPartyName);
        }
        public void setData(Party current, int position){
            int nrOfTracks;
            if(current.Tracks==null){
                nrOfTracks = 0;
            }
            else{
                nrOfTracks = current.Tracks.size();
            }

            this.txtPartyName.setText(current.name);
            this.txtTracks.setText(Integer.toString(nrOfTracks));
            this.txtGuests.setText("14");
            this.current = current;
        }
    }



}
