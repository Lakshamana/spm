package br.ufpa.labes.spm.repository.impl.processModels;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.service.util.SimpleActivityQueryResult;

public class ProcessDAO extends BaseDAO<Process, String> implements IProcessDAO {

  protected ProcessDAO(Class<Process> businessClass) {
    super(businessClass);
  }

  public ProcessDAO() {
    super(Process.class);
  }

  @Override
  public SimpleActivityQueryResult[] getAllNormalActivitiesFromProcess(String processIdent) {
    String hql_activities =
        " from "
            + Normal.class.getName()
            + " as act where act.ident like '"
            + processIdent
            + ".%' ";

    Query query = getPersistenceContext().createQuery(hql_activities);
    @SuppressWarnings("unchecked")
    List<Normal> acts = query.getResultList();

    List<SimpleActivityQueryResult> result = new ArrayList<SimpleActivityQueryResult>();

    for (Normal act : acts) {
      SimpleActivityQueryResult current = new SimpleActivityQueryResult();
      current.setIdent(act.getIdent());
      current.setName(act.getName());

      result.add(current);
    }

    return result.toArray(new SimpleActivityQueryResult[result.size()]);
  }

  @Override
  public SimpleActivityQueryResult[] getAllActivitiesFromProcess(String processIdent) {
    try {

      String hql_activities =
          " from "
              + Activity.class.getName()
              + " as act where act.ident like '"
              + processIdent
              + ".%' ";

      Query query = getPersistenceContext().createQuery(hql_activities);
      Object[] objects = query.getResultList().toArray();
      Activity[] acts = new Activity[objects.length];

      SimpleActivityQueryResult[] result = new SimpleActivityQueryResult[objects.length];

      int sizeActs = 0;
      for (int i = 0; i < objects.length; i++) {
        acts[sizeActs] = (Activity) objects[i];
        SimpleActivityQueryResult current = new SimpleActivityQueryResult();
        if (acts[sizeActs] != null) {
          current.setIdent(acts[sizeActs].getIdent());
          current.setName(acts[sizeActs].getName());
          result[sizeActs] = current;
          sizeActs++;
        }
      }

      return result;

    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }
}
