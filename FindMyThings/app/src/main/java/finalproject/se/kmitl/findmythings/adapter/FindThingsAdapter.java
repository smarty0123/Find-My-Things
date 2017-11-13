package finalproject.se.kmitl.findmythings.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import finalproject.se.kmitl.findmythings.R;
import finalproject.se.kmitl.findmythings.adapter.holder.FindThingsHolder;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;

/**
 * Created by SMART on 12/11/2560.
 */

public class FindThingsAdapter extends RecyclerView.Adapter<FindThingsHolder>{

    private Context context;
    private List<FindThingsPost> data;
    public FindThingsAdapter(Context context){
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<FindThingsPost> data) {
        this.data = data;
    }

    @Override
    public FindThingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.findthings_row, parent, false);
        FindThingsHolder holder = new FindThingsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FindThingsHolder holder, int position) {
        holder.postTitle.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
