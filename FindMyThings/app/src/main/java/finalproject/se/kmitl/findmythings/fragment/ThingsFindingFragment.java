package finalproject.se.kmitl.findmythings.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.Iterator;

import finalproject.se.kmitl.findmythings.activity.CreateNewPostActivity;
import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.FindThingsAdapter;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;
import finalproject.se.kmitl.findmythings.model.PostModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThingsFindingFragment extends Fragment implements View.OnClickListener{
    private FloatingActionButton btnNewPost;
    private DatabaseReference mDatabase;
    private FindThingsAdapter findThingsAdapter;
    private RecyclerView recyclerView;
    private PostModel postModel;
    private FindThingsPost findThingsPost;
    private String desc;
    private String image;
    private String title;

    public ThingsFindingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment\
        View view = inflater.inflate(R.layout.fragment_things_finding, container, false);
        initInstance(view);
        setupRecyclerView();
        storeFindPost();
        return view;
    }


    private void initInstance(View view){
        Log.i("", "initInstance: ");
        recyclerView = view.findViewById(R.id.findThingsList);
        btnNewPost = view.findViewById(R.id.fabFinding);
        btnNewPost.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("find");
        postModel = new PostModel();
    }

    private void setupRecyclerView() {
        Log.i("", "setupRecyclerView: ");
        findThingsAdapter = new FindThingsAdapter(getActivity());
        findThingsAdapter.setData(postModel.getFindThingsList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(findThingsAdapter);
    }

    private void storeFindPost(){
        Log.i("", "storeFindPost: ");

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
            desc = (String) ((DataSnapshot) i.next()).getValue();
            image = (String) ((DataSnapshot) i.next()).getValue();
            title = (String) ((DataSnapshot) i.next()).getValue();
            findThingsPost = new FindThingsPost();
            findThingsPost.setTitle(title);
            postModel.addFindThingsList(findThingsPost);
        }
        int position = findThingsAdapter.getItemCount() - 1;
        if(position >= 0){
            recyclerView.smoothScrollToPosition(position);
        }
        findThingsAdapter.notifyItemRangeChanged(0, findThingsAdapter.getItemCount());

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabFinding) {
            Intent intent = new Intent(getActivity(), CreateNewPostActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("fragmenttype", "find");
            startActivity(intent);
            getActivity().finish();
            getActivity().getFragmentManager().popBackStack();
        }
    }
}
