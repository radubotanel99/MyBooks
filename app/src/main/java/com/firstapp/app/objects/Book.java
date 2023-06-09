package com.firstapp.app.objects;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {

    private int id;
    private String title;
    private Author author;
    private String description;
    private String series;
    private String volume;
    private Category category;
    private String publishedDate;
    private String publisher;
    private int pages;
    private String ISBN;
    private boolean borrowed;
    private boolean lent;

    private byte[] image;

    public Book(int id, String title, Author author, String publisher, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
    }

    public Book(int id, String title, Author author, String description, String series, String volume, Category category, String publishedDate, String publisher, int pages, String ISBN, boolean borrowed, boolean lent, byte[] image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.series = series;
        this.volume = volume;
        this.category = category;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.pages = pages;
        this.ISBN = ISBN;
        this.borrowed = borrowed;
        this.lent = lent;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }

    public boolean isLent() {
        return lent;
    }

    public void setLent(boolean lent) {
        this.lent = lent;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
