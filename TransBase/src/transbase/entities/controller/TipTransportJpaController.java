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
import transbase.entities.Inc2;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.TipTransport;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class TipTransportJpaController implements Serializable {

    public TipTransportJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipTransport tipTransport) throws PreexistingEntityException, Exception {
        if (tipTransport.getInc2List() == null) {
            tipTransport.setInc2List(new ArrayList<Inc2>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Inc2> attachedInc2List = new ArrayList<Inc2>();
            for (Inc2 inc2ListInc2ToAttach : tipTransport.getInc2List()) {
                inc2ListInc2ToAttach = em.getReference(inc2ListInc2ToAttach.getClass(), inc2ListInc2ToAttach.getId());
                attachedInc2List.add(inc2ListInc2ToAttach);
            }
            tipTransport.setInc2List(attachedInc2List);
            em.persist(tipTransport);
            for (Inc2 inc2ListInc2 : tipTransport.getInc2List()) {
                TipTransport oldTipTranspOfInc2ListInc2 = inc2ListInc2.getTipTransp();
                inc2ListInc2.setTipTransp(tipTransport);
                inc2ListInc2 = em.merge(inc2ListInc2);
                if (oldTipTranspOfInc2ListInc2 != null) {
                    oldTipTranspOfInc2ListInc2.getInc2List().remove(inc2ListInc2);
                    oldTipTranspOfInc2ListInc2 = em.merge(oldTipTranspOfInc2ListInc2);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipTransport(tipTransport.getTiptransport()) != null) {
                throw new PreexistingEntityException("TipTransport " + tipTransport + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipTransport tipTransport) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipTransport persistentTipTransport = em.find(TipTransport.class, tipTransport.getTiptransport());
            List<Inc2> inc2ListOld = persistentTipTransport.getInc2List();
            List<Inc2> inc2ListNew = tipTransport.getInc2List();
            List<Inc2> attachedInc2ListNew = new ArrayList<Inc2>();
            for (Inc2 inc2ListNewInc2ToAttach : inc2ListNew) {
                inc2ListNewInc2ToAttach = em.getReference(inc2ListNewInc2ToAttach.getClass(), inc2ListNewInc2ToAttach.getId());
                attachedInc2ListNew.add(inc2ListNewInc2ToAttach);
            }
            inc2ListNew = attachedInc2ListNew;
            tipTransport.setInc2List(inc2ListNew);
            tipTransport = em.merge(tipTransport);
            for (Inc2 inc2ListOldInc2 : inc2ListOld) {
                if (!inc2ListNew.contains(inc2ListOldInc2)) {
                    inc2ListOldInc2.setTipTransp(null);
                    inc2ListOldInc2 = em.merge(inc2ListOldInc2);
                }
            }
            for (Inc2 inc2ListNewInc2 : inc2ListNew) {
                if (!inc2ListOld.contains(inc2ListNewInc2)) {
                    TipTransport oldTipTranspOfInc2ListNewInc2 = inc2ListNewInc2.getTipTransp();
                    inc2ListNewInc2.setTipTransp(tipTransport);
                    inc2ListNewInc2 = em.merge(inc2ListNewInc2);
                    if (oldTipTranspOfInc2ListNewInc2 != null && !oldTipTranspOfInc2ListNewInc2.equals(tipTransport)) {
                        oldTipTranspOfInc2ListNewInc2.getInc2List().remove(inc2ListNewInc2);
                        oldTipTranspOfInc2ListNewInc2 = em.merge(oldTipTranspOfInc2ListNewInc2);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipTransport.getTiptransport();
                if (findTipTransport(id) == null) {
                    throw new NonexistentEntityException("The tipTransport with id " + id + " no longer exists.");
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
            TipTransport tipTransport;
            try {
                tipTransport = em.getReference(TipTransport.class, id);
                tipTransport.getTiptransport();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipTransport with id " + id + " no longer exists.", enfe);
            }
            List<Inc2> inc2List = tipTransport.getInc2List();
            for (Inc2 inc2ListInc2 : inc2List) {
                inc2ListInc2.setTipTransp(null);
                inc2ListInc2 = em.merge(inc2ListInc2);
            }
            em.remove(tipTransport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipTransport> findTipTransportEntities() {
        return findTipTransportEntities(true, -1, -1);
    }

    public List<TipTransport> findTipTransportEntities(int maxResults, int firstResult) {
        return findTipTransportEntities(false, maxResults, firstResult);
    }

    private List<TipTransport> findTipTransportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipTransport.class));
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

    public TipTransport findTipTransport(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipTransport.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipTransportCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipTransport> rt = cq.from(TipTransport.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
