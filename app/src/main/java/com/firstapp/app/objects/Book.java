package com.firstapp.app.objects;

import java.io.Serializable;

public class Book implements Serializable {

    private int id;
    private String title;
    private Author author;
    private String description;
    private int copies;
    private String lentTo;
    private Category category;
    private String publishedDate;
    private String publisher;
    private int pages;
    private String ISBN;
    private boolean digital;
    private boolean lent;
    private boolean read;
    private String imagePath;

    public Book() {

    }

    public Book(int id, String title, Author author, String publisher, Category category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
    }

    public Book(int id, String title, Author author, String description, int copies, String lentTo, Category category,
                String publishedDate, String publisher, int pages, String ISBN, boolean digital, boolean lent,
                boolean read, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.copies = copies;
        this.lentTo = lentTo;
        this.category = category;
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.pages = pages;
        this.ISBN = ISBN;
        this.digital = digital;
        this.lent = lent;
        this.read = read;
        this.imagePath = imagePath;
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

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getLentTo() {
        return lentTo;
    }

    public void setLentTo(String lentTo) {
        this.lentTo = lentTo;
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

    public boolean isDigital() {
        return digital;
    }

    public void setDigital(boolean digital) {
        this.digital = digital;
    }

    public boolean isLent() {
        return lent;
    }

    public void setLent(boolean lent) {
        this.lent = lent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
