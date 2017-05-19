package crowdplay.com.materialrecyclerviewdemo.app;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.OvershootInterpolator;

import crowdplay.com.materialrecyclerviewdemo.R;
import crowdplay.com.materialrecyclerviewdemo.adaptor.RecyclerAdapter;
import crowdplay.com.materialrecyclerviewdemo.model.Track;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("CrowdPlay");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryLight));

        toolbar.inflateMenu(R.menu.menu_main);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerAdapter adapter = new RecyclerAdapter(this, Track.getData());
        recyclerView.setAdapter(adapter);




        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);

        //recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we dont use it then also our items shows default animation. #Check Docs
    }

}
