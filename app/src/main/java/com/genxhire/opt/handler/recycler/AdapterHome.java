package com.genxhire.opt.handler.recycler;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genxhire.opt.handler.R;

import java.util.List;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {
    List<RecyclerViewModel> list;
    Context context;

    // Constructor for initialization
    public AdapterHome(Context context, List<RecyclerViewModel> logText) {
        this.context = context;
        this.list = logText;
    }

    @NonNull
    @Override
    public AdapterHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_activity, parent, false);

        // Passing view to ViewHolder
        AdapterHome.ViewHolder viewHolder = new AdapterHome.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull AdapterHome.ViewHolder holder, int position) {
        // TypeCast Object to int type
        RecyclerViewModel recyclerViewModel = list.get(position);
        holder.text.setText(recyclerViewModel.getLogText());
        if(recyclerViewModel.getColorInt() == 1) {
            holder.text.setTextColor(Color.GREEN);
        }else if(recyclerViewModel.getColorInt() == 2){
            holder.text.setTextColor(Color.YELLOW);
        }else if(recyclerViewModel.getColorInt() == 3){
            holder.text.setTextColor(Color.GREEN);
        }


        else{
            holder.text.setTextColor(Color.WHITE);

        }

    }

    @Override
    public int getItemCount() {
        // Returns number of items
        // currently available in Adapter
        return list.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.logText);
        }
    }
}
