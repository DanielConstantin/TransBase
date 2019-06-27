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
import transbase.entities.Orasedest;
import transbase.entities.Inc2;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.Vami;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class VamiJpaController implements Serializable {

    public VamiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vami vami) throws PreexistingEntityException, Exception {
        if (vami.getInc2List() == null) {
            vami.setInc2List(new ArrayList<Inc2>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orasedest oras = vami.getOras();
            if (oras != null) {
                oras = em.getReference(oras.getClass(), oras.getCodOrdest());
                vami.setOras(oras);
            }
            List<Inc2> attachedInc2List = new ArrayList<Inc2>();
            for (Inc2 inc2ListInc2ToAttach : vami.getInc2List()) {
                inc2ListInc2ToAttach = em.getReference(inc2ListInc2ToAttach.getClass(), inc2ListInc2ToAttach.getId());
                attachedInc2List.add(inc2ListInc2ToAttach);
            }
            vami.setInc2List(attachedInc2List);
            em.persist(vami);
            if (oras != null) {
                oras.getVamiList().add(vami);
                oras = em.merge(oras);
            }
            for (Inc2 inc2ListInc2 : vami.getInc2List()) {
                Vami oldCodVamaOfInc2ListInc2 = inc2ListInc2.getCodVama();
                inc2ListInc2.setCodVama(vami);
                inc2ListInc2 = em.merge(inc2ListInc2);
                if (oldCodVamaOfInc2ListInc2 != null) {
                    oldCodVamaOfInc2ListInc2.getInc2List().remove(inc2ListInc2);
                    oldCodVamaOfInc2ListInc2 = em.merge(oldCodVamaOfInc2ListInc2);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVami(vami.getCodVama()) != null) {
                throw new PreexistingEntityException("Vami " + vami + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vami vami) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vami persistentVami = em.find(Vami.class, vami.getCodVama());
            Orasedest orasOld = persistentVami.getOras();
            Orasedest orasNew = vami.getOras();
            List<Inc2> inc2ListOld = persistentVami.getInc2List();
            List<Inc2> inc2ListNew = vami.getInc2List();
            if (orasNew != null) {
                orasNew = em.getReference(orasNew.getClass(), orasNew.getCodOrdest());
                vami.setOras(orasNew);
            }
            List<Inc2> attachedInc2ListNew = new ArrayList<Inc2>();
            for (Inc2 inc2ListNewInc2ToAttach : inc2ListNew) {
                inc2ListNewInc2ToAttach = em.getReference(inc2ListNewInc2ToAttach.getClass(), inc2ListNewInc2ToAttach.getId());
                attachedInc2ListNew.add(inc2ListNewInc2ToAttach);
            }
            inc2ListNew = attachedInc2ListNew;
            vami.setInc2List(inc2ListNew);
            vami = em.merge(vami);
            if (orasOld != null && !orasOld.equals(orasNew)) {
                orasOld.getVamiList().remove(vami);
                orasOld = em.merge(orasOld);
            }
            if (orasNew != null && !orasNew.equals(orasOld)) {
                orasNew.getVamiList().add(vami);
                orasNew = em.merge(orasNew);
            }
            for (Inc2 inc2ListOldInc2 : inc2ListOld) {
                if (!inc2ListNew.contains(inc2ListOldInc2)) {
                    inc2ListOldInc2.setCodVama(null);
                    inc2ListOldInc2 = em.merge(inc2ListOldInc2);
                }
            }
            for (Inc2 inc2ListNewInc2 : inc2ListNew) {
                if (!inc2ListOld.contains(inc2ListNewInc2)) {
                    Vami oldCodVamaOfInc2ListNewInc2 = inc2ListNewInc2.getCodVama();
                    inc2ListNewInc2.setCodVama(vami);
                    inc2ListNewInc2 = em.merge(inc2ListNewInc2);
                    if (oldCodVamaOfInc2ListNewInc2 != null && !oldCodVamaOfInc2ListNewInc2.equals(vami)) {
                        oldCodVamaOfInc2ListNewInc2.getInc2List().remove(inc2ListNewInc2);
                        oldCodVamaOfInc2ListNewInc2 = em.merge(oldCodVamaOfInc2ListNewInc2);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vami.getCodVama();
                if (findVami(id) == null) {
                    throw new NonexistentEntityException("The vami with id " + id + " no longer exists.");
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
            Vami vami;
            try {
                vami = em.getReference(Vami.class, id);
                vami.getCodVama();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vami with id " + id + " no longer exists.", enfe);
            }
            Orasedest oras = vami.getOras();
            if (oras != null) {
                oras.getVamiList().remove(vami);
                oras = em.merge(oras);
            }
            List<Inc2> inc2List = vami.getInc2List();
            for (Inc2 inc2ListInc2 : inc2List) {
                inc2ListInc2.setCodVama(null);
                inc2ListInc2 = em.merge(inc2ListInc2);
            }
            em.remove(vami);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vami> findVamiEntities() {
        return findVamiEntities(true, -1, -1);
    }

    public List<Vami> findVamiEntities(int maxResults, int firstResult) {
        return findVamiEntities(false, maxResults, firstResult);
    }

    private List<Vami> findVamiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vami.class));
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

    public Vami findVami(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vami.class, id);
        } finally {
            em.close();
        }
    }

    public int getVamiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vami> rt = cq.from(Vami.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
