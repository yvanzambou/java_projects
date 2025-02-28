package model;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext fc, UIComponent comp, Object obj) throws ValidatorException {
        String password = (String) obj;
        String repeatedPassword = (String) comp.getAttributes().get("confirmPassword");

        if (password != null && !password.equals(repeatedPassword)) {
            FacesMessage msg = new FacesMessage("Passwörter stimmen nicht überein.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
