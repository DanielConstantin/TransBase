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
import transbase.entities.Loadingpl;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.TipOp;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class TipOpJpaController implements Serializable {

    public TipOpJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipOp tipOp) throws PreexistingEntityException, Exception {
        if (tipOp.getLoadingplList() == null) {
            tipOp.setLoadingplList(new ArrayList<Loadingpl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Loadingpl> attachedLoadingplList = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListLoadingplToAttach : tipOp.getLoadingplList()) {
                loadingplListLoadingplToAttach = em.getReference(loadingplListLoadingplToAttach.getClass(), loadingplListLoadingplToAttach.getIdLp());
                attachedLoadingplList.add(loadingplListLoadingplToAttach);
            }
            tipOp.setLoadingplList(attachedLoadingplList);
            em.persist(tipOp);
            for (Loadingpl loadingplListLoadingpl : tipOp.getLoadingplList()) {
                TipOp oldTipOpOfLoadingplListLoadingpl = loadingplListLoadingpl.getTipOp();
                loadingplListLoadingpl.setTipOp(tipOp);
                loadingplListLoadingpl = em.merge(loadingplListLoadingpl);
                if (oldTipOpOfLoadingplListLoadingpl != null) {
                    oldTipOpOfLoadingplListLoadingpl.getLoadingplList().remove(loadingplListLoadingpl);
                    oldTipOpOfLoadingplListLoadingpl = em.merge(oldTipOpOfLoadingplListLoadingpl);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipOp(tipOp.getId()) != null) {
                throw new PreexistingEntityException("TipOp " + tipOp + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipOp tipOp) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipOp persistentTipOp = em.find(TipOp.class, tipOp.getId());
            List<Loadingpl> loadingplListOld = persistentTipOp.getLoadingplList();
            List<Loadingpl> loadingplListNew = tipOp.getLoadingplList();
            List<Loadingpl> attachedLoadingplListNew = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListNewLoadingplToAttach : loadingplListNew) {
                loadingplListNewLoadingplToAttach = em.getReference(loadingplListNewLoadingplToAttach.getClass(), loadingplListNewLoadingplToAttach.getIdLp());
                attachedLoadingplListNew.add(loadingplListNewLoadingplToAttach);
            }
            loadingplListNew = attachedLoadingplListNew;
            tipOp.setLoadingplList(loadingplListNew);
            tipOp = em.merge(tipOp);
            for (Loadingpl loadingplListOldLoadingpl : loadingplListOld) {
                if (!loadingplListNew.contains(loadingplListOldLoadingpl)) {
                    loadingplListOldLoadingpl.setTipOp(null);
                    loadingplListOldLoadingpl = em.merge(loadingplListOldLoadingpl);
                }
            }
            for (Loadingpl loadingplListNewLoadingpl : loadingplListNew) {
                if (!loadingplListOld.contains(loadingplListNewLoadingpl)) {
                    TipOp oldTipOpOfLoadingplListNewLoadingpl = loadingplListNewLoadingpl.getTipOp();
                    loadingplListNewLoadingpl.setTipOp(tipOp);
                    loadingplListNewLoadingpl = em.merge(loadingplListNewLoadingpl);
                    if (oldTipOpOfLoadingplListNewLoadingpl != null && !oldTipOpOfLoadingplListNewLoadingpl.equals(tipOp)) {
                        oldTipOpOfLoadingplListNewLoadingpl.getLoadingplList().remove(loadingplListNewLoadingpl);
                        oldTipOpOfLoadingplListNewLoadingpl = em.merge(oldTipOpOfLoadingplListNewLoadingpl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipOp.getId();
                if (findTipOp(id) == null) {
                    throw new NonexistentEntityException("The tipOp with id " + id + " no longer exists.");
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
            TipOp tipOp;
            try {
                tipOp = em.getReference(TipOp.class, id);
                tipOp.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipOp with id " + id + " no longer exists.", enfe);
            }
            List<Loadingpl> loadingplList = tipOp.getLoadingplList();
            for (Loadingpl loadingplListLoadingpl : loadingplList) {
                loadingplListLoadingpl.setTipOp(null);
                loadingplListLoadingpl = em.merge(loadingplListLoadingpl);
            }
            em.remove(tipOp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipOp> findTipOpEntities() {
        return findTipOpEntities(true, -1, -1);
    }

    public List<TipOp> findTipOpEntities(int maxResults, int firstResult) {
        return findTipOpEntities(false, maxResults, firstResult);
    }

    private List<TipOp> findTipOpEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipOp.class));
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

    public TipOp findTipOp(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipOp.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipOpCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipOp> rt = cq.from(TipOp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
