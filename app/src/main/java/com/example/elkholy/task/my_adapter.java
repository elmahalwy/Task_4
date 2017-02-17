package com.example.elkholy.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by elkholy on 16/02/2017.
 */

public class my_adapter extends RecyclerView.Adapter<my_adapter.ViewHolder> {
    public Items[] links;
    public Context mcontext;
    public ItemClickListner clickListener;
public my_adapter(Items[] links , Context mcontext){
    this.links = links;
    this.mcontext=mcontext;
}
    @Override
    public my_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(my_adapter.ViewHolder holder, int position) {
        holder.tv_name.setText(links[position].name);
        holder.tv_download.setText("Download");

    }

    @Override
    public int getItemCount() {
        return links.length;
    }
    public void setClickListener(ItemClickListner itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        TextView tv_download = (TextView) itemView.findViewById(R.id.tv_download);

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(itemView);
            tv_download.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

                clickListener.onClick(tv_download, getPosition());

        }
    }
}
