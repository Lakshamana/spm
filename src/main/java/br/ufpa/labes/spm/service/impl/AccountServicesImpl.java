package org.qrconsult.spm.services.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.qrconsult.spm.dataAccess.interfaces.IUserDAO;
import org.qrconsult.spm.dataAccess.interfaces.people.IAuthorDAO;
import org.qrconsult.spm.dataAccess.interfaces.people.IOrganizationDAO;
import org.qrconsult.spm.dataAccess.interfaces.people.IPersonDAO;
import org.qrconsult.spm.exceptions.SPMBusinessException;
import org.qrconsult.spm.exceptions.SPMException;
import org.qrconsult.spm.exceptions.SPMInfraException;
import org.qrconsult.spm.model.people.Author;
import org.qrconsult.spm.model.people.Organization;
import org.qrconsult.spm.model.people.Person;
import org.qrconsult.spm.model.security.User;
import org.qrconsult.spm.services.interfaces.AccountServices;
import org.qrconsult.spm.util.PagingContext;
import org.qrconsult.spm.util.SortCriteria;


@Stateless
public class AccountServicesImpl implements AccountServices {
	
	@EJB
	IUserDAO userDao;
	@EJB
	IAuthorDAO authorDao;
	@EJB
	IPersonDAO personDao;
	@EJB
	IOrganizationDAO organizationDao;

	@Override
	public Person createPerson(String username, String password, 
			String name, String gender, String email, String interests, String city, String country) throws SPMException {
		if (checkUsernameAvailability(username)) {
			User user = new User(username, password);
			userDao.save(user);
			
			Person person = new Person(user);
			person.setName(name);
			person.setGender(gender);
			person.setEmail(email);
			person.setInterests(interests);
			person.setCity(city);
			person.setCountry(country);
			
			return personDao.save(person);
		}
		else {
			throw new SPMBusinessException("Username already exists.");
		}
	}
	
	@Override
	public Organization createOrganization(String username, String password,
			String name, String domain, String email, String interests,
			String city, String country) throws SPMException {
		
		if (checkUsernameAvailability(username)) {
			User user = new User(username, password);
			userDao.save(user);
			
			Organization organization = new Organization(user);
			organization.setName(name);
			organization.setInterests(interests);
			organization.setEmail(email);
			organization.setInterests(interests);
			organization.setCity(city);
			organization.setCountry(country);
			
			return organizationDao.save(organization);
		}
		else {
			throw new SPMBusinessException("Username already exists.");
		}
	}
	
	@Override
	public boolean checkUsernameAvailability(String username) throws SPMException {
		try {
			return userDao.retrieve(username) == null;
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	};
	
	@Override
	public Person retrievePerson(String uid) throws SPMException {
		try {
			return personDao.retrieve(uid);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public Organization retrieveOrganization(String uid) throws SPMException {
		try {
			return organizationDao.retrieve(uid);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public Person deletePerson(String uid) throws SPMException {
		try {
			Person person = personDao.retrieve(uid);
			return personDao.delete(person);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public Organization deleteOrganization(String uid) throws SPMException {
		try {
			Organization organization = organizationDao.retrieve(uid);
			return organizationDao.delete(organization);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public void updatePerson(Person person) throws SPMException {
		try {
			personDao.update(person);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public void updateOrganization(Organization organization) throws SPMException {
		try {
			organizationDao.update(organization);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public List<Author> searchAuthor(Author searchCriteria) throws SPMException {
		try {
			return authorDao.retrieveByCriteria(searchCriteria);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public List<Author> searchAuthor(Author searchCriteria,
			SortCriteria sortCriteria) throws SPMException {
		try {
			return authorDao.retrieveByCriteria(searchCriteria, sortCriteria);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public List<Author> searchAuthor(Author searchCriteria, SortCriteria sortCriteria, PagingContext paging) throws SPMException {
		try {
			return authorDao.retrieveByCriteria(searchCriteria, sortCriteria, paging);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}
	
	@Override
	public boolean sendMessageToAuthor(String fromUid, String toUid, String message) throws SPMException {
		try {
			Author fromAuthor = authorDao.retrieve(fromUid);
			Author toAuthor = authorDao.retrieve(toUid);
			
			fromAuthor.sendMessageTo(toAuthor, message);
			
			authorDao.update(fromAuthor);
			authorDao.update(toAuthor);
			
			return true;
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

}
