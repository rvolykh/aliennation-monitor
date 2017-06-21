package mu.zz.pikaso.aliennationmonitor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import mu.zz.pikaso.aliennationmonitor.R;
import mu.zz.pikaso.aliennationmonitor.representation.Character;


public class FindCharAdapter extends ArrayAdapter<Character> {
    private final Activity context;

    static class ViewHolder {
        public TextView text;
        public ImageButton button;
    }

    public FindCharAdapter(Activity context, List<Character> characters) {
        super(context, R.layout.chars_list_item, characters);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.chars_list_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.tv_characterName);
            viewHolder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer) ((TextView)v).getTag();
                    Character character = (Character) getItem(pos);
                    ((ListCharsSelector)context).openCharInfo(character);
                }
            });
            viewHolder.button = (ImageButton) rowView
                    .findViewById(R.id.imb_addCharacter);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout vwParentRow = (LinearLayout)v.getParent();
                    TextView child = (TextView)vwParentRow.getChildAt(0);
                    Integer pos = (Integer) ((TextView)child).getTag();
                    ((ListCharsSelector)context).onCharacterAdd(getItem(pos));
                }
            });

            rowView.setTag(viewHolder);
        }
        // save views
        ViewHolder holder = (ViewHolder) rowView.getTag();

        // fill data
        String s = ((Character)getItem(position)).getName();

        if(s!=null){
            if(!s.isEmpty()) {
                holder.text.setText(s);
                holder.text.setTag(position);
            }
        }


        return rowView;
    }

    public interface ListCharsSelector{
        void onCharacterAdd(Character character);
        void openCharInfo(Character character);
    }




}
