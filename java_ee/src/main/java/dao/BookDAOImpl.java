package dao;

import model.Book;
import model.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

    private Database db;

	public BookDAOImpl(Database db) {
            this.db = db;
    }

        @Override
        public List<Book> getAllBooks() {
            List<Book> books = new ArrayList<>();
            String query = "SELECT id, title, author, description, price, publisher, publishing_year, isbn, image_path FROM book"; // "SELECT * FROM book";

            try (Connection con = db.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("publisher"),
                            rs.getInt("publishing_year"),
                            rs.getString("isbn"),
                            rs.getString("image_path"));
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
        public List<Book> getBooks(String category) {
            List<Book> books = new ArrayList<>();
            String query = "SELECT * FROM book "
                    + "JOIN book_category ON book.id = book_category.book_id "
                    + "JOIN category ON book_category.category_id = category.id "
                    + "WHERE category.name = ?";

            try (Connection con = db.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setString(1, category);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Book book = new Book(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("publisher"),
                                rs.getInt("publishing_year"),
                                rs.getString("isbn"),
                                rs.getString("image_path"));
                        books.add(book);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return books;
        }

        @Override
        public void insertBook(Book book, String[] categories) {
            String bookQuery = "INSERT INTO book "
                    + "(title, author, description, price, publisher, publishing_year, isbn, image_path) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = db.getConnection();
                 PreparedStatement bookStmt = con.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS)) {

                con.setAutoCommit(false); // Start transaction

                bookStmt.setString(1, book.getTitle());
                bookStmt.setString(2, book.getAuthor());
                bookStmt.setString(3, book.getDescription());
                bookStmt.setDouble(4, book.getPrice());
                bookStmt.setString(5, book.getPublisher());
                bookStmt.setInt(6, book.getPublishingYear());
                bookStmt.setString(7, book.getIsbn());
                bookStmt.setString(8, book.getImagePath());

                bookStmt.executeUpdate();

                try (ResultSet rs = bookStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int bookId = rs.getInt(1);
                        insertBookCategory(con, bookId, categories);
                    }
                }

                con.commit(); // Commit transaction
            } catch (SQLException e) {
                e.printStackTrace();
                try (Connection con = db.getConnection()) {
                    con.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        }

        @Override
        public void insertBookCategory(Connection con, int bookId, String[] categories) {
            String categoryQuery = "INSERT INTO book_category(book_id, category_id) VALUES(?, ?)";

            try (PreparedStatement categoryStmt = con.prepareStatement(categoryQuery)) {
                for (String category : categories) {
                    int categoryId = getCategoryId(con, category);
                    categoryStmt.setInt(1, bookId);
                    categoryStmt.setInt(2, categoryId);
                    categoryStmt.addBatch();
                }
                categoryStmt.executeBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public int getCategoryId(Connection con, String category) {
            String query = "SELECT id FROM category WHERE name = ?";
            int categoryId = 0;

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, category);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        categoryId = rs.getInt("id");
                    } else {
                        throw new SQLException("Category \""+category+"\" not found.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categoryId;
        }

        @Override
        public Book getBookById(int bookId) {
            String query = "SELECT id, title, author, description, price, publisher, publishing_year, isbn, image_path FROM book WHERE id = ?";
            try (Connection con = db.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

                stmt.setInt(1, bookId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Book(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("publisher"),
                                rs.getInt("publishing_year"),
                                rs.getString("isbn"),
                                rs.getString("image_path"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        //==================== validator ====================

        @Override
        public boolean isNumeric(String str) {
            if (str == null || str.trim().isEmpty()) {
                return true;
            }
            for (char c : str.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return true;
                }
            }
            return false;
        }

        private int countMinus(String isbn) {
            int count = 0;
            for (int i = 0; i < isbn.length(); i++) {
                if (isbn.charAt(i) == '-') {
                    count++;
                }
            }
            return count;
        }

        @Override
        public boolean hasValidIsbn(String isbn) {
            if (isbn.length() != 17) {
                return false;
            } else {
                if (isbn.charAt(3) == '-' && isbn.charAt(5) == '-' && isbn.charAt(8) == '-' && isbn.charAt(15) == '-' && countMinus(isbn) == 4) {
                    String isbnAsNumber = isbn.replace("-", "");
                    for (char ch : isbnAsNumber.toCharArray()) {
                        if (!Character.isDigit(ch)) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean hasValidDate(String date) {
            if (date.length() == 4) {
                for (char c : date.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
                int year = Integer.parseInt(date);
                return year > 1900 && year < 2025;
            } else {
                return false;
            }
        }

        @Override
        public boolean isbnAlreadyExists(String isbn) {
            String query = "SELECT 1 FROM book WHERE isbn = ?";
            try (Connection con = db.getConnection();
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, isbn);

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    @Override
    public boolean bookIdExists(String bookId) {
        String query = "SELECT 1 FROM book WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, bookId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
