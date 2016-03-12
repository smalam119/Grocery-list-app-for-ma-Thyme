package com.test.smalam.grocerylist.com.test.smalam.grocerylist.about;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.test.smalam.grocerylist.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = (ListView) findViewById(R.id.listView_icon_credit);
        CustomAdapterForAbout customAdapterForAbout = new CustomAdapterForAbout(getBaseContext());
        lv.setAdapter(customAdapterForAbout);

    }

}
