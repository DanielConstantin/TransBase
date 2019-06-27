/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import transbase.entities.ComandaTransp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.TariISO;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class TariISOJpaController implements Serializable {

    public TariISOJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TariISO tariISO) throws PreexistingEntityException, Exception {
        if (tariISO.getComandaTranspList() == null) {
            tariISO.setComandaTranspList(new ArrayList<ComandaTransp>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ComandaTransp> attachedComandaTranspList = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListComandaTranspToAttach : tariISO.getComandaTranspList()) {
                comandaTranspListComandaTranspToAttach = em.getReference(comandaTranspListComandaTranspToAttach.getClass(), comandaTranspListComandaTranspToAttach.getCod());
                attachedComandaTranspList.add(comandaTranspListComandaTranspToAttach);
            }
            tariISO.setComandaTranspList(attachedComandaTranspList);
            em.persist(tariISO);
            for (ComandaTransp comandaTranspListComandaTransp : tariISO.getComandaTranspList()) {
                TariISO oldTaraOfComandaTranspListComandaTransp = comandaTranspListComandaTransp.getTara();
                comandaTranspListComandaTransp.setTara(tariISO);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
                if (oldTaraOfComandaTranspListComandaTransp != null) {
                    oldTaraOfComandaTranspListComandaTransp.getComandaTranspList().remove(comandaTranspListComandaTransp);
                    oldTaraOfComandaTranspListComandaTransp = em.merge(oldTaraOfComandaTranspListComandaTransp);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTariISO(tariISO.getTara2L()) != null) {
                throw new PreexistingEntityException("TariISO " + tariISO + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TariISO tariISO) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TariISO persistentTariISO = em.find(TariISO.class, tariISO.getTara2L());
            List<ComandaTransp> comandaTranspListOld = persistentTariISO.getComandaTranspList();
            List<ComandaTransp> comandaTranspListNew = tariISO.getComandaTranspList();
            List<ComandaTransp> attachedComandaTranspListNew = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListNewComandaTranspToAttach : comandaTranspListNew) {
                comandaTranspListNewComandaTranspToAttach = em.getReference(comandaTranspListNewComandaTranspToAttach.getClass(), comandaTranspListNewComandaTranspToAttach.getCod());
                attachedComandaTranspListNew.add(comandaTranspListNewComandaTranspToAttach);
            }
            comandaTranspListNew = attachedComandaTranspListNew;
            tariISO.setComandaTranspList(comandaTranspListNew);
            tariISO = em.merge(tariISO);
            for (ComandaTransp comandaTranspListOldComandaTransp : comandaTranspListOld) {
                if (!comandaTranspListNew.contains(comandaTranspListOldComandaTransp)) {
                    comandaTranspListOldComandaTransp.setTara(null);
                    comandaTranspListOldComandaTransp = em.merge(comandaTranspListOldComandaTransp);
                }
            }
            for (ComandaTransp comandaTranspListNewComandaTransp : comandaTranspListNew) {
                if (!comandaTranspListOld.contains(comandaTranspListNewComandaTransp)) {
                    TariISO oldTaraOfComandaTranspListNewComandaTransp = comandaTranspListNewComandaTransp.getTara();
                    comandaTranspListNewComandaTransp.setTara(tariISO);
                    comandaTranspListNewComandaTransp = em.merge(comandaTranspListNewComandaTransp);
                    if (oldTaraOfComandaTranspListNewComandaTransp != null && !oldTaraOfComandaTranspListNewComandaTransp.equals(tariISO)) {
                        oldTaraOfComandaTranspListNewComandaTransp.getComandaTranspList().remove(comandaTranspListNewComandaTransp);
                        oldTaraOfComandaTranspListNewComandaTransp = em.merge(oldTaraOfComandaTranspListNewComandaTransp);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tariISO.getTara2L();
                if (findTariISO(id) == null) {
                    throw new NonexistentEntityException("The tariISO with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TariISO tariISO;
            try {
                tariISO = em.getReference(TariISO.class, id);
                tariISO.getTara2L();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tariISO with id " + id + " no longer exists.", enfe);
            }
            List<ComandaTransp> comandaTranspList = tariISO.getComandaTranspList();
            for (ComandaTransp comandaTranspListComandaTransp : comandaTranspList) {
                comandaTranspListComandaTransp.setTara(null);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
            }
            em.remove(tariISO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TariISO> findTariISOEntities() {
        return findTariISOEntities(true, -1, -1);
    }

    public List<TariISO> findTariISOEntities(int maxResults, int firstResult) {
        return findTariISOEntities(false, maxResults, firstResult);
    }
    
    public String[] findTariISOString (){
         EntityManager em = getEntityManager();
        try {
            
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TariISO.class));
            Query q = em.createQuery(cq);
            List<TariISO> lst = q.getResultList();
            String[]strarr= new String[lst.size()]; 
            
            for(int i=0;i<lst.size();i++){
                strarr[i]=lst.get(i).toString();
            }
            
            return strarr;
        } finally {
            em.close();
        }
    }
    private List<TariISO> findTariISOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TariISO.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TariISO findTariISO(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TariISO.class, id);
        } finally {
            em.close();
        }
    }

    public int getTariISOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TariISO> rt = cq.from(TariISO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
