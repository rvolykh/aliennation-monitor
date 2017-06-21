package mu.zz.pikaso.aliennationmonitor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.aliennationmonitor.adapters.FindCharAdapter;
import mu.zz.pikaso.aliennationmonitor.internet.Connection;
import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class FindCharFragment extends Fragment implements Connection.FindCharInteraction{
    private ArrayAdapter<Character> adapter;
    private ListView list;
    private SearchView search;
    List<Character> characters;

    public FindCharFragment() {
        characters = new ArrayList<Character>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_char, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SEARCH
        search = (SearchView) getView().findViewById(R.id.searchCharView);
        search.setQueryHint("<Enter nickname>");
        final Context context = getContext();
        final Connection.FindCharInteraction a = this;
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Connection connection = new Connection(context);
                connection.GET_CharList(query, a);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });

        //LIST
        if(adapter == null)
            adapter = new FindCharAdapter(getActivity(), characters);

        list = (ListView) getView().findViewById(R.id.charsList);
        list.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getActivity().setTitle(getText(R.string.find_char));
    }

    @Override
    public void onResume() {
        super.onResume();
        //set title
        getActivity().setTitle(getText(R.string.find_char));
        //display prev data (backstack.)
        ((FindCharAdapter)list.getAdapter()).notifyDataSetChanged();
    }

    public void refresh(){
        if(search != null){
            if(search.getQuery().toString().isEmpty())
            {
                if(adapter != null){
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                }
            }else{
                search.setQuery(search.getQuery(), true);
            }
        }
    }

    @Override
    public void displayChars(List<Character> nicknames) {
        ((FindCharAdapter)list.getAdapter()).clear();
        ((FindCharAdapter)list.getAdapter()).addAll(nicknames);
        ((FindCharAdapter)list.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void displayCLError(String message) {
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
