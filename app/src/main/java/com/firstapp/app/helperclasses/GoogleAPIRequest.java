package com.firstapp.app.helperclasses;

import static com.firstapp.app.helperclasses.GeneralConstants.AUTHORS;
import static com.firstapp.app.helperclasses.GeneralConstants.BOOKS_INFO;
import static com.firstapp.app.helperclasses.GeneralConstants.BOOK_TO_EDIT;
import static com.firstapp.app.helperclasses.GeneralConstants.DESCRIPTION;
import static com.firstapp.app.helperclasses.GeneralConstants.IMAGE_LINK;
import static com.firstapp.app.helperclasses.GeneralConstants.ITEMS;
import static com.firstapp.app.helperclasses.GeneralConstants.PAGE_COUNT;
import static com.firstapp.app.helperclasses.GeneralConstants.PUBLISHED_DATE;
import static com.firstapp.app.helperclasses.GeneralConstants.PUBLISHER;
import static com.firstapp.app.helperclasses.GeneralConstants.TITLE;
import static com.firstapp.app.helperclasses.GeneralConstants.LENT_TO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firstapp.app.activities.AddBookActivity;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GoogleAPIRequest {

    private Context context;
    private RequestQueue mRequestQueue;

    public GoogleAPIRequest(Context ctx) {
        this.context = ctx;
    }

    public void getBookInfo(String isbn) {
        mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray(ITEMS);
                    for (int i=itemsArray.length()-1; i>=0; i--) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject(BOOKS_INFO);
                        String title = getPropertyFromJSON(volumeObj, TITLE);
                        String author = getPropertyFromJSON(volumeObj, AUTHORS);
                        String publisher = getPropertyFromJSON(volumeObj, PUBLISHER);
                        String publishedDate = getPropertyFromJSON(volumeObj, PUBLISHED_DATE);
                        String description = getPropertyFromJSON(volumeObj, DESCRIPTION);
                        int pageCount = volumeObj.optInt(PAGE_COUNT);

                        JSONObject imageLinks = volumeObj.optJSONObject(IMAGE_LINK);
//                        JSONObject saleObj = itemsObj.getJSONObject("saleInfo");
//                        String listPrice = saleObj.optString("listPrice");
//                        String retailPrice = saleObj.optString("retailPrice");

                        String imagePath = savePhotoToDevice(imageLinks, title, isbn);

                        Intent intent = new Intent(context, AddBookActivity.class);
                        Book bookToEdit = new Book(0, title, new Author(author), description, 1, "", null, publishedDate, publisher, pageCount, isbn, false, false, false, imagePath);
                        intent.putExtra(BOOK_TO_EDIT, bookToEdit);
                        context.startActivity(intent);
                        break;
                    }
                } catch (JSONException e) {
                    if (e.getMessage().equals("No value for items")) {
                        alertDialogNoBookFound();
                        return;
                    }
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(booksObjrequest);
    }

    private String getPropertyFromJSON(JSONObject json, String property) {

        if (property.equals(AUTHORS)) {
            try {
                JSONArray authorsArray = json.getJSONArray(property);
                return getAuthor(authorsArray);
            } catch (JSONException e) {
                // no authors
                return "";
            }

        }

        return json.optString(property);
    }

    private String getAuthor(JSONArray authorsArray) {
        return IntStream.range(0, authorsArray.length())
                .mapToObj(i -> {
                    try {
                        return authorsArray.getString(i);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.joining(", "));
    }

    private String savePhotoToDevice(JSONObject imageLinks, String title, String isbn) {
        try {
            String imageUrl = imageLinks.optString("thumbnail");
            //first parameter: imageUrl. Second: title. Third: isbn. If you don't respect the order it will crash
            new ImageDownloaderTask(context.getApplicationContext()).execute(imageUrl, title, isbn);
            return context.getFilesDir() + "/books_Images/" + title + isbn + ".png";
        } catch (NullPointerException e) {
            //No photo for this book
            return "";
        }
    }

    private void alertDialogNoBookFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Sorry, we didn't find a book with that ISBN. You have to add the book manually")
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
