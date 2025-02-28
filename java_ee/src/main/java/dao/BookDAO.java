package dao;

import model.Book;

import java.sql.Connection;
import java.util.List;

public interface BookDAO {
    List<Book> getAllBooks();
    List<Book> getBooks(String category);
    void insertBook(Book book, String[] categories);
    void insertBookCategory(Connection con, int bookId, String[] categories);
    boolean isNumeric(String str);
    Book getBookById(int bookId);
    boolean hasValidIsbn(String isbn);
    boolean hasValidDate(String date);
    boolean isbnAlreadyExists(String isbn);
    boolean bookIdExists(String bookId);
}
