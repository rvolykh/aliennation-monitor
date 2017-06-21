package mu.zz.pikaso.aliennationmonitor.internet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import mu.zz.pikaso.aliennationmonitor.R;
import mu.zz.pikaso.aliennationmonitor.representation.Character;

public class Connection {
    private RequestQueue queue;
    private final static String url1 = "http://kaena.aliennation.lv/muonline/char.php?nick=";//char info
    private final static String url2 = "http://kaena.aliennation.lv/muonline/char.php?nick=";//chars list

    public Connection(Context context){
        queue = Volley.newRequestQueue(context);
    }

    /**
     * Send GET request to Aliennation server, and display the result
     * @param nickname character name
     * @param listener char info fragment
     */
    public void GET_CharInfo(String nickname, CharInfoInteraction listener){
        final CharInfoInteraction charinfo = listener;
        String requestLink = url1+nickname;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Character character = null;

                        try {
                            character = Parser.CharInfoResponse(response);
                        } catch (Parser.ParsingException e) {
                            e.printStackTrace();
                        }

                        if(character != null) {
                            charinfo.displayCharInfo(character);
                        }
                        else{
                            charinfo.displayCIError("Bad response from server. Please contact admin ("+((Context)charinfo).getText(R.string.admin_email)+")");

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        charinfo.displayCIError("Check your Internet connection!");
                    }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Send GET request to Aliennation server, and display the result
     * @param regex character name
     * @param listener char info fragment
     */
    public void GET_CharList(String regex, FindCharInteraction listener){
        final FindCharInteraction charlist = listener;
        String requestLink = url2+regex+"%25";//%25 = '%'
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Character> nicknames = new ArrayList<Character>();
                        try {
                            nicknames = Parser.CharListResponse(response);
                        } catch (Parser.ParsingException e) {
                            e.printStackTrace();
                        }

                        if(nicknames != null) {
                            charlist.displayChars(nicknames);
                        }
                        else{
                            charlist.displayCLError("Bad response from server. Please contact admin (" + ((Context) charlist).getText(R.string.admin_email) + ")");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                charlist.displayCLError("Check your Internet connection!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void GET_CharShortInfo(String nickname, final int pos, CharacterMainFragment listener){
        final CharacterMainFragment charinfo = listener;
        String requestLink = url1+nickname;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Character character = null;

                        try {
                            character = Parser.CharInfoResponse(response);
                        } catch (Parser.ParsingException e) {
                            e.printStackTrace();
                        }

                        if(character != null) {
                            charinfo.displayCharShortInfo(character, pos);
                        }
                        else{
                            charinfo.displayCSIError("Bad response from server. Please contact admin (" + ((Context) charinfo).getText(R.string.admin_email) + ")");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                charinfo.displayCSIError("Check your Internet connection!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface CharInfoInteraction{
        void displayCharInfo(Character character);
        void displayCIError(String message);
    }

    public interface FindCharInteraction{
        void displayChars(List<Character> nicknames);
        void displayCLError(String message);
    }

    public interface CharacterMainFragment{
        void displayCharShortInfo(Character characters, int pos);
        void displayCSIError(String message);
    }


    public Character sync_GET_CharInfo(String nickname) throws InterruptedException, ExecutionException, TimeoutException, Parser.ParsingException {
        String requestLink = url1+nickname;
        // Request a string response from the provided URL.

        RequestFuture<String> listener = RequestFuture.newFuture();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestLink,listener, listener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        String response = listener.get(10, TimeUnit.SECONDS);
        Character character = Parser.CharInfoResponse(response);

        return character;
    }


}
