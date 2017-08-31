package com.pseudozero.thyme.thyme.about;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.settings.SettingsData;

import java.util.ArrayList;

/**
 * Created by SAYED on 3/12/2016.
 */
public class AboutCustomAdapter extends BaseAdapter
{
    Context context;
    ArrayList<IconCreditSingleRow> authorIcon;
    SettingsData settings;

    AboutCustomAdapter(Context c) {
        context = c;
        authorIcon = new ArrayList<IconCreditSingleRow>();
        settings = new SettingsData();
        settings.getSetting(context);

        addAllAuthors();
    }

    public void addAllAuthors()
    {
        authorIcon.add(new IconCreditSingleRow("Balraj Chana",R.drawable.menu_option_add,"http://www.flaticon.com/authors/balraj-chana"));
        authorIcon.add(new IconCreditSingleRow("Freepik",R.drawable.option_menu_edit_blue,"http://www.freepik.com"));
        authorIcon.add(new IconCreditSingleRow("Freepik",R.drawable.option_menu_fav_blue,"http://www.freepik.com"));
        authorIcon.add(new IconCreditSingleRow("Freepik",R.drawable.previous_note_icon_blue,"http://www.freepik.com"));
        authorIcon.add(new IconCreditSingleRow("Nice and Serious",R.drawable.option_menu_reminder_blue,"http://www.flaticon.com/authors/nice-and-serious"));
        authorIcon.add(new IconCreditSingleRow("Freepik",R.drawable.save,"http://www.freepik.com"));
        authorIcon.add(new IconCreditSingleRow("Freepik",R.drawable.action_setting_icon_blue,"http://www.freepik.com"));
        authorIcon.add(new IconCreditSingleRow("Nice and Serious",R.drawable.previous_to_do_icon_blue,"http://www.flaticon.com/authors/nice-and-serious"));
        authorIcon.add(new IconCreditSingleRow("Icomoon",R.drawable.option_menu_trash_blue,"http://www.flaticon.com/authors/icomoon"));
    }


    @Override
    public int getCount() {
        return authorIcon.size();
    }

    @Override
    public Object getItem(int position) {
        return authorIcon.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final IconCreditSingleRow singleRowForIconCredit = authorIcon.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.single_row_about, parent, false);
        TextView tvTitle = (TextView) rowView.findViewById(R.id.textView_title_single_row_about);
        ImageView iv = (ImageView) rowView.findViewById(R.id.icon_about);
        tvTitle.setText(singleRowForIconCredit.getName());
        tvTitle.setTypeface(Typeface.createFromAsset(context.getAssets(), settings.getFont(settings.getFontNumber())));
        iv.setImageResource(singleRowForIconCredit.getImage());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse(singleRowForIconCredit.getUrl()));
                    context.startActivity(i);

            }
        });

        return rowView;
    }
}
