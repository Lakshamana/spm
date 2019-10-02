package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.repository.ResourceMetricRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ResourceMetric}. */
@RestController
@RequestMapping("/api")
public class ResourceMetricResource {

  private final Logger log = LoggerFactory.getLogger(ResourceMetricResource.class);

  private static final String ENTITY_NAME = "resourceMetric";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ResourceMetricRepository resourceMetricRepository;

  public ResourceMetricResource(ResourceMetricRepository resourceMetricRepository) {
    this.resourceMetricRepository = resourceMetricRepository;
  }

  /**
   * {@code POST /resource-metrics} : Create a new resourceMetric.
   *
   * @param resourceMetric the resourceMetric to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     resourceMetric, or with status {@code 400 (Bad Request)} if the resourceMetric has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/resource-metrics")
  public ResponseEntity<ResourceMetric> createResourceMetric(
      @RequestBody ResourceMetric resourceMetric) throws URISyntaxException {
    log.debug("REST request to save ResourceMetric : {}", resourceMetric);
    if (resourceMetric.getId() != null) {
      throw new BadRequestAlertException(
          "A new resourceMetric cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ResourceMetric result = resourceMetricRepository.save(resourceMetric);
    return ResponseEntity.created(new URI("/api/resource-metrics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /resource-metrics} : Updates an existing resourceMetric.
   *
   * @param resourceMetric the resourceMetric to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     resourceMetric, or with status {@code 400 (Bad Request)} if the resourceMetric is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the resourceMetric couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/resource-metrics")
  public ResponseEntity<ResourceMetric> updateResourceMetric(
      @RequestBody ResourceMetric resourceMetric) throws URISyntaxException {
    log.debug("REST request to update ResourceMetric : {}", resourceMetric);
    if (resourceMetric.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ResourceMetric result = resourceMetricRepository.save(resourceMetric);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, resourceMetric.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /resource-metrics} : get all the resourceMetrics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceMetrics
   *     in body.
   */
  @GetMapping("/resource-metrics")
  public List<ResourceMetric> getAllResourceMetrics() {
    log.debug("REST request to get all ResourceMetrics");
    return resourceMetricRepository.findAll();
  }

  /**
   * {@code GET /resource-metrics/:id} : get the "id" resourceMetric.
   *
   * @param id the id of the resourceMetric to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     resourceMetric, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/resource-metrics/{id}")
  public ResponseEntity<ResourceMetric> getResourceMetric(@PathVariable Long id) {
    log.debug("REST request to get ResourceMetric : {}", id);
    Optional<ResourceMetric> resourceMetric = resourceMetricRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(resourceMetric);
  }

  /**
   * {@code DELETE /resource-metrics/:id} : delete the "id" resourceMetric.
   *
   * @param id the id of the resourceMetric to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/resource-metrics/{id}")
  public ResponseEntity<Void> deleteResourceMetric(@PathVariable Long id) {
    log.debug("REST request to delete ResourceMetric : {}", id);
    resourceMetricRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
