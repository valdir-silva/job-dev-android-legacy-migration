package com.goomer.ps.legacy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goomer.ps.R;
import com.goomer.ps.domain.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.VH> {

    public interface OnItemClickListener {
        void onItemClick(MenuItem item);
    }

    private final List<MenuItem> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public MenuAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<MenuItem> newItems) {
        items.clear();
        if (newItems != null) items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MenuItem item = items.get(position);
        holder.name.setText(item.name);
        holder.description.setText(item.description);
        holder.price.setText(holder.itemView.getContext().getString(R.string.price, item.price));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView price;
        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            description = itemView.findViewById(R.id.tvDescription);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}
