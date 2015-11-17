package beacon.rb.app.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.transition.Fade;

import beacon.rb.app.R;

/**
 * Created by jesus.martinez on 03/11/2015.
 */
public class Splash extends Activity {

    private Boolean background = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.containsKey("background")){
                background = true;
                moveTaskToBack(true);
            }
        }


        setContentView(R.layout.splash);


        //Evita un bug que reinicia la app y elimina la que ya estaba en segundo plano
        if (!isTaskRoot()) {
            finish();
            return;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                if(background) intent.putExtra("background", true);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
