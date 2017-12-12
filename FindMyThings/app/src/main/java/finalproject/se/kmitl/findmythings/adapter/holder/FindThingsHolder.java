package finalproject.se.kmitl.findmythings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import finalproject.se.kmitl.findmythings.R;


public class FindThingsHolder extends RecyclerView.ViewHolder{
    public TextView postTitle;
    public View postLinear;
    public FindThingsHolder(View itemView) {
        super(itemView);
        postTitle = itemView.findViewById(R.id.postTitle);
        postLinear = itemView.findViewById(R.id.findThingLinear);
    }
}
