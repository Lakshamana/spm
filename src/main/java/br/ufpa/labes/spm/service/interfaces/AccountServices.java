package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.exceptions.SPMException;
import org.qrconsult.spm.model.people.Author;
import org.qrconsult.spm.model.people.Organization;
import org.qrconsult.spm.model.people.Person;
import org.qrconsult.spm.util.PagingContext;
import org.qrconsult.spm.util.SortCriteria;


@Remote
public interface AccountServices {
	
	// Creation
	public Person createPerson(String username, String password, 
			String name, String gender, String email, String interests, String city, String country) throws SPMException;
	
	public Organization createOrganization(String username, String password,
			String name, String domain, String email, String interests, String city, String country) throws SPMException;
	
	public boolean checkUsernameAvailability(String username) throws SPMException;
	
	// Retrieval
	public Person retrievePerson(String uid) throws SPMException;
	
	public Organization retrieveOrganization(String uid) throws SPMException;
	
	// Deletes
	public Person deletePerson(String uid) throws SPMException;
	
	public Organization deleteOrganization(String uid) throws SPMException;
	
	// Updates
	public void updatePerson(Person person) throws SPMException;

	public void updateOrganization(Organization organization) throws SPMException;
	
	// Search
	public List<Author> searchAuthor(Author searchCriteria) throws SPMException;
	public List<Author> searchAuthor(Author searchCriteria, SortCriteria sortCriteria) throws SPMException;
	public List<Author> searchAuthor(Author searchCriteria, SortCriteria sortCriteria, PagingContext paging) throws SPMException;
	
	// Others
	public boolean sendMessageToAuthor(String fromUid, String toUid, String message) throws SPMException;

}
