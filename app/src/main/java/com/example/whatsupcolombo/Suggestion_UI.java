package com.example.whatsupcolombo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

                        viewHolder.setDetails(getActivity().getApplicationContext() , model.getTitle(), model.getDescription(), model.getImage());

                    }
                };

        //set adapter to recycler view
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}


