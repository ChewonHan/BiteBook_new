package com.example.bitebook_new;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Entry> data;

    private boolean isExpanded = false;


//    HomeCardAdapter(Context content, List<String> data) {
//        this.layoutInflater = LayoutInflater.from(content);
//        this.data = data;
//    }



    HomeCardAdapter(Context content, List<Entry> data) {
        this.layoutInflater = LayoutInflater.from(content);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.home_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Entry entry = data.get(position);

        String title = entry.getResName();
        holder.title.setText(title);

        // before expand the CardView
        holder.resNameCard.setVisibility(View.GONE);
        holder.price.setVisibility(View.GONE);
        holder.area.setVisibility(View.GONE);
        holder.cuisine.setVisibility(View.GONE);
        holder.line3.setVisibility(View.GONE);
        holder.description.setVisibility(View.GONE);

        if (entry.getImage() == null){
            holder.image.setVisibility(View.GONE);
            holder.line2.setVisibility(View.GONE);
        }
        else{
            Picasso
                    .get()
                    .load(entry.getImage())
                    .into(holder.image);
        }

        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;

                if (isExpanded){
                    holder.resNameCard.setVisibility(View.VISIBLE);
                    holder.price.setVisibility(View.VISIBLE);
                    holder.area.setVisibility(View.VISIBLE);
                    holder.cuisine.setVisibility(View.VISIBLE);
                    holder.line3.setVisibility(View.VISIBLE);

                    if (entry.getReview() != null) {
                        holder.description.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    holder.resNameCard.setVisibility(View.GONE);
                    holder.price.setVisibility(View.GONE);
                    holder.area.setVisibility(View.GONE);
                    holder.cuisine.setVisibility(View.GONE);
                    holder.line3.setVisibility(View.GONE);
                    holder.description.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, resNameCard, price, area, cuisine;
        ImageView image;
        View line2, line3;
        ImageView moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.review);
            image = itemView.findViewById(R.id.foodPic);
            line2 = itemView.findViewById(R.id.line2);
            moreButton = itemView.findViewById(R.id.moreIcon);

            // items to be shown when the button is clicked
            resNameCard = itemView.findViewById(R.id.resNameCard);
            price = itemView.findViewById(R.id.price);
            area = itemView.findViewById(R.id.area);
            cuisine = itemView.findViewById(R.id.cuisine);
            line3 = itemView.findViewById(R.id.line3);

        }
    }
}
