package com.firstapp.app.views;

import static com.firstapp.app.helperclasses.GeneralConstants.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firstapp.app.R;
import com.firstapp.app.activities.ViewOneBookActivity;
import com.firstapp.app.objects.Book;

import java.util.ArrayList;

public class BookView extends RecyclerView.Adapter<BookView.ViewHolder>{

    private ArrayList<Book> booksArrayList;
    private Context context;

    public BookView(ArrayList<Book> booksArrayList, Context context) {
        this.booksArrayList = booksArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_rv_item, parent, false);
        return new BookView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookView.ViewHolder holder, int position) {
        Book modal = booksArrayList.get(position);
        holder.titleTV.setText(modal.getTitle());
        holder.authorTV.setText(modal.getAuthor().getName());
        holder.categoryTV.setText(modal.getCategory().getName());

        if (null != modal.getImagePath() && !modal.getImagePath().equals("")) {
            String imagePath = modal.getImagePath();
            Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
            holder.imageView.setImageBitmap(imageBitmap);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ViewOneBookActivity.class);

                i.putExtra(ID, modal.getId());
                i.putExtra(TITLE, modal.getTitle());
                i.putExtra(AUTHOR, modal.getAuthor().getName());
                i.putExtra(DESCRIPTION, modal.getDescription());
                i.putExtra(SERIES, modal.getSeries());
                i.putExtra(VOLUME, modal.getVolume());
                i.putExtra(CATEGORY, modal.getCategory().getName());
                i.putExtra(PUBLISHED_DATE, modal.getPublishedDate());
                i.putExtra(PUBLISHER, modal.getPublisher());
                i.putExtra(PAGE_COUNT, modal.getPages());
                i.putExtra(IS_DIGITAL, modal.isDigital());
                i.putExtra(IS_LENT, modal.isLent());
                i.putExtra(IS_READ, modal.isRead());
                i.putExtra(IMAGE, modal.getImagePath());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, authorTV, categoryTV;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.idTVTitle);
            authorTV = itemView.findViewById(R.id.idTVAuthor);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
            imageView = itemView.findViewById(R.id.idImageView);
        }
    }
}
