package mu.zz.pikaso.aliennationmonitor;


import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


import mu.zz.pikaso.aliennationmonitor.internet.Connection;
import mu.zz.pikaso.aliennationmonitor.representation.Character;
import mu.zz.pikaso.aliennationmonitor.tasks.CharInfoLoader;
import mu.zz.pikaso.aliennationmonitor.tools.Mu;


public class CharInfoFragment extends Fragment implements Connection.CharInfoInteraction {
    private Character character;

    //Views
    private TextView lvl,res,guild,onoff,server,map,coord;
    private ImageView prof,status;
    private LinearLayout block3;
    private ProgressBar progressBar;

    public CharInfoFragment() {
        // Required empty public constructor
    }

    public void setCharacter(Character character){
        this.character = character;
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_char_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvl = (TextView) getView().findViewById(R.id.tv_lvl);
        res = (TextView) getView().findViewById(R.id.tv_res);
        guild = (TextView) getView().findViewById(R.id.tv_Guild);
        onoff = (TextView) getView().findViewById(R.id.tv_status);
        server = (TextView) getView().findViewById(R.id.tv_server);
        map = (TextView) getView().findViewById(R.id.tv_map);
        coord = (TextView) getView().findViewById(R.id.tv_coord);

        prof = (ImageView) getView().findViewById(R.id.im_class);
        status = (ImageView) getView().findViewById(R.id.im_status);

        block3 = (LinearLayout) getView().findViewById(R.id.block3);

        progressBar = (ProgressBar) getView().findViewById(R.id.pb_char_info);

        //Load data from favorite list
        if(character != null){
            loadData();
        }

    }

    private void loadData(){
        CharInfoLoader task = new CharInfoLoader( progressBar,
                getContext(), (Connection.CharInfoInteraction)this,
                character.getName() );
        task.execute();
    }

    public void refresh(){
        loadData();
    }

    @Override
    public void displayCharInfo(Character character) {
        try {

            if (!character.equals(new Character())) {
                //name
                if(character.getName() != null)
                    getActivity().setTitle(character.getName());
                else
                    getActivity().setTitle(getText(R.string.na));
                //level
                if(character.getLevel()>0)
                    lvl.setText(String.format("%s %d ", getText(R.string.level), character.getLevel()));
                else
                    lvl.setText(String.format("%s %s ", getText(R.string.level), getText(R.string.na)));
                //reset
                if(character.getResets()>=0)
                    res.setText(String.format("(%d)", character.getResets()));
                else
                    res.setText(String.format("(%s)",getText(R.string.na)));
                //guild
                if(character.getGuild() != null)
                    guild.setText(String.format("%s %s", getText(R.string.guild), character.getGuild()));
                else
                    guild.setText(String.format("%s", getText(R.string.guild)));
                //status
                if(character.getStatus() != null)
                    onoff.setText(character.getStatus());
                else
                    onoff.setText(getText(R.string.na));
                //server
                if(character.getServerName() != null)
                    server.setText(character.getServerName());
                else
                    server.setText(getText(R.string.na));
                //coord
                String sCoord = getText(R.string.na).toString();
                if(character.getX()>-1 || character.getY()>-1)
                    sCoord = Integer.toString(character.getX()) + " " + getText(R.string.coord_seperator) + " " + Integer.toString(character.getY());
                coord.setText(sCoord);
                //class
                if(character.getClas() != null)
                    prof.setImageDrawable(Mu.convert2Drawable(getActivity().getAssets(),character.getClas(), false));
                else
                    prof.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_delete));
                //status
                if(character.getStatus().equals("Online"))
                    status.setImageResource(R.drawable.online);
                else
                    status.setImageResource(R.drawable.offline);
                //map
                if(character.getMap() != null) {
                    map.setText(character.getMap());
                    block3.setBackgroundDrawable(Mu.convert2Drawable(getActivity().getAssets(), character.getMap(), true));
                }else {
                    map.setText(getText(R.string.na));
                }
            } else {
                Log.d("CharInfoFragment", "Can't update info! Character is {EMPTY}");
                Toast.makeText(getContext(), "Can't update info!", Toast.LENGTH_SHORT).show();
            }

        }catch (NullPointerException | IOException e){
            Log.d("CharInfoFragment", e.toString());
            e.printStackTrace();
        }

    }

    @Override
    public void displayCIError(String message) {
        //TODO: make my own design and feedback form
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
