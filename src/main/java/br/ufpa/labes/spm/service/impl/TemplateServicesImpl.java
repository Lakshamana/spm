package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.interfaces.activities.IActivityDAO;
import br.ufpa.labes.spm.repository.interfaces.activities.IDecomposedDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IConnectionDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IInvolvedArtifactsDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredResourceDAO;
import br.ufpa.labes.spm.repository.interfaces.processModelGraphic.IGraphicCoordinateDAO;
import br.ufpa.labes.spm.repository.interfaces.processModelGraphic.IWebAPSEEObjectDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IDescriptionDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessModelDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.ITemplateDAO;
import br.ufpa.labes.spm.service.dto.SimpleUpdateDTO;
import br.ufpa.labes.spm.service.dto.ProcessModelDTO;
import br.ufpa.labes.spm.service.dto.TemplateDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.DataBaseException;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.Branch;
import br.ufpa.labes.spm.domain.BranchAND;
import br.ufpa.labes.spm.domain.BranchCond;
import br.ufpa.labes.spm.domain.BranchCondToActivity;
import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.domain.Feedback;
import br.ufpa.labes.spm.domain.Join;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.domain.InvolvedArtifacts;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.Parameters;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import br.ufpa.labes.spm.domain.ReqGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.domain.WebAPSEEObject;
import br.ufpa.labes.spm.domain.Description;
import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Template;
import br.ufpa.labes.spm.domain.Reservation;
import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.domain.ToolParameters;
import org.qrconsult.spm.process.impl.CopyProcessServiceImpl;
import org.qrconsult.spm.process.interfaces.CopyProcessServices;
import br.ufpa.labes.spm.service.interfaces.DynamicModeling;
import br.ufpa.labes.spm.service.interfaces.TemplateServices;
import org.qrconsult.spm.util.i18n.Messages;

public class TemplateServicesImpl implements TemplateServices {
	public static final String TEMPLATE_CLASSNAME = Template.class.getSimpleName();
	public static final transient String
	PROC_INST	= "Process_Instantiation",
	PROC_DEST	= "Process_Destilling",
	PROC_COMP	= "Process_Composition",
	COPY_PROC	= "Copy_Process",
	COPY_TEMP	= "Copy_Template",
	NEW_VERS_TEMP	= "New_Version_Template",
	DEC_DEST	= "Decomposed_Destilling";

	ITemplateDAO templateDAO;
	CopyProcessServices copyServices;
	IDescriptionDAO descriptionDAO;
	IProcessDAO processDAO;
	IDecomposedDAO decomposedDAO;
	IProjectDAO projectDAO;
	IAgentDAO agentDAO;
	IProcessModelDAO processModelDAO;
	IArtifactDAO artifactDAO;
	IActivityDAO activityDAO;
	DynamicModeling dynamicModeling;
	IWebAPSEEObjectDAO webAPSEEObjectDAO;
	IGraphicCoordinateDAO graphicCoordinateDAO;
	IConnectionDAO connectionDAO;
	IReqAgentDAO reqAgentDAO;
	IReqGroupDAO reqGroupDAO;
	IRequiredResourceDAO reqResourceDAO;
	IInvolvedArtifactsDAO involvedArtifactsDAO;

	private Query query;
	private Properties artifactsIdents;
	private Hashtable<String, Artifact> newArtifacts;

	@Override
	public Boolean createTemplate(String ident) {
		Template template = new Template();
		template.setIdent(ident);
		ProcessModel processModel = new ProcessModel();
		template.insertIntoTheInstances(processModel);
		templateDAO.save(template);

		return true;
	}

	@Override
	public List<TemplateDTO> getTemplates() {
		String hql = "select o from " + TEMPLATE_CLASSNAME + " o";
		this.query = templateDAO.getPersistenceContext().createQuery(hql);

		List<Template> result = query.getResultList();
		List<TemplateDTO> templatesDTO = this.convertTemplatesToTemplatesDTO(result);
		System.out.println("lista dos templa:"+templatesDTO);
		return templatesDTO;
	}

	@Override
	public String createNewVersion(String template_id, String why, String version_id) {
		Template template = null;
		String state = "";
		try {
			template = getTemplate(template_id);
			state = template.getTemplateState();
		} catch (DAOException e) {
			e.printStackTrace();
		}

		if(state.equals(Template.DEFINED)){
			Object[] artifactsIdentsFromProcess = artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(template_id);
			Properties artifactsIdents = null;

			if( artifactsIdentsFromProcess != null){
				artifactsIdents = new Properties();
				String token = "";

				for (int i = 0; i < artifactsIdentsFromProcess.length; i++) {
					String oldArtifactIdent = (String) artifactsIdentsFromProcess[i];
					artifactsIdents.put(oldArtifactIdent, oldArtifactIdent);
				}
			}

			Template newTemplate = new Template();
			// Make relationship with Description
			// Modify newTemplate
			newTemplate.setIdent(version_id);
			//Copy relationships in common with Old Template
			Hashtable<String, String> coordinates = copyServices.copyProcessModelData(template.getTheProcessModel(),newTemplate.getTheProcessModel(),template_id,newTemplate.getIdent(), CopyProcessServiceImpl.NEW_VERS_TEMP, artifactsIdents);
			newTemplate = (Template)templateDAO.save(newTemplate);

			//Make relationships of Template versioning
			Description desc = new Description();
			desc.setTheNewVersion(newTemplate);
			desc.setTheOldVersion(template);
			desc.setWhy(why);
			desc.setDate(new Date());

			desc = (Description)descriptionDAO.save(desc);

			template.insertIntoTheDerivedVersionDescriptions(desc);
			newTemplate.setTheOriginalVersionDescription(desc);
			template.setTemplateState(Template.PENDING);

			templateDAO.save(newTemplate);
			templateDAO.save(template);

			copyServices.saveCoordinates(coordinates, version_id);
			return newTemplate.getIdent();
		}else {
//			System.out.println(Messages.getString("srcReuse.TempManag.TemplatesServices.templExecAtemplInThe")+" "+state+" "+Messages.getString("srcReuse.TempManag.TemplatesServices.templExecCannBe")); //$NON-NLS-1$ //$NON-NLS-2$
			return "";
		}
	}


	public Object[] getArtifactsIdentsFromProcessModelWithoutTemplates(String template_id){


		return  artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(template_id);
	}
	@Override
	public void processDistilling(String process_id, String template_id) {
		String tar=process_id;
		String s[] = tar.split("\\.");
		 process_id = s[0];
		System.out.println("estado :"+s[0]);
		Process process = (Process)processDAO.retrieveBySecondaryKey(process_id);

		String state = process.getPState();
		System.out.println("estado :"+state);

		if(state.equalsIgnoreCase(Process.FINISHED) || state.equalsIgnoreCase(Process.ENACTING)){
			Object[] artifactsIdentsFromProcess = artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(process_id);
			Properties artifactsIdents = null;

			if( artifactsIdentsFromProcess != null){
				artifactsIdents = new Properties();
				String token = "";
				for (int i = 0; i < artifactsIdentsFromProcess.length; i++) {
					String oldArtifactIdent = (String) artifactsIdentsFromProcess[i];
					String newArtifactIdent = oldArtifactIdent;

					if(oldArtifactIdent.contains("-")){
						token = oldArtifactIdent.substring(0 , oldArtifactIdent.indexOf("-"));
						if(!token.equals("") && token.trim().contains(process_id)){
							newArtifactIdent =  oldArtifactIdent.substring(oldArtifactIdent.indexOf("-")+1).trim();
						}
					}
					artifactsIdents.put(oldArtifactIdent, newArtifactIdent);
				}
			}

			Template template = new Template();
			template.setIdent(template_id);

			//Copy relationships in common with Process
			Hashtable<String, String> coordinates = copyServices.copyProcessModelData(process.getTheProcessModel(),template.getTheProcessModel(),process_id,template.getIdent(), CopyProcessServiceImpl.PROC_DEST, artifactsIdents);
			template = (Template)templateDAO.save(template);

			templateDAO.save(template);
			copyServices.saveCoordinates(coordinates, template_id);

		}else if(state.equalsIgnoreCase(Template.DRAFT) || state.equalsIgnoreCase(Template.OUTDATED)|| state.equalsIgnoreCase(Template.DEFINED)||state.equalsIgnoreCase(Template.PENDING)||state.equalsIgnoreCase(Template.NOT_STARTED)){

			System.out.println("atividade n�o iniciada");
			System.out.println(Messages.getString("srcReuse.TempManag.TemplatesServices.templExecAProce")+" "+state+" "+Messages.getString("srcReuse.TempManag.TemplatesServices.templExecCannBeDist"));

		}

		else System.out.println(Messages.getString("srcReuse.TempManag.TemplatesServices.templExecAProce")+" "+state+" "+Messages.getString("srcReuse.TempManag.TemplatesServices.templExecCannBeDist")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public boolean copyTemplate(String newTemplateIdent,String oldTemplateIdent) throws DAOException {

		Object[] artifactsIdentsFromUser = artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(oldTemplateIdent);

		if( artifactsIdentsFromUser != null){

			this.artifactsIdents = new Properties();

			for (int i = 0; i < artifactsIdentsFromUser.length; i++) {

				String artifactIdent = (String) artifactsIdentsFromUser[i];

				this.artifactsIdents.put(artifactIdent, artifactIdent);
			}
		}

		Template sourceTemplate=null;
		boolean abort=false;

		Object newProcess= templateDAO.retrieveBySecondaryKey(newTemplateIdent);
		if(newProcess!=null){
			abort=true;
		}

		if(abort){
			throw new DAOException("Template "+newTemplateIdent+" already exists! Please choose a new ident!");
		}
		sourceTemplate=(Template) templateDAO.retrieveBySecondaryKey(oldTemplateIdent);
		if(sourceTemplate==null){
			throw new DAOException("Template "+oldTemplateIdent+" not found in database!Cannot copy from a null Process Model");
		}
		//if the code is here .... we need to instantiate a new Process to receive the old data
		Template destinationTemplate = new Template();
		destinationTemplate.setIdent(newTemplateIdent);

		ProcessModel rootPM = sourceTemplate.getTheProcessModel();

		if(rootPM==null){
			throw new DAOException("Template "+oldTemplateIdent+" contains a null Process Model! Cannot copy from a null Process Model");
		}
		Hashtable<String, String> coordinates;
		// now invoke a recursive method to copy the process model data
		coordinates = copyProcessModelData(rootPM,destinationTemplate.getTheProcessModel(),oldTemplateIdent,newTemplateIdent, COPY_TEMP);
		templateDAO.save(destinationTemplate);

		this.saveCoordinates(coordinates, newTemplateIdent);
		return true;

	}//end method

	/**
	 * @param oldProcessModel
	 * @param newProcessModel
	 * @param oldProcessIdent
	 * @param newProcessIdent
	 */
	public Hashtable<String, String> copyProcessModelData(ProcessModel oldProcessModel,
			ProcessModel newProcessModel, String oldProcessIdent,
			String newProcessIdent, String operationType) {
		try{
			Hashtable<String, String> coordinates = new Hashtable<String, String>();

			Collection<Activity> activities = oldProcessModel.getTheActivity();

			Collection<Connection> connections = oldProcessModel.getTheConnection();

			Collection<Activity> newActivities = null;//############################
														// ########
			newActivities = copyActivitiesOnProcessModel(activities,
					oldProcessIdent, newProcessIdent, operationType,
					newProcessIdent, coordinates);

			newProcessModel.insertIntoTheActivity(newActivities);

			Hashtable<String, Activity> activitiesTable = generateActivitiesTable(newActivities);

			Collection<Connection> newConnections = null;//#########################
															// #########
			newConnections = copyConnectionsOnProcessModel(connections,
					oldProcessIdent, newProcessIdent, activitiesTable,
					operationType, coordinates);
			newProcessModel.insertIntoTheConnection(newConnections);

			return coordinates;
		}catch(WebapseeException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param activities
	 * @param oldProcessIdent
	 * @param newProcessIdent
	 * @return
	 * @throws DAOException
	 */
	private Collection<Activity> copyActivitiesOnProcessModel(Collection<Activity> activities, String oldProcessIdent, String newProcessIdent, String operationType, String current_level, Hashtable<String, String> coordinates) throws DAOException {
		Collection<Activity> newActivities = new HashSet<Activity>();

		for(Iterator<Activity> activityIterator = activities.iterator(); activityIterator.hasNext();){
			Activity currentActivity = activityIterator.next();
			Activity newActivity =null;
			if(currentActivity!=null){

				String newActivityIdent = currentActivity.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);

				this.addCoordinate(currentActivity.getOid(), currentActivity.getClass().getSimpleName(), WebAPSEEObject.ACTIVITY + "+" + newActivityIdent, coordinates);

				if(currentActivity instanceof Normal){
					newActivity = new Normal();
					newActivity.setIdent(newActivityIdent);
					copyNormalProperties(((Normal)newActivity),((Normal)currentActivity), operationType, current_level, coordinates);
				}else if(currentActivity instanceof Automatic){
					newActivity = new Automatic();
					newActivity.setIdent(newActivityIdent);
					copyAutomaticProperties(((Automatic)newActivity),((Automatic)currentActivity));
				}else if(currentActivity instanceof Decomposed){
					newActivity = new Decomposed();
					newActivity.setIdent(newActivityIdent);
					copyDecomposedProperties(((Decomposed)newActivity),((Decomposed)currentActivity),oldProcessIdent,newProcessIdent, operationType, coordinates);
				}

				if(currentActivity.getTheActivityType()!=null){
					newActivity.insertIntoTheActivityType(currentActivity.getTheActivityType());
				}

				//add to main Collection
				newActivities.add(newActivity);
			}//end if !=null

		}//end for

		//after all , return the main Collection
		return newActivities;

	}

	private void copyDecomposedProperties(Decomposed newDecomposed, Decomposed currentDecomposed,String oldProcessIdent,String newProcessIdent, String operationType, Hashtable<String, String> coordinates) throws DAOException {
		if(currentDecomposed.getTheActivityType()!=null){
			newDecomposed.setTheActivityType(currentDecomposed.getTheActivityType());
		}
		if(currentDecomposed.getName()!=null){
			newDecomposed.setName(currentDecomposed.getName());
		}
		//subProcessModel and call recursivelly
		//new Refered ProcessModel
		ProcessModel newReferedProcessModel = new ProcessModel();
		if(currentDecomposed.getTheReferedProcessModel()!=null){
			//recursive invocation ....
			coordinates.putAll(copyProcessModelData(currentDecomposed.getTheReferedProcessModel(),newReferedProcessModel, oldProcessIdent, newProcessIdent, operationType));
			newDecomposed.setTheReferedProcessModel(newReferedProcessModel);
		}

	}//END METHOD

	private void copyAutomaticProperties(Automatic newAutomatic, Automatic currentAutomatic) {
		// set common attribute
		newAutomatic.setTheArtifact(currentAutomatic.getTheArtifact());

		//about parameters
		Collection<Parameters> newParameters = null;
		newParameters = copyAutomaticParameters(currentAutomatic.getTheParameters(),newAutomatic);
		newAutomatic.setTheParameters(newParameters);

		//about subroutine
		if(currentAutomatic.getTheSubroutine()!=null){
			Subroutine newSubRoutine = new Subroutine();
			if(currentAutomatic.getTheSubroutine().getTheArtifactType()!=null){
			newSubRoutine.insertIntoTheArtifactType(currentAutomatic.getTheSubroutine().getTheArtifactType());
			}
			newSubRoutine.insertIntoTheAutomatic(newAutomatic);

			//need to copy a new ToolParameter
			Collection<ToolParameters> newToolParameters = null;
			newToolParameters = copySubroutineToolParameters(currentAutomatic.getTheSubroutine().getTheToolParameters(),newSubRoutine);
			newSubRoutine.setTheToolParameters(newToolParameters);
		}//end if

	}//end method

	private void addCoordinate(Integer theReferredOid, String className, String newObjRef, Hashtable<String, String> coordinates) throws DAOException{
		String currentObjKey = className + ":" + String.valueOf(theReferredOid);
		coordinates.put(currentObjKey, newObjRef);
	}

	/**
	 * @param newActivities
	 * @return
	 */
	private Hashtable<String,Activity> generateActivitiesTable(Collection<Activity> newActivities) {
		Hashtable<String,Activity> activitiesTable = new Hashtable<String,Activity>(1000,1);
		for(Iterator<Activity> actIterator = newActivities.iterator();actIterator.hasNext();){
			Activity currentActivity = actIterator.next();
			activitiesTable.put(currentActivity.getIdent(),currentActivity);
		}
		return activitiesTable;
	}

	public void saveCoordinates(Hashtable<String, String> coordinates, String processIdent){
		try{
			Enumeration<String> enumer = coordinates.keys();

			WebAPSEEObject webObj;
			GraphicCoordinate coord, newCoord;

			Activity act;
			Connection con;
			ReqAgent reqAg;
			ReqGroup reqGr;
			RequiredResource reqRes;

			while (enumer.hasMoreElements()) {
				String currentObjKey = (String) enumer.nextElement();

				StringTokenizer currentObjTokenizer = new StringTokenizer(currentObjKey, ":");
				String className = currentObjTokenizer.nextToken();
				Integer theReferredOid = Integer.valueOf(currentObjTokenizer.nextToken());

				webObj = webAPSEEObjectDAO.retrieveWebAPSEEObject(theReferredOid, className);

				if(webObj!=null){
					String key = coordinates.get(currentObjKey);

					StringTokenizer tokenizer = new StringTokenizer(key,"+");
					String webObjType = tokenizer.nextToken();
					String webObjKey = tokenizer.nextToken();

					coord = webObj.getTheGraphicCoordinate();
					newCoord = new GraphicCoordinate();
					newCoord.setTheProcess(processIdent);
					newCoord.setX(coord.getX());
					newCoord.setY(coord.getY());
					newCoord.setVisible(coord.isVisible());

					if(webObjType.equals(WebAPSEEObject.ACTIVITY)){
						act = (Activity) activityDAO.retrieveBySecondaryKey(webObjKey);
						if(act!=null){
							webObj = new WebAPSEEObject(act.getOid(),act.getClass().getSimpleName(),newCoord);
							webAPSEEObjectDAO.save(webObj);
						}
					}else if(webObjType.equals(WebAPSEEObject.CONNECTION)){
						con = (Connection) connectionDAO.retrieveBySecondaryKey(webObjKey);
						if(con!=null){
							webObj = new WebAPSEEObject(con.getOid(),con.getClass().getSimpleName(),newCoord);
							webAPSEEObjectDAO.save(webObj);
						}
					}else{
						StringTokenizer webObjTokenizer = new StringTokenizer(webObjKey,":");

						String typeIdent = null;
						String instanceIdent = null;
						String normalIdent = null;

						if(webObjTokenizer.countTokens()==3){
							typeIdent = webObjTokenizer.nextToken();
							instanceIdent = webObjTokenizer.nextToken();
							normalIdent = webObjTokenizer.nextToken();
						}else if(webObjTokenizer.countTokens()==2){
							typeIdent = webObjTokenizer.nextToken();
							instanceIdent = "";
							normalIdent = webObjTokenizer.nextToken();
						}

						if(webObjType.equals(WebAPSEEObject.REQ_AGENT)){
							reqAg = reqAgentDAO.findReqAgentFromProcessModel(instanceIdent, typeIdent, normalIdent);
							if(reqAg!=null){
								webObj = new WebAPSEEObject(reqAg.getOid(),reqAg.getClass().getSimpleName(),newCoord);
								webAPSEEObjectDAO.save(webObj);
							}
						}else if(webObjType.equals(WebAPSEEObject.REQ_GROUP)){
							reqGr = reqGroupDAO.findReqGroupFromProcessModel(instanceIdent, typeIdent, normalIdent);
							if(reqGr!=null){
								webObj = new WebAPSEEObject(reqGr.getOid(),reqGr.getClass().getSimpleName(),newCoord);
								webAPSEEObjectDAO.save(webObj);
							}
						}else if(webObjType.equals(WebAPSEEObject.REQ_RESOURCE)){
							reqRes = reqResourceDAO.findRequiredResourceFromProcessModel(instanceIdent, typeIdent, normalIdent);
							if(reqRes!=null){
								webObj = new WebAPSEEObject(reqRes.getOid(),reqRes.getClass().getSimpleName(),newCoord);
								webAPSEEObjectDAO.save(webObj);
							}
						}
					}
				}
			}
		}catch(WebapseeException e){
			e.printStackTrace();
		}
	}


	public void decomposedDistilling(String dec_id, String template_id) throws WebapseeException {

		Decomposed decomposed = (Decomposed)decomposedDAO.retrieveBySecondaryKey(dec_id);

		String state = decomposed.getTheReferedProcessModel().getPmState();

		if(!state.equals(ProcessModel.REQUIREMENTS)){

			Template template = new Template();

			template.setIdent(template_id);

			//Copy relationships in common with Process
			Hashtable<String, String> coordinates =  copyServices.copyProcessModelData(decomposed.getTheReferedProcessModel(),template.getTheProcessModel(),dec_id,template.getIdent(), CopyProcessServiceImpl.DEC_DEST, null);

			template = (Template)templateDAO.save(template);

			copyServices.saveCoordinates(coordinates, template_id);
		}else System.out.println(Messages.getString("srcReuse.TempManag.TemplatesServices.templExecADecomp")+" "+state+" "+Messages.getString("srcReuse.TempManag.TemplatesServices.templExecCannBeDist")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public String getStateOfTemplate(String template_id) throws WebapseeException{

		Template template = getTemplate(template_id);

		String state = template.getTemplateState();

		if(state != null && !state.equals(""))
			return state;

		return null;
	}

	@Override
	public void processInstantiation(String template_id, String instance_id, String userIdent) throws WebapseeException {
		Template template = getTemplate(template_id);

		Project projectExists = projectDAO.retrieveBySecondaryKey(instance_id);
		if(projectExists != null) {
			throw new WebapseeException("O Projeto " + instance_id + " já foi cadastrado no banco.");
		} else {
			Object[] artifactsIdentsFromTemplate = artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(template_id);
			Properties artifactsIdents = null;

			if( artifactsIdentsFromTemplate != null){
				artifactsIdents = new Properties();
				String token = "";
				for (int i = 0; i < artifactsIdentsFromTemplate.length; i++) {
					String oldArtifactIdent = (String) artifactsIdentsFromTemplate[i];
					String newArtifactIdent = oldArtifactIdent;

					if(oldArtifactIdent.contains("-")){
						token = oldArtifactIdent.substring(0 , oldArtifactIdent.indexOf("-"));
						if(!token.equals("") && token.trim().contains(template_id)){
							newArtifactIdent =  oldArtifactIdent.substring(oldArtifactIdent.indexOf("-")+1).trim();
						}
					}
					artifactsIdents.put(oldArtifactIdent, newArtifactIdent);
				}
			}
			Process instance = new Process();
			instance.setIdent(instance_id);

			//Copy relationships in common with Process
			Hashtable<String, String> coordinates = copyServices.copyProcessModelData(template.getTheProcessModel(),instance.getTheProcessModel(),template_id,instance.getIdent(), CopyProcessServiceImpl.PROC_INST, artifactsIdents);
			instance = (Process)processDAO.save(instance);

			//Create Project for the instance
			Project project = new Project();
			project.setIdent(instance_id);
			project.setName(instance_id);

			project = (Project) projectDAO.save(project);
			project.setProcessRefered(instance);
			projectDAO.update(project);

			//Define the manager to created project
			Agent manager = (Agent) agentDAO.retrieveBySecondaryKey(userIdent);
			instance.insertIntoTheAgent(manager);
			processDAO.update(instance);

			//Make relationships of Template versioning
			ProcessModel pmodel = instance.getTheProcessModel();
			pmodel.setTheOrigin(template);
			template.insertIntoTheInstances(pmodel);

			processModelDAO.save(pmodel);
			templateDAO.save(template);
			copyServices.saveCoordinates(coordinates, instance_id);
		}
	}

	public void processComposition(String template_id, String currentLevel_id, Object[] artifactsIdentsFromUser) throws DAOException {
		System.out.println("caiu na composi��o: "+artifactsIdentsFromUser[0]);
		System.out.println("caiu na composi��o 2: "+artifactsIdentsFromUser[1]);


//		Object[] artifactsIdentsFromUser = artifactDAO.getArtifactsIdentsFromProcessModelWithoutTemplates(template_id);

		Properties artifactsIdents = null;

		if( artifactsIdentsFromUser != null){

			artifactsIdents = new Properties();

			for (int i = 0; i < artifactsIdentsFromUser.length; i++) {
				SimpleUpdateDTO artifactIdent = (SimpleUpdateDTO)artifactsIdentsFromUser[i];

				artifactsIdents.put(artifactIdent.getOldValue(), artifactIdent.getNewValue());
			}
		}

		Decomposed decomp1 = (Decomposed) decomposedDAO.retrieveBySecondaryKey(currentLevel_id);
		ProcessModel pModel1 = decomp1.getTheReferedProcessModel();

		Template template = (Template) templateDAO.retrieveBySecondaryKey(template_id);
		String state = template.getTemplateState();
		if(state.equals(Template.DEFINED) || state.equals(Template.PENDING)){
//			Delete activities and connections of the Process Model
			if(pModel1.getTheActivity().size()>0 || pModel1.getTheConnection().size()>0){
				Collection actIds = this.getActivitiesToDelete(pModel1);
				Collection conIds = this.getConnectionsToDelete(pModel1);

				Iterator iterActIds = actIds.iterator();
				while (iterActIds.hasNext()) {
					String id = (String) iterActIds.next();
					try {
						//The delete method from DynamicModelin is Invoked for each activity
						dynamicModeling.deleteActivity(id);
					} catch (DAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WebapseeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				Iterator iterConIds = conIds.iterator();
				while (iterConIds.hasNext()) {
					String id = (String) iterConIds.next();
					try {
						//The delete method from DynamicModelin is Invoked for each connection
						dynamicModeling.deleteConnection(id);
					} catch (DAOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (WebapseeException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}

			template = (Template) templateDAO.retrieveBySecondaryKey(template_id);

			Decomposed decomp = (Decomposed) decomposedDAO.retrieveBySecondaryKey(currentLevel_id);
			ProcessModel pModel = decomp.getTheReferedProcessModel();

			//Copy relationships in common with Process
			Hashtable<String, String> coordinates = copyServices.copyProcessModelData(template.getTheProcessModel(),pModel,template.getIdent(),currentLevel_id, CopyProcessServiceImpl.PROC_COMP, artifactsIdents);

			//Make relationships of Template versioning
			pModel.setTheOrigin(template);
			template.insertIntoTheInstances(pModel);

			templateDAO.update(template);

			//Change the process model descriptor
			copyServices.saveCoordinates(coordinates, currentLevel_id.substring(0, currentLevel_id.indexOf(".")));
		}
		else if(state.equals(Template.DRAFT) || state.equals(Template.OUTDATED)|| state.equals(Template.ENACTING)||state.equals(Template.FINISHED )||state.equals(Template.NOT_STARTED)){

			System.out.println("atividade n�o definida agora"+state);

		}
		else System.out.println(Messages.getString("srcReuse.TempManag.TemplatesServices.templExecAtemplInThe")+" "+state+" "+Messages.getString("srcReuse.TempManag.TemplatesServices.templExecCannBeInst"));
	}

	//Delete activities of the Process Model
	private Collection<String> getActivitiesToDelete(ProcessModel pModel){


		Collection<String> actIds = new HashSet<String>();

		//Get activities of the Process Model
		Collection activities = pModel.getTheActivity();

		Iterator iterActivity = activities.iterator();
		while (iterActivity.hasNext()) {

			Activity activity = (Activity)iterActivity.next();
			if(activity != null){
				actIds.add(activity.getIdent());
			}
		}
		return actIds;
	}

	//Delete connections of the Process Model
	private Collection<String> getConnectionsToDelete(ProcessModel pModel){


		Collection<String> conIds = new HashSet<String>();

		//Get connections of the Process Model
		Collection connections = pModel.getTheConnection();

		Iterator iterConnection = connections.iterator();
		while (iterConnection.hasNext()) {

			Connection connection = (Connection)iterConnection.next();
			if(connection != null){
				conIds.add(connection.getIdent());
			}
		}
		return conIds;
	}

	@Override
	public boolean toBecomeDefined(String template_id) throws WebapseeException {
		Template template = getTemplate(template_id);
		String state = template.getTemplateState();

		if(state.equals(Template.DRAFT)){
			template.setTemplateState(Template.DEFINED);

			Description desc = template.getTheOriginalVersionDescription();
			if(desc!=null){
				Template oldTemplate = desc.getTheOldVersion();
				oldTemplate.setTemplateState(Template.OUTDATED);
				templateDAO.update(oldTemplate);
			}

			templateDAO.save(template);
			return true;
		}else {
			return false;
		}
	}

	private TemplateDTO convertTemplateToTemplateDTO(Template template) {
		TemplateDTO dto = new TemplateDTO();
		dto.setIdent(template.getIdent());
		dto.setTemplateState(template.getTemplateState());

		dto.setTheInstances(new ArrayList<ProcessModelDTO>());

		return dto;
	}

	private List<TemplateDTO> convertTemplatesToTemplatesDTO(List<Template> templates) {
		List<TemplateDTO> templatesDTO = new ArrayList<TemplateDTO>();
		for (Template template : templates) {
			templatesDTO.add(this.convertTemplateToTemplateDTO(template));
		}
		return templatesDTO;
	}

	private Template getTemplate(String template_id) throws DAOException{
		//checks of parameters
        Template temp;
        temp = templateDAO.retrieveBySecondaryKey(template_id);
        if (temp == null){
//        	throw new DataBaseException(Messages.getString("srcReuse.TempManag.TemplatesServices.DBExcTempl")+template_id+Messages.getString("srcReuse.TempManag.TemplatesServices.DBExcDoesNot")); //$NON-NLS-1$ //$NON-NLS-2$
        	throw new DataBaseException("Deu erro ao buscar template"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // checks related to the state of the system
        return temp;
	}

	/**
	 * @param connections
	 * @param oldProcessIdent
	 * @param newProcessIdent
	 * @return
	 * @throws DAOException
	 */
	private Collection<Connection> copyConnectionsOnProcessModel(Collection<Connection> connections, String oldProcessIdent, String newProcessIdent,Hashtable<String,Activity> activitiesTable, String operationType, Hashtable<String, String> coordinates) throws DAOException {
		//need to take care about all kind of connections

		Hashtable<String,Connection> connectionTable = new Hashtable<String,Connection>(1000,1);

		HashSet<Connection> connectionsToResult = new HashSet<Connection>(1000);

		Collection<Connection> postProcessingCollection = new LinkedList<Connection>();

		for(Iterator<Connection> connectionsIterator = connections.iterator();connectionsIterator.hasNext();){
			Connection currentConnection = connectionsIterator.next();
			if(currentConnection!=null){
				Connection newConnection = null;
				String newConnectionIdent = currentConnection.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
				if(currentConnection instanceof Sequence){
					newConnection = new Sequence();
					if( ((Sequence)currentConnection).getTheDependency() !=null){
						Dependency newDependency = new Dependency();
						newDependency.setKindDep( ((Sequence)currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheSequence(((Sequence)newConnection));
					}//end if
					//about activities
					//ToActivity
					if ( ((Sequence)currentConnection).getToActivity()!=null)
					{
						//new activity ident
						String actIdent = ((Sequence)currentConnection).getToActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newToAct = activitiesTable.get(actIdent);
						if(newToAct!=null){
							((Sequence)newConnection).insertIntoToActivity(newToAct);
						}//end if
					}//end if != null
					//FromActivity
					if ( ((Sequence)currentConnection).getFromActivity()!=null)
					{
						//new activity ident
						String actIdent = ((Sequence)currentConnection).getFromActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newFromAct = activitiesTable.get(actIdent);
						if(newFromAct!=null){
							((Sequence)newConnection).insertIntoFromActivity(newFromAct);
						}//end if
					}//end if != null
					newConnection.setIdent(newConnectionIdent);
					this.addCoordinate(((Sequence)currentConnection).getOid(), ((Sequence)currentConnection).getClass().getSimpleName(), WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}//########################
				else if(currentConnection instanceof Feedback){
					newConnection = new Feedback();
					/*if( ((Feedback)currentConnection).getTheCondition() !=null){
						Condition newCondition = new Condition();
						newCondition.setCond( ((Feedback)currentConnection).getTheCondition().getCond());
						newCondition.insertIntoIsConditionOf(((Feedback)newConnection));
					}*///end if
					//about activities
					//ToActivity
					if ( ((Feedback)currentConnection).getToActivity()!=null)
					{
						//new activity ident
						String actIdent = ((Feedback)currentConnection).getToActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newToAct = activitiesTable.get(actIdent);
						if(newToAct!=null){
							((Feedback)newConnection).insertIntoToActivity(newToAct);
						}//end if
					}//end if != null
					//FromActivity
					if ( ((Feedback)currentConnection).getFromActivity()!=null)
					{
						//new activity ident
						String actIdent = ((Feedback)currentConnection).getFromActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newFromAct = activitiesTable.get(actIdent);
						if(newFromAct!=null){
							((Feedback)newConnection).insertIntoFromActivity(newFromAct);
						}//end if
					}//end if != null

					if(((Feedback)currentConnection).getTheCondition() != null){
						((Feedback)newConnection).removeFromTheCondition();
						((Feedback)newConnection).insertIntoTheCondition(((Feedback)currentConnection).getTheCondition().createClone());
					}//end if condition != null

					newConnection.setIdent(newConnectionIdent);
					this.addCoordinate(((Feedback)currentConnection).getOid(), ((Feedback)currentConnection).getClass().getSimpleName(), WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);

				}//########################
				else if(currentConnection instanceof ArtifactCon){
					newConnection = new ArtifactCon();
					if( ((ArtifactCon)currentConnection).getTheArtifactType()!=null){
						((ArtifactCon)newConnection).insertIntoTheArtifactType(	((ArtifactCon)currentConnection).getTheArtifactType());
					}

					Artifact currentArtifact = ((ArtifactCon)currentConnection).getTheArtifact();
					if( currentArtifact!=null ){
						String currentArtifactIdent = currentArtifact.getIdent();
						String newArtifactIdent = this.artifactsIdents.getProperty(currentArtifactIdent);

						if(newArtifactIdent==null || currentArtifactIdent.equals(newArtifactIdent)){
							((ArtifactCon)newConnection).insertIntoTheArtifact(	currentArtifact );
						}else{
							try {
								Artifact newArtifact = this.newArtifacts.get(newArtifactIdent);
								if(newArtifact==null){

									newArtifact = (Artifact) artifactDAO.retrieveBySecondaryKey(newArtifactIdent);
									if(newArtifact == null){
										newArtifact = new Artifact();
										newArtifact.setIdent(newArtifactIdent);
										newArtifact.setDescription(currentArtifact.getDescription());
										newArtifact.insertIntoTheArtifactType(((ArtifactCon) currentConnection).getTheArtifactType());
									}
								}

								((ArtifactCon)newConnection).insertIntoTheArtifact(	newArtifact );
								this.newArtifacts.put(newArtifactIdent, newArtifact);
							} catch (Exception e) {
								// TODO: handle exception
							}

						}
					}

					//about activities
					//ToActivity
						Collection toActivities = ((ArtifactCon)currentConnection).getToActivity();
						for(Iterator<Activity> toActivityIterator = toActivities.iterator();toActivityIterator.hasNext();){
							Activity currentToAct = toActivityIterator.next();
							if(currentToAct!=null){
								String actIdent = currentToAct.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
								Activity newToAct = activitiesTable.get(actIdent);
								if(newToAct!=null){
									((ArtifactCon)newConnection).insertIntoToActivity(newToAct);
								}//end if
							}//end if != null
						}//end for
					//FromActivity
						Collection fromActivities = ((ArtifactCon)currentConnection).getFromActivity();
						for(Iterator<Activity> fromActivityIterator = fromActivities.iterator();fromActivityIterator.hasNext();){
							Activity currentFromAct = fromActivityIterator.next();
							if(currentFromAct!=null){
								String actIdent = currentFromAct.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
								Activity newFromAct = activitiesTable.get(actIdent);
								if(newFromAct!=null){
									((ArtifactCon)newConnection).insertIntoFromActivity(newFromAct);
								}//end if
							}//end if != null
						}//end for

					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);
					this.addCoordinate(((ArtifactCon)currentConnection).getOid(), ((ArtifactCon)currentConnection).getClass().getSimpleName(), WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);

				}//########################
				else if(currentConnection instanceof Branch){

					//########################
					if(currentConnection instanceof BranchAND){
						newConnection = new BranchAND();
						//ToActivity
						Collection toActivities = ((BranchAND)currentConnection).getToActivity();
						for(Iterator<Activity> toActivityIterator = toActivities.iterator();toActivityIterator.hasNext();){
							Activity currentToAct = toActivityIterator.next();
							if(currentToAct!=null){
								String actIdent = currentToAct.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
								Activity newToAct = activitiesTable.get(actIdent);
								if(newToAct!=null){
									((BranchAND)newConnection).insertIntoToActivity(newToAct);
								}//end if
							}//end if != null
						}//end for

					}//########################
					else if(currentConnection instanceof BranchCond){
						newConnection = new BranchCond();

						((BranchCond)newConnection).setKindBranch( ((BranchCond)currentConnection).getKindBranch());

						//	ToActivity
						Collection toActivities = ((BranchCond)currentConnection).getTheBranchCondToActivity();
						for(Iterator<BranchCondToActivity> toActivityIterator = toActivities.iterator(); toActivityIterator.hasNext();){
							BranchCondToActivity currentToAct = toActivityIterator.next();
							if(currentToAct!=null){
								BranchCondToActivity newBranchCondToAct = new BranchCondToActivity();

								if(currentToAct.getTheActivity()!=null){
									String actIdent = currentToAct.getTheActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
									Activity newToAct = activitiesTable.get(actIdent);
									if(newToAct!=null){
										newToAct.insertIntoTheBranchCondToActivity(newBranchCondToAct);
									}//end if
								}//end if != null

								//about conditions
								if(currentToAct.getTheCondition() != null){
									newBranchCondToAct.removeFromTheCondition();
									newBranchCondToAct.insertIntoTheCondition(currentToAct.getTheCondition().createClone());
								}//end if condition != null

								//add current element to newBranchCond object
								((BranchCond)newConnection).insertIntoTheBranchCondToActivity(newBranchCondToAct);
							}//end if != null
						}//end for

						//###########

						//	((BranchCond)currentConnection).getKindBranch();

						//##########


					}//end if common atribute for all branch connections ((Branch)newConnection).setFired(((Branch)currentConnection).getFired());

					//about dependency
					if( ((Branch)currentConnection).getTheDependency() !=null){
						Dependency newDependency = new Dependency();
						newDependency.setKindDep( ((Branch)currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheMultipleCon( ((Branch)newConnection));
					}//end if
					//about from activity
					if( ((Branch)currentConnection).getFromActivity()!=null ){
						String actIdent = ((Branch)currentConnection).getFromActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newFromAct = activitiesTable.get(actIdent);
						if(newFromAct!=null){
							((Branch)newConnection).insertIntoFromActivity(newFromAct);
						}//end if
					}//end if

					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);

					this.addCoordinate(((Branch)currentConnection).getOid(), ((Branch)currentConnection).getClass().getSimpleName(), WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}
				else if(currentConnection instanceof Join){
					newConnection = new Join();
					//simple attributes
					//((Join)newConnection).setFired( ((Join)currentConnection).getFired() );

					((Join)newConnection).setKindJoin(((Join)currentConnection).getKindJoin());

					//about dependency
					if( ((Join)currentConnection).getTheDependency() !=null){
						Dependency newDependency = new Dependency();
						newDependency.setKindDep( ((Join)currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheMultipleCon(((Join)newConnection));
					}//end if

					//About activities
					//ToActivity
					//About to Activity
					if( ((Join)currentConnection).getToActivity()!=null ){
						String actIdent = ((Join)currentConnection).getToActivity().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						Activity newToAct = activitiesTable.get(actIdent);
						if(newToAct!=null){
							((Join)newConnection).insertIntoToActivity(newToAct);
						}//end if
					}//end if
					//FromActivity
					Collection fromActivities = ((Join)currentConnection).getFromActivity();
					for(Iterator<Activity> fromActivityIterator = fromActivities.iterator();fromActivityIterator.hasNext();){
						Activity currentFromAct = fromActivityIterator.next();
						if(currentFromAct!=null){
							String actIdent = currentFromAct.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
							Activity newFromAct = activitiesTable.get(actIdent);
							if(newFromAct!=null){
								((Join)newConnection).insertIntoFromActivity(newFromAct);
							}//end if
						}//end if != null
					}//end for
					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);

					this.addCoordinate(((Join)currentConnection).getOid(), ((Join)currentConnection).getClass().getSimpleName(), WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}//end join processing

				//about conection type
				if(currentConnection.getTheConnectionType()!=null){
					newConnection.insertIntoTheConnectionType(currentConnection.getTheConnectionType());
				}//end if
				if(newConnection!=null){
				connectionTable.put(newConnection.getIdent(),newConnection);

				connectionsToResult.add(newConnection);
				}
			}//end if != null
		}//end for

		//After all processing, we need to connect
		//Because multiple connection have connection to another connections...
		//After all mapping we need again check connection relationships
		for(Iterator<Connection> postProcessingIterator = postProcessingCollection.iterator();postProcessingIterator.hasNext();){

			Connection currentPostConnection = postProcessingIterator.next();
			Connection alreadyCreatedConnection = connectionTable.get(currentPostConnection.getIdent().replaceFirst(oldProcessIdent,newProcessIdent));

			//ToMutlipleCon
			if(currentPostConnection instanceof ArtifactCon){

				//To MultipleCon
				Collection toMultipleCon = ((ArtifactCon)currentPostConnection).getToMultipleCon();
				for(Iterator<MultipleCon> fromMultipleConIterator = toMultipleCon.iterator();fromMultipleConIterator.hasNext();){
					MultipleCon currentMultiple = fromMultipleConIterator.next();
					if(currentMultiple!=null){
						String multipleIdent = currentMultiple.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
						MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
						if(newFromMultipleCon!=null){
							((ArtifactCon)alreadyCreatedConnection).insertIntoToMultipleCon(newFromMultipleCon);
						}//end if
					}//end if != null
				}//end for

			}//end if artifactCon
			else if(currentPostConnection instanceof Branch){
				//From MultipleConnection

				MultipleCon fromMultipleCon = ((Branch)currentPostConnection).getFromMultipleConnection();
				if(fromMultipleCon!=null){
					String multipleIdent = fromMultipleCon.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
					MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
					if(newFromMultipleCon!=null){
						((Branch)alreadyCreatedConnection).insertIntoFromMultipleConnection(newFromMultipleCon);
					}//end if
				}//end if

				if(currentPostConnection instanceof BranchAND){
					Collection toMultipleCon = ((BranchAND)currentPostConnection).getToMultipleCon();
					for(Iterator<MultipleCon> toMultipleConIterator = toMultipleCon.iterator();toMultipleConIterator.hasNext();){
						MultipleCon currentMultiple = toMultipleConIterator.next();
						if(currentMultiple!=null){
							String multipleIdent = currentMultiple.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
							MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
							if(newFromMultipleCon!=null){
								((BranchAND)alreadyCreatedConnection).insertIntoToMultipleCon(newFromMultipleCon);
							}//end if
						}//end if != null
					}//end for
				}else if(currentPostConnection instanceof BranchCond){
					Collection toMultipleCon = ((BranchCond)currentPostConnection).getTheBranchCondToMultipleCon();
					for(Iterator<BranchCondToMultipleCon> toMultipleIterator = toMultipleCon.iterator();toMultipleIterator.hasNext();){
						BranchCondToMultipleCon currentToMult = toMultipleIterator.next();
						if(currentToMult!=null){
							BranchCondToMultipleCon newBranchCondToMult = new BranchCondToMultipleCon();
							if(currentToMult.getTheMultipleCon()!=null){
								String multipleIdent = currentToMult.getTheMultipleCon().getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
								MultipleCon newToMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
								if(newToMultipleCon!=null){
									((BranchCondToMultipleCon)newBranchCondToMult).insertIntoTheMultipleCon(newToMultipleCon);
								}//end if
							}//end if != null
							// about conditions
//							newBranchCondToMult.setCondition(currentToMult.getCondition());
							((BranchCond)alreadyCreatedConnection).insertIntoTheBranchCondToMultipleCon(newBranchCondToMult);
						}//end if != null
					}//end for
				}//end if

			}
			else if(currentPostConnection instanceof Join){
				//to MultipleCon
				MultipleCon toMultipleCon = ((Join)currentPostConnection).getToMultipleCon();
				if(toMultipleCon!=null){
					String multipleIdent = toMultipleCon.getIdent().replaceFirst(oldProcessIdent,newProcessIdent);
					MultipleCon newToMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
					if(newToMultipleCon!=null){
						((Join)alreadyCreatedConnection).insertIntoToMultipleCon(newToMultipleCon);
					}//end if
				}//end if
				//Don`t care about fromConnection...be cause all To are already checked!
			}

		}//end for postProcessing

		return connectionsToResult;
	}

	private Collection<ToolParameters> copySubroutineToolParameters(Collection theToolParameters, Subroutine newSubRoutine) {
		Collection<ToolParameters> newParameters = new LinkedList<ToolParameters>();

		for(Iterator<ToolParameters> paramIterator= theToolParameters.iterator();paramIterator.hasNext();){
			ToolParameters currentParameter = paramIterator.next();
			if(currentParameter!=null){
				ToolParameters newParameter = null;
					newParameter = new ToolParameters();
					newParameter.insertIntoTheSubroutine(newSubRoutine);

					newParameter.setLabel(currentParameter.getLabel());
					newParameter.setSeparatorSymbol(currentParameter.getSeparatorSymbol());
					if(currentParameter.getTheArtifactType()!=null){
						newParameter.setTheArtifactType(currentParameter.getTheArtifactType());
					}
					if(currentParameter.getThePrimitiveType()!=null){
						newParameter.setThePrimitiveType(currentParameter.getThePrimitiveType());
					}

				//add to main collection
				newParameters.add(newParameter);
			}//END IF
		}//END FOR
		//after all, retrieve correct collection
		return newParameters;
	}

	private Collection<Parameters> copyAutomaticParameters(Collection theParameters, Automatic newAutomatic) {
		Collection<Parameters> newParameters = new LinkedList<Parameters>();
		for(Iterator<Parameters> paramIterator= theParameters.iterator();paramIterator.hasNext();){
			Parameters currentParameter = paramIterator.next();
			if(currentParameter!=null){
				Parameters newParameter = null;
					newParameter = new Parameters();
					newParameter.insertIntoTheAutomatic(newAutomatic);
					newParameter.setDescription(currentParameter.getDescription());
				//add to main collection
				newParameters.add(newParameter);
			}//END IF
		}//END FOR
		//after all, retrieve correct collection
		return newParameters;
	}//END METHOD

	/**
	 * @param destinationNormal
	 * @param sourceNormal
	 * @throws DAOException
	 */
	private void copyNormalProperties(Normal destinationNormal,Normal sourceNormal, String operationType, String current_level, Hashtable<String, String> coordinates) throws DAOException{
		//simple attributes
		destinationNormal.setHowLong(sourceNormal.getHowLong());
		destinationNormal.setHowLongUnit(sourceNormal.getHowLongUnit());
		destinationNormal.setName(sourceNormal.getName());
		destinationNormal.setScript(sourceNormal.getScript());
		destinationNormal.setDelegable(sourceNormal.getDelegable());
		destinationNormal.setAutoAllocable(sourceNormal.getAutoAllocable());

		if(!operationType.equals(PROC_DEST)){
			destinationNormal.setPlannedBegin(sourceNormal.getPlannedBegin());
			destinationNormal.setPlannedEnd(sourceNormal.getPlannedEnd());
		}

		destinationNormal.setStaticOk(sourceNormal.getStaticOk());
		destinationNormal.setRequirements(sourceNormal.getRequirements());
		// about conditions
		if(sourceNormal.getPreCondition()!=null){
/*	        Condition newPreCondition = new Condition();
	    	newPreCondition.setCond(sourceNormal.getPreCondition().getCond());
	    	newPreCondition.insertIntoPreCondition(destinationNormal);
*/
			/*PolCondition newPreCondition = sourceNormal.getPreCondition().createClone();
			destinationNormal.insertIntoPreCondition(newPreCondition);*/
		}
		if(sourceNormal.getPosCondition()!=null){
/*			Condition newPosCondition = new Condition();
		    newPosCondition.setCond(sourceNormal.getPosCondition().getCond());
	    	newPosCondition.insertIntoPosCondition(destinationNormal);
*/
			/*PolCondition newPosCondition = sourceNormal.getPosCondition().createClone();
			destinationNormal.insertIntoPosCondition(newPosCondition);*/
		}
		//about reservations
		Collection<Reservation> newReservations = null;
		newReservations = copyReservations(sourceNormal.getTheReservation(),destinationNormal);
		destinationNormal.setTheReservation(newReservations);
		//about involved artifacts
		//involved to
		Collection<InvolvedArtifacts> newInvolvedArtifactsToNormal = null;
		newInvolvedArtifactsToNormal = copyInvolvedArtifacts(sourceNormal.getInvolvedArtifactToNormal(),destinationNormal,TO_INVOLVED, current_level);
		destinationNormal.setInvolvedArtifactToNormal(newInvolvedArtifactsToNormal);
		//involved from
		Collection<InvolvedArtifacts> newInvolvedArtifactsFromNormal = null;
		newInvolvedArtifactsFromNormal = copyInvolvedArtifacts(sourceNormal.getInvolvedArtifactFromNormal(),destinationNormal,FROM_INVOLVED, current_level);
		destinationNormal.setInvolvedArtifactFromNormal(newInvolvedArtifactsFromNormal);
		//about requied Resources
		Collection<RequiredResource> newRequiredResources = null;
		newRequiredResources = copyRequiredResources(sourceNormal.getTheRequiredResource(),destinationNormal, operationType, coordinates);
		destinationNormal.setTheRequiredResource(newRequiredResources);
		//about required People
		Collection<RequiredPeople> newRequiredPeople = null;
		newRequiredPeople = copyRequiredPeople(sourceNormal.getTheRequiredPeople(),destinationNormal, operationType, coordinates);
		destinationNormal.setTheRequiredPeople(newRequiredPeople);

	}

	/**
	 * @param theRequiredPeople
	 * @param destinationNormal
	 * @return
	 * @throws DAOException
	 */
	private Collection<RequiredPeople> copyRequiredPeople(Collection theRequiredPeople, Normal destinationNormal, String operationType, Hashtable<String, String> coordinates) throws DAOException {
		Collection<RequiredPeople> newRequiredPeoples = new HashSet<RequiredPeople>();

		String coordinateKey = null;
		Collection<String> roles = new HashSet<String>();

		for(Iterator<RequiredPeople> requiredIterator= theRequiredPeople.iterator();requiredIterator.hasNext();){

			RequiredPeople currentReqPeople = requiredIterator.next();
			if(currentReqPeople!=null){

				if(currentReqPeople instanceof ReqAgent ){
					ReqAgent currentReqAg = (ReqAgent)currentReqPeople;
					ReqAgent newReqAgent = new ReqAgent();
					if(currentReqAg.getTheRole()!=null && !roles.contains(currentReqAg.getTheRole().getIdent())){
						newReqAgent.insertIntoTheRole( currentReqAg.getTheRole());
						roles.add(currentReqAg.getTheRole().getIdent());
						coordinateKey = newReqAgent.getTheRole().getIdent();

						if(!operationType.equals(PROC_DEST)){
							if(currentReqAg.getTheAgent()!=null){
								newReqAgent.insertIntoTheAgent(currentReqAg.getTheAgent());
								coordinateKey = coordinateKey + ":" + newReqAgent.getTheAgent().getIdent();
								}
						}
						coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();

						//about ReqAgentRequiresAbility
						Collection<ReqAgentRequiresAbility> newReqAgReqAbility=null;
						newReqAgReqAbility = copyReqAgentReqAbility(currentReqAg.getTheReqAgentRequiresAbility(),newReqAgent);
						newReqAgent.setTheReqAgentRequiresAbility(newReqAgReqAbility);

						this.addCoordinate(currentReqAg.getOid(), currentReqAg.getClass().getSimpleName(), WebAPSEEObject.REQ_AGENT +"+"+ coordinateKey, coordinates);
						coordinateKey = null;

						//the common attribute normal activity
						newReqAgent.insertIntoTheNormal(destinationNormal);

						//add to main collection
						newRequiredPeoples.add(newReqAgent);
					}
				}else if(currentReqPeople instanceof ReqGroup){
					ReqGroup currentReqGroup = (ReqGroup)currentReqPeople;
					ReqGroup newReqGroup = new ReqGroup();

					if(currentReqGroup.getTheGroupType()!=null){
						newReqGroup.insertIntoTheGroupType( currentReqGroup.getTheGroupType() );
						coordinateKey = newReqGroup.getTheGroupType().getIdent();
						}

					if(!!operationType.equals(PROC_DEST)){
						if(currentReqGroup.getTheGroup()!=null){
							newReqGroup.insertIntoTheGroup(currentReqGroup.getTheGroup());
							coordinateKey = coordinateKey + ":" + newReqGroup.getTheGroup().getIdent();
							}
					}

					coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();
					this.addCoordinate(currentReqGroup.getOid(), currentReqGroup.getClass().getSimpleName(), WebAPSEEObject.REQ_GROUP +"+"+coordinateKey, coordinates);
					coordinateKey = null;

					//the common attribute normal activity
					newReqGroup.insertIntoTheNormal(destinationNormal);

					//add to main collection
					newRequiredPeoples.add(newReqGroup);
				}//end if

			}//END IF

		}//END FOR

		//after all, retrieve correct collection
		return newRequiredPeoples;
	}//end method

	/**
	 * @param theReqAgentRequiresAbility
	 * @param newReqAgent
	 * @return
	 */
	private Collection<ReqAgentRequiresAbility> copyReqAgentReqAbility(Collection theReqAgentRequiresAbility,ReqAgent newReqAgent) {
		Collection<ReqAgentRequiresAbility> newReqAgReqAbilities = new HashSet<ReqAgentRequiresAbility>();

		for(Iterator<ReqAgentRequiresAbility> requiredIterator= theReqAgentRequiresAbility.iterator();requiredIterator.hasNext();){

			ReqAgentRequiresAbility currentReqAgReqAbility = requiredIterator.next();
			if(currentReqAgReqAbility!=null){
				ReqAgentRequiresAbility newReqAgReqAb = new ReqAgentRequiresAbility();

				if(currentReqAgReqAbility.getTheAbility()!=null){
				newReqAgReqAb.insertIntoTheAbility(currentReqAgReqAbility.getTheAbility());
				}

				newReqAgReqAb.setDegree(currentReqAgReqAbility.getDegree());
				newReqAgReqAb.insertIntoTheReqAgent(newReqAgent);

				//add to Main Collection
				newReqAgReqAbilities.add(newReqAgReqAb);

			}//END IF

		}//END FOR
		//after all, retrieve correct collection
		return newReqAgReqAbilities;
	}

	/**
	 * @param theRequiredResource
	 * @param destinationNormal
	 * @return
	 * @throws DAOException
	 */
	private Collection<RequiredResource> copyRequiredResources(Collection<RequiredResource> theRequiredResource, Normal destinationNormal, String operationType, Hashtable<String, String> coordinates) throws DAOException {
		Collection<RequiredResource> newRequiredResources = new HashSet<RequiredResource>();

		String coordinateKey = null;

		for(Iterator<RequiredResource> requiredIterator= theRequiredResource.iterator();requiredIterator.hasNext();){

			RequiredResource currentReqResource = requiredIterator.next();
			if(currentReqResource!=null){
				RequiredResource newRequiredResource = new RequiredResource();

				if(currentReqResource.getTheResourceType()!=null){
				newRequiredResource.insertIntoTheResourceType(currentReqResource.getTheResourceType());
				coordinateKey = currentReqResource.getTheResourceType().getIdent();
				}
				if(!operationType.equals(PROC_DEST)){
					if(currentReqResource.getTheResource()!=null){
						newRequiredResource.insertIntoTheResource(currentReqResource.getTheResource());
						newRequiredResource.setAmountNeeded(currentReqResource.getAmountNeeded());
						coordinateKey = coordinateKey + ":" + currentReqResource.getTheResource().getIdent();
						}
				}

				newRequiredResource.insertIntoTheNormal(destinationNormal);
				coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();
				this.addCoordinate(currentReqResource.getOid(), currentReqResource.getClass().getSimpleName(), WebAPSEEObject.REQ_RESOURCE +"+"+coordinateKey, coordinates);
				coordinateKey = null;

				//add to Main Collection
				newRequiredResources.add(newRequiredResource);

			}//END IF


		}//END FOR
		//after all, retrieve correct collection
		return newRequiredResources;
	}//end method

	/**
	 * @param theReservation  collection of current reservations
	 * @param destinationNormal normal activity that will receive the new collection
	 * @return the new collection of reservations ... to be inserted into the new Normal activity
	 */
	private Collection<Reservation> copyReservations(Collection theReservation, Normal destinationNormal) {
		Collection<Reservation> newInvolvedReservations = new HashSet<Reservation>();
		for(Iterator<Reservation> reservationsIterator= theReservation.iterator();reservationsIterator.hasNext();){
			Reservation currentReservation = reservationsIterator.next();
			if(currentReservation!=null){
				Reservation newReservation = new Reservation();
				newReservation.insertIntoTheNormal(destinationNormal);
				if(currentReservation.getTheExclusive()!=null){
				newReservation.insertIntoTheExclusive(currentReservation.getTheExclusive());
				}
				//dates
				newReservation.setFrom(currentReservation.getFrom());
				newReservation.setTo(currentReservation.getTo());
				//add to Main Collection
				newInvolvedReservations.add(newReservation);
			}//END IF

		}//END FOR
		//after all, retrieve correct collection
		return newInvolvedReservations;
	}//end method

	//private constants, to reuse the method to copay involved artifacts
	private static int TO_INVOLVED =0, FROM_INVOLVED=1;
	/**
	 * @param currentInvolvedArtifacts
	 * @param newNormalReference
	 * @param kindRelationship
	 * @return
	 * @throws DAOException
	 */
	private Collection<InvolvedArtifacts> copyInvolvedArtifacts(Collection<InvolvedArtifacts> currentInvolvedArtifacts,Normal newNormalReference,int kindRelationship, String current_level) throws DAOException {

		Collection<InvolvedArtifacts> newInvolvedArtifacts = new HashSet<InvolvedArtifacts>();
		for(Iterator<InvolvedArtifacts> involvedIterator= currentInvolvedArtifacts.iterator();involvedIterator.hasNext();){
			InvolvedArtifacts currentInvolved = involvedIterator.next();
			if(currentInvolved!=null){
				InvolvedArtifacts newInvolved = new InvolvedArtifacts();
				if(currentInvolved.getTheArtifactType()!=null){
					newInvolved.insertIntoTheArtifactType(currentInvolved.getTheArtifactType());
				}

				Artifact currentArtifact = currentInvolved.getTheArtifact();
				if(currentArtifact!=null){
					String currentArtifactIdent = currentArtifact.getIdent();

					String newArtifactIdent = this.artifactsIdents.getProperty(currentArtifactIdent);

					if(newArtifactIdent==null || currentArtifactIdent.equals(newArtifactIdent)){
						newInvolved.insertIntoTheArtifact(currentArtifact);
					}else{
						Artifact newArtifact;
						newArtifact = newArtifacts.get(newArtifactIdent);
						if(newArtifact == null){
							newArtifact = (Artifact) artifactDAO.retrieveBySecondaryKey(newArtifactIdent);
							if(newArtifact == null){
								newArtifact = new Artifact();
								newArtifact.setIdent(newArtifactIdent);
								newArtifact.setDescription(currentArtifact.getDescription());
								newArtifact.insertIntoTheArtifactType(currentInvolved.getTheArtifactType());
							}
						}
						newInvolved.insertIntoTheArtifact(newArtifact);
						this.newArtifacts.put(newArtifactIdent, newArtifact);
					}
				}

				if(kindRelationship==TO_INVOLVED){
					newInvolved.insertIntoInInvolvedArtifacts(newNormalReference);
				}else if(kindRelationship==FROM_INVOLVED){
					newInvolved.insertIntoOutInvolvedArtifacts(newNormalReference);
				}
				//add to Main Collection
				newInvolvedArtifacts.add(newInvolved);

			}//END IF
		}//END FOR
		//after all, retrieve correct collection
		return newInvolvedArtifacts;
	}//end method


}
