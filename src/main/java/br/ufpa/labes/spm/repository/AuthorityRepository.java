package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA repository for the {@link Authority} entity. */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
