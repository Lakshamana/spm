package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.repository.SubroutineRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Subroutine}. */
@RestController
@RequestMapping("/api")
public class SubroutineResource {

  private final Logger log = LoggerFactory.getLogger(SubroutineResource.class);

  private static final String ENTITY_NAME = "subroutine";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SubroutineRepository subroutineRepository;

  public SubroutineResource(SubroutineRepository subroutineRepository) {
    this.subroutineRepository = subroutineRepository;
  }

  /**
   * {@code POST /subroutines} : Create a new subroutine.
   *
   * @param subroutine the subroutine to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     subroutine, or with status {@code 400 (Bad Request)} if the subroutine has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/subroutines")
  public ResponseEntity<Subroutine> createSubroutine(@RequestBody Subroutine subroutine)
      throws URISyntaxException {
    log.debug("REST request to save Subroutine : {}", subroutine);
    if (subroutine.getId() != null) {
      throw new BadRequestAlertException(
          "A new subroutine cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Subroutine result = subroutineRepository.save(subroutine);
    return ResponseEntity.created(new URI("/api/subroutines/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /subroutines} : Updates an existing subroutine.
   *
   * @param subroutine the subroutine to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     subroutine, or with status {@code 400 (Bad Request)} if the subroutine is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the subroutine couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/subroutines")
  public ResponseEntity<Subroutine> updateSubroutine(@RequestBody Subroutine subroutine)
      throws URISyntaxException {
    log.debug("REST request to update Subroutine : {}", subroutine);
    if (subroutine.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Subroutine result = subroutineRepository.save(subroutine);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, subroutine.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /subroutines} : get all the subroutines.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subroutines in
   *     body.
   */
  @GetMapping("/subroutines")
  public List<Subroutine> getAllSubroutines(@RequestParam(required = false) String filter) {
    if ("theautomatic-is-null".equals(filter)) {
      log.debug("REST request to get all Subroutines where theAutomatic is null");
      return StreamSupport.stream(subroutineRepository.findAll().spliterator(), false)
          .filter(subroutine -> subroutine.getTheAutomatic() == null)
          .collect(Collectors.toList());
    }
    log.debug("REST request to get all Subroutines");
    return subroutineRepository.findAll();
  }

  /**
   * {@code GET /subroutines/:id} : get the "id" subroutine.
   *
   * @param id the id of the subroutine to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subroutine,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/subroutines/{id}")
  public ResponseEntity<Subroutine> getSubroutine(@PathVariable Long id) {
    log.debug("REST request to get Subroutine : {}", id);
    Optional<Subroutine> subroutine = subroutineRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(subroutine);
  }

  /**
   * {@code DELETE /subroutines/:id} : delete the "id" subroutine.
   *
   * @param id the id of the subroutine to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/subroutines/{id}")
  public ResponseEntity<Void> deleteSubroutine(@PathVariable Long id) {
    log.debug("REST request to delete Subroutine : {}", id);
    subroutineRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
