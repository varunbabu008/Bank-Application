package bankApplication;

import bankApplication.util.JsfUtil;
import bankApplication.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@Named("banktansactionsController")
@SessionScoped
public class BanktansactionsController implements Serializable {


    @EJB private bankApplication.BanktansactionsFacade ejbFacade;
    private List<Banktansactions> items = null;
    private List<Banktansactions> filteredItems = null;
    
    private Banktansactions selected;
    
    public BanktansactionsController() {
    }

    public Banktansactions getSelected() {
        return selected;
    }

    public void setSelected(Banktansactions selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private BanktansactionsFacade getFacade() {
        return ejbFacade;
    }

    public Banktansactions prepareCreate() {
        selected = new Banktansactions();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("BanktansactionsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("BanktansactionsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("BanktansactionsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Banktansactions> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
     public List<Banktansactions> getFilteredItems() {
        return filteredItems;
    }
     public void setFilteredItems(List<Banktansactions> filteredItems) {
        this.filteredItems = filteredItems;
    }
     
     public boolean filterByBalance(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    AccountController ac = retrieveAccountController();
                    Account to = ac.getFacade().find(selected.getAccountid().getAcid());
                    Account from = ac.getFacade().find(selected.getFromaccnt().getAcid());

                    if (Objects.equals(to.getAcid(), from.getAcid())) {
                        JsfUtil.addErrorMessage("Cannot transfer funds to your own account");
                        return;
                    }
                    double checkbalance = from.getAcbal() - (double) selected.getAmount();
                    if (checkbalance > 0) {                       
                        selected.setTrtime(getCurrentTime());
                        getFacade().edit(selected);
                        double fromBal = from.getAcbal() - (double) selected.getAmount();
                        double tobal = to.getAcbal() + (double) selected.getAmount();

                        to.setAcbal(tobal);
                        from.setAcbal(fromBal);
                        ac.getFacade().edit(to);
                        ac.getFacade().edit(from);                  
                        ac.destroyItems();

                    } else {
                        JsfUtil.addErrorMessage("Insufficient funds");
                        items = null;    // Invalidate list of items to trigger re-query.
                        return;
                    }
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
    
    public Date getCurrentTime() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = TimeZone.getTimeZone("Australia/Sydney");
        calendar.setTimeZone(timeZone);
        Date currentTime = calendar.getTime();
        return currentTime;
    }
    
    public AccountController retrieveAccountController(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (AccountController) elContext.getELResolver().getValue(elContext, null, "accountController");
    }

    public Banktansactions getBanktansactions(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Banktansactions> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Banktansactions> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Banktansactions.class)
    public static class BanktansactionsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BanktansactionsController controller = (BanktansactionsController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "banktansactionsController");
            return controller.getBanktansactions(getKey(value));
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
            if (object instanceof Banktansactions) {
                Banktansactions o = (Banktansactions) object;
                return getStringKey(o.getTrid());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Banktansactions.class.getName()});
                return null;
            }
        }

    }

}
