package com.example.whatsupcolombo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

public class Suggestion_UI extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.suggest_ui, container, false);

        getActivity().setTitle("Suggestions");
        //recycler view
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //set layout as liner layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //send query to firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Data");

        return rootView;

    }

    //search data
    public void firebaseSearch(String searchtext) {
        Query firebasesearchquery = databaseReference.orderByChild("title").startAt(searchtext).endAt(searchtext + "\uf8ff");
        FirebaseRecyclerAdapter<EventDisplayModel, EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<EventDisplayModel, EventViewHolder>(
                EventDisplayModel.class,
                R.layout.row,
                EventViewHolder.class,
                firebasesearchquery
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, EventDisplayModel model, int position) {
                viewHolder.setDetails(getActivity().getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());
            }

            @Override
            public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                EventViewHolder eventViewHolder = super.onCreateViewHolder(parent, viewType);
                eventViewHolder.setOnClickListener(new EventViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Viewa
                        TextView tv_tilte = view.findViewById(R.id.tv_title);
                        TextView tv_description = view.findViewById(R.id.tv_description);
                        ImageView tmg_Image = view.findViewById(R.id.img_eventphoto);

                        //get data
                        String title = tv_tilte.getText().toString();
                        String decription = tv_description.getText().toString();
                        Drawable drawable = tmg_Image.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                        //pass data to activity
                        Intent intent = new Intent(view.getContext(), EventDataActivity.class);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        intent.putExtra("image", bytes);
                        intent.putExtra("title", title);
                        intent.putExtra("description", decription);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        //implementation
                    }
                });
                return eventViewHolder;
            }
        };

        //set adapter to recycler view
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //load data to recycle view on Start
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<EventDisplayModel, EventViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<EventDisplayModel, EventViewHolder>(
                        EventDisplayModel.class,
                        R.layout.row,
                        EventViewHolder.class,
                        databaseReference
                ) {
                    @Override
                    protected void populateViewHolder(EventViewHolder viewHolder, EventDisplayModel model, int position) {
                        viewHolder.setDetails(getActivity().getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());
                    }

                    @Override
                    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        EventViewHolder eventViewHolder = super.onCreateViewHolder(parent, viewType);
                        eventViewHolder.setOnClickListener(new EventViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Viewa
                                TextView tv_tilte = view.findViewById(R.id.tv_title);
                                TextView tv_description = view.findViewById(R.id.tv_description);
                                ImageView tmg_Image = view.findViewById(R.id.img_eventphoto);

                                //get data
                                String title = tv_tilte.getText().toString();
                                String decription = tv_description.getText().toString();
                                Drawable drawable = tmg_Image.getDrawable();
                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                                //pass data to activity
                                Intent intent = new Intent(view.getContext(), EventDataActivity.class);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] bytes = byteArrayOutputStream.toByteArray();
                                intent.putExtra("image", bytes);
                                intent.putExtra("title", title);
                                intent.putExtra("description", decription);
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //implementation
                            }
                        });
                        return eventViewHolder;
                    }
                };

        //set adapter to recycler view
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d("MyApp", "I am here");
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String x = s;
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //filter while type
                firebaseSearch(s);
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}


