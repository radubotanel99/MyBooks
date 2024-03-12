package com.firstapp.app.helperclasses;

import static com.firstapp.app.helperclasses.GeneralConstants.*;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.firstapp.app.activities.AddBookActivity;
import com.firstapp.app.objects.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public CsvExporter() {
    }

    public void exportToCsv(Context context, String fileName, List<Book> books) {
        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, fileName + ".csv");
        try {
//            FileWriter fileWriter = new FileWriter(file);
//
//            fileWriter.append(TITLE + "," + CATEGORY + "," + AUTHOR + "," + PUBLISHER + "," + DESCRIPTION + "," +
//                    LENT_TO + "," + PUBLISHED_DATE + "," + PAGE_COUNT + "," + IS_DIGITAL + "," + IS_LENT + "," + IS_READ + "," + "\n");
//            // Write the data
//            for (Book book : books) {
//                fileWriter.append(book.getTitle())
//                        .append(',')
//                        .append(book.getCategory().getName())
//                        .append(',')
//                        .append(book.getAuthor().getName())
//                        .append(',')
//                        .append(book.getPublisher())
//                        .append(',')
//                        .append(book.getDescription())
//                        .append(',')
//                        .append(book.getLentTo())
//                        .append(',')
//                        .append(book.getPublishedDate())
//                        .append(',')
//                        .append((char) book.getPages())
//                        .append(',')
//                        .append(book.isDigital() ? "1" : "0")
//                        .append(',')
//                        .append(book.isLent() ? "1" : "0")
//                        .append(',')
//                        .append(book.isRead() ? "1" : "0")
//                        .append('\n');
//            }

            FileWriter fileWriter = new FileWriter(file);

            fileWriter.append(escapeCsvField(TITLE) + "," +
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

            for (Book book : books) {
                fileWriter.append(escapeCsvField(book.getTitle()) + "," +
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

            fileWriter.flush();
            fileWriter.close();

            Toast.makeText(context, "The file was saved in the download folder!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error.", Toast.LENGTH_SHORT).show();
            // Handle the exception (e.g., show an error message)
        }
    }

    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // If the field contains double quotes, escape them by doubling them
        if (field.contains("\"")) {
            field = field.replace("\"", "\"\"");
        }
        // If the field contains commas or line breaks, enclose it within double quotes
        if (field.contains(",") || field.contains("\n")) {
            field = "\"" + field + "\"";
        }
        return field;
    }
}
