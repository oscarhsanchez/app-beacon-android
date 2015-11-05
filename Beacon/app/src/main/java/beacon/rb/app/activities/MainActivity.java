package beacon.rb.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.innoquant.moca.MOCA;
import com.innoquant.moca.MOCAAction;
import com.innoquant.moca.MOCABeacon;
import com.innoquant.moca.MOCAPlace;
import com.innoquant.moca.MOCAProximity;
import com.innoquant.moca.MOCAProximityService;
import com.innoquant.moca.MOCAZone;

import java.util.HashMap;
import java.util.List;

import beacon.rb.app.R;
import beacon.rb.app.adapter.RecyclerAdapter;
import beacon.rb.app.components.HidingScrollListener;
import beacon.rb.app.components.SimpleDividerItemDecoration;


public class MainActivity extends AppCompatActivity implements MOCAProximityService.EventListener, MOCAProximityService.ActionListener {

    public static final int REQUEST_SCANNER = 1;

    private ImageButton btnScanner;
    private TextView tvPlace, tvZone;
    private LinearLayout layoutZone;
    private LinearLayout mToolbarContainer;
    private Toolbar toolbar;
    private RecyclerView beaconRecyclerList;
    private RecyclerAdapter recyclerAdapter;
    private HashMap<String, MOCABeacon> beacons = new HashMap<>();

    private Boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPlace = (TextView) findViewById(R.id.place);
        tvZone = (TextView) findViewById(R.id.zone);
        btnScanner = (ImageButton) findViewById(R.id.btnScanner);
        MOCA.initializeSDK(getApplication());
        MOCAProximityService service = MOCA.getProximityService();
        if (service != null) {
            service.setEventListener(MainActivity.this);
            service.setActionListener(MainActivity.this);
        }

        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firstLoad) {
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initRecyclerView();
                }
            },20);

            btnScanner.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimations();
                }
            },500);
            firstLoad = false;
        }
    }

    private void startAnimations(){
        btnScanner.setVisibility(View.VISIBLE);
        Animation btnAnim = AnimationUtils.loadAnimation(this,R.anim.btn_anim);
        btnScanner.startAnimation(btnAnim);
    }

    private void initToolBar() {
        mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initRecyclerView() {
        layoutZone = (LinearLayout) findViewById(R.id.placeZone);
        beaconRecyclerList = (RecyclerView) findViewById(R.id.beaconList);

        int paddingTop = getSupportActionBar().getHeight() * 2;
        beaconRecyclerList.setPadding(beaconRecyclerList.getPaddingLeft(), paddingTop, beaconRecyclerList.getPaddingRight(), beaconRecyclerList.getPaddingBottom());
        beaconRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        beaconRecyclerList.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));

        recyclerAdapter = new RecyclerAdapter(MainActivity.this, beacons);
        beaconRecyclerList.setAdapter(recyclerAdapter);
        beaconRecyclerList.setOnScrollListener(new HidingScrollListener(this, getSupportActionBar().getHeight()) {
            @Override
            public void onMoved(int distance) {
                mToolbarContainer.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                mToolbarContainer.animate().translationY(-getSupportActionBar().getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        firstLoad = false;
        MOCA.shutdown();
    }

    /*
     * Event Listener
     * */
    @Override
    public void didEnterRange(MOCABeacon mocaBeacon, MOCAProximity mocaProximity) {
        updateBeacon(mocaBeacon);
    }

    @Override
    public void didExitRange(MOCABeacon mocaBeacon) {
        updateBeacon(mocaBeacon);
    }

    @Override
    public void didBeaconProximityChange(MOCABeacon mocaBeacon, MOCAProximity mocaProximity, MOCAProximity mocaProximity1) {
        updateBeacon(mocaBeacon);
    }

    @Override
    public void didEnterPlace(final MOCAPlace mocaPlace) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPlace.setText(mocaPlace.getName());
            }
        });

    }

    @Override
    public void didExitPlace(MOCAPlace mocaPlace) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvPlace.setText("");
            }
        });
    }

    @Override
    public void didEnterZone(final MOCAZone mocaZone) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvZone.setText(mocaZone.getName());
            }
        });
    }

    @Override
    public void didExitZone(MOCAZone mocaZone) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvZone.setText("");
            }
        });
    }

    @Override
    public boolean handleCustomTrigger(String s) {
        return false;
    }

    @Override
    public void didLoadedBeaconsData(List<MOCABeacon> list) {
        for (MOCABeacon b : list) {
            beacons.put(b.getId(), b);
        }
        if(recyclerAdapter!=null) recyclerAdapter.notifyDataSetChanged();
    }


    /*
    * Action Listener
    * */
    @Override
    public boolean displayNotificationAlert(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean openUrl(MOCAAction mocaAction, String s) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", s);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean showHtmlWithString(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean playVideoFromUrl(MOCAAction mocaAction, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        intent.setDataAndType(Uri.parse(s), "video/*");
        startActivity(intent);
        return false;
    }


    @Override
    public boolean displayImageFromUrl(MOCAAction mocaAction, String s) {
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("url", s);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean displayPassFromUrl(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean addTag(MOCAAction mocaAction, String s, String s1) {
        return false;
    }

    @Override
    public boolean playNotificationSound(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean performCustomAction(MOCAAction mocaAction, String s) {
        return false;
    }


    private void updateBeacon(final MOCABeacon mocaBeacon) {
        if (beacons != null && recyclerAdapter != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    beacons.put(mocaBeacon.getId(), mocaBeacon);
                    if (recyclerAdapter != null)
                        recyclerAdapter.notifyDataSetChanged();
                }
            });

        }
    }


    public void onClickScanner(View v) {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE, PRODUCT_MODE");
            startActivityForResult(intent, REQUEST_SCANNER);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_SCANNER) {

            if (resultCode == Activity.RESULT_OK) {
                String result = intent.getStringExtra("SCAN_RESULT");
                Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", result);
                startActivity(webIntent);
            }
        }
    }
}
