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
import finalproject.se.kmitl.findmythings.adapter.holder.FoundThingsHolder;
import finalproject.se.kmitl.findmythings.model.FindThingsPost;
import finalproject.se.kmitl.findmythings.model.FoundThingsPost;

/**
 * Created by SMART on 13/11/2560.
 */

public class FoundThingsAdapter extends RecyclerView.Adapter<FoundThingsHolder> {
    private final Context context;
    private List<FoundThingsPost> data;

    public FoundThingsAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    public void setData(List<FoundThingsPost> data) {
        this.data = data;
    }

    @Override
    public FoundThingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.foundthings_row, parent, false);
        FoundThingsHolder holder = new FoundThingsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FoundThingsHolder holder, int position) {
        holder.postTitle.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
