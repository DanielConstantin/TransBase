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
import transbase.entities.ListaIncarcari;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.TipTranzactie;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class TipTranzactieJpaController implements Serializable {

    public TipTranzactieJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipTranzactie tipTranzactie) throws PreexistingEntityException, Exception {
        if (tipTranzactie.getListaIncarcariList() == null) {
            tipTranzactie.setListaIncarcariList(new ArrayList<ListaIncarcari>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ListaIncarcari> attachedListaIncarcariList = new ArrayList<ListaIncarcari>();
            for (ListaIncarcari listaIncarcariListListaIncarcariToAttach : tipTranzactie.getListaIncarcariList()) {
                listaIncarcariListListaIncarcariToAttach = em.getReference(listaIncarcariListListaIncarcariToAttach.getClass(), listaIncarcariListListaIncarcariToAttach.getComanda());
                attachedListaIncarcariList.add(listaIncarcariListListaIncarcariToAttach);
            }
            tipTranzactie.setListaIncarcariList(attachedListaIncarcariList);
            em.persist(tipTranzactie);
            for (ListaIncarcari listaIncarcariListListaIncarcari : tipTranzactie.getListaIncarcariList()) {
                TipTranzactie oldTipOfListaIncarcariListListaIncarcari = listaIncarcariListListaIncarcari.getTip();
                listaIncarcariListListaIncarcari.setTip(tipTranzactie);
                listaIncarcariListListaIncarcari = em.merge(listaIncarcariListListaIncarcari);
                if (oldTipOfListaIncarcariListListaIncarcari != null) {
                    oldTipOfListaIncarcariListListaIncarcari.getListaIncarcariList().remove(listaIncarcariListListaIncarcari);
                    oldTipOfListaIncarcariListListaIncarcari = em.merge(oldTipOfListaIncarcariListListaIncarcari);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipTranzactie(tipTranzactie.getIdTip()) != null) {
                throw new PreexistingEntityException("TipTranzactie " + tipTranzactie + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipTranzactie tipTranzactie) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipTranzactie persistentTipTranzactie = em.find(TipTranzactie.class, tipTranzactie.getIdTip());
            List<ListaIncarcari> listaIncarcariListOld = persistentTipTranzactie.getListaIncarcariList();
            List<ListaIncarcari> listaIncarcariListNew = tipTranzactie.getListaIncarcariList();
            List<ListaIncarcari> attachedListaIncarcariListNew = new ArrayList<ListaIncarcari>();
            for (ListaIncarcari listaIncarcariListNewListaIncarcariToAttach : listaIncarcariListNew) {
                listaIncarcariListNewListaIncarcariToAttach = em.getReference(listaIncarcariListNewListaIncarcariToAttach.getClass(), listaIncarcariListNewListaIncarcariToAttach.getComanda());
                attachedListaIncarcariListNew.add(listaIncarcariListNewListaIncarcariToAttach);
            }
            listaIncarcariListNew = attachedListaIncarcariListNew;
            tipTranzactie.setListaIncarcariList(listaIncarcariListNew);
            tipTranzactie = em.merge(tipTranzactie);
            for (ListaIncarcari listaIncarcariListOldListaIncarcari : listaIncarcariListOld) {
                if (!listaIncarcariListNew.contains(listaIncarcariListOldListaIncarcari)) {
                    listaIncarcariListOldListaIncarcari.setTip(null);
                    listaIncarcariListOldListaIncarcari = em.merge(listaIncarcariListOldListaIncarcari);
                }
            }
            for (ListaIncarcari listaIncarcariListNewListaIncarcari : listaIncarcariListNew) {
                if (!listaIncarcariListOld.contains(listaIncarcariListNewListaIncarcari)) {
                    TipTranzactie oldTipOfListaIncarcariListNewListaIncarcari = listaIncarcariListNewListaIncarcari.getTip();
                    listaIncarcariListNewListaIncarcari.setTip(tipTranzactie);
                    listaIncarcariListNewListaIncarcari = em.merge(listaIncarcariListNewListaIncarcari);
                    if (oldTipOfListaIncarcariListNewListaIncarcari != null && !oldTipOfListaIncarcariListNewListaIncarcari.equals(tipTranzactie)) {
                        oldTipOfListaIncarcariListNewListaIncarcari.getListaIncarcariList().remove(listaIncarcariListNewListaIncarcari);
                        oldTipOfListaIncarcariListNewListaIncarcari = em.merge(oldTipOfListaIncarcariListNewListaIncarcari);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipTranzactie.getIdTip();
                if (findTipTranzactie(id) == null) {
                    throw new NonexistentEntityException("The tipTranzactie with id " + id + " no longer exists.");
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
            TipTranzactie tipTranzactie;
            try {
                tipTranzactie = em.getReference(TipTranzactie.class, id);
                tipTranzactie.getIdTip();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipTranzactie with id " + id + " no longer exists.", enfe);
            }
            List<ListaIncarcari> listaIncarcariList = tipTranzactie.getListaIncarcariList();
            for (ListaIncarcari listaIncarcariListListaIncarcari : listaIncarcariList) {
                listaIncarcariListListaIncarcari.setTip(null);
                listaIncarcariListListaIncarcari = em.merge(listaIncarcariListListaIncarcari);
            }
            em.remove(tipTranzactie);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipTranzactie> findTipTranzactieEntities() {
        return findTipTranzactieEntities(true, -1, -1);
    }

    public List<TipTranzactie> findTipTranzactieEntities(int maxResults, int firstResult) {
        return findTipTranzactieEntities(false, maxResults, firstResult);
    }

    private List<TipTranzactie> findTipTranzactieEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipTranzactie.class));
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

    public TipTranzactie findTipTranzactie(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipTranzactie.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipTranzactieCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipTranzactie> rt = cq.from(TipTranzactie.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
