package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.RequiredPeopleService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.RequiredPeopleDTO;

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

  private final RequiredPeopleService requiredPeopleService;

  public RequiredPeopleResource(RequiredPeopleService requiredPeopleService) {
    this.requiredPeopleService = requiredPeopleService;
  }

  /**
   * {@code POST /required-people} : Create a new requiredPeople.
   *
   * @param requiredPeopleDTO the requiredPeopleDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     requiredPeopleDTO, or with status {@code 400 (Bad Request)} if the requiredPeople has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/required-people")
  public ResponseEntity<RequiredPeopleDTO> createRequiredPeople(
      @RequestBody RequiredPeopleDTO requiredPeopleDTO) throws URISyntaxException {
    log.debug("REST request to save RequiredPeople : {}", requiredPeopleDTO);
    if (requiredPeopleDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new requiredPeople cannot already have an ID", ENTITY_NAME, "idexists");
    }
    RequiredPeopleDTO result = requiredPeopleService.save(requiredPeopleDTO);
    return ResponseEntity.created(new URI("/api/required-people/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /required-people} : Updates an existing requiredPeople.
   *
   * @param requiredPeopleDTO the requiredPeopleDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     requiredPeopleDTO, or with status {@code 400 (Bad Request)} if the requiredPeopleDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the requiredPeopleDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/required-people")
  public ResponseEntity<RequiredPeopleDTO> updateRequiredPeople(
      @RequestBody RequiredPeopleDTO requiredPeopleDTO) throws URISyntaxException {
    log.debug("REST request to update RequiredPeople : {}", requiredPeopleDTO);
    if (requiredPeopleDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    RequiredPeopleDTO result = requiredPeopleService.save(requiredPeopleDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, requiredPeopleDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /required-people} : get all the requiredPeople.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requiredPeople
   *     in body.
   */
  @GetMapping("/required-people")
  public List<RequiredPeopleDTO> getAllRequiredPeople() {
    log.debug("REST request to get all RequiredPeople");
    return requiredPeopleService.findAll();
  }

  /**
   * {@code GET /required-people/:id} : get the "id" requiredPeople.
   *
   * @param id the id of the requiredPeopleDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     requiredPeopleDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/required-people/{id}")
  public ResponseEntity<RequiredPeopleDTO> getRequiredPeople(@PathVariable Long id) {
    log.debug("REST request to get RequiredPeople : {}", id);
    Optional<RequiredPeopleDTO> requiredPeopleDTO = requiredPeopleService.findOne(id);
    return ResponseUtil.wrapOrNotFound(requiredPeopleDTO);
  }

  /**
   * {@code DELETE /required-people/:id} : delete the "id" requiredPeople.
   *
   * @param id the id of the requiredPeopleDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/required-people/{id}")
  public ResponseEntity<Void> deleteRequiredPeople(@PathVariable Long id) {
    log.debug("REST request to delete RequiredPeople : {}", id);
    requiredPeopleService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
