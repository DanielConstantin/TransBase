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
import transbase.entities.CmdS;
import transbase.entities.Inc2;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class CmdSJpaController implements Serializable {

    public CmdSJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CmdS cmdS) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inc2 idcoms = cmdS.getIdcoms();
            if (idcoms != null) {
                idcoms = em.getReference(idcoms.getClass(), idcoms.getId());
                cmdS.setIdcoms(idcoms);
            }
            em.persist(cmdS);
            if (idcoms != null) {
                idcoms.getCmdSList().add(cmdS);
                idcoms = em.merge(idcoms);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCmdS(cmdS.getComanda()) != null) {
                throw new PreexistingEntityException("CmdS " + cmdS + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CmdS cmdS) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CmdS persistentCmdS = em.find(CmdS.class, cmdS.getComanda());
            Inc2 idcomsOld = persistentCmdS.getIdcoms();
            Inc2 idcomsNew = cmdS.getIdcoms();
            if (idcomsNew != null) {
                idcomsNew = em.getReference(idcomsNew.getClass(), idcomsNew.getId());
                cmdS.setIdcoms(idcomsNew);
            }
            cmdS = em.merge(cmdS);
            if (idcomsOld != null && !idcomsOld.equals(idcomsNew)) {
                idcomsOld.getCmdSList().remove(cmdS);
                idcomsOld = em.merge(idcomsOld);
            }
            if (idcomsNew != null && !idcomsNew.equals(idcomsOld)) {
                idcomsNew.getCmdSList().add(cmdS);
                idcomsNew = em.merge(idcomsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = cmdS.getComanda();
                if (findCmdS(id) == null) {
                    throw new NonexistentEntityException("The cmdS with id " + id + " no longer exists.");
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
            CmdS cmdS;
            try {
                cmdS = em.getReference(CmdS.class, id);
                cmdS.getComanda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cmdS with id " + id + " no longer exists.", enfe);
            }
            Inc2 idcoms = cmdS.getIdcoms();
            if (idcoms != null) {
                idcoms.getCmdSList().remove(cmdS);
                idcoms = em.merge(idcoms);
            }
            em.remove(cmdS);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CmdS> findCmdSEntities() {
        return findCmdSEntities(true, -1, -1);
    }

    public List<CmdS> findCmdSEntities(int maxResults, int firstResult) {
        return findCmdSEntities(false, maxResults, firstResult);
    }

    private List<CmdS> findCmdSEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CmdS.class));
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

    public CmdS findCmdS(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CmdS.class, id);
        } finally {
            em.close();
        }
    }

    public int getCmdSCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CmdS> rt = cq.from(CmdS.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
