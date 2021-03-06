package cpmusic.com.crowdplay.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cpmusic.com.crowdplay.model.spotifyModel.Item;
import cpmusic.com.crowdplay.model.spotifyModel.Tracks;
import cpmusic.com.crowdplay.util.APIConnector;

public class DrakeService extends Service {

    private static final String TAG = DrakeService.class.getSimpleName();
    Context mContext;


    public DrakeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromAPI, new IntentFilter("Drake"));
        mContext = this;

        Thread thread = new Thread(new DrakeThreadClass(1337,this));
        thread.start();
    }

    private BroadcastReceiver mReceiveFromAPI = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            Tracks drakeData = (Tracks) intent.getSerializableExtra("DrakeData");

            CalcDrakePopularity(drakeData);
        }
    };

    private void CalcDrakePopularity(Tracks tracks)
    {
        List<Item> items = tracks.items;
        Item mostPopular = items.get(0);

        for(int i = 0; i<items.size()-1; i++)
        {
            if(mostPopular.popularity < items.get(i+1).popularity)
            {
                mostPopular = items.get(i+1);
            }
        }
        Toast toast = Toast.makeText(this, "Most Popular Drake song on Spotify:\r\n" + "'" + mostPopular.name + "'", Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartedCommand, Thread name: " + Thread.currentThread().getName());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy, Thread name: " + Thread.currentThread().getName());
    }

    final class DrakeThreadClass implements Runnable
    {
        int service_id;
        Service starterservice;
        APIConnector apiConnector;

        DrakeThreadClass(int service_id, Service service)
        {
            this.service_id = service_id;

            apiConnector = new APIConnector(mContext);
        }

        @Override
        public void run()
        {
            synchronized (this)
            {
                while (true) {
                    try {
                        wait(1000*60*60);
                        apiConnector.sendRequestForDrake(mContext);

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
