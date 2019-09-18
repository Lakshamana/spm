package br.ufpa.labes.spm.service.interfaces;

import java.lang.reflect.Array;
import java.util.List;


import org.qrconsult.spm.dtos.formProject.SimpleUpdateDTO;
import org.qrconsult.spm.dtos.template.TemplateDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.WebapseeException;

@Remote
public interface TemplateServices {

	public Boolean createTemplate(String ident);

	public String createNewVersion(String template_id, String why, String version_id);

	public void processDistilling(String process_id, String template_id);

	public List<TemplateDTO> getTemplates();

	public boolean copyTemplate(String newTemplateIdent,String oldTemplateIdent) throws DAOException;

	public boolean toBecomeDefined(String template_id)
			throws WebapseeException;

	public void processInstantiation(String template_id, String instance_id, String userIdent) throws WebapseeException;

	public Object[] getArtifactsIdentsFromProcessModelWithoutTemplates(String template_id);

	public void processComposition(String template_id, String currentLevel_id, Object[] artifactsIdentsFromUser) throws DAOException;
}
