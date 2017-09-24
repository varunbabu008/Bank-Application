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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ACCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAcid", query = "SELECT a FROM Account a WHERE a.acid = :acid"),
    @NamedQuery(name = "Account.findByAcbal", query = "SELECT a FROM Account a WHERE a.acbal = :acbal"),
    @NamedQuery(name = "Account.findByActype", query = "SELECT a FROM Account a WHERE a.actype = :actype")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACID")
    private Integer acid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ACBAL")
    private Double acbal;
    @Size(max = 50)
    @Column(name = "ACTYPE")
    private String actype;
    @JoinColumn(name = "CUSTID", referencedColumnName = "CUSID")
    @ManyToOne
    private Customeruser custid;
    @OneToMany(mappedBy = "accountid")
    private Collection<Banktansactions> banktansactionsCollection;

    public Account() {
    }

    public Account(Integer acid) {
        this.acid = acid;
    }

    public Integer getAcid() {
        return acid;
    }

    public void setAcid(Integer acid) {
        this.acid = acid;
    }

    public Double getAcbal() {
        return acbal;
    }

    public void setAcbal(Double acbal) {
        this.acbal = acbal;
    }

    public String getActype() {
        return actype;
    }

    public void setActype(String actype) {
        this.actype = actype;
    }

    public Customeruser getCustid() {
        return custid;
    }

    public void setCustid(Customeruser custid) {
        this.custid = custid;
    }

    @XmlTransient
    public Collection<Banktansactions> getBanktansactionsCollection() {
        return banktansactionsCollection;
    }

    public void setBanktansactionsCollection(Collection<Banktansactions> banktansactionsCollection) {
        this.banktansactionsCollection = banktansactionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acid != null ? acid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.acid == null && other.acid != null) || (this.acid != null && !this.acid.equals(other.acid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bankApplication.Account[ acid=" + acid + " ]";
    }
    
}
