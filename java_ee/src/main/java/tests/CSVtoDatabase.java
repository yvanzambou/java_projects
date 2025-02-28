package tests;

import model.Book;
import model.Category;
import model.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CSVtoDatabase {

    public static void main(String[] args) throws SQLException, IOException {
        String csv_file = "C:/Users/Yvan ZAMBOU/Desktop/java_ee/Qmdfp93Kc69jpc17DH6fn4tt1SSHjg2GsZUeonDzbs9vX3.csv";
        String line;
        int count = 1;

        Database db = Database.getInstance();
        Connection con = db.getConnection();

        try(BufferedReader br = new BufferedReader(new FileReader(csv_file))) {
            br.readLine(); // erste Zeile ueberspringen

            String query = "DELETE FROM book_category WHERE book_id >= 1";
            String query2 = "DELETE FROM book";
            String query3 = "DELETE FROM category";
            sqlQuery(con, query);
            sqlQuery(con, query2);
            sqlQuery(con, query3);
			resetAutoIncrement(con, "book");
            resetAutoIncrement(con, "category");
			//System.out.println(getCategoryId(con, "Noir"));

			while((line = br.readLine()) != null && count <= 315) {
				if(line.split("\"").length <= 5) {
                    /*
					System.out.println(line.split("\"").length);
					System.out.println("TITLE: "+line.split("\"")[0].split(",")[2]); // title
					System.out.println("AUTHOR: "+line.split("\"")[0].split(",")[3]); // author
					System.out.println("DESCRIPTION: "+line.split("\"")[1]); // description
					System.out.println("ISBN: "+line.split("\"")[2].split(",")[2]);
					System.out.println("GENRES: "+line.split("\"")[3]);
					System.out.println("PUBLISHER: "+line.split("\"")[4].split(",")[4]);
					System.out.println("PUBLISHING_DATE: "+line.split("\"")[4].split(",")[5]);
					System.out.println("IMAGE_PATH: "+line.split("\"")[4].split(",")[8]);
					System.out.println("PRICE: "+line.split("\"")[4].split(",")[9]);
					System.out.println("==================================="+ count);
                     */
                    System.out.println(count+": "+line.split("\"")[0].split(",")[2]);


					Book book = new Book(0,
							line.split("\"")[0].split(",")[2],
							line.split("\"")[0].split(",")[3],
							line.split("\"")[1],
							Double.parseDouble(line.split("\"")[4].split(",")[9]),
							line.split("\"")[4].split(",")[4],
							dateFormat(line.split("\"")[4].split(",")[5]),
							isbnFormat(line.split("\"")[2].split(",")[2]),
							line.split("\"")[4].split(",")[8]);

                    String[] categories = asList(line.split("\"")[3]).toArray(new String[0]);

                    for (String cat: categories) {
                        insertCategory(con, new Category(0, cat));
                    }

					insertBook(con, book, categories);
					count++;
				}
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void insertBook(Connection con, Book book, String[] categories) {
        String bookQuery = "INSERT INTO book "
                + "(title, author, description, price, publisher, publishing_year, isbn, image_path) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement bookStmt = con.prepareStatement(bookQuery, Statement.RETURN_GENERATED_KEYS)) {

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
            try {
                con.rollback(); // Rollback transaction on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException(e);
        }
    }

    public static void insertBookCategory(Connection con, int bookId, String[] categories) {
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

    public static void insertCategory(Connection con, Category category) {
        if (!categoryAlreadyExists(con, category.getName())) {
            String query = "INSERT INTO category(id, name) VALUES(?, ?)";

            try (PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, category.getCategoryId());
                ps.setString(2, category.getName());

                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to insert category: " + category.getName(), e);
            }
        }
    }

    public static int getCategoryId(Connection con, String category) {
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

    public static boolean categoryAlreadyExists(Connection con, String category) {
        String query = "SELECT 1 FROM category WHERE name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String isbnFormat(String isbn) {
        if (isbn == null || isbn.length() != 13) {
            throw new IllegalArgumentException("Ungültige ISBN. Eine ISBN muss 13 Zeichen lang sein.");
        }
        return String.format("%s-%s-%s-%s-%s", isbn.substring(0, 3), isbn.substring(3, 4), isbn.substring(4, 6), isbn.substring(6, 12), isbn.substring(12, 13));
    }

    private static int dateFormat(String date) {
        if (date.length() == 8) {
            return Integer.parseInt("19" + date.substring(6));
        } else {
            return 1999;
        }
    }

    private static List<String> asList(String genres) {
        List<String> list = new ArrayList<>();
        String newGenres = genres.replace("['", "").replace("']", "");
        for (String str : newGenres.split("', '")) {
            list.add(str);
        }
        return list;
    }

    private static void resetAutoIncrement(Connection con, String table) {
        String query = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("Auto-increment value reset successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void sqlQuery(Connection con, String query) {
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("SQL-Query successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}