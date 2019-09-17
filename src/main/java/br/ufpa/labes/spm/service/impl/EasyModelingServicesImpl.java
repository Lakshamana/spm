package org.qrconsult.spm.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.qrconsult.spm.beans.editor.WebAPSEENodePosition;
import org.qrconsult.spm.dataAccess.impl.activities.DecomposedDAO;
import org.qrconsult.spm.dataAccess.impl.plainActivities.NormalDAO;
import org.qrconsult.spm.dataAccess.impl.processModels.ProcessDAO;
import org.qrconsult.spm.dataAccess.impl.processModels.ProcessModelDAO;
import org.qrconsult.spm.dataAccess.interfaces.activities.IActivityDAO;
import org.qrconsult.spm.dataAccess.interfaces.activities.IDecomposedDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IGroupDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IRoleDAO;
import org.qrconsult.spm.dataAccess.interfaces.artifacts.IArtifactDAO;
import org.qrconsult.spm.dataAccess.interfaces.calendar.ICalendarDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IArtifactConDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IBranchCondToActivityDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IBranchCondToMultipleConDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IBranchDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IConnectionDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IJoinDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.IMultipleConDAO;
import org.qrconsult.spm.dataAccess.interfaces.connections.ISimpleConDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IAutomaticDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IInvolvedArtifactsDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.INormalDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IParametersDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IReqAgentDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IReqGroupDAO;
import org.qrconsult.spm.dataAccess.interfaces.plainActivities.IRequiredResourceDAO;
import org.qrconsult.spm.dataAccess.interfaces.policies.staticPolicies.IPolConditionDAO;
import org.qrconsult.spm.dataAccess.interfaces.processModelGraphic.IGraphicCoordinateDAO;
import org.qrconsult.spm.dataAccess.interfaces.processModelGraphic.IWebAPSEEObjectDAO;
import org.qrconsult.spm.dataAccess.interfaces.processModels.IProcessDAO;
import org.qrconsult.spm.dataAccess.interfaces.processModels.IProcessModelDAO;
import org.qrconsult.spm.dataAccess.interfaces.resources.IConsumableDAO;
import org.qrconsult.spm.dataAccess.interfaces.resources.IResourceDAO;
import org.qrconsult.spm.dataAccess.interfaces.taskagenda.IProcessAgendaDAO;
import org.qrconsult.spm.dataAccess.interfaces.taskagenda.ITaskDAO;
import org.qrconsult.spm.dataAccess.interfaces.tools.ISubroutineDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IArtifactTypeDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IGroupTypeDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IResourceTypeDAO;
import org.qrconsult.spm.dtos.calendar.CalendarDTO;
import org.qrconsult.spm.exceptions.DAOException;
import org.qrconsult.spm.exceptions.ModelingException;
import org.qrconsult.spm.exceptions.WebapseeException;
import org.qrconsult.spm.model.activities.Activity;
import org.qrconsult.spm.model.activities.Decomposed;
import org.qrconsult.spm.model.activities.Plain;
import org.qrconsult.spm.model.artifacts.Artifact;
import org.qrconsult.spm.model.connections.ArtifactCon;
import org.qrconsult.spm.model.connections.Branch;
import org.qrconsult.spm.model.connections.BranchAND;
import org.qrconsult.spm.model.connections.BranchCond;
import org.qrconsult.spm.model.connections.BranchCondToActivity;
import org.qrconsult.spm.model.connections.BranchCondToMultipleCon;
import org.qrconsult.spm.model.connections.Connection;
import org.qrconsult.spm.model.connections.Dependency;
import org.qrconsult.spm.model.connections.Feedback;
import org.qrconsult.spm.model.connections.Join;
import org.qrconsult.spm.model.connections.MultipleCon;
import org.qrconsult.spm.model.connections.Sequence;
import org.qrconsult.spm.model.connections.SimpleCon;
import org.qrconsult.spm.model.plainActivities.Automatic;
import org.qrconsult.spm.model.plainActivities.EnactionDescription;
import org.qrconsult.spm.model.plainActivities.InvolvedArtifacts;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.plainActivities.Parameters;
import org.qrconsult.spm.model.plainActivities.ReqAgent;
import org.qrconsult.spm.model.plainActivities.ReqAgentRequiresAbility;
import org.qrconsult.spm.model.plainActivities.ReqGroup;
import org.qrconsult.spm.model.plainActivities.RequiredPeople;
import org.qrconsult.spm.model.plainActivities.RequiredResource;
import org.qrconsult.spm.model.processModelGraphical.GraphicCoordinate;
import org.qrconsult.spm.model.processModelGraphical.WebAPSEEObject;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.processModels.ProcessModel;
import org.qrconsult.spm.model.resources.Reservation;
import org.qrconsult.spm.model.tools.Subroutine;
import org.qrconsult.spm.model.tools.ToolParameters;
import org.qrconsult.spm.services.interfaces.DynamicModeling;
import org.qrconsult.spm.services.interfaces.EasyModelingServices;
import org.qrconsult.spm.services.interfaces.EnactmentEngineLocal;
import org.qrconsult.spm.services.interfaces.NotificationServices;
import org.qrconsult.spm.services.interfaces.commonData.SimpleActivityQueryResult;
import org.qrconsult.spm.util.i18n.Messages;

@Stateless(name="easyModeling")
public class EasyModelingServicesImpl implements EasyModelingServices {

	final int MILLIS_PER_DAY = 1000 * 60 * 60 * 24;

	private Hashtable<String, Activity> activitiesTable = new Hashtable<String, Activity>();

	private Normal lastValid;

	private Properties allActs;
	private Properties allConns;

	@EJB
	EnactmentEngineLocal enactmentLocal;
	@EJB
	ICalendarDAO calendarioDTO;
	@EJB
	DynamicModeling dynamicModeling;
	@EJB
	IProcessDAO procDAO;
	@EJB
	NotificationServices remote;
	@EJB
	IDecomposedDAO decDAO;
	@EJB
	IActivityDAO actDAO;
	@EJB
	INormalDAO normDAO;
	@EJB
	IAutomaticDAO autoDAO;
	@EJB
	IArtifactDAO artDAO;
	@EJB
	IInvolvedArtifactsDAO involvedDAO;
	@EJB
	IGraphicCoordinateDAO coordDAO;
	@EJB
	IProcessModelDAO pmodelDAO;
	@EJB
	ISubroutineDAO subDAO;
	@EJB
	IParametersDAO paramDAO;
	@EJB
	IArtifactConDAO artConDAO;
	@EJB
	IArtifactTypeDAO artTypeDAO;
	@EJB
	IInvolvedArtifactsDAO invArtDAO;
	@EJB
	IMultipleConDAO multiDAO;
	@EJB
	IConnectionDAO conDAO;
	@EJB
	IPolConditionDAO polConditionDAO;
	@EJB
	IBranchCondToMultipleConDAO bctmcDAO;
	@EJB
	IJoinDAO joinDAO;
	@EJB
	IBranchDAO branchDAO;
	@EJB
	IGroupTypeDAO groupTypeDAO;
	@EJB
	IRoleDAO roleDAO;
	@EJB
	IReqAgentDAO reqAgentDAO;
	@EJB
	IAgentDAO agentDAO;
	@EJB
	ITaskDAO taskDAO;
	@EJB
	IGroupDAO groupDAO;
	@EJB
	IReqGroupDAO reqGroupDAO;
	@EJB
	IResourceTypeDAO resTypeDAO;
	@EJB
	IRequiredResourceDAO reqResDAO;
	@EJB
	IResourceDAO resDAO;
	@EJB
	IConsumableDAO consumableDAO;
	@EJB
	IBranchCondToActivityDAO branchCondToActivityDAO;
	@EJB
	ISimpleConDAO simpleDAO;
	@EJB
	IProcessAgendaDAO pAgendaDAO;
	@EJB
	IWebAPSEEObjectDAO webAPSEEObjDAO;
	@EJB
	IGraphicCoordinateDAO grapDAO;
	
	
	private Date newBeginDate;

	private Date newEndDate;

	@Override
	public String messageToFlex() {
		return "Mensagem do easyModelingServices";
	}
	
	@Override
	public Map<String, String> mapToFlex() {
		System.out.println("Entrou no metodo mapToFlex");
		Map<String, String> mapa = new HashMap<String, String>();
		mapa.put("pos1", "1,2");
		mapa.put("pos2", "72,32");
		mapa.put("pos3", "8,54");
		return mapa;
	}
	
	@Override
	public void flexMap(String[] obj) {
		System.out.println("Mapa do Flex");
		System.out.println(obj);
		System.out.println(obj.getClass().getSimpleName());
		System.out.println(obj.getClass());
	}
	
	@Override
	public void getCoordinatesResponse(String processId, String[] idents, String[] xs, String[] ys, String[] types, String[] nodeTypes, String[] referredObjs) {
//		Map<String, String> coordenadas = new HashMap<String, String>();
		
		List<WebAPSEENodePosition> webAPSEENodes = new ArrayList<WebAPSEENodePosition>();
		for (int i = 0; i < idents.length; i++) {
			Double x = new Double(xs[i]);
			Double y = new Double(ys[i]);
			Double nodeType = new Double(nodeTypes[i]);
			int nodePositionType = this.nodePositionType(nodeType.intValue());
			System.out.println("posi: "+x.intValue()+" "+y.intValue()+ " "+idents[i]+" "+ types[i]+" "+nodePositionType);
			
			WebAPSEENodePosition position = new WebAPSEENodePosition(x.intValue(), y.intValue(), idents[i], types[i], nodePositionType);
			
			String[] objects = referredObjs[i].split(";");
			position.setTheReferredObjects(Arrays.asList(objects));
			webAPSEENodes.add(position);
			
//			prints
			System.out.println("Ident: " + idents[i] + "Referred: " + referredObjs[i]);
			System.out.println("ReferredObjects");
			for (String obj : objects) {
				System.out.println("Obj: " + obj);
			}
			
		}
		
		saveWebAPSEENodePositions(processId, webAPSEENodes);

	}
	
	public void saveWebAPSEENodePositions(String processIdent, Collection<WebAPSEENodePosition> positions) {
		
		try {
			WebAPSEEObject webAPSEEObj = null;
			GraphicCoordinate graphicCoord;
			
			Integer theReferredOid = null;
			String className = null;
			boolean ok = false;

			for (Iterator<WebAPSEENodePosition> iterator = positions.iterator(); iterator.hasNext();) {
				WebAPSEENodePosition webAPSEENodePosition = (WebAPSEENodePosition) iterator.next();
				ok = false;
				
				if(webAPSEENodePosition.getNodeType()==WebAPSEENodePosition.ACTIVITYNODE){
					Activity act = (Activity) actDAO.retrieveBySecondaryKey(webAPSEENodePosition.getInstanceID());
					if(act!=null){
						theReferredOid = act.getOid();
						className = act.getClass().getSimpleName();
						ok = true;
						webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
					}
					
				} else if (webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.ARTIFACTCONNODE
						|| webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.BRANCHNODE
						|| webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.JOINNODE
						|| webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.SEQUENCENODE) {

					Connection con = (Connection) conDAO.retrieveBySecondaryKey(webAPSEENodePosition.getInstanceID());
					if(!webAPSEENodePosition.getTheReferredObjects().isEmpty()) {
						
						List<String> objs = (List<String>) webAPSEENodePosition.getTheReferredObjects();
						
						con = new Connection();
						
						con=(Connection) conDAO.retrieveBySecondaryKey(objs.get(0));
						System.out.print("aqui: "+con);
					}
					
					con = (Connection) conDAO.retrieveBySecondaryKey(webAPSEENodePosition.getInstanceID());
					if(con!=null){
						System.out.print("com n�o nula: ");
						theReferredOid = con.getOid();
						className = con.getClass().getSimpleName();
						ok = true;
						webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
					}
					
					if (webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.ARTIFACTCONNODE) {
						con = (Connection) conDAO.retrieveBySecondaryKey(webAPSEENodePosition.getInstanceID());
						ArtifactCon artifactCon = (ArtifactCon) con;
						System.out.println("ArtifactCon: " + artifactCon);
						ok = true;
						theReferredOid = artifactCon.getOid();
						className = artifactCon.getClass().getSimpleName();
						webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
					}
					
				}else if(webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.REQAGENTNODE){
			
					Collection<String> theReferredObjs = webAPSEENodePosition.getTheReferredObjects();
//					System.out.println("caiu no req: "+theReferredObjs);
					if(theReferredObjs.size()==1){
						String[] normals = new String[1];
						theReferredObjs.toArray(normals);
						String normal = normals[0];
						
						ReqAgent reqAg = (ReqAgent) reqAgentDAO.findReqAgentFromProcessModel(webAPSEENodePosition.getInstanceID(), webAPSEENodePosition.getTypeID(), normal);
						if(reqAg!=null){
//							System.out.println("caiu no ref id: "+reqAg.getOid());
							
							theReferredOid = reqAg.getOid();
							className = reqAg.getClass().getSimpleName();
							ok = true;
							webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
						}
					}
				}else if(webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.REQGROUPNODE){
					Collection<String> theReferredObjs = webAPSEENodePosition.getTheReferredObjects();
					if(theReferredObjs.size()==1){
						String[] normals = new String[1];
						theReferredObjs.toArray(normals);
						String normal = normals[0];
						
						ReqGroup reqGroup = (ReqGroup) reqGroupDAO.findReqGroupFromProcessModel(webAPSEENodePosition.getInstanceID(), webAPSEENodePosition.getTypeID(), normal);
						if(reqGroup!=null){
							theReferredOid = reqGroup.getOid();
							className = reqGroup.getClass().getSimpleName();
							ok = true;
							webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
						}
					}
				}else if(webAPSEENodePosition.getNodeType() == WebAPSEENodePosition.REQRESOURCENODE){
					Collection<String> theReferredObjs = webAPSEENodePosition.getTheReferredObjects();
					if(theReferredObjs.size()==1){
						String[] normals = new String[1];
						theReferredObjs.toArray(normals);
						String normal = normals[0];
						
						RequiredResource reqRes = (RequiredResource) reqResDAO.findRequiredResourceFromProcessModel(webAPSEENodePosition.getInstanceID(), webAPSEENodePosition.getTypeID(), normal);
						if(reqRes!=null){
							theReferredOid = reqRes.getOid();
							className = reqRes.getClass().getSimpleName();
							ok = true;
							webAPSEEObj = webAPSEEObjDAO.retrieveWebAPSEEObject(theReferredOid, className);
						}
					}
				}
				if(ok){
					if(webAPSEEObj==null){
						graphicCoord = updateGraphicCoordinate(webAPSEENodePosition, new GraphicCoordinate(), processIdent);
						graphicCoord = (GraphicCoordinate) grapDAO.save(graphicCoord);
						webAPSEEObj = new WebAPSEEObject(theReferredOid, className, graphicCoord);
						System.out.println("caiu no ok"+webAPSEEObj.getTheReferredOid());
						webAPSEEObjDAO.save(webAPSEEObj);
						
						
					}else{
						System.out.print("caiu no else: ");
					System.out.println(webAPSEEObj.getClassName().equals("RequiredResource"));
					//	if(webAPSEEObj.getClassName().equals("RequiredResource")) {
						System.out.println(processIdent);
							System.out.println(webAPSEENodePosition);
							System.out.println(webAPSEEObj.getTheGraphicCoordinate().getX());
							System.out.println(webAPSEEObj.getTheGraphicCoordinate().getY());
						//}
						graphicCoord = updateGraphicCoordinate(webAPSEENodePosition, webAPSEEObj.getTheGraphicCoordinate(), processIdent);
						grapDAO.update(graphicCoord);
					}
				}
			}
			
		} catch (WebapseeException e) {
			e.printStackTrace();
		}
	}

	private GraphicCoordinate updateGraphicCoordinate(WebAPSEENodePosition webAPSEENodePosition, GraphicCoordinate coord, String process) {
		
		coord.setX(webAPSEENodePosition.getX());
		coord.setY(webAPSEENodePosition.getY());
		coord.setVisible(webAPSEENodePosition.isVisible());
		coord.setTheProcess(process);
		
		return coord;
	}
	
	private int nodePositionType(int nodeTypeFromFlex) {
		typesToConvert = new HashMap<Integer, Integer>();
		typesToConvert.put(1, WebAPSEENodePosition.REQAGENTNODE);
		typesToConvert.put(2, WebAPSEENodePosition.ARTIFACTCONNODE);
		typesToConvert.put(3, WebAPSEENodePosition.REQRESOURCENODE);
		typesToConvert.put(4, WebAPSEENodePosition.BRANCHNODE);
		typesToConvert.put(5, WebAPSEENodePosition.JOINNODE);
		typesToConvert.put(6, WebAPSEENodePosition.ACTIVITYNODE);
		typesToConvert.put(7, WebAPSEENodePosition.ACTIVITYNODE);
		typesToConvert.put(8, WebAPSEENodePosition.REQGROUPNODE);
		
		return typesToConvert.get(nodeTypeFromFlex);
	}
	
	@Override
	public String copyActivity(String act_id, String level_to_copy) {

		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_to_copy, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		procDAO.getPersistenceContext().getTransaction().begin();

		Object proc;
		proc = procDAO.retrieveBySecondaryKey(process_id);

		if (proc == null)
			try {
				throw new DAOException(
						Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound"));
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;

		decDAO.getPersistenceContext().getTransaction().begin();

		Decomposed actDecomposed = null; // it is used only if the new activity
											// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			dec = decDAO.retrieveBySecondaryKey(currentModel);

			if (dec == null)
				try {
					throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
							+ Messages.getString("facades.DynamicModeling.DaoExcNotFound"));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		Object act;
		act = actDAO.retrieveBySecondaryKey(act_id);

		if (act == null)
			try {
				throw new DAOException(
						Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound"));
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //$NON-NLS-1$ //$NON-NLS-2$

		Activity activity = (Activity) act;

		// End Checks for the parameters

		// Now we start the implementation of the rules

		// Getting all activity idents from database
		this.initHashtable(pmodel);

		String processIdent = process.getIdent();
		String processState = process.getPState();
		if (processIdent.equals(level_to_copy)) {

			if (processState.equals(Process.NOT_STARTED) || processState.equals(Process.ENACTING)) {

				String ProcessModelState = pmodel.getPmState();
				if (!(ProcessModelState.equals(ProcessModel.CANCELED) || ProcessModelState.equals(ProcessModel.FAILED) || ProcessModelState
						.equals(ProcessModel.FINISHED))) {

					Hashtable<String, String> coordinates = new Hashtable<String, String>();

					Activity clone = null;
					try {
						clone = this.cloneActivity(activity, level_to_copy, coordinates);
					} catch (DAOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Rule
																								// C1.1
					pmodel.insertIntoTheActivity(clone);

					if (clone instanceof Decomposed) {
						// CopyProcess.saveCoordinatesToCopyActivity(
						// activity.getIdent(), clone.getIdent(), process,
						// currentSession );
						if (processState.equals(Process.ENACTING)) {
							try {
								this.enactmentLocal.createTasks(((Decomposed) clone).getTheReferedProcessModel());
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								this.enactmentLocal.searchForFiredConnections(pmodel.getOid(), "Rule C1.1");
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								this.enactmentLocal.searchForReadyActivities(pmodel.getOid());
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								this.enactmentLocal.determineProcessModelStates(pmodel);
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else if (processState.equals(Process.ENACTING) && clone instanceof Normal) {
						try {
							this.enactmentLocal.createTasksNormal((Normal) clone);
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.searchForFiredConnections(pmodel.getOid(), "Rule C1.1");
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.searchForReadyActivities(pmodel.getOid());
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.determineProcessModelStates(pmodel);
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					// Persistence Operations
					procDAO.update(process);
					procDAO.getPersistenceContext().getTransaction().commit();
					this.activitiesTable.clear();

//					this.saveCoordinates(coordinates);

					return clone.getIdent();
				} else {
					this.activitiesTable.clear();

					// BaseDAO.closeSession(currentSession);
					try {
						throw new ModelingException("Process Model not ready to receive copies. It has already been concluded!");
					} catch (ModelingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				this.activitiesTable.clear();

				// BaseDAO.closeSession(currentSession);
				try {
					throw new ModelingException("Process " + process_id + " not ready to receive copies. It has already been concluded!");
				} catch (ModelingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (actDecomposed != null) { // Rule C1.2 and C1.3

			String decomposedState = pmodel.getPmState();
			if (!(decomposedState.equals(ProcessModel.CANCELED) || decomposedState.equals(ProcessModel.FAILED) || decomposedState
					.equals(ProcessModel.FINISHED))) {

				Hashtable<String, String> coordinates = new Hashtable<String, String>();

				Activity clone = null;
				try {
					clone = this.cloneActivity(activity, level_to_copy, coordinates);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pmodel.insertIntoTheActivity(clone);

				if (clone instanceof Decomposed) {
					// CopyProcess.saveCoordinatesToCopyActivity(
					// activity.getIdent(), clone.getIdent(), process,
					// currentSession );
					if (processState.equals(Process.ENACTING)) {
						try {
							this.enactmentLocal.createTasks(((Decomposed) clone).getTheReferedProcessModel());
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.searchForFiredConnections(pmodel.getOid(), "Rule C1.2");
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.searchForReadyActivities(pmodel.getOid());
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							this.enactmentLocal.determineProcessModelStates(pmodel);
						} catch (WebapseeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (processState.equals(Process.ENACTING) && clone instanceof Normal) {
					try {
						this.enactmentLocal.createTasksNormal((Normal) clone);
					} catch (WebapseeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						this.enactmentLocal.searchForFiredConnections(pmodel.getOid(), "Rule C1.2");
					} catch (WebapseeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						this.enactmentLocal.searchForReadyActivities(pmodel.getOid());
					} catch (WebapseeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						this.enactmentLocal.determineProcessModelStates(pmodel);
					} catch (WebapseeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// Persistence Operations
				procDAO.update(process);
				procDAO.getPersistenceContext().getTransaction().commit();
				this.activitiesTable.clear();
				
//				this.saveCoordinates(coordinates);
				
				return clone.getIdent();
			} else {
				this.activitiesTable.clear();
				try {
					throw new ModelingException("Process Model not ready to receive copies. It has already been concluded!");
				} catch (ModelingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			this.activitiesTable.clear();
			try {
				throw new ModelingException("Process model target to copy not found!");
			} catch (ModelingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return processState;

		// Close All Transactions
	}

	@Override
	public void copyActivities(String[] act_ids, String[] cons_ids, String level_to_copy){
		// Checks for the parameters

		StringTokenizer st = new StringTokenizer(level_to_copy, "."); //$NON-NLS-1$
		String process_id = st.nextToken();

		Object proc;
		proc = procDAO.retrieveBySecondaryKey(process_id);

		if (proc == null)
			try {
				throw new DAOException(
						Messages.getString("facades.DynamicModeling.DaoExcProcess") + process_id + Messages.getString("facades.DynamicModeling.DaoExc_NotFound"));
			} catch (DAOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //$NON-NLS-1$ //$NON-NLS-2$

		Process process = (Process) proc;

		ProcessModel pmodel = null;
		
		Decomposed actDecomposed = null; // it is used only if the new activity
											// Is not in the root process model.
		if (st.hasMoreTokens()) {
			String currentModel = process_id;
			while (st.hasMoreTokens()) {
				currentModel += "." + st.nextToken(); //$NON-NLS-1$
			}
			Object dec = null;
			dec = decDAO.retrieveBySecondaryKey(currentModel);

			if (dec == null)
				try {
					throw new DAOException(Messages.getString("facades.DynamicModeling.DaoExcDecomActv") + currentModel //$NON-NLS-1$
							+ Messages.getString("facades.DynamicModeling.DaoExcNotFound"));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //$NON-NLS-1$

			actDecomposed = (Decomposed) dec;

			pmodel = actDecomposed.getTheReferedProcessModel();
		} else {
			pmodel = process.getTheProcessModel();
		}

		Activity[] acts = new Activity[act_ids.length];
		Collection<Activity> activities = new HashSet<Activity>();

		for (int i = 0; i < act_ids.length; i++) {
			String act_id = act_ids[i];
			acts[i] = (Activity) actDAO.retrieveBySecondaryKey(act_id);

			if (acts[i] == null) {
				
				try {
					throw new DAOException(
							Messages.getString("facades.DynamicModeling.ModelingExcActv") + act_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound"));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //$NON-NLS-1$ //$NON-NLS-2$
			}
			activities.add(acts[i]);
		}
		activities.remove(null);

		Connection[] cons = new Connection[cons_ids.length];
		Collection<Connection> connections = new HashSet<Connection>();

		for (int i = 0; i < cons_ids.length; i++) {
			String con_id = cons_ids[i];
			cons[i] = (Connection) conDAO.retrieveBySecondaryKey(con_id);

			if (cons[i] == null) {
				
				try {
					throw new DAOException(
							Messages.getString("facades.DynamicModeling.ModelingExcActv") + con_id + Messages.getString("facades.DynamicModeling.DaoExcNotFound"));
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //$NON-NLS-1$ //$NON-NLS-2$
			}
			connections.add(cons[i]);
		}
		connections.remove(null);

		// End Checks for the parameters

		// Now we start the implementation of the rules

		// Getting all activity idents from database
		this.initHashtable(pmodel);

		String processIdent = process.getIdent();
		if (processIdent.equals(level_to_copy)) {
			String processState = process.getPState();

			if (processState.equals(Process.NOT_STARTED) || processState.equals(Process.ENACTING)) {

				String ProcessModelState = pmodel.getPmState();
				if (!(ProcessModelState.equals(ProcessModel.CANCELED) || ProcessModelState.equals(ProcessModel.FAILED) || ProcessModelState
						.equals(ProcessModel.FINISHED))) {

					// TODO Adapt for new coordinates model
					// Collection<Activity> newActivities =
					// this.copyActivitiesOnProcessModel(activities,
					// level_to_copy, currentSession).values();
					// pmodel.insertIntoTheActivity(newActivities);

					// Hashtable activitiesTable =
					// this.generateActivitiesTable(newActivities);

					Collection<Connection> simples = new HashSet<Connection>();

					Iterator iterActivities = activities.iterator();
					while (iterActivities.hasNext()) {
						Activity activity = (Activity) iterActivities.next();
						simples.addAll(this.getSimpleConnectionsToCopyFromActivity(activity, activities));
					}

					connections.addAll(simples);

					// TODO Adapt for new coordinates model
					// Collection newConnections =
					// this.copyConnectionsOnProcessModel(connections,
					// activitiesTable, level_to_copy);
					// pmodel.insertIntoTheConnection(newConnections);

					// Peristence Operations
					procDAO.update(process);
					this.activitiesTable.clear();
				} else {
					
					try {
						throw new ModelingException("Process Model not ready to receive copies. It has already been concluded!");
					} catch (ModelingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				
				try {
					throw new ModelingException("Process " + process_id + " not ready to receive copies. It has already been concluded!");
				} catch (ModelingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (actDecomposed != null) { // Rule C1.2 and C1.3

			String decomposedState = pmodel.getPmState();
			if (!(decomposedState.equals(ProcessModel.CANCELED) || decomposedState.equals(ProcessModel.FAILED) || decomposedState
					.equals(ProcessModel.FINISHED))) {
				// TODO Adapt for new coordinates model
				// Collection<Activity> newActivities =
				// this.copyActivitiesOnProcessModel(activities, level_to_copy,
				// currentSession).values();
				// pmodel.insertIntoTheActivity(newActivities);

				// Hashtable activitiesTable =
				// this.generateActivitiesTable(newActivities);

				Collection<Connection> simples = new HashSet<Connection>();

				Iterator iterActivities = activities.iterator();
				while (iterActivities.hasNext()) {
					Activity activity = (Activity) iterActivities.next();
					simples.addAll(this.getSimpleConnectionsToCopyFromActivity(activity, activities));
				}

				connections.addAll(simples);

				// TODO Adapt for new coordinates model
				// Collection newConnections =
				// this.copyConnectionsOnProcessModel(connections,
				// activitiesTable, level_to_copy);
				// pmodel.insertIntoTheConnection(newConnections);

				// Peristence Operations
				procDAO.update(process);
				this.activitiesTable.clear();
			} else {
				
				try {
					throw new ModelingException("Process Model not ready to receive copies. It has already been concluded!");
				} catch (ModelingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			
			try {
				throw new ModelingException("Process model target to copy not found!");
			} catch (ModelingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void calculateUtilsDays(long new_begin_date, long new_end_date,CalendarDTO replainDates){
		System.out.print("caiu no recalcula" +new_begin_date+" "+new_end_date);
		CalendarDTO newDates = new CalendarDTO();
		newDates = filtraDatas(replainDates);
		int inicial = 0;
		
		ArrayList<Date> notWorkingDays = new  ArrayList<>();
				
		Date plannedBegin = new Date(new_begin_date);
		Date plannedEnd = new Date(new_end_date);

		SimpleDateFormat simple =  new SimpleDateFormat("MMM dd HH:mm:ss yyyy");
		
		for (int i = 0; i < newDates.getNotWorkingDays().size(); i++) {
			try {
				String s[] = newDates.getNotWorkingDays().get(i).split(" ");
				Date d =  new Date();
				 d = simple.parse(s[1]+" "+s[2]+" "+s[3]+" "+s[5]);
				notWorkingDays.add(new Date((d).getTime()));
				System.out.println("not" +notWorkingDays.get(i));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.print("inicio" +plannedBegin+" "+plannedEnd+"array"+replainDates.getNotWorkingDays().get(0));
		
		newBeginDate = new Date();
		newEndDate = new Date();
		
		Date end = new Date();
		
		for (int i = 0; i < notWorkingDays.size(); i++) {
		
			if (isSunday(plannedBegin) == true) {
            
				plannedBegin = new Date(plannedBegin.getTime() + MILLIS_PER_DAY);
                
				System.out.println("inicio "+plannedBegin);
            } 
			else if (isSaturday(plannedBegin) == true) {
            
				plannedBegin = new Date(plannedBegin.getTime() + (2 * MILLIS_PER_DAY));
                System.out.println("fim sabado"+plannedBegin);
            
			}

            if (isSunday(plannedEnd) == true) {
            
            	plannedEnd = new Date(plannedEnd.getTime() + MILLIS_PER_DAY);
            } 
            else if (isSaturday(plannedEnd) == true) {
            
            	plannedEnd = new Date(plannedEnd.getTime() + (2 * MILLIS_PER_DAY));
            }
			
			
            //começa o paranauê
            if (plannedBegin.after(notWorkingDays.get(i)) || plannedEnd.before(notWorkingDays.get(i))) {
            System.out.println("caiu no if"+notWorkingDays.get(i));
            newBeginDate = plannedBegin;
    		newEndDate = plannedEnd;
            }else if (plannedBegin.before(notWorkingDays.get(i)) && plannedEnd.after(notWorkingDays.get(i))){
				System.out.println("caiu no else");
            	 if (isFriday(notWorkingDays.get(i)) == true) {
                     System.out.println("caiu na sexta");
                     inicial = inicial + 3;

                 } else {
                     if (isSaturday(notWorkingDays.get(i)) == true) {
                         System.out.println("caiu no sabado");
                         inicial = inicial + 2;
                         //   begin = new Date(plannedBegin.getTime() + (2 * MILLIS_PER_DAY));
                         //  end = new Date(plannedEnd.getTime() + (2 * MILLIS_PER_DAY));

                     } else {
                         System.out.println("dia normal");
                         inicial = inicial + 1;
                         //   begin = new Date(begin.getTime()+plannedBegin.getTime() + MILLIS_PER_DAY);
                         //  end = new Date (plannedEnd.getTime() + (MILLIS_PER_DAY));

                     }
			}
            	 end = new Date(plannedEnd.getTime() + (inicial * MILLIS_PER_DAY));
				
            	 newEndDate =  end;
				newBeginDate = plannedBegin;
				System.out.println("nova data inicial parcial"+newBeginDate+" nniva data final parcial"+newEndDate);
				
				//verifica se a nova data final é sabado ou domingo
	            if (isSunday(newEndDate) == true) {
	                newEndDate = new Date(end.getTime() + MILLIS_PER_DAY);

	                for (int i1 = 0; i1 < notWorkingDays.size(); i1++) {
	                    //verifica se a próxima data é um dia não trabalhado
	                    if (newEndDate.before(notWorkingDays.get(i1))) {
	                    } else if (newEndDate.compareTo(notWorkingDays.get(i1))==0) {
	                        System.out.println("é igual");
	                        newEndDate = new Date(newEndDate.getTime() + MILLIS_PER_DAY);
	                        
	                    }
	                }

	            } else if (isSaturday(newEndDate) == true) {
	                newEndDate = new Date(end.getTime() + (2 * MILLIS_PER_DAY));

	                for (int i1 = 0; i1 < notWorkingDays.size(); i1++) {
	                    if (newEndDate.before(notWorkingDays.get(i1))) {
	                    } else if (newEndDate.compareTo(notWorkingDays.get(i1))==0) {
	                        System.out.println("é igual");
	                        newEndDate = new Date(newEndDate.getTime() + MILLIS_PER_DAY);
	                        
	                    }
	                }
	            }
			}
		}
		
		System.out.println("nova data inicial "+newBeginDate+" nniva data final "+newEndDate);
		
		
		
	}
	
	@Override
	public void replanningDates(String act_id, long new_begin_date, long new_end_date, CalendarDTO replanningDates) {
 
		 
		Normal normal = new Normal();
		Decomposed decomposed =  new Decomposed();
		
		Object tipo = actDAO.retrieveBySecondaryKey(act_id);
	if(tipo instanceof Normal){
		normal =(Normal) tipo;
		
	}else{
		
		decomposed =(Decomposed) tipo;
	}
		// Implementation of the rules

		EnactionDescription enact = normal.getTheEnactionDescription();
		String state = enact.getState();

		if (!(state.equals(Plain.FINISHED) || state.equals(Plain.FAILED) || state.equals(Plain.CANCELED))) {

			Date plannedBegin = normal.getPlannedBegin();
			Date plannedEnd = normal.getPlannedEnd();

			plannedBegin = new Date(new_begin_date);
			plannedBegin = this.moveFromWeekendToMonday(plannedBegin);

			plannedEnd = new Date(new_end_date);
			plannedEnd = this.moveFromWeekendToMonday(plannedEnd);

		calculateUtilsDays(new_begin_date,new_end_date,replanningDates);
			//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto
			
			normal.setPlannedBegin(newBeginDate);
			normal.setPlannedEnd(newEndDate);

			long planEnd = plannedEnd.getTime();

			// default value (equals to planned end) if plannedBegin is null
			long planBegin = plannedEnd.getTime();

			if (plannedBegin != null) {
				planBegin = plannedBegin.getTime();
			}

			long newHowLong = this.businessDays(planBegin, planEnd);

			normal.setHowLong(new Float(Math.round(newHowLong)));

			this.propagateReplanning(normal, normal,replanningDates);

			// Propagate for the upper levels...
			Decomposed upperDecomposed = normal.getTheProcessModel().getTheDecomposed();
			this.upperPropagation(upperDecomposed, normal,replanningDates);

			actDAO.update(normal);
			
			
			
			this.lastValid = null;
		} else {
			this.lastValid = null;
			
			try {
				throw new ModelingException(Messages.getString("facades.DynamicModeling.ReplanningFinishedActivity"));
			} catch (ModelingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * AUXILIAR METHODS
	 * 
	 * @throws DAOException
	 */

	private Activity cloneActivity(Activity activity, String level_to, Hashtable<String, String> coordinates) throws DAOException {

		Activity clone = null;
		if (activity != null) {
			if (activity instanceof Normal) {
				Normal normal = (Normal) activity;
				clone = new Normal();
				String newIdent = this.generateNewIdent(activity.getIdent(), level_to, this.allActs);
				clone.setIdent(newIdent);
				this.addCoordinate(activity.getOid(), activity.getClass().getSimpleName(), WebAPSEEObject.ACTIVITY + "+" + newIdent, coordinates);
				this.activitiesTable.put(normal.getIdent(), clone);
				this.copyNormalProperties((Normal) clone, normal, coordinates);
			} else if (activity instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) activity;
				clone = new Decomposed();
				String newIdent = this.generateNewIdent(activity.getIdent(), level_to, this.allActs);
				clone.setIdent(newIdent);
				this.addCoordinate(activity.getOid(), activity.getClass().getSimpleName(), WebAPSEEObject.ACTIVITY + "+" + newIdent, coordinates);
				this.activitiesTable.put(decomposed.getIdent(), clone);
				this.copyDecomposedProperties((Decomposed) clone, decomposed, coordinates);
			} else if (activity instanceof Automatic) {
				Automatic automatic = (Automatic) activity;
				clone = new Automatic();
				String newIdent = this.generateNewIdent(activity.getIdent(), level_to, this.allActs);
				clone.setIdent(newIdent);
				this.addCoordinate(activity.getOid(), activity.getClass().getSimpleName(), WebAPSEEObject.ACTIVITY + "+" + newIdent, coordinates);
				this.activitiesTable.put(automatic.getIdent(), clone);
				this.copyAutomaticProperties((Automatic) clone, automatic);
			}

			clone.insertIntoTheActivityType(activity.getTheActivityType());
		}
		return clone;
	}

	private void addCoordinate(Integer theReferredOid, String className, String newObjRef, Hashtable<String, String> coordinates) throws DAOException {
		String currentObjKey = className + ":" + String.valueOf(theReferredOid);
		coordinates.put(currentObjKey, newObjRef);
	}

	/**
	 * @param destinationNormal
	 * @param sourceNormal
	 * @throws DAOException
	 */
	private void copyNormalProperties(Normal destinationNormal, Normal sourceNormal, Hashtable<String, String> coordinates) throws DAOException {
		// simple attributes
		destinationNormal.setHowLong(sourceNormal.getHowLong());
		destinationNormal.setHowLongUnit(sourceNormal.getHowLongUnit());
		destinationNormal.setName(sourceNormal.getName());
		destinationNormal.setScript(sourceNormal.getScript());

		destinationNormal.setStaticOk(sourceNormal.getStaticOk());
		destinationNormal.setRequirements(sourceNormal.getRequirements());

		destinationNormal.setDelegable(sourceNormal.getDelegable());
		destinationNormal.setAutoAllocable(sourceNormal.getAutoAllocable());

		// about reservations
		Collection<Reservation> newReservations = null;
		newReservations = copyReservations(sourceNormal.getTheReservation(), destinationNormal);
		destinationNormal.setTheReservation(newReservations);
		// about involved artifacts
		// involved to
		Collection<InvolvedArtifacts> newInvolvedArtifactsToNormal = null;
		newInvolvedArtifactsToNormal = copyInvolvedArtifacts(sourceNormal.getInvolvedArtifactToNormal(), destinationNormal, TO_INVOLVED);
		destinationNormal.setInvolvedArtifactToNormal(newInvolvedArtifactsToNormal);
		// involved from
		Collection<InvolvedArtifacts> newInvolvedArtifactsFromNormal = null;
		newInvolvedArtifactsFromNormal = copyInvolvedArtifacts(sourceNormal.getInvolvedArtifactFromNormal(), destinationNormal, FROM_INVOLVED);
		destinationNormal.setInvolvedArtifactFromNormal(newInvolvedArtifactsFromNormal);
		// about requied Resources
		Collection<RequiredResource> newRequiredResources = null;
		newRequiredResources = copyRequiredResources(sourceNormal.getTheRequiredResource(), destinationNormal, coordinates);
		destinationNormal.setTheRequiredResource(newRequiredResources);
		// about required People
		Collection<RequiredPeople> newRequiredPeople = null;
		newRequiredPeople = copyRequiredPeople(sourceNormal.getTheRequiredPeople(), destinationNormal, coordinates);
		destinationNormal.setTheRequiredPeople(newRequiredPeople);
	}

	/**
	 * @param theReservation
	 *            collection of current reservations
	 * @param destinationNormal
	 *            normal activity that will receive the new collection
	 * @return the new collection of reservations ... to be inserted into the
	 *         new Normal activity
	 */
	private Collection<Reservation> copyReservations(Collection theReservation, Normal destinationNormal) {
		Collection<Reservation> newInvolvedReservations = new HashSet<Reservation>();
		for (Iterator<Reservation> reservationsIterator = theReservation.iterator(); reservationsIterator.hasNext();) {
			Reservation currentReservation = reservationsIterator.next();
			if (currentReservation != null) {
				Reservation newReservation = new Reservation();
				newReservation.insertIntoTheNormal(destinationNormal);
				if (currentReservation.getTheExclusive() != null) {
					newReservation.insertIntoTheExclusive(currentReservation.getTheExclusive());
				}
				// dates
				newReservation.setFrom(currentReservation.getFrom());
				newReservation.setTo(currentReservation.getTo());
				// add to Main Collection
				newInvolvedReservations.add(newReservation);
			}// END IF

		}// END FOR
			// after all, retrieve correct collection
		return newInvolvedReservations;
	}// end method

	// private constants, to reuse the method to copy involved artifacts
	private static int TO_INVOLVED = 0, FROM_INVOLVED = 1;

	private Map<Integer, Integer> typesToConvert;

	
	private Collection<InvolvedArtifacts> copyInvolvedArtifacts(Collection<InvolvedArtifacts> currentInvolvedArtifacts, Normal newNormalReference,
			int kindRelationship) {

		Collection<InvolvedArtifacts> newInvolvedArtifacts = new HashSet<InvolvedArtifacts>();
		for (Iterator<InvolvedArtifacts> involvedIterator = currentInvolvedArtifacts.iterator(); involvedIterator.hasNext();) {
			InvolvedArtifacts currentInvolved = involvedIterator.next();
			if (currentInvolved != null) {
				InvolvedArtifacts newInvolved = new InvolvedArtifacts();
				if (currentInvolved.getTheArtifactType() != null) {
					newInvolved.insertIntoTheArtifactType(currentInvolved.getTheArtifactType());
				}
				Artifact theArtifact = currentInvolved.getTheArtifact();
				if (theArtifact != null) {
					newInvolved.insertIntoTheArtifact(theArtifact);
				}
				if (kindRelationship == TO_INVOLVED) {
					newInvolved.insertIntoInInvolvedArtifacts(newNormalReference);
				} else if (kindRelationship == FROM_INVOLVED) {
					newInvolved.insertIntoOutInvolvedArtifacts(newNormalReference);
				}
				// add to Main Collection
				newInvolvedArtifacts.add(newInvolved);

			}// END IF
		}// END FOR
			// after all, retrieve correct collection
		return newInvolvedArtifacts;
	}// end method

	
	private Collection<RequiredResource> copyRequiredResources(Collection<RequiredResource> theRequiredResource, Normal destinationNormal,
			Hashtable<String, String> coordinates) throws DAOException {
		Collection<RequiredResource> newRequiredResources = new HashSet<RequiredResource>();

		String coordinateKey = null;

		for (Iterator<RequiredResource> requiredIterator = theRequiredResource.iterator(); requiredIterator.hasNext();) {

			RequiredResource currentReqResource = requiredIterator.next();
			if (currentReqResource != null) {
				RequiredResource newRequiredResource = new RequiredResource();

				if (currentReqResource.getTheResourceType() != null) {
					newRequiredResource.insertIntoTheResourceType(currentReqResource.getTheResourceType());
					coordinateKey = currentReqResource.getTheResourceType().getIdent();
				}

				if (currentReqResource.getTheResource() != null) {
					newRequiredResource.insertIntoTheResource(currentReqResource.getTheResource());
					newRequiredResource.setAmountNeeded(currentReqResource.getAmountNeeded());
					coordinateKey = coordinateKey + ":" + currentReqResource.getTheResource().getIdent();
				}

				newRequiredResource.insertIntoTheNormal(destinationNormal);

				coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();
				this.addCoordinate(currentReqResource.getOid(), currentReqResource.getClass().getSimpleName(), WebAPSEEObject.REQ_RESOURCE + "+"
						+ coordinateKey, coordinates);
				coordinateKey = null;

				// add to Main Collection
				newRequiredResources.add(newRequiredResource);

			}// END IF
		}// END FOR
			// after all, retrieve correct collection
		return newRequiredResources;
	}// end method

	
	private Collection<RequiredPeople> copyRequiredPeople(Collection theRequiredPeople, Normal destinationNormal,
			Hashtable<String, String> coordinates) throws DAOException {
		Collection<RequiredPeople> newRequiredPeoples = new HashSet<RequiredPeople>();

		String coordinateKey = null;

		for (Iterator<RequiredPeople> requiredIterator = theRequiredPeople.iterator(); requiredIterator.hasNext();) {

			RequiredPeople currentReqPeople = requiredIterator.next();
			if (currentReqPeople != null) {
				RequiredPeople newRequiredPeople = null;

				if (currentReqPeople instanceof ReqAgent) {

					newRequiredPeople = new ReqAgent();

					if (((ReqAgent) currentReqPeople).getTheRole() != null) {
						((ReqAgent) newRequiredPeople).insertIntoTheRole(((ReqAgent) currentReqPeople).getTheRole());
						coordinateKey = ((ReqAgent) newRequiredPeople).getTheRole().getIdent();
					}

					if (((ReqAgent) currentReqPeople).getTheAgent() != null) {
						((ReqAgent) newRequiredPeople).insertIntoTheAgent(((ReqAgent) currentReqPeople).getTheAgent());
						coordinateKey = coordinateKey + ":" + ((ReqAgent) newRequiredPeople).getTheAgent().getIdent();
					}
					// about ReqAgentRequiresAbility
					Collection<ReqAgentRequiresAbility> newReqAgReqAbility = null;
					newReqAgReqAbility = copyReqAgentReqAbility(((ReqAgent) currentReqPeople).getTheReqAgentRequiresAbility(),
							((ReqAgent) newRequiredPeople));
					((ReqAgent) newRequiredPeople).setTheReqAgentRequiresAbility(newReqAgReqAbility);

					coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();
					this.addCoordinate(((ReqAgent) currentReqPeople).getOid(), ((ReqAgent) currentReqPeople).getClass().getSimpleName(),
							WebAPSEEObject.REQ_AGENT + "+" + coordinateKey, coordinates);
					coordinateKey = null;

				} else if (currentReqPeople instanceof ReqGroup) {

					newRequiredPeople = new ReqGroup();

					if (((ReqGroup) currentReqPeople).getTheGroupType() != null) {
						((ReqGroup) newRequiredPeople).insertIntoTheGroupType(((ReqGroup) currentReqPeople).getTheGroupType());
						coordinateKey = ((ReqGroup) newRequiredPeople).getTheGroupType().getIdent();
					}

					if (((ReqGroup) currentReqPeople).getTheGroup() != null) {
						((ReqGroup) newRequiredPeople).insertIntoTheGroup(((ReqGroup) currentReqPeople).getTheGroup());
						coordinateKey = coordinateKey + ":" + ((ReqGroup) newRequiredPeople).getTheGroup().getIdent();
					}

					coordinateKey = coordinateKey + ":" + destinationNormal.getIdent();
					this.addCoordinate(((ReqGroup) currentReqPeople).getOid(), ((ReqGroup) currentReqPeople).getClass().getSimpleName(),
							WebAPSEEObject.REQ_GROUP + "+" + coordinateKey, coordinates);
					coordinateKey = null;

				}// end if

				// the common attribute normal activity
				newRequiredPeople.insertIntoTheNormal(destinationNormal);

				// add to main collection
				newRequiredPeoples.add(newRequiredPeople);

			}// END IF

		}// END FOR

		// after all, retrieve correct collection
		return newRequiredPeoples;
	}// end method

	
	private Collection<ReqAgentRequiresAbility> copyReqAgentReqAbility(Collection theReqAgentRequiresAbility, ReqAgent newReqAgent) {
		Collection<ReqAgentRequiresAbility> newReqAgReqAbilities = new HashSet<ReqAgentRequiresAbility>();

		for (Iterator<ReqAgentRequiresAbility> requiredIterator = theReqAgentRequiresAbility.iterator(); requiredIterator.hasNext();) {

			ReqAgentRequiresAbility currentReqAgReqAbility = requiredIterator.next();
			if (currentReqAgReqAbility != null) {
				ReqAgentRequiresAbility newReqAgReqAb = new ReqAgentRequiresAbility();

				if (currentReqAgReqAbility.getTheAbility() != null) {
					newReqAgReqAb.insertIntoTheAbility(currentReqAgReqAbility.getTheAbility());
				}

				newReqAgReqAb.setDegree(currentReqAgReqAbility.getDegree());
				newReqAgReqAb.insertIntoTheReqAgent(newReqAgent);

				// add to Main Collection
				newReqAgReqAbilities.add(newReqAgReqAb);

			}// END IF

		}// END FOR
			// after all, retrieve correct collection
		return newReqAgReqAbilities;
	}

	private void copyDecomposedProperties(Decomposed newDecomposed, Decomposed currentDecomposed, Hashtable<String, String> coordinates) {
		if (currentDecomposed.getTheActivityType() != null) {
			newDecomposed.setTheActivityType(currentDecomposed.getTheActivityType());
		}
		if (currentDecomposed.getName() != null) {
			newDecomposed.setName(currentDecomposed.getName());
		}
		// subProcessModel and call recursively
		// new Referred ProcessModel
		ProcessModel newReferedProcessModel = new ProcessModel();
		if (currentDecomposed.getTheReferedProcessModel() != null) {
			// recursive invocation ....
			coordinates.putAll(copyProcessModelData(currentDecomposed.getTheReferedProcessModel(), newReferedProcessModel, newDecomposed.getIdent()));
			newDecomposed.insertIntoTheReferedProcessModel(newReferedProcessModel);
		}

	}// END METHOD

	@Override
	public Hashtable<String, String> copyProcessModelData(ProcessModel oldProcessModel, ProcessModel newProcessModel, String level_to) {
		try {
			Collection<Activity> activities = oldProcessModel.getTheActivity();
			Collection<Connection> connections = oldProcessModel.getTheConnection();

			Hashtable<String, String> coordinates = new Hashtable<String, String>();

			Hashtable<String, Activity> newActivities = null;// ####################################
			newActivities = copyActivitiesOnProcessModel(activities, level_to, coordinates);
			newProcessModel.insertIntoTheActivity(newActivities.values());

			// Hashtable<String,Activity> activitiesTable =
			// generateActivitiesTable(newActivities);

			Collection<Connection> newConnections = null;// ##################################
			newConnections = copyConnectionsOnProcessModel(connections, newActivities, level_to, coordinates);
			newProcessModel.insertIntoTheConnection(newConnections);

			return coordinates;
		} catch (WebapseeException e) {
			e.printStackTrace();
		}
		return null;
	}// end method

	
	private Hashtable<String, Activity> copyActivitiesOnProcessModel(Collection<Activity> activities, String level_to,
			Hashtable<String, String> coordinates) throws DAOException {
		Hashtable<String, Activity> newActivities = new Hashtable<String, Activity>();

		for (Iterator<Activity> activityIterator = activities.iterator(); activityIterator.hasNext();) {
			Activity currentActivity = activityIterator.next();
			Activity newActivity = null;
			if (currentActivity != null) {

				String newActivityIdent = this.generateIdent(currentActivity.getIdent(), level_to);

				this.addCoordinate(currentActivity.getOid(), currentActivity.getClass().getSimpleName(), WebAPSEEObject.ACTIVITY + "+"
						+ newActivityIdent, coordinates);

				if (currentActivity instanceof Normal) {
					newActivity = new Normal();
					newActivity.setIdent(newActivityIdent);
					copyNormalProperties(((Normal) newActivity), ((Normal) currentActivity), coordinates);
				} else if (currentActivity instanceof Automatic) {
					newActivity = new Automatic();
					newActivity.setIdent(newActivityIdent);
					copyAutomaticProperties(((Automatic) newActivity), ((Automatic) currentActivity));
				} else if (currentActivity instanceof Decomposed) {
					newActivity = new Decomposed();
					newActivity.setIdent(newActivityIdent);
					copyDecomposedProperties(((Decomposed) newActivity), ((Decomposed) currentActivity), coordinates);
				}

				if (currentActivity.getTheActivityType() != null) {
					newActivity.insertIntoTheActivityType(currentActivity.getTheActivityType());
				}

				// add to main Collection
				newActivities.put(currentActivity.getIdent(), newActivity);
			}// end if !=null

		}// end for

		// after all , return the main Collection
		return newActivities;
	}

	
	/**
	 * @param newActivities
	 * @return
	 */
	private Hashtable<String, Activity> generateActivitiesTable(Collection<Activity> newActivities) {
		Hashtable<String, Activity> activitiesTable = new Hashtable<String, Activity>();
		for (Iterator<Activity> actIterator = newActivities.iterator(); actIterator.hasNext();) {
			Activity currentActivity = actIterator.next();
			activitiesTable.put(currentActivity.getIdent(), currentActivity);
		}
		return activitiesTable;
	}

	
	/**
	 * @param connections
	 * @param oldProcessIdent
	 * @param newProcessIdent
	 * @return
	 * @throws DAOException
	 */
	private Collection<Connection> copyConnectionsOnProcessModel(Collection<Connection> connections, Hashtable<String, Activity> activitiesTable,
			String level_to, Hashtable<String, String> coordinates) throws DAOException {
		// need to take care about all kind of connections

		Hashtable<String, Connection> connectionTable = new Hashtable<String, Connection>(1000, 1);

		HashSet<Connection> connectionsToResult = new HashSet<Connection>(1000);

		Collection<Connection> postProcessingCollection = new LinkedList<Connection>();

		for (Iterator<Connection> connectionsIterator = connections.iterator(); connectionsIterator.hasNext();) {
			Connection currentConnection = connectionsIterator.next();
			if (currentConnection != null) {
				Connection newConnection = null;
				String newConnectionIdent = this.generateIdent(currentConnection.getIdent(), level_to);
				if (currentConnection instanceof Sequence) {
					newConnection = new Sequence();
					if (((Sequence) currentConnection).getTheDependency() != null) {
						Dependency newDependency = new Dependency();
						newDependency.setKindDep(((Sequence) currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheSequence(((Sequence) newConnection));
					}// end if
						// about activities
						// ToActivity
					if (((Sequence) currentConnection).getToActivity() != null) {
						// new activity ident
						Activity newToAct = activitiesTable.get(((Sequence) currentConnection).getToActivity().getIdent());
						if (newToAct != null) {
							((Sequence) newConnection).insertIntoToActivity(newToAct);
						}// end if
					}// end if != null

					// FromActivity
					if (((Sequence) currentConnection).getFromActivity() != null) {
						// new activity ident
						Activity newFromAct = activitiesTable.get(((Sequence) currentConnection).getFromActivity().getIdent());
						if (newFromAct != null) {
							((Sequence) newConnection).insertIntoFromActivity(newFromAct);
						}// end if
					}// end if != null

					newConnection.setIdent(newConnectionIdent);
					this.addCoordinate(((Sequence) currentConnection).getOid(), ((Sequence) currentConnection).getClass().getSimpleName(),
							WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}// ########################
				else if (currentConnection instanceof Feedback) {
					newConnection = new Feedback();
					/*
					 * if( ((Feedback)currentConnection).getTheCondition()
					 * !=null){ Condition newCondition = new Condition();
					 * newCondition.setCond(
					 * ((Feedback)currentConnection).getTheCondition
					 * ().getCond());
					 * newCondition.insertIntoIsConditionOf(((Feedback
					 * )newConnection)); }
					 */// end if
						// about activities
						// ToActivity
					if (((Feedback) currentConnection).getToActivity() != null) {
						// new activity ident
						Activity newToAct = activitiesTable.get(((Feedback) currentConnection).getToActivity().getIdent());
						if (newToAct != null) {
							((Feedback) newConnection).insertIntoToActivity(newToAct);
						}// end if
					}// end if != null
						// FromActivity
					if (((Feedback) currentConnection).getFromActivity() != null) {
						// new activity ident
						Activity newFromAct = activitiesTable.get(((Feedback) currentConnection).getFromActivity().getIdent());
						if (newFromAct != null) {
							((Feedback) newConnection).insertIntoFromActivity(newFromAct);
						}// end if
					}// end if != null

					if (((Feedback) currentConnection).getTheCondition() != null) {
						((Feedback) newConnection).removeFromTheCondition();
						((Feedback) newConnection).insertIntoTheCondition(((Feedback) currentConnection).getTheCondition().createClone());
					}// end if condition != null

					newConnection.setIdent(newConnectionIdent);
					this.addCoordinate(((Feedback) currentConnection).getOid(), ((Feedback) currentConnection).getClass().getSimpleName(),
							WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}// ########################
				else if (currentConnection instanceof ArtifactCon) {
					newConnection = new ArtifactCon();
					if (((ArtifactCon) currentConnection).getTheArtifactType() != null) {
						((ArtifactCon) newConnection).insertIntoTheArtifactType(((ArtifactCon) currentConnection).getTheArtifactType());
					}

					Artifact theArtifact = ((ArtifactCon) currentConnection).getTheArtifact();
					if (theArtifact != null) {
						((ArtifactCon) newConnection).insertIntoTheArtifact(theArtifact);
					}

					// about activities
					// ToActivity
					Collection toActivities = ((ArtifactCon) currentConnection).getToActivity();
					for (Iterator<Activity> toActivityIterator = toActivities.iterator(); toActivityIterator.hasNext();) {
						Activity currentToAct = toActivityIterator.next();
						if (currentToAct != null) {
							Activity newToAct = activitiesTable.get(currentToAct.getIdent());
							if (newToAct != null) {
								((ArtifactCon) newConnection).insertIntoToActivity(newToAct);
							}// end if
						}// end if != null
					}// end for
						// FromActivity
					Collection fromActivities = ((ArtifactCon) currentConnection).getFromActivity();
					for (Iterator<Activity> fromActivityIterator = fromActivities.iterator(); fromActivityIterator.hasNext();) {
						Activity currentFromAct = fromActivityIterator.next();
						if (currentFromAct != null) {
							Activity newFromAct = activitiesTable.get(currentFromAct.getIdent());
							if (newFromAct != null) {
								((ArtifactCon) newConnection).insertIntoFromActivity(newFromAct);
							}// end if
						}// end if != null
					}// end for

					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);
					this.addCoordinate(((ArtifactCon) currentConnection).getOid(), ((ArtifactCon) currentConnection).getClass().getSimpleName(),
							WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}// ########################
				else if (currentConnection instanceof Branch) {

					// ########################
					if (currentConnection instanceof BranchAND) {
						newConnection = new BranchAND();
						// ToActivity
						Collection toActivities = ((BranchAND) currentConnection).getToActivity();
						for (Iterator<Activity> toActivityIterator = toActivities.iterator(); toActivityIterator.hasNext();) {
							Activity currentToAct = toActivityIterator.next();
							if (currentToAct != null) {
								String actIdent = currentToAct.getIdent();
								Activity newToAct = activitiesTable.get(actIdent);
								if (newToAct != null) {
									((BranchAND) newConnection).insertIntoToActivity(newToAct);
								}// end if
							}// end if != null
						}// end for

					}// ########################
					else if (currentConnection instanceof BranchCond) {
						newConnection = new BranchCond();

						((BranchCond) newConnection).setKindBranch(((BranchCond) currentConnection).getKindBranch());

						// ToActivity
						Collection toActivities = ((BranchCond) currentConnection).getTheBranchCondToActivity();
						for (Iterator<BranchCondToActivity> toActivityIterator = toActivities.iterator(); toActivityIterator.hasNext();) {
							BranchCondToActivity currentToAct = toActivityIterator.next();
							if (currentToAct != null) {
								BranchCondToActivity newBranchCondToAct = new BranchCondToActivity();

								if (currentToAct.getTheActivity() != null) {
									String actIdent = currentToAct.getTheActivity().getIdent();
									Activity newToAct = activitiesTable.get(actIdent);
									if (newToAct != null) {
										newToAct.insertIntoTheBranchCondToActivity(newBranchCondToAct);
									}// end if
								}// end if != null

								// about conditions
								if (currentToAct.getTheCondition() != null) {
									newBranchCondToAct.removeFromTheCondition();
									newBranchCondToAct.insertIntoTheCondition(currentToAct.getTheCondition().createClone());
								}// end if condition != null

								// add current element to newBranchCond object
								((BranchCond) newConnection).insertIntoTheBranchCondToActivity(newBranchCondToAct);
							}// end if != null
						}// end for

						// ###########

						// ((BranchCond)currentConnection).getKindBranch();

						// ##########

					}// end if common atribute for all branch connections
						// ((Branch)newConnection).setFired(((Branch)currentConnection).getFired());

					// about dependency
					if (((Branch) currentConnection).getTheDependency() != null) {
						Dependency newDependency = new Dependency();
						newDependency.setKindDep(((Branch) currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheMultipleCon(((Branch) newConnection));
					}// end if
						// about from activity
					if (((Branch) currentConnection).getFromActivity() != null) {
						String actIdent = ((Branch) currentConnection).getFromActivity().getIdent();
						Activity newFromAct = activitiesTable.get(actIdent);
						if (newFromAct != null) {
							((Branch) newConnection).insertIntoFromActivity(newFromAct);
						}// end if
					}// end if

					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);
					this.addCoordinate(((Branch) currentConnection).getOid(), ((Branch) currentConnection).getClass().getSimpleName(),
							WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);

				} else if (currentConnection instanceof Join) {
					newConnection = new Join();
					// simple attributes
					// ((Join)newConnection).setFired(
					// ((Join)currentConnection).getFired() );

					((Join) newConnection).setKindJoin(((Join) currentConnection).getKindJoin());

					// about dependency
					if (((Join) currentConnection).getTheDependency() != null) {
						Dependency newDependency = new Dependency();
						newDependency.setKindDep(((Join) currentConnection).getTheDependency().getKindDep());
						newDependency.insertIntoTheMultipleCon(((Join) newConnection));
					}// end if

					// About activities
					// ToActivity
					// About to Activity
					if (((Join) currentConnection).getToActivity() != null) {
						String actIdent = ((Join) currentConnection).getToActivity().getIdent();
						Activity newToAct = activitiesTable.get(actIdent);
						if (newToAct != null) {
							((Join) newConnection).insertIntoToActivity(newToAct);
						}// end if
					}// end if
						// FromActivity
					Collection fromActivities = ((Join) currentConnection).getFromActivity();
					for (Iterator<Activity> fromActivityIterator = fromActivities.iterator(); fromActivityIterator.hasNext();) {
						Activity currentFromAct = fromActivityIterator.next();
						if (currentFromAct != null) {
							String actIdent = currentFromAct.getIdent();
							Activity newFromAct = activitiesTable.get(actIdent);
							if (newFromAct != null) {
								((Join) newConnection).insertIntoFromActivity(newFromAct);
							}// end if
						}// end if != null
					}// end for
					newConnection.setIdent(newConnectionIdent);
					postProcessingCollection.add(currentConnection);
					this.addCoordinate(((Join) currentConnection).getOid(), ((Join) currentConnection).getClass().getSimpleName(),
							WebAPSEEObject.CONNECTION + "+" + newConnectionIdent, coordinates);
				}// end join processing

				// about conection type
				if (currentConnection.getTheConnectionType() != null) {
					newConnection.insertIntoTheConnectionType(currentConnection.getTheConnectionType());
				}// end if
				if (newConnection != null) {
					connectionTable.put(newConnection.getIdent(), newConnection);

					connectionsToResult.add(newConnection);
				}
			}// end if != null

		}// end for

		// After all processing, we need to connect
		// Because multiple connection have connection to another connections...
		// After all mapping we need again check connection relationships
		for (Iterator<Connection> postProcessingIterator = postProcessingCollection.iterator(); postProcessingIterator.hasNext();) {

			Connection currentPostConnection = postProcessingIterator.next();
			Connection alreadyCreatedConnection = connectionTable.get(currentPostConnection.getIdent());

			// ToMutlipleCon
			if (currentPostConnection instanceof ArtifactCon) {

				// To MultipleCon
				Collection toMultipleCon = ((ArtifactCon) currentPostConnection).getToMultipleCon();
				for (Iterator<MultipleCon> fromMultipleConIterator = toMultipleCon.iterator(); fromMultipleConIterator.hasNext();) {
					MultipleCon currentMultiple = fromMultipleConIterator.next();
					if (currentMultiple != null) {
						String multipleIdent = currentMultiple.getIdent();
						MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
						if (newFromMultipleCon != null) {
							((ArtifactCon) alreadyCreatedConnection).insertIntoToMultipleCon(newFromMultipleCon);
						}// end if
					}// end if != null
				}// end for

			}// end if artifactCon
			else if (currentPostConnection instanceof Branch) {
				// From MultipleConnection

				MultipleCon fromMultipleCon = ((Branch) currentPostConnection).getFromMultipleConnection();
				if (fromMultipleCon != null) {
					String multipleIdent = fromMultipleCon.getIdent();
					MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
					if (newFromMultipleCon != null) {
						((Branch) alreadyCreatedConnection).insertIntoFromMultipleConnection(newFromMultipleCon);
					}// end if
				}// end if

				if (currentPostConnection instanceof BranchAND) {
					Collection toMultipleCon = ((BranchAND) currentPostConnection).getToMultipleCon();
					for (Iterator<MultipleCon> toMultipleConIterator = toMultipleCon.iterator(); toMultipleConIterator.hasNext();) {
						MultipleCon currentMultiple = toMultipleConIterator.next();
						if (currentMultiple != null) {
							String multipleIdent = currentMultiple.getIdent();
							MultipleCon newFromMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
							if (newFromMultipleCon != null) {
								((BranchAND) alreadyCreatedConnection).insertIntoToMultipleCon(newFromMultipleCon);
							}// end if
						}// end if != null
					}// end for
				} else if (currentPostConnection instanceof BranchCond) {
					Collection toMultipleCon = ((BranchCond) currentPostConnection).getTheBranchCondToMultipleCon();
					for (Iterator<BranchCondToMultipleCon> toMultipleIterator = toMultipleCon.iterator(); toMultipleIterator.hasNext();) {
						BranchCondToMultipleCon currentToMult = toMultipleIterator.next();
						if (currentToMult != null) {
							BranchCondToMultipleCon newBranchCondToMult = new BranchCondToMultipleCon();
							if (currentToMult.getTheMultipleCon() != null) {
								String multipleIdent = currentToMult.getTheMultipleCon().getIdent();
								MultipleCon newToMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
								if (newToMultipleCon != null) {
									((BranchCondToMultipleCon) newBranchCondToMult).insertIntoTheMultipleCon(newToMultipleCon);
								}// end if
							}// end if != null
								// about conditions
								// newBranchCondToMult.setCondition(currentToMult.getCondition());
							((BranchCond) alreadyCreatedConnection).insertIntoTheBranchCondToMultipleCon(newBranchCondToMult);
						}// end if != null
					}// end for
				}// end if

			} else if (currentPostConnection instanceof Join) {
				// to MultipleCon
				MultipleCon toMultipleCon = ((Join) currentPostConnection).getToMultipleCon();
				if (toMultipleCon != null) {
					String multipleIdent = toMultipleCon.getIdent();
					MultipleCon newToMultipleCon = (MultipleCon) connectionTable.get(multipleIdent);
					if (newToMultipleCon != null) {
						((Join) alreadyCreatedConnection).insertIntoToMultipleCon(newToMultipleCon);
					}// end if
				}// end if
					// Don`t care about fromConnection...be cause all To are
					// already checked!
			}

		}// end ofr postProcessing

		return connectionsToResult;
	}

	/**
	 * Taken from processUtility.CopyProcess
	 */
	private void copyAutomaticProperties(Automatic newAutomatic, Automatic currentAutomatic) {

		// About Activity Type
		if (currentAutomatic.getTheActivityType() != null) {
			newAutomatic.insertIntoTheActivityType(currentAutomatic.getTheActivityType());
		}

		// set common attribute
		newAutomatic.setTheArtifact(currentAutomatic.getTheArtifact());

		// about parameters
		Collection<Parameters> newParameters = null;
		newParameters = copyAutomaticParameters(currentAutomatic.getTheParameters(), newAutomatic);
		newAutomatic.setTheParameters(newParameters);

		// about subroutine
		if (currentAutomatic.getTheSubroutine() != null) {
			Subroutine newSubRoutine = new Subroutine();
			if (currentAutomatic.getTheSubroutine().getTheArtifactType() != null) {
				newSubRoutine.insertIntoTheArtifactType(currentAutomatic.getTheSubroutine().getTheArtifactType());
			}
			newSubRoutine.insertIntoTheAutomatic(newAutomatic);

			// need to copy a new ToolParameter
			Collection<ToolParameters> newToolParameters = null;
			newToolParameters = copySubroutineToolParameters(currentAutomatic.getTheSubroutine().getTheToolParameters(), newSubRoutine);
			newSubRoutine.setTheToolParameters(newToolParameters);
		}
	}

	/**
	 * Taken from processUtility.CopyProcess
	 */
	private Collection<ToolParameters> copySubroutineToolParameters(Collection theToolParameters, Subroutine newSubRoutine) {
		Collection<ToolParameters> newParameters = new LinkedList<ToolParameters>();

		for (Iterator<ToolParameters> paramIterator = theToolParameters.iterator(); paramIterator.hasNext();) {
			ToolParameters currentParameter = paramIterator.next();
			if (currentParameter != null) {
				ToolParameters newParameter = null;
				newParameter = new ToolParameters();
				newParameter.insertIntoTheSubroutine(newSubRoutine);

				newParameter.setLabel(currentParameter.getLabel());
				newParameter.setSeparatorSymbol(currentParameter.getSeparatorSymbol());
				if (currentParameter.getTheArtifactType() != null) {
					newParameter.setTheArtifactType(currentParameter.getTheArtifactType());
				}
				if (currentParameter.getThePrimitiveType() != null) {
					newParameter.setThePrimitiveType(currentParameter.getThePrimitiveType());
				}

				// add to main collection
				newParameters.add(newParameter);
			}
		}
		// after all, retrieve correct collection
		return newParameters;
	}

	/**
	 * Taken from processUtility.CopyProcess
	 */
	private Collection<Parameters> copyAutomaticParameters(Collection theParameters, Automatic newAutomatic) {
		Collection<Parameters> newParameters = new LinkedList<Parameters>();
		for (Iterator<Parameters> paramIterator = theParameters.iterator(); paramIterator.hasNext();) {
			Parameters currentParameter = paramIterator.next();
			if (currentParameter != null) {
				Parameters newParameter = null;
				newParameter = new Parameters();
				newParameter.insertIntoTheAutomatic(newAutomatic);
				newParameter.setDescription(currentParameter.getDescription());
				// add to main collection
				newParameters.add(newParameter);
			}
		}
		// after all, retrieve correct collection
		return newParameters;
	}

	/**
	 * Generate Ident for copies
	 */
	private String generateNewIdent(String oldIdent, String level_to, Properties properties) {

		String lastToken = this.getLastToken(oldIdent, ".");
		String ident = level_to + "." + lastToken;
		String validIdent = this.getValidCopyId(ident, 0, properties);

		properties.put(validIdent, validIdent);

		return validIdent;
	}

	private String generateIdent(String oldIdent, String level_to) {

		String lastToken = this.getLastToken(oldIdent, ".");
		return level_to + "." + lastToken;
	}

	private String getValidCopyId(String ident, int seq, Properties properties) {

		String act = (String) properties.get(ident);
		if (act == null) {
			return ident;
		} else {

			if (ident.endsWith(" copy")) { // seq == 0
				seq++; // seq =1
				String nextIdent = ident + " " + seq;
				return this.getValidCopyId(nextIdent, 1, properties); // recursive
																		// call
																		// with
																		// next
																		// sequence
																		// number
			} else {

				String lastToken = this.getLastToken(ident, ".");
				if (lastToken.contains(" copy ")) {
					String sequenceNumber = this.getLastToken(lastToken, " ");
					try {
						int intSequenceNumber = Integer.valueOf(sequenceNumber);
						String identWithoutSeq = ident.substring(0, ident.length() - sequenceNumber.length() - 1);
						intSequenceNumber++;
						return this.getValidCopyId(identWithoutSeq + " " + intSequenceNumber, intSequenceNumber, properties);
					} catch (NumberFormatException e) {
						return this.getValidCopyId(ident + " copy", 0, properties);
					}
				} else { // exception - not expected ident! creating a new copy
							// with seq == 0
					return this.getValidCopyId(ident + " copy", 0, properties);
				}
			}
		}
	}

	private void initHashtable(ProcessModel processModel) {

		this.allActs = new Properties();
		this.allConns = new Properties();

		Collection<Activity> allacts = processModel.getTheActivity();
		Collection<Connection> allconns = processModel.getTheConnection();

		for (Activity activity : allacts) {
			String ident = activity.getIdent();
			this.allActs.put(ident, ident);
		}

		for (Connection connection : allconns) {
			String ident = connection.getIdent();
			this.allConns.put(ident, ident);
		}
	}

	/**
	 * Returns the last token given a separator.
	 */
	private String getLastToken(String string, String separator) {

		StringTokenizer st = new StringTokenizer(string, separator);

		int numberTokens = st.countTokens();

		int i = 1; // not zero
		while (i < numberTokens) {
			st.nextToken();
			i++;
		}

		String last = "";
		try {
			last = st.nextToken();
		} catch (Exception e) {
			last = "";
		}

		return last;
	}

	/**
	 * Returns a Collection with the successors of an Activity.
	 */
	private Collection<Connection> getSimpleConnectionsToCopyFromActivity(Activity act, Collection sourceActivities) {

		Collection<Connection> copy = new HashSet<Connection>();

		ProcessModel processModel = act.getTheProcessModel();
		Collection connections = processModel.getTheConnection();
		Iterator iterConns = connections.iterator();
		while (iterConns.hasNext()) {
			Connection connection = (Connection) iterConns.next();
			if (connection != null) {
				if (connection instanceof SimpleCon) {
					SimpleCon simpleCon = (SimpleCon) connection;

					if (sourceActivities.contains(simpleCon.getFromActivity()) && sourceActivities.contains(simpleCon.getToActivity())) {
						copy.add(simpleCon);
					}
				}
			}
		}

		return copy;
	}

	/**
	 * Auxiliar for replanning
	 */

	/**
	 * Propagating the new planning for the successors of 'activity'
	 */
	private void propagateReplanning(Activity activity, Activity already,CalendarDTO utilsDays) {

		if (activity == null)
			return;

		if (activity instanceof Normal)
			this.lastValid = (Normal) activity;

		// greaterPredecessor[0] --> greater Date
		// greaterPredecessor[1] --> predecessor dependency (String).
		Object[] greaterPredecessor = new Object[2];

		Collection connectionsTo = this.getConnectionsTo(activity);
		Iterator iterConnectionsTo = connectionsTo.iterator();
		while (iterConnectionsTo.hasNext()) {
			Connection connection = (Connection) iterConnectionsTo.next();

			Collection successors = this.getSuccessors(connection);
			Iterator iterSuccessors = successors.iterator();
			while (iterSuccessors.hasNext()) {
				Activity successor = (Activity) iterSuccessors.next();

				if (successor.equals(already))
					continue;

				Hashtable<Activity, String> allPredecessors = this.getPredecessorsFrom(successor);

				greaterPredecessor = this.getGreaterDateFromCollection(allPredecessors);
				if (successor instanceof Normal) {
					Normal succNormal = (Normal) successor;
					this.replanningNormalDates(succNormal, greaterPredecessor,utilsDays);
				} else if (successor instanceof Decomposed) {
					Decomposed decomposed = (Decomposed) successor;
					this.replanningDecomposedDates(decomposed, activity, greaterPredecessor,utilsDays);
				}

				// Recursive call for successors of successors...
				this.propagateReplanning(successor, activity,utilsDays);
			}
		}
	}

	private void upperPropagation(Decomposed actDecomposed, Activity already,CalendarDTO utilsDays) {
		if (actDecomposed != null) {
			this.propagateReplanning(actDecomposed, already,utilsDays);
			Decomposed upperDecomposed = actDecomposed.getTheProcessModel().getTheDecomposed();
			this.upperPropagation(upperDecomposed, actDecomposed,utilsDays);
		}
	}

	private Normal retrieveBasedOnLastDate(Collection coll) {
		if (coll.isEmpty())
			return null;

		Iterator iter = coll.iterator();
		Normal greater = (Normal) iter.next();
		while (iter.hasNext()) {
			Normal activity = (Normal) iter.next();
			if (this.isAfter(activity.getPlannedEnd(), greater.getPlannedEnd())) {
				greater = activity;
			}
		}
		return greater;
	}

	/**
	 * param predecessors is never null and never empty! return Object[] -
	 * ret[0] --> Date ret[1] --> String
	 */
	private Object[] getGreaterDateFromCollection(Hashtable<Activity, String> predecessors) {

		Object[] ret = new Object[2];
		Date greater = null;
		String depReturn = "";

		Enumeration enumeration = predecessors.keys();

		while (enumeration.hasMoreElements()) {
			Activity predecessor = (Activity) enumeration.nextElement();
			String dependency = predecessors.get(predecessor);
			if (dependency.equals("end-start")) {
				if (predecessor instanceof Normal) {
					Normal normal = (Normal) predecessor;
					Date end = normal.getPlannedEnd();
					if (this.isAfter(end, greater)) {
						greater = end;
						depReturn = "end-start";
					}
				} else if (predecessor instanceof Decomposed) {
					Decomposed decomposed = (Decomposed) predecessor;
					Normal normal = this.retrieveBasedOnLastDate(this.getCandidatesToFinal(decomposed));
					if (normal != null) {
						Date end = normal.getPlannedEnd();
						if (this.isAfter(end, greater)) {
							greater = end;
							depReturn = "end-start";
						}
					}
				}
			} else if (dependency.equals("start-start")) {
				if (predecessor instanceof Normal) {
					Normal normal = (Normal) predecessor;
					Date begin = normal.getPlannedBegin();
					if (this.isAfter(begin, greater)) {
						greater = begin;
						depReturn = "start-start";
					}
				} else if (predecessor instanceof Decomposed) {
					Decomposed decomposed = (Decomposed) predecessor;
					Normal normal = this.retrieveBasedOnLastDate(this.getCandidatesToFinal(decomposed));
					if (normal != null) {
						Date begin = normal.getPlannedBegin();
						if (this.isAfter(begin, greater)) {
							greater = begin;
							depReturn = "start-start";
						}
					}
				}
			} else if (dependency.equals("end-end")) {
				if (predecessor instanceof Normal) {
					Normal normal = (Normal) predecessor;
					Date end = normal.getPlannedEnd();
					if (this.isAfter(end, greater)) {
						greater = end;
						depReturn = "end-end";
					}
				} else if (predecessor instanceof Decomposed) {
					Decomposed decomposed = (Decomposed) predecessor;
					Normal normal = this.retrieveBasedOnLastDate(this.getCandidatesToFinal(decomposed));
					if (normal != null) {
						Date end = normal.getPlannedEnd();
						if (this.isAfter(end, greater)) {
							greater = end;
							depReturn = "end-end";
						}
					}
				}
			}
		}

		ret[0] = greater;
		ret[1] = depReturn;
		return ret;
	}

	/**
	 * 
	 * 1. END-START
	 * 
	 * PlannedBegin(successor) = PlannedEnd(predecessor) + 1 day
	 * PlannedEnd(successor) = PlannedBegin(successor) + How Long (business
	 * days)
	 * 
	 * 2. END-END
	 * 
	 * PlannedBegin(successor) = PlannedEnd(sucessor) - HowLong (business days)
	 * PlannedEnd(successor) = PlannedEnd(predecessor) + 1 day
	 * 
	 * 3. START-START
	 * 
	 * PlannedBegin(successor) = PlannedBegin(predecessor) + 1 day
	 * PlannedEnd(successor) = PlannedBegin(sucessor) + HowLong (business days)
	 * 
	 */
	private void applyDatesFromPredecessor(Normal normal, Activity predecessor, String dependency, Date greaterPredecessor,CalendarDTO replanningDates ) {

		if (predecessor instanceof Normal) {
			Normal predNormal = (Normal) predecessor;
			String state = normal.getTheEnactionDescription().getState();

			if (dependency.equals("end-start")) {

				Date newPlannedBegin = null;
				if (greaterPredecessor != null) {
					newPlannedBegin = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
				} else {
					newPlannedBegin = new Date(predNormal.getPlannedEnd().getTime() + MILLIS_PER_DAY);
				}
				newPlannedBegin = this.moveFromWeekendToMonday(newPlannedBegin);
//calculateUtilsDays(calculateUtilsDays,replanningDates);
				//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto
				
				normal.setPlannedBegin(newPlannedBegin);

				int oldHowLong = Math.round(normal.getHowLong().floatValue());
				Date newPlannedEnd = new Date(this.plannedEnd(normal.getPlannedBegin().getTime(), oldHowLong));
				normal.setPlannedEnd(newPlannedEnd);

				// Exception handling...
				// Begin should not be after than end!
				if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
					normal.setPlannedEnd(normal.getPlannedBegin());
					normal.setHowLong(0f);
				}
			} else if (dependency.equals("start-start")) {

				Date newPlannedBegin = null;

				if (greaterPredecessor != null) {
					newPlannedBegin = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
				} else {
					newPlannedBegin = new Date(predNormal.getPlannedBegin().getTime() + MILLIS_PER_DAY);
				}

				newPlannedBegin = this.moveFromWeekendToMonday(newPlannedBegin);
				//calculateUtilsDays(replanningDates);
				//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto
				
				normal.setPlannedBegin(newPlannedBegin);

				int oldHowLong = Math.round(normal.getHowLong().floatValue());
				Date newPlannedEnd = new Date(this.plannedEnd(normal.getPlannedBegin().getTime(), oldHowLong));
				normal.setPlannedEnd(newPlannedEnd);

				normal.setPlannedEnd(newPlannedEnd);
				// Exception handling...
				// Begin should not be after than end!
				if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
					normal.setPlannedEnd(normal.getPlannedBegin());
					normal.setHowLong(0f);
				}
			} else if (dependency.equals("end-end")) {

				Date newPlannedEnd = null;

				if (greaterPredecessor != null) {
					newPlannedEnd = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
				} else {
					newPlannedEnd = new Date(predNormal.getPlannedEnd().getTime() + MILLIS_PER_DAY);
				}
				newPlannedEnd = this.moveFromWeekendToMonday(newPlannedEnd);
			//	calculateUtilsDays(replanningDates);
				//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto

				normal.setPlannedEnd(newPlannedEnd);

				Date newPlannedBegin = new Date(this.plannedBegin(normal.getPlannedEnd().getTime(), Math.round(normal.getHowLong().floatValue())));
				normal.setPlannedBegin(newPlannedBegin);

				// Exception handling...
				// Begin should not be after than end!
				if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
					normal.setPlannedBegin(normal.getPlannedEnd());
					normal.setHowLong(0f);
				}
			}
		} else if (predecessor instanceof Decomposed) {
			Decomposed predDecomposed = (Decomposed) predecessor;
			Collection candidates = this.getCandidatesToFinal(predDecomposed);
			Normal thePredecessor = this.retrieveBasedOnLastDate(candidates);
			if (thePredecessor != null) { // if it is null, the decomposed might
											// be empty.
				String state = normal.getTheEnactionDescription().getState();

				if (dependency.equals("end-start")) {
					Date newPlannedBegin = null;
					if (greaterPredecessor != null) {
						newPlannedBegin = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
					} else {
						newPlannedBegin = new Date(thePredecessor.getPlannedEnd().getTime() + MILLIS_PER_DAY);
					}
					newPlannedBegin = this.moveFromWeekendToMonday(newPlannedBegin);
			//		calculateUtilsDays(replanningDates);
					//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto

					normal.setPlannedBegin(newPlannedBegin);

					int oldHowLong = Math.round(normal.getHowLong().floatValue());
					Date newPlannedEnd = new Date(this.plannedEnd(normal.getPlannedBegin().getTime(), oldHowLong));
					normal.setPlannedEnd(newPlannedEnd);

					// Exception handling...
					// Begin should not be after than end!
					if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
						normal.setPlannedEnd(normal.getPlannedBegin());
						normal.setHowLong(0f);
					}
				} else if (dependency.equals("start-start")) {
					Date newPlannedBegin = null;

					if (greaterPredecessor != null) {
						newPlannedBegin = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
					} else {
						newPlannedBegin = new Date(thePredecessor.getPlannedBegin().getTime() + MILLIS_PER_DAY);
					}

					newPlannedBegin = this.moveFromWeekendToMonday(newPlannedBegin);
				//	calculateUtilsDays(replanningDates);
					//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto
					
					normal.setPlannedBegin(newPlannedBegin);

					int oldHowLong = Math.round(normal.getHowLong().floatValue());
					Date newPlannedEnd = new Date(this.plannedEnd(normal.getPlannedBegin().getTime(), oldHowLong));
					normal.setPlannedEnd(newPlannedEnd);

					// Exception handling...
					// Begin should not be after than end!
					if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
						normal.setPlannedEnd(normal.getPlannedBegin());
						normal.setHowLong(0f);
					}
				} else if (dependency.equals("end-end")) {

					Date newPlannedEnd = null;

					if (greaterPredecessor != null) {
						newPlannedEnd = new Date(greaterPredecessor.getTime() + MILLIS_PER_DAY);
					} else {
						newPlannedEnd = new Date(thePredecessor.getPlannedEnd().getTime() + MILLIS_PER_DAY);
					}
					newPlannedEnd = this.moveFromWeekendToMonday(newPlannedEnd);
				//	calculateUtilsDays(replanningDates);
					//Chamar metodo que calcula as datas válidas de inicio e fim da atividade de acordo com o Calendário do projeto

					normal.setPlannedEnd(newPlannedEnd);

					Date newPlannedBegin = new Date(this.plannedBegin(normal.getPlannedEnd().getTime(), Math.round(normal.getHowLong().floatValue())));
					normal.setPlannedBegin(newPlannedBegin);

					// Exception handling...
					// Begin should not be after than end!
					if (this.isAfter(normal.getPlannedBegin(), normal.getPlannedEnd())) {
						normal.setPlannedBegin(normal.getPlannedEnd());
						normal.setHowLong(0f);
					}
				}
			}
		}
	}

	private Collection getCandidatesToFinal(Decomposed dec) {

		Collection subacts = dec.getTheReferedProcessModel().getTheActivity();

		Iterator iterSubacts = subacts.iterator();
		Collection finals = new HashSet();

		while (iterSubacts.hasNext()) {
			Activity subact = (Activity) iterSubacts.next();
			Collection consTo = this.getConnectionsTo(subact);
			if (consTo.isEmpty()) {
				finals.add(subact);
			}
		}

		Collection candidates = new HashSet();

		Iterator iterActs = finals.iterator();
		while (iterActs.hasNext()) {
			Activity act = (Activity) iterActs.next();
			if (act instanceof Normal) {
				candidates.add(act);
			} else if (act instanceof Decomposed) {
				Decomposed decomposed = (Decomposed) act;
				candidates.addAll(this.getCandidatesToFinal(decomposed));
			}
		}

		return candidates;
	}

	private Collection getBeginners(Decomposed dec) {

		Collection subacts = dec.getTheReferedProcessModel().getTheActivity();
		Iterator iterSubacts = subacts.iterator();

		Collection beginners = new HashSet();

		while (iterSubacts.hasNext()) {
			Activity subact = (Activity) iterSubacts.next();
			Collection consFrom = this.getConnectionsFrom(subact);
			if (consFrom.isEmpty()) {
				beginners.add(subact);
			}
		}

		return beginners;
	}

	private boolean isAfter(Date date1, Date date2) {

		if (date1 == null) {
			return false;
		} else {
			if (date2 == null) {
				return true;
			} else {
				if (date1.getTime() > date2.getTime()) {
					return true;
				}
				return false;
			}
		}
	}

	private boolean isSaturday(Date date) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SATURDAY) {
			return true;
		}
		return false;
	}
	
	private boolean isFriday(Date date) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.FRIDAY) {
			return true;
		}
		return false;
	}

	private boolean isSunday(Date date) {

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);

		int day = cal.get(Calendar.DAY_OF_WEEK);
		if (day == Calendar.SUNDAY) {
			return true;
		}
		return false;
	}

	private int businessDays(long begin, long end) {

		int delta = (int) ((end - begin) / MILLIS_PER_DAY) + 1;

		Date beginDate = new Date(begin);
		Calendar calendarBegin = Calendar.getInstance();
		calendarBegin.setTime(beginDate);

		int businessDays = 0;
		while (delta > 0) {

			int dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
				businessDays++;
			}
			calendarBegin.add(Calendar.DATE, 1);
			delta--;
		}
		return businessDays;
	}

	private Date moveFromWeekendToMonday(Date date) {
		Date monday = date;

		if (this.isSaturday(date)) {
			monday = new Date(date.getTime() + (2 * MILLIS_PER_DAY)); // moving
																		// to
																		// monday
																		// through
																		// sunday
		} else if (this.isSunday(date)) {
			monday = new Date(date.getTime() + MILLIS_PER_DAY); // moving to
																// monday
		}
		return monday;
	}

	private long plannedEnd(long begin, int businessDays) {
		Date beginDate = new Date(begin);
		Calendar calendarBegin = Calendar.getInstance();
		calendarBegin.setTime(beginDate);

		// handling begin = saturday...
		int dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY) {
			calendarBegin.add(Calendar.DATE, 1);
		}

		// handling sunday...
		dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY) {
			calendarBegin.add(Calendar.DATE, 1);
		}

		int count = businessDays;
		while (count > 1) {

			calendarBegin.add(Calendar.DATE, 1);
			dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
				count--;
			}
		}
		return calendarBegin.getTimeInMillis();
	}

	private long plannedBegin(long end, int businessDays) {
		Date endDate = new Date(end);
		Calendar calendarEnd = Calendar.getInstance();
		calendarEnd.setTime(endDate);

		// handling end = sunday...
		int dayOfWeek = calendarEnd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SUNDAY) {
			calendarEnd.add(Calendar.DATE, -1);
		}

		// handling saturday...
		dayOfWeek = calendarEnd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == Calendar.SATURDAY) {
			calendarEnd.add(Calendar.DATE, -1);
		}

		int count = businessDays;
		while (count > 1) {

			calendarEnd.add(Calendar.DATE, -1);
			dayOfWeek = calendarEnd.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
				count--;
			}
		}
		return calendarEnd.getTimeInMillis();
	}

	/**
	 * Replan dates for normal activities
	 */
	private boolean replanningNormalDates(Normal normal, Object[] greaterPredecessor,CalendarDTO utilsDays) {

		String state = normal.getTheEnactionDescription().getState();
		if (!(state.equals(Plain.FINISHED) || state.equals(Plain.FAILED) || state.equals(Plain.CANCELED))) {

			this.applyDatesFromPredecessor(normal, this.lastValid, (String) greaterPredecessor[1], (Date) greaterPredecessor[0],utilsDays);

			long newHowLong = normal.getHowLong().longValue();
			if (normal.getPlannedBegin() != null && normal.getPlannedEnd() != null)
				newHowLong = this.businessDays(normal.getPlannedBegin().getTime(), normal.getPlannedEnd().getTime());

			normal.setHowLong(new Float(Math.round(newHowLong)));

			return true;
		}
		return false;
	}

	/**
	 * Replanning the subprocess
	 */
	private void replanningDecomposedDates(Decomposed decomposed, Activity predecessor, Object[] greaterPredecessor,CalendarDTO utilsDays) {

		ProcessModel processModel = decomposed.getTheReferedProcessModel();
		String state = processModel.getPmState();

		if (!(state.equals(ProcessModel.FINISHED) || state.equals(ProcessModel.FAILED) || state.equals(ProcessModel.CANCELED))) { // It
																																	// means
																																	// that
																																	// decomposed
																																	// activity
																																	// has
																																	// already
																																	// in
																																	// an
																																	// final
																																	// state

			Normal temp = this.lastValid; // This temporary variables assures
											// that all begginers have the same
											// predecessor
											// as a base for the dates calculus.

			Collection beginners = this.getBeginners(decomposed); // The firsts
																	// activities
																	// to be
																	// enacted
																	// inside
																	// this
																	// decomposed
			Iterator iterBeginners = beginners.iterator();
			while (iterBeginners.hasNext()) {
				Activity beginner = (Activity) iterBeginners.next();

				if (beginner instanceof Normal) {
					Normal normal = (Normal) beginner;
					this.replanningNormalDates(normal, greaterPredecessor,utilsDays);
				} else if (beginner instanceof Decomposed) {
					Decomposed nextDecomposed = (Decomposed) beginner;
					this.replanningDecomposedDates(nextDecomposed, predecessor, greaterPredecessor,utilsDays);
				}

				this.propagateReplanning(beginner, decomposed,utilsDays);
				this.lastValid = temp; // After the propagation of each
										// begginer, the lastValid attribute
										// returns.
			}

			// recent added, since lastValid shold be
			// the greater planned end inside the decomposed
			if (!beginners.isEmpty()) {
				this.lastValid = this.retrieveBasedOnLastDate(this.getCandidatesToFinal(decomposed));
			}
		}
	}

	/**
	 * Returns a Collection with the successors (Activities only) of a
	 * Connection.
	 */
	private Collection getSuccessors(Connection conn) {

		Collection succ = new LinkedList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			if (seq.getToActivity() != null)
				succ.add(seq.getToActivity());
		} else if (conn instanceof Branch) {
			Branch branch = (Branch) conn;
			if (branch instanceof BranchAND) {
				BranchAND bAND = (BranchAND) branch;
				if (bAND.getToActivity() != null)
					succ.addAll(bAND.getToActivity());
				if (bAND.getToMultipleCon() != null) {
					Collection multis = bAND.getToMultipleCon();
					Iterator iterMultis = multis.iterator();
					while (iterMultis.hasNext()) {
						MultipleCon multi = (MultipleCon) iterMultis.next();
						if (multi != null)
							succ.addAll(this.getSuccessors(multi));
					}
				}
			} else {
				BranchCond bCond = (BranchCond) branch;
				Collection bctmc = bCond.getTheBranchCondToMultipleCon();
				Collection atbc = bCond.getTheBranchCondToActivity();
				Iterator iterMulti = bctmc.iterator(), iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchCondToMultipleCon) iterMulti.next();
					if (multi.getTheMultipleCon() != null)
						succ.addAll(this.getSuccessors(multi.getTheMultipleCon()));
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchCondToActivity) iterAct.next();
					if (act.getTheActivity() != null)
						succ.add(act.getTheActivity());
				}
			}
		} else if (conn instanceof Join) {
			Join join = (Join) conn;
			if (join.getToActivity() != null)
				succ.add(join.getToActivity());
			if (join.getToMultipleCon() != null)
				succ.addAll(this.getSuccessors(join.getToMultipleCon()));
		}
		return succ;
	}

	/**
	 * Returns a Collection with the predecessors (Activities only) of a
	 * Connection.
	 */
	private Collection getPredecessors(Connection conn) {

		Collection pred = new LinkedList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			if (seq.getFromActivity() != null)
				pred.add(seq.getFromActivity());
		} else if (conn instanceof Branch) {
			Branch branch = (Branch) conn;
			if (branch.getFromActivity() != null)
				pred.add(branch.getFromActivity());
			if (branch.getFromMultipleConnection() != null)
				pred.addAll(this.getPredecessors(branch.getFromMultipleConnection()));
		} else if (conn instanceof Join) {
			Join join = (Join) conn;
			if (join.getFromActivity() != null)
				pred.addAll(join.getFromActivity());
			if (join.getFromMultipleCon() != null) {
				Collection multis = join.getFromMultipleCon();
				Iterator iterMultis = multis.iterator();
				while (iterMultis.hasNext()) {
					MultipleCon multi = (MultipleCon) iterMultis.next();
					pred.addAll(this.getPredecessors(multi));
				}
			}
		}
		return pred;
	}

	/**
	 * Returns a Collection with the successors of an Activity.
	 */
	private Collection getConnectionsTo(Activity act) {

		Collection connTo = new LinkedList();
		if (act.getToSimpleCon() != null)
			connTo.addAll(act.getToSimpleCon());
		if (act.getToJoin() != null)
			connTo.addAll(act.getToJoin());
		if (act.getToBranch() != null)
			connTo.addAll(act.getToBranch());
		return connTo;
	}

	/**
	 * Returns a Collection with the predecessors of an Activity.
	 */
	private Collection getConnectionsFrom(Activity act) {

		Collection connFrom = new LinkedList();
		if (act.getFromSimpleCon() != null)
			connFrom.addAll(act.getFromSimpleCon());
		if (act.getFromJoin() != null)
			connFrom.addAll(act.getFromJoin());
		if (act.getFromBranchAND() != null)
			connFrom.addAll(act.getFromBranchAND());
		Collection bctas = act.getTheBranchCondToActivity();
		Iterator iterBctas = bctas.iterator();
		while (iterBctas.hasNext()) {
			BranchCondToActivity bcta = (BranchCondToActivity) iterBctas.next();
			if (bcta.getTheBranchCond() != null)
				connFrom.add(bcta.getTheBranchCond());
		}
		return connFrom;
	}

	private Hashtable<Activity, String> getPredecessorsFrom(Activity act) {

		Hashtable<Activity, String> allPredecessors = new Hashtable<Activity, String>();

		Collection connectionsFrom = this.getConnectionsFrom(act);
		Iterator iterConnectionsFrom = connectionsFrom.iterator();
		while (iterConnectionsFrom.hasNext()) {
			Connection connFrom = (Connection) iterConnectionsFrom.next();
			String dependency = null;
			if (connFrom instanceof Sequence) {
				dependency = ((Sequence) connFrom).getTheDependency().getKindDep();
			} else if (connFrom instanceof MultipleCon) {
				dependency = ((MultipleCon) connFrom).getTheDependency().getKindDep();
			}

			Collection predecessors = this.getPredecessors(connFrom);
			Iterator iterPredecesors = predecessors.iterator();
			while (iterPredecesors.hasNext()) {
				Activity predecessor = (Activity) iterPredecesors.next();
				allPredecessors.put(predecessor, dependency);
			}
		}

		return allPredecessors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.qrconsult.spm.services.impl.EasyModelingInterface#
	 * applyAllocationToProcess(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void applyAllocationToProcess(String process_id, String role_id, String agent_id) {

		
		SimpleActivityQueryResult[] acts = procDAO.getAllNormalActivitiesFromProcess(process_id);

		NormalDAO nDAO = new NormalDAO();

		for (int i = 0; i < acts.length; i++) {

			// defineRequiredAgent(act.getIdent(),role_id, agent_id);
			String normal_id = acts[i].getIdent();

			Normal currentNormal = null;
			currentNormal = (Normal) nDAO.retrieveBySecondaryKey(normal_id);

			if (currentNormal == null)
				continue;

			Collection<RequiredPeople> reqps = currentNormal.getTheRequiredPeople();

			for (RequiredPeople reqp : reqps) {
				if (reqp instanceof ReqAgent) {
					// Not instantiated yet
					if (((ReqAgent) reqp).getTheAgent() == null) {
						if ((((ReqAgent) reqp).getTheRole() != null) && (((ReqAgent) reqp).getTheRole().getIdent().equals(role_id))) {
							// allocate agent to this role
							try {
								// String act_id, String role_id, String ag_id
								dynamicModeling.defineRequiredAgent(currentNormal.getIdent(), role_id, agent_id);
							} catch (WebapseeException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;// break current loop, because we can
							// allocate only once the same agent
							// to one activity
						}
					}
				}
			}// end for
		}

	}

	public CalendarDTO filtraDatas(CalendarDTO calendar){
		CalendarDTO replainDates = new CalendarDTO();
		ArrayList<String> replainTexto = new ArrayList<>();
		
		
		for (int i = 0; i < calendar.getNotWorkingDays().size(); i++) {
			String c[] = calendar.getNotWorkingDays().get(i).split("\\,");
			
			replainTexto.add(c[3]);
		}
		
			replainDates.setNotWorkingDays(replainTexto);
			System.out.println("calendario "+replainDates.getNotWorkingDays().get(0));	
			System.out.println("calendario "+replainDates.getNotWorkingDays().get(1));
				
		
		return replainDates;
		
		
		
		
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.qrconsult.spm.services.impl.EasyModelingInterface#
	 * applyAllocationToProcessModel(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void applyAllocationToProcessModel(String pm_id, String role_id, String agent_id) {

		ProcessModelDAO pmDAO = new ProcessModelDAO();

		ProcessModel pm = null;

		DecomposedDAO dao = new DecomposedDAO();
		ProcessDAO procDAO = new ProcessDAO();

		Decomposed dec = null;
		Process proc = null;
		dec = (Decomposed) dao.retrieveBySecondaryKey(pm_id);
		if (dec != null) {
			// this is not a root process model
			pm = dec.getTheReferedProcessModel();
		} else {
			proc = (Process) procDAO.retrieveBySecondaryKey(pm_id);
			if (proc != null) {
				// this is a root process model
				pm = proc.getTheProcessModel();
			}
		}

		if (pm != null) {
			Collection<Activity> acts = pm.getTheActivity();
			for (Activity act : acts) {

				if (act instanceof Normal) {
					Collection<RequiredPeople> reqps = ((Normal) act).getTheRequiredPeople();

					for (RequiredPeople reqp : reqps) {
						if (reqp instanceof ReqAgent) {
							// Not instantied yet
							if (((ReqAgent) reqp).getTheAgent() == null) {
								if ((((ReqAgent) reqp).getTheRole() != null) && (((ReqAgent) reqp).getTheRole().getIdent().equals(role_id))) {
									// alocate agent to this role
									try {
										// String act_id, String role_id, String
										// ag_id
										dynamicModeling.defineRequiredAgent(act.getIdent(), role_id, agent_id);
									} catch (WebapseeException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;// break current loop, because we can
									// allocate only once the same agent
									// to one activity
								}
							}
						}
					}// end for
				}

			}// end for
		}

	}

}
