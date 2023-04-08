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
import java.util.ArrayList;
import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Entry> data;

    private boolean isExpanded = false;
    private boolean isClicked = false;


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

        String foodName = entry.getMenName();
        float rating = entry.getRating();
        holder.foodName.setText(foodName);

        // before expand the CardView
        holder.resNameCard.setVisibility(View.GONE);
        holder.price.setVisibility(View.GONE);
        holder.area.setVisibility(View.GONE);
        holder.cuisine.setVisibility(View.GONE);
        holder.line3.setVisibility(View.GONE);
        holder.description.setVisibility(View.GONE);

        // shows image depends on the input
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

        // shows the number of stars depends on the input
        if (rating == 1){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
        }else if (rating == 1.5){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.rate_star_half_icon);
        }else if (rating == 2){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
        }else if (rating == 2.5){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.rate_star_half_icon);
        }else if (rating == 3){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.satiesfied_rate_star_icon);
        }else if (rating == 3.5){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star4.setImageResource(R.drawable.rate_star_half_icon);
        }else if (rating == 4){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star4.setImageResource(R.drawable.satiesfied_rate_star_icon);
        }else if (rating == 4.5){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star4.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star5.setImageResource(R.drawable.rate_star_half_icon);
        }else if (rating == 5){
            holder.star1.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star2.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star3.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star4.setImageResource(R.drawable.satiesfied_rate_star_icon);
            holder.star5.setImageResource(R.drawable.satiesfied_rate_star_icon);
        }

        // change the fav_icon depends on the user's click
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = !isClicked;

                if (isClicked){
                    holder.favButton.setImageResource(R.drawable.fav_icon_clicked);
                    // if the favList doesn't have current food & if the button is clicked,
                    // add it to the favList

                    if (!Entry.getFavList().contains(foodName)){
                        Entry.setFavList(foodName);
                    }
                }
                else{
                    holder.favButton.setImageResource(R.drawable.fav_icon);
                    if (Entry.getFavList().contains(foodName)){
                        Entry.removeFavList(foodName);
                    }
                }
            }
        });

        // collapse/ expand the card depends on the user's click
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;

                if (isExpanded){
                    holder.moreButton.setImageResource(R.drawable.more_icon_clicked);
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
                    holder.moreButton.setImageResource(R.drawable.more_icon);
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

        TextView foodName, description, resNameCard, price, area, cuisine;
        ImageView image, star1, star2, star3, star4, star5, moreButton, favButton;
        View line2, line3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.foodName);
            description = itemView.findViewById(R.id.review);
            image = itemView.findViewById(R.id.foodPic);
            line2 = itemView.findViewById(R.id.line2);
            moreButton = itemView.findViewById(R.id.moreIcon);
            favButton = itemView.findViewById(R.id.favIcon);

            // items to be shown when the button is clicked
            resNameCard = itemView.findViewById(R.id.resNameCard);
            price = itemView.findViewById(R.id.price);
            area = itemView.findViewById(R.id.area);
            cuisine = itemView.findViewById(R.id.cuisine);
            line3 = itemView.findViewById(R.id.line3);

            // items for show rating stars
            star1 = itemView.findViewById(R.id.rateStar1);
            star2 = itemView.findViewById(R.id.rateStar2);
            star3 = itemView.findViewById(R.id.rateStar3);
            star4 = itemView.findViewById(R.id.rateStar4);
            star5 = itemView.findViewById(R.id.rateStar5);

        }
    }

//    public boolean have(String food){
//        boolean have = false;
//        for (String item : Entry.getFavList()) {
//            if (item.equals(food)) {
//                have = true;
//                break;
//            }
//        }
//        return have;
//    }
}
