package beacon.rb.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import beacon.rb.app.R;
import beacon.rb.app.components.TouchImageView;


public class FullScreenImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        String url = getIntent().getExtras().getString("url");

        TouchImageView imageView = (TouchImageView) findViewById(R.id.fullImage);
        BeaconApplication.imageLoader.displayImage(url, imageView);

    }

}