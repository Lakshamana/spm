package org.qrconsult.spm.services.impl;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.qrconsult.spm.dataAccess.interfaces.processModels.IProcessDAO;
import org.qrconsult.spm.exceptions.DAOException;
import org.qrconsult.spm.model.activities.Activity;
import org.qrconsult.spm.model.activities.Decomposed;
import org.qrconsult.spm.model.connections.Branch;
import org.qrconsult.spm.model.connections.BranchAND;
import org.qrconsult.spm.model.connections.BranchCond;
import org.qrconsult.spm.model.connections.BranchCondToActivity;
import org.qrconsult.spm.model.connections.BranchCondToMultipleCon;
import org.qrconsult.spm.model.connections.Join;
import org.qrconsult.spm.model.connections.MultipleCon;
import org.qrconsult.spm.model.connections.Sequence;
import org.qrconsult.spm.model.plainActivities.Normal;
import org.qrconsult.spm.model.processModels.Process;
import org.qrconsult.spm.model.processModels.ProcessModel;
import org.qrconsult.spm.model.connections.Connection;

@Stateless
public class CriticalPathMethod {
	
	@EJB
	IProcessDAO processDAO;
		
	private String processIdent;
	
	private Hashtable<String,AuxiliarActivity> tableAct =
		new Hashtable<String,AuxiliarActivity>(100,1);
	
	private Collection<Activity> lastActs =
		new LinkedList<Activity>();
	
	private Date lastFinish = null;
	
	public List<String> getCriticalPath(String procIdent) throws DAOException{ 
		List<String> ret = new LinkedList<String>();
				
		this.processIdent = procIdent;
		System.out.println("dao: " + processDAO);
		System.out.println("ident: " + procIdent);
		Process proc = (Process) processDAO.retrieveBySecondaryKey(this.processIdent);
		ProcessModel pModel = proc.getTheProcessModel();
		
		this.mapActivitiesFromProcessModel(pModel);
		
		this.removeDecomposedActsFromPModel(pModel);
		
		this.discoverCriticalPath();
		
		Collection<AuxiliarActivity> allActs = this.tableAct.values();
		
		for(AuxiliarActivity auxAct: allActs){
			if(auxAct.getBeginIsCritical() || auxAct.getEndIsCritical()){
					ret.add(auxAct.getActivity().getIdent());
					Collection<String> decompIdents = this.getDecomposedIdents(auxAct.getActivity()); 
					if ( decompIdents != null ) 
						ret.addAll(decompIdents);
					//retAct.add(auxAct.getActivity());
			}			
		}
		
		boolean isEmpty = true;
		for (Iterator<String> iterator = ret.iterator(); iterator.hasNext();) {
			String object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	// m�todo criado pois o processDao estava null e pra n�o mexer no m�todo acima
	public List<String> getCriticalPath(String procIdent, IProcessDAO processDAO) throws DAOException{ 
		List<String> ret = new LinkedList<String>();
				
		this.processIdent = procIdent;
		System.out.println("dao: " + processDAO);
		System.out.println("ident: " + procIdent);
		Process proc = (Process) processDAO.retrieveBySecondaryKey(this.processIdent);
		ProcessModel pModel = proc.getTheProcessModel();
		
		this.mapActivitiesFromProcessModel(pModel);
		
		this.removeDecomposedActsFromPModel(pModel);
		
		this.discoverCriticalPath();
		
		Collection<AuxiliarActivity> allActs = this.tableAct.values();
		
		for(AuxiliarActivity auxAct: allActs){
			if(auxAct.getBeginIsCritical() || auxAct.getEndIsCritical()){
					ret.add(auxAct.getActivity().getIdent());
					Collection<String> decompIdents = this.getDecomposedIdents(auxAct.getActivity()); 
					if ( decompIdents != null ) 
						ret.addAll(decompIdents);
					//retAct.add(auxAct.getActivity());
			}			
		}
		
		boolean isEmpty = true;
		for (Iterator<String> iterator = ret.iterator(); iterator.hasNext();) {
			String object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	private void mapActivitiesFromProcessModel(ProcessModel pModel) throws DAOException {
		Collection acts = pModel.getTheActivity();

		for (Iterator iterator = acts.iterator(); iterator.hasNext();) {
			Activity act = (Activity) iterator.next();
			if(act != null){
				if(act instanceof Normal){
					Normal normalAct = (Normal)act;
					AuxiliarActivity auxAct = new AuxiliarActivity();
					auxAct.setActivity(normalAct);

					this.tableAct.put(auxAct.getActivity().getIdent(), auxAct);

					this.verifyCandidateToFinal(normalAct);
				}else if(act instanceof Decomposed){
					Decomposed decAct = (Decomposed) act;
					ProcessModel newPModel = decAct.getTheReferedProcessModel();

					this.mapActivitiesFromProcessModel(newPModel);
				}
			}
		}
	}
	
	private void verifyCandidateToFinal(Normal normalAct) {
		Date planEnd = normalAct.getPlannedEnd();
		
		if(this.lastFinish == null){
			this.lastFinish = planEnd;
			this.lastActs.add(normalAct);
		}else if(this.isSameTime(planEnd, this.lastFinish)){
			this.lastActs.add(normalAct);
		}else if(this.isAfter(planEnd, this.lastFinish)){
			this.lastActs.clear();
			this.lastFinish = planEnd;
			this.lastActs.add(normalAct);
		}
	}
	
	private boolean isSameTime(Date date1, Date date2){
		if(date1 == null || date2 == null){
			return false;
		}
		else {
			if(date1.getTime() == date2.getTime()){
				return true;
			}
			return false;
		}
	}
	
	private boolean isAfter(Date date1, Date date2){
		if(date1 == null){
			return false;
		}
		else {
			if(date2 == null){
				return true;
			}
			else{
				if(date1.getTime() > date2.getTime()){
					return true;
				}
				return false;
			}
		}
	}
	private void removeDecomposedActsFromPModel(ProcessModel pModel) throws DAOException {
		Collection<AuxiliarActivity> allActs = this.tableAct.values();
		
		for (Iterator iterator = allActs.iterator(); iterator.hasNext();) {
			AuxiliarActivity auxAct = (AuxiliarActivity) iterator.next();
			
			if(auxAct != null){
				Activity act = auxAct.getActivity();
				if(act != null){
					if(act instanceof Normal){
						Normal normalAct = (Normal)act;

						Collection<AuxiliarConnection> actSucessors = this.getActsSucessorsOfAnAct(normalAct);
					
						if(actSucessors != null){
							for(AuxiliarConnection auxCon: actSucessors){
								auxAct.insertActSucessor(auxCon);
							}
						}
					}
				}
			}
		}
	}
	public Collection<AuxiliarConnection> getActsSucessorsOfAnAct(Activity beforeAct) throws DAOException{
		Collection<AuxiliarConnection> ret = new HashSet<AuxiliarConnection>();
		
		Collection connections = this.getConnectionsTo(beforeAct);
		
		if(connections != null){
			for(Object objCon: connections){
				if (objCon == null) continue;

				Connection con = (Connection)objCon;

				//if(con instanceof Feedback) continue;
				
				String kindDep = "end-start";
				if(con instanceof Sequence) 
					kindDep = ((Sequence)con).getTheDependency().getKindDep();
				else if(con instanceof MultipleCon)
					kindDep = ((MultipleCon)con).getTheDependency().getKindDep();

				AuxiliarConnection sucCon = new AuxiliarConnection();
				sucCon.setKindDep(kindDep);
				
				if(beforeAct instanceof Normal){
					AuxiliarActivity auxAct = this.tableAct.get(beforeAct.getIdent());					
					
					sucCon.setActFrom(auxAct);
				}

				Collection acts = this.getSuccessors(con);

				if(acts != null){
					for(Object objAct: acts){
						if (objAct == null) continue;

						Activity act = (Activity) objAct;

						if(act instanceof Normal){
							AuxiliarActivity sucAct = this.tableAct.get(act.getIdent());

							if(sucAct != null){
								sucCon.setActTo(sucAct);

								ret.add(sucCon);
							}
						}else if(act instanceof Decomposed){
							Decomposed dec = (Decomposed) act;
							
							Collection<Activity> firstActsFromDec = this.getFirstActsFromPModel(dec.getTheReferedProcessModel());
							if(firstActsFromDec == null) continue;
							
							for(Activity actInsideDec: firstActsFromDec){
								AuxiliarActivity sucActInsideDec = this.tableAct.get(actInsideDec.getIdent());

								if(sucActInsideDec != null){
									AuxiliarConnection cloneConnection = sucCon.clone();
									
									cloneConnection.setActTo(sucActInsideDec);

									ret.add(cloneConnection);
								}
							}
						}
					}
				}
			}
		}else{ //Is the activity inside a decomposed?
			ProcessModel pModel = beforeAct.getTheProcessModel();
			
			Decomposed superDec = pModel.getTheDecomposed();
			if(superDec != null){
				Date beforeActPlannedEnd = null;
				
				if(beforeAct instanceof Normal){
					beforeActPlannedEnd = ( ( Normal ) beforeAct).getPlannedEnd();
				}else if(beforeAct instanceof Decomposed){
					beforeActPlannedEnd = 
						this.getLatestPlannedEndFromProcessModel(
								( ( Decomposed ) beforeAct).getTheReferedProcessModel()
								);
				}
				
				Date lastPlannedEndFromDecomposed = 
					this.getLatestPlannedEndFromProcessModel(superDec.getTheReferedProcessModel());
				
				//Activity is critical for the decomposed.
				if(this.isSameTime(beforeActPlannedEnd, lastPlannedEndFromDecomposed)){
					Collection<AuxiliarConnection> decSucessors = this.getActsSucessorsOfAnAct(superDec);
				
					if (decSucessors != null){
						for (AuxiliarConnection decSuc : decSucessors){
							if(decSuc.getActFrom() == null){
								if(beforeAct instanceof Normal){
									AuxiliarActivity auxAct = this.tableAct.get(beforeAct.getIdent());					
							
									decSuc.setActFrom(auxAct);
								}
							}
							ret.add(decSuc);
						}
					}
				}
			}	
		}
		
		boolean isEmpty = true;
		for (Iterator<AuxiliarConnection> iterator = ret.iterator(); iterator.hasNext();) {
			AuxiliarConnection object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	private Collection getConnectionsTo(Activity act){
		Collection connTo = new LinkedList();
		
		//Ignore Feedback connection
		Collection toSimpleCon = act.getToSimpleCon();
		if(toSimpleCon != null){
		    for(Object obj: toSimpleCon){
				if(obj == null) continue;
				
				if (obj instanceof Sequence) {
					Sequence seq = (Sequence) obj;
					Activity toAct = seq.getToActivity();
					if (toAct != null) connTo.add(obj);
				}
			}
		}
		
		if(act.getToJoin() != null)
		    connTo.addAll(act.getToJoin());
		if(act.getToBranch() != null)
		    connTo.addAll(act.getToBranch());

		boolean isEmpty = true;
		for (Iterator<Path> iterator = connTo.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}		
		
		return (isEmpty)? null:connTo;
	}

	private Collection getConnectionsFrom(Activity act) {
		Collection connFrom = new ArrayList();
		
		//Ignore Feedback connection
		Collection fromSimpleCon = act.getFromSimpleCon();
		if( (fromSimpleCon != null) || (! (fromSimpleCon.isEmpty()) )){
			for(Object obj: fromSimpleCon){
				if(obj == null) continue;
				
				if (obj instanceof Sequence) {
					Sequence seq = (Sequence) obj;
					Activity fromAct = seq.getFromActivity();
					if (fromAct != null) connFrom.add(obj);
				}
			}
		}
		
		connFrom.addAll(act.getFromJoin());
		connFrom.addAll(act.getFromBranchAND());
		Collection bctas = act.getTheBranchCondToActivity();
		Iterator iterBctas = bctas.iterator();
		while (iterBctas.hasNext()) {
			BranchCondToActivity bcta = (BranchCondToActivity) iterBctas.next();
			if (bcta.getTheBranchCond() != null)
				connFrom.add(bcta.getTheBranchCond());
		}

		boolean isEmpty = true;
		for (Iterator iterator = connFrom.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: connFrom;
	}

	private Collection getSuccessors(org.qrconsult.spm.model.connections.Connection conn){
		Collection succ = new LinkedList();
		if(conn instanceof Sequence){
			Sequence seq = (Sequence)conn;
			if(seq.getToActivity() != null)
			    succ.add(seq.getToActivity());
		}
		else if (conn instanceof Branch) {
			Branch branch = (Branch)conn;
			if(branch instanceof BranchAND){
				BranchAND bAND = (BranchAND)branch;
				if(bAND.getToActivity() != null)
				    succ.addAll(bAND.getToActivity());
				if(bAND.getToMultipleCon() != null){
					Collection multis = bAND.getToMultipleCon();
					Iterator iterMultis = multis.iterator();
					while (iterMultis.hasNext()) {
						MultipleCon multi = (MultipleCon) iterMultis.next();
						if(multi != null)
					    succ.addAll(this.getSuccessors(multi));
					}
				}
			}
			else{
				BranchCond bCond = (BranchCond)branch;
				Collection bctmc = bCond.getTheBranchCondToMultipleCon();
				Collection atbc = bCond.getTheBranchCondToActivity();
				Iterator iterMulti = bctmc.iterator(),
						 iterAct = atbc.iterator();
				while (iterMulti.hasNext()) {
					BranchCondToMultipleCon multi = (BranchCondToMultipleCon) iterMulti.next();
					if(multi.getTheMultipleCon() != null)
					    succ.addAll(this.getSuccessors(multi.getTheMultipleCon()));
				}
				while (iterAct.hasNext()) {
					BranchCondToActivity act = (BranchCondToActivity) iterAct.next();
					if(act.getTheActivity() != null)
					    succ.add(act.getTheActivity());
				}
			}
		}
		else if (conn instanceof Join) {
			Join join = (Join)conn;
			if(join.getToActivity() != null)
			    succ.add(join.getToActivity());
			if(join.getToMultipleCon() != null)
			    succ.addAll(this.getSuccessors(join.getToMultipleCon()));
		}
		return succ;
	}
	
	private Collection getPredecessors(Connection conn) {
		Collection pred = new LinkedList();
		if (conn instanceof Sequence) {
			Sequence seq = (Sequence) conn;
			Activity act = seq.getFromActivity();
			if (act != null)
				pred.add(act);

		} else if (conn instanceof Branch) {
			Branch branch = (Branch) conn;
			Activity act = branch.getFromActivity();
			if (act != null)
				pred.add(act);
			MultipleCon multi = branch.getFromMultipleConnection();
			if (multi != null)
				pred.add(multi);
		} else if (conn instanceof Join) {
			Join join = (Join) conn;
			pred.addAll(join.getFromActivity());
			pred.addAll(join.getFromMultipleCon());
		}
		return pred;
	}
	private Collection<Activity> getFirstActsFromPModel(ProcessModel pModel) throws DAOException {
		Collection<Activity> ret = new HashSet<Activity>();
	
		Collection acts = pModel.getTheActivity();
		
		for(Object obj: acts){
			if(obj != null){
				if(obj == null) continue;
				
				Activity act = (Activity) obj;
				
				Collection conFrom = this.getConnectionsFrom(act);
			
				if(conFrom == null) {
					if(act instanceof Normal){
						ret.add(act);
					}else if(act instanceof Decomposed){
						Decomposed dec = (Decomposed)act;
						
						Collection<Activity> decFirstActs = this.getFirstActsFromPModel(dec.getTheReferedProcessModel());
						
						if( (decFirstActs != null) && (! ( decFirstActs.isEmpty()) ) ){
							ret.addAll(decFirstActs);
						}
					}
				}
			}
		}
		
		boolean isEmpty = true;
		for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	public Date getLatestPlannedEndFromProcessModel(ProcessModel pModel){
		Collection actsFromPModel = pModel.getTheActivity();
		
		Date lastFinishFromPModel = null;
		
		for(Object obj: actsFromPModel){
			if(obj == null) continue;
			
			Activity act = (Activity)obj;
			
			Date planEnd = null;
			
			if(act instanceof Normal){
				Normal normalAct = (Normal) act;
				
				planEnd = normalAct.getPlannedEnd();
				
			}else if(act instanceof Decomposed){
				Decomposed decAct = (Decomposed)act;
				
				planEnd = 
					this.getLatestPlannedEndFromProcessModel(decAct.getTheReferedProcessModel());
			}
			
			if(lastFinishFromPModel == null){
				lastFinishFromPModel = planEnd;
			}else if(this.isAfter(planEnd, lastFinishFromPModel)){
				lastFinishFromPModel = planEnd;
			}
		}
		
		return lastFinishFromPModel;
	}
	
	private void discoverCriticalPath() {
		Collection<AuxiliarActivity> allActs = this.tableAct.values();
		
		for(Activity crit: this.lastActs){
			AuxiliarActivity auxAct = this.tableAct.get(crit.getIdent());
			
			auxAct.setBeginIsCritical(new Boolean(true));
			auxAct.setEndIsCritical(new Boolean(true));
			
			this.tableAct.put(crit.getIdent(), auxAct);
		}

		for(AuxiliarActivity auxAct: allActs){
			if(!auxAct.getBeginIsCritical() || !auxAct.getEndIsCritical()){
				List<Path> paths = new LinkedList<Path>();
				
				for(Activity crit: this.lastActs){
					List<Path> pathsBetweenActs = 
						this.getPathsBetweenActs(auxAct.getActivity(), crit);
					
					if((pathsBetweenActs != null) && (! ( pathsBetweenActs.isEmpty() ))){
						paths.addAll(pathsBetweenActs);
					}
				}
				
				for (Iterator iter = paths.iterator(); iter.hasNext();) {
					Path path = (Path) iter.next();
					
					if(path != null){
						this.updateTableActs(path);
					}
				}
			}			
		}
	}
	
	private List<Path> getPathsBetweenActs(Activity actSource, Activity actDest) {
		List<Path> ret = new LinkedList<Path>();
		
		if(actSource.getIdent().equals(actDest.getIdent())){
			Path retPath = new Path();
			retPath.addNode(actSource.getIdent(), null);
			
			ret.add(retPath);
			
			return ret;
		}
			
		AuxiliarActivity auxAct = this.tableAct.get(actSource.getIdent());
		
		if(auxAct == null) return null;
		
		Collection<AuxiliarConnection> pairActs = auxAct.getActSucessors();
		if(pairActs == null) return null;
		
		for(AuxiliarConnection pair: pairActs){
			String kindDep = pair.getKindDep();
			
			Activity nextAct = pair.actTo.getActivity();
				
			Path beforePath = new Path();
			beforePath.addNode(actSource.getIdent(), kindDep);
				
			Normal nextNormal = (Normal)nextAct;
					
			List<Path> nextPaths = this.getPathsBetweenActs(nextNormal, actDest);
			if(nextPaths != null) {
				for(Path path: nextPaths){
					Path clonePath = beforePath.clone();
					clonePath.addAll(path.getPath());
					
					ret.add(clonePath);
				}
			}
		}
		
		boolean isEmpty = true;
		for (Iterator<Path> iterator = ret.iterator(); iterator.hasNext();) {
			Path object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	private void updateTableActs(Path path) {
		List<Node> pathList = path.getPath();
		
		Collections.reverse(pathList);
		
		boolean endIsCritical = true;
		
		for(Node node: pathList){
			if(node.getConnection() == null) continue;
			
			AuxiliarActivity auxAct = this.tableAct.get(node.getIdent());
			
			if(node.getConnection().equals("end-start")){
				
				auxAct.setBeginIsCritical(new Boolean(true));
				auxAct.setEndIsCritical(new Boolean(true));

				endIsCritical = true;
				
			}else if(node.getConnection().equals("start-start")){

				auxAct.setBeginIsCritical(new Boolean(true));
				
				endIsCritical = false;
				
			}else if(node.getConnection().equals("end-end")){

				if(endIsCritical == true){
					
					auxAct.setBeginIsCritical(new Boolean(true));
					auxAct.setEndIsCritical(new Boolean(true));
					
				}else{
					break;
				}
			}
			
			this.tableAct.put(node.getIdent(), auxAct);
		}
	}
	
	private Collection<String> getDecomposedIdents(Activity activity) {
		Collection<String> ret = new HashSet<String>();
		
		ProcessModel pModel = activity.getTheProcessModel();
		while(pModel != null){
			Decomposed dec = pModel.getTheDecomposed();
			if(dec == null){
				pModel = null;
			}else{
				ret.add(dec.getIdent());
				pModel = dec.getTheProcessModel();
			}
		}
		
		boolean isEmpty = true;
		for (Iterator<String> iterator = ret.iterator(); iterator.hasNext();) {
			String object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}
	
	/*private Collection<Activity> getLatestActsFromPModel(ProcessModel pModel) throws DAOException {
		Collection<Activity> ret = new HashSet<Activity>();
		
		Collection acts = pModel.getTheActivity();
		
		for(Object obj: acts){
			if(obj != null){
				if(obj == null) continue;
				
				Activity act = (Activity) obj;
				Collection conTo = this.getConnectionsTo(act);
			
				if(conTo == null) {
					if(act instanceof Normal){
						ret.add(act);
					}else if(act instanceof Decomposed){
						Decomposed dec = (Decomposed)act;
						
						Collection<Activity> decLatestActs = this.getLatestActsFromPModel(dec.getTheReferedProcessModel());
						
						if( (decLatestActs != null) && (! ( decLatestActs.isEmpty()) ) ){
							ret.addAll(decLatestActs);
						}
					}
				}
			}
		}
		
		boolean isEmpty = true;
		for (Iterator iterator = ret.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			if(object != null){
				isEmpty = false;
				break;
			}
		}
		
		return (isEmpty)? null: ret;
	}*/
	
	public static void main(String[] args) throws InterruptedException, DAOException {
		CriticalPathMethod cpm = new CriticalPathMethod();
		List<String> ret = cpm.getCriticalPath("Controle_Acesso");
		
		System.out.println("Numero de atividades criticas: " + ret.size());
		for(String ident: ret){	
			System.out.println("Ident: "+ ident);
		}
		
		System.out.println("Fim...");
	}

	private class AuxiliarActivity{
		private Activity activity;
		private Boolean beginIsCritical = new Boolean(false);
		private Boolean endIsCritical = new Boolean(false);
		
		private Collection<AuxiliarConnection> actSucessors;
		private Collection<AuxiliarConnection> actPredecessors;
		
		public AuxiliarActivity() {
			this.activity = null;
			this.beginIsCritical = new Boolean(false);
			this.endIsCritical = new Boolean(false);
			
			this.actPredecessors = new HashSet<AuxiliarConnection>();
			this.actSucessors = new HashSet<AuxiliarConnection>();
		}
		
		public AuxiliarActivity(Activity act,
				Boolean beginIsCritical, Boolean endIsCritical) {
			this.activity = act;
			this.beginIsCritical = beginIsCritical;
			this.endIsCritical = endIsCritical;
			
			this.actPredecessors = new HashSet<AuxiliarConnection>();
			this.actSucessors = new HashSet<AuxiliarConnection>();
		}

		public Activity getActivity() {
			return activity;
		}
		public void setActivity(Activity activity) {
			this.activity = activity;
		}

		public Boolean getBeginIsCritical() {
			return beginIsCritical;
		}
		public void setBeginIsCritical(Boolean beginIsCritical) {
			this.beginIsCritical = beginIsCritical;
		}

		public Boolean getEndIsCritical() {
			return endIsCritical;
		}
		public void setEndIsCritical(Boolean endIsCritical) {
			this.endIsCritical = endIsCritical;
		}

		public Collection<AuxiliarConnection> getActPredecessors() {
			return actPredecessors;
		}
		public void setActPredecessors(Collection<AuxiliarConnection> actPredecessors) {
			this.actPredecessors = actPredecessors;
		}
		public void insertActPredecessor(AuxiliarConnection actPredecessor){
			if(!this.actPredecessors.contains(actPredecessor)){
				this.actPredecessors.add(actPredecessor);
			}
			
			if(!actPredecessor.getActFrom().getActSucessors().contains(actPredecessor)){
				actPredecessor.getActFrom().getActSucessors().add(actPredecessor);
			}
		}
		public void removeActPredecessor(AuxiliarConnection actPredecessor){
			if(this.actPredecessors.contains(actPredecessor)){
				this.actPredecessors.remove(actPredecessor);
			}
			
			if(actPredecessor.getActFrom().getActSucessors().contains(actPredecessor)){
				actPredecessor.getActFrom().getActSucessors().remove(actPredecessor);
			}
		}
		public void insertAllActPredecessors(Collection<AuxiliarConnection> actPredecessors){
			for(AuxiliarConnection actPredecessor: actPredecessors){
				this.insertActPredecessor(actPredecessor);
			}
		}

		public Collection<AuxiliarConnection> getActSucessors() {
			return actSucessors;
		}
		public void setActSucessors(Collection<AuxiliarConnection> actSucessors) {
			this.actSucessors = actSucessors;
		}
		public void insertActSucessor(AuxiliarConnection actSucessor){
			if(! (this.actSucessors.contains(actSucessor)) ){
				this.actSucessors.add(actSucessor);
			}
			
			if(! (actSucessor.getActTo().getActPredecessors().contains(actSucessor)) ){
				actSucessor.getActTo().getActPredecessors().add(actSucessor);
			}
		}
		public void removeActSucessor(AuxiliarConnection actSucessor){
			if(this.actSucessors.contains(actSucessor)){
				this.actSucessors.remove(actSucessor);
			}
			
			if(actSucessor.getActTo().getActPredecessors().contains(actSucessor)){
				actSucessor.getActTo().getActPredecessors().remove(actSucessor);
			}
		}
		public void insertAllActSucessors(Collection<AuxiliarConnection> actSucessors){
			for(AuxiliarConnection actSucessor: actSucessors){
				this.insertActSucessor(actSucessor);
			}
		}
		
		public boolean equals(Object other){
			if(this==other) return true;
			if(this.getActivity() == null) return false;
			if(this.getActivity().getIdent() == null) return false;
			if(!(other instanceof AuxiliarActivity)) return false;
			final AuxiliarActivity that = (AuxiliarActivity)other;
			if(that.getActivity() == null) return false;
			if(that.getActivity().getIdent() == null) return false;
			return this.getActivity().getIdent().equals(that.getActivity().getIdent());
		}

		public int hashCode(){
			return (this.getActivity() == null || 
					this.getActivity().getIdent() == null|| 
					this.getActivity().getIdent().equals("")) ? 
							System.identityHashCode(this) :
							this.getActivity().getIdent().hashCode();
		}
	}
	
	private class Node{
		private String ident;
		private String connection;
	
		public String getConnection() {
			return connection;
		}
		public void setConnection(String connection) {
			this.connection = connection;
		}
		public String getIdent() {
			return ident;
		}
		public void setIdent(String ident) {
			this.ident = ident;
		}
		public Node clone(){
			Node ret = new Node();
			ret.setConnection(this.getConnection().toString());
			ret.setIdent(this.getIdent().toString());
			return ret;
		}
	}
	
	private class Path{
		private List<Node> path = new LinkedList<Node>();
		
		public void addNode(Node node){
			path.add(node);
		}
		
		public void addNode(String ident, String connection){
			Node node = new Node();
			node.setIdent(ident);
			node.setConnection(connection);
			path.add(node);
		}
		
		public void addAll(List<Node> nodes){
			path.addAll(nodes);
		}
		
		public List<Node> getPath(){
			return path;
		}
		
		public Path clone(){
			Path ret = new Path();
			List<Node> clonePath = new LinkedList<Node>();
			for(Node origem: this.path){
				clonePath.add(origem.clone());
			}
			ret.addAll(clonePath);
			return ret;
		}
		
		public String toString(){
			String ret = "[";
			for(Node node: path){
				ret+= "("+node.getConnection()+","+node.getIdent()+") "; 
			}
			ret+="]";
			return ret;
		}
		
		public void removeFirstNode(){
			for (Iterator iterator = path.iterator(); iterator.hasNext();) {
				Node element = (Node) iterator.next();
				if(element != null){
					iterator.remove();
					break;
				}
			}
		}
		
		public Node getFirstNode(){
			for (Iterator iterator = path.iterator(); iterator.hasNext();) {
				Node element = (Node) iterator.next();
				if(element != null){
					return element;
				}
			}
			return null;
		}
		
		public Node getLastNode(){
			for (Iterator iterator = this.path.iterator(); iterator.hasNext();) {
				Node element = (Node) iterator.next();
				if(element != null){
					return element;
				}
			}
			return null;
		}
		
		public void updateLastNode(Node node){
			for (Iterator iterator = this.path.iterator(); iterator.hasNext();) {
				Node element = (Node) iterator.next();
				if((element != null) && (!iterator.hasNext())){
					iterator.remove();
					break;
				}
			}
			this.path.add(node);
		}
	}
	
	private class AuxiliarConnection{
		private String kindDep;
		private AuxiliarActivity actFrom;
		private AuxiliarActivity actTo;
	
		public AuxiliarConnection(){
			this.setKindDep("end-start");
			this.actFrom = null;
			this.actTo = null;
		}
		
		public String getKindDep() {
			return kindDep;
		}
		public void setKindDep(String kindDep) {
			this.kindDep = kindDep;
		}
		
		public AuxiliarActivity getActFrom() {
			return actFrom;
		}
		public void setActFrom(AuxiliarActivity actFrom) {
			this.actFrom = actFrom;
		}

		public AuxiliarActivity getActTo() {
			return actTo;
		}
		public void setActTo(AuxiliarActivity actTo) {
			this.actTo = actTo;
		}

		public boolean equals(Object other){
			if(this==other) return true;
			
			if(this.getActFrom() == null) return false;
			if(this.getActFrom().getActivity() == null) return false;
			if(this.getActFrom().getActivity().getIdent() == null) return false;
			
			if(this.getActTo() == null) return false;
			if(this.getActTo().getActivity() == null) return false;
			if(this.getActTo().getActivity().getIdent() == null) return false;
			
			if(!(other instanceof AuxiliarConnection)) return false;
			final AuxiliarConnection that = (AuxiliarConnection)other;
			
			if(that.getActFrom() == null) return false;
			if(that.getActFrom().getActivity() == null) return false;
			if(that.getActFrom().getActivity().getIdent() == null) return false;
			
			if(that.getActTo() == null) return false;
			if(that.getActTo().getActivity() == null) return false;
			if(that.getActTo().getActivity().getIdent() == null) return false;
			
			String thisActIdentFrom = this.getActFrom().getActivity().getIdent();
			String thisActIdentTo = this.getActTo().getActivity().getIdent();
			
			String thatActIdentFrom = that.getActFrom().getActivity().getIdent();
			String thatActIdentTo = that.getActTo().getActivity().getIdent();
			
			return (this.getKindDep().equals(that.getKindDep()) && 
					thisActIdentFrom.equals(thatActIdentFrom) && 
					thisActIdentTo.equals(thatActIdentTo));
		}

		public int hashCode(){
			return (this.getActFrom() == null || 
					this.getActFrom().getActivity() == null ||
					this.getActFrom().getActivity().getIdent() == null ||
					this.getActFrom().getActivity().getIdent().equals("") ||
					
					this.getActTo() == null || 
					this.getActTo().getActivity() == null ||
					this.getActTo().getActivity().getIdent() == null ||
					this.getActTo().getActivity().getIdent().equals("")) ? 
							
							System.identityHashCode(this) :
								this.getActFrom().getActivity().getIdent().hashCode()+
								this.getActTo().getActivity().getIdent().hashCode()*10;
		}
		
		public AuxiliarConnection clone(){
			AuxiliarConnection ret = new AuxiliarConnection();
			
			ret.setActFrom(this.getActFrom());
			ret.setActTo(this.getActTo());
			ret.setKindDep(this.getKindDep());
			
			return ret;
		}
	}
}