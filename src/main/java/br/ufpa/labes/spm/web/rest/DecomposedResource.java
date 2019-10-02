package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.repository.DecomposedRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Decomposed}. */
@RestController
@RequestMapping("/api")
public class DecomposedResource {

  private final Logger log = LoggerFactory.getLogger(DecomposedResource.class);

  private static final String ENTITY_NAME = "decomposed";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final DecomposedRepository decomposedRepository;

  public DecomposedResource(DecomposedRepository decomposedRepository) {
    this.decomposedRepository = decomposedRepository;
  }

  /**
   * {@code POST /decomposeds} : Create a new decomposed.
   *
   * @param decomposed the decomposed to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     decomposed, or with status {@code 400 (Bad Request)} if the decomposed has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/decomposeds")
  public ResponseEntity<Decomposed> createDecomposed(@RequestBody Decomposed decomposed)
      throws URISyntaxException {
    log.debug("REST request to save Decomposed : {}", decomposed);
    if (decomposed.getId() != null) {
      throw new BadRequestAlertException(
          "A new decomposed cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Decomposed result = decomposedRepository.save(decomposed);
    return ResponseEntity.created(new URI("/api/decomposeds/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /decomposeds} : Updates an existing decomposed.
   *
   * @param decomposed the decomposed to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     decomposed, or with status {@code 400 (Bad Request)} if the decomposed is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the decomposed couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/decomposeds")
  public ResponseEntity<Decomposed> updateDecomposed(@RequestBody Decomposed decomposed)
      throws URISyntaxException {
    log.debug("REST request to update Decomposed : {}", decomposed);
    if (decomposed.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Decomposed result = decomposedRepository.save(decomposed);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, decomposed.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /decomposeds} : get all the decomposeds.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decomposeds in
   *     body.
   */
  @GetMapping("/decomposeds")
  public List<Decomposed> getAllDecomposeds() {
    log.debug("REST request to get all Decomposeds");
    return decomposedRepository.findAll();
  }

  /**
   * {@code GET /decomposeds/:id} : get the "id" decomposed.
   *
   * @param id the id of the decomposed to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decomposed,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/decomposeds/{id}")
  public ResponseEntity<Decomposed> getDecomposed(@PathVariable Long id) {
    log.debug("REST request to get Decomposed : {}", id);
    Optional<Decomposed> decomposed = decomposedRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(decomposed);
  }

  /**
   * {@code DELETE /decomposeds/:id} : delete the "id" decomposed.
   *
   * @param id the id of the decomposed to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/decomposeds/{id}")
  public ResponseEntity<Void> deleteDecomposed(@PathVariable Long id) {
    log.debug("REST request to delete Decomposed : {}", id);
    decomposedRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
