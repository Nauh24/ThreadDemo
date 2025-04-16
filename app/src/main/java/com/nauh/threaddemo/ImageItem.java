package com.nauh.threaddemo;

import android.graphics.Bitmap;

public class ImageItem {
    private String url;
    private String name;
    private Bitmap bitmap;
    private String status;
    private int progress;
    private boolean showProgress;

    public ImageItem(String url, String name) {
        this.url = url;
        this.name = name;
        this.status = "Pending";
        this.progress = 0;
        this.showProgress = true;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }
}
