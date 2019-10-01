package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.ActivityDTO;
import br.ufpa.labes.spm.service.dto.ActivitysDTO;

public interface GanttServices {

	public ActivitysDTO getGanttActivities(String processIdent);

	public boolean updateGanttTask(ActivityDTO activityDTO);

	public String alo();
}
