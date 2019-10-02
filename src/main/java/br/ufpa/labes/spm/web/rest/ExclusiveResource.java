package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.repository.ExclusiveRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Exclusive}. */
@RestController
@RequestMapping("/api")
public class ExclusiveResource {

  private final Logger log = LoggerFactory.getLogger(ExclusiveResource.class);

  private static final String ENTITY_NAME = "exclusive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ExclusiveRepository exclusiveRepository;

  public ExclusiveResource(ExclusiveRepository exclusiveRepository) {
    this.exclusiveRepository = exclusiveRepository;
  }

  /**
   * {@code POST /exclusives} : Create a new exclusive.
   *
   * @param exclusive the exclusive to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     exclusive, or with status {@code 400 (Bad Request)} if the exclusive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/exclusives")
  public ResponseEntity<Exclusive> createExclusive(@RequestBody Exclusive exclusive)
      throws URISyntaxException {
    log.debug("REST request to save Exclusive : {}", exclusive);
    if (exclusive.getId() != null) {
      throw new BadRequestAlertException(
          "A new exclusive cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Exclusive result = exclusiveRepository.save(exclusive);
    return ResponseEntity.created(new URI("/api/exclusives/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /exclusives} : Updates an existing exclusive.
   *
   * @param exclusive the exclusive to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     exclusive, or with status {@code 400 (Bad Request)} if the exclusive is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the exclusive couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/exclusives")
  public ResponseEntity<Exclusive> updateExclusive(@RequestBody Exclusive exclusive)
      throws URISyntaxException {
    log.debug("REST request to update Exclusive : {}", exclusive);
    if (exclusive.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Exclusive result = exclusiveRepository.save(exclusive);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, exclusive.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /exclusives} : get all the exclusives.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exclusives in
   *     body.
   */
  @GetMapping("/exclusives")
  public List<Exclusive> getAllExclusives() {
    log.debug("REST request to get all Exclusives");
    return exclusiveRepository.findAll();
  }

  /**
   * {@code GET /exclusives/:id} : get the "id" exclusive.
   *
   * @param id the id of the exclusive to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exclusive, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/exclusives/{id}")
  public ResponseEntity<Exclusive> getExclusive(@PathVariable Long id) {
    log.debug("REST request to get Exclusive : {}", id);
    Optional<Exclusive> exclusive = exclusiveRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(exclusive);
  }

  /**
   * {@code DELETE /exclusives/:id} : delete the "id" exclusive.
   *
   * @param id the id of the exclusive to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/exclusives/{id}")
  public ResponseEntity<Void> deleteExclusive(@PathVariable Long id) {
    log.debug("REST request to delete Exclusive : {}", id);
    exclusiveRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
