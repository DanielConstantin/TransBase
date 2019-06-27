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
import transbase.entities.Consignees;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class ConsigneesJpaController implements Serializable {

    public ConsigneesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consignees consignees) throws PreexistingEntityException, Exception {
        if (consignees.getInc2List() == null) {
            consignees.setInc2List(new ArrayList<Inc2>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Inc2> attachedInc2List = new ArrayList<Inc2>();
            for (Inc2 inc2ListInc2ToAttach : consignees.getInc2List()) {
                inc2ListInc2ToAttach = em.getReference(inc2ListInc2ToAttach.getClass(), inc2ListInc2ToAttach.getId());
                attachedInc2List.add(inc2ListInc2ToAttach);
            }
            consignees.setInc2List(attachedInc2List);
            em.persist(consignees);
            for (Inc2 inc2ListInc2 : consignees.getInc2List()) {
                Consignees oldConsigneeOfInc2ListInc2 = inc2ListInc2.getConsignee();
                inc2ListInc2.setConsignee(consignees);
                inc2ListInc2 = em.merge(inc2ListInc2);
                if (oldConsigneeOfInc2ListInc2 != null) {
                    oldConsigneeOfInc2ListInc2.getInc2List().remove(inc2ListInc2);
                    oldConsigneeOfInc2ListInc2 = em.merge(oldConsigneeOfInc2ListInc2);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConsignees(consignees.getIdcons()) != null) {
                throw new PreexistingEntityException("Consignees " + consignees + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consignees consignees) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consignees persistentConsignees = em.find(Consignees.class, consignees.getIdcons());
            List<Inc2> inc2ListOld = persistentConsignees.getInc2List();
            List<Inc2> inc2ListNew = consignees.getInc2List();
            List<Inc2> attachedInc2ListNew = new ArrayList<Inc2>();
            for (Inc2 inc2ListNewInc2ToAttach : inc2ListNew) {
                inc2ListNewInc2ToAttach = em.getReference(inc2ListNewInc2ToAttach.getClass(), inc2ListNewInc2ToAttach.getId());
                attachedInc2ListNew.add(inc2ListNewInc2ToAttach);
            }
            inc2ListNew = attachedInc2ListNew;
            consignees.setInc2List(inc2ListNew);
            consignees = em.merge(consignees);
            for (Inc2 inc2ListOldInc2 : inc2ListOld) {
                if (!inc2ListNew.contains(inc2ListOldInc2)) {
                    inc2ListOldInc2.setConsignee(null);
                    inc2ListOldInc2 = em.merge(inc2ListOldInc2);
                }
            }
            for (Inc2 inc2ListNewInc2 : inc2ListNew) {
                if (!inc2ListOld.contains(inc2ListNewInc2)) {
                    Consignees oldConsigneeOfInc2ListNewInc2 = inc2ListNewInc2.getConsignee();
                    inc2ListNewInc2.setConsignee(consignees);
                    inc2ListNewInc2 = em.merge(inc2ListNewInc2);
                    if (oldConsigneeOfInc2ListNewInc2 != null && !oldConsigneeOfInc2ListNewInc2.equals(consignees)) {
                        oldConsigneeOfInc2ListNewInc2.getInc2List().remove(inc2ListNewInc2);
                        oldConsigneeOfInc2ListNewInc2 = em.merge(oldConsigneeOfInc2ListNewInc2);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consignees.getIdcons();
                if (findConsignees(id) == null) {
                    throw new NonexistentEntityException("The consignees with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consignees consignees;
            try {
                consignees = em.getReference(Consignees.class, id);
                consignees.getIdcons();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consignees with id " + id + " no longer exists.", enfe);
            }
            List<Inc2> inc2List = consignees.getInc2List();
            for (Inc2 inc2ListInc2 : inc2List) {
                inc2ListInc2.setConsignee(null);
                inc2ListInc2 = em.merge(inc2ListInc2);
            }
            em.remove(consignees);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consignees> findConsigneesEntities() {
        return findConsigneesEntities(true, -1, -1);
    }

    public List<Consignees> findConsigneesEntities(int maxResults, int firstResult) {
        return findConsigneesEntities(false, maxResults, firstResult);
    }

    private List<Consignees> findConsigneesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consignees.class));
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

    public Consignees findConsignees(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consignees.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsigneesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consignees> rt = cq.from(Consignees.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
