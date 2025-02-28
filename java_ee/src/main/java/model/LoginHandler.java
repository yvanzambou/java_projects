package model;

import dao.CustomerDAO;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

@Named("loginHandler")
@ManagedBean
@SessionScoped
public class LoginHandler implements Serializable {

    private transient final Database db = Database.getInstance();
    transient CustomerDAO customerDAO = db.getCustomerDAO();

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() throws IOException {
        Customer customer = customerDAO.getCustomer(email, password);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        if (customer != null) {
            int id = customer.getId();
            System.out.println(customer.getFirstname() +" ist angemeldet. ID: "+ id);

            FacesContext fc = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);
            session.setAttribute("customerId", id);

            ec.redirect(ec.getRequestContextPath() + "/categoryPage");
        } else {
            ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
        }
    }
}
