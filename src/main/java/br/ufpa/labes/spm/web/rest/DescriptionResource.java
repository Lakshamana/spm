package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Description;
import br.ufpa.labes.spm.repository.DescriptionRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Description}. */
@RestController
@RequestMapping("/api")
public class DescriptionResource {

  private final Logger log = LoggerFactory.getLogger(DescriptionResource.class);

  private static final String ENTITY_NAME = "description";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final DescriptionRepository descriptionRepository;

  public DescriptionResource(DescriptionRepository descriptionRepository) {
    this.descriptionRepository = descriptionRepository;
  }

  /**
   * {@code POST /descriptions} : Create a new description.
   *
   * @param description the description to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     description, or with status {@code 400 (Bad Request)} if the description has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/descriptions")
  public ResponseEntity<Description> createDescription(@RequestBody Description description)
      throws URISyntaxException {
    log.debug("REST request to save Description : {}", description);
    if (description.getId() != null) {
      throw new BadRequestAlertException(
          "A new description cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Description result = descriptionRepository.save(description);
    return ResponseEntity.created(new URI("/api/descriptions/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /descriptions} : Updates an existing description.
   *
   * @param description the description to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     description, or with status {@code 400 (Bad Request)} if the description is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the description couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/descriptions")
  public ResponseEntity<Description> updateDescription(@RequestBody Description description)
      throws URISyntaxException {
    log.debug("REST request to update Description : {}", description);
    if (description.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Description result = descriptionRepository.save(description);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, description.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /descriptions} : get all the descriptions.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of descriptions in
   *     body.
   */
  @GetMapping("/descriptions")
  public List<Description> getAllDescriptions() {
    log.debug("REST request to get all Descriptions");
    return descriptionRepository.findAll();
  }

  /**
   * {@code GET /descriptions/:id} : get the "id" description.
   *
   * @param id the id of the description to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the description,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/descriptions/{id}")
  public ResponseEntity<Description> getDescription(@PathVariable Long id) {
    log.debug("REST request to get Description : {}", id);
    Optional<Description> description = descriptionRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(description);
  }

  /**
   * {@code DELETE /descriptions/:id} : delete the "id" description.
   *
   * @param id the id of the description to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/descriptions/{id}")
  public ResponseEntity<Void> deleteDescription(@PathVariable Long id) {
    log.debug("REST request to delete Description : {}", id);
    descriptionRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
