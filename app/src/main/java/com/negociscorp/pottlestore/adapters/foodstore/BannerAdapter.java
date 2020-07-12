package com.negociscorp.pottlestore.adapters.foodstore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.negociscorp.pottlestore.R;
import com.negociscorp.pottlestore.models.BannerItem;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<BannerItem> bannerItems;
    private ViewPager2 viewPager2;

    public BannerAdapter(List<BannerItem> bannerItems, ViewPager2 viewPager2) {
        this.bannerItems = bannerItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.banner_image_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.setImage(bannerItems.get(position));
        if(position == bannerItems.size()-2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return bannerItems.size();
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerSlide);
        }

        void setImage(BannerItem bannerItem) {
            imageView.setImageResource(bannerItem.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bannerItems.addAll(bannerItems);
            notifyDataSetChanged();
        }
    };
}
