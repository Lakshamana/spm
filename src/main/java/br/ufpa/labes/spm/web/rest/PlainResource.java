package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.repository.PlainRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Plain}. */
@RestController
@RequestMapping("/api")
public class PlainResource {

  private final Logger log = LoggerFactory.getLogger(PlainResource.class);

  private static final String ENTITY_NAME = "plain";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PlainRepository plainRepository;

  public PlainResource(PlainRepository plainRepository) {
    this.plainRepository = plainRepository;
  }

  /**
   * {@code POST /plains} : Create a new plain.
   *
   * @param plain the plain to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     plain, or with status {@code 400 (Bad Request)} if the plain has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/plains")
  public ResponseEntity<Plain> createPlain(@RequestBody Plain plain) throws URISyntaxException {
    log.debug("REST request to save Plain : {}", plain);
    if (plain.getId() != null) {
      throw new BadRequestAlertException(
          "A new plain cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Plain result = plainRepository.save(plain);
    return ResponseEntity.created(new URI("/api/plains/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /plains} : Updates an existing plain.
   *
   * @param plain the plain to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     plain, or with status {@code 400 (Bad Request)} if the plain is not valid, or with status
   *     {@code 500 (Internal Server Error)} if the plain couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/plains")
  public ResponseEntity<Plain> updatePlain(@RequestBody Plain plain) throws URISyntaxException {
    log.debug("REST request to update Plain : {}", plain);
    if (plain.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Plain result = plainRepository.save(plain);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, plain.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /plains} : get all the plains.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plains in body.
   */
  @GetMapping("/plains")
  public List<Plain> getAllPlains() {
    log.debug("REST request to get all Plains");
    return plainRepository.findAll();
  }

  /**
   * {@code GET /plains/:id} : get the "id" plain.
   *
   * @param id the id of the plain to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plain, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/plains/{id}")
  public ResponseEntity<Plain> getPlain(@PathVariable Long id) {
    log.debug("REST request to get Plain : {}", id);
    Optional<Plain> plain = plainRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(plain);
  }

  /**
   * {@code DELETE /plains/:id} : delete the "id" plain.
   *
   * @param id the id of the plain to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/plains/{id}")
  public ResponseEntity<Void> deletePlain(@PathVariable Long id) {
    log.debug("REST request to delete Plain : {}", id);
    plainRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
