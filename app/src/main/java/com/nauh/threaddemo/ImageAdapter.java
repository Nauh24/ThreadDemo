package com.nauh.threaddemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private List<ImageItem> imageItems;

    public ImageAdapter(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem item = imageItems.get(position);

        // Set the image name
        holder.textViewImageName.setText(item.getName());

        // Set the status
        holder.textViewStatus.setText("Status: " + item.getStatus());

        // Show or hide progress bar
        if (item.isShowProgress()) {
            holder.progressBar.setVisibility(View.VISIBLE);
        } else {
            holder.progressBar.setVisibility(View.GONE);
        }

        // Set the image if available
        if (item.getBitmap() != null) {
            holder.imageView.setImageBitmap(item.getBitmap());
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewImageName;
        ImageView imageView;
        ProgressBar progressBar;
        TextView textViewStatus;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewImageName = itemView.findViewById(R.id.textViewImageName);
            imageView = itemView.findViewById(R.id.imageView);
            progressBar = itemView.findViewById(R.id.progressBar);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
        }
    }
}
