package com.firstapp.app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firstapp.app.R;
import com.firstapp.app.database.Database;
import com.firstapp.app.helperclasses.CaptureAct;
import com.firstapp.app.helperclasses.ImageDownloaderTask;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.views.BookView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewBooksActivity extends AppCompatActivity {

    private ArrayList<Book> booksArrayList;
    private Database db;
    private BookView bookView;
    private RecyclerView booksRV;
    private ImageButton addBookButton;
    private ImageButton scanBtn;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        booksArrayList = new ArrayList<>();
        db = new Database(ViewBooksActivity.this);
        booksArrayList = db.allBooks();
        bookView = new BookView(booksArrayList, ViewBooksActivity.this);
        booksRV = findViewById(R.id.idRVBooks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewBooksActivity.this, RecyclerView.VERTICAL, false);
        booksRV.setLayoutManager(linearLayoutManager);
        booksRV.setAdapter(bookView);

        addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddBookActivity();
            }
        });

        scanBtn = findViewById(R.id.scanButton);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });
    }

    public void openAddBookActivity() {
        Intent intent = new Intent(this, AddBookActivity.class);
        startActivity(intent);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a book");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            getBookInfo(result.getContents());
        }
    });

    private void getBookInfo(String isbn) {
        mRequestQueue = Volley.newRequestQueue(ViewBooksActivity.this);
        mRequestQueue.getCache().clear();
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + isbn;
        RequestQueue queue = Volley.newRequestQueue(ViewBooksActivity.this);

        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i=0; i<itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        JSONArray authorsArray = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        String volume = volumeObj.optString("volume");
                        String series = volumeObj.optString("series");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
//                        JSONObject saleObj = itemsObj.getJSONObject("saleInfo");
//                        String listPrice = saleObj.optString("listPrice");
//                        String retailPrice = saleObj.optString("retailPrice");

                        String imagePath = savePhotoToDevice(imageLinks, title, isbn);
                        String author = getAuthor(authorsArray);

                        Intent intent = new Intent(ViewBooksActivity.this, AddBookActivity.class);
                        Book bookToEdit = new Book(0, title, new Author(author), description, series, volume, null, publishedDate, publisher, pageCount, isbn, false, false, imagePath);
                        intent.putExtra("BOOK_TO_EDIT", bookToEdit);
                        startActivity(intent);
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
                Toast.makeText(ViewBooksActivity.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(booksObjrequest);
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
            new ImageDownloaderTask(getApplicationContext()).execute(imageUrl, title, isbn);
            return getFilesDir() + "/books_Images/" + title + isbn + ".png";
        } catch (NullPointerException e) {
            //No photo for this book
            return "";
        }
    }

    private void alertDialogNoBookFound() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Didn't find a book with that ISBN. You have to add it manually")
                .setPositiveButton("OK", null); // null OnClickListener

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}