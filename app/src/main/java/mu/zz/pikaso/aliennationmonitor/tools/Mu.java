package mu.zz.pikaso.aliennationmonitor.tools;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Mu {
    public static Map<String, String> classes = new HashMap<String,String>(){
        {
            //elf
            put("Elf", "classes/elf.png");
            put("High Elf", "classes/elf.png");
            put("Fairy Elf", "classes/elf.png");
            put("Muse Elf", "classes/elf.png");
            //dk
            put("Dark Knight", "classes/dk.png");
            put("Blade Knight", "classes/dk.png");
            put("Blade Master", "classes/dk.png");
            //dw
            put("Dark Wizard", "classes/sm.png");
            put("Soul Master", "classes/sm.png");
            put("Grand Master", "classes/sm.png");
            //mg
            put("Magic Gladiator", "classes/mg.png");
            put("Duel Master", "classes/mg.png");
            //dl
            put("Dark Lord", "classes/dl.png");
            put("Lord Emperor", "classes/dl.png");
            //sum
            put("Summoner", "classes/sum.png");
            put("Bloody Summoner", "classes/sum.png");
            put("Dimension Master", "classes/sum.png");
            //rf
            put("Rage Fighter", "classes/rf.png");
            put("First Master", "classes/rf.png");
            put("Fist Master", "classes/rf.png");
        }

    };


    public static Map<String, String> maps = new HashMap<String,String>(){
        {
            put("Aida", "maps/aida.jpg");                           //ok
            put("Aida 1", "maps/aida.jpg");                         //ok
            put("Aida 2", "maps/aida.jpg");                         //ok
            put("Arena", "maps/arena.jpg");
            put("Stadium", "maps/arena.jpg");
            put("Atlans", "maps/atlans.jpg");
            put("Atlans 1", "maps/atlans.jpg");
            put("Atlans 2", "maps/atlans.jpg");
            put("Atlans 3", "maps/atlans.jpg");
            put("Barrack", "maps/barrack.jpg");
            put("Crywolf", "maps/crywolf.jpg");                     //ok
            put("Devias", "maps/devias.jpg");
            put("Dungeon", "maps/dungeon.jpg");
            put("Dungeon 1", "maps/dungeon.jpg");
            put("Dungeon 2", "maps/dungeon.jpg");
            put("Dungeon 3", "maps/dungeon.jpg");
            put("Elbeland", "maps/elbeland.jpg");                   //ok
            put("Elbeland 1", "maps/elbeland.jpg");
            put("Elbeland 2", "maps/elbeland.jpg");
            put("Elbeland 3", "maps/elbeland.jpg");
            put("Icarus", "maps/icarus.jpg");
            put("Kalima", "maps/kalima.jpg");
            put("Kalima 1", "maps/kalima.jpg");
            put("Kalima 2", "maps/kalima.jpg");
            put("Kalima 3", "maps/kalima.jpg");
            put("Kalima 4", "maps/kalima.jpg");
            put("Kalima 5", "maps/kalima.jpg");
            put("Kalima 6", "maps/kalima.jpg");
            put("Kalima 7", "maps/kalima.jpg");
            put("Karutan", "maps/karutan.jpg");
            put("Land Of Trials", "maps/landoftrials.jpg");
            put("Loren", "maps/loren.jpg");
            put("Loren Deep", "maps/loren.jpg");
            put("Lorencia", "maps/lorencia.jpg");
            put("Lost Tower", "maps/losttower.jpg");
            put("Lost Tower 1", "maps/losttower.jpg");
            put("Lost Tower 2", "maps/losttower.jpg");
            put("Lost Tower 3", "maps/losttower.jpg");
            put("Lost Tower 4", "maps/losttower.jpg");
            put("Lost Tower 5", "maps/losttower.jpg");
            put("Lost Tower 6", "maps/losttower.jpg");
            put("Lost Tower 7", "maps/losttower.jpg");
            put("Noria", "maps/noria.jpg");
            put("Raklion", "maps/raklion.jpg");
            put("Refuge", "maps/refuge.jpg");
            put("Swamp", "maps/swamp.jpg");                         //ok
            put("Swamp Of Peace", "maps/swamp.jpg");                //ok
            put("Tarkan", "maps/tarkan.jpg");
            put("Tarkan 1", "maps/tarkan.jpg");
            put("Tarkan 2", "maps/tarkan.jpg");
            put("Vulcanus", "maps/vulcanus.jpg");

        }
    };


    public static Drawable convert2Drawable(AssetManager assetManager, String uri, boolean isMap) throws IOException {
        InputStream is = null;
        if(isMap) {
            is = assetManager.open(Mu.maps.get(uri));
            Drawable res = Drawable.createFromStream(is, "map.jpg");
            is.close();

            return res;
        }
        is = assetManager.open(Mu.classes.get(uri));
        Drawable res = Drawable.createFromStream(is, "class.jpg");
        is.close();

        return res;
    }

}
