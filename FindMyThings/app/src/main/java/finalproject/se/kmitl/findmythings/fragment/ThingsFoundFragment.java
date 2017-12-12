package finalproject.se.kmitl.findmythings.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.Iterator;

import finalproject.se.kmitl.findmythings.activity.CreateNewPostActivity;
import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.FoundThingsAdapter;
import finalproject.se.kmitl.findmythings.model.FoundThingsPost;
import finalproject.se.kmitl.findmythings.model.PostModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThingsFoundFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton btnNewPost;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private PostModel postModel;
    private FoundThingsAdapter foundThingsAdapter;
    private FoundThingsPost foundThingsPost;

    public ThingsFoundFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the nav_header_main for this fragment\
        View view = inflater.inflate(R.layout.fragment_things_found, container, false);

        initInstance(view);
        setupRecyclerView();
        storeFoundPost();
        return view;
    }

    private void initInstance(View view) {

        recyclerView = view.findViewById(R.id.foundThingsList);
        btnNewPost = view.findViewById(R.id.fabFound);
        btnNewPost.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("found");
        postModel = new PostModel();
    }

    private void setupRecyclerView() {
        foundThingsAdapter = new FoundThingsAdapter(getActivity());
        foundThingsAdapter.setData(postModel.getFoundThingsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(foundThingsAdapter);
    }

    private void storeFoundPost() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onGetChild(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onGetChild(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String date = (String) ((DataSnapshot) i.next()).getValue();
            String desc = (String) ((DataSnapshot) i.next()).getValue();
            String image = (String) ((DataSnapshot) i.next()).getValue();
            String userKey = (String) ((DataSnapshot) i.next()).getValue();
            String title = (String) ((DataSnapshot) i.next()).getValue();
            String type = (String) ((DataSnapshot) i.next()).getValue();
            String key = dataSnapshot.getKey();
            foundThingsPost = new FoundThingsPost();
            foundThingsPost.setTitle(title);
            foundThingsPost.setKey(key);
            postModel.addFoundThingsList(foundThingsPost);
        }
        int position = foundThingsAdapter.getItemCount() - 1;
        if (position >= 0) {
            recyclerView.smoothScrollToPosition(position);
        }
        //foundThingsAdapter.notifyDataSetChanged();
        foundThingsAdapter.notifyItemRangeChanged(0, foundThingsAdapter.getItemCount());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabFound) {
            Intent intent = new Intent(getActivity(), CreateNewPostActivity.class);
            intent.putExtra("fragmenttype", "found");
            startActivity(intent);
        }
    }
}
