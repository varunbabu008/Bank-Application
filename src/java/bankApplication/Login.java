/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankApplication;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author varunbabu
 */
@Named(value = "login")
@SessionScoped
public class Login implements Serializable {

    /**
     * Creates a new instance of Login
     */
    public Login() {
    }
     public String logout() throws ServletException {
         
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        String att = "varun";
        session.setAttribute("att", att);
        System.out.println("retreiving session variable");
        System.out.println(session.getAttribute("att"));
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //FacesContext context = FacesContext.getCurrentInstance();
        //HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        //HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        //request.logout();
        //session.invalidate();
        return "/index?faces-redirect=true";
    }
    
}
