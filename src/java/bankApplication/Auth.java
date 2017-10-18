/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankApplication;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.TypedQuery;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author varunbabu
 */
@ManagedBean
@ViewScoped
public class Auth implements Serializable{

    private String username;
    private String password;
    private String originalURL;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (originalURL == null) {
            originalURL = externalContext.getRequestContextPath() + "/index.xhtml";
        } else {
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                originalURL += "?" + originalQuery;
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }
    public String login() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            request.login(username, password);
            System.out.println("varun: logged in ");
            UsersController uc = retrieveUsersController();
            CustomeruserController cc = retrieveCustomerUserController();
            AccountController ac = retrieveAccountController();
            
            // had to change getEntity manager from protected to public - Not so good
            TypedQuery<Users> query = 
                    uc.getFacade().getEntityManager().
                    createNamedQuery("Users.findByUsername", Users.class).
                    setParameter("username", username);
            
            
            Users user = query.getSingleResult();  
            TypedQuery<Customeruser> query2 = 
                    cc.getFacade().getEntityManager().
                    createNamedQuery("Customeruser.findByUsername", Customeruser.class).
                    setParameter("username", username);
            
            Customeruser cu = query2.getSingleResult();
            
            TypedQuery<Account> query3 = 
                    ac.getFacade().getEntityManager().                   
                    createNamedQuery("Account.findByCustID", Account.class).
                    setParameter("custid", cu);
            System.out.println(cu.getCusid());
            List<Account> accounts = query3.getResultList();
            
            externalContext.getSessionMap().put("user", user);
            externalContext.getSessionMap().put("CustomerUser", cu);
            externalContext.getSessionMap().put("Accounts", accounts);
            // externalContext.redirect(originalURL);
            
            // checking if the user is an public 
            if (request.isUserInRole("public")){
                //externalContext.redirect(externalContext.getRequestContextPath() + "/public/index");
                return "/public/index?faces-redirect=true";
            }
            else{
                //externalContext.redirect(externalContext.getRequestContextPath() + "/admin/index");
                return "/admin/index?faces-redirect=true";
            }
            
        } catch (ServletException e) {
            // Handle unknown username/password in request.login().
            context.addMessage(null, new FacesMessage("Unknown login"));
            //externalContext.redirect(externalContext.getRequestContextPath() + "/login");
            return "login?faces-redirect=true";
        }
    }


    public String logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return  "/login?faces-redirect=true";
    }
    
     public UsersController retrieveUsersController(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (UsersController) elContext.getELResolver().getValue(elContext, null, "usersController");
    }
     public CustomeruserController retrieveCustomerUserController(){
         ELContext elContext = FacesContext.getCurrentInstance().getELContext();
         return (CustomeruserController) elContext.getELResolver().getValue(elContext, null, "customeruserController");
     }
     
     public AccountController retrieveAccountController(){
         ELContext elContext = FacesContext.getCurrentInstance().getELContext();
         return (AccountController) elContext.getELResolver().getValue(elContext, null, "accountController");
     }
     
}
