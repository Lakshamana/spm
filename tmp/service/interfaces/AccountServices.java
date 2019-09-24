package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.exceptions.SPMException;
import br.ufpa.labes.spm.domain.Author;
import br.ufpa.labes.spm.domain.Organization;
import br.ufpa.labes.spm.domain.Person;
import br.ufpa.labes.spm.util.PagingContext;
import br.ufpa.labes.spm.util.SortCriteria;


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
