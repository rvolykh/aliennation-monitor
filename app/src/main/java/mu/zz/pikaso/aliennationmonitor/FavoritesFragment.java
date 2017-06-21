package mu.zz.pikaso.aliennationmonitor;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.aliennationmonitor.adapters.CharacterMainAdapter;
import mu.zz.pikaso.aliennationmonitor.internet.Connection;
import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class FavoritesFragment extends Fragment implements
        AbsListView.OnItemClickListener, AbsListView.OnItemLongClickListener,Connection.CharacterMainFragment

{
    public final static String CharacterListFileName = "CharacterList";
    private List<Character> characterList;
    private CharacterMainAdapter mAdapter;

    private AbsListView mListView;

    public FavoritesFragment() {
        characterList = new ArrayList<Character>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CharacterMainAdapter(getActivity(), R.layout.favorite_character_row);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        //((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setAdapter(mAdapter);

        load();
        if(characterList.isEmpty())
            setEmptyText("Go to 'Find chars' and Add to list");
        //refresh();

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Character character = mAdapter.getItem(position);
        ((CharacterFavoritesInteraction)getActivity()).openCharInfo(character);
    }



    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void addCharacter2Favorites(Character character){
        mAdapter.add(character);
        mAdapter.notifyDataSetChanged();
        save();
    }

    public void save(){
        FileOutputStream outputStream = null;
        try {
            outputStream = getActivity().openFileOutput(CharacterListFileName, Context.MODE_PRIVATE);
            for(int i=0;i<mAdapter.getCount();i++) {
                Character character = mAdapter.getItem(i);
                if(character != null){
                    String text = String.format("%s;%s\n",character.getName(), character.getClas());
                    outputStream.write(text.getBytes());
                }
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FavoritesFragment", "Can't save file! " +e.toString());
        }
        if(outputStream != null)
            outputStream = null;
    }

    public void load(){
        if(characterList.size()>0)
            characterList.clear();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getActivity().openFileInput(CharacterListFileName)));
            String line = reader.readLine();
            while(line != null){
                String data[] = line.split(";");
                if(data.length == 2) { //OK!
                    Character c = new Character(data[0], data[1]);
                    characterList.add(c);
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.clear();
        mAdapter.addAll(characterList);
        mAdapter.notifyDataSetChanged();
    }

    public void refresh(){
        Connection connection = new Connection(getActivity());
        for(int i=0;i<mAdapter.getCount();i++){
            connection.GET_CharShortInfo(mAdapter.getItem(i).getName(), i, this);
        }

    }


    @Override
    public void displayCharShortInfo(Character characters, int pos) {
        mAdapter.remove(mAdapter.getItem(pos));
        mAdapter.insert(characters, pos);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayCSIError(String message) {
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: make my own design and remove form
        new AlertDialog.Builder(getContext())
                .setTitle("Remove?")
                .setMessage("Are you sure to remove "+mAdapter.getItem(position).getName())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return true;
    }

    public interface CharacterFavoritesInteraction{
        void openCharInfo(Character character);
    }
}
