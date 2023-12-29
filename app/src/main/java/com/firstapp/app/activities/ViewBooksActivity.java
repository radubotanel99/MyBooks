package com.firstapp.app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import com.firstapp.app.helperclasses.FilterListener;
import com.firstapp.app.helperclasses.GoogleAPIRequest;
import com.firstapp.app.helperclasses.ImageDownloaderTask;
import com.firstapp.app.objects.Author;
import com.firstapp.app.objects.Book;
import com.firstapp.app.views.BookView;
import com.firstapp.app.views.FilterBottomSheet;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ViewBooksActivity extends AbstractActivity implements FilterListener {

    private ArrayList<Book> booksArrayList;
    private Database db;
    private BookView bookView;
    private RecyclerView booksRV;
    private ImageButton addBookButton, scanBtn, filterButton;
    private GoogleAPIRequest request = new GoogleAPIRequest(ViewBooksActivity.this);
    private EditText searchEditText;

    private boolean authorFilterIsChecked, categoryFilterIsChecked, readFilterIsChecked, lentFilterIsChecked,
            readRdBtnChecked, lentRdBtnChecked;
    private String authorSelected, categorySelected;

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


        searchEditText = findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterButton = findViewById(R.id.filterButton);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the bottom sheet
                FilterBottomSheet bottomSheetFragment = new FilterBottomSheet(ViewBooksActivity.this, authorFilterIsChecked, categoryFilterIsChecked, readFilterIsChecked, lentFilterIsChecked,
                        readRdBtnChecked, lentRdBtnChecked, authorSelected, categorySelected);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void filterBooks(String query) {
        ArrayList<Book> filteredList = new ArrayList<>();

        for (Book book : booksArrayList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(book);
            }
        }
        bookView = new BookView(filteredList, ViewBooksActivity.this);
        booksRV.setAdapter(bookView);
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
            request.getBookInfo(result.getContents());
        }
    });

    @Override
    public void onFiltersApplied(boolean authorChecked, boolean categoryChecked, boolean readChecked, boolean lentChecked,
                                 boolean readRdBtnChecked, boolean lentRdBtnChecked, String authorSelected,
                                 String categorySelected) {
        this.authorFilterIsChecked = authorChecked;
        this.categoryFilterIsChecked = categoryChecked;
        this.readFilterIsChecked = readChecked;
        this.lentFilterIsChecked = lentChecked;
        this.readRdBtnChecked = readRdBtnChecked;
        this.lentRdBtnChecked = lentRdBtnChecked;
        this.authorSelected = authorSelected;
        this.categorySelected = categorySelected;

        booksArrayList = db.getFilteredBooks(authorFilterIsChecked, categoryFilterIsChecked, readFilterIsChecked, lentFilterIsChecked,
                readRdBtnChecked, lentRdBtnChecked, authorSelected, categorySelected);

        filterBooks(String.valueOf(searchEditText.getText()));
    }
}