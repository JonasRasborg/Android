package cpmusic.com.crowdplay.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.spotifyModel.Item;
import cpmusic.com.crowdplay.model.spotifyModel.Tracks;
import cpmusic.com.crowdplay.util.APIConnector;
import cpmusic.com.crowdplay.util.NetworkChecker;

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
        String mostPopular = items.get(0).name;

        for(int i = 0; i<items.size()-1; i++)
        {
            if(items.get(i).popularity < items.get(i+1).popularity)
            {
                mostPopular = items.get(i+1).name;
            }
        }

        Toast.makeText(mContext, "Most Popular Drake song: " + "'" + mostPopular + "'", Toast.LENGTH_SHORT).show();
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
        NetworkChecker networkChecker;
        APIConnector apiConnector;

        DrakeThreadClass(int service_id, Service service)
        {
            this.service_id = service_id;
            starterservice = service;
            networkChecker = new NetworkChecker();
            apiConnector = new APIConnector(mContext);
        }

        @Override
        public void run()
        {
            synchronized (this)
            {
                while (true) {
                    try {
                        Date date;
                        date = new Date();

                        if (networkChecker.getNetworkStatus(mContext))
                        {
                            wait(1000*30*1*1);
                            apiConnector.sendRequestForDrake(mContext);
                        }
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
