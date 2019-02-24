package com.example.whatsupcolombo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDataActivity extends AppCompatActivity {

    private TextView title;
    private ImageView imageView;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);

        //action bar
        ActionBar actionBar = getSupportActionBar();
        //title
        actionBar.setTitle("Event Details");
        //back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setupUI();

        //get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("image");
        String Gtitle = getIntent().getStringExtra("title");
        String Gdescription = getIntent().getStringExtra("description");
        Bitmap Gbitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        //set data
        title.setText(Gtitle);
        description.setText(Gdescription);
        imageView.setImageBitmap(Gbitmap);


    }

    //handle on back pressed

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupUI() {
        title = (TextView) findViewById(R.id.data_tv_title);
        imageView = (ImageView) findViewById(R.id.data_img_eventphoto);
        description = (TextView) findViewById(R.id.data_tv_description);
    }
}
