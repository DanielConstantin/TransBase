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
import transbase.entities.TariISO;
import transbase.entities.Loadingpl;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import transbase.entities.ComandaTransp;
import transbase.entities.controller.exceptions.IllegalOrphanException;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.entities.controller.exceptions.PreexistingEntityException;

/**
 *
 * @author daniel.constantin
 */
public class ComandaTranspJpaController implements Serializable {

    public ComandaTranspJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComandaTransp comandaTransp) throws PreexistingEntityException, Exception {
        if (comandaTransp.getLoadingplList() == null) {
            comandaTransp.setLoadingplList(new ArrayList<Loadingpl>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FurnizoriClienti codFurnizor = comandaTransp.getCodFurnizor();
            if (codFurnizor != null) {
                codFurnizor = em.getReference(codFurnizor.getClass(), codFurnizor.getCodScala());
                comandaTransp.setCodFurnizor(codFurnizor);
            }
            TariISO tara = comandaTransp.getTara();
            if (tara != null) {
                tara = em.getReference(tara.getClass(), tara.getTara2L());
                comandaTransp.setTara(tara);
            }
            List<Loadingpl> attachedLoadingplList = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListLoadingplToAttach : comandaTransp.getLoadingplList()) {
                loadingplListLoadingplToAttach = em.getReference(loadingplListLoadingplToAttach.getClass(), loadingplListLoadingplToAttach.getIdLp());
                attachedLoadingplList.add(loadingplListLoadingplToAttach);
            }
            comandaTransp.setLoadingplList(attachedLoadingplList);
            em.persist(comandaTransp);
            if (codFurnizor != null) {
                codFurnizor.getComandaTranspList().add(comandaTransp);
                codFurnizor = em.merge(codFurnizor);
            }
            if (tara != null) {
                tara.getComandaTranspList().add(comandaTransp);
                tara = em.merge(tara);
            }
            for (Loadingpl loadingplListLoadingpl : comandaTransp.getLoadingplList()) {
                ComandaTransp oldLodplOfLoadingplListLoadingpl = loadingplListLoadingpl.getLodpl();
                loadingplListLoadingpl.setLodpl(comandaTransp);
                loadingplListLoadingpl = em.merge(loadingplListLoadingpl);
                if (oldLodplOfLoadingplListLoadingpl != null) {
                    oldLodplOfLoadingplListLoadingpl.getLoadingplList().remove(loadingplListLoadingpl);
                    oldLodplOfLoadingplListLoadingpl = em.merge(oldLodplOfLoadingplListLoadingpl);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComandaTransp(comandaTransp.getCod()) != null) {
                throw new PreexistingEntityException("ComandaTransp " + comandaTransp + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComandaTransp comandaTransp) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaTransp persistentComandaTransp = em.find(ComandaTransp.class, comandaTransp.getCod());
            FurnizoriClienti codFurnizorOld = persistentComandaTransp.getCodFurnizor();
            FurnizoriClienti codFurnizorNew = comandaTransp.getCodFurnizor();
            TariISO taraOld = persistentComandaTransp.getTara();
            TariISO taraNew = comandaTransp.getTara();
            List<Loadingpl> loadingplListOld = persistentComandaTransp.getLoadingplList();
            List<Loadingpl> loadingplListNew = comandaTransp.getLoadingplList();
            List<String> illegalOrphanMessages = null;
            for (Loadingpl loadingplListOldLoadingpl : loadingplListOld) {
                if (!loadingplListNew.contains(loadingplListOldLoadingpl)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Loadingpl " + loadingplListOldLoadingpl + " since its lodpl field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codFurnizorNew != null) {
                codFurnizorNew = em.getReference(codFurnizorNew.getClass(), codFurnizorNew.getCodScala());
                comandaTransp.setCodFurnizor(codFurnizorNew);
            }
            if (taraNew != null) {
                taraNew = em.getReference(taraNew.getClass(), taraNew.getTara2L());
                comandaTransp.setTara(taraNew);
            }
            List<Loadingpl> attachedLoadingplListNew = new ArrayList<Loadingpl>();
            for (Loadingpl loadingplListNewLoadingplToAttach : loadingplListNew) {
                loadingplListNewLoadingplToAttach = em.getReference(loadingplListNewLoadingplToAttach.getClass(), loadingplListNewLoadingplToAttach.getIdLp());
                attachedLoadingplListNew.add(loadingplListNewLoadingplToAttach);
            }
            loadingplListNew = attachedLoadingplListNew;
            comandaTransp.setLoadingplList(loadingplListNew);
            comandaTransp = em.merge(comandaTransp);
            if (codFurnizorOld != null && !codFurnizorOld.equals(codFurnizorNew)) {
                codFurnizorOld.getComandaTranspList().remove(comandaTransp);
                codFurnizorOld = em.merge(codFurnizorOld);
            }
            if (codFurnizorNew != null && !codFurnizorNew.equals(codFurnizorOld)) {
                codFurnizorNew.getComandaTranspList().add(comandaTransp);
                codFurnizorNew = em.merge(codFurnizorNew);
            }
            if (taraOld != null && !taraOld.equals(taraNew)) {
                taraOld.getComandaTranspList().remove(comandaTransp);
                taraOld = em.merge(taraOld);
            }
            if (taraNew != null && !taraNew.equals(taraOld)) {
                taraNew.getComandaTranspList().add(comandaTransp);
                taraNew = em.merge(taraNew);
            }
            for (Loadingpl loadingplListNewLoadingpl : loadingplListNew) {
                if (!loadingplListOld.contains(loadingplListNewLoadingpl)) {
                    ComandaTransp oldLodplOfLoadingplListNewLoadingpl = loadingplListNewLoadingpl.getLodpl();
                    loadingplListNewLoadingpl.setLodpl(comandaTransp);
                    loadingplListNewLoadingpl = em.merge(loadingplListNewLoadingpl);
                    if (oldLodplOfLoadingplListNewLoadingpl != null && !oldLodplOfLoadingplListNewLoadingpl.equals(comandaTransp)) {
                        oldLodplOfLoadingplListNewLoadingpl.getLoadingplList().remove(loadingplListNewLoadingpl);
                        oldLodplOfLoadingplListNewLoadingpl = em.merge(oldLodplOfLoadingplListNewLoadingpl);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = comandaTransp.getCod();
                if (findComandaTransp(id) == null) {
                    throw new NonexistentEntityException("The comandaTransp with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ComandaTransp comandaTransp;
            try {
                comandaTransp = em.getReference(ComandaTransp.class, id);
                comandaTransp.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comandaTransp with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Loadingpl> loadingplListOrphanCheck = comandaTransp.getLoadingplList();
            for (Loadingpl loadingplListOrphanCheckLoadingpl : loadingplListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ComandaTransp (" + comandaTransp + ") cannot be destroyed since the Loadingpl " + loadingplListOrphanCheckLoadingpl + " in its loadingplList field has a non-nullable lodpl field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            FurnizoriClienti codFurnizor = comandaTransp.getCodFurnizor();
            if (codFurnizor != null) {
                codFurnizor.getComandaTranspList().remove(comandaTransp);
                codFurnizor = em.merge(codFurnizor);
            }
            TariISO tara = comandaTransp.getTara();
            if (tara != null) {
                tara.getComandaTranspList().remove(comandaTransp);
                tara = em.merge(tara);
            }
            em.remove(comandaTransp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComandaTransp> findComandaTranspEntities() {
        return findComandaTranspEntities(true, -1, -1);
    }

    public List<ComandaTransp> findComandaTranspEntities(int maxResults, int firstResult) {
        return findComandaTranspEntities(false, maxResults, firstResult);
    }

    private List<ComandaTransp> findComandaTranspEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComandaTransp.class));
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

    public ComandaTransp findComandaTransp(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComandaTransp.class, id);
        } finally {
            em.close();
        }
    }

    public int getComandaTranspCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComandaTransp> rt = cq.from(ComandaTransp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
