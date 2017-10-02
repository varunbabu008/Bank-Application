/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankApplication;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author varunbabu
 */
@Entity
@Table(name = "CUSTOMERUSER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customeruser.findAll", query = "SELECT c FROM Customeruser c"),
    @NamedQuery(name = "Customeruser.findByCusid", query = "SELECT c FROM Customeruser c WHERE c.cusid = :cusid"),
    @NamedQuery(name = "Customeruser.findByFirstname", query = "SELECT c FROM Customeruser c WHERE c.firstname = :firstname"),
    @NamedQuery(name = "Customeruser.findByLastname", query = "SELECT c FROM Customeruser c WHERE c.lastname = :lastname"),
    @NamedQuery(name = "Customeruser.findByEmail", query = "SELECT c FROM Customeruser c WHERE c.email = :email"),
    @NamedQuery(name = "Customeruser.findByPassword", query = "SELECT c FROM Customeruser c WHERE c.password = :password"),
    @NamedQuery(name = "Customeruser.findByAddress", query = "SELECT c FROM Customeruser c WHERE c.address = :address"),
    @NamedQuery(name = "Customeruser.findByPhonenumber", query = "SELECT c FROM Customeruser c WHERE c.phonenumber = :phonenumber"),
    @NamedQuery(name = "Customeruser.findByTypes", query = "SELECT c FROM Customeruser c WHERE c.types = :types")})
public class Customeruser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CUSID")
    private Integer cusid;
    @Size(max = 20)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 20)
    @Column(name = "LASTNAME")
    private String lastname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 20)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 20)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 25)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 10)
    @Column(name = "PHONENUMBER")
    private String phonenumber;
    @Column(name = "TYPES")
    private Integer types;
    @OneToMany(mappedBy = "custid")
    private Collection<Account> accountCollection;

    public Customeruser() {
    }

    public Customeruser(Integer cusid) {
        this.cusid = cusid;
    }

    public Integer getCusid() {
        return cusid;
    }

    public void setCusid(Integer cusid) {
        this.cusid = cusid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    @XmlTransient
    public Collection<Account> getAccountCollection() {
        return accountCollection;
    }

    public void setAccountCollection(Collection<Account> accountCollection) {
        this.accountCollection = accountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cusid != null ? cusid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customeruser)) {
            return false;
        }
        Customeruser other = (Customeruser) object;
        if ((this.cusid == null && other.cusid != null) || (this.cusid != null && !this.cusid.equals(other.cusid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomerID: " + cusid + " ]";
    }
    
}
