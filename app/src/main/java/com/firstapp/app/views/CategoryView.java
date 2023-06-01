package com.firstapp.app.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.app.OnRefreshListener;
import com.firstapp.app.R;
import com.firstapp.app.activities.AdministrationActivity;
import com.firstapp.app.activities.ViewBooksActivity;
import com.firstapp.app.activities.ViewCategoriesActivity;
import com.firstapp.app.activities.ViewOneBookActivity;
import com.firstapp.app.database.Database;
import com.firstapp.app.objects.Category;

import java.util.ArrayList;

public class CategoryView extends RecyclerView.Adapter<CategoryView.ViewHolder>{

    private ArrayList<Category> categoriesArrayList;
    private Context context;
    private Database db;

    public CategoryView(ArrayList<Category> categoriesArrayList, Context context) {
        this.categoriesArrayList = categoriesArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_rv_item, parent, false);
        return new CategoryView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryView.ViewHolder holder, int position) {
        db = new Database(context);
        Category modal = categoriesArrayList.get(position);
        holder.categoryNameTV.setText(modal.getName());
        holder.idTVBooksNumber.setText(String.valueOf(db.booksNumber(modal.getName())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete category");
                builder.setMessage("Are you sure you want to delete the " + modal.getName() + " category?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isCategoryDeletable(modal.getName())) {
                            db.deleteCategory(modal.getName());
                            Intent i = new Intent(context, AdministrationActivity.class);
                            context.startActivity(i);
                        } else {
                            Toast.makeText(context, "Cannot delete category. There are books which belongs to it.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private boolean isCategoryDeletable(String categoryName) {
        boolean hasAssociatedBooks = db.hasBooksInCategory(categoryName);
        return !hasAssociatedBooks;
    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView categoryNameTV;
        private TextView idTVBooksNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTV = itemView.findViewById(R.id.idTVCategoryName);
            idTVBooksNumber = itemView.findViewById(R.id.idTVBooksNumber);
//
//            db = new Database(context);
//            int howManyBooksCategoryHas = db.booksNumber(categoryNameTV.getText().toString());
//            idTVBooksNumber.setText(String.valueOf(howManyBooksCategoryHas));
        }

    }
}
