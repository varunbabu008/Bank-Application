/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankApplication;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author varunbabu
 */
@Entity
@Table(name = "BANKTANSACTIONS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Banktansactions.findAll", query = "SELECT b FROM Banktansactions b"),
    @NamedQuery(name = "Banktansactions.findByTrid", query = "SELECT b FROM Banktansactions b WHERE b.trid = :trid"),
    @NamedQuery(name = "Banktansactions.findByAmount", query = "SELECT b FROM Banktansactions b WHERE b.amount = :amount"),
    @NamedQuery(name = "Banktansactions.findByTrtime", query = "SELECT b FROM Banktansactions b WHERE b.trtime = :trtime")})
public class Banktansactions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TRID")
    private Integer trid;
    @Column(name = "AMOUNT")
    private Integer amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TRTIME")
    @Temporal(TemporalType.DATE)
    private Date trtime;
    @JoinColumn(name = "ACCOUNTID", referencedColumnName = "ACID")
    @ManyToOne
    private Account accountid;
    
    private Account fromaccnt;

    public Banktansactions() {
    }

    public Banktansactions(Integer trid) {
        this.trid = trid;
    }

    public Banktansactions(Integer trid, Date trtime) {
        this.trid = trid;
        this.trtime = trtime;
    }

    public Integer getTrid() {
        return trid;
    }

    public void setTrid(Integer trid) {
        this.trid = trid;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getTrtime() {
        return trtime;
    }

    public void setTrtime(Date trtime) {
        this.trtime = trtime;
    }

    public Account getAccountid() {
        return accountid;
    }

    public void setAccountid(Account accountid) {
        this.accountid = accountid;
    }
    
    public Account getFromaccnt() {
        return fromaccnt;
    }

    public void setFromaccnt(Account fromaccnt) {
        this.fromaccnt = fromaccnt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (trid != null ? trid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Banktansactions)) {
            return false;
        }
        Banktansactions other = (Banktansactions) object;
        if ((this.trid == null && other.trid != null) || (this.trid != null && !this.trid.equals(other.trid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bankApplication.Banktansactions[ trid=" + trid + " ]";
    }
    
}
