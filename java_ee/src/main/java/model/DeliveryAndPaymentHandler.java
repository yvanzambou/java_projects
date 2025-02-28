package model;

import dao.CustomerDAO;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Named("deliveryAndPaymentHandler")
@SessionScoped
public class DeliveryAndPaymentHandler implements Serializable {

    private transient final Database db = Database.getInstance();
    transient CustomerDAO customerDAO = db.getCustomerDAO();

    Customer customer;
    Creditcard creditcard;
    Integer customerId;

    public DeliveryAndPaymentHandler() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
        customerId = (Integer) session.getAttribute("customerId");
    }

    public Customer getCustomer() {
        return customerDAO.getCustomer(customerId);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Creditcard getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(Creditcard creditcard) {
        this.creditcard = creditcard;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
