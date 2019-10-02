package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.SimpleCon;
import br.ufpa.labes.spm.repository.SimpleConRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.SimpleCon}. */
@RestController
@RequestMapping("/api")
public class SimpleConResource {

  private final Logger log = LoggerFactory.getLogger(SimpleConResource.class);

  private static final String ENTITY_NAME = "simpleCon";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SimpleConRepository simpleConRepository;

  public SimpleConResource(SimpleConRepository simpleConRepository) {
    this.simpleConRepository = simpleConRepository;
  }

  /**
   * {@code POST /simple-cons} : Create a new simpleCon.
   *
   * @param simpleCon the simpleCon to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     simpleCon, or with status {@code 400 (Bad Request)} if the simpleCon has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/simple-cons")
  public ResponseEntity<SimpleCon> createSimpleCon(@RequestBody SimpleCon simpleCon)
      throws URISyntaxException {
    log.debug("REST request to save SimpleCon : {}", simpleCon);
    if (simpleCon.getId() != null) {
      throw new BadRequestAlertException(
          "A new simpleCon cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SimpleCon result = simpleConRepository.save(simpleCon);
    return ResponseEntity.created(new URI("/api/simple-cons/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /simple-cons} : Updates an existing simpleCon.
   *
   * @param simpleCon the simpleCon to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     simpleCon, or with status {@code 400 (Bad Request)} if the simpleCon is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the simpleCon couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/simple-cons")
  public ResponseEntity<SimpleCon> updateSimpleCon(@RequestBody SimpleCon simpleCon)
      throws URISyntaxException {
    log.debug("REST request to update SimpleCon : {}", simpleCon);
    if (simpleCon.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    SimpleCon result = simpleConRepository.save(simpleCon);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, simpleCon.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /simple-cons} : get all the simpleCons.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of simpleCons in
   *     body.
   */
  @GetMapping("/simple-cons")
  public List<SimpleCon> getAllSimpleCons() {
    log.debug("REST request to get all SimpleCons");
    return simpleConRepository.findAll();
  }

  /**
   * {@code GET /simple-cons/:id} : get the "id" simpleCon.
   *
   * @param id the id of the simpleCon to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the simpleCon, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/simple-cons/{id}")
  public ResponseEntity<SimpleCon> getSimpleCon(@PathVariable Long id) {
    log.debug("REST request to get SimpleCon : {}", id);
    Optional<SimpleCon> simpleCon = simpleConRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(simpleCon);
  }

  /**
   * {@code DELETE /simple-cons/:id} : delete the "id" simpleCon.
   *
   * @param id the id of the simpleCon to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/simple-cons/{id}")
  public ResponseEntity<Void> deleteSimpleCon(@PathVariable Long id) {
    log.debug("REST request to delete SimpleCon : {}", id);
    simpleConRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
