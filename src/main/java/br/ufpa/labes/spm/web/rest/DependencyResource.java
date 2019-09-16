package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.DependencyService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.DependencyDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Dependency}. */
@RestController
@RequestMapping("/api")
public class DependencyResource {

  private final Logger log = LoggerFactory.getLogger(DependencyResource.class);

  private static final String ENTITY_NAME = "dependency";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final DependencyService dependencyService;

  public DependencyResource(DependencyService dependencyService) {
    this.dependencyService = dependencyService;
  }

  /**
   * {@code POST /dependencies} : Create a new dependency.
   *
   * @param dependencyDTO the dependencyDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     dependencyDTO, or with status {@code 400 (Bad Request)} if the dependency has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/dependencies")
  public ResponseEntity<DependencyDTO> createDependency(@RequestBody DependencyDTO dependencyDTO)
      throws URISyntaxException {
    log.debug("REST request to save Dependency : {}", dependencyDTO);
    if (dependencyDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new dependency cannot already have an ID", ENTITY_NAME, "idexists");
    }
    DependencyDTO result = dependencyService.save(dependencyDTO);
    return ResponseEntity.created(new URI("/api/dependencies/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /dependencies} : Updates an existing dependency.
   *
   * @param dependencyDTO the dependencyDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     dependencyDTO, or with status {@code 400 (Bad Request)} if the dependencyDTO is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the dependencyDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/dependencies")
  public ResponseEntity<DependencyDTO> updateDependency(@RequestBody DependencyDTO dependencyDTO)
      throws URISyntaxException {
    log.debug("REST request to update Dependency : {}", dependencyDTO);
    if (dependencyDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    DependencyDTO result = dependencyService.save(dependencyDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, dependencyDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /dependencies} : get all the dependencies.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dependencies in
   *     body.
   */
  @GetMapping("/dependencies")
  public List<DependencyDTO> getAllDependencies() {
    log.debug("REST request to get all Dependencies");
    return dependencyService.findAll();
  }

  /**
   * {@code GET /dependencies/:id} : get the "id" dependency.
   *
   * @param id the id of the dependencyDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     dependencyDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/dependencies/{id}")
  public ResponseEntity<DependencyDTO> getDependency(@PathVariable Long id) {
    log.debug("REST request to get Dependency : {}", id);
    Optional<DependencyDTO> dependencyDTO = dependencyService.findOne(id);
    return ResponseUtil.wrapOrNotFound(dependencyDTO);
  }

  /**
   * {@code DELETE /dependencies/:id} : delete the "id" dependency.
   *
   * @param id the id of the dependencyDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/dependencies/{id}")
  public ResponseEntity<Void> deleteDependency(@PathVariable Long id) {
    log.debug("REST request to delete Dependency : {}", id);
    dependencyService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
