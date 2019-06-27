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
import transbase.entities.ComandaTransp;
import transbase.entities.Inc2;
import transbase.entities.Loadingpl;
import transbase.entities.TipOp;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class LoadingplJpaController implements Serializable {

    public LoadingplJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Loadingpl loadingpl) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaTransp lodpl = loadingpl.getLodpl();
            if (lodpl != null) {
                lodpl = em.getReference(lodpl.getClass(), lodpl.getCod());
                loadingpl.setLodpl(lodpl);
            }
            Inc2 cod = loadingpl.getCod();
            if (cod != null) {
                cod = em.getReference(cod.getClass(), cod.getId());
                loadingpl.setCod(cod);
            }
            TipOp tipOp = loadingpl.getTipOp();
            if (tipOp != null) {
                tipOp = em.getReference(tipOp.getClass(), tipOp.getId());
                loadingpl.setTipOp(tipOp);
            }
            em.persist(loadingpl);
            if (lodpl != null) {
                lodpl.getLoadingplList().add(loadingpl);
                lodpl = em.merge(lodpl);
            }
            if (cod != null) {
                cod.getLoadingplList().add(loadingpl);
                cod = em.merge(cod);
            }
            if (tipOp != null) {
                tipOp.getLoadingplList().add(loadingpl);
                tipOp = em.merge(tipOp);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLoadingpl(loadingpl.getIdLp()) != null) {
                throw new PreexistingEntityException("Loadingpl " + loadingpl + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Loadingpl loadingpl) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Loadingpl persistentLoadingpl = em.find(Loadingpl.class, loadingpl.getIdLp());
            ComandaTransp lodplOld = persistentLoadingpl.getLodpl();
            ComandaTransp lodplNew = loadingpl.getLodpl();
            Inc2 codOld = persistentLoadingpl.getCod();
            Inc2 codNew = loadingpl.getCod();
            TipOp tipOpOld = persistentLoadingpl.getTipOp();
            TipOp tipOpNew = loadingpl.getTipOp();
            if (lodplNew != null) {
                lodplNew = em.getReference(lodplNew.getClass(), lodplNew.getCod());
                loadingpl.setLodpl(lodplNew);
            }
            if (codNew != null) {
                codNew = em.getReference(codNew.getClass(), codNew.getId());
                loadingpl.setCod(codNew);
            }
            if (tipOpNew != null) {
                tipOpNew = em.getReference(tipOpNew.getClass(), tipOpNew.getId());
                loadingpl.setTipOp(tipOpNew);
            }
            loadingpl = em.merge(loadingpl);
            if (lodplOld != null && !lodplOld.equals(lodplNew)) {
                lodplOld.getLoadingplList().remove(loadingpl);
                lodplOld = em.merge(lodplOld);
            }
            if (lodplNew != null && !lodplNew.equals(lodplOld)) {
                lodplNew.getLoadingplList().add(loadingpl);
                lodplNew = em.merge(lodplNew);
            }
            if (codOld != null && !codOld.equals(codNew)) {
                codOld.getLoadingplList().remove(loadingpl);
                codOld = em.merge(codOld);
            }
            if (codNew != null && !codNew.equals(codOld)) {
                codNew.getLoadingplList().add(loadingpl);
                codNew = em.merge(codNew);
            }
            if (tipOpOld != null && !tipOpOld.equals(tipOpNew)) {
                tipOpOld.getLoadingplList().remove(loadingpl);
                tipOpOld = em.merge(tipOpOld);
            }
            if (tipOpNew != null && !tipOpNew.equals(tipOpOld)) {
                tipOpNew.getLoadingplList().add(loadingpl);
                tipOpNew = em.merge(tipOpNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = loadingpl.getIdLp();
                if (findLoadingpl(id) == null) {
                    throw new NonexistentEntityException("The loadingpl with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Loadingpl loadingpl;
            try {
                loadingpl = em.getReference(Loadingpl.class, id);
                loadingpl.getIdLp();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The loadingpl with id " + id + " no longer exists.", enfe);
            }
            ComandaTransp lodpl = loadingpl.getLodpl();
            if (lodpl != null) {
                lodpl.getLoadingplList().remove(loadingpl);
                lodpl = em.merge(lodpl);
            }
            Inc2 cod = loadingpl.getCod();
            if (cod != null) {
                cod.getLoadingplList().remove(loadingpl);
                cod = em.merge(cod);
            }
            TipOp tipOp = loadingpl.getTipOp();
            if (tipOp != null) {
                tipOp.getLoadingplList().remove(loadingpl);
                tipOp = em.merge(tipOp);
            }
            em.remove(loadingpl);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Loadingpl> findLoadingplEntities() {
        return findLoadingplEntities(true, -1, -1);
    }

    public List<Loadingpl> findLoadingplEntities(int maxResults, int firstResult) {
        return findLoadingplEntities(false, maxResults, firstResult);
    }

    private List<Loadingpl> findLoadingplEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Loadingpl.class));
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

    public Loadingpl findLoadingpl(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Loadingpl.class, id);
        } finally {
            em.close();
        }
    }

    public int getLoadingplCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Loadingpl> rt = cq.from(Loadingpl.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
