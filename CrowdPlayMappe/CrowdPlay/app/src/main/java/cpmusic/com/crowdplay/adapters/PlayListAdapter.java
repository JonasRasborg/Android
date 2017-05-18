package cpmusic.com.crowdplay.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.FirebaseConnector;

/**
 * Created by Jonas R. Hartogsohn on 18-05-2017.
 */

public class PlayListAdapter extends ArrayAdapter<Track> implements View.OnClickListener{

    private List<Track> dataSet;
    Context mContext;

    // View lookup cache
    private class ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
        TextView tvVotes;
        ImageView img_album;
        FloatingActionButton fabUpvote;
    }

    public PlayListAdapter(Context context, ArrayList<Track> data) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Track dataModel=(Track)object;

        switch (v.getId())
        {
/*            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;*/
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Track dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvArtist = (TextView) convertView.findViewById(R.id.tvArtist);
            viewHolder.tvVotes = (TextView) convertView.findViewById(R.id.tvVotes);
            viewHolder.img_album = (ImageView) convertView.findViewById(R.id.img_album);
            viewHolder.fabUpvote = (FloatingActionButton)convertView.findViewById(R.id.fabUpvote);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.tvTitle.setText(dataModel.Title);
        viewHolder.tvArtist.setText(dataModel.Artist);
        viewHolder.tvVotes.setText(String.valueOf(dataModel.Votes));
        Picasso.with(mContext).load(dataModel.ImageURL).into(viewHolder.img_album);

        viewHolder.fabUpvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, dataModel.Title + " Upvoted", Toast.LENGTH_SHORT).show();
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }
}
