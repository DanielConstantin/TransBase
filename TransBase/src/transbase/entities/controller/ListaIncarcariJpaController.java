/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase.entities.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import transbase.entities.ListaIncarcari;
import transbase.entities.TipTranzactie;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class ListaIncarcariJpaController implements Serializable {

    public ListaIncarcariJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ListaIncarcari listaIncarcari) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipTranzactie tip = listaIncarcari.getTip();
            if (tip != null) {
                tip = em.getReference(tip.getClass(), tip.getIdTip());
                listaIncarcari.setTip(tip);
            }
            em.persist(listaIncarcari);
            if (tip != null) {
                tip.getListaIncarcariList().add(listaIncarcari);
                tip = em.merge(tip);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findListaIncarcari(listaIncarcari.getComanda()) != null) {
                throw new PreexistingEntityException("ListaIncarcari " + listaIncarcari + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ListaIncarcari listaIncarcari) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ListaIncarcari persistentListaIncarcari = em.find(ListaIncarcari.class, listaIncarcari.getComanda());
            TipTranzactie tipOld = persistentListaIncarcari.getTip();
            TipTranzactie tipNew = listaIncarcari.getTip();
            if (tipNew != null) {
                tipNew = em.getReference(tipNew.getClass(), tipNew.getIdTip());
                listaIncarcari.setTip(tipNew);
            }
            listaIncarcari = em.merge(listaIncarcari);
            if (tipOld != null && !tipOld.equals(tipNew)) {
                tipOld.getListaIncarcariList().remove(listaIncarcari);
                tipOld = em.merge(tipOld);
            }
            if (tipNew != null && !tipNew.equals(tipOld)) {
                tipNew.getListaIncarcariList().add(listaIncarcari);
                tipNew = em.merge(tipNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = listaIncarcari.getComanda();
                if (findListaIncarcari(id) == null) {
                    throw new NonexistentEntityException("The listaIncarcari with id " + id + " no longer exists.");
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
            ListaIncarcari listaIncarcari;
            try {
                listaIncarcari = em.getReference(ListaIncarcari.class, id);
                listaIncarcari.getComanda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The listaIncarcari with id " + id + " no longer exists.", enfe);
            }
            TipTranzactie tip = listaIncarcari.getTip();
            if (tip != null) {
                tip.getListaIncarcariList().remove(listaIncarcari);
                tip = em.merge(tip);
            }
            em.remove(listaIncarcari);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ListaIncarcari> findListaIncarcariEntities() {
        return findListaIncarcariEntities(true, -1, -1);
    }

    public List<ListaIncarcari> findListaIncarcariEntities(int maxResults, int firstResult) {
        return findListaIncarcariEntities(false, maxResults, firstResult);
    }

    private List<ListaIncarcari> findListaIncarcariEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ListaIncarcari.class));
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

    public ListaIncarcari findListaIncarcari(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ListaIncarcari.class, id);
        } finally {
            em.close();
        }
    }

    public int getListaIncarcariCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ListaIncarcari> rt = cq.from(ListaIncarcari.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
