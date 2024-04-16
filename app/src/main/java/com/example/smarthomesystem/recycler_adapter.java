package com.example.smarthomesystem;

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

import java.util.ArrayList;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder> {

    private final ArrayList<com.example.smarthomesystem.recycler_list> recycler_list;
    private final Context context;

    public recycler_adapter(ArrayList<com.example.smarthomesystem.recycler_list> recycler_list, Context context) {
        this.recycler_list = recycler_list;
        this.context = context;
    }

    @NonNull
    @Override
    public recycler_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycler_adapter.ViewHolder holder, int position) {
            holder.imageView.setImageResource(recycler_list.get(position).getImg());
            holder.textView.setText(recycler_list.get(position).getTxt());

            holder.cardView.setOnClickListener(e ->{
                Intent intent = new Intent(context,items.class);
                intent.putExtra("id",position);
                context.startActivity(intent);

            });
    }

    @Override
    public int getItemCount() {
        return recycler_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

        }
    }
}
