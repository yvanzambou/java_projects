package dao;

import model.Customer;
import model.Database;
import model.Util;

import java.sql.*;

public class CustomerDAOImpl implements CustomerDAO {

    Database db;

    public CustomerDAOImpl(Database db) {
        this.db = db;
    }

    @Override
    public Customer getCustomer(String email, String password) {
        String query = "SELECT * FROM customer WHERE e_mail = ? AND password = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("street"),
                        rs.getInt("house_number"),
                        rs.getInt("postal_code"),
                        rs.getString("city"),
                        rs.getString("e_mail"),
                        rs.getDate("birthday"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Customer getCustomer(Integer customerId) {
        String query = "SELECT * FROM customer WHERE id = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("street"),
                        rs.getInt("house_number"),
                        rs.getInt("postal_code"),
                        rs.getString("city"),
                        rs.getString("e_mail"),
                        rs.getDate("birthday"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int insertCustomer(Customer customer) {
        String query = "INSERT INTO customer (firstname, lastname, street, house_number, postal_code, city, e_mail, birthday, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer.getFirstname());
            ps.setString(2, customer.getLastname());
            ps.setString(3, customer.getStreet());
            ps.setInt(4, customer.getHouseNumber());
            ps.setInt(5, customer.getPostalCode());
            ps.setString(6, customer.getCity());
            ps.setString(7, customer.getEmail());
            ps.setDate(8, Util.toSQLDate(customer.getBirthday()));
            ps.setString(9, customer.getPassword());
            ps.executeUpdate();

           try ( ResultSet rs = ps.getGeneratedKeys()) {
               if (rs.next()) {
                   return rs.getInt(1); // ID des hinzugefügten Kunden
               } else {
                   throw new SQLException();
               }
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
