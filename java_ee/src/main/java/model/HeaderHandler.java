package model;

import dao.CategoryDAO;

import javax.faces.context.FacesContext;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class HeaderHandler implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private transient final Database db = Database.getInstance();
    CategoryDAO categoryDAO = db.getCategoryDAO();

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public boolean isLoggedIn() {
        return Util.getSessionObj("customerId") != null;
    }

    public String logOut() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
}
