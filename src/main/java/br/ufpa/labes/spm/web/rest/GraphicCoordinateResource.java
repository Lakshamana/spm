package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.GraphicCoordinateService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.GraphicCoordinateDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.GraphicCoordinate}. */
@RestController
@RequestMapping("/api")
public class GraphicCoordinateResource {

  private final Logger log = LoggerFactory.getLogger(GraphicCoordinateResource.class);

  private static final String ENTITY_NAME = "graphicCoordinate";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final GraphicCoordinateService graphicCoordinateService;

  public GraphicCoordinateResource(GraphicCoordinateService graphicCoordinateService) {
    this.graphicCoordinateService = graphicCoordinateService;
  }

  /**
   * {@code POST /graphic-coordinates} : Create a new graphicCoordinate.
   *
   * @param graphicCoordinateDTO the graphicCoordinateDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     graphicCoordinateDTO, or with status {@code 400 (Bad Request)} if the graphicCoordinate has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/graphic-coordinates")
  public ResponseEntity<GraphicCoordinateDTO> createGraphicCoordinate(
      @RequestBody GraphicCoordinateDTO graphicCoordinateDTO) throws URISyntaxException {
    log.debug("REST request to save GraphicCoordinate : {}", graphicCoordinateDTO);
    if (graphicCoordinateDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new graphicCoordinate cannot already have an ID", ENTITY_NAME, "idexists");
    }
    GraphicCoordinateDTO result = graphicCoordinateService.save(graphicCoordinateDTO);
    return ResponseEntity.created(new URI("/api/graphic-coordinates/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /graphic-coordinates} : Updates an existing graphicCoordinate.
   *
   * @param graphicCoordinateDTO the graphicCoordinateDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     graphicCoordinateDTO, or with status {@code 400 (Bad Request)} if the graphicCoordinateDTO
   *     is not valid, or with status {@code 500 (Internal Server Error)} if the
   *     graphicCoordinateDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/graphic-coordinates")
  public ResponseEntity<GraphicCoordinateDTO> updateGraphicCoordinate(
      @RequestBody GraphicCoordinateDTO graphicCoordinateDTO) throws URISyntaxException {
    log.debug("REST request to update GraphicCoordinate : {}", graphicCoordinateDTO);
    if (graphicCoordinateDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    GraphicCoordinateDTO result = graphicCoordinateService.save(graphicCoordinateDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, graphicCoordinateDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /graphic-coordinates} : get all the graphicCoordinates.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     graphicCoordinates in body.
   */
  @GetMapping("/graphic-coordinates")
  public List<GraphicCoordinateDTO> getAllGraphicCoordinates(
      @RequestParam(required = false) String filter) {
    if ("theobjectreference-is-null".equals(filter)) {
      log.debug("REST request to get all GraphicCoordinates where theObjectReference is null");
      return graphicCoordinateService.findAllWhereTheObjectReferenceIsNull();
    }
    log.debug("REST request to get all GraphicCoordinates");
    return graphicCoordinateService.findAll();
  }

  /**
   * {@code GET /graphic-coordinates/:id} : get the "id" graphicCoordinate.
   *
   * @param id the id of the graphicCoordinateDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     graphicCoordinateDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/graphic-coordinates/{id}")
  public ResponseEntity<GraphicCoordinateDTO> getGraphicCoordinate(@PathVariable Long id) {
    log.debug("REST request to get GraphicCoordinate : {}", id);
    Optional<GraphicCoordinateDTO> graphicCoordinateDTO = graphicCoordinateService.findOne(id);
    return ResponseUtil.wrapOrNotFound(graphicCoordinateDTO);
  }

  /**
   * {@code DELETE /graphic-coordinates/:id} : delete the "id" graphicCoordinate.
   *
   * @param id the id of the graphicCoordinateDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/graphic-coordinates/{id}")
  public ResponseEntity<Void> deleteGraphicCoordinate(@PathVariable Long id) {
    log.debug("REST request to delete GraphicCoordinate : {}", id);
    graphicCoordinateService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
