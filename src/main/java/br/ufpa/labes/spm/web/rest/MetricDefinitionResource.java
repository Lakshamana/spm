package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.MetricDefinition;
import br.ufpa.labes.spm.repository.MetricDefinitionRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.MetricDefinition}. */
@RestController
@RequestMapping("/api")
public class MetricDefinitionResource {

  private final Logger log = LoggerFactory.getLogger(MetricDefinitionResource.class);

  private static final String ENTITY_NAME = "metricDefinition";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final MetricDefinitionRepository metricDefinitionRepository;

  public MetricDefinitionResource(MetricDefinitionRepository metricDefinitionRepository) {
    this.metricDefinitionRepository = metricDefinitionRepository;
  }

  /**
   * {@code POST /metric-definitions} : Create a new metricDefinition.
   *
   * @param metricDefinition the metricDefinition to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     metricDefinition, or with status {@code 400 (Bad Request)} if the metricDefinition has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/metric-definitions")
  public ResponseEntity<MetricDefinition> createMetricDefinition(
      @RequestBody MetricDefinition metricDefinition) throws URISyntaxException {
    log.debug("REST request to save MetricDefinition : {}", metricDefinition);
    if (metricDefinition.getId() != null) {
      throw new BadRequestAlertException(
          "A new metricDefinition cannot already have an ID", ENTITY_NAME, "idexists");
    }
    MetricDefinition result = metricDefinitionRepository.save(metricDefinition);
    return ResponseEntity.created(new URI("/api/metric-definitions/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /metric-definitions} : Updates an existing metricDefinition.
   *
   * @param metricDefinition the metricDefinition to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     metricDefinition, or with status {@code 400 (Bad Request)} if the metricDefinition is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the metricDefinition couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/metric-definitions")
  public ResponseEntity<MetricDefinition> updateMetricDefinition(
      @RequestBody MetricDefinition metricDefinition) throws URISyntaxException {
    log.debug("REST request to update MetricDefinition : {}", metricDefinition);
    if (metricDefinition.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    MetricDefinition result = metricDefinitionRepository.save(metricDefinition);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, metricDefinition.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /metric-definitions} : get all the metricDefinitions.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     metricDefinitions in body.
   */
  @GetMapping("/metric-definitions")
  public List<MetricDefinition> getAllMetricDefinitions() {
    log.debug("REST request to get all MetricDefinitions");
    return metricDefinitionRepository.findAll();
  }

  /**
   * {@code GET /metric-definitions/:id} : get the "id" metricDefinition.
   *
   * @param id the id of the metricDefinition to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     metricDefinition, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/metric-definitions/{id}")
  public ResponseEntity<MetricDefinition> getMetricDefinition(@PathVariable Long id) {
    log.debug("REST request to get MetricDefinition : {}", id);
    Optional<MetricDefinition> metricDefinition = metricDefinitionRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(metricDefinition);
  }

  /**
   * {@code DELETE /metric-definitions/:id} : delete the "id" metricDefinition.
   *
   * @param id the id of the metricDefinition to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/metric-definitions/{id}")
  public ResponseEntity<Void> deleteMetricDefinition(@PathVariable Long id) {
    log.debug("REST request to delete MetricDefinition : {}", id);
    metricDefinitionRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
