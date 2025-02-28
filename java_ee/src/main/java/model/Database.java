package model;

import dao.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    String url;
    String user;
    String password;

    public Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static Database getInstance() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new Database("jdbc:mariadb://localhost:3306/myBookShop", "root", "bitnami");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public BookDAO getBookDAO() {
        return new BookDAOImpl(this);
    }

    public CategoryDAO getCategoryDAO() {
        return new CategoryDAOImpl(this);
    }

    public CustomerDAO getCustomerDAO() {
        return new CustomerDAOImpl(this);
    }
}