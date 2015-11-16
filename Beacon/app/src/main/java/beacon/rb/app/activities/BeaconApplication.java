package beacon.rb.app.activities;

import android.app.Application;

import com.innoquant.moca.MOCA;
import com.innoquant.moca.MOCABeacon;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;


/**
 * Created by jesus.martinez on 30/10/2015.
 */
public class BeaconApplication extends Application{

    public static List<MOCABeacon> beacons;
    public static ImageLoader imageLoader;
    public static MainActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }


}
