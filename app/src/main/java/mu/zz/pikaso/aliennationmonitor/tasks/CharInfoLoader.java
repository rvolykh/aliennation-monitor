package mu.zz.pikaso.aliennationmonitor.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import mu.zz.pikaso.aliennationmonitor.internet.Connection;
import mu.zz.pikaso.aliennationmonitor.internet.Parser;
import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class CharInfoLoader extends AsyncTask<Void, Void, Character> {
    private ProgressBar pb;
    private Context context;
    private Connection.CharInfoInteraction charInfoFragment;
    private String nick;

    public CharInfoLoader(ProgressBar pb, Context context, Connection.CharInfoInteraction charInfoFragment, String nick) {
        this.pb=pb;
        this.context=context;
        this.charInfoFragment=charInfoFragment;
        this.nick=nick;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected Character doInBackground(Void... params) {
        Connection connection = new Connection(context);
        try {
            Character character = connection.sync_GET_CharInfo(nick);
            return character;
        } catch (InterruptedException | ExecutionException | TimeoutException | Parser.ParsingException e) {
            Log.d("CharInfoLoader", e.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Character res) {
        charInfoFragment.displayCharInfo(res);
        pb.setVisibility(View.GONE);;
    }
}
