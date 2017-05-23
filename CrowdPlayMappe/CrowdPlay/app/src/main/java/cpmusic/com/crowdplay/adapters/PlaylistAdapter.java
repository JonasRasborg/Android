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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Guest;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.SharedPreferencesConnector;

/**
 * Created by rrask on 19-05-2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    List<Track> mData;
    private LayoutInflater inflater;
    Context mContext;
    DatabaseReference mTracksRef;
    DatabaseReference mPartyRef;
    SharedPreferencesConnector sharedPreferencesConnector;
    String thisUserID;
    String thisUserFullName;
    String thisUserPicURI;
    DatabaseReference mCurrentTrackVotersRef;
    MyViewHolder holder;


    public PlaylistAdapter(Context context, DatabaseReference root) {
        inflater = LayoutInflater.from(context);
        this.mData = new ArrayList<>();
        mContext = context;
        mPartyRef = root;
        mTracksRef = root.child("Tracks");
        sharedPreferencesConnector = new SharedPreferencesConnector();
        thisUserID = sharedPreferencesConnector.getFacebookUID(mContext);
        thisUserFullName = sharedPreferencesConnector.getFacebookFullName(mContext);
        thisUserPicURI = sharedPreferencesConnector.getFacebookProfilepicUri(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("RecyclerAdapter","onBindViewHolder "+position);
        Track current = mData.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    public void addPointToGuest(String id)
    {
        final DatabaseReference votedGust = mPartyRef.child("Guests").child(id);

        votedGust.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Guest voteReceivingGuest = dataSnapshot.getValue(Guest.class);
                int points = voteReceivingGuest.Points + 1;

                votedGust.child("Points").setValue(points);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void changeVote(Track track)
    {
        for (int i = 0; i<mData.size();i++)
        {
            if (mData.get(i).URI == track.URI)
            {
                mData.get(i).Voters=track.Voters;
                notifyItemChanged(i);
                checkPositions(i);
            }
        }
    }

    public void addPlayingSong(Track track){
        if(track.Voters != null){
            track.Voters.clear();
        }
        track.isVoted=false;
        addTrack(track);
    }


    public void moveTrackToLast(Track track)
    {
        mData.remove(0);
        notifyItemRemoved(0);
        // Remove voters locally

    }

    public void resetVotes(Track track){

        moveTrackToLast(track);

        // Remove all voters from Track on firebse
        mTracksRef.child(track.URI).child("Voters").removeValue();
    }

    public Track getTopTrack(){
        return mData.get(0);
    }

    public void checkPositions(int position){

        int TrackVotesThis =0;
        int TrackVoteslast =0;

       for (int i = position; i > 0; i--)
       {
           // Handling firebase null pointer (Votes is empty)
                /*
                if (mData.get(i).Voters==null)
                {
                    TrackVotesThis = 0;
                }
                */
                if(mData.get(i).Voters!=null)
                {
                    TrackVotesThis = mData.get(i).Voters.size();
                }

                /*
                if(mData.get(i-1).Voters==null)
                {
                    TrackVoteslast = 0;
                }*/

               if(mData.get(i-1).Voters!=null)
               {
                   TrackVoteslast = mData.get(i-1).Voters.size();
               }

                // Asserrt on local int
           if(TrackVotesThis>TrackVoteslast)
            {
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

    public boolean setIsVoted(Track votedTrack){
        if (votedTrack.Voters != null)
        {
            for (HashMap.Entry<String, Guest> entry : votedTrack.Voters.entrySet())
            {
                Guest g = entry.getValue();

                if (g.userID.equals(thisUserID))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void addTrack(Track newTrack){
        newTrack.isVoted = setIsVoted(newTrack);
        mData.add(newTrack);
        notifyItemChanged(mData.size()-1);
        checkPositions(mData.size()-1);
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
            try{
                this.votes.setText(Integer.toString(current.Voters.size()));
            }
            catch (Exception e)
            {
                Log.d("RecycklerViewAdapter","No voters on current track, setting text to 0");
                this.votes.setText("0");
            }
            Picasso.with(mContext).load(current.ImageURL).into(this.imgThumb);
            this.position = position;
            this.current = current;
            if(current.isVoted)
            {
                this.fabVote.setAlpha(40);
            }
            else if(!current.isVoted){
                this.fabVote.setAlpha(255);
            }

        }


        public void setListeners(){
            fabVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   VoteOnTrack();
                    current.isVoted = true;
                }
            });
        }


        @Override
        public void onClick(View v)
        {

        }

        private void VoteOnTrack()
        {
            // databasereference to current track Voters which is voted on
            mCurrentTrackVotersRef = mTracksRef.child(current.URI).child("Voters");

            // Get snapshot of Voters
            mCurrentTrackVotersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                boolean AllreadyVoted = false;
                Guest thisVotingGuest = new Guest(thisUserID,thisUserFullName,thisUserPicURI);


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    AllreadyVoted = false;

                    // If not voters on track at all
                    if(dataSnapshot.getValue()==null)
                    {
                        mCurrentTrackVotersRef.push().setValue(thisVotingGuest);
                        addPointToGuest(current.AddedBy);
                    }

                    else {
                        for (DataSnapshot i : dataSnapshot.getChildren()) {

                            // Check if guest allready voted on track
                            Guest g = dataSnapshot.child(i.getKey()).getValue(Guest.class);
                            if (g.userID.equals(thisUserID)) {
                                AllreadyVoted = true;
                            }
                        }

                        // After For loop asserting
                        if (AllreadyVoted == false) {
                            // Push this Guest on voters list
                            mCurrentTrackVotersRef.push().setValue(thisVotingGuest);
                            addPointToGuest(current.AddedBy);
                            Toast.makeText(mContext, current.Title + " Upvoted", Toast.LENGTH_SHORT).show();
                            AllreadyVoted = true;

                            // Make fab button greyed oyt here
                        }
                        else
                        {
                            Toast.makeText(mContext, "Your allready voted on " + current.Title, Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
