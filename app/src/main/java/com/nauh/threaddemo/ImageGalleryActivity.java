package com.nauh.threaddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private ProgressBar progressBarOverall;
    private TextView textViewProgress;

    private List<ImageItem> imageItems = new ArrayList<>();
    private int totalImages = 0;
    private int loadedImages = 0;

    // List of image URLs to download
    private final String[] imageUrls = {
            "https://fdn2.gsmarena.com/vv/pics/apple/apple-iphone-14-pro-max-1.jpg",
            "https://fdn2.gsmarena.com/vv/pics/samsung/samsung-galaxy-s23-ultra-5g-1.jpg",
            "https://fdn2.gsmarena.com/vv/pics/google/google-pixel-8-pro-1.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewImages);
        progressBarOverall = findViewById(R.id.progressBarOverall);
        textViewProgress = findViewById(R.id.textViewProgress);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Initialize image items
        totalImages = imageUrls.length;
        progressBarOverall.setMax(totalImages);
        textViewProgress.setText("Loading images: 0/" + totalImages);
        
        for (int i = 0; i < imageUrls.length; i++) {
            String url = imageUrls[i];
            String name = getImageNameFromUrl(url);
            imageItems.add(new ImageItem(url, name));
        }
        
        // Set up adapter
        adapter = new ImageAdapter(imageItems);
        recyclerView.setAdapter(adapter);
        
        // Start downloading images
        for (int i = 0; i < imageItems.size(); i++) {
            new ImageDownloadTask(i).execute(imageItems.get(i).getUrl());
        }
    }
    
    private String getImageNameFromUrl(String url) {
        // Extract a simple name from the URL
        String[] parts = url.split("/");
        if (parts.length > 0) {
            String filename = parts[parts.length - 1];
            // Remove file extension if present
            if (filename.contains(".")) {
                filename = filename.substring(0, filename.lastIndexOf('.'));
            }
            // Replace hyphens with spaces and capitalize words
            filename = filename.replace('-', ' ');
            StringBuilder sb = new StringBuilder();
            boolean capitalizeNext = true;
            for (char c : filename.toCharArray()) {
                if (c == ' ') {
                    capitalizeNext = true;
                    sb.append(c);
                } else {
                    if (capitalizeNext) {
                        sb.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        sb.append(c);
                    }
                }
            }
            return sb.toString();
        }
        return "Unknown";
    }
    
    // AsyncTask to download images
    private class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        private final int position;
        
        public ImageDownloadTask(int position) {
            this.position = position;
        }
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageItems.get(position).setStatus("Downloading...");
            adapter.notifyItemChanged(position);
        }
        
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                
                // Get the content length for progress updates
                int contentLength = connection.getContentLength();
                int totalBytesRead = 0;
                
                InputStream input = connection.getInputStream();
                
                // Create a temporary file to store the downloaded data
                byte[] buffer = new byte[4096];
                int bytesRead;
                
                // Read the input stream in chunks and update progress
                while ((bytesRead = input.read(buffer)) != -1) {
                    totalBytesRead += bytesRead;
                    if (contentLength > 0) {
                        // Calculate progress percentage
                        int progress = (int) ((totalBytesRead / (float) contentLength) * 100);
                        publishProgress(progress);
                    }
                    
                    // Simulate slower download for demonstration
                    Thread.sleep(50);
                }
                
                // Convert the downloaded data to a bitmap
                return BitmapFactory.decodeStream(connection.getInputStream());
                
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Update the individual item's progress
            imageItems.get(position).setProgress(values[0]);
            adapter.notifyItemChanged(position);
        }
        
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            
            // Update the image item with the downloaded bitmap
            ImageItem item = imageItems.get(position);
            if (bitmap != null) {
                item.setBitmap(bitmap);
                item.setStatus("Downloaded");
            } else {
                item.setStatus("Error");
            }
            
            // Hide progress bar for this item
            item.setShowProgress(false);
            
            // Update the adapter
            adapter.notifyItemChanged(position);
            
            // Update overall progress
            loadedImages++;
            progressBarOverall.setProgress(loadedImages);
            textViewProgress.setText("Loading images: " + loadedImages + "/" + totalImages);
        }
    }
}
