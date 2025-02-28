package dao;

import model.Customer;

public interface CustomerDAO {

    public Customer getCustomer(String email, String password);
    public Customer getCustomer(Integer customerId);
    int insertCustomer(Customer customer);
}
