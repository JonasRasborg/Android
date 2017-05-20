package cpmusic.com.crowdplay.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Party;

/**
 * Created by ander on 20/05/2017.
 */

public class RecyclePartyViewAdapter extends RecyclerView.Adapter<RecyclePartyViewAdapter.MyPartyViewHolder> {


    private List<Party> parties;
    private LayoutInflater inflater;
    Context mContext;
    Activity PartyActivity;

    public class MyPartyViewHolder extends RecyclerView.ViewHolder{
        TextView txtGuests;
        TextView txtTracks;
        TextView txtPartyName;

        public MyPartyViewHolder(View itemView)
        {super(itemView);
            txtGuests = (TextView) itemView.findViewById(R.id.textViewGuests);
            txtTracks = (TextView) itemView.findViewById(R.id.textViewTracks);
            txtPartyName = (TextView) itemView.findViewById(R.id.textViewPartyName);
        }
    }

    public RecyclePartyViewAdapter(Context context, List<Party> _parties, Activity activity)
    {
        mContext = context;
        parties = _parties;
        inflater = LayoutInflater.from(context);
        PartyActivity = activity;
    }
    @Override
    public MyPartyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_party, parent, false);
        MyPartyViewHolder holder = new MyPartyViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(MyPartyViewHolder holder, int position) {

        Party party = parties.get(position);
        holder.txtGuests.setText("many");
        holder.txtPartyName.setText(party.name);
        holder.txtTracks.setText(party.Tracks.size());

    }

    public void AddParties(List<Party> _parties)
    {
        parties = _parties;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 0;
    }




}
