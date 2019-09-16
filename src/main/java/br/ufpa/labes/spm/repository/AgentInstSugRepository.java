package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.AgentInstSug;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the AgentInstSug entity. */
@SuppressWarnings("unused")
@Repository
public interface AgentInstSugRepository extends JpaRepository<AgentInstSug, Long> {}
