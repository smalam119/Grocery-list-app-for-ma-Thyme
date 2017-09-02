package com.pseudozero.thyme.thyme.settings;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.pseudozero.thyme.thyme.R;
import com.pseudozero.thyme.thyme.about.ScrollingActivity;
import com.pseudozero.thyme.thyme.database.GroceryListDatabaseHelper;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase mSqliteDB;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    ContentValues cv;
    RadioButton f1,f2,f3,f4;
    RadioButton s1,s2,s3;
    LinearLayout linearLayout;

    LoginButton loginButton;
    CallbackManager mCallbackManager;
    String TAG ="Hey";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearLayout = (LinearLayout) findViewById(R.id.setting_activity_linear_layout);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        setSupportActionBar(toolbar);

        SettingsData settings = new SettingsData();

        //NotyAlert.showWarning(SettingsActivity.this,linearLayout,"Age starts showing");

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");

        //registering fb callback
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.i(TAG,"Hello"+loginResult.getAccessToken().getToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });

        //firebase auth state listener
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!=null){
                    name = user.getDisplayName();
                    Toast.makeText(SettingsActivity.this,""+user.getDisplayName(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(SettingsActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
                }


            }
        };


        try
        {
            SQLiteOpenHelper groceryListDatabaseHelper = new GroceryListDatabaseHelper(this);
            mSqliteDB = groceryListDatabaseHelper.getWritableDatabase();
        }
        catch(SQLiteException e)
        {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Typeface droidSansMono = Typeface.createFromAsset(getAssets(),"fonts/DroidSansMono.ttf");
        Typeface archRival = Typeface.createFromAsset(getAssets(),"fonts/SF_Arch_Rival.ttf");
        Typeface raleway = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Regular.ttf");
        Typeface walkway = Typeface.createFromAsset(getAssets(),"fonts/Walkway_Black.ttf");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.what_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ScrollingActivity.class);
                startActivity(i);
            }
        });

        Button c1,c2,c3,c4;

        c1 = (Button) findViewById(R.id.c1);
        c2 = (Button) findViewById(R.id.c2);
        c3 = (Button) findViewById(R.id.c3);
        c4 = (Button) findViewById(R.id.c4);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);
        c4.setOnClickListener(this);



        f1 = (RadioButton) findViewById(R.id.f1);
        f2 = (RadioButton) findViewById(R.id.f2);
        f3 = (RadioButton) findViewById(R.id.f3);
        f4 = (RadioButton) findViewById(R.id.f4);

        f1.setTypeface(droidSansMono);
        f2.setTypeface(archRival);
        f3.setTypeface(raleway);
        f4.setTypeface(walkway);

        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f4.setOnClickListener(this);


        s1 = (RadioButton) findViewById(R.id.small_text);
        s2 = (RadioButton) findViewById(R.id.medium_text);
        s3 = (RadioButton) findViewById(R.id.large_text);

        s1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_small));

        s2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_medium));

        s3.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.text_large));

        s1.setOnClickListener(this);
        s2.setOnClickListener(this);
        s3.setOnClickListener(this);

        settings.getSetting(this);

        currentFont(settings.getFontNumber());
        currentFontSize(settings.getFontSizeNumber());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //handling facebook access token
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SettingsActivity.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SettingsActivity.this, "Authentication error",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.c1:
                fontColorChange(0);
                break;

            case R.id.c2:
                fontColorChange(1);
                break;

            case R.id.c3:
                fontColorChange(2);
                break;

            case R.id.c4:
                fontColorChange(3);
                break;

            case R.id.f1:
                fontChange(0);
                break;

            case R.id.f2:
                fontChange(1);
                break;

            case R.id.f3:
                fontChange(2);
                break;

            case R.id.f4:
                fontChange(3);
                break;

            case R.id.small_text:
                fontSizeChange(0);
                break;

            case R.id.medium_text:
                fontSizeChange(1);
                break;

            case R.id.large_text:
                fontSizeChange(2);
                break;

            default:

        }
    }

    public void fontChange(int fontNumber)
    {
        cv = new ContentValues();
        cv.put("FONT_FAMILY",fontNumber);
        mSqliteDB.update("SETTINGS", cv, "_id=?", new String[]{"1"});
    }

    public void fontColorChange(int fontColorNumber)
    {
        //cv = new ContentValues();
        //cv.put("FONT_COLOR",fontColorNumber);
        //mSqliteDB.update("SETTINGS", cv, "_id=?", new String[]{"1"});
        Toast.makeText(getBaseContext(),"Not available right now",Toast.LENGTH_SHORT).show();
    }

    public void fontSizeChange(int fontSizeNumber)
    {
        cv = new ContentValues();
        cv.put("FONT_SIZE",fontSizeNumber);
        mSqliteDB.update("SETTINGS", cv, "_id=?", new String[]{"1"});
    }

    public void currentFont(int info)
    {
        switch(info)
        {
            case 0 : f1.setChecked(true);
                break;
            case 1 : f2.setChecked(true);
                break;
            case 2 : f3.setChecked(true);
                break;
            case 3 : f4.setChecked(true);
                break;
        }
    }

    public void currentFontSize(int info)
    {
        switch(info)
        {
            case 0 : s1.setChecked(true);
                break;
            case 1 : s2.setChecked(true);
                break;
            case 2 : s3.setChecked(true);
                break;
        }
    }

}
