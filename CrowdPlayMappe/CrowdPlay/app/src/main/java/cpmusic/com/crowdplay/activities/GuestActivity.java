package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cpmusic.com.crowdplay.Fragments.PlayListFragment;
import cpmusic.com.crowdplay.Fragments.SearchFragment;
import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.FragmentAdapter;

public class GuestActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    String partyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        Intent partyIntent = getIntent();
        partyID = partyIntent.getStringExtra("ID");

        mViewPager =(ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    private void setupViewPager(ViewPager viewPager){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        PlayListFragment playListFragment = new PlayListFragment();
        SearchFragment searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putString("ID", partyID);

        playListFragment.setArguments(bundle);
        searchFragment.setArguments(bundle);

        adapter.addFragment(playListFragment,"Playlist");
        adapter.addFragment(searchFragment,"Search");
        viewPager.setAdapter(adapter);

    }
}

