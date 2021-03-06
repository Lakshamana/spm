package br.ufpa.labes.spm.repository.impl.taskagenda;

import java.util.Collection;
import java.util.Iterator;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.IProcessAgendaDAO;
import br.ufpa.labes.spm.repository.interfaces.taskagenda.ITaskDAO;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;

public class ProcessAgendaDAO extends BaseDAO<ProcessAgenda, Integer> implements IProcessAgendaDAO {

  ITaskDAO taskDAO;

  protected ProcessAgendaDAO(Class<ProcessAgenda> businessClass) {
    super(businessClass);
  }

  public ProcessAgendaDAO() {
    super(ProcessAgenda.class);
  }

  // TODO: HAVE TO FINISH THE INTEGRATION TO MESSAGE SERVICES

  public Task addTask(ProcessAgenda pAgenda, Normal actNorm) {
    //		pAgenda = this.retrieve(pAgenda.getId());

    Collection tasks = pAgenda.getTheTasks();
    Iterator iter = tasks.iterator();
    boolean has = false;
    Task returnTask = null;
    while (iter.hasNext()) {
      Task taskAux = (Task) iter.next();
      if (taskAux != null) {
        if (taskAux.getTheNormal().getIdent().equals(actNorm.getIdent())) {
          returnTask = taskAux;
          has = true;
          break;
        }
      }
    }

    if (!has) {
      Task task = new Task();
      task.setTheNormal(actNorm);
      actNorm.getTheTasks().add(task);
      task.setTheProcessAgenda(pAgenda);
      pAgenda.getTheTasks().add(task);

      task = (Task) taskDAO.daoSave(task);

      //
      // System.out.println("Error on save Task: ProcessAgenda.addTask("+
      // actNorm.getIdent()+")");
      // e1.printStackTrace();

      // Notify the agente of the task!!
      String message =
          "<MESSAGE>"
              + "<NOTIFY>"
              + "<OID>"
              + task.getId()
              + "</OID>"
              + "<TYPE>ADD</TYPE>"
              + "<CLASS>"
              + task.getClass().getName()
              + "</CLASS>"
              + "<BY>APSEE_Manager</BY>"
              + "</NOTIFY>"
              + "</MESSAGE>";

      /*
       * try { if(ProcessAgenda.remote==null){ reloadRemote(); }
       * if(ProcessAgenda.remote!=null){
       * ProcessAgenda.remote.sendMessageToUser(message, this
       * .getTheTaskAgenda().getTheAgent().getIdent()); }
       */

      returnTask = task;
    }
    return returnTask;
  }
}
