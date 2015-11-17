package beacon.rb.app.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.innoquant.moca.MOCA;
import com.innoquant.moca.MOCAAction;
import com.innoquant.moca.MOCABeacon;
import com.innoquant.moca.MOCAPlace;
import com.innoquant.moca.MOCAProximity;
import com.innoquant.moca.MOCAProximityService;
import com.innoquant.moca.MOCAZone;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


/**
 * Created by jesus.martinez on 30/10/2015.
 */
public class BeaconApplication extends Application {

    public static List<MOCABeacon> beacons;
    public static ImageLoader imageLoader;
    public static MainActivity activity;

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startService();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }


    public void startService(){
        Intent service = new Intent(this, BeaconService.class);
        startService(service);
    }

}
