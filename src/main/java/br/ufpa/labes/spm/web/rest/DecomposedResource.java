package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.DecomposedService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.DecomposedDTO;

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

  private final DecomposedService decomposedService;

  public DecomposedResource(DecomposedService decomposedService) {
    this.decomposedService = decomposedService;
  }

  /**
   * {@code POST /decomposed-activities} : Create a new decomposed.
   *
   * @param decomposedDTO the decomposedDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     decomposedDTO, or with status {@code 400 (Bad Request)} if the decomposed has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/decomposed-activities")
  public ResponseEntity<DecomposedDTO> createDecomposed(@RequestBody DecomposedDTO decomposedDTO)
      throws URISyntaxException {
    log.debug("REST request to save Decomposed : {}", decomposedDTO);
    if (decomposedDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new decomposed cannot already have an ID", ENTITY_NAME, "idexists");
    }
    DecomposedDTO result = decomposedService.save(decomposedDTO);
    return ResponseEntity.created(new URI("/api/decomposed-activities/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /decomposed-activities} : Updates an existing decomposed.
   *
   * @param decomposedDTO the decomposedDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     decomposedDTO, or with status {@code 400 (Bad Request)} if the decomposedDTO is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the decomposedDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/decomposed-activities")
  public ResponseEntity<DecomposedDTO> updateDecomposed(@RequestBody DecomposedDTO decomposedDTO)
      throws URISyntaxException {
    log.debug("REST request to update Decomposed : {}", decomposedDTO);
    if (decomposedDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    DecomposedDTO result = decomposedService.save(decomposedDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, decomposedDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /decomposed-activities} : get all the decomposedActivities.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     decomposedActivities in body.
   */
  @GetMapping("/decomposed-activities")
  public List<DecomposedDTO> getAllDecomposedActivities(
      @RequestParam(required = false) String filter) {
    if ("theactivitysuper-is-null".equals(filter)) {
      log.debug("REST request to get all Decomposeds where theActivitySuper is null");
      return decomposedService.findAllWhereTheActivitySuperIsNull();
    }
    log.debug("REST request to get all DecomposedActivities");
    return decomposedService.findAll();
  }

  /**
   * {@code GET /decomposed-activities/:id} : get the "id" decomposed.
   *
   * @param id the id of the decomposedDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     decomposedDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/decomposed-activities/{id}")
  public ResponseEntity<DecomposedDTO> getDecomposed(@PathVariable Long id) {
    log.debug("REST request to get Decomposed : {}", id);
    Optional<DecomposedDTO> decomposedDTO = decomposedService.findOne(id);
    return ResponseUtil.wrapOrNotFound(decomposedDTO);
  }

  /**
   * {@code DELETE /decomposed-activities/:id} : delete the "id" decomposed.
   *
   * @param id the id of the decomposedDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/decomposed-activities/{id}")
  public ResponseEntity<Void> deleteDecomposed(@PathVariable Long id) {
    log.debug("REST request to delete Decomposed : {}", id);
    decomposedService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
