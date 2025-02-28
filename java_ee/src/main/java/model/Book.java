package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int bookId;
    private String title;
    private String author;
    private String description;
    private double price;
    private String publisher;
    private int publishingYear;
    private String isbn;
    private String imagePath;
    private List<Category> categories = new ArrayList<>();

    public Book(int bookId, String title, String author, String description, double price, String publisher, int publishingYear, String isbn, String imagePath) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.description = description;
        this.price = price;
        this.publisher = publisher;
        this.publishingYear = publishingYear;
        this.isbn = isbn;
        this.imagePath = imagePath;	}

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceFormat() {
        return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}