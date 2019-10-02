package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.InstantiationSuggestion;
import br.ufpa.labes.spm.repository.InstantiationSuggestionRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.InstantiationSuggestion}. */
@RestController
@RequestMapping("/api")
public class InstantiationSuggestionResource {

  private final Logger log = LoggerFactory.getLogger(InstantiationSuggestionResource.class);

  private static final String ENTITY_NAME = "instantiationSuggestion";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final InstantiationSuggestionRepository instantiationSuggestionRepository;

  public InstantiationSuggestionResource(
      InstantiationSuggestionRepository instantiationSuggestionRepository) {
    this.instantiationSuggestionRepository = instantiationSuggestionRepository;
  }

  /**
   * {@code POST /instantiation-suggestions} : Create a new instantiationSuggestion.
   *
   * @param instantiationSuggestion the instantiationSuggestion to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     instantiationSuggestion, or with status {@code 400 (Bad Request)} if the
   *     instantiationSuggestion has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/instantiation-suggestions")
  public ResponseEntity<InstantiationSuggestion> createInstantiationSuggestion(
      @RequestBody InstantiationSuggestion instantiationSuggestion) throws URISyntaxException {
    log.debug("REST request to save InstantiationSuggestion : {}", instantiationSuggestion);
    if (instantiationSuggestion.getId() != null) {
      throw new BadRequestAlertException(
          "A new instantiationSuggestion cannot already have an ID", ENTITY_NAME, "idexists");
    }
    InstantiationSuggestion result =
        instantiationSuggestionRepository.save(instantiationSuggestion);
    return ResponseEntity.created(new URI("/api/instantiation-suggestions/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /instantiation-suggestions} : Updates an existing instantiationSuggestion.
   *
   * @param instantiationSuggestion the instantiationSuggestion to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     instantiationSuggestion, or with status {@code 400 (Bad Request)} if the
   *     instantiationSuggestion is not valid, or with status {@code 500 (Internal Server Error)} if
   *     the instantiationSuggestion couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/instantiation-suggestions")
  public ResponseEntity<InstantiationSuggestion> updateInstantiationSuggestion(
      @RequestBody InstantiationSuggestion instantiationSuggestion) throws URISyntaxException {
    log.debug("REST request to update InstantiationSuggestion : {}", instantiationSuggestion);
    if (instantiationSuggestion.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    InstantiationSuggestion result =
        instantiationSuggestionRepository.save(instantiationSuggestion);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, instantiationSuggestion.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /instantiation-suggestions} : get all the instantiationSuggestions.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     instantiationSuggestions in body.
   */
  @GetMapping("/instantiation-suggestions")
  public List<InstantiationSuggestion> getAllInstantiationSuggestions() {
    log.debug("REST request to get all InstantiationSuggestions");
    return instantiationSuggestionRepository.findAll();
  }

  /**
   * {@code GET /instantiation-suggestions/:id} : get the "id" instantiationSuggestion.
   *
   * @param id the id of the instantiationSuggestion to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     instantiationSuggestion, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/instantiation-suggestions/{id}")
  public ResponseEntity<InstantiationSuggestion> getInstantiationSuggestion(@PathVariable Long id) {
    log.debug("REST request to get InstantiationSuggestion : {}", id);
    Optional<InstantiationSuggestion> instantiationSuggestion =
        instantiationSuggestionRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(instantiationSuggestion);
  }

  /**
   * {@code DELETE /instantiation-suggestions/:id} : delete the "id" instantiationSuggestion.
   *
   * @param id the id of the instantiationSuggestion to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/instantiation-suggestions/{id}")
  public ResponseEntity<Void> deleteInstantiationSuggestion(@PathVariable Long id) {
    log.debug("REST request to delete InstantiationSuggestion : {}", id);
    instantiationSuggestionRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
