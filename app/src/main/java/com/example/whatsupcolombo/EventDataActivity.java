package com.example.whatsupcolombo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDataActivity extends AppCompatActivity {

    private TextView title;
    private ImageView imageView;
    private TextView description;
    private TextView location;
    private Button viewLocationbutton;

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
        String gLocation = getIntent().getStringExtra("location");
        Bitmap Gbitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        //set data
        title.setText(Gtitle);
        description.setText(Gdescription);
        location.setText(gLocation);
        imageView.setImageBitmap(Gbitmap);

        //go to view maps
        viewLocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDataActivity.this,ViewMapsActivity.class);
                intent.putExtra("EventLocation",location.getText().toString());
                startActivity(intent);
            }
        });


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
        location = (TextView) findViewById(R.id.data_tv_location);
        viewLocationbutton = (Button) findViewById(R.id.data_view_location);
    }
}
