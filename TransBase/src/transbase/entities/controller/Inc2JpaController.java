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
import transbase.entities.Consignees;
import transbase.entities.Contacte;
import transbase.entities.TipTransport;
import transbase.entities.Vami;
import transbase.entities.Loadingpl;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.CmdS;
import transbase.entities.Inc2;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class Inc2JpaController implements Serializable {

    public Inc2JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inc2 inc2) throws PreexistingEntityException, Exception {
        if (inc2.getLoadingplList() == null) {
            inc2.setLoadingplList(new ArrayList<Loadingpl>());
        }
        if (inc2.getCmdSList() == null) {
            inc2.setCmdSList(new ArrayList<CmdS>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consignees consignee = inc2.getConsignee();
            if (consignee != null) {
                consignee = em.getReference(consignee.getClass(), consignee.getIdcons());
                inc2.setConsignee(consignee);
            }
            Contacte contacte = inc2.getContacte();
            if (contacte != null) {
                contacte = em.getReference(contacte.getClass(), contacte.getIdPersoanaCont());
                inc2.setContacte(contacte);
            }
            TipTransport tipTransp = inc2.getTipTransp();
            if (tipTransp != null) {
                tipTransp = em.getReference(tipTransp.getClass(), tipTransp.getTiptransport());
                inc2.setTipTransp(tipTransp);
            }
            Vami codVama = inc2.getCodVama();
            if (codVama != null) {
                codVama = em.getReference(codVama.getClass(), codVama.getCodVama());
                inc2.setCodVama(codVama);
            }
            List<Loadingpl> attachedLoadingplList = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListLoadingplToAttach : inc2.getLoadingplList()) {
                loadingplListLoadingplToAttach = em.getReference(loadingplListLoadingplToAttach.getClass(), loadingplListLoadingplToAttach.getIdLp());
                attachedLoadingplList.add(loadingplListLoadingplToAttach);
            }
            inc2.setLoadingplList(attachedLoadingplList);
            List<CmdS> attachedCmdSList = new ArrayList<CmdS>();
            for (CmdS cmdSListCmdSToAttach : inc2.getCmdSList()) {
                cmdSListCmdSToAttach = em.getReference(cmdSListCmdSToAttach.getClass(), cmdSListCmdSToAttach.getComanda());
                attachedCmdSList.add(cmdSListCmdSToAttach);
            }
            inc2.setCmdSList(attachedCmdSList);
            em.persist(inc2);
            if (consignee != null) {
                consignee.getInc2List().add(inc2);
                consignee = em.merge(consignee);
            }
            if (contacte != null) {
                contacte.getInc2List().add(inc2);
                contacte = em.merge(contacte);
            }
            if (tipTransp != null) {
                tipTransp.getInc2List().add(inc2);
                tipTransp = em.merge(tipTransp);
            }
            if (codVama != null) {
                codVama.getInc2List().add(inc2);
                codVama = em.merge(codVama);
            }
            for (Loadingpl loadingplListLoadingpl : inc2.getLoadingplList()) {
                Inc2 oldCodOfLoadingplListLoadingpl = loadingplListLoadingpl.getCod();
                loadingplListLoadingpl.setCod(inc2);
                loadingplListLoadingpl = em.merge(loadingplListLoadingpl);
                if (oldCodOfLoadingplListLoadingpl != null) {
                    oldCodOfLoadingplListLoadingpl.getLoadingplList().remove(loadingplListLoadingpl);
                    oldCodOfLoadingplListLoadingpl = em.merge(oldCodOfLoadingplListLoadingpl);
                }
            }
            for (CmdS cmdSListCmdS : inc2.getCmdSList()) {
                Inc2 oldIdcomsOfCmdSListCmdS = cmdSListCmdS.getIdcoms();
                cmdSListCmdS.setIdcoms(inc2);
                cmdSListCmdS = em.merge(cmdSListCmdS);
                if (oldIdcomsOfCmdSListCmdS != null) {
                    oldIdcomsOfCmdSListCmdS.getCmdSList().remove(cmdSListCmdS);
                    oldIdcomsOfCmdSListCmdS = em.merge(oldIdcomsOfCmdSListCmdS);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findInc2(inc2.getId()) != null) {
                throw new PreexistingEntityException("Inc2 " + inc2 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Inc2 inc2) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Inc2 persistentInc2 = em.find(Inc2.class, inc2.getId());
            Consignees consigneeOld = persistentInc2.getConsignee();
            Consignees consigneeNew = inc2.getConsignee();
            Contacte contacteOld = persistentInc2.getContacte();
            Contacte contacteNew = inc2.getContacte();
            TipTransport tipTranspOld = persistentInc2.getTipTransp();
            TipTransport tipTranspNew = inc2.getTipTransp();
            Vami codVamaOld = persistentInc2.getCodVama();
            Vami codVamaNew = inc2.getCodVama();
            List<Loadingpl> loadingplListOld = persistentInc2.getLoadingplList();
            List<Loadingpl> loadingplListNew = inc2.getLoadingplList();
            List<CmdS> cmdSListOld = persistentInc2.getCmdSList();
            List<CmdS> cmdSListNew = inc2.getCmdSList();
            if (consigneeNew != null) {
                consigneeNew = em.getReference(consigneeNew.getClass(), consigneeNew.getIdcons());
                inc2.setConsignee(consigneeNew);
            }
            if (contacteNew != null) {
                contacteNew = em.getReference(contacteNew.getClass(), contacteNew.getIdPersoanaCont());
                inc2.setContacte(contacteNew);
            }
            if (tipTranspNew != null) {
                tipTranspNew = em.getReference(tipTranspNew.getClass(), tipTranspNew.getTiptransport());
                inc2.setTipTransp(tipTranspNew);
            }
            if (codVamaNew != null) {
                codVamaNew = em.getReference(codVamaNew.getClass(), codVamaNew.getCodVama());
                inc2.setCodVama(codVamaNew);
            }
            List<Loadingpl> attachedLoadingplListNew = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListNewLoadingplToAttach : loadingplListNew) {
                loadingplListNewLoadingplToAttach = em.getReference(loadingplListNewLoadingplToAttach.getClass(), loadingplListNewLoadingplToAttach.getIdLp());
                attachedLoadingplListNew.add(loadingplListNewLoadingplToAttach);
            }
            loadingplListNew = attachedLoadingplListNew;
            inc2.setLoadingplList(loadingplListNew);
            List<CmdS> attachedCmdSListNew = new ArrayList<CmdS>();
            for (CmdS cmdSListNewCmdSToAttach : cmdSListNew) {
                cmdSListNewCmdSToAttach = em.getReference(cmdSListNewCmdSToAttach.getClass(), cmdSListNewCmdSToAttach.getComanda());
                attachedCmdSListNew.add(cmdSListNewCmdSToAttach);
            }
            cmdSListNew = attachedCmdSListNew;
            inc2.setCmdSList(cmdSListNew);
            inc2 = em.merge(inc2);
            if (consigneeOld != null && !consigneeOld.equals(consigneeNew)) {
                consigneeOld.getInc2List().remove(inc2);
                consigneeOld = em.merge(consigneeOld);
            }
            if (consigneeNew != null && !consigneeNew.equals(consigneeOld)) {
                consigneeNew.getInc2List().add(inc2);
                consigneeNew = em.merge(consigneeNew);
            }
            if (contacteOld != null && !contacteOld.equals(contacteNew)) {
                contacteOld.getInc2List().remove(inc2);
                contacteOld = em.merge(contacteOld);
            }
            if (contacteNew != null && !contacteNew.equals(contacteOld)) {
                contacteNew.getInc2List().add(inc2);
                contacteNew = em.merge(contacteNew);
            }
            if (tipTranspOld != null && !tipTranspOld.equals(tipTranspNew)) {
                tipTranspOld.getInc2List().remove(inc2);
                tipTranspOld = em.merge(tipTranspOld);
            }
            if (tipTranspNew != null && !tipTranspNew.equals(tipTranspOld)) {
                tipTranspNew.getInc2List().add(inc2);
                tipTranspNew = em.merge(tipTranspNew);
            }
            if (codVamaOld != null && !codVamaOld.equals(codVamaNew)) {
                codVamaOld.getInc2List().remove(inc2);
                codVamaOld = em.merge(codVamaOld);
            }
            if (codVamaNew != null && !codVamaNew.equals(codVamaOld)) {
                codVamaNew.getInc2List().add(inc2);
                codVamaNew = em.merge(codVamaNew);
            }
            for (Loadingpl loadingplListOldLoadingpl : loadingplListOld) {
                if (!loadingplListNew.contains(loadingplListOldLoadingpl)) {
                    loadingplListOldLoadingpl.setCod(null);
                    loadingplListOldLoadingpl = em.merge(loadingplListOldLoadingpl);
                }
            }
            for (Loadingpl loadingplListNewLoadingpl : loadingplListNew) {
                if (!loadingplListOld.contains(loadingplListNewLoadingpl)) {
                    Inc2 oldCodOfLoadingplListNewLoadingpl = loadingplListNewLoadingpl.getCod();
                    loadingplListNewLoadingpl.setCod(inc2);
                    loadingplListNewLoadingpl = em.merge(loadingplListNewLoadingpl);
                    if (oldCodOfLoadingplListNewLoadingpl != null && !oldCodOfLoadingplListNewLoadingpl.equals(inc2)) {
                        oldCodOfLoadingplListNewLoadingpl.getLoadingplList().remove(loadingplListNewLoadingpl);
                        oldCodOfLoadingplListNewLoadingpl = em.merge(oldCodOfLoadingplListNewLoadingpl);
                    }
                }
            }
            for (CmdS cmdSListOldCmdS : cmdSListOld) {
                if (!cmdSListNew.contains(cmdSListOldCmdS)) {
                    cmdSListOldCmdS.setIdcoms(null);
                    cmdSListOldCmdS = em.merge(cmdSListOldCmdS);
                }
            }
            for (CmdS cmdSListNewCmdS : cmdSListNew) {
                if (!cmdSListOld.contains(cmdSListNewCmdS)) {
                    Inc2 oldIdcomsOfCmdSListNewCmdS = cmdSListNewCmdS.getIdcoms();
                    cmdSListNewCmdS.setIdcoms(inc2);
                    cmdSListNewCmdS = em.merge(cmdSListNewCmdS);
                    if (oldIdcomsOfCmdSListNewCmdS != null && !oldIdcomsOfCmdSListNewCmdS.equals(inc2)) {
                        oldIdcomsOfCmdSListNewCmdS.getCmdSList().remove(cmdSListNewCmdS);
                        oldIdcomsOfCmdSListNewCmdS = em.merge(oldIdcomsOfCmdSListNewCmdS);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = inc2.getId();
                if (findInc2(id) == null) {
                    throw new NonexistentEntityException("The inc2 with id " + id + " no longer exists.");
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
            Inc2 inc2;
            try {
                inc2 = em.getReference(Inc2.class, id);
                inc2.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inc2 with id " + id + " no longer exists.", enfe);
            }
            Consignees consignee = inc2.getConsignee();
            if (consignee != null) {
                consignee.getInc2List().remove(inc2);
                consignee = em.merge(consignee);
            }
            Contacte contacte = inc2.getContacte();
            if (contacte != null) {
                contacte.getInc2List().remove(inc2);
                contacte = em.merge(contacte);
            }
            TipTransport tipTransp = inc2.getTipTransp();
            if (tipTransp != null) {
                tipTransp.getInc2List().remove(inc2);
                tipTransp = em.merge(tipTransp);
            }
            Vami codVama = inc2.getCodVama();
            if (codVama != null) {
                codVama.getInc2List().remove(inc2);
                codVama = em.merge(codVama);
            }
            List<Loadingpl> loadingplList = inc2.getLoadingplList();
            for (Loadingpl loadingplListLoadingpl : loadingplList) {
                loadingplListLoadingpl.setCod(null);
                loadingplListLoadingpl = em.merge(loadingplListLoadingpl);
            }
            List<CmdS> cmdSList = inc2.getCmdSList();
            for (CmdS cmdSListCmdS : cmdSList) {
                cmdSListCmdS.setIdcoms(null);
                cmdSListCmdS = em.merge(cmdSListCmdS);
            }
            em.remove(inc2);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Inc2> findInc2Entities() {
        return findInc2Entities(true, -1, -1);
    }

    public List<Inc2> findInc2Entities(int maxResults, int firstResult) {
        return findInc2Entities(false, maxResults, firstResult);
    }

    private List<Inc2> findInc2Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inc2.class));
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

    public Inc2 findInc2(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inc2.class, id);
        } finally {
            em.close();
        }
    }

    public int getInc2Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inc2> rt = cq.from(Inc2.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
