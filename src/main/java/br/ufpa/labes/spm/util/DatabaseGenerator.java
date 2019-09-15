package br.ufpa.labes.spm.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DatabaseGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SPMPU");
		EntityManager em = emf.createEntityManager();
		em.close();
		emf.close();

	}

}
