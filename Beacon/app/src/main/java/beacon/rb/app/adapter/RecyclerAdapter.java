package beacon.rb.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innoquant.moca.MOCABeacon;

import java.util.HashMap;

import beacon.rb.app.R;
import beacon.rb.app.utils.Utils;

/**
 * Created by jesus.martinez on 03/11/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private HashMap<String, MOCABeacon> beacons;
    private Activity activity;
    private LayoutInflater inflater;

    private int lastPosition;

    public RecyclerAdapter(Activity act, HashMap<String, MOCABeacon> items) {
        this.activity = act;
        beacons = items;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Not use static
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView beaconIcon;
        public TextView name;
        public TextView major;
        public TextView minor;
        public TextView proximity;
        public TextView accuracy;
        public TextView rssi;
        public LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);
            beaconIcon = (ImageView) itemView.findViewById(R.id.beaconIcon);
            name = (TextView) itemView.findViewById(R.id.nombre);
            accuracy = (TextView) itemView.findViewById(R.id.accuracy);
            proximity = (TextView) itemView.findViewById(R.id.proximity);
            minor = (TextView) itemView.findViewById(R.id.minor);
            major = (TextView) itemView.findViewById(R.id.major);
            rssi = (TextView) itemView.findViewById(R.id.rssi);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.activity_main_row, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        String key = (String) beacons.keySet().toArray()[position];
        MOCABeacon beacon = beacons.get(key);

        holder.name.setText(beacon.getName());
        holder.accuracy.setText(Utils.formatFourDecimals(beacon.getAccuracy()) + "m");
        holder.proximity.setText(beacon.getProximity().ordinal()+"-"+beacon.getProximity().name());
        holder.minor.setText(String.valueOf(beacon.getMinor()));
        holder.major.setText(String.valueOf(beacon.getMajor()));
        holder.rssi.setText(beacon.getRssi()+ " dB");

        switch (position%5){
            case 0:
                holder.beaconIcon.setImageResource(R.drawable.green_beacon);
                break;
            case 1:
                holder.beaconIcon.setImageResource(R.drawable.black_beacon);
                break;
            case 2:
                holder.beaconIcon.setImageResource(R.drawable.blue_beacon);
                break;
            case 3:
                holder.beaconIcon.setImageResource(R.drawable.orange_beacon);
                break;
            case 4:
                holder.beaconIcon.setImageResource(R.drawable.pink_beacon);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return beacons!=null ? beacons.size() : 0;
    }

}
