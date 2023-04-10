package com.example.bitebook_new;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DecideCardAdapter extends RecyclerView.Adapter<DecideCardAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Entry> data;
    private int randomNum;
    private boolean isClicked;


    DecideCardAdapter(Context content, List<Entry> data, int randomNum, boolean isClicked) {
        this.layoutInflater = LayoutInflater.from(content);
        this.data = data;
        this.randomNum = randomNum;
        this.isClicked = isClicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.decide_card, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data.get(position).getResName();
        holder.title.setText(title);

        if (position == randomNum && isClicked) {
            holder.title.setBackground(ContextCompat.getDrawable(holder.title.getContext(), R.drawable.element_border_selected));
            holder.title.setTextColor(R.color.lettuce);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.choice);
        }
    }
}
