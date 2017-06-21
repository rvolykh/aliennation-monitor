package mu.zz.pikaso.aliennationmonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import mu.zz.pikaso.aliennationmonitor.services.ServiceRunner;

/**
 *  Interaction with timeouts buttons
 */
interface SettingsViewInteraction{
    void onSetTimeout(View view);
}

public class SettingsFragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener, Button.OnClickListener {
    private static final int TIME_BUTTONS_COUNT = 6;
    private static final String CFG_FILE_NAME = "config.ini";

    private ServiceRunner service;

    private Switch nSwitch;
    private TextView header;
    private LinearLayout notification_block;
    // 1 5 10 15 20 30 mins
    private Button[] timeButtons = new Button[TIME_BUTTONS_COUNT];

    @Override
    public void onClick(View v) {
        for(Button a : timeButtons){
            a.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_default)); //not active
        }
        Log.d("asd", "as1d");
        ((Button) v).setBackgroundDrawable(getResources().getDrawable(android.R.drawable.btn_star)); //active
        Log.d("asd", "as2d");
        try {
            Settings.timeout = Integer.parseInt(((Button)v).getText().toString());
            Log.d("asd", "as3d");
        }catch (NumberFormatException e){
            Log.d("SettingsFragment", "ERROR: "+e.toString());
            e.printStackTrace();
        }
    }

    //Serializable!
    static class Settings implements Serializable {
        public static Boolean notification;
        public static Integer timeout;

        public static void toDefault(){
            notification = true;
            timeout = 5;
        }

        public static void serialize(){
            // save the object to file
            FileOutputStream fos = null;
            ObjectOutputStream out = null;
            try {
                fos = new FileOutputStream(CFG_FILE_NAME);
                out = new ObjectOutputStream(fos);
                out.writeObject(notification);
                out.writeObject(timeout);
                out.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static void deserialize(){
            // save the object to file
            FileInputStream fis = null;
            ObjectInputStream in = null;
            try {
                fis = new FileInputStream(CFG_FILE_NAME);
                in = new ObjectInputStream(fis);
                notification = (Boolean) in.readObject();
                timeout = (Integer) in.readObject();
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Settings");
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Load views
        notification_block = (LinearLayout) getView().findViewById(R.id.ntf_block);
        nSwitch = (Switch) getView().findViewById(R.id.switch1);
        nSwitch.setOnCheckedChangeListener(this);
        if(Settings.notification == null) Settings.toDefault();
        nSwitch.setChecked(Settings.notification);

        header = (TextView) getView().findViewById(R.id.ntf_header);
        timeButtons[0] = (Button) getView().findViewById(R.id.ntf_b1);
        timeButtons[1] = (Button) getView().findViewById(R.id.ntf_b5);
        timeButtons[2] = (Button) getView().findViewById(R.id.ntf_b10);
        timeButtons[3] = (Button) getView().findViewById(R.id.ntf_b15);
        timeButtons[4] = (Button) getView().findViewById(R.id.ntf_b20);
        timeButtons[5] = (Button) getView().findViewById(R.id.ntf_b30);
        for(Button a : timeButtons){
            a.setOnClickListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Settings.notification = isChecked;

        if(isChecked){
            notification_block.setVisibility(View.VISIBLE);
            //START NOTIFICATION SERVICE
            service = new ServiceRunner(getActivity());
            service.execute();
        }else {
            notification_block.setVisibility(View.GONE);
            //STOP NOTIFICATION SERVICE
            if (service == null)
                service = new ServiceRunner(getActivity());
            service.cancel();
        }

    }





}
