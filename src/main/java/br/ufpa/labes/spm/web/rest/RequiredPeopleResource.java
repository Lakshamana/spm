package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.repository.RequiredPeopleRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.RequiredPeople}. */
@RestController
@RequestMapping("/api")
public class RequiredPeopleResource {

  private final Logger log = LoggerFactory.getLogger(RequiredPeopleResource.class);

  private static final String ENTITY_NAME = "requiredPeople";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RequiredPeopleRepository requiredPeopleRepository;

  public RequiredPeopleResource(RequiredPeopleRepository requiredPeopleRepository) {
    this.requiredPeopleRepository = requiredPeopleRepository;
  }

  /**
   * {@code POST /required-people} : Create a new requiredPeople.
   *
   * @param requiredPeople the requiredPeople to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     requiredPeople, or with status {@code 400 (Bad Request)} if the requiredPeople has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/required-people")
  public ResponseEntity<RequiredPeople> createRequiredPeople(
      @RequestBody RequiredPeople requiredPeople) throws URISyntaxException {
    log.debug("REST request to save RequiredPeople : {}", requiredPeople);
    if (requiredPeople.getId() != null) {
      throw new BadRequestAlertException(
          "A new requiredPeople cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RequiredPeople result = requiredPeopleRepository.save(requiredPeople);
    return ResponseEntity.created(new URI("/api/required-people/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /required-people} : Updates an existing requiredPeople.
   *
   * @param requiredPeople the requiredPeople to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     requiredPeople, or with status {@code 400 (Bad Request)} if the requiredPeople is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the requiredPeople couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/required-people")
  public ResponseEntity<RequiredPeople> updateRequiredPeople(
      @RequestBody RequiredPeople requiredPeople) throws URISyntaxException {
    log.debug("REST request to update RequiredPeople : {}", requiredPeople);
    if (requiredPeople.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RequiredPeople result = requiredPeopleRepository.save(requiredPeople);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, requiredPeople.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /required-people} : get all the requiredPeople.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requiredPeople
   *     in body.
   */
  @GetMapping("/required-people")
  public List<RequiredPeople> getAllRequiredPeople() {
    log.debug("REST request to get all RequiredPeople");
    return requiredPeopleRepository.findAll();
  }

  /**
   * {@code GET /required-people/:id} : get the "id" requiredPeople.
   *
   * @param id the id of the requiredPeople to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     requiredPeople, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/required-people/{id}")
  public ResponseEntity<RequiredPeople> getRequiredPeople(@PathVariable Long id) {
    log.debug("REST request to get RequiredPeople : {}", id);
    Optional<RequiredPeople> requiredPeople = requiredPeopleRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(requiredPeople);
  }

  /**
   * {@code DELETE /required-people/:id} : delete the "id" requiredPeople.
   *
   * @param id the id of the requiredPeople to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/required-people/{id}")
  public ResponseEntity<Void> deleteRequiredPeople(@PathVariable Long id) {
    log.debug("REST request to delete RequiredPeople : {}", id);
    requiredPeopleRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
