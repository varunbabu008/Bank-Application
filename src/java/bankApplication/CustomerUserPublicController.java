package bankApplication;

import bankApplication.util.JsfUtil;
import bankApplication.util.JsfUtil.PersistAction;
import org.apache.commons.codec.binary.Hex;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpServletRequest;

@Named("customeruserPublicController")
@SessionScoped
public class CustomerUserPublicController implements Serializable {

    @EJB
    private bankApplication.CustomeruserFacade ejbFacade;
    private Customeruser items = null;
    private Customeruser selected;

    public CustomerUserPublicController() {
    }

    public Customeruser getSelected() {
        return selected;
    }

    public void setSelected(Customeruser selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public CustomeruserFacade getFacade() {
        return ejbFacade;
    }

    public Customeruser prepareCreate() {
        selected = new Customeruser();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CustomeruserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CustomeruserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CustomeruserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

   
    public Customeruser getItems() {
        if (items == null) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();        
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest(); 
        Customeruser cu = (Customeruser) request.getSession().getAttribute("CustomerUser");
        System.err.println(cu.getFirstname());
        items = getFacade().find(cu.getCusid());
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    if (persistAction == persistAction.CREATE) {
                        selected.setPassword(getHashedPassword(selected.getPassword()));
                        UsersController uc = retrieveUsersController();
                        Users user = getUserToInsertToLoginTable();
                        uc.getFacade().create(user);
                    }

                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Users getUserToInsertToLoginTable() {
        Users u = new Users();
        u.setUsername(selected.getUsername());
        u.setPassword(selected.getPassword());
        if (selected.getTypes() == 1) {
            u.setUsertype("public");
        } else {
            u.setUsertype("admin");
        }
      return u;
    }

    public String getHashedPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.trim().getBytes(StandardCharsets.UTF_8));
        System.out.println(password);
        System.out.println(hash);
        return bytesToHex(hash);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public UsersController retrieveUsersController() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (UsersController) elContext.getELResolver().getValue(elContext, null, "usersController");
    }

    public Customeruser getCustomeruser(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Customeruser> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Customeruser> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Customeruser.class)
    public static class CustomeruserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CustomeruserController controller = (CustomeruserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "customeruserPublicController");
            return controller.getCustomeruser(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Customeruser) {
                Customeruser o = (Customeruser) object;
                return getStringKey(o.getCusid());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Customeruser.class.getName()});
                return null;
            }
        }

    }

}
