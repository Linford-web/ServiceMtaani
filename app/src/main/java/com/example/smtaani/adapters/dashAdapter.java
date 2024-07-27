package com.example.smtaani.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smtaani.MainDashboard;
import com.example.smtaani.R;
import com.example.smtaani.models.dashModel;

import java.util.ArrayList;
import java.util.List;

public class dashAdapter extends RecyclerView.Adapter<dashAdapter.MyViewHolder> {

    Context context;
    List<dashModel> itemList;

    public dashAdapter(MainDashboard mainDashboard, List<dashModel> itemList) {
        this.itemList = itemList;
        this.context = mainDashboard;
    }

    @NonNull
    @Override
    public dashAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dash, viewGroup, false);
        return new dashAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull dashAdapter.MyViewHolder myViewHolder, int i) {

        dashModel item = itemList.get(i);
        myViewHolder.imageView.setImageResource(item.getImageId());
        myViewHolder.textView.setText(item.getTitle());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, item.getActivityClass());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_icon);
            textView = itemView.findViewById(R.id.item_name);
            cardView = itemView.findViewById(R.id.button);
        }
    }
}
