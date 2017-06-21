package mu.zz.pikaso.aliennationmonitor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import mu.zz.pikaso.aliennationmonitor.R;
import mu.zz.pikaso.aliennationmonitor.representation.Character;
import mu.zz.pikaso.aliennationmonitor.tools.Mu;


public class CharacterMainAdapter extends ArrayAdapter<Character> {
    private Activity context;

    public CharacterMainAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
    }

    private static class ViewHolder{
        public ImageView im_class, im_alive, im_status;
        public TextView tx_nickname;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.favorite_character_row, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.tx_nickname = (TextView) rowView.findViewById(R.id.cv_nick);
            viewHolder.im_class = (ImageView) rowView.findViewById(R.id.cv_profa);
            viewHolder.im_alive = (ImageView) rowView.findViewById(R.id.cv_live);
            viewHolder.im_status = (ImageView) rowView.findViewById(R.id.cv_status);

            rowView.setTag(viewHolder);
        }
        // save views
        ViewHolder holder = (ViewHolder) rowView.getTag();
        // fill data
        Character s = ((Character)getItem(position));

        if(s!=null){

            //nickname
            if(s.getName() != null){
                holder.tx_nickname.setText(s.getName());
            }

            //profession
            if(s.getClas() != null){
                try {
                    holder.im_class.setImageDrawable(Mu.convert2Drawable(context.getAssets(), s.getClas(), false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //dead/live
            if(s.getIsLive() != null){
                //TODO: dead/live
            }
            //status
            if(s.getStatus() != null){
                if(s.getStatus().equals("Online"))
                    holder.im_status.setImageResource(R.drawable.online);
                else
                    holder.im_status.setImageResource(R.drawable.offline);
            }

        }

        return rowView;
    }



}
