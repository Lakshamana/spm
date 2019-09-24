package br.ufpa.labes.spm.service.impl;

import java.util.List;


import br.ufpa.labes.spm.repository.interfaces.IUserDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IAuthorDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IOrganizationDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IPersonDAO;
import br.ufpa.labes.spm.exceptions.SPMBusinessException;
import br.ufpa.labes.spm.exceptions.SPMException;
import br.ufpa.labes.spm.exceptions.SPMInfraException;
import br.ufpa.labes.spm.domain.Author;
import br.ufpa.labes.spm.domain.Organization;
import br.ufpa.labes.spm.domain.Person;
import br.ufpa.labes.spm.domain.User;
import br.ufpa.labes.spm.service.interfaces.AccountServices;
import br.ufpa.labes.spm.util.PagingContext;
import br.ufpa.labes.spm.util.SortCriteria;


public class AccountServicesImpl implements AccountServices {

	IUserDAO userDAO;
	IAuthorDAO authorDAO;
	IPersonDAO personDAO;
	IOrganizationDAO organizationDAO;

	@Override
	public Person createPerson(String username, String password,
			String name, String gender, String email, String interests, String city, String country) throws SPMException {
		if (checkUsernameAvailability(username)) {
			User user = new User(username, password);
			userDAO.daoSave(user);

			Person person = new Person(user);
			person.setName(name);
			person.setGender(gender);
			person.setEmail(email);
			person.setInterests(interests);
			person.setCity(city);
			person.setCountry(country);

			return personDAO.daoSave(person);
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
			userDAO.daoSave(user);

			Organization organization = new Organization(user);
			organization.setName(name);
			organization.setInterests(interests);
			organization.setEmail(email);
			organization.setInterests(interests);
			organization.setCity(city);
			organization.setCountry(country);

			return organizationDAO.daoSave(organization);
		}
		else {
			throw new SPMBusinessException("Username already exists.");
		}
	}

	@Override
	public boolean checkUsernameAvailability(String username) throws SPMException {
		try {
			return userDAO.retrieve(username) == null;
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	};

	@Override
	public Person retrievePerson(String uid) throws SPMException {
		try {
			return personDAO.retrieve(uid);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public Organization retrieveOrganization(String uid) throws SPMException {
		try {
			return organizationDAO.retrieve(uid);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public Person deletePerson(String uid) throws SPMException {
		try {
			Person person = personDAO.retrieve(uid);
			return personDAO.daoDelete(person);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public Organization deleteOrganization(String uid) throws SPMException {
		try {
			Organization organization = organizationDAO.retrieve(uid);
			return organizationDAO.daoDelete(organization);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public void updatePerson(Person person) throws SPMException {
		try {
			personDAO.update(person);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public void updateOrganization(Organization organization) throws SPMException {
		try {
			organizationDAO.update(organization);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public List<Author> searchAuthor(Author searchCriteria) throws SPMException {
		try {
			return authorDAO.retrieveByCriteria(searchCriteria);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public List<Author> searchAuthor(Author searchCriteria,
			SortCriteria sortCriteria) throws SPMException {
		try {
			return authorDAO.retrieveByCriteria(searchCriteria, sortCriteria);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public List<Author> searchAuthor(Author searchCriteria, SortCriteria sortCriteria, PagingContext paging) throws SPMException {
		try {
			return authorDAO.retrieveByCriteria(searchCriteria, sortCriteria, paging);
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

	@Override
	public boolean sendMessageToAuthor(String fromUid, String toUid, String message) throws SPMException {
		try {
			Author fromAuthor = authorDAO.retrieve(fromUid);
			Author toAuthor = authorDAO.retrieve(toUid);

			fromAuthor.sendMessageTo(toAuthor, message);

			authorDAO.update(fromAuthor);
			authorDAO.update(toAuthor);

			return true;
		} catch (Exception e) {
			throw new SPMInfraException(e);
		}
	}

}
