package br.ufpa.labes.spm.repository.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.qrconsult.spm.annotations.Criteria;
import org.qrconsult.spm.annotations.EnumCriteriaType;
import br.ufpa.labes.spm.repository.impl.agent.AgentDAO;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Agent;
import org.qrconsult.spm.util.PagingContext;
import org.qrconsult.spm.util.SortCriteria;
import org.qrconsult.spm.util.ident.ConversorDeIdent;
import org.qrconsult.spm.util.ident.SemCaracteresEspeciais;
import org.qrconsult.spm.util.ident.TrocaEspacoPorPonto;

public abstract class BaseDAO<T, PK> implements IBaseDAO<T, PK> {

	@PersistenceContext(unitName = "SPMPU")
	private EntityManager em;

	private Class<T> businessClass;

	protected BaseDAO(Class<T> businessClass) {
		this.businessClass = businessClass;
	}

	@Override
	public T save(T object) {
		this.getPersistenceContext().persist(object);
		return object;
	}

	@Override
	public T update(T object) {
		this.getPersistenceContext().merge(object);
		return object;
	}

	public List<T> retrieveByCriteria(T searchCriteria) {
		return retrieveByCriteria(searchCriteria, null, null);
	}

	public List<T> retrieveByCriteria(T searchCriteria, SortCriteria sortCriteria) {
		return retrieveByCriteria(searchCriteria, sortCriteria, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> retrieveByCriteria(T searchCriteria, SortCriteria sortCriteria, PagingContext paging) {
		ArrayList<Field> criteria = new ArrayList<Field>();
		Field[] fields = this.getBusinessClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(Criteria.class) != null)
				criteria.add(field);
		}

		String queryStr = "from " + this.getBusinessClass().getName() + " as obj " + "where true = true ";

		for (Field field : criteria) {
			try {
				String fieldName = field.getName();
				Object fieldValue = field.get(criteria);

				if (fieldValue != null) {
					queryStr += "and obj." + fieldName + " "
							+ field.getAnnotation(Criteria.class).type().getFormattedText() + " " + ":" + fieldName
							+ " ";
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		if (sortCriteria != null) {
			Collection<SortCriteria.SortEntry> entries = sortCriteria.getEntries().values();

			if (entries.size() > 0) {
				boolean isSorting = false;
				String orderByClause = "order by ";

				Iterator<SortCriteria.SortEntry> entryIterator = entries.iterator();

				while (entryIterator.hasNext()) {
					SortCriteria.SortEntry entry = entryIterator.next();
					String fieldName = entry.getFieldName();

					switch (entry.getType()) {
					case SortCriteria.SortEntry.TYPE_ASCENDING:
						orderByClause += "obj." + fieldName + " asc";
						isSorting = true;
						break;

					case SortCriteria.SortEntry.TYPE_DESCENDING:
						orderByClause += "obj." + fieldName + " desc";
						isSorting = true;
						break;
					}

					if (entryIterator.hasNext())
						orderByClause += ", ";
				}

				if (isSorting)
					queryStr += orderByClause;
			}
		}

		Query query = getPersistenceContext().createQuery(queryStr);

		for (Field field : criteria) {
			try {
				String fieldName = field.getName();
				Object fieldValue = field.get(criteria);

				query.setParameter(fieldName,
						(field.getAnnotation(Criteria.class).type() == EnumCriteriaType.LIKE ? "%" + fieldValue + "%"
								: fieldValue));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		if (paging != null) {
			int firstResult = (paging.getPageNumber() - 1) * paging.getResultsPerPage();

			query.setFirstResult(firstResult);
			query.setMaxResults(paging.getResultsPerPage());
		}

		return query.getResultList();
	}

	@Override
	public T retrieve(PK key) {
		return this.getPersistenceContext().find(this.getBusinessClass(), key);
	}

	@Override
	public Class<T> getBusinessClass() {
		return this.businessClass;
	}

	@Override
	public T delete(T object) {
		this.getPersistenceContext().remove(object);
		return object;
	};

	@Override
	public EntityManager getPersistenceContext() {
		return em;
	}

	public T retrieveBySecondaryKey(String ident) {
		Query query = this.getPersistenceContext()
				.createQuery("FROM " + businessClass.getName() + " as obj WHERE obj.ident = :ident");
		query.setParameter("ident", ident);
		List retorno = (List) query.getResultList();
		if (retorno != null) {
			if (!retorno.isEmpty()) {
				// System.out.println("caiu na consulta"+retorno.get(0));
				return (T) retorno.get(0);
			} else {
				return null;
			}
		} else
			return null;
	}

	@Override
	public String generateIdent(String oldIdent) {
		return ConversorDeIdent.de(oldIdent).para(new SemCaracteresEspeciais()).para(new TrocaEspacoPorPonto()).ident();
	}

	@Override
	public String generateIdent(String oldIdent, T t) {
		oldIdent = this.concatOidOnString(oldIdent, t);
		return ConversorDeIdent.de(oldIdent).para(new SemCaracteresEspeciais()).para(new TrocaEspacoPorPonto()).ident();
	}

	private String concatOidOnString(String oldIdent, T t) {
		Method method = null;
		String oid = "";
		try {
			method = t.getClass().getMethod("getId", new Class<?>[0]);
			oid = method.invoke(t, new Object[0]).toString();
			oid = oid.concat(" " + oldIdent);
		} catch (NoSuchMethodException e) {
			System.out.println("O método oid não foi encontrado para a entidade " + t.getClass().getSimpleName());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return oid;

	}

	public static void main(String[] args) {
		Agent a = new Agent();
		a.setId(336L);
		AgentDAO agentDAO = new AgentDAO();
		System.out.println(
				agentDAO.generateIdent("Template - Plano de Gerência de Documentos e Plano de Comunicação", a));
	}
}
