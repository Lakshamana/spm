package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.MetricDefinitionService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;

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

  private final MetricDefinitionService metricDefinitionService;

  public MetricDefinitionResource(MetricDefinitionService metricDefinitionService) {
    this.metricDefinitionService = metricDefinitionService;
  }

  /**
   * {@code POST /metric-definitions} : Create a new metricDefinition.
   *
   * @param metricDefinitionDTO the metricDefinitionDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     metricDefinitionDTO, or with status {@code 400 (Bad Request)} if the metricDefinition has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/metric-definitions")
  public ResponseEntity<MetricDefinitionDTO> createMetricDefinition(
      @RequestBody MetricDefinitionDTO metricDefinitionDTO) throws URISyntaxException {
    log.debug("REST request to save MetricDefinition : {}", metricDefinitionDTO);
    if (metricDefinitionDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new metricDefinition cannot already have an ID", ENTITY_NAME, "idexists");
    }
    MetricDefinitionDTO result = metricDefinitionService.save(metricDefinitionDTO);
    return ResponseEntity.created(new URI("/api/metric-definitions/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /metric-definitions} : Updates an existing metricDefinition.
   *
   * @param metricDefinitionDTO the metricDefinitionDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     metricDefinitionDTO, or with status {@code 400 (Bad Request)} if the metricDefinitionDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the metricDefinitionDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/metric-definitions")
  public ResponseEntity<MetricDefinitionDTO> updateMetricDefinition(
      @RequestBody MetricDefinitionDTO metricDefinitionDTO) throws URISyntaxException {
    log.debug("REST request to update MetricDefinition : {}", metricDefinitionDTO);
    if (metricDefinitionDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    MetricDefinitionDTO result = metricDefinitionService.save(metricDefinitionDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, metricDefinitionDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /metric-definitions} : get all the metricDefinitions.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     metricDefinitions in body.
   */
  @GetMapping("/metric-definitions")
  public List<MetricDefinitionDTO> getAllMetricDefinitions() {
    log.debug("REST request to get all MetricDefinitions");
    return metricDefinitionService.findAll();
  }

  /**
   * {@code GET /metric-definitions/:id} : get the "id" metricDefinition.
   *
   * @param id the id of the metricDefinitionDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     metricDefinitionDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/metric-definitions/{id}")
  public ResponseEntity<MetricDefinitionDTO> getMetricDefinition(@PathVariable Long id) {
    log.debug("REST request to get MetricDefinition : {}", id);
    Optional<MetricDefinitionDTO> metricDefinitionDTO = metricDefinitionService.findOne(id);
    return ResponseUtil.wrapOrNotFound(metricDefinitionDTO);
  }

  /**
   * {@code DELETE /metric-definitions/:id} : delete the "id" metricDefinition.
   *
   * @param id the id of the metricDefinitionDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/metric-definitions/{id}")
  public ResponseEntity<Void> deleteMetricDefinition(@PathVariable Long id) {
    log.debug("REST request to delete MetricDefinition : {}", id);
    metricDefinitionService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
