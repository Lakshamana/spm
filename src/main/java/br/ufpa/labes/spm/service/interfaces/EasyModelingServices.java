package br.ufpa.labes.spm.service.interfaces;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.Remote;
import org.qrconsult.spm.dtos.calendar.CalendarDTO;
import br.ufpa.labes.spm.domain.ProcessModel;

@Remote
public interface EasyModelingServices {

	public String copyActivity(String act_id, String level_to_copy);

//	public void saveCoordinates(Hashtable<String, String> coordinates);

	public String messageToFlex();
	public Map<String, String> mapToFlex();
	public void getCoordinatesResponse(String processId, String[] idents, String[] xs, String[] ys, String[] types, String[] nodeTypes, String[] referredObjs);

//	public void flexMap(Map<String, String> obj);

    public void copyActivities(String[] act_ids, String[] cons_ids, String level_to_copy);


	/**
	 * @param oldProcessModel
	 * @param newProcessModel
	 * @param oldProcessIdent
	 * @param newProcessIdent
	 */
	public Hashtable<String, String> copyProcessModelData(ProcessModel oldProcessModel, ProcessModel newProcessModel, String level_to);//end method

	public void applyAllocationToProcess(String process_id, String role_id, String agent_id);

	public void applyAllocationToProcessModel(String pm_id, String role_id, String agent_id);

	void replanningDates(String act_id, long new_begin_date, long new_end_date,
			CalendarDTO replanningDates);

//	public void saveCoordinates(Map<String, String> coordinates);

	public void flexMap(String[] obj);

}
