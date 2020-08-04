package com.enricot44.publast.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.enricot44.publast.R;
import com.enricot44.publast.service.model.Cocktail;

import java.util.ArrayList;
import java.util.List;

public class CocktailAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Cocktail> cocktails;
    private ItemClickListener itemClickListener;

    public CocktailAdapter(Context context, ArrayList<Cocktail> cocktails) {
        this.context = context;
        this.cocktails = cocktails;
    }

    public void addClickItemImplementation(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CocktailHolder(LayoutInflater.from(context).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Cocktail cocktail = cocktails.get(position);

        ((CocktailHolder) holder).txtCategory.setText(cocktail.getCategory());
        ((CocktailHolder) holder).txtName.setText(cocktail.getName());
        ((CocktailHolder) holder).imgThumbnail.setImageBitmap(cocktail.getThumbnail());

        ((CocktailHolder) holder).imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(position, holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cocktails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.rv_item_saved_cocktail;
    }

    public ArrayList<Cocktail> getItems() {
        return (ArrayList<Cocktail>) cocktails;
    }

    public void setThumbnailImage() {
        for (int i = 0; i < cocktails.size(); i++) {
            final Cocktail cocktail = cocktails.get(i);
            final int position = i;
            Glide.with(context).asBitmap().load(Uri.parse(cocktail.getThumbnailUrl())).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                    cocktail.setThumbnail(resource);
                    notifyItemChanged(position);
                }

                @Override
                public void onLoadCleared(Drawable placeholder) {

                }
            });
        }
    }

    public void setCocktails(List<Cocktail> cocktails) {
        this.cocktails = cocktails;
        notifyDataSetChanged();
    }

    class CocktailHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtCategory;
        ImageView imgThumbnail;

        public CocktailHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.rv_item_saved_cocktail_name);
            txtCategory = itemView.findViewById(R.id.rv_item_saved_cocktail_desc);
            imgThumbnail = itemView.findViewById(R.id.rv_item_saved_cocktail_image);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, View clickedView);
    }
}
