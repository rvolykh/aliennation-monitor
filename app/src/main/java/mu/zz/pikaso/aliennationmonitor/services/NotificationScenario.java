package mu.zz.pikaso.aliennationmonitor.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import mu.zz.pikaso.aliennationmonitor.FavoritesFragment;
import mu.zz.pikaso.aliennationmonitor.MainActivity;
import mu.zz.pikaso.aliennationmonitor.R;
import mu.zz.pikaso.aliennationmonitor.internet.Connection;
import mu.zz.pikaso.aliennationmonitor.internet.Parser;
import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class NotificationScenario {
    public static final String NOTIFY_LIST_FILE = "alienmonitor.temp";
    private static int notification_id = 1;

    public void scenario(Context context){
        List<Character> characters = null;
        //1. Read favorites list
        try {
            characters = readFavorites(context);
        } catch (IOException e) {
            Log.d("NotificationScenario", "Warning: favorites - empty!");
            e.printStackTrace();
        }

        if(characters != null){
            //2. Update data from INTERNET
            INET_UPDATE task = new INET_UPDATE(context, characters);
            task.execute();
        }
        //end
    }

    class INET_UPDATE extends AsyncTask<Void, Void, List<Character>>{
        private Context context;
        private List<Character> characters;

        public INET_UPDATE(Context context, List<Character> characters){
            this.context = context;
            this.characters = characters;
        }

        @Override
        protected List<Character> doInBackground(Void... params) {
            Connection connection = new Connection(context);
            for(int i=0;i<characters.size();i++)
            {
                Character character = null;
                try {
                    character = connection.sync_GET_CharInfo(characters.get(i).getName());
                } catch (InterruptedException e) {
                    Log.d("NotificationScenario", "Error[SSR0]" + e.toString());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    Log.d("NotificationScenario", "Error[SSR1]" + e.toString());
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    Log.d("NotificationScenario", "Error[SSR2]" + e.toString());
                    e.printStackTrace();
                } catch (Parser.ParsingException e) {
                    Log.d("NotificationScenario", "Error[SSR3]" + e.toString());
                    e.printStackTrace();
                }
                if(character != null)
                    characters.set(i, character);
                else
                    characters.remove(i);
            }

            return characters;
        }

        @Override
        protected void onPostExecute(List<Character> characters) {
            scenario_continue(context, characters);
        }

    }

    private void scenario_continue(Context context, List<Character> characters) {
        //3. Analyse fav list
        String text = "";
        if(NotifyFileExsist(context)){
            try {
                text = readFile(context, characters);
            } catch (IOException e) {
                Log.d("NotificationScenario", "Error: problem with reading file!");
                e.printStackTrace();
            }
        }else{
            try {
                createFile(context, characters);
            } catch (IOException e) {
                Log.d("NotificationScenario", "Error: problem with writing file!");
                e.printStackTrace();
            }
        }
        //4. Save changes
        if(!text.isEmpty()) {
            try {
                write2File(context, text);
            } catch (IOException e) {
                Log.d("NotificationScenario", "Error: problem with writing file 2!");
                e.printStackTrace();
            }
        }
    }



    private List<Character> readFavorites(Context context) throws IOException {
        //1. Read saved nicknames
        List<Character> characters = new ArrayList<Character>();
        //Log.d("NotificationScenario", "readFavorites :> ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(FavoritesFragment.CharacterListFileName)));
        String line = reader.readLine();
        while(line != null){
            String data[] = line.split(";");
            if(data.length == 2) { //OK!
                Character c = new Character(data[0], data[1]);
                //Log.d("NotificationScenario", c.getName());
                characters.add(c);
            }
            line = reader.readLine();
        }
        reader.close();


        return characters;
    }

    private boolean NotifyFileExsist(Context context){
        File file = context.getFileStreamPath(NOTIFY_LIST_FILE);
        if(file.exists()) return true;
        return false;
    }

    /*
     *      Create file if not exsists
     *
     * <nick>;<status>;<isDisplayed>
     *
     *     status:  0 - offline
     *              1 - online
     *              3 - dead
     *
     *     isDisplayed: 0 - false       //NOT USED
     *                  1 - true
     */
    private void createFile(Context context, List<Character> characters) throws IOException {
        FileOutputStream outputStream = null;

        outputStream = context.openFileOutput(NOTIFY_LIST_FILE, Context.MODE_PRIVATE);
        for(int i=0;i<characters.size();i++) {
            Character character = characters.get(i);
            if(character != null){
                int status = status2Int(character.getStatus());
                String text = String.format("%s;%d\n",character.getName(), status);
                outputStream.write(text.getBytes());
            }
        }
        outputStream.close();

        if(outputStream != null)
            outputStream = null;
    }

    private void write2File(Context context, String text) throws IOException {
        //Log.d("NotificationScenario", "write2File: \n"+text);
        FileOutputStream outputStream = null;

        outputStream = context.openFileOutput(NOTIFY_LIST_FILE, Context.MODE_PRIVATE);
        outputStream.write(text.getBytes());
        outputStream.close();

        if(outputStream != null)
            outputStream = null;
    }

    private String readFile(Context context, List<Character> characters) throws IOException {
        String result = "";

        BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(NOTIFY_LIST_FILE)));
        String line = reader.readLine();
        while(line != null){
            //Log.d("NotificationScenario", "readFile: line = "+line);
            String data[] = line.split(";");
            if(data.length == 2) { //OK!
                int index = findCharacter(characters, data[0]);
                if(index >= 0){
                    if(characters.get(index).getStatus() != null) {
                        boolean isEQ = data[1].equals(String.valueOf(status2Int(characters.get(index).getStatus())));
                        if (isEQ) {
                            result += line + "\n";
                            characters.remove(index);
                        } else {
                            //Log.d("NotificationScenario", "data[1] = "+data[1] + "| c.Status = "+String.valueOf(status2Int(characters.get(index).getStatus())) + " |Equal = "+isEQ);
                            result += String.format("%s;%d\n", data[0], status2Int(characters.get(index).getStatus()));
                            //show notification
                            String msg = characters.get(index).getName() + " is " + status2String(status2Int(characters.get(index).getStatus()));
                            showNotification(context, msg, notification_id++);

                            characters.remove(index);
                        }
                    }
                }
                //else: delete this row | do nothing
                notification_id = notification_id>10? 1 : notification_id;
            }
            line = reader.readLine();
        }
        reader.close();
        //process new saved characters
        for(int i=0;i<characters.size();i++){
            result += String.format("%s;%d\n",characters.get(i).getName(), status2Int(characters.get(i).getStatus()));
        }

        return result;
    }

    /*
     *  -1 = error!
     */
    private int findCharacter(List<Character> characters, String name){
        for(int i=0;i<characters.size();i++)
            if(characters.get(i).getName().equals(name)) return i;

        return -1;
    }

    private int status2Int(String status){
        if(status.equals("Online")){
            return 1;
            //return character.getIsLive() ? 1 : 2;  //TODO: isLive..
        }

        return 0;
    }

    private String status2String(int status){
        switch(status){
            case 0:
                return "Offline";
            case 1:
                return "Online";
            case 2:
                return "Dead";

            default:
                return "ERROR!";
        }
    }

    private void showNotification(Context context, String msg, int id){
        //Log.d("NotificationScenario", "showNotification: "+msg);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent1 = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);

        // this is it, we'll build the notification!

        NotificationCompat.Builder mNotification = null;
        mNotification = new NotificationCompat.Builder(context)
                .setContentTitle("Aliennation Monitor")
                .setContentText(msg)
                .setSmallIcon(R.drawable.offline)
                .setContentIntent(pIntent)
                .setPriority(Notification.DEFAULT_ALL)
                .setSound(soundUri);
        Notification notification = mNotification.build();

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
    }



}
