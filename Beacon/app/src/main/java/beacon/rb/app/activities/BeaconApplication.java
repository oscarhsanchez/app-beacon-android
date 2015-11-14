package beacon.rb.app.activities;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * Created by jesus.martinez on 30/10/2015.
 */
public class BeaconApplication extends Application{

    public static ImageLoader imageLoader;

    public static MainActivity activity;

    @Override
    public void onCreate() {
        super.onCreate();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
    }


}
