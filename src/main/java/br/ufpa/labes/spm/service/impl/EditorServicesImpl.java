package org.qrconsult.spm.services.impl;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.Stateless;

import org.qrconsult.spm.dataAccess.impl.activities.DecomposedDAO;
import org.qrconsult.spm.dataAccess.impl.processModelGraphical.WebAPSEEObjectDAO;
import org.qrconsult.spm.dataAccess.impl.processModels.ProcessDAO;
import org.qrconsult.spm.exceptions.DAOException;
import org.qrconsult.spm.model.activities.Activity;
import org.qrconsult.spm.model.activities.Decomposed;
import org.qrconsult.spm.model.connections.ArtifactCon;
import org.qrconsult.spm.model.connections.BranchAND;
import org.qrconsult.spm.model.connections.BranchCond;
import org.qrconsult.spm.model.connections.Connection;
import org.qrconsult.spm.model.connections.Feedback;
import org.qrconsult.spm.model.connections.Join;
import org.qrconsult.spm.model.connections.MultipleCon;
import org.qrconsult.spm.model.connections.Sequence;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.plainActivities.ReqAgent;
import org.qrconsult.spm.model.plainActivities.ReqGroup;
import org.qrconsult.spm.model.plainActivities.RequiredPeople;
import org.qrconsult.spm.model.plainActivities.RequiredResource;
import org.qrconsult.spm.model.processModelGraphical.GraphicCoordinate;
import org.qrconsult.spm.model.processModelGraphical.WebAPSEEObject;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.processModels.ProcessModel;
import org.qrconsult.spm.services.interfaces.EditorServices;

@Stateless
public class EditorServicesImpl implements EditorServices {

	@Override
	public String getActivities(String sessionID) {
		return "Agora Funciona Baralho!";
	}

	private StringBuffer getPositionContent(GraphicCoordinate reqPGP) {
		StringBuffer buff = new StringBuffer();
		if (reqPGP != null) {
			buff.append("<POSITION X=\"").append(reqPGP.getX()).append("\"").append(" Y=\"").append(reqPGP.getY()).append("\" VISIBLE=\"")
					.append(reqPGP.getVisible()).append("\"").append(" />");
		}
		return buff;
	}

	private StringBuffer getArtifactConRef(Collection<ArtifactCon> artifactcons) {
		StringBuffer buff = new StringBuffer();
		for (ArtifactCon acon : artifactcons) {
			if (acon != null)
				buff.append("<ARTIFACTCON ID=\"").append(acon.getIdent()).append("\"").append(" />");
		}
		return buff;
	}

	private StringBuffer getMultipleConRef(Collection<MultipleCon> multiplecons) {
		StringBuffer buff = new StringBuffer();
		for (MultipleCon mcon : multiplecons) {
			if (mcon != null)
				buff.append("<MULTIPLECON ID=\"").append(mcon.getIdent()).append("\"").append(" />");
		}
		return buff;
	}

	private StringBuffer getActivitiesRef(Collection<Activity> acts) {
		StringBuffer buff = new StringBuffer();
		for (Activity act : acts) {
			if (act != null)
				buff.append("<ACTIVITY ID=\"").append(act.getIdent()).append("\"").append(" />");
		}
		return buff;
	}

	public String getEditorContent(String pModelID)  {
		System.out.print(pModelID);
		StringBuffer rootBuffer = new StringBuffer();
		rootBuffer.append("<EDITOR ID=\"").append(pModelID).append("\">");

		StringBuffer activityBuffer = new StringBuffer();
		activityBuffer.append("<ACTIVITIES>");

		StringBuffer reqPeopleBuffer = new StringBuffer();
		reqPeopleBuffer.append("<PEOPLE>");

		StringBuffer reqResourceBuffer = new StringBuffer();
		reqResourceBuffer.append("<RESOURCES>");

		StringBuffer connectionsBuffer = new StringBuffer();
		connectionsBuffer.append("<CONNECTIONS>");

		ProcessDAO processDAO = new ProcessDAO();

		Process process = (Process) processDAO.retrieve(pModelID);

		ProcessModel pModel = null;

		if (process != null) {
			pModel = process.getTheProcessModel();
		} else {
			DecomposedDAO decompDAO = new DecomposedDAO();
			Decomposed decomposed = (Decomposed) decompDAO.retrieveBySecondaryKey(pModelID);
			pModel = decomposed.getTheReferedProcessModel();
		}

		WebAPSEEObjectDAO webAPSEEObjDAO = new WebAPSEEObjectDAO();

		WebAPSEEObject webAPSEEObject = null;

		// processa conexoes
		Collection<Connection> connections = pModel.getTheConnection();
		for (Connection conn : connections) {
			GraphicCoordinate conGP = null;
			try {
				webAPSEEObject = webAPSEEObjDAO.retrieveWebAPSEEObject(conn.getOid(), conn.getClass().getSimpleName());
			} catch (DAOException e) {
				webAPSEEObject= null;
				e.printStackTrace();
			}
			if (webAPSEEObject != null) {
				conGP = webAPSEEObject.getTheGraphicCoordinate();
			}
			if (conn instanceof ArtifactCon) {

				ArtifactCon artCon = (ArtifactCon) conn;
				connectionsBuffer.append("<ARTIFACTCON ID=\"").append(artCon.getIdent()).append("\">");
				if (artCon.getTheArtifact() != null)
					connectionsBuffer.append("<ARTIFACT>").append(artCon.getTheArtifact().getIdent()).append("</ARTIFACT>");
				if (artCon.getTheArtifactType() != null)
					connectionsBuffer.append("<TYPE>").append(artCon.getTheArtifactType().getIdent()).append("</TYPE>");
				connectionsBuffer.append("<TO>");
				connectionsBuffer.append(getActivitiesRef(artCon.getToActivity()));
				connectionsBuffer.append("</TO>");
				connectionsBuffer.append("<FROM>");
				connectionsBuffer.append(getActivitiesRef(artCon.getFromActivity()));
				connectionsBuffer.append("</FROM>");
				if (conGP != null)
					connectionsBuffer.append(getPositionContent(conGP));

				connectionsBuffer.append("</ARTIFACTCON>");
			} else if (conn instanceof MultipleCon) {
				// JOIN
				if (conn instanceof Join) {
					Join jCon = (Join) conn;
					connectionsBuffer.append("<JOIN ID=\"").append(jCon.getIdent()).append("\">");
					if (jCon.getTheDependency() != null)
						connectionsBuffer.append("<DEPENDENCY>").append(jCon.getTheDependency().getKindDep()).append("</DEPENDENCY>");
					if (jCon.getTheConnectionType() != null)
						connectionsBuffer.append("<TYPE>").append(jCon.getTheConnectionType().getIdent()).append("</TYPE>");

					if (jCon.getToActivity() != null) {
						connectionsBuffer.append("<TO_ACTIVITY ID=\"").append(jCon.getToActivity().getIdent()).append("\" />");
					}

					if (jCon.getToMultipleCon() != null) {
						connectionsBuffer.append("<TO_MULTIPLECON ID=\"").append(jCon.getToMultipleCon().getIdent()).append("\" />");
					}

					connectionsBuffer.append("<FROM_MULTIPLECON>");
					connectionsBuffer.append(getMultipleConRef(jCon.getFromMultipleCon()));
					connectionsBuffer.append("</FROM_MULTIPLECON>");

					connectionsBuffer.append("<FROM_ARTIFACTCON>");
					connectionsBuffer.append(getArtifactConRef(jCon.getFromArtifactCon()));
					connectionsBuffer.append("</FROM_ARTIFACTCON>");

					connectionsBuffer.append("<FROM_ACTIVITY>");
					connectionsBuffer.append(getActivitiesRef(jCon.getFromActivity()));
					connectionsBuffer.append("</FROM_ACTIVITY>");
					if (conGP != null)
						connectionsBuffer.append(getPositionContent(conGP));

					connectionsBuffer.append("</JOIN>");
				} else if (conn instanceof BranchAND) {
					BranchAND bCon = (BranchAND) conn;
					connectionsBuffer.append("<BRANCHAND ID=\"").append(bCon.getIdent()).append("\">");
					if (bCon.getTheDependency() != null)
						connectionsBuffer.append("<DEPENDENCY>").append(bCon.getTheDependency().getKindDep()).append("</DEPENDENCY>");
					if (bCon.getTheConnectionType() != null)
						connectionsBuffer.append("<TYPE>").append(bCon.getTheConnectionType().getIdent()).append("</TYPE>");

					if (bCon.getFromActivity() != null) {
						connectionsBuffer.append("<FROM_ACTIVITY ID=\"").append(bCon.getFromActivity().getIdent()).append("\" />");
					}

					if (bCon.getFromMultipleConnection() != null) {
						connectionsBuffer.append("<FROM_MULTIPLECON ID=\"").append(bCon.getFromMultipleConnection().getIdent()).append("\" />");
					}

					connectionsBuffer.append("<TO_MULTIPLECON>");
					connectionsBuffer.append(getMultipleConRef(bCon.getToMultipleCon()));
					connectionsBuffer.append("</TO_MULTIPLECON>");

					connectionsBuffer.append("<FROM_ARTIFACTCON>");
					connectionsBuffer.append(getArtifactConRef(bCon.getFromArtifactCon()));
					connectionsBuffer.append("</FROM_ARTIFACTCON>");

					connectionsBuffer.append("<TO_ACTIVITY>");
					connectionsBuffer.append(getActivitiesRef(bCon.getToActivity()));
					connectionsBuffer.append("</TO_ACTIVITY>");

					if (conGP != null)
						connectionsBuffer.append(getPositionContent(conGP));
					connectionsBuffer.append("</BRANCHAND>");
				} else if (conn instanceof BranchCond) {

				}
			} else if (conn instanceof Sequence) {
				Sequence seq = (Sequence) conn;
				connectionsBuffer.append("<SEQUENCE ID=\"").append(seq.getIdent()).append("\">");
				if (seq.getTheDependency() != null)
					connectionsBuffer.append("<DEPENDENCY>").append(seq.getTheDependency().getKindDep()).append("</DEPENDENCY>");
				if (seq.getTheConnectionType() != null)
					connectionsBuffer.append("<TYPE>").append(seq.getTheConnectionType().getIdent()).append("</TYPE>");
				if (seq.getToActivity() != null) {
					connectionsBuffer.append("<TO ID=\"").append(seq.getToActivity().getIdent()).append("\" />");
				}
				if (seq.getFromActivity() != null) {
					connectionsBuffer.append("<FROM ID=\"").append(seq.getFromActivity().getIdent()).append("\" />");
				}

				connectionsBuffer.append("</SEQUENCE>");

			} else if (conn instanceof Feedback) {
				Feedback feedback = (Feedback) conn;
				connectionsBuffer.append("<FEEDBACK ID=\"").append(feedback.getIdent()).append("\" >");
				if (feedback.getTheConnectionType() != null)
					connectionsBuffer.append("<TYPE>").append(feedback.getTheConnectionType().getIdent()).append("</TYPE>");
				if (feedback.getToActivity() != null) {
					connectionsBuffer.append("<TO ID=\"").append(feedback.getToActivity().getIdent()).append("\" />");
				}
				if (feedback.getFromActivity() != null) {
					connectionsBuffer.append("<FROM ID=\"").append(feedback.getFromActivity().getIdent()).append("\" />");
				}
				connectionsBuffer.append("</FEEDBACK>");
			}
		}

		// processa atividades
		Collection<Activity> activities = pModel.getTheActivity();
		for (Activity activity : activities) {

			try {
				webAPSEEObject = webAPSEEObjDAO.retrieveWebAPSEEObject(activity.getOid(), activity.getClass().getSimpleName());
			} catch (DAOException e1) {
				webAPSEEObject= null;
				e1.printStackTrace();
			}
			GraphicCoordinate actGP = null;
			if (webAPSEEObject != null) {
				actGP = webAPSEEObject.getTheGraphicCoordinate();
			}
			// /Processa especificos para cada tipo de atividade
			if (activity instanceof Normal) {
				Normal normal = (Normal) activity;
				activityBuffer.append("<NORMAL ID=\"").append(activity.getIdent()).append("\" >");
				activityBuffer.append(getPositionContent(actGP));
				activityBuffer.append("</NORMAL>");

				Collection<RequiredPeople> people = normal.getTheRequiredPeople();
				for (RequiredPeople requiredPeople : people) {

					GraphicCoordinate reqPGP = null;

					if (requiredPeople instanceof ReqAgent) {
						ReqAgent reqAg = (ReqAgent) requiredPeople;
						try {
							webAPSEEObject = webAPSEEObjDAO.retrieveWebAPSEEObject(reqAg.getOid(), reqAg.getClass().getSimpleName());
						} catch (DAOException e) {
							webAPSEEObject= null;
							e.printStackTrace();
						}
						if (webAPSEEObject != null) {
							reqPGP = webAPSEEObject.getTheGraphicCoordinate();
						}
						// /////REQAGENT
						reqPeopleBuffer.append("<REQAGENT>");
						if (reqAg.getTheAgent() != null)
							reqPeopleBuffer.append("<AGENT>").append(reqAg.getTheAgent().getIdent()).append("</AGENT>");
						if (reqAg.getTheRole() != null)
							reqPeopleBuffer.append("<ROLE>").append(reqAg.getTheRole().getIdent()).append("</ROLE>");
						reqPeopleBuffer.append("<NORMAL>").append(normal.getIdent()).append("</NORMAL>");
						reqPeopleBuffer.append(getPositionContent(reqPGP));
						reqPeopleBuffer.append("</REQAGENT>");
						// // COMO GARANTIR QUE UM REQAGENT EH REUTILIZADO?? NA
						// HORA DO DESENHO PELA POSICAO

					} else if (requiredPeople instanceof ReqGroup) {
						ReqGroup reqGroup = (ReqGroup) requiredPeople;
						try {
							webAPSEEObject = webAPSEEObjDAO.retrieveWebAPSEEObject(reqGroup.getOid(), reqGroup.getClass().getSimpleName());
						} catch (DAOException e) {
							webAPSEEObject= null;
							e.printStackTrace();
						}
						if (webAPSEEObject != null) {
							reqPGP = webAPSEEObject.getTheGraphicCoordinate();
						}
						// //////REQGROUP
						reqPeopleBuffer.append("<REQGROUP>");
						if (reqGroup.getTheGroup() != null)
							reqPeopleBuffer.append("<GROUP>").append(reqGroup.getTheGroup().getIdent()).append("</GROUP>");
						if (reqGroup.getTheGroupType() != null)
							reqPeopleBuffer.append("<TYPE>").append(reqGroup.getTheGroupType().getIdent()).append("</TYPE>");
						reqPeopleBuffer.append("<NORMAL>").append(normal.getIdent()).append("</NORMAL>");
						reqPeopleBuffer.append(getPositionContent(reqPGP));
						reqPeopleBuffer.append("</REQGROUP>");
					}
				}

				Collection<RequiredResource> resources = normal.getTheRequiredResource();
				for (Iterator<RequiredResource> itReqRes = resources.iterator(); itReqRes.hasNext();) {
					RequiredResource requiredResource = (RequiredResource) itReqRes.next();
					GraphicCoordinate reqRGP = null;
					
					try {
						webAPSEEObject = webAPSEEObjDAO.retrieveWebAPSEEObject(requiredResource.getOid(), requiredResource.getClass().getSimpleName());
					} catch (DAOException e) {
						webAPSEEObject= null;
						e.printStackTrace();
					}

					if (webAPSEEObject != null) {
						reqRGP = webAPSEEObject.getTheGraphicCoordinate();
					}
					reqResourceBuffer.append("<REQRESOURCE>");
					if (requiredResource.getTheResource() != null)
						reqResourceBuffer.append("<RESOURCE>").append(requiredResource.getTheResource().getIdent()).append("</RESOURCE>");
					if (requiredResource.getTheResourceType() != null)
						reqResourceBuffer.append("<TYPE>").append(requiredResource.getTheResourceType().getIdent()).append("</TYPE>");
					reqResourceBuffer.append("<NORMAL>").append(normal.getIdent()).append("</NORMAL>");

					reqResourceBuffer.append(getPositionContent(reqRGP));
					reqResourceBuffer.append("</REQRESOURCE>");

				}
			} else if (activity instanceof Decomposed) {
				activityBuffer.append("<DECOMPOSED ID=\"").append(activity.getIdent()).append("\" >");
				activityBuffer.append(getPositionContent(actGP));
				activityBuffer.append("</DECOMPOSED>");
			}
		}

		activityBuffer.append("</ACTIVITIES>");
		rootBuffer.append(activityBuffer);
		reqPeopleBuffer.append("</PEOPLE>");
		rootBuffer.append(reqPeopleBuffer);
		reqResourceBuffer.append("</RESOURCES>");
		rootBuffer.append(reqResourceBuffer);
		connectionsBuffer.append("</CONNECTIONS>");
		rootBuffer.append(connectionsBuffer);
		rootBuffer.append("</EDITOR>");
		
		
		return rootBuffer.toString();
	}

	public static void main(String args[]) {

		EditorServicesImpl serv = new EditorServicesImpl();
		String buf = serv.getEditorContent("ProjetoRUP");
		System.out.println(" Buffer /n " + buf);

	}

}