package com.firstapp.app.helperclasses;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.firstapp.app.objects.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

import static com.firstapp.app.helperclasses.GeneralConstants.*;

public class CsvExporter {

    public CsvExporter() {
    }

    public void exportToCsv(Context context, String fileName, List<Book> books) {

        if (books == null || books.isEmpty()) {
            Toast.makeText(context, "No data to export.", Toast.LENGTH_SHORT).show();
            return;
        }

        OutputStream outputStream = null;
        try {
            Uri fileUri;
            ContentResolver resolver = context.getContentResolver();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use MediaStore API for Android 10 and above
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName + ".csv");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                fileUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

                if (fileUri == null) {
                    throw new IOException("Failed to create file");
                }
                outputStream = resolver.openOutputStream(fileUri);
            } else {
                // Use legacy storage for older versions
                File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }

                File file = new File(exportDir, fileName + ".csv");
                outputStream = new FileOutputStream(file);
            }

            // Write CSV headers
            StringBuilder csvData = new StringBuilder();
            csvData.append(escapeCsvField(TITLE) + "," +
                    escapeCsvField(CATEGORY) + "," +
                    escapeCsvField(AUTHOR) + "," +
                    escapeCsvField(PUBLISHER) + "," +
                    escapeCsvField(DESCRIPTION) + "," +
                    escapeCsvField(LENT_TO) + "," +
                    escapeCsvField(PUBLISHED_DATE) + "," +
                    escapeCsvField(PAGE_COUNT) + "," +
                    escapeCsvField(IS_DIGITAL) + "," +
                    escapeCsvField(IS_LENT) + "," +
                    escapeCsvField(IS_READ) + "\n");

            // Write book data
            for (Book book : books) {
                csvData.append(escapeCsvField(book.getTitle()) + "," +
                        escapeCsvField(book.getCategory().getName()) + "," +
                        escapeCsvField(book.getAuthor().getName()) + "," +
                        escapeCsvField(book.getPublisher()) + "," +
                        escapeCsvField(book.getDescription()) + "," +
                        escapeCsvField(book.getLentTo()) + "," +
                        escapeCsvField(book.getPublishedDate()) + "," +
                        escapeCsvField(String.valueOf(book.getPages())) + "," +
                        escapeCsvField(book.isDigital() ? "1" : "0") + "," +
                        escapeCsvField(book.isLent() ? "1" : "0") + "," +
                        escapeCsvField(book.isRead() ? "1" : "0") + "\n");
            }

            // Write to the output stream
            if (outputStream != null) {
                outputStream.write(csvData.toString().getBytes());
                outputStream.flush();
                Toast.makeText(context, "The file was saved in the download folder!", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving file.", Toast.LENGTH_SHORT).show();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains("\"")) {
            field = field.replace("\"", "\"\"");
        }
        if (field.contains(",") || field.contains("\n")) {
            field = "\"" + field + "\"";
        }
        return field;
    }
}
