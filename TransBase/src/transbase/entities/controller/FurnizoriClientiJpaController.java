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
import transbase.entities.FurnizoriClienti;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class FurnizoriClientiJpaController implements Serializable {

    public FurnizoriClientiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FurnizoriClienti furnizoriClienti) throws PreexistingEntityException, Exception {
        if (furnizoriClienti.getComandaTranspList() == null) {
            furnizoriClienti.setComandaTranspList(new ArrayList<ComandaTransp>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ComandaTransp> attachedComandaTranspList = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListComandaTranspToAttach : furnizoriClienti.getComandaTranspList()) {
                comandaTranspListComandaTranspToAttach = em.getReference(comandaTranspListComandaTranspToAttach.getClass(), comandaTranspListComandaTranspToAttach.getCod());
                attachedComandaTranspList.add(comandaTranspListComandaTranspToAttach);
            }
            furnizoriClienti.setComandaTranspList(attachedComandaTranspList);
            em.persist(furnizoriClienti);
            for (ComandaTransp comandaTranspListComandaTransp : furnizoriClienti.getComandaTranspList()) {
                FurnizoriClienti oldCodFurnizorOfComandaTranspListComandaTransp = comandaTranspListComandaTransp.getCodFurnizor();
                comandaTranspListComandaTransp.setCodFurnizor(furnizoriClienti);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
                if (oldCodFurnizorOfComandaTranspListComandaTransp != null) {
                    oldCodFurnizorOfComandaTranspListComandaTransp.getComandaTranspList().remove(comandaTranspListComandaTransp);
                    oldCodFurnizorOfComandaTranspListComandaTransp = em.merge(oldCodFurnizorOfComandaTranspListComandaTransp);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFurnizoriClienti(furnizoriClienti.getCodScala()) != null) {
                throw new PreexistingEntityException("FurnizoriClienti " + furnizoriClienti + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FurnizoriClienti furnizoriClienti) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FurnizoriClienti persistentFurnizoriClienti = em.find(FurnizoriClienti.class, furnizoriClienti.getCodScala());
            List<ComandaTransp> comandaTranspListOld = persistentFurnizoriClienti.getComandaTranspList();
            List<ComandaTransp> comandaTranspListNew = furnizoriClienti.getComandaTranspList();
            List<ComandaTransp> attachedComandaTranspListNew = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListNewComandaTranspToAttach : comandaTranspListNew) {
                comandaTranspListNewComandaTranspToAttach = em.getReference(comandaTranspListNewComandaTranspToAttach.getClass(), comandaTranspListNewComandaTranspToAttach.getCod());
                attachedComandaTranspListNew.add(comandaTranspListNewComandaTranspToAttach);
            }
            comandaTranspListNew = attachedComandaTranspListNew;
            furnizoriClienti.setComandaTranspList(comandaTranspListNew);
            furnizoriClienti = em.merge(furnizoriClienti);
            for (ComandaTransp comandaTranspListOldComandaTransp : comandaTranspListOld) {
                if (!comandaTranspListNew.contains(comandaTranspListOldComandaTransp)) {
                    comandaTranspListOldComandaTransp.setCodFurnizor(null);
                    comandaTranspListOldComandaTransp = em.merge(comandaTranspListOldComandaTransp);
                }
            }
            for (ComandaTransp comandaTranspListNewComandaTransp : comandaTranspListNew) {
                if (!comandaTranspListOld.contains(comandaTranspListNewComandaTransp)) {
                    FurnizoriClienti oldCodFurnizorOfComandaTranspListNewComandaTransp = comandaTranspListNewComandaTransp.getCodFurnizor();
                    comandaTranspListNewComandaTransp.setCodFurnizor(furnizoriClienti);
                    comandaTranspListNewComandaTransp = em.merge(comandaTranspListNewComandaTransp);
                    if (oldCodFurnizorOfComandaTranspListNewComandaTransp != null && !oldCodFurnizorOfComandaTranspListNewComandaTransp.equals(furnizoriClienti)) {
                        oldCodFurnizorOfComandaTranspListNewComandaTransp.getComandaTranspList().remove(comandaTranspListNewComandaTransp);
                        oldCodFurnizorOfComandaTranspListNewComandaTransp = em.merge(oldCodFurnizorOfComandaTranspListNewComandaTransp);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = furnizoriClienti.getCodScala();
                if (findFurnizoriClienti(id) == null) {
                    throw new NonexistentEntityException("The furnizoriClienti with id " + id + " no longer exists.");
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
            FurnizoriClienti furnizoriClienti;
            try {
                furnizoriClienti = em.getReference(FurnizoriClienti.class, id);
                furnizoriClienti.getCodScala();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The furnizoriClienti with id " + id + " no longer exists.", enfe);
            }
            List<ComandaTransp> comandaTranspList = furnizoriClienti.getComandaTranspList();
            for (ComandaTransp comandaTranspListComandaTransp : comandaTranspList) {
                comandaTranspListComandaTransp.setCodFurnizor(null);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
            }
            em.remove(furnizoriClienti);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FurnizoriClienti> findFurnizoriClientiEntities() {
        return findFurnizoriClientiEntities(true, -1, -1);
    }

    public List<FurnizoriClienti> findFurnizoriClientiEntities(int maxResults, int firstResult) {
        return findFurnizoriClientiEntities(false, maxResults, firstResult);
    }

    private List<FurnizoriClienti> findFurnizoriClientiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FurnizoriClienti.class));
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

    public FurnizoriClienti findFurnizoriClienti(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FurnizoriClienti.class, id);
        } finally {
            em.close();
        }
    }

    public int getFurnizoriClientiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FurnizoriClienti> rt = cq.from(FurnizoriClienti.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
