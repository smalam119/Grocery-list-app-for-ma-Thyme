package com.pseudozero.thyme.thyme.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.pseudozero.thyme.thyme.R;

public class IconCreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_credit);

        ListView lv = (ListView) findViewById(R.id.listView_icon_credit);
        AboutCustomAdapter customAdapterForAbout = new AboutCustomAdapter(getBaseContext());
        lv.setAdapter(customAdapterForAbout);

    }

}
