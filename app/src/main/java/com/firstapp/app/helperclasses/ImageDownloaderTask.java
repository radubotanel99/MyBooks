package com.firstapp.app.helperclasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDownloaderTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "ImageDownloaderTask";
    private Context context;

    public ImageDownloaderTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (params.length == 0) {
            return false;
        }

        String imageUrl = params[0];
        String title = params[1];
        String isbn = params[2];
        String folderPath = context.getFilesDir() + "/books_Images/";
        String fileName = title + isbn + ".png"; // Change the file name as needed

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();

            Drawable drawable = Drawable.createFromStream(input, null);
            if (drawable != null) {
                Bitmap bitmap = null;
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    bitmap = bitmapDrawable.getBitmap();
                }
                File directory = new File(folderPath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                File imageFile = new File(directory, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            input.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error downloading image: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Log.d(TAG, "Image downloaded and saved successfully.");
        } else {
            Log.e(TAG, "Failed to download and save image.");
        }
    }
}

