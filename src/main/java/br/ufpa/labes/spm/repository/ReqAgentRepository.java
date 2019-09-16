package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ReqAgent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the ReqAgent entity. */
@SuppressWarnings("unused")
@Repository
public interface ReqAgentRepository extends JpaRepository<ReqAgent, Long> {}
