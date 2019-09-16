package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.MetricTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.MetricTypeDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.MetricType}. */
@RestController
@RequestMapping("/api")
public class MetricTypeResource {

  private final Logger log = LoggerFactory.getLogger(MetricTypeResource.class);

  private static final String ENTITY_NAME = "metricType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final MetricTypeService metricTypeService;

  public MetricTypeResource(MetricTypeService metricTypeService) {
    this.metricTypeService = metricTypeService;
  }

  /**
   * {@code POST /metric-types} : Create a new metricType.
   *
   * @param metricTypeDTO the metricTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     metricTypeDTO, or with status {@code 400 (Bad Request)} if the metricType has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/metric-types")
  public ResponseEntity<MetricTypeDTO> createMetricType(@RequestBody MetricTypeDTO metricTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to save MetricType : {}", metricTypeDTO);
    if (metricTypeDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new metricType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    MetricTypeDTO result = metricTypeService.save(metricTypeDTO);
    return ResponseEntity.created(new URI("/api/metric-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /metric-types} : Updates an existing metricType.
   *
   * @param metricTypeDTO the metricTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     metricTypeDTO, or with status {@code 400 (Bad Request)} if the metricTypeDTO is not valid,
   *     or with status {@code 500 (Internal Server Error)} if the metricTypeDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/metric-types")
  public ResponseEntity<MetricTypeDTO> updateMetricType(@RequestBody MetricTypeDTO metricTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to update MetricType : {}", metricTypeDTO);
    if (metricTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    MetricTypeDTO result = metricTypeService.save(metricTypeDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, metricTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /metric-types} : get all the metricTypes.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metricTypes in
   *     body.
   */
  @GetMapping("/metric-types")
  public List<MetricTypeDTO> getAllMetricTypes(@RequestParam(required = false) String filter) {
    if ("thetypesuper-is-null".equals(filter)) {
      log.debug("REST request to get all MetricTypes where theTypeSuper is null");
      return metricTypeService.findAllWhereTheTypeSuperIsNull();
    }
    log.debug("REST request to get all MetricTypes");
    return metricTypeService.findAll();
  }

  /**
   * {@code GET /metric-types/:id} : get the "id" metricType.
   *
   * @param id the id of the metricTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     metricTypeDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/metric-types/{id}")
  public ResponseEntity<MetricTypeDTO> getMetricType(@PathVariable Long id) {
    log.debug("REST request to get MetricType : {}", id);
    Optional<MetricTypeDTO> metricTypeDTO = metricTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(metricTypeDTO);
  }

  /**
   * {@code DELETE /metric-types/:id} : delete the "id" metricType.
   *
   * @param id the id of the metricTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/metric-types/{id}")
  public ResponseEntity<Void> deleteMetricType(@PathVariable Long id) {
    log.debug("REST request to delete MetricType : {}", id);
    metricTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
