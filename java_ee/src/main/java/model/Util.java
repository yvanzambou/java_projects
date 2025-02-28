package model;

import org.jetbrains.annotations.Nullable;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Util {
    @Nullable
    public static <T> T getSessionObj(String attribute) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        return (T) session.getAttribute(attribute);
    }

    public static void setSessionObj(String attribute, Object obj){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        session.setAttribute(attribute, obj);
    }

    public static java.sql.Date toSQLDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}
