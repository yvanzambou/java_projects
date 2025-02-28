package model;

import dao.CustomerDAO;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named("registrationHandler")
@ManagedBean
@SessionScoped
public class RegistrationHandler implements Serializable {

    private transient final Database db = Database.getInstance();
    transient CustomerDAO customerDAO = db.getCustomerDAO();

    String firstname;
    String lastname;
    String street;
    int houseNumber;
    int postalCode;
    String city;
    String eMail;
    Date birthday;
    String password;
    String repeatedPassword;

    public String registration() {
        int customerId = customerDAO.insertCustomer(new Customer(0, firstname, lastname, street, houseNumber, postalCode, city, eMail, birthday, password));
        Util.setSessionObj("customerId", customerId);
        return "index?faces-redirect=true";
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) {
        this.eMail = eMail;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}
