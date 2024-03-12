package com.firstapp.app.helperclasses;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firstapp.app.database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvImporter {
    private static final int REQUEST_PERMISSION = 100;
    private static final int PICK_CSV_FILE = 101;

    private Database db;
    Activity activity;

    public CsvImporter(Database db, Activity activity) {
        this.db = db;
        this.activity = activity;
    }

    public void checkPermissionAndOpenFilePicker() {
        if (ContextCompat.checkSelfPermission(this.activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*"); // Allow all file types
        intent.setType("text/comma-separated-values");
//        String[] mimeTypes = {"text/csv", "text/comma-separated-values", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes); // Set MIME type filter
        this.activity.startActivityForResult(Intent.createChooser(intent, "Select CSV File"), PICK_CSV_FILE);

    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    readAndParseCSV(context, uri);
                }
            }
        }
    }

    private void readAndParseCSV(Context context, Uri uri) {

        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                CSVReaderHelper csvReaderHelper = new CSVReaderHelper();
                csvReaderHelper.readAndCheckCSVContent(reader, context);
                inputStream.close();
            } else {
                Log.e("FileImportHandler", "Input stream is null.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
