package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.repository.ShareableRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Shareable}. */
@RestController
@RequestMapping("/api")
public class ShareableResource {

  private final Logger log = LoggerFactory.getLogger(ShareableResource.class);

  private static final String ENTITY_NAME = "shareable";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ShareableRepository shareableRepository;

  public ShareableResource(ShareableRepository shareableRepository) {
    this.shareableRepository = shareableRepository;
  }

  /**
   * {@code POST /shareables} : Create a new shareable.
   *
   * @param shareable the shareable to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     shareable, or with status {@code 400 (Bad Request)} if the shareable has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/shareables")
  public ResponseEntity<Shareable> createShareable(@RequestBody Shareable shareable)
      throws URISyntaxException {
    log.debug("REST request to save Shareable : {}", shareable);
    if (shareable.getId() != null) {
      throw new BadRequestAlertException(
          "A new shareable cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Shareable result = shareableRepository.save(shareable);
    return ResponseEntity.created(new URI("/api/shareables/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /shareables} : Updates an existing shareable.
   *
   * @param shareable the shareable to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     shareable, or with status {@code 400 (Bad Request)} if the shareable is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the shareable couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/shareables")
  public ResponseEntity<Shareable> updateShareable(@RequestBody Shareable shareable)
      throws URISyntaxException {
    log.debug("REST request to update Shareable : {}", shareable);
    if (shareable.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Shareable result = shareableRepository.save(shareable);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, shareable.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /shareables} : get all the shareables.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareables in
   *     body.
   */
  @GetMapping("/shareables")
  public List<Shareable> getAllShareables() {
    log.debug("REST request to get all Shareables");
    return shareableRepository.findAll();
  }

  /**
   * {@code GET /shareables/:id} : get the "id" shareable.
   *
   * @param id the id of the shareable to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareable, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/shareables/{id}")
  public ResponseEntity<Shareable> getShareable(@PathVariable Long id) {
    log.debug("REST request to get Shareable : {}", id);
    Optional<Shareable> shareable = shareableRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(shareable);
  }

  /**
   * {@code DELETE /shareables/:id} : delete the "id" shareable.
   *
   * @param id the id of the shareable to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/shareables/{id}")
  public ResponseEntity<Void> deleteShareable(@PathVariable Long id) {
    log.debug("REST request to delete Shareable : {}", id);
    shareableRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
