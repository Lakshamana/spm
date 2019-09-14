package br.ufpa.labes.spm.repository.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.processModels.ProcessDAO;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.repository.interfaces.IHelpTopicDAO;
import br.ufpa.labes.spm.repository.interfaces.IReportDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.IProcessDAO;
import org.qrconsult.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.Group;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.HelpTopic;
import br.ufpa.labes.spm.domain.KnowledgeItem;
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.InvolvedArtifacts;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqGroup;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.domain.ArtifactMetric;
import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;
import org.qrconsult.spm.services.impl.CriticalPathMethod;

public class HelpTopicDAO extends BaseDAO<HelpTopic, String> implements IHelpTopicDAO{

	public HelpTopicDAO(){
		super(HelpTopic.class);
	}

	protected HelpTopicDAO(Class<HelpTopic> businessClass) {
		super(businessClass);
	}

	@PersistenceContext(unitName = "SPMPU")
	private EntityManager em;

	IHelpTopicDAO helpTopicDAO;

	@Override
	public List<HelpTopic> getHelpTopics() {
		String queryStr = "from " + HelpTopic.class.getName() + " as help where help.father is null";

		Query query = getPersistenceContext().createQuery(queryStr);

		List<HelpTopic> helpTopics = query.getResultList();
		System.out.println(helpTopics);
		return helpTopics;
	}

	@Override
	public long countTopics() {
		String queryStr = "select count(help) from " + HelpTopic.class.getName() + " as help where help.father is null";

		Query query = getPersistenceContext().createQuery(queryStr);
		return (Long) query.getSingleResult();
	}
}
