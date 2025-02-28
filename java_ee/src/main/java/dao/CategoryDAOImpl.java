package dao;

import model.Book;
import model.Database;
import model.Category;

import java.sql.*;
import java.util.*;

public class CategoryDAOImpl implements CategoryDAO {

    private Database db;

    public CategoryDAOImpl(Database db) {
        this.db = db;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (Connection con = db.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while(rs.next()) {
                Category category = new Category(rs.getInt(1), rs.getString(2));
                categories.add(category);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public void insertCategory(Category category) {
        String query = "INSERT INTO category(id, name) VALUES(?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, 0);
            ps.setString(2, category.getName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getAllCategoriesForBook(Book book) {
        StringBuilder categories = new StringBuilder();
        String query = "SELECT category.name "
                + "FROM category, book_category "
                + "WHERE category.id = book_category.category_id "
                + "AND book_category.book_id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, book.getBookId());
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                categories.append(rs.getString(1)).append("<br>");
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return categories.toString();
    }

    @Override
    public String getCategoryById(int categoryId) {
        String categoryName = "";
        String query = "SELECT name FROM category"
                +" WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                categoryName = rs.getString("name");
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryName;
    }

    @Override
    public boolean categoryAlreadyExists(Category category) {
        String query = "SELECT * FROM category WHERE name = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category.getName());
            ResultSet rs = ps.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean categoryIdExists(String categoryId) {
        String query = "SELECT 1 FROM category WHERE id = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
