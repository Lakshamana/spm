package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ChatLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Spring Data repository for the ChatLog entity. */
@Repository
public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {

  @Query(
      value =
          "select distinct chatLog from ChatLog chatLog left join fetch chatLog.involvedAgentsInChats",
      countQuery = "select count(distinct chatLog) from ChatLog chatLog")
  Page<ChatLog> findAllWithEagerRelationships(Pageable pageable);

  @Query(
      "select distinct chatLog from ChatLog chatLog left join fetch chatLog.involvedAgentsInChats")
  List<ChatLog> findAllWithEagerRelationships();

  @Query(
      "select chatLog from ChatLog chatLog left join fetch chatLog.involvedAgentsInChats where chatLog.id =:id")
  Optional<ChatLog> findOneWithEagerRelationships(@Param("id") Long id);
}
