package com.firstapp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firstapp.app.R;
import com.firstapp.app.database.Database;

public class MainActivity extends AppCompatActivity {

    private Button goToMyBooksBtn;

    private Database db = new Database(MainActivity.this);;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        db.resetDatabase();

        goToMyBooksBtn = (Button) findViewById(R.id.goToMyBooksBtn);
        goToMyBooksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdministrationActivity();
            }
        });
    }

    public void openAdministrationActivity() {
        Intent intent = new Intent(this, AdministrationActivity.class);
        startActivity(intent);
    }
}