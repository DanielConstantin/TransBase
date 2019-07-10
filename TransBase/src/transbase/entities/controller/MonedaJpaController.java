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
import transbase.entities.FurnizoriClienti;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.Moneda;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class MonedaJpaController implements Serializable {

    public MonedaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Moneda moneda) throws PreexistingEntityException, Exception {
        if (moneda.getFurnizoriClientiList() == null) {
            moneda.setFurnizoriClientiList(new ArrayList<FurnizoriClienti>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FurnizoriClienti> attachedFurnizoriClientiList = new ArrayList<FurnizoriClienti>();
            for (FurnizoriClienti furnizoriClientiListFurnizoriClientiToAttach : moneda.getFurnizoriClientiList()) {
                furnizoriClientiListFurnizoriClientiToAttach = em.getReference(furnizoriClientiListFurnizoriClientiToAttach.getClass(), furnizoriClientiListFurnizoriClientiToAttach.getCodScala());
                attachedFurnizoriClientiList.add(furnizoriClientiListFurnizoriClientiToAttach);
            }
            moneda.setFurnizoriClientiList(attachedFurnizoriClientiList);
            em.persist(moneda);
            for (FurnizoriClienti furnizoriClientiListFurnizoriClienti : moneda.getFurnizoriClientiList()) {
                Moneda oldMonedaOfFurnizoriClientiListFurnizoriClienti = furnizoriClientiListFurnizoriClienti.getMoneda();
                furnizoriClientiListFurnizoriClienti.setMoneda(moneda);
                furnizoriClientiListFurnizoriClienti = em.merge(furnizoriClientiListFurnizoriClienti);
                if (oldMonedaOfFurnizoriClientiListFurnizoriClienti != null) {
                    oldMonedaOfFurnizoriClientiListFurnizoriClienti.getFurnizoriClientiList().remove(furnizoriClientiListFurnizoriClienti);
                    oldMonedaOfFurnizoriClientiListFurnizoriClienti = em.merge(oldMonedaOfFurnizoriClientiListFurnizoriClienti);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMoneda(moneda.getIdMoneda()) != null) {
                throw new PreexistingEntityException("Moneda " + moneda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Moneda moneda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Moneda persistentMoneda = em.find(Moneda.class, moneda.getIdMoneda());
            List<FurnizoriClienti> furnizoriClientiListOld = persistentMoneda.getFurnizoriClientiList();
            List<FurnizoriClienti> furnizoriClientiListNew = moneda.getFurnizoriClientiList();
            List<FurnizoriClienti> attachedFurnizoriClientiListNew = new ArrayList<FurnizoriClienti>();
            for (FurnizoriClienti furnizoriClientiListNewFurnizoriClientiToAttach : furnizoriClientiListNew) {
                furnizoriClientiListNewFurnizoriClientiToAttach = em.getReference(furnizoriClientiListNewFurnizoriClientiToAttach.getClass(), furnizoriClientiListNewFurnizoriClientiToAttach.getCodScala());
                attachedFurnizoriClientiListNew.add(furnizoriClientiListNewFurnizoriClientiToAttach);
            }
            furnizoriClientiListNew = attachedFurnizoriClientiListNew;
            moneda.setFurnizoriClientiList(furnizoriClientiListNew);
            moneda = em.merge(moneda);
            for (FurnizoriClienti furnizoriClientiListOldFurnizoriClienti : furnizoriClientiListOld) {
                if (!furnizoriClientiListNew.contains(furnizoriClientiListOldFurnizoriClienti)) {
                    furnizoriClientiListOldFurnizoriClienti.setMoneda(null);
                    furnizoriClientiListOldFurnizoriClienti = em.merge(furnizoriClientiListOldFurnizoriClienti);
                }
            }
            for (FurnizoriClienti furnizoriClientiListNewFurnizoriClienti : furnizoriClientiListNew) {
                if (!furnizoriClientiListOld.contains(furnizoriClientiListNewFurnizoriClienti)) {
                    Moneda oldMonedaOfFurnizoriClientiListNewFurnizoriClienti = furnizoriClientiListNewFurnizoriClienti.getMoneda();
                    furnizoriClientiListNewFurnizoriClienti.setMoneda(moneda);
                    furnizoriClientiListNewFurnizoriClienti = em.merge(furnizoriClientiListNewFurnizoriClienti);
                    if (oldMonedaOfFurnizoriClientiListNewFurnizoriClienti != null && !oldMonedaOfFurnizoriClientiListNewFurnizoriClienti.equals(moneda)) {
                        oldMonedaOfFurnizoriClientiListNewFurnizoriClienti.getFurnizoriClientiList().remove(furnizoriClientiListNewFurnizoriClienti);
                        oldMonedaOfFurnizoriClientiListNewFurnizoriClienti = em.merge(oldMonedaOfFurnizoriClientiListNewFurnizoriClienti);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = moneda.getIdMoneda();
                if (findMoneda(id) == null) {
                    throw new NonexistentEntityException("The moneda with id " + id + " no longer exists.");
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
            Moneda moneda;
            try {
                moneda = em.getReference(Moneda.class, id);
                moneda.getIdMoneda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moneda with id " + id + " no longer exists.", enfe);
            }
            List<FurnizoriClienti> furnizoriClientiList = moneda.getFurnizoriClientiList();
            for (FurnizoriClienti furnizoriClientiListFurnizoriClienti : furnizoriClientiList) {
                furnizoriClientiListFurnizoriClienti.setMoneda(null);
                furnizoriClientiListFurnizoriClienti = em.merge(furnizoriClientiListFurnizoriClienti);
            }
            em.remove(moneda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Moneda> findMonedaEntities() {
        return findMonedaEntities(true, -1, -1);
    }

    public List<Moneda> findMonedaEntities(int maxResults, int firstResult) {
        return findMonedaEntities(false, maxResults, firstResult);
    }

    private List<Moneda> findMonedaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Moneda.class));
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

    public Moneda findMoneda(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Moneda.class, id);
        } finally {
            em.close();
        }
    }

    public int getMonedaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Moneda> rt = cq.from(Moneda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
