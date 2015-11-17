package beacon.rb.app.activities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.innoquant.moca.MOCA;
import com.innoquant.moca.MOCAAction;
import com.innoquant.moca.MOCABeacon;
import com.innoquant.moca.MOCAConfig;
import com.innoquant.moca.MOCAPlace;
import com.innoquant.moca.MOCAProximity;
import com.innoquant.moca.MOCAProximityService;
import com.innoquant.moca.MOCAZone;

import java.util.List;

/**
 * Created by jesus.martinez on 14/11/2015.
 */
public class BeaconService extends Service implements MOCAProximityService.EventListener, MOCAProximityService.ActionListener {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MOCA.initializeSDK(getApplication());

        MOCAProximityService MOCAservice = MOCA.getProximityService();
        if (MOCAservice != null) {
            MOCAservice.setEventListener(BeaconService.this);
            MOCAservice.setActionListener(BeaconService.this);
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //REGION EVENT LISTENERS
    @Override
    public void didEnterRange(MOCABeacon mocaBeacon, MOCAProximity mocaProximity) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.updateBeacon(mocaBeacon);
    }

    @Override
    public void didExitRange(MOCABeacon mocaBeacon) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.updateBeacon(mocaBeacon);
    }

    @Override
    public void didBeaconProximityChange(MOCABeacon mocaBeacon, MOCAProximity mocaProximity, MOCAProximity mocaProximity1) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.updateBeacon(mocaBeacon);
    }

    @Override
    public void didEnterPlace(final MOCAPlace mocaPlace) {
    }

    @Override
    public void didExitPlace(MOCAPlace mocaPlace) {
    }

    @Override
    public void didEnterZone(final MOCAZone mocaZone) {
    }

    @Override
    public void didExitZone(MOCAZone mocaZone) {
    }

    @Override
    public boolean handleCustomTrigger(String s) {
        return false;
    }

    @Override
    public void didLoadedBeaconsData(List<MOCABeacon> list) {
        BeaconApplication.beacons = list;
        if (BeaconApplication.activity != null) BeaconApplication.activity.loadBeacons(list);
    }

    // REGION ACTION LISTENERS
    @Override
    public boolean displayNotificationAlert(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean openUrl(MOCAAction mocaAction, String s) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.startWebView(s);
        return false;
    }

    @Override
    public boolean showHtmlWithString(MOCAAction mocaAction, String s) {
        return false;
    }

    @Override
    public boolean playVideoFromUrl(MOCAAction mocaAction, String s) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.startVideo(s);
        return false;
    }

    @Override
    public boolean displayImageFromUrl(MOCAAction mocaAction, String s) {
        if (BeaconApplication.activity != null) BeaconApplication.activity.startImageViewer(s);
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

}
