package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cpmusic.com.crowdplay.Fragments.PlayListFragment;
import cpmusic.com.crowdplay.Fragments.SearchFragment;
import cpmusic.com.crowdplay.R;

public class GuestMainActivity extends AppCompatActivity {

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
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());

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

 class SectionPageAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);


    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
