/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankApplication;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author varunbabu
 */
@Stateless
public class BanktansactionsFacade extends AbstractFacade<Banktansactions> {

    @PersistenceContext(unitName = "AssignmentPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BanktansactionsFacade() {
        super(Banktansactions.class);
    }
    
}
