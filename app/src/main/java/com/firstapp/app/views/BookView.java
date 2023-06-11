package com.firstapp.app.views;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.publisherTV.setText(modal.getPublisher());
        holder.categoryTV.setText(modal.getCategory().getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, ViewOneBookActivity.class);

                i.putExtra("id", modal.getId());
                i.putExtra("title", modal.getTitle());
                i.putExtra("author", modal.getAuthor().getName());
                i.putExtra("description", modal.getDescription());
                i.putExtra("series", modal.getSeries());
                i.putExtra("volume", modal.getVolume());
                i.putExtra("category", modal.getCategory().getName());
                i.putExtra("publishedDate", modal.getPublishedDate());
                i.putExtra("publisher", modal.getPublisher());
                i.putExtra("pages", modal.getPages());
                i.putExtra("isBorrowed", modal.isBorrowed());
                i.putExtra("isLent", modal.isLent());
                i.putExtra("image", modal.getImage());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV, authorTV, publisherTV, categoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.idTVTitle);
            authorTV = itemView.findViewById(R.id.idTVAuthor);
            publisherTV = itemView.findViewById(R.id.idTVPublisher);
            categoryTV = itemView.findViewById(R.id.idTVCategory);
        }
    }
}
