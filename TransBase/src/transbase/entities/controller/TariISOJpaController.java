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
import transbase.entities.ComandaTransp;
import transbase.entities.Consignees;
import transbase.entities.TariISO;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class TariISOJpaController implements Serializable {

    public TariISOJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TariISO tariISO) throws PreexistingEntityException, Exception {
        if (tariISO.getFurnizoriClientiList() == null) {
            tariISO.setFurnizoriClientiList(new ArrayList<FurnizoriClienti>());
        }
        if (tariISO.getComandaTranspList() == null) {
            tariISO.setComandaTranspList(new ArrayList<ComandaTransp>());
        }
        if (tariISO.getConsigneesList() == null) {
            tariISO.setConsigneesList(new ArrayList<Consignees>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FurnizoriClienti> attachedFurnizoriClientiList = new ArrayList<FurnizoriClienti>();
            for (FurnizoriClienti furnizoriClientiListFurnizoriClientiToAttach : tariISO.getFurnizoriClientiList()) {
                furnizoriClientiListFurnizoriClientiToAttach = em.getReference(furnizoriClientiListFurnizoriClientiToAttach.getClass(), furnizoriClientiListFurnizoriClientiToAttach.getCodScala());
                attachedFurnizoriClientiList.add(furnizoriClientiListFurnizoriClientiToAttach);
            }
            tariISO.setFurnizoriClientiList(attachedFurnizoriClientiList);
            List<ComandaTransp> attachedComandaTranspList = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListComandaTranspToAttach : tariISO.getComandaTranspList()) {
                comandaTranspListComandaTranspToAttach = em.getReference(comandaTranspListComandaTranspToAttach.getClass(), comandaTranspListComandaTranspToAttach.getCod());
                attachedComandaTranspList.add(comandaTranspListComandaTranspToAttach);
            }
            tariISO.setComandaTranspList(attachedComandaTranspList);
            List<Consignees> attachedConsigneesList = new ArrayList<Consignees>();
            for (Consignees consigneesListConsigneesToAttach : tariISO.getConsigneesList()) {
                consigneesListConsigneesToAttach = em.getReference(consigneesListConsigneesToAttach.getClass(), consigneesListConsigneesToAttach.getIdcons());
                attachedConsigneesList.add(consigneesListConsigneesToAttach);
            }
            tariISO.setConsigneesList(attachedConsigneesList);
            em.persist(tariISO);
            for (FurnizoriClienti furnizoriClientiListFurnizoriClienti : tariISO.getFurnizoriClientiList()) {
                TariISO oldTaraOfFurnizoriClientiListFurnizoriClienti = furnizoriClientiListFurnizoriClienti.getTara();
                furnizoriClientiListFurnizoriClienti.setTara(tariISO);
                furnizoriClientiListFurnizoriClienti = em.merge(furnizoriClientiListFurnizoriClienti);
                if (oldTaraOfFurnizoriClientiListFurnizoriClienti != null) {
                    oldTaraOfFurnizoriClientiListFurnizoriClienti.getFurnizoriClientiList().remove(furnizoriClientiListFurnizoriClienti);
                    oldTaraOfFurnizoriClientiListFurnizoriClienti = em.merge(oldTaraOfFurnizoriClientiListFurnizoriClienti);
                }
            }
            for (ComandaTransp comandaTranspListComandaTransp : tariISO.getComandaTranspList()) {
                TariISO oldTaraOfComandaTranspListComandaTransp = comandaTranspListComandaTransp.getTara();
                comandaTranspListComandaTransp.setTara(tariISO);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
                if (oldTaraOfComandaTranspListComandaTransp != null) {
                    oldTaraOfComandaTranspListComandaTransp.getComandaTranspList().remove(comandaTranspListComandaTransp);
                    oldTaraOfComandaTranspListComandaTransp = em.merge(oldTaraOfComandaTranspListComandaTransp);
                }
            }
            for (Consignees consigneesListConsignees : tariISO.getConsigneesList()) {
                TariISO oldCodISOtaraOfConsigneesListConsignees = consigneesListConsignees.getCodISOtara();
                consigneesListConsignees.setCodISOtara(tariISO);
                consigneesListConsignees = em.merge(consigneesListConsignees);
                if (oldCodISOtaraOfConsigneesListConsignees != null) {
                    oldCodISOtaraOfConsigneesListConsignees.getConsigneesList().remove(consigneesListConsignees);
                    oldCodISOtaraOfConsigneesListConsignees = em.merge(oldCodISOtaraOfConsigneesListConsignees);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTariISO(tariISO.getTara2L()) != null) {
                throw new PreexistingEntityException("TariISO " + tariISO + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TariISO tariISO) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TariISO persistentTariISO = em.find(TariISO.class, tariISO.getTara2L());
            List<FurnizoriClienti> furnizoriClientiListOld = persistentTariISO.getFurnizoriClientiList();
            List<FurnizoriClienti> furnizoriClientiListNew = tariISO.getFurnizoriClientiList();
            List<ComandaTransp> comandaTranspListOld = persistentTariISO.getComandaTranspList();
            List<ComandaTransp> comandaTranspListNew = tariISO.getComandaTranspList();
            List<Consignees> consigneesListOld = persistentTariISO.getConsigneesList();
            List<Consignees> consigneesListNew = tariISO.getConsigneesList();
            List<FurnizoriClienti> attachedFurnizoriClientiListNew = new ArrayList<FurnizoriClienti>();
            for (FurnizoriClienti furnizoriClientiListNewFurnizoriClientiToAttach : furnizoriClientiListNew) {
                furnizoriClientiListNewFurnizoriClientiToAttach = em.getReference(furnizoriClientiListNewFurnizoriClientiToAttach.getClass(), furnizoriClientiListNewFurnizoriClientiToAttach.getCodScala());
                attachedFurnizoriClientiListNew.add(furnizoriClientiListNewFurnizoriClientiToAttach);
            }
            furnizoriClientiListNew = attachedFurnizoriClientiListNew;
            tariISO.setFurnizoriClientiList(furnizoriClientiListNew);
            List<ComandaTransp> attachedComandaTranspListNew = new ArrayList<ComandaTransp>();
            for (ComandaTransp comandaTranspListNewComandaTranspToAttach : comandaTranspListNew) {
                comandaTranspListNewComandaTranspToAttach = em.getReference(comandaTranspListNewComandaTranspToAttach.getClass(), comandaTranspListNewComandaTranspToAttach.getCod());
                attachedComandaTranspListNew.add(comandaTranspListNewComandaTranspToAttach);
            }
            comandaTranspListNew = attachedComandaTranspListNew;
            tariISO.setComandaTranspList(comandaTranspListNew);
            List<Consignees> attachedConsigneesListNew = new ArrayList<Consignees>();
            for (Consignees consigneesListNewConsigneesToAttach : consigneesListNew) {
                consigneesListNewConsigneesToAttach = em.getReference(consigneesListNewConsigneesToAttach.getClass(), consigneesListNewConsigneesToAttach.getIdcons());
                attachedConsigneesListNew.add(consigneesListNewConsigneesToAttach);
            }
            consigneesListNew = attachedConsigneesListNew;
            tariISO.setConsigneesList(consigneesListNew);
            tariISO = em.merge(tariISO);
            for (FurnizoriClienti furnizoriClientiListOldFurnizoriClienti : furnizoriClientiListOld) {
                if (!furnizoriClientiListNew.contains(furnizoriClientiListOldFurnizoriClienti)) {
                    furnizoriClientiListOldFurnizoriClienti.setTara(null);
                    furnizoriClientiListOldFurnizoriClienti = em.merge(furnizoriClientiListOldFurnizoriClienti);
                }
            }
            for (FurnizoriClienti furnizoriClientiListNewFurnizoriClienti : furnizoriClientiListNew) {
                if (!furnizoriClientiListOld.contains(furnizoriClientiListNewFurnizoriClienti)) {
                    TariISO oldTaraOfFurnizoriClientiListNewFurnizoriClienti = furnizoriClientiListNewFurnizoriClienti.getTara();
                    furnizoriClientiListNewFurnizoriClienti.setTara(tariISO);
                    furnizoriClientiListNewFurnizoriClienti = em.merge(furnizoriClientiListNewFurnizoriClienti);
                    if (oldTaraOfFurnizoriClientiListNewFurnizoriClienti != null && !oldTaraOfFurnizoriClientiListNewFurnizoriClienti.equals(tariISO)) {
                        oldTaraOfFurnizoriClientiListNewFurnizoriClienti.getFurnizoriClientiList().remove(furnizoriClientiListNewFurnizoriClienti);
                        oldTaraOfFurnizoriClientiListNewFurnizoriClienti = em.merge(oldTaraOfFurnizoriClientiListNewFurnizoriClienti);
                    }
                }
            }
            for (ComandaTransp comandaTranspListOldComandaTransp : comandaTranspListOld) {
                if (!comandaTranspListNew.contains(comandaTranspListOldComandaTransp)) {
                    comandaTranspListOldComandaTransp.setTara(null);
                    comandaTranspListOldComandaTransp = em.merge(comandaTranspListOldComandaTransp);
                }
            }
            for (ComandaTransp comandaTranspListNewComandaTransp : comandaTranspListNew) {
                if (!comandaTranspListOld.contains(comandaTranspListNewComandaTransp)) {
                    TariISO oldTaraOfComandaTranspListNewComandaTransp = comandaTranspListNewComandaTransp.getTara();
                    comandaTranspListNewComandaTransp.setTara(tariISO);
                    comandaTranspListNewComandaTransp = em.merge(comandaTranspListNewComandaTransp);
                    if (oldTaraOfComandaTranspListNewComandaTransp != null && !oldTaraOfComandaTranspListNewComandaTransp.equals(tariISO)) {
                        oldTaraOfComandaTranspListNewComandaTransp.getComandaTranspList().remove(comandaTranspListNewComandaTransp);
                        oldTaraOfComandaTranspListNewComandaTransp = em.merge(oldTaraOfComandaTranspListNewComandaTransp);
                    }
                }
            }
            for (Consignees consigneesListOldConsignees : consigneesListOld) {
                if (!consigneesListNew.contains(consigneesListOldConsignees)) {
                    consigneesListOldConsignees.setCodISOtara(null);
                    consigneesListOldConsignees = em.merge(consigneesListOldConsignees);
                }
            }
            for (Consignees consigneesListNewConsignees : consigneesListNew) {
                if (!consigneesListOld.contains(consigneesListNewConsignees)) {
                    TariISO oldCodISOtaraOfConsigneesListNewConsignees = consigneesListNewConsignees.getCodISOtara();
                    consigneesListNewConsignees.setCodISOtara(tariISO);
                    consigneesListNewConsignees = em.merge(consigneesListNewConsignees);
                    if (oldCodISOtaraOfConsigneesListNewConsignees != null && !oldCodISOtaraOfConsigneesListNewConsignees.equals(tariISO)) {
                        oldCodISOtaraOfConsigneesListNewConsignees.getConsigneesList().remove(consigneesListNewConsignees);
                        oldCodISOtaraOfConsigneesListNewConsignees = em.merge(oldCodISOtaraOfConsigneesListNewConsignees);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tariISO.getTara2L();
                if (findTariISO(id) == null) {
                    throw new NonexistentEntityException("The tariISO with id " + id + " no longer exists.");
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
            TariISO tariISO;
            try {
                tariISO = em.getReference(TariISO.class, id);
                tariISO.getTara2L();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tariISO with id " + id + " no longer exists.", enfe);
            }
            List<FurnizoriClienti> furnizoriClientiList = tariISO.getFurnizoriClientiList();
            for (FurnizoriClienti furnizoriClientiListFurnizoriClienti : furnizoriClientiList) {
                furnizoriClientiListFurnizoriClienti.setTara(null);
                furnizoriClientiListFurnizoriClienti = em.merge(furnizoriClientiListFurnizoriClienti);
            }
            List<ComandaTransp> comandaTranspList = tariISO.getComandaTranspList();
            for (ComandaTransp comandaTranspListComandaTransp : comandaTranspList) {
                comandaTranspListComandaTransp.setTara(null);
                comandaTranspListComandaTransp = em.merge(comandaTranspListComandaTransp);
            }
            List<Consignees> consigneesList = tariISO.getConsigneesList();
            for (Consignees consigneesListConsignees : consigneesList) {
                consigneesListConsignees.setCodISOtara(null);
                consigneesListConsignees = em.merge(consigneesListConsignees);
            }
            em.remove(tariISO);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TariISO> findTariISOEntities() {
        return findTariISOEntities(true, -1, -1);
    }

    public List<TariISO> findTariISOEntities(int maxResults, int firstResult) {
        return findTariISOEntities(false, maxResults, firstResult);
    }

    private List<TariISO> findTariISOEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TariISO.class));
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

    public TariISO findTariISO(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TariISO.class, id);
        } finally {
            em.close();
        }
    }

    public int getTariISOCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TariISO> rt = cq.from(TariISO.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
