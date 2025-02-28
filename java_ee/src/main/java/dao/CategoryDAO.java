package dao;

import model.Book;
import model.Category;

import java.util.List;

public interface CategoryDAO {
    List<Category> getAllCategories();
    void insertCategory(Category category);
    String getAllCategoriesForBook(Book book);
    String getCategoryById(int categoryId);
    boolean categoryAlreadyExists(Category category);
    boolean categoryIdExists(String categoryId);
}
