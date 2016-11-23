package com.pseudozero.thyme.thyme.about;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.pseudozero.thyme.thyme.R;

public class ScrollingActivity extends AppCompatActivity {

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView iv = (ImageView) findViewById(R.id.syd_pp);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i == 7) {
                    Toast.makeText(getApplicationContext(),"If you see this message, i really want to tell you that I love you. " +
                            "My love for you is an endless ocean which is pure as platinum. I fall for your simplicity and " +
                            "piousness, the day I have first seen you. Please give me a chance. Don't fade away......",Toast.LENGTH_LONG).show();
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.pseudo_zero);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),IconCreditActivity.class);
                startActivity(i);
            }
        });
    }
}
