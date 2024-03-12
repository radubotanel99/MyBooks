package com.firstapp.app.helperclasses;

import static com.firstapp.app.helperclasses.GeneralConstants.*;

import android.content.Context;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.objects.Category;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVReaderHelper {
    public void readAndCheckCSVContent(Reader reader, Context context) {
        try (CSVReader csvReader = new CSVReader(reader)) {
            // Read the header row
            String[] headers = csvReader.readNext();
            if (headers == null) {
                Toast.makeText(context, "The table from this file doesn't have columns!", Toast.LENGTH_SHORT).show();
                return;
            }
            normalizeHeaders(headers);

            // Check if the required columns are present
            boolean hasTitle = Arrays.asList(headers).contains("title");
            if (!hasTitle) {
                Toast.makeText(context, "The table from this file doesn't have 'title' column!", Toast.LENGTH_SHORT).show();
                return;
            }

            Database db = Database.getInstance(context);
            // Iterate over each row
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                Map<String, String> headerMapping = determineHeaderMapping(headers, nextLine);
                createBookFromRow(nextLine, headerMapping, db);
            }
            Toast.makeText(context, "The books were added successfully!", Toast.LENGTH_SHORT).show();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> determineHeaderMapping(String[] headers, String[] row) {
        Map<String, String> headerMapping = new HashMap<>();
        // Assuming headers and dataRow are provided by the user
        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].toLowerCase().trim();
            String data = row[i];
            headerMapping.put(header, data);
        }
        return headerMapping;
    }

    private void createBookFromRow(String[] row, Map<String, String> headerMapping, Database db) {
        Book newBook = new Book();
        for (Map.Entry<String, String> entry : headerMapping.entrySet()) {
            String header = entry.getKey();
            String data = entry.getValue();

            if (header != null) {

                switch (header) {
                    case TITLE:
                        newBook.setTitle(data);
                        break;
                    case CATEGORY:
                        Category newCategory = new Category(data);
                        if (null ==newCategory.getName() || newCategory.getName().equals("")) {
                            newCategory = new Category(PERSONAL_BOOKS);
                        }
                        if (db.getCategoryId(newCategory.getName()) == 0 || db.getCategoryId(newCategory.getName()) == -1) {
                            db.addNewCategory(newCategory.getName());
                        }
                        newBook.setCategory(newCategory);
                        break;
                    case AUTHOR:
                        Author newAuthor = new Author(data);
                        newBook.setAuthor(newAuthor);
                        break;
                    case PUBLISHER:
                        newBook.setPublisher(data);
                        break;
                    case DESCRIPTION:
                        newBook.setDescription(data);
                        break;
                    case COPIES:
                        newBook.setCopies(Integer.parseInt(data));
                        break;
                    case "lentto":
                        newBook.setLentTo(data);
                        break;
                    case "publisheddate":

                        newBook.setPublishedDate(data);
                        break;
                    case "pagecount":
                        newBook.setPages(Integer.parseInt(data));
                        break;
                    case "isdigital":
                        newBook.setDigital(Boolean.parseBoolean(data));
                        break;
                    case "islent":
                        newBook.setLent(Boolean.parseBoolean(data));
                        break;
                    case "isread":
                        newBook.setRead(Boolean.parseBoolean(data));
                        break;
                    default:
                        break;
                }
            }
        }
        if (db.getDuplicate(newBook.getTitle(), newBook.getAuthor().getName(), newBook.getPublisher(), newBook.getPages()) == null) {
            db.addNewBook(newBook.getTitle(), newBook.getAuthor().getName(), newBook.getPublisher(), newBook.getCategory().getName(),
                    newBook.getDescription(), newBook.getCopies(), newBook.getLentTo(),
                    newBook.getPublishedDate(), newBook.getPages(), newBook.isDigital(), newBook.isLent(), newBook.isRead(), "");
        }
    }

    private void normalizeHeaders (String[]headers) {
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].toLowerCase().replaceAll("\\W", "");
        }
    }
}

