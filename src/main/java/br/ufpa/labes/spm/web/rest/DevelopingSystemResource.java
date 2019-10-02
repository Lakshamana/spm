package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.DevelopingSystem;
import br.ufpa.labes.spm.repository.DevelopingSystemRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.DevelopingSystem}. */
@RestController
@RequestMapping("/api")
public class DevelopingSystemResource {

  private final Logger log = LoggerFactory.getLogger(DevelopingSystemResource.class);

  private static final String ENTITY_NAME = "developingSystem";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final DevelopingSystemRepository developingSystemRepository;

  public DevelopingSystemResource(DevelopingSystemRepository developingSystemRepository) {
    this.developingSystemRepository = developingSystemRepository;
  }

  /**
   * {@code POST /developing-systems} : Create a new developingSystem.
   *
   * @param developingSystem the developingSystem to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     developingSystem, or with status {@code 400 (Bad Request)} if the developingSystem has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/developing-systems")
  public ResponseEntity<DevelopingSystem> createDevelopingSystem(
      @RequestBody DevelopingSystem developingSystem) throws URISyntaxException {
    log.debug("REST request to save DevelopingSystem : {}", developingSystem);
    if (developingSystem.getId() != null) {
      throw new BadRequestAlertException(
          "A new developingSystem cannot already have an ID", ENTITY_NAME, "idexists");
    }
    DevelopingSystem result = developingSystemRepository.save(developingSystem);
    return ResponseEntity.created(new URI("/api/developing-systems/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /developing-systems} : Updates an existing developingSystem.
   *
   * @param developingSystem the developingSystem to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     developingSystem, or with status {@code 400 (Bad Request)} if the developingSystem is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the developingSystem couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/developing-systems")
  public ResponseEntity<DevelopingSystem> updateDevelopingSystem(
      @RequestBody DevelopingSystem developingSystem) throws URISyntaxException {
    log.debug("REST request to update DevelopingSystem : {}", developingSystem);
    if (developingSystem.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    DevelopingSystem result = developingSystemRepository.save(developingSystem);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, developingSystem.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /developing-systems} : get all the developingSystems.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     developingSystems in body.
   */
  @GetMapping("/developing-systems")
  public List<DevelopingSystem> getAllDevelopingSystems() {
    log.debug("REST request to get all DevelopingSystems");
    return developingSystemRepository.findAll();
  }

  /**
   * {@code GET /developing-systems/:id} : get the "id" developingSystem.
   *
   * @param id the id of the developingSystem to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     developingSystem, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/developing-systems/{id}")
  public ResponseEntity<DevelopingSystem> getDevelopingSystem(@PathVariable Long id) {
    log.debug("REST request to get DevelopingSystem : {}", id);
    Optional<DevelopingSystem> developingSystem = developingSystemRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(developingSystem);
  }

  /**
   * {@code DELETE /developing-systems/:id} : delete the "id" developingSystem.
   *
   * @param id the id of the developingSystem to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/developing-systems/{id}")
  public ResponseEntity<Void> deleteDevelopingSystem(@PathVariable Long id) {
    log.debug("REST request to delete DevelopingSystem : {}", id);
    developingSystemRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
