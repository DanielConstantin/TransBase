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
import transbase.entities.Vami;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.Orasedest;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class OrasedestJpaController implements Serializable {

    public OrasedestJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orasedest orasedest) throws PreexistingEntityException, Exception {
        if (orasedest.getVamiList() == null) {
            orasedest.setVamiList(new ArrayList<Vami>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Vami> attachedVamiList = new ArrayList<Vami>();
            for (Vami vamiListVamiToAttach : orasedest.getVamiList()) {
                vamiListVamiToAttach = em.getReference(vamiListVamiToAttach.getClass(), vamiListVamiToAttach.getCodVama());
                attachedVamiList.add(vamiListVamiToAttach);
            }
            orasedest.setVamiList(attachedVamiList);
            em.persist(orasedest);
            for (Vami vamiListVami : orasedest.getVamiList()) {
                Orasedest oldOrasOfVamiListVami = vamiListVami.getOras();
                vamiListVami.setOras(orasedest);
                vamiListVami = em.merge(vamiListVami);
                if (oldOrasOfVamiListVami != null) {
                    oldOrasOfVamiListVami.getVamiList().remove(vamiListVami);
                    oldOrasOfVamiListVami = em.merge(oldOrasOfVamiListVami);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrasedest(orasedest.getCodOrdest()) != null) {
                throw new PreexistingEntityException("Orasedest " + orasedest + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orasedest orasedest) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orasedest persistentOrasedest = em.find(Orasedest.class, orasedest.getCodOrdest());
            List<Vami> vamiListOld = persistentOrasedest.getVamiList();
            List<Vami> vamiListNew = orasedest.getVamiList();
            List<Vami> attachedVamiListNew = new ArrayList<Vami>();
            for (Vami vamiListNewVamiToAttach : vamiListNew) {
                vamiListNewVamiToAttach = em.getReference(vamiListNewVamiToAttach.getClass(), vamiListNewVamiToAttach.getCodVama());
                attachedVamiListNew.add(vamiListNewVamiToAttach);
            }
            vamiListNew = attachedVamiListNew;
            orasedest.setVamiList(vamiListNew);
            orasedest = em.merge(orasedest);
            for (Vami vamiListOldVami : vamiListOld) {
                if (!vamiListNew.contains(vamiListOldVami)) {
                    vamiListOldVami.setOras(null);
                    vamiListOldVami = em.merge(vamiListOldVami);
                }
            }
            for (Vami vamiListNewVami : vamiListNew) {
                if (!vamiListOld.contains(vamiListNewVami)) {
                    Orasedest oldOrasOfVamiListNewVami = vamiListNewVami.getOras();
                    vamiListNewVami.setOras(orasedest);
                    vamiListNewVami = em.merge(vamiListNewVami);
                    if (oldOrasOfVamiListNewVami != null && !oldOrasOfVamiListNewVami.equals(orasedest)) {
                        oldOrasOfVamiListNewVami.getVamiList().remove(vamiListNewVami);
                        oldOrasOfVamiListNewVami = em.merge(oldOrasOfVamiListNewVami);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = orasedest.getCodOrdest();
                if (findOrasedest(id) == null) {
                    throw new NonexistentEntityException("The orasedest with id " + id + " no longer exists.");
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
            Orasedest orasedest;
            try {
                orasedest = em.getReference(Orasedest.class, id);
                orasedest.getCodOrdest();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orasedest with id " + id + " no longer exists.", enfe);
            }
            List<Vami> vamiList = orasedest.getVamiList();
            for (Vami vamiListVami : vamiList) {
                vamiListVami.setOras(null);
                vamiListVami = em.merge(vamiListVami);
            }
            em.remove(orasedest);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orasedest> findOrasedestEntities() {
        return findOrasedestEntities(true, -1, -1);
    }

    public List<Orasedest> findOrasedestEntities(int maxResults, int firstResult) {
        return findOrasedestEntities(false, maxResults, firstResult);
    }

    private List<Orasedest> findOrasedestEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orasedest.class));
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

    public Orasedest findOrasedest(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orasedest.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrasedestCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orasedest> rt = cq.from(Orasedest.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
