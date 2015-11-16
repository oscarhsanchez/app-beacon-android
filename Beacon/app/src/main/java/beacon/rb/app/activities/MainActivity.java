package beacon.rb.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.innoquant.moca.MOCABeacon;

import java.util.HashMap;
import java.util.List;

import beacon.rb.app.R;
import beacon.rb.app.adapter.RecyclerAdapter;
import beacon.rb.app.components.HidingScrollListener;
import beacon.rb.app.components.SimpleDividerItemDecoration;

public class MainActivity extends AppCompatActivity {

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
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BeaconApplication.activity = this;

        tvPlace = (TextView) findViewById(R.id.place);
        tvZone = (TextView) findViewById(R.id.zone);
        btnScanner = (ImageButton) findViewById(R.id.btnScanner);

        //Iniciamos servicio
        Intent service = new Intent(this, BeaconService.class);
        startService(service);

        initToolBar();
        initProgressDialog();
        loadBeacons(BeaconApplication.beacons);

    }


    @Override
    protected void onResume() {
        super.onResume();
        BeaconApplication.activity = this;
        if (firstLoad) {
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initRecyclerView();
                }
            }, 20);
            firstLoad = false;
        }
        btnScanner.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatingButtonEnterAnimation();
            }
        }, 500);
    }

    /**
     * Init floatingActionButton enter animmation
     */
    private void floatingButtonEnterAnimation() {
        btnScanner.setVisibility(View.VISIBLE);
        Animation btnAnim = AnimationUtils.loadAnimation(this, R.anim.btn_anim_enter);
        btnScanner.startAnimation(btnAnim);
    }

    /**
     * Init toolbar
     */
    private void initToolBar() {
        mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * Init beacon list
     */
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

    /**
     * Init progress dialog
     */
    private void initProgressDialog() {
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading scanner...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firstLoad = false;
        BeaconApplication.activity = null;
    }


    public void loadBeacons(List<MOCABeacon> list){
        if(list!=null) {
            for (MOCABeacon b : list) {
                beacons.put(b.getId(), b);
            }
            if (recyclerAdapter != null) recyclerAdapter.notifyDataSetChanged();
        }
    }


    public void loadLocation(final int type, final String name){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(type == 0){ //Zone
                    tvZone.setText(name);
                }else if(type==1){ //Place
                    tvPlace.setText(name);
                }
            }
        });
    }


    public void startWebView(String s){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", s);
        startActivity(intent);
    }

    public void startVideo(String s){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        intent.setDataAndType(Uri.parse(s), "video/*");
        startActivity(intent);
    }

    public void startImageViewer(String s){
        Intent intent = new Intent(this, FullScreenImageActivity.class);
        intent.putExtra("url", s);
        startActivity(intent);
    }
    /**
     * Update beacon data
     *
     * @param mocaBeacon Beacon updated
     */
    public void updateBeacon(final MOCABeacon mocaBeacon) {
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

    /**
     * Performs click on scanner button
     *
     * @param v View performs click
     */
    public void onClickScanner(View v) {
        dialog.show();
        Animation btnAnim = AnimationUtils.loadAnimation(this, R.anim.btn_anim_exit);
        btnAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                btnScanner.setVisibility(View.GONE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                            intent.putExtra("SCAN_MODE", "QR_CODE_MODE, PRODUCT_MODE");
                            startActivityForResult(intent, REQUEST_SCANNER);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 400);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        btnScanner.startAnimation(btnAnim);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_SCANNER) {
            if (dialog != null) dialog.dismiss();

            if (resultCode == Activity.RESULT_OK) {
                String result = intent.getStringExtra("SCAN_RESULT");
                Intent webIntent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", result);
                startActivity(webIntent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
}
