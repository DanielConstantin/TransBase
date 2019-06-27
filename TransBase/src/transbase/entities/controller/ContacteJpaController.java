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
import transbase.entities.Contacte;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class ContacteJpaController implements Serializable {

    public ContacteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacte contacte) throws PreexistingEntityException, Exception {
        if (contacte.getInc2List() == null) {
            contacte.setInc2List(new ArrayList<Inc2>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Inc2> attachedInc2List = new ArrayList<Inc2>();
            for (Inc2 inc2ListInc2ToAttach : contacte.getInc2List()) {
                inc2ListInc2ToAttach = em.getReference(inc2ListInc2ToAttach.getClass(), inc2ListInc2ToAttach.getId());
                attachedInc2List.add(inc2ListInc2ToAttach);
            }
            contacte.setInc2List(attachedInc2List);
            em.persist(contacte);
            for (Inc2 inc2ListInc2 : contacte.getInc2List()) {
                Contacte oldContacteOfInc2ListInc2 = inc2ListInc2.getContacte();
                inc2ListInc2.setContacte(contacte);
                inc2ListInc2 = em.merge(inc2ListInc2);
                if (oldContacteOfInc2ListInc2 != null) {
                    oldContacteOfInc2ListInc2.getInc2List().remove(inc2ListInc2);
                    oldContacteOfInc2ListInc2 = em.merge(oldContacteOfInc2ListInc2);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContacte(contacte.getIdPersoanaCont()) != null) {
                throw new PreexistingEntityException("Contacte " + contacte + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacte contacte) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacte persistentContacte = em.find(Contacte.class, contacte.getIdPersoanaCont());
            List<Inc2> inc2ListOld = persistentContacte.getInc2List();
            List<Inc2> inc2ListNew = contacte.getInc2List();
            List<Inc2> attachedInc2ListNew = new ArrayList<Inc2>();
            for (Inc2 inc2ListNewInc2ToAttach : inc2ListNew) {
                inc2ListNewInc2ToAttach = em.getReference(inc2ListNewInc2ToAttach.getClass(), inc2ListNewInc2ToAttach.getId());
                attachedInc2ListNew.add(inc2ListNewInc2ToAttach);
            }
            inc2ListNew = attachedInc2ListNew;
            contacte.setInc2List(inc2ListNew);
            contacte = em.merge(contacte);
            for (Inc2 inc2ListOldInc2 : inc2ListOld) {
                if (!inc2ListNew.contains(inc2ListOldInc2)) {
                    inc2ListOldInc2.setContacte(null);
                    inc2ListOldInc2 = em.merge(inc2ListOldInc2);
                }
            }
            for (Inc2 inc2ListNewInc2 : inc2ListNew) {
                if (!inc2ListOld.contains(inc2ListNewInc2)) {
                    Contacte oldContacteOfInc2ListNewInc2 = inc2ListNewInc2.getContacte();
                    inc2ListNewInc2.setContacte(contacte);
                    inc2ListNewInc2 = em.merge(inc2ListNewInc2);
                    if (oldContacteOfInc2ListNewInc2 != null && !oldContacteOfInc2ListNewInc2.equals(contacte)) {
                        oldContacteOfInc2ListNewInc2.getInc2List().remove(inc2ListNewInc2);
                        oldContacteOfInc2ListNewInc2 = em.merge(oldContacteOfInc2ListNewInc2);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = contacte.getIdPersoanaCont();
                if (findContacte(id) == null) {
                    throw new NonexistentEntityException("The contacte with id " + id + " no longer exists.");
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
            Contacte contacte;
            try {
                contacte = em.getReference(Contacte.class, id);
                contacte.getIdPersoanaCont();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacte with id " + id + " no longer exists.", enfe);
            }
            List<Inc2> inc2List = contacte.getInc2List();
            for (Inc2 inc2ListInc2 : inc2List) {
                inc2ListInc2.setContacte(null);
                inc2ListInc2 = em.merge(inc2ListInc2);
            }
            em.remove(contacte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacte> findContacteEntities() {
        return findContacteEntities(true, -1, -1);
    }

    public List<Contacte> findContacteEntities(int maxResults, int firstResult) {
        return findContacteEntities(false, maxResults, firstResult);
    }

    private List<Contacte> findContacteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacte.class));
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

    public Contacte findContacte(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacte.class, id);
        } finally {
            em.close();
        }
    }

    public int getContacteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacte> rt = cq.from(Contacte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
