package br.ufpa.labes.spm.service.interfaces;


import org.qrconsult.spm.dtos.formActivity.ActivityDTO;
import org.qrconsult.spm.dtos.formActivity.ActivitysDTO;

@Remote
public interface GanttServices {

	public ActivitysDTO getGanttActivities(String processIdent);

	public boolean updateGanttTask(ActivityDTO activityDTO);

	public String alo();
}
