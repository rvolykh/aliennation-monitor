package mu.zz.pikaso.aliennationmonitor.internet;


import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class Parser {

    public static Character CharInfoResponse(String response) throws ParsingException {
        Character character = new Character();

        String a[] = response.split("\n");
        //Log.d("Parser", "Response: "+a[a.length-1]);
        if(a.length == 17) {
            String res[] = a[a.length - 1].split("(\\<.*?\\>)", 0);
            if(res.length == 63) {
                //parsing..
                character.setGuild(res[9]);
                //Log.d("Parser", "setGuild: " + res[9]);
                character.setName(res[15]);
                //Log.d("Parser", "setName: " + res[15]);
                try{
                    //Log.d("Parser", "setLevel: "+res[20]);
                    character.setLevel(Integer.parseInt(res[20]));
                }catch(NumberFormatException e){
                    character.setLevel(-1);
                }
                try{
                    //Log.d("Parser", "setResets: " + res[24]);
                    character.setResets(Integer.parseInt(res[24]));
                }catch(NumberFormatException e){
                    character.setResets(-1);
                }
                if(res[28].isEmpty()) {
                    //Log.d("Parser", "setClas: " + res[28]);
                    character.setClas("Rage Fighter");
                }else {
                    //Log.d("Parser", "setClas: " + res[28]);
                    character.setClas(res[28]);
                }
                try{
                    //Log.d("Parser", "setBansCount: " + res[32]);
                    character.setBansCount(Integer.parseInt(res[32]));
                }catch(NumberFormatException e){
                    character.setBansCount(-1);
                }
                try{
                    //Log.d("Parser", "setBonus: " + res[36]);
                    character.setBonus(Integer.parseInt(res[36]));
                }catch(NumberFormatException e){
                    character.setBonus(-1);
                }
                //Log.d("Parser", "setMap: " + res[40]);
                character.setMap(res[40]);
                try{
                    //Log.d("Parser", "setX: " + res[44]);
                    character.setX(Integer.parseInt(res[44]));
                }catch(NumberFormatException e){
                    character.setX(-1);
                }
                try{
                    //Log.d("Parser", "setY: " + res[48]);
                    character.setY(Integer.parseInt(res[48]));
                }catch(NumberFormatException e){
                    character.setY(-1);
                }
                //Log.d("Parser", "setServerName: " + res[52]);
                character.setServerName(res[52]);
                //Log.d("Parser", "setStatus: " + res[56]);
                character.setStatus(res[56]);
                return character;
            }
        }
        throw new ParsingException("Couldn't parse received response!");
    }

    public static List<Character> CharListResponse(String response) throws ParsingException {
        List<Character> result = new ArrayList<Character>();

        String a[] = response.split("\n");
        String res[] = a[a.length - 1].split("(\\<.*?\\>)", 0);

        for(int i=0;i<res.length;i++){

            if(res[i].equals("offline") || res[i].equals("Online") || res[i].equals("Offline")){
                Character character = new Character();
                //Log.d("Parser", "setStatus: " + res[i]);
                character.setStatus(res[i]);//4
                //Log.d("Parser", "setServerName: " + res[i-4]);
                character.setServerName(res[i - 4]);//4
                try{
                    //Log.d("Parser", "setY: " + res[i-8]);
                    character.setY(Integer.parseInt(res[i - 8]));//4
                }catch(NumberFormatException e){
                    character.setY(-1);//4
                }
                try{
                    //Log.d("Parser", "setY: " + res[i-8]);
                    character.setX(Integer.parseInt(res[i - 12]));//4
                }catch(NumberFormatException e){
                    character.setX(-1);//4
                }
                //Log.d("Parser", "setMap: " + res[i-16]);
                character.setMap(res[i - 16]);//4
                try{
                    //Log.d("Parser", "setBonus: " + res[i-20]);
                    character.setBonus(Integer.parseInt(res[i - 20]));//4
                }catch(NumberFormatException e){
                    character.setBonus(-1);//4
                }
                try{
                    //Log.d("Parser", "setBansCount: " + res[i-24]);
                    character.setBansCount(Integer.parseInt(res[i - 24]));//4
                }catch(NumberFormatException e){
                    character.setBansCount(-1);//4
                }
                if(res[i - 28].isEmpty()) {
                    //Log.d("Parser", "setClas: " + "Rage Fighter");
                    character.setClas("Rage Fighter");
                }else {
                    //Log.d("Parser", "setClas: " + res[i-28]);
                    character.setClas(res[i - 28]);//4
                }
                try{
                    //Log.d("Parser", "setResets: " + res[i-32]);
                    character.setResets(Integer.parseInt(res[i - 32]));//4
                }catch(NumberFormatException e){
                    character.setResets(-1);//4
                }
                try{
                    //Log.d("Parser", "setLevel: " + res[i-36]);
                    character.setLevel(Integer.parseInt(res[i - 36]));//4
                }catch(NumberFormatException e){
                    character.setLevel(-1);//4
                }
                //Log.d("Parser", "setName: " + res[i-41]);
                character.setName(res[i - 41]);//5
                //Log.d("Parser", "setGuild: " + res[i-47]);
                character.setGuild(res[i - 47]);//6
                result.add(character);
            }
        }

        return result;
    }

    public static class ParsingException extends Exception {
        public ParsingException() { super(); }
        public ParsingException(String message) { super(message); }
        public ParsingException(String message, Throwable cause) { super(message, cause); }
        public ParsingException(Throwable cause) { super(cause); }
    }
}
