package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ExclusiveService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ExclusiveDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Exclusive}. */
@RestController
@RequestMapping("/api")
public class ExclusiveResource {

  private final Logger log = LoggerFactory.getLogger(ExclusiveResource.class);

  private static final String ENTITY_NAME = "exclusive";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ExclusiveService exclusiveService;

  public ExclusiveResource(ExclusiveService exclusiveService) {
    this.exclusiveService = exclusiveService;
  }

  /**
   * {@code POST /exclusives} : Create a new exclusive.
   *
   * @param exclusiveDTO the exclusiveDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     exclusiveDTO, or with status {@code 400 (Bad Request)} if the exclusive has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/exclusives")
  public ResponseEntity<ExclusiveDTO> createExclusive(@RequestBody ExclusiveDTO exclusiveDTO)
      throws URISyntaxException {
    log.debug("REST request to save Exclusive : {}", exclusiveDTO);
    if (exclusiveDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new exclusive cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ExclusiveDTO result = exclusiveService.save(exclusiveDTO);
    return ResponseEntity.created(new URI("/api/exclusives/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /exclusives} : Updates an existing exclusive.
   *
   * @param exclusiveDTO the exclusiveDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     exclusiveDTO, or with status {@code 400 (Bad Request)} if the exclusiveDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the exclusiveDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/exclusives")
  public ResponseEntity<ExclusiveDTO> updateExclusive(@RequestBody ExclusiveDTO exclusiveDTO)
      throws URISyntaxException {
    log.debug("REST request to update Exclusive : {}", exclusiveDTO);
    if (exclusiveDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ExclusiveDTO result = exclusiveService.save(exclusiveDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, exclusiveDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /exclusives} : get all the exclusives.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exclusives in
   *     body.
   */
  @GetMapping("/exclusives")
  public List<ExclusiveDTO> getAllExclusives() {
    log.debug("REST request to get all Exclusives");
    return exclusiveService.findAll();
  }

  /**
   * {@code GET /exclusives/:id} : get the "id" exclusive.
   *
   * @param id the id of the exclusiveDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exclusiveDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/exclusives/{id}")
  public ResponseEntity<ExclusiveDTO> getExclusive(@PathVariable Long id) {
    log.debug("REST request to get Exclusive : {}", id);
    Optional<ExclusiveDTO> exclusiveDTO = exclusiveService.findOne(id);
    return ResponseUtil.wrapOrNotFound(exclusiveDTO);
  }

  /**
   * {@code DELETE /exclusives/:id} : delete the "id" exclusive.
   *
   * @param id the id of the exclusiveDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/exclusives/{id}")
  public ResponseEntity<Void> deleteExclusive(@PathVariable Long id) {
    log.debug("REST request to delete Exclusive : {}", id);
    exclusiveService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
